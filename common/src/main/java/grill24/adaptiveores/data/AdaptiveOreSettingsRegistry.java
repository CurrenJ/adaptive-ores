package grill24.adaptiveores.data;

import grill24.adaptiveores.AdaptiveOres;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class AdaptiveOreSettingsRegistry {
    public static final ResourceKey<Registry<AdaptiveOreSettings>> REGISTRY_KEY = ResourceKey.createRegistryKey(
        ResourceLocation.fromNamespaceAndPath(AdaptiveOres.MOD_ID, "adaptive_ore_settings")
    );

    public static Registry<AdaptiveOreSettings> REGISTRY;

    public static void init() {
        REGISTRY = AdaptiveOres.REGISTRY_HELPER.getAdaptiveOreSettingsRegistry();
        AdaptiveOres.REGISTRY_HELPER.registerAdaptiveOreSettingsRegistry();
    }
}
