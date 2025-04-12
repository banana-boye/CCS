package net.orion.create_cold_sweat.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.orion.create_cold_sweat.CreateColdSweat;
import net.orion.create_cold_sweat.utils.TagUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class BlockTagGenerator extends BlockTagsProvider {

    public BlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, CreateColdSweat.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        IntrinsicTagAppender<Block> pipes = tag(TagUtils.PIPES);
        IntrinsicTagAppender<Block> containers = tag(TagUtils.FLUID_CONTAINERS);
        IntrinsicTagAppender<Block> tanks = tag(TagUtils.BOILER);
        IntrinsicTagAppender<Block> burners = tag(TagUtils.BLAZE_BURNER);
        IntrinsicTagAppender<Block> steamEngines = tag(TagUtils.STEAM_ENGINE);

        for (Block block : BuiltInRegistries.BLOCK) {
            String blockName = block.getDescriptionId().toLowerCase();

            if (blockName.contains("pipe") || (blockName.contains("mechanical") && blockName.contains("pump"))) {
                pipes.add(block);
            } else if (blockName.contains("tank")) {
                tanks.add(block);
            } else if (blockName.contains("basin")) {
                containers.add(block);
            } else if (blockName.contains("blaze") && blockName.contains("burner")) {
                burners.add(block);
            } else if (blockName.contains("steam") && blockName.contains("engine")) {
                steamEngines.add(block);
            }
        }
    }
}
