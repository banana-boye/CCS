package net.orion.createcoldsweat;

import com.momosoftworks.coldsweat.api.event.core.registry.BlockTempRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.orion.createcoldsweat.blockeffects.BlazeBurner;

@Mod.EventBusSubscriber(modid = CreateColdSweat.MODID)
public class BlockTempRegister {

    @SubscribeEvent
    public void register(BlockTempRegisterEvent blockTempRegisterEvent) {
        blockTempRegisterEvent.register(new BlazeBurner());
    }
}
