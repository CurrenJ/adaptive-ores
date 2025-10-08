package grill24.adaptiveores.data;

import grill24.adaptiveores.foundation.AdaptiveOreBlocks;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.List;
import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {

    public ModBlockLootTables(HolderLookup.Provider registries) {
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
        add(AdaptiveOreBlocks.ADAPTIVE_REDSTONE_ORE.value(), createRedstoneOreDrops(AdaptiveOreBlocks.ADAPTIVE_REDSTONE_ORE.value()));

        // Lapis (4-9 lapis, fortune applicable)
        add(AdaptiveOreBlocks.ADAPTIVE_LAPIS_ORE.value(), createLapisOreDrops(AdaptiveOreBlocks.ADAPTIVE_LAPIS_ORE.value()));

        // Diamond
        add(AdaptiveOreBlocks.ADAPTIVE_DIAMOND_ORE.value(), block -> createOreDrop(block, Items.DIAMOND));

        // Emerald
        add(AdaptiveOreBlocks.ADAPTIVE_EMERALD_ORE.value(), block -> createOreDrop(block, Items.EMERALD));
    }

    @Override
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
}
