package net.orion.create_cold_sweat.blockeffects;

import com.momosoftworks.coldsweat.api.temperature.block_temp.BlockTemp;
import com.momosoftworks.coldsweat.api.util.Temperature;
import com.momosoftworks.coldsweat.util.world.WorldHelper;
import com.simibubi.create.content.kinetics.fan.EncasedFanBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import net.orion.create_cold_sweat.Config;
import net.orion.create_cold_sweat.MathConstants;
import net.orion.create_cold_sweat.utils.HeatUtils;
import org.jetbrains.annotations.Nullable;

public class EncasedFan extends BlockTemp {

    public EncasedFan(Block... blocks) {
        super(blocks);
    }

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

        if (encasedFan.getSpeed() == 0) return 0d;

        Vec3 entityPosition = livingEntity.position();

        Direction facing = encasedFan.getBlockState().getValue(BlockStateProperties.FACING);
        double angle = getAngleFromFront(blockPos, facing, entityPosition);

        // Yes I know I could've asked for radians instead but degrees is just more readable, come on
        double maxRadians = Math.toRadians(Config.CONFIG.maximumFanAngle.get());

        if (angle <= maxRadians) {
            // Efficiency
            double effectEfficiency = getEffectEfficiency(encasedFan, angle, maxRadians);
            double targetTemperature = WorldHelper.getBiomeTemperature(level, level.getBiome(blockPos));
            double playerTemperature = Temperature.get(livingEntity, Temperature.Trait.WORLD);
            double generatedTemperature = getGeneratedTemperature(targetTemperature, playerTemperature, effectEfficiency);
            return HeatUtils.blend(distance, generatedTemperature, (int) (encasedFan.getMaxDistance() + 0.5));
        }

        return 0d;
    }

    private static double getGeneratedTemperature(double targetTemperature, double playerTemperature, double effectEfficiency) {
        double difference = targetTemperature - playerTemperature;
        double generated = effectEfficiency * difference;

        // If x approaches 0 from the left, then the max is 0, if x approaches 0 from the right, then the min is 0
        // This is so that it doesn't overshoot, a fan can't make you cooler than the surrounding air
        double generatedTemperature = targetTemperature > playerTemperature ? Math.max(0, generated) : Math.min(0, generated);
        generatedTemperature = generatedTemperature * MathConstants.FAN_DAMPENER;
        return generatedTemperature;
    }

    private static double getEffectEfficiency(EncasedFanBlockEntity encasedFan, double angle, double maxRadians) {
        double angleDifference = Math.abs(angle - maxRadians);
        double unitAngleDifference = angleDifference / maxRadians;
        float unitSpeed = encasedFan.getSpeed() / 256;

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
