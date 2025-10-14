package grill24.adaptiveores.data;

import grill24.adaptiveores.platform.datagen.TagProvider;
import grill24.adaptiveores.platform.datagen.IDataGeneratorHelper;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class CommonDataProviders {
    public static void addProviders(IDataGeneratorHelper dataGeneratorHelper)
    {
        dataGeneratorHelper.AddProvider(CommonLootTableProvider::new);
        TagsProvider<Block> blockTagsProvider = dataGeneratorHelper.AddProvider(TagProvider::createBlockTagsProvider);
        TagsProvider<Item> itemTagsProvider = dataGeneratorHelper.AddProvider((packOutput, lookupProvider) -> TagProvider.createItemTagsProvider(packOutput, lookupProvider,  blockTagsProvider.contentsGetter()));

        dataGeneratorHelper.AddProvider(AdaptiveOreSettingsProvider::new);
    }
}
