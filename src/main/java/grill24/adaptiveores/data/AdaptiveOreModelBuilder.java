package grill24.adaptiveores.data;

import com.google.gson.JsonObject;
import grill24.adaptiveores.client.model.AdaptiveOreModelLoader;
import grill24.adaptiveores.foundation.OreType;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.CustomLoaderBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import javax.annotation.Nonnull;

/**
 * Custom loader builder for adaptive ore models in data generation.
 * This generates JSON models that use the "adaptiveores:adaptive_ore" loader.
 */
public class AdaptiveOreModelBuilder extends CustomLoaderBuilder<BlockModelBuilder> {

    private final OreType oreType;
    private ResourceLocation overlayTexture;

    public AdaptiveOreModelBuilder(BlockModelBuilder parent, ExistingFileHelper existingFileHelper, OreType oreType) {
        super(
            AdaptiveOreModelLoader.ID,
            parent,
            existingFileHelper,
            false // Don't allow inline vanilla elements as fallback
        );
        this.oreType = oreType;
        // Use the default overlay texture from OreType
        this.overlayTexture = oreType.getOverlayTexture();
    }

    /**
     * Override the default overlay texture location.
     * @param overlayTexture The texture location to use
     * @return This builder for chaining
     */
    public AdaptiveOreModelBuilder overlayTexture(ResourceLocation overlayTexture) {
        this.overlayTexture = overlayTexture;
        return this;
    }

    /**
     * Override the default overlay texture location.
     * @param overlayTexture The texture location string to use
     * @return This builder for chaining
     */
    public AdaptiveOreModelBuilder overlayTexture(String overlayTexture) {
        this.overlayTexture = ResourceLocation.parse(overlayTexture);
        return this;
    }

    @Nonnull
    @Override
    public JsonObject toJson(JsonObject json) {
        // Add ore_type field
        json.addProperty("ore_type", oreType.getName());

        // Add overlay texture field
        json.addProperty("overlay", overlayTexture.toString());

        // Call super to add the loader property and other standard fields
        return super.toJson(json);
    }
}
