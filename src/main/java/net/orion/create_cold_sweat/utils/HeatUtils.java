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
import net.orion.create_cold_sweat.MathConstants;
import net.orion.create_cold_sweat.datagen.DataGeneratorRegister;
import net.orion.create_cold_sweat.datagen.FluidTemperatureType;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public class HeatUtils {

    public static BiFunction<Double, Double, Double> fluidBlend;
    public static BiFunction<Double, Double, Double> blazeBlend;
    public static BiFunction<Double, Double, Double> boilerBlend;
    public static BiFunction<Double, Double, Double> steamEngineBlend;

    /**
     * Applies fluidBlend based off the distance to the container
     * @param level The relevant level
     * @param distance The distance to the fluid container
     * @param fluidStack The FluidStack in the container
     * @return The temperature blend
     */
    public static double getTemperatureFromDistanceAndFluidStack(Level level, double distance, FluidStack fluidStack) {
        double dataTemp = getFluidDataTemp(level, fluidStack);
        return fluidBlend.apply(distance, dataTemp == 0d ? calculateFluidTemperature(fluidStack) : dataTemp);
    }

    /**
     * Gets the temperature for the relevant fluid based off the level
     * @param level The relevant level
     * @param fluidStack The fluid stack
     * @return The fluid temperature
     */
    public static double getFluidDataTemp(Level level, FluidStack fluidStack) {
        if (!(level instanceof ServerLevel server)) return 0d;

        RegistryAccess registryAccess = server.registryAccess();
        Optional<Registry<FluidTemperatureType>> registryOptional = registryAccess.registry(DataGeneratorRegister.FLUID_TEMPERATURE_KEY);

        if (registryOptional.isEmpty()) return 0d;
        Registry<FluidTemperatureType> registry = registryOptional.get();
        return getFluidTemperature(registry, fluidStack);
    }

    /**
     * Gets the temperature for the relevant fluid based off registry
     * @param registry The registry
     * @param fluidStack The fluid stack
     * @return The temperature
     */
    private static double getFluidTemperature(Registry<FluidTemperatureType> registry, FluidStack fluidStack) {
        String fluidId = Objects.requireNonNull(ForgeRegistries.FLUIDS.getKey(fluidStack.getFluid())).toString();

        for (FluidTemperatureType fluidTemperatureType : registry) {
            if (!fluidTemperatureType.values().containsKey(fluidId)) continue;
            return Temperature.convert(fluidTemperatureType.values().get(fluidId), fluidTemperatureType.units(), Temperature.Units.MC, false);
        }

        return 0d;
    }

    /**
     * Calculates a fluid temperature based off minecraft's integrated fluid temperatures
     * @param fluidStack The fluid stack
     * @return The temperature
     */
    public static double calculateFluidTemperature(FluidStack fluidStack) {
        if (!Config.CONFIG.calculateDefaultLiquidTemperature.get()) return 0d;

        // Calculates the fluid temperature based off the registered fluid temperature in game
        // Since fluid temps *should* be in Kelvin, this is some math for that

        int temperature = fluidStack.getFluid().getFluidType().getTemperature();

        // 300 is roughly room temperature, and since we're looking for the amount of temperature it *adds* we ignore this.
        if (temperature == 300) return 0d;
        double MCUnitTemperature = getMcUnitFluidTemperatureFromKelvin(fluidStack, temperature);

        return MCUnitTemperature / Config.CONFIG.defaultFluidDampener.get();
    }

    /**
     * Converts a fluid's Kelvin temperature to MC units
     * @param fluidStack The fluid stack
     * @param temperature The Kelvin temperature
     * @return The temperature in MC Units
     */
    private static double getMcUnitFluidTemperatureFromKelvin(FluidStack fluidStack, int temperature) {
        int amount = fluidStack.getAmount();

        // Pipes sometimes transport 1 mb at a time, this effectively avoids that by making it 1b
        // Perhaps a curve/range should be implemented here
        int effectiveContainedFluidTemperature = amount == 1 ? 1000 : amount;
        // Effective kelvin temperature without including room temp.
        int kelvinTemperature = temperature - 300;

        return kelvinTemperature * MathConstants.ONE_CELSIUS_IN_MC_UNITS * effectiveContainedFluidTemperature / 1000;
    }

    /**
     * Curries the max distance into a blend function
     * @param maxDistance The max distance the blend affects
     * @return Blend BiFunction
     */
    public static BiFunction<Double, Double, Double> createBlender(int maxDistance) {
        return (Double distance, Double temperature) -> blend(distance, temperature, maxDistance);
    }

    /**
     * Default blend, blendsTo 0 and puts a minimum range of 0.5
     * @param distance The distance
     * @param temperature The temperature
     * @param maxDistance The max distance
     * @return The blended value
     */
    public static double blend(double distance, double temperature, int maxDistance) {
        return CSMath.blend(temperature, 0, distance, 0.5, maxDistance);
    }

    /**
     * Calculates the boiler temperature based on size and more
     * @param tankBlockEntity The tank's block entity
     * @param calculateHeat the blend Function for calculating the heat
     * @return The blended temperature
     */
    public static double calculateBoilerTemperature(FluidTankBlockEntity tankBlockEntity, Function<Double, Double> calculateHeat) {
        if (tankBlockEntity == null) return 0d;
        FluidTankBlockEntity controller = tankBlockEntity.getControllerBE();
        if (controller == null) return 0d;
        return calculateHeat.apply(Config.CONFIG.boilerTemperatureIncrement.get() * controller.boiler.activeHeat);
    }

    /**
     * Checks all directions on a block to find what fluid is inside
     * @param iFluidHandler The fluid handler of the block
     * @return A fluid, null if none found
     */
    @Nullable
    public static FluidStack getFluid(IFluidHandler iFluidHandler) {
        for (int i = 0; i < iFluidHandler.getTanks(); i++) {
            FluidStack tank = iFluidHandler.getFluidInTank(i);
            if(!tank.isEmpty()) return tank;
        }
        return null;
    }
}
