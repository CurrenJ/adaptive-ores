package grill24.adaptiveores.neoforge;

import grill24.adaptiveores.AdaptiveOres;
import grill24.adaptiveores.content.block.AdaptiveOreBlock;
import grill24.adaptiveores.foundation.AdaptiveOreBlocks;
import grill24.adaptiveores.neoforge.client.model.AdaptiveOreModelLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.core.registries.Registries;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.IModBusEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@EventBusSubscriber(modid = AdaptiveOres.MOD_ID, value = Dist.CLIENT)
@Mod(value = AdaptiveOres.MOD_ID, dist = Dist.CLIENT)
public class AdaptiveOresNeoForgeClient implements IModBusEvent {
    
    public AdaptiveOresNeoForgeClient(ModContainer container) {
        // Allows NeoForge to create a config screen for this mod's configs.
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        // Some client setup code
        AdaptiveOresNeoForge.LOGGER.info("Adaptive Ores Client Setup");
        AdaptiveOresNeoForge.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());

        // Render types are now handled by the BakedModel#getRenderTypes override in AdaptiveOreModel
        ItemBlockRenderTypes.setRenderLayer(AdaptiveOreBlocks.ADAPTIVE_COAL_ORE.value(), net.minecraft.client.renderer.RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(AdaptiveOreBlocks.ADAPTIVE_IRON_ORE.value(), net.minecraft.client.renderer.RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(AdaptiveOreBlocks.ADAPTIVE_COPPER_ORE.value(), net.minecraft.client.renderer.RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(AdaptiveOreBlocks.ADAPTIVE_GOLD_ORE.value(), net.minecraft.client.renderer.RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(AdaptiveOreBlocks.ADAPTIVE_REDSTONE_ORE.value(), net.minecraft.client.renderer.RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(AdaptiveOreBlocks.ADAPTIVE_LAPIS_ORE.value(), net.minecraft.client.renderer.RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(AdaptiveOreBlocks.ADAPTIVE_DIAMOND_ORE.value(), net.minecraft.client.renderer.RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(AdaptiveOreBlocks.ADAPTIVE_EMERALD_ORE.value(), net.minecraft.client.renderer.RenderType.translucent());
    }

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
