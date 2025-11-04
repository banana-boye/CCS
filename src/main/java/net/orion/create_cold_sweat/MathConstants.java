package net.orion.create_cold_sweat;

public class MathConstants {
    public static final double ONE_CELSIUS_IN_MC_UNITS = 1 / 23.3;

    // TODO:: FIGURE OUT MAGIC NUMBER LATER, HAS SOMETHING TO DO WITH MINECRAFT'S LAVA TEMP
    public static final double DEFAULT_FLUID_DAMPENING_VALUE = 5.391630902d;

    // Avoids that multiple fans affect the player's temperature too drastically causing a yo-yo effect
    public static final double FAN_DAMPENER = 0.2d;
}
