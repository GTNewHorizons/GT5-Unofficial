package gregtech.loaders.postload.chains;

import static bartworks.API.recipe.BartWorksRecipeMaps.electricImplosionCompressorRecipes;
import static goodgenerator.api.recipe.GoodGeneratorRecipeMaps.neutronActivatorRecipes;
import static goodgenerator.util.MyRecipeAdder.computeRangeNKE;
import static gregtech.api.enums.Mods.AdvancedSolarPanel;
import static gregtech.api.enums.Mods.AppliedEnergistics2;
import static gregtech.api.enums.Mods.EtFuturumRequiem;
import static gregtech.api.enums.Mods.HardcoreEnderExpansion;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.autoclaveRecipes;
import static gregtech.api.recipe.RecipeMaps.blastFurnaceRecipes;
import static gregtech.api.recipe.RecipeMaps.distillationTowerRecipes;
import static gregtech.api.recipe.RecipeMaps.fusionRecipes;
import static gregtech.api.recipe.RecipeMaps.multiblockChemicalReactorRecipes;
import static gregtech.api.recipe.RecipeMaps.sifterRecipes;
import static gregtech.api.recipe.RecipeMaps.vacuumFreezerRecipes;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.*;
import static gregtech.api.util.GTRecipeConstants.CHEMPLANT_CASING_TIER;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;
import static gregtech.api.util.GTRecipeConstants.FUSION_THRESHOLD;
import static gregtech.api.util.GTRecipeConstants.NKE_RANGE;
import static gregtech.api.util.GTRecipeConstants.QFT_CATALYST;
import static gregtech.api.util.GTRecipeConstants.QFT_FOCUS_TIER;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.centrifugeNonCellRecipes;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.chemicalPlantRecipes;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.electrolyzerNonCellRecipes;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.mixerNonCellRecipes;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.quantumForceTransformerRecipes;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import goodgenerator.util.ItemRefer;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtnhintergalactic.recipe.IGRecipeMaps;

public class EnderLineRecipes {

    static ItemStack missing = new ItemStack(Blocks.fire);

    public static void run() {

        // Ender Air Processing
        if (EtFuturumRequiem.isModLoaded()) {

            // GoG recipe only

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(EtFuturumRequiem.ID, "chorus_flower", 16, 0), GTUtility.getIntegratedCircuit(24))
                .itemOutputs(getModItem(EtFuturumRequiem.ID, "chorus_fruit_popped", 8, 0))
                .fluidInputs(
                    new FluidStack(FluidRegistry.getFluid("molten.uraniumtetrafluoride"), 1440),
                    new FluidStack(FluidRegistry.getFluid("vapor_of_levity"), 10),
                    new FluidStack(FluidRegistry.getFluid("liquid_sunshine"), 10))
                .fluidOutputs(Materials.EnderAir.getGas(1440))
                .duration(30 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(multiblockChemicalReactorRecipes);

            // Ender Air fusion

            GTValues.RA.stdBuilder()
                .fluidInputs(Materials.EnderAir.getGas(144), new FluidStack(FluidRegistry.getFluid("ender"), 144))
                .fluidOutputs(Materials.EnderAirUnstable.getGas(144))
                .duration(3 * SECONDS + 12 * TICKS)
                .eut(3145728)
                .metadata(FUSION_THRESHOLD, 240_000_000L)
                .addTo(fusionRecipes);

            // Ender Air Cryo

            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.CallistoIce, 8))
                .fluidInputs(
                    Materials.EnderAirUnstable.getGas(10000),
                    new FluidStack(FluidRegistry.getFluid("cryotheum"), 1000))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Snow, 8))
                .fluidOutputs(Materials.EnderAirCryostable.getGas(10000))
                .duration(15 * SECONDS)
                .eut(TierEU.RECIPE_UV)
                .addTo(vacuumFreezerRecipes);

            GTValues.RA.stdBuilder()
                .fluidInputs(Materials.EnderAirCryostable.getGas(200000))
                .fluidOutputs(
                    Materials.Radon.getFluid(1500),
                    new FluidStack(FluidRegistry.getFluid("xenon"), 2000),
                    new FluidStack(FluidRegistry.getFluid("krypton"), 10000),
                    Materials.Tritium.getGas(14000),
                    Materials.TeleportatiumUnstableVolatile.getGas(16040),
                    Materials.Helium.getGas(30000),
                    Materials.Deuterium.getGas(100000),
                    Materials.NitrogenDioxide.getGas(122000))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Cryotheum, 80))
                .duration(100 * SECONDS)
                .eut(TierEU.RECIPE_UV)
                .addTo(distillationTowerRecipes);

            // Ender Air Balanced

            GTValues.RA.stdBuilder()
                .fluidInputs(Materials.EnderAirUnstable.getGas(10000))
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.EndSteel, 8),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Ichorium, 8),
                    GTUtility.getIntegratedCircuit(3))
                .itemOutputs(
                    GTOreDictUnificator.get(OrePrefixes.ingotHot, Materials.Tungsten, 4),
                    GTOreDictUnificator.get(OrePrefixes.ingotHot, Materials.Ichorium, 4))
                .fluidOutputs(Materials.EnderAirBalanced.getGas(10000))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_ZPM)
                .metadata(COIL_HEAT, 9001)
                .addTo(blastFurnaceRecipes);

            GTValues.RA.stdBuilder()
                .fluidInputs(Materials.EnderAirBalanced.getGas(20000))
                .itemOutputs(
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Thaumium, 8L),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Silicon, 2L),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Magnesium, 1L),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 1L))
                .outputChances(7500, 5000, 2500, 1000)
                .fluidOutputs(Materials.TeleportatiumUnstableUnbalanced.getGas(15400))
                .duration(20 * SECONDS)
                .eut(TierEU.RECIPE_ZPM)
                .addTo(electrolyzerNonCellRecipes);

            // Ender Air Fortified

            GTValues.RA.stdBuilder()
                .fluidInputs(
                    Materials.EnderAirUnstable.getGas(10000),
                    Materials.Galgadorian.getFluid(100),
                    Materials.ReinforceGlass.getFluid(1000))
                .fluidOutputs(Materials.EnderAirFortified.getGas(10000))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_LuV)
                .addTo(multiblockChemicalReactorRecipes);

            GTValues.RA.stdBuilder()
                .fluidInputs(Materials.EnderAirFortified.getGas(20000))
                .itemOutputs(
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Endstone, 10L),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Glass, 5L),
                    getModItem(HardcoreEnderExpansion.ID, "end_powder", 4L, 0))
                .outputChances(5000, 2500, 2000)
                .fluidOutputs(Materials.TeleportatiumUnstableSemifluid.getGas(14400))
                .duration(20 * SECONDS)
                .eut(TierEU.RECIPE_LuV)
                .addTo(sifterRecipes);

            // Ender Air Pyro

            GTValues.RA.stdBuilder()
                .fluidInputs(
                    Materials.EnderAirUnstable.getGas(10000),
                    new FluidStack(FluidRegistry.getFluid("pyrotheum"), 1000))
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Firestone, 8),
                    GTOreDictUnificator.get(OrePrefixes.dust, MaterialsAlloy.ENERGYCRYSTAL, 8))
                .fluidOutputs(Materials.EnderAirPyrostable.getGas(10000))
                .duration(2 * SECONDS)
                .eut(TierEU.RECIPE_IV)
                .addTo(mixerNonCellRecipes);

            GTValues.RA.stdBuilder()
                .fluidInputs(Materials.EnderAirPyrostable.getGas(20000))
                .itemOutputs(
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.DarkAsh, 10L),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Pyrotheum, 5L),
                    getModItem(HardcoreEnderExpansion.ID, "stardust", 5L, 0),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.EnderPearl, 2L),
                    getModItem(HardcoreEnderExpansion.ID, "essence", 2L, 1),
                    getModItem(HardcoreEnderExpansion.ID, "essence", 1L, 0))
                .outputChances(5000, 4000, 2000, 1000, 500, 250)
                .fluidOutputs(Materials.TeleportatiumUnstableHypercritical.getGas(13400))
                .duration(20 * SECONDS)
                .eut(TierEU.RECIPE_IV)
                .addTo(centrifugeNonCellRecipes);

        }

        // Teleportatium processing
        {
            // Neutron source alternate recipe

            GTValues.RA.stdBuilder()
                .itemInputs(ItemRefer.High_Density_Uranium.get(8))
                .fluidInputs(Materials.Steel.getFluid(41472))
                .itemOutputs(ItemRefer.Neutron_Source.get(8))
                .duration(15 * SECONDS)
                .eut(TierEU.RECIPE_LuV)
                .addTo(autoclaveRecipes);

            // Neutron activator seed

            GTValues.RA.stdBuilder()
                .fluidInputs(Materials.TeleportatiumUnstableAmalgam.getGas(8000))
                .itemInputs(
                    getModItem(EtFuturumRequiem.ID, "chorus_flower", 16, 0),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.EnderEye, 4L),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Enderium, 1L),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.EnderiumBase, 1L),
                    GTOreDictUnificator.get(OrePrefixes.round, Materials.Netherite, 1L),
                    ItemRefer.Neutron_Source.get(8))
                .itemOutputs(
                    ItemList.Teleportium_Stable_Seed.get(8),
                    getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 8, 6, missing),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.EnderPearl, 4L),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.HeeEndium, 2L))
                .outputChances(100, 90, 50, 25)
                .fluidOutputs(Materials.TeleportatiumStable.getGas(1))
                .duration(100 * SECONDS)
                .eut(0)
                .metadata(NKE_RANGE, computeRangeNKE(1100, 500))
                .addTo(neutronActivatorRecipes);

            // Unstable Amalgam

            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.HeeEndium, 2L))
                .fluidInputs(
                    Materials.TeleportatiumUnstableVolatile.getGas(4020),
                    Materials.TeleportatiumUnstableUnbalanced.getGas(4020),
                    Materials.TeleportatiumUnstableSemifluid.getGas(4020),
                    Materials.TeleportatiumUnstableHypercritical.getGas(4020))
                .fluidOutputs(
                    Materials.TeleportatiumUnstableAmalgam.getGas(8080),
                    FluidRegistry.getFluidStack("sludge", 6000))
                .duration(33 * SECONDS)
                .eut(TierEU.RECIPE_UV)
                .metadata(CHEMPLANT_CASING_TIER, 7)
                .addTo(chemicalPlantRecipes);

            // Ender Goo recipe

            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.Teleportium_Stable_Seed.get(1),
                    getModItem(HardcoreEnderExpansion.ID, "stardust", 1L, 0))
                .fluidInputs(
                    Materials.EnderAirPyrostable.getGas(20000),
                    Materials.EnderAirCryostable.getGas(20000),
                    Materials.EnderAirFortified.getGas(20000),
                    Materials.EnderAirBalanced.getGas(20000))
                .fluidOutputs(Materials.EnderAirUnstable.getGas(10000), FluidRegistry.getFluidStack("endergoo", 60000))
                .duration(33 * SECONDS)
                .eut(TierEU.RECIPE_ZPM)
                .metadata(CHEMPLANT_CASING_TIER, 5)
                .addTo(chemicalPlantRecipes);

            // Formation Mixer vanish recipes

            GTValues.RA.stdBuilder()
                .fluidInputs(
                    FluidRegistry.getFluidStack("endergoo", 3000),
                    Materials.TeleportatiumUnstableSemifluid.getGas(10000),
                    Materials.TeleportatiumUnstableUnbalanced.getGas(10000),
                    Materials.TeleportatiumUnstableVolatile.getGas(10000))
                .fluidOutputs(Materials.TeleportatiumUnstableAmalgam.getGas(1296))
                .duration(60 * SECONDS)
                .eut(TierEU.RECIPE_ZPM)
                .addTo(mixerNonCellRecipes);

            GTValues.RA.stdBuilder()
                .fluidInputs(
                    FluidRegistry.getFluidStack("endergoo", 3000),
                    Materials.TeleportatiumUnstableHypercritical.getGas(10000),
                    Materials.TeleportatiumUnstableUnbalanced.getGas(10000),
                    Materials.TeleportatiumUnstableVolatile.getGas(10000))
                .fluidOutputs(Materials.TeleportatiumUnstableAmalgam.getGas(1296))
                .duration(60 * SECONDS)
                .eut(TierEU.RECIPE_ZPM)
                .addTo(mixerNonCellRecipes);

            GTValues.RA.stdBuilder()
                .fluidInputs(
                    FluidRegistry.getFluidStack("endergoo", 3000),
                    Materials.TeleportatiumUnstableHypercritical.getGas(10000),
                    Materials.TeleportatiumUnstableSemifluid.getGas(10000),
                    Materials.TeleportatiumUnstableVolatile.getGas(10000))
                .fluidOutputs(Materials.TeleportatiumUnstableAmalgam.getGas(1296))
                .duration(60 * SECONDS)
                .eut(TierEU.RECIPE_ZPM)
                .addTo(mixerNonCellRecipes);

            GTValues.RA.stdBuilder()
                .fluidInputs(
                    FluidRegistry.getFluidStack("endergoo", 3000),
                    Materials.TeleportatiumUnstableHypercritical.getGas(10000),
                    Materials.TeleportatiumUnstableSemifluid.getGas(10000),
                    Materials.TeleportatiumUnstableUnbalanced.getGas(10000))
                .fluidOutputs(Materials.TeleportatiumUnstableAmalgam.getGas(1296))
                .duration(60 * SECONDS)
                .eut(TierEU.RECIPE_ZPM)
                .addTo(mixerNonCellRecipes);

            // QFT Recipe and catalyst

            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Enderium, 64L),
                    getModItem(EtFuturumRequiem.ID, "chorus_flower", 64, 0),
                    Materials.Glowstone.getNanite(64),
                    getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 64L, 6, missing),
                    GregtechItemList.PinkMetalCatalyst.get(1),
                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.TeleportatiumEncased, 1L),
                    GTUtility.getIntegratedCircuit(10))
                .fluidInputs(Materials.TeleportatiumUnstableAmalgam.getGas(10000))
                .itemOutputs(GregtechItemList.TeleportiumCatalyst.get(1))
                .duration(3 * SECONDS + 4 * TICKS)
                .eut(TierEU.RECIPE_UEV)
                .addTo(assemblerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.EnderPearl, 64L),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Endstone, 64L),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Enderium, 64L),
                    getModItem(EtFuturumRequiem.ID, "chorus_flower", 64, 0))
                .itemOutputs(getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 64L, 6, missing))
                .fluidInputs(
                    Materials.NefariousOil.getGas(10000),
                    Materials.EnderAirUnstable.getGas(10000),
                    Materials.TeleportatiumUnstableAmalgam.getGas(1000),
                    FluidRegistry.getFluidStack("endergoo", 4000))
                .fluidOutputs(
                    Materials.TeleportatiumStable.getGas(800),
                    Materials.TeleportatiumUnstableVolatile.getGas(1000),
                    Materials.TeleportatiumUnstableUnbalanced.getGas(1000),
                    Materials.TeleportatiumUnstableSemifluid.getGas(1000),
                    Materials.TeleportatiumUnstableHypercritical.getGas(1000),
                    Materials.EnderAir.getGas(1000))
                .duration(20 * SECONDS)
                .eut(TierEU.RECIPE_UEV)
                .metadata(QFT_CATALYST, GregtechItemList.TeleportiumCatalyst.get(0))
                .metadata(QFT_FOCUS_TIER, 3)
                .addTo(quantumForceTransformerRecipes);

        }

        // L3 Teleportatium processing ( Move from EXXON chem to new multiblock when done)
        {

            // FORMATION

            GTValues.RA.stdBuilder()
                .fluidInputs(
                    FluidRegistry.getFluidStack("endergoo", 3000),
                    Materials.TeleportatiumUnstableSemifluid.getGas(10000),
                    Materials.TeleportatiumUnstableUnbalanced.getGas(10000),
                    Materials.TeleportatiumUnstableVolatile.getGas(10000))
                .fluidOutputs(
                    Materials.TeleportatiumSemistable.getFluid(3000),
                    Materials.TeleportatiumUnstableHypercritical.getGas(1000),
                    Materials.TeleportatiumUnstableAmalgam.getGas(1296))
                .duration(60 * SECONDS)
                .eut(TierEU.RECIPE_ZPM)
                .metadata(CHEMPLANT_CASING_TIER, 7)
                .addTo(chemicalPlantRecipes);

            GTValues.RA.stdBuilder()
                .fluidInputs(
                    FluidRegistry.getFluidStack("endergoo", 3000),
                    Materials.TeleportatiumUnstableHypercritical.getGas(10000),
                    Materials.TeleportatiumUnstableUnbalanced.getGas(10000),
                    Materials.TeleportatiumUnstableVolatile.getGas(10000))
                .fluidOutputs(
                    Materials.TeleportatiumSemistable.getFluid(3000),
                    Materials.TeleportatiumUnstableSemifluid.getGas(1000),
                    Materials.TeleportatiumUnstableAmalgam.getGas(1296))
                .duration(60 * SECONDS)
                .eut(TierEU.RECIPE_ZPM)
                .metadata(CHEMPLANT_CASING_TIER, 7)
                .addTo(chemicalPlantRecipes);

            GTValues.RA.stdBuilder()
                .fluidInputs(
                    FluidRegistry.getFluidStack("endergoo", 3000),
                    Materials.TeleportatiumUnstableHypercritical.getGas(10000),
                    Materials.TeleportatiumUnstableSemifluid.getGas(10000),
                    Materials.TeleportatiumUnstableVolatile.getGas(10000))
                .fluidOutputs(
                    Materials.TeleportatiumSemistable.getFluid(3000),
                    Materials.TeleportatiumUnstableUnbalanced.getGas(1000),
                    Materials.TeleportatiumUnstableAmalgam.getGas(1296))
                .duration(60 * SECONDS)
                .eut(TierEU.RECIPE_ZPM)
                .metadata(CHEMPLANT_CASING_TIER, 7)
                .addTo(chemicalPlantRecipes);

            GTValues.RA.stdBuilder()
                .fluidInputs(
                    FluidRegistry.getFluidStack("endergoo", 3000),
                    Materials.TeleportatiumUnstableHypercritical.getGas(10000),
                    Materials.TeleportatiumUnstableSemifluid.getGas(10000),
                    Materials.TeleportatiumUnstableUnbalanced.getGas(10000))
                .fluidOutputs(
                    Materials.TeleportatiumSemistable.getFluid(3000),
                    Materials.TeleportatiumUnstableVolatile.getGas(1000),
                    Materials.TeleportatiumUnstableAmalgam.getGas(1296))
                .duration(60 * SECONDS)
                .eut(TierEU.RECIPE_ZPM)
                .metadata(CHEMPLANT_CASING_TIER, 7)
                .addTo(chemicalPlantRecipes);

            // STABILIZATION

            GTValues.RA.stdBuilder()
                .itemInputs(
                    getModItem(EtFuturumRequiem.ID, "chorus_fruit_popped", 8, 0),
                    ItemList.Teleportium_Stable_Seed.get(1))
                .fluidInputs(
                    Materials.Grade4PurifiedWater.getFluid(4000),
                    Materials.TeleportatiumSemistable.getFluid(10000))
                .fluidOutputs(Materials.TeleportatiumStable.getFluid(7500))
                .itemOutputs(getModItem(HardcoreEnderExpansion.ID, "instability_orb", 1, 0))
                .duration(20 * SECONDS)
                .eut(TierEU.RECIPE_UV)
                .metadata(CHEMPLANT_CASING_TIER, 8)
                .addTo(chemicalPlantRecipes);
        }

        EnderLineRecipes.addEncasedTeleportatiumParts();
    }

    private static void addEncasedTeleportatiumParts() {
        addEncasedTeleportatiumPartsRecipe(OrePrefixes.ingot, 1, 1);
        addEncasedTeleportatiumPartsRecipe(OrePrefixes.plate, 1, 1);
        addEncasedTeleportatiumPartsRecipe(OrePrefixes.plateDouble, 1, 2);
        addEncasedTeleportatiumPartsRecipe(OrePrefixes.plateDense, 1, 9);
        addEncasedTeleportatiumPartsRecipe(OrePrefixes.stick, 2, 1);
        addEncasedTeleportatiumPartsRecipe(OrePrefixes.round, 9, 1);
        addEncasedTeleportatiumPartsRecipe(OrePrefixes.bolt, 8, 1);
        addEncasedTeleportatiumPartsRecipe(OrePrefixes.screw, 8, 1);
        addEncasedTeleportatiumPartsRecipe(OrePrefixes.ring, 4, 1);
        addEncasedTeleportatiumPartsRecipe(OrePrefixes.foil, 4, 1);
        addEncasedTeleportatiumPartsRecipe(OrePrefixes.itemCasing, 2, 1);
        addEncasedTeleportatiumPartsRecipe(OrePrefixes.gearGtSmall, 1, 1);
        addEncasedTeleportatiumPartsRecipe(OrePrefixes.rotor, 1, 5);
        addEncasedTeleportatiumPartsRecipe(OrePrefixes.stickLong, 1, 1);
        addEncasedTeleportatiumPartsRecipe(OrePrefixes.gearGt, 1, 4);
        addEncasedTeleportatiumPartsRecipe(OrePrefixes.block, 1, 9);

    }

    private static void addEncasedTeleportatiumPartsRecipe(OrePrefixes prefix, int multiplier, int inverseMultiplier) {

        // Space Assembler

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(prefix, Materials.Enderium, multiplier),
                ItemList.Field_Generator_IV.get(1),
                ItemList.Field_Generator_MV.get(1),
                getModItem(AdvancedSolarPanel.ID, "asp_crafting_items", 2, 1),
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "item.HeavyDutyPlateTier5", 1))
            .itemOutputs(GTOreDictUnificator.get(prefix, Materials.TeleportatiumEncased, multiplier))
            .fluidInputs(
                // new FluidStack(solderUEV, 144),
                Materials.TeleportatiumStable.getFluid(288L * inverseMultiplier),
                Materials.EnderAirFortified.getFluid(144L * inverseMultiplier),
                Materials.EnderAirBalanced.getFluid(144L * inverseMultiplier))
            .metadata(IGRecipeMaps.MODULE_TIER, 1)
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(IGRecipeMaps.spaceAssemblerRecipes);

        // Electric compressor

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(prefix, Materials.Netherite, multiplier),
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "item.HeavyDutyPlateTier7", 1),
                getModItem(AdvancedSolarPanel.ID, "asp_crafting_items", 2, 4),
                getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 4L * inverseMultiplier, 6, missing))
            .itemOutputs(GTOreDictUnificator.get(prefix, Materials.TeleportatiumEncased, multiplier))
            .fluidInputs(Materials.TeleportatiumStable.getMolten(288L * inverseMultiplier))
            .fluidOutputs(Materials.EnderAir.getGas(14L * inverseMultiplier))
            .metadata(IGRecipeMaps.MODULE_TIER, 1)
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(electricImplosionCompressorRecipes);

    }

}
