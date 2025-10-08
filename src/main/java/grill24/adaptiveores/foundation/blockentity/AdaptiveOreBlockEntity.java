package grill24.adaptiveores.foundation.blockentity;

import grill24.adaptiveores.content.block.AdaptiveOreBlock;
import grill24.adaptiveores.foundation.AdaptiveOreBlockEntities;
import grill24.adaptiveores.AdaptiveOres;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos.MutableBlockPos;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Block entity for adaptive ore blocks.
 * Stores the backdrop material and handles synchronization.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AdaptiveOreBlockEntity extends BlockEntity implements IAdaptiveOreBlockEntity {

    protected BlockState backdropMaterial;
    private boolean hasCustomBackdrop = false;
    private boolean backdropInitialized = false;

    public AdaptiveOreBlockEntity(BlockPos pos, BlockState blockState) {
        super(AdaptiveOreBlockEntities.ADAPTIVE_ORE_BLOCK_ENTITY.get(), pos, blockState);
        this.backdropMaterial = Blocks.STONE.defaultBlockState(); // Default backdrop
    }

    @Override
    public BlockState getBackdropMaterial() {
        return backdropMaterial != null ? backdropMaterial : Blocks.STONE.defaultBlockState();
    }

    @Override
    public void setBackdropMaterial(BlockState backdropMaterial) {
        this.backdropMaterial = backdropMaterial;
        this.hasCustomBackdrop = !backdropMaterial.is(Blocks.STONE);
        notifyUpdate();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        // Log load invocation and early-return reasons
        if (level == null) {
            AdaptiveOres.LOGGER.debug("AdaptiveOreBlockEntity.onLoad: level is null for {}", getBlockPos());
            return;
        }
        if (level.isClientSide()) {
            return;
        }
        if (backdropInitialized) {
            AdaptiveOres.LOGGER.debug("AdaptiveOreBlockEntity.onLoad: already initialized for {}", getBlockPos());
            return;
        }

        if (!hasCustomBackdrop) {
            // Detect a dominant backdrop and apply it
            BlockState detected = detectDominantBackdrop(level, getBlockPos());
            // detectDominantBackdrop never returns null (defaults to STONE), so just apply
            setBackdropMaterial(detected);
        }

        backdropInitialized = true;
    }

    private BlockState detectDominantBackdrop(Level level, BlockPos origin) {
        Map<BlockState, Integer> counts = new HashMap<>();
        MutableBlockPos sample = new MutableBlockPos();

        for(Direction direction : Direction.values())
        {
            sample.setWithOffset(origin, direction);
            BlockState s = level.getBlockState(sample);
            // Simple check: only count non-air stone-like blocks
            if (!s.isAir() && AdaptiveOreBlock.isValidBackdropBlock(s)) {
                counts.put(s, counts.getOrDefault(s, 0) + 1);
            }
        }

        if (counts.isEmpty()) {
            AdaptiveOres.LOGGER.debug("detectDominantBackdrop: no candidate blocks found around {} - defaulting to STONE", origin);
            return Blocks.STONE.defaultBlockState();
        }

        // Log the candidate counts for debugging
        // counts.forEach((state, count) -> AdaptiveOres.LOGGER.debug("detectDominantBackdrop: candidate {} -> {} occurrences", state.getBlock(), count));

        BlockState chosen = counts.entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse(Blocks.STONE.defaultBlockState());
        return chosen;
    }

    @Override
    public boolean hasCustomBackdrop() {
        return hasCustomBackdrop;
    }

    @Override
    public void setBackdropMaterialFromPacket(BlockState backdropMaterial) {
        // Apply the backdrop state from a client-bound packet without triggering another update
        this.backdropMaterial = backdropMaterial;
        this.hasCustomBackdrop = !backdropMaterial.is(Blocks.STONE);
        // Do NOT call notifyUpdate() here to avoid a feedback loop of updates
    }

    @Override
    public void notifyUpdate() {
        setChanged();
        if (level != null) {
            // Always request a block update so both server and client re-render and
            // clients receive block entity update packets. This helps ensure the
            // ModelData is refreshed when the backdrop changes.
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        IAdaptiveOreBlockEntity.writeSafe(this, tag, registries);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        IAdaptiveOreBlockEntity.read(this, tag, registries, false);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        IAdaptiveOreBlockEntity.write(this, tag, registries, true);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        super.handleUpdateTag(tag, lookupProvider);
        IAdaptiveOreBlockEntity.read(this, tag, lookupProvider, true);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
