package gtPlusPlus.core.recipe;

import static gregtech.api.recipe.RecipeMaps.brewingRecipes;
import static gregtech.api.recipe.RecipeMaps.chemicalBathRecipes;
import static gregtech.api.recipe.RecipeMaps.electrolyzerRecipes;
import static gregtech.api.recipe.RecipeMaps.laserEngraverRecipes;
import static gregtech.api.recipe.RecipeMaps.mixerRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gtPlusPlus.core.material.MISC_MATERIALS.BRINE;
import static gtPlusPlus.core.material.MISC_MATERIALS.HYDROGEN_CHLORIDE;
import static gtPlusPlus.core.material.MISC_MATERIALS.RARE_EARTH_HIGH;
import static gtPlusPlus.core.material.MISC_MATERIALS.RARE_EARTH_LOW;
import static gtPlusPlus.core.material.MISC_MATERIALS.RARE_EARTH_MID;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.material.MaterialGenerator;
import gtPlusPlus.core.material.ORES;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public class RECIPES_RareEarthProcessing {

    public static void init() {

        // Brine Check and assignment
        FluidStack mBrine = FluidUtils.getFluidStack("brine", 1000);
        if (mBrine == null) {
            Fluid f = BRINE.generateFluid();
            BRINE.registerComponentForMaterial(FluidUtils.getFluidStack(f, 1000));
            mBrine = BRINE.getFluidStack(1000);
        } else {
            BRINE.registerComponentForMaterial(FluidUtils.getFluidStack(mBrine, 1000));
        }

        // Hydrogen Chloride Check and assignment
        FluidStack mHydrogenChloride = FluidUtils.getFluidStack("hydrogenchloride", 1000);
        if (mHydrogenChloride == null) {
            HYDROGEN_CHLORIDE.generateFluid();
            mHydrogenChloride = BRINE.getFluidStack(1000);
        } else {
            HYDROGEN_CHLORIDE.registerComponentForMaterial(FluidUtils.getFluidStack(mHydrogenChloride, 1000));
        }

        // Add Process for creating Brine
        GT_Values.RA.stdBuilder().itemInputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Salt, 16L))
                .fluidInputs(Materials.SaltWater.getFluid(2000L)).fluidOutputs(FluidUtils.getFluidStack(mBrine, 4000))
                .duration(20 * SECONDS).eut(TierEU.RECIPE_MV).addTo(brewingRecipes);

        // Chloralkali process
        GT_Values.RA.stdBuilder().itemInputs(GT_Utility.getIntegratedCircuit(1), ItemList.Cell_Empty.get(2L))
                .itemOutputs(
                        GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Chlorine, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Hydrogen, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SodiumHydroxide, 1L))
                .fluidInputs(FluidUtils.getFluidStack(mBrine, 2000)).duration(30 * SECONDS).eut(TierEU.RECIPE_MV)
                .addTo(electrolyzerRecipes);

        // Generate Special Laser Recipe
        GT_Values.RA.stdBuilder()
                .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Chlorine, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Hydrogen, 1L),
                        CI.getNumberedBioCircuit(2))
                .itemOutputs(ItemUtils.getSimpleStack(ModItems.cellHydrogenChlorideMix, 2)).duration(10 * SECONDS)
                .eut(TierEU.RECIPE_HV).addTo(mixerRecipes);

        GT_Values.RA.stdBuilder()
                .itemInputs(
                        ItemUtils.getSimpleStack(ModItems.cellHydrogenChlorideMix, 4),
                        GregtechItemList.Laser_Lens_WoodsGlass.get(0))
                .itemOutputs(ItemUtils.getItemStackOfAmountFromOreDict("cellHydrogenChloride", 4))
                .duration(30 * SECONDS).eut(TierEU.RECIPE_HV).noOptimize().addTo(laserEngraverRecipes);

        // Set Material Tiers correctly
        ORES.GREENOCKITE.vTier = 1;
        RARE_EARTH_LOW.vTier = 1;
        RARE_EARTH_MID.vTier = 3;
        RARE_EARTH_HIGH.vTier = 5;

        // Set Material Voltages correctly
        ORES.GREENOCKITE.vVoltageMultiplier = 30;
        RARE_EARTH_LOW.vVoltageMultiplier = 30;
        RARE_EARTH_MID.vVoltageMultiplier = 480;
        RARE_EARTH_HIGH.vVoltageMultiplier = 7680;

        // Set Material Tooltips to be shorter
        RARE_EARTH_LOW.vChemicalFormula = "??????";
        RARE_EARTH_MID.vChemicalFormula = "??????";
        RARE_EARTH_HIGH.vChemicalFormula = "??????";

        // Set Material Tooltips to be shorter
        RARE_EARTH_LOW.vChemicalSymbol = "??";
        RARE_EARTH_MID.vChemicalSymbol = "??";
        RARE_EARTH_HIGH.vChemicalSymbol = "??";

        // Generate Ore Materials
        MaterialGenerator.generateOreMaterial(RARE_EARTH_LOW);
        MaterialGenerator.generateOreMaterial(RARE_EARTH_MID);
        MaterialGenerator.generateOreMaterial(RARE_EARTH_HIGH);

        // industrial strength HCl
        Fluid aHydrochloric = FluidUtils.getFluidStack("hydrogenchloride", 1).getFluid();

        // LV Rare Earth
        GT_Values.RA.stdBuilder().itemInputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.RareEarth, 3L))
                .itemOutputs(RARE_EARTH_LOW.getCrushed(2), RARE_EARTH_LOW.getCrushed(2), RARE_EARTH_LOW.getCrushed(2))
                .fluidInputs(Materials.SulfuricAcid.getFluid(1000L)).duration(30 * SECONDS).eut(TierEU.RECIPE_LV)
                .addTo(chemicalBathRecipes);

        // HV Rare Earth
        GT_Values.RA.stdBuilder().itemInputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.RareEarth, 3L))
                .itemOutputs(RARE_EARTH_MID.getCrushed(2), RARE_EARTH_MID.getCrushed(2), RARE_EARTH_MID.getCrushed(2))
                .outputChances(10000, 9000, 8000).fluidInputs(FluidUtils.getFluidStack(aHydrochloric, 1000))
                .duration(15 * SECONDS).eut(TierEU.RECIPE_HV).addTo(chemicalBathRecipes);

        // IV Rare Earth
        GT_Values.RA.stdBuilder().itemInputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.RareEarth, 3L))
                .itemOutputs(
                        RARE_EARTH_HIGH.getCrushed(2),
                        RARE_EARTH_HIGH.getCrushed(2),
                        RARE_EARTH_HIGH.getCrushed(2))
                .outputChances(10000, 9000, 8000).fluidInputs(FluidUtils.getHydrofluoricAcid(1000))
                .duration(10 * SECONDS).eut(TierEU.RECIPE_IV).addTo(chemicalBathRecipes);

        // IV Rare Earth
        GT_Values.RA.stdBuilder().itemInputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.RareEarth, 3L))
                .itemOutputs(
                        RARE_EARTH_HIGH.getCrushed(2),
                        RARE_EARTH_HIGH.getCrushed(2),
                        RARE_EARTH_HIGH.getCrushed(2))
                .outputChances(9000, 8000, 7000).fluidInputs(Materials.HydrofluoricAcid.getFluid(2000L))
                .duration(10 * SECONDS).eut(TierEU.RECIPE_IV).addTo(chemicalBathRecipes);
    }
}
