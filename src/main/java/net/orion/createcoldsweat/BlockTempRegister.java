package net.orion.createcoldsweat;

import com.momosoftworks.coldsweat.api.event.core.registry.BlockTempRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.orion.createcoldsweat.blockeffects.BlazeBurner;
import net.orion.createcoldsweat.blockeffects.LavaContainer;
import net.orion.createcoldsweat.blockeffects.SteamEngineTank;
import net.orion.createcoldsweat.blockeffects.SteamEngine;

@Mod.EventBusSubscriber(modid = CreateColdSweat.MODID)
public class BlockTempRegister {

    @SubscribeEvent
    public void register(BlockTempRegisterEvent blockTempRegisterEvent) {
        blockTempRegisterEvent.register(new BlazeBurner());
        blockTempRegisterEvent.register(new SteamEngine());
        blockTempRegisterEvent.register(new SteamEngineTank());
        blockTempRegisterEvent.register(new LavaContainer());
    }
}
