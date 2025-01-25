package net.orion.createcoldsweat.blockeffects;

import com.google.common.util.concurrent.AtomicDouble;
import com.momosoftworks.coldsweat.api.temperature.block_temp.BlockTemp;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.orion.createcoldsweat.Config;
import net.orion.createcoldsweat.utils.HeatUtils;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;

public class FluidTank extends BlockTemp {
    public FluidTank() {
        super(AllBlocks.FLUID_TANK.get());
    }

    private static final BiFunction<Double, Double, Double> boilerBlend = HeatUtils.createBlender(3);


    @Override
    public double getTemperature(Level level, @Nullable LivingEntity livingEntity, BlockState blockState, BlockPos blockPos, double distance) {
        if (!Config.CONFIG.boilerTemperature.get() && !Config.CONFIG.lavaTemperature.get()) return 0d;

        FluidTankBlockEntity blockEntity = (FluidTankBlockEntity) level.getBlockEntity(blockPos);
        if (this.hasBlock(blockState.getBlock()) && blockEntity != null) {
            AtomicDouble blockTemperature = new AtomicDouble();

            // Calculate boiler blocks
            if (Config.CONFIG.boilerTemperature.get()){
                blockTemperature.set(HeatUtils.calculateBoilerTemperature(blockEntity, (Double temperature) -> boilerBlend.apply(distance, temperature)));
                if (blockTemperature.get() != 0) return blockTemperature.get();
            }

            if (Config.CONFIG.lavaTemperature.get() && blockTemperature.get() == 0){
                LazyOptional<IFluidHandler> lazyOptional = blockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER);
                lazyOptional.ifPresent(
                        iFluidHandler -> {
                            FluidStack fluidTank = HeatUtils.getFluid(iFluidHandler, HeatUtils.LAVA);
                            if (fluidTank != null) blockTemperature.set(Config.CONFIG.lavaTemperatureIncrement.get() * fluidTank.getAmount() / 1000);
                        }
                );
            }
            return HeatUtils.lavaBlend.apply(distance, blockTemperature.get());
        }
        return 0d;
    }
}
