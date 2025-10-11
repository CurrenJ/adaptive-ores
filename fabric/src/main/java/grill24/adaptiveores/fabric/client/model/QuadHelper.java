package grill24.adaptiveores.fabric.client.model;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import grill24.adaptiveores.AdaptiveOres;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;

/**
 * Helper class for manipulating quads in Fabric.
 */
public class QuadHelper {

    /**
     * Offsets a quad slightly outward along its face normal to prevent z-fighting.
     * This creates a small gap between the backdrop and overlay layers.
     *
     * @param quad The original quad to offset
     * @param side The face direction (can be null for non-culled quads)
     * @param offset The offset amount in model units
     * @return A new BakedQuad with offset vertex positions
     */
    public static BakedQuad offsetQuad(BakedQuad quad, @Nullable Direction side, float offset) {
        // If no direction is specified, use the quad's own direction
        Direction direction = side != null ? side : quad.getDirection();

        // Get the vertex data from the original quad
        int[] originalVertices = quad.getVertices();
        int[] newVertices = originalVertices.clone();

        // Calculate offset vector based on face normal
        float offsetX = 0, offsetY = 0, offsetZ = 0;
        if (direction != null) {
            offsetX = direction.getStepX() * offset;
            offsetY = direction.getStepY() * offset;
            offsetZ = direction.getStepZ() * offset;
        }

        // Determine stride directly from quad's vertex array (4 vertices)
        int vertexSize = originalVertices.length / 4;
        if (vertexSize < 8) {
            // Unexpected format; avoid corrupting data
            if (AdaptiveOres.RENDER_DEBUG) {
                AdaptiveOres.LOGGER.warn("QuadHelper: Unexpected vertexSize {} for quad {} - skipping offset", vertexSize, quad.getDirection());
            }
            return quad;
        }

        for (int vertex = 0; vertex < 4; vertex++) {
            int baseIndex = vertex * vertexSize;

            // Get current position (stored as floats in int array)
            float x = Float.intBitsToFloat(newVertices[baseIndex]);
            float y = Float.intBitsToFloat(newVertices[baseIndex + 1]);
            float z = Float.intBitsToFloat(newVertices[baseIndex + 2]);

            // Apply offset
            x += offsetX;
            y += offsetY;
            z += offsetZ;

            // Store back as int bits
            newVertices[baseIndex] = Float.floatToRawIntBits(x);
            newVertices[baseIndex + 1] = Float.floatToRawIntBits(y);
            newVertices[baseIndex + 2] = Float.floatToRawIntBits(z);
        }

        // Create new quad with offset vertices
        return new BakedQuad(
            newVertices,
            quad.getTintIndex(),
            quad.getDirection(),
            quad.getSprite(),
            quad.isShade()
        );
    }
}



