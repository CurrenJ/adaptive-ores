package grill24.adaptiveores.fabric;

import grill24.adaptiveores.SidedDataGenerator;
import grill24.adaptiveores.data.CommonDataProviders;
import grill24.adaptiveores.fabric.data.ModAdaptiveOreModelProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class AdaptiveOresFabricDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        // Register common data providers - item and block tags, loot tables, etc.
        CommonDataProviders.addProviders(SidedDataGenerator.create(fabricDataGenerator));

        // Register data provider for models
        pack.addProvider(ModAdaptiveOreModelProvider::new);
    }
}
