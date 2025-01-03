package gtPlusPlus.core.recipe;

import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.*;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.material.MaterialMisc;
import gtPlusPlus.core.material.MaterialsElements;
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
            .itemInputs(MaterialMisc.SELENIUM_DIOXIDE.getDust(1))
            .fluidInputs(FluidUtils.getSteam(500))
            .fluidOutputs(MaterialMisc.SELENIUM_DIOXIDE.getFluidStack(1000))
            .duration(24 * SECONDS)
            .eut(1024)
            .addTo(cokeOvenRecipes);

        // Produce Selenious Acid
        GTValues.RA.stdBuilder()
            .fluidInputs(FluidUtils.getHotWater(4000), MaterialMisc.SELENIUM_DIOXIDE.getFluidStack(1000))
            .fluidOutputs(MaterialMisc.SELENIOUS_ACID.getFluidStack(1000))
            .duration(24 * SECONDS)
            .noOptimize()
            .eut(TierEU.RECIPE_EV)
            .addTo(cokeOvenRecipes);

        // Make Selenium
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(14), MaterialsElements.getInstance().CARBON.getDust(16))
            .fluidInputs(MaterialMisc.SELENIOUS_ACID.getFluidStack(750), Materials.SulfuricAcid.getFluid(8000))
            .fluidOutputs(
                MaterialsElements.getInstance().SELENIUM.getFluidStack(144 * 2 + 36),
                Materials.DilutedSulfuricAcid.getFluid(12000))
            .eut(TierEU.RECIPE_IV)
            .noOptimize()
            .duration(5 * MINUTES)
            .addTo(alloyBlastSmelterRecipes);
    }

    public static void processCopperRecipes() {

        // Copper
        GTValues.RA.stdBuilder()
            .itemInputs(ItemUtils.getOrePrefixStack(OrePrefixes.crushedPurified, Materials.Copper, 1))
            .itemOutputs(
                ItemUtils.getOrePrefixStack(OrePrefixes.crushedCentrifuged, Materials.Copper, 1),
                MaterialMisc.SELENIUM_DIOXIDE.getDust(1))
            .outputChances(100_00, 3_00)
            .fluidInputs(FluidUtils.getHotWater(1000))
            .fluidOutputs(MaterialMisc.SELENIUM_DIOXIDE.getFluidStack(20))
            .eut(1024)
            .duration(40 * SECONDS)
            .addTo(chemicalDehydratorRecipes);

        // Tetra
        GTValues.RA.stdBuilder()
            .itemInputs(ItemUtils.getOrePrefixStack(OrePrefixes.crushedPurified, Materials.Tetrahedrite, 1))
            .itemOutputs(
                ItemUtils.getOrePrefixStack(OrePrefixes.crushedCentrifuged, Materials.Tetrahedrite, 1),
                MaterialMisc.SELENIUM_DIOXIDE.getDust(1))
            .outputChances(100_00, 2_50)
            .fluidInputs(FluidUtils.getHotWater(1000))
            .fluidOutputs(MaterialMisc.SELENIUM_DIOXIDE.getFluidStack(10))
            .eut(1024)
            .duration(40 * SECONDS)
            .addTo(chemicalDehydratorRecipes);

        // Chalco
        GTValues.RA.stdBuilder()
            .itemInputs(ItemUtils.getOrePrefixStack(OrePrefixes.crushedPurified, Materials.Chalcopyrite, 1))
            .itemOutputs(
                ItemUtils.getOrePrefixStack(OrePrefixes.crushedCentrifuged, Materials.Chalcopyrite, 1),
                MaterialMisc.SELENIUM_DIOXIDE.getDust(1))
            .outputChances(100_00, 2_50)
            .fluidInputs(FluidUtils.getHotWater(1000))
            .fluidOutputs(MaterialMisc.SELENIUM_DIOXIDE.getFluidStack(10))
            .eut(1024)
            .duration(40 * SECONDS)
            .addTo(chemicalDehydratorRecipes);

        // Malachite
        GTValues.RA.stdBuilder()
            .itemInputs(ItemUtils.getOrePrefixStack(OrePrefixes.crushedPurified, Materials.Malachite, 1))
            .itemOutputs(
                ItemUtils.getOrePrefixStack(OrePrefixes.crushedCentrifuged, Materials.Malachite, 1),
                MaterialMisc.SELENIUM_DIOXIDE.getDust(1))
            .outputChances(100_00, 2_50)
            .fluidInputs(FluidUtils.getHotWater(1000))
            .fluidOutputs(MaterialMisc.SELENIUM_DIOXIDE.getFluidStack(10))
            .eut(1024)
            .duration(40 * SECONDS)
            .addTo(chemicalDehydratorRecipes);
    }
}
