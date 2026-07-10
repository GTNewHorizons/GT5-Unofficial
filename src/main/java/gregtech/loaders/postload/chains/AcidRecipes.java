package gregtech.loaders.postload.chains;

import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.blastFurnaceRecipes;
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

import com.ruling_0.materiallib.api.MaterialLibAPI;

import bartworks.system.material.WerkstoffLoader;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;
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
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Thorium, Materials2Shapes.shapeDust, (int) (4)),
                Materials.ChromiumTrioxide.getDust(2))
            .fluidInputs(WerkstoffLoader.AmmoniumChloride.getFluidOrGas(2000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.ThoriumElutionAdsorbent,
                    Materials2FluidShapes.shapeFluidLiquid,
                    (int) (2000)))
            .eut(TierEU.RECIPE_HV)
            .duration(15 * SECONDS)
            .addTo(mixerRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.ThoriumElutionAdsorbent,
                    Materials2FluidShapes.shapeFluidLiquid,
                    (int) (8000)))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Chrome, Materials2Shapes.shapeDust, (int) (4)))
            .outputChances(5500)
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.ImpureFranciumSolution,
                    Materials2FluidShapes.shapeFluidLiquid,
                    (int) (4500)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Ammonia, Materials2FluidShapes.shapeFluidGas, (int) (3500)))
            .eut(TierEU.RECIPE_LuV)
            .duration(30 * SECONDS)
            .addTo(electrolyzerNonCellRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.SiliconDioxide, Materials2Shapes.shapeDust, (int) (4)),
                MaterialLibAPI.getStack(Materials2Materials.Barite, Materials2Shapes.shapeDust, (int) (1)))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.ImpureFranciumSolution,
                    Materials2FluidShapes.shapeFluidLiquid,
                    (int) (5000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.FranciumSlurry,
                    Materials2FluidShapes.shapeFluidLiquid,
                    (int) (4000)))
            .eut(600)
            .duration(15 * SECONDS)
            .addTo(mixerRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.FranciumSlurry,
                    Materials2FluidShapes.shapeFluidLiquid,
                    (int) (4000)))
            .itemOutputs(
                Materials.Francium.getDust(1),
                Materials.Francium.getDust(1),
                MaterialLibAPI.getStack(Materials2Materials.Lead, Materials2Shapes.shapeDust, (int) (1)))
            .outputChances(9000, 8000, 6500)
            .eut(TierEU.RECIPE_HV)
            .duration(5 * SECONDS)
            .addTo(sifterRecipes);
        // Fr + H2O = FrOH + H
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Francium.getDust(1))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.FranciumHydroxide, Materials2Shapes.shapeDust, (int) (3)))
            .fluidInputs(Materials.Water.getFluid(1000L))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, (int) (1000)))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);
        // Chlorosulfonic Acid Line
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI
                    .getStack(Materials2Materials.PhosphorousPentoxide, Materials2Shapes.shapeDust, (int) (7)))
            .fluidInputs(new FluidStack(GTPPFluids.IndustrialStrengthHydrogenChloride, 4000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.PhosphorusChlorineMixture,
                    Materials2FluidShapes.shapeFluidLiquid,
                    (int) (2000)),
                Materials.Water.getFluid(5000L))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);
        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.Bismuthinit.get(OrePrefixes.crushed, 5))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.PhosphorusChlorineMixture,
                    Materials2FluidShapes.shapeFluidLiquid,
                    (int) (1000)))
            .itemOutputs(
                MaterialLibAPI
                    .getStack(Materials2Materials.PhosphorusTrichloride, Materials2Shapes.shapeDust, (int) (4)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.StagnantWasteWater,
                    Materials2FluidShapes.shapeFluidLiquid,
                    (int) (200)))
            .eut(5040)
            .duration(10 * SECONDS)
            .metadata(COIL_HEAT, 1800)
            .addTo(digesterRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialsOres.LAFOSSAITE.getCrushed(2))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.PhosphorusChlorineMixture,
                    Materials2FluidShapes.shapeFluidLiquid,
                    (int) (1000)))
            .itemOutputs(
                MaterialLibAPI
                    .getStack(Materials2Materials.PhosphorusTrichloride, Materials2Shapes.shapeDust, (int) (4)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.StagnantWasteWater,
                    Materials2FluidShapes.shapeFluidLiquid,
                    (int) (2000)))
            .eut(5040)
            .duration(10 * SECONDS)
            .metadata(COIL_HEAT, 1800)
            .addTo(digesterRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.StagnantWasteWater,
                    Materials2FluidShapes.shapeFluidLiquid,
                    (int) (6000)),
                MaterialMisc.HYDROGEN_CYANIDE.getFluidStack(4000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.ActivatedWasteWater,
                    Materials2FluidShapes.shapeFluidLiquid,
                    (int) (2000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Chlorine, Materials2FluidShapes.shapeFluidGas, (int) (8000)))
            .circuit(1)// Necessary to exceed properties.minItemInputs threshold
            .eut(1440)
            .duration(10 * SECONDS)
            .addTo(mixerNonCellRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.EmptyCatalystCarrier.get(10),
                MaterialLibAPI.getStack(Materials2Materials.Osmium, Materials2Shapes.shapeDust, (int) (10)),
                MaterialLibAPI.getStack(Materials2Materials.Naquadah, Materials2Shapes.shapeDust, (int) (3)))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.ActivatedWasteWater,
                    Materials2FluidShapes.shapeFluidLiquid,
                    (int) (24000)))
            .itemOutputs(GregtechItemList.ChlorinationCatalyst.get(10))
            .eut(TierEU.RECIPE_IV)
            .duration(150 * SECONDS)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.ChlorinationCatalyst.get(0),
                MaterialLibAPI
                    .getStack(Materials2Materials.PhosphorusTrichloride, Materials2Shapes.shapeDust, (int) (4)))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Chlorine, Materials2FluidShapes.shapeFluidGas, (int) (2000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.PhosphorusPentachloride,
                    Materials2FluidShapes.shapeFluidLiquid,
                    (int) (1000)))
            .eut(600)
            .duration(90 * SECONDS)
            .metadata(CHEMPLANT_CASING_TIER, 3)
            .addTo(chemicalPlantRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.ToxicAir, Materials2FluidShapes.shapeFluidGas, (int) (25000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.CarbonMonoxide,
                    Materials2FluidShapes.shapeFluidGas,
                    (int) (6700)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Mercury, Materials2FluidShapes.shapeFluidLiquid, (int) (5000)),
                BotWerkstoffMaterialPool.Phosgene.getFluidOrGas(3000),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.UltraContaminatedGas,
                    Materials2FluidShapes.shapeFluidGas,
                    (int) (2000)),
                WerkstoffLoader.Krypton.getFluidOrGas(1500))
            .eut(TierEU.RECIPE_EV)
            .duration(20 * SECONDS)
            .addTo(distillationTowerRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.ToxicAir, Materials2FluidShapes.shapeFluidGas, (int) (3000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Ozone, Materials2FluidShapes.shapeFluidGas, (int) (10000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.ToxicSlurry,
                    Materials2FluidShapes.shapeFluidLiquid,
                    (int) (400)))
            .eut(TierEU.RECIPE_EV)
            .duration(5 * SECONDS)
            .addTo(vacuumFreezerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Quantium, Materials2Shapes.shapeCrushed, (int) (6)))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.ToxicSlurry,
                    Materials2FluidShapes.shapeFluidLiquid,
                    (int) (3000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.DestabilizationSlurry,
                    Materials2FluidShapes.shapeFluidLiquid,
                    (int) (2500)))
            .eut(TierEU.RECIPE_IV)
            .duration(20 * SECONDS)
            .addTo(UniversalChemical);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialsElements.STANDALONE.RUNITE.getCrushed(4))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.ToxicSlurry,
                    Materials2FluidShapes.shapeFluidLiquid,
                    (int) (3000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.DestabilizationSlurry,
                    Materials2FluidShapes.shapeFluidLiquid,
                    (int) (3000)))
            .eut(TierEU.RECIPE_IV)
            .duration(20 * SECONDS)
            .addTo(UniversalChemical);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.ToxicSlurry,
                    Materials2FluidShapes.shapeFluidLiquid,
                    (int) (3000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.TerephthalicAcid,
                    Materials2FluidShapes.shapeFluidLiquid,
                    (int) (2500)),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.CarbonDioxide,
                    Materials2FluidShapes.shapeFluidGas,
                    (int) (2000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Ammonia, Materials2FluidShapes.shapeFluidGas, (int) (1800)))
            .eut(TierEU.RECIPE_HV)
            .duration(15 * SECONDS)
            .addTo(centrifugeNonCellRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.PhosphorusPentachloride,
                    Materials2FluidShapes.shapeFluidLiquid,
                    (int) (5000)),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.SulfuricAcid,
                    Materials2FluidShapes.shapeFluidLiquid,
                    (int) (5000)),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.UltraContaminatedGas,
                    Materials2FluidShapes.shapeFluidGas,
                    (int) (1000)),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.DestabilizationSlurry,
                    Materials2FluidShapes.shapeFluidLiquid,
                    (int) (1000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.ChlorosulfonicAcid,
                    Materials2FluidShapes.shapeFluidLiquid,
                    (int) (5000)),
                Materials.HydrochloricAcid.getFluid(10000L),
                Materials.PhosphoricAcid.getFluid(5000L),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.AgitatingSlurry,
                    Materials2FluidShapes.shapeFluidLiquid,
                    (int) (1000)))
            .eut(TierEU.RECIPE_IV)
            .duration(5 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        if (Mods.UniversalSingularities.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Mods.UniversalSingularities.ID, "universal.toxic.singularity", 0))
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.AgitatingSlurry,
                        Materials2FluidShapes.shapeFluidLiquid,
                        (int) (10000)),
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.Ozone, Materials2FluidShapes.shapeFluidGas, (int) (2000)))
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.DestabilizationSlurry,
                        Materials2FluidShapes.shapeFluidLiquid,
                        (int) (7000)))
                .eut(TierEU.RECIPE_EV)
                .duration(10 * SECONDS)
                .addTo(multiblockChemicalReactorRecipes);
        }
        if (Mods.TwilightForest.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Mods.TwilightForest.ID, "tile.TFSapling", 4))
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.SulfuricAcid,
                        Materials2FluidShapes.shapeFluidLiquid,
                        (int) (2000)))
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.PoisonousSlurry,
                        Materials2FluidShapes.shapeFluidLiquid,
                        (int) (2500)))
                .eut(TierEU.RECIPE_EV)
                .duration(10 * SECONDS)
                .metadata(COIL_HEAT, 3050)
                .addTo(blastFurnaceRecipes);
        }
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.PoisonousSlurry,
                    Materials2FluidShapes.shapeFluidLiquid,
                    (int) (7500)),
                new FluidStack(GTPPFluids.FermentationBase, 2500))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.ToxicSlurry,
                    Materials2FluidShapes.shapeFluidLiquid,
                    (int) (1000)))
            .eut(TierEU.RECIPE_IV)
            .duration(4 * SECONDS)
            .metadata(CHEMPLANT_CASING_TIER, 2)
            .addTo(chemicalPlantRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Benzene, Materials2FluidShapes.shapeFluidLiquid, (int) (7000)),
                new FluidStack(GTPPFluids.Formaldehyde, 4000),
                Materials.Diphenylisophthalate.getFluid(1000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.UltraContaminatedGas,
                    Materials2FluidShapes.shapeFluidGas,
                    (int) (12000)))
            .eut(TierEU.RECIPE_EV)
            .duration(8 * SECONDS)
            .addTo(mixerNonCellRecipes);
    }
}
