package goodgenerator.loader;

import static goodgenerator.api.recipe.GoodGeneratorRecipeMaps.neutronActivatorRecipes;
import static goodgenerator.util.MyRecipeAdder.computeRangeNKE;
import static gregtech.api.recipe.RecipeMaps.autoclaveRecipes;
import static gregtech.api.recipe.RecipeMaps.blastFurnaceRecipes;
import static gregtech.api.recipe.RecipeMaps.centrifugeRecipes;
import static gregtech.api.recipe.RecipeMaps.chemicalReactorRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidSolidifierRecipes;
import static gregtech.api.recipe.RecipeMaps.maceratorRecipes;
import static gregtech.api.recipe.RecipeMaps.mixerRecipes;
import static gregtech.api.recipe.RecipeMaps.multiblockChemicalReactorRecipes;
import static gregtech.api.recipe.RecipeMaps.quantumForceTransformerRecipes;
import static gregtech.api.recipe.RecipeMaps.vacuumFreezerRecipes;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.STACKS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.ADDITIVE_AMOUNT;
import static gregtech.api.util.GTRecipeConstants.BlastFurnaceWithGas;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;
import static gregtech.api.util.GTRecipeConstants.NKE_RANGE;
import static gregtech.api.util.GTRecipeConstants.QFT_CATALYST;
import static gregtech.api.util.GTRecipeConstants.QFT_FOCUS_TIER;
import static gregtech.api.util.GTRecipeConstants.UniversalChemical;
import static gregtech.common.items.MetaGeneratedItem01.registerCauldronCleaningFor;

import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import goodgenerator.util.CrackRecipeAdder;
import goodgenerator.util.ItemRefer;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.CellShapes;
import gregtech.api.enums.materials.FluidShapes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.material.MaterialParts;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public class NaquadahRecipeLoader {

    public static void RecipeLoad() {
        registerCauldronCleaningFor(Materials.Naquadah, Materials.NaquadahOxideMixture);
        registerCauldronCleaningFor(Materials.NaquadahEnriched, Materials.EnrichedNaquadahOxideMixture);
        registerCauldronCleaningFor(Materials.Naquadria, Materials.NaquadriaOxideMixture);

        // Naquadah (UEV)
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.NaquadahOxideMixture, Shapes.dust, 32),
                MaterialLibAPI.getStack(Materials.Sodium, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.InertNaquadah, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Titanium, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.Adamantium, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.Gallium, Shapes.dust, 64))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 64_000),
                MaterialLibAPI.getFluidStack(Materials.Fluorine, FluidShapes.fluidGas, 64_000),
                MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 100))
            .duration(10 * SECONDS)
            .eut(GTValues.VP[10])
            .metadata(QFT_CATALYST, GregtechItemList.SimpleNaquadahCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 2)
            .addTo(quantumForceTransformerRecipes);
        // Enriched Naquadah (UIV)
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.EnrichedNaquadahOxideMixture, Shapes.dust, 32),
                MaterialLibAPI.getStack(Materials.Zinc, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.InertEnrichedNaquadah, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Trinium, Shapes.dust, 64))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.SulfuricAcid, FluidShapes.fluidLiquid, 16_000),
                MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 100))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.WasteLiquid, FluidShapes.fluidLiquid, 32_000))
            .duration(10 * SECONDS)
            .eut(GTValues.VP[11])
            .metadata(QFT_CATALYST, GregtechItemList.SimpleNaquadahCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 2)
            .addTo(quantumForceTransformerRecipes);
        // Naquadria (UMV)
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.NaquadriaOxideMixture, Shapes.dust, 32),
                MaterialLibAPI.getStack(Materials.Magnesium, Shapes.dust, 64))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.InertNaquadria, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Barium, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.Indium, Shapes.dust, 64),
                ItemList.NaquadriaSupersolid.get(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.PhosphoricAcidGT5U, FluidShapes.fluidLiquid, 16_000),
                MaterialLibAPI.getFluidStack(Materials.SulfuricAcid, FluidShapes.fluidLiquid, 16_000),
                MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 100))
            .duration(5 * SECONDS)
            .eut(GTValues.VP[12])
            .metadata(QFT_CATALYST, GregtechItemList.AdvancedNaquadahCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 3)
            .addTo(quantumForceTransformerRecipes);

        // Activate Them

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.InertNaquadah, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.InertNaquadah, Shapes.dust, 32))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Nickel, Shapes.dust, 16))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Nickel, FluidShapes.fluidPlasma, 16 * INGOTS))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Naquadah, FluidShapes.fluidMolten, 144 * STACKS))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(0)
            .metadata(NKE_RANGE, computeRangeNKE(600, 500))
            .addTo(neutronActivatorRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.InertEnrichedNaquadah, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.InertEnrichedNaquadah, Shapes.dust, 32))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Titanium, Shapes.dust, 16))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Titanium, FluidShapes.fluidPlasma, 16 * INGOTS))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.NaquadahEnriched, FluidShapes.fluidMolten, 144 * STACKS))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(0)
            .metadata(NKE_RANGE, computeRangeNKE(900, 850))
            .addTo(neutronActivatorRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.InertNaquadria, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.InertNaquadria, Shapes.dust, 32))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Americium, Shapes.dust, 16))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Americium, FluidShapes.fluidPlasma, 16 * INGOTS))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Naquadria, FluidShapes.fluidMolten, 144 * STACKS))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(0)
            .metadata(NKE_RANGE, computeRangeNKE(1100, 1080))
            .addTo(neutronActivatorRecipes);

        // Naquadah Rework Line
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.NaquadahOxideMixture, Shapes.dust, 2))
            .circuit(1)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.FluoroantimonicAcid, FluidShapes.fluidLiquid, 3_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.LowQualityNaquadahEmulsion, FluidShapes.fluidLiquid, 2_000))
            .itemOutputs(MaterialLibAPI.getStack(Materials.TitaniumTrifluoride, Shapes.dust, 4))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(COIL_HEAT, 3000)
            .addTo(blastFurnaceRecipes);

        // TiF3 + 3H = Ti + 3HF
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.TitaniumTrifluoride, Shapes.dust, 4))
            .circuit(1)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 3_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.HydrofluoricAcidGT5U, FluidShapes.fluidLiquid, 3_000))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Titanium, Shapes.ingotHot, 1))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .metadata(COIL_HEAT, 2000)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.copyAmount(0, MaterialLibAPI.getStack(Materials.Copper, Shapes.plate, 1)),
                MaterialParts.requireCell(Materials.Hydrogen, 8))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SeedOil, FluidShapes.fluidLiquid, 3_000))
            .itemOutputs(ItemList.Cell_Empty.get(8))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials._2Ethyl1Hexanol, FluidShapes.fluidLiquid, 1_000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        // 2C8H18O + H3PO4 =Na,C2H6O= C16H35O3P + 2H2O
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Sodium, Shapes.dust, 2))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials._2Ethyl1Hexanol, FluidShapes.fluidLiquid, 2_000),
                MaterialLibAPI.getFluidStack(Materials.PhosphoricAcidGT5U, FluidShapes.fluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Ethanol, FluidShapes.fluidLiquid, 2_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.P507, FluidShapes.fluidLiquid, 1_000))
            .duration(1 * MINUTES)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.LowQualityNaquadahSolution, CellShapes.cell, 36),
                MaterialLibAPI.getStack(Materials.P507, CellShapes.cell, 4))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.NaquadahAdamantiumSolution, CellShapes.cell, 30),
                ItemList.Cell_Empty.get(10))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.FluorineRichWasteLiquid, FluidShapes.fluidLiquid, 10_000))
            .duration(3 * MINUTES + 20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.P507, FluidShapes.fluidLiquid, 4_000),
                MaterialLibAPI.getFluidStack(Materials.LowQualityNaquadahSolution, FluidShapes.fluidLiquid, 36_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.FluorineRichWasteLiquid, FluidShapes.fluidLiquid, 10_000),
                MaterialLibAPI.getFluidStack(Materials.NaquadahAdamantiumSolution, FluidShapes.fluidLiquid, 30_000))
            .duration(3 * MINUTES + 20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Quicklime, Shapes.dust, 40))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.FluorineRichWasteLiquid, FluidShapes.fluidLiquid, 1_500))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.WasteLiquid, FluidShapes.fluidLiquid, 1_000))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Fluorspar, Shapes.dust, 60))
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        CrackRecipeAdder.addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.WasteLiquid, FluidShapes.fluidLiquid, 10_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.SaltWater, FluidShapes.fluidLiquid, 3_000),
                MaterialLibAPI.getFluidStack(Materials.Phenol, FluidShapes.fluidLiquid, 2_000),
                MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 5_000) },
            MaterialLibAPI.getStack(Materials.Chrome, Shapes.dust, 3),
            15 * SECONDS,
            TierEU.RECIPE_HV);

        GTValues.RA.stdBuilder()
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Adamantine, Shapes.dust, 4),
                MaterialLibAPI.getStack(Materials.NaquadahOxideMixture, Shapes.dust, 2),
                MaterialLibAPI.getStack(Materials.ConcentratedEnrichedNaquadahSludge, Shapes.dust, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.NaquadahAdamantiumSolution, FluidShapes.fluidLiquid, 3_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.NaquadahRichSolution, FluidShapes.fluidLiquid, 2_000))
            .duration(5 * SECONDS)
            .eut(0)
            .metadata(NKE_RANGE, computeRangeNKE(230, 200))
            .addTo(neutronActivatorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Shapes.dust, 27))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.NaquadahRichSolution, FluidShapes.fluidLiquid, 5_000))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Naquadahine, Shapes.dust, 30))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.P507, FluidShapes.fluidLiquid, 1_000))
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(autoclaveRecipes);

        // NqO2 + C = CO2 + Nq
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Naquadahine, Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, 1))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.CarbonDioxide, FluidShapes.fluidGas, 1_000))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Naquadah, Shapes.ingotHot, 1))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .metadata(COIL_HEAT, 5000)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Shapes.dust, 27))
            .circuit(1)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.LowQualityNaquadahEmulsion, FluidShapes.fluidLiquid, 10_000))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.GalliumHydroxide, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.GalliumHydroxide, Shapes.dust, 48),
                MaterialLibAPI.getStack(Materials.Antimony, Shapes.dust, 15))
            .outputChances(6250, 6250, 10000)
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.LowQualityNaquadahSolution, FluidShapes.fluidLiquid, 9_000))
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.EnrichedNaquadahOxideMixture, Shapes.dust, 4))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.P507, FluidShapes.fluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials.SulfuricAcid, FluidShapes.fluidLiquid, 18_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.EnrichedNaquadahRichSolution, FluidShapes.fluidLiquid, 4_000),
                MaterialLibAPI.getFluidStack(Materials.WasteLiquid, FluidShapes.fluidLiquid, 1_000))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.NaquadahOxideMixture, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.TriniumSulphate, Shapes.dust, 1))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        // ZnSO4 + 2H = H2SO4 + Zn
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.ZincSulfate, Shapes.dust, 6))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 2_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.SulfuricAcid, FluidShapes.fluidLiquid, 1_000))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Zinc, Shapes.dust, 1))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Shapes.dust, 60))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.EnrichedNaquadahRichSolution, FluidShapes.fluidLiquid, 10_000))
            .itemOutputs(MaterialLibAPI.getStack(Materials.ConcentratedEnrichedNaquadahSludge, Shapes.dust, 8))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.P507, FluidShapes.fluidLiquid, 2_500))
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.ConcentratedEnrichedNaquadahSludge, Shapes.dust, 16))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.EnrichedNaquadahSulphate, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.EnrichedNaquadahSulphate, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.EnrichedNaquadahSulphate, Shapes.dust, 37),
                MaterialLibAPI.getStack(Materials.SodiumSulfate, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.SodiumSulfate, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.SodiumSulfate, Shapes.dust, 12),
                MaterialLibAPI.getStack(Materials.LowQualityNaquadriaSulphate, Shapes.dust, 2))
            .duration(6 * SECONDS)
            .eut(0)
            .metadata(NKE_RANGE, computeRangeNKE(480, 460))
            .addTo(neutronActivatorRecipes);

        // Nq+(SO4)2 + 2Zn = Nq+ + 2ZnSO4
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.EnrichedNaquadahSulphate, Shapes.dust, 11),
                MaterialLibAPI.getStack(Materials.Zinc, Shapes.dust, 2))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.NaquadahEnriched, Shapes.ingotHot, 1),
                MaterialLibAPI.getStack(Materials.ZincSulfate, Shapes.dust, 12))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .metadata(COIL_HEAT, 7500)
            .addTo(blastFurnaceRecipes);

        // KeSO4 + 2H = Ke + H2SO4
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.TriniumSulphate, Shapes.dust, 6))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 2_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.SulfuricAcid, FluidShapes.fluidLiquid, 1_000))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Trinium, Shapes.dust, 1))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.NaquadriaOxideMixture, Shapes.dust, 4))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.PhosphoricAcidGT5U, FluidShapes.fluidLiquid, 4_000))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.IndiumPhosphate, Shapes.dust, 6),
                MaterialLibAPI.getStack(Materials.LowQualityNaquadriaPhosphate, Shapes.dust, 4))
            .outputChances(2000, 10000)
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(centrifugeRecipes);

        // Ga(OH)3 + 3Na = Ga + 3NaOH
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.GalliumHydroxide, Shapes.dust, 7),
                MaterialLibAPI.getStack(Materials.Sodium, Shapes.dust, 3))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Gallium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Shapes.dust, 9))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // 2InPO4 + 3Ca = 2In + Ca3(PO4)2
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.IndiumPhosphate, Shapes.dust, 12),
                MaterialLibAPI.getStack(Materials.Calcium, Shapes.dust, 3))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Indium, Shapes.dust, 2),
                MaterialLibAPI.getStack(Materials.TricalciumPhosphate, Shapes.dust, 5))
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.LowQualityNaquadriaPhosphate, Shapes.dust, 10),
                MaterialLibAPI.getStack(Materials.SulfuricAcid, CellShapes.cell, 30))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.NaquadriaRichSolution, FluidShapes.fluidLiquid, 9_000))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Barite, Shapes.dust, 1), ItemList.Cell_Empty.get(30))
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.NaquadriaSulphate, Shapes.dust, 44),
                MaterialLibAPI.getStack(Materials.LowQualityNaquadriaSulphate, Shapes.dust, 6))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.NaquadriaRichSolution, FluidShapes.fluidLiquid, 9_000))
            .duration(5 * SECONDS)
            .eut(0)
            .metadata(NKE_RANGE, computeRangeNKE(1100, 1050))
            .addTo(neutronActivatorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.LowQualityNaquadriaSulphate, Shapes.dust, 3),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Water, 3))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.P507, FluidShapes.fluidLiquid, 500))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.LowQualityNaquadriaSulphate, FluidShapes.fluidLiquid, 3_500))
            .itemOutputs(ItemList.Cell_Empty.get(3))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(UniversalChemical);

        CrackRecipeAdder.addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.LowQualityNaquadriaSulphate, FluidShapes.fluidLiquid, 7_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.P507, FluidShapes.fluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials.NaquadriaRichSolution, FluidShapes.fluidLiquid, 5_400),
                MaterialLibAPI.getFluidStack(Materials.DilutedSulfuricAcid, FluidShapes.fluidLiquid, 12_000) },
            MaterialLibAPI.getStack(Materials.EnrichedNaquadahOxideMixture, Shapes.dust, 2),
            25 * SECONDS,
            TierEU.RECIPE_IV);

        // Nq*(SO4)2 + 2Mg = Nq* + 2MgSO4
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.NaquadriaSulphate, Shapes.dust, 11),
                MaterialLibAPI.getStack(Materials.Magnesium, Shapes.dust, 2))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Naquadria, Shapes.ingotHot, 1),
                MaterialLibAPI.getStack(Materials.MagnesiumSulphate, Shapes.dust, 12))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .metadata(COIL_HEAT, 9100)
            .addTo(blastFurnaceRecipes);

        // InPO4 + Ga(OH)3 = InGaP
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.IndiumPhosphate, Shapes.dust, 6),
                MaterialLibAPI.getStack(Materials.GalliumHydroxide, Shapes.dust, 7))
            .circuit(2)
            .itemOutputs(MaterialLibAPI.getStack(Materials.IndiumGalliumPhosphide, Shapes.dust, 3))
            .duration(15 * TICKS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Ball.get(0))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.NaquadahGoo, FluidShapes.fluidLiquid, 72))
            .itemOutputs(ItemRefer.Naquadah_Mass.get(1))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Ball.get(0))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.EnrichedNaquadahGoo, FluidShapes.fluidLiquid, 72))
            .itemOutputs(ItemRefer.Enriched_Naquadah_Mass.get(1))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Ball.get(0))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.NaquadriaGoo, FluidShapes.fluidLiquid, 72))
            .itemOutputs(ItemRefer.Naquadria_Mass.get(1))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.Naquadah_Mass.get(1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.NaquadahOxideMixture, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.EnrichedNaquadahOxideMixture, Shapes.dust, 1))
            .outputChances(10000, 100)
            .duration(5 * SECONDS)
            .eut(2)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.Enriched_Naquadah_Mass.get(1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.EnrichedNaquadahOxideMixture, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.NaquadriaOxideMixture, Shapes.dust, 1))
            .outputChances(10000, 100)
            .duration(5 * SECONDS)
            .eut(2)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.Naquadria_Mass.get(1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.NaquadriaOxideMixture, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.NaquadriaOxideMixture, Shapes.dust, 1))
            .outputChances(10000, 100)
            .duration(5 * SECONDS)
            .eut(2)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.block, Materials.SiliconSolarGrade, 16),
                MaterialLibAPI.getStack(Materials.Naquadahine, Shapes.dust, 3),
                ItemList.GalliumArsenideCrystal.get(1L))
            .itemOutputs(ItemList.Circuit_Silicon_Ingot3.get(1))
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .metadata(COIL_HEAT, 4484)
            .metadata(ADDITIVE_AMOUNT, 8000)
            .addTo(BlastFurnaceWithGas);

        // NqO2 + 4Na = 2Na2O + Nq
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Naquadahine, Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials.Sodium, Shapes.dust, 4))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Naquadah, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.SodiumOxide, Shapes.dust, 6))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.NaquadahOxideMixture, Shapes.dust, 2))
            .circuit(2)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Naquadah, Shapes.nugget, 1))
            .duration(2 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .metadata(COIL_HEAT, 5000)
            .metadata(ADDITIVE_AMOUNT, 1000)
            .addTo(BlastFurnaceWithGas);

        // C2H4 + H2O(g) = C2H6O
        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, 1_000),
                MaterialUtils.gas(Materials.Steam, 2_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Ethanol, FluidShapes.fluidLiquid, 1_000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Ethylene, 1))
            .circuit(24)
            .fluidInputs(MaterialUtils.gas(Materials.Steam, 2_000))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Ethanol, CellShapes.cell, 1))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Naquadah, Shapes.ingotHot, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Naquadah, Shapes.ingot, 1))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(vacuumFreezerRecipes);
    }
}
