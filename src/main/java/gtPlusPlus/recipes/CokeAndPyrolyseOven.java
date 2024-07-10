package gtPlusPlus.recipes;

import static gregtech.api.util.GT_RecipeBuilder.MINUTES;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.cokeOvenRecipes;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
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
            AddGregtechRecipe.addCokeAndPyrolyseRecipes(
                ItemUtils.getSimpleStack(stack, 20),
                20,
                GT_ModHandler.getSteam(1000),
                GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Charcoal, 24L),
                FluidUtils.getFluidStack("fluid.coalgas", 1440),
                60,
                30);
        }

        // Coal to Coke
        AddGregtechRecipe.addCokeAndPyrolyseRecipes(
            GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Coal, 16L),
            22,
            GT_ModHandler.getSteam(1000),
            ItemUtils.getItemStackOfAmountFromOreDict("fuelCoke", 10),
            FluidUtils.getFluidStack("fluid.coalgas", 2880),
            30,
            120);

        // Coke & Coal
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Coal, 12L),
                ItemUtils.getItemStackOfAmountFromOreDict("fuelCoke", 6))
            .itemOutputs(ItemUtils.getItemStackOfAmountFromOreDict("fuelCoke", 14))
            .fluidInputs(GT_ModHandler.getSteam(2000))
            .fluidOutputs(FluidUtils.getFluidStack("fluid.coalgas", 5040))
            .eut(240)
            .duration(1 * MINUTES)
            .addTo(cokeOvenRecipes);
    }

    private static void convertPyroToCokeOven() {
        int aCount = 0;
        for (GT_Recipe g : RecipeMaps.pyrolyseRecipes.getAllRecipes()) {
            if (AddGregtechRecipe.importPyroRecipe(g)) {
                aCount++;
            }
        }
        Logger.INFO("Converted " + aCount + " Pyrolyse recipes into Industrial Coke Oven recipes.");
    }
}
