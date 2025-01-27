package net.orion.createcoldsweat.blockeffects;

import com.google.common.util.concurrent.AtomicDouble;
import com.momosoftworks.coldsweat.api.temperature.block_temp.BlockTemp;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.orion.createcoldsweat.Config;
import net.orion.createcoldsweat.utils.HeatUtils;
import org.jetbrains.annotations.Nullable;

public class FluidContainers extends BlockTemp {
    public FluidContainers(Block... blocks){
        super(blocks);
    }

    @Override
    public double getTemperature(Level level, @Nullable LivingEntity livingEntity, BlockState blockState, BlockPos blockPos, double distance) {
        if(!Config.CONFIG.lavaTemperature.get()) return 0d;

        SmartBlockEntity blockEntity = (SmartBlockEntity) level.getBlockEntity(blockPos);
        if (blockEntity == null) return 0d;
        AtomicDouble blockTemperature = new AtomicDouble(0d);

        LazyOptional<IFluidHandler> lazyOptional = blockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER);
        lazyOptional.ifPresent(
            iFluidHandler -> {
                FluidStack fluidStack = HeatUtils.getFluid(iFluidHandler, HeatUtils.LAVA);
                if (fluidStack != null) blockTemperature.set(Config.CONFIG.lavaTemperatureIncrement.get() * fluidStack.getAmount() / 1000);
            }
        );

        return blockTemperature.get();
    }
}
