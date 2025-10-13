package grill24.adaptiveores.mixin;

import com.mojang.serialization.Codec;
import grill24.adaptiveores.foundation.AdaptiveOreBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.MonsterRoomFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Predicate;

@Mixin(MonsterRoomFeature.class)
public abstract class MonsterRoomFeatureMixin extends Feature<NoneFeatureConfiguration> {
    @Unique
    private static WeightedStateProvider adaptiveores$SPAWNABLE;

    public MonsterRoomFeatureMixin(Codec<NoneFeatureConfiguration> codec) {
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

    @Redirect(method = "place", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/feature/MonsterRoomFeature;safeSetBlock(Lnet/minecraft/world/level/WorldGenLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Ljava/util/function/Predicate;)V"))
    private void place(MonsterRoomFeature instance, WorldGenLevel worldGenLevel, BlockPos pos, BlockState blockState, Predicate<BlockState> predicate) {
        if (blockState.is(Blocks.MOSSY_COBBLESTONE)) {
            if (worldGenLevel.getRandom().nextInt(5) == 0) {
                BlockState oreState = adaptiveores$getSpawnable().getState(worldGenLevel.getRandom(), BlockPos.ZERO);
                super.safeSetBlock(worldGenLevel, pos, oreState, predicate);
                // Set backdrop to mossy cobblestone
                BlockEntity be = worldGenLevel.getBlockEntity(pos);
                if (be instanceof grill24.adaptiveores.foundation.blockentity.AdaptiveOreBlockEntity adaptive) {
                    adaptive.setBackdropMaterial(Blocks.MOSSY_COBBLESTONE.defaultBlockState());
                }
            } else {
                super.safeSetBlock(worldGenLevel, pos, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), predicate);
            }
        } else {
            super.safeSetBlock(worldGenLevel, pos, blockState, predicate);
        }
    }

    @Redirect(method = "place", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/SpawnerBlockEntity;setEntityId(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/util/RandomSource;)V"))
    private void setEntityId(SpawnerBlockEntity spawnerBlockEntity, EntityType<?> entityType, RandomSource random, FeaturePlaceContext<NoneFeatureConfiguration> context) {
        Level level = context.level().getLevel();
        Holder<Biome> biome = level.getBiome(spawnerBlockEntity.getBlockPos());
        if (biome.value().getBaseTemperature() < 0.15f && entityType == EntityType.SKELETON) {
            spawnerBlockEntity.setEntityId(EntityType.STRAY, level.getRandom());
        } else if (biome.value().getPrecipitationAt(context.origin()) == Biome.Precipitation.NONE && entityType == EntityType.ZOMBIE) {
            spawnerBlockEntity.setEntityId(EntityType.HUSK, level.getRandom());
        } else {
            spawnerBlockEntity.setEntityId(entityType, level.getRandom());
        }
    }
}
