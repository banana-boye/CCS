package net.orion.create_cold_sweat.blockeffects;

import com.momosoftworks.coldsweat.api.temperature.block_temp.BlockTemp;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
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
        if ((!Config.CONFIG.boilerTemperature.get() && !Config.CONFIG.liquidTemperature.get()) || !this.hasBlock(blockState.getBlock()) || !(level.getBlockEntity(blockPos) instanceof FluidTankBlockEntity fluidTankBlockEntity)) return 0d;

        double blockTemperature = 0d;

        // Calculate boiler blocks
        if (Config.CONFIG.boilerTemperature.get()){
            blockTemperature = HeatUtils.calculateBoilerTemperature(fluidTankBlockEntity, (Double temperature) -> boilerBlend.apply(distance, temperature));
            if (blockTemperature != 0) return blockTemperature;
        }

        if (Config.CONFIG.liquidTemperature.get() && Capabilities.FluidHandler.BLOCK.getCapability(level, blockPos, blockState, fluidTankBlockEntity, null) instanceof IFluidHandler fluidHandler){
            FluidStack fluidStack = HeatUtils.getFluid(fluidHandler);
            if (fluidStack != null) blockTemperature = HeatUtils.getTemperatureFromDistanceAndFluidStack(level, distance, fluidStack);
        }
        return blockTemperature;
    }
}
