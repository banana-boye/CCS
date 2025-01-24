package net.orion.createcoldsweat.blockeffects;

import com.momosoftworks.coldsweat.api.temperature.block_temp.BlockTemp;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.steamEngine.SteamEngineBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.orion.createcoldsweat.Config;
import net.orion.createcoldsweat.utils.HeatUtils;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;

public class SteamEngine extends BlockTemp {
    public SteamEngine() {
        super(AllBlocks.STEAM_ENGINE.get());
    }

    private static final BiFunction<Double, Double, Double> steamEngineBlend = HeatUtils.createBlender(3);

    @Override
    public double getTemperature(Level level, @Nullable LivingEntity livingEntity, BlockState blockState, BlockPos blockPos, double distance) {
        if (Config.CONFIG.boilerTemperature.get()) return 0d;
        SteamEngineBlockEntity blockEntity = (SteamEngineBlockEntity) level.getBlockEntity(blockPos);
        if (this.hasBlock(blockState.getBlock()) && blockEntity != null) {
            return HeatUtils.calculateBoilerTemperature(blockEntity.getTank(), (Double temperature) -> steamEngineBlend.apply(distance, temperature));
        }
        return 0d;
    }
}
