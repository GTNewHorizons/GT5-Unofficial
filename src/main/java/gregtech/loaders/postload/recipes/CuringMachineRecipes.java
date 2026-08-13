package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.curingMachineRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;

public class CuringMachineRecipes implements Runnable {

    @Override
    public void run() {
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.WovenKevlar.get(1))
            .fluidInputs(Materials.PolyurethaneResin.getFluid(1000))
            .itemOutputs(Materials.Kevlar.getPlates(1))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(curingMachineRecipes);
    }
}
