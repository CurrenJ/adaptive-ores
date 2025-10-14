package grill24.adaptiveores.fabric;

import grill24.adaptiveores.data.CommonBlockTags;
import grill24.adaptiveores.data.CommonItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

public class TagProviderImpl {
    public static TagsProvider<Block> createBlockTagsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        return new FabricTagProvider.BlockTagProvider((FabricDataOutput) packOutput, lookupProvider) {
            @Override
            protected void addTags(HolderLookup.Provider arg) {
                CommonBlockTags.addVanillaTags((tag, blocks) -> this.tag(tag).add(blocks));
            }
        };
    }

    public static TagsProvider<Item> createItemTagsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagsProvider.TagLookup<Block>> blockTagsProvider) {
        return new ItemTagsProvider((FabricDataOutput) packOutput, lookupProvider, blockTagsProvider) {
            @Override
            protected void addTags(HolderLookup.Provider arg) {
                CommonItemTags.addVanillaTags((tag, items) -> this.tag(tag).add(items));
            }
        };
    }
}

