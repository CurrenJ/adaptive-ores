package grill24.adaptiveores.data;

import grill24.adaptiveores.foundation.AdaptiveOreBlocks;
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
import java.util.List;
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
        add(AdaptiveOreBlocks.ADAPTIVE_COAL_ORE.value(), block -> createOreDrop(block, Items.COAL));

        // Iron
        add(AdaptiveOreBlocks.ADAPTIVE_IRON_ORE.value(), block -> createOreDrop(block, Items.RAW_IRON));

        // Copper
        add(AdaptiveOreBlocks.ADAPTIVE_COPPER_ORE.value(), this::createCopperOreDrops);

        // Gold
        add(AdaptiveOreBlocks.ADAPTIVE_GOLD_ORE.value(), block -> createOreDrop(block, Items.RAW_GOLD));

        // Redstone
        Block redstoneBlock = AdaptiveOreBlocks.ADAPTIVE_REDSTONE_ORE.value();
        add(redstoneBlock, createRedstoneOreDrops(redstoneBlock));

        Block lapisBlock = AdaptiveOreBlocks.ADAPTIVE_LAPIS_ORE.value();
        // Lapis (4-9 lapis, fortune applicable)
        add(lapisBlock, createLapisOreDrops(lapisBlock));

        // Diamond
        add(AdaptiveOreBlocks.ADAPTIVE_DIAMOND_ORE.value(), block -> createOreDrop(block, Items.DIAMOND));

        // Emerald
        add(AdaptiveOreBlocks.ADAPTIVE_EMERALD_ORE.value(), block -> createOreDrop(block, Items.EMERALD));
    }

    protected Iterable<Block> getKnownBlocks() {
        return List.of(
            AdaptiveOreBlocks.ADAPTIVE_COAL_ORE.value(),
            AdaptiveOreBlocks.ADAPTIVE_IRON_ORE.value(),
            AdaptiveOreBlocks.ADAPTIVE_COPPER_ORE.value(),
            AdaptiveOreBlocks.ADAPTIVE_GOLD_ORE.value(),
            AdaptiveOreBlocks.ADAPTIVE_REDSTONE_ORE.value(),
            AdaptiveOreBlocks.ADAPTIVE_LAPIS_ORE.value(),
            AdaptiveOreBlocks.ADAPTIVE_DIAMOND_ORE.value(),
            AdaptiveOreBlocks.ADAPTIVE_EMERALD_ORE.value()
        );
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
