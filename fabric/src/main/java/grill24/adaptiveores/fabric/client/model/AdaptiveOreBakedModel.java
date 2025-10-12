package grill24.adaptiveores.fabric.client.model;

import grill24.adaptiveores.AdaptiveOres;
import grill24.adaptiveores.foundation.OreType;
import grill24.adaptiveores.foundation.blockentity.AdaptiveOreBlockEntity;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.material.BlendMode;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Fabric baked model for adaptive ore blocks.
 * Implements FabricBakedModel to support custom rendering with access to BlockEntity data.
 */
public class AdaptiveOreBakedModel implements BakedModel, FabricBakedModel {

    // Small offset to prevent z-fighting between backdrop and overlay
    private static final float OVERLAY_OFFSET = 0.0025f;

    // Translucent material for overlay quads
    private static final RenderMaterial OVERLAY_MATERIAL;

    static {
        var renderer = RendererAccess.INSTANCE.getRenderer();
        if (renderer != null) {
            OVERLAY_MATERIAL = renderer.materialFinder()
                .blendMode(0, BlendMode.TRANSLUCENT)
                .disableDiffuse(0, true)
                .find();
            if (AdaptiveOres.RENDER_DEBUG) {
                AdaptiveOres.LOGGER.info("AdaptiveOreBakedModel: Initialized cutout_mipped overlay material");
            }
        } else {
            OVERLAY_MATERIAL = null;
            AdaptiveOres.LOGGER.warn("AdaptiveOreBakedModel: Fabric Renderer not available; overlay may not render correctly");
        }
    }

    private final TextureAtlasSprite overlaySprite;
    private final BakedModel overlayModel;
    private final OreType oreType;
    private final ItemTransforms itemTransforms;

    public AdaptiveOreBakedModel(TextureAtlasSprite overlaySprite, BakedModel overlayModel, OreType oreType) {
        this.overlaySprite = overlaySprite;
        this.overlayModel = overlayModel;
        this.oreType = oreType;
        this.itemTransforms = overlayModel != null ? overlayModel.getTransforms() : ItemTransforms.NO_TRANSFORMS;
    }

    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public void emitBlockQuads(BlockAndTintGetter blockView, BlockState state, BlockPos pos,
                               Supplier<RandomSource> randomSupplier, RenderContext context) {
        BlockState backdrop = Blocks.STONE.defaultBlockState();

        if (blockView.getBlockEntity(pos) instanceof AdaptiveOreBlockEntity oreEntity) {
            backdrop = oreEntity.getBackdropMaterial();
        }

        if (AdaptiveOres.RENDER_DEBUG) {
            AdaptiveOres.LOGGER.info("emitBlockQuads: pos={}, backdrop={}", pos, backdrop.getBlock().getName().getString());
        }

        if (!AdaptiveOres.OVERLAY_ONLY_TEST_MODE) {
            BakedModel backdropModel = getBackdropModel(backdrop);
            if (backdropModel != null) {
                if (backdropModel instanceof FabricBakedModel fabricBackdrop) {
                    fabricBackdrop.emitBlockQuads(blockView, backdrop, pos, randomSupplier, context);
                } else {
                    emitVanillaQuads(backdropModel, backdrop, randomSupplier.get(), context);
                }
            }
        }

        // Overlay model
        BakedModel overlayModel = this.overlayModel;
        if (AdaptiveOres.RENDER_DEBUG) {
            if (overlayModel == null) {
                AdaptiveOres.LOGGER.info("emitBlockQuads: Overlay model is null for ore type {}", oreType.getName());
            }
        }
        // Render overlay as a transparent layer atop the backdrop
        if (overlayModel != null && OVERLAY_MATERIAL != null) {
            if (overlayModel instanceof FabricBakedModel fabricOverlay) {
                fabricOverlay.emitBlockQuads(blockView, state, pos, randomSupplier, context);
            } else {
                emitVanillaQuads(overlayModel, state, randomSupplier.get(), context);
            }
        }
    }

    @Override
    public void emitItemQuads(ItemStack stack, Supplier<RandomSource> randomSupplier, RenderContext context) {
        BlockState defaultBackdrop = Blocks.STONE.defaultBlockState();
        BakedModel backdropModel = getBackdropModel(defaultBackdrop);

        if (backdropModel != null) {
            if (backdropModel instanceof FabricBakedModel fabricBackdrop) {
                fabricBackdrop.emitItemQuads(stack, randomSupplier, context);
            } else {
                emitVanillaQuads(backdropModel, defaultBackdrop, randomSupplier.get(), context);
            }
        }

        if (overlayModel != null) {
            if (overlayModel instanceof FabricBakedModel fabricOverlay) {
                fabricOverlay.emitItemQuads(stack, randomSupplier, context);
            } else {
                emitVanillaQuads(overlayModel, defaultBackdrop, randomSupplier.get(), context);
            }
        }
    }

    private void emitVanillaQuads(BakedModel model, BlockState state, RandomSource random, RenderContext context) {
        for (Direction direction : Direction.values()) {
            List<BakedQuad> quads = model.getQuads(state, direction, random);
            for (BakedQuad quad : quads) {
                context.getEmitter().fromVanilla(quad, null, direction);
            }
        }

        List<BakedQuad> generalQuads = model.getQuads(state, null, random);
        for (BakedQuad quad : generalQuads) {
            context.getEmitter().fromVanilla(quad, null, quad.getDirection());
        }
    }

    private BakedModel getBackdropModel(BlockState backdrop) {
        return Minecraft.getInstance().getBlockRenderer().getBlockModel(backdrop);
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand) {
        List<BakedQuad> quads = new ArrayList<>();

        BlockState backdrop = Blocks.STONE.defaultBlockState();
        BakedModel backdropModel = getBackdropModel(backdrop);

        if (backdropModel != null && !AdaptiveOres.OVERLAY_ONLY_TEST_MODE) {
            quads.addAll(backdropModel.getQuads(backdrop, side, rand));
        }

        if (overlayModel != null) {
            List<BakedQuad> overlayQuads = overlayModel.getQuads(state, side, rand);
            for (BakedQuad quad : overlayQuads) {
                quads.add(QuadHelper.offsetQuad(quad, side, OVERLAY_OFFSET));
            }
        }

        return quads;
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
    public ItemOverrides getOverrides() {
        return ItemOverrides.EMPTY;
    }

    @Override
    public ItemTransforms getTransforms() {
        return itemTransforms;
    }
}
