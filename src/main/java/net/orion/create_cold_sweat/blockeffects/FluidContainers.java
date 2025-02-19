package net.orion.create_cold_sweat.blockeffects;

import com.google.common.util.concurrent.AtomicDouble;
import com.momosoftworks.coldsweat.api.temperature.block_temp.BlockTemp;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
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

public class FluidContainers extends BlockTemp {
    public FluidContainers(Block... blocks){
        super(blocks);
    }

    @Override
    public double getTemperature(Level level, @Nullable LivingEntity livingEntity, BlockState blockState, BlockPos blockPos, double distance) {
        if(!Config.CONFIG.liquidTemperature.get()) return 0d;
        if (!(level.getBlockEntity(blockPos) instanceof SmartBlockEntity blockEntity)) return 0d;
        AtomicDouble blockTemperature = new AtomicDouble(0d);

        blockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER).ifPresent(
            iFluidHandler -> {
                FluidStack fluidStack = HeatUtils.getFluid(iFluidHandler);
                if (fluidStack != null) blockTemperature.set(HeatUtils.getTemperatureFromDistanceAndFluidStack(level, distance, fluidStack));
            }
        );

        return blockTemperature.get();
    }
}
