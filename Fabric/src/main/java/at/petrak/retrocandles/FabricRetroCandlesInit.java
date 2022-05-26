package at.petrak.retrocandles;

import at.petrak.retrocandles.fabric.FabricRetroCandlesConfig;
import at.petrak.retrocandles.lib.ModBlockEntities;
import at.petrak.retrocandles.lib.ModBlocks;
import at.petrak.retrocandles.lib.ModItems;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiConsumer;

public class FabricRetroCandlesInit implements ModInitializer {
    @Override
    public void onInitialize() {
        FabricRetroCandlesConfig.setup();

        initRegistries();
    }

    private void initRegistries() {
        ModBlocks.registerBlocks(bind(Registry.BLOCK));
        ModBlocks.registerBlockItems(bind(Registry.ITEM));
        ModBlockEntities.registerBlockEntities(bind(Registry.BLOCK_ENTITY_TYPE));
        ModItems.registerItems(bind(Registry.ITEM));
    }

    private static <T> BiConsumer<T, ResourceLocation> bind(Registry<? super T> registry) {
        return (t, id) -> Registry.register(registry, id, t);
    }
}
