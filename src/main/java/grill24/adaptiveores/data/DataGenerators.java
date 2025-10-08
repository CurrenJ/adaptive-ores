package grill24.adaptiveores.data;

import grill24.adaptiveores.AdaptiveOres;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.tags.ItemTagsProvider;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.IModBusEvent;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = AdaptiveOres.MODID)
public class DataGenerators implements IModBusEvent {

    @SubscribeEvent
    public static void gatherData(final net.neoforged.neoforge.data.event.GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        // Register server (loot tables) provider
        generator.addProvider(event.includeServer(), new ModLootTableProvider(packOutput, lookupProvider));

        BlockTagsProvider blockTagsProvider = new ModBlockTags(packOutput, lookupProvider);
        ItemTagsProvider itemTagsProvider = new ModItemTags(packOutput, lookupProvider, blockTagsProvider.contentsGetter());
        // Register server tags provider for block tool requirements and mineable tags
        generator.addProvider(event.includeServer(), blockTagsProvider);
        // Register server tags provider for item tags (mirror ore item tags)
        generator.addProvider(event.includeServer(), itemTagsProvider);

        // Register client-side provider for models (block + item models using custom loader)
        generator.addProvider(event.includeClient(), new ModAdaptiveOreModelProvider(packOutput));
    }
}
