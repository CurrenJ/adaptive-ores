package grill24.adaptiveores.foundation;

import grill24.adaptiveores.AdaptiveOres;
import grill24.adaptiveores.foundation.blockentity.AdaptiveOreBlockEntity;
import grill24.adaptiveores.platform.registry.IRegistryHelper;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Holder;

public class AdaptiveOreBlockEntities {
    public static Holder<BlockEntityType<?>> ADAPTIVE_ORE_BLOCK_ENTITY;

    public static void init(IRegistryHelper registryHelper) {
        ADAPTIVE_ORE_BLOCK_ENTITY = registryHelper.registerBlockEntity(ResourceLocation.fromNamespaceAndPath(AdaptiveOres.MOD_ID, "adaptive_ore_block_entity"), () ->
            BlockEntityType.Builder.of(AdaptiveOreBlockEntity::new,
                    AdaptiveOreBlocks.ADAPTIVE_COAL_ORE.value(),
                    AdaptiveOreBlocks.ADAPTIVE_IRON_ORE.value(),
                    AdaptiveOreBlocks.ADAPTIVE_COPPER_ORE.value(),
                    AdaptiveOreBlocks.ADAPTIVE_GOLD_ORE.value(),
                    AdaptiveOreBlocks.ADAPTIVE_REDSTONE_ORE.value(),
                    AdaptiveOreBlocks.ADAPTIVE_LAPIS_ORE.value(),
                    AdaptiveOreBlocks.ADAPTIVE_DIAMOND_ORE.value(),
                    AdaptiveOreBlocks.ADAPTIVE_EMERALD_ORE.value()
            ).build(null));
    }
}
