package net.orion.create_cold_sweat;

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.slf4j.Logger;

@Mod(CreateColdSweat.MOD_ID)
public class CreateColdSweat
{
    public static final String MOD_ID = "create_cold_sweat";
    public static final Logger LOGGER = LogUtils.getLogger();

    public CreateColdSweat () {
        IEventBus FORGE_EVENT_BUS = MinecraftForge.EVENT_BUS;

        LOGGER.info("Registering..");

        FORGE_EVENT_BUS.register(new BlockTempRegister());

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SERVER_SPEC);

        LOGGER.info("Successfully registered.");
    }
}
