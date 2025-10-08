package grill24.adaptiveores.data;

import grill24.adaptiveores.AdaptiveOres;
import grill24.adaptiveores.foundation.AdaptiveOreBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

/**
 * Register adaptive ore items with appropriate ore item tags so data-driven recipes
 * and other systems that consume item ore tags recognize them.
 */
public class ModItemTags extends ItemTagsProvider {

    public ModItemTags(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTagsProvider) {
        // NeoForge's ItemTagsProvider expects four arguments in its constructor; pass null for existing file helper
        super(packOutput, lookupProvider, blockTagsProvider, AdaptiveOres.MODID, null);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // Mirror block ore tags for the corresponding item forms
        tag(ItemTags.COAL_ORES).add(AdaptiveOreBlocks.ADAPTIVE_COAL_ORE.value().asItem());
        tag(ItemTags.IRON_ORES).add(AdaptiveOreBlocks.ADAPTIVE_IRON_ORE.value().asItem());
        tag(ItemTags.COPPER_ORES).add(AdaptiveOreBlocks.ADAPTIVE_COPPER_ORE.value().asItem());
        tag(ItemTags.GOLD_ORES).add(AdaptiveOreBlocks.ADAPTIVE_GOLD_ORE.value().asItem());
        tag(ItemTags.REDSTONE_ORES).add(AdaptiveOreBlocks.ADAPTIVE_REDSTONE_ORE.value().asItem());
        tag(ItemTags.LAPIS_ORES).add(AdaptiveOreBlocks.ADAPTIVE_LAPIS_ORE.value().asItem());
        tag(ItemTags.DIAMOND_ORES).add(AdaptiveOreBlocks.ADAPTIVE_DIAMOND_ORE.value().asItem());
        tag(ItemTags.EMERALD_ORES).add(AdaptiveOreBlocks.ADAPTIVE_EMERALD_ORE.value().asItem());
    }
}

