package grill24.adaptiveores.fabric;

import grill24.adaptiveores.fabric.platform.FabricRegistryHelper;
import grill24.adaptiveores.platform.IRegistryHelper;

public class SidedRegistryHelperImpl {
    public static IRegistryHelper create() {
        return new FabricRegistryHelper("adaptiveores");
    }
}
