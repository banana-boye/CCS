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

    public Config(ForgeConfigSpec.Builder builder) {
        builder.comment("Create: Cold Sweat settings")
                .push("General");

        blazeBurnerTemperature = builder
                .comment("Should blaze burners apply heat?")
                .define("Blaze burner", true);
        bBSmouldering = builder
                .comment("Blaze burner smouldering temperature (MC UNITS)")
                .defineInRange("Smouldering", 0.04291845494d,0d,1d);
        bBFading = builder
                .comment("Blaze burner fading temperature (MC UNITS)")
                .defineInRange("Fading", 0.1287553648d, 0d, 1d);
        bBKindled = builder
                .comment("Blaze burner kindled temperature (MC UNITS)")
                .defineInRange("Kindled", 0.2145922747d, 0d, 1d);
        bBSeething = builder
                .comment("Blaze burner seething temperature (MC UNITS)")
                .defineInRange("Seething", 0.4291845494d, 0d, 1d);

        boilerTemperature = builder
                .comment("Should Boilers apply heat?")
                .define("Boiler", true);

        boilerTemperatureIncrement = builder
                .comment("The amount of MC UNITS to increase per heat level (max heat level is 18) (per block)")
                .defineInRange("Boiler Increment", 0.00476871721d, 0d, 1d);

        liquidTemperature = builder
                .comment("Should Liquids apply heat while in containers?")
                .define("Liquids", true);

        defaultFluidDampener = builder
                .comment("The temperature dampening on all default calculated liquids (calculated temperature / dampening)")
                .defineInRange("Liquid temperature dampening", 5.391630902d, -100d, 100d);

        builder.pop();
    }
}
