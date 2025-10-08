package grill24.adaptiveores.client.model;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import grill24.adaptiveores.AdaptiveOres;
import grill24.adaptiveores.foundation.OreType;
import grill24.adaptiveores.foundation.blockentity.AdaptiveOreBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.ChunkRenderTypeSet;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.data.ModelProperty;

/**
 * Dual-layer baked model for adaptive ores.
 * Renders backdrop material (from BlockEntity) + ore overlay (static texture).
 */
public class AdaptiveOreModel implements BakedModel {
    
    // ModelData property for storing backdrop material
    public static final ModelProperty<BlockState> BACKDROP_PROPERTY = 
        new ModelProperty<>();
    
    // Small offset to prevent z-fighting between backdrop and overlay
    // This value pushes overlay quads slightly outward from the block surface
    private static final float OVERLAY_OFFSET = 0.001f;

    private final TextureAtlasSprite overlaySprite;
    private final BakedModel overlayModel;
    private final OreType oreType;
    private final ItemTransforms itemTransforms;

    // RenderType sets for deciding which layer to emit in which pass
    private static final ChunkRenderTypeSet BACKDROP_TYPES = ChunkRenderTypeSet.of(RenderType.solid());
    // Allow overlay to be rendered in CUTOUT (binary alpha) or TRANSLUCENT (smooth alpha)
    // CUTOUT provides crisp, non-blended edges; TRANSLUCENT allows semi-transparent pixels
    private static final ChunkRenderTypeSet OVERLAY_TYPES = ChunkRenderTypeSet.of(RenderType.translucent());
    
    public AdaptiveOreModel(TextureAtlasSprite overlaySprite, BakedModel overlayModel, OreType oreType) {
        this.overlaySprite = overlaySprite;
        this.overlayModel = overlayModel;
        this.oreType = oreType;
        this.itemTransforms = overlayModel.getTransforms();
    }
    
    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, 
                                            @Nullable Direction side, 
                                            @NotNull RandomSource rand) {
        return getQuads(state, side, rand, ModelData.EMPTY, null);
    }
    
    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state,
                                            @Nullable Direction side,
                                            @NotNull RandomSource rand,
                                            @NotNull ModelData modelData,
                                            @Nullable RenderType renderType) {
        List<BakedQuad> quads = new ArrayList<>();
        
        // Get backdrop material from ModelData
        BlockState backdrop = modelData.get(BACKDROP_PROPERTY);
        if (backdrop == null) {
            backdrop = Blocks.STONE.defaultBlockState();
        }
        
        if (AdaptiveOres.RENDER_DEBUG && side != null) {
            // Use debug level to avoid spamming info logs during normal runs
            AdaptiveOres.LOGGER.debug("getQuads called: side={}, backdrop={}, renderType={}",
                side, backdrop.getBlock().getName().getString(), renderType);
        }
        
        // Layer 1: Render backdrop material (the base stone texture)
        // Skip backdrop entirely when running in overlay-only test mode
        if (!AdaptiveOres.OVERLAY_ONLY_TEST_MODE && (renderType == null || BACKDROP_TYPES.contains(renderType))) {
            BakedModel backdropModel = getBackdropModel(backdrop);
            if (backdropModel != null) {
                List<BakedQuad> backdropQuads = backdropModel.getQuads(backdrop, side, rand, ModelData.EMPTY, renderType);
                quads.addAll(backdropQuads);
                
                if (AdaptiveOres.RENDER_DEBUG && side != null) {
                    AdaptiveOres.LOGGER.debug("Added {} backdrop quads for side={}", backdropQuads.size(), side);
                }
            }
        }
        
        // Layer 2: Render ore overlay using our custom transparent overlay texture
        // Only add overlay in CUTOUT/CUTOUT_MIPPED/TRANSLUCENT render passes (for transparent textures)
        // Use the baked overlay model (cube_all with overlay texture) instead of manually creating quads
        // This ensures proper UV mapping and vertex winding order
        if (renderType == null || OVERLAY_TYPES.contains(renderType)) {
            if (overlayModel != null) {
                List<BakedQuad> overlayQuads = overlayModel.getQuads(state, side, rand, ModelData.EMPTY, renderType);

                // Apply offset to overlay quads to prevent z-fighting with backdrop
                for (BakedQuad quad : overlayQuads) {
                    quads.add(offsetQuad(quad, side));
                }

                if (AdaptiveOres.RENDER_DEBUG && side != null) {
                    AdaptiveOres.LOGGER.debug("Added {} offset overlay quads for side={}, renderType={}",
                        overlayQuads.size(), side, renderType);
                }
            }
        }

        if (AdaptiveOres.RENDER_DEBUG) {
            AdaptiveOres.LOGGER.debug("Returning {} total quads (backdrop + overlay) for side={}", quads.size(), side);
        }
        
        return quads;
    }
    
    /**
     * Offsets a quad slightly outward along its face normal to prevent z-fighting.
     * This creates a small gap between the backdrop and overlay layers.
     *
     * @param quad The original quad to offset
     * @param side The face direction (can be null for non-culled quads)
     * @return A new BakedQuad with offset vertex positions
     */
    private BakedQuad offsetQuad(BakedQuad quad, @Nullable Direction side) {
        // If no direction is specified, use the quad's own direction
        Direction direction = side != null ? side : quad.getDirection();

        // Get the vertex data from the original quad
        int[] originalVertices = quad.getVertices();
        int[] newVertices = originalVertices.clone();

        // Calculate offset vector based on face normal
        float offsetX = 0, offsetY = 0, offsetZ = 0;
        if (direction != null) {
            offsetX = direction.getStepX() * OVERLAY_OFFSET;
            offsetY = direction.getStepY() * OVERLAY_OFFSET;
            offsetZ = direction.getStepZ() * OVERLAY_OFFSET;
        }

        // The vertex format for block quads is BLOCK (position, color, uv, lightmap, normal)
        // getVertexSize() returns bytes, divide by 4 to get integer count
        // Each vertex uses 8 integers (32 bytes): position(3 floats=3 ints), color(1 int), uv(2 floats=2 ints), lightmap(1 int), normal(1 int)
        int vertexSize = DefaultVertexFormat.BLOCK.getVertexSize() / 4;

        for (int vertex = 0; vertex < 4; vertex++) {
            int baseIndex = vertex * vertexSize;

            // Get current position (stored as floats in int array)
            float x = Float.intBitsToFloat(newVertices[baseIndex]);
            float y = Float.intBitsToFloat(newVertices[baseIndex + 1]);
            float z = Float.intBitsToFloat(newVertices[baseIndex + 2]);

            // Apply offset
            x += offsetX;
            y += offsetY;
            z += offsetZ;

            // Store back as int bits
            newVertices[baseIndex] = Float.floatToRawIntBits(x);
            newVertices[baseIndex + 1] = Float.floatToRawIntBits(y);
            newVertices[baseIndex + 2] = Float.floatToRawIntBits(z);
        }

        // Create new quad with offset vertices
        return new BakedQuad(
            newVertices,
            quad.getTintIndex(),
            quad.getDirection(),
            quad.getSprite(),
            quad.isShade()
        );
    }

    /**
     * Get the baked model for the backdrop material
     */
    private BakedModel getBackdropModel(BlockState backdrop) {
        return Minecraft.getInstance()
            .getBlockRenderer()
            .getBlockModel(backdrop);
    }
    
    @Override
    public @NotNull ModelData getModelData(@NotNull BlockAndTintGetter level,
                                          @NotNull BlockPos pos,
                                          @NotNull BlockState state,
                                          @NotNull ModelData modelData) {
        // Extract backdrop from BlockEntity
        if (level.getBlockEntity(pos) instanceof AdaptiveOreBlockEntity oreEntity) {
            BlockState backdrop = oreEntity.getBackdropMaterial();
            return modelData.derive()
                .with(BACKDROP_PROPERTY, backdrop)
                .build();
        }
        
        return modelData;
    }
    
    @Override
    public boolean useAmbientOcclusion() {
        return true;
    }
    
    @Override
    public boolean isGui3d() {
        return true;
    }
    
    @Override
    public boolean usesBlockLight() {
        return true;
    }
    
    @Override
    public boolean isCustomRenderer() {
        return false;
    }
    
    @Override
    public TextureAtlasSprite getParticleIcon() {
        return overlaySprite;
    }
    
    @Override
    public TextureAtlasSprite getParticleIcon(@NotNull ModelData data) {
        return overlaySprite;
    }
    
    @Override
    public net.neoforged.neoforge.client.ChunkRenderTypeSet getRenderTypes(@NotNull BlockState state,
                                                                           @NotNull RandomSource rand,
                                                                           @NotNull ModelData data) {
        // Use SOLID for backdrop and allow both CUTOUT and TRANSLUCENT for overlay
        // CUTOUT: binary alpha (no blending), TRANSLUCENT: smooth alpha blending
        return net.neoforged.neoforge.client.ChunkRenderTypeSet.of(
            RenderType.solid(),
            RenderType.cutout(),
            RenderType.translucent()
        );
    }
    
    @Override
    public ItemOverrides getOverrides() {
        return ItemOverrides.EMPTY;
    }

    @Override
    public ItemTransforms getTransforms() {
        // Return the item transforms from the overlay model (cube_all)
        // This provides proper GUI/firstperson/thirdperson display settings
        return itemTransforms;
    }
}
