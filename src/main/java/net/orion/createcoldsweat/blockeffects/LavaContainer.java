package net.orion.createcoldsweat.blockeffects;

import com.momosoftworks.coldsweat.api.temperature.block_temp.BlockTemp;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import net.orion.createcoldsweat.Config;
import net.orion.createcoldsweat.utils.HeatUtils;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;

public class LavaContainer extends BlockTemp {

    public LavaContainer() {
        super(
                AllBlocks.FLUID_TANK.get(),
                AllBlocks.FLUID_PIPE.get(),
                AllBlocks.GLASS_FLUID_PIPE.get(),
                AllBlocks.SMART_FLUID_PIPE.get()
        );
    }

    private static final BiFunction<Double, Double, Double> lavaBlend = HeatUtils.createBlender(3);

    @Override
    public double getTemperature(Level level, @Nullable LivingEntity livingEntity, BlockState blockState, BlockPos blockPos, double distance) {
//        if (!Config.CONFIG.lavaTemperature.get()) return 0d;
//        FluidTankBlockEntity blockEntity = (FluidTankBlockEntity) level.getBlockEntity(blockPos);
//        if (this.hasBlock(blockState.getBlock()) && blockEntity != null){
//            FluidStack fluidStack = blockEntity.getFluid(1);
//            if (fluidStack.containsFluid(HeatUtils.LAVA))
//                return lavaBlend.apply(distance, Config.CONFIG.lavaTemperatureIncrement.get() * fluidStack.getAmount());
//        }
        return 0d;
    }
}
