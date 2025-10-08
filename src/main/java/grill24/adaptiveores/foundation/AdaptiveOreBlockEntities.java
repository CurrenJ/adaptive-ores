package grill24.adaptiveores.foundation;

import grill24.adaptiveores.AdaptiveOres;
import grill24.adaptiveores.foundation.blockentity.AdaptiveOreBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class AdaptiveOreBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = 
            DeferredRegister.create(net.minecraft.core.registries.Registries.BLOCK_ENTITY_TYPE, AdaptiveOres.MODID);

    public static final Supplier<BlockEntityType<AdaptiveOreBlockEntity>> ADAPTIVE_ORE_BLOCK_ENTITY = 
            BLOCK_ENTITIES.register("adaptive_ore_block_entity", () ->
                    BlockEntityType.Builder.of(AdaptiveOreBlockEntity::new,
                            AdaptiveOreBlocks.ADAPTIVE_COAL_ORE.get(),
                            AdaptiveOreBlocks.ADAPTIVE_IRON_ORE.get(),
                            AdaptiveOreBlocks.ADAPTIVE_COPPER_ORE.get(),
                            AdaptiveOreBlocks.ADAPTIVE_GOLD_ORE.get(),
                            AdaptiveOreBlocks.ADAPTIVE_REDSTONE_ORE.get(),
                            AdaptiveOreBlocks.ADAPTIVE_LAPIS_ORE.get(),
                            AdaptiveOreBlocks.ADAPTIVE_DIAMOND_ORE.get(),
                            AdaptiveOreBlocks.ADAPTIVE_EMERALD_ORE.get()
                    ).build(null));
}