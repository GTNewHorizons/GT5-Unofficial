package tectech.loader.recipe;

import static gregtech.api.recipe.RecipeMaps.circuitAssemblerRecipes;
import static gregtech.api.util.GTRecipeBuilder.HALF_INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.Circuits;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials;
import gregtech.api.enums.materials2.Shapes;
import gregtech.api.material.MaterialUtils;
import tectech.thing.CustomItemList;

public class CircuitAssembler implements Runnable {

    @Override
    public void run() {
        // Parametrizer Memory Card
        GTValues.RA.stdBuilder()
            .itemInputs(Circuits.LV.get(2), MaterialLibAPI.getStack(Materials.Gold, Shapes.foil, 4))
            .itemOutputs(CustomItemList.parametrizerMemory.get(1))
            .fluidInputs(MaterialUtils.molten(Materials.Plastic, 1 * HALF_INGOTS))
            .requiresCleanRoom()
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(circuitAssemblerRecipes);
    }
}
