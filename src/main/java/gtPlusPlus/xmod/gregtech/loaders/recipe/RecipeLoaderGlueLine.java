package gtPlusPlus.xmod.gregtech.loaders.recipe;

import static gregtech.api.enums.Mods.GalacticraftCore;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.chemicalReactorRecipes;
import static gregtech.api.recipe.RecipeMaps.distillationTowerRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidHeaterRecipes;
import static gregtech.api.recipe.RecipeMaps.mixerRecipes;
import static gregtech.api.recipe.RecipeMaps.multiblockChemicalReactorRecipes;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.CHEMPLANT_CASING_TIER;
import static gregtech.api.util.GTRecipeConstants.UniversalChemical;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.chemicalDehydratorRecipes;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.chemicalPlantRecipes;

import gregtech.api.enums.materials2.MaterialFacades;
import gregtech.api.enums.materials2.Materials;
import gregtech.api.enums.materials2.Shapes;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.CellShapes;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.fluids.GTPPFluids;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public class RecipeLoaderGlueLine {

    public static void generate() {
        createRecipes();
    }

    private static void createRecipes() {
        chemicalPlantRecipes();
        chemicalReactorRecipes();
        dehydratorRecipes();
        distillationTowerRecipes();
        fluidHeaterRecipes();
        mixerRecipes();

        glueUsageRecipes();
    }

    private static void chemicalPlantRecipes() {
        // CO + C3H6O2 = C4H6O3
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.BlueMetalCatalyst.get(0))
            .circuit(17)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.CarbonMonoxide, Materials2FluidShapes.fluidGas, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.MethylAcetate, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(MaterialUtils.legacyGtppFluid(Materials.AceticAnhydride, 1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(CHEMPLANT_CASING_TIER, 3)
            .addTo(chemicalPlantRecipes);
        GTValues.RA.stdBuilder()
            .circuit(18)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.AceticAcid, Materials2FluidShapes.fluidLiquid, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Chlorine, Materials2FluidShapes.fluidGas, (int) (1_000)),
                MaterialUtils.legacyGtppFluid(Materials.AceticAnhydride, 1_000))
            .fluidOutputs(
                MaterialUtils.legacyGtppFluid(Materials.ChloroaceticMixture, 1_000),
                MaterialUtils.legacyGtppFluid(Materials.AceticAnhydride, 950))
            .duration(2 * MINUTES + 30 * SECONDS)
            .eut(1000)
            .metadata(CHEMPLANT_CASING_TIER, 4)
            .addTo(chemicalPlantRecipes);
        // Na2CO3 + NaCN + C2H3O2Cl + 2HCl = C3H3NO2 + 3NaCl + CO2 + H2O
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.SodiumCarbonate, Shapes.dust, (int) (6)),
                MaterialLibAPI.getStack(Materials.SodiumCyanide, Shapes.dust, 3))
            .circuit(19)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.CyanoaceticAcid, Shapes.dust, 9),
                MaterialLibAPI.getStack(Materials.Salt, Shapes.dust, (int) (6)))
            .fluidInputs(
                MaterialUtils.legacyGtppFluid(Materials.ChloroaceticAcid, 1_000),
                MaterialUtils.fluid(Materials.HydrochloricAcidGT5U, 2_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.CarbonDioxide, Materials2FluidShapes.fluidGas, (int) (1_000)),
                GTUtility.getWater(1_000))
            .duration(20 * SECONDS)
            .eut(1000)
            .metadata(CHEMPLANT_CASING_TIER, 4)
            .addTo(chemicalPlantRecipes);
        // CuSO4 + 5C3H3NO2 + 5C2H6O = CuSO4·5(H2O) + 5C5H7NO2
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.SolidAcidCatalyst.get(0),
                MaterialLibAPI.getStack(Materials.CopperIISulfate, Shapes.dust, 6),
                MaterialLibAPI.getStack(Materials.CyanoaceticAcid, Shapes.dust, 45))
            .circuit(20)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.CopperIISulfatePentahydrate, Shapes.dust, 11))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.Ethanol, Materials2FluidShapes.fluidLiquid, (int) (5_000)))
            .fluidOutputs(MaterialUtils.legacyGtppFluid(Materials.EthylCyanoacetate, 5_000))
            .duration(8 * MINUTES + 20 * SECONDS)
            .eut(6000)
            .metadata(CHEMPLANT_CASING_TIER, 5)
            .addTo(chemicalPlantRecipes);
        // C3H3NO2 + C2H6O = C5H7NO2 + H2O
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.CyanoaceticAcid, Shapes.dust, 9))
            .circuit(21)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.Ethanol, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(MaterialUtils.legacyGtppFluid(Materials.EthylCyanoacetate, 1_000))
            .duration(16 * MINUTES + 40 * SECONDS)
            .eut(6000)
            .metadata(CHEMPLANT_CASING_TIER, 5)
            .addTo(chemicalPlantRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.SolidAcidCatalyst.get(0))
            .circuit(22)
            .fluidInputs(
                MaterialUtils.legacyGtppFluid(Materials.EthylCyanoacetate, 100),
                new FluidStack(GTPPFluids.Formaldehyde, 100))
            .fluidOutputs(
                MaterialUtils.legacyGtppFluid(Materials.CyanoacrylatePolymer, 100),
                GTUtility.getWater(1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .metadata(CHEMPLANT_CASING_TIER, 5)
            .addTo(chemicalPlantRecipes);
        // CH4 + NH3 + 3O = HCN + 3H2O
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.PinkMetalCatalyst.get(0))
            .circuit(23)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.Methane, Materials2FluidShapes.fluidGas, (int) (2_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Ammonia, Materials2FluidShapes.fluidGas, (int) (2_000)),
                MaterialLibAPI.getFluidStack(Materials.Oxygen, Materials2FluidShapes.fluidGas, (int) (6_000)))
            .fluidOutputs(
                MaterialUtils.legacyGtppFluid(Materials.HydrogenCyanide, 2_000),
                GTUtility.getWater(6_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(CHEMPLANT_CASING_TIER, 3)
            .addTo(chemicalPlantRecipes);

    }

    private static void chemicalReactorRecipes() {
        // NaOH + HCN = NaCN + H2O
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.SodiumHydroxideGT5U, 3))
            .circuit(17)
            .itemOutputs(MaterialLibAPI.getStack(Materials.SodiumCyanide, Shapes.dust, 3))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.HydrogenCyanide, 1_000))
            .fluidOutputs(GTUtility.getWater(1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        // #UniversalChemical recipemap won't generate LCR recipe if config >= 10
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.SodiumHydroxideGT5U, 3))
            .circuit(17)
            .itemOutputs(MaterialLibAPI.getStack(Materials.SodiumCyanide, Shapes.dust, 3))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.HydrogenCyanide, 1_000))
            .fluidOutputs(GTUtility.getWater(1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        // CaCO3 + 2HCl = CaCl2 + CO2 + H2O
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.CalciumCarbonateDust.get(5))
            .circuit(8)
            .itemOutputs(MaterialLibAPI.getStack(Materials.CalciumChloride, Shapes.dust, 3))
            .fluidInputs(MaterialUtils.fluid(Materials.HydrochloricAcidGT5U, 2_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.CarbonDioxide, Materials2FluidShapes.fluidGas, (int) (1_000)))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Cu + 2H2SO4 = CuSO4 + SO2 + 2H2O
        // SO2 + 2H2O -> diluted sulfuric acid
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Copper, Shapes.dust, (int) (1)))
            .circuit(19)
            .itemOutputs(MaterialLibAPI.getStack(Materials.CopperIISulfate, Shapes.dust, 6))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.SulfuricAcid, Materials2FluidShapes.fluidLiquid, (int) (2_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.DilutedSulfuricAcid,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        // #UniversalChemical won't generate LCR recipe if config >= 10
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Copper, Shapes.dust, (int) (1)))
            .circuit(19)
            .itemOutputs(MaterialLibAPI.getStack(Materials.CopperIISulfate, Shapes.dust, 6))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.SulfuricAcid, Materials2FluidShapes.fluidLiquid, (int) (2_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.DilutedSulfuricAcid,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);
    }

    private static void dehydratorRecipes() {
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.CopperIISulfatePentahydrate, Shapes.dust, 11))
            .itemOutputs(MaterialLibAPI.getStack(Materials.CopperIISulfate, Shapes.dust, 6))
            .fluidOutputs(GTUtility.getWater(5_000))
            .eut(10)
            .duration(5 * MINUTES)
            .addTo(chemicalDehydratorRecipes);
    }

    private static void distillationTowerRecipes() {
        GTValues.RA.stdBuilder()
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.ChloroaceticMixture, 1_000))
            .fluidOutputs(
                MaterialUtils.legacyGtppFluid(Materials.ChloroaceticAcid, 100),
                MaterialUtils.legacyGtppFluid(Materials.DichloroaceticAcid, 450),
                MaterialUtils.legacyGtppFluid(Materials.TrichloroaceticAcid, 450))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(distillationTowerRecipes);

    }

    private static void fluidHeaterRecipes() {

        GTValues.RA.stdBuilder()
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.CyanoacrylatePolymer, 100))
            .fluidOutputs(MaterialUtils.legacyGtppFluid(Materials.EthylCyanoacrylateSuperGlue, 100))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(fluidHeaterRecipes);
    }

    private static void mixerRecipes() {
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.DichloroaceticAcid, CellShapes.cellMolten, 1))
            .circuit(1)
            .itemOutputs(ItemList.Cell_Empty.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.TrichloroaceticAcid, 1_000))
            .fluidOutputs(MaterialUtils.legacyGtppFluid(Materials.ChloroaceticMixture, 2_000))
            .duration(5 * SECONDS)
            .eut(100)
            .addTo(mixerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.SulfurTrioxide, CellShapes.cell, (int) (1)))
            .circuit(2)
            .itemOutputs(ItemList.Cell_Empty.get(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.SulfuricAcid, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(MaterialUtils.legacyGtppFluid(Materials.SolidAcidCatalystMixture, 1_000))
            .duration(5 * SECONDS)
            .eut(40)
            .addTo(mixerRecipes);

    }

    private static void glueUsageRecipes() {
        // Braintech Tape recipe, PBI and superglue make 16 tape at once
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Polybenzimidazole, Shapes.plate, (int) (1)),
                GTModHandler.getIC2Item("carbonMesh", 1L))
            .circuit(10)
            .itemOutputs(ItemList.Duct_Tape.get(16L))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.EthylCyanoacrylateSuperGlue, 100))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);
        // Maintenance Hatch recipe, using Braintech Tape
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Hull_LV.get(1), ItemList.Duct_Tape.get(1))
            .circuit(1)
            .itemOutputs(ItemList.Hatch_Maintenance.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.EthylCyanoacrylateSuperGlue, 100))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);
        // Graphene recipes from later wafer tiers, using superglue instead of the bronze age glue
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Graphite, Shapes.dust, (int) (64)),
                ItemList.Circuit_Silicon_Wafer2.get(8L))
            .circuit(2)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Graphene, Shapes.dust, (int) (64)))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.EthylCyanoacrylateSuperGlue, 1000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Graphite, Shapes.dust, (int) (64)),
                ItemList.Circuit_Silicon_Wafer3.get(2L))
            .circuit(2)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Graphene, Shapes.dust, (int) (64)))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.EthylCyanoacrylateSuperGlue, 750))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Graphite, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Graphite, Shapes.dust, (int) (64)),
                ItemList.Circuit_Silicon_Wafer4.get(1L))
            .circuit(2)
            .itemOutputs(
                GTUtility.copyAmountUnsafe(
                    128,
                    MaterialLibAPI.getStack(Materials.Graphene, Shapes.dust, (int) (1))))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.EthylCyanoacrylateSuperGlue, 500))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Graphite, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Graphite, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Graphite, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Graphite, Shapes.dust, (int) (64)),
                ItemList.Circuit_Silicon_Wafer5.get(1L),
                MaterialLibAPI.getStack(Materials.Graphite, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Graphite, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Graphite, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Graphite, Shapes.dust, (int) (64)))
            .itemOutputs(
                GTUtility.copyAmountUnsafe(
                    512,
                    MaterialLibAPI.getStack(Materials.Graphene, Shapes.dust, (int) (1))))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.EthylCyanoacrylateSuperGlue, 250))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Sulfur, Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials.Lithium, Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials.Wood, Shapes.dust, (int) (4)))
            .circuit(1)
            .itemOutputs(ItemList.SFMixture.get(32))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.EthylCyanoacrylateSuperGlue, 100))
            .duration(1 * MINUTES + 20 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.GelledToluene.get(1),
                MaterialLibAPI.getStack(Materials.Aluminium, Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials.Polybenzimidazole, Shapes.dustTiny, (int) (1)))
            .circuit(1)
            .itemOutputs(ItemList.SFMixture.get(64))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.EthylCyanoacrylateSuperGlue, 100))
            .duration(1 * MINUTES + 20 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.PolyvinylChloride, Shapes.foil, (int) (8)),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Paper, 32))
            .itemOutputs(new ItemStack(Items.book, 64, 0))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.EthylCyanoacrylateSuperGlue, 200))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.cableGt01, Materials.Naquadah, 2),
                MaterialLibAPI.getStack(Materials.RedSteel, Shapes.plate, (int) (18)))
            .itemOutputs(ItemList.BatteryHull_LuV.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.EthylCyanoacrylateSuperGlue, 144))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.cableGt01, Materials.NaquadahAlloy, 2),
                MaterialLibAPI.getStack(Materials.Europium, Shapes.plate, (int) (6)))
            .itemOutputs(ItemList.BatteryHull_ZPM.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.EthylCyanoacrylateSuperGlue, 288))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.cableGt01, Materials.ElectrumFlux, 2),
                MaterialLibAPI.getStack(Materials.Americium, Shapes.plate, (int) (18)))
            .itemOutputs(ItemList.BatteryHull_UV.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.EthylCyanoacrylateSuperGlue, 576))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.cableGt02, Materials.ElectrumFlux, 2),
                MaterialLibAPI.getStack(Materials.Naquadah, Shapes.plate, (int) (24)))
            .itemOutputs(ItemList.BatteryHull_UHV.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.EthylCyanoacrylateSuperGlue, 1152))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.ElectrumFlux, 2),
                MaterialLibAPI.getStack(Materials.NaquadahEnriched, Shapes.plate, (int) (36)))
            .itemOutputs(ItemList.BatteryHull_UEV.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.EthylCyanoacrylateSuperGlue, 2304))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.cableGt08, Materials.ElectrumFlux, 2),
                MaterialLibAPI.getStack(Materials.NaquadahAlloy, Shapes.plate, (int) (48)))
            .itemOutputs(ItemList.BatteryHull_UIV.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.EthylCyanoacrylateSuperGlue, 4608))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.wireGt16, MaterialFacades.SuperconductorUHV, 2),
                MaterialLibAPI.getStack(Materials.Neutronium, Shapes.plate, (int) (56)))
            .itemOutputs(ItemList.BatteryHull_UMV.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.EthylCyanoacrylateSuperGlue, 9216))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.wireGt16, MaterialFacades.SuperconductorUHV, 2),
                MaterialLibAPI.getStack(Materials.DraconiumAwakened, Shapes.plate, (int) (64)))
            .itemOutputs(ItemList.BatteryHull_UxV.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.EthylCyanoacrylateSuperGlue, 18432))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(assemblerRecipes);

        if (NewHorizonsCoreMod.isModLoaded() && GalacticraftCore.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    getModItem(Mods.GalacticraftMars.ID, "item.itemBasicAsteroids", 1, 7),
                    MaterialLibAPI.getStack(Materials.Titanium, Shapes.foil, (int) (8)),
                    getModItem(Mods.NewHorizonsCoreMod.ID, "TungstenString", 8))
                .circuit(1)
                .itemOutputs(getModItem(Mods.GalaxySpace.ID, "item.ThermalClothT2", 1))
                .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.EthylCyanoacrylateSuperGlue, 576))
                .duration(30 * SECONDS)
                .eut(TierEU.RECIPE_EV / 2)
                .addTo(assemblerRecipes);

        }
    }
}
