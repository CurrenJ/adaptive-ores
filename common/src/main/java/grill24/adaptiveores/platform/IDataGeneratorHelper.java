package grill24.adaptiveores.platform;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;

public interface IDataGeneratorHelper {
    <T extends DataProvider> T AddProvider(DataProvider.Factory<T> factory);
    <T extends DataProvider> T AddProvider(RegistryDependentFactory<T> factory);

    @FunctionalInterface
    public interface RegistryDependentFactory<T extends DataProvider> {
        T create(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider);
    }
}
