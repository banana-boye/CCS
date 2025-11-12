package net.orion.create_cold_sweat;

import net.createmod.catnip.config.ui.BaseConfigScreen;
import net.minecraft.client.gui.screens.Screen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@EventBusSubscriber(modid = CreateColdSweat.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModLoadingContext {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        if (FMLEnvironment.dist.isDedicatedServer()) return;
        net.neoforged.fml.ModLoadingContext.get().registerExtensionPoint(
                IConfigScreenFactory.class,
                () -> (ModContainer mc, Screen parent) -> new BaseConfigScreen(parent, CreateColdSweat.MOD_ID)
        );
    }
}
