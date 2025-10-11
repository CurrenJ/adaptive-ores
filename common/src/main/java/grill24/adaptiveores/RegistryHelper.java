package grill24.adaptiveores;

import dev.architectury.injectables.annotations.ExpectPlatform;
import grill24.adaptiveores.platform.IRegistryHelper;

public class RegistryHelper {

    @ExpectPlatform
    public static IRegistryHelper create() {
        throw new AssertionError();
    }
}
