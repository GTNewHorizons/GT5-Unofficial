package gregtech.loaders.postload.recipes;

import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sSolarFactoryRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.TierEU;

public class SolarFactoryRecipes implements Runnable {

    @Override
    public void run() {
        // just a test
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Cover_SolarPanel_LV.get(256))
            .itemOutputs(ItemList.Cover_SolarPanel_UIV.get(1))
            .noFluidInputs()
            .noFluidOutputs()
            .duration(10 * SECONDS) // 31,540,000
            .eut(TierEU.RECIPE_IV)
            .addTo(sSolarFactoryRecipes);

    }
}
