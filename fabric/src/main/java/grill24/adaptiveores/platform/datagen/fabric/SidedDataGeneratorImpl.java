package grill24.adaptiveores.platform.datagen.fabric;

import grill24.adaptiveores.platform.datagen.IDataGeneratorHelper;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class SidedDataGeneratorImpl {
    public static IDataGeneratorHelper create(Object object) {
        if (!(object instanceof FabricDataGenerator fabricDataGenerator)) {
            throw new IllegalArgumentException("Expected FabricDataGenerator but got " + object);
        }

        return new FabricDataGeneratorHelper(fabricDataGenerator);
    }
}
