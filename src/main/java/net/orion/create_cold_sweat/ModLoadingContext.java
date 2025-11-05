package net.orion.create_cold_sweat;

import net.createmod.catnip.config.ui.BaseConfigScreen;
import net.minecraft.client.gui.screens.Screen;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.orion.create_cold_sweat.utils.HeatUtils;

import static net.orion.create_cold_sweat.utils.HeatUtils.createBlender;

@EventBusSubscriber(modid = CreateColdSweat.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModLoadingContext {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        net.neoforged.fml.ModLoadingContext.get().registerExtensionPoint(
                IConfigScreenFactory.class,
                () -> (ModContainer mc, Screen parent) -> new BaseConfigScreen(parent, CreateColdSweat.MOD_ID)
        );
    }

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
