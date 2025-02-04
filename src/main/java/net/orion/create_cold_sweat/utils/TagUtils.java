package net.orion.create_cold_sweat.utils;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import net.orion.create_cold_sweat.CreateColdSweat;

import java.util.ArrayList;
import java.util.List;

public class TagUtils {
    public static final TagKey<Block> PIPES = blockTag("pipe_block");
    public static final TagKey<Block> FLUID_CONTAINERS = blockTag("fluid_container_block");
    public static final TagKey<Block> BOILER = blockTag("fluid_tank_block");
    public static final TagKey<Block> STEAM_ENGINE = blockTag("steam_engine_block");
    public static final TagKey<Block> BLAZE_BURNER = blockTag("blaze_burner_block");

    public static TagKey<Block> blockTag(String name) {
        return BlockTags.create(new ResourceLocation(CreateColdSweat.MODID, name));
    }

    public static List<Block> getBlocksTaggedWith(TagKey<Block> tagKey) {
        List<Block> blocks = new ArrayList<>();
        for (Block block : ForgeRegistries.BLOCKS) {
            if (block.defaultBlockState().is(tagKey)) {
                blocks.add(block);
                CreateColdSweat.LOGGER.info(block.getName().toString());
            }
        }
        return blocks;
    }
}
