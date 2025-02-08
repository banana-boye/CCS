package net.orion.create_cold_sweat.datagen;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Objects;

public record FluidTemperatureType(List<Pair<Fluid, Double>> values) {

    public static final Codec<Pair<Fluid, Double>> PAIR_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("fluid").forGetter(pair -> ForgeRegistries.FLUIDS.getKey(pair.getFirst())),
            Codec.DOUBLE.fieldOf("temperature").forGetter(Pair::getSecond)
    ).apply(instance, (fluidId, temperature) -> new Pair<>(
            Objects.requireNonNull(ForgeRegistries.FLUIDS.getValue(fluidId)), temperature
    )));

    public static final Codec<FluidTemperatureType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            PAIR_CODEC.listOf().fieldOf("values").forGetter(FluidTemperatureType::values)
    ).apply(instance, FluidTemperatureType::new));

}
