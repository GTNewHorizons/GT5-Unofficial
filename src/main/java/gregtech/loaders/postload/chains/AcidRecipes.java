package gregtech.loaders.postload.chains;

import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.centrifugeRecipes;
import static gregtech.api.recipe.RecipeMaps.distillationTowerRecipes;
import static gregtech.api.recipe.RecipeMaps.mixerRecipes;
import static gregtech.api.recipe.RecipeMaps.multiblockChemicalReactorRecipes;
import static gregtech.api.recipe.RecipeMaps.sifterRecipes;
import static gregtech.api.recipe.RecipeMaps.vacuumFreezerRecipes;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeConstants.CHEMPLANT_CASING_TIER;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;
import static gregtech.api.util.GTRecipeConstants.DISSOLUTION_TANK_RATIO;
import static gregtech.api.util.GTRecipeConstants.UniversalChemical;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.centrifugeNonCellRecipes;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.chemicalPlantRecipes;
import static gtnhlanth.api.recipe.LanthanidesRecipeMaps.digesterRecipes;
import static gtnhlanth.api.recipe.LanthanidesRecipeMaps.dissolutionTankRecipes;

import net.minecraftforge.fluids.FluidStack;

import bartworks.system.material.WerkstoffLoader;
import goodgenerator.items.GGMaterial;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
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
            .itemInputs(Materials.Uranium.getDust(16))
            .fluidInputs(Materials.NitricAcid.getFluid(10000L), Materials.TerephthalicAcid.getFluid(400L))
            .itemOutputs(Materials.DepletedUraniumResidue.getDust(8))
            .fluidOutputs(Materials.UraniumInfusedAcidicSolution.getFluid(15000L))
            .duration(240 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .metadata(DISSOLUTION_TANK_RATIO, 25)
            .addTo(dissolutionTankRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Uranium235.getDust(8))
            .fluidInputs(Materials.NitricAcid.getFluid(10000L), Materials.TerephthalicAcid.getFluid(400L))
            .itemOutputs(Materials.DepletedUraniumResidue.getDust(8))
            .fluidOutputs(Materials.UraniumInfusedAcidicSolution.getFluid(30000L))
            .duration(160 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .metadata(DISSOLUTION_TANK_RATIO, 25)
            .addTo(dissolutionTankRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialsElements.getInstance().URANIUM233.getDust(4))
            .fluidInputs(Materials.NitricAcid.getFluid(10000L), Materials.TerephthalicAcid.getFluid(400L))
            .itemOutputs(Materials.DepletedUraniumResidue.getDust(8))
            .fluidOutputs(Materials.UraniumInfusedAcidicSolution.getFluid(60000L))
            .duration(80 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .metadata(DISSOLUTION_TANK_RATIO, 25)
            .addTo(dissolutionTankRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.DepletedUraniumResidue.getDust(5))
            .itemOutputs(Materials.Lead.getDust(3), Materials.Bismuth.getDust(2))
            .duration(10 * SECONDS)
            .eut(200)
            .addTo(centrifugeRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.UraniumInfusedAcidicSolution.getFluid(6000L))
            .itemOutputs(Materials.Thorium.getDust(1), Materials.Lead.getDust(2), Materials.CrudeFrancium.getDust(1))
            .fluidOutputs(Materials.NitrogenDioxide.getFluid(1000L))
            .outputChances(9000, 8000, 6000)
            .duration(15 * SECONDS)
            .eut(1200)
            .addTo(sifterRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.CrudeFrancium.getDust(10), GGMaterial.vanadiumPentoxide.get(OrePrefixes.dust, 1))
            .fluidInputs(Materials.HydrochloricAcid.getFluid(4000L))
            .itemOutputs(Materials.Francium.getDust(1))
            .fluidOutputs(Materials.HypochlorousAcid.getFluid(4000L))
            .outputChances(3000)
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(UniversalChemical);
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
            .fluidOutputs(Materials.PhosphorusDichloride.getFluid(2000L), Materials.Water.getFluid(5000L))
            .duration(24 * SECONDS)
            .eut(480)
            .addTo(UniversalChemical);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialsOres.LAFOSSAITE.getCrushed(2))
            .fluidInputs(Materials.PhosphorusDichloride.getFluid(1000L))
            .itemOutputs(Materials.PhosphorusTrichloride.getDust(4))
            .fluidOutputs(Materials.StagnantWasteWater.getFluid(3000L))
            .eut(5040)
            .duration(10 * SECONDS)
            .metadata(COIL_HEAT, 1800)
            .addTo(digesterRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                Materials.StagnantWasteWater.getFluid(8000L),
                MaterialMisc.HYDROGEN_CYANIDE.getFluidStack(4000))
            .itemOutputs(Materials.ChromiumDioxide.getDust(1))
            .fluidOutputs(Materials.ActivatedWasteWater.getFluid(12000L))
            .circuit(1)// Necessary to exceed properties.minItemInputs threshold
            .eut(1440)
            .duration(10 * SECONDS)
            .addTo(mixerRecipes);
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
                Materials.Mercury.getGas(5000L),
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
