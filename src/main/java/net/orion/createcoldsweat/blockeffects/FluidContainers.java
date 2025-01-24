package net.orion.createcoldsweat.blockeffects;

import com.momosoftworks.coldsweat.api.temperature.block_temp.BlockTemp;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

public class FluidContainers extends BlockTemp {
    public FluidContainers(){
        super(
                AllBlocks.BASIN.get(),
                AllBlocks.FLUID_TANK.get()
        );
    }

    @Override
    public double getTemperature(Level level, @Nullable LivingEntity livingEntity, BlockState blockState, BlockPos blockPos, double distance) {
        SmartBlockEntity blockEntity = (SmartBlockEntity) level.getBlockEntity(blockPos);
        LazyOptional<IFluidHandler> lazyOptional = blockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER);
        lazyOptional.ifPresent(iFluidHandler -> {
            System.out.println(iFluidHandler.getFluidInTank(1).getFluid());
        });

        return 0;
    }
}
