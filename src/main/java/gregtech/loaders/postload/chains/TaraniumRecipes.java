package gregtech.loaders.postload.chains;

import static goodgenerator.util.CrackRecipeAdder.addUniversalDistillationRecipe;
import static gregtech.api.recipe.RecipeMaps.autoclaveRecipes;
import static gregtech.api.recipe.RecipeMaps.centrifugeRecipes;
import static gregtech.api.recipe.RecipeMaps.chemicalBathRecipes;
import static gregtech.api.recipe.RecipeMaps.fusionRecipes;
import static gregtech.api.recipe.RecipeMaps.mixerRecipes;
import static gregtech.api.recipe.RecipeMaps.multiblockChemicalReactorRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;
import static gregtech.api.util.GTRecipeConstants.FUSION_THRESHOLD;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.centrifugeNonCellRecipes;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.chemicalDehydratorRecipes;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.flotationCellRecipes;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.mixerNonCellRecipes;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.vacuumFurnaceRecipes;
import static gtnhlanth.common.register.WerkstoffMaterialPool.Gangue;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import bartworks.system.material.WerkstoffLoader;
import goodgenerator.items.GGMaterial;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.core.util.minecraft.FluidUtils;

public class TaraniumRecipes {

    public static void run() {

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Stone, 64),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Stone, 32))
            .fluidInputs(Materials.HydrofluoricAcid.getFluid(1000))
            .fluidOutputs(Materials.DirtyHexafluorosilicicAcid.getFluid(500))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.DirtyHexafluorosilicicAcid.getFluid(1000))
            .fluidOutputs(Materials.DilutedHydrofluorosilicicAcid.getFluid(1000))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.StoneResidue, 8))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.StoneResidue, 64),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.StoneResidue, 64),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.SamariumMagnetic, 16))
            .fluidInputs(Materials.Grade2PurifiedWater.getFluid(100))
            .fluidOutputs(Materials.StoneFroth.getFluid(1000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(flotationCellRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.StoneFroth.getFluid(4000))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Samarium, 64),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.TraceElementResidue, 16),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Ilmenite, 38),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Chromite, 57),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Barite, 64),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Barite, 12),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Pyrolusite, 64),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Pyrolusite, 64),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Pyrolusite, 24))
            .fluidOutputs(Materials.Water.getFluid(400), Materials.StoneMudSlurry.getFluid(2000))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(COIL_HEAT, 3600)
            .addTo(vacuumFurnaceRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.StoneMudSlurry.getFluid(100))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Bauxite, 9),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.BandedIron, 9),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.PotassiumFeldspar, 64),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.PotassiumFeldspar, 44),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Biotite, 48),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Lazurite, 24))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1800)
            .addTo(chemicalDehydratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.TraceElementResidue, 64))
            .fluidInputs(GGMaterial.P507.getFluidOrGas(1000))
            .fluidOutputs(Materials.TraceElementSlurry.getFluid(4000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(chemicalBathRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.TraceElementSlurry.getFluid(16000))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.UncommonResidue, 8))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(chemicalDehydratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.UncommonResidue, 10))
            .fluidInputs(Materials.DioxygenDifluoride.getFluid(10000))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.PartiallyOxidizedResidue, 10))
            .duration(12 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.PartiallyOxidizedResidue, 24))
            .fluidInputs(FluidRegistry.getFluidStack("ic2distilledwater", 10000))
            .fluidOutputs(Materials.TraceResidue.getFluid(8000), Materials.OxidizedResidualSolution.getFluid(10000))
            .duration(4 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_EV)
            .addTo(centrifugeNonCellRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Terbium, 16))
            .fluidInputs(Materials.TraceResidue.getFluid(64000))
            .fluidOutputs(Materials.DepletedTraceElementSlurry.getFluid(64000))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.TerbiumBondedTraceResidue, 16))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.TerbiumBondedTraceResidue, 64))
            .fluidInputs(FluidUtils.getHydrofluoricAcid(16000))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Terbium, 64))
            .fluidOutputs(Materials.FluorinatedTraceResidue.getFluid(16000))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(chemicalBathRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.FluorinatedTraceResidue.getFluid(1000), Materials.Helium_3.getGas(4000))
            .fluidOutputs(Materials.DustyHelium3.getFluid(5000))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(mixerNonCellRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.DustyHelium3.getFluid(5000))
            .fluidOutputs(
                Materials.TaraniumEnrichedHelium3.getFluid(500),
                Materials.TaraniumSemidepletedHelium3.getFluid(1500),
                Materials.TaraniumDepletedHelium3.getFluid(3000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(centrifugeNonCellRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.SeparationElectromagnet.get(0))
            .fluidInputs(Materials.TaraniumSemidepletedHelium3.getFluid(1000))
            .fluidOutputs(
                Materials.TaraniumEnrichedHelium3.getFluid(100),
                Materials.TaraniumDepletedHelium3.getFluid(900))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(centrifugeNonCellRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.TaraniumDepletedHelium3.getFluid(1000), Materials.Helium.getPlasma(1000))
            .fluidOutputs(Materials.TaraniumDepletedHeliumPlasma.getFluid(2000))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(mixerNonCellRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.SeparationElectromagnet.get(0))
            .fluidInputs(Materials.TaraniumDepletedHeliumPlasma.getFluid(10000))
            .fluidOutputs(Materials.Helium.getPlasma(4000))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.UncommonResidue, 6))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                Materials.TaraniumEnrichedHelium3.getFluid(4000),
                new FluidStack(MaterialsElements.STANDALONE.FORCE.getPlasma(), 500))
            .fluidOutputs(Materials.TaraniumrichHelium3.getFluid(4000))
            .duration(8 * SECONDS)
            .eut(16384)
            .metadata(FUSION_THRESHOLD, 100000000)
            .addTo(fusionRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.TaraniumrichHelium3.getFluid(1000))
            .fluidOutputs(Materials.TaraniumDepletedHelium3.getPlasma(500))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Taranium, 1))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.DepletedTraceElementSlurry.getFluid(1000))
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.QuartzSand, 64))
            .fluidOutputs(GGMaterial.P507.getFluidOrGas(1500))
            .itemOutputs(
                Gangue.get(OrePrefixes.dust, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.TraceElementResidue, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.TraceElementResidue, 2),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.TraceElementResidue, 4),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.TraceElementResidue, 8),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.TraceElementResidue, 16))
            .outputChances(2000, 9000, 8000, 7000, 6000, 5000)
            .duration(12 * TICKS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(centrifugeRecipes);

        addUniversalDistillationRecipe(
            Materials.DilutedHydrofluorosilicicAcid.getFluid(3000),
            new FluidStack[] { Materials.Water.getFluid(2000),
                WerkstoffLoader.HexafluorosilicicAcid.getFluidOrGas(1000) },
            null,
            1 * SECONDS + 10 * TICKS,
            TierEU.RECIPE_HV / 2);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                Materials.OxidizedResidualSolution.getFluid(2000),
                Materials.Grade3PurifiedWater.getFluid(1000))
            .fluidOutputs(Materials.Grade2PurifiedWater.getFluid(3000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.MicrofocusXRayTube.get(0))
            .fluidInputs(Materials.Oxygen.getGas(2000), Materials.Fluorine.getGas(2000))
            .fluidOutputs(Materials.DioxygenDifluoride.getFluid(1000))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

    }
}
