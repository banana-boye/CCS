package net.orion.createcoldsweat.blockeffects;

import com.momosoftworks.coldsweat.api.temperature.block_temp.BlockTemp;
import com.simibubi.create.content.fluids.FluidTransportBehaviour;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import net.orion.createcoldsweat.Config;
import net.orion.createcoldsweat.utils.HeatUtils;
import org.jetbrains.annotations.Nullable;

public class PipesAndPumps extends BlockTemp {

    public PipesAndPumps(Block... blocks) {
        super(blocks);
    }
    @Override
    public double getTemperature(Level level, @Nullable LivingEntity livingEntity, BlockState blockState, BlockPos blockPos, double distance) {
        if (!Config.CONFIG.lavaTemperature.get()) return 0d;
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (!this.hasBlock(blockState.getBlock()) || blockEntity == null) return 0d;
        SmartBlockEntity pipeBlockEntity = (SmartBlockEntity) blockEntity;
        FluidTransportBehaviour fluidTransportBehaviour = pipeBlockEntity.getBehaviour(FluidTransportBehaviour.TYPE);
        for (Direction direction: fluidTransportBehaviour.interfaces.keySet()) {
            FluidStack fluidStack = fluidTransportBehaviour.getProvidedOutwardFluid(direction);
            if (fluidStack.isFluidEqual(HeatUtils.LAVA)) {
                return HeatUtils.lavaBlend.apply(
                        distance,
                        Config.CONFIG.lavaTemperatureIncrement.get() * fluidStack.getAmount()
                );
            }
        }
        return 0d;
    }
}