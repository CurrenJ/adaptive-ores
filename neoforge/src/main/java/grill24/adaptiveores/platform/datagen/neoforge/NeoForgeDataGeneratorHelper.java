package grill24.adaptiveores.platform.datagen.neoforge;

import grill24.adaptiveores.platform.datagen.IDataGeneratorHelper;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

public class NeoForgeDataGeneratorHelper implements IDataGeneratorHelper {
    private DataGenerator generator;
    private PackOutput pack;
    private CompletableFuture<HolderLookup.Provider> lookupProvider;

    public NeoForgeDataGeneratorHelper(GatherDataEvent event) {
        this.generator = event.getGenerator();
        this.pack = generator.getPackOutput();
        this.lookupProvider = event.getLookupProvider();
    }

    @Override
    public <T extends DataProvider> T AddProvider(DataProvider.Factory<T> factory) {
        return generator.addProvider(true, factory);
    }

    @Override
    public <T extends DataProvider> T AddProvider(RegistryDependentFactory<T> factory) {
        return generator.addProvider(true, factory.create(pack, lookupProvider));
    }
}
