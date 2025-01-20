package net.orion.createcoldsweat;

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

    public Config(ForgeConfigSpec.Builder builder) {
        builder.comment("General settings for YourMod")
                .push("General");

        blazeBurnerTemperature = builder
                .comment("Should blaze burners apply heat?")
                .define("Blaze burner", true);
        bBSmouldering = builder
                .comment("Blaze burner smouldering temperature (MC UNITS)")
                .defineInRange("Kindled", 0.04291845494d,0d,1d);
        bBFading = builder
                .comment("Blaze burner fading temperature (MC UNITS)")
                .defineInRange("exampleFeatureEnabled", 0.1287553648d, 0d, 1d);
        bBKindled = builder
                .comment("Blaze burner kindled temperature (MC UNITS)")
                .defineInRange("exampleFeatureEnabled", 0.2145922747d, 0d, 1d);
        bBSeething = builder
                .comment("Blaze burner seething temperature (MC UNITS)")
                .defineInRange("exampleFeatureEnabled", 0.4291845494d, 0d, 1d);

        boilerTemperature = builder
                .comment("Should Boilers apply heat?")
                .define("Boiler", true);

        builder.pop();
    }
}
