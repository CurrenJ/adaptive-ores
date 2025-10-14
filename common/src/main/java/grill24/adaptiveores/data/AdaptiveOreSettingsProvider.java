package grill24.adaptiveores.data;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import grill24.adaptiveores.AdaptiveOres;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class AdaptiveOreSettingsProvider implements DataProvider {
    private final PackOutput packOutput;
    Map<ResourceLocation, AdaptiveOreSettings> settings;

    public AdaptiveOreSettingsProvider(PackOutput packOutput) {
        this.packOutput = packOutput;
        this.settings = new HashMap<>();

        addOreSettings(ResourceLocation.fromNamespaceAndPath(AdaptiveOres.MOD_ID, "copper_ore"),
            new AdaptiveOreSettings(Blocks.COPPER_ORE.defaultBlockState(), Blocks.STONE.defaultBlockState(), List.of(/* modifiers */)));
    }

    protected void addOreSettings(ResourceLocation id, AdaptiveOreSettings settings) {
        this.settings.put(id, settings);
    }

    @Override
    public CompletableFuture<Void> run(CachedOutput output) {
        return CompletableFuture.allOf(settings.entrySet().stream().map(
            entry -> {
                Path path = packOutput.createRegistryElementsPathProvider(AdaptiveOreSettingsRegistry.REGISTRY_KEY).json(entry.getKey());
                JsonElement json = AdaptiveOreSettings.CODEC.encodeStart(JsonOps.INSTANCE, entry.getValue()).result().orElseThrow();
                try {
                    return DataProvider.saveStable(output, json, path);
                } catch (Exception e) {
                    return CompletableFuture.failedFuture(e);
                }
            }
        ).toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return "Adaptive Ore Settings";
    }
}
