package grill24.adaptiveores.platform.registry;

import dev.architectury.injectables.annotations.ExpectPlatform;

public class SidedRegistryHelper {
    @ExpectPlatform
    public static IRegistryHelper create() {
        throw new AssertionError();
    }
}
