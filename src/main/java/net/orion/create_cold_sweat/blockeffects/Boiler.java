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

public class Boiler extends BlockTemp {

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

        double blockTemperature = 0d;

        // Calculate boiler blocks
        if (Config.CONFIG.boilerTemperature.get()){
            blockTemperature = HeatUtils.calculateBoilerTemperature(fluidTankBlockEntity, (Double temperature) -> HeatUtils.boilerBlend.apply(distance, temperature));
            if (blockTemperature != 0) return blockTemperature;
        }

        if (Config.CONFIG.liquidTemperature.get() && Capabilities.FluidHandler.BLOCK.getCapability(level, blockPos, blockState, fluidTankBlockEntity, null) instanceof IFluidHandler fluidHandler){
            FluidStack fluidStack = HeatUtils.getFluid(fluidHandler);
            if (fluidStack != null) blockTemperature = HeatUtils.getTemperatureFromDistanceAndFluidStack(level, distance, fluidStack);
        }
        return blockTemperature;
    }
}
