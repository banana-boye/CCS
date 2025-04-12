package net.orion.create_cold_sweat;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class Config {
    public static final ModConfigSpec SERVER_SPEC;
    public static final Config CONFIG;

    static {
        Pair<Config, ModConfigSpec> pair =
                new ModConfigSpec.Builder().configure(Config::new);
        CONFIG = pair.getLeft();
        SERVER_SPEC = pair.getRight();
    }

    public final ModConfigSpec.BooleanValue blazeBurnerTemperature;
    public final ModConfigSpec.DoubleValue bBSmouldering;
    public final ModConfigSpec.DoubleValue bBFading;
    public final ModConfigSpec.DoubleValue bBKindled;
    public final ModConfigSpec.DoubleValue bBSeething;

    public final ModConfigSpec.BooleanValue boilerTemperature;
    public final ModConfigSpec.DoubleValue boilerTemperatureIncrement;

    public final ModConfigSpec.BooleanValue liquidTemperature;
    public final ModConfigSpec.DoubleValue defaultFluidDampener;

    public Config(ModConfigSpec.Builder builder) {
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