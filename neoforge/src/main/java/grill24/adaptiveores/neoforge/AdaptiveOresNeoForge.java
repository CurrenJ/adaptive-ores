package grill24.adaptiveores.neoforge;

import grill24.adaptiveores.AdaptiveOres;
import grill24.adaptiveores.RegistryHelper;
import grill24.adaptiveores.foundation.blockentity.IAdaptiveOreBlockEntity;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import grill24.adaptiveores.neoforge.platform.NeoForgeRegistryHelper;
import grill24.adaptiveores.platform.IRegistryHelper;

@Mod(AdaptiveOres.MOD_ID)
public class AdaptiveOresNeoForge {
    public static final Logger LOGGER = LogUtils.getLogger();
    // Enable to log render-time debug information from AdaptiveOreModelNeoForge
    public static boolean RENDER_DEBUG = true;
    // When true, models should render only the overlay (no backdrop) for testing.
    public static boolean OVERLAY_ONLY_TEST_MODE = false;

    public AdaptiveOresNeoForge(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events
        NeoForge.EVENT_BUS.register(this);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        // Set up registry helper
        IRegistryHelper helper = RegistryHelper.create();
        if (helper instanceof NeoForgeRegistryHelper nfHelper) {
            nfHelper.registerToEventBus(modEventBus);
        }
        grill24.adaptiveores.AdaptiveOres.REGISTRY_HELPER = helper;

        // Initialize common code
        grill24.adaptiveores.AdaptiveOres.init();
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("Adaptive Ores Common Setup Started");
        
        // Any common setup code goes here
        
        LOGGER.info("Adaptive Ores Common Setup Complete");
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
                                                Component msg = Component.literal("AdaptiveOre backdrop: " + adaptive.getBackdropMaterial().getBlock().getName().getString());
                                                ctx.getSource().sendSuccess(() -> msg, false);
                                            } else {
                                                ctx.getSource().sendFailure(Component.literal("No AdaptiveOre block entity at " + pos));
                                            }
                                            return 1;
                                        })))))
                        // overlayonly <true|false>
                        .then(Commands.literal("overlayonly")
                            .then(Commands.argument("value", BoolArgumentType.bool())
                                .executes(ctx -> {
                                    boolean value = BoolArgumentType.getBool(ctx, "value");
                                    AdaptiveOresNeoForge.OVERLAY_ONLY_TEST_MODE = value;
                                    ctx.getSource().sendSuccess(() -> Component.literal("AdaptiveOres overlay-only test mode set to: " + value), true);
                                    return 1;
                                }))));
            }
        } catch (Exception ex) {
            LOGGER.warn("Failed to register adaptiveores debug command", ex);
        }
    }
}
