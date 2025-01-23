package net.orion.createcoldsweat.blockeffects;

import com.momosoftworks.coldsweat.api.temperature.block_temp.BlockTemp;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.fluids.FluidTransportBehaviour;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlock;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlockEntity;
import com.simibubi.create.content.fluids.pipes.GlassFluidPipeBlock;
import com.simibubi.create.content.fluids.pipes.SmartFluidPipeBlockEntity;
import com.simibubi.create.content.fluids.pump.PumpBlockEntity;
import com.simibubi.create.content.fluids.tank.FluidTankBlock;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.orion.createcoldsweat.utils.HeatUtils;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;

public class LavaContainer extends BlockTemp {

    public LavaContainer() {
        super(
                AllBlocks.FLUID_PIPE.get(),
                AllBlocks.SMART_FLUID_PIPE.get(),
                AllBlocks.GLASS_FLUID_PIPE.get(),
                AllBlocks.MECHANICAL_PUMP.get()
        );
    }

    private static final BiFunction<Double, Double, Double> lavaBlend = HeatUtils.createBlender(3);

    @Override
    public double getTemperature(Level level, @Nullable LivingEntity livingEntity, BlockState blockState, BlockPos blockPos, double distance) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (!this.hasBlock(blockState.getBlock()) || blockEntity == null) return 0d;
        SmartBlockEntity pipeBlockEntity = (SmartBlockEntity) blockEntity;
        FluidTransportBehaviour fluidTransportBehaviour = pipeBlockEntity.getBehaviour(FluidTransportBehaviour.TYPE);
        for (Direction direction: fluidTransportBehaviour.interfaces.keySet()) {
            if (fluidTransportBehaviour.getProvidedOutwardFluid(direction).containsFluid(HeatUtils.LAVA)) return 1d;
        }
        return 0d;
    }
}
