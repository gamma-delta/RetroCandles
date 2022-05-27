package at.petrak.retrocandles.datagen;

import at.petrak.retrocandles.api.RetroCandlesAPI;
import at.petrak.retrocandles.datagen.xplat.ModBlockStatesAndModels;
import at.petrak.retrocandles.datagen.xplat.ModItemModels;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = RetroCandlesAPI.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModDataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent evt) {
        var gen = evt.getGenerator();
        var efh = evt.getExistingFileHelper();

        if (System.getProperty("retrocandles.xplat_datagen") != null) {
            if (evt.includeClient()) {
                gen.addProvider(new ModBlockStatesAndModels(gen, efh));
                gen.addProvider(new ModItemModels(gen, efh));
            }
            if (evt.includeServer()) {
                gen.addProvider(new ModLootTables(gen));
                gen.addProvider(new ModRecipes(gen));
            }
        } else {
        }
    }
}
