package at.petrak.retrocandles.datagen.xplat;

import at.petrak.paucal.api.forge.datagen.PaucalItemModelProvider;
import at.petrak.retrocandles.api.RetroCandlesAPI;
import at.petrak.retrocandles.lib.ModBlocks;
import at.petrak.retrocandles.lib.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemModels extends PaucalItemModelProvider {
    public ModItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, RetroCandlesAPI.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(ModItems.SEALING_WAX);

        simpleItem(ModBlocks.TICKING_CANDLE.asItem());
        simpleItem(ModBlocks.INTERDICTION_CANDLE.asItem());
    }
}
