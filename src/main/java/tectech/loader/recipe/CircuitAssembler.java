package tectech.loader.recipe;

import static gregtech.api.recipe.RecipeMaps.circuitAssemblerRecipes;
import static gregtech.api.util.GTRecipeBuilder.HALF_INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTOreDictUnificator;
import tectech.thing.CustomItemList;

public class CircuitAssembler implements Runnable {

    @Override
    public void run() {
        // Parametrizer Memory Card
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.LV, 2),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.Gold, 4))
            .itemOutputs(CustomItemList.parametrizerMemory.get(1))
            .fluidInputs(Materials.Polyethylene.getMolten(1 * HALF_INGOTS))
            .requiresCleanRoom()
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(circuitAssemblerRecipes);
    }
}
