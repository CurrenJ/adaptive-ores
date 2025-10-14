package grill24.adaptiveores.neoforge;

import grill24.adaptiveores.AdaptiveOres;
import grill24.adaptiveores.neoforge.platform.NeoForgeRegistryHelper;
import grill24.adaptiveores.platform.IRegistryHelper;

public class SidedRegistryHelperImpl {
    public static IRegistryHelper create() {
        return new NeoForgeRegistryHelper(AdaptiveOres.MOD_ID);
    }
}
