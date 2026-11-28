package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.electrochemicalCutterRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.TierEU;

public final class ElectrochemicalCutterRecipes implements Runnable {

    @Override
    public void run() {
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Precise_Silicon_Boule.get(1))
            .itemOutputs(ItemList.Crude_Si_Wafer.get(48))
            .duration(240 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(electrochemicalCutterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Precise_Microwaved_Silicon_Boule.get(1))
            .itemOutputs(ItemList.Microwaved_Si_Wafer.get(48))
            .duration(300 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(electrochemicalCutterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Precise_AlGaAs_Boule.get(1))
            .itemOutputs(ItemList.AlGaAs_Wafer.get(64))
            .duration(300 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(electrochemicalCutterRecipes);
    }
}
