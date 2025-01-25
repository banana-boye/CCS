package net.orion.createcoldsweat;

import com.momosoftworks.coldsweat.api.event.core.registry.BlockTempRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.orion.createcoldsweat.blockeffects.*;

@Mod.EventBusSubscriber(modid = CreateColdSweat.MODID)
public class BlockTempRegister {

    @SubscribeEvent
    public void register(BlockTempRegisterEvent blockTempRegisterEvent) {
        blockTempRegisterEvent.register(new BlazeBurner());
        blockTempRegisterEvent.register(new SteamEngine());
        blockTempRegisterEvent.register(new FluidTank());
        blockTempRegisterEvent.register(new PipesAndPumps());
        blockTempRegisterEvent.register(new FluidContainers());
    }
}
