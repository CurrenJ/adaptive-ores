package grill24.adaptiveores.content.block;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import grill24.adaptiveores.foundation.OreType;
import grill24.adaptiveores.foundation.blockentity.IAdaptiveOreBlockEntity;
import grill24.adaptiveores.AdaptiveOres;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
// client-only classes are accessed reflectively at runtime to avoid classloading issues on dedicated servers

/**
 * Adaptive Ore Block that can have any stone-type block as its backdrop.
 * Uses dual-layer rendering: backdrop material + ore overlay texture.
 */
@ParametersAreNonnullByDefault
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
            AdaptiveOres.LOGGER.debug("AdaptiveOreBlock.onPlace: placed adaptive ore {} at {} (old={})", this, pos, oldState.getBlock());
        }
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, net.minecraft.world.entity.LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (level != null && !level.isClientSide()) {
            AdaptiveOres.LOGGER.debug("AdaptiveOreBlock.setPlacedBy: adaptive ore {} placed by {} at {}", this, placer == null ? "<unknown>" : placer.getName().getString(), pos);
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
         // We validate the candidate (appliedState) rather than the existing block at `pos` so
         // players can always manually override the backdrop even after the block entity has
         // auto-detected a backdrop during worldgen or onLoad.
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

         return !isAdaptiveOre && canOcclude;
     }

     @Override
     public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                           Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (player == null || !player.mayBuild()) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        // Sneak + empty-hand right-click: debug block entity backdrop
        if (player != null && player.isShiftKeyDown() && hand == InteractionHand.MAIN_HAND && player.getItemInHand(hand).isEmpty()) {
            // Server: existing BE text message
            if (!level.isClientSide()) {
                BlockEntity be = level.getBlockEntity(pos);
                if (be instanceof IAdaptiveOreBlockEntity adaptive) {
                    BlockState backdrop = adaptive.getBackdropMaterial();
                    player.sendSystemMessage(net.minecraft.network.chat.Component.literal("AdaptiveOre backdrop: " + backdrop.getBlock().getName().getString()));
                } else {
                    player.sendSystemMessage(net.minecraft.network.chat.Component.literal("No AdaptiveOre block entity at " + pos));
                }
            }

            // Client: send more detailed model debug info (quad counts) using reflection so classes aren't linked on server
            if (level.isClientSide()) {
                try {
                    BlockState backdropState = getBackdropMaterial(level, pos);

                    Class<?> mcClass = Class.forName("net.minecraft.client.Minecraft");
                    Object mc = mcClass.getMethod("getInstance").invoke(null);
                    Object blockRenderer = mcClass.getMethod("getBlockRenderer").invoke(mc);

                    Class<?> brClass = blockRenderer.getClass();
                    java.lang.reflect.Method getModelMethod = brClass.getMethod("getBlockModel", net.minecraft.world.level.block.state.BlockState.class);
                    Object model = getModelMethod.invoke(blockRenderer, backdropState);

                    Class<?> bakedModelClass = Class.forName("net.minecraft.client.resources.model.BakedModel");
                    Class<?> randomClass = Class.forName("net.minecraft.util.RandomSource");
                    Object rand = randomClass.getMethod("create").invoke(null);

                    java.lang.reflect.Method getQuadsMethod = bakedModelClass.getMethod("getQuads", net.minecraft.world.level.block.state.BlockState.class, net.minecraft.core.Direction.class, randomClass);

                    int sideCount = 0;
                    for (net.minecraft.core.Direction d : net.minecraft.core.Direction.values()) {
                        @SuppressWarnings("unchecked")
                        java.util.List<?> qs = (java.util.List<?>) getQuadsMethod.invoke(model, backdropState, d, rand);
                        sideCount += qs == null ? 0 : qs.size();
                    }

                    @SuppressWarnings("unchecked")
                    java.util.List<?> general = (java.util.List<?>) getQuadsMethod.invoke(model, backdropState, null, rand);
                    int generalCount = general == null ? 0 : general.size();
                    int total = sideCount + generalCount;

                    player.sendSystemMessage(net.minecraft.network.chat.Component.literal("AdaptiveOre model debug: backdrop=" + backdropState.getBlock().getName().getString() + " totalQuads=" + total + " sideQuads=" + sideCount + " generalQuads=" + generalCount));
                } catch (Throwable t) {
                    // If anything fails, fall back to a simple backdrop message
                    player.sendSystemMessage(net.minecraft.network.chat.Component.literal("AdaptiveOre debug: backdrop only available"));
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
            // If the block entity hasn't been created yet (common right after worldgen),
            // attempt to create it on the server by re-setting the block state which
            // triggers block entity creation. If we're on the client, return pass so
            // the server can handle creation.
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
                // If still missing, give up and let default interaction handle it
                return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            }
        }

        if (level.isClientSide()) {
            // Client-side early-success to mirror server acknowledgement
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
        return new grill24.adaptiveores.foundation.blockentity.AdaptiveOreBlockEntity(pos, state);
     }

     @Override
     public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        // This tick is scheduled by worldgen mixin after placing adaptive ore into chunk sections
        // We ensure the block entity exists and push an update so clients receive rendering data
        try {
            if (!level.isClientSide()) {
                BlockState current = level.getBlockState(pos);
                if (current.getBlock() == this) {
                    // Re-set the block to trigger block entity creation/registration and send block updates
                    level.setBlock(pos, current, 3);
                    BlockEntity be = level.getBlockEntity(pos);
                    if (be instanceof grill24.adaptiveores.foundation.blockentity.AdaptiveOreBlockEntity adaptive) {
                        adaptive.notifyUpdate();
                    } else {
                        AdaptiveOres.LOGGER.debug("AdaptiveOreBlock.tick: no block entity present at {} after setBlock", pos);
                    }
                }
            }
        } catch (Throwable t) {
            AdaptiveOres.LOGGER.warn("AdaptiveOreBlock.tick: error during scheduled init at {}", pos, t);
        }
     }

     /**
      * Get the backdrop material for rendering purposes
      */
     public static BlockState getBackdropMaterial(BlockAndTintGetter level, BlockPos pos) {
         BlockEntity be = level.getBlockEntity(pos);
         if (be instanceof IAdaptiveOreBlockEntity adaptiveOreBE) {
             return adaptiveOreBE.getBackdropMaterial();
         }
         // Default to stone if no backdrop is set
         return net.minecraft.world.level.block.Blocks.STONE.defaultBlockState();
     }
 }
