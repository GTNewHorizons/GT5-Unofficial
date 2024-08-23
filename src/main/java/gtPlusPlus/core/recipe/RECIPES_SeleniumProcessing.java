package gtPlusPlus.core.recipe;

import static gregtech.api.recipe.RecipeMaps.pyrolyseRecipes;
import static gregtech.api.util.GT_RecipeBuilder.MINUTES;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.alloyBlastSmelterRecipes;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.chemicalDehydratorRecipes;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.material.ELEMENT;
import gtPlusPlus.core.material.MISC_MATERIALS;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.MaterialUtils;

public class RECIPES_SeleniumProcessing {

    public static void init() {

        // We need this
        MaterialUtils.generateSpecialDustAndAssignToAMaterial(MISC_MATERIALS.SELENIUM_DIOXIDE, false);

        // Makes Selenium Dioxide
        processCopperRecipes();

        // Liquify the Dried Dioxide
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(13), MISC_MATERIALS.SELENIUM_DIOXIDE.getDust(1))
            .fluidInputs(FluidUtils.getSteam(500))
            .fluidOutputs(MISC_MATERIALS.SELENIUM_DIOXIDE.getFluidStack(1000))
            .duration(24 * SECONDS)
            .eut(1024)
            .addTo(pyrolyseRecipes);

        // Produce Selenious Acid
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(14), MISC_MATERIALS.SELENIUM_DIOXIDE.getCell(1))
            .itemOutputs(CI.emptyCells(1))
            .fluidInputs(FluidUtils.getHotWater(4000))
            .fluidOutputs(MISC_MATERIALS.SELENIOUS_ACID.getFluidStack(1000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(pyrolyseRecipes);

        // Make Selenium
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(14), ELEMENT.getInstance().CARBON.getDust(16))
            .itemOutputs(ELEMENT.getInstance().SELENIUM.getIngot(1), ELEMENT.getInstance().SELENIUM.getIngot(1))
            .outputChances(100_00, 20_00)
            .fluidInputs(MISC_MATERIALS.SELENIOUS_ACID.getFluidStack(750), Materials.SulfuricAcid.getFluid(8000))
            .fluidOutputs(ELEMENT.getInstance().SELENIUM.getFluidStack(144 * 1))
            .eut(TierEU.RECIPE_IV)
            .duration(5 * MINUTES)
            .addTo(alloyBlastSmelterRecipes);
    }

    public static void processCopperRecipes() {

        // Copper
        GT_Values.RA.stdBuilder()
            .itemInputs(
                CI.getNumberedAdvancedCircuit(23),
                ItemUtils.getOrePrefixStack(OrePrefixes.crushedPurified, Materials.Copper, 1))
            .itemOutputs(
                ItemUtils.getOrePrefixStack(OrePrefixes.crushedCentrifuged, Materials.Copper, 1),
                MISC_MATERIALS.SELENIUM_DIOXIDE.getDust(1),
                MISC_MATERIALS.SELENIUM_DIOXIDE.getDust(1),
                MISC_MATERIALS.SELENIUM_DIOXIDE.getSmallDust(1),
                MISC_MATERIALS.SELENIUM_DIOXIDE.getSmallDust(1),
                MISC_MATERIALS.SELENIUM_DIOXIDE.getSmallDust(1),
                MISC_MATERIALS.SELENIUM_DIOXIDE.getTinyDust(1),
                MISC_MATERIALS.SELENIUM_DIOXIDE.getTinyDust(1),
                MISC_MATERIALS.SELENIUM_DIOXIDE.getTinyDust(1))
            .outputChances(100_00, 1_00, 1_00, 5_00, 5_00, 5_00, 10_00, 10_00, 10_00)
            .fluidInputs(FluidUtils.getHotWater(1000))
            .fluidOutputs(MISC_MATERIALS.SELENIUM_DIOXIDE.getFluidStack(20))
            .eut(1024)
            .duration(40 * SECONDS)
            .addTo(chemicalDehydratorRecipes);

        // Tetra
        GT_Values.RA.stdBuilder()
            .itemInputs(
                CI.getNumberedAdvancedCircuit(23),
                ItemUtils.getOrePrefixStack(OrePrefixes.crushedPurified, Materials.Tetrahedrite, 1))
            .itemOutputs(
                ItemUtils.getOrePrefixStack(OrePrefixes.crushedCentrifuged, Materials.Tetrahedrite, 1),
                MISC_MATERIALS.SELENIUM_DIOXIDE.getDust(1),
                MISC_MATERIALS.SELENIUM_DIOXIDE.getDust(1),
                MISC_MATERIALS.SELENIUM_DIOXIDE.getSmallDust(1),
                MISC_MATERIALS.SELENIUM_DIOXIDE.getSmallDust(1),
                MISC_MATERIALS.SELENIUM_DIOXIDE.getSmallDust(1),
                MISC_MATERIALS.SELENIUM_DIOXIDE.getTinyDust(1),
                MISC_MATERIALS.SELENIUM_DIOXIDE.getTinyDust(1),
                MISC_MATERIALS.SELENIUM_DIOXIDE.getTinyDust(1))
            .outputChances(100_00, 1_00, 1_00, 3_00, 3_00, 3_00, 8_00, 8_00, 8_00)
            .fluidInputs(FluidUtils.getHotWater(1000))
            .fluidOutputs(MISC_MATERIALS.SELENIUM_DIOXIDE.getFluidStack(10))
            .eut(1024)
            .duration(40 * SECONDS)
            .addTo(chemicalDehydratorRecipes);

        // Chalco
        GT_Values.RA.stdBuilder()
            .itemInputs(
                CI.getNumberedAdvancedCircuit(23),
                ItemUtils.getOrePrefixStack(OrePrefixes.crushedPurified, Materials.Chalcopyrite, 1))
            .itemOutputs(
                ItemUtils.getOrePrefixStack(OrePrefixes.crushedCentrifuged, Materials.Chalcopyrite, 1),
                MISC_MATERIALS.SELENIUM_DIOXIDE.getDust(1),
                MISC_MATERIALS.SELENIUM_DIOXIDE.getDust(1),
                MISC_MATERIALS.SELENIUM_DIOXIDE.getSmallDust(1),
                MISC_MATERIALS.SELENIUM_DIOXIDE.getSmallDust(1),
                MISC_MATERIALS.SELENIUM_DIOXIDE.getSmallDust(1),
                MISC_MATERIALS.SELENIUM_DIOXIDE.getTinyDust(1),
                MISC_MATERIALS.SELENIUM_DIOXIDE.getTinyDust(1),
                MISC_MATERIALS.SELENIUM_DIOXIDE.getTinyDust(1))
            .outputChances(100_00, 1_00, 1_00, 3_00, 3_00, 3_00, 8_00, 8_00, 8_00)
            .fluidInputs(FluidUtils.getHotWater(1000))
            .fluidOutputs(MISC_MATERIALS.SELENIUM_DIOXIDE.getFluidStack(10))
            .eut(1024)
            .duration(40 * SECONDS)
            .addTo(chemicalDehydratorRecipes);

        // Malachite
        GT_Values.RA.stdBuilder()
            .itemInputs(
                CI.getNumberedAdvancedCircuit(23),
                ItemUtils.getOrePrefixStack(OrePrefixes.crushedPurified, Materials.Malachite, 1))
            .itemOutputs(
                ItemUtils.getOrePrefixStack(OrePrefixes.crushedCentrifuged, Materials.Malachite, 1),
                MISC_MATERIALS.SELENIUM_DIOXIDE.getDust(1),
                MISC_MATERIALS.SELENIUM_DIOXIDE.getDust(1),
                MISC_MATERIALS.SELENIUM_DIOXIDE.getSmallDust(1),
                MISC_MATERIALS.SELENIUM_DIOXIDE.getSmallDust(1),
                MISC_MATERIALS.SELENIUM_DIOXIDE.getSmallDust(1),
                MISC_MATERIALS.SELENIUM_DIOXIDE.getTinyDust(1),
                MISC_MATERIALS.SELENIUM_DIOXIDE.getTinyDust(1),
                MISC_MATERIALS.SELENIUM_DIOXIDE.getTinyDust(1))
            .outputChances(100_00, 1_00, 1_00, 3_00, 3_00, 3_00, 8_00, 8_00, 8_00)
            .fluidInputs(FluidUtils.getHotWater(1000))
            .fluidOutputs(MISC_MATERIALS.SELENIUM_DIOXIDE.getFluidStack(10))
            .eut(1024)
            .duration(40 * SECONDS)
            .addTo(chemicalDehydratorRecipes);
    }
}
