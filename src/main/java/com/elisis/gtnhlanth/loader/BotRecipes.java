package com.elisis.gtnhlanth.loader;

import static com.elisis.gtnhlanth.common.register.BotWerkstoffMaterialPool.*;
import static gregtech.api.enums.Mods.GTPlusPlus;
import static gregtech.api.enums.OrePrefixes.*;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sChemicalRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;
import static gregtech.api.util.GT_RecipeConstants.UniversalChemical;

import java.util.Collection;
import java.util.HashSet;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
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
        GT_Values.RA.addChemicalRecipe(
                Materials.Calcite.getDust(5),
                Materials.Empty.getCells(1),
                Materials.HydrochloricAcid.getFluid(2000),
                Materials.Water.getFluid(1000),
                Materials.CarbonDioxide.getCells(1),
                WerkstoffLoader.CalciumChloride.get(dust, 3),
                80,
                120);

        // tungsten chain
        FluidStack sodiumTungsten = SodiumTungstate.getFluidOrGas(1000);
        ItemStack scheelite = Materials.Scheelite.getDust(6);

        // Li2WO4 + 2Na = Na2WO4 + 2Li
        GT_Values.RA.addAutoclaveRecipe(
                Materials.Tungstate.getDust(7),
                Materials.Sodium.getDust(2),
                Materials.Water.getFluid(4000),
                sodiumTungsten,
                Materials.Lithium.getDust(2),
                10000,
                100,
                1920,
                false);

        // MnWO4 + 2Na = Na2WO4 + Mn
        GT_Values.RA.addAutoclaveRecipe(
                WerkstoffLoader.Huebnerit.get(dust, 6),
                Materials.Sodium.getDust(2),
                Materials.Water.getFluid(4000),
                sodiumTungsten,
                Materials.Manganese.getDust(1),
                10000,
                100,
                1920,
                false);

        // FeWO4 + 2Na = Na2WO4 + Fe
        GT_Values.RA.addAutoclaveRecipe(
                WerkstoffLoader.Ferberite.get(dust, 6),
                Materials.Sodium.getDust(2),
                Materials.Water.getFluid(4000),
                sodiumTungsten,
                Materials.Iron.getDust(1),
                10000,
                100,
                1920,
                false);

        // CaCl2 + Na2WO4 = 2NaCl + CaWO4
        ItemStack Ca2Cl = WerkstoffLoader.CalciumChloride.get(dust, 3);
        GT_Values.RA
                .addChemicalRecipe(Ca2Cl, null, sodiumTungsten, null, scheelite, Materials.Salt.getDust(4), 100, 480);

        ItemStack H2WO4 = TungsticAcid.get(dust, 7);
        // CaWO4 + 2HCl = H2WO4 + CaCl2
        GT_Values.RA.addChemicalRecipe(
                scheelite,
                null,
                Materials.HydrochloricAcid.getFluid(2000),
                null,
                H2WO4,
                Ca2Cl,
                50,
                1920);

        ItemStack WO3 = TungstenTrioxide.get(dust, 4);
        // H2WO4 = WO3 + H2O
        GT_Values.RA.addBlastRecipe(H2WO4, null, null, null, WO3, null, 200, 480, 1200);

        // WO3 + 6H = W + 3H2O
        GT_Values.RA.addBlastRecipe(
                WO3,
                C2,
                Materials.Hydrogen.getGas(6000),
                GT_ModHandler.getSteam(3000),
                Materials.Tungsten.getDust(1),
                null,
                100,
                1920,
                1000);

        WO3.stackSize = 8;
        // 2WO3 + 3C = 2W + 3CO2
        GT_Values.RA.addBlastRecipe(
                WO3,
                Materials.Carbon.getDust(3),
                null,
                Materials.CarbonDioxide.getGas(3000),
                GT_OreDictUnificator.get(ingotHot, Materials.Tungsten, 2L),
                null,
                8000,
                1920,
                3000);

        // rocket fuels
        // LMP103S
        // 2Cl + CO = COCl2

        GT_Values.RA.stdBuilder().itemInputs(Materials.CarbonMonoxide.getCells(1), GT_Utility.getIntegratedCircuit(12))
                .itemOutputs(Phosgene.get(cell, 1)).fluidInputs(Materials.Chlorine.getGas(2000))
                .duration(2 * SECONDS + 10 * TICKS).eut(TierEU.RECIPE_HV).addTo(sChemicalRecipes);
        GT_Values.RA.stdBuilder().itemInputs(Materials.Chlorine.getCells(2), GT_Utility.getIntegratedCircuit(12))
                .itemOutputs(Phosgene.get(cell, 1), Materials.Empty.getCells(1))
                .fluidInputs(Materials.CarbonMonoxide.getGas(1000)).duration(2 * SECONDS + 10 * TICKS)
                .eut(TierEU.RECIPE_HV).addTo(sChemicalRecipes);
        GT_Values.RA.stdBuilder().itemInputs(Materials.CarbonMonoxide.getCells(1), Materials.Chlorine.getCells(2))
                .itemOutputs(Phosgene.get(cell, 1), Materials.Empty.getCells(2)).duration(2 * SECONDS + 10 * TICKS)
                .eut(TierEU.RECIPE_HV).addTo(sChemicalRecipes);
        GT_Values.RA.stdBuilder().itemInputs(Materials.Chlorine.getCells(2), GT_Utility.getIntegratedCircuit(2))
                .itemOutputs(Materials.Empty.getCells(2)).fluidInputs(Materials.CarbonMonoxide.getGas(1000))
                .fluidOutputs(BotWerkstoffMaterialPool.Phosgene.getFluidOrGas(1000)).duration(2 * SECONDS + 10 * TICKS)
                .eut(TierEU.RECIPE_HV).addTo(UniversalChemical);

        // H3PO4 = P + H2O
        GT_Values.RA.addDistilleryRecipe(
                C2,
                Materials.PhosphoricAcid.getFluid(1000),
                Materials.Water.getFluid(500),
                Materials.Phosphorus.getDust(1),
                20,
                480,
                false);

        ItemStack cells = Ic2Items.cell.copy();
        cells.stackSize = 1;
        // NH4Cl = HCl + NH3
        GT_Values.RA.addDistilleryRecipe(
                cells,
                WerkstoffLoader.AmmoniumChloride.getFluidOrGas(1000),
                Materials.HydrochloricAcid.getFluid(1000),
                Materials.Ammonia.getCells(1),
                50,
                120,
                false);

        // N2H4O3 + NaOH = NaNO3 + NH3 + H2O
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(
                AmmoniumNitrate.get(dust, 9),
                Materials.SodiumHydroxide.getDust(3),
                null,
                Materials.Ammonia.getGas(1000),
                WerkstoffLoader.SodiumNitrate.get(dust, 5),
                null,
                100,
                480);

        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { AmmoniumNitrate.get(dust, 9), Materials.SodiumHydroxide.getDust(3), C2 },
                null,
                new FluidStack[] { Materials.Ammonia.getGas(1000), Materials.Water.getFluid(1000) },
                new ItemStack[] { WerkstoffLoader.SodiumNitrate.get(dust, 5) },
                100,
                480);

        // N2H4O3 + NaOH + H =H2SO4= NH3 + HNO3 + Na + H2O
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { C24, AmmoniumNitrate.get(dust, 9), Materials.SodiumHydroxide.getDust(3) },
                new FluidStack[] { Materials.SulfuricAcid.getFluid(1000), Materials.Hydrogen.getGas(1000) },
                new FluidStack[] { Materials.Ammonia.getGas(1000), Materials.NitricAcid.getFluid(1000),
                        Materials.DilutedSulfuricAcid.getFluid(1000) },
                new ItemStack[] { Materials.Sodium.getDust(1) },
                300,
                480);

        // Monomethylhydrazine
        cells.stackSize = 1;
        // C7H8 + CH4O = C8H10 + H2O
        GT_Values.RA.addCrackingRecipe(
                1,
                Materials.Toluene.getFluid(1000),
                Materials.Methanol.getFluid(1000),
                OXylene.getFluidOrGas(1000),
                600,
                4096);

        // 2HNO3 + C3H8 = 2CH3NO2 + 2H2O + C
        GT_Values.RA.addCrackingRecipe(
                2,
                Materials.Propane.getGas(1000),
                Materials.NitricAcid.getFluid(2000),
                Nitromethane.getFluidOrGas(2000),
                300,
                1920);

        if (GTPlusPlus.isModLoaded()) {
            // N2H4 + C2H4O2 =C2H6O= C2H6N2O + H2O
            GT_Values.RA.addMultiblockChemicalRecipe(
                    new ItemStack[] { C2 },
                    new FluidStack[] { Materials.AceticAcid.getFluid(1000), Materials.Ethanol.getFluid(1000),
                            new FluidStack(FluidRegistry.getFluid("fluid.hydrazine"), 1000) },
                    new FluidStack[] { Acetylhydrazine.getFluidOrGas(1000), Materials.Ethanol.getFluid(1000) },
                    null,
                    40,
                    30_720);

            // C2H6N2O + 2CH2O + 4H = C2H8N2 + C2H4O2 + H2O
            GT_Values.RA.addMultiblockChemicalRecipe(
                    new ItemStack[] { C2 },
                    new FluidStack[] { Acetylhydrazine.getFluidOrGas(1000),
                            new FluidStack(FluidRegistry.getFluid("fluid.formaldehyde"), 2000),
                            Materials.Hydrogen.getGas(4000) },
                    new FluidStack[] { UnsymmetricalDimethylhydrazine.getFluidOrGas(1000),
                            Materials.AceticAcid.getFluid(1000), Materials.Water.getFluid(1000) },
                    null,
                    20,
                    122_880);
        }
    }

    public static void removeRecipes() {
        BotRecipes.removeTungstenElectro();
    }

    public static void removeTungstenElectro() {
        Collection<GT_Recipe> electroRecipeMap = GT_Recipe.GT_Recipe_Map.sElectrolyzerRecipes.mRecipeList;
        HashSet<GT_Recipe> toDel = new HashSet<>();
        ItemStack[] toRemove = { Materials.Scheelite.getDust(1), Materials.Tungstate.getDust(1),
                WerkstoffLoader.Ferberite.get(dust, 1), WerkstoffLoader.Huebnerit.get(dust, 1) };
        for (GT_Recipe tRecipe : electroRecipeMap) {
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
        electroRecipeMap.removeAll(toDel);
        GT_Recipe.GT_Recipe_Map.sElectrolyzerRecipes.reInit();
    }
}
