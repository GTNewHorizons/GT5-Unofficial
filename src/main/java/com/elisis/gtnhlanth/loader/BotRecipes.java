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
                .itemOutputs(Phosgene.get(cell, 1)).fluidInputs(Materials.Chlorine.getGas(2000)).noFluidOutputs()
                .duration(2 * SECONDS + 10 * TICKS).eut(TierEU.RECIPE_HV).addTo(sChemicalRecipes);
        GT_Values.RA.stdBuilder().itemInputs(Materials.Chlorine.getCells(2), GT_Utility.getIntegratedCircuit(12))
                .itemOutputs(Phosgene.get(cell, 1), Materials.Empty.getCells(1))
                .fluidInputs(Materials.CarbonMonoxide.getGas(1000)).noFluidOutputs().duration(2 * SECONDS + 10 * TICKS)
                .eut(TierEU.RECIPE_HV).addTo(sChemicalRecipes);
        GT_Values.RA.stdBuilder().itemInputs(Materials.CarbonMonoxide.getCells(1), Materials.Chlorine.getCells(2))
                .itemOutputs(Phosgene.get(cell, 1), Materials.Empty.getCells(2)).noFluidInputs().noFluidOutputs()
                .duration(2 * SECONDS + 10 * TICKS).eut(TierEU.RECIPE_HV).addTo(sChemicalRecipes);
        GT_Values.RA.stdBuilder().itemInputs(Materials.Chlorine.getCells(2), GT_Utility.getIntegratedCircuit(2))
                .itemOutputs(Materials.Empty.getCells(2)).fluidInputs(Materials.CarbonMonoxide.getGas(1000))
                .fluidOutputs(BotWerkstoffMaterialPool.Phosgene.getFluidOrGas(1000)).duration(2 * SECONDS + 10 * TICKS)
                .eut(TierEU.RECIPE_HV).addTo(UniversalChemical);

        // COCl2 + C2H6O = HCl + C3H5ClO2
        GT_Values.RA.addChemicalRecipe(
                Phosgene.get(cell, 1),
                C2,
                Materials.Ethanol.getFluid(1000),
                Materials.HydrochloricAcid.getFluid(1000),
                Ethylchloroformate.get(cell, 1),
                20,
                1920);

        // C3H5ClO2 + 2NH3 = C3H7O2N + NH4Cl
        GT_Values.RA.addChemicalRecipe(
                Ethylchloroformate.get(cell, 1),
                C2,
                Materials.Ammonia.getGas(2000),
                WerkstoffLoader.AmmoniumChloride.getFluidOrGas(1000),
                Ethylcarbamate.get(cell, 1),
                200,
                120);

        // C3H7O2N + HNO3 = C3H6N2O4 + H2O
        GT_Values.RA.addChemicalRecipe(
                Ethylcarbamate.get(cell, 1),
                C2,
                Materials.NitricAcid.getFluid(1000),
                Materials.Water.getFluid(1000),
                EthylNnitrocarbamate.get(cell, 1),
                40,
                1024);

        // C3H6N2O4 + NH3 = C3H9N3O4
        GT_Values.RA.addChemicalRecipe(
                EthylNnitrocarbamate.get(cell, 1),
                C2,
                Materials.Ammonia.getGas(1000),
                null,
                AmmoniumNnitrourethane.get(cell, 1),
                40,
                1920);

        // C3H9N3O4 + N2O5 = C3H5N3O6 + N2H4O3
        GT_Values.RA.addChemicalRecipe(
                AmmoniumNnitrourethane.get(cell, 1),
                DinitrogenPentoxide.get(dust, 7),
                null,
                null,
                EthylDinitrocarbamate.get(cell, 1),
                AmmoniumNitrate.get(dust, 9),
                200,
                480);

        // C3H5N3O6 + 2NH3 = C3H7O2N + H4N4O4
        GT_Values.RA.addChemicalRecipe(
                EthylDinitrocarbamate.get(cell, 1),
                C2,
                Materials.Ammonia.getGas(2000),
                Ethylcarbamate.getFluidOrGas(1000),
                AmmoniumDinitramide.get(cell, 1),
                200,
                1920);

        // P4O10 + 2HNO3 + 5H2O = 4H3PO4 + N2O5
        GT_Values.RA.addChemicalRecipe(
                Materials.PhosphorousPentoxide.getDust(14),
                C2,
                Materials.NitricAcid.getFluid(2000),
                Materials.PhosphoricAcid.getFluid(4000),
                DinitrogenPentoxide.get(dust, 7),
                200,
                1920);

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

        // 2NaNO3 + H2SO4 = 2HNO3 + Na2SO4
        GT_Values.RA.addChemicalRecipe(
                WerkstoffLoader.SodiumNitrate.get(dust, 10),
                C2,
                Materials.SulfuricAcid.getFluid(1000),
                Materials.NitricAcid.getFluid(2000),
                WerkstoffLoader.Sodiumsulfate.get(dust, 7),
                200,
                120);

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

        // C8H10 + 6O =V2O5= C8H4O3 + 3H2O
        GT_Values.RA.addChemicalRecipe(
                OXylene.get(cell, 1),
                VanadiumPentoxide.get(dustTiny),
                Materials.Oxygen.getGas(6000),
                Materials.Water.getFluid(3000),
                PhthalicAnhydride.get(dust, 15),
                800,
                1920);

        // C6H6 + C4H8 = C10H14
        GT_Values.RA.addChemicalRecipe(
                Materials.Benzene.getCells(1),
                C2,
                Materials.Butene.getGas(1000),
                TertButylbenzene.getFluidOrGas(1000),
                cells,
                100,
                1920);

        // C8H4O3 + C10H14 = C18H16O2 + H2O
        GT_Values.RA.addChemicalRecipe(
                PhthalicAnhydride.get(dust, 15),
                C2,
                TertButylbenzene.getFluidOrGas(1000),
                TwoTertButylAnthraquinone.getFluidOrGas(1000),
                null,
                200,
                7680);

        // C18H16O2 + H =Pd= C18H17O2
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(
                Materials.Hydrogen.getCells(10),
                Materials.Palladium.getDustTiny(1),
                TwoTertButylAnthraquinone.getFluidOrGas(10000),
                TwoTertButylAnthrahydroquinone.getFluidOrGas(10000),
                null,
                null,
                1200,
                7680);

        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { C2, Materials.Palladium.getDustTiny(1) },
                new FluidStack[] { Materials.Hydrogen.getGas(10000), TwoTertButylAnthraquinone.getFluidOrGas(10000) },
                new FluidStack[] { TwoTertButylAnthrahydroquinone.getFluidOrGas(10000) },
                null,
                1200,
                7680);

        // 2CH4O + H2SO4 = C2H6O4S + 2H2O
        GT_Values.RA.addChemicalRecipe(
                Materials.SulfuricAcid.getCells(1),
                C2,
                Materials.Methanol.getFluid(2000),
                null,
                DimethylSulfate.get(cell, 1),
                50,
                480);

        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(
                Materials.SulfuricAcid.getCells(1),
                C1,
                Materials.Methanol.getFluid(2000),
                DimethylSulfate.getFluidOrGas(1000),
                cells,
                null,
                50,
                480);

        // 2HNO3 + C3H8 = 2CH3NO2 + 2H2O + C
        GT_Values.RA.addCrackingRecipe(
                2,
                Materials.Propane.getGas(1000),
                Materials.NitricAcid.getFluid(2000),
                Nitromethane.getFluidOrGas(2000),
                300,
                1920);

        // HF + BF3 + 3CH3NO2 + 6HNO3 = 3CO2 + 8H2O + 8NO + NO2BF4
        // Combine of two reactions:
        // BF3 + HF + HNO3 = NO2BF4 + H2O
        // 3CH3NO2 + 5HNO3 = 3CO2 + 7H2O + 8NO
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { C2 },
                new FluidStack[] { Materials.HydrofluoricAcid.getFluid(1000), BoronTrifluoride.getFluidOrGas(1000),
                        Nitromethane.getFluidOrGas(3000), Materials.NitricAcid.getFluid(6000), },
                new FluidStack[] { Materials.CarbonDioxide.getGas(3000), Materials.Water.getFluid(8000),
                        Materials.NitricOxide.getGas(8000) },
                new ItemStack[] { NitroniumTetrafluoroborate.get(dust, 8) },
                100,
                7_680);

        // NO2BF4 + H4N4O4 = N4O6 + NH4BF4
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { C2, NitroniumTetrafluoroborate.get(dust, 8) },
                new FluidStack[] { AmmoniumDinitramide.getFluidOrGas(1000) },
                new FluidStack[] { Trinitramid.getFluidOrGas(1000), AmmoniaBoronfluorideSolution.getFluidOrGas(1000) },
                null,
                20,
                30_720);

        // B2O3 + 6HF = 2BF3 + 3H2O
        cells.stackSize = 3;
        GT_Values.RA.addChemicalRecipe(
                BoronTrioxide.get(dust, 5),
                cells,
                Materials.HydrofluoricAcid.getFluid(6000),
                BoronTrifluoride.getFluidOrGas(2000),
                Materials.Water.getCells(3),
                50,
                480);

        // Na2B4O7(H2O)10 + H2SO4 -> 2B2O3 + Na2SO4 + 11H2O
        GT_Values.RA.addChemicalRecipe(
                Materials.Borax.getDust(23),
                C2,
                Materials.SulfuricAcid.getFluid(1000),
                null,
                WerkstoffLoader.Sodiumsulfate.get(dust, 7),
                BoronTrioxide.get(dust, 10),
                400,
                1920);

        // NH4BF4 = NH3 + HBF4
        cells.stackSize = 1;
        GT_Values.RA.addUniversalDistillationRecipe(
                AmmoniaBoronfluorideSolution.getFluidOrGas(1000),
                new FluidStack[] { Materials.Ammonia.getGas(1000), Tetrafluoroborate.getFluidOrGas(1000) },
                null,
                20,
                30_720);

        // HBF4 + NaOH = NaBF4 + H2O
        GT_Values.RA.addChemicalRecipe(
                Materials.SodiumHydroxide.getDust(3),
                null,
                Tetrafluoroborate.getFluidOrGas(1000),
                SodiumTetrafluoroborate.getFluidOrGas(1000),
                null,
                100,
                1920);

        // NaBF4 = NaF + BF3
        GT_Values.RA.addDistilleryRecipe(
                C2,
                SodiumTetrafluoroborate.getFluidOrGas(1000),
                BoronTrifluoride.getFluidOrGas(1000),
                SodiumFluoride.get(dust, 2),
                40,
                480,
                false);

        // 2NaF + H2SO4 = 2HF + Na2SO4
        GT_Values.RA.addChemicalRecipe(
                SodiumFluoride.get(dust, 4),
                C2,
                Materials.SulfuricAcid.getFluid(1000),
                Materials.HydrofluoricAcid.getFluid(2000),
                WerkstoffLoader.Sodiumsulfate.get(dust, 7),
                50,
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

    public static void addFuels() {
        GT_Recipe.GT_Recipe_Map.sTurbineFuels.addFuel(TertButylbenzene.get(cell, 1), null, 420);
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
