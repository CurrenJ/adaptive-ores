package grill24.adaptiveores.neoforge.data;

import grill24.adaptiveores.content.block.AdaptiveOreBlock;
import grill24.adaptiveores.platform.BlockRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {

    public ModBlockLootTables(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        // Coal
        add(BlockRegistry.getAdaptiveCoalOre(registries).value(), block -> createOreDrop(block, Items.COAL));

        // Iron
        add(BlockRegistry.getAdaptiveIronOre(registries).value(), block -> createOreDrop(block, Items.RAW_IRON));

        // Copper
        add(BlockRegistry.getAdaptiveCopperOre(registries).value(), this::createCopperOreDrops);

        // Gold
        add(BlockRegistry.getAdaptiveGoldOre(registries).value(), block -> createOreDrop(block, Items.RAW_GOLD));

        // Redstone
        Block redstoneBlock = BlockRegistry.getAdaptiveRedstoneOre(registries).value();
        add(redstoneBlock, createRedstoneOreDrops(redstoneBlock));

        Block lapisBlock = BlockRegistry.getAdaptiveLapisOre(registries).value();
        // Lapis (4-9 lapis, fortune applicable)
        add(lapisBlock, createLapisOreDrops(lapisBlock));

        // Diamond
        add(BlockRegistry.getAdaptiveDiamondOre(registries).value(), block -> createOreDrop(block, Items.DIAMOND));

        // Emerald
        add(BlockRegistry.getAdaptiveEmeraldOre(registries).value(), block -> createOreDrop(block, Items.EMERALD));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return BlockRegistry.getAllAdaptiveOres(registries);
    }
}
