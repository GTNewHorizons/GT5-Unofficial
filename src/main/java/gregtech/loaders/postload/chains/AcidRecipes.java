package gregtech.loaders.postload.chains;

import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.distillationTowerRecipes;
import static gregtech.api.recipe.RecipeMaps.mixerRecipes;
import static gregtech.api.recipe.RecipeMaps.multiblockChemicalReactorRecipes;
import static gregtech.api.recipe.RecipeMaps.sifterRecipes;
import static gregtech.api.recipe.RecipeMaps.vacuumFreezerRecipes;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeConstants.CHEMPLANT_CASING_TIER;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;
import static gregtech.api.util.GTRecipeConstants.UniversalChemical;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.centrifugeNonCellRecipes;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.chemicalPlantRecipes;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.electrolyzerNonCellRecipes;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.mixerNonCellRecipes;
import static gtnhlanth.api.recipe.LanthanidesRecipeMaps.digesterRecipes;

import net.minecraftforge.fluids.FluidStack;

import bartworks.system.material.WerkstoffLoader;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.core.fluids.GTPPFluids;
import gtPlusPlus.core.material.MaterialMisc;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.core.material.MaterialsOres;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtnhlanth.common.register.BotWerkstoffMaterialPool;

public class AcidRecipes {

    public static void run() {
        // Francium Line
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Thorium.getDust(4), Materials.ChromiumTrioxide.getDust(2))
            .fluidInputs(WerkstoffLoader.AmmoniumChloride.getFluidOrGas(2000))
            .fluidOutputs(Materials.ThoriumElutionAdsorbent.getFluid(2000L))
            .eut(TierEU.RECIPE_HV)
            .duration(15 * SECONDS)
            .addTo(mixerRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.ThoriumElutionAdsorbent.getFluid(8000L))
            .itemOutputs(Materials.Chrome.getDust(4))
            .outputChances(5500)
            .fluidOutputs(Materials.ImpureFranciumSolution.getFluid(4500L), Materials.Ammonia.getGas(3500L))
            .eut(TierEU.RECIPE_LuV)
            .duration(30 * SECONDS)
            .addTo(electrolyzerNonCellRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.SiliconDioxide.getDust(4), Materials.Barite.getDust(1))
            .fluidInputs(Materials.ImpureFranciumSolution.getFluid(5000L))
            .fluidOutputs(Materials.FranciumSlurry.getFluid(4000L))
            .eut(600)
            .duration(15 * SECONDS)
            .addTo(mixerRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.FranciumSlurry.getFluid(4000L))
            .itemOutputs(Materials.Francium.getDust(1), Materials.Francium.getDust(1), Materials.Lead.getDust(1))
            .outputChances(9000, 8000, 6500)
            .eut(TierEU.RECIPE_HV)
            .duration(5 * SECONDS)
            .addTo(sifterRecipes);
        // Fr + H2O = FrOH + H
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Francium.getDust(1))
            .circuit(1)
            .itemOutputs(Materials.FranciumHydroxide.getDust(3))
            .fluidInputs(Materials.Water.getFluid(1000L))
            .fluidOutputs(Materials.Hydrogen.getGas(1000L))
            .duration(5 * SECONDS)
            .eut(8)
            .addTo(UniversalChemical);
        // Chlorosulfonic Acid Line
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.PhosphorousPentoxide.getDust(7))
            .fluidInputs(new FluidStack(GTPPFluids.IndustrialStrengthHydrogenChloride, 4000))
            .fluidOutputs(Materials.PhosphorusChlorineMixture.getFluid(2000L), Materials.Water.getFluid(5000L))
            .duration(24 * SECONDS)
            .eut(480)
            .addTo(UniversalChemical);
        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.Bismuthinit.get(OrePrefixes.crushed, 5))
            .fluidInputs(Materials.PhosphorusChlorineMixture.getFluid(1000L))
            .itemOutputs(Materials.PhosphorusTrichloride.getDust(4))
            .fluidOutputs(Materials.StagnantWasteWater.getFluid(200L))
            .eut(5040)
            .duration(10 * SECONDS)
            .metadata(COIL_HEAT, 1800)
            .addTo(digesterRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialsOres.LAFOSSAITE.getCrushed(2))
            .fluidInputs(Materials.PhosphorusChlorineMixture.getFluid(1000L))
            .itemOutputs(Materials.PhosphorusTrichloride.getDust(4))
            .fluidOutputs(Materials.StagnantWasteWater.getFluid(2000L))
            .eut(5040)
            .duration(10 * SECONDS)
            .metadata(COIL_HEAT, 1800)
            .addTo(digesterRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                Materials.StagnantWasteWater.getFluid(6000L),
                MaterialMisc.HYDROGEN_CYANIDE.getFluidStack(4000))
            .fluidOutputs(Materials.ActivatedWasteWater.getFluid(2000L), Materials.Chlorine.getGas(8000L))
            .circuit(1)// Necessary to exceed properties.minItemInputs threshold
            .eut(1440)
            .duration(10 * SECONDS)
            .addTo(mixerNonCellRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.EmptyCatalystCarrier.get(10),
                Materials.Osmium.getDust(10),
                Materials.Naquadah.getDust(3))
            .fluidInputs(Materials.ActivatedWasteWater.getFluid(24000L))
            .itemOutputs(GregtechItemList.ChlorinationCatalyst.get(10))
            .eut(7680)
            .duration(150 * SECONDS)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.ChlorinationCatalyst.get(0), Materials.PhosphorusTrichloride.getDust(4))
            .fluidInputs(Materials.Chlorine.getGas(2000L))
            .fluidOutputs(Materials.PhosphorusPentachloride.getFluid(1000L))
            .eut(600)
            .duration(90 * SECONDS)
            .metadata(CHEMPLANT_CASING_TIER, 3)
            .addTo(chemicalPlantRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.ToxicAir.getGas(25000L))
            .fluidOutputs(
                Materials.CarbonMonoxide.getGas(6700L),
                Materials.Mercury.getFluid(5000L),
                BotWerkstoffMaterialPool.Phosgene.getFluidOrGas(3000),
                Materials.UltraContaminatedGas.getGas(2000L),
                WerkstoffLoader.Krypton.getFluidOrGas(1500))
            .eut(1920)
            .duration(20 * SECONDS)
            .addTo(distillationTowerRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.ToxicAir.getGas(3000L), Materials.Ozone.getGas(10000L))
            .fluidOutputs(Materials.ToxicSlurry.getFluid(400L))
            .eut(1920)
            .duration(30 * SECONDS)
            .addTo(vacuumFreezerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.crushed, Materials.Quantium, 6))
            .fluidInputs(Materials.ToxicSlurry.getFluid(3000L))
            .fluidOutputs(Materials.DestabilizationSlurry.getFluid(2500L))
            .eut(7680)
            .duration(20 * SECONDS)
            .addTo(UniversalChemical);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialsElements.STANDALONE.RUNITE.getCrushed(4))
            .fluidInputs(Materials.ToxicSlurry.getFluid(3000L))
            .fluidOutputs(Materials.DestabilizationSlurry.getFluid(3000L))
            .eut(7680)
            .duration(20 * SECONDS)
            .addTo(UniversalChemical);
        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.ToxicSlurry.getFluid(3000L))
            .fluidOutputs(
                Materials.TerephthalicAcid.getFluid(2500L),
                Materials.CarbonDioxide.getGas(2000L),
                Materials.Ammonia.getGas(1800L))
            .eut(480)
            .duration(15 * SECONDS)
            .addTo(centrifugeNonCellRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                Materials.PhosphorusPentachloride.getFluid(5000L),
                Materials.SulfuricAcid.getFluid(5000L),
                Materials.UltraContaminatedGas.getGas(1000L),
                Materials.DestabilizationSlurry.getFluid(1000L))
            .fluidOutputs(
                Materials.ChlorosulfonicAcid.getFluid(5000L),
                Materials.HydrochloricAcid.getFluid(10000L),
                Materials.PhosphoricAcid.getFluid(5000L),
                Materials.AgitatingSlurry.getFluid(1000L))
            .eut(7680)
            .duration(5 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        if (Mods.UniversalSingularities.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Mods.UniversalSingularities.ID, "universal.toxic.singularity", 0))
                .fluidInputs(Materials.AgitatingSlurry.getFluid(10000L), Materials.Ozone.getGas(2000L))
                .fluidOutputs(Materials.DestabilizationSlurry.getFluid(7000L))
                .eut(1920)
                .duration(10 * SECONDS)
                .addTo(multiblockChemicalReactorRecipes);
        }
    }
}
