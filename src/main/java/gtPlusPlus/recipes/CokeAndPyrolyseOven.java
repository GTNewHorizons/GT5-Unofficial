package gtPlusPlus.recipes;

import static gregtech.api.recipe.RecipeMaps.pyrolyseRecipes;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.cokeOvenRecipes;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.reflect.AddGregtechRecipe;

public class CokeAndPyrolyseOven {

    public static void onLoadComplete() {
        convertPyroToCokeOven();
    }

    public static void postInit() {
        Logger.INFO("Loading Recipes for Industrial Coking Oven.");
        // Wood to Charcoal
        // Try use all woods found
        ArrayList<ItemStack> aLogData = OreDictionary.getOres("logWood");
        for (ItemStack stack : aLogData) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.getIntegratedCircuit(20), ItemUtils.getSimpleStack(stack, 20))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.Charcoal, 24L))
                .fluidInputs(GTModHandler.getSteam(1000))
                .fluidOutputs(FluidUtils.getFluidStack("fluid.coalgas", 1440))
                .duration(72 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(pyrolyseRecipes);
        }

        // Coal to Coke
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(22),
                GTOreDictUnificator.get(OrePrefixes.gem, Materials.Coal, 16L))
            .itemOutputs(ItemUtils.getItemStackOfAmountFromOreDict("fuelCoke", 10))
            .fluidInputs(GTModHandler.getSteam(1000))
            .fluidOutputs(FluidUtils.getFluidStack("fluid.coalgas", 2880))
            .duration(36 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(pyrolyseRecipes);

        // Coke & Coal
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.gem, Materials.Coal, 12L),
                ItemUtils.getItemStackOfAmountFromOreDict("fuelCoke", 6))
            .itemOutputs(ItemUtils.getItemStackOfAmountFromOreDict("fuelCoke", 14))
            .fluidInputs(GTModHandler.getSteam(2000))
            .fluidOutputs(FluidUtils.getFluidStack("fluid.coalgas", 5040))
            .eut(240)
            .duration(1 * MINUTES)
            .addTo(cokeOvenRecipes);
    }

    private static void convertPyroToCokeOven() {
        int aCount = 0;
        for (GTRecipe g : RecipeMaps.pyrolyseRecipes.getAllRecipes()) {
            if (AddGregtechRecipe.importPyroRecipe(g.copy())) {
                aCount++;
            }
        }
        Logger.INFO("Converted " + aCount + " Pyrolyse recipes into Industrial Coke Oven recipes.");
    }
}
