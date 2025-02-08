package net.orion.create_cold_sweat.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DataPackRegistryEvent;
import net.orion.create_cold_sweat.CreateColdSweat;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = CreateColdSweat.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGeneratorRegister {
    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(event.includeServer(), new BlockTagGenerator(packOutput, lookupProvider, existingFileHelper));
    }

    public static final ResourceKey<Registry<FluidTemperatureType>> FLUID_TEMPERATURE_KEY = ResourceKey.createRegistryKey(new ResourceLocation(CreateColdSweat.MOD_ID, "fluid_temperatures"));

    @SubscribeEvent
    public static void onDataPackRegistry(DataPackRegistryEvent.NewRegistry newRegistry) {

        newRegistry.dataPackRegistry(FLUID_TEMPERATURE_KEY, FluidTemperatureType.CODEC);
    }
}
