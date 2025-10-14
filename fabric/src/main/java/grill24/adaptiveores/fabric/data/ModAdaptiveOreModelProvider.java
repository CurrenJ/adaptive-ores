package grill24.adaptiveores.fabric.data;

import grill24.adaptiveores.foundation.AdaptiveOreBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.world.level.block.Block;

public class ModAdaptiveOreModelProvider extends FabricModelProvider {

    public ModAdaptiveOreModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerators) {
        // Generate blockstates for all adaptive ore types
        adaptiveOreBlock(blockStateModelGenerators, AdaptiveOreBlocks.ADAPTIVE_COAL_ORE.value());
        adaptiveOreBlock(blockStateModelGenerators, AdaptiveOreBlocks.ADAPTIVE_IRON_ORE.value());
        adaptiveOreBlock(blockStateModelGenerators, AdaptiveOreBlocks.ADAPTIVE_COPPER_ORE.value());
        adaptiveOreBlock(blockStateModelGenerators, AdaptiveOreBlocks.ADAPTIVE_GOLD_ORE.value());
        adaptiveOreBlock(blockStateModelGenerators, AdaptiveOreBlocks.ADAPTIVE_REDSTONE_ORE.value());
        adaptiveOreBlock(blockStateModelGenerators, AdaptiveOreBlocks.ADAPTIVE_LAPIS_ORE.value());
        adaptiveOreBlock(blockStateModelGenerators, AdaptiveOreBlocks.ADAPTIVE_DIAMOND_ORE.value());
        adaptiveOreBlock(blockStateModelGenerators, AdaptiveOreBlocks.ADAPTIVE_EMERALD_ORE.value());
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerators) {
        // Generate item models for all adaptive ore types
//        adaptiveOreItem(itemModelGenerators, AdaptiveOreBlocks.ADAPTIVE_COAL_ORE.value());
//        adaptiveOreItem(itemModelGenerators, AdaptiveOreBlocks.ADAPTIVE_IRON_ORE.value());
//        adaptiveOreItem(itemModelGenerators, AdaptiveOreBlocks.ADAPTIVE_COPPER_ORE.value());
//        adaptiveOreItem(itemModelGenerators, AdaptiveOreBlocks.ADAPTIVE_GOLD_ORE.value());
//        adaptiveOreItem(itemModelGenerators, AdaptiveOreBlocks.ADAPTIVE_REDSTONE_ORE.value());
//        adaptiveOreItem(itemModelGenerators, AdaptiveOreBlocks.ADAPTIVE_LAPIS_ORE.value());
//        adaptiveOreItem(itemModelGenerators, AdaptiveOreBlocks.ADAPTIVE_DIAMOND_ORE.value());
//        adaptiveOreItem(itemModelGenerators, AdaptiveOreBlocks.ADAPTIVE_EMERALD_ORE.value());
    }

    /**
     * Registers a simple blockstate for an adaptive ore block.
     * The model location is automatically determined.
     */
    private void adaptiveOreBlock(BlockModelGenerators generator, Block block) {
        // generate simple cube all model with
        generator.createTrivialCube(block);
    }
}
