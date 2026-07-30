package gtPlusPlus.core.recipe;

import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.QUARTER_INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.*;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTModHandler;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class RecipesSeleniumProcessing {

    public static void init() {

        // Makes Selenium Dioxide
        processCopperRecipes();

        // Liquify the Dried Dioxide
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.SeleniumDioxide, Materials2Shapes.dust, 1))
            .fluidInputs(MaterialUtils.gas(Materials.Steam, 500))
            .fluidOutputs(MaterialUtils.legacyGtppFluid(Materials.SeleniumDioxide, 1_000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_EV / 2)
            .addTo(cokeOvenRecipes);

        // Produce Selenious Acid
        GTValues.RA.stdBuilder()
            .fluidInputs(
                GTModHandler.getHotWater(4_000),
                MaterialUtils.legacyGtppFluid(Materials.SeleniumDioxide, 1_000))
            .fluidOutputs(MaterialUtils.legacyGtppFluid(Materials.SeleniousAcid, 1_000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(cokeOvenRecipes);

        // Make Selenium
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Carbon, Materials2Shapes.dust, 16))
            .circuit(14)
            .fluidInputs(
                MaterialUtils.legacyGtppFluid(Materials.SeleniousAcid, 750),
                MaterialLibAPI
                    .getFluidStack(Materials.SulfuricAcid, Materials2FluidShapes.fluidLiquid, (int) (8_000)))
            .fluidOutputs(
                MaterialUtils.legacyGtppFluid(Materials.Selenium, 2 * INGOTS + 1 * QUARTER_INGOTS),
                MaterialLibAPI.getFluidStack(
                    Materials.DilutedSulfuricAcid,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (12_000)))
            .eut(TierEU.RECIPE_IV)
            .duration(5 * MINUTES)
            .addTo(alloyBlastSmelterRecipes);
    }

    public static void processCopperRecipes() {

        // Copper
        GTValues.RA.stdBuilder()
            .itemInputs(ItemUtils.getOrePrefixStack(OrePrefixes.crushedPurified, Materials.Copper, 1))
            .itemOutputs(
                ItemUtils.getOrePrefixStack(OrePrefixes.crushedCentrifuged, Materials.Copper, 1),
                MaterialLibAPI.getStack(Materials.SeleniumDioxide, Materials2Shapes.dust, 1))
            .outputChances(100_00, 3_00)
            .fluidInputs(GTModHandler.getHotWater(1_000))
            .fluidOutputs(MaterialUtils.legacyGtppFluid(Materials.SeleniumDioxide, 20))
            .eut(TierEU.RECIPE_EV / 2)
            .duration(40 * SECONDS)
            .addTo(chemicalDehydratorRecipes);

        // Tetra
        GTValues.RA.stdBuilder()
            .itemInputs(ItemUtils.getOrePrefixStack(OrePrefixes.crushedPurified, Materials.Tetrahedrite, 1))
            .itemOutputs(
                ItemUtils.getOrePrefixStack(OrePrefixes.crushedCentrifuged, Materials.Tetrahedrite, 1),
                MaterialLibAPI.getStack(Materials.SeleniumDioxide, Materials2Shapes.dust, 1))
            .outputChances(100_00, 2_50)
            .fluidInputs(GTModHandler.getHotWater(1_000))
            .fluidOutputs(MaterialUtils.legacyGtppFluid(Materials.SeleniumDioxide, 10))
            .eut(TierEU.RECIPE_EV / 2)
            .duration(40 * SECONDS)
            .addTo(chemicalDehydratorRecipes);

        // Chalco
        GTValues.RA.stdBuilder()
            .itemInputs(ItemUtils.getOrePrefixStack(OrePrefixes.crushedPurified, Materials.Chalcopyrite, 1))
            .itemOutputs(
                ItemUtils.getOrePrefixStack(OrePrefixes.crushedCentrifuged, Materials.Chalcopyrite, 1),
                MaterialLibAPI.getStack(Materials.SeleniumDioxide, Materials2Shapes.dust, 1))
            .outputChances(100_00, 2_50)
            .fluidInputs(GTModHandler.getHotWater(1_000))
            .fluidOutputs(MaterialUtils.legacyGtppFluid(Materials.SeleniumDioxide, 10))
            .eut(TierEU.RECIPE_EV / 2)
            .duration(40 * SECONDS)
            .addTo(chemicalDehydratorRecipes);

        // Malachite
        GTValues.RA.stdBuilder()
            .itemInputs(ItemUtils.getOrePrefixStack(OrePrefixes.crushedPurified, Materials.Malachite, 1))
            .itemOutputs(
                ItemUtils.getOrePrefixStack(OrePrefixes.crushedCentrifuged, Materials.Malachite, 1),
                MaterialLibAPI.getStack(Materials.SeleniumDioxide, Materials2Shapes.dust, 1))
            .outputChances(100_00, 2_50)
            .fluidInputs(GTModHandler.getHotWater(1_000))
            .fluidOutputs(MaterialUtils.legacyGtppFluid(Materials.SeleniumDioxide, 10))
            .eut(TierEU.RECIPE_EV / 2)
            .duration(40 * SECONDS)
            .addTo(chemicalDehydratorRecipes);
    }
}
