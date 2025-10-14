package grill24.adaptiveores.neoforge.data;

import grill24.adaptiveores.AdaptiveOres;
import grill24.adaptiveores.foundation.AdaptiveOreBlocks;
import grill24.adaptiveores.foundation.OreType;
import net.minecraft.data.PackOutput;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

/**
 * Data generator for adaptive ore models and blockstates.
 * Generates JSON files that use the custom "adaptiveores:adaptive_ore" loader.
 */
public class ModAdaptiveOreModelProvider extends BlockStateProvider {

    public ModAdaptiveOreModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, AdaptiveOres.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        // Generate models and blockstates for all adaptive ore types
        adaptiveOreBlock(AdaptiveOreBlocks.ADAPTIVE_COAL_ORE.value(), OreType.COAL);
        adaptiveOreBlock(AdaptiveOreBlocks.ADAPTIVE_IRON_ORE.value(), OreType.IRON);
        adaptiveOreBlock(AdaptiveOreBlocks.ADAPTIVE_COPPER_ORE.value(), OreType.COPPER);
        adaptiveOreBlock(AdaptiveOreBlocks.ADAPTIVE_GOLD_ORE.value(), OreType.GOLD);
        adaptiveOreBlock(AdaptiveOreBlocks.ADAPTIVE_REDSTONE_ORE.value(), OreType.REDSTONE);
        adaptiveOreBlock(AdaptiveOreBlocks.ADAPTIVE_LAPIS_ORE.value(), OreType.LAPIS);
        adaptiveOreBlock(AdaptiveOreBlocks.ADAPTIVE_DIAMOND_ORE.value(), OreType.DIAMOND);
        adaptiveOreBlock(AdaptiveOreBlocks.ADAPTIVE_EMERALD_ORE.value(), OreType.EMERALD);
    }

    /**
     * Creates a block model and blockstate for an adaptive ore block.
     *
     * @param block The block to generate for
     * @param oreType The ore type (determines overlay texture)
     */
    private void adaptiveOreBlock(Block block, OreType oreType) {
        String blockName = ModelLocationUtils.getModelLocation(block).getPath();

        // Create the block model using our custom loader
        BlockModelBuilder model = models().getBuilder(blockName)
            .customLoader((builder, helper) -> new AdaptiveOreModelBuilder(builder, helper, oreType))
            .end();

        // Create a simple blockstate that references the model
        simpleBlock(block, model); // Blockstate gen moved to common

        // Create the item model (same as block model)
        simpleBlockItem(block, model);
    }
}
