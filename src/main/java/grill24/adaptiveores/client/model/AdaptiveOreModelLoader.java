package grill24.adaptiveores.client.model;

import grill24.adaptiveores.AdaptiveOres;
import grill24.adaptiveores.foundation.OreType;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;
import net.neoforged.neoforge.client.model.geometry.IUnbakedGeometry;

import javax.annotation.Nonnull;
import java.util.function.Function;

/**
 * Custom model loader for adaptive ore blocks.
 * Reads JSON model files with custom "loader" property.
 */
public class AdaptiveOreModelLoader implements IGeometryLoader<AdaptiveOreModelLoader.AdaptiveOreGeometry> {
    
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(AdaptiveOres.MODID, "adaptive_ore");
    
    @Override
    public AdaptiveOreGeometry read(@Nonnull JsonObject jsonObject, 
                                   @Nonnull JsonDeserializationContext context) {
        // Read ore type from JSON
        String oreTypeName = jsonObject.get("ore_type").getAsString();
        OreType oreType = OreType.valueOf(oreTypeName.toUpperCase());
        
        // Read overlay texture location
        String overlayTexture = jsonObject.has("overlay") 
            ? jsonObject.get("overlay").getAsString()
            : "adaptiveores:block/" + oreTypeName + "_ore_overlay";
        
        return new AdaptiveOreGeometry(oreType, ResourceLocation.parse(overlayTexture));
    }
    
    /**
     * Unbaked geometry for adaptive ore models.
     * This gets baked into the actual BakedModel during resource loading.
     */
    public static class AdaptiveOreGeometry implements IUnbakedGeometry<AdaptiveOreGeometry> {
        
        private final OreType oreType;
        private final ResourceLocation overlayTexture;
        
        public AdaptiveOreGeometry(OreType oreType, 
                                  ResourceLocation overlayTexture) {
            this.oreType = oreType;
            this.overlayTexture = overlayTexture;
        }
        
        @Override
        public BakedModel bake(@Nonnull IGeometryBakingContext context,
                              @Nonnull ModelBaker baker,
                              @Nonnull Function<Material, TextureAtlasSprite> spriteGetter,
                              @Nonnull ModelState modelState,
                              @Nonnull ItemOverrides overrides) {
            
            // Get the overlay texture sprite from the texture atlas
            Material material = new Material(
                InventoryMenu.BLOCK_ATLAS,
                overlayTexture
            );
            TextureAtlasSprite overlaySprite = spriteGetter.apply(material);
            
            // Bake an overlay model using a simple cube_all model
            // This ensures proper UV mapping and vertex winding, just like the backdrop
            BakedModel overlayModel = null;
            try {
                // Create an unbaked model that uses cube_all with our overlay texture
                // We'll use the block/cube_all parent model as a template
                ResourceLocation cubeAllLocation = ResourceLocation.withDefaultNamespace("block/cube_all");
                UnbakedModel cubeAllModel = baker.getModel(cubeAllLocation);
                
                if (cubeAllModel != null) {
                    // Bake the cube_all model with our overlay texture
                    // This gives us properly baked quads with correct UVs and vertex winding
                    overlayModel = cubeAllModel.bake(
                        baker,
                        model -> spriteGetter.apply(material), // Use our overlay sprite for all faces
                        modelState
                    );
                }
            } catch (Exception e) {
                AdaptiveOres.LOGGER.error("Failed to bake overlay model for {}: {}", overlayTexture, e.getMessage());
            }
            
            // Create and return the baked model
            return new AdaptiveOreModel(overlaySprite, overlayModel, oreType);
        }
    }
}
