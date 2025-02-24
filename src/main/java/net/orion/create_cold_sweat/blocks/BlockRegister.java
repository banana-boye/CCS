package net.orion.create_cold_sweat.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.orion.create_cold_sweat.CreateColdSweat;

public class BlockRegister {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, CreateColdSweat.MOD_ID);

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
