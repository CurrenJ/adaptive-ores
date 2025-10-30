package grill24.adaptiveores.content.block;

import grill24.adaptiveores.AdaptiveOres;
import grill24.adaptiveores.foundation.OreType;
import grill24.adaptiveores.foundation.blockentity.AdaptiveOreBlockEntity;
import grill24.adaptiveores.foundation.blockentity.IAdaptiveOreBlockEntity;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

/**
 * Adaptive Ore Block that can have any stone-type block as its backdrop.
 * Uses dual-layer rendering: backdrop material + ore overlay texture.
 */
@MethodsReturnNonnullByDefault
public class AdaptiveOreBlock extends Block implements EntityBlock {

    private final OreType oreType;

    public AdaptiveOreBlock(Properties properties, OreType oreType) {
        super(properties);
        this.oreType = oreType;
    }

    public OreType getOreType() {
        return oreType;
    }

    @Nullable
    public IAdaptiveOreBlockEntity getAdaptiveOreBlockEntity(BlockGetter worldIn, BlockPos pos) {
        BlockEntity blockEntity = worldIn.getBlockEntity(pos);
        if (blockEntity instanceof IAdaptiveOreBlockEntity adaptiveOreBE) {
            return adaptiveOreBE;
        }
        return null;
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);
        if (level != null && !level.isClientSide()) {
            AdaptiveOres.LOGGER.info("AdaptiveOreBlock.onPlace: placed adaptive ore {} at {} (old={})", this, pos, oldState.getBlock());
        }

        // Schedule backdrop initialization to happen after placement
        if (level != null && !level.isClientSide()) {
            level.scheduleTick(pos, this, 2);
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        // Detect a dominant backdrop and apply it
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof AdaptiveOreBlockEntity adaptive) {
            adaptive.sampleAndSetBackdropMaterial();
        }
    }

    /**
     * Determines if a block state can be accepted as a backdrop material.
     * Only allows stone-type blocks with full cube shapes.
     */
    @Nullable
    public BlockState getAcceptedBackdropState(Level level, BlockPos pos, ItemStack item, Direction face) {
        if (!(item.getItem() instanceof BlockItem blockItem)) {
            return null;
        }

        Block block = blockItem.getBlock();
        BlockState appliedState = block.defaultBlockState();

        // Don't allow using an adaptive ore block as a backdrop
        if (!isValidBackdropBlock(appliedState)) {
            return null;
        }

        // Check if the candidate block is a stone-type / full-cube block when placed at this position.
        if (level != null) {
            VoxelShape shape = appliedState.getShape(level, pos);
            if (shape.isEmpty() || !shape.bounds().equals(Shapes.block().bounds())) {
                return null;
            }
        }

        return appliedState;
    }

    /**
     * Simple check to determine if a block is stone-type.
     * This can be expanded to use tags later.
     */
    private static final AABB CUBE_BOUNDS = Shapes.block().bounds();
    public static boolean isValidBackdropBlock(BlockState state) {
        boolean isAdaptiveOre = state.getBlock() instanceof AdaptiveOreBlock;
        boolean canOcclude = state.canOcclude();
        boolean isOre = state.is(BlockTags.COAL_ORES) || state.is(BlockTags.IRON_ORES)
                     || state.is(BlockTags.COPPER_ORES) || state.is(BlockTags.GOLD_ORES)
                     || state.is(BlockTags.REDSTONE_ORES) || state.is(BlockTags.LAPIS_ORES)
                     || state.is(BlockTags.DIAMOND_ORES) || state.is(BlockTags.EMERALD_ORES);

        return !isAdaptiveOre && canOcclude && !isOre;
    }

    @Override
    public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                          Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (player == null || !player.mayBuild()) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        // Sneak + empty-hand right-click: debug block entity backdrop
        if (player.isShiftKeyDown() && hand == InteractionHand.MAIN_HAND && player.getItemInHand(hand).isEmpty()) {
            if (!level.isClientSide()) {
                BlockEntity be = level.getBlockEntity(pos);
                if (be instanceof IAdaptiveOreBlockEntity adaptive) {
                    BlockState backdrop = adaptive.getBackdropMaterial();
                    player.sendSystemMessage(net.minecraft.network.chat.Component.literal("AdaptiveOre backdrop: " + backdrop.getBlock().getName().getString()));
                } else {
                    player.sendSystemMessage(net.minecraft.network.chat.Component.literal("No AdaptiveOre block entity at " + pos));
                }
            }
            return ItemInteractionResult.SUCCESS;
        }

        Direction face = hitResult.getDirection();
        BlockState backdropMaterial = getAcceptedBackdropState(level, pos, stack, face);

        if (backdropMaterial == null) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        IAdaptiveOreBlockEntity adaptiveOreBE = getAdaptiveOreBlockEntity(level, pos);
        if (adaptiveOreBE == null) {
            if (level.isClientSide()) {
                return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            }

            try {
                level.setBlock(pos, state, 3);
            } catch (Throwable t) {
                AdaptiveOres.LOGGER.warn("AdaptiveOreBlock.useItemOn: failed to set block to create BE at {}", pos, t);
            }

            adaptiveOreBE = getAdaptiveOreBlockEntity(level, pos);
            if (adaptiveOreBE == null) {
                return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            }
        }

        if (level.isClientSide()) {
            return ItemInteractionResult.SUCCESS;
        }

        // Set the new backdrop material
        adaptiveOreBE.setBackdropMaterial(backdropMaterial);

        // Play sound
        level.playSound(null, pos, backdropMaterial.getSoundType().getPlaceSound(),
                       SoundSource.BLOCKS, 1.0F, 0.8F);

        // Consume item if not creative
        if (!player.isCreative()) {
            stack.shrink(1);
            if (stack.isEmpty()) {
                player.setItemInHand(hand, ItemStack.EMPTY);
            }
        }

        return ItemInteractionResult.SUCCESS;
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AdaptiveOreBlockEntity(pos, state);
    }
}
