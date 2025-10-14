package grill24.adaptiveores.data;

import grill24.adaptiveores.platform.BlockRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.function.BiConsumer;

public class CommonBlockLootTables extends BlockLootSubProvider {

    public CommonBlockLootTables(HolderLookup.Provider registries) {
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

    protected Iterable<Block> getKnownBlocks() {
        return BlockRegistry.getAllAdaptiveOres(registries);
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> biConsumer) {
        this.generate();
        Set<ResourceKey<LootTable>> set = new HashSet();

        for(Block block : this.getKnownBlocks()) {
            if (block.isEnabled(this.enabledFeatures)) {
                ResourceKey<LootTable> resourceKey = block.getLootTable();
                if (resourceKey != BuiltInLootTables.EMPTY && set.add(resourceKey)) {
                    LootTable.Builder builder = (LootTable.Builder)this.map.remove(resourceKey);
                    if (builder == null) {
                        throw new IllegalStateException(String.format(Locale.ROOT, "Missing loottable '%s' for '%s'", resourceKey.location(), BuiltInRegistries.BLOCK.getKey(block)));
                    }

                    biConsumer.accept(resourceKey, builder);
                }
            }
        }

        if (!this.map.isEmpty()) {
            throw new IllegalStateException("Created block loot tables for non-blocks: " + String.valueOf(this.map.keySet()));
        }
    }

}
