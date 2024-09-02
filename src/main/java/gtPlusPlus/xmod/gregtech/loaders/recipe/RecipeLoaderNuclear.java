package gtPlusPlus.xmod.gregtech.loaders.recipe;

import static gregtech.api.enums.GTValues.RA;
import static gregtech.api.recipe.RecipeMaps.autoclaveRecipes;
import static gregtech.api.recipe.RecipeMaps.blastFurnaceRecipes;
import static gregtech.api.recipe.RecipeMaps.centrifugeRecipes;
import static gregtech.api.recipe.RecipeMaps.chemicalBathRecipes;
import static gregtech.api.recipe.RecipeMaps.electroMagneticSeparatorRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidExtractionRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidHeaterRecipes;
import static gregtech.api.recipe.RecipeMaps.fusionRecipes;
import static gregtech.api.recipe.RecipeMaps.maceratorRecipes;
import static gregtech.api.recipe.RecipeMaps.mixerRecipes;
import static gregtech.api.recipe.RecipeMaps.multiblockChemicalReactorRecipes;
import static gregtech.api.recipe.RecipeMaps.sifterRecipes;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;
import static gregtech.api.util.GTRecipeConstants.FUSION_THRESHOLD;
import static gregtech.api.util.GTRecipeConstants.UniversalChemical;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.chemicalDehydratorRecipes;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.item.chemistry.GenericChem;
import gtPlusPlus.core.material.MaterialMisc;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.core.material.nuclear.MaterialsFluorides;
import gtPlusPlus.core.material.nuclear.MaterialsNuclides;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class RecipeLoaderNuclear {

    public static void generate() {
        createRecipes();
        RecipeLoaderLFTR.generate();
        RecipeLoaderNuclearFuelProcessing.generate();
    }

    private static void createRecipes() {
        autoclave();
        blastFurnace();
        centrifugeRecipes();
        chemicalBathRecipes();
        chemicalReactorRecipes();
        dehydratorRecipes();
        electroMagneticSeperator();
        fluidExtractorRecipes();
        fluidHeater();
        fusionChainRecipes();
        macerator();
        mixerRecipes();
        sifter();
    }

    private static void autoclave() {
        GTValues.RA.stdBuilder()
            .itemInputs(ItemUtils.getItemStackOfAmountFromOreDict("dustZirconium", 1))
            .itemOutputs(ItemUtils.getItemStackOfAmountFromOreDict("pelletZirconium", 1))
            .fluidInputs(FluidUtils.getFluidStack("chlorine", 4_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(autoclaveRecipes);
    }

    private static void blastFurnace() {

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialsFluorides.LITHIUM_FLUORIDE.getDust(4),
                MaterialsFluorides.BERYLLIUM_FLUORIDE.getDust(3))
            .itemOutputs(MaterialsNuclides.Li2BeF4.getDust(7))
            .duration(2 * MINUTES + 20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .metadata(COIL_HEAT, 3000)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemUtils.getItemStackOfAmountFromOreDict("dustZrCl4", 1))
            .itemOutputs(ItemUtils.getItemStackOfAmountFromOreDict("dustCookedZrCl4", 1))
            .duration(1 * MINUTES)
            .eut(340)
            .metadata(COIL_HEAT, 300)
            .addTo(blastFurnaceRecipes);
    }

    private static void centrifugeRecipes() {

        // Process Used Fuel Rods for Krypton

        // Uranium
        for (ItemStack depletedRod : new ItemStack[] {
            ItemUtils.getItemStackFromFQRN("IC2:reactorUraniumSimpledepleted", 8),
            ItemUtils.getItemStackFromFQRN("IC2:reactorUraniumDualdepleted", 4),
            ItemUtils.getItemStackFromFQRN("IC2:reactorUraniumQuaddepleted", 2) }) {
            GTValues.RA.stdBuilder()
                .itemInputs(depletedRod, GTUtility.getIntegratedCircuit(20))
                .itemOutputs(
                    ItemList.IC2_Fuel_Rod_Empty.get(8),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Uranium, 2L),
                    MaterialsElements.getInstance().URANIUM232.getSmallDust(1),
                    MaterialsElements.getInstance().URANIUM233.getSmallDust(1),
                    GTOreDictUnificator.get(OrePrefixes.dustSmall, Materials.Uranium235, 1L),
                    GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.Plutonium, 1L))
                .outputChances(10000, 10000, 1000, 1000, 1000, 500)
                .fluidOutputs(FluidUtils.getFluidStack("krypton", 60))
                .duration(4 * MINUTES + 10 * SECONDS)
                .eut(TierEU.RECIPE_IV)
                .addTo(centrifugeRecipes);
        }

        // Mox
        for (ItemStack depletedRod : new ItemStack[] {
            ItemUtils.getItemStackFromFQRN("IC2:reactorMOXSimpledepleted", 8),
            ItemUtils.getItemStackFromFQRN("IC2:reactorMOXDualdepleted", 4),
            ItemUtils.getItemStackFromFQRN("IC2:reactorMOXQuaddepleted", 2) }) {
            GTValues.RA.stdBuilder()
                .itemInputs(depletedRod, GTUtility.getIntegratedCircuit(20))
                .itemOutputs(
                    ItemList.IC2_Fuel_Rod_Empty.get(8),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Plutonium, 2L),
                    GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.Plutonium241, 1L),
                    GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.Plutonium, 1L),
                    MaterialsElements.getInstance().PLUTONIUM238.getTinyDust(1),
                    GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.Plutonium, 1L))
                .outputChances(10000, 10000, 500, 500, 500, 500)
                .fluidOutputs(FluidUtils.getFluidStack("krypton", 90))
                .duration(6 * MINUTES + 15 * SECONDS)
                .eut(TierEU.RECIPE_IV)
                .addTo(centrifugeRecipes);
        }

        // Thorium
        for (ItemStack depletedRod : new ItemStack[] { ItemList.Depleted_Thorium_1.get(8),
            ItemList.Depleted_Thorium_2.get(4), ItemList.Depleted_Thorium_4.get(2) }) {
            GTValues.RA.stdBuilder()
                .itemInputs(depletedRod, GTUtility.getIntegratedCircuit(20))
                .itemOutputs(
                    ItemList.IC2_Fuel_Rod_Empty.get(8),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Thorium, 2L),
                    MaterialsElements.getInstance().THORIUM232.getDust(1),
                    GTOreDictUnificator.get(OrePrefixes.dustSmall, Materials.Lutetium, 1L),
                    MaterialsElements.getInstance().POLONIUM.getSmallDust(1),
                    MaterialsElements.getInstance().THALLIUM.getTinyDust(1))
                .outputChances(10000, 10000, 5000, 5000, 5000, 2500)
                .fluidOutputs(FluidUtils.getFluidStack("krypton", 30))
                .duration(2 * MINUTES + 5 * SECONDS)
                .eut(TierEU.RECIPE_IV)
                .addTo(centrifugeRecipes);
        }
    }

    private static void chemicalBathRecipes() {

        int[] chances = { 9000, 6000, 3000 };
        GTValues.RA.stdBuilder()
            .itemInputs(ItemUtils.getItemStackOfAmountFromOreDict("dustTin", 12))
            .itemOutputs(
                ItemUtils.getItemStackOfAmountFromOreDict("dustZirconium", 3),
                ItemUtils.getItemStackOfAmountFromOreDict("dustZirconium", 4),
                ItemUtils.getItemStackOfAmountFromOreDict("dustZirconium", 5))
            .outputChances(chances)
            .fluidInputs(FluidUtils.getFluidStack("chlorine", 2400))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(chemicalBathRecipes);

        chances = new int[] { 9000, 3000, 1000 };
        GTValues.RA.stdBuilder()
            .itemInputs(ItemUtils.getItemStackOfAmountFromOreDict("dustRutile", 5))
            .itemOutputs(
                ItemUtils.getItemStackOfAmountFromOreDict("dustZirconium", 3),
                ItemUtils.getItemStackOfAmountFromOreDict("dustTitanium", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustHafnium", 1))
            .outputChances(chances)
            .fluidInputs(FluidUtils.getFluidStack("chlorine", 4000))
            .duration(30 * SECONDS)
            .eut(1024)
            .addTo(chemicalBathRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemUtils.getItemStackOfAmountFromOreDict("dustLithiumCarbonate", 3))
            .itemOutputs(ItemUtils.getItemStackOfAmountFromOreDict("dustLithiumFluoride", 2))
            .fluidInputs(FluidUtils.getFluidStack("hydrofluoricacid", 500))
            .duration(9 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(chemicalBathRecipes);

    }

    private static void chemicalReactorRecipes() {

        ItemStack aGtHydrofluoricAcid = ItemUtils
            .getItemStackOfAmountFromOreDictNoBroken("cellHydrofluoricAcid_GT5U", 2);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemUtils.getItemStackOfAmountFromOreDict("dustLithiumCarbonate", 6),
                ItemUtils.getItemStackOfAmountFromOreDict("dustCalciumHydroxide", 5))
            .itemOutputs(ItemUtils.getItemStackOfAmountFromOreDict("dustLi2CO3CaOH2", 11))
            .duration(10 * MINUTES)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemUtils.getItemStackOfAmountFromOreDict("dustLithiumHydroxide", 3))
            .itemOutputs(ItemUtils.getItemStackOfAmountFromOreDict("dustLithiumFluoride", 2))
            .fluidInputs(FluidUtils.getFluidStack("hydrofluoricacid", 500))
            .fluidOutputs(FluidUtils.getFluidStack("water", 1000))
            .duration(2 * MINUTES)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemUtils.getItemStackOfAmountFromOreDict("cellOxygen", 8),
                ItemUtils.getItemStackOfAmountFromOreDict("dustLithium7", 16))
            .itemOutputs(CI.emptyCells(8))
            .fluidInputs(FluidUtils.getFluidStack("water", 8000))
            .fluidOutputs(FluidUtils.getFluidStack("lithiumhydroxide", 6912))
            .duration(5 * MINUTES)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // LFTR Fuel Related Compounds
        // Hydroxide
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(3), MaterialsElements.getInstance().OXYGEN.getCell(1))
            .itemOutputs(CI.emptyCells(1))
            .fluidInputs(MaterialsElements.getInstance().HYDROGEN.getFluidStack(1000))
            .fluidOutputs(MaterialMisc.HYDROXIDE.getFluidStack(1000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Beryllium Hydroxide
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(3), MaterialsElements.getInstance().BERYLLIUM.getDust(1))
            .fluidInputs(MaterialMisc.HYDROXIDE.getFluidStack(2000))
            .fluidOutputs(MaterialsFluorides.BERYLLIUM_HYDROXIDE.getFluidStack(432))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Ammonium Bifluoride
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(3),
                ItemUtils.getItemStackOfAmountFromOreDict("cellHydrofluoricAcid", 1))
            .itemOutputs(CI.emptyCells(1))
            .fluidInputs(MaterialMisc.AMMONIA.getFluidStack(1000))
            .fluidOutputs(MaterialsFluorides.AMMONIUM_BIFLUORIDE.getFluidStack(576))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Ammonium Bifluoride
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(3), aGtHydrofluoricAcid)
            .itemOutputs(CI.emptyCells(2))
            .fluidInputs(MaterialMisc.AMMONIA.getFluidStack(1000))
            .fluidOutputs(MaterialsFluorides.AMMONIUM_BIFLUORIDE.getFluidStack(576))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Ammonium
        // To be deprecated now that it is no longer needed for ammonium bifluoride
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(3), MaterialsElements.getInstance().HYDROGEN.getCell(1))
            .itemOutputs(CI.emptyCells(1))
            .fluidInputs(MaterialMisc.AMMONIA.getFluidStack(1000))
            .fluidOutputs(MaterialMisc.AMMONIUM.getFluidStack(2000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Sodium Fluoride
        GTValues.RA.stdBuilder()
            .itemInputs(
                CI.getNumberedBioCircuit(15),
                ItemUtils.getItemStackOfAmountFromOreDict("dustSodiumHydroxide", 3))
            .itemOutputs(MaterialsFluorides.SODIUM_FLUORIDE.getDust(2))
            .fluidInputs(FluidUtils.getFluidStack("hydrofluoricacid", 500))
            .fluidOutputs(FluidUtils.getWater(1000))
            .duration(1 * MINUTES)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                CI.getNumberedBioCircuit(15),
                ItemUtils.getItemStackOfAmountFromOreDict("dustSodiumHydroxide", 3))
            .itemOutputs(MaterialsFluorides.SODIUM_FLUORIDE.getDust(2))
            .fluidInputs(FluidUtils.getFluidStack("hydrofluoricacid_gt5u", 1000))
            .fluidOutputs(FluidUtils.getWater(1000))
            .duration(1 * MINUTES)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);
    }

    private static void dehydratorRecipes() {

        // Makes 7-Lithium
        GTValues.RA.stdBuilder()
            .itemInputs(CI.getNumberedAdvancedCircuit(14))
            .fluidInputs(FluidUtils.getFluidStack("sulfuriclithium", 1440))
            .itemOutputs(
                ItemUtils.getItemStackOfAmountFromOreDict("dustSulfur", 3),
                ItemUtils.getItemStackOfAmountFromOreDict("dustCopper", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustSodium", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustCarbon", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustLithium7", 4))
            .duration(30 * SECONDS)
            .eut(30)
            .addTo(chemicalDehydratorRecipes);

        // Makes Lithium Carbonate
        GTValues.RA.stdBuilder()
            .itemInputs(CI.emptyCells(12), ItemUtils.getItemStackOfAmountFromOreDict("dustLepidolite", 20))
            .itemOutputs(
                ItemUtils.getItemStackOfAmountFromOreDict("dustPotassium", 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Aluminium, 4),
                ItemUtils.getItemStackOfAmountFromOreDict("cellOxygen", 10),
                ItemUtils.getItemStackOfAmountFromOreDict("cellFluorine", 2),
                ItemUtils.getItemStackOfAmountFromOreDict("dustLithiumCarbonate", 3))
            .fluidInputs(FluidUtils.getFluidStack("sulfuricacid", 10000))
            .fluidOutputs(FluidUtils.getFluidStack("sulfuriclithium", 10000))
            .eut(1_000)
            .duration(1 * MINUTES + 15 * SECONDS)
            .addTo(chemicalDehydratorRecipes);

        // Calcium Hydroxide
        if (ItemUtils.checkForInvalidItems(ItemUtils.getItemStackOfAmountFromOreDict("dustQuicklime", 1))) {
            // CaO + H2O = Ca(OH)2
            GTValues.RA.stdBuilder()
                .itemInputs(CI.getNumberedBioCircuit(20), ItemUtils.getItemStackOfAmountFromOreDict("dustQuicklime", 2))
                .itemOutputs(ItemUtils.getItemStackOfAmountFromOreDict("dustCalciumHydroxide", 5))
                .fluidInputs(FluidUtils.getFluidStack("water", 1000))
                .eut(TierEU.RECIPE_MV)
                .duration(12 * SECONDS)
                .addTo(chemicalDehydratorRecipes);

        } else {
            Logger.INFO("[dustCalciumHydroxide] FAILED TO LOAD RECIPE");
            if (!ItemUtils.checkForInvalidItems(ItemUtils.getItemStackOfAmountFromOreDict("dustQuicklime", 1))) {
                Logger.INFO("Could not find dustQuicklime, cannot make dustCalciumHydroxide.");
            }
        }

        // 2 LiOH + CaCO3
        GTValues.RA.stdBuilder()
            .itemInputs(
                CI.getNumberedAdvancedCircuit(20),
                ItemUtils.getItemStackOfAmountFromOreDict("dustLi2CO3CaOH2", 11))
            .itemOutputs(
                ItemUtils.getItemStackOfAmountFromOreDict("dustLithiumHydroxide", 6),
                ItemUtils.getItemStackOfAmountFromOreDict("dustCalciumCarbonate", 5))
            .eut(1_000)
            .duration(6 * MINUTES)
            .addTo(chemicalDehydratorRecipes);

        // LiOH Liquid to Dust
        GTValues.RA.stdBuilder()
            .itemInputs(CI.getNumberedAdvancedCircuit(22))
            .itemOutputs(ItemUtils.getItemStackOfAmountFromOreDict("dustLithiumHydroxide", 1))
            .fluidInputs(FluidUtils.getFluidStack("lithiumhydroxide", 144))
            .eut(64)
            .duration(1 * SECONDS)
            .addTo(chemicalDehydratorRecipes);

        // Zirconium Chloride -> TetraFluoride
        FluidStack aHydrogenChloride = new FluidStack(GenericChem.HydrochloricAcid, 800);
        GTValues.RA.stdBuilder()
            .itemInputs(
                CI.getNumberedAdvancedCircuit(11),
                ItemUtils.getItemStackOfAmountFromOreDict("dustCookedZrCl4", 1))
            .itemOutputs(MaterialsFluorides.ZIRCONIUM_TETRAFLUORIDE.getDust(1))
            .fluidInputs(FluidUtils.getFluidStack("hydrofluoricacid", 400))
            .fluidOutputs(aHydrogenChloride)
            .eut(TierEU.RECIPE_HV)
            .duration(15 * SECONDS)
            .addTo(chemicalDehydratorRecipes);

        // Zirconium Chloride -> TetraFluoride
        GTValues.RA.stdBuilder()
            .itemInputs(
                CI.getNumberedAdvancedCircuit(10),
                ItemUtils.getItemStackOfAmountFromOreDict("dustCookedZrCl4", 1))
            .itemOutputs(MaterialsFluorides.ZIRCONIUM_TETRAFLUORIDE.getDust(1))
            .fluidInputs(FluidUtils.getFluidStack("hydrofluoricacid_gt5u", 800))
            .fluidOutputs(aHydrogenChloride)
            .eut(TierEU.RECIPE_HV)
            .duration(30 * SECONDS)
            .addTo(chemicalDehydratorRecipes);

        // Be(OH)2 + 2 (NH4)HF2 → (NH4)2BeF4 + 2 H2O
        // Inputs use solid rule because they are molten forms of solids
        // Outputs use fluid rule because they are not molten forms of solids
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialsFluorides.BERYLLIUM_HYDROXIDE.getDust(3), CI.emptyCells(2))
            .itemOutputs(ItemUtils.getItemStackOfAmountFromOreDict("cellWater", 2))
            .fluidInputs(MaterialsFluorides.AMMONIUM_BIFLUORIDE.getFluidStack(1152))
            .fluidOutputs(MaterialsFluorides.AMMONIUM_TETRAFLUOROBERYLLATE.getFluidStack(1000))
            .eut(64)
            .duration(6 * SECONDS)
            .addTo(chemicalDehydratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialsFluorides.BERYLLIUM_HYDROXIDE.getDust(3), CI.getNumberedAdvancedCircuit(17))
            .fluidInputs(MaterialsFluorides.AMMONIUM_BIFLUORIDE.getFluidStack(1152))
            .fluidOutputs(
                Materials.Water.getFluid(2000L),
                MaterialsFluorides.AMMONIUM_TETRAFLUOROBERYLLATE.getFluidStack(1000))
            .eut(TierEU.RECIPE_MV)
            .duration(5 * MINUTES)
            .addTo(multiblockChemicalReactorRecipes);

        // (NH4)2BeF4 → 2 NH3 + 2 HF + BeF2
        // Ammonium tetrafluoroberyllate uses fluid rule because it is not a molten form of a solid
        // Beryllium fluoride uses solid rule
        // Industrial strength hydrofluoric acid follows its usual convention where it is twice as dense as regular
        // hydrofluoric acid
        GTValues.RA.stdBuilder()
            .itemInputs(CI.getNumberedAdvancedCircuit(17), CI.emptyCells(3))
            .itemOutputs(
                MaterialMisc.AMMONIA.getCell(2),
                ItemUtils.getItemStackOfAmountFromOreDict("cellHydrofluoricAcid", 1),
                MaterialsFluorides.BERYLLIUM_FLUORIDE.getDust(3))
            .fluidInputs(MaterialsFluorides.AMMONIUM_TETRAFLUOROBERYLLATE.getFluidStack(1000))
            .eut(TierEU.RECIPE_MV)
            .duration(5 * MINUTES)
            .addTo(chemicalDehydratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(CI.getNumberedAdvancedCircuit(17))
            .itemOutputs(MaterialsFluorides.BERYLLIUM_FLUORIDE.getDust(3))
            .fluidInputs(MaterialsFluorides.AMMONIUM_TETRAFLUOROBERYLLATE.getFluidStack(1000))
            .fluidOutputs(MaterialMisc.AMMONIA.getFluidStack(2000), FluidUtils.getFluidStack("hydrofluoricacid", 1000))
            .eut(TierEU.RECIPE_MV)
            .duration(5 * MINUTES)
            .addTo(multiblockChemicalReactorRecipes);
    }

    private static void electroMagneticSeperator() {

        // Zirconium
        GTValues.RA.stdBuilder()
            .itemInputs(ItemUtils.getItemStackOfAmountFromOreDict("crushedPurifiedBauxite", 1))
            .itemOutputs(
                ItemUtils.getItemStackOfAmountFromOreDict("dustBauxite", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustSmallRutile", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("nuggetZirconium", 1))
            .outputChances(10000, 2500, 4000)
            .duration(20 * SECONDS)
            .eut(24)
            .addTo(electroMagneticSeparatorRecipes);
        // Zircon
        GTValues.RA.stdBuilder()
            .itemInputs(ItemUtils.getItemStackOfAmountFromOreDict("crushedPurifiedMagnetite", 1))
            .itemOutputs(
                ItemUtils.getItemStackOfAmountFromOreDict("dustMagnetite", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustSmallZircon", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustTinyZircon", 1))
            .outputChances(10000, 1250, 2500)
            .duration(20 * SECONDS)
            .eut(24)
            .addTo(electroMagneticSeparatorRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemUtils.getItemStackOfAmountFromOreDict("crushedPurifiedCassiterite", 1))
            .itemOutputs(
                ItemUtils.getItemStackOfAmountFromOreDict("dustCassiterite", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustSmallZircon", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustTinyZircon", 1))
            .outputChances(10000, 1250, 2500)
            .duration(20 * SECONDS)
            .eut(24)
            .addTo(electroMagneticSeparatorRecipes);

    }

    private static void fluidExtractorRecipes() {

        // FLiBe fuel
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialsNuclides.Li2BeF4.getDust(1))
            .fluidOutputs(MaterialsNuclides.Li2BeF4.getFluidStack(144))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(fluidExtractionRecipes);

        // Lithium Fluoride
        GTValues.RA.stdBuilder()
            .itemInputs(ItemUtils.getItemStackOfAmountFromOreDict("dustLithiumFluoride", 1))
            .fluidOutputs(MaterialsFluorides.LITHIUM_FLUORIDE.getFluidStack(144))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(fluidExtractionRecipes);

        // Lithium Fluoride
        GTValues.RA.stdBuilder()
            .itemInputs(ItemUtils.getItemStackOfAmountFromOreDict("dustBerylliumFluoride", 1))
            .fluidOutputs(MaterialsFluorides.BERYLLIUM_FLUORIDE.getFluidStack(144))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(fluidExtractionRecipes);

        // LFTR Fuel 1
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialsNuclides.LiFBeF2ZrF4U235.getDust(1))
            .fluidOutputs(MaterialsNuclides.LiFBeF2ZrF4U235.getFluidStack(144))
            .duration(12 * SECONDS + 10 * TICKS)
            .eut(1000)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialsNuclides.LiFBeF2ZrF4UF4.getDust(1))
            .fluidOutputs(MaterialsNuclides.LiFBeF2ZrF4UF4.getFluidStack(144))
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(1500)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialsNuclides.LiFBeF2ThF4UF4.getDust(1))
            .fluidOutputs(MaterialsNuclides.LiFBeF2ThF4UF4.getFluidStack(144))
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_EV)
            .addTo(fluidExtractionRecipes);

        // ZIRCONIUM_TETRAFLUORIDE
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialsFluorides.ZIRCONIUM_TETRAFLUORIDE.getDust(1))
            .fluidOutputs(MaterialsFluorides.ZIRCONIUM_TETRAFLUORIDE.getFluidStack(144))
            .duration(10 * SECONDS)
            .eut(512 + 256)
            .addTo(fluidExtractionRecipes);
    }

    private static void fusionChainRecipes() {
        // Mk1
        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Boron.getPlasma(144), Materials.Calcium.getPlasma(16))
            .fluidOutputs(new FluidStack(MaterialsElements.getInstance().NEON.getPlasma(), 1000))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(TierEU.RECIPE_LuV)
            .metadata(FUSION_THRESHOLD, 100_000_000)
            .addTo(fusionRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                new FluidStack(MaterialsElements.getInstance().NEON.getPlasma(), 144),
                Materials.Bedrockium.getMolten(144))
            .fluidOutputs(new FluidStack(MaterialsElements.STANDALONE.FORCE.getPlasma(), 1000))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(TierEU.RECIPE_LuV)
            .metadata(FUSION_THRESHOLD, 100_000_000)
            .addTo(fusionRecipes);

        // Mk2
        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Niobium.getPlasma(144), Materials.Zinc.getPlasma(144))
            .fluidOutputs(new FluidStack(MaterialsElements.getInstance().KRYPTON.getPlasma(), 144))
            .duration(32 * TICKS)
            .eut(TierEU.RECIPE_ZPM)
            .metadata(FUSION_THRESHOLD, 300_000_000)
            .addTo(fusionRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                new FluidStack(MaterialsElements.getInstance().KRYPTON.getPlasma(), 144),
                new FluidStack(MaterialsElements.STANDALONE.FORCE.getPlasma(), 1000))
            .fluidOutputs(new FluidStack(MaterialsElements.STANDALONE.ASTRAL_TITANIUM.getPlasma(), 1000))
            .duration(32 * TICKS)
            .eut(TierEU.RECIPE_ZPM)
            .metadata(FUSION_THRESHOLD, 300_000_000)
            .addTo(fusionRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                new FluidStack(MaterialsElements.STANDALONE.ASTRAL_TITANIUM.getPlasma(), 144),
                new FluidStack(MaterialsAlloy.TITANSTEEL.getFluid(), 8))
            .fluidOutputs(new FluidStack(MaterialsElements.STANDALONE.RUNITE.getPlasma(), 1000))
            .duration(32 * TICKS)
            .eut(TierEU.RECIPE_ZPM)
            .metadata(FUSION_THRESHOLD, 300_000_000)
            .addTo(fusionRecipes);

        // Mk3
        GTValues.RA.stdBuilder()
            .fluidInputs(MaterialsElements.getInstance().CURIUM.getFluidStack(144), Materials.Americium.getPlasma(144))
            .fluidOutputs(new FluidStack(MaterialsElements.getInstance().XENON.getPlasma(), 144))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_UV)
            .metadata(FUSION_THRESHOLD, 500_000_000)
            .addTo(fusionRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                new FluidStack(MaterialsElements.getInstance().XENON.getPlasma(), 144),
                new FluidStack(MaterialsElements.STANDALONE.RUNITE.getPlasma(), 1000))
            .fluidOutputs(new FluidStack(MaterialsElements.STANDALONE.ADVANCED_NITINOL.getPlasma(), 1000))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_UV)
            .metadata(FUSION_THRESHOLD, 500_000_000)
            .addTo(fusionRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                new FluidStack(MaterialsElements.STANDALONE.ADVANCED_NITINOL.getPlasma(), 72),
                Materials.Tartarite.getMolten(2))
            .fluidOutputs(new FluidStack(MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getPlasma(), 1000))
            .duration(8 * TICKS)
            .eut(TierEU.RECIPE_UV)
            .metadata(FUSION_THRESHOLD, 500_000_000)
            .addTo(fusionRecipes);
    }

    private static void macerator() {
        RA.stdBuilder()
            .itemInputs(ItemUtils.getItemStackOfAmountFromOreDict("pelletZirconium", 1))
            .itemOutputs(ItemUtils.getItemStackOfAmountFromOreDict("dustZrCl4", 5))
            .eut(2)
            .duration(20 * SECONDS)
            .addTo(maceratorRecipes);
    }

    private static void mixerRecipes() {

        // Rebalanced to correct the chemistry
        // UF4 uses solid rule due to item form even though item form currently is inaccessible because item form may be
        // accessible in future and must be consistent
        // UF4 solid rule also assumes 1:144 item:fluid ratio in this case
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemUtils.getItemStackOfAmountFromOreDict("dustUranium233", 4),
                ItemUtils.getItemStackOfAmountFromOreDict("dustUranium235", 1))
            .fluidInputs(FluidUtils.getFluidStack("hydrofluoricacid", 10000))
            .fluidOutputs(MaterialsFluorides.URANIUM_TETRAFLUORIDE.getFluidStack(3600))
            .duration(2 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

    }

    private static void sifter() {
        // Zirconium
        GTValues.RA.stdBuilder()
            .itemInputs(ItemUtils.getItemStackOfAmountFromOreDict("crushedPurifiedIlmenite", 1))
            .itemOutputs(
                ItemUtils.getItemStackOfAmountFromOreDict("dustIron", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustWroughtIron", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustZirconium", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustZirconium", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustHafnium", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustHafnium", 1))
            .outputChances(5000, 278, 1000, 1000, 300, 300)
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(sifterRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemUtils.getItemStackOfAmountFromOreDict("crushedPurifiedTin", 1))
            .itemOutputs(
                ItemUtils.getItemStackOfAmountFromOreDict("dustTin", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustZinc", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustZirconium", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustZirconium", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustZirconium", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustZirconium", 1))
            .outputChances(10000, 556, 1500, 1000, 500, 500)
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(sifterRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemUtils.getItemStackOfAmountFromOreDict("crushedPurifiedCassiterite", 1))
            .itemOutputs(
                ItemUtils.getItemStackOfAmountFromOreDict("dustCassiterite", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustTin", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustZirconium", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustZirconium", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustZirconium", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustZirconium", 1))
            .outputChances(10000, 556, 1500, 1000, 500, 500)
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(sifterRecipes);
        // Radium
        GTValues.RA.stdBuilder()
            .itemInputs(ItemUtils.getItemStackOfAmountFromOreDict("crushedPurifiedThorium", 1))
            .itemOutputs(
                ItemUtils.getItemStackOfAmountFromOreDict("dustThorium", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustLead", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustRadium226", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustRadium226", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustRadium226", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustRadium226", 1))
            .outputChances(10000, 500, 300, 200, 100, 100)
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(sifterRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemUtils.getItemStackOfAmountFromOreDict("crushedPurifiedUranium", 1))
            .itemOutputs(
                ItemUtils.getItemStackOfAmountFromOreDict("dustUranium", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustLead", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustRadium226", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustRadium226", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustRadium226", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustRadium226", 1))
            .outputChances(10000, 556, 1000, 500, 500, 500)
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(sifterRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemUtils.getItemStackOfAmountFromOreDict("crushedPurifiedUraninite", 1))
            .itemOutputs(
                ItemUtils.getItemStackOfAmountFromOreDict("dustUraninite", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustUranium", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustRadium226", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustRadium226", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustRadium226", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustRadium226", 1))
            .outputChances(10000, 556, 500, 250, 250, 250)
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(sifterRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemUtils.getItemStackOfAmountFromOreDict("crushedPurifiedPitchblende", 1))
            .itemOutputs(
                ItemUtils.getItemStackOfAmountFromOreDict("dustPitchblende", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustLead", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustRadium226", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustRadium226", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustRadium226", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustRadium226", 1))
            .outputChances(10000, 556, 500, 250, 250, 250)
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(sifterRecipes);

    }

    private static void fluidHeater() {
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialsFluorides.SODIUM_FLUORIDE.getDust(1))
            .fluidOutputs(MaterialsFluorides.SODIUM_FLUORIDE.getFluidStack(144))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(fluidHeaterRecipes);
    }
}
