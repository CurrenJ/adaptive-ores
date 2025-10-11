package grill24.adaptiveores.fabric;

import grill24.adaptiveores.AdaptiveOres;
import net.fabricmc.api.ModInitializer;

public final class AdaptiveOresFabricMod implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        grill24.adaptiveores.AdaptiveOres.REGISTRY_HELPER = RegistryHelperImpl.create();

        // Run our common setup.
        AdaptiveOres.init();

        // Register commands
        AdaptiveOresCommands.register();
    }
}
