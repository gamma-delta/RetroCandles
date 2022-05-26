package at.petrak.retrocandles.datagen.xplat;

import at.petrak.paucal.api.forge.datagen.PaucalItemModelProvider;
import at.petrak.retrocandles.api.RetroCandlesAPI;
import at.petrak.retrocandles.lib.ModBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemModels extends PaucalItemModelProvider {
    public ModItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, RetroCandlesAPI.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(ModBlocks.TICKING_CANDLE.asItem());
    }
}
