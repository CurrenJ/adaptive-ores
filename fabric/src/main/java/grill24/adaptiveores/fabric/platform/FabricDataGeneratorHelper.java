package grill24.adaptiveores.fabric.platform;

import grill24.adaptiveores.platform.IDataGeneratorHelper;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.data.DataProvider;

public class FabricDataGeneratorHelper implements IDataGeneratorHelper {
    private FabricDataGenerator generator;
    private FabricDataGenerator.Pack pack;

    public FabricDataGeneratorHelper(FabricDataGenerator generator) {
        this.generator = generator;
        this.pack = generator.createPack();
    }

    @Override
    public <T extends DataProvider> T AddProvider(DataProvider.Factory<T> factory) {
        return pack.addProvider(factory);
    }

    @Override
    public <T extends DataProvider> T AddProvider(RegistryDependentFactory<T> factory) {
        return pack.addProvider(factory::create);
    }
}
