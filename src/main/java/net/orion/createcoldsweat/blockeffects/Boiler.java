package net.orion.createcoldsweat.blockeffects;

import com.momosoftworks.coldsweat.api.temperature.block_temp.BlockTemp;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.orion.createcoldsweat.Config;
import org.jetbrains.annotations.Nullable;

public class Boiler extends BlockTemp {
    public Boiler(Block block) {
        super(block);
    }

    @Override
    public double getTemperature(Level level, @Nullable LivingEntity livingEntity, BlockState blockState, BlockPos blockPos, double v) {
        if (Config.CONFIG.boilerTemperature.get() && blockState.hasBlockEntity()) {
            return 1f;
        }
        return 0f;
    }
}
