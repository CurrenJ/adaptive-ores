package grill24.adaptiveores.data;

import grill24.adaptiveores.AdaptiveOres;
import grill24.adaptiveores.foundation.AdaptiveOreBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.Tags;

import java.util.concurrent.CompletableFuture;

public class ModBlockTags extends net.neoforged.neoforge.common.data.BlockTagsProvider {

    public ModBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        // NeoForge's BlockTagsProvider expects four arguments in its constructor; pass null for the existing file helper
        super(output, lookupProvider, AdaptiveOres.MODID, null);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // All ores are mineable with pickaxe
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
                AdaptiveOreBlocks.ADAPTIVE_COAL_ORE.value(),
                AdaptiveOreBlocks.ADAPTIVE_IRON_ORE.value(),
                AdaptiveOreBlocks.ADAPTIVE_COPPER_ORE.value(),
                AdaptiveOreBlocks.ADAPTIVE_GOLD_ORE.value(),
                AdaptiveOreBlocks.ADAPTIVE_REDSTONE_ORE.value(),
                AdaptiveOreBlocks.ADAPTIVE_LAPIS_ORE.value(),
                AdaptiveOreBlocks.ADAPTIVE_DIAMOND_ORE.value(),
                AdaptiveOreBlocks.ADAPTIVE_EMERALD_ORE.value()
        );

        // Minimum tool requirements (match vanilla behavior as closely as possible):
        // Coal: no minimum (wooden pickaxe works) -> do not add to NEEDS_*.
        // Stone-level required for: iron, copper, lapis
        tag(BlockTags.NEEDS_STONE_TOOL).add(
                AdaptiveOreBlocks.ADAPTIVE_IRON_ORE.value(),
                AdaptiveOreBlocks.ADAPTIVE_COPPER_ORE.value(),
                AdaptiveOreBlocks.ADAPTIVE_LAPIS_ORE.value()
        );

        // Iron-level required for: gold, redstone, diamond, emerald
        tag(BlockTags.NEEDS_IRON_TOOL).add(
                AdaptiveOreBlocks.ADAPTIVE_GOLD_ORE.value(),
                AdaptiveOreBlocks.ADAPTIVE_REDSTONE_ORE.value(),
                AdaptiveOreBlocks.ADAPTIVE_DIAMOND_ORE.value(),
                AdaptiveOreBlocks.ADAPTIVE_EMERALD_ORE.value()
        );

        tag(BlockTags.COAL_ORES).add(AdaptiveOreBlocks.ADAPTIVE_COAL_ORE.value());
        tag(BlockTags.IRON_ORES).add(AdaptiveOreBlocks.ADAPTIVE_IRON_ORE.value());
        tag(BlockTags.COPPER_ORES).add(AdaptiveOreBlocks.ADAPTIVE_COPPER_ORE.value());
        tag(BlockTags.GOLD_ORES).add(AdaptiveOreBlocks.ADAPTIVE_GOLD_ORE.value());
        tag(BlockTags.REDSTONE_ORES).add(AdaptiveOreBlocks.ADAPTIVE_REDSTONE_ORE.value());
        tag(BlockTags.LAPIS_ORES).add(AdaptiveOreBlocks.ADAPTIVE_LAPIS_ORE.value());
        tag(BlockTags.DIAMOND_ORES).add(AdaptiveOreBlocks.ADAPTIVE_DIAMOND_ORE.value());
        tag(BlockTags.EMERALD_ORES).add(AdaptiveOreBlocks.ADAPTIVE_EMERALD_ORE.value());
    }
}
