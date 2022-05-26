package at.petrak.retrocandles;

import at.petrak.retrocandles.api.RetroCandlesAPI;
import at.petrak.retrocandles.forge.ForgeRetroCandlesConfig;
import at.petrak.retrocandles.lib.ModBlockEntities;
import at.petrak.retrocandles.lib.ModBlocks;
import at.petrak.retrocandles.lib.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Mod(RetroCandlesAPI.MOD_ID)
public class ForgeRetroCandlesInit {
    public ForgeRetroCandlesInit() {
        var specPair = new ForgeConfigSpec.Builder().configure(ForgeRetroCandlesConfig::new);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, specPair.getRight());
        RetroCandlesConfig.setCommon(specPair.getLeft());

        initRegistries();

        var modBus = FMLJavaModLoadingContext.get().getModEventBus();
        var evBus = MinecraftForge.EVENT_BUS;
    }

    private void initRegistries() {
        bind(ForgeRegistries.BLOCKS, ModBlocks::registerBlocks);
        bind(ForgeRegistries.ITEMS, ModBlocks::registerBlockItems);
        bind(ForgeRegistries.BLOCK_ENTITIES, ModBlockEntities::registerBlockEntities);
        bind(ForgeRegistries.ITEMS, ModItems::registerItems);
    }

    private static <T extends IForgeRegistryEntry<T>> void bind(IForgeRegistry<T> registry,
        Consumer<BiConsumer<T, ResourceLocation>> source) {
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(registry.getRegistrySuperType(),
            (RegistryEvent.Register<T> event) -> {
                IForgeRegistry<T> forgeRegistry = event.getRegistry();
                source.accept((t, rl) -> {
                    t.setRegistryName(rl);
                    forgeRegistry.register(t);
                });
            });
    }
}
