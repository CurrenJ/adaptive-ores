package grill24.adaptiveores.neoforge.client;

import grill24.adaptiveores.AdaptiveOres;
import grill24.adaptiveores.content.block.AdaptiveOreBlock;
import grill24.adaptiveores.neoforge.AdaptiveOresNeoForge;
import grill24.adaptiveores.neoforge.client.model.AdaptiveOreModelLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.registries.Registries;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.IModBusEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

/**
 * Client-side setup and event handlers for AdaptiveOres
 */
@EventBusSubscriber(modid = AdaptiveOres.MOD_ID, value = Dist.CLIENT)
public class AOClient implements IModBusEvent {

    @SubscribeEvent
    public static void registerModelLoaders(ModelEvent.RegisterGeometryLoaders event) {
        event.register(AdaptiveOreModelLoader.ID, new AdaptiveOreModelLoader());
        AdaptiveOresNeoForge.LOGGER.info("Registered adaptive ore model loader");
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        BlockColor grassTint = (state, world, pos, tintIndex) -> {
            if (world != null && pos != null) {
                return net.minecraft.client.renderer.BiomeColors.getAverageGrassColor(world, pos);
            } else {
                return 0x91BD59; // Default grass color
            }
        };

        if(Minecraft.getInstance() instanceof Minecraft minecraft && minecraft.level != null)
        {
            minecraft.level.registryAccess().lookupOrThrow(Registries.BLOCK)
                    .filterElements(b -> b instanceof AdaptiveOreBlock)
                    .listElements()
                    .forEach(blockRef -> event.register(grassTint, blockRef.value()));
        }
    }
}
