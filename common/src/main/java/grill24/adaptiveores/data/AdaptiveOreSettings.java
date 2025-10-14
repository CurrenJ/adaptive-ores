package grill24.adaptiveores.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

import java.util.List;

public record AdaptiveOreSettings(
    BlockState oreBlock,
    BlockState defaultBackdrop,
    List<PlacementModifier> placementModifiers
) {
    public static final Codec<AdaptiveOreSettings> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
            BlockState.CODEC.fieldOf("ore_block").forGetter(AdaptiveOreSettings::oreBlock),
            BlockState.CODEC.fieldOf("default_backdrop").forGetter(AdaptiveOreSettings::defaultBackdrop),
            PlacementModifier.CODEC.listOf().fieldOf("placement_modifiers").forGetter(AdaptiveOreSettings::placementModifiers)
        ).apply(instance, AdaptiveOreSettings::new)
    );
}
