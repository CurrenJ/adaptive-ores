package grill24.adaptiveores.foundation.blockentity;

import grill24.adaptiveores.AdaptiveOres;
import grill24.adaptiveores.content.block.AdaptiveOreBlock;
import grill24.adaptiveores.foundation.AdaptiveOreBlockEntities;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos.MutableBlockPos;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Block entity for adaptive ore blocks.
 * Stores the backdrop material and handles synchronization.
 */
@MethodsReturnNonnullByDefault
public class AdaptiveOreBlockEntity extends BlockEntity implements IAdaptiveOreBlockEntity {

    protected BlockState backdropMaterial;
    private boolean hasCustomBackdrop = false;
    private boolean backdropSampled = false;

    public AdaptiveOreBlockEntity(BlockPos pos, BlockState blockState) {
        super(AdaptiveOreBlockEntities.ADAPTIVE_ORE_BLOCK_ENTITY.value(), pos, blockState);
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
    public void setLevel(Level level) {
        super.setLevel(level);
        if (!backdropSampled && level != null && !level.isClientSide) {
            level.scheduleTick(getBlockPos(), getBlockState().getBlock(), 1);
        }
    }

    public void sampleAndSetBackdropMaterial() {
        if (level == null || level.isClientSide) {
            return; // Only sample on server side
        }

        BlockPos pos = getBlockPos();
        BlockState detected = detectDominantBackdrop(level, pos, true);
        setBackdropMaterial(detected);
        backdropSampled = true;
    }

    public static BlockState detectDominantBackdrop(BlockGetter level, BlockPos origin, boolean updateBlockEntity) {
        Map<BlockState, Integer> counts = new HashMap<>();
        MutableBlockPos sample = new MutableBlockPos();

        for(Direction direction : Direction.values())
        {
            sample.setWithOffset(origin, direction);
            BlockState s = level.getBlockState(sample);
            if (updateBlockEntity) {
            Optional<AdaptiveOreBlockEntity> adaptiveOreBlockEntity = (Optional<AdaptiveOreBlockEntity>) level.getBlockEntity(sample, AdaptiveOreBlockEntities.ADAPTIVE_ORE_BLOCK_ENTITY.value());
            if (adaptiveOreBlockEntity.isPresent())
            {
                s = adaptiveOreBlockEntity.get().getBackdropMaterial();
            }
            }
            // Simple check: only count non-air stone-like blocks
            if (!s.isAir() && AdaptiveOreBlock.isValidBackdropBlock(s)) {
                int weight = 1;
                if (!s.is(Blocks.STONE) && !s.is(Blocks.DEEPSLATE))
                {
                    weight = 4; // Weight non-stone backdrops higher
                }
                counts.put(s, counts.getOrDefault(s, 0) + weight);
            }
        }

        if (counts.isEmpty()) {
            return Blocks.STONE.defaultBlockState();
        }

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
            // clients receive block entity update packets.
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
        backdropSampled = true; // Mark as sampled since it was loaded from save
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        IAdaptiveOreBlockEntity.write(this, tag, registries, true);
        return tag;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
