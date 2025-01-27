package net.orion.createcoldsweat;

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.slf4j.Logger;

@Mod(CreateColdSweat.MODID)
public class CreateColdSweat
{
    public static final String MODID = "createcoldsweat";
    public static final Logger LOGGER = LogUtils.getLogger();

    public CreateColdSweat () {
        IEventBus FORGE_EVENT_BUS = MinecraftForge.EVENT_BUS;

        LOGGER.info("Registering..");

        FORGE_EVENT_BUS.register(new BlockTempRegister());

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SERVER_SPEC);

        LOGGER.info("Successfully registered.");
    }
}
