package grill24.adaptiveores.fabric;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import grill24.adaptiveores.AdaptiveOres;
import grill24.adaptiveores.foundation.blockentity.IAdaptiveOreBlockEntity;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * Registers commands for Adaptive Ores in Fabric.
 */
public class AdaptiveOresCommands {

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(
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
                                grill24.adaptiveores.AdaptiveOres.OVERLAY_ONLY_TEST_MODE = value;
                                ctx.getSource().sendSuccess(() -> Component.literal("AdaptiveOres overlay-only test mode set to: " + value), true);
                                return 1;
                            })))
                    // renderdebug <true|false>
                    .then(Commands.literal("renderdebug")
                        .then(Commands.argument("value", BoolArgumentType.bool())
                            .executes(ctx -> {
                                boolean value = BoolArgumentType.getBool(ctx, "value");
                                grill24.adaptiveores.AdaptiveOres.RENDER_DEBUG = value;
                                ctx.getSource().sendSuccess(() -> Component.literal("AdaptiveOres render debug mode set to: " + value), true);
                                return 1;
                            })))
            );
        });

        AdaptiveOres.LOGGER.info("Registered Adaptive Ores commands");
    }
}

