package grill24.adaptiveores.foundation;

import grill24.adaptiveores.content.block.AdaptiveOreBlock;
import grill24.adaptiveores.platform.IRegistryHelper;
import net.minecraft.core.Holder;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

import java.util.List;
import java.util.function.Supplier;

public class AdaptiveOreBlocks {
    // Base properties for adaptive ore blocks
    private static BlockBehaviour.Properties baseOreProperties() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.STONE)
                .requiresCorrectToolForDrops()
                .strength(3.0F, 3.0F)
                .sound(SoundType.STONE);
    }

    // Adaptive ore blocks
    public static Holder<Block> ADAPTIVE_COAL_ORE;
    public static Holder<Block> ADAPTIVE_IRON_ORE;
    public static Holder<Block> ADAPTIVE_COPPER_ORE;
    public static Holder<Block> ADAPTIVE_GOLD_ORE;
    public static Holder<Block> ADAPTIVE_REDSTONE_ORE;
    public static Holder<Block> ADAPTIVE_LAPIS_ORE;
    public static Holder<Block> ADAPTIVE_DIAMOND_ORE;
    public static Holder<Block> ADAPTIVE_EMERALD_ORE;

    // Block items
    public static Holder<Item> ADAPTIVE_COAL_ORE_ITEM;
    public static Holder<Item> ADAPTIVE_IRON_ORE_ITEM;
    public static Holder<Item> ADAPTIVE_COPPER_ORE_ITEM;
    public static Holder<Item> ADAPTIVE_GOLD_ORE_ITEM;
    public static Holder<Item> ADAPTIVE_REDSTONE_ORE_ITEM;
    public static Holder<Item> ADAPTIVE_LAPIS_ORE_ITEM;
    public static Holder<Item> ADAPTIVE_DIAMOND_ORE_ITEM;
    public static Holder<Item> ADAPTIVE_EMERALD_ORE_ITEM;

    public static void init(IRegistryHelper registryHelper) {
        ADAPTIVE_COAL_ORE = registryHelper.registerBlock(AdaptiveOreConstants.ADAPTIVE_COAL_ORE, () -> new AdaptiveOreBlock(baseOreProperties(), OreType.COAL));
        ADAPTIVE_IRON_ORE = registryHelper.registerBlock(AdaptiveOreConstants.ADAPTIVE_IRON_ORE, () -> new AdaptiveOreBlock(baseOreProperties().strength(3.0F, 3.0F), OreType.IRON));
        ADAPTIVE_COPPER_ORE = registryHelper.registerBlock(AdaptiveOreConstants.ADAPTIVE_COPPER_ORE, () -> new AdaptiveOreBlock(baseOreProperties().strength(3.0F, 3.0F), OreType.COPPER));
        ADAPTIVE_GOLD_ORE = registryHelper.registerBlock(AdaptiveOreConstants.ADAPTIVE_GOLD_ORE, () -> new AdaptiveOreBlock(baseOreProperties().strength(3.0F, 3.0F), OreType.GOLD));
        ADAPTIVE_REDSTONE_ORE = registryHelper.registerBlock(AdaptiveOreConstants.ADAPTIVE_REDSTONE_ORE, () -> new AdaptiveOreBlock(baseOreProperties().strength(3.0F, 3.0F), OreType.REDSTONE));
        ADAPTIVE_LAPIS_ORE = registryHelper.registerBlock(AdaptiveOreConstants.ADAPTIVE_LAPIS_ORE, () -> new AdaptiveOreBlock(baseOreProperties().strength(3.0F, 3.0F), OreType.LAPIS));
        ADAPTIVE_DIAMOND_ORE = registryHelper.registerBlock(AdaptiveOreConstants.ADAPTIVE_DIAMOND_ORE, () -> new AdaptiveOreBlock(baseOreProperties().strength(3.0F, 3.0F), OreType.DIAMOND));
        ADAPTIVE_EMERALD_ORE = registryHelper.registerBlock(AdaptiveOreConstants.ADAPTIVE_EMERALD_ORE, () -> new AdaptiveOreBlock(baseOreProperties().strength(3.0F, 3.0F), OreType.EMERALD));

        ADAPTIVE_COAL_ORE_ITEM = registryHelper.registerItem(AdaptiveOreConstants.ADAPTIVE_COAL_ORE, () -> new BlockItem(ADAPTIVE_COAL_ORE.value(), new Item.Properties()));
        ADAPTIVE_IRON_ORE_ITEM = registryHelper.registerItem(AdaptiveOreConstants.ADAPTIVE_IRON_ORE, () -> new BlockItem(ADAPTIVE_IRON_ORE.value(), new Item.Properties()));
        ADAPTIVE_COPPER_ORE_ITEM = registryHelper.registerItem(AdaptiveOreConstants.ADAPTIVE_COPPER_ORE, () -> new BlockItem(ADAPTIVE_COPPER_ORE.value(), new Item.Properties()));
        ADAPTIVE_GOLD_ORE_ITEM = registryHelper.registerItem(AdaptiveOreConstants.ADAPTIVE_GOLD_ORE, () -> new BlockItem(ADAPTIVE_GOLD_ORE.value(), new Item.Properties()));
        ADAPTIVE_REDSTONE_ORE_ITEM = registryHelper.registerItem(AdaptiveOreConstants.ADAPTIVE_REDSTONE_ORE, () -> new BlockItem(ADAPTIVE_REDSTONE_ORE.value(), new Item.Properties()));
        ADAPTIVE_LAPIS_ORE_ITEM = registryHelper.registerItem(AdaptiveOreConstants.ADAPTIVE_LAPIS_ORE, () -> new BlockItem(ADAPTIVE_LAPIS_ORE.value(), new Item.Properties()));
        ADAPTIVE_DIAMOND_ORE_ITEM = registryHelper.registerItem(AdaptiveOreConstants.ADAPTIVE_DIAMOND_ORE, () -> new BlockItem(ADAPTIVE_DIAMOND_ORE.value(), new Item.Properties()));
        ADAPTIVE_EMERALD_ORE_ITEM = registryHelper.registerItem(AdaptiveOreConstants.ADAPTIVE_EMERALD_ORE, () -> new BlockItem(ADAPTIVE_EMERALD_ORE.value(), new Item.Properties()));
    }

    public static Iterable<Block> getAllAdaptiveOres() {
        return List.of(
                ADAPTIVE_COAL_ORE.value(),
                ADAPTIVE_IRON_ORE.value(),
                ADAPTIVE_COPPER_ORE.value(),
                ADAPTIVE_GOLD_ORE.value(),
                ADAPTIVE_REDSTONE_ORE.value(),
                ADAPTIVE_LAPIS_ORE.value(),
                ADAPTIVE_DIAMOND_ORE.value(),
                ADAPTIVE_EMERALD_ORE.value()
        );
    }
}
