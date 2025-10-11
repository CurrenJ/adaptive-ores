package grill24.adaptiveores.platform.fabric;

import grill24.adaptiveores.foundation.AdaptiveOreBlocks;
import net.minecraft.world.level.block.Block;

import java.util.List;

/**
 * Fabric implementation for accessing registered blocks.
 */
public class BlockRegistryImpl {

    public static Block getAdaptiveCoalOre() {
        return AdaptiveOreBlocks.ADAPTIVE_COAL_ORE.value();
    }

    public static Block getAdaptiveIronOre() {
        return AdaptiveOreBlocks.ADAPTIVE_IRON_ORE.value();
    }

    public static Block getAdaptiveCopperOre() {
        return AdaptiveOreBlocks.ADAPTIVE_COPPER_ORE.value();
    }

    public static Block getAdaptiveGoldOre() {
        return AdaptiveOreBlocks.ADAPTIVE_GOLD_ORE.value();
    }

    public static Block getAdaptiveRedstoneOre() {
        return AdaptiveOreBlocks.ADAPTIVE_REDSTONE_ORE.value();
    }

    public static Block getAdaptiveLapisOre() {
        return AdaptiveOreBlocks.ADAPTIVE_LAPIS_ORE.value();
    }

    public static Block getAdaptiveDiamondOre() {
        return AdaptiveOreBlocks.ADAPTIVE_DIAMOND_ORE.value();
    }

    public static Block getAdaptiveEmeraldOre() {
        return AdaptiveOreBlocks.ADAPTIVE_EMERALD_ORE.value();
    }

    public static List<Block> getAllAdaptiveOres() {
        return List.of(
            getAdaptiveCoalOre(),
            getAdaptiveIronOre(),
            getAdaptiveCopperOre(),
            getAdaptiveGoldOre(),
            getAdaptiveRedstoneOre(),
            getAdaptiveLapisOre(),
            getAdaptiveDiamondOre(),
            getAdaptiveEmeraldOre()
        );
    }
}

