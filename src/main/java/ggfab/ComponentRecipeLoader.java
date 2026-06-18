package ggfab;

import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeConstants.AssemblyLine;
import static gregtech.api.util.GTRecipeConstants.RESEARCH_ITEM;
import static gregtech.api.util.GTRecipeConstants.SCANNING;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.recipe.Scanning;
import gtPlusPlus.core.material.MaterialsAlloy;

class ComponentRecipeLoader implements Runnable {

    @Override
    public void run() {
        // Advanced Assembly Line
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.Machine_Multi_Assemblyline.get(1L))
            .metadata(SCANNING, new Scanning(1 * MINUTES + 30 * SECONDS, TierEU.RECIPE_IV))
            .itemInputs(
                ItemList.Machine_Multi_Assemblyline.get(1L),
                new Object[] { OrePrefixes.circuit.get(Materials.LuV), 2 },
                new Object[] { OrePrefixes.circuit.get(Materials.IV), 4 },
                new Object[] { OrePrefixes.circuit.get(Materials.EV), 8 },
                ItemList.Automation_ChestBuffer_LuV.get(1L))
            .fluidInputs(MaterialsAlloy.INDALLOY_140.getFluidStack(9 * INGOTS), Materials.Lubricant.getFluid(2_000))
            .itemOutputs(GGItemList.AdvAssLine.get(1L))
            .eut(TierEU.RECIPE_LuV)
            .duration(3 * MINUTES)
            .addTo(AssemblyLine);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hatch_Input_Bus_IV.get(1L),
                ItemList.Emitter_IV.get(1L),
                ItemList.Sensor_IV.get(1L),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Enderium, 1L))
            .circuit(12)
            .itemOutputs(GGItemList.LinkedInputBus.get(1L))
            .fluidInputs(Materials.Polybenzimidazole.getMolten(1 * INGOTS))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

    }
}
