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

import bartworks.system.material.gtenhancement.PlatinumSludgeOverHaul;
import goodgenerator.main.GoodGenerator;
import goodgenerator.util.CrackRecipeAdder;
import goodgenerator.util.ItemRefer;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.CellShapes;
import gregtech.api.enums.materials.FluidShapes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.material.MaterialUtils;
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
                MaterialLibAPI.getStack(Materials.Hydrogen, CellShapes.cell, 8))
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
            .itemInputs(MaterialLibAPI.getStack(Materials.Ethylene, CellShapes.cell, 1))
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
                    if (tRecipe.mOutputs[i]
                        .isItemEqual(MaterialLibAPI.getStack(Materials.Naquadah, Shapes.dustTiny, 1))) {
                        tRecipe.mOutputs[i] = GTUtility.copyAmount(
                            tRecipe.mOutputs[i].stackSize * 2L,
                            MaterialLibAPI.getStack(Materials.NaquadahOxideMixture, Shapes.dustTiny, 1));
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
                    if (tRecipe.mOutputs[i]
                        .isItemEqual(MaterialLibAPI.getStack(Materials.Naquadah, Shapes.dustTiny, 1))) {
                        tRecipe.mOutputs[i] = GTUtility.copyAmount(
                            tRecipe.mOutputs[i].stackSize * 2,
                            MaterialLibAPI.getStack(Materials.NaquadahOxideMixture, Shapes.dustTiny, 1));
                    } else if (tRecipe.mOutputs[i]
                        .isItemEqual(MaterialLibAPI.getStack(Materials.NaquadahEnriched, Shapes.dustTiny, 1))) {
                            tRecipe.mOutputs[i] = GTUtility.copyAmount(
                                tRecipe.mOutputs[i].stackSize * 2,
                                MaterialLibAPI.getStack(Materials.EnrichedNaquadahOxideMixture, Shapes.dustTiny, 1));
                        } else if (tRecipe.mOutputs[i]
                            .isItemEqual(MaterialLibAPI.getStack(Materials.Naquadria, Shapes.dustTiny, 1))) {
                                tRecipe.mOutputs[i] = GTUtility.copyAmount(
                                    tRecipe.mOutputs[i].stackSize * 2,
                                    MaterialLibAPI.getStack(Materials.NaquadriaOxideMixture, Shapes.dustTiny, 1));
                            } else if (tRecipe.mOutputs[i]
                                .isItemEqual(MaterialLibAPI.getStack(Materials.Naquadah, Shapes.dust, 1))) {
                                    tRecipe.mOutputs[i] = GTUtility.copyAmount(
                                        tRecipe.mOutputs[i].stackSize * 2,
                                        MaterialLibAPI.getStack(Materials.NaquadahOxideMixture, Shapes.dust, 1));
                                } else if (tRecipe.mOutputs[i]
                                    .isItemEqual(MaterialLibAPI.getStack(Materials.NaquadahEnriched, Shapes.dust, 1))) {
                                        tRecipe.mOutputs[i] = GTUtility.copyAmount(
                                            tRecipe.mOutputs[i].stackSize * 2,
                                            MaterialLibAPI
                                                .getStack(Materials.EnrichedNaquadahOxideMixture, Shapes.dust, 1));
                                    } else if (tRecipe.mOutputs[i]
                                        .isItemEqual(MaterialLibAPI.getStack(Materials.Naquadria, Shapes.dust, 1))) {
                                            tRecipe.mOutputs[i] = GTUtility.copyAmount(
                                                tRecipe.mOutputs[i].stackSize * 2,
                                                MaterialLibAPI
                                                    .getStack(Materials.NaquadriaOxideMixture, Shapes.dust, 1));
                                        } else if (tRecipe.mOutputs[i].isItemEqual(
                                            MaterialLibAPI.getStack(Materials.Naquadah, Shapes.dustSmall, 1))) {
                                                tRecipe.mOutputs[i] = GTUtility.copyAmount(
                                                    tRecipe.mOutputs[i].stackSize * 2,
                                                    MaterialLibAPI
                                                        .getStack(Materials.NaquadahOxideMixture, Shapes.dustSmall, 1));
                                            } else if (tRecipe.mOutputs[i].isItemEqual(
                                                MaterialLibAPI
                                                    .getStack(Materials.NaquadahEnriched, Shapes.dustSmall, 1))) {
                                                        tRecipe.mOutputs[i] = GTUtility.copyAmount(
                                                            tRecipe.mOutputs[i].stackSize * 2,
                                                            MaterialLibAPI.getStack(
                                                                Materials.EnrichedNaquadahOxideMixture,
                                                                Shapes.dustSmall,
                                                                1));
                                                    } else
                                                if (tRecipe.mOutputs[i].isItemEqual(
                                                    MaterialLibAPI
                                                        .getStack(Materials.Naquadria, Shapes.dustSmall, 1))) {
                                                            tRecipe.mOutputs[i] = GTUtility.copyAmount(
                                                                tRecipe.mOutputs[i].stackSize * 2,
                                                                MaterialLibAPI.getStack(
                                                                    Materials.NaquadriaOxideMixture,
                                                                    Shapes.dustSmall,
                                                                    1));
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
                if (recipe.mFluidOutputs[i]
                    .isFluidEqual(MaterialLibAPI.getFluidStack(Materials.Naquadah, FluidShapes.fluidMolten, 1))
                    || recipe.mFluidOutputs[i].isFluidEqual(
                        MaterialLibAPI.getFluidStack(Materials.NaquadahEnriched, FluidShapes.fluidMolten, 1))
                    || recipe.mFluidOutputs[i]
                        .isFluidEqual(MaterialLibAPI.getFluidStack(Materials.Naquadria, FluidShapes.fluidMolten, 1))) {
                    isAny = true;
                    break;
                }
            }
            if (!isAny) continue;
            GTRecipe tRecipe = recipe.copy();
            boolean modified = false;
            for (int i = 0; i < recipe.mFluidOutputs.length; i++) {
                if (recipe.mFluidOutputs[i] == null) continue;
                if (recipe.mFluidOutputs[i]
                    .isFluidEqual(MaterialLibAPI.getFluidStack(Materials.Naquadah, FluidShapes.fluidMolten, 1))) {
                    tRecipe.mFluidOutputs[i] = MaterialLibAPI.getFluidStack(
                        Materials.NaquadahGoo,
                        FluidShapes.fluidLiquid,
                        (int) (recipe.mFluidOutputs[i].amount * 2));
                    modified = true;
                } else if (recipe.mFluidOutputs[i].isFluidEqual(
                    MaterialLibAPI.getFluidStack(Materials.NaquadahEnriched, FluidShapes.fluidMolten, 1))) {
                        tRecipe.mFluidOutputs[i] = MaterialLibAPI.getFluidStack(
                            Materials.EnrichedNaquadahGoo,
                            FluidShapes.fluidLiquid,
                            (int) (recipe.mFluidOutputs[i].amount * 2));
                        modified = true;
                    } else if (recipe.mFluidOutputs[i]
                        .isFluidEqual(MaterialLibAPI.getFluidStack(Materials.Naquadria, FluidShapes.fluidMolten, 1))) {
                            tRecipe.mFluidOutputs[i] = MaterialLibAPI.getFluidStack(
                                Materials.NaquadriaGoo,
                                FluidShapes.fluidLiquid,
                                (int) (recipe.mFluidOutputs[i].amount * 2));
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
                if (recipe.mFluidOutputs[i]
                    .isFluidEqual(MaterialLibAPI.getFluidStack(Materials.Naquadah, FluidShapes.fluidMolten, 1))
                    || recipe.mFluidOutputs[i].isFluidEqual(
                        MaterialLibAPI.getFluidStack(Materials.NaquadahEnriched, FluidShapes.fluidMolten, 1))
                    || recipe.mFluidOutputs[i]
                        .isFluidEqual(MaterialLibAPI.getFluidStack(Materials.Naquadria, FluidShapes.fluidMolten, 1))) {
                    isAny = true;
                    break;
                }
            }
            if (!isAny) continue;
            GTRecipe tRecipe = recipe.copy();
            boolean modified = false;
            for (int i = 0; i < recipe.mFluidOutputs.length; i++) {
                if (recipe.mFluidOutputs[i] == null) continue;
                if (recipe.mFluidOutputs[i]
                    .isFluidEqual(MaterialLibAPI.getFluidStack(Materials.Naquadah, FluidShapes.fluidMolten, 1))) {
                    tRecipe.mFluidOutputs[i] = MaterialLibAPI.getFluidStack(
                        Materials.NaquadahGoo,
                        FluidShapes.fluidLiquid,
                        (int) (recipe.mFluidOutputs[i].amount * 2));
                    modified = true;
                } else if (recipe.mFluidOutputs[i].isFluidEqual(
                    MaterialLibAPI.getFluidStack(Materials.NaquadahEnriched, FluidShapes.fluidMolten, 1))) {
                        tRecipe.mFluidOutputs[i] = MaterialLibAPI.getFluidStack(
                            Materials.EnrichedNaquadahGoo,
                            FluidShapes.fluidLiquid,
                            (int) (recipe.mFluidOutputs[i].amount * 2));
                        modified = true;
                    } else if (recipe.mFluidOutputs[i]
                        .isFluidEqual(MaterialLibAPI.getFluidStack(Materials.Naquadria, FluidShapes.fluidMolten, 1))) {
                            tRecipe.mFluidOutputs[i] = MaterialLibAPI.getFluidStack(
                                Materials.NaquadriaGoo,
                                FluidShapes.fluidLiquid,
                                (int) (recipe.mFluidOutputs[i].amount * 2));
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
                    if (tRecipe.mOutputs[i].isItemEqual(MaterialLibAPI.getStack(Materials.Naquadah, Shapes.dust, 1))) {
                        tRecipe.mOutputs[i] = GTUtility.copyAmount(
                            tRecipe.mOutputs[i].stackSize,
                            MaterialLibAPI.getStack(Materials.NaquadahOxideMixture, Shapes.dust, 1));
                        modified = true;
                    } else if (tRecipe.mOutputs[i]
                        .isItemEqual(MaterialLibAPI.getStack(Materials.NaquadahEnriched, Shapes.dust, 1))) {
                            tRecipe.mOutputs[i] = GTUtility.copyAmount(
                                tRecipe.mOutputs[i].stackSize,
                                MaterialLibAPI.getStack(Materials.EnrichedNaquadahOxideMixture, Shapes.dust, 1));
                            modified = true;
                        } else if (tRecipe.mOutputs[i]
                            .isItemEqual(MaterialLibAPI.getStack(Materials.Naquadria, Shapes.dust, 1))) {
                                tRecipe.mOutputs[i] = GTUtility.copyAmount(
                                    tRecipe.mOutputs[i].stackSize,
                                    MaterialLibAPI.getStack(Materials.NaquadriaOxideMixture, Shapes.dust, 1));
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
        registerCauldronCleaningFor(Materials.Naquadah, Materials.NaquadahOxideMixture);
        registerCauldronCleaningFor(Materials.NaquadahEnriched, Materials.EnrichedNaquadahOxideMixture);
        registerCauldronCleaningFor(Materials.Naquadria, Materials.NaquadriaOxideMixture);
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
            if (tRecipe.mOutputs[i].isItemEqual(MaterialLibAPI.getStack(Materials.Naquadah, Shapes.dust, 1))) {
                tRecipe.mOutputs[i] = GTUtility.copyAmount(
                    tRecipe.mOutputs[i].stackSize * 2,
                    MaterialLibAPI.getStack(Materials.NaquadahOxideMixture, Shapes.dust, 1));
                modified = true;
            } else if (tRecipe.mOutputs[i]
                .isItemEqual(MaterialLibAPI.getStack(Materials.NaquadahEnriched, Shapes.dust, 1))) {
                    tRecipe.mOutputs[i] = GTUtility.copyAmount(
                        tRecipe.mOutputs[i].stackSize * 2,
                        MaterialLibAPI.getStack(Materials.EnrichedNaquadahOxideMixture, Shapes.dust, 1));
                    modified = true;
                } else
                if (tRecipe.mOutputs[i].isItemEqual(MaterialLibAPI.getStack(Materials.Naquadria, Shapes.dust, 1))) {
                    tRecipe.mOutputs[i] = GTUtility.copyAmount(
                        tRecipe.mOutputs[i].stackSize * 2,
                        MaterialLibAPI.getStack(Materials.NaquadriaOxideMixture, Shapes.dust, 1));
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

        if (GTUtility.areStacksEqual(result, MaterialLibAPI.getStack(Materials.Naquadah, Shapes.dust, 1), true)) {
            if (PlatinumSludgeOverHaul.checkRecipe(input, Materials.Naquadah)) {
                return;
            }
            mutableRecipe
                .gt5u$setRecipeOutputItem(MaterialLibAPI.getStack(Materials.NaquadahOxideMixture, Shapes.dust, 2));
        } else if (GTUtility
            .areStacksEqual(result, MaterialLibAPI.getStack(Materials.NaquadahEnriched, Shapes.dust, 1), true)) {
                if (PlatinumSludgeOverHaul.checkRecipe(input, Materials.NaquadahEnriched)) {
                    return;
                }
                mutableRecipe.gt5u$setRecipeOutputItem(
                    MaterialLibAPI.getStack(Materials.EnrichedNaquadahOxideMixture, Shapes.dust, 2));
            } else
            if (GTUtility.areStacksEqual(result, MaterialLibAPI.getStack(Materials.Naquadria, Shapes.dust, 1), true)) {
                if (PlatinumSludgeOverHaul.checkRecipe(input, Materials.Naquadria)) {
                    return;
                }
                mutableRecipe
                    .gt5u$setRecipeOutputItem(MaterialLibAPI.getStack(Materials.NaquadriaOxideMixture, Shapes.dust, 2));
            }
    }
}
