package net.orion.create_cold_sweat.blockeffects;

import com.momosoftworks.coldsweat.api.registry.BlockTempRegistry;
import com.momosoftworks.coldsweat.api.temperature.block_temp.BlockTemp;
import com.momosoftworks.coldsweat.api.util.Temperature;
import com.momosoftworks.coldsweat.config.ConfigSettings;
import com.momosoftworks.coldsweat.util.world.WorldHelper;
import com.simibubi.create.content.kinetics.fan.EncasedFanBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidStack;
import net.orion.create_cold_sweat.Config;
import net.orion.create_cold_sweat.utils.HeatUtils;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class EncasedFan extends BlockTemp {

    public EncasedFan(Block... blocks) {
        super(blocks);
    }

    public static final Map<LivingEntity, Set<EncasedFanBlockEntity>> fanMap = new HashMap<>();
    int ticking = 0;

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
        if (speed == 0 || ticking++ != 60) return 0d;
        ticking = 0;

        Vec3 entityPosition = livingEntity.position();

        Direction facing = encasedFan.getBlockState().getValue(BlockStateProperties.FACING);
        double angle = getAngleFromFront(blockPos, facing, entityPosition);

        // Yes I know I could've asked for radians instead but degrees is just more readable, come on
        double maxRadians = Math.toRadians(Config.CONFIG.maximumFanAngle.get());

        if (angle > maxRadians)
            return 0d;

        fanMap.computeIfAbsent(livingEntity, e -> new HashSet<>());
        Set<EncasedFanBlockEntity> fans = fanMap.get(livingEntity);
        fans.add(encasedFan);

        int blockRange = ConfigSettings.BLOCK_RANGE.get() == null ? 16 : ConfigSettings.BLOCK_RANGE.get();
        int maxFanDistance = Config.CONFIG.maxFanDistance.get();
        int maxDistance = Math.min(maxFanDistance, blockRange);
        int maxDistanceSqr = maxDistance * maxDistance;

        // Remove old fans
        fans.removeIf(fan -> fan.getBlockPos().distSqr(livingEntity.blockPosition()) > maxDistanceSqr);

        double biomeTemperature = WorldHelper.getBiomeTemperature(level, level.getBiome(blockPos));
        double playerTemperature = Temperature.get(livingEntity, Temperature.Trait.WORLD);

        boolean fanTemperatureInteraction = Config.CONFIG.fanTemperatureInteraction.get();

        // --- Step 1: Calculate combined target temperature ---
        double totalTargetTemp = 0;
        int contributingFans = 0;

        for (EncasedFanBlockEntity fan : fans) {
            Direction fanFacing = fan.getBlockState().getValue(BlockStateProperties.FACING);

            double fanTargetTemp = biomeTemperature;

            if (fanTemperatureInteraction) {
                BlockPos offset = fan.getBlockPos().offset(fanFacing.getNormal());
                BlockState state = level.getBlockState(offset);

                double tempForBlock = BlockTempRegistry.getBlockTempsFor(state)
                        .stream().findFirst().map(temp -> temp.getTemperature(level, null, state, offset, 0d)).orElse(0d);

                FluidState fluidState = level.getFluidState(offset);
                double fluidTemp = fluidState.isEmpty() ? 0d : HeatUtils.getFluidDataTemp(level, new FluidStack(fluidState.getType(), 1000));

                // Add either block or fluid temperature on top of biome
                fanTargetTemp += tempForBlock == 0d ? fluidTemp : tempForBlock;
            }

            totalTargetTemp += fanTargetTemp;
            contributingFans++;
        }

        double averageTargetTemp = contributingFans == 0 ? biomeTemperature : totalTargetTemp / contributingFans;

        // --- Step 2: Calculate combined efficiency ---
        double totalEfficiency = fans.stream()
            .mapToDouble(fan -> getEffectEfficiency(
                fan.getSpeed(),
                getAngleFromFront(fan.getBlockPos(), fan.getBlockState().getValue(BlockStateProperties.FACING), entityPosition),
                maxRadians
            ))
            .sum();

        double combinedEfficiency = 1 - Math.exp(-totalEfficiency);

        // --- Step 3: Apply generation ---
        return HeatUtils.blend(distance, getGeneratedTemperature(averageTargetTemp, playerTemperature, combinedEfficiency), Config.CONFIG.maxFanDistance.get());

    }

    private static double getGeneratedTemperature(double targetTemperature, double playerTemperature, double effectEfficiency) {
        double difference = targetTemperature - playerTemperature;
        double generated = effectEfficiency * difference;

        // If x approaches 0 from the left, then the max is 0, if x approaches 0 from the right, then the min is 0
        // This is so that it doesn't overshoot, a fan can't make you cooler than the surrounding air
        return targetTemperature > playerTemperature ? Math.max(0, generated) : Math.min(0, generated);
    }

    private static double getEffectEfficiency(float speed, double angle, double maxRadians) {
        double angleDifference = Math.abs(angle - maxRadians);
        double unitAngleDifference = angleDifference / maxRadians;
        float unitSpeed = Math.abs(speed) / 256;

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
