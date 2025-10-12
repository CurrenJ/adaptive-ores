package grill24.adaptiveores.neoforge.data;

import grill24.adaptiveores.AdaptiveOres;
import grill24.adaptiveores.neoforge.AdaptiveOresNeoForge;
import grill24.adaptiveores.platform.BlockRegistry;
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
        super(packOutput, lookupProvider, blockTagsProvider, AdaptiveOres.MOD_ID, null);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // Mirror block ore tags for the corresponding item forms
        tag(ItemTags.COAL_ORES).add(BlockRegistry.getAdaptiveCoalOre(provider).value().asItem());
        tag(ItemTags.IRON_ORES).add(BlockRegistry.getAdaptiveIronOre(provider).value().asItem());
        tag(ItemTags.COPPER_ORES).add(BlockRegistry.getAdaptiveCopperOre(provider).value().asItem());
        tag(ItemTags.GOLD_ORES).add(BlockRegistry.getAdaptiveGoldOre(provider).value().asItem());
        tag(ItemTags.REDSTONE_ORES).add(BlockRegistry.getAdaptiveRedstoneOre(provider).value().asItem());
        tag(ItemTags.LAPIS_ORES).add(BlockRegistry.getAdaptiveLapisOre(provider).value().asItem());
        tag(ItemTags.DIAMOND_ORES).add(BlockRegistry.getAdaptiveDiamondOre(provider).value().asItem());
        tag(ItemTags.EMERALD_ORES).add(BlockRegistry.getAdaptiveEmeraldOre(provider).value().asItem());
    }
}
