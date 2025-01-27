package net.orion.create_cold_sweat.blockeffects;

import com.momosoftworks.coldsweat.api.temperature.block_temp.BlockTemp;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.orion.create_cold_sweat.Config;
import net.orion.create_cold_sweat.utils.HeatUtils;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;

public class BlazeBurner extends BlockTemp {

    private static final BiFunction<Double, Double, Double> blazeBlend = HeatUtils.createBlender(7);

    public BlazeBurner(Block... blocks) {
        super(blocks);
    }

    @Override
    public double getTemperature(Level level, @Nullable LivingEntity livingEntity, BlockState blockState, BlockPos blockPos, double distance) {
        if (!Config.CONFIG.blazeBurnerTemperature.get()) return 0d;
        if (!this.hasBlock(blockState.getBlock())) return 0d;
        if (isHeatLevel(blockState, BlazeBurnerBlock.HeatLevel.SMOULDERING))
            return blazeBlend.apply(distance, Config.CONFIG.bBSmouldering.get());
        else if (isHeatLevel(blockState, BlazeBurnerBlock.HeatLevel.FADING))
            return blazeBlend.apply(distance, Config.CONFIG.bBFading.get());
        else if (isHeatLevel(blockState, BlazeBurnerBlock.HeatLevel.KINDLED))
            return blazeBlend.apply(distance, Config.CONFIG.bBKindled.get());
        else if (isHeatLevel(blockState, BlazeBurnerBlock.HeatLevel.SEETHING))
            return blazeBlend.apply(distance, Config.CONFIG.bBSeething.get());
        return 0d;
    }

    private boolean isHeatLevel(BlockState blockState, BlazeBurnerBlock.HeatLevel heatLevel) {
        return (blockState.getValue(BlazeBurnerBlock.HEAT_LEVEL) == heatLevel);
    }
}
