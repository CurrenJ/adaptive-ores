package grill24.adaptiveores.data;

import grill24.adaptiveores.TagAdder;
import grill24.adaptiveores.foundation.AdaptiveOreConstants;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;

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
    public static void addVanillaTags(TagAdder<Block> tagAdder) {
        // All ores are mineable with pickaxe
        tagAdder.add(BlockTags.MINEABLE_WITH_PICKAXE,
                AdaptiveOreConstants.ADAPTIVE_COAL_ORE_BLOCK,
                AdaptiveOreConstants.ADAPTIVE_IRON_ORE_BLOCK,
                AdaptiveOreConstants.ADAPTIVE_COPPER_ORE_BLOCK,
                AdaptiveOreConstants.ADAPTIVE_GOLD_ORE_BLOCK,
                AdaptiveOreConstants.ADAPTIVE_REDSTONE_ORE_BLOCK,
                AdaptiveOreConstants.ADAPTIVE_LAPIS_ORE_BLOCK,
                AdaptiveOreConstants.ADAPTIVE_DIAMOND_ORE_BLOCK,
                AdaptiveOreConstants.ADAPTIVE_EMERALD_ORE_BLOCK
        );

        // Stone-level required for: iron, copper, lapis
        tagAdder.add(BlockTags.NEEDS_STONE_TOOL,
                AdaptiveOreConstants.ADAPTIVE_IRON_ORE_BLOCK,
                AdaptiveOreConstants.ADAPTIVE_COPPER_ORE_BLOCK,
                AdaptiveOreConstants.ADAPTIVE_LAPIS_ORE_BLOCK
        );

        // Iron-level required for: gold, redstone, diamond, emerald
        tagAdder.add(BlockTags.NEEDS_IRON_TOOL,
                AdaptiveOreConstants.ADAPTIVE_GOLD_ORE_BLOCK,
                AdaptiveOreConstants.ADAPTIVE_REDSTONE_ORE_BLOCK,
                AdaptiveOreConstants.ADAPTIVE_DIAMOND_ORE_BLOCK,
                AdaptiveOreConstants.ADAPTIVE_EMERALD_ORE_BLOCK
        );

        tagAdder.add(BlockTags.COAL_ORES, AdaptiveOreConstants.ADAPTIVE_COAL_ORE_BLOCK);
        tagAdder.add(BlockTags.IRON_ORES, AdaptiveOreConstants.ADAPTIVE_IRON_ORE_BLOCK);
        tagAdder.add(BlockTags.COPPER_ORES, AdaptiveOreConstants.ADAPTIVE_COPPER_ORE_BLOCK);
        tagAdder.add(BlockTags.GOLD_ORES, AdaptiveOreConstants.ADAPTIVE_GOLD_ORE_BLOCK);
        tagAdder.add(BlockTags.REDSTONE_ORES, AdaptiveOreConstants.ADAPTIVE_REDSTONE_ORE_BLOCK);
        tagAdder.add(BlockTags.LAPIS_ORES, AdaptiveOreConstants.ADAPTIVE_LAPIS_ORE_BLOCK);
        tagAdder.add(BlockTags.DIAMOND_ORES, AdaptiveOreConstants.ADAPTIVE_DIAMOND_ORE_BLOCK);
        tagAdder.add(BlockTags.EMERALD_ORES, AdaptiveOreConstants.ADAPTIVE_EMERALD_ORE_BLOCK);
    }


}
