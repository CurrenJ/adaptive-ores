package grill24.adaptiveores.fabric.client;

import grill24.adaptiveores.AdaptiveOres;
import grill24.adaptiveores.foundation.OreType;
import grill24.adaptiveores.fabric.client.model.AdaptiveOreBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;

/**
 * Fabric UnbakedModel for adaptive ore models.
 * This class provides the model baking logic that creates an
 * AdaptiveOreBakedModel which composes a backdrop + overlay.
 */
public class AdaptiveOreUnbakedModel implements UnbakedModel {

    private final OreType oreType;
    private final ResourceLocation overlayTexture;

    // Resolved dependency (cube_all) will be stored here by resolveParents
    private UnbakedModel cubeAllUnbaked = null;

    public AdaptiveOreUnbakedModel(OreType oreType, ResourceLocation overlayTexture) {
        this.oreType = oreType;
        this.overlayTexture = overlayTexture;
        AdaptiveOres.LOGGER.info("AdaptiveOreUnbakedModel created for {} with overlay texture: {}", oreType.getName(), overlayTexture);
    }

    @Override
    public Collection<ResourceLocation> getDependencies() {
        // Depend on the built-in cube_all model so we can reuse its geometry for the overlay
        return Collections.singleton(ResourceLocation.parse("block/cube_all"));
    }

    @Override
    public void resolveParents(Function<ResourceLocation, UnbakedModel> resolver) {
        try {
            this.cubeAllUnbaked = resolver.apply(ResourceLocation.parse("block/cube_all"));
            AdaptiveOres.LOGGER.info("AdaptiveOreUnbakedModel: Successfully resolved cube_all parent for {}", oreType.getName());
        } catch (Exception e) {
            AdaptiveOres.LOGGER.error("AdaptiveOreUnbakedModel: failed to resolve cube_all parent for {}: {}", oreType.getName(), e.getMessage());
            this.cubeAllUnbaked = null;
        }
    }

    @Override
    public BakedModel bake(ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState) {
        // Prepare material and sprite for overlay
        Material overlayMaterial = new Material(InventoryMenu.BLOCK_ATLAS, overlayTexture);
        TextureAtlasSprite overlaySprite = null;
        try {
            overlaySprite = spriteGetter.apply(overlayMaterial);
        } catch (Exception e) {
            AdaptiveOres.LOGGER.error("AdaptiveOreUnbakedModel: FAILED to get overlay sprite {} for {}: {}", overlayTexture, oreType.getName(), e.getMessage(), e);
        }

        // Make it final for lambda capture
        final TextureAtlasSprite finalOverlaySprite = overlaySprite;

        // Bake overlay using the resolved cube_all unbaked model (if available)
        BakedModel overlayModel = null;
        try {
            if (cubeAllUnbaked != null && finalOverlaySprite != null) {
                // Always use the overlay sprite for any texture requests during bake of cube_all
                Function<Material, TextureAtlasSprite> overlaySpriteGetter = material -> {
                    if (AdaptiveOres.RENDER_DEBUG) {
                        AdaptiveOres.LOGGER.debug("AdaptiveOreUnbakedModel: Forcing texture {} to overlay sprite {} for ore {}", material.texture(), overlayTexture, oreType.getName());
                    }
                    return finalOverlaySprite;
                };

                overlayModel = cubeAllUnbaked.bake(baker, overlaySpriteGetter, modelState);

                if (overlayModel != null && AdaptiveOres.RENDER_DEBUG) {
                    AdaptiveOres.LOGGER.info("AdaptiveOreUnbakedModel: Successfully baked overlay model for {}", oreType.getName());
                } else {
                    AdaptiveOres.LOGGER.error("AdaptiveOreUnbakedModel: Baked overlay model is NULL for {}", oreType.getName());
                }
            } else {
                if (cubeAllUnbaked == null) {
                    AdaptiveOres.LOGGER.error("AdaptiveOreUnbakedModel: Cannot bake overlay - cubeAllUnbaked is NULL for {}", oreType.getName());
                }
                if (finalOverlaySprite == null) {
                    AdaptiveOres.LOGGER.error("AdaptiveOreUnbakedModel: Cannot bake overlay - overlaySprite is NULL for {}", oreType.getName());
                }
            }
        } catch (Exception e) {
            AdaptiveOres.LOGGER.error("AdaptiveOreUnbakedModel: failed to bake overlay model for {} with texture {}: {}",
                oreType.getName(), overlayTexture, e.getMessage(), e);
        }

        return new AdaptiveOreBakedModel(overlaySprite, overlayModel, oreType);
    }
}
