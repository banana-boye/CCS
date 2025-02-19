package net.orion.create_cold_sweat.blockeffects;

import com.momosoftworks.coldsweat.api.temperature.block_temp.BlockTemp;
import com.simibubi.create.content.fluids.FluidTransportBehaviour;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import net.orion.create_cold_sweat.Config;
import net.orion.create_cold_sweat.utils.HeatUtils;
import org.jetbrains.annotations.Nullable;

public class PipesAndPumps extends BlockTemp {

    public PipesAndPumps(Block... blocks) {
        super(blocks);
    }
    @Override
    public double getTemperature(Level level, @Nullable LivingEntity livingEntity, BlockState blockState, BlockPos blockPos, double distance) {
        if (!Config.CONFIG.liquidTemperature.get()) return 0d;
        if (!this.hasBlock(blockState.getBlock()) || !(level.getBlockEntity(blockPos) instanceof SmartBlockEntity pipeBlockEntity)) return 0d;

        FluidTransportBehaviour fluidTransportBehaviour = pipeBlockEntity.getBehaviour(FluidTransportBehaviour.TYPE);

        for (Direction direction : fluidTransportBehaviour.interfaces.keySet()) {
            FluidStack fluidStack = fluidTransportBehaviour.getProvidedOutwardFluid(direction);
            if(fluidStack.isEmpty()) continue;
            return HeatUtils.getTemperatureFromDistanceAndFluidStack(level, distance, fluidStack);
        }

        return 0d;
    }
}