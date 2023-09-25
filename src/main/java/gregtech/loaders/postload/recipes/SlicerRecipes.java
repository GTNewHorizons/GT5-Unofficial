package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.slicerRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;

public class SlicerRecipes implements Runnable {

    @Override
    public void run() {
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Food_Dough_Chocolate.get(1), ItemList.Shape_Slicer_Flat.get(0))
            .itemOutputs(ItemList.Food_Raw_Cookie.get(4))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(4)
            .addTo(slicerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Food_Baked_Bun.get(1), ItemList.Shape_Slicer_Flat.get(0))
            .itemOutputs(ItemList.Food_Sliced_Bun.get(2))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(4)
            .addTo(slicerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Food_Baked_Bread.get(1), ItemList.Shape_Slicer_Flat.get(0))
            .itemOutputs(ItemList.Food_Sliced_Bread.get(2))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(4)
            .addTo(slicerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Food_Baked_Baguette.get(1), ItemList.Shape_Slicer_Flat.get(0))
            .itemOutputs(ItemList.Food_Sliced_Baguette.get(2))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(4)
            .addTo(slicerRecipes);
    }
}
