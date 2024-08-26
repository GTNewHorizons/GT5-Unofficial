package com.elisis.gtnhlanth.loader;

import static com.elisis.gtnhlanth.common.register.BotWerkstoffMaterialPool.*;
import static gregtech.api.enums.OrePrefixes.*;
import static gregtech.api.recipe.RecipeMaps.autoclaveRecipes;
import static gregtech.api.recipe.RecipeMaps.blastFurnaceRecipes;
import static gregtech.api.recipe.RecipeMaps.chemicalReactorRecipes;
import static gregtech.api.recipe.RecipeMaps.crackingRecipes;
import static gregtech.api.recipe.RecipeMaps.distilleryRecipes;
import static gregtech.api.recipe.RecipeMaps.electrolyzerRecipes;
import static gregtech.api.recipe.RecipeMaps.multiblockChemicalReactorRecipes;
import static gregtech.api.util.GT_RecipeBuilder.MINUTES;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;
import static gregtech.api.util.GT_RecipeConstants.COIL_HEAT;
import static gregtech.api.util.GT_RecipeConstants.UniversalChemical;

import java.util.HashSet;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.elisis.gtnhlanth.common.register.BotWerkstoffMaterialPool;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import ic2.core.Ic2Items;

/*
 * Originally authored by botn365 under the MIT License. See BotdustriesLICENSE
 */

public class BotRecipes {

    public static void addGTRecipe() {
        ItemStack C1 = GT_Utility.getIntegratedCircuit(1);
        ItemStack C2 = GT_Utility.getIntegratedCircuit(2);
        ItemStack C24 = GT_Utility.getIntegratedCircuit(24);

        // CaCO3 + 2HCl = H2O + CO2 + CaCl2
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Calcite.getDust(5), Materials.Empty.getCells(1))
            .itemOutputs(Materials.CarbonDioxide.getCells(1), WerkstoffLoader.CalciumChloride.get(dust, 3))
            .fluidInputs(Materials.HydrochloricAcid.getFluid(2000))
            .fluidOutputs(Materials.Water.getFluid(1000))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        // tungsten chain
        FluidStack sodiumTungsten = SodiumTungstate.getFluidOrGas(1000);
        ItemStack scheelite = Materials.Scheelite.getDust(6);

        // Li2WO4 + 2Na = Na2WO4 + 2Li
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Tungstate.getDust(7), Materials.Sodium.getDust(2))
            .itemOutputs(Materials.Lithium.getDust(2))
            .fluidInputs(Materials.Water.getFluid(4000))
            .fluidOutputs(sodiumTungsten)
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(autoclaveRecipes);

        // MnWO4 + 2Na = Na2WO4 + Mn
        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.Huebnerit.get(dust, 6), Materials.Sodium.getDust(2))
            .itemOutputs(Materials.Manganese.getDust(1))
            .fluidInputs(Materials.Water.getFluid(4000))
            .fluidOutputs(sodiumTungsten)
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(autoclaveRecipes);

        // FeWO4 + 2Na = Na2WO4 + Fe
        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.Ferberite.get(dust, 6), Materials.Sodium.getDust(2))
            .itemOutputs(Materials.Iron.getDust(1))
            .fluidInputs(Materials.Water.getFluid(4000))
            .fluidOutputs(sodiumTungsten)
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(autoclaveRecipes);

        // CaCl2 + Na2WO4 = 2NaCl + CaWO4
        ItemStack Ca2Cl = WerkstoffLoader.CalciumChloride.get(dust, 3);
        GT_Values.RA.stdBuilder()
            .itemInputs(Ca2Cl)
            .itemOutputs(scheelite, Materials.Salt.getDust(4))
            .fluidInputs(sodiumTungsten)
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        ItemStack H2WO4 = TungsticAcid.get(dust, 7);
        // CaWO4 + 2HCl = H2WO4 + CaCl2
        GT_Values.RA.stdBuilder()
            .itemInputs(scheelite)
            .itemOutputs(H2WO4, Ca2Cl)
            .fluidInputs(Materials.HydrochloricAcid.getFluid(2000))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_EV)
            .addTo(UniversalChemical);

        ItemStack WO3 = TungstenTrioxide.get(dust, 4);
        // H2WO4 = WO3 + H2O
        GT_Values.RA.stdBuilder()
            .itemInputs(H2WO4)
            .itemOutputs(WO3)
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        // WO3 + 6H = W + 3H2O
        GT_Values.RA.stdBuilder()
            .itemInputs(WO3, C2)
            .itemOutputs(Materials.Tungsten.getDust(1))
            .fluidInputs(Materials.Hydrogen.getGas(6000))
            .fluidOutputs(GT_ModHandler.getSteam(3000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(COIL_HEAT, 1000)
            .addTo(blastFurnaceRecipes);

        WO3.stackSize = 8;
        // 2WO3 + 3C = 2W + 3CO2
        GT_Values.RA.stdBuilder()
            .itemInputs(WO3, Materials.Carbon.getDust(3))
            .itemOutputs(GT_OreDictUnificator.get(ingotHot, Materials.Tungsten, 2L))
            .fluidOutputs(Materials.CarbonDioxide.getGas(3000))
            .duration(6 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .metadata(COIL_HEAT, 3000)
            .addTo(blastFurnaceRecipes);

        // rocket fuels
        // LMP103S
        // 2Cl + CO = COCl2

        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.CarbonMonoxide.getCells(1), GT_Utility.getIntegratedCircuit(12))
            .itemOutputs(Phosgene.get(cell, 1))
            .fluidInputs(Materials.Chlorine.getGas(2000))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(chemicalReactorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Chlorine.getCells(2), GT_Utility.getIntegratedCircuit(12))
            .itemOutputs(Phosgene.get(cell, 1), Materials.Empty.getCells(1))
            .fluidInputs(Materials.CarbonMonoxide.getGas(1000))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(chemicalReactorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.CarbonMonoxide.getCells(1), Materials.Chlorine.getCells(2))
            .itemOutputs(Phosgene.get(cell, 1), Materials.Empty.getCells(2))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(chemicalReactorRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Chlorine.getCells(2), GT_Utility.getIntegratedCircuit(2))
            .itemOutputs(Materials.Empty.getCells(2))
            .fluidInputs(Materials.CarbonMonoxide.getGas(1000))
            .fluidOutputs(BotWerkstoffMaterialPool.Phosgene.getFluidOrGas(1000))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        // H3PO4 = P + H2O
        GT_Values.RA.stdBuilder()
            .itemInputs(C2)
            .itemOutputs(Materials.Phosphorus.getDust(1))
            .fluidInputs(Materials.PhosphoricAcid.getFluid(1000))
            .fluidOutputs(Materials.Water.getFluid(500))
            .eut(TierEU.RECIPE_HV)
            .duration(1 * SECONDS)
            .addTo(distilleryRecipes);

        ItemStack cells = Ic2Items.cell.copy();
        cells.stackSize = 1;
        // NH4Cl = HCl + NH3
        GT_Values.RA.stdBuilder()
            .itemInputs(cells)
            .itemOutputs(Materials.Ammonia.getCells(1))
            .fluidInputs(WerkstoffLoader.AmmoniumChloride.getFluidOrGas(1000))
            .fluidOutputs(Materials.HydrochloricAcid.getFluid(1000))
            .eut(TierEU.RECIPE_MV)
            .duration(2 * SECONDS + 10 * TICKS)
            .addTo(distilleryRecipes);

        // N2H4O3 + NaOH = NaNO3 + NH3 + H2O
        GT_Values.RA.stdBuilder()
            .itemInputs(AmmoniumNitrate.get(dust, 9), Materials.SodiumHydroxide.getDust(3))
            .itemOutputs(WerkstoffLoader.SodiumNitrate.get(dust, 5))
            .fluidOutputs(Materials.Ammonia.getGas(1000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(chemicalReactorRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(AmmoniumNitrate.get(dust, 9), Materials.SodiumHydroxide.getDust(3), C2)
            .itemOutputs(WerkstoffLoader.SodiumNitrate.get(dust, 5))
            .fluidOutputs(Materials.Ammonia.getGas(1000), Materials.Water.getFluid(1000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        // N2H4O3 + NaOH + H =H2SO4= NH3 + HNO3 + Na + H2O
        GT_Values.RA.stdBuilder()
            .itemInputs(C24, AmmoniumNitrate.get(dust, 9), Materials.SodiumHydroxide.getDust(3))
            .itemOutputs(Materials.Sodium.getDust(1))
            .fluidInputs(Materials.SulfuricAcid.getFluid(1000), Materials.Hydrogen.getGas(1000))
            .fluidOutputs(
                Materials.Ammonia.getGas(1000),
                Materials.NitricAcid.getFluid(1000),
                Materials.DilutedSulfuricAcid.getFluid(1000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        // 2HNO3 + C3H8 = 2CH3NO2 + 2H2O + C
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(2))
            .fluidInputs(Materials.Propane.getGas(1000), Materials.NitricAcid.getFluid(2000))
            .fluidOutputs(Nitromethane.getFluidOrGas(2000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(crackingRecipes);
    }

    public static void removeRecipes() {
        BotRecipes.removeTungstenElectro();
    }

    public static void removeTungstenElectro() {
        HashSet<GT_Recipe> toDel = new HashSet<>();
        ItemStack[] toRemove = { Materials.Scheelite.getDust(1), Materials.Tungstate.getDust(1),
            WerkstoffLoader.Ferberite.get(dust, 1), WerkstoffLoader.Huebnerit.get(dust, 1) };
        for (GT_Recipe tRecipe : electrolyzerRecipes.getAllRecipes()) {
            if (tRecipe.mFakeRecipe) continue;
            for (int i = 0; i < tRecipe.mInputs.length; i++) {
                ItemStack tItem = tRecipe.mInputs[i];
                if (item == null || !GT_Utility.isStackValid(tItem)) continue;
                for (ItemStack tStack : toRemove) {
                    if (GT_Utility.areStacksEqual(tItem, tStack)) {
                        toDel.add(tRecipe);
                        continue;
                    }
                }
            }
        }
        electrolyzerRecipes.getBackend()
            .removeRecipes(toDel);
        electrolyzerRecipes.getBackend()
            .reInit();
    }
}
