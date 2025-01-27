package net.orion.createcoldsweat;

import com.momosoftworks.coldsweat.api.event.core.registry.BlockTempRegisterEvent;
import com.momosoftworks.coldsweat.api.temperature.block_temp.BlockTemp;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.orion.createcoldsweat.blockeffects.*;
import net.orion.createcoldsweat.utils.TagUtils;

import java.util.function.BiConsumer;
import java.util.function.Function;

@Mod.EventBusSubscriber(modid = CreateColdSweat.MODID)
public class BlockTempRegister {

    @SubscribeEvent
    public void register(BlockTempRegisterEvent blockTempRegisterEvent) {
        BiConsumer<TagKey<Block>, Function<Block, BlockTemp>> registerBlockTemp = (tagKey, blockBlockTempFunction) -> registerBlocksOfTag(blockTempRegisterEvent, tagKey, blockBlockTempFunction);
        CreateColdSweat.LOGGER.info("Registering Create: Cold Sweat BlockEffects");
        registerBlockTemp.accept(TagUtils.BLAZE_BURNER, BlazeBurner::new);
        registerBlockTemp.accept(TagUtils.STEAM_ENGINE, SteamEngine::new);
        registerBlockTemp.accept(TagUtils.BOILER, Boiler::new);
        registerBlockTemp.accept(TagUtils.PIPES, PipesAndPumps::new);
        registerBlockTemp.accept(TagUtils.TANKS, FluidContainers::new);
        CreateColdSweat.LOGGER.info("Registered Create: Cold Sweat BlockEffects");
    }

    public static void registerBlocksOfTag(BlockTempRegisterEvent blockTempRegisterEvent, TagKey<Block> tagKey, Function<Block, BlockTemp> blockTempSupplier) {
        TagUtils.getBlocksTaggedWith(tagKey).forEach(block -> blockTempRegisterEvent.register(blockTempSupplier.apply(block)));
    }
}
