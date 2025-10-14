package grill24.adaptiveores;

import com.mojang.logging.LogUtils;
import grill24.adaptiveores.data.AdaptiveOreSettingsRegistry;
import grill24.adaptiveores.foundation.AdaptiveOreBlocks;
import grill24.adaptiveores.foundation.AdaptiveOreBlockEntities;
import grill24.adaptiveores.platform.IRegistryHelper;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;

public class AdaptiveOres {
    public static final String MOD_ID = "adaptiveores";
    public static final Logger LOGGER = LogUtils.getLogger();

    // Enable to log render-time debug information from AdaptiveOreModel
    public static boolean RENDER_DEBUG = false;
    // When true, models should render only the overlay (no backdrop) for testing.
    public static boolean OVERLAY_ONLY_TEST_MODE = false;

    public static IRegistryHelper REGISTRY_HELPER;

    // Creative tab
    public static Holder<CreativeModeTab> ADAPTIVE_ORES_TAB;

    public static void init() {
        if (REGISTRY_HELPER == null) {
            throw new IllegalStateException("Registry helper not set!");
        }
        LOGGER.info("Initializing Adaptive Ores");

        // Register blocks, items, and block entities
        AdaptiveOreBlocks.init(REGISTRY_HELPER);
        AdaptiveOreBlockEntities.init(REGISTRY_HELPER);
        AdaptiveOreSettingsRegistry.init();
        ADAPTIVE_ORES_TAB = REGISTRY_HELPER.registerCreativeTab(ResourceLocation.fromNamespaceAndPath(MOD_ID, "adaptive_ores_tab"), () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
            .title(Component.translatable("itemGroup.adaptiveores.adaptive_ores"))
            .icon(() -> new ItemStack(AdaptiveOreBlocks.ADAPTIVE_IRON_ORE.value()))
            .displayItems((parameters, output) -> {
                // Add all adaptive ore items to the tab
                output.accept(AdaptiveOreBlocks.ADAPTIVE_COAL_ORE.value());
                output.accept(AdaptiveOreBlocks.ADAPTIVE_IRON_ORE.value());
                output.accept(AdaptiveOreBlocks.ADAPTIVE_COPPER_ORE.value());
                output.accept(AdaptiveOreBlocks.ADAPTIVE_GOLD_ORE.value());
                output.accept(AdaptiveOreBlocks.ADAPTIVE_REDSTONE_ORE.value());
                output.accept(AdaptiveOreBlocks.ADAPTIVE_LAPIS_ORE.value());
                output.accept(AdaptiveOreBlocks.ADAPTIVE_DIAMOND_ORE.value());
                output.accept(AdaptiveOreBlocks.ADAPTIVE_EMERALD_ORE.value());
            })
            .build());

        LOGGER.info("Adaptive Ores initialization complete");
    }
}
