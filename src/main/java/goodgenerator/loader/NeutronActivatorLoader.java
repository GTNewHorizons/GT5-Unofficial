package goodgenerator.loader;

import static goodgenerator.api.recipe.GoodGeneratorRecipeMaps.neutronActivatorRecipes;
import static goodgenerator.items.MyMaterial.plutoniumBasedLiquidFuel;
import static goodgenerator.items.MyMaterial.plutoniumBasedLiquidFuelExcited;
import static goodgenerator.items.MyMaterial.thoriumBasedLiquidFuelDepleted;
import static goodgenerator.items.MyMaterial.thoriumBasedLiquidFuelExcited;
import static goodgenerator.items.MyMaterial.uraniumBasedLiquidFuel;
import static goodgenerator.items.MyMaterial.uraniumBasedLiquidFuelExcited;
import static goodgenerator.util.MyRecipeAdder.computeRangeNKE;
import static gregtech.api.util.RecipeBuilder.MINUTES;
import static gregtech.api.util.RecipeBuilder.SECONDS;
import static gregtech.api.util.RecipeConstants.NKE_RANGE;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.OreDictUnificator;

public class NeutronActivatorLoader {

    public static void NARecipeLoad() {
        GT_Values.RA.stdBuilder()
            .fluidInputs(thoriumBasedLiquidFuelExcited.getFluidOrGas(200))
            .fluidOutputs(thoriumBasedLiquidFuelDepleted.getFluidOrGas(200))
            .duration(8 * MINUTES + 20 * SECONDS)
            .eut(0)
            .metadata(NKE_RANGE, computeRangeNKE(700, 500))
            .noOptimize()
            .addTo(neutronActivatorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmount(0, OreDictUnificator.get(OrePrefixes.plate, Materials.Tungsten, 1)))
            .fluidInputs(uraniumBasedLiquidFuel.getFluidOrGas(100))
            .fluidOutputs(uraniumBasedLiquidFuelExcited.getFluidOrGas(100))
            .duration(4 * SECONDS)
            .eut(0)
            .metadata(NKE_RANGE, computeRangeNKE(550, 450))
            .noOptimize()
            .addTo(neutronActivatorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmount(0, OreDictUnificator.get(OrePrefixes.plate, Materials.Tritanium, 1)))
            .fluidInputs(plutoniumBasedLiquidFuel.getFluidOrGas(100))
            .fluidOutputs(plutoniumBasedLiquidFuelExcited.getFluidOrGas(100))
            .duration(4 * SECONDS)
            .eut(0)
            .metadata(NKE_RANGE, computeRangeNKE(600, 500))
            .noOptimize()
            .addTo(neutronActivatorRecipes);

    }
}
