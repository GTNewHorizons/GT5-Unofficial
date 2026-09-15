package goodgenerator.loader;

import static goodgenerator.api.recipe.GoodGeneratorRecipeMaps.neutronActivatorRecipes;
import static goodgenerator.items.GGMaterial.plutoniumBasedLiquidFuel;
import static goodgenerator.items.GGMaterial.plutoniumBasedLiquidFuelExcited;
import static goodgenerator.items.GGMaterial.thoriumBasedLiquidFuelDepleted;
import static goodgenerator.items.GGMaterial.thoriumBasedLiquidFuelExcited;
import static goodgenerator.items.GGMaterial.uraniumBasedLiquidFuel;
import static goodgenerator.items.GGMaterial.uraniumBasedLiquidFuelExcited;
import static goodgenerator.util.MyRecipeAdder.computeRangeNKE;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeConstants.NKE_RANGE;

import goodgenerator.items.GGMaterial;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

public class NeutronActivatorLoader {

    public static void NARecipeLoad() {
        GTValues.RA.stdBuilder()
            .fluidInputs(thoriumBasedLiquidFuelExcited.getFluidOrGas(200))
            .fluidOutputs(thoriumBasedLiquidFuelDepleted.getFluidOrGas(200))
            .duration(8 * MINUTES + 20 * SECONDS)
            .eut(0)
            .metadata(NKE_RANGE, computeRangeNKE(700, 500))
            .addTo(neutronActivatorRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(0, GTOreDictUnificator.get(OrePrefixes.plate, Materials.Tungsten, 1)))
            .fluidInputs(uraniumBasedLiquidFuel.getFluidOrGas(100))
            .fluidOutputs(uraniumBasedLiquidFuelExcited.getFluidOrGas(100))
            .duration(4 * SECONDS)
            .eut(0)
            .metadata(NKE_RANGE, computeRangeNKE(550, 450))
            .addTo(neutronActivatorRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(0, GTOreDictUnificator.get(OrePrefixes.plate, Materials.Tritanium, 1)))
            .fluidInputs(plutoniumBasedLiquidFuel.getFluidOrGas(100))
            .fluidOutputs(plutoniumBasedLiquidFuelExcited.getFluidOrGas(100))
            .duration(4 * SECONDS)
            .eut(0)
            .metadata(NKE_RANGE, computeRangeNKE(600, 500))
            .addTo(neutronActivatorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Tesseract.get(1))
            .fluidInputs(GGMaterial.naquadahBasedFuelMkV.getFluidOrGas(64))
            .fluidOutputs(GGMaterial.naquadahBasedFuelMkVDepleted.getFluidOrGas(64))
            .itemOutputs(ItemList.EnergisedTesseract.get(1))
            .duration(16400 * SECONDS)
            .eut(0)
            .metadata(NKE_RANGE, computeRangeNKE(1100, 1050))
            .addTo(neutronActivatorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Tesseract.get(1))
            .fluidInputs(GGMaterial.naquadahBasedFuelMkVI.getFluidOrGas(64))
            .fluidOutputs(GGMaterial.naquadahBasedFuelMkVIDepleted.getFluidOrGas(64))
            .itemOutputs(ItemList.EnergisedTesseract.get(1))
            .duration(24600 * SECONDS)
            .eut(0)
            .metadata(NKE_RANGE, computeRangeNKE(1100, 1075))
            .addTo(neutronActivatorRecipes);

    }
}
