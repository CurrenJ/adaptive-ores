package grill24.adaptiveores;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@EventBusSubscriber(modid = AdaptiveOres.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class AdaptiveOresClient {
    
    public AdaptiveOresClient(ModContainer container) {
        // Allows NeoForge to create a config screen for this mod's configs.
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        // Some client setup code
        AdaptiveOres.LOGGER.info("Adaptive Ores Client Setup");
        AdaptiveOres.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
    }

    // Model replacement is now handled by the custom model loader in AOClient
    // See: grill24.adaptiveores.client.AOClient and grill24.adaptiveores.client.model.AdaptiveOreModelLoader
}
