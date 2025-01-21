package net.orion.createcoldsweat;

import com.momosoftworks.coldsweat.api.event.core.registry.BlockTempRegisterEvent;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.orion.createcoldsweat.blockeffects.BlazeBurner;
import net.orion.createcoldsweat.blockeffects.Boiler;
import net.orion.createcoldsweat.blockeffects.SteamEngine;

@Mod.EventBusSubscriber(modid = CreateColdSweat.MODID)
public class BlockTempRegister {

    @SubscribeEvent
    public void register(BlockTempRegisterEvent blockTempRegisterEvent) {
        blockTempRegisterEvent.register(new BlazeBurner());
        for (Block block : ForgeRegistries.BLOCKS.getValues()) if(block instanceof SteamEngine) blockTempRegisterEvent.register(new Boiler(block));
    }
}
