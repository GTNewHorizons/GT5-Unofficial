package gtPlusPlus.core.recipe;

import static gregtech.api.recipe.RecipeMaps.pyrolyseRecipes;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.alloyBlastSmelterRecipes;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.chemicalDehydratorRecipes;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.material.MaterialMisc;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.MaterialUtils;

public class RecipesSeleniumProcessing {

    public static void init() {

        // We need this
        MaterialUtils.generateSpecialDustAndAssignToAMaterial(MaterialMisc.SELENIUM_DIOXIDE, false);

        // Makes Selenium Dioxide
        processCopperRecipes();

        // Liquify the Dried Dioxide
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(13), MaterialMisc.SELENIUM_DIOXIDE.getDust(1))
            .fluidInputs(FluidUtils.getSteam(500))
            .fluidOutputs(MaterialMisc.SELENIUM_DIOXIDE.getFluidStack(1000))
            .duration(24 * SECONDS)
            .eut(1024)
            .addTo(pyrolyseRecipes);

        // Produce Selenious Acid
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(14), MaterialMisc.SELENIUM_DIOXIDE.getCell(1))
            .itemOutputs(CI.emptyCells(1))
            .fluidInputs(FluidUtils.getHotWater(4000))
            .fluidOutputs(MaterialMisc.SELENIOUS_ACID.getFluidStack(1000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(pyrolyseRecipes);

        // Make Selenium
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(14), MaterialsElements.getInstance().CARBON.getDust(16))
            .itemOutputs(
                MaterialsElements.getInstance().SELENIUM.getIngot(1),
                MaterialsElements.getInstance().SELENIUM.getIngot(1))
            .outputChances(100_00, 20_00)
            .fluidInputs(MaterialMisc.SELENIOUS_ACID.getFluidStack(750), Materials.SulfuricAcid.getFluid(8000))
            .fluidOutputs(MaterialsElements.getInstance().SELENIUM.getFluidStack(144 * 1))
            .eut(TierEU.RECIPE_IV)
            .duration(5 * MINUTES)
            .addTo(alloyBlastSmelterRecipes);
    }

    public static void processCopperRecipes() {

        // Copper
        GTValues.RA.stdBuilder()
            .itemInputs(
                CI.getNumberedAdvancedCircuit(23),
                ItemUtils.getOrePrefixStack(OrePrefixes.crushedPurified, Materials.Copper, 1))
            .itemOutputs(
                ItemUtils.getOrePrefixStack(OrePrefixes.crushedCentrifuged, Materials.Copper, 1),
                MaterialMisc.SELENIUM_DIOXIDE.getDust(1),
                MaterialMisc.SELENIUM_DIOXIDE.getDust(1),
                MaterialMisc.SELENIUM_DIOXIDE.getSmallDust(1),
                MaterialMisc.SELENIUM_DIOXIDE.getSmallDust(1),
                MaterialMisc.SELENIUM_DIOXIDE.getSmallDust(1),
                MaterialMisc.SELENIUM_DIOXIDE.getTinyDust(1),
                MaterialMisc.SELENIUM_DIOXIDE.getTinyDust(1),
                MaterialMisc.SELENIUM_DIOXIDE.getTinyDust(1))
            .outputChances(100_00, 1_00, 1_00, 5_00, 5_00, 5_00, 10_00, 10_00, 10_00)
            .fluidInputs(FluidUtils.getHotWater(1000))
            .fluidOutputs(MaterialMisc.SELENIUM_DIOXIDE.getFluidStack(20))
            .eut(1024)
            .duration(40 * SECONDS)
            .addTo(chemicalDehydratorRecipes);

        // Tetra
        GTValues.RA.stdBuilder()
            .itemInputs(
                CI.getNumberedAdvancedCircuit(23),
                ItemUtils.getOrePrefixStack(OrePrefixes.crushedPurified, Materials.Tetrahedrite, 1))
            .itemOutputs(
                ItemUtils.getOrePrefixStack(OrePrefixes.crushedCentrifuged, Materials.Tetrahedrite, 1),
                MaterialMisc.SELENIUM_DIOXIDE.getDust(1),
                MaterialMisc.SELENIUM_DIOXIDE.getDust(1),
                MaterialMisc.SELENIUM_DIOXIDE.getSmallDust(1),
                MaterialMisc.SELENIUM_DIOXIDE.getSmallDust(1),
                MaterialMisc.SELENIUM_DIOXIDE.getSmallDust(1),
                MaterialMisc.SELENIUM_DIOXIDE.getTinyDust(1),
                MaterialMisc.SELENIUM_DIOXIDE.getTinyDust(1),
                MaterialMisc.SELENIUM_DIOXIDE.getTinyDust(1))
            .outputChances(100_00, 1_00, 1_00, 3_00, 3_00, 3_00, 8_00, 8_00, 8_00)
            .fluidInputs(FluidUtils.getHotWater(1000))
            .fluidOutputs(MaterialMisc.SELENIUM_DIOXIDE.getFluidStack(10))
            .eut(1024)
            .duration(40 * SECONDS)
            .addTo(chemicalDehydratorRecipes);

        // Chalco
        GTValues.RA.stdBuilder()
            .itemInputs(
                CI.getNumberedAdvancedCircuit(23),
                ItemUtils.getOrePrefixStack(OrePrefixes.crushedPurified, Materials.Chalcopyrite, 1))
            .itemOutputs(
                ItemUtils.getOrePrefixStack(OrePrefixes.crushedCentrifuged, Materials.Chalcopyrite, 1),
                MaterialMisc.SELENIUM_DIOXIDE.getDust(1),
                MaterialMisc.SELENIUM_DIOXIDE.getDust(1),
                MaterialMisc.SELENIUM_DIOXIDE.getSmallDust(1),
                MaterialMisc.SELENIUM_DIOXIDE.getSmallDust(1),
                MaterialMisc.SELENIUM_DIOXIDE.getSmallDust(1),
                MaterialMisc.SELENIUM_DIOXIDE.getTinyDust(1),
                MaterialMisc.SELENIUM_DIOXIDE.getTinyDust(1),
                MaterialMisc.SELENIUM_DIOXIDE.getTinyDust(1))
            .outputChances(100_00, 1_00, 1_00, 3_00, 3_00, 3_00, 8_00, 8_00, 8_00)
            .fluidInputs(FluidUtils.getHotWater(1000))
            .fluidOutputs(MaterialMisc.SELENIUM_DIOXIDE.getFluidStack(10))
            .eut(1024)
            .duration(40 * SECONDS)
            .addTo(chemicalDehydratorRecipes);

        // Malachite
        GTValues.RA.stdBuilder()
            .itemInputs(
                CI.getNumberedAdvancedCircuit(23),
                ItemUtils.getOrePrefixStack(OrePrefixes.crushedPurified, Materials.Malachite, 1))
            .itemOutputs(
                ItemUtils.getOrePrefixStack(OrePrefixes.crushedCentrifuged, Materials.Malachite, 1),
                MaterialMisc.SELENIUM_DIOXIDE.getDust(1),
                MaterialMisc.SELENIUM_DIOXIDE.getDust(1),
                MaterialMisc.SELENIUM_DIOXIDE.getSmallDust(1),
                MaterialMisc.SELENIUM_DIOXIDE.getSmallDust(1),
                MaterialMisc.SELENIUM_DIOXIDE.getSmallDust(1),
                MaterialMisc.SELENIUM_DIOXIDE.getTinyDust(1),
                MaterialMisc.SELENIUM_DIOXIDE.getTinyDust(1),
                MaterialMisc.SELENIUM_DIOXIDE.getTinyDust(1))
            .outputChances(100_00, 1_00, 1_00, 3_00, 3_00, 3_00, 8_00, 8_00, 8_00)
            .fluidInputs(FluidUtils.getHotWater(1000))
            .fluidOutputs(MaterialMisc.SELENIUM_DIOXIDE.getFluidStack(10))
            .eut(1024)
            .duration(40 * SECONDS)
            .addTo(chemicalDehydratorRecipes);
    }
}
