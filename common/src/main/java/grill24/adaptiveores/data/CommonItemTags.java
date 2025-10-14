package grill24.adaptiveores.data;

import grill24.adaptiveores.TagAdder;
import grill24.adaptiveores.foundation.AdaptiveOreConstants;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;

/**
 * Common helper for registering the vanilla item tags for adaptive ores.
 * Platforms should call CommonItemTags.addVanillaTags(this::tag) from their ItemTags provider's addTags method,
 * or provide a BiConsumer<TagKey<Item>, Item[]> that performs the equivalent tag addition.
 */
public final class CommonItemTags {

    private CommonItemTags() {}

    /**
     * Add vanilla item tags for adaptive ore items using the provided tag adder.
     * The tagAdder receives a TagKey<Item> and an array of Item instances to add to that tag.
     */
    public static void addVanillaTags(TagAdder<Item> tagAdder) {
        tagAdder.add(ItemTags.COAL_ORES, AdaptiveOreConstants.ADAPTIVE_COAL_ORE_ITEM);
        tagAdder.add(ItemTags.IRON_ORES, AdaptiveOreConstants.ADAPTIVE_IRON_ORE_ITEM);
        tagAdder.add(ItemTags.COPPER_ORES, AdaptiveOreConstants.ADAPTIVE_COPPER_ORE_ITEM);
        tagAdder.add(ItemTags.GOLD_ORES, AdaptiveOreConstants.ADAPTIVE_GOLD_ORE_ITEM);
        tagAdder.add(ItemTags.REDSTONE_ORES, AdaptiveOreConstants.ADAPTIVE_REDSTONE_ORE_ITEM);
        tagAdder.add(ItemTags.LAPIS_ORES, AdaptiveOreConstants.ADAPTIVE_LAPIS_ORE_ITEM);
        tagAdder.add(ItemTags.DIAMOND_ORES, AdaptiveOreConstants.ADAPTIVE_DIAMOND_ORE_ITEM);
        tagAdder.add(ItemTags.EMERALD_ORES, AdaptiveOreConstants.ADAPTIVE_EMERALD_ORE_ITEM);
    }
}
