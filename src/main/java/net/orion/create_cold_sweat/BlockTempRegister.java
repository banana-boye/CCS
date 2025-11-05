package net.orion.create_cold_sweat;

import com.momosoftworks.coldsweat.api.event.core.registry.BlockTempRegisterEvent;
import com.momosoftworks.coldsweat.api.temperature.block_temp.BlockTemp;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.orion.create_cold_sweat.blockeffects.*;
import net.orion.create_cold_sweat.utils.TagUtils;

import java.util.function.BiConsumer;
import java.util.function.Function;

@EventBusSubscriber(modid = CreateColdSweat.MOD_ID)
public class BlockTempRegister {

    @SubscribeEvent
    public static void register(BlockTempRegisterEvent blockTempRegisterEvent) {
        BiConsumer<TagKey<Block>, Function<Block, BlockTemp>> registerBlockTempWithTag = (tagKey, blockBlockTempFunction) -> registerBlocksOfTag(blockTempRegisterEvent, tagKey, blockBlockTempFunction);

        registerBlockTempWithTag.accept(TagUtils.BLAZE_BURNER, BlazeBurner::new);
        registerBlockTempWithTag.accept(TagUtils.STEAM_ENGINE, SteamEngine::new);
        registerBlockTempWithTag.accept(TagUtils.BOILER, Boiler::new);
        registerBlockTempWithTag.accept(TagUtils.PIPES, PipesAndPumps::new);
        registerBlockTempWithTag.accept(TagUtils.FLUID_CONTAINERS, FluidContainers::new);
        registerBlockTempWithTag.accept(TagUtils.ENCASED_FAN, EncasedFan::new);
        blockTempRegisterEvent.register(new LitBlazeBurner());

        CreateColdSweat.LOGGER.info("Registered Create: Cold Sweat BlockEffects");
    }

    public static void registerBlocksOfTag(BlockTempRegisterEvent blockTempRegisterEvent, TagKey<Block> tagKey, Function<Block, BlockTemp> blockTempSupplier) {
        TagUtils.getBlocksTaggedWith(tagKey).forEach(block -> {
            CreateColdSweat.LOGGER.info("Registering {} with tag {}", block.getName(), tagKey.toString());
            blockTempRegisterEvent.register(blockTempSupplier.apply(block));
        });
    }
}
