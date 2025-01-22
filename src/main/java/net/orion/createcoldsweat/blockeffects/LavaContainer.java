package net.orion.createcoldsweat.blockeffects;

import com.momosoftworks.coldsweat.api.temperature.block_temp.BlockTemp;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlock;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlockEntity;
import com.simibubi.create.content.fluids.tank.FluidTankBlock;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import net.minecraft.core.BlockPos;
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
                AllBlocks.FLUID_PIPE.get()
//                AllBlocks.GLASS_FLUID_PIPE.get(),
//                AllBlocks.SMART_FLUID_PIPE.get(),
//                AllBlocks.BASIN.get()
        );
    }

    private static final BiFunction<Double, Double, Double> lavaBlend = HeatUtils.createBlender(3);

    @Override
    public double getTemperature(Level level, @Nullable LivingEntity livingEntity, BlockState blockState, BlockPos blockPos, double distance) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (!this.hasBlock(blockState.getBlock()) || blockEntity == null) return 0d;
        FluidPipeBlockEntity pipeBlockEntity = (FluidPipeBlockEntity) blockEntity;
        System.out.println("Pipe NBT:" + pipeBlockEntity.getBehaviour(Standard));
        return 0d;
    }
}
