package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMap.sExtractorRecipes;
import static gregtech.api.util.GT_ModHandler.getIC2Item;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.WILDCARD;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;

public class ExtractorRecipes implements Runnable {

    @Override
    public void run() {
        addExtractionRecipe(new ItemStack(Blocks.bookshelf, 1, WILDCARD), new ItemStack(Items.book, 3, 0));
        addExtractionRecipe(
            new ItemStack(Items.slime_ball, 1),
            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.RawRubber, 2L));
        addExtractionRecipe(
            ItemList.IC2_Resin.get(1L),
            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.RawRubber, 3L));
        addExtractionRecipe(
            getIC2Item("rubberSapling", 1L),
            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.RawRubber, 1L));
        addExtractionRecipe(
            getIC2Item("rubberLeaves", 16L),
            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.RawRubber, 1L));

        addExtractionRecipe(ItemList.Cell_Air.get(1L), ItemList.Cell_Empty.get(1L));
    }

    public void addExtractionRecipe(ItemStack input, ItemStack output) {
        output = GT_OreDictUnificator.get(true, output);
        GT_Values.RA.stdBuilder()
            .itemInputs(input)
            .itemOutputs(output)
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(sExtractorRecipes);
    }
}
