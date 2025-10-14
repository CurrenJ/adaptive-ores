package grill24.adaptiveores.foundation;

import grill24.adaptiveores.AdaptiveOres;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class AdaptiveOreConstants {
    public static final ResourceLocation ADAPTIVE_COAL_ORE = ResourceLocation.fromNamespaceAndPath(AdaptiveOres.MOD_ID, "adaptive_coal_ore");
    public static final ResourceKey<Block> ADAPTIVE_COAL_ORE_BLOCK = block(ADAPTIVE_COAL_ORE);
    public static final ResourceKey<Item> ADAPTIVE_COAL_ORE_ITEM = item(ADAPTIVE_COAL_ORE);
    public static final ResourceLocation ADAPTIVE_IRON_ORE = ResourceLocation.fromNamespaceAndPath(AdaptiveOres.MOD_ID, "adaptive_iron_ore");
    public static final ResourceKey<Block> ADAPTIVE_IRON_ORE_BLOCK = block(ADAPTIVE_IRON_ORE);
    public static final ResourceKey<Item> ADAPTIVE_IRON_ORE_ITEM = item(ADAPTIVE_IRON_ORE);
    public static final ResourceLocation ADAPTIVE_COPPER_ORE = ResourceLocation.fromNamespaceAndPath(AdaptiveOres.MOD_ID, "adaptive_copper_ore");
    public static final ResourceKey<Block> ADAPTIVE_COPPER_ORE_BLOCK = block(ADAPTIVE_COPPER_ORE);
    public static final ResourceKey<Item> ADAPTIVE_COPPER_ORE_ITEM = item(ADAPTIVE_COPPER_ORE);
    public static final ResourceLocation ADAPTIVE_GOLD_ORE = ResourceLocation.fromNamespaceAndPath(AdaptiveOres.MOD_ID, "adaptive_gold_ore");
    public static final ResourceKey<Block> ADAPTIVE_GOLD_ORE_BLOCK = block(ADAPTIVE_GOLD_ORE);
    public static final ResourceKey<Item> ADAPTIVE_GOLD_ORE_ITEM = item(ADAPTIVE_GOLD_ORE);
    public static final ResourceLocation ADAPTIVE_REDSTONE_ORE = ResourceLocation.fromNamespaceAndPath(AdaptiveOres.MOD_ID, "adaptive_redstone_ore");
    public static final ResourceKey<Block> ADAPTIVE_REDSTONE_ORE_BLOCK = block(ADAPTIVE_REDSTONE_ORE);
    public static final ResourceKey<Item> ADAPTIVE_REDSTONE_ORE_ITEM = item(ADAPTIVE_REDSTONE_ORE);
    public static final ResourceLocation ADAPTIVE_LAPIS_ORE = ResourceLocation.fromNamespaceAndPath(AdaptiveOres.MOD_ID, "adaptive_lapis_ore");
    public static final ResourceKey<Block> ADAPTIVE_LAPIS_ORE_BLOCK = block(ADAPTIVE_LAPIS_ORE);
    public static final ResourceKey<Item> ADAPTIVE_LAPIS_ORE_ITEM = item(ADAPTIVE_LAPIS_ORE);
    public static final ResourceLocation ADAPTIVE_DIAMOND_ORE = ResourceLocation.fromNamespaceAndPath(AdaptiveOres.MOD_ID, "adaptive_diamond_ore");
    public static final ResourceKey<Block> ADAPTIVE_DIAMOND_ORE_BLOCK = block(ADAPTIVE_DIAMOND_ORE);
    public static final ResourceKey<Item> ADAPTIVE_DIAMOND_ORE_ITEM = item(ADAPTIVE_DIAMOND_ORE);
    public static final ResourceLocation ADAPTIVE_EMERALD_ORE = ResourceLocation.fromNamespaceAndPath(AdaptiveOres.MOD_ID, "adaptive_emerald_ore");
    public static final ResourceKey<Block> ADAPTIVE_EMERALD_ORE_BLOCK = block(ADAPTIVE_EMERALD_ORE);
    public static final ResourceKey<Item> ADAPTIVE_EMERALD_ORE_ITEM = item(ADAPTIVE_EMERALD_ORE);

    private static ResourceKey<Block> block(ResourceLocation location) {
        return ResourceKey.create(Registries.BLOCK, location);
    }

    private static ResourceKey<Item> item(ResourceLocation location) {
        return ResourceKey.create(Registries.ITEM, location);
    }
}
