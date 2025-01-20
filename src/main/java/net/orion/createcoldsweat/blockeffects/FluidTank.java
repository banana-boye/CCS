package net.orion.createcoldsweat.blockeffects;

import com.momosoftworks.coldsweat.api.temperature.block_temp.BlockTemp;
import com.momosoftworks.coldsweat.common.block.BoilerBlock;
import com.simibubi.create.AllBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import net.orion.createcoldsweat.Config;
import org.jetbrains.annotations.Nullable;

public class FluidTank extends BlockTemp {

    public FluidTank () {
        super(ForgeRegistries.BLOCKS)
    }

    @Override
    public double getTemperature(Level level, @Nullable LivingEntity livingEntity, BlockState blockState, BlockPos blockPos, double v) {
        if (Config.CONFIG.boilerTemperature.get() && blockState.hasBlockEntity() && level.getBlockEntity(blockPos) instanceof BoilerBlock) {
            //
        }
        return 0f;
    }
}
