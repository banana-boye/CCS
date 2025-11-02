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

import java.util.Arrays;
import java.util.function.BiFunction;

public class BlazeBurner extends BlockTemp {

    public static final BiFunction<Double, Double, Double> blazeBlend = HeatUtils.createBlender(8);

    public BlazeBurner(Block... blocks) {
        super(blocks);
    }

    @Override
    public double getTemperature(Level level, @Nullable LivingEntity livingEntity, BlockState blockState, BlockPos blockPos, double distance) {
        boolean blazeBurnerTemperatureDisabled = !Config.CONFIG.blazeBurnerTemperature.get();
        if (blazeBurnerTemperatureDisabled || !this.hasBlock(blockState.getBlock())) return 0d;

        return Arrays.stream(BlazeBurnerBlock.HeatLevel.values())
                .map(hl -> isHeatLevel(blockState, hl) ? blazeBlend.apply(distance, switch (hl) {
                    case NONE -> 0d;
                    case SMOULDERING -> Config.CONFIG.bBSmouldering.get();
                    case FADING -> Config.CONFIG.bBFading.get();
                    case KINDLED -> Config.CONFIG.bBKindled.get();
                    case SEETHING -> Config.CONFIG.bBSeething.get();
                }) : 0d)
                .filter(t -> t != 0d)
                .findFirst()
                .orElse(0d);
    }

    private boolean isHeatLevel(BlockState blockState, BlazeBurnerBlock.HeatLevel heatLevel) {
        return (blockState.getValue(BlazeBurnerBlock.HEAT_LEVEL) == heatLevel);
    }
}
