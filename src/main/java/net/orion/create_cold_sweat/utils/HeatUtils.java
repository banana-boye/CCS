package net.orion.create_cold_sweat.utils;

import com.momosoftworks.coldsweat.util.math.CSMath;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.orion.create_cold_sweat.Config;

import javax.annotation.Nullable;
import java.util.function.BiFunction;
import java.util.function.Function;

public class HeatUtils {

    public static final BiFunction<Double, Double, Double> fluidBlend = HeatUtils.createBlender(3);

    public static double getTemperatureFromFluidStack(double distance, FluidStack fluidStack) {
        int temperature = fluidStack.getFluid().getFluidType().getTemperature();
        if (temperature == 300) return 0d;
        int amount = fluidStack.getAmount();
        double mcuAmounted = ((temperature - 300) * 0.04291845494) * (amount == 1 ? 1000 : amount) / 1000;
        return fluidBlend.apply(distance, mcuAmounted / Config.CONFIG.defaultFluidDampener.get());
    }

    public static BiFunction<Double, Double, Double> createBlender(int maxDistance) {
        return (Double distance, Double temperature) -> blend(distance, temperature, maxDistance);
    }

    public static double blend(double distance, double temperature, int maxDistance) {
        return CSMath.blend(temperature, 0, distance, 0.5, maxDistance);
    }

    public static double calculateBoilerTemperature(FluidTankBlockEntity tankBlockEntity, Function<Double, Double> calculateHeat) {
        if (tankBlockEntity == null) return 0d;
        if (tankBlockEntity.boiler == null) return 0d;
        if (tankBlockEntity.boiler.activeHeat == 0) return 0d;
        return calculateHeat.apply(Config.CONFIG.boilerTemperatureIncrement.get() * tankBlockEntity.boiler.activeHeat);
    }

    @Nullable
    public static FluidStack getFluid(IFluidHandler iFluidHandler) {
        for (int i = 0; i < iFluidHandler.getTanks(); i++) {
            FluidStack tank = iFluidHandler.getFluidInTank(i);
            if(!tank.isEmpty()) return tank;
        }
        return null;
    }
}
