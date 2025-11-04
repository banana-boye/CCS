package net.orion.create_cold_sweat;

import net.createmod.catnip.config.ui.BaseConfigScreen;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;

@Mod.EventBusSubscriber(modid = CreateColdSweat.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModLoadingContext {
    @SubscribeEvent
    public static void onLoadComplete(FMLLoadCompleteEvent event) {
        ModContainer createContainer = ModList.get()
                .getModContainerById(CreateColdSweat.MOD_ID)
                .orElseThrow(() -> new IllegalStateException("Create cold sweat mod container missing on LoadComplete"));
        createContainer.registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory(
                        (mc, previousScreen) -> new BaseConfigScreen(previousScreen, CreateColdSweat.MOD_ID)));
    }
}
