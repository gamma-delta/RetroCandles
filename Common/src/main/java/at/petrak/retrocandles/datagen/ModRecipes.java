package at.petrak.retrocandles.datagen;

import at.petrak.paucal.api.datagen.PaucalRecipeProvider;
import at.petrak.retrocandles.api.RetroCandlesAPI;
import at.petrak.retrocandles.lib.ModBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;

public class ModRecipes extends PaucalRecipeProvider {
    public ModRecipes(DataGenerator gen) {
        super(gen, RetroCandlesAPI.MOD_ID);
    }

    @Override
    protected void makeRecipes(Consumer<FinishedRecipe> recipes) {
        ShapedRecipeBuilder.shaped(ModBlocks.INTERDICTION_CANDLE, 2)
            .define('D', Items.DIAMOND)
            .define('G', Items.GLOWSTONE_DUST)
            .define('C', ItemTags.CANDLES)
            .define('H', Items.HEART_OF_THE_SEA)
            .pattern("CDC")
            .pattern("DHD")
            .pattern("GGG")
            .unlockedBy("has_item", hasItem(ItemTags.CANDLES))
            .save(recipes);

        ShapedRecipeBuilder.shaped(ModBlocks.TICKING_CANDLE)
            .define('C', ItemTags.CANDLES)
            .define('L', Items.CLOCK)
            .define('N', Items.NETHER_STAR) // high quality gaming
            .pattern(" L ")
            .pattern("NCN")
            .pattern(" L ")
            .unlockedBy("has_item", hasItem(ItemTags.CANDLES))
            .save(recipes);
        ShapedRecipeBuilder.shaped(ModBlocks.TICKING_CANDLE)
            .define('C', ItemTags.CANDLES)
            .define('L', Items.CLOCK)
            .define('N', Items.NETHER_STAR)
            .pattern(" N ")
            .pattern("LCL")
            .pattern(" N ")
            .unlockedBy("has_item", hasItem(ItemTags.CANDLES))
            .save(recipes, modLoc("tick_accelerator_candle_rotated"));

    }
}
