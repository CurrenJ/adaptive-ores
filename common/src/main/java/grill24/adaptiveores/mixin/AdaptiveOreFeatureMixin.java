package grill24.adaptiveores.mixin;

import com.mojang.serialization.Codec;
import grill24.adaptiveores.foundation.AdaptiveOreBlocks;
import grill24.adaptiveores.AdaptiveOres;
import grill24.adaptiveores.content.block.AdaptiveOreBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;

import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.BulkSectionAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.BitSet;
import java.util.Iterator;
import java.util.Optional;

@Mixin(OreFeature.class)
public abstract class AdaptiveOreFeatureMixin extends Feature<OreConfiguration> {

    public AdaptiveOreFeatureMixin(Codec<OreConfiguration> codec) {
        super(codec);
    }

    @Inject(method = "doPlace", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/level/chunk/LevelChunkSection;setBlockState(IIILnet/minecraft/world/level/block/state/BlockState;Z)Lnet/minecraft/world/level/block/state/BlockState;"), locals = LocalCapture.CAPTURE_FAILSOFT, require = 1)
    public void onSetBlockState(WorldGenLevel level, RandomSource random, OreConfiguration config, double minX, double maxX, double minZ, double maxZ, double minY, double maxY, int x, int y, int z, int width, int height, CallbackInfoReturnable<Boolean> cir, int i, BitSet bitset, BlockPos.MutableBlockPos blockpos$mutableblockpos, int j, double[] adouble, BulkSectionAccess bulksectionaccess, int j4, double d9, double d11, double d13, double d15, int k4, int l, int i1, int j1, int k1, int l1, int i2, double d5, int j2, double d6, int k2, double d7, int l2, LevelChunkSection levelchunksection, int i3, int j3, int k3, BlockState blockstate, Iterator<?> var57, OreConfiguration.TargetBlockState oreconfiguration$targetblockstate) {
        adaptiveores$onPlaceOre(level, random, blockpos$mutableblockpos, bulksectionaccess, levelchunksection, i3, j3, k3, blockstate, oreconfiguration$targetblockstate.state);
    }

    @SuppressWarnings("unused")
    @Unique
    private static void adaptiveores$onPlaceOre(WorldGenLevel level, RandomSource random, BlockPos.MutableBlockPos blockpos$mutableblockpos, BulkSectionAccess bulksectionaccess, LevelChunkSection levelchunksection, int x, int y, int z, BlockState replacing, BlockState placing) {
        if (bulksectionaccess == null || blockpos$mutableblockpos == null || random == null) {
            AdaptiveOres.LOGGER.info("AdaptiveOreFeatureMixin: Early exit - null parameters");
            return;
        }

        // Replace vanilla ores with adaptive versions if we have stone-type blocks nearby
        // First, see if the block being placed is an ore -> map to adaptive ore
        Block adaptiveOreBlock = adaptiveores$getAdaptiveOreEquivalent(placing);
        boolean adaptiveFromPlacing = false;


        // If the placing block is an ore, note that the adaptive mapping came from the placing block
        if (adaptiveOreBlock != null) {
            adaptiveFromPlacing = true;
        } else {
            // If the placing block isn't an ore but the block being replaced is a vanilla ore,
            // place the adaptive equivalent of the replaced ore instead. This prevents non-ore
            // features (e.g. granite/andesite/diorite patches) from overwriting vanilla ores.
            Block adaptiveFromReplacing = adaptiveores$getAdaptiveOreEquivalent(replacing);
            if (adaptiveFromReplacing != null) {
                adaptiveOreBlock = adaptiveFromReplacing;
            }
        }

        // If the ore was derived from the placing block (i.e. we're placing a vanilla ore)
        // and the block being replaced is vanilla STONE or DEEPSLATE, prefer to keep the
        // vanilla ore variant instead of replacing it with our adaptive ore (identical visuals,
        // unnecessary overhead).
        if (adaptiveOreBlock != null && adaptiveFromPlacing && replacing != null) {
            BlockState detectedBackdrop = AdaptiveOreBlock.detectDominantBackdrop(level, blockpos$mutableblockpos);
            if (detectedBackdrop.is(Blocks.STONE) || detectedBackdrop.is(Blocks.DEEPSLATE)) {
                return; // Keep the vanilla ore instead
            }
        }

        if (adaptiveOreBlock != null) {
             // Place the adaptive ore instead (do not set block entity backdrop during worldgen)
             BlockState adaptiveOreState = adaptiveOreBlock.defaultBlockState();
             Optional<BlockState> placed = adaptiveores$tryPlaceBlockState(level, bulksectionaccess, adaptiveOreState, blockpos$mutableblockpos);

             if (placed.isPresent() && AdaptiveOres.RENDER_DEBUG) {
                 AdaptiveOres.LOGGER.info("AdaptiveOreFeatureMixin: Successfully placed adaptive ore, previous state was: {}",
                     placed.get().getBlock().getName().getString());
             } else if(AdaptiveOres.RENDER_DEBUG) {
                 AdaptiveOres.LOGGER.warn("AdaptiveOreFeatureMixin: Failed to place adaptive ore at {}", blockpos$mutableblockpos);
             }

             // Schedule a short-delayed server tick so the block entity can be created/initialized
             // and its client-rendering data (ModelData / BE packet) can be sent. Worldgen writes
             // directly to chunk sections and doesn't create block entities, so we ask the server
             // tick system to handle initialization.
             if (placed.isPresent() && !level.isClientSide()) {
                 try {
                     BlockPos pos = blockpos$mutableblockpos.immutable();
                     // Schedule after 1 tick using TickPriority to match available overloads
                     level.scheduleTick(pos, adaptiveOreBlock, 1);
                 } catch (Throwable t) {
                     AdaptiveOres.LOGGER.warn("AdaptiveOreFeatureMixin: failed to schedule tick for adaptive ore placement", t);
                 }
             }
        }
    }

    @Unique
    private static Block adaptiveores$getAdaptiveOreEquivalent(BlockState state) {
        if (state == null) return null;

        // Map vanilla ores to adaptive equivalents
        if (state.is(BlockTags.COAL_ORES)) {
            return AdaptiveOreBlocks.ADAPTIVE_COAL_ORE.value();
        } else if (state.is(BlockTags.IRON_ORES)) {
            return AdaptiveOreBlocks.ADAPTIVE_IRON_ORE.value();
        } else if (state.is(BlockTags.COPPER_ORES)) {
            return AdaptiveOreBlocks.ADAPTIVE_COPPER_ORE.value();
        } else if (state.is(BlockTags.GOLD_ORES)) {
            return AdaptiveOreBlocks.ADAPTIVE_GOLD_ORE.value();
        } else if (state.is(BlockTags.REDSTONE_ORES)) {
            return AdaptiveOreBlocks.ADAPTIVE_REDSTONE_ORE.value();
        } else if (state.is(BlockTags.LAPIS_ORES)) {
            return AdaptiveOreBlocks.ADAPTIVE_LAPIS_ORE.value();
        } else if (state.is(BlockTags.DIAMOND_ORES)) {
            return AdaptiveOreBlocks.ADAPTIVE_DIAMOND_ORE.value();
        } else if (state.is(BlockTags.EMERALD_ORES)) {
            return AdaptiveOreBlocks.ADAPTIVE_EMERALD_ORE.value();
        }
        return null;
    }

    // Note: backdrop detection and setting during worldgen removed. Block entities initialize themselves on load.

    @Unique
    private static Optional<BlockState> adaptiveores$tryPlaceBlockState(WorldGenLevel level, BulkSectionAccess bulkSectionAccess, BlockState blockstate, BlockPos pos) {
        // Perform a fast chunk-section write during worldgen to avoid triggering block updates
        if (level.ensureCanWrite(pos)) {
            int sx = SectionPos.sectionRelative(pos.getX());
            int sy = SectionPos.sectionRelative(pos.getY());
            int sz = SectionPos.sectionRelative(pos.getZ());
            LevelChunkSection section = bulkSectionAccess.getSection(pos);
            if (section != null) {
                return Optional.of(section.setBlockState(sx, sy, sz, blockstate, false));
            }
        }
        return Optional.empty();
    }

    // backdrop setting during worldgen intentionally removed - block entities initialize themselves on load
}
