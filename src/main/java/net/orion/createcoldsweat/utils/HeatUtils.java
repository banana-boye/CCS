package net.orion.createcoldsweat.utils;

import com.momosoftworks.coldsweat.util.math.CSMath;

import java.util.function.BiFunction;
import java.util.function.Function;

public class HeatUtils {
    public static BiFunction<Double, Double, Double> createBlender(int maxDistance) {
        return (Double distance, Double temperature) -> blend(distance, temperature, maxDistance);
    }

    public static double blend(double distance, double temperature, int maxDistance) {
        return CSMath.blend(temperature, 0, distance, 0.5, maxDistance);
    }
}
