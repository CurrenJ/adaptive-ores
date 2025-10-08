package grill24.adaptiveores.client.model;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.client.model.pipeline.QuadBakingVertexConsumer;

/**
 * Helper for building quads from texture sprites
 */
public class QuadHelper {
    
    /**
     * Create a single quad for a cube face (no offset)
     */
    public static BakedQuad createQuad(Direction direction, TextureAtlasSprite sprite) {
        return createQuad(direction, sprite, 0f);
    }

    /**
     * Create a single quad for a cube face with a small offset along the face normal.
     * This is useful to avoid z-fighting when rendering an overlay exactly on top of a backdrop.
     *
     * @param direction face direction
     * @param sprite texture sprite
     * @param offset small offset (in model units, where 1.0 = full block). Positive moves outward along the face normal.
     */
    public static BakedQuad createQuad(Direction direction, TextureAtlasSprite sprite, float offset) {
        QuadBakingVertexConsumer builder = new QuadBakingVertexConsumer();
        builder.setSprite(sprite);
        builder.setDirection(direction);
        builder.setShade(true);  // Enable shading for proper lighting
        builder.setHasAmbientOcclusion(true);  // Enable AO
        
        if (grill24.adaptiveores.AdaptiveOres.RENDER_DEBUG) {
            // Use debug level to avoid spamming info logs during rendering
            grill24.adaptiveores.AdaptiveOres.LOGGER.debug("Creating quad for direction={}, sprite={}, UV range: u0={}, v0={}, u16={}, v16={}",
                direction, sprite.toString(),
                sprite.getU(0), sprite.getV(0), sprite.getU(16), sprite.getV(16));
        }
        
        // Define vertices based on direction
        // Each face is a square from 0,0,0 to 1,1,1
        // UV coordinates need to use sprite.getU/getV to map to texture atlas
        switch (direction) {
            case DOWN -> {
                // Bottom face (y=0)
                putVertex(builder, 0, 0, 1, sprite.getU(0), sprite.getV(16), direction, offset);
                putVertex(builder, 1, 0, 1, sprite.getU(16), sprite.getV(16), direction, offset);
                putVertex(builder, 1, 0, 0, sprite.getU(16), sprite.getV(0), direction, offset);
                putVertex(builder, 0, 0, 0, sprite.getU(0), sprite.getV(0), direction, offset);
            }
            case UP -> {
                // Top face (y=1)
                putVertex(builder, 0, 1, 0, sprite.getU(0), sprite.getV(0), direction, offset);
                putVertex(builder, 1, 1, 0, sprite.getU(16), sprite.getV(0), direction, offset);
                putVertex(builder, 1, 1, 1, sprite.getU(16), sprite.getV(16), direction, offset);
                putVertex(builder, 0, 1, 1, sprite.getU(0), sprite.getV(16), direction, offset);
            }
            case NORTH -> {
                // North face (z=0)
                putVertex(builder, 1, 1, 0, sprite.getU(16), sprite.getV(0), direction, offset);
                putVertex(builder, 0, 1, 0, sprite.getU(0), sprite.getV(0), direction, offset);
                putVertex(builder, 0, 0, 0, sprite.getU(0), sprite.getV(16), direction, offset);
                putVertex(builder, 1, 0, 0, sprite.getU(16), sprite.getV(16), direction, offset);
            }
            case SOUTH -> {
                // South face (z=1)
                putVertex(builder, 0, 1, 1, sprite.getU(0), sprite.getV(0), direction, offset);
                putVertex(builder, 1, 1, 1, sprite.getU(16), sprite.getV(0), direction, offset);
                putVertex(builder, 1, 0, 1, sprite.getU(16), sprite.getV(16), direction, offset);
                putVertex(builder, 0, 0, 1, sprite.getU(0), sprite.getV(16), direction, offset);
            }
            case WEST -> {
                // West face (x=0)
                putVertex(builder, 0, 1, 0, sprite.getU(0), sprite.getV(0), direction, offset);
                putVertex(builder, 0, 1, 1, sprite.getU(16), sprite.getV(0), direction, offset);
                putVertex(builder, 0, 0, 1, sprite.getU(16), sprite.getV(16), direction, offset);
                putVertex(builder, 0, 0, 0, sprite.getU(0), sprite.getV(16), direction, offset);
            }
            case EAST -> {
                // East face (x=1)
                putVertex(builder, 1, 1, 1, sprite.getU(0), sprite.getV(0), direction, offset);
                putVertex(builder, 1, 1, 0, sprite.getU(16), sprite.getV(0), direction, offset);
                putVertex(builder, 1, 0, 0, sprite.getU(16), sprite.getV(16), direction, offset);
                putVertex(builder, 1, 0, 1, sprite.getU(0), sprite.getV(16), direction, offset);
            }
        }
        
        BakedQuad quad = builder.bakeQuad();
        
        if (grill24.adaptiveores.AdaptiveOres.RENDER_DEBUG) {
            grill24.adaptiveores.AdaptiveOres.LOGGER.debug("Baked quad for direction={}, tintIndex={}, sprite={}",
                direction, quad.getTintIndex(), quad.getSprite().toString());
        }
        
        return quad;
    }
    
    /**
     * Add a vertex to the quad builder using VertexConsumer API
     */
    private static void putVertex(VertexConsumer builder, 
                                  float x, float y, float z, 
                                  float u, float v,
                                  Direction direction,
                                  float offset) {
        int nx = direction.getStepX();
        int ny = direction.getStepY();
        int nz = direction.getStepZ();
        
        // Apply a small offset along the face normal to avoid z-fighting when overlaying exactly
        float ox = x + nx * offset;
        float oy = y + ny * offset;
        float oz = z + nz * offset;

        // Don't set light here - let the BakedQuad builder handle it
        // The renderer will apply proper lighting when rendering
        builder.addVertex(ox, oy, oz)
               .setColor(0xFFFFFFFF)  // White color (no tint)
               .setUv(u, v)
               .setNormal(nx, ny, nz);
    }
}
