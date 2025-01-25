package net.orion.createcoldsweat.utils;

import com.momosoftworks.coldsweat.util.math.CSMath;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.orion.createcoldsweat.Config;

import javax.annotation.Nullable;
import java.util.function.BiFunction;
import java.util.function.Function;

public class HeatUtils {

    // Comparison value
    public static final FluidStack LAVA = new FluidStack(Fluids.LAVA, 1);

    public static final BiFunction<Double, Double, Double> lavaBlend = HeatUtils.createBlender(3);

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
    public static FluidStack getFluid(IFluidHandler iFluidHandler, FluidStack toCompare) {
        for (int i = 0; i < iFluidHandler.getTanks(); i++) {
            FluidStack tank = iFluidHandler.getFluidInTank(i);
            if(tank.isFluidEqual(toCompare)) return tank;
        }
        return null;
    }
}
