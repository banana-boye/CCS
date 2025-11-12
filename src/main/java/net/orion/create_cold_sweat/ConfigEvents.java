package net.orion.create_cold_sweat;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.orion.create_cold_sweat.utils.HeatUtils;

import static net.orion.create_cold_sweat.utils.HeatUtils.createBlender;

@Mod.EventBusSubscriber(modid = CreateColdSweat.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ConfigEvents {
    @SubscribeEvent
    public static void onConfigLoading(ModConfigEvent.Loading event) {
        setConfig();
    }

    @SubscribeEvent
    public static void onConfigReloading(ModConfigEvent.Reloading event) {
        setConfig();
    }

    public static void setConfig() {
        HeatUtils.fluidBlend = createBlender(Config.CONFIG.maxFluidContainerDistance.get());
        HeatUtils.blazeBlend = createBlender(Config.CONFIG.maxBlazeBurnerDistance.get());
        HeatUtils.boilerBlend = createBlender(Config.CONFIG.maxBoilerDistance.get());
        HeatUtils.steamEngineBlend = createBlender(Config.CONFIG.maxSteamEngineDistance.get());
    }
}
