package gtPlusPlus.core.recipe;

import static gregtech.api.enums.GTValues.RA;
import static gregtech.api.enums.Mods.Backpack;
import static gregtech.api.enums.Mods.Baubles;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.recipe.RecipeMaps.alloySmelterRecipes;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.brewingRecipes;
import static gregtech.api.recipe.RecipeMaps.centrifugeRecipes;
import static gregtech.api.recipe.RecipeMaps.compressorRecipes;
import static gregtech.api.recipe.RecipeMaps.distillationTowerRecipes;
import static gregtech.api.recipe.RecipeMaps.distilleryRecipes;
import static gregtech.api.recipe.RecipeMaps.electrolyzerRecipes;
import static gregtech.api.recipe.RecipeMaps.extractorRecipes;
import static gregtech.api.recipe.RecipeMaps.extruderRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidExtractionRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidHeaterRecipes;
import static gregtech.api.recipe.RecipeMaps.fusionRecipes;
import static gregtech.api.recipe.RecipeMaps.laserEngraverRecipes;
import static gregtech.api.recipe.RecipeMaps.maceratorRecipes;
import static gregtech.api.recipe.RecipeMaps.mixerRecipes;
import static gregtech.api.recipe.RecipeMaps.multiblockChemicalReactorRecipes;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.EIGHTH_INGOTS;
import static gregtech.api.util.GTRecipeBuilder.HALF_INGOTS;
import static gregtech.api.util.GTRecipeBuilder.HOURS;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.QUARTER_INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.STACKS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.ADDITIVE_AMOUNT;
import static gregtech.api.util.GTRecipeConstants.AssemblyLine;
import static gregtech.api.util.GTRecipeConstants.BlastFurnaceWithGas;
import static gregtech.api.util.GTRecipeConstants.CHEMPLANT_CASING_TIER;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;
import static gregtech.api.util.GTRecipeConstants.FUEL_TYPE;
import static gregtech.api.util.GTRecipeConstants.FUEL_VALUE;
import static gregtech.api.util.GTRecipeConstants.FUSION_THRESHOLD;
import static gregtech.api.util.GTRecipeConstants.RESEARCH_ITEM;
import static gregtech.api.util.GTRecipeConstants.SCANNING;
import static gregtech.api.util.GTRecipeConstants.UniversalChemical;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.alloyBlastSmelterRecipes;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.chemicalDehydratorRecipes;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.chemicalPlantRecipes;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.cyclotronRecipes;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.thermalBoilerRecipes;
import static gtPlusPlus.core.material.MaterialsAlloy.TITANSTEEL;

import java.util.Arrays;
import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import bartworks.system.material.WerkstoffLoader;
import goodgenerator.items.GGMaterial;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipeConstants;
import gregtech.api.util.GTUtility;
import gregtech.api.util.recipe.Scanning;
import gregtech.loaders.postload.chains.NetheriteRecipes;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.fluids.GTPPFluids;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.item.chemistry.IonParticles;
import gtPlusPlus.core.item.crafting.ItemDummyResearch;
import gtPlusPlus.core.item.crafting.ItemDummyResearch.ASSEMBLY_LINE_RESEARCH;
import gtPlusPlus.core.material.MaterialMisc;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.core.material.MaterialsOres;
import gtPlusPlus.core.material.Particle;
import gtPlusPlus.core.material.nuclear.MaterialsFluorides;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.MaterialUtils;
import gtPlusPlus.xmod.bop.blocks.BOPBlockRegistrator;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.thermalfoundation.fluid.TFFluids;
import ic2.core.Ic2Items;

public class RecipesGregTech {

    public static void run() {
        Logger.INFO("Loading Recipes through GregAPI for Industrial Multiblocks.");
        execute();
    }

    private static void execute() {
        electrolyzerRecipes();
        assemblerRecipes();
        distilleryRecipes();
        extractorRecipes();
        fluidExtractorRecipes();
        chemicalReactorRecipes();
        dehydratorRecipes();
        blastFurnaceRecipes();
        largeChemReactorRecipes();
        fusionRecipes();
        compressorRecipes();
        mixerRecipes();
        macerationRecipes();
        centrifugeRecipes();
        cyclotronRecipes();
        blastSmelterRecipes();
        extruderRecipes();
        breweryRecipes();
        laserEngraverRecipes();
        assemblyLineRecipes();
        fluidHeaterRecipes();
        chemplantRecipes();
        alloySmelterRecipes();
        thermalBoilerRecipes();
        craftingTableRecipes();

        /*
         * Special Recipe handlers
         */
        RecipesSeleniumProcessing.init();
        RecipesRareEarthProcessing.init();
        NetheriteRecipes.run();

        addFuels();
    }

    private static void alloySmelterRecipes() {

        // Wood's Glass Laser Lens
        RA.stdBuilder()
            .itemInputs(MaterialMisc.WOODS_GLASS.getDust(5), ItemList.Shape_Mold_Ball.get(0))
            .itemOutputs(GregtechItemList.Laser_Lens_WoodsGlass.get(1))
            .duration(5 * MINUTES)
            .eut(TierEU.RECIPE_HV)
            .addTo(alloySmelterRecipes);
    }

    private static void chemplantRecipes() {

        // This is subsequently absorbed in water to form nitric acid and nitric oxide.
        // 3 NO2 (g) + H2O (l) → 2 HNO3 (aq) + NO (g) (ΔH = −117 kJ/mol)
        // The nitric oxide is cycled back for reoxidation. Alternatively, if the last step is carried out in air:
        // 4 NO2 (g) + O2 (g) + 2 H2O (l) → 4 HNO3 (aq)

        // Advanced method for Nitric Acid Production
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(17), CI.getPinkCatalyst(0))
            .fluidInputs(
                Materials.NitrogenDioxide.getGas(4_000),
                Materials.Air.getGas(4_000),
                Materials.Water.getFluid(2_000))
            .fluidOutputs(Materials.NitricAcid.getFluid(4_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(CHEMPLANT_CASING_TIER, 3)
            .addTo(chemicalPlantRecipes);

        // Advanced recipe for Fluorine Production
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(17),
                CI.getPurpleCatalyst(0),
                new ItemStack(Blocks.sandstone, 64),
                new ItemStack(Blocks.sandstone, 64))
            .itemOutputs(
                MaterialsFluorides.FLUORITE.getOre(8),
                MaterialsFluorides.FLUORITE.getOre(4),
                MaterialsFluorides.FLUORITE.getOre(4),
                MaterialsFluorides.FLUORITE.getOre(4))
            .fluidInputs(Materials.NitricAcid.getFluid(4_000), Materials.Air.getGas(8_000))
            .duration(10 * SECONDS)
            .eut(1024)
            .metadata(CHEMPLANT_CASING_TIER, 5)
            .addTo(chemicalPlantRecipes);
        // Advanced recipe for Fluorine Production
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(17),
                CI.getPurpleCatalyst(0),
                new ItemStack(Blocks.sand, 64),
                new ItemStack(Blocks.sand, 64))
            .itemOutputs(
                MaterialsFluorides.FLUORITE.getOre(4),
                MaterialsFluorides.FLUORITE.getOre(2),
                MaterialsFluorides.FLUORITE.getOre(2),
                MaterialsFluorides.FLUORITE.getOre(2))
            .fluidInputs(Materials.NitricAcid.getFluid(5_000), Materials.Air.getGas(12_000))
            .duration(10 * SECONDS)
            .eut(1024)
            .metadata(CHEMPLANT_CASING_TIER, 5)
            .addTo(chemicalPlantRecipes);

        // 3NO2 + H2O = 2HNO3 + NO
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(16), CI.getPinkCatalyst(0))
            .fluidInputs(Materials.NitrogenDioxide.getGas(3_000), GTModHandler.getDistilledWater(1_000))
            .fluidOutputs(Materials.NitricAcid.getFluid(2_000), Materials.NitricOxide.getGas(1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(CHEMPLANT_CASING_TIER, 2)
            .addTo(chemicalPlantRecipes);
        // Produce Boric Acid
        // Na2B4O7·10H2O + 2HCl = 4B(OH)3 + 2NaCl + 5H2O
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(21), ItemUtils.getItemStackOfAmountFromOreDict("dustBorax", 23))
            .itemOutputs(ItemUtils.getItemStackOfAmountFromOreDict("dustSalt", 4))
            .fluidInputs(Materials.HydrochloricAcid.getFluid(2_000))
            .fluidOutputs(FluidUtils.getFluidStack("boricacid", 4_000), Materials.Water.getFluid(5_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(CHEMPLANT_CASING_TIER, 3)
            .addTo(chemicalPlantRecipes);

        // Produce Th232
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(22), MaterialsElements.getInstance().THORIUM.getDust(16))
            .itemOutputs(
                MaterialsElements.getInstance().THORIUM.getSmallDust(32),
                MaterialsElements.getInstance().THORIUM232.getDust(2),
                MaterialsElements.getInstance().THORIUM232.getSmallDust(2),
                MaterialsElements.getInstance().URANIUM232.getDust(1))
            .fluidInputs(GTModHandler.getDistilledWater(2_000), FluidUtils.getFluidStack("boricacid", 1_500))
            .duration(5 * MINUTES)
            .eut(TierEU.RECIPE_EV)
            .metadata(CHEMPLANT_CASING_TIER, 4)
            .addTo(chemicalPlantRecipes);

        // Modify Sapling into Pine Sapling
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(6), new ItemStack(Blocks.sapling, 32))
            .itemOutputs(new ItemStack(BOPBlockRegistrator.sapling_Pine, 16))
            .fluidInputs(FluidUtils.getFluidStack("fluid.geneticmutagen", 2_000), GTModHandler.getDistilledWater(8_000))
            .duration(120 * SECONDS)
            .eut(64)
            .metadata(CHEMPLANT_CASING_TIER, 2)
            .addTo(chemicalPlantRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.gem, Materials.NetherStar, 2),
                GTUtility.getIntegratedCircuit(20))
            .fluidInputs(FluidRegistry.getFluidStack("mobessence", 5_000))
            .fluidOutputs(new FluidStack(GTPPFluids.GeneticMutagen, 8_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        int aLaureniumTier = MaterialsAlloy.LAURENIUM.vTier;
        // Adding Recipes for Casings
        GTValues.RA.stdBuilder()
            .itemInputs(
                CI.getTieredMachineCasing(aLaureniumTier - 1),
                MaterialsAlloy.LAURENIUM.getPlate(8),
                CI.getGear(aLaureniumTier, 2))
            .itemOutputs(GregtechItemList.Casing_Machine_Custom_3.get(1))
            .fluidInputs(
                CI.getTieredFluid(aLaureniumTier, 2 * INGOTS),
                CI.getAlternativeTieredFluid(aLaureniumTier - 1, 4 * INGOTS),
                CI.getTertiaryTieredFluid(aLaureniumTier - 2, 6 * INGOTS))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .metadata(CHEMPLANT_CASING_TIER, 5)
            .addTo(chemicalPlantRecipes);

        int aBotmiumTier = MaterialsAlloy.BOTMIUM.vTier;
        // Adding Recipes for Casings
        GTValues.RA.stdBuilder()
            .itemInputs(
                CI.getTieredMachineCasing(aBotmiumTier - 1),
                MaterialsAlloy.BOTMIUM.getPlate(8),
                CI.getGear(aBotmiumTier, 2))
            .itemOutputs(GregtechItemList.Casing_Machine_Custom_4.get(1))
            .fluidInputs(
                CI.getTieredFluid(aBotmiumTier, 2 * INGOTS),
                CI.getAlternativeTieredFluid(aBotmiumTier - 1, 4 * INGOTS),
                CI.getTertiaryTieredFluid(aBotmiumTier - 2, 6 * INGOTS))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .metadata(CHEMPLANT_CASING_TIER, 6)
            .addTo(chemicalPlantRecipes);

        // Refine GT HF into GT++ HF
        if (FluidUtils.doesHydrofluoricAcidGtExist()) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.getIntegratedCircuit(22))
                .fluidInputs(FluidUtils.getHydrofluoricAcid(2_000), FluidUtils.getHydrofluoricAcidGT(5_000))
                .fluidOutputs(FluidUtils.getHydrofluoricAcid(4_500))
                .duration(30 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .metadata(CHEMPLANT_CASING_TIER, 3)
                .addTo(chemicalPlantRecipes);

        }
    }

    private static void fluidHeaterRecipes() {
        RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(20))
            .fluidInputs(Materials.Water.getFluid(1_000))
            .fluidOutputs(FluidUtils.getHotWater(1_000))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(fluidHeaterRecipes);
    }

    private static void fusionRecipes() {
        // Hypogen
        RA.stdBuilder()
            .fluidInputs(
                MaterialsElements.STANDALONE.DRAGON_METAL.getFluidStack(1 * INGOTS),
                MaterialsElements.STANDALONE.RHUGNOR.getFluidStack(2 * INGOTS))
            .fluidOutputs(MaterialsElements.STANDALONE.HYPOGEN.getFluidStack(36))
            .duration(6 * MINUTES + 49 * SECONDS + 12 * TICKS)
            .eut(TierEU.RECIPE_UHV)
            .metadata(FUSION_THRESHOLD, 1_200_000_000L)
            .addTo(fusionRecipes);

        // Rhugnor
        RA.stdBuilder()
            .fluidInputs(
                MaterialUtils.getMaterial("Infinity", "Neutronium")
                    .getMolten(1 * INGOTS),
                MaterialsAlloy.QUANTUM.getFluidStack(2 * INGOTS))
            .fluidOutputs(MaterialsElements.STANDALONE.RHUGNOR.getFluidStack(1 * INGOTS))
            .duration(25 * SECONDS + 12 * TICKS)
            .eut(TierEU.RECIPE_UV)
            .metadata(FUSION_THRESHOLD, 2_000_000_000L)
            .addTo(fusionRecipes);

        // Rhugnor Mk5
        RA.stdBuilder()
            .fluidInputs(
                MaterialsUEVplus.QuarkGluonPlasma.getFluid(1 * HALF_INGOTS),
                MaterialsAlloy.QUANTUM.getFluidStack(4 * INGOTS))
            .fluidOutputs(MaterialsElements.STANDALONE.RHUGNOR.getFluidStack(4 * INGOTS))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_UEV)
            .metadata(FUSION_THRESHOLD, 2_000_000_000L)
            .addTo(fusionRecipes);
    }

    private static void assemblyLineRecipes() {

        // Containment Casings
        RA.stdBuilder()
            .metadata(
                RESEARCH_ITEM,
                ItemDummyResearch.getResearchStack(ASSEMBLY_LINE_RESEARCH.RESEARCH_1_CONTAINMENT, 1))
            .metadata(SCANNING, new Scanning(50 * SECONDS, TierEU.RECIPE_IV))
            .itemInputs(
                ItemList.Field_Generator_IV.get(32),
                ItemList.Electric_Motor_EV.get(64),
                ItemList.Energy_LapotronicOrb.get(32),
                CI.getTieredComponent(OrePrefixes.cableGt12, 7, 32),
                CI.getTieredComponent(OrePrefixes.wireGt16, 6, 64),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Naquadria, 64L),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Gadolinium, 32L),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Samarium, 16L),
                MaterialsAlloy.ARCANITE.getGear(8),
                new Object[] { "circuitElite", 64 },
                new Object[] { "circuitMaster", 32 },
                new Object[] { "circuitUltimate", 16 },
                GregtechItemList.Laser_Lens_Special.get(1),
                GregtechItemList.DehydratorCoilWireZPM.get(64))
            .fluidInputs(
                MaterialsAlloy.NITINOL_60.getFluidStack(36 * INGOTS),
                MaterialsAlloy.ENERGYCRYSTAL.getFluidStack(1 * STACKS + 8 * INGOTS),
                MaterialsAlloy.TUMBAGA.getFluidStack(4 * STACKS + 32 * INGOTS),
                Materials.Nichrome.getMolten(16 * INGOTS))
            .itemOutputs(new ItemStack(ModBlocks.blockCasings3Misc, 32, 15))
            .eut(TierEU.RECIPE_LuV)
            .duration(20 * MINUTES)
            .addTo(AssemblyLine);

        // Turbine Housing / Turbine Automation Port
        RA.stdBuilder()
            .metadata(
                RESEARCH_ITEM,
                ItemDummyResearch.getResearchStack(ASSEMBLY_LINE_RESEARCH.RESEARCH_8_TURBINE_AUTOMATION, 1))
            .metadata(SCANNING, new Scanning(1 * MINUTES, TierEU.RECIPE_ZPM))
            .itemInputs(
                CI.getTieredMachineHull(8, 4),
                CI.getConveyor(8, 24),
                CI.getElectricMotor(7, 32),
                CI.getElectricPiston(7, 16),
                CI.getEnergyCore(6, 8),
                CI.getPlate(8, 24),
                CI.getTieredComponent(OrePrefixes.screw, 8, 48),
                CI.getTieredComponent(OrePrefixes.bolt, 7, 32),
                CI.getTieredComponent(OrePrefixes.rod, 6, 12),
                new Object[] { "circuitUltimate", 20 },
                CI.getTieredComponent(OrePrefixes.rotor, 6, 16))
            .fluidInputs(
                CI.getTieredFluid(8, 32 * INGOTS),
                CI.getAlternativeTieredFluid(7, 16 * INGOTS),
                CI.getTertiaryTieredFluid(7, 16 * INGOTS),
                MaterialsAlloy.BABBIT_ALLOY.getFluidStack(2 * STACKS))
            .itemOutputs(GregtechItemList.Hatch_Input_TurbineHousing.get(4))
            .eut(TierEU.RECIPE_UV)
            .duration(2 * MINUTES)
            .addTo(AssemblyLine);

        /*
         * Containment casings
         */
        ItemStack[] aCoilWire = new ItemStack[] { GregtechItemList.DehydratorCoilWireEV.get(64),
            GregtechItemList.DehydratorCoilWireIV.get(64), GregtechItemList.DehydratorCoilWireLuV.get(64),
            GregtechItemList.DehydratorCoilWireZPM.get(64) };
        ItemStack[] aGemCasings = new ItemStack[] { GregtechItemList.Battery_Casing_Gem_1.get(1),
            GregtechItemList.Battery_Casing_Gem_2.get(1), GregtechItemList.Battery_Casing_Gem_3.get(1),
            GregtechItemList.Battery_Casing_Gem_4.get(1), };
        ItemStack[] aResearch = new ItemStack[] { Particle.getBaseParticle(Particle.UNKNOWN),
            GregtechItemList.Battery_Casing_Gem_1.get(1), GregtechItemList.Battery_Casing_Gem_2.get(1),
            GregtechItemList.Battery_Casing_Gem_3.get(1), };

        int aCasingSlot = 0;
        for (int j = 6; j < 10; j++) {
            RA.stdBuilder()
                .metadata(RESEARCH_ITEM, aResearch[aCasingSlot])
                .metadata(SCANNING, new Scanning(50 * SECONDS, GTValues.VP[aCasingSlot + 5]))
                .itemInputs(
                    CI.getTieredComponent(OrePrefixes.plate, j - 1, 16),
                    CI.getTieredComponent(OrePrefixes.cableGt08, j + 1, 32),
                    CI.getTieredComponent(OrePrefixes.gearGt, j - 1, 4),
                    aCoilWire[aCasingSlot])
                .fluidInputs(
                    CI.getTieredFluid(j, 8 * INGOTS),
                    CI.getTertiaryTieredFluid(j - 2, 16 * INGOTS),
                    CI.getAlternativeTieredFluid(j, 16 * INGOTS))
                .itemOutputs(aGemCasings[aCasingSlot++])
                .eut(GTValues.VP[j])
                .duration(2 * MINUTES)
                .addTo(AssemblyLine);
        }

        /*
         * Gem Battery Recipes
         */

        ItemStack[] aGemBatteries = new ItemStack[] { GregtechItemList.Battery_Gem_1.get(1),
            GregtechItemList.Battery_Gem_2.get(1), GregtechItemList.Battery_Gem_3.get(1),
            GregtechItemList.Battery_Gem_4.get(1), };

        ItemStack[] aExoticInputs = new ItemStack[] { Particle.getBaseParticle(Particle.PROTON),
            Particle.getBaseParticle(Particle.ELECTRON), Particle.getBaseParticle(Particle.CHARM),
            Particle.getBaseParticle(Particle.GRAVITON) };
        aCasingSlot = 0;
        for (int j = 6; j < 10; j++) {
            RA.stdBuilder()
                .metadata(RESEARCH_ITEM, aExoticInputs[aCasingSlot])
                .metadata(SCANNING, new Scanning(1 * MINUTES, GTValues.VP[aCasingSlot + 7]))
                .itemInputs(
                    aGemCasings[aCasingSlot],
                    GTUtility.copyAmount(16, aExoticInputs[aCasingSlot]),
                    CI.getTieredComponent(OrePrefixes.plate, j, 16),
                    new Object[] { CI.circuits[j], 8 },
                    CI.getTieredComponent(OrePrefixes.wireGt16, j + 1, 32),
                    CI.getTieredComponent(OrePrefixes.bolt, j, 8),
                    CI.getTieredComponent(OrePrefixes.screw, j - 1, 8))
                .fluidInputs(
                    CI.getTieredFluid(j, 16 * INGOTS),
                    CI.getTertiaryTieredFluid(j - 2, 32 * INGOTS),
                    CI.getAlternativeTieredFluid(j, 16 * INGOTS),
                    CI.getTertiaryTieredFluid(j - 1, 16 * INGOTS))
                .itemOutputs(aGemBatteries[aCasingSlot++])
                .eut(GTValues.VP[j])
                .duration(2 * MINUTES)
                .addTo(AssemblyLine);
        }

        if (Baubles.isModLoaded()) {
            // Personal Healing Nanobooster
            RA.stdBuilder()
                .metadata(RESEARCH_ITEM, new ItemStack(Items.golden_apple, 1, 1))
                .metadata(SCANNING, new Scanning(2 * MINUTES, TierEU.RECIPE_LuV))
                .itemInputs(
                    GTUtility.copyAmount(4, aGemCasings[2]),
                    CI.getTieredComponent(OrePrefixes.plate, 8, 32),
                    new Object[] { "circuitUltimate", 16 },
                    CI.getTieredComponent(OrePrefixes.cableGt02, 7, 16),
                    CI.getTieredComponent(OrePrefixes.gearGt, 6, 6),
                    CI.getTieredComponent(OrePrefixes.screw, 7, 16),
                    CI.getTieredComponent(OrePrefixes.bolt, 5, 24),
                    CI.getTieredComponent(OrePrefixes.frameGt, 4, 12),
                    GregtechItemList.DehydratorCoilWireZPM.get(64))
                .fluidInputs(
                    CI.getTieredFluid(7, 4 * STACKS + 32 * INGOTS),
                    CI.getTertiaryTieredFluid(7, 4 * STACKS + 32 * INGOTS),
                    CI.getAlternativeTieredFluid(6, 4 * STACKS + 32 * INGOTS),
                    CI.getAlternativeTieredFluid(7, 4 * STACKS + 32 * INGOTS))
                .itemOutputs(GregtechItemList.PersonalHealingDevice.get(1))
                .eut(TierEU.RECIPE_ZPM)
                .duration(3 * MINUTES)
                .addTo(AssemblyLine);

            // Charge Pack LuV-UV

            ItemStack[] aChargeResearch = new ItemStack[] { GregtechItemList.Energy_Core_LuV.get(1),
                GregtechItemList.Energy_Core_ZPM.get(1), GregtechItemList.Energy_Core_UV.get(1),
                GregtechItemList.Energy_Core_UHV.get(1) };

            ItemStack[] aChargeOutputs = new ItemStack[] { new ItemStack(ModItems.itemChargePack_High_1, 1),
                new ItemStack(ModItems.itemChargePack_High_2, 1), new ItemStack(ModItems.itemChargePack_High_3, 1),
                new ItemStack(ModItems.itemChargePack_High_4, 1), };

            int aCurrSlot = 0;
            for (int h = 6; h < 9; h++) {
                RA.stdBuilder()
                    .metadata(RESEARCH_ITEM, aChargeResearch[aCurrSlot])
                    .metadata(SCANNING, new Scanning(30 * SECONDS, GTValues.VP[aCurrSlot + 5]))
                    .itemInputs(
                        GTUtility.copyAmount(2, aGemBatteries[aCurrSlot]),
                        aCoilWire[aCurrSlot],
                        CI.getTieredComponent(OrePrefixes.plate, h, 8),
                        new Object[] { CI.circuits[h], 4 },
                        new Object[] { CI.circuits[h - 1], 8 },
                        CI.getTieredComponent(OrePrefixes.cableGt12, h - 1, 16),
                        CI.getTieredComponent(OrePrefixes.screw, h, 16),
                        CI.getTieredComponent(OrePrefixes.bolt, h - 2, 32),
                        CI.getFieldGenerator(h - 1, 1))
                    .fluidInputs(
                        CI.getTieredFluid(h, 32 * INGOTS),
                        CI.getTertiaryTieredFluid(h - 1, 32 * INGOTS),
                        CI.getAlternativeTieredFluid(h - 1, 32 * INGOTS),
                        CI.getAlternativeTieredFluid(h - 2, 32 * INGOTS))
                    .itemOutputs(aChargeOutputs[aCurrSlot])
                    .eut(GTValues.VP[h])
                    .duration(3 * MINUTES)
                    .addTo(AssemblyLine);
                aCurrSlot++;
            }

            RA.stdBuilder()
                .metadata(RESEARCH_ITEM, aChargeResearch[aCurrSlot])
                .metadata(SCANNING, new Scanning(30 * SECONDS, GTValues.VP[aCurrSlot + 5]))
                .itemInputs(
                    GTUtility.copyAmount(2, aGemBatteries[aCurrSlot]),
                    aCoilWire[aCurrSlot],
                    CI.getTieredComponent(OrePrefixes.plate, 9, 8),
                    new Object[] { "circuitInfinite", 4 },
                    new Object[] { "circuitSuperconductor", 8 },
                    CI.getTieredComponent(OrePrefixes.cableGt12, 8, 16),
                    CI.getTieredComponent(OrePrefixes.screw, 9, 16),
                    CI.getTieredComponent(OrePrefixes.bolt, 7, 32),
                    CI.getFieldGenerator(8, 1))
                .fluidInputs(
                    CI.getTieredFluid(9, 32 * INGOTS),
                    CI.getTertiaryTieredFluid(8, 1 * STACKS),
                    CI.getAlternativeTieredFluid(7, 32 * INGOTS))
                .itemOutputs(aChargeOutputs[aCurrSlot])
                .eut(GTValues.VP[9])
                .duration(3 * MINUTES)
                .addTo(AssemblyLine);

            // Personal Cloaking device
            RA.stdBuilder()
                .metadata(
                    RESEARCH_ITEM,
                    ItemDummyResearch.getResearchStack(ASSEMBLY_LINE_RESEARCH.RESEARCH_9_CLOAKING, 1))
                .metadata(SCANNING, new Scanning(1 * MINUTES + 40 * SECONDS, TierEU.RECIPE_ZPM))
                .itemInputs(
                    GTUtility.copyAmount(4, aGemCasings[3]),
                    CI.getTieredComponent(OrePrefixes.plate, 8, 32),
                    new Object[] { "circuitUltimate", 16 },
                    CI.getTieredComponent(OrePrefixes.cableGt04, 8, 16),
                    CI.getTieredComponent(OrePrefixes.gearGt, 7, 6),
                    CI.getTieredComponent(OrePrefixes.screw, 8, 16),
                    CI.getTieredComponent(OrePrefixes.bolt, 7, 24),
                    CI.getTieredComponent(OrePrefixes.frameGt, 5, 12),
                    GregtechItemList.DehydratorCoilWireZPM.get(64))
                .fluidInputs(
                    CI.getTieredFluid(8, 4 * STACKS + 32 * INGOTS),
                    CI.getTertiaryTieredFluid(8, 4 * STACKS + 32 * INGOTS),
                    CI.getAlternativeTieredFluid(7, 9 * STACKS))
                .itemOutputs(GregtechItemList.PersonalCloakingDevice.get(1))
                .eut(TierEU.RECIPE_UV)
                .duration(3 * MINUTES)
                .addTo(AssemblyLine);
        }

        // Mega Alloy Blast Smelter
        RA.stdBuilder()
            .metadata(RESEARCH_ITEM, GregtechItemList.Industrial_AlloyBlastSmelter.get(1, new Object() {}))
            .metadata(SCANNING, new Scanning(2 * MINUTES + 20 * SECONDS, TierEU.RECIPE_ZPM))
            .itemInputs(
                GregtechItemList.Industrial_AlloyBlastSmelter.get(64L, new Object() {}),
                GregtechItemList.Industrial_AlloyBlastSmelter.get(64L, new Object() {}),
                GregtechItemList.Industrial_AlloyBlastSmelter.get(64L, new Object() {}),
                GregtechItemList.Industrial_AlloyBlastSmelter.get(64L, new Object() {}),
                ItemList.UV_Coil.get(16L, new Object() {}),
                ItemList.Conveyor_Module_UV.get(4L, new Object() {}),
                new Object[] { OrePrefixes.circuit.get(Materials.UV), 8 },
                new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 16 },
                ItemList.Circuit_Chip_PPIC.get(16, new Object() {}),
                MaterialsAlloy.PIKYONIUM.getPlate(16),
                MaterialsAlloy.CINOBITE.getScrew(32))
            .fluidInputs(
                MaterialsAlloy.PIKYONIUM.getFluidStack(8 * INGOTS),
                MaterialsAlloy.INDALLOY_140.getFluidStack(9 * INGOTS),
                Materials.SolderingAlloy.getMolten(10 * INGOTS))
            .itemOutputs(GregtechItemList.Mega_AlloyBlastSmelter.get(1L))
            .eut(TierEU.RECIPE_UHV / 2)
            .duration(1 * MINUTES)
            .addTo(AssemblyLine);

        // Expandable Hand Pump
        RA.stdBuilder()
            .metadata(RESEARCH_ITEM, new ItemStack(ModItems.itemGenericToken, 1, 4))
            .metadata(SCANNING, new Scanning(1 * MINUTES + 30 * SECONDS, TierEU.RECIPE_IV))
            .itemInputs(
                ItemList.Electric_Pump_LuV.get(4),
                ItemList.Electric_Motor_LuV.get(4),
                GregtechItemList.VOLUMETRIC_FLASK_32k.get(4),
                MaterialsAlloy.LAFIUM.getScrew(16),
                WerkstoffLoader.LuVTierMaterial.get(OrePrefixes.ring, 8),
                WerkstoffLoader.LuVTierMaterial.get(OrePrefixes.stick, 16),
                Materials.Osmiridium.getPlates(32))
            .fluidInputs(
                MaterialsAlloy.HELICOPTER.getFluidStack(32 * INGOTS),
                MaterialsAlloy.INDALLOY_140.getFluidStack(1 * STACKS))
            .itemOutputs(GregtechItemList.ExpandableHandPump.get(1))
            .eut(TierEU.RECIPE_LuV)
            .duration(30 * SECONDS)
            .addTo(AssemblyLine);
    }

    private static void laserEngraverRecipes() {

        // Laser Sensors and Emitters together
        GregtechItemList[] aTransParts = new GregtechItemList[] { GregtechItemList.TransmissionComponent_LV,
            GregtechItemList.TransmissionComponent_MV, GregtechItemList.TransmissionComponent_HV,
            GregtechItemList.TransmissionComponent_EV, GregtechItemList.TransmissionComponent_IV,
            GregtechItemList.TransmissionComponent_LuV, GregtechItemList.TransmissionComponent_ZPM,
            GregtechItemList.TransmissionComponent_UV, GregtechItemList.TransmissionComponent_UHV, };
        for (int i = 1; i <= aTransParts.length; i++) {
            RA.stdBuilder()
                .itemInputs(CI.getEmitter(i, 2), CI.getSensor(i, 2))
                .itemOutputs(aTransParts[i - 1].get(1))
                .duration(5 * SECONDS)
                .eut(GTValues.VP[i])
                .addTo(laserEngraverRecipes);
        }

        RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Tungsten, 6L),
                GregtechItemList.Laser_Lens_Special.get(0))
            .itemOutputs(MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getDust(1))
            .duration(3 * MINUTES)
            .eut(TierEU.RECIPE_UEV)
            .addTo(laserEngraverRecipes);

        RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Titanium, 8L),
                GregtechItemList.Laser_Lens_Special.get(0))
            .itemOutputs(MaterialsElements.STANDALONE.ASTRAL_TITANIUM.getDust(1))
            .duration(2 * MINUTES)
            .eut(TierEU.RECIPE_UHV)
            .addTo(laserEngraverRecipes);

        RA.stdBuilder()
            .itemInputs(MaterialsAlloy.NITINOL_60.getBlock(2), GregtechItemList.Laser_Lens_Special.get(0))
            .itemOutputs(MaterialsElements.STANDALONE.ADVANCED_NITINOL.getBlock(1))
            .duration(1 * MINUTES)
            .eut(TierEU.RECIPE_UV)
            .addTo(laserEngraverRecipes);

        RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Glass, 64L),
                GregtechItemList.Laser_Lens_Special.get(0))
            .itemOutputs(MaterialsElements.STANDALONE.CHRONOMATIC_GLASS.getDust(1))
            .duration(5 * MINUTES)
            .eut(TierEU.RECIPE_UHV)
            .addTo(laserEngraverRecipes);

        RA.stdBuilder()
            .itemInputs(CI.getFieldGenerator(6, 1), CI.getEmitter(7, 2))
            .itemOutputs(ItemDummyResearch.getResearchStack(ASSEMBLY_LINE_RESEARCH.RESEARCH_1_CONTAINMENT, 1))
            .duration(5 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(laserEngraverRecipes);

    }

    private static void breweryRecipes() {

        if (Mods.OpenBlocks.isModLoaded()) {
            RA.stdBuilder()
                .itemInputs(GTUtility.getIntegratedCircuit(14))
                .fluidInputs(FluidRegistry.getFluidStack("mobessence", 100))
                .fluidOutputs(FluidRegistry.getFluidStack("xpjuice", 1_332))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(brewingRecipes);
            RA.stdBuilder()
                .itemInputs(GTUtility.getIntegratedCircuit(14))
                .fluidInputs(FluidRegistry.getFluidStack("xpjuice", 1_332))
                .fluidOutputs(FluidRegistry.getFluidStack("mobessence", 100))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(brewingRecipes);
        }

        RA.stdBuilder()
            .itemInputs(new ItemStack(BOPBlockRegistrator.sapling_Rainforest))
            .fluidInputs(Materials.Water.getFluid(100))
            .fluidOutputs(Materials.Biomass.getFluid(100))
            .duration(1 * MINUTES)
            .eut(3)
            .addTo(brewingRecipes);
        RA.stdBuilder()
            .itemInputs(new ItemStack(BOPBlockRegistrator.sapling_Rainforest))
            .fluidInputs(Materials.Honey.getFluid(100))
            .fluidOutputs(Materials.Biomass.getFluid(100))
            .duration(1 * MINUTES)
            .eut(3)
            .addTo(brewingRecipes);
        RA.stdBuilder()
            .itemInputs(new ItemStack(BOPBlockRegistrator.sapling_Rainforest))
            .fluidInputs(FluidUtils.getFluidStack("juice", 100))
            .fluidOutputs(Materials.Biomass.getFluid(100))
            .duration(1 * MINUTES)
            .eut(3)
            .addTo(brewingRecipes);
    }

    private static void electrolyzerRecipes() {
        RA.stdBuilder()
            .itemInputs(new ItemStack(ModItems.dustDecayedRadium226, 1))
            .fluidOutputs(Materials.Radon.getGas(1 * INGOTS))
            .duration(1 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(electrolyzerRecipes);
    }

    private static void extruderRecipes() {
        // Osmium Credits
        RA.stdBuilder()
            .itemInputs(ItemUtils.getItemStackOfAmountFromOreDict("blockOsmium", 1), ItemList.Shape_Mold_Credit.get(0))
            .itemOutputs(ItemList.Credit_Greg_Osmium.get(1))
            .duration(6 * MINUTES + 20 * SECONDS)
            .eut(TierEU.RECIPE_EV / 2)
            .addTo(extruderRecipes);
    }

    private static void blastSmelterRecipes() {

        // Enderium
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(5),
                Materials.Tin.getDust(2),
                Materials.Platinum.getDust(1),
                Materials.Silver.getDust(1),
                Materials.Thaumium.getDust(2),
                Materials.EnderPearl.getDust(2))
            .fluidOutputs(Materials.Enderium.getMolten(8 * INGOTS))
            .eut(TierEU.RECIPE_EV)
            .duration(20 * SECONDS + 1 * MINUTES)
            .addTo(alloyBlastSmelterRecipes);

        // Signalium
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(3),
                Materials.AnnealedCopper.getDust(30),
                Materials.Ardite.getDust(10),
                Materials.Redstone.getDust(50))
            .fluidOutputs(GGMaterial.signalium.getMolten(5 * INGOTS))
            .eut(TierEU.RECIPE_LuV)
            .duration(5 * MINUTES)
            .addTo(alloyBlastSmelterRecipes);

        // Lumiium
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(6),
                Materials.Tin.getDust(10),
                Materials.Iron.getDust(10),
                Materials.Copper.getDust(2),
                Materials.Silver.getDust(8),
                GGMaterial.lumiinessence.get(OrePrefixes.dust, 10),
                Materials.Glowstone.getDust(10))
            .fluidOutputs(GGMaterial.lumiium.getMolten(5 * INGOTS))
            .eut(TierEU.RECIPE_LuV)
            .duration(5 * MINUTES)
            .addTo(alloyBlastSmelterRecipes);

        // Eglin Steel
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(6),
                MaterialsElements.getInstance().IRON.getDust(4),
                MaterialsAlloy.KANTHAL.getDust(1),
                MaterialsAlloy.INVAR.getDust(5),
                MaterialsElements.getInstance().SULFUR.getDust(1),
                MaterialsElements.getInstance().CARBON.getDust(1),
                MaterialsElements.getInstance().SILICON.getDust(4))
            .fluidOutputs(MaterialsAlloy.EGLIN_STEEL.getFluidStack(16 * INGOTS))
            .eut(TierEU.RECIPE_MV)
            .duration(45 * SECONDS)
            .addTo(alloyBlastSmelterRecipes);

        // HG1223
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(5),
                MaterialsElements.getInstance().BARIUM.getDust(2),
                MaterialsElements.getInstance().CALCIUM.getDust(2),
                MaterialsElements.getInstance().COPPER.getDust(3))
            .fluidInputs(Materials.Oxygen.getGas(8_000), Materials.Mercury.getFluid(1_000))
            .fluidOutputs(MaterialsAlloy.HG1223.getFluidStack(16 * INGOTS))
            .eut(TierEU.RECIPE_LuV)
            .duration(2 * MINUTES)
            .addTo(alloyBlastSmelterRecipes);

        // NITINOL_60
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(2),
                MaterialsElements.getInstance().TITANIUM.getDust(3),
                MaterialsElements.getInstance().NICKEL.getDust(2))
            .fluidOutputs(MaterialsAlloy.NITINOL_60.getFluidStack(5 * INGOTS))
            .eut(TierEU.RECIPE_IV)
            .duration(1 * MINUTES + 15 * SECONDS)
            .addTo(alloyBlastSmelterRecipes);

        // INDALLOY_140
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(5),
                MaterialsElements.getInstance().BISMUTH.getDust(47),
                MaterialsElements.getInstance().LEAD.getDust(25),
                MaterialsElements.getInstance().TIN.getDust(13),
                MaterialsElements.getInstance().CADMIUM.getDust(10),
                MaterialsElements.getInstance().INDIUM.getDust(5))
            .fluidOutputs(MaterialsAlloy.INDALLOY_140.getFluidStack(1 * STACKS + 36 * INGOTS))
            .eut(TierEU.RECIPE_IV)
            .duration(40 * SECONDS)
            .addTo(alloyBlastSmelterRecipes);

        // Germanium Roasting
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(15),
                ItemUtils.getItemStackOfAmountFromOreDict("crushedPurifiedSphalerite", 1),
                MaterialsElements.getInstance().CARBON.getDust(4))
            .fluidInputs(Materials.SulfuricAcid.getFluid(250))
            .fluidOutputs(MaterialsElements.getInstance().GERMANIUM.getFluidStack(1 * QUARTER_INGOTS))
            .eut(4_000)
            .duration(37 * SECONDS + 10 * TICKS)
            .addTo(alloyBlastSmelterRecipes);

        // Rhenium Roasting
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(20),
                ItemUtils.getItemStackOfAmountFromOreDict("crushedPurifiedScheelite", 1),
                MaterialsElements.getInstance().CARBON.getDust(4))
            .fluidInputs(Materials.SulfuricAcid.getFluid(1_250))
            .fluidOutputs(MaterialsElements.getInstance().RHENIUM.getFluidStack(1 * EIGHTH_INGOTS))
            .eut(4_000)
            .duration(1 * MINUTES + 15 * SECONDS)
            .addTo(alloyBlastSmelterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(20),
                ItemUtils.getItemStackOfAmountFromOreDict("crushedPurifiedMolybdenite", 2),
                MaterialsElements.getInstance().CARBON.getDust(8))
            .fluidInputs(Materials.SulfuricAcid.getFluid(1_875))
            .fluidOutputs(MaterialsElements.getInstance().RHENIUM.getFluidStack(1 * QUARTER_INGOTS))
            .eut(4_000)
            .duration(37 * SECONDS + 10 * TICKS)
            .addTo(alloyBlastSmelterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(20),
                ItemUtils.getItemStackOfAmountFromOreDict("crushedPurifiedMolybdenum", 1),
                MaterialsElements.getInstance().CARBON.getDust(4))
            .fluidInputs(Materials.SulfuricAcid.getFluid(625))
            .fluidOutputs(MaterialsElements.getInstance().RHENIUM.getFluidStack(1 * QUARTER_INGOTS))
            .eut(4_000)
            .duration(37 * SECONDS + 10 * TICKS)
            .addTo(alloyBlastSmelterRecipes);

        // Thallium Roasting
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(21),
                ItemUtils.getItemStackOfAmountFromOreDict("crushedPurifiedZinc", 3),
                ItemUtils.getItemStackOfAmountFromOreDict("crushedPurifiedPyrite", 4),
                MaterialsElements.getInstance().CARBON.getDust(16))
            .fluidInputs(Materials.SulfuricAcid.getFluid(1_250))
            .fluidOutputs(MaterialsElements.getInstance().THALLIUM.getFluidStack(2 * INGOTS))
            .eut(TierEU.RECIPE_IV)
            .duration(1 * MINUTES + 15 * SECONDS)
            .addTo(alloyBlastSmelterRecipes);

        // Strontium processing
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(21),
                MaterialMisc.STRONTIUM_OXIDE.getDust(8),
                MaterialsElements.getInstance().ALUMINIUM.getDust(8))
            .fluidOutputs(
                MaterialsElements.getInstance().OXYGEN.getFluidStack(8_000),
                MaterialsElements.getInstance().ALUMINIUM.getFluidStack(8 * INGOTS),
                MaterialsElements.getInstance().STRONTIUM.getFluidStack(8 * INGOTS))
            .eut(TierEU.RECIPE_EV)
            .duration(2 * MINUTES)
            .addTo(alloyBlastSmelterRecipes);

        // molten botmium
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(4),
                ItemUtils.getItemStackOfAmountFromOreDict("dustNitinol60", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustOsmium", 6),
                ItemUtils.getItemStackOfAmountFromOreDict("dustRuthenium", 6),
                ItemUtils.getItemStackOfAmountFromOreDict("dustThallium", 3))
            .fluidOutputs(MaterialsAlloy.BOTMIUM.getFluidStack(16 * INGOTS))
            .eut(TierEU.RECIPE_UV)
            .duration(2 * MINUTES)
            .addTo(alloyBlastSmelterRecipes);

        // lossless phonon transfer medium
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(5),
                WerkstoffLoader.MagnetoResonaticDust.get(OrePrefixes.dust, 5),
                GGMaterial.metastableOganesson.get(OrePrefixes.dust, 1),
                Materials.Praseodymium.getDust(15),
                Materials.SuperconductorUIVBase.getDust(6))
            .fluidInputs(MaterialsUEVplus.PhononCrystalSolution.getFluid(4_000))
            .fluidOutputs(MaterialsUEVplus.PhononMedium.getFluid(1_000))
            .eut(TierEU.RECIPE_UIV)
            .duration(2 * MINUTES)
            .addTo(alloyBlastSmelterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(5),
                MaterialsUEVplus.TranscendentMetal.getDust(2),
                GGMaterial.tairitsu.get(OrePrefixes.dust, 2),
                Materials.Tartarite.getDust(2),
                TITANSTEEL.getDust(1),
                Materials.Infinity.getDust(1))
            .fluidInputs(MaterialsUEVplus.DimensionallyTranscendentResidue.getFluid(1_000))
            .fluidOutputs(MaterialsUEVplus.MoltenProtoHalkoniteBase.getFluid(8 * INGOTS))
            .eut(TierEU.RECIPE_UEV)
            .duration(60 * SECONDS)
            .addTo(alloyBlastSmelterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(6),
                Materials.Iron.getDust(12),
                Materials.Nickel.getDust(10),
                Materials.Cobalt.getDust(8),
                Materials.Titanium.getDust(4),
                Materials.Molybdenum.getDust(2),
                Materials.Aluminium.getDust(1))
            .fluidOutputs(GGMaterial.incoloy903.getMolten(37 * INGOTS))
            .eut(TierEU.RECIPE_IV)
            .duration(1 * MINUTES)
            .addTo(alloyBlastSmelterRecipes);
    }

    private static void dehydratorRecipes() {
        Logger.INFO("Loading Recipes for Chemical Dehydrator.");

        ItemStack cropGrape = ItemUtils.getItemStackOfAmountFromOreDictNoBroken("cropGrape", 1);
        ItemStack foodRaisins = ItemUtils.getItemStackOfAmountFromOreDictNoBroken("foodRaisins", 1);

        if (cropGrape != null && foodRaisins != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(cropGrape)
                .itemOutputs(foodRaisins)
                .eut(2)
                .duration(10 * TICKS)
                .addTo(chemicalDehydratorRecipes);
        }

        // Process Waste Water
        GTValues.RA.stdBuilder()
            .itemOutputs(
                new ItemStack(Blocks.dirt),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Clay, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 1L),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Copper, 1L),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Tin, 1L),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Nickel, 1L),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Aluminium, 1L),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Silver, 1L),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Gold, 1L))
            .outputChances(20_00, 5_00, 10, 7, 6, 5, 4, 3, 2)
            .fluidInputs(FluidUtils.getFluidStack("sludge", 1_000))
            .fluidOutputs(Materials.Methane.getGas(100))
            .eut(TierEU.RECIPE_HV)
            .duration(2 * SECONDS)
            .addTo(chemicalDehydratorRecipes);

        // C8H10 = C8H8 + 2H
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(17), ItemList.Cell_Empty.get(3))
            .itemOutputs(
                ItemUtils.getItemStackOfAmountFromOreDict("cellStyrene", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("cellHydrogen", 2))
            .fluidInputs(FluidUtils.getFluidStack("fluid.ethylbenzene", 1_000))
            .eut(TierEU.RECIPE_LV)
            .duration(3 * SECONDS)
            .addTo(chemicalDehydratorRecipes);

        /*
         * Add custom recipes for drying leather
         */
        if (Backpack.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.getIntegratedCircuit(18), new ItemStack(Items.leather, 2))
                .itemOutputs(getModItem(Backpack.ID, "tannedLeather", 1))
                .fluidInputs(FluidUtils.getFluidStack("fluid.ethylbenzene", 1_000))
                .eut(180)
                .duration(5 * SECONDS)
                .addTo(chemicalDehydratorRecipes);

            if (NewHorizonsCoreMod.isModLoaded()) {
                GTValues.RA.stdBuilder()
                    .itemInputs(
                        GTUtility.getIntegratedCircuit(18),
                        getModItem(NewHorizonsCoreMod.ID, "item.ArtificialLeather", 2L, 0))
                    .itemOutputs(getModItem(Backpack.ID, "tannedLeather", 1))
                    .fluidInputs(FluidUtils.getFluidStack("fluid.ethylbenzene", 1_000))
                    .eut(180)
                    .duration(5 * SECONDS)
                    .addTo(chemicalDehydratorRecipes);
            }
        }
        // Alternative ACETIC ANHYDRIDE recipe for Kevlar Line
        // 2C2H4O2 = C4H6O3 + H2O
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Cell_Empty.get(1))
            .itemOutputs(ItemUtils.getItemStackOfAmountFromOreDict("cellWater", 1))
            .fluidInputs(FluidUtils.getFluidStack("aceticacid", 2_000))
            .fluidOutputs(MaterialMisc.ACETIC_ANHYDRIDE.getFluidStack(1_000))
            .eut(TierEU.RECIPE_HV)
            .duration(30 * SECONDS)
            .addTo(chemicalDehydratorRecipes);
    }

    private static void largeChemReactorRecipes() {
        // Styrene
        // C8H10 = C8H8 + 2H
        RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(24))
            .itemOutputs()
            .fluidInputs(FluidUtils.getFluidStack("fluid.ethylbenzene", 1_000))
            .fluidOutputs(Materials.Styrene.getFluid(1_000), Materials.Hydrogen.getGas(2_000))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);
    }

    private static void assemblerRecipes() {

        RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Vanadium, 32L),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.VanadiumSteel, 8L))
            .itemOutputs(new ItemStack(ModItems.itemHalfCompleteCasings, 4))
            .fluidInputs(Materials.Oxygen.getGas(8_000))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(assemblerRecipes);

        RA.stdBuilder()
            .itemInputs(
                new ItemStack(ModItems.itemHalfCompleteCasings, 2),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.VanadiumGallium, 8L))
            .itemOutputs(new ItemStack(ModItems.itemHalfCompleteCasings, 8, 1))
            .fluidInputs(Materials.Tantalum.getMolten(4 * INGOTS))
            .duration(32 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);

        RA.stdBuilder()
            .itemInputs(
                new ItemStack(ModItems.itemHalfCompleteCasings, 1, 1),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Lead, 4L),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.HV, 4),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorHV, 2L))
            .itemOutputs(GregtechItemList.Casing_Vanadium_Redox.get(1))
            .fluidInputs(Materials.Oxygen.getGas(16_000))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(assemblerRecipes);

        RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Casing_Vanadium_Redox.get(1),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Titanium, 4L),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.EV, 4),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorEV, 2L))
            .itemOutputs(GregtechItemList.Casing_Vanadium_Redox_IV.get(1))
            .fluidInputs(Materials.Nitrogen.getGas(16_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Casing_Vanadium_Redox_IV.get(1),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.TungstenSteel, 4L),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.IV, 4),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorIV, 2L))
            .itemOutputs(GregtechItemList.Casing_Vanadium_Redox_LuV.get(1))
            .fluidInputs(Materials.Helium.getGas(8_000))
            .duration(12 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Casing_Vanadium_Redox_LuV.get(1),
                ItemUtils.getItemStackOfAmountFromOreDict("plateAlloyIridium", 16),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.LuV, 4),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorLuV, 2L))
            .itemOutputs(GregtechItemList.Casing_Vanadium_Redox_ZPM.get(1))
            .fluidInputs(Materials.Argon.getGas(4_000))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);

        RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Casing_Vanadium_Redox_ZPM.get(1),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Naquadah, 4L),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.ZPM, 4),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorZPM, 2L))
            .itemOutputs(GregtechItemList.Casing_Vanadium_Redox_UV.get(1))
            .fluidInputs(Materials.Radon.getGas(4_000))
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(assemblerRecipes);

        RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Casing_Vanadium_Redox_UV.get(1),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Americium, 4L),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UV, 4),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUV, 2L))
            .itemOutputs(GregtechItemList.Casing_Vanadium_Redox_MAX.get(1))
            .fluidInputs(FluidUtils.getFluidStack("krypton", 500))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(assemblerRecipes);

        RA.stdBuilder()
            .itemInputs(
                GTUtility.copyAmount(2, CI.explosiveITNT),
                GTUtility.copyAmount(4, CI.explosiveTNT),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 2L),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Iron, 1L))
            .itemOutputs(GregtechItemList.MiningExplosives.get(3))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(assemblerRecipes);

        RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.gem, Materials.NetherStar, 1L),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 8L),
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.BlackSteel, 8L))
            .itemOutputs(GregtechItemList.WitherGuard.get(64))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        RA.stdBuilder()
            .itemInputs(
                ItemList.FluidRegulator_LV.get(1),
                ItemList.Electric_Motor_LV.get(1),
                CI.getTieredComponent(OrePrefixes.bolt, 1, 8),
                GTOreDictUnificator.get(OrePrefixes.ring, Materials.Brass, 1L),
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.Brass, 1L),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Steel, 2L))
            .itemOutputs(new ItemStack(ModItems.itemGenericToken, 1, 1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        RA.stdBuilder()
            .itemInputs(
                ItemList.FluidRegulator_MV.get(1),
                ItemList.Electric_Motor_MV.get(1),
                CI.getTieredComponent(OrePrefixes.bolt, 2, 8),
                GTOreDictUnificator.get(OrePrefixes.ring, Materials.Invar, 1L),
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.Invar, 1L),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Aluminium, 2L))
            .itemOutputs(new ItemStack(ModItems.itemGenericToken, 1, 2))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);

        RA.stdBuilder()
            .itemInputs(
                ItemList.FluidRegulator_HV.get(1),
                ItemList.Electric_Motor_HV.get(1),
                CI.getTieredComponent(OrePrefixes.bolt, 3, 8),
                GTOreDictUnificator.get(OrePrefixes.ring, Materials.Chrome, 1L),
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.Chrome, 1L),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.StainlessSteel, 2L))
            .itemOutputs(new ItemStack(ModItems.itemGenericToken, 1, 3))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        RA.stdBuilder()
            .itemInputs(
                ItemList.FluidRegulator_EV.get(1),
                ItemList.Electric_Motor_EV.get(1),
                CI.getTieredComponent(OrePrefixes.bolt, 4, 8),
                GTOreDictUnificator.get(OrePrefixes.ring, Materials.Titanium, 1L),
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.Titanium, 1L),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 2L))
            .itemOutputs(new ItemStack(ModItems.itemGenericToken, 1, 4))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        RA.stdBuilder()
            .itemInputs(new ItemStack(ModItems.itemGenericToken, 1, 1), GTUtility.getIntegratedCircuit(20))
            .itemOutputs(GregtechItemList.SimpleHandPump.get(1))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        RA.stdBuilder()
            .itemInputs(new ItemStack(ModItems.itemGenericToken, 1, 2), GTUtility.getIntegratedCircuit(20))
            .itemOutputs(GregtechItemList.AdvancedHandPump.get(1))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);

        RA.stdBuilder()
            .itemInputs(new ItemStack(ModItems.itemGenericToken, 1, 3), GTUtility.getIntegratedCircuit(20))
            .itemOutputs(GregtechItemList.SuperHandPump.get(1))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        RA.stdBuilder()
            .itemInputs(new ItemStack(ModItems.itemGenericToken, 1, 4), GTUtility.getIntegratedCircuit(20))
            .itemOutputs(GregtechItemList.UltimateHandPump.get(1))
            .duration(1 * MINUTES + 36 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        // Low tier Charge Packs

        final ItemStack[] aPackBatteries = new ItemStack[] { ItemList.Battery_RE_LV_Lithium.get(4),
            ItemList.Battery_RE_MV_Lithium.get(4), ItemList.Battery_RE_HV_Lithium.get(4),
            GregtechItemList.Battery_RE_EV_Lithium.get(4), ItemList.Energy_LapotronicOrb.get(4), };
        final ItemStack[] aPackPlates = new ItemStack[] { CI.getPlate(1, 8), CI.getPlate(2, 8), CI.getPlate(3, 8),
            CI.getPlate(4, 8), CI.getPlate(5, 8), };
        final ItemStack[] aPackWire = new ItemStack[] { CI.getTieredComponent(OrePrefixes.wireGt02, 1, 6),
            CI.getTieredComponent(OrePrefixes.wireGt04, 2, 6), CI.getTieredComponent(OrePrefixes.wireGt08, 3, 6),
            CI.getTieredComponent(OrePrefixes.wireGt12, 4, 6), CI.getTieredComponent(OrePrefixes.wireGt16, 5, 6), };
        final ItemStack[] aPackCircuit = new ItemStack[] { CI.getTieredComponent(OrePrefixes.circuit, 1, 4),
            CI.getTieredComponent(OrePrefixes.circuit, 2, 4), CI.getTieredComponent(OrePrefixes.circuit, 3, 4),
            CI.getTieredComponent(OrePrefixes.circuit, 4, 4), CI.getTieredComponent(OrePrefixes.circuit, 5, 4), };
        final ItemStack[] aPackRing = new ItemStack[] { CI.getTieredComponent(OrePrefixes.ring, 1, 12),
            CI.getTieredComponent(OrePrefixes.ring, 2, 12), CI.getTieredComponent(OrePrefixes.ring, 3, 12),
            CI.getTieredComponent(OrePrefixes.ring, 4, 12), CI.getTieredComponent(OrePrefixes.ring, 5, 12), };
        final ItemStack[] aPackOutput = new ItemStack[] { new ItemStack(ModItems.itemChargePack_Low_1),
            new ItemStack(ModItems.itemChargePack_Low_2), new ItemStack(ModItems.itemChargePack_Low_3),
            new ItemStack(ModItems.itemChargePack_Low_4), new ItemStack(ModItems.itemChargePack_Low_5) };

        for (int i = 1; i < 6; i++) {

            int aAS = i - 1;

            RA.stdBuilder()
                .itemInputs(
                    aPackPlates[aAS],
                    aPackRing[aAS],
                    aPackWire[aAS],
                    aPackCircuit[aAS],
                    aPackBatteries[aAS],
                    CI.getSensor(i, 4))
                .itemOutputs(aPackOutput[aAS])
                .fluidInputs(CI.getTieredFluid(i, 4 * INGOTS))
                .duration(30 * i * SECONDS)
                .eut(GTValues.VP[i])
                .addTo(assemblerRecipes);
        }

        if (Baubles.isModLoaded()) {

            // Turbine Housing Research Page
            RA.stdBuilder()
                .itemInputs(
                    GTUtility.getIntegratedCircuit(17),
                    GTOreDictUnificator.get(OrePrefixes.plate, Materials.Trinium, 64L),
                    CI.getSensor(6, 6),
                    CI.getBolt(7, 64),
                    GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Platinum, 64L),
                    ItemUtils.getItemStackOfAmountFromOreDict("circuitUltimate", 12))
                .itemOutputs(
                    ItemDummyResearch.getResearchStack(ASSEMBLY_LINE_RESEARCH.RESEARCH_8_TURBINE_AUTOMATION, 1))
                .fluidInputs(CI.getAlternativeTieredFluid(7, 32 * INGOTS))
                .duration(5 * MINUTES)
                .eut(TierEU.RECIPE_LuV)
                .addTo(assemblerRecipes);

            // Cloaking Device Research Page
            RA.stdBuilder()
                .itemInputs(
                    GTUtility.getIntegratedCircuit(17),
                    new ItemStack(ModItems.itemCircuitLFTR, 4),
                    CI.getFieldGenerator(6, 16),
                    GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Palladium, 32L),
                    ItemUtils.getItemStackOfAmountFromOreDict("circuitMaster", 12))
                .itemOutputs(ItemDummyResearch.getResearchStack(ASSEMBLY_LINE_RESEARCH.RESEARCH_9_CLOAKING, 1))
                .fluidInputs(CI.getAlternativeTieredFluid(7, 32 * INGOTS))
                .duration(10 * MINUTES)
                .eut(TierEU.RECIPE_ZPM)
                .addTo(assemblerRecipes);
        }
    }

    private static void distilleryRecipes() {
        Logger.INFO("Registering Distillery/Distillation Tower Recipes.");
        RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(4))
            .fluidInputs(Materials.Air.getGas(1_000))
            .fluidOutputs(Materials.Helium.getGas(1L))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(distilleryRecipes);

        RA.stdBuilder()
            .fluidInputs(Materials.Air.getGas(20_000))
            .fluidOutputs(Materials.Helium.getGas(25L))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(distillationTowerRecipes);

        // Apatite Distillation
        /*
         * so if you dissolve aparite in sulphuric acid you'll get a mixture of SO2, H2O, HF and HCl
         */

        RA.stdBuilder()
            .fluidInputs(FluidUtils.getFluidStack("sulfuricapatite", 5_200))
            .fluidOutputs(
                FluidUtils.getFluidStack("sulfurousacid", 3_800),
                FluidUtils.getFluidStack("hydrogenchloride", 1_000),
                FluidUtils.getFluidStack("hydrofluoricacid", 400))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(distillationTowerRecipes);

        RA.stdBuilder()
            .fluidInputs(FluidUtils.getFluidStack("sulfurousacid", 1_000))
            .fluidOutputs(Materials.SulfurDioxide.getGas(500), Materials.Water.getFluid(500))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(distillationTowerRecipes);
    }

    private static void thermalBoilerRecipes() {
        Logger.INFO("Registering Thermal Boiler Recipes.");

        // Recipes with special value -1 display additional tooltip in NEI about lava filters.

        // Lava

        RA.stdBuilder()
            .fluidInputs(Materials.Lava.getFluid(1_000), Materials.Water.getFluid(16_000 / GTValues.STEAM_PER_WATER))
            .fluidOutputs(FluidUtils.getPahoehoeLava(1_000), Materials.Steam.getGas(16_000))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Copper, 1),
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Tin, 1),
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Gold, 1),
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Silver, 1),
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Tantalum, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Tungstate, 1),
                new ItemStack(Blocks.obsidian, 1, 0))
            .outputChances(444, 222, 56, 56, 56, 125, 1000)
            .duration(1 * SECONDS)
            .eut(0)
            .addTo(thermalBoilerRecipes);

        // Pahoehoe Lava

        RA.stdBuilder()
            .fluidInputs(FluidUtils.getPahoehoeLava(1_000), Materials.Water.getFluid(16_000 / GTValues.STEAM_PER_WATER))
            .fluidOutputs(Materials.Steam.getGas(16_000))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Bronze, 1),
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Electrum, 1),
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Tantalum, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Tungstate, 1),
                new ItemStack(Blocks.obsidian, 1, 0))
            .outputChances(167, 56, 56, 125, 3700)
            .duration(1 * SECONDS)
            .eut(0)
            .addTo(thermalBoilerRecipes);

        // Hot Coolant

        RA.stdBuilder()
            .fluidInputs(
                FluidUtils.getFluidStack("ic2hotcoolant", 500),
                Materials.Water.getFluid(100_000 / GTValues.STEAM_PER_WATER))
            .fluidOutputs(GTModHandler.getIC2Coolant(500), FluidUtils.getSuperHeatedSteam(100_000))
            .duration(1 * SECONDS)
            .eut(0)
            .addTo(thermalBoilerRecipes);

        // Solar Salt (Hot)

        RA.stdBuilder()
            .fluidInputs(
                MaterialMisc.SOLAR_SALT_HOT.getFluidStack(100),
                Materials.Water.getFluid(100_000 / GTValues.STEAM_PER_WATER))
            .fluidOutputs(MaterialMisc.SOLAR_SALT_COLD.getFluidStack(100), FluidUtils.getSuperHeatedSteam(100_000))
            .duration(1 * SECONDS)
            .eut(0)
            .addTo(thermalBoilerRecipes);
    }

    private static void addFuels() {
        Logger.INFO("Registering New Fuels.");

        RA.stdBuilder()
            .itemInputs(new ItemStack(Items.lava_bucket))
            .metadata(FUEL_VALUE, 32)
            .metadata(FUEL_TYPE, 2)
            .duration(0)
            .eut(0)
            .addTo(GTRecipeConstants.Fuel);
        RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(2, Ic2Items.lavaCell))
            .metadata(FUEL_VALUE, 32)
            .metadata(FUEL_TYPE, 2)
            .duration(0)
            .eut(0)
            .addTo(GTRecipeConstants.Fuel);
        RA.stdBuilder()
            .itemInputs(Ic2Items.pahoehoelavaCell.copy())
            .metadata(FUEL_VALUE, 24)
            .metadata(FUEL_TYPE, 2)
            .duration(0)
            .eut(0)
            .addTo(GTRecipeConstants.Fuel);
    }

    private static void extractorRecipes() {
        Logger.INFO("Registering Extractor Recipes.");
        RA.stdBuilder()
            .itemInputs(GregtechItemList.Battery_RE_EV_Sodium.get(1L))
            .itemOutputs(ItemList.Battery_Hull_HV.get(4L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        RA.stdBuilder()
            .itemInputs(GregtechItemList.Battery_RE_EV_Cadmium.get(1L))
            .itemOutputs(ItemList.Battery_Hull_HV.get(4L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        RA.stdBuilder()
            .itemInputs(GregtechItemList.Battery_RE_EV_Lithium.get(1L))
            .itemOutputs(ItemList.Battery_Hull_HV.get(4L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
    }

    private static void fluidExtractorRecipes() {
        // Gelid Cryotheum
        RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Cryotheum, 1L))
            .fluidOutputs(new FluidStack(TFFluids.fluidCryotheum, 250))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(fluidExtractionRecipes);

        // Blazing Pyrotheum
        RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Pyrotheum, 1L))
            .fluidOutputs(new FluidStack(TFFluids.fluidPyrotheum, 250))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(fluidExtractionRecipes);
    }

    private static void centrifugeRecipes() {

        RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(2), MaterialMisc.SOLAR_SALT_COLD.getCell(1))
            .itemOutputs(
                MaterialMisc.SODIUM_NITRATE.getDust(3),
                MaterialMisc.POTASSIUM_NITRATE.getDust(2),
                ItemList.Cell_Empty.get(1))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(centrifugeRecipes);
    }

    private static void mixerRecipes() {

        RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(2),
                ItemList.Cell_Empty.get(1),
                MaterialMisc.SODIUM_NITRATE.getDust(3),
                MaterialMisc.POTASSIUM_NITRATE.getDust(2))
            .itemOutputs(MaterialMisc.SOLAR_SALT_COLD.getCell(1))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(2),
                Materials.Titanium.getDust(9),
                Materials.Carbon.getDust(9),
                Materials.Potassium.getDust(9),
                Materials.Lithium.getDust(9),
                Materials.Sulfur.getDust(9))
            .itemOutputs(MaterialsAlloy.LEAGRISIUM.getDust(50))
            .fluidInputs(Materials.Hydrogen.getGas(5_000))
            .duration(1 * MINUTES)
            .eut(TierEU.RECIPE_EV)
            .addTo(mixerRecipes);

        RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(2),
                Materials.Steel.getDust(16),
                Materials.Molybdenum.getDust(1),
                Materials.Titanium.getDust(1),
                Materials.Nickel.getDust(4),
                Materials.Cobalt.getDust(2))
            .itemOutputs(MaterialsAlloy.MARAGING250.getDust(24))
            .duration(1 * MINUTES)
            .eut(TierEU.RECIPE_EV)
            .addTo(mixerRecipes);
    }

    private static void chemicalReactorRecipes() {

        RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(2),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Apatite, 32L))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 2L))
            .fluidInputs(Materials.SulfuricAcid.getFluid(4_000))
            .fluidOutputs(FluidUtils.getFluidStack("sulfuricapatite", 8_000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // KOH + HNO3 = KNO3 + H2O
        RA.stdBuilder()
            .itemInputs(GregtechItemList.PotassiumHydroxide.get(3), GTUtility.getIntegratedCircuit(1))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.PotassiumNitrade, 5L))
            .fluidInputs(Materials.NitricAcid.getFluid(1_000))
            .fluidOutputs(Materials.Water.getFluid(1_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Na2CO3 + 2HNO3 = 2NaNO3 + CO2 + H2O
        RA.stdBuilder()
            .itemInputs(Materials.SodiumCarbonate.getDust(6), GTUtility.getIntegratedCircuit(1))
            .itemOutputs(MaterialMisc.SODIUM_NITRATE.getDust(10))
            .fluidInputs(Materials.NitricAcid.getFluid(2_000))
            .fluidOutputs(Materials.CarbonDioxide.getGas(1_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);
    }

    private static void blastFurnaceRecipes() {

        // Synthetic Graphite
        RA.stdBuilder()
            .itemInputs(MaterialsAlloy.SILICON_CARBIDE.getDust(2), GTUtility.getIntegratedCircuit(22))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Graphite, 1L),
                GTOreDictUnificator.get(OrePrefixes.dustSmall, Materials.Silicon, 1L))
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_IV)
            .metadata(COIL_HEAT, 4500)
            .metadata(ADDITIVE_AMOUNT, 500)
            .addTo(BlastFurnaceWithGas);

    }

    private static void compressorRecipes() {
        RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Clay, 1L))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plate, Materials.Clay, 1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(compressorRecipes);

        RA.stdBuilder()
            .itemInputs(ItemList.FusionComputer_UV.get(9))
            .itemOutputs(GregtechItemList.Compressed_Fusion_Reactor.get(1))
            .duration(1 * HOURS + 50 * MINUTES)
            .eut(TierEU.RECIPE_UV)
            .addTo(compressorRecipes);
    }

    private static void macerationRecipes() {

        if (getModItem(Mods.Chisel.ID, "limestone", 1, 0) != null) {
            RA.stdBuilder()
                .itemInputs(ItemUtils.getItemStackOfAmountFromOreDict("limestone", 1))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Calcite, 4L))
                .duration(20 * SECONDS)
                .eut(2)
                .addTo(maceratorRecipes);
        }
    }

    private static void cyclotronRecipes() {

        // Polonium
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(1))
            .itemOutputs(GregtechItemList.Pellet_RTG_PO210.get(1))
            .outputChances(100)
            .fluidInputs(Materials.Bismuth.getMolten(1))
            .duration(8 * HOURS + 20 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(cyclotronRecipes);

        // Americium
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(1))
            .itemOutputs(GregtechItemList.Pellet_RTG_AM241.get(4))
            .outputChances(2500)
            .fluidInputs(Materials.Americium.getMolten(1))
            .duration(8 * HOURS + 20 * MINUTES)
            .eut(4080)
            .addTo(cyclotronRecipes);

        // Strontium u235
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(1))
            .itemOutputs(GregtechItemList.Pellet_RTG_SR90.get(1))
            .outputChances(570)
            .fluidInputs(Materials.Uranium235.getMolten(10))
            .duration(8 * HOURS + 20 * MINUTES)
            .eut(4080)
            .addTo(cyclotronRecipes);

        // Strontium u233
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(1))
            .itemOutputs(GregtechItemList.Pellet_RTG_SR90.get(1))
            .outputChances(660)
            .fluidInputs(FluidUtils.getFluidStack("molten.uranium233", 10))
            .duration(8 * HOURS + 20 * MINUTES)
            .eut(4080)
            .addTo(cyclotronRecipes);

        // Strontium pu239
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(1))
            .itemOutputs(GregtechItemList.Pellet_RTG_SR90.get(1))
            .outputChances(220)
            .fluidInputs(Materials.Plutonium.getMolten(10))
            .duration(8 * HOURS + 20 * MINUTES)
            .eut(4080)
            .addTo(cyclotronRecipes);

        // Plutonium
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(1))
            .itemOutputs(GregtechItemList.Pellet_RTG_PU238.get(2))
            .outputChances(780)
            .fluidInputs(FluidUtils.getFluidStack("molten.plutonium238", 1))
            .duration(8 * HOURS + 20 * MINUTES)
            .eut(4080)
            .addTo(cyclotronRecipes);

        // Neptunium
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialsElements.getInstance().URANIUM238.getDust(1))
            .itemOutputs(new ItemStack(ModItems.dustNeptunium238))
            .outputChances(500)
            .fluidInputs(Materials.Deuterium.getGas(400))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(cyclotronRecipes);

        /*
         * Particle Science
         */

        // Quark Smash
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(3))
            .itemOutputs(
                Particle.getBaseParticle(Particle.UP),
                Particle.getBaseParticle(Particle.DOWN),
                Particle.getBaseParticle(Particle.CHARM),
                Particle.getBaseParticle(Particle.STRANGE),
                Particle.getBaseParticle(Particle.TOP),
                Particle.getBaseParticle(Particle.BOTTOM))
            .outputChances(100, 100, 100, 100, 100, 100)
            .fluidInputs(FluidUtils.getFluidStack("plasma.hydrogen", 100))
            .duration(2 * MINUTES)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(cyclotronRecipes);
        // Lepton Smash
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(6))
            .itemOutputs(
                Particle.getBaseParticle(Particle.ELECTRON),
                Particle.getBaseParticle(Particle.MUON),
                Particle.getBaseParticle(Particle.TAU),
                Particle.getBaseParticle(Particle.ELECTRON_NEUTRINO),
                Particle.getBaseParticle(Particle.MUON_NEUTRINO),
                Particle.getBaseParticle(Particle.TAU_NEUTRINO))
            .outputChances(600, 40, 20, 15, 10, 5)
            .fluidInputs(FluidUtils.getFluidStack("plasma.helium", 1_500))
            .duration(40 * MINUTES)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(cyclotronRecipes);
        // Boson Smash
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(9))
            .itemOutputs(
                Particle.getBaseParticle(Particle.GLUON),
                Particle.getBaseParticle(Particle.PHOTON),
                Particle.getBaseParticle(Particle.Z_BOSON),
                Particle.getBaseParticle(Particle.W_BOSON),
                Particle.getBaseParticle(Particle.HIGGS_BOSON))
            .outputChances(160, 260, 150, 150, 1)
            .fluidInputs(FluidUtils.getFluidStack("plasma.helium", 1_500))
            .duration(30 * MINUTES)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(cyclotronRecipes);
        // Mixed Smash 1
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(12))
            .itemOutputs(
                Particle.getBaseParticle(Particle.GRAVITON),
                Particle.getBaseParticle(Particle.ETA_MESON),
                Particle.getBaseParticle(Particle.PION),
                Particle.getBaseParticle(Particle.PROTON),
                Particle.getBaseParticle(Particle.NEUTRON),
                Particle.getBaseParticle(Particle.LAMBDA),
                Particle.getBaseParticle(Particle.OMEGA),
                Particle.getBaseParticle(Particle.HIGGS_BOSON))
            .outputChances(50, 50, 50, 40, 30, 20, 20, 10)
            .fluidInputs(Materials.Americium.getPlasma(2_500))
            .duration(16 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(cyclotronRecipes);
        // Mixed Smash 1
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(12))
            .itemOutputs(
                Particle.getBaseParticle(Particle.GRAVITON),
                Particle.getBaseParticle(Particle.ETA_MESON),
                Particle.getBaseParticle(Particle.PION),
                Particle.getBaseParticle(Particle.PROTON),
                Particle.getBaseParticle(Particle.NEUTRON),
                Particle.getBaseParticle(Particle.LAMBDA),
                Particle.getBaseParticle(Particle.OMEGA),
                Particle.getBaseParticle(Particle.HIGGS_BOSON))
            .outputChances(5000, 200, 200, 100, 80, 60, 40, 30)
            .fluidInputs(new FluidStack(MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getPlasma(), 2_500))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(cyclotronRecipes);
        // Graviton Smash
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(15))
            .itemOutputs(Particle.getBaseParticle(Particle.GRAVITON), Particle.getBaseParticle(Particle.UNKNOWN))
            .outputChances(1000, 100)
            .fluidInputs(FluidUtils.getFluidStack("plasma.hydrogen", 100))
            .duration(1 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(cyclotronRecipes);

        FluidStack aPlasma = Materials.Duranium.getMolten(40);
        FluidStack aPlasma_NULL = Materials._NULL.getPlasma(1);

        if (aPlasma == null || aPlasma.isFluidEqual(aPlasma_NULL)) {
            aPlasma = Materials.Americium.getMolten(20);
        }

        // Quantum Anomaly
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(24), Particle.getBaseParticle(Particle.UNKNOWN))
            .itemOutputs(GregtechItemList.Laser_Lens_Special.get(1))
            .outputChances(100)
            .fluidInputs(aPlasma)
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(cyclotronRecipes);

        /*
         * Ions
         */

        int IonCount = 2;
        int tenCountA = 2;
        int tenCountB = 0;
        for (String y : IonParticles.MetaToNameMap.values()) {
            if (y.toLowerCase()
                .contains("hydrogen")) {
                continue;
            }
            FluidStack plasmaFromName = FluidUtils.getFluidStack("plasma." + y.toLowerCase(), 1_000);

            Materials particleMaterial = MaterialUtils.getMaterial(y);
            FluidStack recipePlasma = particleMaterial != null ? particleMaterial.getPlasma(1_000) : plasmaFromName;

            // Ionize Plasma
            if (recipePlasma != null && !recipePlasma.isFluidEqual(aPlasma_NULL)) {
                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.getIntegratedCircuit(1 + (tenCountA - 1)))
                    .itemOutputs(
                        Particle.getIon(y, 1),
                        Particle.getIon(y, 2),
                        Particle.getIon(y, 3),
                        Particle.getIon(y, -1),
                        Particle.getIon(y, -2),
                        Particle.getIon(y, -3),
                        Particle.getIon(y, 1),
                        Particle.getIon(y, 2),
                        Particle.getIon(y, -1))
                    .outputChances(275, 250, 225, 275, 250, 225, 275, 250, 275)
                    .fluidInputs(recipePlasma)
                    .duration(20 * SECONDS * (IonCount++) * tenCountA)
                    .eut(TierEU.RECIPE_ZPM)
                    .addTo(cyclotronRecipes);

            } else {
                Logger.INFO("Plasma for " + y + " does not exist, please report this to Alkalus.");
            }

            if (tenCountB == 12) {
                tenCountB = 0;
                tenCountA++;
            } else {
                tenCountB++;
            }
        }

        // Generate Hydrogen Ion Recipe
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(24))
            .itemOutputs(
                Particle.getIon("Hydrogen", 1),
                Particle.getIon("Hydrogen", 2),
                Particle.getIon("Hydrogen", 3),
                Particle.getIon("Hydrogen", 1),
                Particle.getIon("Hydrogen", 2),
                Particle.getIon("Hydrogen", 3),
                Particle.getIon("Hydrogen", -1),
                Particle.getIon("Hydrogen", -2),
                Particle.getIon("Hydrogen", -3))
            .outputChances(500, 500, 500, 500, 500, 500, 500, 500, 500)
            .fluidInputs(Materials.Hydrogen.getGas(1_000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(cyclotronRecipes);
        // Generate Hydrogen Plasma Recipe
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(21), Particle.getIon("Hydrogen", 0))
            .itemOutputs(
                Particle.getBaseParticle(Particle.PROTON),
                Particle.getBaseParticle(Particle.NEUTRON),
                Particle.getBaseParticle(Particle.ELECTRON),
                Particle.getBaseParticle(Particle.UNKNOWN),
                Particle.getBaseParticle(Particle.UNKNOWN),
                Particle.getBaseParticle(Particle.UNKNOWN))
            .outputChances(1250, 1250, 1250, 750, 750, 750)
            .fluidInputs(Materials.Hydrogen.getGas(1_000))
            .fluidOutputs(FluidUtils.getFluidStack("plasma.hydrogen", 100))
            .duration(2 * MINUTES)
            .eut(TierEU.RECIPE_LuV)
            .addTo(cyclotronRecipes);
        // Generate Protons Easily
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(20), Particle.getIon("Hydrogen", 0))
            .itemOutputs(
                Particle.getBaseParticle(Particle.PROTON),
                Particle.getBaseParticle(Particle.PROTON),
                Particle.getBaseParticle(Particle.PROTON),
                Particle.getBaseParticle(Particle.PROTON),
                Particle.getBaseParticle(Particle.PROTON),
                Particle.getBaseParticle(Particle.PROTON),
                Particle.getBaseParticle(Particle.PROTON),
                Particle.getBaseParticle(Particle.PROTON),
                Particle.getBaseParticle(Particle.PROTON))
            .outputChances(750, 750, 750, 750, 750, 750, 750, 750, 750)
            .fluidInputs(Materials.Hydrogen.getGas(100))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(cyclotronRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(22), Particle.getBaseParticle(Particle.UNKNOWN))
            .itemOutputs(
                Particle.getBaseParticle(Particle.PROTON),
                Particle.getBaseParticle(Particle.PROTON),
                Particle.getBaseParticle(Particle.PROTON),
                Particle.getBaseParticle(Particle.PROTON),
                Particle.getBaseParticle(Particle.PROTON),
                Particle.getBaseParticle(Particle.PROTON),
                Particle.getBaseParticle(Particle.PROTON),
                Particle.getBaseParticle(Particle.PROTON),
                Particle.getBaseParticle(Particle.PROTON))
            .outputChances(375, 375, 375, 375, 375, 375, 375, 375, 375)
            .fluidInputs(Materials.Hydrogen.getGas(100))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(cyclotronRecipes);
        // Create Strange Dust
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialsElements.getInstance().PLUTONIUM238.getDust(1),
                Particle.getBaseParticle(Particle.UNKNOWN),
                Particle.getBaseParticle(Particle.UNKNOWN),
                Particle.getBaseParticle(Particle.UNKNOWN),
                Particle.getBaseParticle(Particle.UNKNOWN),
                Particle.getBaseParticle(Particle.UNKNOWN),
                Particle.getBaseParticle(Particle.UNKNOWN),
                Particle.getBaseParticle(Particle.UNKNOWN),
                Particle.getBaseParticle(Particle.UNKNOWN))
            .itemOutputs(MaterialsOres.DEEP_EARTH_REACTOR_FUEL_DEPOSIT.getDust(1))
            .outputChances(2500)
            .fluidInputs(new FluidStack(TFFluids.fluidEnder, 1_000))
            .duration(15 * MINUTES)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(cyclotronRecipes);

    }

    private static void craftingTableRecipes() {

        List<ItemList> tankList = Arrays.asList(
            ItemList.Super_Tank_LV,
            ItemList.Super_Tank_MV,
            ItemList.Super_Tank_HV,
            ItemList.Super_Tank_EV,
            ItemList.Super_Tank_IV,
            ItemList.Quantum_Tank_LV,
            ItemList.Quantum_Tank_MV,
            ItemList.Quantum_Tank_HV,
            ItemList.Quantum_Tank_EV,
            ItemList.Quantum_Tank_IV);

        for (int i = 0; i < 10; i++) {
            ItemStack tank = tankList.get(i)
                .get(1);
            ItemStack handPump = GregtechItemList.ExpandableHandPump.get(1);
            ItemStack pumpWithNBT = handPump.copy();
            NBTTagCompound nbt = new NBTTagCompound();
            int capacity = i == 9 ? Integer.MAX_VALUE : 4_000_000 * (int) GTUtility.powInt(2, i);
            nbt.setInteger("mMeta", 4);
            nbt.setBoolean("mInit", true);
            nbt.setString("mFluid", "@@@@@");
            nbt.setInteger("mFluidAmount", 0);
            nbt.setInteger("mCapacity", capacity);
            nbt.setBoolean("capacityInit", true);
            pumpWithNBT.setTagCompound(nbt);
            GTModHandler.addShapelessCraftingRecipe(pumpWithNBT, new Object[] { handPump, tank });
        }
    }
}
