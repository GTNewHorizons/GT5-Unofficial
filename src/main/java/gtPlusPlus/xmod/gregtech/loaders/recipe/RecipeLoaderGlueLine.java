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

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import bartworks.system.material.WerkstoffLoader;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.fluids.GTPPFluids;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.material.MaterialMisc;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
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
            .itemInputs(GTUtility.getIntegratedCircuit(17), GregtechItemList.BlueMetalCatalyst.get(0))
            .fluidInputs(
                FluidUtils.getFluidStack("carbonmonoxide", 1_000),
                FluidUtils.getFluidStack("methylacetate", 1_000))
            .fluidOutputs(MaterialMisc.ACETIC_ANHYDRIDE.getFluidStack(1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(CHEMPLANT_CASING_TIER, 3)
            .addTo(chemicalPlantRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(18))
            .fluidInputs(
                FluidUtils.getFluidStack("aceticacid", 1_000),
                FluidUtils.getFluidStack("chlorine", 1_000),
                MaterialMisc.ACETIC_ANHYDRIDE.getFluidStack(1_000))
            .fluidOutputs(
                MaterialMisc.CHLOROACETIC_MIXTURE.getFluidStack(1_000),
                MaterialMisc.ACETIC_ANHYDRIDE.getFluidStack(950))
            .duration(2 * MINUTES + 30 * SECONDS)
            .eut(1000)
            .metadata(CHEMPLANT_CASING_TIER, 4)
            .addTo(chemicalPlantRecipes);
        // Na2CO3 + NaCN + C2H3O2Cl + 2HCl = C3H3NO2 + 3NaCl + CO2 + H2O
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(19),
                Materials.SodiumCarbonate.getDust(6),
                MaterialMisc.SODIUM_CYANIDE.getDust(3))
            .itemOutputs(MaterialMisc.CYANOACETIC_ACID.getDust(9), Materials.Salt.getDust(6))
            .fluidInputs(
                MaterialMisc.CHLOROACETIC_ACID.getFluidStack(1_000),
                FluidUtils.getFluidStack("hydrochloricacid_gt5u", 2_000))
            .fluidOutputs(Materials.CarbonDioxide.getGas(1_000), Materials.Water.getFluid(1_000))
            .duration(20 * SECONDS)
            .eut(1000)
            .metadata(CHEMPLANT_CASING_TIER, 4)
            .addTo(chemicalPlantRecipes);
        // CuSO4 + 5C3H3NO2 + 5C2H6O = CuSO4·5(H2O) + 5C5H7NO2
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(20),
                GregtechItemList.SolidAcidCatalyst.get(0),
                MaterialMisc.COPPER_SULFATE.getDust(6),
                MaterialMisc.CYANOACETIC_ACID.getDust(45))
            .itemOutputs(MaterialMisc.COPPER_SULFATE_HYDRATED.getDust(11))
            .fluidInputs(Materials.Ethanol.getFluid(5_000))
            .fluidOutputs(MaterialMisc.ETHYL_CYANOACETATE.getFluidStack(5_000))
            .duration(8 * MINUTES + 20 * SECONDS)
            .eut(6000)
            .metadata(CHEMPLANT_CASING_TIER, 5)
            .addTo(chemicalPlantRecipes);
        // C3H3NO2 + C2H6O = C5H7NO2 + H2O
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(21), MaterialMisc.CYANOACETIC_ACID.getDust(9))
            .fluidInputs(Materials.Ethanol.getFluid(1_000))
            .fluidOutputs(MaterialMisc.ETHYL_CYANOACETATE.getFluidStack(1_000))
            .duration(16 * MINUTES + 40 * SECONDS)
            .eut(6000)
            .metadata(CHEMPLANT_CASING_TIER, 5)
            .addTo(chemicalPlantRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(22), GregtechItemList.SolidAcidCatalyst.get(0))
            .fluidInputs(
                MaterialMisc.ETHYL_CYANOACETATE.getFluidStack(100),
                new FluidStack(GTPPFluids.Formaldehyde, 100))
            .fluidOutputs(MaterialMisc.CYANOACRYLATE_POLYMER.getFluidStack(100), Materials.Water.getFluid(1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .metadata(CHEMPLANT_CASING_TIER, 5)
            .addTo(chemicalPlantRecipes);
        // CH4 + NH3 + 3O = HCN + 3H2O
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(23), GregtechItemList.PinkMetalCatalyst.get(0))
            .fluidInputs(
                Materials.Methane.getGas(2_000),
                FluidUtils.getFluidStack("ammonia", 2_000),
                Materials.Oxygen.getGas(6_000))
            .fluidOutputs(MaterialMisc.HYDROGEN_CYANIDE.getFluidStack(2_000), Materials.Water.getFluid(6_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(CHEMPLANT_CASING_TIER, 3)
            .addTo(chemicalPlantRecipes);

    }

    private static void chemicalReactorRecipes() {
        // NaOH + HCN = NaCN + H2O
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(17), Materials.SodiumHydroxide.getDust(3))
            .itemOutputs(MaterialMisc.SODIUM_CYANIDE.getDust(3))
            .fluidInputs(MaterialMisc.HYDROGEN_CYANIDE.getFluidStack(1_000))
            .fluidOutputs(Materials.Water.getFluid(1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        // #UniversalChemical recipemap won't generate LCR recipe if config >= 10
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(17), Materials.SodiumHydroxide.getDust(3))
            .itemOutputs(MaterialMisc.SODIUM_CYANIDE.getDust(3))
            .fluidInputs(MaterialMisc.HYDROGEN_CYANIDE.getFluidStack(1_000))
            .fluidOutputs(Materials.Water.getFluid(1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        // CaCO3 + 2HCl = CaCl2 + CO2 + H2O
        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(ModItems.dustCalciumCarbonate, 5), GTUtility.getIntegratedCircuit(8))
            .itemOutputs(WerkstoffLoader.CalciumChloride.get(OrePrefixes.dust, 3))
            .fluidInputs(Materials.HydrochloricAcid.getFluid(2_000))
            .fluidOutputs(Materials.CarbonDioxide.getGas(1_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Cu + 2H2SO4 = CuSO4 + SO2 + 2H2O
        // SO2 + 2H2O -> diluted sulfuric acid
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(19), ItemUtils.getItemStackOfAmountFromOreDict("dustCopper", 1))
            .itemOutputs(MaterialMisc.COPPER_SULFATE.getDust(6))
            .fluidInputs(Materials.SulfuricAcid.getFluid(2_000))
            .fluidOutputs(FluidUtils.getFluidStack("dilutedsulfuricacid", 1_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        // #UniversalChemical won't generate LCR recipe if config >= 10
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(19), ItemUtils.getItemStackOfAmountFromOreDict("dustCopper", 1))
            .itemOutputs(MaterialMisc.COPPER_SULFATE.getDust(6))
            .fluidInputs(Materials.SulfuricAcid.getFluid(2_000))
            .fluidOutputs(FluidUtils.getFluidStack("dilutedsulfuricacid", 1_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);
    }

    private static void dehydratorRecipes() {
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialMisc.COPPER_SULFATE_HYDRATED.getDust(11))
            .itemOutputs(MaterialMisc.COPPER_SULFATE.getDust(6))
            .fluidOutputs(Materials.Water.getFluid(5_000))
            .eut(10)
            .duration(5 * MINUTES)
            .addTo(chemicalDehydratorRecipes);
    }

    private static void distillationTowerRecipes() {
        GTValues.RA.stdBuilder()
            .fluidInputs(MaterialMisc.CHLOROACETIC_MIXTURE.getFluidStack(1_000))
            .fluidOutputs(
                MaterialMisc.CHLOROACETIC_ACID.getFluidStack(100),
                MaterialMisc.DICHLOROACETIC_ACID.getFluidStack(450),
                MaterialMisc.TRICHLOROACETIC_ACID.getFluidStack(450))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(distillationTowerRecipes);

    }

    private static void fluidHeaterRecipes() {

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(16))
            .fluidInputs(MaterialMisc.CYANOACRYLATE_POLYMER.getFluidStack(100))
            .fluidOutputs(MaterialMisc.ETHYL_CYANOACRYLATE.getFluidStack(100))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(fluidHeaterRecipes);
    }

    private static void mixerRecipes() {
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(1), MaterialMisc.DICHLOROACETIC_ACID.getCell(1))
            .itemOutputs(ItemList.Cell_Empty.get(1))
            .fluidInputs(MaterialMisc.TRICHLOROACETIC_ACID.getFluidStack(1_000))
            .fluidOutputs(MaterialMisc.CHLOROACETIC_MIXTURE.getFluidStack(2_000))
            .duration(5 * SECONDS)
            .eut(100)
            .addTo(mixerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemUtils.getItemStackOfAmountFromOreDict("cellSulfurTrioxide", 1),
                GTUtility.getIntegratedCircuit(2))
            .itemOutputs(ItemList.Cell_Empty.get(1))
            .fluidInputs(Materials.SulfuricAcid.getFluid(1_000))
            .fluidOutputs(MaterialMisc.SOLID_ACID_MIXTURE.getFluidStack(1_000))
            .duration(5 * SECONDS)
            .eut(40)
            .addTo(mixerRecipes);

    }

    private static void glueUsageRecipes() {
        // Braintech Tape recipe, PBI and superglue make 16 tape at once
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Polybenzimidazole, 1L),
                GTModHandler.getIC2Item("carbonMesh", 1L),
                GTUtility.getIntegratedCircuit(10))
            .itemOutputs(ItemList.Duct_Tape.get(16L))
            .fluidInputs(MaterialMisc.ETHYL_CYANOACRYLATE.getFluidStack(100))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);
        // Maintenance Hatch recipe, using Braintech Tape
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Hull_LV.get(1), ItemList.Duct_Tape.get(1), GTUtility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Hatch_Maintenance.get(1))
            .fluidInputs(MaterialMisc.ETHYL_CYANOACRYLATE.getFluidStack(100))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);
        // Graphene recipes from later wafer tiers, using superglue instead of the bronze age glue
        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.Graphite.getDust(64),
                ItemList.Circuit_Silicon_Wafer2.get(8L),
                GTUtility.getIntegratedCircuit(2))
            .itemOutputs(Materials.Graphene.getDust(64))
            .fluidInputs(MaterialMisc.ETHYL_CYANOACRYLATE.getFluidStack(1000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.Graphite.getDust(64),
                ItemList.Circuit_Silicon_Wafer3.get(2L),
                GTUtility.getIntegratedCircuit(2))
            .itemOutputs(Materials.Graphene.getDust(64))
            .fluidInputs(MaterialMisc.ETHYL_CYANOACRYLATE.getFluidStack(750))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.Graphite.getDust(64),
                Materials.Graphite.getDust(64),
                ItemList.Circuit_Silicon_Wafer4.get(1L),
                GTUtility.getIntegratedCircuit(2))
            .itemOutputs(ItemUtils.getItemStackOfAmountFromOreDict("dustGraphene", 128))
            .fluidInputs(MaterialMisc.ETHYL_CYANOACRYLATE.getFluidStack(500))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.Graphite.getDust(64),
                Materials.Graphite.getDust(64),
                Materials.Graphite.getDust(64),
                Materials.Graphite.getDust(64),
                ItemList.Circuit_Silicon_Wafer5.get(1L),
                Materials.Graphite.getDust(64),
                Materials.Graphite.getDust(64),
                Materials.Graphite.getDust(64),
                Materials.Graphite.getDust(64))
            .itemOutputs(ItemUtils.getItemStackOfAmountFromOreDict("dustGraphene", 512))
            .fluidInputs(MaterialMisc.ETHYL_CYANOACRYLATE.getFluidStack(250))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Lithium, 1L),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 4L),
                GTUtility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.SFMixture.get(32))
            .fluidInputs(MaterialMisc.ETHYL_CYANOACRYLATE.getFluidStack(100))
            .duration(1 * MINUTES + 20 * SECONDS)
            .eut(16)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.GelledToluene.get(1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Aluminium, 1L),
                GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.Polybenzimidazole, 1L),
                GTUtility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.SFMixture.get(64))
            .fluidInputs(MaterialMisc.ETHYL_CYANOACRYLATE.getFluidStack(100))
            .duration(1 * MINUTES + 20 * SECONDS)
            .eut(16)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.PolyvinylChloride, 8),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Paper, 32))
            .itemOutputs(new ItemStack(Items.book, 64, 0))
            .fluidInputs(MaterialMisc.ETHYL_CYANOACRYLATE.getFluidStack(200))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(8)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.cableGt01, Materials.Naquadah, 2),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.RedSteel, 18))
            .itemOutputs(ItemList.BatteryHull_LuV.get(1))
            .fluidInputs(MaterialMisc.ETHYL_CYANOACRYLATE.getFluidStack(144))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.cableGt01, Materials.NaquadahAlloy, 2),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Europium, 6))
            .itemOutputs(ItemList.BatteryHull_ZPM.get(1))
            .fluidInputs(MaterialMisc.ETHYL_CYANOACRYLATE.getFluidStack(288))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.cableGt01, Materials.ElectrumFlux, 2),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Americium, 18))
            .itemOutputs(ItemList.BatteryHull_UV.get(1))
            .fluidInputs(MaterialMisc.ETHYL_CYANOACRYLATE.getFluidStack(576))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.cableGt02, Materials.ElectrumFlux, 2),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Naquadah, 24))
            .itemOutputs(ItemList.BatteryHull_UHV.get(1))
            .fluidInputs(MaterialMisc.ETHYL_CYANOACRYLATE.getFluidStack(1152))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.ElectrumFlux, 2),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.NaquadahEnriched, 36))
            .itemOutputs(ItemList.BatteryHull_UEV.get(1))
            .fluidInputs(MaterialMisc.ETHYL_CYANOACRYLATE.getFluidStack(2304))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.cableGt08, Materials.ElectrumFlux, 2),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.NaquadahAlloy, 48))
            .itemOutputs(ItemList.BatteryHull_UIV.get(1))
            .fluidInputs(MaterialMisc.ETHYL_CYANOACRYLATE.getFluidStack(4608))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUHV, 2),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 56))
            .itemOutputs(ItemList.BatteryHull_UMV.get(1))
            .fluidInputs(MaterialMisc.ETHYL_CYANOACRYLATE.getFluidStack(9216))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUHV, 2),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.DraconiumAwakened, 64))
            .itemOutputs(ItemList.BatteryHull_UxV.get(1))
            .fluidInputs(MaterialMisc.ETHYL_CYANOACRYLATE.getFluidStack(18432))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(assemblerRecipes);

        if (NewHorizonsCoreMod.isModLoaded() && GalacticraftCore.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    getModItem(Mods.GalacticraftMars.ID, "item.itemBasicAsteroids", 1, 7),
                    GTOreDictUnificator.get(OrePrefixes.foil, Materials.Titanium, 8),
                    getModItem(Mods.NewHorizonsCoreMod.ID, "item.TungstenString", 8),
                    GTUtility.getIntegratedCircuit(1))
                .itemOutputs(getModItem(Mods.GalaxySpace.ID, "item.ThermalClothT2", 1))
                .fluidInputs(MaterialMisc.ETHYL_CYANOACRYLATE.getFluidStack(576))
                .duration(30 * SECONDS)
                .eut(1024)
                .addTo(assemblerRecipes);

        }
    }
}
