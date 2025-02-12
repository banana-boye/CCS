package net.orion.create_cold_sweat.utils;

import com.momosoftworks.coldsweat.api.util.Temperature;
import com.momosoftworks.coldsweat.util.math.CSMath;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.registries.ForgeRegistries;
import net.orion.create_cold_sweat.Config;
import net.orion.create_cold_sweat.datagen.DataGeneratorRegister;
import net.orion.create_cold_sweat.datagen.FluidTemperatureType;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public class HeatUtils {

    public static final BiFunction<Double, Double, Double> fluidBlend = HeatUtils.createBlender(3);

    public static double getTemperatureFromDistanceAndFluidStack(Level level, double distance, FluidStack fluidStack) {
        double dataTemp = getFluidDataTemp(level, fluidStack);
        return fluidBlend.apply(distance, dataTemp == 0d ? calculateFluidTemperature(fluidStack) : dataTemp);
    }

    private static double getFluidDataTemp(Level level, FluidStack fluidStack) {
        if (!(level instanceof ServerLevel server)) return 0d;
        RegistryAccess registryAccess = server.registryAccess();
        Optional<Registry<FluidTemperatureType>> registryOptional = registryAccess.registry(DataGeneratorRegister.FLUID_TEMPERATURE_KEY);

        if (registryOptional.isEmpty()) return 0d;
        Registry<FluidTemperatureType> registry = registryOptional.get();
        return getFluidTemperature(registry, fluidStack);
    }

    private static double getFluidTemperature(Registry<FluidTemperatureType> registry, FluidStack fluidStack) {
        String fluidId = Objects.requireNonNull(ForgeRegistries.FLUIDS.getKey(fluidStack.getFluid())).toString();
        for (FluidTemperatureType fluidTemperatureType : registry) {
            if (!fluidTemperatureType.values().containsKey(fluidId)) continue;
            return Temperature.convert(fluidTemperatureType.values().get(fluidId), fluidTemperatureType.units(), Temperature.Units.MC, false);
        }
        return 0d;
    }

    public static double calculateFluidTemperature(FluidStack fluidStack) {
        int temperature = fluidStack.getFluid().getFluidType().getTemperature();
        if (temperature == 300) return 0d;
        int amount = fluidStack.getAmount();
        return (((temperature - 300) * 0.04291845494) * (amount == 1 ? 1000 : amount) / 1000) / Config.CONFIG.defaultFluidDampener.get();
    }

    public static BiFunction<Double, Double, Double> createBlender(int maxDistance) {
        return (Double distance, Double temperature) -> blend(distance, temperature, maxDistance);
    }

    public static double blend(double distance, double temperature, int maxDistance) {
        return CSMath.blend(temperature, 0, distance, 0.5, maxDistance);
    }

    public static double calculateBoilerTemperature(FluidTankBlockEntity tankBlockEntity, Function<Double, Double> calculateHeat) {
        if (tankBlockEntity == null) return 0d;
        return calculateHeat.apply(Config.CONFIG.boilerTemperatureIncrement.get() * tankBlockEntity.getControllerBE().boiler.activeHeat);
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
