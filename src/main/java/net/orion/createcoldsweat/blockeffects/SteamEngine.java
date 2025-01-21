package net.orion.createcoldsweat.blockeffects;

import com.momosoftworks.coldsweat.api.temperature.block_temp.BlockTemp;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.fluids.tank.BoilerData;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.content.kinetics.steamEngine.SteamEngineBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class SteamEngine extends BlockTemp {
    private static Block ref = AllBlocks.STEAM_ENGINE.get();
    public SteamEngine() {
        super(AllBlocks.STEAM_ENGINE.get());
    }

    @Override
    public double getTemperature(Level level, @Nullable LivingEntity livingEntity, BlockState blockState, BlockPos blockPos, double v) {
        SteamEngineBlockEntity blockEntity = (SteamEngineBlockEntity) level.getBlockEntity(blockPos);
        if (this.hasBlock(blockState.getBlock()) && blockEntity != null) {
            FluidTankBlockEntity tank = blockEntity.getTank();
            if (tank == null) return 0d;
            int heat = getActualHeat(tank.boiler, tank.getTotalTankSize());
            if (heat == 0) return 0d;

        }
        return 0;
    }

    private int getActualHeat(BoilerData boilerData, int boilerSize) {
        int forBoilerSize = boilerData.getMaxHeatLevelForBoilerSize(boilerSize);
        int forWaterSupply = boilerData.getMaxHeatLevelForWaterSupply();
        int actualHeat = Math.min(boilerData.activeHeat, Math.min(forWaterSupply, forBoilerSize));
        return actualHeat;
    }
}
