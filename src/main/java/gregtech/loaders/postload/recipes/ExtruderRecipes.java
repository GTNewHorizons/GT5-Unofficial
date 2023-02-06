package gregtech.loaders.postload.recipes;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;

public class ExtruderRecipes implements Runnable {

    @Override
    public void run() {
        GT_Values.RA.addExtruderRecipe(
                ItemList.FR_Wax.get(1L),
                ItemList.Shape_Extruder_Cell.get(0L),
                ItemList.FR_WaxCapsule.get(1L),
                64,
                16);

        GT_Values.RA.addExtruderRecipe(
                ItemList.FR_RefractoryWax.get(1L),
                ItemList.Shape_Extruder_Cell.get(0L),
                ItemList.FR_RefractoryCapsule.get(1L),
                128,
                16);
    }
}
