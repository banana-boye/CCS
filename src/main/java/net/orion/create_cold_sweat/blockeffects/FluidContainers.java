package net.orion.create_cold_sweat.blockeffects;

import com.momosoftworks.coldsweat.api.temperature.block_temp.BlockTemp;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
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

public class FluidContainers extends BlockTemp {
    public FluidContainers(Block... blocks){
        super(blocks);
    }

    @Override
    public double getTemperature(Level level, @Nullable LivingEntity livingEntity, BlockState blockState, BlockPos blockPos, double distance) {
        if(
            !Config.CONFIG.liquidTemperature.get() ||
            !(level.getBlockEntity(blockPos) instanceof SmartBlockEntity blockEntity) ||
            !(Capabilities.FluidHandler.BLOCK.getCapability(level, blockPos, blockState, blockEntity, null) instanceof IFluidHandler fluidHandler)
        ) return 0d;
        double blockTemperature = 0d;
        FluidStack fluidStack = HeatUtils.getFluid(fluidHandler);
        if (fluidStack != null) blockTemperature = HeatUtils.getTemperatureFromDistanceAndFluidStack(level, distance, fluidStack);

        return blockTemperature;
    }
}
