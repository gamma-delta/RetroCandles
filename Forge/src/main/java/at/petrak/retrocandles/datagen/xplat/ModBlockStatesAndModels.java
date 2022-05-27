package at.petrak.retrocandles.datagen.xplat;

import at.petrak.paucal.api.forge.datagen.PaucalBlockStateAndModelProvider;
import at.petrak.retrocandles.api.RetroCandlesAPI;
import at.petrak.retrocandles.common.block.BlockModCandle;
import at.petrak.retrocandles.lib.ModBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockStatesAndModels extends PaucalBlockStateAndModelProvider {
    public ModBlockStatesAndModels(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, RetroCandlesAPI.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        candle(ModBlocks.TICKING_CANDLE, "tick_accelerator_candle");
        candle(ModBlocks.INTERDICTION_CANDLE, "interdiction_candle");
    }

    protected void candle(BlockModCandle candle, String nameStub) {
        getVariantBuilder(candle).forAllStates(bs -> {
            int candleCount = bs.getValue(BlockModCandle.CANDLES);
            var count = new String[]{"one", "two", "three", "four"}[candleCount - 1];
            var litness = bs.getValue(BlockModCandle.LIT) ? "_lit" : "";
            var sealedness = bs.getValue(BlockModCandle.SEALED) ? "_sealed" : "";

            var name = nameStub + "_" + count + litness + sealedness;
            var texName = modLoc("block/" + nameStub + litness + sealedness);
            var template = new String[]{
                "candle",
                "two_candles",
                "three_candles",
                "four_candles",
            }[candleCount - 1];
            var model = models().withExistingParent(name, new ResourceLocation("block/template_" + template))
                .texture("all", texName)
                .texture("particle", texName);
            return ConfiguredModel.builder()
                .modelFile(model)
                .build();
        });
    }
}
