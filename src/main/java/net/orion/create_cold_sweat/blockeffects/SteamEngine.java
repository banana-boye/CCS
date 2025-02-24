package net.orion.create_cold_sweat.blockeffects;

import com.momosoftworks.coldsweat.api.temperature.block_temp.BlockTemp;
import com.simibubi.create.content.kinetics.steamEngine.SteamEngineBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.orion.create_cold_sweat.Config;
import net.orion.create_cold_sweat.utils.HeatUtils;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;

public class SteamEngine extends BlockTemp {
    private static final BiFunction<Double, Double, Double> steamEngineBlend = HeatUtils.createBlender(3);

    public SteamEngine(Block... blocks) {
        super(blocks);
    }

    @Override
    public double getTemperature(Level level, @Nullable LivingEntity livingEntity, BlockState blockState, BlockPos blockPos, double distance) {
        if (Config.CONFIG.boilerTemperature.get() || !this.hasBlock(blockState.getBlock()) || !(level.getBlockEntity(blockPos) instanceof SteamEngineBlockEntity blockEntity)) return 0d;

        return HeatUtils.calculateBoilerTemperature(blockEntity.getTank(), (Double temperature) -> steamEngineBlend.apply(distance, temperature));
    }
}
