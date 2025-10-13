package grill24.adaptiveores.mixin;

import com.mojang.serialization.Codec;
import grill24.adaptiveores.foundation.AdaptiveOreBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.GeodeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.GeodeConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.util.random.SimpleWeightedRandomList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;

@Mixin(GeodeFeature.class)
public abstract class GeodeFeatureMixin extends Feature<GeodeConfiguration> {
    @Unique
    private static WeightedStateProvider adaptiveores$SPAWNABLE;

    @Unique
    private boolean adaptiveores$hasOres;

    public GeodeFeatureMixin(Codec<GeodeConfiguration> codec) {
        super(codec);
    }

    @Unique
    private static WeightedStateProvider adaptiveores$getSpawnable() {
        if (adaptiveores$SPAWNABLE == null) {
            adaptiveores$SPAWNABLE = new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
                    .add(AdaptiveOreBlocks.ADAPTIVE_COAL_ORE.value().defaultBlockState(), 1)
                    .add(AdaptiveOreBlocks.ADAPTIVE_IRON_ORE.value().defaultBlockState(), 1)
                    .add(AdaptiveOreBlocks.ADAPTIVE_COPPER_ORE.value().defaultBlockState(), 1)
                    .add(AdaptiveOreBlocks.ADAPTIVE_GOLD_ORE.value().defaultBlockState(), 1)
                    .add(AdaptiveOreBlocks.ADAPTIVE_LAPIS_ORE.value().defaultBlockState(), 1)
                    .add(AdaptiveOreBlocks.ADAPTIVE_REDSTONE_ORE.value().defaultBlockState(), 1)
                    .add(AdaptiveOreBlocks.ADAPTIVE_DIAMOND_ORE.value().defaultBlockState(), 1)
                    .add(AdaptiveOreBlocks.ADAPTIVE_EMERALD_ORE.value().defaultBlockState(), 1)
                    .build());
        }
        return adaptiveores$SPAWNABLE;
    }

    @Inject(method = "place", at = @At("HEAD"))
    private void setHasOres(FeaturePlaceContext<GeodeConfiguration> context, CallbackInfoReturnable<Boolean> cir) {
        adaptiveores$hasOres = context.level().getRandom().nextBoolean();
    }

    @Redirect(method = "place", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/feature/GeodeFeature;safeSetBlock(Lnet/minecraft/world/level/WorldGenLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Ljava/util/function/Predicate;)V"))
    private void place(GeodeFeature instance, WorldGenLevel worldGenLevel, BlockPos pos, BlockState blockState, Predicate<BlockState> predicate) {
        if (adaptiveores$hasOres && blockState.is(Blocks.AMETHYST_BLOCK)) {
            if (worldGenLevel.getRandom().nextInt(5) == 0) {
                BlockState oreState = adaptiveores$getSpawnable().getState(worldGenLevel.getRandom(), BlockPos.ZERO);
                super.safeSetBlock(worldGenLevel, pos, oreState, predicate);
                // Set backdrop to amethyst block
                BlockEntity be = worldGenLevel.getBlockEntity(pos);
                if (be instanceof grill24.adaptiveores.foundation.blockentity.AdaptiveOreBlockEntity adaptive) {
                    adaptive.setBackdropMaterial(Blocks.AMETHYST_BLOCK.defaultBlockState());
                }
            } else {
                super.safeSetBlock(worldGenLevel, pos, Blocks.AMETHYST_BLOCK.defaultBlockState(), predicate);
            }
        } else {
            super.safeSetBlock(worldGenLevel, pos, blockState, predicate);
        }
    }
}
