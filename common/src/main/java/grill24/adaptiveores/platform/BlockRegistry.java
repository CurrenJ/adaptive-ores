package grill24.adaptiveores.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;
import grill24.adaptiveores.foundation.AdaptiveOreConstants;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.Optional;

/**
 * Platform abstraction for accessing registered blocks without depending on Architectury types at runtime.
 */
public class BlockRegistry {

    private static <T> Optional<Holder.Reference<T>> getHolder(HolderLookup.Provider registryAccess, ResourceKey<? extends Registry<T>> registryKey, ResourceLocation id)
    {
        return registryAccess.lookupOrThrow(registryKey).get(ResourceKey.create(registryKey, id));
    }

    private static <T> Holder.Reference<T> getRequiredHolder(HolderLookup.Provider registryAccess, ResourceKey<? extends Registry<T>> registryKey, ResourceLocation id)
    {
        return getHolder(registryAccess, registryKey, id).orElseThrow(() -> new IllegalStateException("Missing required registry entry: " + id));
    }

    public static Holder<Block> getAdaptiveCoalOre(HolderLookup.Provider registryAccess) {
        return getRequiredHolder(registryAccess, Registries.BLOCK, AdaptiveOreConstants.ADAPTIVE_COAL_ORE);
    }

    public static Holder<Block> getAdaptiveIronOre(HolderLookup.Provider registryAccess) {
        return getRequiredHolder(registryAccess, Registries.BLOCK, AdaptiveOreConstants.ADAPTIVE_IRON_ORE);
    }

    public static Holder<Block> getAdaptiveCopperOre(HolderLookup.Provider registryAccess) {
        return getRequiredHolder(registryAccess, Registries.BLOCK, AdaptiveOreConstants.ADAPTIVE_COPPER_ORE);
    }

    public static Holder<Block> getAdaptiveGoldOre(HolderLookup.Provider registryAccess) {
        return getRequiredHolder(registryAccess, Registries.BLOCK, AdaptiveOreConstants.ADAPTIVE_GOLD_ORE);
    }

    public static Holder<Block> getAdaptiveRedstoneOre(HolderLookup.Provider registryAccess) {
        return getRequiredHolder(registryAccess, Registries.BLOCK, AdaptiveOreConstants.ADAPTIVE_REDSTONE_ORE);
    }

    public static Holder<Block> getAdaptiveLapisOre(HolderLookup.Provider registryAccess) {
        return getRequiredHolder(registryAccess, Registries.BLOCK, AdaptiveOreConstants.ADAPTIVE_LAPIS_ORE);
    }

    public static Holder<Block> getAdaptiveDiamondOre(HolderLookup.Provider registryAccess) {
        return getRequiredHolder(registryAccess, Registries.BLOCK, AdaptiveOreConstants.ADAPTIVE_DIAMOND_ORE);
    }

    public static Holder<Block> getAdaptiveEmeraldOre(HolderLookup.Provider registryAccess) {
        return getRequiredHolder(registryAccess, Registries.BLOCK, AdaptiveOreConstants.ADAPTIVE_EMERALD_ORE);
    }

    public static List<Block> getAllAdaptiveOres(HolderLookup.Provider registries) {
        return List.of(
                AdaptiveOreConstants.ADAPTIVE_COAL_ORE,
                AdaptiveOreConstants.ADAPTIVE_IRON_ORE,
                AdaptiveOreConstants.ADAPTIVE_COPPER_ORE,
                AdaptiveOreConstants.ADAPTIVE_GOLD_ORE,
                AdaptiveOreConstants.ADAPTIVE_REDSTONE_ORE,
                AdaptiveOreConstants.ADAPTIVE_LAPIS_ORE,
                AdaptiveOreConstants.ADAPTIVE_DIAMOND_ORE,
                AdaptiveOreConstants.ADAPTIVE_EMERALD_ORE
        ).stream().map(id -> getRequiredHolder(registries, Registries.BLOCK, id).value()).toList();
    }
}

