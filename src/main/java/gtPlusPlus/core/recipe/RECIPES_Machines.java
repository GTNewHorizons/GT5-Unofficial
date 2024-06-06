package gtPlusPlus.core.recipe;

import static gregtech.api.enums.Mods.BartWorks;
import static gregtech.api.enums.Mods.EternalSingularity;
import static gregtech.api.enums.Mods.GoodGenerator;
import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.enums.Mods.Railcraft;
import static gregtech.api.enums.Mods.RemoteIO;

import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import com.github.technus.tectech.recipe.TT_recipeAdder;
import com.github.technus.tectech.thing.CustomItemList;
import com.google.common.collect.ImmutableList;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.item.chemistry.AgriculturalChem;
import gtPlusPlus.core.item.crafting.ItemDummyResearch;
import gtPlusPlus.core.item.crafting.ItemDummyResearch.ASSEMBLY_LINE_RESEARCH;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.ALLOY;
import gtPlusPlus.core.material.ELEMENT;
import gtPlusPlus.core.material.MISC_MATERIALS;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.MaterialUtils;
import gtPlusPlus.core.util.minecraft.RecipeUtils;
import gtPlusPlus.core.util.minecraft.gregtech.PollutionUtils;
import gtPlusPlus.everglades.dimension.Dimension_Everglades;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.covers.CoverManager;
import gtPlusPlus.xmod.gregtech.common.helpers.VolumetricFlaskHelper;

public class RECIPES_Machines {

    // Outputs
    public static ItemStack RECIPE_Buffer_ULV = GregtechItemList.Energy_Buffer_1by1_ULV.get(1);
    public static ItemStack RECIPE_Buffer_LV = GregtechItemList.Energy_Buffer_1by1_LV.get(1);
    public static ItemStack RECIPE_Buffer_MV = GregtechItemList.Energy_Buffer_1by1_MV.get(1);
    public static ItemStack RECIPE_Buffer_HV = GregtechItemList.Energy_Buffer_1by1_HV.get(1);
    public static ItemStack RECIPE_Buffer_EV = GregtechItemList.Energy_Buffer_1by1_EV.get(1);
    public static ItemStack RECIPE_Buffer_IV = GregtechItemList.Energy_Buffer_1by1_IV.get(1);
    public static ItemStack RECIPE_Buffer_LuV = GregtechItemList.Energy_Buffer_1by1_LuV.get(1);
    public static ItemStack RECIPE_Buffer_ZPM = GregtechItemList.Energy_Buffer_1by1_ZPM.get(1);
    public static ItemStack RECIPE_Buffer_UV = GregtechItemList.Energy_Buffer_1by1_UV.get(1);
    public static ItemStack RECIPE_Buffer_MAX = GregtechItemList.Energy_Buffer_1by1_MAX.get(1);
    // Industrial Centrifuge
    public static ItemStack RECIPE_IndustrialCentrifugeController;
    public static ItemStack RECIPE_IndustrialCentrifugeCasing;
    // Industrial Coke Oven
    public static ItemStack RECIPE_IndustrialCokeOvenController;
    public static ItemStack RECIPE_IndustrialCokeOvenFrame;
    public static ItemStack RECIPE_IndustrialCokeOvenCasingA;
    public static ItemStack RECIPE_IndustrialCokeOvenCasingB;
    // Industrial Electrolyzer
    public static ItemStack RECIPE_IndustrialElectrolyzerController;
    public static ItemStack RECIPE_IndustrialElectrolyzerFrame;
    // Industrial Material Press
    public static ItemStack RECIPE_IndustrialMaterialPressController;
    public static ItemStack RECIPE_IndustrialMaterialPressFrame;
    // Industrial Maceration Stack
    public static ItemStack RECIPE_IndustrialMacerationStackController;
    public static ItemStack RECIPE_IndustrialMacerationStackFrame;
    // Industrial Wire Factory
    public static ItemStack RECIPE_IndustrialWireFactoryController;
    public static ItemStack RECIPE_IndustrialWireFactoryFrame;
    // Industrial Multi Tank
    public static ItemStack RECIPE_IndustrialMultiTankController;
    public static ItemStack RECIPE_IndustrialMultiTankFrame;
    // Industrial Matter Fabricator
    public static ItemStack RECIPE_IndustrialMatterFabController;
    public static ItemStack RECIPE_IndustrialMatterFabFrame;
    public static ItemStack RECIPE_IndustrialMatterFabCoil;
    // Industrial Blast Smelter
    public static ItemStack RECIPE_IndustrialBlastSmelterController;
    public static ItemStack RECIPE_IndustrialBlastSmelterFrame;
    public static ItemStack RECIPE_IndustrialBlastSmelterCoil;
    // Industrial Sieve
    public static ItemStack RECIPE_IndustrialSieveController;
    public static ItemStack RECIPE_IndustrialSieveFrame;
    public static ItemStack RECIPE_IndustrialSieveGrate;
    // Industrial Tree Farmer
    public static ItemStack RECIPE_TreeFarmController;
    public static ItemStack RECIPE_TreeFarmFrame;
    // Tesseracts
    public static ItemStack RECIPE_TesseractGenerator;
    public static ItemStack RECIPE_TesseractTerminal;
    // Thermal Boiler
    public static ItemStack RECIPE_ThermalBoilerController;
    public static ItemStack RECIPE_ThermalBoilerCasing;

    // Thorium Reactor
    public static ItemStack RECIPE_LFTRController;
    public static ItemStack RECIPE_LFTROuterCasing;
    public static ItemStack RECIPE_LFTRInnerCasing;

    // Nuclear Salt Processing Plant
    public static ItemStack RECIPE_SaltPlantController;

    // Cyclotron
    public static ItemStack RECIPE_CyclotronController;
    public static ItemStack RECIPE_CyclotronOuterCasing;
    public static ItemStack RECIPE_CyclotronInnerCoil;

    // Wire
    public static String cableTier4 = "cableGt04Gold";
    public static String cableTier6 = "cableGt04Tungsten";

    public static String pipeTier1 = "pipeHuge" + "Clay";
    public static String pipeTier2 = "pipeHuge" + "Potin";
    public static String pipeTier3 = "pipeHuge" + "Steel";
    public static String pipeTier4 = "pipeHuge" + "StainlessSteel";
    public static String pipeTier7 = "pipeHuge" + "Tantalloy60";

    // EV/IV MACHINES
    public static ItemStack IV_MACHINE_Electrolyzer;
    public static ItemStack EV_MACHINE_Centrifuge;
    public static ItemStack EV_MACHINE_BendingMachine;
    public static ItemStack IV_MACHINE_Wiremill;
    public static ItemStack EV_MACHINE_Macerator;
    public static ItemStack IV_MACHINE_Macerator;
    public static ItemStack IV_MACHINE_Cutter;
    public static ItemStack IV_MACHINE_Extruder;
    public static ItemStack HV_MACHINE_Sifter;
    public static ItemStack EV_MACHINE_ThermalCentrifuge;
    public static ItemStack EV_MACHINE_OreWasher;
    public static ItemStack IV_MACHINE_AlloySmelter;
    public static ItemStack IV_MACHINE_Mixer;
    public static ItemStack EV_MACHINE_ChemicalBath;

    // Plates
    public static String plateBronze = "plateBronze";
    public static String plateSteel = "plateSteel";

    // Pipes
    public static String pipeHugeStainlessSteel = "pipeHugeStainlessSteel";

    // Lava Boiler
    public static ItemStack boiler_Coal;
    public static ItemStack IC2MFE;
    public static ItemStack IC2MFSU;

    // Misc
    public static ItemStack INPUT_RCCokeOvenBlock;

    public static final void loadRecipes() {
        run();
        Logger.INFO("Loading Recipes for the Various machine blocks.");
    }

    private static void run() {

        initModItems();
        tieredMachineHulls();
        energyCores();
        wirelessChargers();
        largeArcFurnace();
        industrialVacuumFurnace();
        fakeMachineCasingCovers();
        overflowValveCovers();
        superBuses();
        distillus();
        algaeFarm();
        chemPlant();
        zyngen();
        milling();
        sparging();
        chisels();
        rockBreaker();
        thermicFluidHeater();
        advHeatExchanger();
        chiselBuses();
        solidifierHatches();

        gt4FarmManager();
        gt4Inventory();

        multiForgeHammer();
        multiMolecularTransformer();
        multiXlTurbines();
        multiSolarTower();
        multiElementalDuplicator();

        resonanceChambers();
        modulators();
    }

    private static void thermicFluidHeater() {

        RecipeUtils.addShapedGregtechRecipe(
            CI.getPlate(5, 1),
            CI.circuitTier5,
            CI.getPlate(5, 1),
            pipeTier7,
            ItemList.Machine_IV_FluidHeater.get(1),
            pipeTier7,
            CI.getPlate(5, 1),
            CI.circuitTier4,
            CI.getPlate(5, 1),
            GregtechItemList.Controller_IndustrialFluidHeater.get(1));
    }

    private static void advHeatExchanger() {
        CORE.RA.addSixSlotAssemblingRecipe(
            new ItemStack[] { CI.getNumberedAdvancedCircuit(18), ItemList.Machine_Multi_HeatExchanger.get(1),
                CI.getDoublePlate(6, 8), CI.getScrew(6, 16), CI.getCircuit(5, 8) },
            CI.tieredMaterials[5].getMolten(144 * 8),
            GregtechItemList.XL_HeatExchanger.get(1),
            20 * 60,
            MaterialUtils.getVoltageForTier(6));

        CORE.RA.addSixSlotAssemblingRecipe(
            new ItemStack[] { CI.getNumberedAdvancedCircuit(18), ItemList.Casing_StableTitanium.get(1),
                CI.getPlate(5, 4), CI.getScrew(5, 8), },
            CI.tieredMaterials[5].getMolten(144 * 2),
            GregtechItemList.Casing_XL_HeatExchanger.get(1),
            20 * 5,
            MaterialUtils.getVoltageForTier(6));
    }

    private static void gt4FarmManager() {

        ItemList[] aInputHatches = new ItemList[] { ItemList.Hatch_Input_LV, ItemList.Hatch_Input_MV,
            ItemList.Hatch_Input_HV, ItemList.Hatch_Input_EV, ItemList.Hatch_Input_IV, ItemList.Hatch_Input_LuV,
            ItemList.Hatch_Input_ZPM, ItemList.Hatch_Input_UV };
        GregtechItemList[] aOutputMachines = new GregtechItemList[] { GregtechItemList.GT4_Crop_Harvester_LV,
            GregtechItemList.GT4_Crop_Harvester_MV, GregtechItemList.GT4_Crop_Harvester_HV,
            GregtechItemList.GT4_Crop_Harvester_EV, GregtechItemList.GT4_Crop_Harvester_IV,
            GregtechItemList.GT4_Crop_Harvester_LuV, GregtechItemList.GT4_Crop_Harvester_ZPM,
            GregtechItemList.GT4_Crop_Harvester_UV };

        int aTier = 1;
        for (int i = 0; i < 8; i++) {
            RecipeUtils.addShapedRecipe(
                CI.getRobotArm(aTier, 1),
                CI.getSensor(aTier, 1),
                CI.getRobotArm(aTier, 1),
                ItemUtils.getOrePrefixStack(OrePrefixes.toolHeadSense, CI.tieredMaterials[aTier], 1),
                CI.getTieredMachineHull(aTier, 1),
                ItemUtils.getOrePrefixStack(OrePrefixes.toolHeadSense, CI.tieredMaterials[aTier], 1),
                CI.getTieredCircuitOreDictName(aTier),
                aInputHatches[i].get(1),
                CI.getTieredCircuitOreDictName(aTier),
                aOutputMachines[i].get(1));
            aTier++;
        }
    }

    private static void gt4Inventory() {

        GregtechItemList[] aOutputElectricCraftingTable = new GregtechItemList[] {
            GregtechItemList.GT4_Electric_Auto_Workbench_LV, GregtechItemList.GT4_Electric_Auto_Workbench_MV,
            GregtechItemList.GT4_Electric_Auto_Workbench_HV, GregtechItemList.GT4_Electric_Auto_Workbench_EV,
            GregtechItemList.GT4_Electric_Auto_Workbench_IV, GregtechItemList.GT4_Electric_Auto_Workbench_LuV,
            GregtechItemList.GT4_Electric_Auto_Workbench_ZPM, GregtechItemList.GT4_Electric_Auto_Workbench_UV };

        int aTier = 1;
        for (int i = 0; i < 8; i++) {
            RecipeUtils.addShapedRecipe(
                ItemUtils.getOrePrefixStack(OrePrefixes.plate, CI.tieredMaterials[aTier], 1),
                ItemUtils.getSimpleStack(Blocks.crafting_table),
                ItemUtils.getOrePrefixStack(OrePrefixes.plate, CI.tieredMaterials[aTier], 1),
                CI.getTieredCircuitOreDictName(aTier),
                CI.getTieredMachineHull(aTier),
                CI.getTieredCircuitOreDictName(aTier),
                ItemUtils.getOrePrefixStack(OrePrefixes.plate, CI.tieredMaterials[aTier], 1),
                CI.getRobotArm(aTier, 1),
                ItemUtils.getOrePrefixStack(OrePrefixes.plate, CI.tieredMaterials[aTier], 1),
                aOutputElectricCraftingTable[i].get(1));
            aTier++;
        }
    }

    private static void multiForgeHammer() {

        CORE.RA.addSixSlotAssemblingRecipe(
            new ItemStack[] { ItemUtils.getSimpleStack(CI.machineHull_IV, 2), ItemList.Machine_IV_Hammer.get(1),
                CI.getPlate(4, 8), CI.getBolt(5, 32), ELEMENT.getInstance().ZIRCONIUM.getFineWire(32),
                ItemUtils.getItemStackOfAmountFromOreDict("circuitElite", 4) },
            CI.getTieredFluid(4, 144 * 12),
            GregtechItemList.Controller_IndustrialForgeHammer.get(1),
            20 * 30,
            MaterialUtils.getVoltageForTier(5));

        GT_ModHandler.addCraftingRecipe(
            GregtechItemList.Casing_IndustrialForgeHammer.get(1),
            CI.bitsd,
            new Object[] { "IBI", "HCH", "IHI", 'I', CI.getPlate(4, 1), 'B', ALLOY.BABBIT_ALLOY.getPlate(1), 'C',
                ItemList.Casing_HeatProof.get(1), 'H', ALLOY.HASTELLOY_X.getRod(1) });
    }

    private static void multiMolecularTransformer() {

        CORE.RA.addSixSlotAssemblingRecipe(
            new ItemStack[] { CI.getTieredGTPPMachineCasing(6, 1), CI.getPlate(5, 16), CI.getBolt(5, 32),
                ALLOY.HG1223.getFineWire(64), CI.getEmitter(4, 8),
                ItemUtils.getItemStackOfAmountFromOreDict("circuitMaster", 10) },
            CI.getTieredFluid(5, 144 * 16),
            ItemDummyResearch.getResearchStack(ASSEMBLY_LINE_RESEARCH.RESEARCH_11_MOLECULAR_TRANSFORMER, 1),
            20 * 60,
            MaterialUtils.getVoltageForTier(5));

        CORE.RA.addAssemblylineRecipe(
            ItemDummyResearch.getResearchStack(ASSEMBLY_LINE_RESEARCH.RESEARCH_11_MOLECULAR_TRANSFORMER, 1),
            20 * 60 * 30,
            new Object[] { ALLOY.HG1223.getFineWire(64), ALLOY.HG1223.getFineWire(64),
                ItemList.Electric_Motor_IV.get(16), ItemList.Energy_LapotronicOrb.get(16),
                CI.getTieredComponent(OrePrefixes.cableGt12, 6, 16), CI.getTieredComponent(OrePrefixes.wireGt16, 5, 32),
                ALLOY.ZERON_100.getFrameBox(4), ALLOY.ZIRCONIUM_CARBIDE.getPlateDouble(32),
                ALLOY.BABBIT_ALLOY.getPlate(64), ALLOY.LEAGRISIUM.getGear(8),
                new Object[] { CI.getTieredCircuitOreDictName(4), 64 },
                new Object[] { CI.getTieredCircuitOreDictName(5), 32 },
                new Object[] { CI.getTieredCircuitOreDictName(6), 16 },
                GregtechItemList.Laser_Lens_WoodsGlass.get(1), },
            new FluidStack[] { ALLOY.NITINOL_60.getFluidStack(144 * 9 * (2)),
                ALLOY.INCOLOY_MA956.getFluidStack(144 * 9 * (8)), ALLOY.KANTHAL.getFluidStack(144 * 1 * (4)), },
            GregtechItemList.Controller_MolecularTransformer.get(1),
            20 * 60 * 10 * (1),
            MaterialUtils.getVoltageForTier(6));

        CORE.RA.addSixSlotAssemblingRecipe(
            new ItemStack[] { CI.getNumberedAdvancedCircuit(16), CI.getPlate(6, 4), CI.getScrew(6, 8),
                ELEMENT.getInstance().PALLADIUM.getFineWire(16), CI.getSensor(5, 2),
                ItemUtils.getItemStackOfAmountFromOreDict("circuitElite", 4) },
            CI.getTieredFluid(5, 144 * 4),
            GregtechItemList.Casing_Molecular_Transformer_1.get(1),
            20 * 20,
            MaterialUtils.getVoltageForTier(5));

        CORE.RA.addSixSlotAssemblingRecipe(
            new ItemStack[] { CI.getNumberedAdvancedCircuit(16), CI.getPlate(5, 4), CI.getScrew(5, 8),
                ItemList.Casing_Coil_Nichrome.get(2), CI.getFieldGenerator(3, 2),
                ItemUtils.getItemStackOfAmountFromOreDict("circuitData", 8) },
            CI.getTieredFluid(5, 144 * 4),
            GregtechItemList.Casing_Molecular_Transformer_2.get(1),
            20 * 20,
            MaterialUtils.getVoltageForTier(5));

        CORE.RA.addSixSlotAssemblingRecipe(
            new ItemStack[] { CI.getNumberedAdvancedCircuit(16), ItemUtils.getSimpleStack(Blocks.glowstone, 16),
                CI.getGear(5, 8), ELEMENT.getInstance().TITANIUM.getWire04(4), CI.getFieldGenerator(4, 2),
                ItemUtils.getItemStackOfAmountFromOreDict("circuitData", 8) },
            CI.getTieredFluid(5, 144 * 4),
            GregtechItemList.Casing_Molecular_Transformer_3.get(1),
            20 * 60,
            MaterialUtils.getVoltageForTier(5));
    }

    private static void multiXlTurbines() {

        RecipeUtils.addShapedRecipe(
            CI.getDoublePlate(4, 1),
            CI.getElectricMotor(3, 1),
            CI.getDoublePlate(4, 1),
            ItemUtils.getItemStackOfAmountFromOreDict("cellLubricant", 1),
            ItemList.Casing_Gearbox_Titanium.get(1),
            ItemUtils.getItemStackOfAmountFromOreDict("cellLubricant", 1),
            CI.getDoublePlate(4, 1),
            CI.getElectricMotor(3, 1),
            CI.getDoublePlate(4, 1),
            GregtechItemList.Casing_Turbine_Shaft.get(1));

        CORE.RA.addSixSlotAssemblingRecipe(
            new ItemStack[] { CI.getNumberedAdvancedCircuit(18), ItemList.Casing_Turbine.get(1), CI.getPlate(4, 4),
                CI.getScrew(4, 8), CI.getCircuit(4, 4), CI.getGear(3, 8) },
            CI.tieredMaterials[3].getMolten(144 * 8),
            GregtechItemList.Hatch_Turbine_Rotor.get(1),
            20 * 60,
            MaterialUtils.getVoltageForTier(4));

        // Steam
        CORE.RA.addSixSlotAssemblingRecipe(
            new ItemStack[] { CI.getNumberedAdvancedCircuit(18), ItemList.Casing_Turbine.get(1), CI.getPlate(3, 4),
                CI.getScrew(3, 8), },
            CI.tieredMaterials[2].getMolten(144 * 2),
            GregtechItemList.Casing_Turbine_LP.get(1),
            20 * 5,
            MaterialUtils.getVoltageForTier(3));
        CORE.RA.addSixSlotAssemblingRecipe(
            new ItemStack[] { CI.getNumberedAdvancedCircuit(18), ItemList.LargeSteamTurbine.get(1), CI.getPlate(4, 8),
                CI.getScrew(4, 16), CI.getGear(4, 4), CI.getCircuit(4, 8) },
            CI.tieredMaterials[4].getMolten(144 * 8),
            GregtechItemList.Large_Steam_Turbine.get(1),
            20 * 60,
            MaterialUtils.getVoltageForTier(4));

        // Gas
        CORE.RA.addSixSlotAssemblingRecipe(
            new ItemStack[] { CI.getNumberedAdvancedCircuit(18), ItemList.Casing_Turbine1.get(1), CI.getPlate(4, 4),
                CI.getScrew(4, 8), },
            CI.tieredMaterials[3].getMolten(144 * 2),
            GregtechItemList.Casing_Turbine_Gas.get(1),
            20 * 5,
            MaterialUtils.getVoltageForTier(4));
        CORE.RA.addSixSlotAssemblingRecipe(
            new ItemStack[] { CI.getNumberedAdvancedCircuit(18), ItemList.LargeGasTurbine.get(1), CI.getPlate(5, 8),
                CI.getScrew(5, 16), CI.getGear(5, 4), CI.getCircuit(5, 8) },
            CI.tieredMaterials[5].getMolten(144 * 8),
            GregtechItemList.Large_Gas_Turbine.get(1),
            20 * 60,
            MaterialUtils.getVoltageForTier(5));

        // HP Steam
        CORE.RA.addSixSlotAssemblingRecipe(
            new ItemStack[] { CI.getNumberedAdvancedCircuit(18), ItemList.Casing_Turbine2.get(1), CI.getPlate(5, 4),
                CI.getScrew(5, 8), },
            CI.tieredMaterials[4].getMolten(144 * 2),
            GregtechItemList.Casing_Turbine_HP.get(1),
            20 * 5,
            MaterialUtils.getVoltageForTier(5));
        CORE.RA.addSixSlotAssemblingRecipe(
            new ItemStack[] { CI.getNumberedAdvancedCircuit(18), ItemList.LargeHPSteamTurbine.get(1), CI.getPlate(6, 8),
                CI.getScrew(6, 16), CI.getGear(6, 4), CI.getCircuit(6, 8) },
            CI.tieredMaterials[6].getMolten(144 * 8),
            GregtechItemList.Large_HPSteam_Turbine.get(1),
            20 * 60,
            MaterialUtils.getVoltageForTier(6));

        // Plasma
        CORE.RA.addSixSlotAssemblingRecipe(
            new ItemStack[] { CI.getNumberedAdvancedCircuit(18), ItemList.Casing_Turbine3.get(1), CI.getPlate(6, 4),
                CI.getScrew(6, 8), },
            CI.tieredMaterials[5].getMolten(144 * 2),
            GregtechItemList.Casing_Turbine_Plasma.get(1),
            20 * 5,
            MaterialUtils.getVoltageForTier(6));
        CORE.RA.addSixSlotAssemblingRecipe(
            new ItemStack[] { CI.getNumberedAdvancedCircuit(18), ItemList.LargePlasmaTurbine.get(1), CI.getPlate(7, 8),
                CI.getScrew(7, 16), CI.getGear(7, 4), CI.getCircuit(7, 8) },
            CI.tieredMaterials[7].getMolten(144 * 8),
            GregtechItemList.Large_Plasma_Turbine.get(1),
            20 * 60,
            MaterialUtils.getVoltageForTier(7));
        GT_Values.RA.addAssemblerRecipe(
            new ItemStack[] { CI.getNumberedAdvancedCircuit(18),
                GT_ModHandler.getModItem(GoodGenerator.ID, "supercriticalFluidTurbineCasing", 1),
                GT_ModHandler.getModItem(BartWorks.ID, "gt.bwMetaGeneratedplate", 4, 10101),
                GT_ModHandler.getModItem(BartWorks.ID, "gt.bwMetaGeneratedscrew", 8, 10101) },
            FluidRegistry.getFluidStack("molten.adamantium alloy", 144 * 2),
            GregtechItemList.Casing_Turbine_SC.get(1),
            20 * 5,
            MaterialUtils.getVoltageForTier(6));
        GT_Values.RA.addAssemblerRecipe(
            new ItemStack[] { CI.getNumberedAdvancedCircuit(18),
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 1, 32016),
                GT_ModHandler.getModItem(BartWorks.ID, "gt.bwMetaGeneratedplate", 8, 10104),
                GT_ModHandler.getModItem(BartWorks.ID, "gt.bwMetaGeneratedscrew", 16, 10104),
                GT_ModHandler.getModItem(BartWorks.ID, "gt.bwMetaGeneratedgearGt", 4, 10104), CI.getCircuit(7, 8) },
            FluidRegistry.getFluidStack("molten.hikarium", 144 * 8),
            GregtechItemList.Large_SCSteam_Turbine.get(1),
            20 * 60,
            MaterialUtils.getVoltageForTier(7));
    }

    private static void multiSolarTower() {

        CORE.RA.addSixSlotAssemblingRecipe(
            new ItemStack[] { CI.getNumberedAdvancedCircuit(17), CI.getTieredGTPPMachineCasing(3, 4),
                ALLOY.MARAGING250.getPlate(8), ALLOY.MARAGING250.getBolt(8), ALLOY.MARAGING250.getScrew(8),
                CI.getCircuit(5, 8) },
            CI.getTieredFluid(3, 144 * 16),
            GregtechItemList.Industrial_Solar_Tower.get(1),
            20 * 30,
            MaterialUtils.getVoltageForTier(4));

        CORE.RA.addSixSlotAssemblingRecipe(
            new ItemStack[] { CI.getNumberedAdvancedCircuit(17), ALLOY.MARAGING350.getFrameBox(1),
                ALLOY.STAINLESS_STEEL.getPlate(4), ALLOY.MARAGING350.getScrew(8) },
            CI.getTieredFluid(3, 144 * 4),
            GregtechItemList.Casing_SolarTower_Structural.get(1),
            20 * 30,
            MaterialUtils.getVoltageForTier(3));

        CORE.RA.addSixSlotAssemblingRecipe(
            new ItemStack[] { CI.getNumberedAdvancedCircuit(17), ALLOY.MARAGING250.getFrameBox(1),
                ALLOY.STAINLESS_STEEL.getPlate(4), ALLOY.MARAGING250.getBolt(16),
                ELEMENT.getInstance().ALUMINIUM.getScrew(8) },
            CI.getTieredFluid(3, 144 * 4),
            GregtechItemList.Casing_SolarTower_SaltContainment.get(1),
            20 * 30,
            MaterialUtils.getVoltageForTier(3));

        CORE.RA.addSixSlotAssemblingRecipe(
            new ItemStack[] { CI.getNumberedAdvancedCircuit(17), ALLOY.MARAGING250.getFrameBox(1),
                ALLOY.STEEL_BLACK.getPlate(4), ALLOY.MARAGING250.getScrew(8) },
            CI.getAlternativeTieredFluid(3, 144 * 4),
            GregtechItemList.Casing_SolarTower_HeatContainment.get(1),
            20 * 30,
            MaterialUtils.getVoltageForTier(3));

        CORE.RA.addSixSlotAssemblingRecipe(
            new ItemStack[] { CI.getNumberedAdvancedCircuit(17), CI.getTieredGTPPMachineCasing(2, 1), CI.getPlate(3, 2),
                CI.getGear(3, 4), CI.getElectricMotor(3, 2), CI.getCircuit(3, 4) },
            CI.getTertiaryTieredFluid(3, 144 * 4),
            GregtechItemList.Solar_Tower_Reflector.get(1),
            20 * 60,
            MaterialUtils.getVoltageForTier(3));
    }

    private static void multiElementalDuplicator() {

        CORE.RA.addAssemblylineRecipe(
            ItemList.Machine_IV_Replicator.get(1),
            20 * 60 * 60 * 12,
            new Object[] { CI.getTieredMachineHull(7, 4), CI.getFieldGenerator(5, 16), CI.getElectricMotor(7, 16),
                CI.getElectricPiston(7, 4), CI.getEnergyCore(6, 2), CI.getPlate(7, 16), CI.getScrew(7, 32),
                CI.getBolt(6, 32), CI.getTieredComponent(OrePrefixes.rod, 6, 10),
                new Object[] { CI.getTieredCircuitOreDictName(7), 20 }, ItemList.Tool_DataOrb.get(32),
                GregtechItemList.Laser_Lens_Special.get(1) },
            new FluidStack[] { CI.getTieredFluid(7, 144 * 32), CI.getAlternativeTieredFluid(6, 144 * 16),
                CI.getTertiaryTieredFluid(6, 144 * 16), ALLOY.BABBIT_ALLOY.getFluidStack(128 * 144), },
            GregtechItemList.Controller_ElementalDuplicator.get(1),
            20 * 60 * 60,
            (int) MaterialUtils.getVoltageForTier(7));

        CORE.RA.addAssemblylineRecipe(
            GregtechItemList.Modulator_III.get(1),
            20 * 60 * 60 * 4,
            new Object[] { CI.getTieredGTPPMachineCasing(7, 2), CI.getFieldGenerator(4, 4), CI.getEnergyCore(4, 2),
                CI.getPlate(7, 8), CI.getScrew(6, 16), CI.getBolt(6, 16), CI.getTieredComponent(OrePrefixes.rod, 5, 16),
                new Object[] { CI.getTieredCircuitOreDictName(6), 32 }, ItemList.Tool_DataOrb.get(32), },
            new FluidStack[] { CI.getTieredFluid(6, 144 * 16), CI.getAlternativeTieredFluid(5, 144 * 8),
                CI.getTertiaryTieredFluid(5, 144 * 8), ALLOY.BABBIT_ALLOY.getFluidStack(64 * 144), },
            GregtechItemList.Hatch_Input_Elemental_Duplicator.get(1),
            20 * 60 * 60 * (2),
            (int) MaterialUtils.getVoltageForTier(6));

        CORE.RA.addAssemblylineRecipe(
            GregtechItemList.ResonanceChamber_III.get(1),
            20 * 60 * 60 * 2,
            new Object[] { CI.getTieredMachineHull(6, 5), CI.getFieldGenerator(3, 16), CI.getEnergyCore(2, 2),
                CI.getPlate(7, 4), CI.getScrew(7, 4), CI.getBolt(6, 8), CI.getTieredComponent(OrePrefixes.rod, 5, 4),
                new Object[] { CI.getTieredCircuitOreDictName(5), 4 }, ItemList.Tool_DataStick.get(4), },
            new FluidStack[] { CI.getTieredFluid(5, 144 * 16), CI.getAlternativeTieredFluid(4, 144 * 8),
                CI.getTertiaryTieredFluid(4, 144 * 8), ALLOY.BABBIT_ALLOY.getFluidStack(16 * 144), },
            GregtechItemList.Casing_ElementalDuplicator.get(1),
            20 * 60 * (10),
            (int) MaterialUtils.getVoltageForTier(6));
    }

    private static void resonanceChambers() {
        int aFieldTier = 1;
        int aCasingTier = 4;
        for (int i = 0; i < 4; i++) {
            RecipeUtils.addShapedRecipe(
                CI.getDoublePlate(aCasingTier, 1),
                CI.getFieldGenerator(aFieldTier, 1),
                CI.getDoublePlate(aCasingTier, 1),
                CI.getFieldGenerator(aFieldTier, 1),
                CI.getTieredMachineCasing(aCasingTier),
                CI.getFieldGenerator(aFieldTier, 1),
                CI.getDoublePlate(aCasingTier, 1),
                CI.getFieldGenerator(aFieldTier, 1),
                CI.getDoublePlate(aCasingTier, 1),
                ItemUtils.simpleMetaStack(ModBlocks.blockSpecialMultiCasings2, i, 1));
            aCasingTier++;
            aFieldTier++;
        }
    }

    private static void modulators() {
        int aCasingTier = 4;
        for (int i = 4; i < 8; i++) {
            RecipeUtils.addShapedRecipe(
                CI.getTieredCircuitOreDictName(aCasingTier),
                CI.getPlate(aCasingTier, 1),
                CI.getTieredCircuitOreDictName(aCasingTier),
                CI.getPlate(aCasingTier, 1),
                CI.getTieredMachineCasing(aCasingTier),
                CI.getPlate(aCasingTier, 1),
                CI.getTieredCircuitOreDictName(aCasingTier),
                CI.getPlate(aCasingTier, 1),
                CI.getTieredCircuitOreDictName(aCasingTier),
                ItemUtils.simpleMetaStack(ModBlocks.blockSpecialMultiCasings2, i, 1));
            aCasingTier++;
        }
    }

    private static void zyngen() {
        CORE.RA.addSixSlotAssemblingRecipe(
            new ItemStack[] { CI.getNumberedAdvancedCircuit(6), CI.getTieredMachineHull(4),
                ItemList.Machine_IV_AlloySmelter.get(1), CI.getGear(3, 16), CI.getBolt(3, 64), CI.getPlate(4, 16) },
            CI.getAlternativeTieredFluid(4, 144 * 8),
            GregtechItemList.Industrial_AlloySmelter.get(1),
            20 * 30,
            MaterialUtils.getVoltageForTier(4));
    }

    private static void chemPlant() {

        GT_ModHandler.addCraftingRecipe(
            GregtechItemList.Casing_Machine_Custom_1.get(2L, new Object[0]),
            CI.bits,
            new Object[] { "PhP", "PFP", "PwP", 'P', OrePrefixes.plate.get(Materials.Bronze), 'F',
                OrePrefixes.frameGt.get(Materials.Bronze) });

        GT_ModHandler.addCraftingRecipe(
            GregtechItemList.Casing_Machine_Custom_2.get(2L, new Object[0]),
            CI.bits,
            new Object[] { "PPP", "hFw", "PPP", 'P', OrePrefixes.plate.get(Materials.Aluminium), 'F',
                OrePrefixes.frameGt.get(Materials.Aluminium) });

        CORE.RA.addSixSlotAssemblingRecipe(
            new ItemStack[] { CI.getNumberedBioCircuit(19), CI.getTieredGTPPMachineCasing(2, 4),
                CI.getTieredComponentOfMaterial(Materials.Aluminium, OrePrefixes.gearGt, 4),
                CI.getTieredComponentOfMaterial(Materials.AnnealedCopper, OrePrefixes.plate, 16),
                CI.getTieredComponentOfMaterial(Materials.Plastic, OrePrefixes.pipeLarge, 4),
                CI.getTieredComponent(OrePrefixes.frameGt, 2, 4), },
            ALLOY.STEEL_BLACK.getFluidStack(2 * (144 * 4)),
            GregtechItemList.ChemicalPlant_Controller.get(1),
            120 * 20,
            MaterialUtils.getVoltageForTier(2));

        CORE.RA.addSixSlotAssemblingRecipe(
            new ItemStack[] { CI.getNumberedBioCircuit(15), CI.getTieredGTPPMachineCasing(1, 2),
                ItemList.Hatch_Input_Bus_MV.get(1),
                CI.getTieredComponentOfMaterial(Materials.Bronze, OrePrefixes.gearGt, 8),
                CI.getTieredComponentOfMaterial(Materials.Lead, OrePrefixes.plate, 48),
                CI.getTieredComponentOfMaterial(Materials.SolderingAlloy, OrePrefixes.wireFine, 16), },
            ALLOY.BRONZE.getFluidStack(2 * (144 * 4)),
            GregtechItemList.Bus_Catalysts.get(1),
            60 * 20,
            MaterialUtils.getVoltageForTier(2));
    }

    private static void algaeFarm() {

        // Give the bad algae a use.
        CORE.RA.addDistilleryRecipe(
            ItemUtils.getSimpleStack(AgriculturalChem.mAlgaeBiosmass, 32),
            null,
            null,
            ItemUtils.getSimpleStack(AgriculturalChem.mGreenAlgaeBiosmass, 4),
            20 * 15,
            16,
            false);

        CORE.RA.addSixSlotAssemblingRecipe(
            new ItemStack[] { CI.getNumberedBioCircuit(21), CI.getTieredGTPPMachineCasing(0, 4),
                CI.getTieredComponentOfMaterial(Materials.Aluminium, OrePrefixes.rod, 12),
                CI.getTieredComponentOfMaterial(Materials.Wood, OrePrefixes.plate, 32),
                CI.getTieredComponentOfMaterial(Materials.Steel, OrePrefixes.bolt, 16),
                CI.getTieredComponentOfMaterial(Materials.Redstone, OrePrefixes.dust, 32), },
            ALLOY.POTIN.getFluidStack(2 * (144 * 4)),
            GregtechItemList.AlgaeFarm_Controller.get(1),
            60 * 20,
            MaterialUtils.getVoltageForTier(2));
    }

    private static void distillus() {

        CORE.RA.addChemicalPlantRecipe(
            new ItemStack[] { CI.getNumberedAdvancedCircuit(19), ItemList.Distillation_Tower.get(2),
                GregtechItemList.GTPP_Casing_IV.get(16), CI.getTieredComponent(OrePrefixes.circuit, 6, 8) },
            new FluidStack[] { ALLOY.AQUATIC_STEEL.getFluidStack(144 * 32), ALLOY.BABBIT_ALLOY.getFluidStack(144 * 16),
                ALLOY.BRONZE.getFluidStack(144 * 64), ALLOY.KANTHAL.getFluidStack(144 * 16), },
            new ItemStack[] { GregtechItemList.Machine_Adv_DistillationTower.get(1) },
            new FluidStack[] {},
            20 * 600,
            MaterialUtils.getVoltageForTier(6),
            5);
    }

    private static void overflowValveCovers() {
        ItemStack aOutputs[] = new ItemStack[] { GregtechItemList.Cover_Overflow_LV.get(1L),
            GregtechItemList.Cover_Overflow_MV.get(1L), GregtechItemList.Cover_Overflow_HV.get(1L),
            GregtechItemList.Cover_Overflow_EV.get(1L), GregtechItemList.Cover_Overflow_IV.get(1L), };

        for (int tier = 1; tier < aOutputs.length + 1; tier++) {
            CORE.RA.addSixSlotAssemblingRecipe(
                new ItemStack[] { CI.getNumberedBioCircuit(19), CI.getElectricPump(tier, 2),
                    CI.getElectricMotor(tier, 2), CI.getPlate(tier, 4) },
                Materials.SolderingAlloy.getFluid(tier * (144)),
                aOutputs[tier - 1].copy(),
                20 * 20,
                MaterialUtils.getVoltageForTier(tier));
        }
    }

    private static void tieredMachineHulls() {

        GregtechItemList[] aHulls = new GregtechItemList[] { GregtechItemList.GTPP_Casing_ULV,
            GregtechItemList.GTPP_Casing_LV, GregtechItemList.GTPP_Casing_MV, GregtechItemList.GTPP_Casing_HV,
            GregtechItemList.GTPP_Casing_EV, GregtechItemList.GTPP_Casing_IV, GregtechItemList.GTPP_Casing_LuV,
            GregtechItemList.GTPP_Casing_ZPM, GregtechItemList.GTPP_Casing_UV, GregtechItemList.GTPP_Casing_UHV };

        for (int i = 0; i < 10; i++) {
            CORE.RA.addSixSlotAssemblingRecipe(
                new ItemStack[] { CI.getNumberedBioCircuit(20), CI.getTieredMachineCasing(i), CI.getPlate(i, 8),
                    CI.getGear(i, 2), CI.getTieredComponent(OrePrefixes.cableGt02, i, 4),
                    CI.getTieredComponent(OrePrefixes.circuit, i, 2) },
                CI.getAlternativeTieredFluid(i, 144 * (i + 1) * 2),
                aHulls[i].get(1),
                20 * 20,
                MaterialUtils.getVoltageForTier(i));
        }
    }

    private static void initModItems() {
        IC2MFE = ItemUtils.getItemStackWithMeta(true, "IC2:blockElectric", "IC2_MFE", 1, 1);
        IC2MFSU = ItemUtils.getItemStackWithMeta(true, "IC2:blockElectric", "IC2_MFSU", 2, 1);

        // Lava Boiler
        boiler_Coal = ItemList.Machine_Bronze_Boiler.get(1);

        // IV/EV/HV MACHINES
        IV_MACHINE_Electrolyzer = ItemList.Machine_IV_Electrolyzer.get(1);
        EV_MACHINE_Centrifuge = ItemList.Machine_EV_Centrifuge.get(1);
        EV_MACHINE_BendingMachine = ItemList.Machine_EV_Bender.get(1);
        IV_MACHINE_Wiremill = ItemList.Machine_IV_Wiremill.get(1);
        EV_MACHINE_Macerator = ItemList.Machine_EV_Macerator.get(1);
        IV_MACHINE_Macerator = ItemList.Machine_IV_Macerator.get(1);
        IV_MACHINE_Cutter = ItemList.Machine_IV_Cutter.get(1);
        IV_MACHINE_Extruder = ItemList.Machine_IV_Extruder.get(1);
        HV_MACHINE_Sifter = ItemList.Machine_HV_Sifter.get(1);
        EV_MACHINE_ThermalCentrifuge = ItemList.Machine_EV_ThermalCentrifuge.get(1);
        EV_MACHINE_OreWasher = ItemList.Machine_EV_OreWasher.get(1);
        IV_MACHINE_AlloySmelter = ItemList.Machine_IV_AlloySmelter.get(1);
        IV_MACHINE_Mixer = ItemList.Machine_IV_Mixer.get(1);
        EV_MACHINE_ChemicalBath = ItemList.Machine_EV_ChemicalBath.get(1);
        if (CORE.ConfigSwitches.enableMultiblock_IndustrialCokeOven) {
            if (Railcraft.isModLoaded()) {
                // Misc
                INPUT_RCCokeOvenBlock = ItemUtils
                    .getItemStackWithMeta(Railcraft.isModLoaded(), "Railcraft:machine.alpha", "Coke_Oven_RC", 7, 1);
            }
        }
        runModRecipes();
    }

    private static void runModRecipes() {
        // Computer Cube
        CORE.RA.addSixSlotAssemblingRecipe(
            new ItemStack[] { ItemUtils.getSimpleStack(CI.getDataOrb(), 4 * (1)), ItemList.Cover_Screen.get(4),
                CI.machineHull_IV, ItemUtils.getItemStackOfAmountFromOreDict(CI.getTieredCircuitOreDictName(7), 2) },
            ELEMENT.getInstance().TANTALUM.getFluidStack(144 * 16),
            GregtechItemList.Gregtech_Computer_Cube.get(1),
            60 * 20 * 3,
            8000);

        // Circuit programmer
        CORE.RA.addSixSlotAssemblingRecipe(
            new ItemStack[] { ItemUtils.getSimpleStack(CI.robotArm_LV, 4 * (1)),
                ItemList.Cover_Controller.get(1, CI.electricMotor_MV), CI.machineHull_MV,
                ItemUtils.getItemStackOfAmountFromOreDict(CI.getTieredCircuitOreDictName(1), 2),
                ItemUtils.getItemStackOfAmountFromOreDict(CI.getTieredCircuitOreDictName(2), 2) },
            ELEMENT.getInstance().IRON.getFluidStack(144 * 4),
            ItemUtils.getSimpleStack(ModBlocks.blockCircuitProgrammer),
            60 * 10 * 1,
            30);

        // Lead Lined Chest
        for (ItemStack plateRubber : OreDictionary.getOres("plateAnyRubber")) CORE.RA.addSixSlotAssemblingRecipe(
            new ItemStack[] { CI.machineHull_LV, GT_Utility.copyAmount(32, plateRubber),
                ItemUtils.getItemStackOfAmountFromOreDict("plateDenseLead", 9),
                ItemUtils.getSimpleStack(Blocks.chest) },
            ELEMENT.getInstance().LEAD.getFluidStack(144 * 16),
            ItemUtils.getSimpleStack(ModBlocks.blockDecayablesChest),
            60 * 10 * 3,
            60);

        // RTG
        CORE.RA.addSixSlotAssemblingRecipe(
            new ItemStack[] { ItemUtils.getItemStackWithMeta(true, "IC2:blockGenerator:6", "IC2-RTG", 6, 1),
                ALLOY.NITINOL_60.getPlate(8), ALLOY.MARAGING350.getGear(4),
                ItemUtils.getSimpleStack(CI.fieldGenerator_EV, 8),
                ItemUtils.getItemStackOfAmountFromOreDict("wireFinePlatinum", 32),
                ItemUtils.getItemStackOfAmountFromOreDict(CI.getTieredCircuitOreDictName(6), 4) },
            ALLOY.NIOBIUM_CARBIDE.getFluidStack(144 * 16),
            GregtechItemList.RTG.get(1),
            60 * 20 * 10,
            8000);

        // Super Jukebox
        CORE.RA.addSixSlotAssemblingRecipe(
            new ItemStack[] { CI.machineHull_LV, ItemUtils.getItemStackOfAmountFromOreDict("circuitBasic", 4),
                ItemUtils.getItemStackOfAmountFromOreDict("plateTumbaga", 8),
                ItemUtils.getSimpleStack(Blocks.jukebox) },
            ELEMENT.getInstance().COPPER.getFluidStack(144 * 2),
            ItemUtils.getSimpleStack(ModBlocks.blockCustomJukebox),
            20 * 30,
            30);

        // Poo Collector
        CORE.RA.addSixSlotAssemblingRecipe(
            new ItemStack[] { CI.machineHull_MV, ItemList.FluidRegulator_MV.get(2),
                CI.getTieredComponent(OrePrefixes.pipeMedium, 2, 2), ALLOY.EGLIN_STEEL.getPlate(4),
                ALLOY.POTIN.getScrew(6) },
            ALLOY.TUMBAGA.getFluidStack(144 * 4),
            ItemUtils.getSimpleStack(ModBlocks.blockPooCollector),
            20 * 60,
            30);

        // Adv. Poo Collector
        CORE.RA.addSixSlotAssemblingRecipe(
            new ItemStack[] { CI.getTieredMachineHull(-1), ItemUtils.getSimpleStack(ModBlocks.blockPooCollector),
                ItemList.FluidRegulator_IV.get(2), CI.getTieredComponent(OrePrefixes.pipeHuge, 6, 4),
                CI.getTieredComponent(OrePrefixes.screw, 6, 16) },
            CI.getAlternativeTieredFluid(5, 144 * 9),
            ItemUtils.getSimpleStack(ModBlocks.blockPooCollector, 8, 1),
            20 * 60 * 5,
            500);

        ItemStack aBronzeBricks = ItemUtils.simpleMetaStack(GregTech_API.sBlockCasings1, 10, 1);
        // Steam Macerator Multi
        RecipeUtils.addShapedGregtechRecipe(
            aBronzeBricks,
            "gemDiamond",
            aBronzeBricks,
            "craftingPiston",
            ALLOY.TUMBAGA.getFrameBox(1),
            "craftingPiston",
            aBronzeBricks,
            "gemDiamond",
            aBronzeBricks,
            GregtechItemList.Controller_SteamMaceratorMulti.get(1));

        // Steam Washer Multi
        RecipeUtils.addShapedGregtechRecipe(
            aBronzeBricks,
            "plateWroughtIron",
            aBronzeBricks,
            "rotorTin",
            ALLOY.TUMBAGA.getFrameBox(1),
            "rotorTin",
            aBronzeBricks,
            "plateWroughtIron",
            aBronzeBricks,
            GregtechItemList.Controller_SteamWasherMulti.get(1));

        // Steam Centrifuge Multi
        RecipeUtils.addShapedGregtechRecipe(
            aBronzeBricks,
            "plateWroughtIron",
            aBronzeBricks,
            "gearBronze",
            ALLOY.TUMBAGA.getFrameBox(1),
            "gearBronze",
            aBronzeBricks,
            "plateWroughtIron",
            aBronzeBricks,
            GregtechItemList.Controller_SteamCentrifugeMulti.get(1));

        // Steam Compressor Multi
        RecipeUtils.addShapedGregtechRecipe(
            aBronzeBricks,
            "craftingPiston",
            aBronzeBricks,
            ALLOY.TUMBAGA.getGear(1),
            ALLOY.TUMBAGA.getFrameBox(1),
            ALLOY.TUMBAGA.getGear(1),
            aBronzeBricks,
            "craftingPiston",
            aBronzeBricks,
            GregtechItemList.Controller_SteamCompressorMulti.get(1));

        // Steam Hatch
        RecipeUtils.addShapedGregtechRecipe(
            "plateBronze",
            "pipeMediumBronze",
            "plateBronze",
            "plateBronze",
            GregtechItemList.GT_FluidTank_ULV.get(1),
            "plateBronze",
            "plateBronze",
            "pipeMediumBronze",
            "plateBronze",
            GregtechItemList.Hatch_Input_Steam.get(1));

        // Steam Input Bus
        RecipeUtils.addShapedGregtechRecipe(
            "plateBronze",
            ALLOY.TUMBAGA.getPlate(1),
            "plateBronze",
            "plateTin",
            ItemUtils.getSimpleStack(Blocks.hopper),
            "plateTin",
            "plateBronze",
            ALLOY.TUMBAGA.getPlate(1),
            "plateBronze",
            GregtechItemList.Hatch_Input_Bus_Steam.get(1));

        // Steam Output Bus
        RecipeUtils.addShapedGregtechRecipe(
            "plateBronze",
            "plateTin",
            "plateBronze",
            ALLOY.TUMBAGA.getPlate(1),
            ItemUtils.getSimpleStack(Blocks.hopper),
            ALLOY.TUMBAGA.getPlate(1),
            "plateBronze",
            "plateTin",
            "plateBronze",
            GregtechItemList.Hatch_Output_Bus_Steam.get(1));

        // Flask Configurator
        CORE.RA.addSixSlotAssemblingRecipe(
            new ItemStack[] { CI.getNumberedAdvancedCircuit(8), CI.getTieredMachineHull(2),
                ItemUtils.getSimpleStack(ModBlocks.blockCircuitProgrammer), VolumetricFlaskHelper.getVolumetricFlask(8),
                CI.getTieredComponent(OrePrefixes.pipeSmall, 2, 2), CI.getPlate(2, 4) },
            CI.getAlternativeTieredFluid(1, 144 * 8),
            ItemUtils.getSimpleStack(ModBlocks.blockVolumetricFlaskSetter, 1),
            20 * 60,
            120);

        if (CORE.ConfigSwitches.enableMultiblock_IndustrialCentrifuge) {
            // Industrial Centrifuge
            RECIPE_IndustrialCentrifugeController = GregtechItemList.Industrial_Centrifuge.get(1);
            RECIPE_IndustrialCentrifugeCasing = GregtechItemList.Casing_Centrifuge1.get(1);

            // Industrial Centrifuge
            RecipeUtils.addShapedGregtechRecipe(
                CI.circuitTier4,
                pipeHugeStainlessSteel,
                CI.circuitTier4,
                CI.component_Plate[6],
                EV_MACHINE_Centrifuge,
                CI.component_Plate[6],
                CI.component_Plate[8],
                CI.machineCasing_EV,
                CI.component_Plate[8],
                RECIPE_IndustrialCentrifugeController);
            // Centrifuge Casing
            RecipeUtils.addShapedGregtechRecipe(
                CI.component_Plate[6],
                "stickTumbaga",
                CI.component_Plate[6],
                CI.component_Plate[8],
                "stickTumbaga",
                CI.component_Plate[8],
                CI.component_Plate[6],
                "stickTumbaga",
                CI.component_Plate[6],
                RECIPE_IndustrialCentrifugeCasing);
            GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ALLOY.MARAGING250.getPlate(4), ALLOY.INCONEL_792.getPlate(2), ALLOY.TUMBAGA.getRod(3),
                    GT_Utility.getIntegratedCircuit(1), },
                GT_Values.NF,
                RECIPE_IndustrialCentrifugeCasing,
                50,
                16);
        }

        if (CORE.ConfigSwitches.enableMultiblock_IndustrialCokeOven) {
            // Industrial Coke Oven
            RECIPE_IndustrialCokeOvenController = GregtechItemList.Industrial_CokeOven.get(1);
            RECIPE_IndustrialCokeOvenFrame = GregtechItemList.Casing_CokeOven.get(1);
            RECIPE_IndustrialCokeOvenCasingA = GregtechItemList.Casing_CokeOven_Coil1.get(1);
            RECIPE_IndustrialCokeOvenCasingB = GregtechItemList.Casing_CokeOven_Coil2.get(1);

            if (Railcraft.isModLoaded()) {
                // Industrial Coke Oven
                RecipeUtils.addShapedGregtechRecipe(
                    CI.component_Plate[7],
                    CI.circuitTier4,
                    CI.component_Plate[7],
                    CI.machineCasing_EV,
                    INPUT_RCCokeOvenBlock,
                    CI.machineCasing_EV,
                    CI.component_Plate[7],
                    CI.circuitTier4,
                    CI.component_Plate[7],
                    RECIPE_IndustrialCokeOvenController);
            }
            // Coke Oven Frame Casing
            RecipeUtils.addShapedGregtechRecipe(
                CI.component_Plate[7],
                CI.component_Rod[7],
                CI.component_Plate[7],
                CI.component_Rod[7],
                "frameGtTantalloy61",
                CI.component_Rod[7],
                CI.component_Plate[7],
                CI.component_Rod[7],
                CI.component_Plate[7],
                RECIPE_IndustrialCokeOvenFrame);
            GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ALLOY.TANTALLOY_61.getPlate(4), ALLOY.TANTALLOY_61.getRod(4),
                    ALLOY.TANTALLOY_61.getFrameBox(1), GT_Utility.getIntegratedCircuit(1), },
                GT_Values.NF,
                RECIPE_IndustrialCokeOvenFrame,
                50,
                16);
            // Coke Oven Coil 1
            RecipeUtils.addShapedGregtechRecipe(
                plateBronze,
                plateBronze,
                plateBronze,
                "frameGtBronze",
                CI.gearboxCasing_Tier_1,
                "frameGtBronze",
                plateBronze,
                plateBronze,
                plateBronze,
                RECIPE_IndustrialCokeOvenCasingA);
            // Coke Oven Coil 2
            RecipeUtils.addShapedGregtechRecipe(
                plateSteel,
                plateSteel,
                plateSteel,
                "frameGtSteel",
                CI.gearboxCasing_Tier_2,
                "frameGtSteel",
                plateSteel,
                plateSteel,
                plateSteel,
                RECIPE_IndustrialCokeOvenCasingB);
        }

        if (CORE.ConfigSwitches.enableMultiblock_IndustrialElectrolyzer) {
            // Industrial Electrolyzer
            RECIPE_IndustrialElectrolyzerController = GregtechItemList.Industrial_Electrolyzer.get(1);
            RECIPE_IndustrialElectrolyzerFrame = GregtechItemList.Casing_Electrolyzer.get(1);

            // Electrolyzer Frame Casing
            RecipeUtils.addShapedGregtechRecipe(
                "platePotin",
                "stickLongChrome",
                "platePotin",
                "stickLongPotin",
                "frameGtPotin",
                "stickLongPotin",
                "platePotin",
                "stickLongPotin",
                "platePotin",
                RECIPE_IndustrialElectrolyzerFrame);
            GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ALLOY.POTIN.getPlate(4), ALLOY.POTIN.getLongRod(3),
                    GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Chrome, 1), ALLOY.POTIN.getFrameBox(1),
                    GT_Utility.getIntegratedCircuit(1), },
                GT_Values.NF,
                RECIPE_IndustrialElectrolyzerFrame,
                50,
                16);
            // Industrial Electrolyzer
            RecipeUtils.addShapedGregtechRecipe(
                "plateStellite",
                CI.circuitTier5,
                "plateStellite",
                CI.machineCasing_IV,
                IV_MACHINE_Electrolyzer,
                CI.machineCasing_IV,
                "plateStellite",
                "rotorStellite",
                "plateStellite",
                RECIPE_IndustrialElectrolyzerController);
        }

        if (CORE.ConfigSwitches.enableMultiblock_IndustrialPlatePress) {
            // Industrial Material Press
            RECIPE_IndustrialMaterialPressController = GregtechItemList.Industrial_PlatePress.get(1);
            RECIPE_IndustrialMaterialPressFrame = GregtechItemList.Casing_MaterialPress.get(1);

            // Material Press Frame Casing
            RecipeUtils.addShapedGregtechRecipe(
                "plateTitanium",
                "stickLongTumbaga",
                "plateTitanium",
                "stickTantalloy60",
                "frameGtTumbaga",
                "stickTantalloy60",
                "plateTitanium",
                "stickLongTumbaga",
                "plateTitanium",
                RECIPE_IndustrialMaterialPressFrame);
            GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Titanium, 4),
                    ALLOY.TANTALLOY_60.getRod(2), ALLOY.TUMBAGA.getLongRod(2), ALLOY.TUMBAGA.getFrameBox(1),
                    GT_Utility.getIntegratedCircuit(1), },
                GT_Values.NF,
                RECIPE_IndustrialMaterialPressFrame,
                50,
                16);
            // Industrial Material Press
            RecipeUtils.addShapedGregtechRecipe(
                "plateTitanium",
                CI.circuitTier4,
                "plateTitanium",
                CI.machineCasing_EV,
                EV_MACHINE_BendingMachine,
                CI.machineCasing_EV,
                "plateTitanium",
                CI.circuitTier4,
                "plateTitanium",
                RECIPE_IndustrialMaterialPressController);
        }

        if (CORE.ConfigSwitches.enableMultiblock_IndustrialMacerationStack) {
            // Industrial Maceration Stack
            RECIPE_IndustrialMacerationStackController = GregtechItemList.Industrial_MacerationStack.get(1);
            RECIPE_IndustrialMacerationStackFrame = GregtechItemList.Casing_MacerationStack.get(1);

            // Maceration Frame Casing
            RecipeUtils.addShapedGregtechRecipe(
                "platePalladium",
                "platePalladium",
                "platePalladium",
                "stickPlatinum",
                "frameGtInconel625",
                "stickPlatinum",
                "platePalladium",
                "stickLongPalladium",
                "platePalladium",
                RECIPE_IndustrialMacerationStackFrame);
            GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Palladium, 5),
                    GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Platinum, 2),
                    GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Palladium, 1),
                    ALLOY.INCONEL_625.getFrameBox(1), GT_Utility.getIntegratedCircuit(1), },
                GT_Values.NF,
                RECIPE_IndustrialMacerationStackFrame,
                50,
                16);
            // Industrial Maceration stack
            RecipeUtils.addShapedGregtechRecipe(
                "plateTitanium",
                EV_MACHINE_Macerator,
                "plateTitanium",
                EV_MACHINE_Macerator,
                CI.circuitTier4,
                EV_MACHINE_Macerator,
                "plateTitanium",
                EV_MACHINE_Macerator,
                "plateTitanium",
                RECIPE_IndustrialMacerationStackController);
            // T2-Upgrade Card
            RecipeUtils.addShapedGregtechRecipe(
                "plateTungstenCarbide",
                IV_MACHINE_Macerator,
                "plateTungstenCarbide",
                IV_MACHINE_Macerator,
                CI.circuitTier7,
                IV_MACHINE_Macerator,
                "plateTungstenCarbide",
                IV_MACHINE_Macerator,
                "plateTungstenCarbide",
                GregtechItemList.Maceration_Upgrade_Chip.get(1));
        }

        if (CORE.ConfigSwitches.enableMultiblock_IndustrialWireMill) {
            // Industrial Wire Factory
            RECIPE_IndustrialWireFactoryController = GregtechItemList.Industrial_WireFactory.get(1);
            RECIPE_IndustrialWireFactoryFrame = GregtechItemList.Casing_WireFactory.get(1);

            // Wire Factory Frame Casing
            RecipeUtils.addShapedGregtechRecipe(
                "plateBlueSteel",
                "stickBlueSteel",
                "plateBlueSteel",
                "stickBlueSteel",
                "frameGtBlueSteel",
                "stickBlueSteel",
                "plateBlueSteel",
                "stickBlueSteel",
                "plateBlueSteel",
                RECIPE_IndustrialWireFactoryFrame);
            GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.plate, Materials.BlueSteel, 4),
                    GT_OreDictUnificator.get(OrePrefixes.stick, Materials.BlueSteel, 4),
                    GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.BlueSteel, 1),
                    GT_Utility.getIntegratedCircuit(1), },
                GT_Values.NF,
                RECIPE_IndustrialWireFactoryFrame,
                50,
                16);
            // Industrial Wire Factory
            RecipeUtils.addShapedGregtechRecipe(
                "plateZeron100",
                CI.machineCasing_IV,
                "plateZeron100",
                CI.circuitTier5,
                IV_MACHINE_Wiremill,
                CI.circuitTier5,
                "plateZeron100",
                CI.machineCasing_IV,
                "plateZeron100",
                RECIPE_IndustrialWireFactoryController);
        }

        // Tiered Tanks
        if (CORE.ConfigSwitches.enableMachine_FluidTanks) {
            CI.component_Plate[1] = "plateTin";
            pipeTier1 = "pipeLargeClay";
            CI.circuitTier1 = ItemList.Circuit_Primitive.get(1);
            CI.component_Plate[2] = "plateCopper";
            pipeTier2 = "pipeHugeClay";
            CI.component_Plate[3] = "plateBronze";
            pipeTier3 = "pipeMediumBronze";
            CI.component_Plate[4] = "plateIron";
            pipeTier4 = "pipeMediumSteel";
            CI.component_Plate[5] = "plateSteel";
            CI.component_Plate[6] = "plateRedstone";
            CI.component_Plate[7] = "plateAluminium";
            CI.component_Plate[8] = "plateDarkSteel";
            ItemStack waterBucket = ItemUtils.getSimpleStack(Items.water_bucket);

            // Allows clearing stored fluids.
            GregtechItemList[] aTanks = new GregtechItemList[] { GregtechItemList.GT_FluidTank_ULV,
                GregtechItemList.GT_FluidTank_LV, GregtechItemList.GT_FluidTank_MV, GregtechItemList.GT_FluidTank_HV };
            for (GregtechItemList aTank : aTanks) {
                RecipeUtils.addShapelessGregtechRecipe(new Object[] { aTank.get(1) }, aTank.get(1));
            }

            RecipeUtils.addShapedGregtechRecipe(
                CI.component_Plate[1],
                CI.component_Plate[5],
                CI.component_Plate[1],
                CI.component_Plate[4],
                pipeTier1,
                CI.component_Plate[4],
                CI.component_Plate[4],
                waterBucket,
                CI.component_Plate[4],
                GregtechItemList.GT_FluidTank_ULV.get(1));
            RecipeUtils.addShapedGregtechRecipe(
                CI.component_Plate[5],
                CI.component_Plate[4],
                CI.component_Plate[5],
                CI.component_Plate[3],
                pipeTier2,
                CI.component_Plate[3],
                CI.component_Plate[3],
                CI.electricPump_LV,
                CI.component_Plate[3],
                GregtechItemList.GT_FluidTank_LV.get(1));
            RecipeUtils.addShapedGregtechRecipe(
                CI.component_Plate[8],
                CI.component_Plate[3],
                CI.component_Plate[8],
                CI.component_Plate[5],
                pipeTier3,
                CI.component_Plate[5],
                CI.component_Plate[5],
                CI.electricPump_LV,
                CI.component_Plate[5],
                GregtechItemList.GT_FluidTank_MV.get(1));
            RecipeUtils.addShapedGregtechRecipe(
                CI.circuitTier1,
                CI.component_Plate[7],
                CI.circuitTier1,
                CI.component_Plate[8],
                pipeTier4,
                CI.component_Plate[8],
                CI.circuitTier1,
                CI.electricPump_MV,
                CI.circuitTier1,
                GregtechItemList.GT_FluidTank_HV.get(1));
        }

        if (CORE.ConfigSwitches.enableMultiblock_MultiTank) {
            // Industrial Multi Tank
            // RECIPE_IndustrialMultiTankController = GregtechItemList.Industrial_MultiTank.get(1);
            RECIPE_IndustrialMultiTankFrame = GregtechItemList.Casing_MultitankExterior.get(1);

            // Industrial Multi Tank Casing
            RecipeUtils.addShapedGregtechRecipe(
                "stickGrisium",
                "plateGrisium",
                "stickGrisium",
                "plateGrisium",
                "frameGtGrisium",
                "plateGrisium",
                "plateGrisium",
                "plateGrisium",
                "plateGrisium",
                RECIPE_IndustrialMultiTankFrame);
            // Industrial Multi Tank
            RecipeUtils.addShapedGregtechRecipe(
                "pipeHugeTantalloy60",
                "gearGrisium",
                "pipeHugeTantalloy60",
                CI.circuitTier4,
                RECIPE_IndustrialMultiTankFrame,
                CI.circuitTier4,
                "plateDoubleGrisium",
                "rotorGrisium",
                "plateDoubleGrisium",
                RECIPE_IndustrialMultiTankController);
        }
        // TODO

        // Semi-Fluid Generators
        ItemStack[][] aSemiFluidInputs = new ItemStack[5][10];
        aSemiFluidInputs[0] = new ItemStack[] { CI.getNumberedBioCircuit(14), CI.getTieredMachineHull(1, 1),
            CI.getElectricMotor(1, 2), CI.getElectricPiston(1, 2), CI.getTieredComponent(OrePrefixes.cableGt01, 1, 1),
            CI.getTieredComponent(OrePrefixes.circuit, 1, 1), CI.getGear(1, 2) };
        aSemiFluidInputs[1] = new ItemStack[] { CI.getNumberedBioCircuit(14), CI.getTieredMachineHull(2, 1),
            CI.getElectricMotor(2, 2), CI.getElectricPiston(2, 2), CI.getTieredComponent(OrePrefixes.cableGt01, 2, 1),
            CI.getTieredComponent(OrePrefixes.circuit, 2, 1), CI.getGear(2, 2) };
        aSemiFluidInputs[2] = new ItemStack[] { CI.getNumberedBioCircuit(14), CI.getTieredMachineHull(3, 1),
            CI.getElectricMotor(3, 2), CI.getElectricPiston(3, 2), CI.getTieredComponent(OrePrefixes.cableGt01, 3, 1),
            CI.getTieredComponent(OrePrefixes.circuit, 3, 1),
            GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.Chrome, 2) };
        aSemiFluidInputs[3] = new ItemStack[] { CI.getNumberedBioCircuit(14), CI.getTieredMachineHull(4, 1),
            CI.getElectricMotor(4, 2), CI.getElectricPiston(4, 2), CI.getTieredComponent(OrePrefixes.cableGt01, 4, 1),
            CI.getTieredComponent(OrePrefixes.circuit, 4, 1), CI.getGear(4, 2) };
        aSemiFluidInputs[4] = new ItemStack[] { CI.getNumberedBioCircuit(14), CI.getTieredMachineHull(5, 1),
            CI.getElectricMotor(5, 2), CI.getElectricPiston(5, 2), CI.getTieredComponent(OrePrefixes.cableGt01, 5, 1),
            CI.getTieredComponent(OrePrefixes.circuit, 5, 1), CI.getGear(5, 2) };
        FluidStack[] aSemiFluidFluidInputs = new FluidStack[] { ALLOY.POLYETHYLENE.getFluidStack(144),
            ALLOY.POLYETHYLENE.getFluidStack(144), ALLOY.POLYETHYLENE.getFluidStack(144),
            ALLOY.POLYETHYLENE.getFluidStack(144), ALLOY.POLYTETRAFLUOROETHYLENE.getFluidStack(144) };

        ItemStack[] aSemifluids = new ItemStack[] { GregtechItemList.Generator_SemiFluid_LV.get(1),
            GregtechItemList.Generator_SemiFluid_MV.get(1), GregtechItemList.Generator_SemiFluid_HV.get(1),
            GregtechItemList.Generator_SemiFluid_EV.get(1), GregtechItemList.Generator_SemiFluid_IV.get(1) };
        for (int o = 0; o < 5; o++) {
            CORE.RA.addSixSlotAssemblingRecipe(
                aSemiFluidInputs[o],
                aSemiFluidFluidInputs[o],
                aSemifluids[o],
                20 * 30,
                MaterialUtils.getVoltageForTier(o + 1));
        }
        GT_ModHandler.addCraftingRecipe(
            GregtechItemList.Generator_SemiFluid_LV.get(1L),
            new Object[] { "PCP", "EME", "GWG", 'M', ItemList.Hull_LV, 'P', ItemList.Electric_Piston_LV, 'E',
                ItemList.Electric_Motor_LV, 'C', OrePrefixes.circuit.get(Materials.Basic), 'W',
                OrePrefixes.cableGt01.get(Materials.Tin), 'G', ALLOY.TUMBAGA.getGear(2) });
        GT_ModHandler.addCraftingRecipe(
            GregtechItemList.Generator_SemiFluid_MV.get(1L),
            new Object[] { "PCP", "EME", "GWG", 'M', ItemList.Hull_MV, 'P', ItemList.Electric_Piston_MV, 'E',
                ItemList.Electric_Motor_MV, 'C', OrePrefixes.circuit.get(Materials.Good), 'W',
                OrePrefixes.cableGt01.get(Materials.AnyCopper), 'G', ALLOY.EGLIN_STEEL.getGear(2) });
        GT_ModHandler.addCraftingRecipe(
            GregtechItemList.Generator_SemiFluid_HV.get(1L),
            new Object[] { "PCP", "EME", "GWG", 'M', ItemList.Hull_HV, 'P', ItemList.Electric_Piston_HV, 'E',
                ItemList.Electric_Motor_HV, 'C', OrePrefixes.circuit.get(Materials.Advanced), 'W',
                OrePrefixes.cableGt01.get(Materials.Gold), 'G',
                GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.Chrome, 1) });
        GT_ModHandler.addCraftingRecipe(
            GregtechItemList.Generator_SemiFluid_EV.get(1L),
            new Object[] { "PCP", "EME", "GWG", 'M', ItemList.Hull_EV, 'P', ItemList.Electric_Piston_EV, 'E',
                ItemList.Electric_Motor_EV, 'C', OrePrefixes.circuit.get(Materials.Data), 'W',
                OrePrefixes.cableGt01.get(Materials.Aluminium), 'G', ALLOY.INCOLOY_DS.getGear(1) });
        GT_ModHandler.addCraftingRecipe(
            GregtechItemList.Generator_SemiFluid_IV.get(1L),
            new Object[] { "PCP", "EME", "GWG", 'M', ItemList.Hull_IV, 'P', ItemList.Electric_Piston_IV, 'E',
                ItemList.Electric_Motor_IV, 'C', OrePrefixes.circuit.get(Materials.Elite), 'W',
                OrePrefixes.cableGt01.get(Materials.Tungsten), 'G', ALLOY.NITINOL_60.getGear(1) });

        if (CORE.ConfigSwitches.enableMultiblock_AlloyBlastSmelter) {
            // Industrial Blast Smelter
            RECIPE_IndustrialBlastSmelterController = GregtechItemList.Industrial_AlloyBlastSmelter.get(1);
            RECIPE_IndustrialBlastSmelterFrame = GregtechItemList.Casing_BlastSmelter.get(1);
            RECIPE_IndustrialBlastSmelterCoil = GregtechItemList.Casing_Coil_BlastSmelter.get(1);

            // Blast Smelter
            RecipeUtils.addShapedGregtechRecipe(
                "plateZirconiumCarbide",
                CI.circuitTier5,
                "plateZirconiumCarbide",
                cableTier6,
                IV_MACHINE_AlloySmelter,
                cableTier6,
                "plateZirconiumCarbide",
                CI.circuitTier5,
                "plateZirconiumCarbide",
                RECIPE_IndustrialBlastSmelterController);
            // Blast Smelter Frame Casing
            RecipeUtils.addShapedGregtechRecipe(
                "plateZirconiumCarbide",
                CI.craftingToolHammer_Hard,
                "plateZirconiumCarbide",
                "plateZirconiumCarbide",
                "frameGtZirconiumCarbide",
                "plateZirconiumCarbide",
                "plateZirconiumCarbide",
                CI.craftingToolWrench,
                "plateZirconiumCarbide",
                RECIPE_IndustrialBlastSmelterFrame);
            GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ALLOY.ZIRCONIUM_CARBIDE.getPlate(6), ALLOY.ZIRCONIUM_CARBIDE.getFrameBox(1),
                    GT_Utility.getIntegratedCircuit(1), },
                GT_Values.NF,
                RECIPE_IndustrialBlastSmelterFrame,
                50,
                16);
            // Blast Smelter Coil
            RecipeUtils.addShapedGregtechRecipe(
                "plateStaballoy",
                "plateStaballoy",
                "plateStaballoy",
                "frameGtStaballoy",
                CI.gearboxCasing_Tier_3,
                "frameGtStaballoy",
                "plateStaballoy",
                "plateStaballoy",
                "plateStaballoy",
                RECIPE_IndustrialBlastSmelterCoil);
            GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ALLOY.STABALLOY.getPlate(6), ALLOY.STABALLOY.getFrameBox(2), CI.gearboxCasing_Tier_3,
                    GT_Utility.getIntegratedCircuit(1), },
                GT_Values.NF,
                RECIPE_IndustrialBlastSmelterCoil,
                50,
                16);
        }

        CORE.RA.addSixSlotAssemblingRecipe(
            new ItemStack[] { ItemList.Casing_Coil_Infinity.get(1), ItemList.Reactor_Coolant_Sp_6.get(4),
                ALLOY.LAURENIUM.getPlateDouble(2), CustomItemList.eM_Coil.get(1) },
            ALLOY.QUANTUM.getFluidStack(144 * 4),
            GregtechItemList.Casing_Coil_QuantumForceTransformer.get(1),
            60 * 30,
            MaterialUtils.getVoltageForTier(6));

        TT_recipeAdder.addResearchableAssemblylineRecipe(
            GregtechItemList.Casing_Coil_QuantumForceTransformer.get(1),
            2048 * 120 * 20,
            2048,
            (int) GT_Values.VP[11],
            16,
            new Object[] { GregtechItemList.Controller_MolecularTransformer.get(1),
                GT_ModHandler.getModItem(EternalSingularity.ID, "eternal_singularity", 1),
                new Object[] { OrePrefixes.circuit.get(Materials.Bio), 8 }, ItemList.Electric_Pump_UEV.get(4),
                ItemList.Field_Generator_UEV.get(4), GregtechItemList.Laser_Lens_Special.get(1) },
            new FluidStack[] { MISC_MATERIALS.MUTATED_LIVING_SOLDER.getFluidStack(144 * 10),
                ALLOY.PIKYONIUM.getFluidStack(144 * 32) },
            GregtechItemList.QuantumForceTransformer.get(1),
            1200 * 20,
            (int) GT_Values.VP[11]);

        if (CORE.ConfigSwitches.enableMultiblock_MatterFabricator) {
            // Industrial Matter Fabricator
            RECIPE_IndustrialMatterFabController = GregtechItemList.Industrial_MassFab.get(1);
            RECIPE_IndustrialMatterFabFrame = GregtechItemList.Casing_MatterFab.get(1);
            RECIPE_IndustrialMatterFabCoil = GregtechItemList.Casing_MatterGen.get(1);

            // Matter Fabricator CPU
            RecipeUtils.addShapedGregtechRecipe(
                CI.getPlate(8, 1),
                CI.circuitTier8,
                CI.getPlate(8, 1),
                GT_OreDictUnificator.get(OrePrefixes.cableGt04.get(Materials.NaquadahAlloy), 1L),
                CI.machineCasing_UV,
                GT_OreDictUnificator.get(OrePrefixes.cableGt04.get(Materials.NaquadahAlloy), 1L),
                CI.getPlate(8, 1),
                CI.circuitTier8,
                CI.getPlate(8, 1),
                RECIPE_IndustrialMatterFabController);
            // Matter Fabricator Frame Casing
            RecipeUtils.addShapedGregtechRecipe(
                "plateNiobiumCarbide",
                CI.component_Rod[8],
                "plateNiobiumCarbide",
                CI.component_Rod[8],
                "frameGtInconel690",
                CI.component_Rod[8],
                "plateNiobiumCarbide",
                CI.component_Rod[8],
                "plateNiobiumCarbide",
                RECIPE_IndustrialMatterFabFrame);
            GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ALLOY.NIOBIUM_CARBIDE.getPlate(4), ALLOY.INCONEL_792.getRod(4),
                    ALLOY.INCONEL_690.getFrameBox(1), GT_Utility.getIntegratedCircuit(1), },
                GT_Values.NF,
                RECIPE_IndustrialMatterFabFrame,
                50,
                16);
            // Matter Fabricator Coil
            RecipeUtils.addShapedGregtechRecipe(
                CI.getPlate(6, 1),
                CI.getPlate(7, 1),
                CI.getPlate(6, 1),
                "frameGtStellite",
                CI.machineCasing_UV,
                "frameGtStellite",
                CI.getPlate(6, 1),
                CI.getPlate(7, 1),
                CI.getPlate(6, 1),
                RECIPE_IndustrialMatterFabCoil);
            GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { CI.machineCasing_UV, ALLOY.ZERON_100.getPlate(4), ALLOY.PIKYONIUM.getPlate(2),
                    ALLOY.STELLITE.getFrameBox(2), GT_Utility.getIntegratedCircuit(1), },
                GT_Values.NF,
                RECIPE_IndustrialMatterFabCoil,
                50,
                16);
        }

        if (CORE.ConfigSwitches.enableMultiblock_IndustrialSifter) {
            // Industrial Sieve
            RECIPE_IndustrialSieveController = GregtechItemList.Industrial_Sifter.get(1);
            RECIPE_IndustrialSieveFrame = GregtechItemList.Casing_Sifter.get(1);
            RECIPE_IndustrialSieveGrate = GregtechItemList.Casing_SifterGrate.get(1);

            // Industrial Sieve
            RecipeUtils.addShapedGregtechRecipe(
                "plateEglinSteel",
                CI.circuitTier3,
                "plateEglinSteel",
                cableTier4,
                HV_MACHINE_Sifter,
                cableTier4,
                "plateEglinSteel",
                CI.circuitTier3,
                "plateEglinSteel",
                RECIPE_IndustrialSieveController);
            // Industrial Sieve Casing
            RecipeUtils.addShapedGregtechRecipe(
                "plateEglinSteel",
                "plateEglinSteel",
                "plateEglinSteel",
                "plateEglinSteel",
                "frameGtTumbaga",
                "plateEglinSteel",
                "plateEglinSteel",
                "plateEglinSteel",
                "plateEglinSteel",
                RECIPE_IndustrialSieveFrame);
            GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ALLOY.EGLIN_STEEL.getPlate(8), ALLOY.TUMBAGA.getFrameBox(1),
                    GT_Utility.getIntegratedCircuit(1), },
                GT_Values.NF,
                RECIPE_IndustrialSieveFrame,
                50,
                16);
            // Industrial Sieve Grate
            RecipeUtils.addShapedGregtechRecipe(
                "frameGtEglinSteel",
                "wireFineSteel",
                "frameGtEglinSteel",
                "wireFineSteel",
                "wireFineSteel",
                "wireFineSteel",
                "frameGtEglinSteel",
                "wireFineSteel",
                "frameGtEglinSteel",
                RECIPE_IndustrialSieveGrate);
            GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Steel, 5),
                    ALLOY.EGLIN_STEEL.getFrameBox(4), GT_Utility.getIntegratedCircuit(1), },
                GT_Values.NF,
                RECIPE_IndustrialSieveGrate,
                50,
                16);
        }

        if (CORE.ConfigSwitches.enableMultiblock_TreeFarmer) {
            // Industrial Tree Farmer
            RECIPE_TreeFarmController = GregtechItemList.Industrial_TreeFarm.get(1);
            RECIPE_TreeFarmFrame = GregtechItemList.Casing_PLACEHOLDER_TreeFarmer.get(1);
            // Industrial Tree Farm Controller
            RecipeUtils.addShapedGregtechRecipe(
                ItemList.Field_Generator_IV.get(1),
                ALLOY.INCOLOY_MA956.getRotor(1),
                ItemList.Field_Generator_IV.get(1),
                ALLOY.NITINOL_60.getPlate(1),
                GregtechItemList.GTPP_Casing_IV.get(1),
                ALLOY.NITINOL_60.getPlate(1),
                ItemList.Field_Generator_IV.get(1),
                ALLOY.INCONEL_792.getComponentByPrefix(OrePrefixes.pipeMedium, 1),
                ItemList.Field_Generator_IV.get(1),
                RECIPE_TreeFarmController);
            // Industrial Tree Farm Frame
            GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(2), ALLOY.TUMBAGA.getFrameBox(1),
                    ItemUtils.getItemStackOfAmountFromOreDict("pipeTinySteel", 1), ItemList.MV_Coil.get(1),
                    ItemList.IC2_Plantball.get(4), GT_OreDictUnificator.get(OrePrefixes.plank, Materials.Wood, 8), },
                GT_ModHandler.getDistilledWater(2000),
                RECIPE_TreeFarmFrame,
                200,
                64);
        }

        if (CORE.ConfigSwitches.enableMachine_Tesseracts) {
            // Tesseracts
            RECIPE_TesseractGenerator = GregtechItemList.GT4_Tesseract_Generator.get(1);
            RECIPE_TesseractTerminal = GregtechItemList.GT4_Tesseract_Terminal.get(1);
            // Tesseract Generator
            RecipeUtils.addShapedGregtechRecipe(
                "plateTitanium",
                "circuitMaster",
                "plateTitanium",
                "circuitMaster",
                ItemUtils.getSimpleStack(Blocks.ender_chest),
                "circuitMaster",
                "plateTitanium",
                GregtechItemList.Gregtech_Computer_Cube.get(1),
                "plateTitanium",
                RECIPE_TesseractGenerator);
            // Tesseract Terminal
            RecipeUtils.addShapedGregtechRecipe(
                "plateTitanium",
                "circuitElite",
                "plateTitanium",
                "circuitElite",
                ItemUtils.getSimpleStack(Blocks.ender_chest),
                "circuitElite",
                "plateTitanium",
                CI.machineHull_EV,
                "plateTitanium",
                RECIPE_TesseractTerminal);
        }

        if (CORE.ConfigSwitches.enableMachine_SimpleWasher) {
            final List<ItemStack> washers = ImmutableList.of(
                GregtechItemList.SimpleDustWasher_LV.get(1),
                GregtechItemList.SimpleDustWasher_MV.get(1),
                GregtechItemList.SimpleDustWasher_HV.get(1),
                GregtechItemList.SimpleDustWasher_EV.get(1),
                GregtechItemList.SimpleDustWasher_IV.get(1),
                GregtechItemList.SimpleDustWasher_LuV.get(1),
                GregtechItemList.SimpleDustWasher_ZPM.get(1),
                GregtechItemList.SimpleDustWasher_UV.get(1));

            for (int i = 0; i < washers.size(); i++) {
                final int tier = i + 1;
                CORE.RA.addSixSlotAssemblingRecipe(
                    new ItemStack[] { CI.getTieredMachineHull(tier),
                        CI.getTieredComponent(OrePrefixes.screw, tier, tier * 4),
                        CI.getTieredComponent(OrePrefixes.plate, tier - 1, tier * 2),
                        CI.getTieredComponent(OrePrefixes.rod, tier, tier),
                        CI.getTieredComponent(OrePrefixes.circuit, tier, 1) },
                    CI.getTieredFluid(tier, 144 * tier),
                    washers.get(i),
                    20 * 5 * tier,
                    (int) GT_Values.V[tier]);
            }
        }

        if (CORE.ConfigSwitches.enableMachine_Pollution && PollutionUtils.isPollutionEnabled()) {

            RecipeUtils.addShapedGregtechRecipe(
                "plateCarbon",
                "plateCarbon",
                "plateCarbon",
                "dustCarbon",
                "dustCarbon",
                "dustCarbon",
                "plateCarbon",
                "plateCarbon",
                "plateCarbon",
                ItemUtils.simpleMetaStack(ModItems.itemAirFilter, 0, 1));

            RecipeUtils.addShapedGregtechRecipe(
                "plateCarbon",
                "plateCarbon",
                "plateCarbon",
                "cellLithiumPeroxide",
                "dustCarbon",
                "cellLithiumPeroxide",
                "plateCarbon",
                "plateCarbon",
                "plateCarbon",
                ItemUtils.simpleMetaStack(ModItems.itemAirFilter, 1, 1));

            // Pollution Detector
            RecipeUtils.addShapedGregtechRecipe(
                "plateSteel",
                CI.sensor_LV,
                "plateSteel",
                "plateSteel",
                CI.electricMotor_LV,
                "plateSteel",
                CI.getTieredCircuit(1),
                CI.machineHull_LV,
                CI.getTieredCircuit(1),
                GregtechItemList.Pollution_Detector.get(1));

            // LV
            RecipeUtils.addShapedGregtechRecipe(
                CI.component_Plate[1],
                ItemUtils.simpleMetaStack(ModItems.itemAirFilter, 0, 1),
                CI.component_Plate[1],
                CI.component_Plate[1],
                CI.electricMotor_LV,
                CI.component_Plate[1],
                CI.getTieredCircuit(1),
                CI.machineHull_LV,
                CI.getTieredCircuit(1),
                GregtechItemList.Pollution_Cleaner_LV.get(1));
            // MV
            RecipeUtils.addShapedGregtechRecipe(
                CI.component_Plate[2],
                ItemUtils.simpleMetaStack(ModItems.itemAirFilter, 0, 1),
                CI.component_Plate[2],
                CI.component_Plate[2],
                CI.electricMotor_MV,
                CI.component_Plate[2],
                CI.getTieredCircuit(2),
                CI.machineHull_MV,
                CI.getTieredCircuit(2),
                GregtechItemList.Pollution_Cleaner_MV.get(1));
            // HV
            RecipeUtils.addShapedGregtechRecipe(
                CI.component_Plate[3],
                ItemUtils.simpleMetaStack(ModItems.itemAirFilter, 0, 1),
                CI.component_Plate[3],
                CI.component_Plate[3],
                CI.electricMotor_HV,
                CI.component_Plate[3],
                CI.getTieredCircuit(3),
                CI.machineHull_HV,
                CI.getTieredCircuit(3),
                GregtechItemList.Pollution_Cleaner_HV.get(1));
            // EV
            RecipeUtils.addShapedGregtechRecipe(
                CI.component_Plate[4],
                ItemUtils.simpleMetaStack(ModItems.itemAirFilter, 0, 1),
                CI.component_Plate[4],
                CI.component_Plate[4],
                CI.electricMotor_EV,
                CI.component_Plate[4],
                CI.getTieredCircuit(4),
                CI.machineHull_EV,
                CI.getTieredCircuit(4),
                GregtechItemList.Pollution_Cleaner_EV.get(1));
            // IV
            RecipeUtils.addShapedGregtechRecipe(
                CI.component_Plate[5],
                ItemUtils.simpleMetaStack(ModItems.itemAirFilter, 1, 1),
                CI.component_Plate[5],
                CI.component_Plate[5],
                CI.electricMotor_IV,
                CI.component_Plate[5],
                CI.getTieredCircuit(5),
                CI.machineHull_IV,
                CI.getTieredCircuit(5),
                GregtechItemList.Pollution_Cleaner_IV.get(1));
            // LuV
            RecipeUtils.addShapedGregtechRecipe(
                CI.component_Plate[6],
                ItemUtils.simpleMetaStack(ModItems.itemAirFilter, 1, 1),
                CI.component_Plate[6],
                CI.component_Plate[6],
                CI.electricMotor_LuV,
                CI.component_Plate[6],
                CI.getTieredCircuit(6),
                CI.machineHull_LuV,
                CI.getTieredCircuit(6),
                GregtechItemList.Pollution_Cleaner_LuV.get(1));
            // ZPM
            RecipeUtils.addShapedGregtechRecipe(
                CI.component_Plate[7],
                ItemUtils.simpleMetaStack(ModItems.itemAirFilter, 1, 1),
                CI.component_Plate[7],
                CI.component_Plate[7],
                CI.electricMotor_ZPM,
                CI.component_Plate[7],
                CI.getTieredCircuit(7),
                CI.machineHull_ZPM,
                CI.getTieredCircuit(7),
                GregtechItemList.Pollution_Cleaner_ZPM.get(1));
            // UV
            RecipeUtils.addShapedGregtechRecipe(
                CI.component_Plate[8],
                ItemUtils.simpleMetaStack(ModItems.itemAirFilter, 1, 1),
                CI.component_Plate[8],
                CI.component_Plate[8],
                CI.electricMotor_UV,
                CI.component_Plate[8],
                CI.getTieredCircuit(8),
                CI.machineHull_UV,
                CI.getTieredCircuit(8),
                GregtechItemList.Pollution_Cleaner_UV.get(1));
            // MAX
            RecipeUtils.addShapedGregtechRecipe(
                CI.component_Plate[9],
                ItemUtils.simpleMetaStack(ModItems.itemAirFilter, 1, 1),
                CI.component_Plate[9],
                CI.component_Plate[9],
                CI.electricMotor_UHV,
                CI.component_Plate[9],
                CI.getTieredCircuit(9),
                CI.machineHull_UHV,
                CI.getTieredCircuit(9),
                GregtechItemList.Pollution_Cleaner_MAX.get(1));
        }

        if (CORE.ConfigSwitches.enableMultiblock_ThermalBoiler) {
            RECIPE_ThermalBoilerController = GregtechItemList.GT4_Thermal_Boiler.get(1);
            RECIPE_ThermalBoilerCasing = GregtechItemList.Casing_ThermalContainment.get(2);
            ItemStack centrifugeEV = ItemList.Machine_EV_Centrifuge.get(1);

            RecipeUtils.addShapedGregtechRecipe(
                "craftingGeothermalGenerator",
                centrifugeEV,
                "craftingGeothermalGenerator",
                "gearGtTitanium",
                CI.getTieredCircuitOreDictName(6),
                "gearGtTitanium",
                "craftingGeothermalGenerator",
                centrifugeEV,
                "craftingGeothermalGenerator",
                RECIPE_ThermalBoilerController);

            RecipeUtils.addShapedGregtechRecipe(
                "craftingGeothermalGenerator",
                centrifugeEV,
                "craftingGeothermalGenerator",
                "gearGtTungstenSteel",
                CI.getTieredCircuitOreDictName(5),
                "gearGtTungstenSteel",
                "craftingGeothermalGenerator",
                centrifugeEV,
                "craftingGeothermalGenerator",
                RECIPE_ThermalBoilerController);

            RecipeUtils.addShapedGregtechRecipe(
                ALLOY.MARAGING350.getPlate(1),
                "plateStainlessSteel",
                ALLOY.MARAGING350.getPlate(1),
                "circuitAdvanced",
                CI.machineCasing_HV,
                "circuitAdvanced",
                ALLOY.MARAGING350.getPlate(1),
                ALLOY.MARAGING350.getPlate(1),
                ALLOY.MARAGING350.getPlate(1),
                RECIPE_ThermalBoilerCasing);

            // Lava Filter Recipe
            CORE.RA.addSixSlotAssemblingRecipe(
                new ItemStack[] { CI.getNumberedCircuit(18),
                    ItemUtils.getItemStackOfAmountFromOreDict("dustCarbon", 32),
                    ItemUtils.getItemStackOfAmountFromOreDict("wireFineSteel", 32),
                    ItemUtils.getItemStackOfAmountFromOreDict("ringTumbaga", 16),
                    ItemUtils.getItemStackOfAmountFromOreDict("foilCopper", 4),
                    ItemUtils.getItemStackWithMeta(true, "IC2:itemPartCarbonMesh", "RawCarbonMesh", 0, 64), },
                CI.getTieredFluid(3, 144),
                ItemUtils.getSimpleStack(ModItems.itemLavaFilter, 16),
                1600,
                240);
        }

        // Air Intake Hatch
        RecipeUtils.addShapedGregtechRecipe(
            CI.component_Plate[6],
            ItemList.Casing_Grate.get(1),
            CI.component_Plate[6],
            CI.component_Plate[6],
            CI.getFluidRegulator(5, 1),
            CI.component_Plate[6],
            CI.getTieredCircuit(5),
            ItemList.Hatch_Input_IV.get(1),
            CI.getTieredCircuit(5),
            GregtechItemList.Hatch_Air_Intake.get(1));

        RecipeUtils.addShapedGregtechRecipe(
            CI.getPlate(7, 1),
            GregtechItemList.Hatch_Air_Intake.get(1),
            CI.getPlate(7, 1),
            CI.getPlate(7, 1),
            CI.getFluidRegulator(7, 1),
            CI.getPlate(7, 1),
            CI.getTieredCircuit(7),
            ItemList.Hatch_Input_ZPM.get(1),
            CI.getTieredCircuit(7),
            GregtechItemList.Hatch_Air_Intake_Extreme.get(1));

        if (CORE.ConfigSwitches.enableMultiblock_LiquidFluorideThoriumReactor) {

            // Thorium Reactor
            RECIPE_LFTRController = GregtechItemList.ThoriumReactor.get(1);
            RECIPE_LFTRInnerCasing = GregtechItemList.Casing_Reactor_II.get(1); // Zeron
            RECIPE_LFTROuterCasing = GregtechItemList.Casing_Reactor_I.get(1); // Hastelloy

            ItemStack controlCircuit = ItemUtils.getSimpleStack(ModItems.itemCircuitLFTR);
            RecipeUtils.addShapedGregtechRecipe(
                controlCircuit,
                "cableGt12Naquadah",
                controlCircuit,
                "plateDoubleHastelloyN",
                GregtechItemList.Gregtech_Computer_Cube.get(1),
                "plateDoubleHastelloyN",
                "plateThorium232",
                CI.machineHull_IV,
                "plateThorium232",
                RECIPE_LFTRController);
            RecipeUtils.addShapedGregtechRecipe(
                "plateDoubleHastelloyC276",
                CI.craftingToolScrewdriver,
                "plateDoubleHastelloyC276",
                "gearGtTalonite",
                CI.fieldGenerator_LV,
                "gearGtTalonite",
                "plateDoubleHastelloyC276",
                CI.craftingToolHammer_Hard,
                "plateDoubleHastelloyC276",
                RECIPE_LFTRInnerCasing);

            ItemStack IC2HeatPlate = ItemUtils.getItemStackFromFQRN("IC2:reactorPlatingHeat", 1);
            RecipeUtils.addShapedGregtechRecipe(
                "plateDoubleHastelloyN",
                IC2HeatPlate,
                "plateDoubleHastelloyN",
                IC2HeatPlate,
                "frameGtHastelloyC276",
                IC2HeatPlate,
                "plateDoubleHastelloyN",
                IC2HeatPlate,
                "plateDoubleHastelloyN",
                RECIPE_LFTROuterCasing);

            // LFTR Control Circuit
            CORE.RA.addSixSlotAssemblingRecipe(
                new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict(CI.getTieredCircuitOreDictName(6), 1),
                    CI.fieldGenerator_HV },
                null,
                controlCircuit,
                240 * 20,
                500);

            // Fission Fuel Plant
            RecipeUtils.addShapedGregtechRecipe(
                CI.getTieredCircuitOreDictName(5),
                CI.craftingToolSolderingIron,
                CI.getTieredCircuitOreDictName(5),
                "plateDenseTungstenSteel",
                GregtechItemList.Gregtech_Computer_Cube.get(1),
                "plateDenseTungstenSteel",
                "gearGtStellite",
                CI.machineHull_IV,
                "gearGtStellite",
                GregtechItemList.Industrial_FuelRefinery.get(1));

            ItemStack mInnerTank = ItemList.Super_Tank_IV.get(1);

            // Incoloy Casing
            RecipeUtils.addShapedGregtechRecipe(
                "plateIncoloyDS",
                "pipeHugeStaballoy",
                "plateIncoloyDS",
                "gearGtIncoloyDS",
                mInnerTank,
                "gearGtIncoloyDS",
                "plateIncoloyDS",
                "pipeHugeStaballoy",
                "plateIncoloyDS",
                GregtechItemList.Casing_Refinery_Internal.get(1));

            // Hastelloy-N Sealant Casing
            RecipeUtils.addShapedGregtechRecipe(
                "plateIncoloyMA956",
                "plateHastelloyN",
                "plateIncoloyMA956",
                "plateHastelloyN",
                "frameGtHastelloyC276",
                "plateHastelloyN",
                "plateIncoloyMA956",
                "plateHastelloyN",
                "plateIncoloyMA956",
                GregtechItemList.Casing_Refinery_External.get(1));

            // Hastelloy-X Structural Casing
            RecipeUtils.addShapedGregtechRecipe(
                "ringInconel792",
                "gearGtHastelloyX",
                CI.component_Plate[5],
                CI.craftingToolHammer_Hard,
                "frameGtHastelloyC276",
                CI.craftingToolWrench,
                CI.component_Plate[5],
                CI.getTieredMachineCasing(4),
                "ringInconel792",
                GregtechItemList.Casing_Refinery_Structural.get(1));

            RecipeUtils.addShapedGregtechRecipe(
                CI.getPlate(5, 1),
                ALLOY.HASTELLOY_X.getPlateDouble(1),
                CI.getPlate(5, 1),
                CI.getPlate(5, 1),
                CI.getTieredMachineCasing(5),
                CI.getPlate(5, 1),
                CI.getRobotArm(5, 1),
                ItemList.Casing_FrostProof.get(1),
                CI.getRobotArm(5, 1),
                GregtechItemList.ColdTrap_IV.get(1));
            RecipeUtils.addShapedGregtechRecipe(
                CI.getPlate(7, 1),
                ALLOY.HS188A.getPlateDouble(1),
                CI.getPlate(7, 1),
                CI.getPlate(7, 1),
                GregtechItemList.ColdTrap_IV.get(1),
                CI.getPlate(7, 1),
                CI.getRobotArm(7, 1),
                ItemList.Casing_FrostProof.get(1),
                CI.getRobotArm(7, 1),
                GregtechItemList.ColdTrap_ZPM.get(1));

            RecipeUtils.addShapedGregtechRecipe(
                CI.getFieldGenerator(3, 1),
                CI.getRobotArm(5, 1),
                CI.getPlate(5, 1),
                ALLOY.HASTELLOY_N.getPlateDouble(1),
                ItemList.Machine_IV_ChemicalReactor.get(1),
                ALLOY.HASTELLOY_N.getPlateDouble(1),
                CI.getPlate(5, 1),
                ALLOY.HASTELLOY_N.getPlateDouble(1),
                CI.getFieldGenerator(3, 1),
                GregtechItemList.ReactorProcessingUnit_IV.get(1));
            RecipeUtils.addShapedGregtechRecipe(
                CI.getFieldGenerator(5, 1),
                CI.getRobotArm(7, 1),
                CI.getPlate(7, 1),
                ALLOY.HS188A.getPlateDouble(1),
                GregtechItemList.ReactorProcessingUnit_IV.get(1),
                ALLOY.HS188A.getPlateDouble(1),
                CI.getPlate(7, 1),
                ALLOY.HS188A.getPlateDouble(1),
                CI.getFieldGenerator(5, 1),
                GregtechItemList.ReactorProcessingUnit_ZPM.get(1));

            // Nuclear Salt Processing Plant Controller
            RECIPE_SaltPlantController = GregtechItemList.Nuclear_Salt_Processing_Plant.get(1);

            RecipeUtils.addShapedGregtechRecipe(
                "plateOsmiridium",
                GregtechItemList.ReactorProcessingUnit_IV.get(1),
                "plateOsmiridium",
                "plateRuridit",
                CI.getTieredCircuitOreDictName(7),
                "plateRuridit",
                "plateOsmiridium",
                GregtechItemList.ColdTrap_IV.get(1),
                "plateOsmiridium",
                RECIPE_SaltPlantController);
        }

        // Cyclotron
        if (CORE.ConfigSwitches.enableMultiblock_Cyclotron) {
            RECIPE_CyclotronController = GregtechItemList.COMET_Cyclotron.get(1);
            RECIPE_CyclotronOuterCasing = GregtechItemList.Casing_Cyclotron_External.get(1);
            RECIPE_CyclotronInnerCoil = GregtechItemList.Casing_Cyclotron_Coil.get(1);

            // Outer Casing
            CORE.RA.addSixSlotAssemblingRecipe(
                new ItemStack[] { ItemList.Casing_FrostProof.get(1),
                    ItemUtils.simpleMetaStack("miscutils:itemDehydratorCoilWire", 0, 4), ALLOY.INCOLOY_DS.getPlate(8),
                    ALLOY.INCONEL_690.getScrew(16), ALLOY.EGLIN_STEEL.getLongRod(4), CI.getElectricPiston(3, 2) },
                ALLOY.ZIRCONIUM_CARBIDE.getFluidStack(144 * 8), // Input Fluid
                RECIPE_CyclotronOuterCasing,
                30 * 20 * 2,
                MaterialUtils.getVoltageForTier(4));

            // Inner Coil
            CORE.RA.addSixSlotAssemblingRecipe(
                new ItemStack[] { ItemList.Casing_Coil_Nichrome.get(1),
                    ItemUtils.simpleMetaStack("miscutils:itemDehydratorCoilWire", 1, 8),
                    ALLOY.INCOLOY_MA956.getPlate(8), ALLOY.TANTALLOY_61.getBolt(16), ALLOY.INCOLOY_020.getScrew(32),
                    CI.getFieldGenerator(4, 1) },
                ALLOY.HG1223.getFluidStack(144 * 5), // Input Fluid
                RECIPE_CyclotronInnerCoil,
                60 * 20 * 2,
                MaterialUtils.getVoltageForTier(5));

            // Controller
            CORE.RA.addSixSlotAssemblingRecipe(
                new ItemStack[] { CI.machineHull_IV, ItemUtils.getSimpleStack(RECIPE_CyclotronInnerCoil, 2),
                    ALLOY.INCOLOY_020.getPlate(8), ALLOY.TANTALLOY_61.getGear(2), ALLOY.INCOLOY_MA956.getScrew(16),
                    ItemUtils.getItemStackOfAmountFromOreDict(CI.getTieredCircuitOreDictName(5), 16) },
                ALLOY.INCOLOY_020.getFluidStack(144 * 9), // Input Fluid
                RECIPE_CyclotronController,
                60 * 20 * 5,
                MaterialUtils.getVoltageForTier(5));
        }

        // Mazut
        GT_ModHandler.addCraftingRecipe(
            GregtechItemList.Controller_LargeSemifluidGenerator.get(1L),
            CI.bitsd,
            new Object[] { "PCP", "EME", "GWG", 'M', ItemList.Hull_EV, 'P', ItemList.Electric_Piston_EV, 'E',
                ItemList.Electric_Pump_EV, 'C', OrePrefixes.circuit.get(Materials.Data), 'W',
                OrePrefixes.cableGt08.get(Materials.Electrum), 'G', ALLOY.INCONEL_792.getGear(1) });

        if (CORE.ConfigSwitches.enableMultiblock_PowerSubstation) {
            RecipeUtils.addShapedRecipe(
                "screwTitanium",
                "plateIncoloy020",
                "screwTitanium",
                "plateIncoloy020",
                "frameGtIncoloyMA956",
                "plateIncoloy020",
                "screwTitanium",
                "plateIncoloy020",
                "screwTitanium",
                GregtechItemList.Casing_Power_SubStation.get(1));

            ItemStack mBattery = ItemUtils.getSimpleStack(ModItems.itemCircuitLFTR);

            RecipeUtils.addShapedRecipe(
                "plateIncoloyMA956",
                mBattery,
                "plateIncoloyMA956",
                GregtechItemList.Casing_Power_SubStation.get(1),
                GregtechItemList.Casing_Vanadium_Redox.get(1),
                GregtechItemList.Casing_Power_SubStation.get(1),
                "plateIncoloy020",
                "plateIncoloyMA956",
                "plateIncoloy020",
                GregtechItemList.PowerSubStation.get(1));
        }

        if (CORE.ConfigSwitches.enableMultiblock_IndustrialThermalCentrifuge) {
            RecipeUtils.addShapedRecipe(
                "plateRedSteel",
                CI.craftingToolHammer_Hard,
                "plateRedSteel",
                "plateRedSteel",
                "frameGtBlackSteel",
                "plateRedSteel",
                "plateRedSteel",
                CI.craftingToolWrench,
                "plateRedSteel",
                GregtechItemList.Casing_ThermalCentrifuge.get(1));
            GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.plate, Materials.RedSteel, 6),
                    GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.BlackSteel, 1),
                    GT_Utility.getIntegratedCircuit(1), },
                GT_Values.NF,
                GregtechItemList.Casing_ThermalCentrifuge.get(1L),
                50,
                16);

            RecipeUtils.addShapedRecipe(
                "plateRedSteel",
                "circuitData",
                "plateRedSteel",
                "stickTalonite",
                EV_MACHINE_ThermalCentrifuge,
                "stickTalonite",
                "plateRedSteel",
                "gearGtTalonite",
                "plateRedSteel",
                GregtechItemList.Industrial_ThermalCentrifuge.get(1));
        }

        if (CORE.ConfigSwitches.enableMultiblock_IndustrialWashPlant) {
            RecipeUtils.addShapedRecipe(
                "plateGrisium",
                CI.craftingToolHammer_Hard,
                "plateGrisium",
                "plateTalonite",
                "frameGtGrisium",
                "plateTalonite",
                "plateGrisium",
                CI.craftingToolWrench,
                "plateGrisium",
                GregtechItemList.Casing_WashPlant.get(1));
            GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ALLOY.LEAGRISIUM.getPlate(4), ALLOY.TALONITE.getPlate(2),
                    ALLOY.LEAGRISIUM.getFrameBox(1), GT_Utility.getIntegratedCircuit(1), },
                GT_Values.NF,
                GregtechItemList.Casing_WashPlant.get(1L),
                50,
                16);

            RecipeUtils.addShapedRecipe(
                "plateGrisium",
                EV_MACHINE_OreWasher,
                "plateGrisium",
                "plateTalonite",
                "circuitData",
                "plateTalonite",
                "plateGrisium",
                EV_MACHINE_ChemicalBath,
                "plateGrisium",
                GregtechItemList.Industrial_WashPlant.get(1));
        }

        if (CORE.ConfigSwitches.enableMultiblock_LargeAutoCrafter) {
            CORE.RA.addSixSlotAssemblingRecipe(
                new ItemStack[] { ItemUtils.getSimpleStack(GregtechItemList.Casing_Multi_Use.get(1), 1),
                    ItemList.Block_IridiumTungstensteel.get(1), CI.getTieredComponent(OrePrefixes.circuit, 2, 16),
                    CI.getTieredComponent(OrePrefixes.screw, 5, 32), CI.getTieredComponent(OrePrefixes.bolt, 5, 12),
                    CI.getTieredComponent(OrePrefixes.plate, 6, 8), },
                CI.getTertiaryTieredFluid(6, 144 * (4)),
                GregtechItemList.Casing_Autocrafter.get(1),
                20 * 60 * 2,
                MaterialUtils.getVoltageForTier(5));

            CORE.RA.addSixSlotAssemblingRecipe(
                new ItemStack[] { GregtechItemList.Casing_Refinery_Structural.get(4),
                    ItemUtils.getSimpleStack(ModItems.itemCircuitLFTR, 1),
                    CI.getTieredComponent(OrePrefixes.cableGt08, 6, 16), CI.getTransmissionComponent(5, 2),
                    GregtechItemList.Gregtech_Computer_Cube.get(1), },
                CI.getTieredFluid(7, 144 * 8),
                GregtechItemList.GT4_Multi_Crafter.get(1),
                20 * 60 * 5,
                MaterialUtils.getVoltageForTier(5));

            CORE.RA.addSixSlotAssemblingRecipe(
                new ItemStack[] { ItemUtils.getSimpleStack(GregtechItemList.Casing_Multi_Use.get(1), 1),
                    CI.getEmitter(4, 2), CI.getRobotArm(4, 2), CI.getTieredComponent(OrePrefixes.circuit, 2, 8),
                    CI.getTieredComponent(OrePrefixes.screw, 3, 8), CI.getTieredComponent(OrePrefixes.plate, 5, 4), },
                CI.getAlternativeTieredFluid(5, 144 * 4),
                ItemUtils.getSimpleStack(ModBlocks.blockProjectTable),
                20 * 30 * 3,
                MaterialUtils.getVoltageForTier(4));
        }

        if (CORE.ConfigSwitches.enableMultiblock_IndustrialCuttingMachine) {
            ItemStack plate = ALLOY.MARAGING300.getPlate(1);
            RecipeUtils.addShapedRecipe(
                plate,
                CI.craftingToolHammer_Hard,
                plate,
                "plateStellite",
                "frameGtTalonite",
                "plateStellite",
                plate,
                CI.craftingToolWrench,
                plate,
                GregtechItemList.Casing_CuttingFactoryFrame.get(1));
            GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ALLOY.MARAGING300.getPlate(4), ALLOY.STELLITE.getPlate(2),
                    ALLOY.TALONITE.getFrameBox(1), GT_Utility.getIntegratedCircuit(1), },
                GT_Values.NF,
                GregtechItemList.Casing_CuttingFactoryFrame.get(1L),
                50,
                16);

            RecipeUtils.addShapedRecipe(
                plate,
                CI.getTieredCircuit(4),
                plate,
                "wireFinePlatinum",
                IV_MACHINE_Cutter,
                "wireFinePlatinum",
                plate,
                CI.getTieredCircuit(4),
                plate,
                GregtechItemList.Industrial_CuttingFactoryController.get(1));
        }

        // IV_MACHINE_Extruder
        if (CORE.ConfigSwitches.enableMultiblock_IndustrialExtrudingMachine) {
            ItemStack plate = ALLOY.INCONEL_690.getPlate(1);
            RecipeUtils.addShapedRecipe(
                plate,
                CI.craftingToolHammer_Hard,
                plate,
                "plateTalonite",
                "frameGtStaballoy",
                "plateTalonite",
                plate,
                CI.craftingToolWrench,
                plate,
                GregtechItemList.Casing_Extruder.get(1));
            GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ALLOY.INCONEL_690.getPlate(4), ALLOY.TALONITE.getPlate(2),
                    ALLOY.STABALLOY.getFrameBox(1), GT_Utility.getIntegratedCircuit(1), },
                GT_Values.NF,
                GregtechItemList.Casing_Extruder.get(1L),
                50,
                16);

            RecipeUtils.addShapedRecipe(
                plate,
                CI.getTieredCircuit(5),
                plate,
                CI.electricPiston_IV,
                IV_MACHINE_Extruder,
                CI.electricPiston_IV,
                plate,
                CI.getTieredCircuit(5),
                plate,
                GregtechItemList.Industrial_Extruder.get(1));
        }

        if (CORE.ConfigSwitches.enableMultiblock_IndustrialFishingPort) {
            ItemStack plate = ALLOY.AQUATIC_STEEL.getPlate(1);
            RecipeUtils.addShapedRecipe(
                plate,
                CI.craftingToolHammer_Hard,
                plate,
                "plateEglinSteel",
                "frameGtEglinSteel",
                "plateEglinSteel",
                plate,
                CI.craftingToolWrench,
                plate,
                GregtechItemList.Casing_FishPond.get(1));
            GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ALLOY.AQUATIC_STEEL.getPlate(4), ALLOY.EGLIN_STEEL.getPlate(2),
                    ALLOY.EGLIN_STEEL.getFrameBox(1), GT_Utility.getIntegratedCircuit(1), },
                GT_Values.NF,
                GregtechItemList.Casing_FishPond.get(1L),
                50,
                16);
            RecipeUtils.addShapedRecipe(
                plate,
                CI.getTieredCircuit(5),
                plate,
                "wireFineElectrum",
                ItemUtils.getSimpleStack(ModBlocks.blockFishTrap),
                "wireFineElectrum",
                plate,
                CI.getTieredCircuit(5),
                plate,
                GregtechItemList.Industrial_FishingPond.get(1));
        }

        if (true) {
            // Advanced Vacuum Freezer
            ItemStack plate = ALLOY.HG1223.getPlateDouble(1);
            ItemStack gear = ALLOY.INCOLOY_MA956.getGear(1);
            ItemStack frame = ALLOY.LAFIUM.getFrameBox(1);
            ItemStack cell1 = ItemList.Reactor_Coolant_He_6.get(1);
            ItemStack cell2 = ItemList.Reactor_Coolant_NaK_6.get(1);

            RecipeUtils.addShapedRecipe(
                plate,
                gear,
                plate,
                cell1,
                frame,
                cell2,
                plate,
                gear,
                plate,
                GregtechItemList.Casing_AdvancedVacuum.get(1));
            RecipeUtils.addShapedRecipe(
                gear,
                CI.getTieredCircuit(6),
                gear,
                CI.electricPiston_IV,
                GregtechItemList.Casing_AdvancedVacuum.get(1),
                CI.electricPiston_IV,
                plate,
                GregtechItemList.Gregtech_Computer_Cube.get(1),
                plate,
                GregtechItemList.Industrial_Cryogenic_Freezer.get(1));

            // Advanced Blast Furnace
            plate = ALLOY.HASTELLOY_N.getPlateDouble(1);
            gear = ALLOY.HASTELLOY_W.getGear(1);
            frame = ALLOY.HASTELLOY_X.getFrameBox(1);
            cell1 = ItemUtils.simpleMetaStack("IC2:reactorHeatSwitchDiamond:1", 1, 1);
            cell2 = ItemUtils.simpleMetaStack("IC2:reactorVentGold:1", 1, 1);
            ItemStack cell3 = ItemUtils.simpleMetaStack("IC2:reactorVentDiamond:1:1", 1, 1);

            RecipeUtils.addShapedRecipe(
                plate,
                cell1,
                plate,
                cell3,
                frame,
                cell2,
                plate,
                gear,
                plate,
                GregtechItemList.Casing_Adv_BlastFurnace.get(1));
            RecipeUtils.addShapedRecipe(
                gear,
                CI.getTieredCircuit(6),
                gear,
                CI.robotArm_IV,
                GregtechItemList.Casing_Adv_BlastFurnace.get(1),
                CI.robotArm_IV,
                plate,
                GregtechItemList.Gregtech_Computer_Cube.get(1),
                plate,
                GregtechItemList.Machine_Adv_BlastFurnace.get(1));
            // Hatch_Input_Pyrotheum
            GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hatch_Input_IV.get(1), GregtechItemList.Casing_Adv_BlastFurnace.get(1),
                    ALLOY.MARAGING250.getPlate(4), ALLOY.MARAGING300.getGear(1),
                    GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Elite, 2),
                    GT_Utility.getIntegratedCircuit(1), },
                GT_Values.NF,
                GregtechItemList.Hatch_Input_Pyrotheum.get(1L),
                50,
                16);
            // Casing_Adv_BlastFurnace
            GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ALLOY.HASTELLOY_X.getFrameBox(1), ALLOY.HASTELLOY_N.getPlateDouble(4),
                    ALLOY.HASTELLOY_W.getGear(1), ItemUtils.simpleMetaStack("IC2:reactorHeatSwitchDiamond:1", 1, 1),
                    ItemUtils.simpleMetaStack("IC2:reactorVentGold:1", 1, 1),
                    ItemUtils.simpleMetaStack("IC2:reactorVentDiamond:1:1", 1, 1),
                    GT_Utility.getIntegratedCircuit(1), },
                GT_Values.NF,
                GregtechItemList.Casing_Adv_BlastFurnace.get(1L),
                50,
                16);

            // Advanced Implosion Compressor
            plate = ItemUtils.getItemStackOfAmountFromOreDict("plateAlloyIridium", 1);
            gear = ALLOY.LEAGRISIUM.getGear(1);
            frame = ALLOY.CINOBITE.getFrameBox(1);
            cell1 = ItemUtils.simpleMetaStack("IC2:reactorHeatSwitchDiamond:1", 1, 1);
            cell2 = ItemUtils.simpleMetaStack("IC2:reactorVentGold:1", 1, 1);

            RecipeUtils.addShapedRecipe(
                gear,
                CI.getTieredCircuit(6),
                gear,
                CI.fieldGenerator_IV,
                CI.machineHull_ZPM,
                CI.robotArm_IV,
                plate,
                GregtechItemList.Gregtech_Computer_Cube.get(1),
                plate,
                GregtechItemList.Machine_Adv_ImplosionCompressor.get(1));

            // Supply Depot
            plate = ALLOY.TUNGSTEN_CARBIDE.getPlateDouble(1);
            gear = ALLOY.TRINIUM_TITANIUM.getRing(1);
            frame = ALLOY.TUNGSTEN_CARBIDE.getFrameBox(1);
            cell1 = CI.conveyorModule_LuV;
            cell2 = CI.electricMotor_LuV;
            ItemStack casingAmazon = GregtechItemList.Casing_AmazonWarehouse.get(1);

            RecipeUtils.addShapedRecipe(
                plate,
                ItemUtils.getItemStackOfAmountFromOreDict("cableGt12VanadiumGallium", 1),
                plate,
                cell1,
                frame,
                cell2,
                plate,
                gear,
                plate,
                GregtechItemList.Casing_AmazonWarehouse.get(1));
            GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ALLOY.TUNGSTEN_CARBIDE.getFrameBox(1), ALLOY.TUNGSTEN_CARBIDE.getPlateDouble(4),
                    ALLOY.TRINIUM_TITANIUM.getRing(1), ItemList.Electric_Motor_LuV.get(1),
                    ItemList.Conveyor_Module_LuV.get(1),
                    ItemUtils.getItemStackOfAmountFromOreDict("cableGt12VanadiumGallium", 1),
                    GT_Utility.getIntegratedCircuit(1), },
                GT_Values.NF,
                GregtechItemList.Casing_AmazonWarehouse.get(1L),
                50,
                16);
            RecipeUtils.addShapedRecipe(
                casingAmazon,
                CI.getTieredCircuit(7),
                casingAmazon,
                CI.robotArm_LuV,
                ItemList.Machine_LuV_Unboxinator.get(1),
                CI.robotArm_LuV,
                CI.conveyorModule_LuV,
                GregtechItemList.Gregtech_Computer_Cube.get(1),
                CI.conveyorModule_LuV,
                GregtechItemList.Amazon_Warehouse_Controller.get(1));

            // Industrial Mixing Machine
            RecipeUtils.addShapedRecipe(
                "plateStaballoy",
                CI.getTieredCircuit(5),
                "plateStaballoy",
                "plateZirconiumCarbide",
                IV_MACHINE_Mixer,
                "plateZirconiumCarbide",
                "plateStaballoy",
                CI.getTieredCircuit(5),
                "plateStaballoy",
                GregtechItemList.Industrial_Mixer.get(1));
        }

        if (CORE.ConfigSwitches.enableMultiblock_IndustrialMultiMachine) {
            ItemStack plate = ALLOY.STABALLOY.getPlate(1);

            ItemStack o_Compressor = ItemList.Machine_IV_Compressor.get(1);
            ItemStack o_Lathe = ItemList.Machine_IV_Lathe.get(1);
            ItemStack o_Electromagnet = ItemList.Machine_IV_Polarizer.get(1);
            ItemStack o_Fermenter = ItemList.Machine_IV_Fermenter.get(1);
            ItemStack o_Distillery = ItemList.Machine_IV_FluidExtractor.get(1);
            ItemStack o_Extractor = ItemList.Machine_IV_Extractor.get(1);

            RecipeUtils.addShapedRecipe(
                plate,
                CI.craftingToolHammer_Hard,
                plate,
                "plateStainlessSteel",
                "frameGtZirconiumCarbide",
                "plateStainlessSteel",
                plate,
                CI.craftingToolWrench,
                plate,
                GregtechItemList.Casing_Multi_Use.get(1));
            GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ALLOY.STABALLOY.getPlate(4),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.StainlessSteel, 2),
                    ALLOY.ZIRCONIUM_CARBIDE.getFrameBox(1), GT_Utility.getIntegratedCircuit(1), },
                GT_Values.NF,
                GregtechItemList.Casing_Multi_Use.get(1L),
                50,
                16);

            RecipeUtils.addShapedRecipe(
                o_Compressor,
                o_Lathe,
                o_Electromagnet,
                plate,
                ItemUtils.getSimpleStack(ModBlocks.blockProjectTable),
                plate,
                o_Fermenter,
                o_Distillery,
                o_Extractor,
                GregtechItemList.Industrial_MultiMachine.get(1));
        }

        /*
         * 6/1/19 - Content additions
         */

        // Drilling Platform Casings
        CORE.RA.addSixSlotAssemblingRecipe(
            new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict("frameGtTriniumNaquadahCarbonite", 4),
                ItemUtils.getItemStackOfAmountFromOreDict("plateDoubleTriniumTitaniumAlloy", 1 * (1)),
                ItemUtils.getItemStackOfAmountFromOreDict("gearGtPikyonium64B", 2 * (1)),
                ALLOY.TRINIUM_REINFORCED_STEEL.getPlateDouble(4 * 1),
                ItemUtils.getSimpleStack((CI.machineHull_LuV), 1 * 1), },
            ALLOY.MARAGING350.getFluidStack(144 * 16 * 1),
            GregtechItemList.Casing_BedrockMiner.get(1),
            (int) GT_Values.V[4],
            (int) GT_Values.V[6]);

        int aCostMultiplier = 1;

        // Reservoir Hatch
        GT_Values.RA.addAssemblerRecipe(
            new ItemStack[] { ItemList.Hatch_Input_EV.get(1), GT_ModHandler.getModItem(RemoteIO.ID, "tile.machine", 1),
                ItemList.Electric_Pump_EV.get(1) },
            GT_Values.NF,
            GregtechItemList.Hatch_Reservoir.get(1),
            100,
            1920);

        // Mystic Frame
        CORE.RA.addSixSlotAssemblingRecipe(
            new ItemStack[] { GregtechItemList.Casing_Multi_Use.get(1),
                ItemList.Field_Generator_MV.get(1, CI.circuitTier7),
                ItemList.Field_Generator_HV.get(1, CI.circuitTier7), ItemList.Emitter_HV.get(1, CI.circuitTier7),
                ItemList.Sensor_HV.get(1, CI.circuitTier7),
                CI.getTieredComponent(OrePrefixes.plate, 7, 8 * aCostMultiplier),
                CI.getTieredComponent(OrePrefixes.wireGt08, 8, 4 * aCostMultiplier), },
            CI.getTieredFluid(6, (144 * 8)), // Input Fluid
            ItemUtils.getSimpleStack(Dimension_Everglades.blockPortalFrame, 2),
            45 * 20 * 1 * (6),
            MaterialUtils.getVoltageForTier(6));

        // Player Doors
        ItemStack[] aDoorInputs = new ItemStack[] { ItemUtils.getSimpleStack(Blocks.log2),
            ItemUtils.getSimpleStack(Blocks.iron_block), ItemUtils.getSimpleStack(Blocks.glass),
            ItemUtils.getSimpleStack(Blocks.packed_ice), ItemUtils.getSimpleStack(Blocks.cactus), };
        ItemStack[] aDoorOutputs = new ItemStack[] { ItemUtils.getSimpleStack(ModBlocks.blockPlayerDoorWooden),
            ItemUtils.getSimpleStack(ModBlocks.blockPlayerDoorIron),
            ItemUtils.getSimpleStack(ModBlocks.blockPlayerDoorCustom_Glass),
            ItemUtils.getSimpleStack(ModBlocks.blockPlayerDoorCustom_Ice),
            ItemUtils.getSimpleStack(ModBlocks.blockPlayerDoorCustom_Cactus), };

        for (int y = 0; y < aDoorInputs.length; y++) {
            CORE.RA.addSixSlotAssemblingRecipe(
                new ItemStack[] { ItemUtils.getSimpleStack(Items.iron_door), aDoorInputs[y],
                    ItemList.Sensor_LV.get(1, CI.circuitTier7),
                    CI.getTieredComponent(OrePrefixes.plate, 1, 2 * aCostMultiplier),
                    CI.getTieredComponent(OrePrefixes.wireGt02, 1, 2 * aCostMultiplier),
                    ItemUtils.getSimpleStack(Items.redstone, 16) },
                CI.getTieredFluid(1, (144 * 2)), // Input Fluid
                aDoorOutputs[y],
                100,
                MaterialUtils.getVoltageForTier(1));
        }

        Logger.INFO("Done loading recipes for the Various machine blocks.");
    }

    private static void energyCores() {

        ItemStack[] aBufferOutput = new ItemStack[] { RECIPE_Buffer_ULV, RECIPE_Buffer_LV, RECIPE_Buffer_MV,
            RECIPE_Buffer_HV, RECIPE_Buffer_EV, RECIPE_Buffer_IV, RECIPE_Buffer_LuV, RECIPE_Buffer_ZPM,
            RECIPE_Buffer_UV, RECIPE_Buffer_MAX };

        ItemStack[] aOutput = new ItemStack[] {
            ItemUtils.getItemStackFromFQRN("miscutils:item.itemBufferCore" + "1", 1),
            ItemUtils.getItemStackFromFQRN("miscutils:item.itemBufferCore" + "2", 1),
            ItemUtils.getItemStackFromFQRN("miscutils:item.itemBufferCore" + "3", 1),
            ItemUtils.getItemStackFromFQRN("miscutils:item.itemBufferCore" + "4", 1),
            ItemUtils.getItemStackFromFQRN("miscutils:item.itemBufferCore" + "5", 1),
            ItemUtils.getItemStackFromFQRN("miscutils:item.itemBufferCore" + "6", 1),
            ItemUtils.getItemStackFromFQRN("miscutils:item.itemBufferCore" + "7", 1),
            ItemUtils.getItemStackFromFQRN("miscutils:item.itemBufferCore" + "8", 1),
            ItemUtils.getItemStackFromFQRN("miscutils:item.itemBufferCore" + "9", 1),
            ItemUtils.getItemStackFromFQRN("miscutils:item.itemBufferCore" + "10", 1) };

        for (int i = 0; i < 10; i++) {

            ItemStack aPrevTier = (i == 0 ? CI.getTieredMachineHull(1) : aOutput[i - 1]);
            aPrevTier.stackSize = 1;
            int aTier = (i + 1);
            CORE.RA.addSixSlotAssemblingRecipe(
                new ItemStack[] { aPrevTier, CI.getTieredComponent(OrePrefixes.plate, aTier, 4),
                    CI.getTieredComponent(OrePrefixes.cableGt04, i, 2),
                    CI.getTieredComponent(OrePrefixes.circuit, aTier, 2),
                    CI.getTieredComponent(OrePrefixes.screw, aTier, 6),
                    CI.getTieredComponent(OrePrefixes.bolt, i, 12), },
                CI.getTieredFluid(i, (144 * 4 * aTier)), // Input Fluid
                aOutput[i],
                45 * 10 * 1 * (aTier),
                MaterialUtils.getVoltageForTier(i));

            // Energy Buffer
            CORE.RA.addSixSlotAssemblingRecipe(
                new ItemStack[] { ItemUtils.getSimpleStack(aOutput[i], 4),
                    CI.getTieredComponent(OrePrefixes.plate, aTier, 8),
                    CI.getTieredComponent(OrePrefixes.wireGt08, i, 4), CI.getTieredComponent(OrePrefixes.circuit, i, 4),
                    CI.getTieredComponent(OrePrefixes.stickLong, aTier, 4),
                    CI.getTieredComponent(OrePrefixes.gearGt, i, 5), },
                CI.getTieredFluid(aTier, (144 * 16 * aTier)), // Input Fluid
                aBufferOutput[i],
                45 * 20 * 1 * (aTier),
                MaterialUtils.getVoltageForTier(i));
        }
    }

    private static void wirelessChargers() {

        ItemStack[] aChargers = new ItemStack[] { GregtechItemList.Charger_LV.get(1),
            GregtechItemList.Charger_MV.get(1), GregtechItemList.Charger_HV.get(1), GregtechItemList.Charger_EV.get(1),
            GregtechItemList.Charger_IV.get(1), GregtechItemList.Charger_LuV.get(1),
            GregtechItemList.Charger_ZPM.get(1), GregtechItemList.Charger_UV.get(1),
            GregtechItemList.Charger_UHV.get(1) };

        for (int tier = 1; tier < aChargers.length + 1; tier++) {

            ItemStack[] aInputs = new ItemStack[] { CI.getTieredMachineHull(tier, 1),
                CI.getTransmissionComponent(tier, 2), CI.getFieldGenerator(tier, 1),
                CI.getTieredComponent(OrePrefixes.plate, tier + 1, 4),
                CI.getTieredComponent(OrePrefixes.circuit, tier + 1, 2), };
            CORE.RA.addSixSlotAssemblingRecipe(
                aInputs,
                CI.getAlternativeTieredFluid(tier, (144 * 2 * (tier + 1))), // Input Fluid
                aChargers[tier - 1],
                45 * 10 * (tier + 1),
                MaterialUtils.getVoltageForTier(tier));
        }
    }

    private static void largeArcFurnace() {
        int aCostMultiplier = 1;
        CORE.RA.addSixSlotAssemblingRecipe(
            new ItemStack[] { GregtechItemList.Casing_Multi_Use.get(aCostMultiplier),
                CI.getTransmissionComponent(2, 2 * aCostMultiplier), CI.getElectricPiston(4, 2 * aCostMultiplier),
                CI.getTieredComponent(OrePrefixes.plate, 5, 4 * aCostMultiplier),
                CI.getTieredComponent(OrePrefixes.pipeSmall, 4, 1 * aCostMultiplier), },
            CI.getAlternativeTieredFluid(5, (144 * 2 * 4)), // Input Fluid
            GregtechItemList.Casing_Industrial_Arc_Furnace.get(1),
            20 * 10 * 1 * (6),
            MaterialUtils.getVoltageForTier(5));
        CORE.RA.addSixSlotAssemblingRecipe(
            new ItemStack[] { GregtechItemList.Casing_Industrial_Arc_Furnace.get(1),
                CI.getFieldGenerator(4, 2 * aCostMultiplier), CI.getRobotArm(5, 4 * aCostMultiplier),
                CI.getEnergyCore(4, 2 * aCostMultiplier),
                CI.getTieredComponent(OrePrefixes.plate, 6, 8 * aCostMultiplier),
                CI.getTieredComponent(OrePrefixes.circuit, 5, 8 * aCostMultiplier), },
            CI.getAlternativeTieredFluid(6, (144 * 4 * 5)), // Input Fluid
            GregtechItemList.Industrial_Arc_Furnace.get(1),
            60 * 20 * 8,
            MaterialUtils.getVoltageForTier(6));
    }

    private static void industrialVacuumFurnace() {

        CORE.RA.addSixSlotAssemblingRecipe(
            new ItemStack[] { GregtechItemList.Casing_Multi_Use.get(1), CI.getHeatCoil(2), CI.getElectricPiston(3, 2),
                CI.getTieredComponent(OrePrefixes.plate, 6, 4), CI.getTieredComponent(OrePrefixes.gearGt, 6, 2), },
            CI.getTertiaryTieredFluid(5, (144 * 2 * 4)), // Input Fluid
            GregtechItemList.Casing_Vacuum_Furnace.get(1),
            20 * 10 * 6,
            MaterialUtils.getVoltageForTier(6));

        ;

        CORE.RA.addSixSlotAssemblingRecipe(
            new ItemStack[] { GregtechItemList.Casing_Vacuum_Furnace.get(1),
                CI.getTieredComponent(OrePrefixes.wireGt16, 7, 4), CI.getEnergyCore(5, 1), CI.getRobotArm(4, 4),
                CI.getTieredComponent(OrePrefixes.plate, 6, 8), CI.getTieredComponent(OrePrefixes.circuit, 6, 8), },
            CI.getTieredFluid(6, (144 * 4 * 5)), // Input Fluid
            GregtechItemList.Controller_Vacuum_Furnace.get(1),
            60 * 20 * 12,
            MaterialUtils.getVoltageForTier(6));
    }

    private static void milling() {

        /*
         * public static ItemStack RECIPE_ISAMill_Controller; public static ItemStack RECIPE_ISAMill_Gearbox; public
         * static ItemStack RECIPE_ISAMill_Casing; public static ItemStack RECIPE_ISAMill_Hatch; public static ItemStack
         * RECIPE_Flotation_Controller; public static ItemStack RECIPE_Flotation_Casing;
         */

        // Isa Mill Controller
        CORE.RA.addAssemblylineRecipe(
            ItemList.Machine_IV_Macerator.get(1),
            20 * 60 * 20,
            new Object[] { GregtechItemList.Casing_IsaMill_Gearbox.get(4), CI.getTieredGTPPMachineCasing(6, 4),
                ItemList.Component_Grinder_Tungsten.get(16), new Object[] { CI.getTieredCircuitOreDictName(6), 8 },
                ALLOY.INCONEL_625.getGear(8), ALLOY.INCONEL_625.getPlate(32), ALLOY.ZERON_100.getPlateDouble(8),
                ALLOY.ZERON_100.getPlateDouble(8), ALLOY.ZERON_100.getScrew(64),
                CI.getTieredComponentOfMaterial(Materials.NiobiumTitanium, OrePrefixes.wireFine, 32),
                CI.getTieredComponentOfMaterial(Materials.NiobiumTitanium, OrePrefixes.wireFine, 32),
                CI.getTieredComponentOfMaterial(Materials.Titanium, OrePrefixes.foil, 16),
                CI.getTieredComponentOfMaterial(Materials.Titanium, OrePrefixes.foil, 16), },
            new FluidStack[] { CI.getTieredFluid(6, 16 * 144), CI.getAlternativeTieredFluid(6, 32 * 144),
                CI.getTertiaryTieredFluid(6, 32 * 144) },
            GregtechItemList.Controller_IsaMill.get(1),
            20 * 60 * 10,
            MaterialUtils.getVoltageForTier(6));

        // Isa Mill Gearbox
        CORE.RA.addSixSlotAssemblingRecipe(
            new ItemStack[] { CI.getNumberedAdvancedCircuit(7), ItemList.Casing_Gearbox_Titanium.get(2),
                ALLOY.INCONEL_625.getGear(4),
                CI.getTieredComponentOfMaterial(Materials.HSSE, OrePrefixes.gearGtSmall, 8),
                ALLOY.INCONEL_625.getPlate(16), ALLOY.ZERON_100.getBolt(16), },
            ALLOY.TUNGSTENSTEEL.getFluidStack(8 * 144),
            GregtechItemList.Casing_IsaMill_Gearbox.get(1),
            60 * 20 * 2,
            MaterialUtils.getVoltageForTier(6));

        // Isa Mill Casing
        CORE.RA.addSixSlotAssemblingRecipe(
            new ItemStack[] { CI.getNumberedAdvancedCircuit(7), CI.getTieredGTPPMachineCasing(5, 1),
                ALLOY.INCONEL_625.getPlate(8), ALLOY.ZERON_100.getRod(4),
                CI.getTieredComponentOfMaterial(Materials.HSSG, OrePrefixes.gearGtSmall, 4),
                ALLOY.ZERON_100.getScrew(8), },
            ELEMENT.getInstance().TITANIUM.getFluidStack(4 * 144),
            GregtechItemList.Casing_IsaMill_Casing.get(1),
            60 * 20 * 2,
            MaterialUtils.getVoltageForTier(6));

        // Isa Mill Pipe
        CORE.RA.addSixSlotAssemblingRecipe(
            new ItemStack[] { CI.getNumberedAdvancedCircuit(7), CI.getTieredGTPPMachineCasing(4, 2),
                ALLOY.INCONEL_625.getPlateDouble(4), ALLOY.INCOLOY_MA956.getRing(8),
                CI.getTieredComponentOfMaterial(Materials.HSSE, OrePrefixes.plate, 8),
                ALLOY.INCOLOY_MA956.getBolt(16), },
            ELEMENT.getInstance().ALUMINIUM.getFluidStack(8 * 144),
            GregtechItemList.Casing_IsaMill_Pipe.get(1),
            60 * 20 * 8,
            MaterialUtils.getVoltageForTier(4));

        // Flotation Cell Controller
        CORE.RA.addAssemblylineRecipe(
            ItemList.Distillation_Tower.get(1),
            20 * 60 * 20,
            new Object[] { GregtechItemList.Casing_Flotation_Cell.get(4), CI.getTieredGTPPMachineCasing(5, 4),
                ItemList.Machine_IV_Distillery.get(1), new Object[] { CI.getTieredCircuitOreDictName(6), 8 },
                ALLOY.STELLITE.getGear(8), ALLOY.STELLITE.getPlate(32), ALLOY.HASTELLOY_N.getPlateDouble(8),
                ALLOY.HASTELLOY_N.getPlateDouble(8), ALLOY.HASTELLOY_N.getScrew(64),
                CI.getTieredComponentOfMaterial(Materials.YttriumBariumCuprate, OrePrefixes.wireFine, 64),
                CI.getTieredComponentOfMaterial(Materials.YttriumBariumCuprate, OrePrefixes.wireFine, 64),
                CI.getTieredComponentOfMaterial(Materials.Platinum, OrePrefixes.foil, 32),
                CI.getTieredComponentOfMaterial(Materials.Platinum, OrePrefixes.foil, 32), },
            new FluidStack[] { CI.getTieredFluid(5, 16 * 144), CI.getAlternativeTieredFluid(4, 32 * 144),
                CI.getTertiaryTieredFluid(4, 32 * 144) },
            GregtechItemList.Controller_Flotation_Cell.get(1),
            20 * 60 * 10,
            MaterialUtils.getVoltageForTier(6));

        // Flotation Cell Casing
        CORE.RA.addSixSlotAssemblingRecipe(
            new ItemStack[] { CI.getNumberedAdvancedCircuit(7), CI.getTieredGTPPMachineCasing(4, 1),
                ALLOY.AQUATIC_STEEL.getPlate(8), ALLOY.STELLITE.getRing(8),
                CI.getTieredComponentOfMaterial(Materials.HSSG, OrePrefixes.plateDouble, 4),
                ALLOY.HASTELLOY_N.getScrew(8), },
            ALLOY.STAINLESS_STEEL.getFluidStack(8 * 144),
            GregtechItemList.Casing_Flotation_Cell.get(1),
            60 * 20 * 2,
            MaterialUtils.getVoltageForTier(6));

        // Milling Bus
        CORE.RA.addSixSlotAssemblingRecipe(
            new ItemStack[] { CI.getNumberedAdvancedCircuit(7), CI.getTieredGTPPMachineCasing(5, 1),
                ItemList.Hatch_Input_Bus_EV.get(1),
                CI.getTieredComponentOfMaterial(Materials.Titanium, OrePrefixes.gearGt, 8),
                CI.getTieredComponentOfMaterial(Materials.TungstenSteel, OrePrefixes.plate, 32),
                CI.getTieredComponentOfMaterial(Materials.SolderingAlloy, OrePrefixes.wireFine, 16), },
            ELEMENT.getInstance().TUNGSTEN.getFluidStack(8 * 144),
            GregtechItemList.Bus_Milling_Balls.get(1),
            60 * 20 * 4,
            MaterialUtils.getVoltageForTier(5));
    }

    private static void sparging() {

        // Sparge Tower Research
        CORE.RA.addSixSlotAssemblingRecipe(
            new ItemStack[] { CI.getNumberedAdvancedCircuit(8), ELEMENT.getInstance().HELIUM.getCell(8),
                ELEMENT.getInstance().FLUORINE.getCell(8), ALLOY.HS188A.getIngot(8),
                ItemList.Distillation_Tower.get(1) },
            null,
            ItemDummyResearch.getResearchStack(ASSEMBLY_LINE_RESEARCH.RESEARCH_10_SPARGING, 1),
            60 * 20 * 5,
            MaterialUtils.getVoltageForTier(5));

        // Sparge Tower Controller
        CORE.RA.addAssemblylineRecipe(
            ItemDummyResearch.getResearchStack(ASSEMBLY_LINE_RESEARCH.RESEARCH_10_SPARGING, 1),
            20 * 60 * 20,
            new Object[] { GregtechItemList.Casing_Sparge_Tower_Exterior.get(4), CI.getTieredGTPPMachineCasing(4, 4),
                ItemList.Machine_IV_Distillery.get(1), new Object[] { CI.getTieredCircuitOreDictName(5), 8 },
                ALLOY.HS188A.getGear(8), ALLOY.HS188A.getPlate(32), ALLOY.HASTELLOY_N.getPlateDouble(8),
                ALLOY.HASTELLOY_N.getPlateDouble(8), ALLOY.HASTELLOY_N.getScrew(64),
                CI.getTieredComponentOfMaterial(Materials.YttriumBariumCuprate, OrePrefixes.wireFine, 64),
                CI.getTieredComponentOfMaterial(Materials.YttriumBariumCuprate, OrePrefixes.wireFine, 64),
                CI.getTieredComponentOfMaterial(Materials.Platinum, OrePrefixes.foil, 32),
                CI.getTieredComponentOfMaterial(Materials.Platinum, OrePrefixes.foil, 32), },
            new FluidStack[] { CI.getTieredFluid(4, 16 * 144), CI.getAlternativeTieredFluid(3, 32 * 144),
                CI.getTertiaryTieredFluid(3, 32 * 144) },
            GregtechItemList.Controller_Sparge_Tower.get(1),
            20 * 60 * 10,
            MaterialUtils.getVoltageForTier(6));

        // Sparge Tower Casing
        CORE.RA.addSixSlotAssemblingRecipe(
            new ItemStack[] { CI.getNumberedAdvancedCircuit(8), CI.getTieredGTPPMachineCasing(3, 1),
                ALLOY.HS188A.getPlate(2), ALLOY.HASTELLOY_N.getRing(4),
                CI.getTieredComponentOfMaterial(Materials.TungstenSteel, OrePrefixes.plateDouble, 4),
                ALLOY.HASTELLOY_N.getScrew(4), },
            ALLOY.STAINLESS_STEEL.getFluidStack(8 * 144),
            GregtechItemList.Casing_Sparge_Tower_Exterior.get(1),
            60 * 20 * 2,
            MaterialUtils.getVoltageForTier(5));
    }

    private static void chisels() {
        ItemStack[] aChisels = new ItemStack[] { GregtechItemList.GT_Chisel_LV.get(1),
            GregtechItemList.GT_Chisel_MV.get(1), GregtechItemList.GT_Chisel_HV.get(1), };
        for (int i = 1; i < 4; i++) {
            CORE.RA.addSixSlotAssemblingRecipe(
                new ItemStack[] { CI.getNumberedBioCircuit(10 + i), CI.getTieredMachineCasing(i), CI.getPlate(i, 4),
                    CI.getElectricMotor(i, 2), CI.getConveyor(i, 2), CI.getRobotArm(i, 1) },
                CI.getTieredFluid(i, 144 * 4),
                aChisels[i - 1],
                20 * 20,
                MaterialUtils.getVoltageForTier(i));
        }

        CORE.RA.addSixSlotAssemblingRecipe(
            new ItemStack[] { CI.getNumberedBioCircuit(14), aChisels[2], CI.getPlate(4, 8), CI.getElectricMotor(4, 8),
                CI.getConveyor(4, 8), CI.getRobotArm(4, 4) },
            CI.getTieredFluid(4, 144 * 8),
            GregtechItemList.Controller_IndustrialAutoChisel.get(1),
            20 * 20,
            MaterialUtils.getVoltageForTier(4));

        CORE.RA.addSixSlotAssemblingRecipe(
            new ItemStack[] { CI.getNumberedBioCircuit(14), ItemList.Casing_SolidSteel.get(2), CI.getPlate(4, 2),
                CI.getTieredComponent(OrePrefixes.plate, 3, 4), CI.getTieredComponent(OrePrefixes.ring, 3, 8),
                CI.getTieredComponent(OrePrefixes.rod, 2, 4), },
            CI.getTieredFluid(2, 144 * 2),
            GregtechItemList.Casing_IndustrialAutoChisel.get(1),
            20 * 20,
            MaterialUtils.getVoltageForTier(3));
    }

    private static void rockBreaker() {

        CORE.RA.addSixSlotAssemblingRecipe(
            new ItemStack[] { CI.getNumberedAdvancedCircuit(12), ItemList.Machine_EV_RockBreaker.get(1),
                ALLOY.STAINLESS_STEEL.getPlate(8), ALLOY.STAINLESS_STEEL.getRing(4),
                CI.getTieredComponentOfMaterial(Materials.Aluminium, OrePrefixes.plateDouble, 8),
                ALLOY.EGLIN_STEEL.getScrew(8), },
            ELEMENT.getInstance().ALUMINIUM.getFluidStack(144 * 8),
            GregtechItemList.Controller_IndustrialRockBreaker.get(1),
            60 * 20 * 2,
            MaterialUtils.getVoltageForTier(4));
    }

    private static void fakeMachineCasingCovers() {
        int aMaxTier = GT_Values.VOLTAGE_NAMES.length;
        ItemStack aTier[] = new ItemStack[aMaxTier];
        for (int i = 0; i < aMaxTier; i++) {
            aTier[i] = ItemUtils.simpleMetaStack(CoverManager.Cover_Gt_Machine_Casing, i, 7);
        }
        // Add recipes for new ones
        for (int i = 0; i < aMaxTier; i++) {
            GT_Values.RA
                .addCutterRecipe(CI.getTieredMachineCasing(i), aTier[i], null, 20 * 5 * i, (int) GT_Values.V[i]);
        }
    }

    private static void superBuses() {
        GregtechItemList[] mSuperBusesInput = new GregtechItemList[] { GregtechItemList.Hatch_SuperBus_Input_LV,
            GregtechItemList.Hatch_SuperBus_Input_MV, GregtechItemList.Hatch_SuperBus_Input_HV,
            GregtechItemList.Hatch_SuperBus_Input_EV, GregtechItemList.Hatch_SuperBus_Input_IV,
            GregtechItemList.Hatch_SuperBus_Input_LuV, GregtechItemList.Hatch_SuperBus_Input_ZPM,
            GregtechItemList.Hatch_SuperBus_Input_UV, GregtechItemList.Hatch_SuperBus_Input_MAX, };

        GregtechItemList[] mSuperBusesOutput = new GregtechItemList[] { GregtechItemList.Hatch_SuperBus_Output_LV,
            GregtechItemList.Hatch_SuperBus_Output_MV, GregtechItemList.Hatch_SuperBus_Output_HV,
            GregtechItemList.Hatch_SuperBus_Output_EV, GregtechItemList.Hatch_SuperBus_Output_IV,
            GregtechItemList.Hatch_SuperBus_Output_LuV, GregtechItemList.Hatch_SuperBus_Output_ZPM,
            GregtechItemList.Hatch_SuperBus_Output_UV, GregtechItemList.Hatch_SuperBus_Output_MAX, };

        ItemStack[] mInputHatch = new ItemStack[] { ItemList.Hatch_Input_Bus_EV.get(1),
            ItemList.Hatch_Input_Bus_IV.get(1), ItemList.Hatch_Input_Bus_LuV.get(1),
            ItemList.Hatch_Input_Bus_ZPM.get(1), ItemList.Hatch_Input_Bus_UV.get(1),
            ItemList.Hatch_Input_Bus_MAX.get(1), GregtechItemList.Hatch_SuperBus_Input_LV.get(1),
            GregtechItemList.Hatch_SuperBus_Input_MV.get(1), GregtechItemList.Hatch_SuperBus_Input_HV.get(1),
            GregtechItemList.Hatch_SuperBus_Input_EV.get(1), };

        ItemStack[] mOutputHatch = new ItemStack[] { ItemList.Hatch_Output_Bus_EV.get(1),
            ItemList.Hatch_Output_Bus_IV.get(1), ItemList.Hatch_Output_Bus_LuV.get(1),
            ItemList.Hatch_Output_Bus_ZPM.get(1), ItemList.Hatch_Output_Bus_UV.get(1),
            ItemList.Hatch_Output_Bus_MAX.get(1), GregtechItemList.Hatch_SuperBus_Output_LV.get(1),
            GregtechItemList.Hatch_SuperBus_Output_MV.get(1), GregtechItemList.Hatch_SuperBus_Output_HV.get(1),
            GregtechItemList.Hatch_SuperBus_Output_EV.get(1), };

        // Input Buses
        for (int tier = 1; tier < mSuperBusesInput.length + 1; tier++) {
            CORE.RA.addSixSlotAssemblingRecipe(
                new ItemStack[] { CI.getNumberedCircuit(17), mInputHatch[tier - 1], CI.getElectricMotor(tier, 2),
                    CI.getConveyor(tier, 5), CI.getBolt(tier, 16),
                    CI.getTieredComponent(OrePrefixes.circuit, tier, 2) },
                CI.getAlternativeTieredFluid(tier, 144 * 8),
                mSuperBusesInput[tier - 1].get(1),
                20 * 30 * 2,
                (int) GT_Values.V[tier]);
        }
        // Output Buses
        for (int tier = 1; tier < mSuperBusesOutput.length + 1; tier++) {
            CORE.RA.addSixSlotAssemblingRecipe(
                new ItemStack[] { CI.getNumberedCircuit(18), mOutputHatch[tier - 1], CI.getElectricPiston(tier, 2),
                    CI.getConveyor(tier, 5), CI.getGear(tier, 3), CI.getTieredComponent(OrePrefixes.circuit, tier, 2) },
                CI.getTertiaryTieredFluid(tier, 144 * 8),
                mSuperBusesOutput[tier - 1].get(1),
                20 * 30 * 2,
                (int) GT_Values.V[tier]);
        }
    }

    private static void chiselBuses() {
        ItemStack[] mSuperBusesInput = new ItemStack[] { GregtechItemList.Hatch_SuperBus_Input_LV.get(1),
            GregtechItemList.Hatch_SuperBus_Input_MV.get(1), GregtechItemList.Hatch_SuperBus_Input_HV.get(1), };

        ItemStack[] mChiselBuses = new ItemStack[] { GregtechItemList.GT_MetaTileEntity_ChiselBus_LV.get(1),
            GregtechItemList.GT_MetaTileEntity_ChiselBus_MV.get(1),
            GregtechItemList.GT_MetaTileEntity_ChiselBus_HV.get(1), };

        for (int tier = 1; tier < mChiselBuses.length + 1; tier++) {
            CORE.RA.addSixSlotAssemblingRecipe(
                new ItemStack[] { CI.getNumberedCircuit(17), mSuperBusesInput[tier - 1], CI.getSensor(tier, 1),
                    CI.getRobotArm(tier, 2), CI.getBolt(tier, 16), ItemUtils.getSimpleStack(Blocks.chest) },
                CI.getAlternativeTieredFluid(tier, 144 * 2),
                mChiselBuses[tier - 1],
                20 * 30 * 2,
                (int) GT_Values.VP[tier + 1]);
        }
    }

    private static void solidifierHatches() {
        ItemStack[] mSuperBusesInput = new ItemStack[] { ItemList.Hatch_Input_IV.get(1),
            ItemList.Hatch_Input_LuV.get(1), ItemList.Hatch_Input_ZPM.get(1), ItemList.Hatch_Input_UV.get(1), };

        ItemStack[] mSolidifierHatches = new ItemStack[] { GregtechItemList.GT_MetaTileEntity_Solidifier_I.get(1),
            GregtechItemList.GT_MetaTileEntity_Solidifier_II.get(1),
            GregtechItemList.GT_MetaTileEntity_Solidifier_III.get(1),
            GregtechItemList.GT_MetaTileEntity_Solidifier_IV.get(1), };

        for (int i = 0; i < 4; i++) {
            int componentTier = i + 5;
            CORE.RA.addSixSlotAssemblingRecipe(
                new ItemStack[] { CI.getNumberedCircuit(17), mSuperBusesInput[i], CI.getSensor(componentTier, 1),
                    CI.getFluidRegulator(componentTier, 1),
                    CI.getTieredComponent(OrePrefixes.circuit, componentTier + 1, 4),
                    ItemUtils.getSimpleStack(Blocks.chest) },
                CI.getTieredFluid(componentTier, 144 * 2),
                mSolidifierHatches[i],
                20 * 30,
                (int) GT_Values.VP[componentTier]);
        }
    }
}
