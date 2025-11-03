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
import net.orion.create_cold_sweat.CreateColdSweat;
import org.jetbrains.annotations.Nullable;

public class EncasedFan extends BlockTemp {

    public EncasedFan(Block... blocks) {
        super(blocks);
    }

    @Override
    public double getTemperature(Level level, @Nullable LivingEntity livingEntity, BlockState blockState, BlockPos blockPos, double distance) {
        boolean configDisabled = false;
        boolean doesNotHaveBlock = !this.hasBlock(blockState.getBlock());

        if (
                configDisabled ||
                doesNotHaveBlock ||
                !(level.getBlockEntity(blockPos) instanceof EncasedFanBlockEntity encasedFan)
        ) return 0d;

        Vec3 entityPosition = livingEntity.position();

        Direction facing = encasedFan.getBlockState().getValue(BlockStateProperties.FACING);
        double angle = getAngleFromFront(blockPos, facing, entityPosition);
        int maxDegrees = 110;
        double radians = Math.toRadians(maxDegrees);

        if (encasedFan.getSpeed() > 0 && (angle <= radians)) {
            double effectEfficiency = ((Math.abs(angle - radians) / radians) / 2) + ((encasedFan.getSpeed() / 256) / 2);
            double targetTemperature = WorldHelper.getBiomeTemperature(level, level.getBiome(blockPos));
            double playerTemperature = Temperature.get(livingEntity, Temperature.Trait.WORLD);
            double difference = targetTemperature - playerTemperature;
            double generated = effectEfficiency * difference;
            double generatedCap = targetTemperature > playerTemperature ? Math.max(0, generated) : Math.min(0, generated);
            CreateColdSweat.LOGGER.info("diff {}\nefficiency: {}\nworld temp: {}\n entity temp: {}\n return temp: {}", Temperature.convert(difference, Temperature.Units.MC, Temperature.Units.C, false), effectEfficiency, Temperature.convert(targetTemperature, Temperature.Units.MC, Temperature.Units.C, false), Temperature.convert(playerTemperature, Temperature.Units.MC, Temperature.Units.C, false), Temperature.convert(generatedCap, Temperature.Units.MC, Temperature.Units.C, false));
            return generatedCap;
        }

        return 0d;
    }

    public static double getAngleFromFront(BlockPos fanPos, Direction facing, Vec3 targetPos) {
        Vec3 fanOrigin = Vec3.atCenterOf(fanPos).add(Vec3.atLowerCornerOf(facing.getNormal()).scale(0.5));
        Vec3 toTarget = targetPos.subtract(fanOrigin).normalize();
        Vec3 facingVec = Vec3.atLowerCornerOf(facing.getNormal()).normalize();
        double dot = facingVec.dot(toTarget);
        return Math.acos(dot);
    }

}
