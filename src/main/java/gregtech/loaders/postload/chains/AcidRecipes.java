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

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Mods;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.FluidShapes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.fluids.GTPPFluids;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public class AcidRecipes {

    public static void run() {
        // Francium Line
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Thorium, Shapes.dust, 4),
                MaterialLibAPI.getStack(Materials.Chromiumtrioxide, Shapes.dust, 2))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.AmmoniumChloride, FluidShapes.fluidLiquid, 2000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.ThoriumElutionAdsorbent, FluidShapes.fluidLiquid, 2000))
            .eut(TierEU.RECIPE_HV)
            .duration(15 * SECONDS)
            .addTo(mixerRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.ThoriumElutionAdsorbent, FluidShapes.fluidLiquid, 8000))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Chrome, Shapes.dust, 4))
            .outputChances(5500)
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.ImpureFranciumSolution, FluidShapes.fluidLiquid, 4500),
                MaterialLibAPI.getFluidStack(Materials.Ammonia, FluidShapes.fluidGas, 3500))
            .eut(TierEU.RECIPE_LuV)
            .duration(30 * SECONDS)
            .addTo(electrolyzerNonCellRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, 4),
                MaterialLibAPI.getStack(Materials.Barite, Shapes.dust, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.ImpureFranciumSolution, FluidShapes.fluidLiquid, 5000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.FranciumSlurry, FluidShapes.fluidLiquid, 4000))
            .eut(600)
            .duration(15 * SECONDS)
            .addTo(mixerRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.FranciumSlurry, FluidShapes.fluidLiquid, 4000))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.FranciumGT5U, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.FranciumGT5U, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Lead, Shapes.dust, 1))
            .outputChances(9000, 8000, 6500)
            .eut(TierEU.RECIPE_HV)
            .duration(5 * SECONDS)
            .addTo(sifterRecipes);
        // Fr + H2O = FrOH + H
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.FranciumGT5U, Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.FranciumHydroxide, Shapes.dust, 3))
            .fluidInputs(GTUtility.getWater(1000L))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 1000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);
        // Chlorosulfonic Acid Line
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.PhosphorousPentoxide, Shapes.dust, 7))
            .fluidInputs(new FluidStack(GTPPFluids.IndustrialStrengthHydrogenChloride, 4000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.PhosphorusChlorineMixture, FluidShapes.fluidLiquid, 2000),
                GTUtility.getWater(5000L))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Bismuthinite, Shapes.crushed, 5))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.PhosphorusChlorineMixture, FluidShapes.fluidLiquid, 1000))
            .itemOutputs(MaterialLibAPI.getStack(Materials.PhosphorusTrichloride, Shapes.dust, 4))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.StagnantWasteWater, FluidShapes.fluidLiquid, 200))
            .eut(5040)
            .duration(10 * SECONDS)
            .metadata(COIL_HEAT, 1800)
            .addTo(digesterRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Lafossaite, Shapes.crushed, 2))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.PhosphorusChlorineMixture, FluidShapes.fluidLiquid, 1000))
            .itemOutputs(MaterialLibAPI.getStack(Materials.PhosphorusTrichloride, Shapes.dust, 4))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.StagnantWasteWater, FluidShapes.fluidLiquid, 2000))
            .eut(5040)
            .duration(10 * SECONDS)
            .metadata(COIL_HEAT, 1800)
            .addTo(digesterRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.StagnantWasteWater, FluidShapes.fluidLiquid, 6000),
                MaterialUtils.anyFluid(Materials.HydrogenCyanide, 4000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.ActivatedWasteWater, FluidShapes.fluidLiquid, 2000),
                MaterialLibAPI.getFluidStack(Materials.Chlorine, FluidShapes.fluidGas, 8000))
            .circuit(1)// Necessary to exceed properties.minItemInputs threshold
            .eut(1440)
            .duration(10 * SECONDS)
            .addTo(mixerNonCellRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.EmptyCatalystCarrier.get(10),
                MaterialLibAPI.getStack(Materials.Osmium, Shapes.dust, 10),
                MaterialLibAPI.getStack(Materials.Naquadah, Shapes.dust, 3))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.ActivatedWasteWater, FluidShapes.fluidLiquid, 24000))
            .itemOutputs(GregtechItemList.ChlorinationCatalyst.get(10))
            .eut(TierEU.RECIPE_IV)
            .duration(150 * SECONDS)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.ChlorinationCatalyst.get(0),
                MaterialLibAPI.getStack(Materials.PhosphorusTrichloride, Shapes.dust, 4))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Chlorine, FluidShapes.fluidGas, 2000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.PhosphorusPentachloride, FluidShapes.fluidLiquid, 1000))
            .eut(600)
            .duration(90 * SECONDS)
            .metadata(CHEMPLANT_CASING_TIER, 3)
            .addTo(chemicalPlantRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.ToxicAir, FluidShapes.fluidGas, 25000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.CarbonMonoxide, FluidShapes.fluidGas, 6700),
                MaterialLibAPI.getFluidStack(Materials.Mercury, FluidShapes.fluidLiquid, 5000),
                MaterialLibAPI.getFluidStack(Materials.Phosgene, FluidShapes.fluidLiquid, 3000),
                MaterialLibAPI.getFluidStack(Materials.UltraContaminatedGas, FluidShapes.fluidGas, 2000),
                MaterialLibAPI.getFluidStack(Materials.Krypton, FluidShapes.fluidLiquid, 1500))
            .eut(TierEU.RECIPE_EV)
            .duration(20 * SECONDS)
            .addTo(distillationTowerRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.ToxicAir, FluidShapes.fluidGas, 3000),
                MaterialLibAPI.getFluidStack(Materials.Ozone, FluidShapes.fluidGas, 10000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.ToxicSlurry, FluidShapes.fluidLiquid, 400))
            .eut(TierEU.RECIPE_EV)
            .duration(5 * SECONDS)
            .addTo(vacuumFreezerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Quantium, Shapes.crushed, 6))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.ToxicSlurry, FluidShapes.fluidLiquid, 3000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.DestabilizationSlurry, FluidShapes.fluidLiquid, 2500))
            .eut(TierEU.RECIPE_IV)
            .duration(20 * SECONDS)
            .addTo(UniversalChemical);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Runite, Shapes.crushed, 4))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.ToxicSlurry, FluidShapes.fluidLiquid, 3000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.DestabilizationSlurry, FluidShapes.fluidLiquid, 3000))
            .eut(TierEU.RECIPE_IV)
            .duration(20 * SECONDS)
            .addTo(UniversalChemical);
        GTValues.RA.stdBuilder()
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.ToxicSlurry, FluidShapes.fluidLiquid, 3000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.TerephthalicAcid, FluidShapes.fluidLiquid, 2500),
                MaterialLibAPI.getFluidStack(Materials.CarbonDioxide, FluidShapes.fluidGas, 2000),
                MaterialLibAPI.getFluidStack(Materials.Ammonia, FluidShapes.fluidGas, 1800))
            .eut(TierEU.RECIPE_HV)
            .duration(15 * SECONDS)
            .addTo(centrifugeNonCellRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.PhosphorusPentachloride, FluidShapes.fluidLiquid, 5000),
                MaterialLibAPI.getFluidStack(Materials.SulfuricAcid, FluidShapes.fluidLiquid, 5000),
                MaterialLibAPI.getFluidStack(Materials.UltraContaminatedGas, FluidShapes.fluidGas, 1000),
                MaterialLibAPI.getFluidStack(Materials.DestabilizationSlurry, FluidShapes.fluidLiquid, 1000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.ChlorosulfonicAcid, FluidShapes.fluidLiquid, 5000),
                MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, (int) (10000L)),
                MaterialLibAPI.getFluidStack(Materials.PhosphoricAcidGT5U, FluidShapes.fluidLiquid, (int) (5000L)),
                MaterialLibAPI.getFluidStack(Materials.AgitatingSlurry, FluidShapes.fluidLiquid, 1000))
            .eut(TierEU.RECIPE_IV)
            .duration(5 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        if (Mods.UniversalSingularities.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Mods.UniversalSingularities.ID, "universal.toxic.singularity", 0))
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(Materials.AgitatingSlurry, FluidShapes.fluidLiquid, 10000),
                    MaterialLibAPI.getFluidStack(Materials.Ozone, FluidShapes.fluidGas, 2000))
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(Materials.DestabilizationSlurry, FluidShapes.fluidLiquid, 7000))
                .eut(TierEU.RECIPE_EV)
                .duration(10 * SECONDS)
                .addTo(multiblockChemicalReactorRecipes);
        }
        if (Mods.TwilightForest.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Mods.TwilightForest.ID, "tile.TFSapling", 4))
                .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SulfuricAcid, FluidShapes.fluidLiquid, 2000))
                .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.PoisonousSlurry, FluidShapes.fluidLiquid, 2500))
                .eut(TierEU.RECIPE_EV)
                .duration(10 * SECONDS)
                .metadata(COIL_HEAT, 3050)
                .addTo(blastFurnaceRecipes);
        }
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.PoisonousSlurry, FluidShapes.fluidLiquid, 7500),
                new FluidStack(GTPPFluids.FermentationBase, 2500))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.ToxicSlurry, FluidShapes.fluidLiquid, 1000))
            .eut(TierEU.RECIPE_IV)
            .duration(4 * SECONDS)
            .metadata(CHEMPLANT_CASING_TIER, 2)
            .addTo(chemicalPlantRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Benzene, FluidShapes.fluidLiquid, 7000),
                new FluidStack(GTPPFluids.Formaldehyde, 4000),
                MaterialLibAPI.getFluidStack(Materials.DiphenylIsophtalate, FluidShapes.fluidLiquid, 1000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.UltraContaminatedGas, FluidShapes.fluidGas, 12000))
            .eut(TierEU.RECIPE_EV)
            .duration(8 * SECONDS)
            .addTo(mixerNonCellRecipes);
    }
}
