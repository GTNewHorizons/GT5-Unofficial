package goodgenerator.loader;

import static goodgenerator.api.recipe.GoodGeneratorRecipeMaps.neutronActivatorRecipes;
import static goodgenerator.items.GGMaterial.P507;
import static goodgenerator.items.GGMaterial.adamantine;
import static goodgenerator.items.GGMaterial.concentratedEnrichedNaquadahSludge;
import static goodgenerator.items.GGMaterial.enrichedNaquadahEarth;
import static goodgenerator.items.GGMaterial.enrichedNaquadahGoo;
import static goodgenerator.items.GGMaterial.enrichedNaquadahRichSolution;
import static goodgenerator.items.GGMaterial.enrichedNaquadahSulphate;
import static goodgenerator.items.GGMaterial.fluorineRichWasteLiquid;
import static goodgenerator.items.GGMaterial.fluoroantimonicAcid;
import static goodgenerator.items.GGMaterial.galliumHydroxide;
import static goodgenerator.items.GGMaterial.indiumPhosphate;
import static goodgenerator.items.GGMaterial.inertEnrichedNaquadah;
import static goodgenerator.items.GGMaterial.inertNaquadah;
import static goodgenerator.items.GGMaterial.inertNaquadria;
import static goodgenerator.items.GGMaterial.lowQualityNaquadahEmulsion;
import static goodgenerator.items.GGMaterial.lowQualityNaquadahSolution;
import static goodgenerator.items.GGMaterial.lowQualityNaquadriaPhosphate;
import static goodgenerator.items.GGMaterial.lowQualityNaquadriaSolution;
import static goodgenerator.items.GGMaterial.lowQualityNaquadriaSulphate;
import static goodgenerator.items.GGMaterial.magnesiumSulphate;
import static goodgenerator.items.GGMaterial.naquadahAdamantiumSolution;
import static goodgenerator.items.GGMaterial.naquadahEarth;
import static goodgenerator.items.GGMaterial.naquadahGoo;
import static goodgenerator.items.GGMaterial.naquadahRichSolution;
import static goodgenerator.items.GGMaterial.naquadahine;
import static goodgenerator.items.GGMaterial.naquadriaEarth;
import static goodgenerator.items.GGMaterial.naquadriaGoo;
import static goodgenerator.items.GGMaterial.naquadriaRichSolution;
import static goodgenerator.items.GGMaterial.naquadriaSulphate;
import static goodgenerator.items.GGMaterial.titaniumTrifluoride;
import static goodgenerator.items.GGMaterial.towEthyl1Hexanol;
import static goodgenerator.items.GGMaterial.triniumSulphate;
import static goodgenerator.items.GGMaterial.wasteLiquid;
import static goodgenerator.main.GGConfigLoader.EnableNaquadahRework;
import static goodgenerator.util.MyRecipeAdder.computeRangeNKE;
import static gregtech.api.recipe.RecipeMaps.autoclaveRecipes;
import static gregtech.api.recipe.RecipeMaps.blastFurnaceRecipes;
import static gregtech.api.recipe.RecipeMaps.centrifugeRecipes;
import static gregtech.api.recipe.RecipeMaps.chemicalReactorRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidSolidifierRecipes;
import static gregtech.api.recipe.RecipeMaps.maceratorRecipes;
import static gregtech.api.recipe.RecipeMaps.mixerRecipes;
import static gregtech.api.recipe.RecipeMaps.multiblockChemicalReactorRecipes;
import static gregtech.api.recipe.RecipeMaps.vacuumFreezerRecipes;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;
import static gregtech.api.util.GTRecipeConstants.NKE_RANGE;
import static gregtech.api.util.GTRecipeConstants.QFT_FOCUS_TIER;
import static gregtech.api.util.GTRecipeConstants.UniversalChemical;
import static gregtech.common.items.MetaGeneratedItem01.registerCauldronCleaningFor;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.quantumForceTransformerRecipes;

import java.util.HashSet;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import bartworks.system.material.WerkstoffLoader;
import bartworks.system.material.gtenhancement.PlatinumSludgeOverHaul;
import goodgenerator.main.GoodGenerator;
import goodgenerator.util.CrackRecipeAdder;
import goodgenerator.util.ItemRefer;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.IRecipeMutableAccess;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.common.items.CombType;
import gregtech.loaders.misc.GTBees;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.item.chemistry.GenericChem;

public class NaquadahReworkRecipeLoader {

    public static void RecipeLoad() {

        if (!EnableNaquadahRework) return;

        // Naquadah (UEV)
        GTValues.RA.stdBuilder()
            .itemInputs(
                naquadahEarth.get(OrePrefixes.dust, 32),
                Materials.Sodium.getDust(64),
                Materials.Carbon.getDust(1),
                GTUtility.copyAmount(0, GenericChem.mSimpleNaquadahCatalyst))
            .itemOutputs(
                inertNaquadah.get(OrePrefixes.dust, 1),
                Materials.Titanium.getDust(64),
                Materials.Adamantium.getDust(64),
                Materials.Gallium.getDust(64))
            .fluidInputs(
                Materials.Hydrogen.getGas(64000L),
                Materials.Fluorine.getGas(64000L),
                Materials.Oxygen.getGas(100L))
            .duration(10 * SECONDS)
            .eut(GTValues.VP[10])
            .metadata(QFT_FOCUS_TIER, 2)
            .addTo(quantumForceTransformerRecipes);
        // Enriched Naquadah (UIV)
        GTValues.RA.stdBuilder()
            .itemInputs(
                enrichedNaquadahEarth.get(OrePrefixes.dust, 32),
                Materials.Zinc.getDust(64),
                Materials.Carbon.getDust(1),
                GTUtility.copyAmount(0, GenericChem.mSimpleNaquadahCatalyst))
            .itemOutputs(inertEnrichedNaquadah.get(OrePrefixes.dust, 1), Materials.Trinium.getDust(64))
            .fluidInputs(Materials.SulfuricAcid.getFluid(16000), Materials.Oxygen.getGas(100L))
            .fluidOutputs(wasteLiquid.getFluidOrGas(32000))
            .duration(10 * SECONDS)
            .eut(GTValues.VP[11])
            .metadata(QFT_FOCUS_TIER, 2)
            .addTo(quantumForceTransformerRecipes);
        // Naquadria (UMV)
        GTValues.RA.stdBuilder()
            .itemInputs(
                naquadriaEarth.get(OrePrefixes.dust, 32),
                Materials.Magnesium.getDust(64),
                GTUtility.copyAmount(0, GenericChem.mAdvancedNaquadahCatalyst))
            .itemOutputs(
                inertNaquadria.get(OrePrefixes.dust, 1),
                Materials.Barium.getDust(64),
                Materials.Indium.getDust(64),
                ItemList.NaquadriaSupersolid.get(1))
            .fluidInputs(
                Materials.PhosphoricAcid.getFluid(16000),
                Materials.SulfuricAcid.getFluid(16000),
                Materials.Oxygen.getGas(100L))
            .duration(5 * SECONDS)
            .eut(GTValues.VP[12])
            .metadata(QFT_FOCUS_TIER, 3)
            .addTo(quantumForceTransformerRecipes);

        // Activate Them

        GTValues.RA.stdBuilder()
            .itemInputs(inertNaquadah.get(OrePrefixes.dust, 64), inertNaquadah.get(OrePrefixes.dust, 32))
            .itemOutputs(Materials.Nickel.getDust(16))
            .fluidInputs(Materials.Nickel.getPlasma(144 * 16))
            .fluidOutputs(Materials.Naquadah.getMolten(144 * 9216))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(0)
            .metadata(NKE_RANGE, computeRangeNKE(600, 500))
            .noOptimize()
            .addTo(neutronActivatorRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                inertEnrichedNaquadah.get(OrePrefixes.dust, 64),
                inertEnrichedNaquadah.get(OrePrefixes.dust, 32))
            .itemOutputs(Materials.Titanium.getDust(16))
            .fluidInputs(Materials.Titanium.getPlasma(16 * 144))
            .fluidOutputs(Materials.NaquadahEnriched.getMolten(144 * 9216))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(0)
            .metadata(NKE_RANGE, computeRangeNKE(900, 850))
            .noOptimize()
            .addTo(neutronActivatorRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(inertNaquadria.get(OrePrefixes.dust, 64), inertNaquadria.get(OrePrefixes.dust, 32))
            .itemOutputs(Materials.Americium.getDust(16))
            .fluidInputs(Materials.Americium.getPlasma(144 * 16))
            .fluidOutputs(Materials.Naquadria.getMolten(144 * 9216))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(0)
            .metadata(NKE_RANGE, computeRangeNKE(1100, 1080))
            .noOptimize()
            .addTo(neutronActivatorRecipes);

        // Fix shit
        GTValues.RA.stdBuilder()
            .itemInputs(lowQualityNaquadriaSolution.get(OrePrefixes.cell, 1))
            .itemOutputs(Materials.Tin.getDust(2))
            .duration(16 * SECONDS + 14 * TICKS)
            .eut(4)
            .addTo(maceratorRecipes);

        // Naquadah Rework Line
        GTValues.RA.stdBuilder()
            .itemInputs(naquadahEarth.get(OrePrefixes.dust, 2), GTUtility.getIntegratedCircuit(1))
            .fluidInputs(fluoroantimonicAcid.getFluidOrGas(3000))
            .fluidOutputs(lowQualityNaquadahEmulsion.getFluidOrGas(2000))
            .itemOutputs(titaniumTrifluoride.get(OrePrefixes.dust, 4))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(COIL_HEAT, 3000)
            .addTo(blastFurnaceRecipes);

        // TiF3 + 3H = Ti + 3HF
        GTValues.RA.stdBuilder()
            .itemInputs(titaniumTrifluoride.get(OrePrefixes.dust, 4), GTUtility.getIntegratedCircuit(1))
            .fluidInputs(Materials.Hydrogen.getGas(3000))
            .fluidOutputs(Materials.HydrofluoricAcid.getFluid(3000))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.ingotHot, Materials.Titanium, 1))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .metadata(COIL_HEAT, 2000)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.copyAmount(0, GTOreDictUnificator.get(OrePrefixes.plate, Materials.Copper, 1)),
                Materials.Hydrogen.getCells(8))
            .fluidInputs(FluidRegistry.getFluidStack("seedoil", 3000))
            .itemOutputs(ItemList.Cell_Empty.get(8))
            .fluidOutputs(towEthyl1Hexanol.getFluidOrGas(1000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        // 2C8H18O + H3PO4 =Na,C2H6O= C16H35O3P + 2H2O
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 2))
            .fluidInputs(
                towEthyl1Hexanol.getFluidOrGas(2000),
                Materials.PhosphoricAcid.getFluid(1000),
                Materials.Ethanol.getFluid(2000))
            .fluidOutputs(P507.getFluidOrGas(1000))
            .duration(1 * MINUTES)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(lowQualityNaquadahSolution.get(OrePrefixes.cell, 36), P507.get(OrePrefixes.cell, 4))
            .itemOutputs(naquadahAdamantiumSolution.get(OrePrefixes.cell, 30), ItemList.Cell_Empty.get(10))
            .fluidOutputs(fluorineRichWasteLiquid.getFluidOrGas(10000))
            .duration(3 * MINUTES + 20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(P507.getFluidOrGas(4000), lowQualityNaquadahSolution.getFluidOrGas(36000))
            .fluidOutputs(fluorineRichWasteLiquid.getFluidOrGas(10000), naquadahAdamantiumSolution.getFluidOrGas(30000))
            .duration(3 * MINUTES + 20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Quicklime, 40))
            .fluidInputs(fluorineRichWasteLiquid.getFluidOrGas(1500))
            .fluidOutputs(wasteLiquid.getFluidOrGas(1000))
            .itemOutputs(WerkstoffLoader.Fluorspar.get(OrePrefixes.dust, 60))
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        CrackRecipeAdder.addUniversalDistillationRecipe(
            wasteLiquid.getFluidOrGas(10000),
            new FluidStack[] { Materials.SaltWater.getFluid(3000), FluidRegistry.getFluidStack("phenol", 2000),
                Materials.HydrochloricAcid.getFluid(5000) },
            GTOreDictUnificator.get(OrePrefixes.dust, Materials.Chrome, 3),
            15 * SECONDS,
            TierEU.RECIPE_HV);

        GTValues.RA.stdBuilder()
            .itemOutputs(
                adamantine.get(OrePrefixes.dust, 4),
                naquadahEarth.get(OrePrefixes.dust, 2),
                concentratedEnrichedNaquadahSludge.get(OrePrefixes.dust, 1))
            .fluidInputs(naquadahAdamantiumSolution.getFluidOrGas(3000))
            .fluidOutputs(naquadahRichSolution.getFluidOrGas(2000))
            .duration(5 * SECONDS)
            .eut(0)
            .metadata(NKE_RANGE, computeRangeNKE(230, 200))
            .noOptimize()
            .addTo(neutronActivatorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.SodiumHydroxide, 27))
            .fluidInputs(naquadahRichSolution.getFluidOrGas(5000))
            .itemOutputs(naquadahine.get(OrePrefixes.dust, 30))
            .fluidOutputs(P507.getFluidOrGas(1000))
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(autoclaveRecipes);

        // NqO2 + C = CO2 + Nq
        GTValues.RA.stdBuilder()
            .itemInputs(
                naquadahine.get(OrePrefixes.dust, 3),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 1))
            .fluidOutputs(Materials.CarbonDioxide.getGas(1000))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.ingotHot, Materials.Naquadah, 1))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .metadata(COIL_HEAT, 5000)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.SodiumHydroxide, 27),
                GTUtility.getIntegratedCircuit(1))
            .fluidInputs(lowQualityNaquadahEmulsion.getFluidOrGas(10000))
            .itemOutputs(
                galliumHydroxide.get(OrePrefixes.dust, 64),
                galliumHydroxide.get(OrePrefixes.dust, 48),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Antimony, 15))
            .outputChances(6250, 6250, 10000)
            .fluidOutputs(lowQualityNaquadahSolution.getFluidOrGas(9000))
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(enrichedNaquadahEarth.get(OrePrefixes.dust, 4))
            .fluidInputs(P507.getFluidOrGas(1000), Materials.SulfuricAcid.getFluid(18000))
            .fluidOutputs(enrichedNaquadahRichSolution.getFluidOrGas(4000), wasteLiquid.getFluidOrGas(1000))
            .itemOutputs(naquadahEarth.get(OrePrefixes.dust, 1), triniumSulphate.get(OrePrefixes.dust, 1))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        // ZnSO4 + 2H = H2SO4 + Zn
        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.ZincSulfate.get(OrePrefixes.dust, 6))
            .fluidInputs(Materials.Hydrogen.getGas(2000))
            .fluidOutputs(Materials.SulfuricAcid.getFluid(1000))
            .itemOutputs(Materials.Zinc.getDust(1))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.SodiumHydroxide, 60))
            .fluidInputs(enrichedNaquadahRichSolution.getFluidOrGas(10000))
            .itemOutputs(concentratedEnrichedNaquadahSludge.get(OrePrefixes.dust, 8))
            .fluidOutputs(P507.getFluidOrGas(2500))
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(concentratedEnrichedNaquadahSludge.get(OrePrefixes.dust, 16))
            .itemOutputs(
                enrichedNaquadahSulphate.get(OrePrefixes.dust, 64),
                enrichedNaquadahSulphate.get(OrePrefixes.dust, 64),
                enrichedNaquadahSulphate.get(OrePrefixes.dust, 37),
                WerkstoffLoader.Sodiumsulfate.get(OrePrefixes.dust, 64),
                WerkstoffLoader.Sodiumsulfate.get(OrePrefixes.dust, 64),
                WerkstoffLoader.Sodiumsulfate.get(OrePrefixes.dust, 12),
                lowQualityNaquadriaSulphate.get(OrePrefixes.dust, 2))
            .duration(6 * SECONDS)
            .eut(0)
            .metadata(NKE_RANGE, computeRangeNKE(480, 460))
            .noOptimize()
            .addTo(neutronActivatorRecipes);

        // Nq+(SO4)2 + 2Zn = Nq+ + 2ZnSO4
        GTValues.RA.stdBuilder()
            .itemInputs(
                enrichedNaquadahSulphate.get(OrePrefixes.dust, 11),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Zinc, 2))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.ingotHot, Materials.NaquadahEnriched, 1),
                WerkstoffLoader.ZincSulfate.get(OrePrefixes.dust, 12))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .metadata(COIL_HEAT, 7500)
            .addTo(blastFurnaceRecipes);

        // KeSO4 + 2H = Ke + H2SO4
        GTValues.RA.stdBuilder()
            .itemInputs(triniumSulphate.get(OrePrefixes.dust, 6))
            .fluidInputs(Materials.Hydrogen.getGas(2000))
            .fluidOutputs(Materials.SulfuricAcid.getFluid(1000))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Trinium, 1))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(naquadriaEarth.get(OrePrefixes.dust, 4))
            .fluidInputs(Materials.PhosphoricAcid.getFluid(4000))
            .itemOutputs(
                indiumPhosphate.get(OrePrefixes.dust, 6),
                lowQualityNaquadriaPhosphate.get(OrePrefixes.dust, 4))
            .outputChances(2000, 10000)
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(centrifugeRecipes);

        // Ga(OH)3 + 3Na = Ga + 3NaOH
        GTValues.RA.stdBuilder()
            .itemInputs(
                galliumHydroxide.get(OrePrefixes.dust, 7),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 3))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Gallium, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.SodiumHydroxide, 9))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // 2InPO4 + 3Ca = 2In + Ca3(PO4)2
        GTValues.RA.stdBuilder()
            .itemInputs(
                indiumPhosphate.get(OrePrefixes.dust, 12),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Calcium, 3))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Indium, 2),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.TricalciumPhosphate, 5))
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(lowQualityNaquadriaPhosphate.get(OrePrefixes.dust, 10), Materials.SulfuricAcid.getCells(30))
            .fluidOutputs(naquadriaRichSolution.getFluidOrGas(9000))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Barite, 1), ItemList.Cell_Empty.get(30))
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemOutputs(
                naquadriaSulphate.get(OrePrefixes.dust, 44),
                lowQualityNaquadriaSulphate.get(OrePrefixes.dust, 6))
            .fluidInputs(naquadriaRichSolution.getFluidOrGas(9000))
            .duration(5 * SECONDS)
            .eut(0)
            .metadata(NKE_RANGE, computeRangeNKE(1100, 1050))
            .noOptimize()
            .addTo(neutronActivatorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(lowQualityNaquadriaSulphate.get(OrePrefixes.dust, 3), Materials.Water.getCells(3))
            .fluidInputs(P507.getFluidOrGas(500))
            .fluidOutputs(lowQualityNaquadriaSolution.getFluidOrGas(3500))
            .itemOutputs(ItemList.Cell_Empty.get(3))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(UniversalChemical);

        CrackRecipeAdder.addUniversalDistillationRecipe(
            lowQualityNaquadriaSolution.getFluidOrGas(7000),
            new FluidStack[] { P507.getFluidOrGas(1000), naquadriaRichSolution.getFluidOrGas(5400),
                Materials.DilutedSulfuricAcid.getFluid(12000) },
            enrichedNaquadahEarth.get(OrePrefixes.dust, 2),
            25 * SECONDS,
            TierEU.RECIPE_IV);

        // Nq*(SO4)2 + 2Mg = Nq* + 2MgSO4
        GTValues.RA.stdBuilder()
            .itemInputs(
                naquadriaSulphate.get(OrePrefixes.dust, 11),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Magnesium, 2))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.ingotHot, Materials.Naquadria, 1),
                magnesiumSulphate.get(OrePrefixes.dust, 12))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .metadata(COIL_HEAT, 9100)
            .addTo(blastFurnaceRecipes);

        // InPO4 + Ga(OH)3 = InGaP
        GTValues.RA.stdBuilder()
            .itemInputs(
                indiumPhosphate.get(OrePrefixes.dust, 6),
                galliumHydroxide.get(OrePrefixes.dust, 7),
                GTUtility.getIntegratedCircuit(2))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.IndiumGalliumPhosphide, 3))
            .duration(15 * TICKS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Ball.get(0))
            .fluidInputs(naquadahGoo.getFluidOrGas(72))
            .itemOutputs(ItemRefer.Naquadah_Mass.get(1))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Ball.get(0))
            .fluidInputs(enrichedNaquadahGoo.getFluidOrGas(72))
            .itemOutputs(ItemRefer.Enriched_Naquadah_Mass.get(1))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Ball.get(0))
            .fluidInputs(naquadriaGoo.getFluidOrGas(72))
            .itemOutputs(ItemRefer.Naquadria_Mass.get(1))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.Naquadah_Mass.get(1))
            .itemOutputs(naquadahEarth.get(OrePrefixes.dust, 1), enrichedNaquadahEarth.get(OrePrefixes.dust, 1))
            .outputChances(10000, 100)
            .duration(5 * SECONDS)
            .eut(2)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.Enriched_Naquadah_Mass.get(1))
            .itemOutputs(enrichedNaquadahEarth.get(OrePrefixes.dust, 1), naquadriaEarth.get(OrePrefixes.dust, 1))
            .outputChances(10000, 100)
            .duration(5 * SECONDS)
            .eut(2)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.Naquadria_Mass.get(1))
            .itemOutputs(naquadriaEarth.get(OrePrefixes.dust, 1), naquadriaEarth.get(OrePrefixes.dust, 1))
            .outputChances(10000, 100)
            .duration(5 * SECONDS)
            .eut(2)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.block, Materials.SiliconSG, 16),
                naquadahine.get(OrePrefixes.dust, 3),
                ItemList.GalliumArsenideCrystal.get(1L))
            .fluidInputs(Materials.Argon.getGas(8000))
            .itemOutputs(ItemList.Circuit_Silicon_Ingot3.get(1))
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .metadata(COIL_HEAT, 4484)
            .addTo(blastFurnaceRecipes);

        // NqO2 + 4Na = 2Na2O + Nq
        GTValues.RA.stdBuilder()
            .itemInputs(naquadahine.get(OrePrefixes.dust, 3), Materials.Sodium.getDust(4))
            .itemOutputs(Materials.Naquadah.getDust(1), Materials.SodiumOxide.getDust(6))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(naquadahEarth.get(OrePrefixes.dust, 2), GTUtility.getIntegratedCircuit(2))
            .fluidInputs(Materials.Nitrogen.getGas(1000))
            .itemOutputs(Materials.Naquadah.getNuggets(1))
            .duration(2 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .metadata(COIL_HEAT, 5000)
            .addTo(blastFurnaceRecipes);

        // C2H4 + H2O(g) = C2H6O
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(24))
            .fluidInputs(Materials.Ethylene.getGas(1000), FluidRegistry.getFluidStack("steam", 2000))
            .fluidOutputs(Materials.Ethanol.getFluid(1000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Ethylene.getCells(1), GTUtility.getIntegratedCircuit(24))
            .fluidInputs(FluidRegistry.getFluidStack("steam", 2000))
            .itemOutputs(Materials.Ethanol.getCells(1))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.ingotHot, Materials.Naquadah, 1))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Naquadah, 1))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(vacuumFreezerRecipes);
    }

    public static void SmallRecipeChange() {

        GTRecipe tRecipe;

        tRecipe = RecipeMaps.chemicalReactorRecipes.findRecipe(
            null,
            false,
            1 << 30,
            new FluidStack[] { Materials.SulfuricAcid.getFluid(500) },
            GTOreDictUnificator.get(OrePrefixes.foil, Materials.Platinum, 16),
            ItemList.Empty_Board_Elite.get(2));
        if (tRecipe != null) {
            RecipeMaps.chemicalReactorRecipes.getBackend()
                .removeRecipe(tRecipe);
            GTRecipe tRecipe2 = tRecipe.copy();
            tRecipe2.mInputs = new ItemStack[] { GTOreDictUnificator.get(OrePrefixes.foil, Materials.Naquadah, 8),
                ItemList.Empty_Board_Elite.get(2) };
            RecipeMaps.chemicalReactorRecipes.add(tRecipe2);
            RecipeMaps.chemicalReactorRecipes.getBackend()
                .reInit();
        }

        tRecipe = RecipeMaps.multiblockChemicalReactorRecipes.findRecipe(
            null,
            false,
            1 << 30,
            new FluidStack[] { Materials.SulfuricAcid.getFluid(500) },
            GTOreDictUnificator.get(OrePrefixes.foil, Materials.Platinum, 16),
            ItemList.Empty_Board_Elite.get(2));
        if (tRecipe != null) {
            RecipeMaps.multiblockChemicalReactorRecipes.getBackend()
                .removeRecipe(tRecipe);
            GTRecipe tRecipe2 = tRecipe.copy();
            tRecipe2.mInputs = new ItemStack[] { GTOreDictUnificator.get(OrePrefixes.foil, Materials.Naquadah, 8),
                ItemList.Empty_Board_Elite.get(2) };
            RecipeMaps.multiblockChemicalReactorRecipes.add(tRecipe2);
            RecipeMaps.multiblockChemicalReactorRecipes.getBackend()
                .reInit();
        }

        tRecipe = RecipeMaps.assemblerRecipes.findRecipe(
            null,
            false,
            1 << 30,
            new FluidStack[] { Materials.Polybenzimidazole.getMolten(36) },
            GTOreDictUnificator.get(OrePrefixes.foil, Materials.Polybenzimidazole, 2),
            GTOreDictUnificator.get(OrePrefixes.foil, Materials.HSSS, 1),
            GTUtility.getIntegratedCircuit(1));
        if (tRecipe != null) {
            RecipeMaps.assemblerRecipes.getBackend()
                .removeRecipe(tRecipe);
            GTRecipe tRecipe2 = tRecipe.copy();
            tRecipe2.mInputs = new ItemStack[] {
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.Polybenzimidazole, 2),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.NaquadahEnriched, 1) };
            RecipeMaps.assemblerRecipes.add(tRecipe2);
            RecipeMaps.assemblerRecipes.getBackend()
                .reInit();
        }
    }

    public static void Remover() {

        if (!EnableNaquadahRework) return;

        GTLog.out.print(GoodGenerator.MOD_ID + ": Begin to remove pure Naquadah, Enriched Naquadah and Naquadria.\n");

        HashSet<GTRecipe> remove = new HashSet<>(5000);
        HashSet<GTRecipe> reAdd = new HashSet<>(5000);

        // For Crusher
        for (GTRecipe recipe : RecipeMaps.maceratorRecipes.getAllRecipes()) {
            ItemStack input = recipe.mInputs[0];
            if (GTUtility.isStackValid(input)) {
                int[] oreDict = OreDictionary.getOreIDs(input);
                for (int oreDictID : oreDict) {
                    if ((OreDictionary.getOreName(oreDictID)
                        .startsWith("ore")
                        || OreDictionary.getOreName(oreDictID)
                            .startsWith("rawOre")
                        || OreDictionary.getOreName(oreDictID)
                            .startsWith("crushed"))
                        && OreDictionary.getOreName(oreDictID)
                            .contains("Naq")) {
                        GTRecipe tRecipe = recipe.copy();
                        boolean modified = false;
                        for (int i = 0; i < tRecipe.mOutputs.length; i++) {
                            if (!GTUtility.isStackValid(tRecipe.mOutputs[i])) continue;
                            if (tRecipe.mOutputs[i].isItemEqual(Materials.Naquadah.getDust(1))) {
                                tRecipe.mOutputs[i] = GTUtility.copyAmount(
                                    tRecipe.mOutputs[i].stackSize * 2,
                                    naquadahEarth.get(OrePrefixes.dust, 1));
                                modified = true;
                            } else if (tRecipe.mOutputs[i].isItemEqual(Materials.NaquadahEnriched.getDust(1))) {
                                tRecipe.mOutputs[i] = GTUtility.copyAmount(
                                    tRecipe.mOutputs[i].stackSize * 2,
                                    enrichedNaquadahEarth.get(OrePrefixes.dust, 1));
                                modified = true;
                            } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Naquadria.getDust(1))) {
                                tRecipe.mOutputs[i] = GTUtility.copyAmount(
                                    tRecipe.mOutputs[i].stackSize * 2,
                                    naquadriaEarth.get(OrePrefixes.dust, 1));
                                modified = true;
                            }
                        }
                        if (modified) {
                            reAdd.add(tRecipe);
                            remove.add(recipe);
                        }
                        break;
                    }
                }
            }
        }
        RecipeMaps.maceratorRecipes.getBackend()
            .removeRecipes(remove);
        reAdd.forEach(RecipeMaps.maceratorRecipes::add);
        RecipeMaps.maceratorRecipes.getBackend()
            .reInit();

        GTLog.out.print(GoodGenerator.MOD_ID + ": Replace " + remove.size() + "! ");

        remove.clear();
        reAdd.clear();

        GTLog.out.print("Crusher done!\n");

        // For Washer
        for (GTRecipe recipe : RecipeMaps.oreWasherRecipes.getAllRecipes()) {
            ItemStack input = recipe.mInputs[0];
            if (GTUtility.isStackValid(input)) {
                int[] oreDict = OreDictionary.getOreIDs(input);
                for (int oreDictID : oreDict) {
                    if (OreDictionary.getOreName(oreDictID)
                        .startsWith("crushed")
                        && OreDictionary.getOreName(oreDictID)
                            .contains("Naq")) {
                        GTRecipe tRecipe = recipe.copy();
                        boolean modified = false;
                        for (int i = 0; i < tRecipe.mOutputs.length; i++) {
                            if (!GTUtility.isStackValid(tRecipe.mOutputs[i])) continue;
                            if (tRecipe.mOutputs[i].isItemEqual(Materials.Naquadah.getDust(1))) {
                                tRecipe.mOutputs[i] = GTUtility.copyAmount(
                                    tRecipe.mOutputs[i].stackSize * 2,
                                    naquadahEarth.get(OrePrefixes.dust, 1));
                                modified = true;
                            } else if (tRecipe.mOutputs[i].isItemEqual(Materials.NaquadahEnriched.getDust(1))) {
                                tRecipe.mOutputs[i] = GTUtility.copyAmount(
                                    tRecipe.mOutputs[i].stackSize * 2,
                                    enrichedNaquadahEarth.get(OrePrefixes.dust, 1));
                                modified = true;
                            } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Naquadria.getDust(1))) {
                                tRecipe.mOutputs[i] = GTUtility.copyAmount(
                                    tRecipe.mOutputs[i].stackSize * 2,
                                    naquadriaEarth.get(OrePrefixes.dust, 1));
                                modified = true;
                            }
                        }
                        if (modified) {
                            reAdd.add(tRecipe);
                            remove.add(recipe);
                        }
                        break;
                    }
                }
            }
        }
        RecipeMaps.oreWasherRecipes.getBackend()
            .removeRecipes(remove);
        reAdd.forEach(RecipeMaps.oreWasherRecipes::add);
        RecipeMaps.oreWasherRecipes.getBackend()
            .reInit();

        GTLog.out.print(GoodGenerator.MOD_ID + ": Replace " + remove.size() + "! ");

        remove.clear();
        reAdd.clear();

        GTLog.out.print("Washer done!\n");

        // For Thermal Centrifuge
        for (GTRecipe recipe : RecipeMaps.thermalCentrifugeRecipes.getAllRecipes()) {
            ItemStack input = recipe.mInputs[0];
            if (GTUtility.isStackValid(input)) {
                int[] oreDict = OreDictionary.getOreIDs(input);
                for (int oreDictID : oreDict) {
                    if (OreDictionary.getOreName(oreDictID)
                        .startsWith("crushed")
                        && OreDictionary.getOreName(oreDictID)
                            .contains("Naq")) {
                        GTRecipe tRecipe = recipe.copy();
                        boolean modified = false;
                        for (int i = 0; i < tRecipe.mOutputs.length; i++) {
                            if (!GTUtility.isStackValid(tRecipe.mOutputs[i])) continue;
                            if (tRecipe.mOutputs[i].isItemEqual(Materials.Naquadah.getDust(1))) {
                                tRecipe.mOutputs[i] = GTUtility.copyAmount(
                                    tRecipe.mOutputs[i].stackSize * 2,
                                    naquadahEarth.get(OrePrefixes.dust, 1));
                                modified = true;
                            } else if (tRecipe.mOutputs[i].isItemEqual(Materials.NaquadahEnriched.getDust(1))) {
                                tRecipe.mOutputs[i] = GTUtility.copyAmount(
                                    tRecipe.mOutputs[i].stackSize * 2,
                                    enrichedNaquadahEarth.get(OrePrefixes.dust, 1));
                                modified = true;
                            } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Naquadria.getDust(1))) {
                                tRecipe.mOutputs[i] = GTUtility.copyAmount(
                                    tRecipe.mOutputs[i].stackSize * 2,
                                    naquadriaEarth.get(OrePrefixes.dust, 1));
                                modified = true;
                            }
                        }
                        if (modified) {
                            reAdd.add(tRecipe);
                            remove.add(recipe);
                        }
                        break;
                    }
                }
            }
        }
        RecipeMaps.thermalCentrifugeRecipes.getBackend()
            .removeRecipes(remove);
        reAdd.forEach(RecipeMaps.thermalCentrifugeRecipes::add);
        RecipeMaps.thermalCentrifugeRecipes.getBackend()
            .reInit();

        GTLog.out.print(GoodGenerator.MOD_ID + ": Replace " + remove.size() + "! ");

        remove.clear();
        reAdd.clear();

        GTLog.out.print("Thermal Centrifuge done!\n");
        final boolean checkCombs = Mods.Forestry.isModLoaded();
        // For Centrifuge
        for (GTRecipe recipe : RecipeMaps.centrifugeRecipes.getAllRecipes()) {
            ItemStack input = null;
            if (recipe.mInputs.length > 0) input = recipe.mInputs[0];
            if (GTUtility.isStackValid(input)) {
                int[] oreDict = OreDictionary.getOreIDs(input);
                if (checkCombs && input.isItemEqual(GTBees.combs.getStackForType(CombType.DOB))) {
                    GTRecipe tRecipe = recipe.copy();
                    boolean modified = false;
                    for (int i = 0; i < tRecipe.mOutputs.length; i++) {
                        if (!GTUtility.isStackValid(tRecipe.mOutputs[i])) continue;
                        if (tRecipe.mOutputs[i].isItemEqual(Materials.Naquadah.getDustTiny(1))) {
                            tRecipe.mOutputs[i] = GTUtility.copyAmount(
                                tRecipe.mOutputs[i].stackSize * 2L,
                                naquadahEarth.get(OrePrefixes.dustTiny, 1));
                            modified = true;
                        }
                    }
                    if (modified) {
                        reAdd.add(tRecipe);
                        remove.add(recipe);
                    }
                } else for (int oreDictID : oreDict) {
                    if (OreDictionary.getOreName(oreDictID)
                        .startsWith("dustPureNaq")
                        || OreDictionary.getOreName(oreDictID)
                            .startsWith("dustImpureNaq")
                        || OreDictionary.getOreName(oreDictID)
                            .startsWith("dustSpace")
                        || OreDictionary.getOreName(oreDictID)
                            .startsWith("dustNaq")) {
                        GTRecipe tRecipe = recipe.copy();
                        boolean modified = false;
                        for (int i = 0; i < tRecipe.mOutputs.length; i++) {
                            if (!GTUtility.isStackValid(tRecipe.mOutputs[i])) continue;
                            if (tRecipe.mOutputs[i].isItemEqual(Materials.Naquadah.getDustTiny(1))) {
                                tRecipe.mOutputs[i] = GTUtility.copyAmount(
                                    tRecipe.mOutputs[i].stackSize * 2,
                                    naquadahEarth.get(OrePrefixes.dustTiny, 1));
                                modified = true;
                            } else if (tRecipe.mOutputs[i].isItemEqual(Materials.NaquadahEnriched.getDustTiny(1))) {
                                tRecipe.mOutputs[i] = GTUtility.copyAmount(
                                    tRecipe.mOutputs[i].stackSize * 2,
                                    enrichedNaquadahEarth.get(OrePrefixes.dustTiny, 1));
                                modified = true;
                            } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Naquadria.getDustTiny(1))) {
                                tRecipe.mOutputs[i] = GTUtility.copyAmount(
                                    tRecipe.mOutputs[i].stackSize * 2,
                                    naquadriaEarth.get(OrePrefixes.dustTiny, 1));
                                modified = true;
                            } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Naquadah.getDust(1))) {
                                tRecipe.mOutputs[i] = GTUtility.copyAmount(
                                    tRecipe.mOutputs[i].stackSize * 2,
                                    naquadahEarth.get(OrePrefixes.dust, 1));
                                modified = true;
                            } else if (tRecipe.mOutputs[i].isItemEqual(Materials.NaquadahEnriched.getDust(1))) {
                                tRecipe.mOutputs[i] = GTUtility.copyAmount(
                                    tRecipe.mOutputs[i].stackSize * 2,
                                    enrichedNaquadahEarth.get(OrePrefixes.dust, 1));
                                modified = true;
                            } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Naquadria.getDust(1))) {
                                tRecipe.mOutputs[i] = GTUtility.copyAmount(
                                    tRecipe.mOutputs[i].stackSize * 2,
                                    naquadriaEarth.get(OrePrefixes.dust, 1));
                                modified = true;
                            } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Naquadah.getDustSmall(1))) {
                                tRecipe.mOutputs[i] = GTUtility.copyAmount(
                                    tRecipe.mOutputs[i].stackSize * 2,
                                    naquadahEarth.get(OrePrefixes.dustSmall, 1));
                                modified = true;
                            } else if (tRecipe.mOutputs[i].isItemEqual(Materials.NaquadahEnriched.getDustSmall(1))) {
                                tRecipe.mOutputs[i] = GTUtility.copyAmount(
                                    tRecipe.mOutputs[i].stackSize * 2,
                                    enrichedNaquadahEarth.get(OrePrefixes.dustSmall, 1));
                                modified = true;
                            } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Naquadria.getDustSmall(1))) {
                                tRecipe.mOutputs[i] = GTUtility.copyAmount(
                                    tRecipe.mOutputs[i].stackSize * 2,
                                    naquadriaEarth.get(OrePrefixes.dustSmall, 1));
                                modified = true;
                            }
                        }
                        if (modified) {
                            reAdd.add(tRecipe);
                            remove.add(recipe);
                        }
                        break;
                    }
                }
            }
        }
        RecipeMaps.centrifugeRecipes.getBackend()
            .removeRecipes(remove);
        reAdd.forEach(RecipeMaps.centrifugeRecipes::add);
        RecipeMaps.centrifugeRecipes.getBackend()
            .reInit();

        GTLog.out.print(GoodGenerator.MOD_ID + ": Replace " + remove.size() + "! ");

        remove.clear();
        reAdd.clear();

        GTLog.out.print("Centrifuge done!\n");

        // For Centrifuge (PA)
        for (GTRecipe recipe : RecipeMaps.centrifugeNonCellRecipes.getAllRecipes()) {
            ItemStack input = null;
            if (recipe.mInputs.length > 0) input = recipe.mInputs[0];
            if (GTUtility.isStackValid(input)) {
                int[] oreDict = OreDictionary.getOreIDs(input);
                if (checkCombs && input.isItemEqual(GTBees.combs.getStackForType(CombType.DOB))) {
                    GTRecipe tRecipe = recipe.copy();
                    boolean modified = false;
                    for (int i = 0; i < tRecipe.mOutputs.length; i++) {
                        if (!GTUtility.isStackValid(tRecipe.mOutputs[i])) continue;
                        if (tRecipe.mOutputs[i].isItemEqual(Materials.Naquadah.getDustTiny(1))) {
                            tRecipe.mOutputs[i] = GTUtility.copyAmount(
                                tRecipe.mOutputs[i].stackSize * 2L,
                                naquadahEarth.get(OrePrefixes.dustTiny, 1));
                            modified = true;
                        }
                    }
                    if (modified) {
                        reAdd.add(tRecipe);
                        remove.add(recipe);
                    }
                } else for (int oreDictID : oreDict) {
                    if (OreDictionary.getOreName(oreDictID)
                        .startsWith("dustPureNaq")
                        || OreDictionary.getOreName(oreDictID)
                            .startsWith("dustImpureNaq")
                        || OreDictionary.getOreName(oreDictID)
                            .startsWith("dustSpace")
                        || OreDictionary.getOreName(oreDictID)
                            .startsWith("dustNaq")) {
                        GTRecipe tRecipe = recipe.copy();
                        boolean modified = false;
                        for (int i = 0; i < tRecipe.mOutputs.length; i++) {
                            if (!GTUtility.isStackValid(tRecipe.mOutputs[i])) continue;
                            if (tRecipe.mOutputs[i].isItemEqual(Materials.Naquadah.getDustTiny(1))) {
                                tRecipe.mOutputs[i] = GTUtility.copyAmount(
                                    tRecipe.mOutputs[i].stackSize * 2,
                                    naquadahEarth.get(OrePrefixes.dustTiny, 1));
                                modified = true;
                            } else if (tRecipe.mOutputs[i].isItemEqual(Materials.NaquadahEnriched.getDustTiny(1))) {
                                tRecipe.mOutputs[i] = GTUtility.copyAmount(
                                    tRecipe.mOutputs[i].stackSize * 2,
                                    enrichedNaquadahEarth.get(OrePrefixes.dustTiny, 1));
                                modified = true;
                            } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Naquadria.getDustTiny(1))) {
                                tRecipe.mOutputs[i] = GTUtility.copyAmount(
                                    tRecipe.mOutputs[i].stackSize * 2,
                                    naquadriaEarth.get(OrePrefixes.dustTiny, 1));
                                modified = true;
                            } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Naquadah.getDust(1))) {
                                tRecipe.mOutputs[i] = GTUtility.copyAmount(
                                    tRecipe.mOutputs[i].stackSize * 2,
                                    naquadahEarth.get(OrePrefixes.dust, 1));
                                modified = true;
                            } else if (tRecipe.mOutputs[i].isItemEqual(Materials.NaquadahEnriched.getDust(1))) {
                                tRecipe.mOutputs[i] = GTUtility.copyAmount(
                                    tRecipe.mOutputs[i].stackSize * 2,
                                    enrichedNaquadahEarth.get(OrePrefixes.dust, 1));
                                modified = true;
                            } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Naquadria.getDust(1))) {
                                tRecipe.mOutputs[i] = GTUtility.copyAmount(
                                    tRecipe.mOutputs[i].stackSize * 2,
                                    naquadriaEarth.get(OrePrefixes.dust, 1));
                                modified = true;
                            } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Naquadah.getDustSmall(1))) {
                                tRecipe.mOutputs[i] = GTUtility.copyAmount(
                                    tRecipe.mOutputs[i].stackSize * 2,
                                    naquadahEarth.get(OrePrefixes.dustSmall, 1));
                                modified = true;
                            } else if (tRecipe.mOutputs[i].isItemEqual(Materials.NaquadahEnriched.getDustSmall(1))) {
                                tRecipe.mOutputs[i] = GTUtility.copyAmount(
                                    tRecipe.mOutputs[i].stackSize * 2,
                                    enrichedNaquadahEarth.get(OrePrefixes.dustSmall, 1));
                                modified = true;
                            } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Naquadria.getDustSmall(1))) {
                                tRecipe.mOutputs[i] = GTUtility.copyAmount(
                                    tRecipe.mOutputs[i].stackSize * 2,
                                    naquadriaEarth.get(OrePrefixes.dustSmall, 1));
                                modified = true;
                            }
                        }
                        if (modified) {
                            reAdd.add(tRecipe);
                            remove.add(recipe);
                        }
                        break;
                    }
                }
            }
        }
        RecipeMaps.centrifugeNonCellRecipes.getBackend()
            .removeRecipes(remove);
        reAdd.forEach(RecipeMaps.centrifugeNonCellRecipes::add);
        RecipeMaps.centrifugeNonCellRecipes.getBackend()
            .reInit();

        GTLog.out.print(GoodGenerator.MOD_ID + ": Replace " + remove.size() + "! ");

        remove.clear();
        reAdd.clear();

        GTLog.out.print("Centrifuge (PA) done!\n");

        // For Hammer
        for (GTRecipe recipe : RecipeMaps.hammerRecipes.getAllRecipes()) {
            ItemStack input = recipe.mInputs[0];
            if (GTUtility.isStackValid(input)) {
                int[] oreDict = OreDictionary.getOreIDs(input);
                for (int oreDictID : oreDict) {
                    if (OreDictionary.getOreName(oreDictID)
                        .startsWith("crushed")
                        && OreDictionary.getOreName(oreDictID)
                            .contains("Naq")) {
                        GTRecipe tRecipe = recipe.copy();
                        boolean modified = false;
                        for (int i = 0; i < tRecipe.mOutputs.length; i++) {
                            if (!GTUtility.isStackValid(tRecipe.mOutputs[i])) continue;
                            if (tRecipe.mOutputs[i].isItemEqual(Materials.Naquadah.getDust(1))) {
                                tRecipe.mOutputs[i] = GTUtility.copyAmount(
                                    tRecipe.mOutputs[i].stackSize * 2,
                                    naquadahEarth.get(OrePrefixes.dust, 1));
                                modified = true;
                            } else if (tRecipe.mOutputs[i].isItemEqual(Materials.NaquadahEnriched.getDust(1))) {
                                tRecipe.mOutputs[i] = GTUtility.copyAmount(
                                    tRecipe.mOutputs[i].stackSize * 2,
                                    enrichedNaquadahEarth.get(OrePrefixes.dust, 1));
                                modified = true;
                            } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Naquadria.getDust(1))) {
                                tRecipe.mOutputs[i] = GTUtility.copyAmount(
                                    tRecipe.mOutputs[i].stackSize * 2,
                                    naquadriaEarth.get(OrePrefixes.dust, 1));
                                modified = true;
                            }
                        }
                        if (modified) {
                            reAdd.add(tRecipe);
                            remove.add(recipe);
                        }
                        break;
                    }
                }
            }
        }
        RecipeMaps.hammerRecipes.getBackend()
            .removeRecipes(remove);
        reAdd.forEach(RecipeMaps.hammerRecipes::add);
        RecipeMaps.hammerRecipes.getBackend()
            .reInit();

        GTLog.out.print(GoodGenerator.MOD_ID + ": Replace " + remove.size() + "! ");

        remove.clear();
        reAdd.clear();

        GTLog.out.print("Hammer done!\n");

        // For Chemical Reactor
        for (GTRecipe recipe : RecipeMaps.chemicalReactorRecipes.getAllRecipes()) {
            if (recipe.mFluidOutputs == null) continue;
            boolean isAny = false;
            for (int i = 0; i < recipe.mFluidOutputs.length; i++) {
                if (recipe.mFluidOutputs[i] == null) continue;
                if (recipe.mFluidOutputs[i].isFluidEqual(Materials.Naquadah.getMolten(1))
                    || recipe.mFluidOutputs[i].isFluidEqual(Materials.NaquadahEnriched.getMolten(1))
                    || recipe.mFluidOutputs[i].isFluidEqual(Materials.Naquadria.getMolten(1))) {
                    isAny = true;
                    break;
                }
            }
            if (!isAny) continue;
            GTRecipe tRecipe = recipe.copy();
            boolean modified = false;
            for (int i = 0; i < recipe.mFluidOutputs.length; i++) {
                if (recipe.mFluidOutputs[i] == null) continue;
                if (recipe.mFluidOutputs[i].isFluidEqual(Materials.Naquadah.getMolten(1))) {
                    tRecipe.mFluidOutputs[i] = naquadahGoo.getFluidOrGas(recipe.mFluidOutputs[i].amount * 2);
                    modified = true;
                } else if (recipe.mFluidOutputs[i].isFluidEqual(Materials.NaquadahEnriched.getMolten(1))) {
                    tRecipe.mFluidOutputs[i] = enrichedNaquadahGoo.getFluidOrGas(recipe.mFluidOutputs[i].amount * 2);
                    modified = true;
                } else if (recipe.mFluidOutputs[i].isFluidEqual(Materials.Naquadria.getMolten(1))) {
                    tRecipe.mFluidOutputs[i] = naquadriaGoo.getFluidOrGas(recipe.mFluidOutputs[i].amount * 2);
                    modified = true;
                }
            }
            if (modified) {
                reAdd.add(tRecipe);
                remove.add(recipe);
            }
        }
        RecipeMaps.chemicalReactorRecipes.getBackend()
            .removeRecipes(remove);
        reAdd.forEach(RecipeMaps.chemicalReactorRecipes::add);
        RecipeMaps.chemicalReactorRecipes.getBackend()
            .reInit();

        GTLog.out.print(GoodGenerator.MOD_ID + ": Replace " + remove.size() + "! ");

        remove.clear();
        reAdd.clear();

        GTLog.out.print("Chemical Reactor done!\n");

        // For Multi Chemical Reactor
        for (GTRecipe recipe : RecipeMaps.multiblockChemicalReactorRecipes.getAllRecipes()) {
            if (recipe.mFluidOutputs == null) continue;
            boolean isAny = false;
            for (int i = 0; i < recipe.mFluidOutputs.length; i++) {
                if (recipe.mFluidOutputs[i] == null) continue;
                if (recipe.mFluidOutputs[i].isFluidEqual(Materials.Naquadah.getMolten(1))
                    || recipe.mFluidOutputs[i].isFluidEqual(Materials.NaquadahEnriched.getMolten(1))
                    || recipe.mFluidOutputs[i].isFluidEqual(Materials.Naquadria.getMolten(1))) {
                    isAny = true;
                    break;
                }
            }
            if (!isAny) continue;
            GTRecipe tRecipe = recipe.copy();
            boolean modified = false;
            for (int i = 0; i < recipe.mFluidOutputs.length; i++) {
                if (recipe.mFluidOutputs[i] == null) continue;
                if (recipe.mFluidOutputs[i].isFluidEqual(Materials.Naquadah.getMolten(1))) {
                    tRecipe.mFluidOutputs[i] = naquadahGoo.getFluidOrGas(recipe.mFluidOutputs[i].amount * 2);
                    modified = true;
                } else if (recipe.mFluidOutputs[i].isFluidEqual(Materials.NaquadahEnriched.getMolten(1))) {
                    tRecipe.mFluidOutputs[i] = enrichedNaquadahGoo.getFluidOrGas(recipe.mFluidOutputs[i].amount * 2);
                    modified = true;
                } else if (recipe.mFluidOutputs[i].isFluidEqual(Materials.Naquadria.getMolten(1))) {
                    tRecipe.mFluidOutputs[i] = naquadriaGoo.getFluidOrGas(recipe.mFluidOutputs[i].amount * 2);
                    modified = true;
                }
            }
            if (modified) {
                reAdd.add(tRecipe);
                remove.add(recipe);
            }
        }
        RecipeMaps.multiblockChemicalReactorRecipes.getBackend()
            .removeRecipes(remove);
        reAdd.forEach(RecipeMaps.multiblockChemicalReactorRecipes::add);
        RecipeMaps.multiblockChemicalReactorRecipes.getBackend()
            .reInit();

        GTLog.out.print(GoodGenerator.MOD_ID + ": Replace " + remove.size() + "! ");

        remove.clear();
        reAdd.clear();

        GTLog.out.print("Multi Chemical Reactor done!\n");

        // For Gt++ Multi Centrifuge
        // Apparently NEI will break down if one modifies the hash list directly.
        // GTPP_Recipe.GTPP_Recipe_Map.sMultiblockCentrifugeRecipes_GT.mRecipeList.clear();
        // RecipeGen_MultisUsingFluidInsteadOfCells.generateRecipesNotUsingCells(
        // RecipeMaps.centrifugeRecipes,
        // GTPP_Recipe.GTPP_Recipe_Map.sMultiblockCentrifugeRecipes_GT);
        // GTPP_Recipe.GTPP_Recipe_Map.sMultiblockCentrifugeRecipes_GT.reInit();

        // For Simple Washer
        for (GTRecipe recipe : GTPPRecipeMaps.simpleWasherRecipes.getAllRecipes()) {
            ItemStack input = recipe.mInputs[0];
            if (GTUtility.isStackValid(input)) {
                int[] oreDict = OreDictionary.getOreIDs(input);
                for (int oreDictID : oreDict) {
                    if (OreDictionary.getOreName(oreDictID)
                        .startsWith("dustImpureNaq")
                        || OreDictionary.getOreName(oreDictID)
                            .startsWith("dustPureNaq")) {
                        GTRecipe tRecipe = recipe.copy();
                        boolean modified = false;
                        for (int i = 0; i < tRecipe.mOutputs.length; i++) {
                            if (!GTUtility.isStackValid(tRecipe.mOutputs[i])) continue;
                            if (tRecipe.mOutputs[i].isItemEqual(Materials.Naquadah.getDust(1))) {
                                tRecipe.mOutputs[i] = GTUtility
                                    .copyAmount(tRecipe.mOutputs[i].stackSize, naquadahEarth.get(OrePrefixes.dust, 1));
                                modified = true;
                            } else if (tRecipe.mOutputs[i].isItemEqual(Materials.NaquadahEnriched.getDust(1))) {
                                tRecipe.mOutputs[i] = GTUtility.copyAmount(
                                    tRecipe.mOutputs[i].stackSize,
                                    enrichedNaquadahEarth.get(OrePrefixes.dust, 1));
                                modified = true;
                            } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Naquadria.getDust(1))) {
                                tRecipe.mOutputs[i] = GTUtility
                                    .copyAmount(tRecipe.mOutputs[i].stackSize, naquadriaEarth.get(OrePrefixes.dust, 1));
                                modified = true;
                            }
                        }
                        if (modified) {
                            reAdd.add(tRecipe);
                            remove.add(recipe);
                        }
                        break;
                    }
                }
            }
        }
        GTPPRecipeMaps.simpleWasherRecipes.getBackend()
            .removeRecipes(remove);
        reAdd.forEach(GTPPRecipeMaps.simpleWasherRecipes::add);
        GTPPRecipeMaps.simpleWasherRecipes.getBackend()
            .reInit();

        GTLog.out.print(GoodGenerator.MOD_ID + ": Replace " + remove.size() + "! ");

        remove.clear();
        reAdd.clear();

        GTLog.out.print("Simple Washer done!\n");

        // For Cauldron Wash
        registerCauldronCleaningFor(Materials.Naquadah, naquadahEarth.getBridgeMaterial());
        registerCauldronCleaningFor(Materials.NaquadahEnriched, enrichedNaquadahEarth.getBridgeMaterial());
        registerCauldronCleaningFor(Materials.Naquadria, naquadriaEarth.getBridgeMaterial());
        GTLog.out.print(GoodGenerator.MOD_ID + ": Replace 3! ");
        GTLog.out.print("Cauldron Wash done!\n");

        // For Crafting Table
        CraftingManager.getInstance()
            .getRecipeList()
            .forEach(NaquadahReworkRecipeLoader::replaceInCraftTable);

        GTLog.out.print(GoodGenerator.MOD_ID + ": Replace Unknown! ");
        GTLog.out.print("Crafting Table done!\n");
    }

    public static void replaceInCraftTable(Object obj) {
        IRecipe recipe = (IRecipe) obj;
        ItemStack result = recipe.getRecipeOutput();
        if (!(recipe instanceof IRecipeMutableAccess mutableRecipe)) {
            return;
        }

        Object input = mutableRecipe.gt5u$getRecipeInputs();

        if (GTUtility.areStacksEqual(result, Materials.Naquadah.getDust(1), true)) {
            if (PlatinumSludgeOverHaul.checkRecipe(input, Materials.Naquadah)) {
                return;
            }
            mutableRecipe.gt5u$setRecipeOutputItem(naquadahEarth.get(OrePrefixes.dust, 2));
        } else if (GTUtility.areStacksEqual(result, Materials.NaquadahEnriched.getDust(1), true)) {
            if (PlatinumSludgeOverHaul.checkRecipe(input, Materials.NaquadahEnriched)) {
                return;
            }
            mutableRecipe.gt5u$setRecipeOutputItem(enrichedNaquadahEarth.get(OrePrefixes.dust, 2));
        } else if (GTUtility.areStacksEqual(result, Materials.Naquadria.getDust(1), true)) {
            if (PlatinumSludgeOverHaul.checkRecipe(input, Materials.Naquadria)) {
                return;
            }
            mutableRecipe.gt5u$setRecipeOutputItem(naquadriaEarth.get(OrePrefixes.dust, 2));
        }
    }
}
