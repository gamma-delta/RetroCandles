package at.petrak.retrocandles.datagen;

import at.petrak.paucal.api.datagen.PaucalRecipeProvider;
import at.petrak.retrocandles.api.RetroCandlesAPI;
import at.petrak.retrocandles.lib.ModBlocks;
import at.petrak.retrocandles.lib.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
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

        // Yeah this recipe is OP
        // this is a Timmy-ass mod alright?
        ShapedRecipeBuilder.shaped(ModBlocks.TICKING_CANDLE)
            .define('C', ItemTags.CANDLES)
            .define('L', Items.CLOCK)
            .pattern(" L ")
            .pattern("LCL")
            .pattern(" L ")
            .unlockedBy("has_item", hasItem(ItemTags.CANDLES))
            .save(recipes);

        ShapelessRecipeBuilder.shapeless(ModItems.SEALING_WAX, 4)
            .requires(ItemTags.CANDLES)
            .requires(Items.CLAY)
            .requires(Items.LAPIS_LAZULI)
            .unlockedBy("has_item", hasItem(ItemTags.CANDLES))
            .save(recipes);

    }
}
