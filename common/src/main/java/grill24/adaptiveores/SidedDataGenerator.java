package grill24.adaptiveores;

import dev.architectury.injectables.annotations.ExpectPlatform;
import grill24.adaptiveores.platform.IDataGeneratorHelper;

public class SidedDataGenerator {
    /**
     * Creates a sided IDataGeneratorHelper instance.
     * NeoForge implementation expects a GatherDataEvent object.
     * Fabric implementation expects a FabricDataGenerator object.
     * @param object
     * @return
     */
    @ExpectPlatform
    public static IDataGeneratorHelper create(Object object) {
        throw new AssertionError();
    }
}
