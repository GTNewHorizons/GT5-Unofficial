package tectech.loader.recipe;

import static gregtech.api.recipe.RecipeMaps.circuitAssemblerRecipes;
import static gregtech.api.util.GTRecipeBuilder.HALF_INGOTS;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.core.material.MaterialMisc;
import tectech.thing.CustomItemList;

public class CircuitAssembler implements Runnable {

    @Override
    public void run() {
        // Optical Processor
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Optically_Perfected_CPU.get(1L),
                ItemList.Optically_Compatible_Memory.get(2L),
                ItemList.Circuit_Parts_CapacitorXSMD.get(16L),
                ItemList.Circuit_Parts_DiodeXSMD.get(16L),
                CustomItemList.DATApipe.get(4L),
                GTOreDictUnificator.get(OrePrefixes.bolt, Materials.EnrichedHolmium, 16))
            .itemOutputs(ItemList.Circuit_OpticalProcessor.get(1L))
            .fluidInputs(MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(2 * INGOTS))
            .requiresCleanRoom()
            .duration(20 * SECONDS)
            .eut(614400)
            .addTo(circuitAssemblerRecipes);

        // Parametrizer Memory Card
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.LV, 2),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.Gold, 4))
            .itemOutputs(CustomItemList.parametrizerMemory.get(1))
            .fluidInputs(Materials.Plastic.getMolten(1 * HALF_INGOTS))
            .requiresCleanRoom()
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(circuitAssemblerRecipes);
    }
}
