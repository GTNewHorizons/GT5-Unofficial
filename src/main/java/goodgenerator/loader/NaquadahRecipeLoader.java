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
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.quantumForceTransformerRecipes;

import java.util.HashSet;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import bartworks.system.material.WerkstoffLoader;
import bartworks.system.material.gtenhancement.PlatinumSludgeOverHaul;
import goodgenerator.items.GGMaterial;
import goodgenerator.main.GoodGenerator;
import goodgenerator.util.CrackRecipeAdder;
import goodgenerator.util.ItemRefer;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2CellShapes;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.common.items.CombType;
import gregtech.loaders.misc.GTBees;
import gregtech.mixin.interfaces.accessors.IRecipeMutableAccess;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public class NaquadahReworkRecipeLoader {

    public static void RecipeLoad() {

        // Naquadah (UEV)
        GTValues.RA.stdBuilder()
            .itemInputs(
                GGMaterial.naquadahEarth.get(OrePrefixes.dust, 32),
                MaterialLibAPI.getStack(Materials2Materials.Sodium, Materials2Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dust, 1))
            .itemOutputs(
                GGMaterial.inertNaquadah.get(OrePrefixes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Titanium, Materials2Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials2Materials.Adamantium, Materials2Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials2Materials.Gallium, Materials2Shapes.dust, 64))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.fluidGas, 64_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Fluorine, Materials2FluidShapes.fluidGas, 64_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.fluidGas, 100))
            .duration(10 * SECONDS)
            .eut(GTValues.VP[10])
            .metadata(QFT_CATALYST, GregtechItemList.SimpleNaquadahCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 2)
            .addTo(quantumForceTransformerRecipes);
        // Enriched Naquadah (UIV)
        GTValues.RA.stdBuilder()
            .itemInputs(
                GGMaterial.enrichedNaquadahEarth.get(OrePrefixes.dust, 32),
                MaterialLibAPI.getStack(Materials2Materials.Zinc, Materials2Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dust, 1))
            .itemOutputs(
                GGMaterial.inertEnrichedNaquadah.get(OrePrefixes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Trinium, Materials2Shapes.dust, 64))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.fluidLiquid, 16_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.fluidGas, 100))
            .fluidOutputs(GGMaterial.wasteLiquid.getFluidOrGas(32_000))
            .duration(10 * SECONDS)
            .eut(GTValues.VP[11])
            .metadata(QFT_CATALYST, GregtechItemList.SimpleNaquadahCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 2)
            .addTo(quantumForceTransformerRecipes);
        // Naquadria (UMV)
        GTValues.RA.stdBuilder()
            .itemInputs(
                GGMaterial.naquadriaEarth.get(OrePrefixes.dust, 32),
                MaterialLibAPI.getStack(Materials2Materials.Magnesium, Materials2Shapes.dust, 64))
            .itemOutputs(
                GGMaterial.inertNaquadria.get(OrePrefixes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Barium, Materials2Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials2Materials.Indium, Materials2Shapes.dust, 64),
                ItemList.NaquadriaSupersolid.get(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.PhosphoricAcidGT5U, Materials2FluidShapes.fluidLiquid, 16_000),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.fluidLiquid, 16_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.fluidGas, 100))
            .duration(5 * SECONDS)
            .eut(GTValues.VP[12])
            .metadata(QFT_CATALYST, GregtechItemList.AdvancedNaquadahCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 3)
            .addTo(quantumForceTransformerRecipes);

        // Activate Them

        GTValues.RA.stdBuilder()
            .itemInputs(
                GGMaterial.inertNaquadah.get(OrePrefixes.dust, 64),
                GGMaterial.inertNaquadah.get(OrePrefixes.dust, 32))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Nickel, Materials2Shapes.dust, 16))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Nickel, Materials2FluidShapes.fluidPlasma, 16 * INGOTS))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Naquadah, Materials2FluidShapes.fluidMolten, 144 * STACKS))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(0)
            .metadata(NKE_RANGE, computeRangeNKE(600, 500))
            .addTo(neutronActivatorRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GGMaterial.inertEnrichedNaquadah.get(OrePrefixes.dust, 64),
                GGMaterial.inertEnrichedNaquadah.get(OrePrefixes.dust, 32))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Titanium, Materials2Shapes.dust, 16))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Titanium, Materials2FluidShapes.fluidPlasma, 16 * INGOTS))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.NaquadahEnriched,
                    Materials2FluidShapes.fluidMolten,
                    144 * STACKS))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(0)
            .metadata(NKE_RANGE, computeRangeNKE(900, 850))
            .addTo(neutronActivatorRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GGMaterial.inertNaquadria.get(OrePrefixes.dust, 64),
                GGMaterial.inertNaquadria.get(OrePrefixes.dust, 32))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Americium, Materials2Shapes.dust, 16))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Americium, Materials2FluidShapes.fluidPlasma, 16 * INGOTS))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Naquadria, Materials2FluidShapes.fluidMolten, 144 * STACKS))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(0)
            .metadata(NKE_RANGE, computeRangeNKE(1100, 1080))
            .addTo(neutronActivatorRecipes);

        // Naquadah Rework Line
        GTValues.RA.stdBuilder()
            .itemInputs(GGMaterial.naquadahEarth.get(OrePrefixes.dust, 2))
            .circuit(1)
            .fluidInputs(GGMaterial.fluoroantimonicAcid.getFluidOrGas(3_000))
            .fluidOutputs(GGMaterial.lowQualityNaquadahEmulsion.getFluidOrGas(2_000))
            .itemOutputs(GGMaterial.titaniumTrifluoride.get(OrePrefixes.dust, 4))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(COIL_HEAT, 3000)
            .addTo(blastFurnaceRecipes);

        // TiF3 + 3H = Ti + 3HF
        GTValues.RA.stdBuilder()
            .itemInputs(GGMaterial.titaniumTrifluoride.get(OrePrefixes.dust, 4))
            .circuit(1)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.fluidGas, 3_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.HydrofluoricAcidGT5U, Materials2FluidShapes.fluidLiquid, 3_000))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Titanium, Materials2Shapes.ingotHot, 1))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .metadata(COIL_HEAT, 2000)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.copyAmount(0, MaterialLibAPI.getStack(Materials2Materials.Copper, Materials2Shapes.plate, 1)),
                MaterialLibAPI.getStack(Materials2Materials.Hydrogen, Materials2CellShapes.cell, 8))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.SeedOil, Materials2FluidShapes.fluidLiquid, 3_000))
            .itemOutputs(ItemList.Cell_Empty.get(8))
            .fluidOutputs(GGMaterial.towEthyl1Hexanol.getFluidOrGas(1_000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        // 2C8H18O + H3PO4 =Na,C2H6O= C16H35O3P + 2H2O
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Sodium, Materials2Shapes.dust, 2))
            .fluidInputs(
                GGMaterial.towEthyl1Hexanol.getFluidOrGas(2_000),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.PhosphoricAcidGT5U, Materials2FluidShapes.fluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethanol, Materials2FluidShapes.fluidLiquid, 2_000))
            .fluidOutputs(GGMaterial.P507.getFluidOrGas(1_000))
            .duration(1 * MINUTES)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GGMaterial.lowQualityNaquadahSolution.get(OrePrefixes.cell, 36),
                GGMaterial.P507.get(OrePrefixes.cell, 4))
            .itemOutputs(GGMaterial.naquadahAdamantiumSolution.get(OrePrefixes.cell, 30), ItemList.Cell_Empty.get(10))
            .fluidOutputs(GGMaterial.fluorineRichWasteLiquid.getFluidOrGas(10_000))
            .duration(3 * MINUTES + 20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                GGMaterial.P507.getFluidOrGas(4_000),
                GGMaterial.lowQualityNaquadahSolution.getFluidOrGas(36_000))
            .fluidOutputs(
                GGMaterial.fluorineRichWasteLiquid.getFluidOrGas(10_000),
                GGMaterial.naquadahAdamantiumSolution.getFluidOrGas(30_000))
            .duration(3 * MINUTES + 20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Quicklime, Materials2Shapes.dust, 40))
            .fluidInputs(GGMaterial.fluorineRichWasteLiquid.getFluidOrGas(1_500))
            .fluidOutputs(GGMaterial.wasteLiquid.getFluidOrGas(1_000))
            .itemOutputs(WerkstoffLoader.Fluorspar.get(OrePrefixes.dust, 60))
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        CrackRecipeAdder.addUniversalDistillationRecipe(
            GGMaterial.wasteLiquid.getFluidOrGas(10_000),
            new FluidStack[] {
                MaterialLibAPI.getFluidStack(Materials2Materials.SaltWater, Materials2FluidShapes.fluidLiquid, 3_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Phenol, Materials2FluidShapes.fluidLiquid, 2_000),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.HydrochloricAcidGT5U,
                    Materials2FluidShapes.fluidLiquid,
                    5_000) },
            MaterialLibAPI.getStack(Materials2Materials.Chrome, Materials2Shapes.dust, 3),
            15 * SECONDS,
            TierEU.RECIPE_HV);

        GTValues.RA.stdBuilder()
            .itemOutputs(
                GGMaterial.adamantine.get(OrePrefixes.dust, 4),
                GGMaterial.naquadahEarth.get(OrePrefixes.dust, 2),
                GGMaterial.concentratedEnrichedNaquadahSludge.get(OrePrefixes.dust, 1))
            .fluidInputs(GGMaterial.naquadahAdamantiumSolution.getFluidOrGas(3_000))
            .fluidOutputs(GGMaterial.naquadahRichSolution.getFluidOrGas(2_000))
            .duration(5 * SECONDS)
            .eut(0)
            .metadata(NKE_RANGE, computeRangeNKE(230, 200))
            .addTo(neutronActivatorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.SodiumHydroxideGT5U, Materials2Shapes.dust, 27))
            .fluidInputs(GGMaterial.naquadahRichSolution.getFluidOrGas(5_000))
            .itemOutputs(GGMaterial.naquadahine.get(OrePrefixes.dust, 30))
            .fluidOutputs(GGMaterial.P507.getFluidOrGas(1_000))
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(autoclaveRecipes);

        // NqO2 + C = CO2 + Nq
        GTValues.RA.stdBuilder()
            .itemInputs(
                GGMaterial.naquadahine.get(OrePrefixes.dust, 3),
                MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dust, 1))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.CarbonDioxide, Materials2FluidShapes.fluidGas, 1_000))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Naquadah, Materials2Shapes.ingotHot, 1))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .metadata(COIL_HEAT, 5000)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.SodiumHydroxideGT5U, Materials2Shapes.dust, 27))
            .circuit(1)
            .fluidInputs(GGMaterial.lowQualityNaquadahEmulsion.getFluidOrGas(10_000))
            .itemOutputs(
                GGMaterial.galliumHydroxide.get(OrePrefixes.dust, 64),
                GGMaterial.galliumHydroxide.get(OrePrefixes.dust, 48),
                MaterialLibAPI.getStack(Materials2Materials.Antimony, Materials2Shapes.dust, 15))
            .outputChances(6250, 6250, 10000)
            .fluidOutputs(GGMaterial.lowQualityNaquadahSolution.getFluidOrGas(9_000))
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GGMaterial.enrichedNaquadahEarth.get(OrePrefixes.dust, 4))
            .fluidInputs(
                GGMaterial.P507.getFluidOrGas(1_000),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.fluidLiquid, 18_000))
            .fluidOutputs(
                GGMaterial.enrichedNaquadahRichSolution.getFluidOrGas(4_000),
                GGMaterial.wasteLiquid.getFluidOrGas(1_000))
            .itemOutputs(
                GGMaterial.naquadahEarth.get(OrePrefixes.dust, 1),
                GGMaterial.triniumSulphate.get(OrePrefixes.dust, 1))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        // ZnSO4 + 2H = H2SO4 + Zn
        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.ZincSulfate.get(OrePrefixes.dust, 6))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.fluidGas, 2_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.fluidLiquid, 1_000))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Zinc, Materials2Shapes.dust, 1))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.SodiumHydroxideGT5U, Materials2Shapes.dust, 60))
            .fluidInputs(GGMaterial.enrichedNaquadahRichSolution.getFluidOrGas(10_000))
            .itemOutputs(GGMaterial.concentratedEnrichedNaquadahSludge.get(OrePrefixes.dust, 8))
            .fluidOutputs(GGMaterial.P507.getFluidOrGas(2_500))
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GGMaterial.concentratedEnrichedNaquadahSludge.get(OrePrefixes.dust, 16))
            .itemOutputs(
                GGMaterial.enrichedNaquadahSulphate.get(OrePrefixes.dust, 64),
                GGMaterial.enrichedNaquadahSulphate.get(OrePrefixes.dust, 64),
                GGMaterial.enrichedNaquadahSulphate.get(OrePrefixes.dust, 37),
                WerkstoffLoader.Sodiumsulfate.get(OrePrefixes.dust, 64),
                WerkstoffLoader.Sodiumsulfate.get(OrePrefixes.dust, 64),
                WerkstoffLoader.Sodiumsulfate.get(OrePrefixes.dust, 12),
                GGMaterial.lowQualityNaquadriaSulphate.get(OrePrefixes.dust, 2))
            .duration(6 * SECONDS)
            .eut(0)
            .metadata(NKE_RANGE, computeRangeNKE(480, 460))
            .addTo(neutronActivatorRecipes);

        // Nq+(SO4)2 + 2Zn = Nq+ + 2ZnSO4
        GTValues.RA.stdBuilder()
            .itemInputs(
                GGMaterial.enrichedNaquadahSulphate.get(OrePrefixes.dust, 11),
                MaterialLibAPI.getStack(Materials2Materials.Zinc, Materials2Shapes.dust, 2))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.NaquadahEnriched, Materials2Shapes.ingotHot, 1),
                WerkstoffLoader.ZincSulfate.get(OrePrefixes.dust, 12))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .metadata(COIL_HEAT, 7500)
            .addTo(blastFurnaceRecipes);

        // KeSO4 + 2H = Ke + H2SO4
        GTValues.RA.stdBuilder()
            .itemInputs(GGMaterial.triniumSulphate.get(OrePrefixes.dust, 6))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.fluidGas, 2_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.fluidLiquid, 1_000))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Trinium, Materials2Shapes.dust, 1))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GGMaterial.naquadriaEarth.get(OrePrefixes.dust, 4))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.PhosphoricAcidGT5U, Materials2FluidShapes.fluidLiquid, 4_000))
            .itemOutputs(
                GGMaterial.indiumPhosphate.get(OrePrefixes.dust, 6),
                GGMaterial.lowQualityNaquadriaPhosphate.get(OrePrefixes.dust, 4))
            .outputChances(2000, 10000)
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(centrifugeRecipes);

        // Ga(OH)3 + 3Na = Ga + 3NaOH
        GTValues.RA.stdBuilder()
            .itemInputs(
                GGMaterial.galliumHydroxide.get(OrePrefixes.dust, 7),
                MaterialLibAPI.getStack(Materials2Materials.Sodium, Materials2Shapes.dust, 3))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Gallium, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.SodiumHydroxideGT5U, Materials2Shapes.dust, 9))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // 2InPO4 + 3Ca = 2In + Ca3(PO4)2
        GTValues.RA.stdBuilder()
            .itemInputs(
                GGMaterial.indiumPhosphate.get(OrePrefixes.dust, 12),
                MaterialLibAPI.getStack(Materials2Materials.Calcium, Materials2Shapes.dust, 3))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Indium, Materials2Shapes.dust, 2),
                MaterialLibAPI.getStack(Materials2Materials.TricalciumPhosphate, Materials2Shapes.dust, 5))
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GGMaterial.lowQualityNaquadriaPhosphate.get(OrePrefixes.dust, 10),
                MaterialLibAPI.getStack(Materials2Materials.SulfuricAcid, Materials2CellShapes.cell, 30))
            .fluidOutputs(GGMaterial.naquadriaRichSolution.getFluidOrGas(9_000))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Barite, Materials2Shapes.dust, 1),
                ItemList.Cell_Empty.get(30))
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemOutputs(
                GGMaterial.naquadriaSulphate.get(OrePrefixes.dust, 44),
                GGMaterial.lowQualityNaquadriaSulphate.get(OrePrefixes.dust, 6))
            .fluidInputs(GGMaterial.naquadriaRichSolution.getFluidOrGas(9_000))
            .duration(5 * SECONDS)
            .eut(0)
            .metadata(NKE_RANGE, computeRangeNKE(1100, 1050))
            .addTo(neutronActivatorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GGMaterial.lowQualityNaquadriaSulphate.get(OrePrefixes.dust, 3), Materials.Water.getCells(3))
            .fluidInputs(GGMaterial.P507.getFluidOrGas(500))
            .fluidOutputs(GGMaterial.lowQualityNaquadriaSolution.getFluidOrGas(3_500))
            .itemOutputs(ItemList.Cell_Empty.get(3))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(UniversalChemical);

        CrackRecipeAdder.addUniversalDistillationRecipe(
            GGMaterial.lowQualityNaquadriaSolution.getFluidOrGas(7_000),
            new FluidStack[] { GGMaterial.P507.getFluidOrGas(1_000),
                GGMaterial.naquadriaRichSolution.getFluidOrGas(5_400),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.DilutedSulfuricAcid,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (12_000)) },
            GGMaterial.enrichedNaquadahEarth.get(OrePrefixes.dust, 2),
            25 * SECONDS,
            TierEU.RECIPE_IV);

        // Nq*(SO4)2 + 2Mg = Nq* + 2MgSO4
        GTValues.RA.stdBuilder()
            .itemInputs(
                GGMaterial.naquadriaSulphate.get(OrePrefixes.dust, 11),
                MaterialLibAPI.getStack(Materials2Materials.Magnesium, Materials2Shapes.dust, 2))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Naquadria, Materials2Shapes.ingotHot, 1),
                GGMaterial.magnesiumSulphate.get(OrePrefixes.dust, 12))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .metadata(COIL_HEAT, 9100)
            .addTo(blastFurnaceRecipes);

        // InPO4 + Ga(OH)3 = InGaP
        GTValues.RA.stdBuilder()
            .itemInputs(
                GGMaterial.indiumPhosphate.get(OrePrefixes.dust, 6),
                GGMaterial.galliumHydroxide.get(OrePrefixes.dust, 7))
            .circuit(2)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.IndiumGalliumPhosphide, Materials2Shapes.dust, 3))
            .duration(15 * TICKS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Ball.get(0))
            .fluidInputs(GGMaterial.naquadahGoo.getFluidOrGas(72))
            .itemOutputs(ItemRefer.Naquadah_Mass.get(1))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Ball.get(0))
            .fluidInputs(GGMaterial.enrichedNaquadahGoo.getFluidOrGas(72))
            .itemOutputs(ItemRefer.Enriched_Naquadah_Mass.get(1))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Ball.get(0))
            .fluidInputs(GGMaterial.naquadriaGoo.getFluidOrGas(72))
            .itemOutputs(ItemRefer.Naquadria_Mass.get(1))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.Naquadah_Mass.get(1))
            .itemOutputs(
                GGMaterial.naquadahEarth.get(OrePrefixes.dust, 1),
                GGMaterial.enrichedNaquadahEarth.get(OrePrefixes.dust, 1))
            .outputChances(10000, 100)
            .duration(5 * SECONDS)
            .eut(2)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.Enriched_Naquadah_Mass.get(1))
            .itemOutputs(
                GGMaterial.enrichedNaquadahEarth.get(OrePrefixes.dust, 1),
                GGMaterial.naquadriaEarth.get(OrePrefixes.dust, 1))
            .outputChances(10000, 100)
            .duration(5 * SECONDS)
            .eut(2)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.Naquadria_Mass.get(1))
            .itemOutputs(
                GGMaterial.naquadriaEarth.get(OrePrefixes.dust, 1),
                GGMaterial.naquadriaEarth.get(OrePrefixes.dust, 1))
            .outputChances(10000, 100)
            .duration(5 * SECONDS)
            .eut(2)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.block, Materials.SiliconSG, 16),
                GGMaterial.naquadahine.get(OrePrefixes.dust, 3),
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
                GGMaterial.naquadahine.get(OrePrefixes.dust, 3),
                MaterialLibAPI.getStack(Materials2Materials.Sodium, Materials2Shapes.dust, 4))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Naquadah, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.SodiumOxide, Materials2Shapes.dust, 6))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(GGMaterial.naquadahEarth.get(OrePrefixes.dust, 2))
            .circuit(2)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Naquadah, Materials2Shapes.nugget, 1))
            .duration(2 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .metadata(COIL_HEAT, 5000)
            .metadata(ADDITIVE_AMOUNT, 1000)
            .addTo(BlastFurnaceWithGas);

        // C2H4 + H2O(g) = C2H6O
        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethylene, Materials2FluidShapes.fluidGas, 1_000),
                Materials.Steam.getGas(2_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethanol, Materials2FluidShapes.fluidLiquid, 1_000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Ethylene, Materials2CellShapes.cell, 1))
            .circuit(24)
            .fluidInputs(Materials.Steam.getGas(2_000))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Ethanol, Materials2CellShapes.cell, 1))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Naquadah, Materials2Shapes.ingotHot, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Naquadah, Materials2Shapes.ingot, 1))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(vacuumFreezerRecipes);
    }

    public static String displayRecipe(GTRecipe recipe) {
        StringBuilder result = new StringBuilder();
        // item inputs
        result.append("Item inputs: ");
        for (ItemStack itemstack : recipe.mInputs) {
            if (itemstack == null) {
                result.append("nullstack, ");
            } else {
                result.append(itemstack.getUnlocalizedName());
                result.append(", ");
            }
        }

        // fluid inputs
        result.append(" Fluid inputs: ");
        for (FluidStack fluidStack : recipe.mFluidInputs) {
            if (fluidStack == null) {
                result.append("nullstack, ");
            } else {
                result.append(fluidStack.getUnlocalizedName());
                result.append(", ");
            }
        }

        // item outputs
        result.append(" Item outputs: ");
        for (ItemStack itemstack : recipe.mOutputs) {
            if (itemstack == null) {
                result.append("nullstack, ");
            } else {
                result.append(itemstack.getUnlocalizedName());
                result.append(", ");
            }
        }

        // fluid outputs
        result.append(" Fluid outputs: ");
        for (FluidStack fluidStack : recipe.mFluidOutputs) {
            if (fluidStack == null) {
                result.append("nullstack, ");
            } else {
                result.append(fluidStack.getUnlocalizedName());
                result.append(", ");
            }
        }

        return result.toString();
    }

    public static void Remover() {

        GTLog.out.print(GoodGenerator.MOD_ID + ": Begin to remove pure Naquadah, Enriched Naquadah and Naquadria.\n");

        HashSet<GTRecipe> remove = new HashSet<>(5000);
        HashSet<GTRecipe> reAdd = new HashSet<>(5000);

        // For Crusher
        for (GTRecipe recipe : RecipeMaps.maceratorRecipes.getAllRecipes()) {
            ItemStack input = recipe.mInputs[0];
            if (!GTUtility.isStackValid(input)) continue;

            int[] oreDict = OreDictionary.getOreIDs(input);
            for (int oreDictID : oreDict) {
                String oreDictName = OreDictionary.getOreName(oreDictID);
                if (!((oreDictName.startsWith("ore") || oreDictName.startsWith("rawOre")
                    || oreDictName.startsWith("crushed")) && oreDictName.contains("Naq"))) {
                    continue;
                }

                processRecipe(remove, reAdd, recipe);
                break;
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
            if (!GTUtility.isStackValid(input)) continue;

            int[] oreDict = OreDictionary.getOreIDs(input);
            for (int oreDictID : oreDict) {
                String oredictName = OreDictionary.getOreName(oreDictID);
                if (!oredictName.startsWith("crushed") || !oredictName.contains("Naq")) continue;

                processRecipe(remove, reAdd, recipe);
                break;

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
            if (!GTUtility.isStackValid(input)) continue;

            int[] oreDict = OreDictionary.getOreIDs(input);
            for (int oreDictID : oreDict) {
                String oredictName = OreDictionary.getOreName(oreDictID);
                if (!oredictName.startsWith("crushed") || !oredictName.contains("Naq")) continue;

                processRecipe(remove, reAdd, recipe);
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
            if (!GTUtility.isStackValid(input)) continue;

            int[] oreDict = OreDictionary.getOreIDs(input);
            if (checkCombs && input.isItemEqual(GTBees.combs.getStackForType(CombType.DOB))) {
                GTRecipe tRecipe = recipe.copy();
                boolean modified = false;
                for (int i = 0; i < tRecipe.mOutputs.length; i++) {
                    if (!GTUtility.isStackValid(tRecipe.mOutputs[i])) continue;
                    if (tRecipe.mOutputs[i].isItemEqual(
                        MaterialLibAPI.getStack(Materials2Materials.Naquadah, Materials2Shapes.dustTiny, (int) (1)))) {
                        tRecipe.mOutputs[i] = GTUtility.copyAmount(
                            tRecipe.mOutputs[i].stackSize * 2L,
                            GGMaterial.naquadahEarth.get(OrePrefixes.dustTiny, 1));
                        modified = true;
                    }
                }
                if (modified) {
                    GTLog.err.println("recipe edited: " + displayRecipe(tRecipe));
                    reAdd.add(tRecipe);
                    remove.add(recipe);
                }
                continue;
            }

            for (int oreDictID : oreDict) {
                String oredictName = OreDictionary.getOreName(oreDictID);
                if (!oredictName.startsWith("dustPureNaq") && !oredictName.startsWith("dustImpureNaq")
                    && !oredictName.startsWith("dustSpace")
                    && !oredictName.startsWith("dustNaq")) {
                    continue;
                }
                GTRecipe tRecipe = recipe.copy();
                boolean modified = false;
                for (int i = 0; i < tRecipe.mOutputs.length; i++) {
                    if (!GTUtility.isStackValid(tRecipe.mOutputs[i])) continue;
                    if (tRecipe.mOutputs[i].isItemEqual(
                        MaterialLibAPI.getStack(Materials2Materials.Naquadah, Materials2Shapes.dustTiny, (int) (1)))) {
                        tRecipe.mOutputs[i] = GTUtility.copyAmount(
                            tRecipe.mOutputs[i].stackSize * 2,
                            GGMaterial.naquadahEarth.get(OrePrefixes.dustTiny, 1));
                    } else if (tRecipe.mOutputs[i].isItemEqual(
                        MaterialLibAPI
                            .getStack(Materials2Materials.NaquadahEnriched, Materials2Shapes.dustTiny, (int) (1)))) {
                                tRecipe.mOutputs[i] = GTUtility.copyAmount(
                                    tRecipe.mOutputs[i].stackSize * 2,
                                    GGMaterial.enrichedNaquadahEarth.get(OrePrefixes.dustTiny, 1));
                            } else
                        if (tRecipe.mOutputs[i].isItemEqual(
                            MaterialLibAPI
                                .getStack(Materials2Materials.Naquadria, Materials2Shapes.dustTiny, (int) (1)))) {
                                    tRecipe.mOutputs[i] = GTUtility.copyAmount(
                                        tRecipe.mOutputs[i].stackSize * 2,
                                        GGMaterial.naquadriaEarth.get(OrePrefixes.dustTiny, 1));
                                } else
                            if (tRecipe.mOutputs[i].isItemEqual(
                                MaterialLibAPI
                                    .getStack(Materials2Materials.Naquadah, Materials2Shapes.dust, (int) (1)))) {
                                        tRecipe.mOutputs[i] = GTUtility.copyAmount(
                                            tRecipe.mOutputs[i].stackSize * 2,
                                            GGMaterial.naquadahEarth.get(OrePrefixes.dust, 1));
                                    } else
                                if (tRecipe.mOutputs[i].isItemEqual(
                                    MaterialLibAPI.getStack(
                                        Materials2Materials.NaquadahEnriched,
                                        Materials2Shapes.dust,
                                        (int) (1)))) {
                                            tRecipe.mOutputs[i] = GTUtility.copyAmount(
                                                tRecipe.mOutputs[i].stackSize * 2,
                                                GGMaterial.enrichedNaquadahEarth.get(OrePrefixes.dust, 1));
                                        } else
                                    if (tRecipe.mOutputs[i].isItemEqual(
                                        MaterialLibAPI.getStack(
                                            Materials2Materials.Naquadria,
                                            Materials2Shapes.dust,
                                            (int) (1)))) {
                                                tRecipe.mOutputs[i] = GTUtility.copyAmount(
                                                    tRecipe.mOutputs[i].stackSize * 2,
                                                    GGMaterial.naquadriaEarth.get(OrePrefixes.dust, 1));
                                            } else
                                        if (tRecipe.mOutputs[i].isItemEqual(
                                            MaterialLibAPI.getStack(
                                                Materials2Materials.Naquadah,
                                                Materials2Shapes.dustSmall,
                                                (int) (1)))) {
                                                    tRecipe.mOutputs[i] = GTUtility.copyAmount(
                                                        tRecipe.mOutputs[i].stackSize * 2,
                                                        GGMaterial.naquadahEarth.get(OrePrefixes.dustSmall, 1));
                                                } else
                                            if (tRecipe.mOutputs[i].isItemEqual(
                                                MaterialLibAPI.getStack(
                                                    Materials2Materials.NaquadahEnriched,
                                                    Materials2Shapes.dustSmall,
                                                    (int) (1)))) {
                                                        tRecipe.mOutputs[i] = GTUtility.copyAmount(
                                                            tRecipe.mOutputs[i].stackSize * 2,
                                                            GGMaterial.enrichedNaquadahEarth
                                                                .get(OrePrefixes.dustSmall, 1));
                                                    } else
                                                if (tRecipe.mOutputs[i].isItemEqual(
                                                    MaterialLibAPI.getStack(
                                                        Materials2Materials.Naquadria,
                                                        Materials2Shapes.dustSmall,
                                                        (int) (1)))) {
                                                            tRecipe.mOutputs[i] = GTUtility.copyAmount(
                                                                tRecipe.mOutputs[i].stackSize * 2,
                                                                GGMaterial.naquadriaEarth
                                                                    .get(OrePrefixes.dustSmall, 1));
                                                        } else {
                                                            continue;
                                                        }

                    modified = true;
                }
                if (modified) {
                    GTLog.err.println("recipe edited: " + displayRecipe(tRecipe));
                    reAdd.add(tRecipe);
                    remove.add(recipe);
                }
                break;

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

        // For Hammer
        for (GTRecipe recipe : RecipeMaps.hammerRecipes.getAllRecipes()) {
            ItemStack input = recipe.mInputs[0];
            if (!GTUtility.isStackValid(input)) continue;

            int[] oreDict = OreDictionary.getOreIDs(input);
            for (int oreDictID : oreDict) {
                String oredictName = OreDictionary.getOreName(oreDictID);
                if (!oredictName.startsWith("crushed") || !oredictName.contains("Naq")) continue;
                processRecipe(remove, reAdd, recipe);
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
                if (recipe.mFluidOutputs[i].isFluidEqual(
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.Naquadah, Materials2FluidShapes.fluidMolten, (int) (1)))
                    || recipe.mFluidOutputs[i].isFluidEqual(
                        MaterialLibAPI.getFluidStack(
                            Materials2Materials.NaquadahEnriched,
                            Materials2FluidShapes.fluidMolten,
                            (int) (1)))
                    || recipe.mFluidOutputs[i].isFluidEqual(
                        MaterialLibAPI.getFluidStack(
                            Materials2Materials.Naquadria,
                            Materials2FluidShapes.fluidMolten,
                            (int) (1)))) {
                    isAny = true;
                    break;
                }
            }
            if (!isAny) continue;
            GTRecipe tRecipe = recipe.copy();
            boolean modified = false;
            for (int i = 0; i < recipe.mFluidOutputs.length; i++) {
                if (recipe.mFluidOutputs[i] == null) continue;
                if (recipe.mFluidOutputs[i].isFluidEqual(
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.Naquadah, Materials2FluidShapes.fluidMolten, (int) (1)))) {
                    tRecipe.mFluidOutputs[i] = GGMaterial.naquadahGoo.getFluidOrGas(recipe.mFluidOutputs[i].amount * 2);
                    modified = true;
                } else if (recipe.mFluidOutputs[i].isFluidEqual(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.NaquadahEnriched,
                        Materials2FluidShapes.fluidMolten,
                        (int) (1)))) {
                            tRecipe.mFluidOutputs[i] = GGMaterial.enrichedNaquadahGoo
                                .getFluidOrGas(recipe.mFluidOutputs[i].amount * 2);
                            modified = true;
                        } else
                    if (recipe.mFluidOutputs[i].isFluidEqual(
                        MaterialLibAPI.getFluidStack(
                            Materials2Materials.Naquadria,
                            Materials2FluidShapes.fluidMolten,
                            (int) (1)))) {
                                tRecipe.mFluidOutputs[i] = GGMaterial.naquadriaGoo
                                    .getFluidOrGas(recipe.mFluidOutputs[i].amount * 2);
                                modified = true;
                            }
            }
            if (modified) {
                GTLog.err.println("recipe edited: " + displayRecipe(tRecipe));
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
                if (recipe.mFluidOutputs[i].isFluidEqual(
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.Naquadah, Materials2FluidShapes.fluidMolten, (int) (1)))
                    || recipe.mFluidOutputs[i].isFluidEqual(
                        MaterialLibAPI.getFluidStack(
                            Materials2Materials.NaquadahEnriched,
                            Materials2FluidShapes.fluidMolten,
                            (int) (1)))
                    || recipe.mFluidOutputs[i].isFluidEqual(
                        MaterialLibAPI.getFluidStack(
                            Materials2Materials.Naquadria,
                            Materials2FluidShapes.fluidMolten,
                            (int) (1)))) {
                    isAny = true;
                    break;
                }
            }
            if (!isAny) continue;
            GTRecipe tRecipe = recipe.copy();
            boolean modified = false;
            for (int i = 0; i < recipe.mFluidOutputs.length; i++) {
                if (recipe.mFluidOutputs[i] == null) continue;
                if (recipe.mFluidOutputs[i].isFluidEqual(
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.Naquadah, Materials2FluidShapes.fluidMolten, (int) (1)))) {
                    tRecipe.mFluidOutputs[i] = GGMaterial.naquadahGoo.getFluidOrGas(recipe.mFluidOutputs[i].amount * 2);
                    modified = true;
                } else if (recipe.mFluidOutputs[i].isFluidEqual(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.NaquadahEnriched,
                        Materials2FluidShapes.fluidMolten,
                        (int) (1)))) {
                            tRecipe.mFluidOutputs[i] = GGMaterial.enrichedNaquadahGoo
                                .getFluidOrGas(recipe.mFluidOutputs[i].amount * 2);
                            modified = true;
                        } else
                    if (recipe.mFluidOutputs[i].isFluidEqual(
                        MaterialLibAPI.getFluidStack(
                            Materials2Materials.Naquadria,
                            Materials2FluidShapes.fluidMolten,
                            (int) (1)))) {
                                tRecipe.mFluidOutputs[i] = GGMaterial.naquadriaGoo
                                    .getFluidOrGas(recipe.mFluidOutputs[i].amount * 2);
                                modified = true;
                            }
            }
            if (modified) {
                GTLog.err.println("recipe edited: " + displayRecipe(tRecipe));
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

        // For Simple Washer
        for (GTRecipe recipe : GTPPRecipeMaps.simpleWasherRecipes.getAllRecipes()) {
            ItemStack input = recipe.mInputs[0];
            if (!GTUtility.isStackValid(input)) continue;
            int[] oreDict = OreDictionary.getOreIDs(input);
            for (int oreDictID : oreDict) {
                String oredictName = OreDictionary.getOreName(oreDictID);
                if (!oredictName.startsWith("dustImpureNaq") && !oredictName.startsWith("dustPureNaq")) continue;

                GTRecipe tRecipe = recipe.copy();
                boolean modified = false;
                for (int i = 0; i < tRecipe.mOutputs.length; i++) {
                    if (!GTUtility.isStackValid(tRecipe.mOutputs[i])) continue;
                    if (tRecipe.mOutputs[i].isItemEqual(
                        MaterialLibAPI.getStack(Materials2Materials.Naquadah, Materials2Shapes.dust, (int) (1)))) {
                        tRecipe.mOutputs[i] = GTUtility.copyAmount(
                            tRecipe.mOutputs[i].stackSize,
                            GGMaterial.naquadahEarth.get(OrePrefixes.dust, 1));
                        modified = true;
                    } else if (tRecipe.mOutputs[i].isItemEqual(
                        MaterialLibAPI
                            .getStack(Materials2Materials.NaquadahEnriched, Materials2Shapes.dust, (int) (1)))) {
                                tRecipe.mOutputs[i] = GTUtility.copyAmount(
                                    tRecipe.mOutputs[i].stackSize,
                                    GGMaterial.enrichedNaquadahEarth.get(OrePrefixes.dust, 1));
                                modified = true;
                            } else
                        if (tRecipe.mOutputs[i].isItemEqual(
                            MaterialLibAPI.getStack(Materials2Materials.Naquadria, Materials2Shapes.dust, (int) (1)))) {
                                tRecipe.mOutputs[i] = GTUtility.copyAmount(
                                    tRecipe.mOutputs[i].stackSize,
                                    GGMaterial.naquadriaEarth.get(OrePrefixes.dust, 1));
                                modified = true;
                            }
                }
                if (modified) {
                    GTLog.err.println("recipe edited: " + displayRecipe(tRecipe));
                    reAdd.add(tRecipe);
                    remove.add(recipe);
                }
                break;

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
        registerCauldronCleaningFor(Materials.Naquadah, GGMaterial.naquadahEarth.getBridgeMaterial());
        registerCauldronCleaningFor(Materials.NaquadahEnriched, GGMaterial.enrichedNaquadahEarth.getBridgeMaterial());
        registerCauldronCleaningFor(Materials.Naquadria, GGMaterial.naquadriaEarth.getBridgeMaterial());
        GTLog.out.print(GoodGenerator.MOD_ID + ": Replace 3! ");
        GTLog.out.print("Cauldron Wash done!\n");

        // For Crafting Table
        CraftingManager.getInstance()
            .getRecipeList()
            .forEach(NaquadahReworkRecipeLoader::replaceInCraftTable);

        GTLog.out.print(GoodGenerator.MOD_ID + ": Replace Unknown! ");
        GTLog.out.print("Crafting Table done!\n");
    }

    private static void processRecipe(HashSet<GTRecipe> remove, HashSet<GTRecipe> reAdd, GTRecipe recipe) {
        GTRecipe tRecipe = recipe.copy();
        boolean modified = false;
        for (int i = 0; i < tRecipe.mOutputs.length; i++) {
            if (!GTUtility.isStackValid(tRecipe.mOutputs[i])) continue;
            if (tRecipe.mOutputs[i]
                .isItemEqual(MaterialLibAPI.getStack(Materials2Materials.Naquadah, Materials2Shapes.dust, (int) (1)))) {
                tRecipe.mOutputs[i] = GTUtility
                    .copyAmount(tRecipe.mOutputs[i].stackSize * 2, GGMaterial.naquadahEarth.get(OrePrefixes.dust, 1));
                modified = true;
            } else if (tRecipe.mOutputs[i].isItemEqual(
                MaterialLibAPI.getStack(Materials2Materials.NaquadahEnriched, Materials2Shapes.dust, (int) (1)))) {
                    tRecipe.mOutputs[i] = GTUtility.copyAmount(
                        tRecipe.mOutputs[i].stackSize * 2,
                        GGMaterial.enrichedNaquadahEarth.get(OrePrefixes.dust, 1));
                    modified = true;
                } else if (tRecipe.mOutputs[i].isItemEqual(
                    MaterialLibAPI.getStack(Materials2Materials.Naquadria, Materials2Shapes.dust, (int) (1)))) {
                        tRecipe.mOutputs[i] = GTUtility.copyAmount(
                            tRecipe.mOutputs[i].stackSize * 2,
                            GGMaterial.naquadriaEarth.get(OrePrefixes.dust, 1));
                        modified = true;
                    }
        }
        if (modified) {
            GTLog.err.println("recipe edited: " + displayRecipe(tRecipe));
            reAdd.add(tRecipe);
            remove.add(recipe);
        }
    }

    public static void replaceInCraftTable(Object obj) {
        IRecipe recipe = (IRecipe) obj;
        ItemStack result = recipe.getRecipeOutput();
        if (!(recipe instanceof IRecipeMutableAccess mutableRecipe)) {
            return;
        }

        Object input = mutableRecipe.gt5u$getRecipeInputs();

        if (GTUtility.areStacksEqual(
            result,
            MaterialLibAPI.getStack(Materials2Materials.Naquadah, Materials2Shapes.dust, (int) (1)),
            true)) {
            if (PlatinumSludgeOverHaul.checkRecipe(input, Materials.Naquadah)) {
                return;
            }
            mutableRecipe.gt5u$setRecipeOutputItem(GGMaterial.naquadahEarth.get(OrePrefixes.dust, 2));
        } else if (GTUtility.areStacksEqual(
            result,
            MaterialLibAPI.getStack(Materials2Materials.NaquadahEnriched, Materials2Shapes.dust, (int) (1)),
            true)) {
                if (PlatinumSludgeOverHaul.checkRecipe(input, Materials.NaquadahEnriched)) {
                    return;
                }
                mutableRecipe.gt5u$setRecipeOutputItem(GGMaterial.enrichedNaquadahEarth.get(OrePrefixes.dust, 2));
            } else if (GTUtility.areStacksEqual(
                result,
                MaterialLibAPI.getStack(Materials2Materials.Naquadria, Materials2Shapes.dust, (int) (1)),
                true)) {
                    if (PlatinumSludgeOverHaul.checkRecipe(input, Materials.Naquadria)) {
                        return;
                    }
                    mutableRecipe.gt5u$setRecipeOutputItem(GGMaterial.naquadriaEarth.get(OrePrefixes.dust, 2));
                }
    }
}
