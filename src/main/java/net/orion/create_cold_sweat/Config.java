package net.orion.create_cold_sweat;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
    public static final ForgeConfigSpec SERVER_SPEC;
    public static final Config CONFIG;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        CONFIG = new Config(builder);
        SERVER_SPEC = builder.build();
    }

    public final ForgeConfigSpec.BooleanValue blazeBurnerTemperature;
    public final ForgeConfigSpec.DoubleValue bBSmouldering;
    public final ForgeConfigSpec.DoubleValue bBFading;
    public final ForgeConfigSpec.DoubleValue bBKindled;
    public final ForgeConfigSpec.DoubleValue bBSeething;

    public final ForgeConfigSpec.BooleanValue boilerTemperature;
    public final ForgeConfigSpec.DoubleValue boilerTemperatureIncrement;

    public final ForgeConfigSpec.BooleanValue liquidTemperature;
    public final ForgeConfigSpec.DoubleValue defaultFluidDampener;
    public final ForgeConfigSpec.BooleanValue fanCooling;
    public final ForgeConfigSpec.DoubleValue maximumFanAngle;
    public final ForgeConfigSpec.BooleanValue fanTemperatureInteraction;

    public final ForgeConfigSpec.BooleanValue calculateDefaultLiquidTemperature;

    public Config(ForgeConfigSpec.Builder builder) {
        builder.comment("Create: Cold Sweat settings")
                .push("General");

        blazeBurnerTemperature = builder
                .comment("Should blaze burners apply heat?")
                .define("Blaze burner", true);
        bBSmouldering = builder
                .comment("Blaze burner smouldering temperature (MC UNITS)")
                .defineInRange("Smouldering", MathConstants.ONE_CELSIUS_IN_MC_UNITS,0d,1d);
        bBFading = builder
                .comment("Blaze burner fading temperature (MC UNITS)")
                .defineInRange("Fading", MathConstants.ONE_CELSIUS_IN_MC_UNITS * 3, 0d, 1d);
        bBKindled = builder
                .comment("Blaze burner kindled temperature (MC UNITS)")
                .defineInRange("Kindled", MathConstants.ONE_CELSIUS_IN_MC_UNITS * 5, 0d, 1d);
        bBSeething = builder
                .comment("Blaze burner seething temperature (MC UNITS)")
                .defineInRange("Seething", MathConstants.ONE_CELSIUS_IN_MC_UNITS * 10, 0d, 1d);

        boilerTemperature = builder
                .comment("Should Boilers apply heat?")
                .define("Boiler", true);

        boilerTemperatureIncrement = builder
                .comment("The amount of MC UNITS to increase per heat level (max heat level is 18) (per block)")
                .defineInRange("Boiler Increment", MathConstants.ONE_CELSIUS_IN_MC_UNITS / 9, 0d, 1d);

        liquidTemperature = builder
                .comment("Should Liquids apply heat while in containers?")
                .define("Liquids", true);

        fanCooling = builder
                .comment("Should fans affect player temperature?")
                .define("Fans", true);

        fanTemperatureInteraction = builder
                .comment("Should fans use the block directly in front of them to set a temperature")
                .define("Fan Temperature Interaction", false);

        maximumFanAngle = builder
                .comment("The maximum angle fans are allowed to affect the player (Degrees)")
                .defineInRange("Fan Angle", 110d, 90d, 180d);

        defaultFluidDampener = builder
                .comment("The temperature dampening on all default calculated liquids (calculated temperature / dampening)")
                .defineInRange("Liquid temperature dampening", MathConstants.DEFAULT_FLUID_DAMPENING_VALUE, -100d, 100d);

        calculateDefaultLiquidTemperature = builder
                .comment("Whether the default fluid temperature should be calculated based off internal temperature values")
                .define("Default Liquid Temperature", false);

        builder.pop();
    }
}
