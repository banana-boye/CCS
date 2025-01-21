package net.orion.createcoldsweat.blockeffects;

import com.momosoftworks.coldsweat.api.temperature.block_temp.BlockTemp;
import com.momosoftworks.coldsweat.util.math.CSMath;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.orion.createcoldsweat.Config;
import org.jetbrains.annotations.Nullable;

public class BlazeBurner extends BlockTemp {

    public BlazeBurner(){
        super(AllBlocks.BLAZE_BURNER.get());
    }

    private static final

    @Override
    public double getTemperature(Level level, @Nullable LivingEntity livingEntity, BlockState blockState, BlockPos blockPos, double distance) {
        if (Config.CONFIG.blazeBurnerTemperature.get() && this.hasBlock(blockState.getBlock())){
            if (isHeatLevel(blockState, BlazeBurnerBlock.HeatLevel.SMOULDERING))
                return blazeBlend(distance, Config.CONFIG.bBSmouldering.get());
            else if (isHeatLevel(blockState, BlazeBurnerBlock.HeatLevel.FADING))
                return blazeBlend(distance, Config.CONFIG.bBFading.get());
            else if (isHeatLevel(blockState, BlazeBurnerBlock.HeatLevel.KINDLED))
                return blazeBlend(distance, Config.CONFIG.bBKindled.get());
            else if (isHeatLevel(blockState, BlazeBurnerBlock.HeatLevel.SEETHING))
                return blazeBlend(distance, Config.CONFIG.bBSeething.get());
        }
        return 0f;
    }

    private boolean isHeatLevel(BlockState blockState, BlazeBurnerBlock.HeatLevel heatLevel) {
        return (blockState.getValue(BlazeBurnerBlock.HEAT_LEVEL) == heatLevel);
    }
}
