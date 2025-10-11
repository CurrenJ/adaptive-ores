package grill24.adaptiveores.data;

import grill24.adaptiveores.foundation.AdaptiveOreBlocks;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;

import java.util.function.BiConsumer;

/**
 * Common helper for registering the vanilla block tags for adaptive ores.
 * Platforms should call CommonBlockTags.addVanillaTags(this::tag) from their BlockTags provider's addTags method,
 * or provide a BiConsumer<TagKey<Block>, Block[]> that performs the equivalent tag addition.
 */
public final class CommonBlockTags {

    private CommonBlockTags() {}

    /**
     * Add vanilla block tags for adaptive ore blocks using the provided tag adder.
     * The tagAdder receives a TagKey<Block> and an array of Block instances to add to that tag.
     */
    public static void addVanillaTags(BiConsumer<net.minecraft.tags.TagKey<Block>, Block[]> tagAdder) {
        // All ores are mineable with pickaxe
        tagAdder.accept(BlockTags.MINEABLE_WITH_PICKAXE, new Block[] {
                AdaptiveOreBlocks.ADAPTIVE_COAL_ORE.value(),
                AdaptiveOreBlocks.ADAPTIVE_IRON_ORE.value(),
                AdaptiveOreBlocks.ADAPTIVE_COPPER_ORE.value(),
                AdaptiveOreBlocks.ADAPTIVE_GOLD_ORE.value(),
                AdaptiveOreBlocks.ADAPTIVE_REDSTONE_ORE.value(),
                AdaptiveOreBlocks.ADAPTIVE_LAPIS_ORE.value(),
                AdaptiveOreBlocks.ADAPTIVE_DIAMOND_ORE.value(),
                AdaptiveOreBlocks.ADAPTIVE_EMERALD_ORE.value()
        });

        // Stone-level required for: iron, copper, lapis
        tagAdder.accept(BlockTags.NEEDS_STONE_TOOL, new Block[] {
                AdaptiveOreBlocks.ADAPTIVE_IRON_ORE.value(),
                AdaptiveOreBlocks.ADAPTIVE_COPPER_ORE.value(),
                AdaptiveOreBlocks.ADAPTIVE_LAPIS_ORE.value()
        });

        // Iron-level required for: gold, redstone, diamond, emerald
        tagAdder.accept(BlockTags.NEEDS_IRON_TOOL, new Block[] {
                AdaptiveOreBlocks.ADAPTIVE_GOLD_ORE.value(),
                AdaptiveOreBlocks.ADAPTIVE_REDSTONE_ORE.value(),
                AdaptiveOreBlocks.ADAPTIVE_DIAMOND_ORE.value(),
                AdaptiveOreBlocks.ADAPTIVE_EMERALD_ORE.value()
        });

        tagAdder.accept(BlockTags.COAL_ORES, new Block[] { AdaptiveOreBlocks.ADAPTIVE_COAL_ORE.value() });
        tagAdder.accept(BlockTags.IRON_ORES, new Block[] { AdaptiveOreBlocks.ADAPTIVE_IRON_ORE.value() });
        tagAdder.accept(BlockTags.COPPER_ORES, new Block[] { AdaptiveOreBlocks.ADAPTIVE_COPPER_ORE.value() });
        tagAdder.accept(BlockTags.GOLD_ORES, new Block[] { AdaptiveOreBlocks.ADAPTIVE_GOLD_ORE.value() });
        tagAdder.accept(BlockTags.REDSTONE_ORES, new Block[] { AdaptiveOreBlocks.ADAPTIVE_REDSTONE_ORE.value() });
        tagAdder.accept(BlockTags.LAPIS_ORES, new Block[] { AdaptiveOreBlocks.ADAPTIVE_LAPIS_ORE.value() });
        tagAdder.accept(BlockTags.DIAMOND_ORES, new Block[] { AdaptiveOreBlocks.ADAPTIVE_DIAMOND_ORE.value() });
        tagAdder.accept(BlockTags.EMERALD_ORES, new Block[] { AdaptiveOreBlocks.ADAPTIVE_EMERALD_ORE.value() });
    }
}
