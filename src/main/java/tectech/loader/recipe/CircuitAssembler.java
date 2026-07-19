package tectech.loader.recipe;

import static gregtech.api.recipe.RecipeMaps.circuitAssemblerRecipes;
import static gregtech.api.util.GTRecipeBuilder.HALF_INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.Circuits;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;
import tectech.thing.CustomItemList;

public class CircuitAssembler implements Runnable {

    @Override
    public void run() {
        // Parametrizer Memory Card
        GTValues.RA.stdBuilder()
            .itemInputs(Circuits.LV.get(2), MaterialLibAPI.getStack(Materials2Materials.Gold, Materials2Shapes.foil, 4))
            .itemOutputs(CustomItemList.parametrizerMemory.get(1))
            .fluidInputs(Materials.Polyethylene.getMolten(1 * HALF_INGOTS))
            .requiresCleanRoom()
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(circuitAssemblerRecipes);
    }
}
