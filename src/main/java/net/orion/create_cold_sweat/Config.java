package net.orion.create_cold_sweat;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    public static final ModConfigSpec SERVER_SPEC;
    public static final Config CONFIG;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        CONFIG = new Config(builder);
        SERVER_SPEC = builder.build();
    }

    public final ModConfigSpec.BooleanValue blazeBurnerTemperature;
    public final ModConfigSpec.DoubleValue bBSmouldering;
    public final ModConfigSpec.DoubleValue bBFading;
    public final ModConfigSpec.DoubleValue bBKindled;
    public final ModConfigSpec.DoubleValue bBSeething;
    public final ModConfigSpec.IntValue maxBlazeBurnerDistance;

    public final ModConfigSpec.BooleanValue boilerTemperature;
    public final ModConfigSpec.DoubleValue boilerTemperatureIncrement;
    public final ModConfigSpec.IntValue maxBoilerDistance;

    public final ModConfigSpec.BooleanValue liquidTemperature;
    public final ModConfigSpec.DoubleValue defaultFluidDampener;
    public final ModConfigSpec.IntValue maxFluidContainerDistance;

    public final ModConfigSpec.BooleanValue fanCooling;
    public final ModConfigSpec.DoubleValue maximumFanAngle;
    public final ModConfigSpec.BooleanValue fanTemperatureInteraction;
    public final ModConfigSpec.IntValue maxFanDistance;
    public final ModConfigSpec.BooleanValue calculateDefaultLiquidTemperature;

    public final ModConfigSpec.IntValue maxSteamEngineDistance;

    public Config(ModConfigSpec.Builder builder) {
        builder.comment("Create: Cold Sweat settings")
                .push("General");

        // --- Blaze Burner ---
        blazeBurnerTemperature = builder
                .comment("Should blaze burners apply heat?")
                .define("Blaze burner", true);

        bBSmouldering = builder
                .comment("Blaze burner smouldering temperature (MC UNITS)")
                .defineInRange("Smouldering", MathConstants.ONE_CELSIUS_IN_MC_UNITS, 0d, 1d);

        bBFading = builder
                .comment("Blaze burner fading temperature (MC UNITS)")
                .defineInRange("Fading", MathConstants.ONE_CELSIUS_IN_MC_UNITS * 3, 0d, 1d);

        bBKindled = builder
                .comment("Blaze burner kindled temperature (MC UNITS)")
                .defineInRange("Kindled", MathConstants.ONE_CELSIUS_IN_MC_UNITS * 5, 0d, 1d);

        bBSeething = builder
                .comment("Blaze burner seething temperature (MC UNITS)")
                .defineInRange("Seething", MathConstants.ONE_CELSIUS_IN_MC_UNITS * 10, 0d, 1d);

        maxBlazeBurnerDistance = builder
                .comment("The maximum distance a blaze burner affects players")
                .defineInRange("Max Blaze Burner", 8, 1, 16);

        // --- Boiler ---
        boilerTemperature = builder
                .comment("Should Boilers apply heat?")
                .define("Boiler", true);

        boilerTemperatureIncrement = builder
                .comment("The amount of MC UNITS to increase per heat level (max heat level is 18) (per block)")
                .defineInRange("Boiler Increment", MathConstants.ONE_CELSIUS_IN_MC_UNITS / 9, 0d, 1d);

        maxBoilerDistance = builder
                .comment("The maximum distance a boiler affects players")
                .defineInRange("Max Boiler", 8, 1, 16);

        // --- Liquids ---
        liquidTemperature = builder
                .comment("Should Liquids apply heat while in containers?")
                .define("Liquids", true);

        defaultFluidDampener = builder
                .comment("The temperature dampening on all default calculated liquids (calculated temperature / dampening)")
                .defineInRange("Liquid temperature dampening", MathConstants.DEFAULT_FLUID_DAMPENING_VALUE, -16d, 16d);

        maxFluidContainerDistance = builder
                .comment("The maximum distance a fluid container affects players")
                .defineInRange("Max Fluid Container", 8, 1, 16);

        // --- Fans ---
        fanCooling = builder
                .comment("Should fans affect player temperature?")
                .define("Fans", true);

        maximumFanAngle = builder
                .comment("The maximum angle fans are allowed to affect the player (Degrees)")
                .defineInRange("Fan Angle", 110d, 90d, 180d);

        fanTemperatureInteraction = builder
                .comment("Should fans use the block directly in front of them to set a temperature")
                .define("Fan Temperature Interaction", false);

        maxFanDistance = builder
                .comment("The maximum distance a fan affects players")
                .defineInRange("Max Fan", 8, 1, 16);

        calculateDefaultLiquidTemperature = builder
                .comment("Whether the default fluid temperature should be calculated based off internal temperature values")
                .define("Default Liquid Temperature", false);

        // --- Steam Engine ---
        maxSteamEngineDistance = builder
                .comment("The maximum distance a steam engine affects players")
                .defineInRange("Max Steam Engine", 8, 1, 16);

        builder.pop();
    }

}
