package net.orion.create_cold_sweat.blockeffects;

import com.google.common.util.concurrent.AtomicDouble;
import com.momosoftworks.coldsweat.api.temperature.block_temp.BlockTemp;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.orion.create_cold_sweat.Config;
import net.orion.create_cold_sweat.utils.HeatUtils;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;

public class Boiler extends BlockTemp {

    private static final BiFunction<Double, Double, Double> boilerBlend = HeatUtils.createBlender(8);

    public Boiler(Block... block) {
        super(block);
    }


    @Override
    public double getTemperature(Level level, @Nullable LivingEntity livingEntity, BlockState blockState, BlockPos blockPos, double distance) {
        // Whether configs disabled this feature
        Boolean liquidTemperatureEnabled = Config.CONFIG.liquidTemperature.get();
        Boolean boilerTemperatureEnabled = Config.CONFIG.boilerTemperature.get();
        boolean configDisabled = !boilerTemperatureEnabled && !liquidTemperatureEnabled;
        boolean doesNotHaveBlock = !this.hasBlock(blockState.getBlock());

        if (
            configDisabled ||
            doesNotHaveBlock ||
            !(level.getBlockEntity(blockPos) instanceof FluidTankBlockEntity fluidTankBlockEntity)
        ) return 0d;

        AtomicDouble blockTemperature = new AtomicDouble();

        // Calculate boiler blocks
        if (boilerTemperatureEnabled){
            // Checks if the fluid tank is an active boiler and calculates the value according to speed and other values
            blockTemperature.set(HeatUtils.calculateBoilerTemperature(fluidTankBlockEntity, (Double temperature) -> boilerBlend.apply(distance, temperature)));
            if (blockTemperature.get() != 0) return blockTemperature.get();
        }

        // If the fluid tank is NOT a boiler, it'll calculate the fluid temperature it contains
        if (blockTemperature.get() == 0 && liquidTemperatureEnabled){
            fluidTankBlockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER).ifPresent(
                    iFluidHandler -> {
                        FluidStack fluidStack = HeatUtils.getFluid(iFluidHandler);
                        if (fluidStack != null) blockTemperature.set(HeatUtils.getTemperatureFromDistanceAndFluidStack(level, distance, fluidStack));
                    }
            );
        }
        return blockTemperature.get();
    }
}
