package net.orion.create_cold_sweat;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

@Mod(CreateColdSweat.MOD_ID)
public class CreateColdSweat
{
    public static final String MOD_ID = "create_cold_sweat";
    public static final Logger LOGGER = LogUtils.getLogger();

    public CreateColdSweat (IEventBus eventBus, ModContainer container) {
        LOGGER.info("Registering to events..");

        IEventBus eventBus1 = NeoForge.EVENT_BUS;

        eventBus1.register(new BlockTempRegister());

        container.registerConfig(ModConfig.Type.SERVER, Config.SERVER_SPEC);

        LOGGER.info("Event registering complete.");
    }
}
