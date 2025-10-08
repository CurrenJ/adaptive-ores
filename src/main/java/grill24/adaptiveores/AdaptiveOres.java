package grill24.adaptiveores;

import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import grill24.adaptiveores.foundation.AdaptiveOreBlocks;
import grill24.adaptiveores.foundation.AdaptiveOreBlockEntities;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import grill24.adaptiveores.foundation.blockentity.IAdaptiveOreBlockEntity;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(AdaptiveOres.MODID)
public class AdaptiveOres {
    public static final String MODID = "adaptiveores";
    public static final Logger LOGGER = LogUtils.getLogger();
    // Enable to log render-time debug information from AdaptiveOreModelNeoForge
    public static boolean RENDER_DEBUG = false;
    // When true, models should render only the overlay (no backdrop) for testing.
    public static boolean OVERLAY_ONLY_TEST_MODE = false;
    
    // Create a Deferred Register to hold CreativeModeTabs
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    // Creates a creative tab for adaptive ores
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> ADAPTIVE_ORES_TAB = CREATIVE_MODE_TABS.register("adaptive_ores_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.adaptiveores.adaptive_ores"))
            .withTabsBefore(CreativeModeTabs.NATURAL_BLOCKS)
            .icon(() -> AdaptiveOreBlocks.ADAPTIVE_IRON_ORE.get().asItem().getDefaultInstance())
            .displayItems((parameters, output) -> {
                // Add all adaptive ore items to the tab
                output.accept(AdaptiveOreBlocks.ADAPTIVE_COAL_ORE.get());
                output.accept(AdaptiveOreBlocks.ADAPTIVE_IRON_ORE.get());
                output.accept(AdaptiveOreBlocks.ADAPTIVE_COPPER_ORE.get());
                output.accept(AdaptiveOreBlocks.ADAPTIVE_GOLD_ORE.get());
                output.accept(AdaptiveOreBlocks.ADAPTIVE_REDSTONE_ORE.get());
                output.accept(AdaptiveOreBlocks.ADAPTIVE_LAPIS_ORE.get());
                output.accept(AdaptiveOreBlocks.ADAPTIVE_DIAMOND_ORE.get());
                output.accept(AdaptiveOreBlocks.ADAPTIVE_EMERALD_ORE.get());
            }).build());

    public AdaptiveOres(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register the Deferred Register to the mod event bus so blocks get registered
        AdaptiveOreBlocks.BLOCKS.register(modEventBus);
        AdaptiveOreBlocks.ITEMS.register(modEventBus);
        AdaptiveOreBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        
        // Register creative mode tabs
        CREATIVE_MODE_TABS.register(modEventBus);

        // Register ourselves for server and other game events
        NeoForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("Adaptive Ores Common Setup Started");
        
        // Any common setup code goes here
        
        LOGGER.info("Adaptive Ores Common Setup Complete");
    }

    // Add adaptive ores to the natural blocks tab as well
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
            event.accept(AdaptiveOreBlocks.ADAPTIVE_COAL_ORE.get());
            event.accept(AdaptiveOreBlocks.ADAPTIVE_IRON_ORE.get());
            event.accept(AdaptiveOreBlocks.ADAPTIVE_COPPER_ORE.get());
            event.accept(AdaptiveOreBlocks.ADAPTIVE_GOLD_ORE.get());
            event.accept(AdaptiveOreBlocks.ADAPTIVE_REDSTONE_ORE.get());
            event.accept(AdaptiveOreBlocks.ADAPTIVE_LAPIS_ORE.get());
            event.accept(AdaptiveOreBlocks.ADAPTIVE_DIAMOND_ORE.get());
            event.accept(AdaptiveOreBlocks.ADAPTIVE_EMERALD_ORE.get());
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("Adaptive Ores Server Starting");
        try {
            if (event.getServer() != null) {
                event.getServer().getCommands().getDispatcher().register(
                    Commands.literal("adaptiveores")
                        // debug <x> <y> <z>
                        .then(Commands.literal("debug")
                            .then(Commands.argument("x", IntegerArgumentType.integer())
                                .then(Commands.argument("y", IntegerArgumentType.integer())
                                    .then(Commands.argument("z", IntegerArgumentType.integer())
                                        .executes(ctx -> {
                                            int x = IntegerArgumentType.getInteger(ctx, "x");
                                            int y = IntegerArgumentType.getInteger(ctx, "y");
                                            int z = IntegerArgumentType.getInteger(ctx, "z");
                                            ServerLevel level = ctx.getSource().getLevel();
                                            BlockPos pos = new BlockPos(x, y, z);
                                            BlockEntity be = level.getBlockEntity(pos);
                                            if (be instanceof IAdaptiveOreBlockEntity adaptive) {
                                                Component msg = net.minecraft.network.chat.Component.literal("AdaptiveOre backdrop: " + adaptive.getBackdropMaterial().getBlock().getName().getString());
                                                ctx.getSource().sendSuccess(() -> msg, false);
                                            } else {
                                                ctx.getSource().sendFailure(net.minecraft.network.chat.Component.literal("No AdaptiveOre block entity at " + pos));
                                            }
                                            return 1;
                                        })))))
                        // overlayonly <true|false>
                        .then(Commands.literal("overlayonly")
                            .then(Commands.argument("value", BoolArgumentType.bool())
                                .executes(ctx -> {
                                    boolean value = BoolArgumentType.getBool(ctx, "value");
                                    AdaptiveOres.OVERLAY_ONLY_TEST_MODE = value;
                                    ctx.getSource().sendSuccess(() -> net.minecraft.network.chat.Component.literal("AdaptiveOres overlay-only test mode set to: " + value), true);
                                    return 1;
                                }))));
            }
        } catch (Exception ex) {
            LOGGER.warn("Failed to register adaptiveores debug command", ex);
        }
    }
}
