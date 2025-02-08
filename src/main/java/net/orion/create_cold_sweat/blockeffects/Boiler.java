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
        if (!Config.CONFIG.boilerTemperature.get() && !Config.CONFIG.liquidTemperature.get()) return 0d;

        FluidTankBlockEntity blockEntity = (FluidTankBlockEntity) level.getBlockEntity(blockPos);
        if (this.hasBlock(blockState.getBlock()) && blockEntity != null) {
            AtomicDouble blockTemperature = new AtomicDouble();

            // Calculate boiler blocks
            if (Config.CONFIG.boilerTemperature.get()){
                blockTemperature.set(HeatUtils.calculateBoilerTemperature(blockEntity, (Double temperature) -> boilerBlend.apply(distance, temperature)));
                if (blockTemperature.get() != 0) return blockTemperature.get();
            }

            if (blockTemperature.get() == 0 && Config.CONFIG.liquidTemperature.get()){
                blockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER).ifPresent(
                        iFluidHandler -> {
                            FluidStack fluidStack = HeatUtils.getFluid(iFluidHandler);
                            if (fluidStack != null) blockTemperature.set(HeatUtils.getTemperatureFromDistanceAndFluidStack(level, distance, fluidStack));
                        }
                );
            }
            return blockTemperature.get();
        }
        return 0d;
    }
}
