package gregtech.loaders.load;

import static gregtech.api.enums.Mods.BuildCraftFactory;
import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.enums.Mods.GalacticraftCore;
import static gregtech.api.enums.Mods.Gendustry;
import static gregtech.api.enums.Mods.IndustrialCraft2;
import static gregtech.api.enums.Mods.NotEnoughItems;
import static gregtech.api.enums.Mods.Thaumcraft;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import codechicken.nei.api.API;
import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OreDictNames;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine_GT_Recipe;
import gregtech.api.util.ExternalMaterials;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gregtech.loaders.postload.GT_PCBFactoryMaterialLoader;
import gregtech.loaders.postload.GT_ProcessingArrayRecipeLoader;
import ic2.core.Ic2Items;

public class GT_Loader_MetaTileEntities_Recipes implements Runnable {

    private static final String aTextPlate = "PPP";
    private static final String aTextPlateWrench = "PwP";
    private static final String aTextPlateMotor = "PMP";
    private static final String aTextCableHull = "CMC";
    private static final String aTextWireHull = "WMW";
    private static final String aTextWireChest = "WTW";
    private static final String aTextWireCoil = "WCW";
    private static final String aTextMotorWire = "EWE";
    private static final String aTextWirePump = "WPW";

    private static final long bits = GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE
        | GT_ModHandler.RecipeBits.BUFFERED;
    private static final long bitsd = GT_ModHandler.RecipeBits.DISMANTLEABLE | bits;

    private static void registerMachineTypes() {
        registerAlloySmelter();
        registerArcFurnace();
        registerAutoclave();
        registerAssembler();
        registerBendingMachine();
        registerCanner();
        registerCentrifuge();
        registerCompressor();
        registerChemicalBath();
        registerChemicalReactor();
        registerCircuitAssembler();
        registerCuttingMachine();
        registerDistillery();
        registerElectricFurnace();
        registerElectrolyser();
        registerElectromagneticSeparator();
        registerExtractor();
        registerExtruder();
        registerFermenter();
        registerFluidCanner();
        registerFluidExtractor();
        registerFluidHeater();
        registerFluidSolidifier();
        registerForgeHammer();
        registerFormingPress();
        registerLathe();
        registerLaserEngraver();
        registerMacerator();
        registerMatterAmplifier();
        registerMicrowave();
        registerMixer();
        registerOreWasher();
        registerOven();
        registerPlasmaArcFurnace();
        registerPolarizer();
        registerPrinter();
        registerRecycler();
        registerSifter();
        registerSlicer();
        registerThermalCentrifuge();
        registerUnpackager();
        registerWiremill();
    }

    private static void registerAlloySmelter() {
        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_AlloySmelter.get(1),
            bitsd,
            new Object[] { "ECE", aTextCableHull, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL,
                'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE },
            1);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_AlloySmelter.get(1),
            bitsd,
            new Object[] { "ECE", aTextCableHull, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL,
                'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE },
            2);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_AlloySmelter.get(1),
            bitsd,
            new Object[] { "ECE", aTextCableHull, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL,
                'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE },
            3);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_AlloySmelter.get(1),
            bitsd,
            new Object[] { "ECE", aTextCableHull, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL,
                'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE },
            4);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_AlloySmelter.get(1),
            bitsd,
            new Object[] { "ECE", aTextCableHull, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL,
                'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE },
            5);

    }

    private static void registerArcFurnace() {
        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_ArcFurnace.get(1),
            bitsd,
            new Object[] { "WGW", aTextCableHull, aTextPlate, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4, 'G', OrePrefixes.cell.get(Materials.Graphite) },
            1);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_ArcFurnace.get(1),
            bitsd,
            new Object[] { "WGW", aTextCableHull, aTextPlate, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4, 'G', OrePrefixes.cell.get(Materials.Graphite) },
            2);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_ArcFurnace.get(1),
            bitsd,
            new Object[] { "WGW", aTextCableHull, aTextPlate, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4, 'G', OrePrefixes.cell.get(Materials.Graphite) },
            3);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_ArcFurnace.get(1),
            bitsd,
            new Object[] { "WGW", aTextCableHull, aTextPlate, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4, 'G', OrePrefixes.cell.get(Materials.Graphite) },
            4);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_ArcFurnace.get(1),
            bitsd,
            new Object[] { "WGW", aTextCableHull, aTextPlate, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4, 'G', OrePrefixes.cell.get(Materials.Graphite) },
            5);

    }

    private static void registerAssembler() {
        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_Assembler.get(1),
            bitsd,
            new Object[] { "ACA", "VMV", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'V',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'A',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE },
            1);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_Assembler.get(1),
            bitsd,
            new Object[] { "ACA", "VMV", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'V',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'A',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE },
            2);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_Assembler.get(1),
            bitsd,
            new Object[] { "ACA", "VMV", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'V',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'A',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE },
            3);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_Assembler.get(1),
            bitsd,
            new Object[] { "ACA", "VMV", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'V',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'A',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE },
            4);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_Assembler.get(1),
            bitsd,
            new Object[] { "ACA", "VMV", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'V',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'A',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE },
            5);
    }

    private static void registerAutoclave() {
        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_Autoclave.get(1),
            bitsd,
            new Object[] { "IGI", "IMI", "CPC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'I', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE,
                'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS },
            1);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_Autoclave.get(1),
            bitsd,
            new Object[] { "IGI", "IMI", "CPC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'I', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE,
                'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS },
            2);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_Autoclave.get(1),
            bitsd,
            new Object[] { "IGI", "IMI", "CPC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'I', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE,
                'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS },
            3);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_Autoclave.get(1),
            bitsd,
            new Object[] { "IGI", "IMI", "CPC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'I', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE,
                'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS },
            4);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_Autoclave.get(1),
            bitsd,
            new Object[] { "IGI", "IMI", "CPC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'I', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE,
                'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS },
            5);

    }

    private static void registerBendingMachine() {
        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_Bender.get(1),
            bitsd,
            new Object[] { aTextPlateWrench, aTextCableHull, aTextMotorWire, 'M',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR,
                'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE },
            1);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_Bender.get(1),
            bitsd,
            new Object[] { aTextPlateWrench, aTextCableHull, aTextMotorWire, 'M',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR,
                'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE },
            2);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_Bender.get(1),
            bitsd,
            new Object[] { aTextPlateWrench, aTextCableHull, aTextMotorWire, 'M',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR,
                'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE },
            3);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_Bender.get(1),
            bitsd,
            new Object[] { aTextPlateWrench, aTextCableHull, aTextMotorWire, 'M',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR,
                'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE },
            4);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_Bender.get(1),
            bitsd,
            new Object[] { aTextPlateWrench, aTextCableHull, aTextMotorWire, 'M',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR,
                'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE },
            5);
    }

    private static void registerCanner() {
        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_Canner.get(1),
            bitsd,
            new Object[] { aTextWirePump, aTextCableHull, "GGG", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL,
                'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS },
            1);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_Canner.get(1),
            bitsd,
            new Object[] { aTextWirePump, aTextCableHull, "GGG", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL,
                'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS },
            2);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_Canner.get(1),
            bitsd,
            new Object[] { aTextWirePump, aTextCableHull, "GGG", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL,
                'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS },
            3);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_Canner.get(1),
            bitsd,
            new Object[] { aTextWirePump, aTextCableHull, "GGG", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL,
                'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS },
            4);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_Canner.get(1),
            bitsd,
            new Object[] { aTextWirePump, aTextCableHull, "GGG", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL,
                'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS },
            5);
    }

    private static void registerCentrifuge() {
        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_Centrifuge.get(1),
            bitsd,
            new Object[] { "CEC", aTextWireHull, "CEC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE },
            1);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_Centrifuge.get(1),
            bitsd,
            new Object[] { "CEC", aTextWireHull, "CEC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE },
            2);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_Centrifuge.get(1),
            bitsd,
            new Object[] { "CEC", aTextWireHull, "CEC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE },
            3);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_Centrifuge.get(1),
            bitsd,
            new Object[] { "CEC", aTextWireHull, "CEC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE },
            4);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_Centrifuge.get(1),
            bitsd,
            new Object[] { "CEC", aTextWireHull, "CEC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE },
            5);
    }

    private static void registerChemicalBath() {
        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_ChemicalBath.get(1),
            bitsd,
            new Object[] { "VGW", "PGV", aTextCableHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'V',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS },
            1);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_ChemicalBath.get(1),
            bitsd,
            new Object[] { "VGW", "PGV", aTextCableHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'V',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS },
            2);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_ChemicalBath.get(1),
            bitsd,
            new Object[] { "VGW", "PGV", aTextCableHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'V',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS },
            3);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_ChemicalBath.get(1),
            bitsd,
            new Object[] { "VGW", "PGV", aTextCableHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'V',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS },
            4);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_ChemicalBath.get(1),
            bitsd,
            new Object[] { "VGW", "PGV", aTextCableHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'V',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS },
            5);

    }

    private static void registerChemicalReactor() {
        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_ChemicalReactor.get(1),
            bitsd,
            new Object[] { "GRG", "WEW", aTextCableHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR,
                'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS },
            1);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_ChemicalReactor.get(1),
            bitsd,
            new Object[] { "GRG", "WEW", aTextCableHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR,
                'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS },
            2);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_ChemicalReactor.get(1),
            bitsd,
            new Object[] { "GRG", "WEW", aTextCableHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR,
                'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', OrePrefixes.pipeMedium.get(Materials.Plastic) },
            3);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_ChemicalReactor.get(1),
            bitsd,
            new Object[] { "GRG", "WEW", aTextCableHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR,
                'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', OrePrefixes.pipeLarge.get(Materials.Plastic) },
            4);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_ChemicalReactor.get(1),
            bitsd,
            new Object[] { "GRG", "WEW", aTextCableHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR,
                'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', OrePrefixes.pipeHuge.get(Materials.Plastic) },
            5);
    }

    private static void registerCircuitAssembler() {
        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_CircuitAssembler.get(1),
            bitsd,
            new Object[] { "ACE", "VMV", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'V',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'A',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'E',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER },
            1);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_CircuitAssembler.get(1),
            bitsd,
            new Object[] { "ACE", "VMV", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'V',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'A',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'E',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER },
            2);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_CircuitAssembler.get(1),
            bitsd,
            new Object[] { "ACE", "VMV", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'V',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'A',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'E',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER },
            3);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_CircuitAssembler.get(1),
            bitsd,
            new Object[] { "ACE", "VMV", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'V',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'A',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'E',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER },
            4);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_CircuitAssembler.get(1),
            bitsd,
            new Object[] { "ACE", "VMV", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'V',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'A',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'E',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER },
            5);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LuV_CircuitAssembler.get(1),
            bitsd,
            new Object[] { "ACE", "VMV", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'V',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'A',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'E',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER },
            6);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_ZPM_CircuitAssembler.get(1),
            bitsd,
            new Object[] { "ACE", "VMV", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'V',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'A',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'E',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER },
            7);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_UV_CircuitAssembler.get(1),
            bitsd,
            new Object[] { "ACE", "VMV", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'V',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'A',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'E',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER },
            8);

    }

    private static void registerCompressor() {
        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_Compressor.get(1),
            bitsd,
            new Object[] { aTextWireCoil, aTextPlateMotor, aTextWireCoil, 'M',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON,
                'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE },
            1);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_Compressor.get(1),
            bitsd,
            new Object[] { aTextWireCoil, aTextPlateMotor, aTextWireCoil, 'M',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON,
                'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE },
            2);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_Compressor.get(1),
            bitsd,
            new Object[] { aTextWireCoil, aTextPlateMotor, aTextWireCoil, 'M',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON,
                'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE },
            3);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_Compressor.get(1),
            bitsd,
            new Object[] { aTextWireCoil, aTextPlateMotor, aTextWireCoil, 'M',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON,
                'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE },
            4);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_Compressor.get(1),
            bitsd,
            new Object[] { aTextWireCoil, aTextPlateMotor, aTextWireCoil, 'M',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON,
                'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE },
            5);

    }

    private static void registerCuttingMachine() {
        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_Cutter.get(1),
            bitsd,
            new Object[] { "WCG", "VMB", "CWE", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'V',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS,
                'B', OreDictNames.craftingDiamondBlade },
            1);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_Cutter.get(1),
            bitsd,
            new Object[] { "WCG", "VMB", "CWE", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'V',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS,
                'B', OreDictNames.craftingDiamondBlade },
            2);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_Cutter.get(1),
            bitsd,
            new Object[] { "WCG", "VMB", "CWE", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'V',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS,
                'B', OreDictNames.craftingDiamondBlade },
            3);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_Cutter.get(1),
            bitsd,
            new Object[] { "WCG", "VMB", "CWE", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'V',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS,
                'B', OreDictNames.craftingDiamondBlade },
            4);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_Cutter.get(1),
            bitsd,
            new Object[] { "WCG", "VMB", "CWE", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'V',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS,
                'B', OreDictNames.craftingDiamondBlade },
            5);

    }

    private static void registerDistillery() {
        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_Distillery.get(1),
            bitsd,
            new Object[] { "GBG", aTextCableHull, aTextWirePump, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL,
                'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'B',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PIPE, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS },
            1);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_Distillery.get(1),
            bitsd,
            new Object[] { "GBG", aTextCableHull, aTextWirePump, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL,
                'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'B',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PIPE, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS },
            2);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_Distillery.get(1),
            bitsd,
            new Object[] { "GBG", aTextCableHull, aTextWirePump, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL,
                'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'B',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PIPE, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS },
            3);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_Distillery.get(1),
            bitsd,
            new Object[] { "GBG", aTextCableHull, aTextWirePump, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL,
                'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'B',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PIPE, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS },
            4);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_Distillery.get(1),
            bitsd,
            new Object[] { "GBG", aTextCableHull, aTextWirePump, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL,
                'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'B',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PIPE, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS },
            5);

    }

    private static void registerElectricFurnace() {
        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_E_Furnace.get(1),
            bitsd,
            new Object[] { "ECE", aTextCableHull, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL,
                'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING },
            1);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_E_Furnace.get(1),
            bitsd,
            new Object[] { "ECE", aTextCableHull, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL,
                'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING },
            2);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_E_Furnace.get(1),
            bitsd,
            new Object[] { "ECE", aTextCableHull, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL,
                'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING },
            3);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_E_Furnace.get(1),
            bitsd,
            new Object[] { "ECE", aTextCableHull, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL,
                'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING },
            4);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_E_Furnace.get(1),
            bitsd,
            new Object[] { "ECE", aTextCableHull, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL,
                'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING },
            5);

    }

    private static void registerElectrolyser() {
        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_Electrolyzer.get(1),
            bitsd,
            new Object[] { "IGI", "IMI", "CWC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'I', OrePrefixes.wireGt01.get(Materials.Gold), 'G',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS },
            1);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_Electrolyzer.get(1),
            bitsd,
            new Object[] { "IGI", "IMI", "CWC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'I', OrePrefixes.wireGt01.get(Materials.Silver), 'G',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS },
            2);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_Electrolyzer.get(1),
            bitsd,
            new Object[] { "IGI", "IMI", "CWC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'I', OrePrefixes.wireGt01.get(Materials.Electrum), 'G',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS },
            3);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_Electrolyzer.get(1),
            bitsd,
            new Object[] { "IGI", "IMI", "CWC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'I', OrePrefixes.wireGt01.get(Materials.Platinum), 'G',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS },
            4);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_Electrolyzer.get(1),
            bitsd,
            new Object[] { "IGI", "IMI", "CWC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'I', OrePrefixes.wireGt01.get(Materials.HSSG), 'G',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS },
            5);

    }

    private static void registerElectromagneticSeparator() {
        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_ElectromagneticSeparator.get(1),
            bitsd,
            new Object[] { "VWZ", "WMS", "CWZ", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'S',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.STICK_ELECTROMAGNETIC, 'Z',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_ELECTRIC, 'V',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE },
            1);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_ElectromagneticSeparator.get(1),
            bitsd,
            new Object[] { "VWZ", "WMS", "CWZ", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'S',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.STICK_ELECTROMAGNETIC, 'Z',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_ELECTRIC, 'V',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE },
            2);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_ElectromagneticSeparator.get(1),
            bitsd,
            new Object[] { "VWZ", "WMS", "CWZ", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'S',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.STICK_ELECTROMAGNETIC, 'Z',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_ELECTRIC, 'V',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE },
            3);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_ElectromagneticSeparator.get(1),
            bitsd,
            new Object[] { "VWZ", "WMS", "CWZ", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'S',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.STICK_ELECTROMAGNETIC, 'Z',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_ELECTRIC, 'V',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE },
            4);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_ElectromagneticSeparator.get(1),
            bitsd,
            new Object[] { "VWZ", "WMS", "CWZ", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'S',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.STICK_ELECTROMAGNETIC, 'Z',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_ELECTRIC, 'V',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE },
            5);

    }

    private static void registerExtractor() {
        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_Extractor.get(1),
            bitsd,
            new Object[] { "GCG", "EMP", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP,
                'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS },
            1);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_Extractor.get(1),
            bitsd,
            new Object[] { "GCG", "EMP", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP,
                'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS },
            2);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_Extractor.get(1),
            bitsd,
            new Object[] { "GCG", "EMP", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP,
                'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS },
            3);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_Extractor.get(1),
            bitsd,
            new Object[] { "GCG", "EMP", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP,
                'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS },
            4);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_Extractor.get(1),
            bitsd,
            new Object[] { "GCG", "EMP", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP,
                'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS },
            5);

    }

    private static void registerExtruder() {
        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_Extruder.get(1),
            bitsd,
            new Object[] { "CCE", "XMP", "CCE", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'X',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'E',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'P',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PIPE, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE },
            1);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_Extruder.get(1),
            bitsd,
            new Object[] { "CCE", "XMP", "CCE", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'X',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'E',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'P',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PIPE, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE },
            2);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_Extruder.get(1),
            bitsd,
            new Object[] { "CCE", "XMP", "CCE", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'X',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'E',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'P',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PIPE, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE },
            3);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_Extruder.get(1),
            bitsd,
            new Object[] { "CCE", "XMP", "CCE", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'X',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'E',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'P',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PIPE, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE },
            4);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_Extruder.get(1),
            bitsd,
            new Object[] { "CCE", "XMP", "CCE", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'X',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'E',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'P',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PIPE, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE },
            5);

    }

    private static void registerFermenter() {
        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_Fermenter.get(1),
            bitsd,
            new Object[] { aTextWirePump, "GMG", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL,
                'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS },
            1);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_Fermenter.get(1),
            bitsd,
            new Object[] { aTextWirePump, "GMG", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL,
                'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS },
            2);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_Fermenter.get(1),
            bitsd,
            new Object[] { aTextWirePump, "GMG", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL,
                'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS },
            3);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_Fermenter.get(1),
            bitsd,
            new Object[] { aTextWirePump, "GMG", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL,
                'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS },
            4);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_Fermenter.get(1),
            bitsd,
            new Object[] { aTextWirePump, "GMG", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL,
                'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS },
            5);

    }

    private static void registerFluidCanner() {
        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_FluidCanner.get(1),
            bitsd,
            new Object[] { "GCG", "GMG", "WPW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS },
            1);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_FluidCanner.get(1),
            bitsd,
            new Object[] { "GCG", "GMG", "WPW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS },
            2);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_FluidCanner.get(1),
            bitsd,
            new Object[] { "GCG", "GMG", "WPW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS },
            3);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_FluidCanner.get(1),
            bitsd,
            new Object[] { "GCG", "GMG", "WPW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS },
            4);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_FluidCanner.get(1),
            bitsd,
            new Object[] { "GCG", "GMG", "WPW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS },
            5);

    }

    private static void registerFluidExtractor() {
        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_FluidExtractor.get(1),
            bitsd,
            new Object[] { "GEG", "TPT", "CMC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP,
                'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'T',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS },
            1);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_FluidExtractor.get(1),
            bitsd,
            new Object[] { "GEG", "TPT", "CMC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP,
                'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'T',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS },
            2);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_FluidExtractor.get(1),
            bitsd,
            new Object[] { "GEG", "TPT", "CMC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP,
                'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'T',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS },
            3);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_FluidExtractor.get(1),
            bitsd,
            new Object[] { "GEG", "TPT", "CMC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP,
                'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'T',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS },
            4);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_FluidExtractor.get(1),
            bitsd,
            new Object[] { "GEG", "TPT", "CMC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP,
                'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'T',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS },
            5);

    }

    private static void registerFluidHeater() {
        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_FluidHeater.get(1),
            bitsd,
            new Object[] { "OGO", aTextPlateMotor, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL,
                'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'O',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS },
            1);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_FluidHeater.get(1),
            bitsd,
            new Object[] { "OGO", aTextPlateMotor, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL,
                'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'O',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS },
            2);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_FluidHeater.get(1),
            bitsd,
            new Object[] { "OGO", aTextPlateMotor, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL,
                'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'O',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS },
            3);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_FluidHeater.get(1),
            bitsd,
            new Object[] { "OGO", aTextPlateMotor, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL,
                'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'O',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS },
            4);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_FluidHeater.get(1),
            bitsd,
            new Object[] { "OGO", aTextPlateMotor, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL,
                'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'O',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS },
            5);
    }

    private static void registerFluidSolidifier() {
        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_FluidSolidifier.get(1),
            bitsd,
            new Object[] { "PGP", aTextWireHull, "CBC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS,
                'B', OreDictNames.craftingChest },
            1);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_FluidSolidifier.get(1),
            bitsd,
            new Object[] { "PGP", aTextWireHull, "CBC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS,
                'B', OreDictNames.craftingChest },
            2);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_FluidSolidifier.get(1),
            bitsd,
            new Object[] { "PGP", aTextWireHull, "CBC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS,
                'B', OreDictNames.craftingChest },
            3);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_FluidSolidifier.get(1),
            bitsd,
            new Object[] { "PGP", aTextWireHull, "CBC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS,
                'B', OreDictNames.craftingChest },
            4);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_FluidSolidifier.get(1),
            bitsd,
            new Object[] { "PGP", aTextWireHull, "CBC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS,
                'B', OreDictNames.craftingChest },
            5);

    }

    private static void registerForgeHammer() {
        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_Hammer.get(1),
            bitsd,
            new Object[] { aTextWirePump, aTextCableHull, "WAW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL,
                'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'O',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE, 'A', OreDictNames.craftingAnvil },
            1);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_Hammer.get(1),
            bitsd,
            new Object[] { aTextWirePump, aTextCableHull, "WAW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL,
                'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'O',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE, 'A', OreDictNames.craftingAnvil },
            2);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_Hammer.get(1),
            bitsd,
            new Object[] { aTextWirePump, aTextCableHull, "WAW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL,
                'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'O',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE, 'A', OreDictNames.craftingAnvil },
            3);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_Hammer.get(1),
            bitsd,
            new Object[] { aTextWirePump, aTextCableHull, "WAW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL,
                'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'O',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE, 'A', OreDictNames.craftingAnvil },
            4);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_Hammer.get(1),
            bitsd,
            new Object[] { aTextWirePump, aTextCableHull, "WAW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL,
                'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'O',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE, 'A', OreDictNames.craftingAnvil },
            5);

    }

    private static void registerFormingPress() {
        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_Press.get(1),
            bitsd,
            new Object[] { aTextWirePump, aTextCableHull, aTextWirePump, 'M',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON,
                'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE },
            1);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_Press.get(1),
            bitsd,
            new Object[] { aTextWirePump, aTextCableHull, aTextWirePump, 'M',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON,
                'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE },
            2);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_Press.get(1),
            bitsd,
            new Object[] { aTextWirePump, aTextCableHull, aTextWirePump, 'M',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON,
                'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE },
            3);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_Press.get(1),
            bitsd,
            new Object[] { aTextWirePump, aTextCableHull, aTextWirePump, 'M',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON,
                'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE },
            4);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_Press.get(1),
            bitsd,
            new Object[] { aTextWirePump, aTextCableHull, aTextWirePump, 'M',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON,
                'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE },
            5);
    }

    private static void registerLaserEngraver() {
        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_LaserEngraver.get(1),
            bitsd,
            new Object[] { "PEP", aTextCableHull, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL,
                'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER, 'P',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE },
            1);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_LaserEngraver.get(1),
            bitsd,
            new Object[] { "PEP", aTextCableHull, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL,
                'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER, 'P',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE },
            2);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_LaserEngraver.get(1),
            bitsd,
            new Object[] { "PEP", aTextCableHull, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL,
                'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER, 'P',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE },
            3);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_LaserEngraver.get(1),
            bitsd,
            new Object[] { "PEP", aTextCableHull, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL,
                'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER, 'P',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE },
            4);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_LaserEngraver.get(1),
            bitsd,
            new Object[] { "PEP", aTextCableHull, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL,
                'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER, 'P',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE },
            5);

    }

    private static void registerLathe() {
        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_Lathe.get(1),
            bitsd,
            new Object[] { aTextWireCoil, "EMD", "CWP", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'D', OrePrefixes.gem.get(Materials.Diamond) },
            1);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_Lathe.get(1),
            bitsd,
            new Object[] { aTextWireCoil, "EMD", "CWP", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'D', OrePrefixes.gemFlawless.get(Materials.Diamond) },
            2);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_Lathe.get(1),
            bitsd,
            new Object[] { aTextWireCoil, "EMD", "CWP", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'D', OreDictNames.craftingIndustrialDiamond },
            3);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_Lathe.get(1),
            bitsd,
            new Object[] { aTextWireCoil, "EMD", "CWP", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'D', OreDictNames.craftingIndustrialDiamond },
            4);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_Lathe.get(1),
            bitsd,
            new Object[] { aTextWireCoil, "EMD", "CWP", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'D', OreDictNames.craftingIndustrialDiamond },
            5);

    }

    private static void registerMacerator() {
        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_Macerator.get(1),
            bitsd,
            new Object[] { "PEG", "WWM", "CCW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', OrePrefixes.gem.get(Materials.Diamond) },
            1);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_Macerator.get(1),
            bitsd,
            new Object[] { "PEG", "WWM", "CCW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', OrePrefixes.gemFlawless.get(Materials.Diamond) },
            2);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_Macerator.get(1),
            bitsd,
            new Object[] { "PEG", "WWM", "CCW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', OreDictNames.craftingGrinder },
            3);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_Macerator.get(1),
            bitsd,
            new Object[] { "PEG", "WWM", "CCW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', OreDictNames.craftingGrinder },
            4);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_Macerator.get(1),
            bitsd,
            new Object[] { "PEG", "WWM", "CCW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', OreDictNames.craftingGrinder },
            5);

    }

    private static void registerMatterAmplifier() {
        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_Amplifab.get(1),
            bitsd,
            new Object[] { aTextWirePump, aTextPlateMotor, "CPC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL,
                'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4 },
            1);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_Amplifab.get(1),
            bitsd,
            new Object[] { aTextWirePump, aTextPlateMotor, "CPC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL,
                'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4 },
            2);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_Amplifab.get(1),
            bitsd,
            new Object[] { aTextWirePump, aTextPlateMotor, "CPC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL,
                'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4 },
            3);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_Amplifab.get(1),
            bitsd,
            new Object[] { aTextWirePump, aTextPlateMotor, "CPC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL,
                'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4 },
            4);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_Amplifab.get(1),
            bitsd,
            new Object[] { aTextWirePump, aTextPlateMotor, "CPC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL,
                'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4 },
            5);

    }

    private static void registerMicrowave() {
        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_Microwave.get(1),
            bitsd,
            new Object[] { "LWC", "LMR", "LEC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'R',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'L', OrePrefixes.plate.get(Materials.Lead) },
            1);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_Microwave.get(1),
            bitsd,
            new Object[] { "LWC", "LMR", "LEC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'R',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'L', OrePrefixes.plate.get(Materials.Lead) },
            2);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_Microwave.get(1),
            bitsd,
            new Object[] { "LWC", "LMR", "LEC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'R',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'L', OrePrefixes.plate.get(Materials.Lead) },
            3);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_Microwave.get(1),
            bitsd,
            new Object[] { "LWC", "LMR", "LEC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'R',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'L', OrePrefixes.plate.get(Materials.Lead) },
            4);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_Microwave.get(1),
            bitsd,
            new Object[] { "LWC", "LMR", "LEC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'R',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'L', OrePrefixes.plate.get(Materials.Lead) },
            5);

    }

    private static void registerMixer() {
        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_Mixer.get(1),
            bitsd,
            new Object[] { "GRG", "GEG", aTextCableHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'R', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR,
                'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'G',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS },
            1);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_Mixer.get(1),
            bitsd,
            new Object[] { "GRG", "GEG", aTextCableHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'R', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR,
                'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'G',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS },
            2);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_Mixer.get(1),
            bitsd,
            new Object[] { "GRG", "GEG", aTextCableHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'R', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR,
                'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'G',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS },
            3);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_Mixer.get(1),
            bitsd,
            new Object[] { "GRG", "GEG", aTextCableHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'R', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR,
                'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'G',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS },
            4);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_Mixer.get(1),
            bitsd,
            new Object[] { "GRG", "GEG", aTextCableHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'R', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR,
                'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'G',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS },
            5);
    }

    private static void registerOreWasher() {
        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_OreWasher.get(1),
            bitsd,
            new Object[] { "RGR", "CEC", aTextWireHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR,
                'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP },
            1);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_OreWasher.get(1),
            bitsd,
            new Object[] { "RGR", "CEC", aTextWireHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR,
                'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP },
            2);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_OreWasher.get(1),
            bitsd,
            new Object[] { "RGR", "CEC", aTextWireHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR,
                'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP },
            3);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_OreWasher.get(1),
            bitsd,
            new Object[] { "RGR", "CEC", aTextWireHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR,
                'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP },
            4);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_OreWasher.get(1),
            bitsd,
            new Object[] { "RGR", "CEC", aTextWireHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR,
                'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP },
            5);

    }

    private static void registerOven() {
        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_Oven.get(1),
            bitsd,
            new Object[] { "CEC", aTextCableHull, "WEW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING },
            1);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_Oven.get(1),
            bitsd,
            new Object[] { "CEC", aTextCableHull, "WEW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING },
            2);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_Oven.get(1),
            bitsd,
            new Object[] { "CEC", aTextCableHull, "WEW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING },
            3);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_Oven.get(1),
            bitsd,
            new Object[] { "CEC", aTextCableHull, "WEW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING },
            4);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_Oven.get(1),
            bitsd,
            new Object[] { "CEC", aTextCableHull, "WEW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING },
            5);

    }

    private static void registerPlasmaArcFurnace() {
        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_PlasmaArcFurnace.get(1),
            bitsd,
            new Object[] { "WGW", aTextCableHull, "TPT", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4, 'T', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP,
                'G', OrePrefixes.cell.get(Materials.Graphite) },
            1);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_PlasmaArcFurnace.get(1),
            bitsd,
            new Object[] { "WGW", aTextCableHull, "TPT", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4, 'T', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP,
                'G', OrePrefixes.cell.get(Materials.Graphite) },
            2);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_PlasmaArcFurnace.get(1),
            bitsd,
            new Object[] { "WGW", aTextCableHull, "TPT", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4, 'T', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP,
                'G', OrePrefixes.cell.get(Materials.Graphite) },
            3);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_PlasmaArcFurnace.get(1),
            bitsd,
            new Object[] { "WGW", aTextCableHull, "TPT", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4, 'T', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP,
                'G', OrePrefixes.cell.get(Materials.Graphite) },
            4);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_PlasmaArcFurnace.get(1),
            bitsd,
            new Object[] { "WGW", aTextCableHull, "TPT", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4, 'T', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP,
                'G', OrePrefixes.cell.get(Materials.Graphite) },
            5);

    }

    private static void registerPolarizer() {
        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_Polarizer.get(1),
            bitsd,
            new Object[] { "ZSZ", aTextWireHull, "ZSZ", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'S',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.STICK_ELECTROMAGNETIC, 'Z',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_ELECTRIC, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE },
            1);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_Polarizer.get(1),
            bitsd,
            new Object[] { "ZSZ", aTextWireHull, "ZSZ", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'S',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.STICK_ELECTROMAGNETIC, 'Z',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_ELECTRIC, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE },
            2);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_Polarizer.get(1),
            bitsd,
            new Object[] { "ZSZ", aTextWireHull, "ZSZ", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'S',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.STICK_ELECTROMAGNETIC, 'Z',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_ELECTRIC, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE },
            3);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_Polarizer.get(1),
            bitsd,
            new Object[] { "ZSZ", aTextWireHull, "ZSZ", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'S',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.STICK_ELECTROMAGNETIC, 'Z',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_ELECTRIC, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE },
            4);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_Polarizer.get(1),
            bitsd,
            new Object[] { "ZSZ", aTextWireHull, "ZSZ", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'S',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.STICK_ELECTROMAGNETIC, 'Z',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_ELECTRIC, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE },
            5);

    }

    private static void registerPrinter() {
        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_Printer.get(1),
            bitsd,
            new Object[] { aTextMotorWire, aTextCableHull, "WEW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL,
                'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE },
            1);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_Printer.get(1),
            bitsd,
            new Object[] { aTextMotorWire, aTextCableHull, "WEW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL,
                'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE },
            2);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_Printer.get(1),
            bitsd,
            new Object[] { aTextMotorWire, aTextCableHull, "WEW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL,
                'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE },
            3);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_Printer.get(1),
            bitsd,
            new Object[] { aTextMotorWire, aTextCableHull, "WEW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL,
                'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE },
            4);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_Printer.get(1),
            bitsd,
            new Object[] { aTextMotorWire, aTextCableHull, "WEW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL,
                'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE },
            5);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LuV_Printer.get(1),
            bitsd,
            new Object[] { aTextMotorWire, aTextCableHull, "WEW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL,
                'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE },
            6);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_ZPM_Printer.get(1),
            bitsd,
            new Object[] { aTextMotorWire, aTextCableHull, "WEW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL,
                'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE },
            7);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_UV_Printer.get(1),
            bitsd,
            new Object[] { aTextMotorWire, aTextCableHull, "WEW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL,
                'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE },
            8);

    }

    private static void registerRecycler() {
        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_Recycler.get(1),
            bitsd,
            new Object[] { "GCG", aTextPlateMotor, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL,
                'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', OrePrefixes.dust.get(Materials.Glowstone) },
            1);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_Recycler.get(1),
            bitsd,
            new Object[] { "GCG", aTextPlateMotor, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL,
                'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', OrePrefixes.dust.get(Materials.Glowstone) },
            2);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_Recycler.get(1),
            bitsd,
            new Object[] { "GCG", aTextPlateMotor, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL,
                'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', OrePrefixes.dust.get(Materials.Glowstone) },
            3);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_Recycler.get(1),
            bitsd,
            new Object[] { "GCG", aTextPlateMotor, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL,
                'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', OrePrefixes.dust.get(Materials.Glowstone) },
            4);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_Recycler.get(1),
            bitsd,
            new Object[] { "GCG", aTextPlateMotor, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL,
                'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', OrePrefixes.dust.get(Materials.Glowstone) },
            5);

    }

    private static void registerSifter() {
        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_Sifter.get(1),
            bitsd,
            new Object[] { "WFW", aTextPlateMotor, "CFC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'F', OreDictNames.craftingFilter, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE },
            1);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_Sifter.get(1),
            bitsd,
            new Object[] { "WFW", aTextPlateMotor, "CFC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'F', OreDictNames.craftingFilter, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE },
            2);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_Sifter.get(1),
            bitsd,
            new Object[] { "WFW", aTextPlateMotor, "CFC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'F', OreDictNames.craftingFilter, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE },
            3);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_Sifter.get(1),
            bitsd,
            new Object[] { "WFW", aTextPlateMotor, "CFC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'F', OreDictNames.craftingFilter, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE },
            4);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_Sifter.get(1),
            bitsd,
            new Object[] { "WFW", aTextPlateMotor, "CFC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'F', OreDictNames.craftingFilter, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE },
            5);

    }

    private static void registerSlicer() {
        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_Slicer.get(1),
            bitsd,
            new Object[] { aTextWireCoil, "PMV", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL,
                'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'V',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE },
            1);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_Slicer.get(1),
            bitsd,
            new Object[] { aTextWireCoil, "PMV", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL,
                'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'V',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE },
            2);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_Slicer.get(1),
            bitsd,
            new Object[] { aTextWireCoil, "PMV", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL,
                'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'V',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE },
            3);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_Slicer.get(1),
            bitsd,
            new Object[] { aTextWireCoil, "PMV", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL,
                'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'V',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE },
            4);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_Slicer.get(1),
            bitsd,
            new Object[] { aTextWireCoil, "PMV", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL,
                'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'V',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE },
            5);

    }

    private static void registerThermalCentrifuge() {
        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_ThermalCentrifuge.get(1),
            bitsd,
            new Object[] { "CEC", "OMO", "WEW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'O',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE },
            1);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_ThermalCentrifuge.get(1),
            bitsd,
            new Object[] { "CEC", "OMO", "WEW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'O',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE },
            2);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_ThermalCentrifuge.get(1),
            bitsd,
            new Object[] { "CEC", "OMO", "WEW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'O',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE },
            3);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_ThermalCentrifuge.get(1),
            bitsd,
            new Object[] { "CEC", "OMO", "WEW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'O',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE },
            4);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_ThermalCentrifuge.get(1),
            bitsd,
            new Object[] { "CEC", "OMO", "WEW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'O',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE },
            5);

    }

    private static void registerUnpackager() {
        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_Unboxinator.get(1),
            bitsd,
            new Object[] { "BCB", "VMR", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'V',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'B', OreDictNames.craftingChest },
            1);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_Unboxinator.get(1),
            bitsd,
            new Object[] { "BCB", "VMR", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'V',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'B', OreDictNames.craftingChest },
            2);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_Unboxinator.get(1),
            bitsd,
            new Object[] { "BCB", "VMR", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'V',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'B', OreDictNames.craftingChest },
            3);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_Unboxinator.get(1),
            bitsd,
            new Object[] { "BCB", "VMR", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'V',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'B', OreDictNames.craftingChest },
            4);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_Unboxinator.get(1),
            bitsd,
            new Object[] { "BCB", "VMR", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'V',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'B', OreDictNames.craftingChest },
            5);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LuV_Unboxinator.get(1),
            bitsd,
            new Object[] { "BCB", "VMR", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'V',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'B', OreDictNames.craftingChest },
            6);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_ZPM_Unboxinator.get(1),
            bitsd,
            new Object[] { "BCB", "VMR", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'V',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'B', OreDictNames.craftingChest },
            7);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_UV_Unboxinator.get(1),
            bitsd,
            new Object[] { "BCB", "VMR", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'V',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'B', OreDictNames.craftingChest },
            8);
    }

    private static void registerWiremill() {
        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_Wiremill.get(1),
            bitsd,
            new Object[] { aTextMotorWire, aTextCableHull, aTextMotorWire, 'M',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR,
                'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE },
            1);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_Wiremill.get(1),
            bitsd,
            new Object[] { aTextMotorWire, aTextCableHull, aTextMotorWire, 'M',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR,
                'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE },
            2);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_Wiremill.get(1),
            bitsd,
            new Object[] { aTextMotorWire, aTextCableHull, aTextMotorWire, 'M',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR,
                'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE },
            3);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_Wiremill.get(1),
            bitsd,
            new Object[] { aTextMotorWire, aTextCableHull, aTextMotorWire, 'M',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR,
                'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE },
            4);

        GT_ModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_Wiremill.get(1),
            bitsd,
            new Object[] { aTextMotorWire, aTextCableHull, aTextMotorWire, 'M',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR,
                'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE },
            5);
    }

    private static void registerShapedCraftingRecipes() {
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_Pipe_Polytetrafluoroethylene.get(1L),
            bits,
            new Object[] { "PIP", "IFI", "PIP", 'P', OrePrefixes.plate.get(Materials.Polytetrafluoroethylene), 'F',
                OrePrefixes.frameGt.get(Materials.Polytetrafluoroethylene), 'I',
                OrePrefixes.pipeMedium.get(Materials.Polytetrafluoroethylene) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_Pipe_Polybenzimidazole.get(1L),
            bits,
            new Object[] { "PIP", "IFI", "PIP", 'P', OrePrefixes.plate.get(Materials.Polybenzimidazole), 'F',
                OrePrefixes.frameGt.get(Materials.Polybenzimidazole), 'I',
                OrePrefixes.pipeMedium.get(Materials.Polybenzimidazole) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_ULV.get(1L),
            bits,
            new Object[] { aTextPlate, aTextPlateWrench, aTextPlate, 'P',
                OrePrefixes.plate.get(Materials.WroughtIron) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_LV.get(1L),
            bits,
            new Object[] { aTextPlate, aTextPlateWrench, aTextPlate, 'P', OrePrefixes.plate.get(Materials.Steel) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_MV.get(1L),
            bits,
            new Object[] { aTextPlate, aTextPlateWrench, aTextPlate, 'P', OrePrefixes.plate.get(Materials.Aluminium) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_HV.get(1L),
            bits,
            new Object[] { aTextPlate, aTextPlateWrench, aTextPlate, 'P',
                OrePrefixes.plate.get(Materials.StainlessSteel) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_EV.get(1L),
            bits,
            new Object[] { aTextPlate, aTextPlateWrench, aTextPlate, 'P', OrePrefixes.plate.get(Materials.Titanium) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_IV.get(1L),
            bits,
            new Object[] { aTextPlate, aTextPlateWrench, aTextPlate, 'P',
                OrePrefixes.plate.get(Materials.TungstenSteel) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_LuV.get(1L),
            bits,
            new Object[] { aTextPlate, aTextPlateWrench, aTextPlate, 'P',
                OrePrefixes.plate.get(ExternalMaterials.getRhodiumPlatedPalladium()) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_ZPM.get(1L),
            bits,
            new Object[] { aTextPlate, aTextPlateWrench, aTextPlate, 'P', OrePrefixes.plate.get(Materials.Iridium) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_UV.get(1L),
            bits,
            new Object[] { aTextPlate, aTextPlateWrench, aTextPlate, 'P', OrePrefixes.plate.get(Materials.Osmium) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_MAX.get(1L),
            bits,
            new Object[] { aTextPlate, aTextPlateWrench, aTextPlate, 'P',
                OrePrefixes.plate.get(Materials.Neutronium) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_BronzePlatedBricks.get(1L),
            bits,
            new Object[] { "PhP", "PBP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.Bronze), 'B',
                new ItemStack(Blocks.brick_block, 1) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_SolidSteel.get(1L),
            bits,
            new Object[] { "PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.Steel), 'F',
                OrePrefixes.frameGt.get(Materials.Steel) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_StableTitanium.get(1L),
            bits,
            new Object[] { "PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.Titanium), 'F',
                OrePrefixes.frameGt.get(Materials.Titanium) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_HeatProof.get(1L),
            bits,
            new Object[] { "PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.Invar), 'F',
                OrePrefixes.frameGt.get(Materials.Invar) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_FrostProof.get(1L),
            bits,
            new Object[] { "PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.Aluminium), 'F',
                OrePrefixes.frameGt.get(Materials.Aluminium) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_CleanStainlessSteel.get(1L),
            bits,
            new Object[] { "PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.StainlessSteel), 'F',
                OrePrefixes.frameGt.get(Materials.StainlessSteel) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_RobustTungstenSteel.get(1L),
            bits,
            new Object[] { "PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.TungstenSteel), 'F',
                OrePrefixes.frameGt.get(Materials.TungstenSteel) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_MiningOsmiridium.get(1L),
            bits,
            new Object[] { "PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.Osmiridium), 'F',
                OrePrefixes.frameGt.get(Materials.Osmiridium) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_MiningNeutronium.get(1L),
            bits,
            new Object[] { "PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.Neutronium), 'F',
                OrePrefixes.frameGt.get(Materials.Neutronium) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_MiningBlackPlutonium.get(1L),
            bits,
            new Object[] { "PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.BlackPlutonium), 'F',
                OrePrefixes.frameGt.get(Materials.BlackPlutonium) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_Turbine.get(1L),
            bits,
            new Object[] { "PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.Magnalium), 'F',
                OrePrefixes.frameGt.get(Materials.BlueSteel) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_Turbine1.get(1L),
            bits,
            new Object[] { "PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.StainlessSteel), 'F',
                ItemList.Casing_Turbine });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_Turbine2.get(1L),
            bits,
            new Object[] { "PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.Titanium), 'F',
                ItemList.Casing_Turbine });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_Turbine3.get(1L),
            bits,
            new Object[] { "PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.TungstenSteel), 'F',
                ItemList.Casing_Turbine });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_TurbineGasAdvanced.get(1L),
            bits,
            new Object[] { "PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.HSSS), 'F',
                ItemList.Casing_Turbine });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_Pipe_Bronze.get(1L),
            bits,
            new Object[] { "PIP", "IFI", "PIP", 'P', OrePrefixes.plate.get(Materials.Bronze), 'F',
                OrePrefixes.frameGt.get(Materials.Bronze), 'I', OrePrefixes.pipeMedium.get(Materials.Bronze) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_Pipe_Steel.get(1L),
            bits,
            new Object[] { "PIP", "IFI", "PIP", 'P', OrePrefixes.plate.get(Materials.Steel), 'F',
                OrePrefixes.frameGt.get(Materials.Steel), 'I', OrePrefixes.pipeMedium.get(Materials.Steel) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_Pipe_Titanium.get(1L),
            bits,
            new Object[] { "PIP", "IFI", "PIP", 'P', OrePrefixes.plate.get(Materials.Titanium), 'F',
                OrePrefixes.frameGt.get(Materials.Titanium), 'I', OrePrefixes.pipeMedium.get(Materials.Titanium) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_Pipe_TungstenSteel.get(1L),
            bits,
            new Object[] { "PIP", "IFI", "PIP", 'P', OrePrefixes.plate.get(Materials.TungstenSteel), 'F',
                OrePrefixes.frameGt.get(Materials.TungstenSteel), 'I',
                OrePrefixes.pipeMedium.get(Materials.TungstenSteel) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_Gearbox_Bronze.get(1L),
            bits,
            new Object[] { "PhP", "GFG", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.Bronze), 'F',
                OrePrefixes.frameGt.get(Materials.Bronze), 'G', OrePrefixes.gearGt.get(Materials.Bronze) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_Gearbox_Steel.get(1L),
            bits,
            new Object[] { "PhP", "GFG", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.Steel), 'F',
                OrePrefixes.frameGt.get(Materials.Steel), 'G', OrePrefixes.gearGt.get(Materials.Steel) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_Gearbox_Titanium.get(1L),
            bits,
            new Object[] { "PhP", "GFG", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.Steel), 'F',
                OrePrefixes.frameGt.get(Materials.Titanium), 'G', OrePrefixes.gearGt.get(Materials.Titanium) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_Gearbox_TungstenSteel.get(1L),
            bits,
            new Object[] { "PhP", "GFG", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.Steel), 'F',
                OrePrefixes.frameGt.get(Materials.TungstenSteel), 'G', ItemList.Robot_Arm_IV });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_Grate.get(1L),
            bits,
            new Object[] { "PVP", "PFP", aTextPlateMotor, 'P', new ItemStack(Blocks.iron_bars, 1), 'F',
                OrePrefixes.frameGt.get(Materials.Steel), 'M', ItemList.Electric_Motor_MV, 'V',
                OrePrefixes.rotor.get(Materials.Steel) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_Assembler.get(1L),
            bits,
            new Object[] { "PVP", "PFP", aTextPlateMotor, 'P', OrePrefixes.circuit.get(Materials.ZPM), 'F',
                OrePrefixes.frameGt.get(Materials.TungstenSteel), 'M', ItemList.Electric_Motor_IV, 'V',
                OrePrefixes.circuit.get(Materials.LuV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_Firebox_Bronze.get(1L),
            bits,
            new Object[] { "PSP", "SFS", "PSP", 'P', OrePrefixes.plate.get(Materials.Bronze), 'F',
                OrePrefixes.frameGt.get(Materials.Bronze), 'S', OrePrefixes.stick.get(Materials.Bronze) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.WoodenCasing.get(1L),
            bits,
            new Object[] { "PSP", "PFP", "PSP", 'F', OrePrefixes.gear.get(Materials.Wood), 'P',
                OrePrefixes.frameGt.get(Materials.Wood), 'S', OrePrefixes.screw.get(Materials.Wood) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Hatch_Output_ULV.get(1L),
            bits,
            new Object[] { "ASA", "AFA", "APA", 'S', GT_ModHandler.getModItem(BuildCraftFactory.ID, "tankBlock", 1L, 0),
                'F', ItemList.Hull_ULV.get(1L), 'A', OrePrefixes.plate.get(Materials.Rubber), 'P',
                OrePrefixes.ring.get(Materials.Rubber) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Hatch_Input_ULV.get(1L),
            bits,
            new Object[] { "ASA", "AFA", "APA", 'S', GT_ModHandler.getModItem(BuildCraftFactory.ID, "tankBlock", 1L, 0),
                'F', ItemList.Hull_ULV.get(1L), 'A', OrePrefixes.plate.get(Materials.Rubber), 'P',
                OrePrefixes.gear.get(Materials.Rubber) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_Firebox_Steel.get(1L),
            bits,
            new Object[] { "PSP", "SFS", "PSP", 'P', OrePrefixes.plate.get(Materials.Steel), 'F',
                OrePrefixes.frameGt.get(Materials.Steel), 'S', OrePrefixes.stick.get(Materials.Steel) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_Firebox_Titanium.get(1L),
            bits,
            new Object[] { "PSP", "SFS", "PSP", 'P', OrePrefixes.plate.get(Materials.Titanium), 'F',
                OrePrefixes.frameGt.get(Materials.Titanium), 'S', OrePrefixes.stick.get(Materials.Titanium) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_Firebox_TungstenSteel.get(1L),
            bits,
            new Object[] { "PSP", "SFS", "PSP", 'P', OrePrefixes.plate.get(Materials.TungstenSteel), 'F',
                OrePrefixes.frameGt.get(Materials.TungstenSteel), 'S',
                OrePrefixes.stick.get(Materials.TungstenSteel) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_Stripes_A.get(1L),
            bits,
            new Object[] { "Y  ", " M ", "  B", 'M', ItemList.Casing_SolidSteel, 'Y', Dyes.dyeYellow, 'B',
                Dyes.dyeBlack });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_Stripes_B.get(1L),
            bits,
            new Object[] { "  Y", " M ", "B  ", 'M', ItemList.Casing_SolidSteel, 'Y', Dyes.dyeYellow, 'B',
                Dyes.dyeBlack });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_RadioactiveHazard.get(1L),
            bits,
            new Object[] { " YB", " M ", "   ", 'M', ItemList.Casing_SolidSteel, 'Y', Dyes.dyeYellow, 'B',
                Dyes.dyeBlack });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_BioHazard.get(1L),
            bits,
            new Object[] { " Y ", " MB", "   ", 'M', ItemList.Casing_SolidSteel, 'Y', Dyes.dyeYellow, 'B',
                Dyes.dyeBlack });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_ExplosionHazard.get(1L),
            bits,
            new Object[] { " Y ", " M ", "  B", 'M', ItemList.Casing_SolidSteel, 'Y', Dyes.dyeYellow, 'B',
                Dyes.dyeBlack });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_FireHazard.get(1L),
            bits,
            new Object[] { " Y ", " M ", " B ", 'M', ItemList.Casing_SolidSteel, 'Y', Dyes.dyeYellow, 'B',
                Dyes.dyeBlack });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_AcidHazard.get(1L),
            bits,
            new Object[] { " Y ", " M ", "B  ", 'M', ItemList.Casing_SolidSteel, 'Y', Dyes.dyeYellow, 'B',
                Dyes.dyeBlack });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_MagicHazard.get(1L),
            bits,
            new Object[] { " Y ", "BM ", "   ", 'M', ItemList.Casing_SolidSteel, 'Y', Dyes.dyeYellow, 'B',
                Dyes.dyeBlack });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_FrostHazard.get(1L),
            bits,
            new Object[] { "BY ", " M ", "   ", 'M', ItemList.Casing_SolidSteel, 'Y', Dyes.dyeYellow, 'B',
                Dyes.dyeBlack });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_NoiseHazard.get(1L),
            bits,
            new Object[] { "   ", " M ", "BY ", 'M', ItemList.Casing_SolidSteel, 'Y', Dyes.dyeYellow, 'B',
                Dyes.dyeBlack });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_Advanced_Iridium.get(1L),
            bits,
            new Object[] { "PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.Iridium), 'F',
                OrePrefixes.frameGt.get(Materials.Iridium) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_Bricked_BlastFurnace.get(1L),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED,
            new Object[] { "BFB", "FwF", "BFB", 'B', ItemList.Casing_Firebricks, 'F',
                OreDictNames.craftingIronFurnace });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Hull_Bronze.get(1L),
            bits,
            new Object[] { aTextPlate, "PhP", aTextPlate, 'P', OrePrefixes.plate.get(Materials.Bronze) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Hull_Bronze_Bricks.get(1L),
            bits,
            new Object[] { aTextPlate, "PhP", "BBB", 'P', OrePrefixes.plate.get(Materials.Bronze), 'B',
                new ItemStack(Blocks.brick_block, 1) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Hull_HP.get(1L),
            bits,
            new Object[] { aTextPlate, "PhP", aTextPlate, 'P', OrePrefixes.plate.get(Materials.Steel) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Hull_HP_Bricks.get(1L),
            bits,
            new Object[] { aTextPlate, "PhP", "BBB", 'P', OrePrefixes.plate.get(Materials.WroughtIron), 'B',
                new ItemStack(Blocks.brick_block, 1) });

        // hull crafting recipes. (They can't be used for recycling as that would create an exploit loop with the
        // assembler recipes.)

        GT_ModHandler.addCraftingRecipe(
            ItemList.Hull_ULV.get(1L),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED,
            new Object[] { "PHP", aTextCableHull, 'M', ItemList.Casing_ULV, 'C',
                OrePrefixes.cableGt01.get(Materials.Lead), 'H', OrePrefixes.plate.get(Materials.WroughtIron), 'P',
                OrePrefixes.plate.get(Materials.Wood) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Hull_LV.get(1L),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED,
            new Object[] { "PHP", aTextCableHull, 'M', ItemList.Casing_LV, 'C',
                OrePrefixes.cableGt01.get(Materials.Tin), 'H', OrePrefixes.plate.get(Materials.Steel), 'P',
                OrePrefixes.plate.get(Materials.WroughtIron) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Hull_MV.get(1L),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED,
            new Object[] { "PHP", aTextCableHull, 'M', ItemList.Casing_MV, 'C',
                OrePrefixes.cableGt01.get(Materials.Copper), 'H', OrePrefixes.plate.get(Materials.Aluminium), 'P',
                OrePrefixes.plate.get(Materials.WroughtIron) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Hull_HV.get(1L),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED,
            new Object[] { "PHP", aTextCableHull, 'M', ItemList.Casing_HV, 'C',
                OrePrefixes.cableGt01.get(Materials.Gold), 'H', OrePrefixes.plate.get(Materials.StainlessSteel), 'P',
                OrePrefixes.plate.get(Materials.Plastic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Hull_EV.get(1L),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED,
            new Object[] { "PHP", aTextCableHull, 'M', ItemList.Casing_EV, 'C',
                OrePrefixes.cableGt01.get(Materials.Aluminium), 'H', OrePrefixes.plate.get(Materials.Titanium), 'P',
                OrePrefixes.plate.get(Materials.Plastic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Hull_IV.get(1L),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED,
            new Object[] { "PHP", aTextCableHull, 'M', ItemList.Casing_IV, 'C',
                OrePrefixes.cableGt01.get(Materials.Tungsten), 'H', OrePrefixes.plate.get(Materials.TungstenSteel), 'P',
                OrePrefixes.plate.get(Materials.Polytetrafluoroethylene) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Hull_LuV.get(1L),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED,
            new Object[] { "PHP", aTextCableHull, 'M', ItemList.Casing_LuV, 'C',
                OrePrefixes.cableGt01.get(Materials.VanadiumGallium), 'H',
                OrePrefixes.plate.get(ExternalMaterials.getRhodiumPlatedPalladium()), 'P',
                OrePrefixes.plate.get(Materials.Polytetrafluoroethylene) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Hull_ZPM.get(1L),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED,
            new Object[] { "PHP", aTextCableHull, 'M', ItemList.Casing_ZPM, 'C',
                OrePrefixes.cableGt01.get(Materials.Naquadah), 'H', OrePrefixes.plate.get(Materials.Iridium), 'P',
                OrePrefixes.plate.get(Materials.Polybenzimidazole) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Hull_UV.get(1L),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED,
            new Object[] { "PHP", aTextCableHull, 'M', ItemList.Casing_UV, 'C',
                OrePrefixes.wireGt04.get(Materials.NaquadahAlloy), 'H', OrePrefixes.plate.get(Materials.Osmium), 'P',
                OrePrefixes.plate.get(Materials.Polybenzimidazole) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Hull_MAX.get(1L),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED,
            new Object[] { "PHP", aTextCableHull, 'M', ItemList.Casing_MAX, 'C',
                OrePrefixes.wireGt04.get(Materials.SuperconductorUV), 'H', OrePrefixes.plate.get(Materials.Neutronium),
                'P', OrePrefixes.plate.get(Materials.Polybenzimidazole) });

        // hull recycling data
        GT_OreDictUnificator.addItemDataFromInputs(
            ItemList.Hull_ULV.get(1L),
            ItemList.Casing_ULV.get(1),
            OrePrefixes.cableGt01.get(Materials.Lead),
            OrePrefixes.cableGt01.get(Materials.Lead));
        GT_OreDictUnificator.addItemDataFromInputs(
            ItemList.Hull_LV.get(1L),
            ItemList.Casing_LV.get(1),
            OrePrefixes.cableGt01.get(Materials.Tin),
            OrePrefixes.cableGt01.get(Materials.Tin));
        GT_OreDictUnificator.addItemDataFromInputs(
            ItemList.Hull_MV.get(1L),
            ItemList.Casing_MV.get(1),
            OrePrefixes.cableGt01.get(Materials.AnyCopper),
            OrePrefixes.cableGt01.get(Materials.AnyCopper));
        GT_OreDictUnificator.addItemDataFromInputs(
            ItemList.Hull_HV.get(1L),
            ItemList.Casing_HV.get(1),
            OrePrefixes.cableGt01.get(Materials.Gold),
            OrePrefixes.cableGt01.get(Materials.Gold));
        GT_OreDictUnificator.addItemDataFromInputs(
            ItemList.Hull_EV.get(1L),
            ItemList.Casing_EV.get(1),
            OrePrefixes.cableGt01.get(Materials.Aluminium),
            OrePrefixes.cableGt01.get(Materials.Aluminium));
        GT_OreDictUnificator.addItemDataFromInputs(
            ItemList.Hull_IV.get(1L),
            ItemList.Casing_IV.get(1),
            OrePrefixes.cableGt01.get(Materials.Tungsten),
            OrePrefixes.cableGt01.get(Materials.Tungsten));
        GT_OreDictUnificator.addItemDataFromInputs(
            ItemList.Hull_LuV.get(1L),
            ItemList.Casing_LuV.get(1),
            OrePrefixes.cableGt01.get(Materials.VanadiumGallium),
            OrePrefixes.cableGt01.get(Materials.VanadiumGallium));
        GT_OreDictUnificator.addItemDataFromInputs(
            ItemList.Hull_ZPM.get(1L),
            ItemList.Casing_ZPM.get(1),
            OrePrefixes.cableGt02.get(Materials.Naquadah),
            OrePrefixes.cableGt02.get(Materials.Naquadah));
        GT_OreDictUnificator.addItemDataFromInputs(
            ItemList.Hull_UV.get(1L),
            ItemList.Casing_UV.get(1),
            OrePrefixes.cableGt04.get(Materials.NaquadahAlloy),
            OrePrefixes.cableGt04.get(Materials.NaquadahAlloy));
        GT_OreDictUnificator.addItemDataFromInputs(
            ItemList.Hull_MAX.get(1L),
            ItemList.Casing_MAX.get(1),
            OrePrefixes.cableGt04.get(Materials.SuperconductorUV),
            OrePrefixes.cableGt04.get(Materials.SuperconductorUV));

        GT_ModHandler.addCraftingRecipe(
            ItemList.Transformer_LV_ULV.get(1L),
            bitsd,
            new Object[] { " BB", "CM ", " BB", 'M', ItemList.Hull_ULV, 'C', OrePrefixes.cableGt01.get(Materials.Tin),
                'B', OrePrefixes.cableGt01.get(Materials.Lead) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Transformer_MV_LV.get(1L),
            bitsd,
            new Object[] { " BB", "CM ", " BB", 'M', ItemList.Hull_LV, 'C',
                OrePrefixes.cableGt01.get(Materials.AnyCopper), 'B', OrePrefixes.cableGt01.get(Materials.Tin) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Transformer_HV_MV.get(1L),
            bitsd,
            new Object[] { "KBB", "CM ", "KBB", 'M', ItemList.Hull_MV, 'C', OrePrefixes.cableGt01.get(Materials.Gold),
                'B', OrePrefixes.cableGt01.get(Materials.AnyCopper), 'K',
                OrePrefixes.componentCircuit.get(Materials.Inductor) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Transformer_EV_HV.get(1L),
            bitsd,
            new Object[] { "KBB", "CM ", "KBB", 'M', ItemList.Hull_HV, 'C',
                OrePrefixes.cableGt01.get(Materials.Aluminium), 'B', OrePrefixes.cableGt01.get(Materials.Gold), 'K',
                ItemList.Circuit_Chip_ULPIC });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Transformer_IV_EV.get(1L),
            bitsd,
            new Object[] { "KBB", "CM ", "KBB", 'M', ItemList.Hull_EV, 'C',
                OrePrefixes.cableGt01.get(Materials.Tungsten), 'B', OrePrefixes.cableGt01.get(Materials.Aluminium), 'K',
                ItemList.Circuit_Chip_LPIC });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Transformer_LuV_IV.get(1L),
            bitsd,
            new Object[] { "KBB", "CM ", "KBB", 'M', ItemList.Hull_IV, 'C',
                OrePrefixes.cableGt01.get(Materials.VanadiumGallium), 'B',
                OrePrefixes.cableGt01.get(Materials.Tungsten), 'K', ItemList.Circuit_Chip_PIC });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Transformer_ZPM_LuV.get(1L),
            bitsd,
            new Object[] { "KBB", "CM ", "KBB", 'M', ItemList.Hull_LuV, 'C',
                OrePrefixes.cableGt01.get(Materials.Naquadah), 'B',
                OrePrefixes.cableGt01.get(Materials.VanadiumGallium), 'K', ItemList.Circuit_Chip_HPIC });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Transformer_UV_ZPM.get(1L),
            bitsd,
            new Object[] { "KBB", "CM ", "KBB", 'M', ItemList.Hull_ZPM, 'C',
                OrePrefixes.cableGt01.get(Materials.NaquadahAlloy), 'B', OrePrefixes.cableGt01.get(Materials.Naquadah),
                'K', ItemList.Circuit_Chip_UHPIC });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Transformer_MAX_UV.get(1L),
            bitsd,
            new Object[] { "KBB", "CM ", "KBB", 'M', ItemList.Hull_UV, 'C',
                OrePrefixes.wireGt01.get(Materials.Bedrockium), 'B', OrePrefixes.cableGt01.get(Materials.NaquadahAlloy),
                'K', ItemList.Circuit_Chip_NPIC });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Hatch_Dynamo_ULV.get(1L),
            bitsd,
            new Object[] { "XOL", "SMP", "XOL", 'M', ItemList.Hull_ULV, 'S', OrePrefixes.spring.get(Materials.Lead),
                'X', OrePrefixes.circuit.get(Materials.ULV), 'O', ItemList.ULV_Coil, 'L',
                OrePrefixes.cell.get(Materials.Lubricant), 'P', OrePrefixes.rotor.get(Materials.Lead) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Hatch_Dynamo_LV.get(1L),
            bitsd,
            new Object[] { "XOL", "SMP", "XOL", 'M', ItemList.Hull_LV, 'S', OrePrefixes.spring.get(Materials.Tin), 'X',
                OrePrefixes.circuit.get(Materials.LV), 'O', ItemList.LV_Coil, 'L',
                OrePrefixes.cell.get(Materials.Lubricant), 'P', ItemList.Electric_Pump_LV });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Hatch_Dynamo_MV.get(1L),
            bitsd,
            new Object[] { "XOL", "SMP", "XOL", 'M', ItemList.Hull_MV, 'S', OrePrefixes.spring.get(Materials.Copper),
                'X', ItemList.Circuit_Chip_ULPIC, 'O', ItemList.MV_Coil, 'L', OrePrefixes.cell.get(Materials.Lubricant),
                'P', ItemList.Electric_Pump_MV });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Hatch_Energy_ULV.get(1L),
            bitsd,
            new Object[] { "COL", "XMP", "COL", 'M', ItemList.Hull_ULV, 'C', OrePrefixes.cableGt01.get(Materials.Lead),
                'X', OrePrefixes.circuit.get(Materials.ULV), 'O', ItemList.ULV_Coil, 'L',
                OrePrefixes.cell.get(Materials.Lubricant), 'P', OrePrefixes.rotor.get(Materials.Lead) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Hatch_Energy_LV.get(1L),
            bitsd,
            new Object[] { "COL", "XMP", "COL", 'M', ItemList.Hull_LV, 'C', OrePrefixes.cableGt01.get(Materials.Tin),
                'X', OrePrefixes.circuit.get(Materials.LV), 'O', ItemList.LV_Coil, 'L',
                OrePrefixes.cell.get(Materials.Lubricant), 'P', ItemList.Electric_Pump_LV });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Hatch_Energy_MV.get(1L),
            bitsd,
            new Object[] { "XOL", "CMP", "XOL", 'M', ItemList.Hull_MV, 'C', OrePrefixes.cableGt01.get(Materials.Copper),
                'X', ItemList.Circuit_Chip_ULPIC, 'O', ItemList.MV_Coil, 'L', OrePrefixes.cell.get(Materials.Lubricant),
                'P', ItemList.Electric_Pump_MV });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Hatch_Maintenance.get(1L),
            bitsd,
            new Object[] { "dwx", "hMc", "fsr", 'M', ItemList.Hull_LV });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Hatch_DataAccess_EV.get(1L),
            bitsd,
            new Object[] { "COC", "OMO", "COC", 'M', ItemList.Hull_EV, 'O', ItemList.Tool_DataStick, 'C',
                OrePrefixes.circuit.get(Materials.IV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Hatch_DataAccess_LuV.get(1L),
            bitsd,
            new Object[] { "COC", "OMO", "COC", 'M', ItemList.Hull_LuV, 'O', ItemList.Tool_DataOrb, 'C',
                OrePrefixes.circuit.get(Materials.ZPM) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Hatch_DataAccess_UV.get(1L),
            bitsd,
            new Object[] { "CRC", "OMO", "CRC", 'M', ItemList.Hull_UV, 'O', ItemList.Tool_DataOrb, 'C',
                OrePrefixes.circuit.get(Materials.UHV), 'R', ItemList.Robot_Arm_UV });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Hatch_AutoMaintenance.get(1L),
            bitsd,
            new Object[] { "CHC", "AMA", "CHC", 'M', ItemList.Hull_LuV, 'H', ItemList.Hatch_Maintenance, 'A',
                ItemList.Robot_Arm_LuV, 'C', OrePrefixes.circuit.get(Materials.ZPM) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Hatch_Muffler_LV.get(1L),
            bitsd,
            new Object[] { "MX ", "PR ", 'M', ItemList.Hull_LV, 'P', OrePrefixes.pipeMedium.get(Materials.Bronze), 'R',
                OrePrefixes.rotor.get(Materials.Bronze), 'X', ItemList.Electric_Motor_LV });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Hatch_Muffler_MV.get(1L),
            bitsd,
            new Object[] { "MX ", "PR ", 'M', ItemList.Hull_MV, 'P', OrePrefixes.pipeMedium.get(Materials.Steel), 'R',
                OrePrefixes.rotor.get(Materials.Steel), 'X', ItemList.Electric_Motor_MV });

        // recycling data for the other mufflers, recipes are in assembler
        GT_OreDictUnificator.addItemDataFromInputs(
            ItemList.Hatch_Muffler_HV.get(1L),
            ItemList.Hull_HV.get(1),
            OrePrefixes.pipeLarge.get(Materials.StainlessSteel),
            OrePrefixes.rotor.get(Materials.StainlessSteel),
            ItemList.Electric_Motor_HV.get(1));
        GT_OreDictUnificator.addItemDataFromInputs(
            ItemList.Hatch_Muffler_EV.get(1L),
            ItemList.Hull_EV.get(1),
            OrePrefixes.pipeLarge.get(Materials.Titanium),
            OrePrefixes.rotor.get(Materials.Titanium),
            ItemList.Electric_Motor_EV.get(1));
        GT_OreDictUnificator.addItemDataFromInputs(
            ItemList.Hatch_Muffler_IV.get(1L),
            ItemList.Hull_IV.get(1),
            OrePrefixes.pipeLarge.get(Materials.TungstenSteel),
            OrePrefixes.rotor.get(Materials.TungstenSteel),
            ItemList.Electric_Motor_IV.get(1));
        GT_OreDictUnificator.addItemDataFromInputs(
            ItemList.Hatch_Muffler_LuV.get(1L),
            ItemList.Hull_LuV.get(1),
            OrePrefixes.pipeLarge.get(Materials.Enderium),
            OrePrefixes.rotor.get(Materials.Enderium),
            ItemList.Electric_Motor_LuV.get(1));
        GT_OreDictUnificator.addItemDataFromInputs(
            ItemList.Hatch_Muffler_ZPM.get(1L),
            ItemList.Hull_ZPM.get(1),
            OrePrefixes.pipeLarge.get(Materials.Naquadah),
            OrePrefixes.rotor.get(Materials.NaquadahAlloy),
            ItemList.Electric_Motor_ZPM.get(1));
        GT_OreDictUnificator.addItemDataFromInputs(
            ItemList.Hatch_Muffler_UV.get(1L),
            ItemList.Hull_UV.get(1),
            OrePrefixes.pipeLarge.get(Materials.NetherStar),
            OrePrefixes.rotor.get(Materials.Neutronium),
            ItemList.Electric_Motor_UV.get(1));
        GT_OreDictUnificator.addItemDataFromInputs(
            ItemList.Hatch_Muffler_MAX.get(1L),
            ItemList.Hull_MAX.get(1),
            OrePrefixes.pipeLarge.get(Materials.MysteriousCrystal),
            OrePrefixes.rotor.get(Materials.CosmicNeutronium),
            ItemList.Electric_Motor_UHV.get(1));

        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_Bronze_Boiler.get(1L),
            bitsd,
            new Object[] { aTextPlate, "PwP", "BFB", 'F', OreDictNames.craftingIronFurnace, 'P',
                OrePrefixes.plate.get(Materials.Bronze), 'B', new ItemStack(Blocks.brick_block, 1) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_Steel_Boiler.get(1L),
            bitsd,
            new Object[] { aTextPlate, "PwP", "BFB", 'F', OreDictNames.craftingIronFurnace, 'P',
                OrePrefixes.plate.get(Materials.Steel), 'B', new ItemStack(Blocks.brick_block, 1) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_Steel_Boiler_Lava.get(1L),
            bitsd,
            new Object[] { aTextPlate, "PTP", aTextPlateMotor, 'M', ItemList.Hull_HP, 'P',
                OrePrefixes.plate.get(Materials.Steel), 'T',
                GT_ModHandler.getModItem(BuildCraftFactory.ID, "tankBlock", 1L, 0) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_Bronze_Boiler_Solar.get(1L),
            bitsd,
            new Object[] { "GGG", "SSS", aTextPlateMotor, 'M', ItemList.Hull_Bronze_Bricks, 'P',
                OrePrefixes.pipeSmall.get(Materials.Bronze), 'S', OrePrefixes.plateDouble.get(Materials.Silver), 'G',
                new ItemStack(Blocks.glass, 1) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_HP_Solar.get(1L),
            bitsd,
            new Object[] { "GGG", "SSS", aTextPlateMotor, 'M', ItemList.Hull_HP_Bricks, 'P',
                OrePrefixes.pipeSmall.get(Materials.Steel), 'S', OrePrefixes.plateTriple.get(Materials.Silver), 'G',
                GT_ModHandler.getModItem(IndustrialCraft2.ID, "blockAlloyGlass", 1L) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_Bronze_Furnace.get(1L),
            bitsd,
            new Object[] { "XXX", "XMX", "XFX", 'M', ItemList.Hull_Bronze_Bricks, 'X',
                OrePrefixes.pipeSmall.get(Materials.Bronze), 'F', OreDictNames.craftingFurnace });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_HP_Furnace.get(1L),
            bitsd,
            new Object[] { "XSX", "PMP", "XXX", 'M', ItemList.Machine_Bronze_Furnace, 'X',
                OrePrefixes.pipeSmall.get(Materials.WroughtIron), 'P', OrePrefixes.plate.get(Materials.WroughtIron),
                'S', OrePrefixes.plate.get(Materials.Steel) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_Bronze_Macerator.get(1L),
            bitsd,
            new Object[] { "DXD", "XMX", "PXP", 'M', ItemList.Hull_Bronze, 'X',
                OrePrefixes.pipeSmall.get(Materials.Bronze), 'P', OreDictNames.craftingPiston, 'D',
                OrePrefixes.gem.get(Materials.Diamond) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_HP_Macerator.get(1L),
            bitsd,
            new Object[] { "PSP", "XMX", "PPP", 'M', ItemList.Machine_Bronze_Macerator, 'X',
                OrePrefixes.pipeSmall.get(Materials.WroughtIron), 'P', OrePrefixes.plate.get(Materials.WroughtIron),
                'S', OrePrefixes.plate.get(Materials.Steel) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_Bronze_Extractor.get(1L),
            bitsd,
            new Object[] { "XXX", "PMG", "XXX", 'M', ItemList.Hull_Bronze, 'X',
                OrePrefixes.pipeSmall.get(Materials.Bronze), 'P', OreDictNames.craftingPiston, 'G',
                new ItemStack(Blocks.glass, 1) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_HP_Extractor.get(1L),
            bitsd,
            new Object[] { "XSX", "PMP", "XXX", 'M', ItemList.Machine_Bronze_Extractor, 'X',
                OrePrefixes.pipeSmall.get(Materials.WroughtIron), 'P', OrePrefixes.plate.get(Materials.WroughtIron),
                'S', OrePrefixes.plate.get(Materials.Steel) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_Bronze_Hammer.get(1L),
            bitsd,
            new Object[] { "XPX", "XMX", "XAX", 'M', ItemList.Hull_Bronze, 'X',
                OrePrefixes.pipeSmall.get(Materials.Bronze), 'P', OreDictNames.craftingPiston, 'A',
                OreDictNames.craftingAnvil });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_HP_Hammer.get(1L),
            bitsd,
            new Object[] { "PSP", "XMX", "PPP", 'M', ItemList.Machine_Bronze_Hammer, 'X',
                OrePrefixes.pipeSmall.get(Materials.WroughtIron), 'P', OrePrefixes.plate.get(Materials.WroughtIron),
                'S', OrePrefixes.plate.get(Materials.Steel) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_Bronze_Compressor.get(1L),
            bitsd,
            new Object[] { "XXX", aTextPlateMotor, "XXX", 'M', ItemList.Hull_Bronze, 'X',
                OrePrefixes.pipeSmall.get(Materials.Bronze), 'P', OreDictNames.craftingPiston });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_HP_Compressor.get(1L),
            bitsd,
            new Object[] { "XSX", "PMP", "XXX", 'M', ItemList.Machine_Bronze_Compressor, 'X',
                OrePrefixes.pipeSmall.get(Materials.WroughtIron), 'P', OrePrefixes.plate.get(Materials.WroughtIron),
                'S', OrePrefixes.plate.get(Materials.Steel) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_Bronze_AlloySmelter.get(1L),
            bitsd,
            new Object[] { "XXX", "FMF", "XXX", 'M', ItemList.Hull_Bronze_Bricks, 'X',
                OrePrefixes.pipeSmall.get(Materials.Bronze), 'F', OreDictNames.craftingFurnace });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_HP_AlloySmelter.get(1L),
            bitsd,
            new Object[] { "PSP", "PMP", "PXP", 'M', ItemList.Machine_Bronze_AlloySmelter, 'X',
                OrePrefixes.pipeSmall.get(Materials.WroughtIron), 'P', OrePrefixes.plate.get(Materials.WroughtIron),
                'S', OrePrefixes.plate.get(Materials.Steel) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_1by1_ULV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_ULV, 'W',
                OrePrefixes.wireGt01.get(Materials.Lead), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_1by1_LV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_LV, 'W',
                OrePrefixes.wireGt01.get(Materials.Tin), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_1by1_MV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_MV, 'W',
                OrePrefixes.wireGt01.get(Materials.AnyCopper), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_1by1_HV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_HV, 'W',
                OrePrefixes.wireGt01.get(Materials.Gold), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_1by1_EV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_EV, 'W',
                OrePrefixes.wireGt01.get(Materials.Aluminium), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_1by1_IV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_IV, 'W',
                OrePrefixes.wireGt01.get(Materials.Tungsten), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_1by1_LuV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_LuV, 'W',
                OrePrefixes.wireGt01.get(Materials.VanadiumGallium), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_1by1_ZPM.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_ZPM, 'W',
                OrePrefixes.wireGt01.get(Materials.Naquadah), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_1by1_UV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_UV, 'W',
                OrePrefixes.wireGt01.get(Materials.NaquadahAlloy), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_1by1_UHV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_MAX, 'W',
                OrePrefixes.wireGt01.get(Materials.SuperconductorUHV), 'T', OreDictNames.craftingChest });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_2by2_ULV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_ULV, 'W',
                OrePrefixes.wireGt04.get(Materials.Lead), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_2by2_LV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_LV, 'W',
                OrePrefixes.wireGt04.get(Materials.Tin), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_2by2_MV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_MV, 'W',
                OrePrefixes.wireGt04.get(Materials.AnyCopper), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_2by2_HV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_HV, 'W',
                OrePrefixes.wireGt04.get(Materials.Gold), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_2by2_EV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_EV, 'W',
                OrePrefixes.wireGt04.get(Materials.Aluminium), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_2by2_IV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_IV, 'W',
                OrePrefixes.wireGt04.get(Materials.Tungsten), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_2by2_LuV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_LuV, 'W',
                OrePrefixes.wireGt04.get(Materials.VanadiumGallium), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_2by2_ZPM.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_ZPM, 'W',
                OrePrefixes.wireGt04.get(Materials.Naquadah), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_2by2_UV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_UV, 'W',
                OrePrefixes.wireGt04.get(Materials.NaquadahAlloy), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_2by2_UHV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_MAX, 'W',
                OrePrefixes.wireGt04.get(Materials.SuperconductorUHV), 'T', OreDictNames.craftingChest });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_3by3_ULV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_ULV, 'W',
                OrePrefixes.wireGt08.get(Materials.Lead), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_3by3_LV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_LV, 'W',
                OrePrefixes.wireGt08.get(Materials.Tin), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_3by3_MV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_MV, 'W',
                OrePrefixes.wireGt08.get(Materials.AnyCopper), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_3by3_HV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_HV, 'W',
                OrePrefixes.wireGt08.get(Materials.Gold), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_3by3_EV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_EV, 'W',
                OrePrefixes.wireGt08.get(Materials.Aluminium), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_3by3_IV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_IV, 'W',
                OrePrefixes.wireGt08.get(Materials.Tungsten), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_3by3_LuV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_LuV, 'W',
                OrePrefixes.wireGt08.get(Materials.VanadiumGallium), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_3by3_ZPM.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_ZPM, 'W',
                OrePrefixes.wireGt08.get(Materials.Naquadah), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_3by3_UV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_UV, 'W',
                OrePrefixes.wireGt08.get(Materials.NaquadahAlloy), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_3by3_UHV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_MAX, 'W',
                OrePrefixes.wireGt08.get(Materials.SuperconductorUHV), 'T', OreDictNames.craftingChest });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_4by4_ULV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_ULV, 'W',
                OrePrefixes.wireGt16.get(Materials.Lead), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_4by4_LV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_LV, 'W',
                OrePrefixes.wireGt16.get(Materials.Tin), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_4by4_MV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_MV, 'W',
                OrePrefixes.wireGt16.get(Materials.AnyCopper), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_4by4_HV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_HV, 'W',
                OrePrefixes.wireGt16.get(Materials.Gold), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_4by4_EV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_EV, 'W',
                OrePrefixes.wireGt16.get(Materials.Aluminium), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_4by4_IV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_IV, 'W',
                OrePrefixes.wireGt16.get(Materials.Tungsten), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_4by4_LuV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_LuV, 'W',
                OrePrefixes.wireGt16.get(Materials.VanadiumGallium), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_4by4_ZPM.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_ZPM, 'W',
                OrePrefixes.wireGt16.get(Materials.Naquadah), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_4by4_UV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_UV, 'W',
                OrePrefixes.wireGt16.get(Materials.NaquadahAlloy), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_4by4_UHV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_MAX, 'W',
                OrePrefixes.wireGt16.get(Materials.SuperconductorUHV), 'T', OreDictNames.craftingChest });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Charger_4by4_ULV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, "BCB", 'M', ItemList.Hull_ULV, 'W',
                OrePrefixes.wireGt16.get(Materials.Lead), 'T', OreDictNames.craftingChest, 'B',
                ItemList.Battery_RE_ULV_Tantalum, 'C', OrePrefixes.circuit.get(Materials.ULV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Charger_4by4_LV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, "BCB", 'M', ItemList.Hull_LV, 'W',
                OrePrefixes.wireGt16.get(Materials.Tin), 'T', OreDictNames.craftingChest, 'B',
                ItemList.Battery_RE_LV_Lithium, 'C', OrePrefixes.circuit.get(Materials.LV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Charger_4by4_MV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, "BCB", 'M', ItemList.Hull_MV, 'W',
                OrePrefixes.wireGt16.get(Materials.AnyCopper), 'T', OreDictNames.craftingChest, 'B',
                ItemList.Battery_RE_MV_Lithium, 'C', OrePrefixes.circuit.get(Materials.MV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Charger_4by4_HV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, "BCB", 'M', ItemList.Hull_HV, 'W',
                OrePrefixes.wireGt16.get(Materials.Gold), 'T', OreDictNames.craftingChest, 'B',
                ItemList.Battery_RE_HV_Lithium, 'C', OrePrefixes.circuit.get(Materials.HV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Charger_4by4_EV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, "BCB", 'M', ItemList.Hull_EV, 'W',
                OrePrefixes.wireGt16.get(Materials.Aluminium), 'T', OreDictNames.craftingChest, 'B',
                OrePrefixes.battery.get(Materials.EV), 'C', OrePrefixes.circuit.get(Materials.EV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Charger_4by4_IV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, "BCB", 'M', ItemList.Hull_IV, 'W',
                OrePrefixes.wireGt16.get(Materials.Tungsten), 'T', OreDictNames.craftingChest, 'B',
                OrePrefixes.battery.get(Materials.IV), 'C', OrePrefixes.circuit.get(Materials.IV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Charger_4by4_LuV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, "BCB", 'M', ItemList.Hull_LuV, 'W',
                OrePrefixes.wireGt16.get(Materials.VanadiumGallium), 'T', OreDictNames.craftingChest, 'B',
                OrePrefixes.battery.get(Materials.LuV), 'C', OrePrefixes.circuit.get(Materials.LuV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Charger_4by4_ZPM.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, "BCB", 'M', ItemList.Hull_ZPM, 'W',
                OrePrefixes.wireGt16.get(Materials.Naquadah), 'T', OreDictNames.craftingChest, 'B',
                OrePrefixes.battery.get(Materials.ZPM), 'C', OrePrefixes.circuit.get(Materials.ZPM) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Charger_4by4_UV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, "BCB", 'M', ItemList.Hull_UV, 'W',
                OrePrefixes.wireGt16.get(Materials.NaquadahAlloy), 'T', OreDictNames.craftingChest, 'B', ItemList.ZPM2,
                'C', OrePrefixes.circuit.get(Materials.UV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Charger_4by4_UHV.get(1L),
            bitsd,
            new Object[] { aTextWireChest, aTextWireHull, "BCB", 'M', ItemList.Hull_MAX, 'W',
                OrePrefixes.wireGt16.get(Materials.SuperconductorUHV), 'T', OreDictNames.craftingChest, 'B',
                ItemList.ZPM2, 'C', OrePrefixes.circuit.get(Materials.UHV) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Locker_ULV.get(1L),
            bitsd,
            new Object[] { "T", "M", 'M', ItemList.Battery_Buffer_2by2_ULV, 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Locker_LV.get(1L),
            bitsd,
            new Object[] { "T", "M", 'M', ItemList.Battery_Buffer_2by2_LV, 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Locker_MV.get(1L),
            bitsd,
            new Object[] { "T", "M", 'M', ItemList.Battery_Buffer_2by2_MV, 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Locker_HV.get(1L),
            bitsd,
            new Object[] { "T", "M", 'M', ItemList.Battery_Buffer_2by2_HV, 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Locker_EV.get(1L),
            bitsd,
            new Object[] { "T", "M", 'M', ItemList.Battery_Buffer_2by2_EV, 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Locker_IV.get(1L),
            bitsd,
            new Object[] { "T", "M", 'M', ItemList.Battery_Buffer_2by2_IV, 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Locker_LuV.get(1L),
            bitsd,
            new Object[] { "T", "M", 'M', ItemList.Battery_Buffer_2by2_LuV, 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Locker_ZPM.get(1L),
            bitsd,
            new Object[] { "T", "M", 'M', ItemList.Battery_Buffer_2by2_ZPM, 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Locker_UV.get(1L),
            bitsd,
            new Object[] { "T", "M", 'M', ItemList.Battery_Buffer_2by2_UV, 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Locker_MAX.get(1L),
            bitsd,
            new Object[] { "T", "M", 'M', ItemList.Battery_Buffer_2by2_UHV, 'T', OreDictNames.craftingChest });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_LV_Scanner.get(1L),
            bitsd,
            new Object[] { "CTC", aTextWireHull, "CRC", 'M', ItemList.Hull_LV, 'T', ItemList.Emitter_LV, 'R',
                ItemList.Sensor_LV, 'C', OrePrefixes.circuit.get(Materials.MV), 'W',
                OrePrefixes.cableGt01.get(Materials.Tin) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_MV_Scanner.get(1L),
            bitsd,
            new Object[] { "CTC", aTextWireHull, "CRC", 'M', ItemList.Hull_MV, 'T', ItemList.Emitter_MV, 'R',
                ItemList.Sensor_MV, 'C', OrePrefixes.circuit.get(Materials.HV), 'W',
                OrePrefixes.cableGt01.get(Materials.AnyCopper) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_HV_Scanner.get(1L),
            bitsd,
            new Object[] { "CTC", aTextWireHull, "CRC", 'M', ItemList.Hull_HV, 'T', ItemList.Emitter_HV, 'R',
                ItemList.Sensor_HV, 'C', OrePrefixes.circuit.get(Materials.EV), 'W',
                OrePrefixes.cableGt01.get(Materials.Gold) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_EV_Scanner.get(1L),
            bitsd,
            new Object[] { "CTC", aTextWireHull, "CRC", 'M', ItemList.Hull_EV, 'T', ItemList.Emitter_EV, 'R',
                ItemList.Sensor_EV, 'C', OrePrefixes.circuit.get(Materials.IV), 'W',
                OrePrefixes.cableGt01.get(Materials.Aluminium) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_IV_Scanner.get(1L),
            bitsd,
            new Object[] { "CTC", aTextWireHull, "CRC", 'M', ItemList.Hull_IV, 'T', ItemList.Emitter_IV, 'R',
                ItemList.Sensor_IV, 'C', OrePrefixes.circuit.get(Materials.LuV), 'W',
                OrePrefixes.cableGt01.get(Materials.Tungsten) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_LV_Boxinator.get(1L),
            bitsd,
            new Object[] { "BCB", "RMV", aTextWireCoil, 'M', ItemList.Hull_LV, 'R', ItemList.Robot_Arm_LV, 'V',
                ItemList.Conveyor_Module_LV, 'C', OrePrefixes.circuit.get(Materials.LV), 'W',
                OrePrefixes.cableGt01.get(Materials.Tin), 'B', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_MV_Boxinator.get(1L),
            bitsd,
            new Object[] { "BCB", "RMV", aTextWireCoil, 'M', ItemList.Hull_MV, 'R', ItemList.Robot_Arm_MV, 'V',
                ItemList.Conveyor_Module_MV, 'C', OrePrefixes.circuit.get(Materials.MV), 'W',
                OrePrefixes.cableGt01.get(Materials.AnyCopper), 'B', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_HV_Boxinator.get(1L),
            bitsd,
            new Object[] { "BCB", "RMV", aTextWireCoil, 'M', ItemList.Hull_HV, 'R', ItemList.Robot_Arm_HV, 'V',
                ItemList.Conveyor_Module_HV, 'C', OrePrefixes.circuit.get(Materials.HV), 'W',
                OrePrefixes.cableGt01.get(Materials.Gold), 'B', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_EV_Boxinator.get(1L),
            bitsd,
            new Object[] { "BCB", "RMV", aTextWireCoil, 'M', ItemList.Hull_EV, 'R', ItemList.Robot_Arm_EV, 'V',
                ItemList.Conveyor_Module_EV, 'C', OrePrefixes.circuit.get(Materials.EV), 'W',
                OrePrefixes.cableGt01.get(Materials.Aluminium), 'B', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_IV_Boxinator.get(1L),
            bitsd,
            new Object[] { "BCB", "RMV", aTextWireCoil, 'M', ItemList.Hull_IV, 'R', ItemList.Robot_Arm_IV, 'V',
                ItemList.Conveyor_Module_IV, 'C', OrePrefixes.circuit.get(Materials.IV), 'W',
                OrePrefixes.cableGt01.get(Materials.Tungsten), 'B', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_LuV_Boxinator.get(1L),
            bitsd,
            new Object[] { "BCB", "RMV", aTextWireCoil, 'M', ItemList.Hull_LuV, 'R', ItemList.Robot_Arm_LuV, 'V',
                ItemList.Conveyor_Module_LuV, 'C', OrePrefixes.circuit.get(Materials.LuV), 'W',
                OrePrefixes.cableGt01.get(Materials.VanadiumGallium), 'B', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_ZPM_Boxinator.get(1L),
            bitsd,
            new Object[] { "BCB", "RMV", aTextWireCoil, 'M', ItemList.Hull_ZPM, 'R', ItemList.Robot_Arm_ZPM, 'V',
                ItemList.Conveyor_Module_ZPM, 'C', OrePrefixes.circuit.get(Materials.ZPM), 'W',
                OrePrefixes.cableGt01.get(Materials.Naquadah), 'B', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_UV_Boxinator.get(1L),
            bitsd,
            new Object[] { "BCB", "RMV", aTextWireCoil, 'M', ItemList.Hull_UV, 'R', ItemList.Robot_Arm_UV, 'V',
                ItemList.Conveyor_Module_UV, 'C', OrePrefixes.circuit.get(Materials.UV), 'W',
                OrePrefixes.cableGt01.get(Materials.NaquadahAlloy), 'B', OreDictNames.craftingChest });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_LV_RockBreaker.get(1L),
            bitsd,
            new Object[] { "PED", aTextWireHull, "GGG", 'M', ItemList.Hull_LV, 'D', OreDictNames.craftingGrinder, 'E',
                ItemList.Electric_Motor_LV, 'P', ItemList.Electric_Piston_LV, 'C',
                OrePrefixes.circuit.get(Materials.LV), 'W', OrePrefixes.cableGt01.get(Materials.Tin), 'G',
                new ItemStack(Blocks.glass, 1) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_MV_RockBreaker.get(1L),
            bitsd,
            new Object[] { "PED", aTextWireHull, "GGG", 'M', ItemList.Hull_MV, 'D', OreDictNames.craftingGrinder, 'E',
                ItemList.Electric_Motor_MV, 'P', ItemList.Electric_Piston_MV, 'C',
                OrePrefixes.circuit.get(Materials.MV), 'W', OrePrefixes.cableGt01.get(Materials.AnyCopper), 'G',
                new ItemStack(Blocks.glass, 1) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_HV_RockBreaker.get(1L),
            bitsd,
            new Object[] { "PED", aTextWireHull, "GGG", 'M', ItemList.Hull_HV, 'D', OreDictNames.craftingGrinder, 'E',
                ItemList.Electric_Motor_HV, 'P', ItemList.Electric_Piston_HV, 'C',
                OrePrefixes.circuit.get(Materials.HV), 'W', OrePrefixes.cableGt01.get(Materials.Gold), 'G',
                new ItemStack(Blocks.glass, 1) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_EV_RockBreaker.get(1L),
            bitsd,
            new Object[] { "PED", aTextWireHull, "GGG", 'M', ItemList.Hull_EV, 'D', OreDictNames.craftingGrinder, 'E',
                ItemList.Electric_Motor_EV, 'P', ItemList.Electric_Piston_EV, 'C',
                OrePrefixes.circuit.get(Materials.EV), 'W', OrePrefixes.cableGt01.get(Materials.Aluminium), 'G',
                new ItemStack(Blocks.glass, 1) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_IV_RockBreaker.get(1L),
            bitsd,
            new Object[] { "PED", aTextWireHull, "GGG", 'M', ItemList.Hull_IV, 'D', OreDictNames.craftingGrinder, 'E',
                ItemList.Electric_Motor_IV, 'P', ItemList.Electric_Piston_IV, 'C',
                OrePrefixes.circuit.get(Materials.IV), 'W', OrePrefixes.cableGt01.get(Materials.Tungsten), 'G',
                new ItemStack(Blocks.glass, 1) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_LV_Massfab.get(1L),
            bitsd,
            new Object[] { "CFC", aTextWireHull, "CFC", 'M', ItemList.Hull_LV, 'F', ItemList.Field_Generator_LV, 'C',
                OrePrefixes.circuit.get(Materials.MV), 'W', OrePrefixes.cableGt04.get(Materials.Tin) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_MV_Massfab.get(1L),
            bitsd,
            new Object[] { "CFC", aTextWireHull, "CFC", 'M', ItemList.Hull_MV, 'F', ItemList.Field_Generator_MV, 'C',
                OrePrefixes.circuit.get(Materials.HV), 'W', OrePrefixes.cableGt04.get(Materials.AnyCopper) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_HV_Massfab.get(1L),
            bitsd,
            new Object[] { "CFC", aTextWireHull, "CFC", 'M', ItemList.Hull_HV, 'F', ItemList.Field_Generator_HV, 'C',
                OrePrefixes.circuit.get(Materials.EV), 'W', OrePrefixes.cableGt04.get(Materials.Gold) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_EV_Massfab.get(1L),
            bitsd,
            new Object[] { "CFC", aTextWireHull, "CFC", 'M', ItemList.Hull_EV, 'F', ItemList.Field_Generator_EV, 'C',
                OrePrefixes.circuit.get(Materials.IV), 'W', OrePrefixes.cableGt04.get(Materials.Aluminium) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_IV_Massfab.get(1L),
            bitsd,
            new Object[] { "CFC", aTextWireHull, "CFC", 'M', ItemList.Hull_IV, 'F', ItemList.Field_Generator_IV, 'C',
                OrePrefixes.circuit.get(Materials.LuV), 'W', OrePrefixes.cableGt04.get(Materials.Tungsten) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_LV_Replicator.get(1L),
            bitsd,
            new Object[] { "EFE", aTextCableHull, aTextMotorWire, 'M', ItemList.Hull_LV, 'F',
                ItemList.Field_Generator_LV, 'E', ItemList.Emitter_LV, 'C', OrePrefixes.circuit.get(Materials.MV), 'W',
                OrePrefixes.cableGt04.get(Materials.Tin) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_MV_Replicator.get(1L),
            bitsd,
            new Object[] { "EFE", aTextCableHull, aTextMotorWire, 'M', ItemList.Hull_MV, 'F',
                ItemList.Field_Generator_MV, 'E', ItemList.Emitter_MV, 'C', OrePrefixes.circuit.get(Materials.HV), 'W',
                OrePrefixes.cableGt04.get(Materials.AnyCopper) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_HV_Replicator.get(1L),
            bitsd,
            new Object[] { "EFE", aTextCableHull, aTextMotorWire, 'M', ItemList.Hull_HV, 'F',
                ItemList.Field_Generator_HV, 'E', ItemList.Emitter_HV, 'C', OrePrefixes.circuit.get(Materials.EV), 'W',
                OrePrefixes.cableGt04.get(Materials.Gold) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_EV_Replicator.get(1L),
            bitsd,
            new Object[] { "EFE", aTextCableHull, aTextMotorWire, 'M', ItemList.Hull_EV, 'F',
                ItemList.Field_Generator_EV, 'E', ItemList.Emitter_EV, 'C', OrePrefixes.circuit.get(Materials.IV), 'W',
                OrePrefixes.cableGt04.get(Materials.Aluminium) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_IV_Replicator.get(1L),
            bitsd,
            new Object[] { "EFE", aTextCableHull, aTextMotorWire, 'M', ItemList.Hull_IV, 'F',
                ItemList.Field_Generator_IV, 'E', ItemList.Emitter_IV, 'C', OrePrefixes.circuit.get(Materials.LuV), 'W',
                OrePrefixes.cableGt04.get(Materials.Tungsten) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_LV_Brewery.get(1L),
            bitsd,
            new Object[] { "GPG", aTextWireHull, "CBC", 'M', ItemList.Hull_LV, 'P', ItemList.Electric_Pump_LV, 'B',
                new ItemStack(Items.brewing_stand, 0), 'C', OrePrefixes.circuit.get(Materials.LV), 'W',
                OrePrefixes.cableGt01.get(Materials.Tin), 'G', new ItemStack(Blocks.glass, 1) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_MV_Brewery.get(1L),
            bitsd,
            new Object[] { "GPG", aTextWireHull, "CBC", 'M', ItemList.Hull_MV, 'P', ItemList.Electric_Pump_MV, 'B',
                new ItemStack(Items.brewing_stand, 0), 'C', OrePrefixes.circuit.get(Materials.MV), 'W',
                OrePrefixes.cableGt01.get(Materials.AnyCopper), 'G', new ItemStack(Blocks.glass, 1) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_HV_Brewery.get(1L),
            bitsd,
            new Object[] { "GPG", aTextWireHull, "CBC", 'M', ItemList.Hull_HV, 'P', ItemList.Electric_Pump_HV, 'B',
                new ItemStack(Items.brewing_stand, 0), 'C', OrePrefixes.circuit.get(Materials.HV), 'W',
                OrePrefixes.cableGt01.get(Materials.Gold), 'G', new ItemStack(Blocks.glass, 1) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_EV_Brewery.get(1L),
            bitsd,
            new Object[] { "GPG", aTextWireHull, "CBC", 'M', ItemList.Hull_EV, 'P', ItemList.Electric_Pump_EV, 'B',
                new ItemStack(Items.brewing_stand, 0), 'C', OrePrefixes.circuit.get(Materials.EV), 'W',
                OrePrefixes.cableGt01.get(Materials.Aluminium), 'G', new ItemStack(Blocks.glass, 1) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_IV_Brewery.get(1L),
            bitsd,
            new Object[] { "GPG", aTextWireHull, "CBC", 'M', ItemList.Hull_IV, 'P', ItemList.Electric_Pump_IV, 'B',
                new ItemStack(Items.brewing_stand, 0), 'C', OrePrefixes.circuit.get(Materials.IV), 'W',
                OrePrefixes.cableGt01.get(Materials.Tungsten), 'G', new ItemStack(Blocks.glass, 1) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_LV_Miner.get(1L),
            bitsd,
            new Object[] { "EEE", aTextWireHull, "CSC", 'M', ItemList.Hull_LV, 'E', ItemList.Electric_Motor_LV, 'C',
                OrePrefixes.circuit.get(Materials.LV), 'W', OrePrefixes.cableGt01.get(Materials.Tin), 'S',
                ItemList.Sensor_LV });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_MV_Miner.get(1L),
            bitsd,
            new Object[] { "PEP", aTextWireHull, "CSC", 'M', ItemList.Hull_MV, 'E', ItemList.Electric_Motor_MV, 'P',
                ItemList.Electric_Piston_MV, 'C', OrePrefixes.circuit.get(Materials.MV), 'W',
                OrePrefixes.cableGt02.get(Materials.Copper), 'S', ItemList.Sensor_MV });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_HV_Miner.get(1L),
            bitsd,
            new Object[] { "RPR", aTextWireHull, "CSC", 'M', ItemList.Hull_HV, 'E', ItemList.Electric_Motor_HV, 'P',
                ItemList.Electric_Piston_HV, 'R', ItemList.Robot_Arm_HV, 'C', OrePrefixes.circuit.get(Materials.HV),
                'W', OrePrefixes.cableGt04.get(Materials.Gold), 'S', ItemList.Sensor_HV });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_Multi_BlastFurnace.get(1L),
            bitsd,
            new Object[] { "FFF", aTextCableHull, aTextWireCoil, 'M', ItemList.Casing_HeatProof, 'F',
                OreDictNames.craftingIronFurnace, 'C', OrePrefixes.circuit.get(Materials.LV), 'W',
                OrePrefixes.cableGt01.get(Materials.Tin) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_Multi_VacuumFreezer.get(1L),
            bitsd,
            new Object[] { aTextPlate, aTextCableHull, aTextWireCoil, 'M', ItemList.Casing_FrostProof, 'P',
                ItemList.Electric_Pump_HV, 'C', OrePrefixes.circuit.get(Materials.EV), 'W',
                OrePrefixes.cableGt01.get(Materials.Gold) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_Multi_ImplosionCompressor.get(1L),
            bitsd,
            new Object[] { "OOO", aTextCableHull, aTextWireCoil, 'M', ItemList.Casing_SolidSteel, 'O',
                Ic2Items.reinforcedStone, 'C', OrePrefixes.circuit.get(Materials.HV), 'W',
                OrePrefixes.cableGt01.get(Materials.Gold) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_Multi_Furnace.get(1L),
            bitsd,
            new Object[] { "FFF", aTextCableHull, aTextWireCoil, 'M', ItemList.Casing_HeatProof, 'F',
                OreDictNames.craftingIronFurnace, 'C', OrePrefixes.circuit.get(Materials.HV), 'W',
                OrePrefixes.cableGt01.get(Materials.AnnealedCopper) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_Multi_LargeBoiler_Bronze.get(1L),
            bitsd,
            new Object[] { aTextWireCoil, aTextCableHull, aTextWireCoil, 'M', ItemList.Casing_Firebox_Bronze, 'C',
                OrePrefixes.circuit.get(Materials.MV), 'W', OrePrefixes.cableGt01.get(Materials.Tin) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_Multi_LargeBoiler_Steel.get(1L),
            bitsd,
            new Object[] { aTextWireCoil, aTextCableHull, aTextWireCoil, 'M', ItemList.Casing_Firebox_Steel, 'C',
                OrePrefixes.circuit.get(Materials.HV), 'W', OrePrefixes.cableGt01.get(Materials.AnyCopper) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_Multi_LargeBoiler_Titanium.get(1L),
            bitsd,
            new Object[] { aTextWireCoil, aTextCableHull, aTextWireCoil, 'M', ItemList.Casing_Firebox_Titanium, 'C',
                OrePrefixes.circuit.get(Materials.EV), 'W', OrePrefixes.cableGt01.get(Materials.Gold) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_Multi_LargeBoiler_TungstenSteel.get(1L),
            bitsd,
            new Object[] { aTextWireCoil, aTextCableHull, aTextWireCoil, 'M', ItemList.Casing_Firebox_TungstenSteel,
                'C', OrePrefixes.circuit.get(Materials.IV), 'W', OrePrefixes.cableGt01.get(Materials.Aluminium) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Generator_Diesel_LV.get(1L),
            bitsd,
            new Object[] { "PCP", "EME", "GWG", 'M', ItemList.Hull_LV, 'P', ItemList.Electric_Piston_LV, 'E',
                ItemList.Electric_Motor_LV, 'C', OrePrefixes.circuit.get(Materials.LV), 'W',
                OrePrefixes.cableGt01.get(Materials.Tin), 'G', OrePrefixes.gearGt.get(Materials.Steel) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Generator_Diesel_MV.get(1L),
            bitsd,
            new Object[] { "PCP", "EME", "GWG", 'M', ItemList.Hull_MV, 'P', ItemList.Electric_Piston_MV, 'E',
                ItemList.Electric_Motor_MV, 'C', OrePrefixes.circuit.get(Materials.MV), 'W',
                OrePrefixes.cableGt01.get(Materials.AnyCopper), 'G', OrePrefixes.gearGt.get(Materials.Aluminium) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Generator_Diesel_HV.get(1L),
            bitsd,
            new Object[] { "PCP", "EME", "GWG", 'M', ItemList.Hull_HV, 'P', ItemList.Electric_Piston_HV, 'E',
                ItemList.Electric_Motor_HV, 'C', OrePrefixes.circuit.get(Materials.HV), 'W',
                OrePrefixes.cableGt01.get(Materials.Gold), 'G', OrePrefixes.gearGt.get(Materials.StainlessSteel) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Generator_Gas_Turbine_LV.get(1L),
            bitsd,
            new Object[] { "CRC", "RMR", aTextMotorWire, 'M', ItemList.Hull_LV, 'E', ItemList.Electric_Motor_LV, 'R',
                OrePrefixes.rotor.get(Materials.Tin), 'C', OrePrefixes.circuit.get(Materials.LV), 'W',
                OrePrefixes.cableGt01.get(Materials.Tin) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Generator_Gas_Turbine_MV.get(1L),
            bitsd,
            new Object[] { "CRC", "RMR", aTextMotorWire, 'M', ItemList.Hull_MV, 'E', ItemList.Electric_Motor_MV, 'R',
                OrePrefixes.rotor.get(Materials.Bronze), 'C', OrePrefixes.circuit.get(Materials.MV), 'W',
                OrePrefixes.cableGt01.get(Materials.AnyCopper) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Generator_Gas_Turbine_HV.get(1L),
            bitsd,
            new Object[] { "CRC", "RMR", aTextMotorWire, 'M', ItemList.Hull_HV, 'E', ItemList.Electric_Motor_HV, 'R',
                OrePrefixes.rotor.get(Materials.Steel), 'C', OrePrefixes.circuit.get(Materials.HV), 'W',
                OrePrefixes.cableGt01.get(Materials.Gold) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Generator_Gas_Turbine_EV.get(1L),
            bitsd,
            new Object[] { "CRC", "RMR", aTextMotorWire, 'M', ItemList.Hull_EV, 'E', ItemList.Electric_Motor_EV, 'R',
                OrePrefixes.rotor.get(Materials.Titanium), 'C', OrePrefixes.circuit.get(Materials.EV), 'W',
                OrePrefixes.cableGt01.get(Materials.Aluminium) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Generator_Gas_Turbine_IV.get(1L),
            bitsd,
            new Object[] { "CRC", "RMR", aTextMotorWire, 'M', ItemList.Hull_IV, 'E', ItemList.Electric_Motor_IV, 'R',
                OrePrefixes.rotor.get(Materials.TungstenSteel), 'C', OrePrefixes.circuit.get(Materials.IV), 'W',
                OrePrefixes.cableGt01.get(Materials.Tungsten) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Generator_Steam_Turbine_LV.get(1L),
            bitsd,
            new Object[] { "PCP", "RMR", aTextMotorWire, 'M', ItemList.Hull_LV, 'E', ItemList.Electric_Motor_LV, 'R',
                OrePrefixes.rotor.get(Materials.Tin), 'C', OrePrefixes.circuit.get(Materials.LV), 'W',
                OrePrefixes.cableGt01.get(Materials.Tin), 'P', OrePrefixes.pipeMedium.get(Materials.Bronze) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Generator_Steam_Turbine_MV.get(1L),
            bitsd,
            new Object[] { "PCP", "RMR", aTextMotorWire, 'M', ItemList.Hull_MV, 'E', ItemList.Electric_Motor_MV, 'R',
                OrePrefixes.rotor.get(Materials.Bronze), 'C', OrePrefixes.circuit.get(Materials.MV), 'W',
                OrePrefixes.cableGt01.get(Materials.AnyCopper), 'P', OrePrefixes.pipeMedium.get(Materials.Steel) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Generator_Steam_Turbine_HV.get(1L),
            bitsd,
            new Object[] { "PCP", "RMR", aTextMotorWire, 'M', ItemList.Hull_HV, 'E', ItemList.Electric_Motor_HV, 'R',
                OrePrefixes.rotor.get(Materials.Steel), 'C', OrePrefixes.circuit.get(Materials.HV), 'W',
                OrePrefixes.cableGt01.get(Materials.Gold), 'P', OrePrefixes.pipeMedium.get(Materials.StainlessSteel) });

        if (!Thaumcraft.isModLoaded()) {
            GT_ModHandler.addCraftingRecipe(
                ItemList.MagicEnergyConverter_LV.get(1L),
                bitsd,
                new Object[] { "CTC", "FMF", "CBC", 'M', ItemList.Hull_LV, 'B', new ItemStack(Blocks.beacon), 'C',
                    OrePrefixes.circuit.get(Materials.HV), 'T', ItemList.Field_Generator_LV, 'F',
                    OrePrefixes.plate.get(Materials.Platinum) });
            GT_ModHandler.addCraftingRecipe(
                ItemList.MagicEnergyConverter_MV.get(1L),
                bitsd,
                new Object[] { "CTC", "FMF", "CBC", 'M', ItemList.Hull_MV, 'B', new ItemStack(Blocks.beacon), 'C',
                    OrePrefixes.circuit.get(Materials.EV), 'T', ItemList.Field_Generator_MV, 'F',
                    OrePrefixes.plate.get(Materials.Iridium) });
            GT_ModHandler.addCraftingRecipe(
                ItemList.MagicEnergyConverter_HV.get(1L),
                bitsd,
                new Object[] { "CTC", "FMF", "CBC", 'M', ItemList.Hull_HV, 'B', new ItemStack(Blocks.beacon), 'C',
                    OrePrefixes.circuit.get(Materials.IV), 'T', ItemList.Field_Generator_HV, 'F',
                    OrePrefixes.plate.get(Materials.Neutronium) });

            GT_ModHandler.addCraftingRecipe(
                ItemList.MagicEnergyAbsorber_LV.get(1L),
                bitsd,
                new Object[] { "CTC", "FMF", "CBC", 'M', ItemList.Hull_LV, 'B',
                    ItemList.MagicEnergyConverter_LV.get(1L), 'C', OrePrefixes.circuit.get(Materials.HV), 'T',
                    ItemList.Field_Generator_LV, 'F', OrePrefixes.plate.get(Materials.Platinum) });
            GT_ModHandler.addCraftingRecipe(
                ItemList.MagicEnergyAbsorber_MV.get(1L),
                bitsd,
                new Object[] { "CTC", "FMF", "CBC", 'M', ItemList.Hull_MV, 'B',
                    ItemList.MagicEnergyConverter_MV.get(1L), 'C', OrePrefixes.circuit.get(Materials.EV), 'T',
                    ItemList.Field_Generator_MV, 'F', OrePrefixes.plate.get(Materials.Iridium) });
            GT_ModHandler.addCraftingRecipe(
                ItemList.MagicEnergyAbsorber_HV.get(1L),
                bitsd,
                new Object[] { "CTC", "FMF", "CBC", 'M', ItemList.Hull_HV, 'B',
                    ItemList.MagicEnergyConverter_MV.get(1L), 'C', OrePrefixes.circuit.get(Materials.IV), 'T',
                    ItemList.Field_Generator_HV, 'F', OrePrefixes.plate.get(Materials.Europium) });
            GT_ModHandler.addCraftingRecipe(
                ItemList.MagicEnergyAbsorber_EV.get(1L),
                bitsd,
                new Object[] { "CTC", "FMF", "CBC", 'M', ItemList.Hull_HV, 'B',
                    ItemList.MagicEnergyConverter_HV.get(1L), 'C', OrePrefixes.circuit.get(Materials.LuV), 'T',
                    ItemList.Field_Generator_EV, 'F', OrePrefixes.plate.get(Materials.Neutronium) });
        }
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_Fusion_Coil.get(1L),
            bitsd,
            new Object[] { "CTC", "FMF", "CTC", 'M', ItemList.Casing_Coil_Superconductor, 'C',
                OrePrefixes.circuit.get(Materials.LuV), 'F', ItemList.Field_Generator_MV, 'T',
                ItemList.Neutron_Reflector });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Generator_Plasma_EV.get(1L),
            bitsd,
            new Object[] { "UCU", "FMF", aTextWireCoil, 'M', ItemList.Hull_LuV, 'F', ItemList.Field_Generator_HV, 'C',
                OrePrefixes.circuit.get(Materials.IV), 'W', OrePrefixes.cableGt04.get(Materials.Tungsten), 'U',
                OrePrefixes.stick.get(Materials.Plutonium241) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Generator_Plasma_IV.get(1L),
            bitsd,
            new Object[] { "UCU", "FMF", aTextWireCoil, 'M', ItemList.Hull_ZPM, 'F', ItemList.Field_Generator_EV, 'C',
                OrePrefixes.circuit.get(Materials.LuV), 'W', OrePrefixes.wireGt04.get(Materials.VanadiumGallium), 'U',
                OrePrefixes.stick.get(Materials.Europium) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Generator_Plasma_LuV.get(1L),
            bitsd,
            new Object[] { "UCU", "FMF", aTextWireCoil, 'M', ItemList.Hull_UV, 'F', ItemList.Field_Generator_IV, 'C',
                OrePrefixes.circuit.get(Materials.ZPM), 'W', OrePrefixes.wireGt04.get(Materials.Naquadah), 'U',
                OrePrefixes.stick.get(Materials.Americium) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Processing_Array.get(1L),
            bitsd,
            new Object[] { "CTC", "FMF", "CBC", 'M', ItemList.Hull_EV, 'B',
                OrePrefixes.pipeLarge.get(Materials.StainlessSteel), 'C', OrePrefixes.circuit.get(Materials.IV), 'F',
                ItemList.Robot_Arm_EV, 'T', ItemList.Energy_LapotronicOrb });

        GT_ProcessingArrayRecipeLoader.registerDefaultGregtechMaps();
        GT_ModHandler.addCraftingRecipe(
            ItemList.Distillation_Tower.get(1L),
            bitsd,
            new Object[] { "CBC", "FMF", "CBC", 'M', ItemList.Hull_HV, 'B',
                OrePrefixes.pipeLarge.get(Materials.StainlessSteel), 'C', OrePrefixes.circuit.get(Materials.EV), 'F',
                ItemList.Electric_Pump_HV });

        GT_ModHandler.addCraftingRecipe(
            ItemList.LargeSteamTurbine.get(1L),
            bitsd,
            new Object[] { "CPC", aTextPlateMotor, "BPB", 'M', ItemList.Hull_HV, 'B',
                OrePrefixes.pipeLarge.get(Materials.Steel), 'C', OrePrefixes.circuit.get(Materials.HV), 'P',
                OrePrefixes.gearGt.get(Materials.Steel) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.LargeGasTurbine.get(1L),
            bitsd,
            new Object[] { "CPC", aTextPlateMotor, "BPB", 'M', ItemList.Hull_EV, 'B',
                OrePrefixes.pipeLarge.get(Materials.StainlessSteel), 'C', OrePrefixes.circuit.get(Materials.EV), 'P',
                OrePrefixes.gearGt.get(Materials.StainlessSteel) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.LargeAdvancedGasTurbine.get(1L),
            bitsd,
            new Object[] { "CPC", aTextPlateMotor, "BPB", 'M', ItemList.Hull_IV, 'B',
                OrePrefixes.pipeLarge.get(Materials.TungstenSteel), 'C', OrePrefixes.circuit.get(Materials.LuV), 'P',
                OrePrefixes.gearGt.get(Materials.HSSG) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Pump_LV.get(1L),
            bitsd,
            new Object[] { "CPC", aTextPlateMotor, "BPB", 'M', ItemList.Hull_LV, 'B',
                OrePrefixes.pipeLarge.get(Materials.Bronze), 'C', OrePrefixes.circuit.get(Materials.LV), 'P',
                ItemList.Electric_Pump_LV });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Pump_MV.get(1L),
            bitsd,
            new Object[] { "CPC", aTextPlateMotor, "BPB", 'M', ItemList.Hull_MV, 'B',
                OrePrefixes.pipeLarge.get(Materials.Steel), 'C', OrePrefixes.circuit.get(Materials.MV), 'P',
                ItemList.Electric_Pump_MV });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Pump_HV.get(1L),
            bitsd,
            new Object[] { "CPC", aTextPlateMotor, "BPB", 'M', ItemList.Hull_HV, 'B',
                OrePrefixes.pipeLarge.get(Materials.StainlessSteel), 'C', OrePrefixes.circuit.get(Materials.HV), 'P',
                ItemList.Electric_Pump_HV });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Pump_EV.get(1L),
            bitsd,
            new Object[] { "CPC", aTextPlateMotor, "BPB", 'M', ItemList.Hull_EV, 'B',
                OrePrefixes.pipeLarge.get(Materials.Titanium), 'C', OrePrefixes.circuit.get(Materials.EV), 'P',
                ItemList.Electric_Pump_EV });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Pump_IV.get(1L),
            bitsd,
            new Object[] { "CPC", aTextPlateMotor, "BPB", 'M', ItemList.Hull_IV, 'B',
                OrePrefixes.pipeLarge.get(Materials.TungstenSteel), 'C', OrePrefixes.circuit.get(Materials.IV), 'P',
                ItemList.Electric_Pump_IV });

        GT_ModHandler.addCraftingRecipe(
            ItemList.MobRep_LV.get(1L),
            bitsd,
            new Object[] { "EEE", " M ", "CCC", 'M', ItemList.Hull_LV, 'E', ItemList.Emitter_LV.get(1L), 'C',
                OrePrefixes.circuit.get(Materials.LV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.MobRep_MV.get(1L),
            bitsd,
            new Object[] { "EEE", " M ", "CCC", 'M', ItemList.Hull_MV, 'E', ItemList.Emitter_MV.get(1L), 'C',
                OrePrefixes.circuit.get(Materials.MV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.MobRep_HV.get(1L),
            bitsd,
            new Object[] { "EEE", " M ", "CCC", 'M', ItemList.Hull_HV, 'E', ItemList.Emitter_HV.get(1L), 'C',
                OrePrefixes.circuit.get(Materials.HV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.MobRep_EV.get(1L),
            bitsd,
            new Object[] { "EEE", " M ", "CCC", 'M', ItemList.Hull_EV, 'E', ItemList.Emitter_EV.get(1L), 'C',
                OrePrefixes.circuit.get(Materials.EV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.MobRep_IV.get(1L),
            bitsd,
            new Object[] { "EEE", " M ", "CCC", 'M', ItemList.Hull_IV, 'E', ItemList.Emitter_IV.get(1L), 'C',
                OrePrefixes.circuit.get(Materials.IV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.MobRep_LuV.get(1L),
            bitsd,
            new Object[] { "EEE", " M ", "CCC", 'M', ItemList.Hull_LuV, 'E', ItemList.Emitter_LuV.get(1L), 'C',
                OrePrefixes.circuit.get(Materials.LuV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.MobRep_ZPM.get(1L),
            bitsd,
            new Object[] { "EEE", " M ", "CCC", 'M', ItemList.Hull_ZPM, 'E', ItemList.Emitter_ZPM.get(1L), 'C',
                OrePrefixes.circuit.get(Materials.ZPM) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.MobRep_UV.get(1L),
            bitsd,
            new Object[] { "EEE", " M ", "CCC", 'M', ItemList.Hull_UV, 'E', ItemList.Emitter_UV.get(1L), 'C',
                OrePrefixes.circuit.get(Materials.UV) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_Multi_HeatExchanger.get(1L),
            bitsd,
            new Object[] { aTextWireCoil, aTextCableHull, aTextWireCoil, 'M', ItemList.Casing_Pipe_Titanium, 'C',
                OrePrefixes.pipeMedium.get(Materials.Titanium), 'W', ItemList.Electric_Pump_EV });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Charcoal_Pile.get(1L),
            bitsd,
            new Object[] { "EXE", "EME", "hCw", 'M', ItemList.Hull_HP_Bricks, 'E',
                OrePrefixes.plate.get(Materials.AnyBronze), 'C', new ItemStack(Items.flint_and_steel, 1), 'X',
                OrePrefixes.rotor.get(Materials.Steel), });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Seismic_Prospector_Adv_LV.get(1L),
            bitsd,
            new Object[] { "WWW", "EME", "CXC", 'M', ItemList.Hull_LV, 'W',
                OrePrefixes.plateDouble.get(Materials.Steel), 'E', OrePrefixes.circuit.get(Materials.LV), 'C',
                ItemList.Sensor_LV, 'X', OrePrefixes.cableGt02.get(Materials.Tin) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Seismic_Prospector_Adv_MV.get(1L),
            bitsd,
            new Object[] { "WWW", "EME", "CXC", 'M', ItemList.Hull_MV, 'W',
                OrePrefixes.plateDouble.get(Materials.BlackSteel), 'E', OrePrefixes.circuit.get(Materials.MV), 'C',
                ItemList.Sensor_MV, 'X', OrePrefixes.cableGt02.get(Materials.Copper) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Seismic_Prospector_Adv_HV.get(1L),
            bitsd,
            new Object[] { "WWW", "EME", "CXC", 'M', ItemList.Hull_HV, 'W',
                OrePrefixes.plateDouble.get(Materials.StainlessSteel), 'E', OrePrefixes.circuit.get(Materials.HV), 'C',
                ItemList.Sensor_HV, 'X', OrePrefixes.cableGt04.get(Materials.Gold) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Seismic_Prospector_Adv_EV.get(1L),
            bitsd,
            new Object[] { "WWW", "EME", "CXC", 'M', ItemList.Hull_EV, 'W',
                OrePrefixes.plateDouble.get(Materials.VanadiumSteel), 'E', OrePrefixes.circuit.get(Materials.EV), 'C',
                ItemList.Sensor_EV, 'X', OrePrefixes.cableGt04.get(Materials.Aluminium) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.ConcreteBackfiller1.get(1L),
            bitsd,
            new Object[] { "WPW", "EME", "CQC", 'M', ItemList.Hull_MV, 'W', OrePrefixes.frameGt.get(Materials.Steel),
                'E', OrePrefixes.circuit.get(Materials.MV), 'C', ItemList.Electric_Motor_MV, 'P',
                OrePrefixes.pipeLarge.get(Materials.Steel), 'Q', ItemList.Electric_Pump_MV });
        GT_ModHandler.addCraftingRecipe(
            ItemList.ConcreteBackfiller2.get(1L),
            bitsd,
            new Object[] { "WPW", "EME", "CQC", 'M', ItemList.ConcreteBackfiller1, 'W',
                OrePrefixes.frameGt.get(Materials.Titanium), 'E', OrePrefixes.circuit.get(Materials.EV), 'C',
                ItemList.Electric_Motor_EV, 'P', OrePrefixes.pipeLarge.get(Materials.Steel), 'Q',
                ItemList.Electric_Pump_EV });

        GT_ModHandler.addCraftingRecipe(
            ItemList.PyrolyseOven.get(1L),
            bitsd,
            new Object[] { "WEP", "EME", "WCP", 'M', ItemList.Hull_MV, 'W', ItemList.Electric_Piston_MV, 'P',
                OrePrefixes.wireGt04.get(Materials.Cupronickel), 'E', OrePrefixes.circuit.get(Materials.MV), 'C',
                ItemList.Electric_Pump_MV });

        GT_ModHandler.addCraftingRecipe(
            ItemList.OilCracker.get(1L),
            bitsd,
            new Object[] { aTextWireCoil, "EME", aTextWireCoil, 'M', ItemList.Hull_HV, 'W',
                ItemList.Casing_Coil_Cupronickel, 'E', OrePrefixes.circuit.get(Materials.HV), 'C',
                ItemList.Electric_Pump_HV });

        GT_ModHandler.addCraftingRecipe(
            ItemList.MicroTransmitter_HV.get(1L),
            bitsd,
            new Object[] { "CPC", aTextCableHull, "GBG", 'M', ItemList.Hull_HV, 'B', ItemList.Battery_RE_HV_Lithium,
                'C', ItemList.Emitter_HV, 'G', OrePrefixes.circuit.get(Materials.HV), 'P',
                ItemList.Field_Generator_HV });
        GT_ModHandler.addCraftingRecipe(
            ItemList.MicroTransmitter_EV.get(1L),
            bitsd,
            new Object[] { "CPC", aTextCableHull, "GBG", 'M', ItemList.Hull_EV, 'B',
                GT_ModHandler.getIC2Item("lapotronCrystal", 1L, GT_Values.W), 'C', ItemList.Emitter_EV, 'G',
                OrePrefixes.circuit.get(Materials.EV), 'P', ItemList.Field_Generator_EV });
        GT_ModHandler.addCraftingRecipe(
            ItemList.MicroTransmitter_IV.get(1L),
            bitsd,
            new Object[] { "CPC", aTextCableHull, "GBG", 'M', ItemList.Hull_IV, 'B', ItemList.Energy_LapotronicOrb, 'C',
                ItemList.Emitter_IV, 'G', OrePrefixes.circuit.get(Materials.IV), 'P', ItemList.Field_Generator_IV });
        GT_ModHandler.addCraftingRecipe(
            ItemList.MicroTransmitter_LUV.get(1L),
            bitsd,
            new Object[] { "CPC", aTextCableHull, "GBG", 'M', ItemList.Hull_LuV, 'B', ItemList.Energy_LapotronicOrb2,
                'C', ItemList.Emitter_LuV, 'G', OrePrefixes.circuit.get(Materials.LuV), 'P',
                ItemList.Field_Generator_LuV });
        GT_ModHandler.addCraftingRecipe(
            ItemList.MicroTransmitter_ZPM.get(1L),
            bitsd,
            new Object[] { "CPC", aTextCableHull, "GBG", 'M', ItemList.Hull_ZPM, 'B', ItemList.Energy_Module, 'C',
                ItemList.Emitter_ZPM, 'G', OrePrefixes.circuit.get(Materials.ZPM), 'P', ItemList.Field_Generator_ZPM });
        GT_ModHandler.addCraftingRecipe(
            ItemList.MicroTransmitter_UV.get(1L),
            bitsd,
            new Object[] { "CPC", aTextCableHull, "GBG", 'M', ItemList.Hull_UV, 'B', ItemList.Energy_Module, 'C',
                ItemList.Emitter_UV, 'G', OrePrefixes.circuit.get(Materials.UV), 'P', ItemList.Field_Generator_UV });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_Multi_Assemblyline.get(1L),
            bitsd,
            new Object[] { aTextWireCoil, "EME", aTextWireCoil, 'M', ItemList.Hull_IV, 'W', ItemList.Casing_Assembler,
                'E', OrePrefixes.circuit.get(Materials.IV), 'C', ItemList.Robot_Arm_IV });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_Multi_DieselEngine.get(1L),
            bitsd,
            new Object[] { "PCP", "EME", "GWG", 'M', ItemList.Hull_EV, 'P', ItemList.Electric_Piston_EV, 'E',
                ItemList.Electric_Motor_EV, 'C', OrePrefixes.circuit.get(Materials.IV), 'W',
                OrePrefixes.cableGt01.get(Materials.TungstenSteel), 'G', OrePrefixes.gearGt.get(Materials.Titanium) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_EngineIntake.get(4L),
            bitsd,
            new Object[] { "PhP", "RFR", aTextPlateWrench, 'R', OrePrefixes.pipeMedium.get(Materials.Titanium), 'F',
                ItemList.Casing_StableTitanium, 'P', OrePrefixes.rotor.get(Materials.Titanium) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_Multi_ExtremeDieselEngine.get(1L),
            bitsd,
            new Object[] { "PCP", "EME", "GWG", 'M', ItemList.Hull_IV, 'P', ItemList.Electric_Piston_IV, 'E',
                ItemList.Electric_Motor_IV, 'C', OrePrefixes.circuit.get(Materials.LuV), 'W',
                OrePrefixes.cableGt01.get(Materials.HSSG), 'G', OrePrefixes.gearGt.get(Materials.TungstenSteel) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_ExtremeEngineIntake.get(4L),
            bitsd,
            new Object[] { "PhP", "RFR", aTextPlateWrench, 'R', OrePrefixes.pipeMedium.get(Materials.TungstenSteel),
                'F', ItemList.Casing_RobustTungstenSteel, 'P', OrePrefixes.rotor.get(Materials.TungstenSteel) });

        // If Cleanroom is enabled, add a recipe, else hide from NEI.
        if (GT_Mod.gregtechproxy.mEnableCleanroom) {
            GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_Multi_Cleanroom.get(1L),
                bitsd,
                new Object[] { "FFF", "RHR", "MCM", 'H', ItemList.Hull_HV, 'F', ItemList.Component_Filter, 'R',
                    OrePrefixes.rotor.get(Materials.StainlessSteel), 'M', ItemList.Electric_Motor_HV, 'C',
                    OrePrefixes.circuit.get(Materials.HV) });
        } else {
            if (NotEnoughItems.isModLoaded()) {
                API.hideItem(ItemList.Machine_Multi_Cleanroom.get(1L));
            }
        }

        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_HV_LightningRod.get(1L),
            bitsd,
            new Object[] { "LTL", "TMT", "LTL", 'M', ItemList.Hull_LuV, 'L', ItemList.Energy_LapotronicOrb, 'T',
                ItemList.Transformer_ZPM_LuV });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_EV_LightningRod.get(1L),
            bitsd,
            new Object[] { "LTL", "TMT", "LTL", 'M', ItemList.Hull_ZPM, 'L', ItemList.Energy_LapotronicOrb2, 'T',
                ItemList.Transformer_UV_ZPM });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_IV_LightningRod.get(1L),
            bitsd,
            new Object[] { "LTL", "TMT", "LTL", 'M', ItemList.Hull_UV, 'L', ItemList.ZPM2, 'T',
                ItemList.Transformer_MAX_UV });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_Multi_LargeChemicalReactor.get(1L),
            bitsd,
            new Object[] { "CRC", "PMP", "CBC", 'C', OrePrefixes.circuit.get(Materials.HV), 'R',
                OrePrefixes.rotor.get(Materials.StainlessSteel), 'P',
                OrePrefixes.pipeLarge.get(Materials.Polytetrafluoroethylene), 'M', ItemList.Electric_Motor_HV, 'B',
                ItemList.Hull_HV });

        // Add Drone down link hatch
        GT_ModHandler.addCraftingRecipe(
            ItemList.Hatch_DroneDownLink.get(1L),
            bits,
            new Object[] { " S ", "CMC", "RRR", 'M', ItemList.Hatch_Maintenance, 'S', ItemList.Sensor_IV, 'R',
                new ItemStack(GregTech_API.sBlockReinforced, 1, 9), 'C', ItemList.Conveyor_Module_EV });

        // And Drone Centre
        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_Assembler.get(1),
                ItemList.Cover_SolarPanel_HV.get(4),
                ItemList.Conveyor_Module_IV.get(2),
                ItemList.Robot_Arm_IV.get(2),
                ItemList.Sensor_IV.get(2),
                ItemList.Energy_LapotronicOrb.get(4),
                ItemList.Cover_WirelessNeedsMaintainance.get(1),
                GalacticraftCore.isModLoaded() ? GT_ModHandler.getModItem(GalacticraftCore.ID, "item.basicItem", 1, 19)
                    : ItemList.Sensor_EV.get(4))
            .itemOutputs(ItemList.Machine_Multi_DroneCentre.get(1L))
            .fluidInputs(Materials.AdvancedGlue.getFluid(8000L))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);
    }

    private static void registerShapelessCraftingRecipes() {
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Casing_SolidSteel.get(1L),
            bits,
            new Object[] { ItemList.Casing_Stripes_A });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Casing_SolidSteel.get(1L),
            bits,
            new Object[] { ItemList.Casing_Stripes_B });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Casing_SolidSteel.get(1L),
            bits,
            new Object[] { ItemList.Casing_RadioactiveHazard });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Casing_SolidSteel.get(1L),
            bits,
            new Object[] { ItemList.Casing_BioHazard });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Casing_SolidSteel.get(1L),
            bits,
            new Object[] { ItemList.Casing_ExplosionHazard });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Casing_SolidSteel.get(1L),
            bits,
            new Object[] { ItemList.Casing_FireHazard });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Casing_SolidSteel.get(1L),
            bits,
            new Object[] { ItemList.Casing_AcidHazard });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Casing_SolidSteel.get(1L),
            bits,
            new Object[] { ItemList.Casing_MagicHazard });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Casing_SolidSteel.get(1L),
            bits,
            new Object[] { ItemList.Casing_FrostHazard });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Casing_SolidSteel.get(1L),
            bits,
            new Object[] { ItemList.Casing_NoiseHazard });

        if (Forestry.isModLoaded() && Gendustry.isModLoaded()) {
            /* Conversion recipes */
            // TODO: Move those recipes with the other recipes

            GT_ModHandler.addShapelessCraftingRecipe(
                ItemList.Machine_IndustrialApiary.get(1L),
                new Object[] { GT_ModHandler.getModItem(Gendustry.ID, "IndustrialApiary", 1, 0) });
            GT_ModHandler.addShapelessCraftingRecipe(
                ItemList.IndustrialApiary_Upgrade_Frame.get(1),
                new Object[] { GT_ModHandler.getModItem(Gendustry.ID, "UpgradeFrame", 1) });
            GT_ModHandler.addShapelessCraftingRecipe(
                ItemList.IndustrialApiary_Upgrade_PRODUCTION.get(1L),
                new Object[] { GT_ModHandler.getModItem(Gendustry.ID, "ApiaryUpgrade", 1, 0) });
            GT_ModHandler.addShapelessCraftingRecipe(
                ItemList.IndustrialApiary_Upgrade_PLAINS.get(1L),
                new Object[] { GT_ModHandler.getModItem(Gendustry.ID, "ApiaryUpgrade", 1, 17) });
            GT_ModHandler.addShapelessCraftingRecipe(
                ItemList.IndustrialApiary_Upgrade_LIGHT.get(1L),
                new Object[] { GT_ModHandler.getModItem(Gendustry.ID, "ApiaryUpgrade", 1, 11) });
            GT_ModHandler.addShapelessCraftingRecipe(
                ItemList.IndustrialApiary_Upgrade_FLOWERING.get(1L),
                new Object[] { GT_ModHandler.getModItem(Gendustry.ID, "ApiaryUpgrade", 1, 2) });
            GT_ModHandler.addShapelessCraftingRecipe(
                ItemList.IndustrialApiary_Upgrade_WINTER.get(1L),
                new Object[] { GT_ModHandler.getModItem(Gendustry.ID, "ApiaryUpgrade", 1, 20) });
            GT_ModHandler.addShapelessCraftingRecipe(
                ItemList.IndustrialApiary_Upgrade_DRYER.get(1L),
                new Object[] { GT_ModHandler.getModItem(Gendustry.ID, "ApiaryUpgrade", 1, 5) });
            GT_ModHandler.addShapelessCraftingRecipe(
                ItemList.IndustrialApiary_Upgrade_AUTOMATION.get(1L),
                new Object[] { GT_ModHandler.getModItem(Gendustry.ID, "ApiaryUpgrade", 1, 14) });
            GT_ModHandler.addShapelessCraftingRecipe(
                ItemList.IndustrialApiary_Upgrade_HUMIDIFIER.get(1L),
                new Object[] { GT_ModHandler.getModItem(Gendustry.ID, "ApiaryUpgrade", 1, 4) });
            GT_ModHandler.addShapelessCraftingRecipe(
                ItemList.IndustrialApiary_Upgrade_HELL.get(1L),
                new Object[] { GT_ModHandler.getModItem(Gendustry.ID, "ApiaryUpgrade", 1, 13) });
            GT_ModHandler.addShapelessCraftingRecipe(
                ItemList.IndustrialApiary_Upgrade_POLLEN.get(1L),
                new Object[] { GT_ModHandler.getModItem(Gendustry.ID, "ApiaryUpgrade", 1, 22) });
            GT_ModHandler.addShapelessCraftingRecipe(
                ItemList.IndustrialApiary_Upgrade_DESERT.get(1L),
                new Object[] { GT_ModHandler.getModItem(Gendustry.ID, "ApiaryUpgrade", 1, 16) });
            GT_ModHandler.addShapelessCraftingRecipe(
                ItemList.IndustrialApiary_Upgrade_COOLER.get(1L),
                new Object[] { GT_ModHandler.getModItem(Gendustry.ID, "ApiaryUpgrade", 1, 7) });
            GT_ModHandler.addShapelessCraftingRecipe(
                ItemList.IndustrialApiary_Upgrade_LIFESPAN.get(1L),
                new Object[] { GT_ModHandler.getModItem(Gendustry.ID, "ApiaryUpgrade", 1, 1) });
            GT_ModHandler.addShapelessCraftingRecipe(
                ItemList.IndustrialApiary_Upgrade_SEAL.get(1L),
                new Object[] { GT_ModHandler.getModItem(Gendustry.ID, "ApiaryUpgrade", 1, 10) });
            GT_ModHandler.addShapelessCraftingRecipe(
                ItemList.IndustrialApiary_Upgrade_STABILIZER.get(1L),
                new Object[] { GT_ModHandler.getModItem(Gendustry.ID, "ApiaryUpgrade", 1, 19) });
            GT_ModHandler.addShapelessCraftingRecipe(
                ItemList.IndustrialApiary_Upgrade_JUNGLE.get(1L),
                new Object[] { GT_ModHandler.getModItem(Gendustry.ID, "ApiaryUpgrade", 1, 18) });
            GT_ModHandler.addShapelessCraftingRecipe(
                ItemList.IndustrialApiary_Upgrade_TERRITORY.get(1L),
                new Object[] { GT_ModHandler.getModItem(Gendustry.ID, "ApiaryUpgrade", 1, 3) });
            GT_ModHandler.addShapelessCraftingRecipe(
                ItemList.IndustrialApiary_Upgrade_OCEAN.get(1L),
                new Object[] { GT_ModHandler.getModItem(Gendustry.ID, "ApiaryUpgrade", 1, 21) });
            GT_ModHandler.addShapelessCraftingRecipe(
                ItemList.IndustrialApiary_Upgrade_SKY.get(1L),
                new Object[] { GT_ModHandler.getModItem(Gendustry.ID, "ApiaryUpgrade", 1, 12) });
            GT_ModHandler.addShapelessCraftingRecipe(
                ItemList.IndustrialApiary_Upgrade_HEATER.get(1L),
                new Object[] { GT_ModHandler.getModItem(Gendustry.ID, "ApiaryUpgrade", 1, 6) });
            GT_ModHandler.addShapelessCraftingRecipe(
                ItemList.IndustrialApiary_Upgrade_SIEVE.get(1L),
                new Object[] { GT_ModHandler.getModItem(Gendustry.ID, "ApiaryUpgrade", 1, 15) });

        }
    }

    private static void run4() {
        long bits = GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
            | GT_ModHandler.RecipeBits.REVERSIBLE
            | GT_ModHandler.RecipeBits.BUFFERED;

        // high pressure fluid pipes
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.pipeSmall, Materials.TungstenSteel, 1L),
                ItemList.Electric_Pump_EV.get(1L),
                GT_Utility.getIntegratedCircuit(5))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.pipeSmall, Materials.Ultimate, 1L))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.TungstenSteel, 1L),
                ItemList.Electric_Pump_IV.get(1L),
                GT_Utility.getIntegratedCircuit(5))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Ultimate, 1L))
            .duration(20 * SECONDS)
            .eut(4096)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.TungstenSteel, 1L),
                ItemList.Electric_Pump_IV.get(2L),
                GT_Utility.getIntegratedCircuit(5))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Ultimate, 1L))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_ChestBuffer_ULV.get(1L),
            bits,
            new Object[] { "CMV", " X ", 'M', ItemList.Hull_ULV, 'V', ItemList.Conveyor_Module_LV, 'C',
                OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.ULV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_ChestBuffer_LV.get(1L),
            bits,
            new Object[] { "CMV", " X ", 'M', ItemList.Hull_LV, 'V', ItemList.Conveyor_Module_LV, 'C',
                OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.LV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_ChestBuffer_MV.get(1L),
            bits,
            new Object[] { "CMV", " X ", 'M', ItemList.Hull_MV, 'V', ItemList.Conveyor_Module_MV, 'C',
                OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.MV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_ChestBuffer_HV.get(1L),
            bits,
            new Object[] { "CMV", " X ", 'M', ItemList.Hull_HV, 'V', ItemList.Conveyor_Module_HV, 'C',
                OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.HV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_ChestBuffer_EV.get(1L),
            bits,
            new Object[] { "CMV", " X ", 'M', ItemList.Hull_EV, 'V', ItemList.Conveyor_Module_EV, 'C',
                OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.EV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_ChestBuffer_IV.get(1L),
            bits,
            new Object[] { "CMV", " X ", 'M', ItemList.Hull_IV, 'V', ItemList.Conveyor_Module_IV, 'C',
                OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.IV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_ChestBuffer_LuV.get(1L),
            bits,
            new Object[] { "CMV", " X ", 'M', ItemList.Hull_LuV, 'V', ItemList.Conveyor_Module_LuV, 'C',
                OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.LuV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_ChestBuffer_ZPM.get(1L),
            bits,
            new Object[] { "CMV", " X ", 'M', ItemList.Hull_ZPM, 'V', ItemList.Conveyor_Module_ZPM, 'C',
                OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.ZPM) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_ChestBuffer_UV.get(1L),
            bits,
            new Object[] { "CMV", " X ", 'M', ItemList.Hull_UV, 'V', ItemList.Conveyor_Module_UV, 'C',
                OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.UV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_ChestBuffer_UHV.get(1L),
            bits,
            new Object[] { "CMV", " X ", 'M', ItemList.Hull_MAX, 'V', ItemList.Conveyor_Module_UHV, 'C',
                OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.UHV) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_Filter_ULV.get(1L),
            bits,
            new Object[] { " F ", "CMV", " X ", 'M', ItemList.Hull_ULV, 'V', ItemList.Conveyor_Module_LV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_Filter_LV.get(1L),
            bits,
            new Object[] { " F ", "CMV", " X ", 'M', ItemList.Hull_LV, 'V', ItemList.Conveyor_Module_LV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_Filter_MV.get(1L),
            bits,
            new Object[] { " F ", "CMV", " X ", 'M', ItemList.Hull_MV, 'V', ItemList.Conveyor_Module_MV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_Filter_HV.get(1L),
            bits,
            new Object[] { " F ", "CMV", " X ", 'M', ItemList.Hull_HV, 'V', ItemList.Conveyor_Module_HV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_Filter_EV.get(1L),
            bits,
            new Object[] { " F ", "CMV", " X ", 'M', ItemList.Hull_EV, 'V', ItemList.Conveyor_Module_EV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_Filter_IV.get(1L),
            bits,
            new Object[] { " F ", "CMV", " X ", 'M', ItemList.Hull_IV, 'V', ItemList.Conveyor_Module_IV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_Filter_LuV.get(1L),
            bits,
            new Object[] { " F ", "CMV", " X ", 'M', ItemList.Hull_LuV, 'V', ItemList.Conveyor_Module_LuV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_Filter_ZPM.get(1L),
            bits,
            new Object[] { " F ", "CMV", " X ", 'M', ItemList.Hull_ZPM, 'V', ItemList.Conveyor_Module_ZPM, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_Filter_UV.get(1L),
            bits,
            new Object[] { " F ", "CMV", " X ", 'M', ItemList.Hull_UV, 'V', ItemList.Conveyor_Module_UV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_Filter_MAX.get(1L),
            bits,
            new Object[] { " F ", "CMV", " X ", 'M', ItemList.Hull_MAX, 'V', ItemList.Conveyor_Module_UHV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_TypeFilter_ULV.get(1L),
            bits,
            new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_ULV, 'V', ItemList.Conveyor_Module_LV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_TypeFilter_LV.get(1L),
            bits,
            new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_LV, 'V', ItemList.Conveyor_Module_LV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_TypeFilter_MV.get(1L),
            bits,
            new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_MV, 'V', ItemList.Conveyor_Module_MV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_TypeFilter_HV.get(1L),
            bits,
            new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_HV, 'V', ItemList.Conveyor_Module_HV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_TypeFilter_EV.get(1L),
            bits,
            new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_EV, 'V', ItemList.Conveyor_Module_EV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_TypeFilter_IV.get(1L),
            bits,
            new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_IV, 'V', ItemList.Conveyor_Module_IV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_TypeFilter_LuV.get(1L),
            bits,
            new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_LuV, 'V', ItemList.Conveyor_Module_LuV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_TypeFilter_ZPM.get(1L),
            bits,
            new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_ZPM, 'V', ItemList.Conveyor_Module_ZPM, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_TypeFilter_UV.get(1L),
            bits,
            new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_UV, 'V', ItemList.Conveyor_Module_UV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_TypeFilter_MAX.get(1L),
            bits,
            new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_MAX, 'V', ItemList.Conveyor_Module_UHV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_Regulator_ULV.get(1L),
            bits,
            new Object[] { "XFX", "VMV", "XCX", 'M', ItemList.Hull_ULV, 'V', ItemList.Robot_Arm_LV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_Regulator_LV.get(1L),
            bits,
            new Object[] { "XFX", "VMV", "XCX", 'M', ItemList.Hull_LV, 'V', ItemList.Robot_Arm_LV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_Regulator_MV.get(1L),
            bits,
            new Object[] { "XFX", "VMV", "XCX", 'M', ItemList.Hull_MV, 'V', ItemList.Robot_Arm_MV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_Regulator_HV.get(1L),
            bits,
            new Object[] { "XFX", "VMV", "XCX", 'M', ItemList.Hull_HV, 'V', ItemList.Robot_Arm_HV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_Regulator_EV.get(1L),
            bits,
            new Object[] { "XFX", "VMV", "XCX", 'M', ItemList.Hull_EV, 'V', ItemList.Robot_Arm_EV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_Regulator_IV.get(1L),
            bits,
            new Object[] { "XFX", "VMV", "XCX", 'M', ItemList.Hull_IV, 'V', ItemList.Robot_Arm_IV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_Regulator_LuV.get(1L),
            bits,
            new Object[] { "XFX", "VMV", "XCX", 'M', ItemList.Hull_LuV, 'V', ItemList.Robot_Arm_LuV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_Regulator_ZPM.get(1L),
            bits,
            new Object[] { "XFX", "VMV", "XCX", 'M', ItemList.Hull_ZPM, 'V', ItemList.Robot_Arm_ZPM, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_Regulator_UV.get(1L),
            bits,
            new Object[] { "XFX", "VMV", "XCX", 'M', ItemList.Hull_UV, 'V', ItemList.Robot_Arm_UV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_Regulator_MAX.get(1L),
            bits,
            new Object[] { "XFX", "VMV", "XCX", 'M', ItemList.Hull_MAX, 'V', ItemList.Robot_Arm_UHV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_SuperBuffer_ULV.get(1L),
            bits,
            new Object[] { "DMV", 'M', ItemList.Automation_ChestBuffer_ULV, 'V', ItemList.Conveyor_Module_LV, 'D',
                ItemList.Tool_DataOrb });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_SuperBuffer_LV.get(1L),
            bits,
            new Object[] { "DMV", 'M', ItemList.Automation_ChestBuffer_LV, 'V', ItemList.Conveyor_Module_LV, 'D',
                ItemList.Tool_DataOrb });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_SuperBuffer_MV.get(1L),
            bits,
            new Object[] { "DMV", 'M', ItemList.Automation_ChestBuffer_MV, 'V', ItemList.Conveyor_Module_MV, 'D',
                ItemList.Tool_DataOrb });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_SuperBuffer_HV.get(1L),
            bits,
            new Object[] { "DMV", 'M', ItemList.Automation_ChestBuffer_HV, 'V', ItemList.Conveyor_Module_HV, 'D',
                ItemList.Tool_DataOrb });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_SuperBuffer_EV.get(1L),
            bits,
            new Object[] { "DMV", 'M', ItemList.Automation_ChestBuffer_EV, 'V', ItemList.Conveyor_Module_EV, 'D',
                ItemList.Tool_DataOrb });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_SuperBuffer_IV.get(1L),
            bits,
            new Object[] { "DMV", 'M', ItemList.Automation_ChestBuffer_IV, 'V', ItemList.Conveyor_Module_IV, 'D',
                ItemList.Tool_DataOrb });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_SuperBuffer_LuV.get(1L),
            bits,
            new Object[] { "DMV", 'M', ItemList.Automation_ChestBuffer_LuV, 'V', ItemList.Conveyor_Module_LuV, 'D',
                ItemList.Tool_DataOrb });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_SuperBuffer_ZPM.get(1L),
            bits,
            new Object[] { "DMV", 'M', ItemList.Automation_ChestBuffer_ZPM, 'V', ItemList.Conveyor_Module_ZPM, 'D',
                ItemList.Tool_DataOrb });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_SuperBuffer_UV.get(1L),
            bits,
            new Object[] { "DMV", 'M', ItemList.Automation_ChestBuffer_UV, 'V', ItemList.Conveyor_Module_UV, 'D',
                ItemList.Tool_DataOrb });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_SuperBuffer_MAX.get(1L),
            bits,
            new Object[] { "DMV", 'M', ItemList.Automation_ChestBuffer_UHV, 'V', ItemList.Conveyor_Module_UHV, 'D',
                ItemList.Tool_DataOrb });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_SuperBuffer_ULV.get(1L),
            bits,
            new Object[] { "DMV", "DDD", 'M', ItemList.Hull_ULV, 'V', ItemList.Conveyor_Module_LV, 'D',
                ItemList.Tool_DataStick });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_SuperBuffer_LV.get(1L),
            bits,
            new Object[] { "DMV", "DDD", 'M', ItemList.Hull_LV, 'V', ItemList.Conveyor_Module_LV, 'D',
                ItemList.Tool_DataStick });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_SuperBuffer_MV.get(1L),
            bits,
            new Object[] { "DMV", "DDD", 'M', ItemList.Hull_MV, 'V', ItemList.Conveyor_Module_MV, 'D',
                ItemList.Tool_DataStick });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_SuperBuffer_HV.get(1L),
            bits,
            new Object[] { "DMV", "DDD", 'M', ItemList.Hull_HV, 'V', ItemList.Conveyor_Module_HV, 'D',
                ItemList.Tool_DataStick });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_SuperBuffer_EV.get(1L),
            bits,
            new Object[] { "DMV", "DDD", 'M', ItemList.Hull_EV, 'V', ItemList.Conveyor_Module_EV, 'D',
                ItemList.Tool_DataStick });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_SuperBuffer_IV.get(1L),
            bits,
            new Object[] { "DMV", "DDD", 'M', ItemList.Hull_IV, 'V', ItemList.Conveyor_Module_IV, 'D',
                ItemList.Tool_DataStick });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_SuperBuffer_LuV.get(1L),
            bits,
            new Object[] { "DMV", "DDD", 'M', ItemList.Hull_LuV, 'V', ItemList.Conveyor_Module_LuV, 'D',
                ItemList.Tool_DataStick });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_SuperBuffer_ZPM.get(1L),
            bits,
            new Object[] { "DMV", "DDD", 'M', ItemList.Hull_ZPM, 'V', ItemList.Conveyor_Module_ZPM, 'D',
                ItemList.Tool_DataStick });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_SuperBuffer_UV.get(1L),
            bits,
            new Object[] { "DMV", "DDD", 'M', ItemList.Hull_UV, 'V', ItemList.Conveyor_Module_UV, 'D',
                ItemList.Tool_DataStick });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_SuperBuffer_MAX.get(1L),
            bits,
            new Object[] { "DMV", "DDD", 'M', ItemList.Hull_MAX, 'V', ItemList.Conveyor_Module_UHV, 'D',
                ItemList.Tool_DataStick });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_ItemDistributor_ULV.get(1L),
            bits,
            new Object[] { "XCX", "VMV", " V ", 'M', ItemList.Hull_ULV, 'V', ItemList.Conveyor_Module_LV, 'C',
                OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.LV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_ItemDistributor_LV.get(1L),
            bits,
            new Object[] { "XCX", "VMV", " V ", 'M', ItemList.Hull_LV, 'V', ItemList.Conveyor_Module_LV, 'C',
                OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.LV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_ItemDistributor_MV.get(1L),
            bits,
            new Object[] { "XCX", "VMV", " V ", 'M', ItemList.Hull_MV, 'V', ItemList.Conveyor_Module_MV, 'C',
                OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.LV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_ItemDistributor_HV.get(1L),
            bits,
            new Object[] { "XCX", "VMV", " V ", 'M', ItemList.Hull_HV, 'V', ItemList.Conveyor_Module_HV, 'C',
                OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.LV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_ItemDistributor_EV.get(1L),
            bits,
            new Object[] { "XCX", "VMV", " V ", 'M', ItemList.Hull_EV, 'V', ItemList.Conveyor_Module_EV, 'C',
                OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.LV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_ItemDistributor_IV.get(1L),
            bits,
            new Object[] { "XCX", "VMV", " V ", 'M', ItemList.Hull_IV, 'V', ItemList.Conveyor_Module_IV, 'C',
                OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.LV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_ItemDistributor_LuV.get(1L),
            bits,
            new Object[] { "XCX", "VMV", " V ", 'M', ItemList.Hull_LuV, 'V', ItemList.Conveyor_Module_LuV, 'C',
                OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.LV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_ItemDistributor_ZPM.get(1L),
            bits,
            new Object[] { "XCX", "VMV", " V ", 'M', ItemList.Hull_ZPM, 'V', ItemList.Conveyor_Module_ZPM, 'C',
                OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.LV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_ItemDistributor_UV.get(1L),
            bits,
            new Object[] { "XCX", "VMV", " V ", 'M', ItemList.Hull_UV, 'V', ItemList.Conveyor_Module_UV, 'C',
                OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.LV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_ItemDistributor_MAX.get(1L),
            bits,
            new Object[] { "XCX", "VMV", " V ", 'M', ItemList.Hull_MAX, 'V', ItemList.Conveyor_Module_UHV, 'C',
                OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.LV) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_RecipeFilter_ULV.get(1L),
            bits,
            new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_ULV, 'V', ItemList.Robot_Arm_LV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_RecipeFilter_LV.get(1L),
            bits,
            new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_LV, 'V', ItemList.Robot_Arm_LV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_RecipeFilter_MV.get(1L),
            bits,
            new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_MV, 'V', ItemList.Robot_Arm_MV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_RecipeFilter_HV.get(1L),
            bits,
            new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_HV, 'V', ItemList.Robot_Arm_HV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_RecipeFilter_EV.get(1L),
            bits,
            new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_EV, 'V', ItemList.Robot_Arm_EV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_RecipeFilter_IV.get(1L),
            bits,
            new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_IV, 'V', ItemList.Robot_Arm_IV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_RecipeFilter_LuV.get(1L),
            bits,
            new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_LuV, 'V', ItemList.Robot_Arm_LuV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_RecipeFilter_ZPM.get(1L),
            bits,
            new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_ZPM, 'V', ItemList.Robot_Arm_ZPM, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_RecipeFilter_UV.get(1L),
            bits,
            new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_UV, 'V', ItemList.Robot_Arm_UV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Automation_RecipeFilter_MAX.get(1L),
            bits,
            new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_MAX, 'V', ItemList.Robot_Arm_UHV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
    }

    @Override
    public void run() {
        registerShapelessCraftingRecipes();
        registerShapedCraftingRecipes();
        GT_Log.out.println("GT_Mod: Recipes for MetaTileEntities.");
        registerMachineTypes();
        GT_PCBFactoryMaterialLoader.load();
        run4();
    }
}
