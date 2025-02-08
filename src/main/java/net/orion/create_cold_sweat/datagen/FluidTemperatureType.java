package net.orion.create_cold_sweat.datagen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.momosoftworks.coldsweat.api.util.Temperature;

import java.util.Map;

public record FluidTemperatureType(Temperature.Units units, Map<String, Double> values) {
    public static final Codec<FluidTemperatureType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Temperature.Units.CODEC.fieldOf("units").forGetter(FluidTemperatureType::units),
            Codec.unboundedMap(Codec.STRING, Codec.DOUBLE).fieldOf("values").forGetter(FluidTemperatureType::values)
    ).apply(instance, FluidTemperatureType::new));
}
