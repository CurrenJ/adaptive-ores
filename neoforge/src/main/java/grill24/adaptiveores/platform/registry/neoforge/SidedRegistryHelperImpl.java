package grill24.adaptiveores.platform.registry.neoforge;

import grill24.adaptiveores.AdaptiveOres;
import grill24.adaptiveores.platform.registry.IRegistryHelper;

public class SidedRegistryHelperImpl {
    public static IRegistryHelper create() {
        return new NeoForgeRegistryHelper(AdaptiveOres.MOD_ID);
    }
}
