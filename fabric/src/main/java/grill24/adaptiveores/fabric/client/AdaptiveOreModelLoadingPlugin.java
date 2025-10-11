package grill24.adaptiveores.fabric.client;

import grill24.adaptiveores.AdaptiveOres;
import grill24.adaptiveores.foundation.OreType;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.resources.ResourceLocation;

/**
 * Fabric model loading plugin for adaptive ore custom models.
 * This intercepts model loading and provides custom models for adaptive ore blocks.
 */
public class AdaptiveOreModelLoadingPlugin {

    public static void register() {
        ModelLoadingPlugin.register(pluginContext -> {
            AdaptiveOres.LOGGER.info("Registering Adaptive Ore model loading plugin");

            pluginContext.resolveModel().register(context -> {
                ResourceLocation id = context.id();

                // Only handle our adaptive ore models (block OR item)
                if (!id.getNamespace().equals(AdaptiveOres.MOD_ID)) {
                    return null;
                }

                String path = id.getPath();

                // Support both block and item model IDs like:
                //  - block/adaptive_<type>_ore
                //  - item/adaptive_<type>_ore
                boolean isAdaptiveBlock = path.startsWith("block/adaptive_") && path.endsWith("_ore");
                boolean isAdaptiveItem = path.startsWith("item/adaptive_") && path.endsWith("_ore");

                if (isAdaptiveBlock || isAdaptiveItem) {
                    // Determine the prefix length to strip (either "block/adaptive_" or "item/adaptive_")
                    String prefix = isAdaptiveBlock ? "block/adaptive_" : "item/adaptive_";

                    // Extract ore type from model path
                    // Format: "(block|item)/adaptive_<type>_ore"
                    String typeStr = path.substring(prefix.length(), path.length() - "_ore".length());

                    try {
                        OreType oreType = OreType.valueOf(typeStr.toUpperCase());
                        ResourceLocation overlayTexture = oreType.getOverlayTexture();

                        AdaptiveOres.LOGGER.debug("Loading custom model for {} with ore type {}", id, oreType);

                        // Return our custom unbaked model for both block and item model ids
                        return new AdaptiveOreUnbakedModel(oreType, overlayTexture);
                    } catch (IllegalArgumentException e) {
                        AdaptiveOres.LOGGER.warn("Unknown ore type in model path: {}", typeStr);
                    }
                }

                return null; // Let vanilla handle other models
            });
        });
    }
}
