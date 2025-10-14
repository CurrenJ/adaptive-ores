package grill24.adaptiveores.platform.registry.fabric;

import grill24.adaptiveores.platform.registry.IRegistryHelper;

public class SidedRegistryHelperImpl {
    public static IRegistryHelper create() {
        return new FabricRegistryHelper("adaptiveores");
    }
}
