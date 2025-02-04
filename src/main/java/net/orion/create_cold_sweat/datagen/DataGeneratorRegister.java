package net.orion.create_cold_sweat.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.orion.create_cold_sweat.CreateColdSweat;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = CreateColdSweat.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGeneratorRegister {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(event.includeServer(), new BlockTagGenerator(packOutput, lookupProvider, existingFileHelper));
    }
}
