package net.orion.createcoldsweat.utils;

import com.momosoftworks.coldsweat.util.math.CSMath;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.orion.createcoldsweat.Config;

import java.util.function.BiFunction;
import java.util.function.Function;

public class HeatUtils {

    // Comparison value
    public static final FluidStack LAVA = new FluidStack(Fluids.LAVA, 0);

    public static BiFunction<Double, Double, Double> createBlender(int maxDistance) {
        return (Double distance, Double temperature) -> blend(distance, temperature, maxDistance);
    }

    public static double blend(double distance, double temperature, int maxDistance) {
        return CSMath.blend(temperature, 0, distance, 0.5, maxDistance);
    }

    public static double calculateBoilerTemperature(FluidTankBlockEntity tankBlockEntity, Function<Double, Double> calculateHeat) {
        if (tankBlockEntity == null) return 0d;
        if (tankBlockEntity.boiler.activeHeat == 0) return 0d;
        // 0.04768717215 is a singleDegree (Celsius) times 20/18 (18 being the max heat level), effectively, this should make it so the maximum applied temperature is 20 celcius based off heat
        return calculateHeat.apply(Config.CONFIG.boilerTemperatureIncrement.get() * tankBlockEntity.boiler.activeHeat);
    }
}
