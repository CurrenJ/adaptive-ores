package grill24.adaptiveores.neoforge.data;

import grill24.adaptiveores.AdaptiveOres;
import grill24.adaptiveores.data.CommonDataProviders;
import grill24.adaptiveores.platform.datagen.SidedDataGenerator;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.IModBusEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = AdaptiveOres.MOD_ID)
public class DataGenerators implements IModBusEvent {

    @SubscribeEvent
    public static void gatherData(final GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        // Register common providers (recipes, tags, loot tables, etc.)
        CommonDataProviders.addProviders(SidedDataGenerator.create(event));

        // Register client-side provider for models (block + item models using custom loader)
        generator.addProvider(event.includeClient(), new ModAdaptiveOreModelProvider(packOutput, existingFileHelper));
    }
}
