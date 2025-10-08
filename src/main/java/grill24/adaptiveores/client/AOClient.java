package grill24.adaptiveores.client;

import grill24.adaptiveores.AdaptiveOres;
import grill24.adaptiveores.client.model.AdaptiveOreModelLoader;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.IModBusEvent;
import net.neoforged.neoforge.client.event.ModelEvent;

/**
 * Client-side setup and event handlers for AdaptiveOres
 */
@EventBusSubscriber(modid = AdaptiveOres.MODID, value = Dist.CLIENT)
public class AOClient implements IModBusEvent {
    
    @SubscribeEvent
    public static void registerModelLoaders(ModelEvent.RegisterGeometryLoaders event) {
        event.register(AdaptiveOreModelLoader.ID, new AdaptiveOreModelLoader());
        AdaptiveOres.LOGGER.info("Registered adaptive ore model loader");
    }
}
