package grill24.adaptiveores.neoforge;

import grill24.adaptiveores.AdaptiveOres;
import grill24.adaptiveores.data.CommonBlockTags;
import grill24.adaptiveores.data.CommonItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class TagProviderImpl {
    public static TagsProvider<Block> createBlockTagsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        return new IntrinsicHolderTagsProvider<>(packOutput, Registries.BLOCK, lookupProvider, argx -> argx.builtInRegistryHolder().key(), AdaptiveOres.MOD_ID, null) {
            @Override
            protected void addTags(HolderLookup.Provider provider) {
                CommonBlockTags.addVanillaTags((tag, items) -> this.tag(tag).add(items));
            }
        };
    }

    public static TagsProvider<Item> createItemTagsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagsProvider.TagLookup<Block>> blockTagsProvider) {
        return new ItemTagsProvider(packOutput, lookupProvider, blockTagsProvider, AdaptiveOres.MOD_ID, null) {
            @Override
            protected void addTags(HolderLookup.Provider provider) {
                CommonItemTags.addVanillaTags((tag, items) -> this.tag(tag).add(items));
            }
        };
    }
}

