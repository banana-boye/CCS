package net.orion.createcoldsweat.blockeffects;

import com.momosoftworks.coldsweat.api.temperature.block_temp.BlockTemp;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.orion.createcoldsweat.Config;
import net.orion.createcoldsweat.utils.HeatUtils;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;

public class SteamEngineTank extends BlockTemp {
    public SteamEngineTank() {
        super(AllBlocks.FLUID_TANK.get());
    }

    private static final BiFunction<Double, Double, Double> boilerBlend = HeatUtils.createBlender(3);


    @Override
    public double getTemperature(Level level, @Nullable LivingEntity livingEntity, BlockState blockState, BlockPos blockPos, double distance) {
        if (!Config.CONFIG.boilerTemperature.get()) return 0d;
        FluidTankBlockEntity blockEntity = (FluidTankBlockEntity) level.getBlockEntity(blockPos);
        if (this.hasBlock(blockState.getBlock()) && blockEntity != null) {
            return HeatUtils.calculateBoilerTemperature(blockEntity, (Double temperature) -> boilerBlend.apply(distance, temperature));
        }
        return 0d;
    }
}
