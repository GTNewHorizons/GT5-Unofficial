package gregtech.loaders.postload.recipes;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;

public class SlicerRecipes implements Runnable {

    @Override
    public void run() {
        GT_Values.RA.addSlicerRecipe(
                ItemList.Food_Dough_Chocolate.get(1L),
                ItemList.Shape_Slicer_Flat.get(0L),
                ItemList.Food_Raw_Cookie.get(4L),
                128,
                4);
        GT_Values.RA.addSlicerRecipe(
                ItemList.Food_Baked_Bun.get(1L),
                ItemList.Shape_Slicer_Flat.get(0L),
                ItemList.Food_Sliced_Bun.get(2L),
                128,
                4);
        GT_Values.RA.addSlicerRecipe(
                ItemList.Food_Baked_Bread.get(1L),
                ItemList.Shape_Slicer_Flat.get(0L),
                ItemList.Food_Sliced_Bread.get(2L),
                128,
                4);
        GT_Values.RA.addSlicerRecipe(
                ItemList.Food_Baked_Baguette.get(1L),
                ItemList.Shape_Slicer_Flat.get(0L),
                ItemList.Food_Sliced_Baguette.get(2L),
                128,
                4);
    }
}
