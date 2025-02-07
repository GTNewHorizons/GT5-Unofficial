package gtPlusPlus.recipes;

import static gregtech.api.recipe.RecipeMaps.pyrolyseRecipes;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.cokeOvenRecipes;

import java.util.ArrayList;

import gtPlusPlus.core.item.ModItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
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

        // Cactus and Sugar charcoal/coke, copied over from the ICO and adjusted for pyrolyse (*20 input and output, duration*1.25 per item.)
        ItemStack[] aInputs1 = new ItemStack[] { ItemUtils.getSimpleStack(Blocks.cactus,20),
            ItemUtils.getSimpleStack(Items.reeds,20) };
        ItemStack[] aInputs2 = new ItemStack[] { ItemUtils.getSimpleStack(ModItems.itemCactusCharcoal,20),
            ItemUtils.getSimpleStack(ModItems.itemSugarCharcoal,20) };
        ItemStack[] aOutputs = new ItemStack[] { ItemUtils.getSimpleStack(ModItems.itemCactusCoke,20),
            ItemUtils.getSimpleStack(ModItems.itemSugarCoke,20) };
        for (int i = 0; i < aOutputs.length; i++) {
            // Plant to Charcoal
            GTValues.RA.stdBuilder()
                .itemInputs(aInputs1[i], GTUtility.getIntegratedCircuit(3))
                .itemOutputs(aInputs2[i])
                .fluidOutputs(FluidUtils.getFluidStack("creosote", 2000))
                .eut(16)
                .duration(25 * SECONDS)
                .addTo(pyrolyseRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(aInputs1[i], GTUtility.getIntegratedCircuit(4))
                .itemOutputs(aInputs2[i])
                .fluidInputs(FluidUtils.getFluidStack("nitrogen", 2000))
                .fluidOutputs(FluidUtils.getFluidStack("charcoal_byproducts", 4000))
                .eut(16)
                .duration(250 * TICKS)
                .addTo(pyrolyseRecipes);
            // Charcoal to Coke
            GTValues.RA.stdBuilder()
                .itemInputs(aInputs2[i], GTUtility.getIntegratedCircuit(3))
                .itemOutputs(aOutputs[i])
                .fluidOutputs(FluidUtils.getFluidStack("creosote", 4000))
                .eut(16)
                .duration(50 * SECONDS)
                .addTo(pyrolyseRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(aInputs2[i], GTUtility.getIntegratedCircuit(4))
                .itemOutputs(aOutputs[i])
                .fluidInputs(FluidUtils.getFluidStack("nitrogen", 1000))
                .fluidOutputs(FluidUtils.getFluidStack("charcoal_byproducts", 2000))
                .eut(16)
                .duration(25 * SECONDS)
                .addTo(pyrolyseRecipes);
            // Coke to Wood tar/Wood gas
            GTValues.RA.stdBuilder()
                .itemInputs(aOutputs[i], GTUtility.getIntegratedCircuit(5))
                .itemOutputs(Materials.Ash.getDust(5))
                .fluidInputs(FluidUtils.getFluidStack("steam", 2000))
                .fluidOutputs(Materials.WoodTar.getFluid(4000))
                .eut(240)
                .duration(75 * SECONDS)
                .addTo(pyrolyseRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(aOutputs[i], GTUtility.getIntegratedCircuit(6))
                .itemOutputs(Materials.Ash.getDust(5))
                .fluidInputs(FluidUtils.getFluidStack("steam", 2000))
                .fluidOutputs(Materials.WoodGas.getGas(6000))
                .eut(240)
                .duration(75 * SECONDS)
                .addTo(pyrolyseRecipes);
        }
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
