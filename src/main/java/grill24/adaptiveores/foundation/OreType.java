package grill24.adaptiveores.foundation;

import net.minecraft.resources.ResourceLocation;

/**
 * Enum defining the different types of ores that can be adaptive.
 * Each ore type has an associated overlay texture.
 */
public enum OreType {
    COAL("coal"),
    IRON("iron"),
    COPPER("copper"),
    GOLD("gold"),
    REDSTONE("redstone"),
    LAPIS("lapis"),
    DIAMOND("diamond"),
    EMERALD("emerald");

    private final String name;
    private final ResourceLocation overlayTexture;

    OreType(String name) {
        this.name = name;
        this.overlayTexture = ResourceLocation.fromNamespaceAndPath("adaptiveores", "block/overlay/" + name + "_ore_overlay");
    }

    public String getName() {
        return name;
    }

    public ResourceLocation getOverlayTexture() {
        return overlayTexture;
    }
}