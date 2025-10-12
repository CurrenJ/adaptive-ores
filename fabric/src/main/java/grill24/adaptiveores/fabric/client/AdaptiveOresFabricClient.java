package grill24.adaptiveores.fabric.client;

import grill24.adaptiveores.AdaptiveOres;
import grill24.adaptiveores.foundation.AdaptiveOreBlocks;
import grill24.adaptiveores.platform.BlockRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.RenderType;

public final class AdaptiveOresFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Initialize Adaptive Ores client-side rendering
        AdaptiveOres.LOGGER.info("Initializing Adaptive Ores Fabric Client");

        // Register render layers for overlay rendering (use cutout mipped like vanilla ores)
        registerBlockRenderLayers();
        registerAdaptiveOresTints();

        // Register model loading plugin
        AdaptiveOreModelLoadingPlugin.register();

        AdaptiveOres.LOGGER.info("Adaptive Ores Fabric Client initialization complete");
    }

    private static void registerBlockRenderLayers() {
        for (net.minecraft.world.level.block.Block block : AdaptiveOreBlocks.getAllAdaptiveOres()) {
            BlockRenderLayerMap.INSTANCE.putBlock(block, RenderType.translucent());
        }
    }

    private void registerAdaptiveOresTints() {
        BlockColor colorProvider = (state, world, pos, tintIndex) -> {
            if (world != null && pos != null) {
                return BiomeColors.getAverageGrassColor(world, pos);
            } else {
                return 0x91BD59; // Default grass color
            }
        };

        for (net.minecraft.world.level.block.Block block : AdaptiveOreBlocks.getAllAdaptiveOres()) {
            ColorProviderRegistry.BLOCK.register(colorProvider, block);
        }
    }
}
