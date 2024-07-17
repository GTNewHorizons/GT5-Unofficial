package goodgenerator.loader;

import static goodgenerator.api.recipe.GoodGeneratorRecipeMaps.neutronActivatorRecipes;
import static goodgenerator.items.MyMaterial.*;
import static goodgenerator.util.MyRecipeAdder.computeRangeNKE;
import static gregtech.api.util.GT_RecipeBuilder.MINUTES;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeConstants.NKE_RANGE;

import gregtech.api.enums.GT_Values;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import goodgenerator.util.MyRecipeAdder;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;

public class NeutronActivatorLoader {

    public static void NARecipeLoad() {
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                thoriumBasedLiquidFuelExcited.getFluidOrGas(200))
            .fluidOutputs(
                thoriumBasedLiquidFuelDepleted.getFluidOrGas(200))
            .duration(8 * MINUTES + 20 * SECONDS)
            .eut(0)
            .metadata(NKE_RANGE,computeRangeNKE(700,500))
            .noOptimize()
            .addTo(neutronActivatorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.copyAmount(0, GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Tungsten, 1)))
            .fluidInputs(
                uraniumBasedLiquidFuel.getFluidOrGas(100))
            .fluidOutputs(
                uraniumBasedLiquidFuelExcited.getFluidOrGas(100))
            .duration(4 * SECONDS)
            .eut(0)
            .metadata(NKE_RANGE,computeRangeNKE(550,450))
            .noOptimize()
            .addTo(neutronActivatorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.copyAmount(0, GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Tritanium, 1)))
            .fluidInputs(
                plutoniumBasedLiquidFuel.getFluidOrGas(100))
            .fluidOutputs(
                plutoniumBasedLiquidFuelExcited.getFluidOrGas(100))
            .duration(4 * SECONDS)
            .eut(0)
            .metadata(NKE_RANGE,computeRangeNKE(600,500))
            .noOptimize()
            .addTo(neutronActivatorRecipes);

    }
}
