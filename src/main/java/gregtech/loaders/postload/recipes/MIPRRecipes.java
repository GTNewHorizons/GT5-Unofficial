package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.miprRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;

public final class MIPRRecipes implements Runnable {

    @Override
    public void run() {
        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Silane.getGas(1_000))
            .itemOutputs(ItemList.Microwaved_Silicon_Dust.get(1))
            .fluidOutputs(Materials.Hydrogen.getPlasma(400))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(miprRecipes);
    }
}
