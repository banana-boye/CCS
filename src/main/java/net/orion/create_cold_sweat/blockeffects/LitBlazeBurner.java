package net.orion.create_cold_sweat.blockeffects;

import com.momosoftworks.coldsweat.api.temperature.block_temp.BlockTemp;
import com.simibubi.create.AllBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.orion.create_cold_sweat.Config;
import org.jetbrains.annotations.Nullable;

public class LitBlazeBurner extends BlockTemp {

    public LitBlazeBurner() {
        super(AllBlocks.LIT_BLAZE_BURNER.get());
    }

    @Override
    public double getTemperature(Level level, @Nullable LivingEntity livingEntity, BlockState blockState, BlockPos blockPos, double distance) {
        if (!Config.CONFIG.blazeBurnerTemperature.get() || !this.hasBlock(blockState.getBlock())) return 0d;

        return BlazeBurner.blazeBlend.apply(distance, 0.1287553648d);
    }
}
