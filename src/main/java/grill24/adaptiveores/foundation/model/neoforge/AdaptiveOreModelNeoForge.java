package grill24.adaptiveores.foundation.model.neoforge;

import grill24.adaptiveores.content.block.AdaptiveOreBlock;
import grill24.adaptiveores.foundation.blockentity.AdaptiveOreBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import grill24.adaptiveores.AdaptiveOres;

import java.util.ArrayList;
import java.util.List;

/**
 * NeoForge model wrapper for adaptive ore blocks.
 * Implements dual-layer rendering with backdrop + overlay.
 * Simplified version for initial implementation.
 */
public class AdaptiveOreModelNeoForge implements BakedModel {
    
    private final BakedModel originalModel;
    public static final ModelProperty<BlockState> BACKDROP_PROPERTY = new ModelProperty<>();

    public AdaptiveOreModelNeoForge(BakedModel originalModel) {
        this.originalModel = originalModel;
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction direction, @NotNull RandomSource random) {
        // Default to the ModelData-less path which delegates to the richer method
        return getQuads(state, direction, random, ModelData.EMPTY, null);
    }

    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state,
                                            @Nullable Direction direction,
                                            @NotNull RandomSource random,
                                            @NotNull ModelData data,
                                            @Nullable RenderType renderType) {
        List<BakedQuad> quads = new ArrayList<>();

        // Determine backdrop material from ModelData first
        BlockState backdrop = data.get(BACKDROP_PROPERTY);
        if (backdrop == null) backdrop = Blocks.STONE.defaultBlockState();

        // Layer 1: backdrop model quads
        BakedModel backdropModel = getBackdropModel(backdrop);
        if (backdropModel != null) {
            List<BakedQuad> backdropQuads = backdropModel.getQuads(backdrop, direction, random);
            if (AdaptiveOres.RENDER_DEBUG) {
                // Debug-level to reduce high-volume logging during rendering
                AdaptiveOres.LOGGER.debug("AdaptiveOreModel: backdrop={} quads={}", backdrop.getBlock(), backdropQuads.size());
            }
            quads.addAll(backdropQuads);
        }

        // Layer 2: overlay from the original model
        // We assume the original model contains the overlay quads (item model / block model)
        List<BakedQuad> overlayQuads = originalModel.getQuads(state, direction, random);
        if (overlayQuads != null && !overlayQuads.isEmpty()) {
            if (AdaptiveOres.RENDER_DEBUG) {
                AdaptiveOres.LOGGER.debug("AdaptiveOreModel: overlay quads={}", overlayQuads.size());
            }
            quads.addAll(overlayQuads);
        }

        return quads;
    }

    @Override
    public boolean useAmbientOcclusion() {
        return originalModel.useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return originalModel.isGui3d();
    }

    @Override
    public boolean usesBlockLight() {
        return originalModel.usesBlockLight();
    }

    @Override
    public boolean isCustomRenderer() {
        return false;
    }

    @Override
    public @NotNull TextureAtlasSprite getParticleIcon() {
        return originalModel.getParticleIcon();
    }

    @Override
    public @NotNull ItemOverrides getOverrides() {
        return originalModel.getOverrides();
    }

    /**
     * Provide ModelData from the BlockEntity so NeoForge can pass the backdrop material.
     */
    public @NotNull ModelData getModelData(@NotNull BlockAndTintGetter world, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ModelData blockEntityData) {
        if (blockEntityData != null && blockEntityData.get(BACKDROP_PROPERTY) != null) return blockEntityData;

        if (world.getBlockEntity(pos) instanceof AdaptiveOreBlockEntity be) {
            BlockState backdrop = be.getBackdropMaterial();
            return blockEntityData.derive().with(BACKDROP_PROPERTY, backdrop).build();
        }

        return blockEntityData;
    }

    private BakedModel getBackdropModel(BlockState backdrop) {
        try {
            return Minecraft.getInstance().getBlockRenderer().getBlockModel(backdrop);
        } catch (Exception e) {
            return null;
        }
    }
}