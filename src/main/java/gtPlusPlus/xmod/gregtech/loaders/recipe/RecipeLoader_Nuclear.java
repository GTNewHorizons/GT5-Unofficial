package gtPlusPlus.xmod.gregtech.loaders.recipe;

import static gregtech.api.enums.GT_Values.RA;
import static gregtech.api.recipe.RecipeMaps.blastFurnaceRecipes;
import static gregtech.api.recipe.RecipeMaps.centrifugeRecipes;
import static gregtech.api.recipe.RecipeMaps.fusionRecipes;
import static gregtech.api.recipe.RecipeMaps.maceratorRecipes;
import static gregtech.api.util.GT_RecipeBuilder.MINUTES;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;
import static gregtech.api.util.GT_RecipeConstants.COIL_HEAT;
import static gregtech.api.util.GT_RecipeConstants.FUSION_THRESHOLD;
import static gregtech.api.util.GT_RecipeConstants.UniversalChemical;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.chemicalDehydratorRecipes;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.item.chemistry.GenericChem;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.ALLOY;
import gtPlusPlus.core.material.ELEMENT;
import gtPlusPlus.core.material.MISC_MATERIALS;
import gtPlusPlus.core.material.nuclear.FLUORIDES;
import gtPlusPlus.core.material.nuclear.NUCLIDE;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class RecipeLoader_Nuclear {

    public static void generate() {
        createRecipes();
        RecipeLoader_LFTR.generate();
        RecipeLoader_NuclearFuelProcessing.generate();
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

        GT_Values.RA.addAutoclaveRecipe(
            ItemUtils.getItemStackOfAmountFromOreDict("dustZirconium", 1),
            FluidUtils.getFluidStack("chlorine", 4000),
            ItemUtils.getItemStackOfAmountFromOreDict("pelletZirconium", 1),
            0,
            15 * 20,
            30);
    }

    private static void blastFurnace() {

        GT_Values.RA.stdBuilder()
            .itemInputs(
                FLUORIDES.LITHIUM_FLUORIDE.getDust(4),
                FLUORIDES.BERYLLIUM_FLUORIDE.getDust(3)
            )
            .itemOutputs(NUCLIDE.Li2BeF4.getDust(7))
            .duration(2*MINUTES+20*SECONDS)
            .eut(2000)
            .metadata(COIL_HEAT, 3000)
            .addTo(blastFurnaceRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemUtils.getItemStackOfAmountFromOreDict("dustZrCl4", 1))
            .itemOutputs(ItemUtils.getItemStackOfAmountFromOreDict("dustCookedZrCl4", 1))
            .duration(1*MINUTES)
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
            GT_Values.RA.stdBuilder()
                .itemInputs(depletedRod, GT_Utility.getIntegratedCircuit(20))
                .itemOutputs(
                    ItemList.IC2_Fuel_Rod_Empty.get(8),
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Uranium, 2L),
                    ELEMENT.getInstance().URANIUM232.getSmallDust(1),
                    ELEMENT.getInstance().URANIUM233.getSmallDust(1),
                    GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Uranium235, 1L),
                    GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Plutonium, 1L))
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
            GT_Values.RA.stdBuilder()
                .itemInputs(depletedRod, GT_Utility.getIntegratedCircuit(20))
                .itemOutputs(
                    ItemList.IC2_Fuel_Rod_Empty.get(8),
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Plutonium, 2L),
                    GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Plutonium241, 1L),
                    GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Plutonium, 1L),
                    ELEMENT.getInstance().PLUTONIUM238.getTinyDust(1),
                    GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Plutonium, 1L))
                .outputChances(10000, 10000, 500, 500, 500, 500)
                .fluidOutputs(FluidUtils.getFluidStack("krypton", 90))
                .duration(6 * MINUTES + 15 * SECONDS)
                .eut(TierEU.RECIPE_IV)
                .addTo(centrifugeRecipes);
        }

        // Thorium
        for (ItemStack depletedRod : new ItemStack[] { ItemList.Depleted_Thorium_1.get(8),
            ItemList.Depleted_Thorium_2.get(4), ItemList.Depleted_Thorium_4.get(2) }) {
            GT_Values.RA.stdBuilder()
                .itemInputs(depletedRod, GT_Utility.getIntegratedCircuit(20))
                .itemOutputs(
                    ItemList.IC2_Fuel_Rod_Empty.get(8),
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Thorium, 2L),
                    ELEMENT.getInstance().THORIUM232.getDust(1),
                    GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Lutetium, 1L),
                    ELEMENT.getInstance().POLONIUM.getSmallDust(1),
                    ELEMENT.getInstance().THALLIUM.getTinyDust(1))
                .outputChances(10000, 10000, 5000, 5000, 5000, 2500)
                .fluidOutputs(FluidUtils.getFluidStack("krypton", 30))
                .duration(2 * MINUTES + 5 * SECONDS)
                .eut(TierEU.RECIPE_IV)
                .addTo(centrifugeRecipes);
        }
    }

    private static void chemicalBathRecipes() {

        int[] chances = { 9000, 6000, 3000 };
        GT_Values.RA.addChemicalBathRecipe(
            ItemUtils.getItemStackOfAmountFromOreDict("dustTin", 12),
            FluidUtils.getFluidStack("chlorine", 2400),
            ItemUtils.getItemStackOfAmountFromOreDict("dustZirconium", 3),
            ItemUtils.getItemStackOfAmountFromOreDict("dustZirconium", 4),
            ItemUtils.getItemStackOfAmountFromOreDict("dustZirconium", 5),
            chances,
            30 * 20,
            480);

        chances = new int[] { 9000, 3000, 1000 };
        GT_Values.RA.addChemicalBathRecipe(
            ItemUtils.getItemStackOfAmountFromOreDict("dustRutile", 5),
            FluidUtils.getFluidStack("chlorine", 4000),
            ItemUtils.getItemStackOfAmountFromOreDict("dustZirconium", 3),
            ItemUtils.getItemStackOfAmountFromOreDict("dustTitanium", 1),
            ItemUtils.getItemStackOfAmountFromOreDict("dustHafnium", 1),
            chances,
            30 * 20,
            1024);

        GT_Values.RA.addChemicalBathRecipe(
            ItemUtils.getItemStackOfAmountFromOreDict("dustLithiumCarbonate", 3),
            FluidUtils.getFluidStack("hydrofluoricacid", 500),
            ItemUtils.getItemStackOfAmountFromOreDict("dustLithiumFluoride", 2),
            null,
            null,
            new int[] {},
            9 * 20,
            480);
    }

    private static void chemicalReactorRecipes() {

        ItemStack aGtHydrofluoricAcid = ItemUtils
            .getItemStackOfAmountFromOreDictNoBroken("cellHydrofluoricAcid_GT5U", 2);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemUtils.getItemStackOfAmountFromOreDict("dustLithiumCarbonate", 6),
                ItemUtils.getItemStackOfAmountFromOreDict("dustCalciumHydroxide", 5)
            )
            .itemOutputs(ItemUtils.getItemStackOfAmountFromOreDict("dustLi2CO3CaOH2", 11))
            .duration(10*MINUTES)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GT_Values.RA.stdBuilder()
            .itemInputs( ItemUtils.getItemStackOfAmountFromOreDict("dustLithiumHydroxide", 3))
            .itemOutputs(ItemUtils.getItemStackOfAmountFromOreDict("dustLithiumFluoride", 2))
            .fluidInputs(FluidUtils.getFluidStack("hydrofluoricacid", 500))
            .fluidOutputs(FluidUtils.getFluidStack("water", 1000))
            .duration(2*MINUTES)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemUtils.getItemStackOfAmountFromOreDict("cellOxygen", 8),
                ItemUtils.getItemStackOfAmountFromOreDict("dustLithium7", 16)
            )
            .itemOutputs(CI.emptyCells(8))
            .fluidInputs( FluidUtils.getFluidStack("water", 8000))
            .fluidOutputs(FluidUtils.getFluidStack("lithiumhydroxide", 6912))
            .duration(5*MINUTES)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // LFTR Fuel Related Compounds
        // Hydroxide
        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemUtils.getGregtechCircuit(3),
                ELEMENT.getInstance().OXYGEN.getCell(1)
            )
            .itemOutputs(CI.emptyCells(1))
            .fluidInputs(ELEMENT.getInstance().HYDROGEN.getFluidStack(1000))
            .fluidOutputs(MISC_MATERIALS.HYDROXIDE.getFluidStack(1000))
            .duration(8*SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Beryllium Hydroxide
        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemUtils.getGregtechCircuit(3),
                ELEMENT.getInstance().BERYLLIUM.getDust(1)
            )
            .fluidInputs(MISC_MATERIALS.HYDROXIDE.getFluidStack(2000))
            .fluidOutputs(FLUORIDES.BERYLLIUM_HYDROXIDE.getFluidStack(432))
            .duration(4*SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Ammonium Bifluoride
        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemUtils.getGregtechCircuit(3),
                ItemUtils.getItemStackOfAmountFromOreDict("cellHydrofluoricAcid", 1)
            )
            .itemOutputs(CI.emptyCells(1))
            .fluidInputs(MISC_MATERIALS.AMMONIA.getFluidStack(1000))
            .fluidOutputs(FLUORIDES.AMMONIUM_BIFLUORIDE.getFluidStack(576))
            .duration(20*SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Ammonium Bifluoride
        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemUtils.getGregtechCircuit(3),
                aGtHydrofluoricAcid
            )
            .itemOutputs(CI.emptyCells(2))
            .fluidInputs(MISC_MATERIALS.AMMONIA.getFluidStack(1000))
            .fluidOutputs(FLUORIDES.AMMONIUM_BIFLUORIDE.getFluidStack(576))
            .duration(40*SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Ammonium
        // To be deprecated now that it is no longer needed for ammonium bifluoride
        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemUtils.getGregtechCircuit(3),
                ELEMENT.getInstance().HYDROGEN.getCell(1)
            )
            .itemOutputs(CI.emptyCells(1))
            .fluidInputs(MISC_MATERIALS.AMMONIA.getFluidStack(1000))
            .fluidOutputs(MISC_MATERIALS.AMMONIUM.getFluidStack(2000))
            .duration(20*SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Sodium Fluoride
        GT_Values.RA.stdBuilder()
            .itemInputs(
                CI.getNumberedBioCircuit(15),
                ItemUtils.getItemStackOfAmountFromOreDict("dustSodiumHydroxide", 3)
            )
            .itemOutputs(FLUORIDES.SODIUM_FLUORIDE.getDust(2))
            .fluidInputs(FluidUtils.getFluidStack("hydrofluoricacid", 500))
            .fluidOutputs(FluidUtils.getWater(1000))
            .duration(1*MINUTES)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                CI.getNumberedBioCircuit(15),
                ItemUtils.getItemStackOfAmountFromOreDict("dustSodiumHydroxide", 3)
            )
            .itemOutputs(FLUORIDES.SODIUM_FLUORIDE.getDust(2))
            .fluidInputs(FluidUtils.getFluidStack("hydrofluoricacid_gt5u", 1000))
            .fluidOutputs(FluidUtils.getWater(1000))
            .duration(1*MINUTES)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);
    }

    private static void dehydratorRecipes() {

        // Makes 7-Lithium
        GT_Values.RA.stdBuilder()
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
        CORE.RA.addDehydratorRecipe(
            new ItemStack[] { CI.emptyCells(12), ItemUtils.getItemStackOfAmountFromOreDict("dustLepidolite", 20) }, // Item
                                                                                                                    // input
                                                                                                                    // (Array,
                                                                                                                    // up
                                                                                                                    // to
                                                                                                                    // 2)
            FluidUtils.getFluidStack("sulfuricacid", 10000),
            FluidUtils.getFluidStack("sulfuriclithium", 10000),
            new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict("dustPotassium", 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Aluminium, 4),
                ItemUtils.getItemStackOfAmountFromOreDict("cellOxygen", 10),
                ItemUtils.getItemStackOfAmountFromOreDict("cellFluorine", 2),
                ItemUtils.getItemStackOfAmountFromOreDict("dustLithiumCarbonate", 3), // LithiumCarbonate
            }, // Output Array of Items - Upto 9,
            new int[] { 10000, 10000, 10000, 10000, 10000 },
            75 * 20, // Time in ticks
            1000); // EU

        // Calcium Hydroxide
        if (ItemUtils.checkForInvalidItems(ItemUtils.getItemStackOfAmountFromOreDict("dustQuicklime", 1))) {
            // CaO + H2O = Ca(OH)2
            CORE.RA.addDehydratorRecipe(
                new ItemStack[] { CI.getNumberedBioCircuit(20),
                    ItemUtils.getItemStackOfAmountFromOreDict("dustQuicklime", 2) },
                FluidUtils.getFluidStack("water", 1000),
                null, // Fluid output (slot 2)
                new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict("dustCalciumHydroxide", 5) }, // Output
                new int[] { 10000 },
                12 * 20, // Time in ticks
                120); // EU
        } else {
            Logger.INFO("[dustCalciumHydroxide] FAILED TO LOAD RECIPE");
            if (!ItemUtils.checkForInvalidItems(ItemUtils.getItemStackOfAmountFromOreDict("dustQuicklime", 1))) {
                Logger.INFO("Could not find dustQuicklime, cannot make dustCalciumHydroxide.");
            }
        }

        // 2 LiOH + CaCO3
        CORE.RA.addDehydratorRecipe(
            new ItemStack[] { CI.getNumberedAdvancedCircuit(20),
                ItemUtils.getItemStackOfAmountFromOreDict("dustLi2CO3CaOH2", 11) }, // Item
            null, // Fluid input (slot 1)
            null, // Fluid output (slot 2)
            new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict("dustLithiumHydroxide", 6),
                ItemUtils.getItemStackOfAmountFromOreDict("dustCalciumCarbonate", 5) }, // Output
            new int[] { 10000, 10000 },
            240 * 20, // Time in ticks
            1000); // EU

        // LiOH Liquid to Dust
        CORE.RA.addDehydratorRecipe(
            new ItemStack[] { CI.getNumberedAdvancedCircuit(22) },
            FluidUtils.getFluidStack("lithiumhydroxide", 144),
            null,
            new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict("dustLithiumHydroxide", 1) },
            new int[] { 10000 },
            1 * 20, // Time in ticks
            64); // EU

        // Zirconium Chloride -> TetraFluoride
        FluidStack aHydrogenChloride = new FluidStack(GenericChem.HydrochloricAcid, 800);
        CORE.RA.addDehydratorRecipe(
            new ItemStack[] { CI.getNumberedAdvancedCircuit(11),
                ItemUtils.getItemStackOfAmountFromOreDict("dustCookedZrCl4", 1), }, // Item
            FluidUtils.getFluidStack("hydrofluoricacid", 400),
            aHydrogenChloride,
            new ItemStack[] { FLUORIDES.ZIRCONIUM_TETRAFLUORIDE.getDust(1) },
            new int[] { 10000 },
            15 * 20, // Time in ticks
            500); // EU

        // Zirconium Chloride -> TetraFluoride
        CORE.RA.addDehydratorRecipe(
            new ItemStack[] { CI.getNumberedAdvancedCircuit(10),
                ItemUtils.getItemStackOfAmountFromOreDict("dustCookedZrCl4", 1) },
            FluidUtils.getFluidStack("hydrofluoricacid_gt5u", 800),
            aHydrogenChloride,
            new ItemStack[] { FLUORIDES.ZIRCONIUM_TETRAFLUORIDE.getDust(1) },
            new int[] { 10000 },
            30 * 20, // Time in ticks
            500); // EU

        // Be(OH)2 + 2 (NH4)HF2 → (NH4)2BeF4 + 2 H2O
        // Inputs use solid rule because they are molten forms of solids
        // Outputs use fluid rule because they are not molten forms of solids
        CORE.RA.addDehydratorRecipe(
            new ItemStack[] { FLUORIDES.BERYLLIUM_HYDROXIDE.getDust(3), CI.emptyCells(2) },
            FLUORIDES.AMMONIUM_BIFLUORIDE.getFluidStack(1152), // Fluid input (slot 1)
            FLUORIDES.AMMONIUM_TETRAFLUOROBERYLLATE.getFluidStack(1000),
            new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict("cellWater", 2) },
            new int[] { 10000 },
            6 * 20, // Time in ticks
            64); // EU

        // (NH4)2BeF4 → 2 NH3 + 2 HF + BeF2
        // Ammonium tetrafluoroberyllate uses fluid rule because it is not a molten form of a solid
        // Beryllium fluoride uses solid rule
        // Industrial strength hydrofluoric acid follows its usual convention where it is twice as dense as regular
        // hydrofluoric acid
        CORE.RA.addDehydratorRecipe(
            new ItemStack[] { CI.getNumberedAdvancedCircuit(17), CI.emptyCells(3) },
            FLUORIDES.AMMONIUM_TETRAFLUOROBERYLLATE.getFluidStack(1000),
            null,
            new ItemStack[] { MISC_MATERIALS.AMMONIA.getCell(2),
                ItemUtils.getItemStackOfAmountFromOreDict("cellHydrofluoricAcid", 1),
                FLUORIDES.BERYLLIUM_FLUORIDE.getDust(3) },
            new int[] { 10000, 10000, 10000 },
            5 * 60 * 20,
            120);
    }

    private static void electroMagneticSeperator() {

        // Zirconium
        GT_Values.RA.addElectromagneticSeparatorRecipe(
            ItemUtils.getItemStackOfAmountFromOreDict("crushedPurifiedBauxite", 1),
            ItemUtils.getItemStackOfAmountFromOreDict("dustBauxite", 1),
            ItemUtils.getItemStackOfAmountFromOreDict("dustSmallRutile", 1),
            ItemUtils.getItemStackOfAmountFromOreDict("nuggetZirconium", 1),
            new int[] { 10000, 2500, 4000 },
            20 * 20,
            24);

        // Zircon
        GT_Values.RA.addElectromagneticSeparatorRecipe(
            ItemUtils.getItemStackOfAmountFromOreDict("crushedPurifiedMagnetite", 1),
            ItemUtils.getItemStackOfAmountFromOreDict("dustMagnetite", 1),
            ItemUtils.getItemStackOfAmountFromOreDict("dustSmallZircon", 1),
            ItemUtils.getItemStackOfAmountFromOreDict("dustTinyZircon", 1),
            new int[] { 10000, 1250, 2500 },
            20 * 20,
            24);
        GT_Values.RA.addElectromagneticSeparatorRecipe(
            ItemUtils.getItemStackOfAmountFromOreDict("crushedPurifiedCassiterite", 1),
            ItemUtils.getItemStackOfAmountFromOreDict("dustCassiterite", 1),
            ItemUtils.getItemStackOfAmountFromOreDict("dustSmallZircon", 1),
            ItemUtils.getItemStackOfAmountFromOreDict("dustTinyZircon", 1),
            new int[] { 10000, 1250, 2500 },
            20 * 20,
            24);
    }

    private static void fluidExtractorRecipes() {

        // FLiBe fuel
        CORE.RA.addFluidExtractionRecipe(NUCLIDE.Li2BeF4.getDust(1), NUCLIDE.Li2BeF4.getFluidStack(144), 100, 500);
        // Lithium Fluoride
        CORE.RA.addFluidExtractionRecipe(
            ItemUtils.getItemStackOfAmountFromOreDict("dustLithiumFluoride", 1),
            FLUORIDES.LITHIUM_FLUORIDE.getFluidStack(144),
            100,
            500);
        // LFTR Fuel 1
        CORE.RA.addFluidExtractionRecipe(
            NUCLIDE.LiFBeF2ZrF4U235.getDust(1),
            NUCLIDE.LiFBeF2ZrF4U235.getFluidStack(144),
            250,
            1000);
        CORE.RA.addFluidExtractionRecipe(
            NUCLIDE.LiFBeF2ZrF4UF4.getDust(1),
            NUCLIDE.LiFBeF2ZrF4UF4.getFluidStack(144),
            150,
            1500);
        CORE.RA.addFluidExtractionRecipe(
            NUCLIDE.LiFBeF2ThF4UF4.getDust(1),
            NUCLIDE.LiFBeF2ThF4UF4.getFluidStack(144),
            150,
            2000);

        // ZIRCONIUM_TETRAFLUORIDE
        CORE.RA.addFluidExtractionRecipe(
            FLUORIDES.ZIRCONIUM_TETRAFLUORIDE.getDust(1),
            FLUORIDES.ZIRCONIUM_TETRAFLUORIDE.getFluidStack(144),
            200,
            512 + 256);
    }

    private static void fusionChainRecipes() {
        // Mk1
        GT_Values.RA.stdBuilder()
            .fluidInputs(Materials.Boron.getPlasma(144), Materials.Calcium.getPlasma(16))
            .fluidOutputs(new FluidStack(ELEMENT.getInstance().NEON.getPlasma(), 1000))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(TierEU.RECIPE_LuV)
            .metadata(FUSION_THRESHOLD, 100000000)
            .addTo(fusionRecipes);

        GT_Values.RA.stdBuilder()
            .fluidInputs(
                new FluidStack(ELEMENT.getInstance().NEON.getPlasma(), 144),
                Materials.Bedrockium.getMolten(144))
            .fluidOutputs(new FluidStack(ELEMENT.STANDALONE.FORCE.getPlasma(), 1000))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(TierEU.RECIPE_LuV)
            .metadata(FUSION_THRESHOLD, 100000000)
            .addTo(fusionRecipes);

        // Mk2
        GT_Values.RA.stdBuilder()
            .fluidInputs(Materials.Niobium.getPlasma(144), Materials.Zinc.getPlasma(144))
            .fluidOutputs(new FluidStack(ELEMENT.getInstance().KRYPTON.getPlasma(), 144))
            .duration(32 * TICKS)
            .eut(TierEU.RECIPE_ZPM)
            .metadata(FUSION_THRESHOLD, 300000000)
            .addTo(fusionRecipes);

        GT_Values.RA.stdBuilder()
            .fluidInputs(
                new FluidStack(ELEMENT.getInstance().KRYPTON.getPlasma(), 144),
                new FluidStack(ELEMENT.STANDALONE.FORCE.getPlasma(), 1000))
            .fluidOutputs(new FluidStack(ELEMENT.STANDALONE.ASTRAL_TITANIUM.getPlasma(), 1000))
            .duration(32 * TICKS)
            .eut(TierEU.RECIPE_ZPM)
            .metadata(FUSION_THRESHOLD, 300000000)
            .addTo(fusionRecipes);

        GT_Values.RA.stdBuilder()
            .fluidInputs(
                new FluidStack(ELEMENT.STANDALONE.ASTRAL_TITANIUM.getPlasma(), 144),
                new FluidStack(ALLOY.TITANSTEEL.getFluid(), 8))
            .fluidOutputs(new FluidStack(ELEMENT.STANDALONE.RUNITE.getPlasma(), 1000))
            .duration(32 * TICKS)
            .eut(TierEU.RECIPE_ZPM)
            .metadata(FUSION_THRESHOLD, 300000000)
            .addTo(fusionRecipes);

        // Mk3
        GT_Values.RA.stdBuilder()
            .fluidInputs(ELEMENT.getInstance().CURIUM.getFluidStack(144), Materials.Americium.getPlasma(144))
            .fluidOutputs(new FluidStack(ELEMENT.getInstance().XENON.getPlasma(), 144))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_UV)
            .metadata(FUSION_THRESHOLD, 500000000)
            .addTo(fusionRecipes);

        GT_Values.RA.stdBuilder()
            .fluidInputs(
                new FluidStack(ELEMENT.getInstance().XENON.getPlasma(), 144),
                new FluidStack(ELEMENT.STANDALONE.RUNITE.getPlasma(), 1000))
            .fluidOutputs(new FluidStack(ELEMENT.STANDALONE.ADVANCED_NITINOL.getPlasma(), 1000))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_UV)
            .metadata(FUSION_THRESHOLD, 500000000)
            .addTo(fusionRecipes);

        GT_Values.RA.stdBuilder()
            .fluidInputs(
                new FluidStack(ELEMENT.STANDALONE.ADVANCED_NITINOL.getPlasma(), 144),
                Materials.Tartarite.getMolten(2))
            .fluidOutputs(new FluidStack(ELEMENT.STANDALONE.CELESTIAL_TUNGSTEN.getPlasma(), 1000))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_UV)
            .metadata(FUSION_THRESHOLD, 500000000)
            .addTo(fusionRecipes);
    }

    private static void macerator() {
        RA.stdBuilder().itemInputs(ItemUtils.getItemStackOfAmountFromOreDict("pelletZirconium", 1))
            .itemOutputs(ItemUtils.getItemStackOfAmountFromOreDict("dustZrCl4", 5)).eut(2).duration(20*SECONDS).addTo(maceratorRecipes);
    }

    private static void mixerRecipes() {

        // Rebalanced to correct the chemistry
        // UF4 uses solid rule due to item form even though item form currently is inaccessible because item form may be
        // accessible in future and must be consistent
        // UF4 solid rule also assumes 1:144 item:fluid ratio in this case
        GT_Values.RA.addMixerRecipe(
            ItemUtils.getItemStackOfAmountFromOreDict("dustUranium233", 4),
            ItemUtils.getItemStackOfAmountFromOreDict("dustUranium235", 1),
            null,
            null,
            FluidUtils.getFluidStack("hydrofluoricacid", 10000),
            FLUORIDES.URANIUM_TETRAFLUORIDE.getFluidStack(3600),
            null,
            3000,
            500);
    }

    private static void sifter() {

        // Zirconium
        GT_Values.RA.addSifterRecipe(
            ItemUtils.getItemStackOfAmountFromOreDict("crushedPurifiedIlmenite", 1),
            new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict("dustIron", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustWroughtIron", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustZirconium", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustZirconium", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustHafnium", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustHafnium", 1) },
            new int[] { 5000, 278, 1000, 1000, 300, 300 },
            20 * 30,
            500);
        GT_Values.RA.addSifterRecipe(
            ItemUtils.getItemStackOfAmountFromOreDict("crushedPurifiedTin", 1),
            new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict("dustTin", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustZinc", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustZirconium", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustZirconium", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustZirconium", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustZirconium", 1) },
            new int[] { 10000, 556, 1500, 1000, 500, 500 },
            20 * 30,
            500);
        GT_Values.RA.addSifterRecipe(
            ItemUtils.getItemStackOfAmountFromOreDict("crushedPurifiedCassiterite", 1),
            new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict("dustCassiterite", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustTin", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustZirconium", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustZirconium", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustZirconium", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustZirconium", 1) },
            new int[] { 10000, 556, 1500, 1000, 500, 500 },
            20 * 30,
            500);

        // Radium
        GT_Values.RA.addSifterRecipe(
            ItemUtils.getItemStackOfAmountFromOreDict("crushedPurifiedThorium", 1),
            new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict("dustThorium", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustLead", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustRadium226", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustRadium226", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustRadium226", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustRadium226", 1) },
            new int[] { 10000, 500, 300, 200, 100, 100 },
            20 * 30,
            500);

        GT_Values.RA.addSifterRecipe(
            ItemUtils.getItemStackOfAmountFromOreDict("crushedPurifiedUranium", 1),
            new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict("dustUranium", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustLead", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustRadium226", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustRadium226", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustRadium226", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustRadium226", 1) },
            new int[] { 10000, 556, 1000, 500, 500, 500 },
            20 * 30,
            500);

        GT_Values.RA.addSifterRecipe(
            ItemUtils.getItemStackOfAmountFromOreDict("crushedPurifiedUraninite", 1),
            new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict("dustUraninite", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustUranium", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustRadium226", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustRadium226", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustRadium226", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustRadium226", 1) },
            new int[] { 10000, 556, 500, 250, 250, 250 },
            20 * 30,
            500);

        GT_Values.RA.addSifterRecipe(
            ItemUtils.getItemStackOfAmountFromOreDict("crushedPurifiedPitchblende", 1),
            new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict("dustPitchblende", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustLead", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustRadium226", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustRadium226", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustRadium226", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustRadium226", 1) },
            new int[] { 10000, 556, 500, 250, 250, 250 },
            20 * 30,
            500);
    }

    private static void fluidHeater() {

        CORE.RA.addFluidHeaterRecipe(
            FLUORIDES.SODIUM_FLUORIDE.getDust(1),
            null,
            FLUORIDES.SODIUM_FLUORIDE.getFluidStack(144),
            20 * 30,
            500);
    }
}
