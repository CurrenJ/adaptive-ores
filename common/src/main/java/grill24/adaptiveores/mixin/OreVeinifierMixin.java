package grill24.adaptiveores.mixin;

import grill24.adaptiveores.foundation.AdaptiveOreBlocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.OreVeinifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = OreVeinifier.class)
public class OreVeinifierMixin {

    @Redirect(method = "method_40547", at = @At(value = "FIELD", target = "Lnet/minecraft/world/level/levelgen/OreVeinifier$VeinType;ore:Lnet/minecraft/world/level/block/state/BlockState;"))
    private static BlockState redirectOre(OreVeinifier.VeinType veinType) {
        if (veinType == OreVeinifier.VeinType.COPPER) {
            return AdaptiveOreBlocks.ADAPTIVE_COPPER_ORE.value().defaultBlockState();
        } else if (veinType == OreVeinifier.VeinType.IRON) {
            return AdaptiveOreBlocks.ADAPTIVE_IRON_ORE.value().defaultBlockState();
        }
        return veinType.ore;
    }
}
