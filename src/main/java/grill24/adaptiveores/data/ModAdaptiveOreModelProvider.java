package grill24.adaptiveores.data;

import grill24.adaptiveores.AdaptiveOres;
import grill24.adaptiveores.foundation.AdaptiveOreBlocks;
import grill24.adaptiveores.foundation.OreType;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

/**
 * Data generator for adaptive ore models and blockstates.
 * Generates JSON files that use the custom "adaptiveores:adaptive_ore" loader.
 */
public class ModAdaptiveOreModelProvider extends BlockStateProvider {

    public ModAdaptiveOreModelProvider(PackOutput output) {
        super(output, AdaptiveOres.MODID, new ExistingFileHelper(
            java.util.Collections.emptyList(),
            java.util.Collections.emptySet(),
            false,
            null,
            null
        ));
    }

    @Override
    protected void registerStatesAndModels() {
        // Generate models and blockstates for all adaptive ore types
        adaptiveOreBlock(AdaptiveOreBlocks.ADAPTIVE_COAL_ORE, OreType.COAL);
        adaptiveOreBlock(AdaptiveOreBlocks.ADAPTIVE_IRON_ORE, OreType.IRON);
        adaptiveOreBlock(AdaptiveOreBlocks.ADAPTIVE_COPPER_ORE, OreType.COPPER);
        adaptiveOreBlock(AdaptiveOreBlocks.ADAPTIVE_GOLD_ORE, OreType.GOLD);
        adaptiveOreBlock(AdaptiveOreBlocks.ADAPTIVE_REDSTONE_ORE, OreType.REDSTONE);
        adaptiveOreBlock(AdaptiveOreBlocks.ADAPTIVE_LAPIS_ORE, OreType.LAPIS);
        adaptiveOreBlock(AdaptiveOreBlocks.ADAPTIVE_DIAMOND_ORE, OreType.DIAMOND);
        adaptiveOreBlock(AdaptiveOreBlocks.ADAPTIVE_EMERALD_ORE, OreType.EMERALD);
    }

    /**
     * Creates a block model and blockstate for an adaptive ore block.
     *
     * @param block The block to generate for
     * @param oreType The ore type (determines overlay texture)
     */
    private void adaptiveOreBlock(DeferredBlock<? extends Block> block, OreType oreType) {
        String blockName = block.getId().getPath();

        // Create the block model using our custom loader
        BlockModelBuilder model = models().getBuilder(blockName)
            .customLoader((builder, helper) -> new AdaptiveOreModelBuilder(builder, helper, oreType))
            .end();

        // Create a simple blockstate that references the model
        simpleBlock(block.get(), model);

        // Create the item model (same as block model)
        simpleBlockItem(block.get(), model);
    }
}
