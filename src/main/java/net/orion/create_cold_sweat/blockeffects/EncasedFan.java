package net.orion.create_cold_sweat.blockeffects;

import com.momosoftworks.coldsweat.api.registry.BlockTempRegistry;
import com.momosoftworks.coldsweat.api.temperature.block_temp.BlockTemp;
import com.momosoftworks.coldsweat.api.util.Temperature;
import com.momosoftworks.coldsweat.config.ConfigSettings;
import com.momosoftworks.coldsweat.data.codec.impl.ConfigData;
import com.momosoftworks.coldsweat.util.world.WorldHelper;
import com.simibubi.create.content.kinetics.fan.EncasedFanBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidStack;
import net.orion.create_cold_sweat.Config;
import net.orion.create_cold_sweat.CreateColdSweat;
import net.orion.create_cold_sweat.MathConstants;
import net.orion.create_cold_sweat.utils.HeatUtils;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class EncasedFan extends BlockTemp {

    public EncasedFan(Block... blocks) {
        super(blocks);
    }

    public static final Map<LivingEntity, Map<Double, Float>> fanMap = new HashMap<>();

    @Override
    public double getTemperature(Level level, @Nullable LivingEntity livingEntity, BlockState blockState, BlockPos blockPos, double distance) {
        boolean configDisabled = !Config.CONFIG.fanCooling.get();
        boolean doesNotHaveBlock = !this.hasBlock(blockState.getBlock());

        if (
            configDisabled ||
            doesNotHaveBlock ||
            !(level.getBlockEntity(blockPos) instanceof EncasedFanBlockEntity encasedFan) ||
            livingEntity == null
        ) return 0d;

        float speed = encasedFan.getSpeed();
        if (speed == 0) return 0d;

        Vec3 entityPosition = livingEntity.position();

        Direction facing = encasedFan.getBlockState().getValue(BlockStateProperties.FACING);
        double angle = getAngleFromFront(blockPos, facing, entityPosition);

        // Yes I know I could've asked for radians instead but degrees is just more readable, come on
        double maxRadians = Math.toRadians(Config.CONFIG.maximumFanAngle.get());

        if (angle > maxRadians)
            return 0d;

        // Efficiency
        double effectEfficiency = getEffectEfficiency(encasedFan, angle, maxRadians);
        double targetTemperature = 0;
        double biomeTemperature = WorldHelper.getBiomeTemperature(level, level.getBiome(blockPos));

        if (Config.CONFIG.fanTemperatureInteraction.get()){
            // Position in front of the block
            BlockPos offset = blockPos.offset(facing.getNormal());
            BlockState state = level.getBlockState(offset);

            // Get Temperature of max block in front
            double tempForBlock = BlockTempRegistry.getBlockTempsFor(state)
                    .stream().toList().get(0)
                    .getTemperature(level, null, state, offset, 0d);

            // Get fluid state of block for in case there is no block temperature
            FluidState fluidState = level.getFluidState(offset);
            double fluidStateTemperature = fluidState.isEmpty() ? 0d : HeatUtils.getFluidDataTemp(level, new FluidStack(fluidState.getType(), 1000));

            // If there is no block temperature, use the fluidState temperature
            // If using the block temperature add the biome temp
            targetTemperature = tempForBlock == 0d ? fluidStateTemperature : tempForBlock + biomeTemperature;
        }

        targetTemperature = targetTemperature == 0d ? biomeTemperature : targetTemperature;
        double playerTemperature = Temperature.get(livingEntity, Temperature.Trait.WORLD);
        int nearbyFans = countFansAffectingEntity(level, livingEntity);

        return HeatUtils.blend(distance, getGeneratedTemperature(targetTemperature, playerTemperature, effectEfficiency, nearbyFans), Math.max((int) (encasedFan.getMaxDistance() + 0.5), Config.CONFIG.maxFanDistance.get()));
    }

    private static double getGeneratedTemperature(double targetTemperature, double playerTemperature, double effectEfficiency, int nearbyFans) {
        double difference = targetTemperature - playerTemperature;
        double generated = effectEfficiency * difference;

        // If x approaches 0 from the left, then the max is 0, if x approaches 0 from the right, then the min is 0
        // This is so that it doesn't overshoot, a fan can't make you cooler than the surrounding air
        double generatedTemperature = targetTemperature > playerTemperature ? Math.max(0, generated) : Math.min(0, generated);
        double dampener = Math.sqrt(nearbyFans);
        generatedTemperature *= dampener;
        CreateColdSweat.LOGGER.info("dampener {}, fans {}", dampener, nearbyFans);
        return generatedTemperature;
    }

    private static int countFansAffectingEntity(Level level, LivingEntity entity) {
        int count = 0;
        // Example: search nearby fans in a cube radius (adjust as needed)
        BlockPos entityPos = entity.blockPosition();
        int blockRange = ConfigSettings.BLOCK_RANGE.get() == null ? 16 : ConfigSettings.BLOCK_RANGE.get();
        int maxFanDistance = Config.CONFIG.maxFanDistance.get();
        int radius = Math.min(maxFanDistance, blockRange);
        for (BlockPos pos : BlockPos.betweenClosed(
            entityPos.offset(-radius, -radius, -radius),
            entityPos.offset(radius, radius, radius)
        )) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof EncasedFanBlockEntity fan && fan.getSpeed() != 0) {
                count++;
            }
        }
        return Math.max(count, 1); // avoid divide-by-zero
    }

    private static double getEffectEfficiency(EncasedFanBlockEntity encasedFan, double angle, double maxRadians) {
        double angleDifference = Math.abs(angle - maxRadians);
        double unitAngleDifference = angleDifference / maxRadians;
        float unitSpeed = Math.abs(encasedFan.getSpeed()) / 256;

        // Unit percentage of temperature effect on the player
        // The angle affects half and so does the speed
        // For maximum efficiency the angle has to be 90 (Directly in front) and the speed 256 (The maximum)
        return (unitAngleDifference + unitSpeed) / 2;
    }

    public static double getAngleFromFront(BlockPos fanPos, Direction facing, Vec3 targetPos) {
        Vec3 fanOrigin = Vec3.atCenterOf(fanPos).add(Vec3.atLowerCornerOf(facing.getNormal()).scale(0.5));
        Vec3 toTarget = targetPos.subtract(fanOrigin).normalize();
        Vec3 facingVec = Vec3.atLowerCornerOf(facing.getNormal()).normalize();
        double dot = facingVec.dot(toTarget);
        return Math.acos(dot);
    }

}
