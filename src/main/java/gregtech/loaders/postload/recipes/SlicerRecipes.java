package gregtech.loaders.postload.recipes;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;

import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sSlicerRecipes;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;

public class SlicerRecipes implements Runnable {

    @Override
    public void run() {
        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Food_Dough_Chocolate.get(1),
                ItemList.Shape_Slicer_Flat.get(0)
            )
            .itemOutputs(
                ItemList.Food_Raw_Cookie.get(4)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(4)
            .addTo(sSlicerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Food_Baked_Bun.get(1),
                ItemList.Shape_Slicer_Flat.get(0)
            )
            .itemOutputs(
                ItemList.Food_Sliced_Bun.get(2)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(4)
            .addTo(sSlicerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Food_Baked_Bread.get(1),
                ItemList.Shape_Slicer_Flat.get(0)
            )
            .itemOutputs(
                ItemList.Food_Sliced_Bread.get(2)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(4)
            .addTo(sSlicerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Food_Baked_Baguette.get(1),
                ItemList.Shape_Slicer_Flat.get(0)
            )
            .itemOutputs(
                ItemList.Food_Sliced_Baguette.get(2)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(4)
            .addTo(sSlicerRecipes);
    }
}
