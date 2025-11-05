package net.orion.create_cold_sweat;

import com.mojang.logging.LogUtils;
import com.momosoftworks.coldsweat.api.event.core.registry.BlockTempRegisterEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

@Mod(CreateColdSweat.MOD_ID)
public class CreateColdSweat {
    public static final String MOD_ID = "create_cold_sweat";
    public static final Logger LOGGER = LogUtils.getLogger();

    public CreateColdSweat (IEventBus modEventBus, ModContainer container) {
        IEventBus NEO_FORGE_EVENT_BUS = NeoForge.EVENT_BUS;

        LOGGER.info("Registering to events..");

        NEO_FORGE_EVENT_BUS.addListener(BlockTempRegisterEvent.class, BlockTempRegister::register);

        container.registerConfig(ModConfig.Type.SERVER, Config.SERVER_SPEC);

        LOGGER.info("Event registering complete.");
    }
}
