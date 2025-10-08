package grill24.adaptiveores.foundation.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;

public interface IAdaptiveOreBlockEntity {

    /**
     * Gets the backdrop material block state.
     * @return The backdrop material, or stone if none is set
     */
    BlockState getBackdropMaterial();

    /**
     * Sets the backdrop material block state.
     * @param backdropMaterial The new backdrop material
     */
    void setBackdropMaterial(BlockState backdropMaterial);

    /**
     * Checks if this ore has a custom backdrop material set.
     * @return true if a custom backdrop is set, false if using default
     */
    boolean hasCustomBackdrop();

    /**
     * Notify clients of updates (for rendering)
     */
    void notifyUpdate();

    /**
     * Get the block position
     */
    BlockPos getBlockPos();

    /**
     * Sets the backdrop material from a client update packet without triggering another
     * notifyUpdate() to avoid feedback loops. Implementations should update internal
     * fields but not call notifyUpdate().
     */
    void setBackdropMaterialFromPacket(BlockState backdropMaterial);

    /**
     * Default NBT reading method for backdrop material
     */
    static void read(IAdaptiveOreBlockEntity blockEntity, CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        if (tag.contains("BackdropMaterial")) {
            BlockState material = net.minecraft.nbt.NbtUtils.readBlockState(
                registries.lookupOrThrow(net.minecraft.core.registries.Registries.BLOCK), 
                tag.getCompound("BackdropMaterial")
            );
            if (clientPacket) {
                // On the client, apply the material without triggering another notify
                blockEntity.setBackdropMaterialFromPacket(material);
            } else {
                blockEntity.setBackdropMaterial(material);
            }
        }
    }

    /**
     * Default NBT writing method for backdrop material
     */
    static void write(IAdaptiveOreBlockEntity blockEntity, CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        // For client update packets we always include the backdrop material so the client
        // can update its ModelData and rendering immediately even when the backdrop is
        // the default (STONE). For persistent saves, only write custom backdrops.
        if (clientPacket) {
            tag.put("BackdropMaterial", net.minecraft.nbt.NbtUtils.writeBlockState(blockEntity.getBackdropMaterial()));
        } else if (blockEntity.hasCustomBackdrop()) {
            tag.put("BackdropMaterial", net.minecraft.nbt.NbtUtils.writeBlockState(blockEntity.getBackdropMaterial()));
        }
    }

    /**
     * Default safe NBT writing method
     */
    static void writeSafe(IAdaptiveOreBlockEntity blockEntity, CompoundTag tag, HolderLookup.Provider registries) {
        write(blockEntity, tag, registries, false);
    }
}