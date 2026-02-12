package gregtech.loaders.load;

import static gregtech.api.enums.Mods.BuildCraftFactory;
import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.enums.Mods.GalacticraftCore;
import static gregtech.api.enums.Mods.Gendustry;
import static gregtech.api.enums.Mods.IndustrialCraft2;
import static gregtech.api.enums.Mods.NotEnoughItems;
import static gregtech.api.enums.Mods.StorageDrawers;
import static gregtech.api.enums.Mods.Thaumcraft;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.util.GTModHandler.RecipeBits.BUFFERED;
import static gregtech.api.util.GTModHandler.RecipeBits.DISMANTLEABLE;
import static gregtech.api.util.GTModHandler.RecipeBits.NOT_REMOVABLE;
import static gregtech.api.util.GTModHandler.RecipeBits.REVERSIBLE;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.WILDCARD;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.jaquadro.minecraft.storagedrawers.StorageDrawers;

import codechicken.nei.api.API;
import gregtech.GTMod;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OreDictNames;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.metatileentity.implementations.MTEBasicMachineWithRecipe;
import gregtech.api.util.ExternalMaterials;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.loaders.postload.PCBFactoryMaterialLoader;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import ic2.core.Ic2Items;

public class MTERecipeLoader implements Runnable {

    private static final String aTextPlate = "PPP";
    private static final String aTextPlateWrench = "PwP";
    private static final String aTextPlateMotor = "PMP";
    private static final String aTextCableHull = "CMC";
    private static final String aTextWireHull = "WMW";
    private static final String aTextWireChest = "WTW";
    private static final String aTextWireCoil = "WCW";
    private static final String aTextMotorWire = "EWE";
    private static final String aTextWirePump = "WPW";

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
        registerReworkMigrationRecipes();
        registerReworkMTERecipes();
        registerSifter();
        registerThermalCentrifuge();
        registerUnpackager();
        registerWiremill();
        registerDrawerFramer();
    }

    private static void registerAlloySmelter() {
        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_AlloySmelter.get(1),
            new Object[] { "ECE", aTextCableHull, aTextWireCoil, 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'C',
                MTEBasicMachineWithRecipe.X.COIL_HEATING_DOUBLE },
            1);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_AlloySmelter.get(1),
            new Object[] { "ECE", aTextCableHull, aTextWireCoil, 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'C',
                MTEBasicMachineWithRecipe.X.COIL_HEATING_DOUBLE },
            2);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_AlloySmelter.get(1),
            new Object[] { "ECE", aTextCableHull, aTextWireCoil, 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'C',
                MTEBasicMachineWithRecipe.X.COIL_HEATING_DOUBLE },
            3);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_AlloySmelter.get(1),
            new Object[] { "ECE", aTextCableHull, aTextWireCoil, 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'C',
                MTEBasicMachineWithRecipe.X.COIL_HEATING_DOUBLE },
            4);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_AlloySmelter.get(1),
            new Object[] { "ECE", aTextCableHull, aTextWireCoil, 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'C',
                MTEBasicMachineWithRecipe.X.COIL_HEATING_DOUBLE },
            5);

    }

    private static void registerArcFurnace() {
        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_ArcFurnace.get(1),
            new Object[] { "WGW", aTextCableHull, aTextPlate, 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PLATE, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE4, 'G', OrePrefixes.cell.get(Materials.Graphite) },
            1);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_ArcFurnace.get(1),
            new Object[] { "WGW", aTextCableHull, aTextPlate, 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PLATE, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE4, 'G', OrePrefixes.cell.get(Materials.Graphite) },
            2);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_ArcFurnace.get(1),
            new Object[] { "WGW", aTextCableHull, aTextPlate, 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PLATE, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE4, 'G', OrePrefixes.cell.get(Materials.Graphite) },
            3);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_ArcFurnace.get(1),
            new Object[] { "WGW", aTextCableHull, aTextPlate, 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PLATE, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE4, 'G', OrePrefixes.cell.get(Materials.Graphite) },
            4);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_ArcFurnace.get(1),
            new Object[] { "WGW", aTextCableHull, aTextPlate, 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PLATE, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE4, 'G', OrePrefixes.cell.get(Materials.Graphite) },
            5);

    }

    private static void registerAssembler() {
        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_Assembler.get(1),
            new Object[] { "ACA", "VMV", aTextWireCoil, 'M', MTEBasicMachineWithRecipe.X.HULL, 'V',
                MTEBasicMachineWithRecipe.X.CONVEYOR, 'A', MTEBasicMachineWithRecipe.X.ROBOT_ARM, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE },
            1);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_Assembler.get(1),
            new Object[] { "ACA", "VMV", aTextWireCoil, 'M', MTEBasicMachineWithRecipe.X.HULL, 'V',
                MTEBasicMachineWithRecipe.X.CONVEYOR, 'A', MTEBasicMachineWithRecipe.X.ROBOT_ARM, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE },
            2);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_Assembler.get(1),
            new Object[] { "ACA", "VMV", aTextWireCoil, 'M', MTEBasicMachineWithRecipe.X.HULL, 'V',
                MTEBasicMachineWithRecipe.X.CONVEYOR, 'A', MTEBasicMachineWithRecipe.X.ROBOT_ARM, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE },
            3);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_Assembler.get(1),
            new Object[] { "ACA", "VMV", aTextWireCoil, 'M', MTEBasicMachineWithRecipe.X.HULL, 'V',
                MTEBasicMachineWithRecipe.X.CONVEYOR, 'A', MTEBasicMachineWithRecipe.X.ROBOT_ARM, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE },
            4);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_Assembler.get(1),
            new Object[] { "ACA", "VMV", aTextWireCoil, 'M', MTEBasicMachineWithRecipe.X.HULL, 'V',
                MTEBasicMachineWithRecipe.X.CONVEYOR, 'A', MTEBasicMachineWithRecipe.X.ROBOT_ARM, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE },
            5);
    }

    private static void registerAutoclave() {
        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_Autoclave.get(1),
            new Object[] { "IGI", "IMI", "CPC", 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PUMP, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE, 'I', MTEBasicMachineWithRecipe.X.PLATE, 'G',
                MTEBasicMachineWithRecipe.X.GLASS },
            1);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_Autoclave.get(1),
            new Object[] { "IGI", "IMI", "CPC", 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PUMP, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE, 'I', MTEBasicMachineWithRecipe.X.PLATE, 'G',
                MTEBasicMachineWithRecipe.X.GLASS },
            2);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_Autoclave.get(1),
            new Object[] { "IGI", "IMI", "CPC", 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PUMP, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE, 'I', MTEBasicMachineWithRecipe.X.PLATE, 'G',
                MTEBasicMachineWithRecipe.X.GLASS },
            3);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_Autoclave.get(1),
            new Object[] { "IGI", "IMI", "CPC", 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PUMP, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE, 'I', MTEBasicMachineWithRecipe.X.PLATE, 'G',
                MTEBasicMachineWithRecipe.X.GLASS },
            4);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_Autoclave.get(1),
            new Object[] { "IGI", "IMI", "CPC", 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PUMP, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE, 'I', MTEBasicMachineWithRecipe.X.PLATE, 'G',
                MTEBasicMachineWithRecipe.X.GLASS },
            5);

    }

    private static void registerBendingMachine() {
        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_Bender.get(1),
            new Object[] { aTextPlateWrench, aTextCableHull, aTextMotorWire, 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.MOTOR, 'P', MTEBasicMachineWithRecipe.X.PISTON, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE },
            1);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_Bender.get(1),
            new Object[] { aTextPlateWrench, aTextCableHull, aTextMotorWire, 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.MOTOR, 'P', MTEBasicMachineWithRecipe.X.PISTON, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE },
            2);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_Bender.get(1),
            new Object[] { aTextPlateWrench, aTextCableHull, aTextMotorWire, 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.MOTOR, 'P', MTEBasicMachineWithRecipe.X.PISTON, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE },
            3);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_Bender.get(1),
            new Object[] { aTextPlateWrench, aTextCableHull, aTextMotorWire, 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.MOTOR, 'P', MTEBasicMachineWithRecipe.X.PISTON, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE },
            4);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_Bender.get(1),
            new Object[] { aTextPlateWrench, aTextCableHull, aTextMotorWire, 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.MOTOR, 'P', MTEBasicMachineWithRecipe.X.PISTON, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE },
            5);
    }

    private static void registerCanner() {
        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_Canner.get(1),
            new Object[] { aTextWirePump, aTextCableHull, "GGG", 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PUMP, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE, 'G', MTEBasicMachineWithRecipe.X.GLASS },
            1);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_Canner.get(1),
            new Object[] { aTextWirePump, aTextCableHull, "GGG", 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PUMP, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE, 'G', MTEBasicMachineWithRecipe.X.GLASS },
            2);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_Canner.get(1),
            new Object[] { aTextWirePump, aTextCableHull, "GGG", 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PUMP, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE, 'G', MTEBasicMachineWithRecipe.X.GLASS },
            3);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_Canner.get(1),
            new Object[] { aTextWirePump, aTextCableHull, "GGG", 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PUMP, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE, 'G', MTEBasicMachineWithRecipe.X.GLASS },
            4);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_Canner.get(1),
            new Object[] { aTextWirePump, aTextCableHull, "GGG", 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PUMP, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE, 'G', MTEBasicMachineWithRecipe.X.GLASS },
            5);
    }

    private static void registerCentrifuge() {
        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_Centrifuge.get(1),
            new Object[] { "CEC", aTextWireHull, "CEC", 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.MOTOR, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE },
            1);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_Centrifuge.get(1),
            new Object[] { "CEC", aTextWireHull, "CEC", 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.MOTOR, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE },
            2);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_Centrifuge.get(1),
            new Object[] { "CEC", aTextWireHull, "CEC", 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.MOTOR, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE },
            3);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_Centrifuge.get(1),
            new Object[] { "CEC", aTextWireHull, "CEC", 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.MOTOR, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE },
            4);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_Centrifuge.get(1),
            new Object[] { "CEC", aTextWireHull, "CEC", 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.MOTOR, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE },
            5);
    }

    private static void registerChemicalBath() {
        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_ChemicalBath.get(1),
            new Object[] { "VGW", "PGV", aTextCableHull, 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PUMP, 'V', MTEBasicMachineWithRecipe.X.CONVEYOR, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'G',
                MTEBasicMachineWithRecipe.X.GLASS },
            1);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_ChemicalBath.get(1),
            new Object[] { "VGW", "PGV", aTextCableHull, 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PUMP, 'V', MTEBasicMachineWithRecipe.X.CONVEYOR, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'G',
                MTEBasicMachineWithRecipe.X.GLASS },
            2);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_ChemicalBath.get(1),
            new Object[] { "VGW", "PGV", aTextCableHull, 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PUMP, 'V', MTEBasicMachineWithRecipe.X.CONVEYOR, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'G',
                MTEBasicMachineWithRecipe.X.GLASS },
            3);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_ChemicalBath.get(1),
            new Object[] { "VGW", "PGV", aTextCableHull, 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PUMP, 'V', MTEBasicMachineWithRecipe.X.CONVEYOR, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'G',
                MTEBasicMachineWithRecipe.X.GLASS },
            4);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_ChemicalBath.get(1),
            new Object[] { "VGW", "PGV", aTextCableHull, 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PUMP, 'V', MTEBasicMachineWithRecipe.X.CONVEYOR, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'G',
                MTEBasicMachineWithRecipe.X.GLASS },
            5);

    }

    private static void registerChemicalReactor() {
        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_ChemicalReactor.get(1),
            new Object[] { "GRG", "WEW", aTextCableHull, 'M', MTEBasicMachineWithRecipe.X.HULL, 'R',
                MTEBasicMachineWithRecipe.X.ROTOR, 'E', MTEBasicMachineWithRecipe.X.MOTOR, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'G',
                MTEBasicMachineWithRecipe.X.GLASS },
            1);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_ChemicalReactor.get(1),
            new Object[] { "GRG", "WEW", aTextCableHull, 'M', MTEBasicMachineWithRecipe.X.HULL, 'R',
                MTEBasicMachineWithRecipe.X.ROTOR, 'E', MTEBasicMachineWithRecipe.X.MOTOR, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'G',
                MTEBasicMachineWithRecipe.X.GLASS },
            2);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_ChemicalReactor.get(1),
            new Object[] { "GRG", "WEW", aTextCableHull, 'M', MTEBasicMachineWithRecipe.X.HULL, 'R',
                MTEBasicMachineWithRecipe.X.ROTOR, 'E', MTEBasicMachineWithRecipe.X.MOTOR, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'G',
                OrePrefixes.pipeMedium.get(Materials.Polyethylene) },
            3);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_ChemicalReactor.get(1),
            new Object[] { "GRG", "WEW", aTextCableHull, 'M', MTEBasicMachineWithRecipe.X.HULL, 'R',
                MTEBasicMachineWithRecipe.X.ROTOR, 'E', MTEBasicMachineWithRecipe.X.MOTOR, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'G',
                OrePrefixes.pipeLarge.get(Materials.Polyethylene) },
            4);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_ChemicalReactor.get(1),
            new Object[] { "GRG", "WEW", aTextCableHull, 'M', MTEBasicMachineWithRecipe.X.HULL, 'R',
                MTEBasicMachineWithRecipe.X.ROTOR, 'E', MTEBasicMachineWithRecipe.X.MOTOR, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'G',
                OrePrefixes.pipeHuge.get(Materials.Polyethylene) },
            5);
    }

    private static void registerCircuitAssembler() {
        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_CircuitAssembler.get(1),
            new Object[] { "ACE", "VMV", aTextWireCoil, 'M', MTEBasicMachineWithRecipe.X.HULL, 'V',
                MTEBasicMachineWithRecipe.X.CONVEYOR, 'A', MTEBasicMachineWithRecipe.X.ROBOT_ARM, 'C',
                MTEBasicMachineWithRecipe.X.BETTER_CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'E',
                MTEBasicMachineWithRecipe.X.EMITTER },
            1);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_CircuitAssembler.get(1),
            new Object[] { "ACE", "VMV", aTextWireCoil, 'M', MTEBasicMachineWithRecipe.X.HULL, 'V',
                MTEBasicMachineWithRecipe.X.CONVEYOR, 'A', MTEBasicMachineWithRecipe.X.ROBOT_ARM, 'C',
                MTEBasicMachineWithRecipe.X.BETTER_CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'E',
                MTEBasicMachineWithRecipe.X.EMITTER },
            2);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_CircuitAssembler.get(1),
            new Object[] { "ACE", "VMV", aTextWireCoil, 'M', MTEBasicMachineWithRecipe.X.HULL, 'V',
                MTEBasicMachineWithRecipe.X.CONVEYOR, 'A', MTEBasicMachineWithRecipe.X.ROBOT_ARM, 'C',
                MTEBasicMachineWithRecipe.X.BETTER_CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'E',
                MTEBasicMachineWithRecipe.X.EMITTER },
            3);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_CircuitAssembler.get(1),
            new Object[] { "ACE", "VMV", aTextWireCoil, 'M', MTEBasicMachineWithRecipe.X.HULL, 'V',
                MTEBasicMachineWithRecipe.X.CONVEYOR, 'A', MTEBasicMachineWithRecipe.X.ROBOT_ARM, 'C',
                MTEBasicMachineWithRecipe.X.BETTER_CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'E',
                MTEBasicMachineWithRecipe.X.EMITTER },
            4);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_CircuitAssembler.get(1),
            new Object[] { "ACE", "VMV", aTextWireCoil, 'M', MTEBasicMachineWithRecipe.X.HULL, 'V',
                MTEBasicMachineWithRecipe.X.CONVEYOR, 'A', MTEBasicMachineWithRecipe.X.ROBOT_ARM, 'C',
                MTEBasicMachineWithRecipe.X.BETTER_CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'E',
                MTEBasicMachineWithRecipe.X.EMITTER },
            5);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LuV_CircuitAssembler.get(1),
            new Object[] { "ACE", "VMV", aTextWireCoil, 'M', MTEBasicMachineWithRecipe.X.HULL, 'V',
                MTEBasicMachineWithRecipe.X.CONVEYOR, 'A', MTEBasicMachineWithRecipe.X.ROBOT_ARM, 'C',
                MTEBasicMachineWithRecipe.X.BETTER_CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'E',
                MTEBasicMachineWithRecipe.X.EMITTER },
            6);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_ZPM_CircuitAssembler.get(1),
            new Object[] { "ACE", "VMV", aTextWireCoil, 'M', MTEBasicMachineWithRecipe.X.HULL, 'V',
                MTEBasicMachineWithRecipe.X.CONVEYOR, 'A', MTEBasicMachineWithRecipe.X.ROBOT_ARM, 'C',
                MTEBasicMachineWithRecipe.X.BETTER_CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'E',
                MTEBasicMachineWithRecipe.X.EMITTER },
            7);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_UV_CircuitAssembler.get(1),
            new Object[] { "ACE", "VMV", aTextWireCoil, 'M', MTEBasicMachineWithRecipe.X.HULL, 'V',
                MTEBasicMachineWithRecipe.X.CONVEYOR, 'A', MTEBasicMachineWithRecipe.X.ROBOT_ARM, 'C',
                MTEBasicMachineWithRecipe.X.BETTER_CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'E',
                MTEBasicMachineWithRecipe.X.EMITTER },
            8);

    }

    private static void registerCompressor() {
        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_Compressor.get(1),
            new Object[] { aTextWireCoil, aTextPlateMotor, aTextWireCoil, 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PISTON, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE },
            1);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_Compressor.get(1),
            new Object[] { aTextWireCoil, aTextPlateMotor, aTextWireCoil, 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PISTON, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE },
            2);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_Compressor.get(1),
            new Object[] { aTextWireCoil, aTextPlateMotor, aTextWireCoil, 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PISTON, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE },
            3);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_Compressor.get(1),
            new Object[] { aTextWireCoil, aTextPlateMotor, aTextWireCoil, 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PISTON, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE },
            4);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_Compressor.get(1),
            new Object[] { aTextWireCoil, aTextPlateMotor, aTextWireCoil, 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PISTON, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE },
            5);

    }

    private static void registerCuttingMachine() {
        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_Cutter.get(1),
            new Object[] { "WCG", "VMB", "CWE", 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.MOTOR, 'V', MTEBasicMachineWithRecipe.X.CONVEYOR, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'G',
                MTEBasicMachineWithRecipe.X.GLASS, 'B', OreDictNames.craftingDiamondBlade },
            1);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_Cutter.get(1),
            new Object[] { "WCG", "VMB", "CWE", 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.MOTOR, 'V', MTEBasicMachineWithRecipe.X.CONVEYOR, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'G',
                MTEBasicMachineWithRecipe.X.GLASS, 'B', OreDictNames.craftingDiamondBlade },
            2);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_Cutter.get(1),
            new Object[] { "WCG", "VMB", "CWE", 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.MOTOR, 'V', MTEBasicMachineWithRecipe.X.CONVEYOR, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'G',
                MTEBasicMachineWithRecipe.X.GLASS, 'B', OreDictNames.craftingDiamondBlade },
            3);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_Cutter.get(1),
            new Object[] { "WCG", "VMB", "CWE", 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.MOTOR, 'V', MTEBasicMachineWithRecipe.X.CONVEYOR, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'G',
                MTEBasicMachineWithRecipe.X.GLASS, 'B', OreDictNames.craftingDiamondBlade },
            4);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_Cutter.get(1),
            new Object[] { "WCG", "VMB", "CWE", 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.MOTOR, 'V', MTEBasicMachineWithRecipe.X.CONVEYOR, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'G',
                MTEBasicMachineWithRecipe.X.GLASS, 'B', OreDictNames.craftingDiamondBlade },
            5);

    }

    private static void registerDistillery() {
        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_Distillery.get(1),
            new Object[] { "GBG", aTextCableHull, aTextWirePump, 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PUMP, 'B', MTEBasicMachineWithRecipe.X.PIPE, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'G',
                MTEBasicMachineWithRecipe.X.GLASS },
            1);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_Distillery.get(1),
            new Object[] { "GBG", aTextCableHull, aTextWirePump, 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PUMP, 'B', MTEBasicMachineWithRecipe.X.PIPE, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'G',
                MTEBasicMachineWithRecipe.X.GLASS },
            2);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_Distillery.get(1),
            new Object[] { "GBG", aTextCableHull, aTextWirePump, 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PUMP, 'B', MTEBasicMachineWithRecipe.X.PIPE, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'G',
                MTEBasicMachineWithRecipe.X.GLASS },
            3);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_Distillery.get(1),
            new Object[] { "GBG", aTextCableHull, aTextWirePump, 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PUMP, 'B', MTEBasicMachineWithRecipe.X.PIPE, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'G',
                MTEBasicMachineWithRecipe.X.GLASS },
            4);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_Distillery.get(1),
            new Object[] { "GBG", aTextCableHull, aTextWirePump, 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PUMP, 'B', MTEBasicMachineWithRecipe.X.PIPE, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'G',
                MTEBasicMachineWithRecipe.X.GLASS },
            5);

    }

    private static void registerElectricFurnace() {
        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_E_Furnace.get(1),
            new Object[] { "ECE", aTextCableHull, aTextWireCoil, 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'C',
                MTEBasicMachineWithRecipe.X.COIL_HEATING },
            1);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_E_Furnace.get(1),
            new Object[] { "ECE", aTextCableHull, aTextWireCoil, 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'C',
                MTEBasicMachineWithRecipe.X.COIL_HEATING },
            2);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_E_Furnace.get(1),
            new Object[] { "ECE", aTextCableHull, aTextWireCoil, 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'C',
                MTEBasicMachineWithRecipe.X.COIL_HEATING },
            3);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_E_Furnace.get(1),
            new Object[] { "ECE", aTextCableHull, aTextWireCoil, 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'C',
                MTEBasicMachineWithRecipe.X.COIL_HEATING },
            4);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_E_Furnace.get(1),
            new Object[] { "ECE", aTextCableHull, aTextWireCoil, 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'C',
                MTEBasicMachineWithRecipe.X.COIL_HEATING },
            5);

    }

    private static void registerElectrolyser() {
        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_Electrolyzer.get(1),
            new Object[] { "IGI", "IMI", "CWC", 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.MOTOR, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE, 'I', OrePrefixes.wireGt01.get(Materials.Gold), 'G',
                MTEBasicMachineWithRecipe.X.GLASS },
            1);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_Electrolyzer.get(1),
            new Object[] { "IGI", "IMI", "CWC", 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.MOTOR, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE, 'I', OrePrefixes.wireGt01.get(Materials.Silver), 'G',
                MTEBasicMachineWithRecipe.X.GLASS },
            2);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_Electrolyzer.get(1),
            new Object[] { "IGI", "IMI", "CWC", 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.MOTOR, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE, 'I', OrePrefixes.wireGt01.get(Materials.Electrum), 'G',
                MTEBasicMachineWithRecipe.X.GLASS },
            3);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_Electrolyzer.get(1),
            new Object[] { "IGI", "IMI", "CWC", 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.MOTOR, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE, 'I', OrePrefixes.wireGt01.get(Materials.Platinum), 'G',
                MTEBasicMachineWithRecipe.X.GLASS },
            4);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_Electrolyzer.get(1),
            new Object[] { "IGI", "IMI", "CWC", 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.MOTOR, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE, 'I', OrePrefixes.wireGt01.get(Materials.HSSG), 'G',
                MTEBasicMachineWithRecipe.X.GLASS },
            5);

    }

    private static void registerElectromagneticSeparator() {
        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_ElectromagneticSeparator.get(1),
            new Object[] { "VWZ", "WMS", "CWZ", 'M', MTEBasicMachineWithRecipe.X.HULL, 'S',
                MTEBasicMachineWithRecipe.X.STICK_ELECTROMAGNETIC, 'Z', MTEBasicMachineWithRecipe.X.COIL_ELECTRIC, 'V',
                MTEBasicMachineWithRecipe.X.CONVEYOR, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE },
            1);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_ElectromagneticSeparator.get(1),
            new Object[] { "VWZ", "WMS", "CWZ", 'M', MTEBasicMachineWithRecipe.X.HULL, 'S',
                MTEBasicMachineWithRecipe.X.STICK_ELECTROMAGNETIC, 'Z', MTEBasicMachineWithRecipe.X.COIL_ELECTRIC, 'V',
                MTEBasicMachineWithRecipe.X.CONVEYOR, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE },
            2);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_ElectromagneticSeparator.get(1),
            new Object[] { "VWZ", "WMS", "CWZ", 'M', MTEBasicMachineWithRecipe.X.HULL, 'S',
                MTEBasicMachineWithRecipe.X.STICK_ELECTROMAGNETIC, 'Z', MTEBasicMachineWithRecipe.X.COIL_ELECTRIC, 'V',
                MTEBasicMachineWithRecipe.X.CONVEYOR, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE },
            3);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_ElectromagneticSeparator.get(1),
            new Object[] { "VWZ", "WMS", "CWZ", 'M', MTEBasicMachineWithRecipe.X.HULL, 'S',
                MTEBasicMachineWithRecipe.X.STICK_ELECTROMAGNETIC, 'Z', MTEBasicMachineWithRecipe.X.COIL_ELECTRIC, 'V',
                MTEBasicMachineWithRecipe.X.CONVEYOR, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE },
            4);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_ElectromagneticSeparator.get(1),
            new Object[] { "VWZ", "WMS", "CWZ", 'M', MTEBasicMachineWithRecipe.X.HULL, 'S',
                MTEBasicMachineWithRecipe.X.STICK_ELECTROMAGNETIC, 'Z', MTEBasicMachineWithRecipe.X.COIL_ELECTRIC, 'V',
                MTEBasicMachineWithRecipe.X.CONVEYOR, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE },
            5);

    }

    private static void registerExtractor() {
        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_Extractor.get(1),
            new Object[] { "GCG", "EMP", aTextWireCoil, 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.PISTON, 'P', MTEBasicMachineWithRecipe.X.PUMP, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'G',
                MTEBasicMachineWithRecipe.X.GLASS },
            1);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_Extractor.get(1),
            new Object[] { "GCG", "EMP", aTextWireCoil, 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.PISTON, 'P', MTEBasicMachineWithRecipe.X.PUMP, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'G',
                MTEBasicMachineWithRecipe.X.GLASS },
            2);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_Extractor.get(1),
            new Object[] { "GCG", "EMP", aTextWireCoil, 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.PISTON, 'P', MTEBasicMachineWithRecipe.X.PUMP, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'G',
                MTEBasicMachineWithRecipe.X.GLASS },
            3);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_Extractor.get(1),
            new Object[] { "GCG", "EMP", aTextWireCoil, 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.PISTON, 'P', MTEBasicMachineWithRecipe.X.PUMP, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'G',
                MTEBasicMachineWithRecipe.X.GLASS },
            4);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_Extractor.get(1),
            new Object[] { "GCG", "EMP", aTextWireCoil, 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.PISTON, 'P', MTEBasicMachineWithRecipe.X.PUMP, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'G',
                MTEBasicMachineWithRecipe.X.GLASS },
            5);

    }

    private static void registerExtruder() {
        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_Extruder.get(1),
            new Object[] { "CCE", "XMP", "CCE", 'M', MTEBasicMachineWithRecipe.X.HULL, 'X',
                MTEBasicMachineWithRecipe.X.PISTON, 'E', MTEBasicMachineWithRecipe.X.CIRCUIT, 'P',
                MTEBasicMachineWithRecipe.X.PIPE, 'C', MTEBasicMachineWithRecipe.X.COIL_HEATING_DOUBLE },
            1);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_Extruder.get(1),
            new Object[] { "CCE", "XMP", "CCE", 'M', MTEBasicMachineWithRecipe.X.HULL, 'X',
                MTEBasicMachineWithRecipe.X.PISTON, 'E', MTEBasicMachineWithRecipe.X.CIRCUIT, 'P',
                MTEBasicMachineWithRecipe.X.PIPE, 'C', MTEBasicMachineWithRecipe.X.COIL_HEATING_DOUBLE },
            2);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_Extruder.get(1),
            new Object[] { "CCE", "XMP", "CCE", 'M', MTEBasicMachineWithRecipe.X.HULL, 'X',
                MTEBasicMachineWithRecipe.X.PISTON, 'E', MTEBasicMachineWithRecipe.X.CIRCUIT, 'P',
                MTEBasicMachineWithRecipe.X.PIPE, 'C', MTEBasicMachineWithRecipe.X.COIL_HEATING_DOUBLE },
            3);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_Extruder.get(1),
            new Object[] { "CCE", "XMP", "CCE", 'M', MTEBasicMachineWithRecipe.X.HULL, 'X',
                MTEBasicMachineWithRecipe.X.PISTON, 'E', MTEBasicMachineWithRecipe.X.CIRCUIT, 'P',
                MTEBasicMachineWithRecipe.X.PIPE, 'C', MTEBasicMachineWithRecipe.X.COIL_HEATING_DOUBLE },
            4);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_Extruder.get(1),
            new Object[] { "CCE", "XMP", "CCE", 'M', MTEBasicMachineWithRecipe.X.HULL, 'X',
                MTEBasicMachineWithRecipe.X.PISTON, 'E', MTEBasicMachineWithRecipe.X.CIRCUIT, 'P',
                MTEBasicMachineWithRecipe.X.PIPE, 'C', MTEBasicMachineWithRecipe.X.COIL_HEATING_DOUBLE },
            5);

    }

    private static void registerFermenter() {
        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_Fermenter.get(1),
            new Object[] { aTextWirePump, "GMG", aTextWireCoil, 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PUMP, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE, 'G', MTEBasicMachineWithRecipe.X.GLASS },
            1);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_Fermenter.get(1),
            new Object[] { aTextWirePump, "GMG", aTextWireCoil, 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PUMP, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE, 'G', MTEBasicMachineWithRecipe.X.GLASS },
            2);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_Fermenter.get(1),
            new Object[] { aTextWirePump, "GMG", aTextWireCoil, 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PUMP, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE, 'G', MTEBasicMachineWithRecipe.X.GLASS },
            3);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_Fermenter.get(1),
            new Object[] { aTextWirePump, "GMG", aTextWireCoil, 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PUMP, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE, 'G', MTEBasicMachineWithRecipe.X.GLASS },
            4);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_Fermenter.get(1),
            new Object[] { aTextWirePump, "GMG", aTextWireCoil, 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PUMP, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE, 'G', MTEBasicMachineWithRecipe.X.GLASS },
            5);

    }

    private static void registerFluidExtractor() {
        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_FluidExtractor.get(1),
            new Object[] { "GEG", "TPT", "CMC", 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.PISTON, 'P', MTEBasicMachineWithRecipe.X.PUMP, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'T', MTEBasicMachineWithRecipe.X.WIRE, 'G',
                MTEBasicMachineWithRecipe.X.GLASS },
            1);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_FluidExtractor.get(1),
            new Object[] { "GEG", "TPT", "CMC", 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.PISTON, 'P', MTEBasicMachineWithRecipe.X.PUMP, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'T', MTEBasicMachineWithRecipe.X.WIRE, 'G',
                MTEBasicMachineWithRecipe.X.GLASS },
            2);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_FluidExtractor.get(1),
            new Object[] { "GEG", "TPT", "CMC", 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.PISTON, 'P', MTEBasicMachineWithRecipe.X.PUMP, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'T', MTEBasicMachineWithRecipe.X.WIRE, 'G',
                MTEBasicMachineWithRecipe.X.GLASS },
            3);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_FluidExtractor.get(1),
            new Object[] { "GEG", "TPT", "CMC", 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.PISTON, 'P', MTEBasicMachineWithRecipe.X.PUMP, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'T', MTEBasicMachineWithRecipe.X.WIRE, 'G',
                MTEBasicMachineWithRecipe.X.GLASS },
            4);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_FluidExtractor.get(1),
            new Object[] { "GEG", "TPT", "CMC", 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.PISTON, 'P', MTEBasicMachineWithRecipe.X.PUMP, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'T', MTEBasicMachineWithRecipe.X.WIRE, 'G',
                MTEBasicMachineWithRecipe.X.GLASS },
            5);

    }

    private static void registerFluidHeater() {
        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_FluidHeater.get(1),
            new Object[] { "OGO", aTextPlateMotor, aTextWireCoil, 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PUMP, 'O', MTEBasicMachineWithRecipe.X.COIL_HEATING_DOUBLE, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'G',
                MTEBasicMachineWithRecipe.X.GLASS },
            1);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_FluidHeater.get(1),
            new Object[] { "OGO", aTextPlateMotor, aTextWireCoil, 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PUMP, 'O', MTEBasicMachineWithRecipe.X.COIL_HEATING_DOUBLE, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'G',
                MTEBasicMachineWithRecipe.X.GLASS },
            2);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_FluidHeater.get(1),
            new Object[] { "OGO", aTextPlateMotor, aTextWireCoil, 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PUMP, 'O', MTEBasicMachineWithRecipe.X.COIL_HEATING_DOUBLE, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'G',
                MTEBasicMachineWithRecipe.X.GLASS },
            3);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_FluidHeater.get(1),
            new Object[] { "OGO", aTextPlateMotor, aTextWireCoil, 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PUMP, 'O', MTEBasicMachineWithRecipe.X.COIL_HEATING_DOUBLE, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'G',
                MTEBasicMachineWithRecipe.X.GLASS },
            4);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_FluidHeater.get(1),
            new Object[] { "OGO", aTextPlateMotor, aTextWireCoil, 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PUMP, 'O', MTEBasicMachineWithRecipe.X.COIL_HEATING_DOUBLE, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'G',
                MTEBasicMachineWithRecipe.X.GLASS },
            5);
    }

    private static void registerFluidSolidifier() {
        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_FluidSolidifier.get(1),
            new Object[] { "PGP", aTextWireHull, "CBC", 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PUMP, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE, 'G', MTEBasicMachineWithRecipe.X.GLASS, 'B',
                OreDictNames.craftingChest },
            1);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_FluidSolidifier.get(1),
            new Object[] { "PGP", aTextWireHull, "CBC", 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PUMP, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE, 'G', MTEBasicMachineWithRecipe.X.GLASS, 'B',
                OreDictNames.craftingChest },
            2);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_FluidSolidifier.get(1),
            new Object[] { "PGP", aTextWireHull, "CBC", 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PUMP, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE, 'G', MTEBasicMachineWithRecipe.X.GLASS, 'B',
                OreDictNames.craftingChest },
            3);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_FluidSolidifier.get(1),
            new Object[] { "PGP", aTextWireHull, "CBC", 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PUMP, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE, 'G', MTEBasicMachineWithRecipe.X.GLASS, 'B',
                OreDictNames.craftingChest },
            4);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_FluidSolidifier.get(1),
            new Object[] { "PGP", aTextWireHull, "CBC", 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PUMP, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE, 'G', MTEBasicMachineWithRecipe.X.GLASS, 'B',
                OreDictNames.craftingChest },
            5);

    }

    private static void registerForgeHammer() {
        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_Hammer.get(1),
            new Object[] { aTextWirePump, aTextCableHull, "WAW", 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PISTON, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE, 'O', MTEBasicMachineWithRecipe.X.COIL_HEATING_DOUBLE, 'A',
                OreDictNames.craftingAnvil },
            1);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_Hammer.get(1),
            new Object[] { aTextWirePump, aTextCableHull, "WAW", 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PISTON, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE, 'O', MTEBasicMachineWithRecipe.X.COIL_HEATING_DOUBLE, 'A',
                OreDictNames.craftingAnvil },
            2);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_Hammer.get(1),
            new Object[] { aTextWirePump, aTextCableHull, "WAW", 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PISTON, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE, 'O', MTEBasicMachineWithRecipe.X.COIL_HEATING_DOUBLE, 'A',
                OreDictNames.craftingAnvil },
            3);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_Hammer.get(1),
            new Object[] { aTextWirePump, aTextCableHull, "WAW", 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PISTON, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE, 'O', MTEBasicMachineWithRecipe.X.COIL_HEATING_DOUBLE, 'A',
                OreDictNames.craftingAnvil },
            4);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_Hammer.get(1),
            new Object[] { aTextWirePump, aTextCableHull, "WAW", 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PISTON, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE, 'O', MTEBasicMachineWithRecipe.X.COIL_HEATING_DOUBLE, 'A',
                OreDictNames.craftingAnvil },
            5);

    }

    private static void registerFormingPress() {
        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_Press.get(1),
            new Object[] { aTextWirePump, aTextCableHull, aTextWirePump, 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PISTON, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE },
            1);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_Press.get(1),
            new Object[] { aTextWirePump, aTextCableHull, aTextWirePump, 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PISTON, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE },
            2);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_Press.get(1),
            new Object[] { aTextWirePump, aTextCableHull, aTextWirePump, 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PISTON, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE },
            3);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_Press.get(1),
            new Object[] { aTextWirePump, aTextCableHull, aTextWirePump, 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PISTON, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE },
            4);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_Press.get(1),
            new Object[] { aTextWirePump, aTextCableHull, aTextWirePump, 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PISTON, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE },
            5);
    }

    private static void registerLaserEngraver() {
        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_LaserEngraver.get(1),
            new Object[] { "PEP", aTextCableHull, aTextWireCoil, 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.EMITTER, 'P', MTEBasicMachineWithRecipe.X.PISTON, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE },
            1);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_LaserEngraver.get(1),
            new Object[] { "PEP", aTextCableHull, aTextWireCoil, 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.EMITTER, 'P', MTEBasicMachineWithRecipe.X.PISTON, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE },
            2);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_LaserEngraver.get(1),
            new Object[] { "PEP", aTextCableHull, aTextWireCoil, 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.EMITTER, 'P', MTEBasicMachineWithRecipe.X.PISTON, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE },
            3);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_LaserEngraver.get(1),
            new Object[] { "PEP", aTextCableHull, aTextWireCoil, 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.EMITTER, 'P', MTEBasicMachineWithRecipe.X.PISTON, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE },
            4);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_LaserEngraver.get(1),
            new Object[] { "PEP", aTextCableHull, aTextWireCoil, 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.EMITTER, 'P', MTEBasicMachineWithRecipe.X.PISTON, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE },
            5);

    }

    private static void registerLathe() {
        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_Lathe.get(1),
            new Object[] { aTextWireCoil, "EMD", "CWP", 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.MOTOR, 'P', MTEBasicMachineWithRecipe.X.PISTON, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'D',
                OrePrefixes.gem.get(Materials.Diamond) },
            1);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_Lathe.get(1),
            new Object[] { aTextWireCoil, "EMD", "CWP", 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.MOTOR, 'P', MTEBasicMachineWithRecipe.X.PISTON, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'D',
                OrePrefixes.gemFlawless.get(Materials.Diamond) },
            2);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_Lathe.get(1),
            new Object[] { aTextWireCoil, "EMD", "CWP", 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.MOTOR, 'P', MTEBasicMachineWithRecipe.X.PISTON, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'D',
                OreDictNames.craftingIndustrialDiamond },
            3);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_Lathe.get(1),
            new Object[] { aTextWireCoil, "EMD", "CWP", 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.MOTOR, 'P', MTEBasicMachineWithRecipe.X.PISTON, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'D',
                OreDictNames.craftingIndustrialDiamond },
            4);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_Lathe.get(1),
            new Object[] { aTextWireCoil, "EMD", "CWP", 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.MOTOR, 'P', MTEBasicMachineWithRecipe.X.PISTON, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'D',
                OreDictNames.craftingIndustrialDiamond },
            5);

    }

    private static void registerMacerator() {
        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_Macerator.get(1),
            new Object[] { "PEG", "WWM", "CCW", 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.MOTOR, 'P', MTEBasicMachineWithRecipe.X.PISTON, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'G',
                OrePrefixes.gem.get(Materials.Diamond) },
            1);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_Macerator.get(1),
            new Object[] { "PEG", "WWM", "CCW", 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.MOTOR, 'P', MTEBasicMachineWithRecipe.X.PISTON, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'G',
                OrePrefixes.gemFlawless.get(Materials.Diamond) },
            2);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_Macerator.get(1),
            new Object[] { "PEG", "WWM", "CCW", 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.MOTOR, 'P', MTEBasicMachineWithRecipe.X.PISTON, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'G',
                OreDictNames.craftingGrinder },
            3);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_Macerator.get(1),
            new Object[] { "PEG", "WWM", "CCW", 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.MOTOR, 'P', MTEBasicMachineWithRecipe.X.PISTON, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'G',
                OreDictNames.craftingGrinder },
            4);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_Macerator.get(1),
            new Object[] { "PEG", "WWM", "CCW", 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.MOTOR, 'P', MTEBasicMachineWithRecipe.X.PISTON, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'G',
                OreDictNames.craftingGrinder },
            5);

    }

    private static void registerMatterAmplifier() {
        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_Amplifab.get(1),
            new Object[] { aTextWirePump, aTextPlateMotor, "CPC", 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PUMP, 'C', MTEBasicMachineWithRecipe.X.BETTER_CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE4 },
            1);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_Amplifab.get(1),
            new Object[] { aTextWirePump, aTextPlateMotor, "CPC", 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PUMP, 'C', MTEBasicMachineWithRecipe.X.BETTER_CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE4 },
            2);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_Amplifab.get(1),
            new Object[] { aTextWirePump, aTextPlateMotor, "CPC", 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PUMP, 'C', MTEBasicMachineWithRecipe.X.BETTER_CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE4 },
            3);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_Amplifab.get(1),
            new Object[] { aTextWirePump, aTextPlateMotor, "CPC", 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PUMP, 'C', MTEBasicMachineWithRecipe.X.BETTER_CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE4 },
            4);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_Amplifab.get(1),
            new Object[] { aTextWirePump, aTextPlateMotor, "CPC", 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PUMP, 'C', MTEBasicMachineWithRecipe.X.BETTER_CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE4 },
            5);

    }

    private static void registerMicrowave() {
        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_Microwave.get(1),
            new Object[] { "LWC", "LMR", "LEC", 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.MOTOR, 'R', MTEBasicMachineWithRecipe.X.EMITTER, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'L',
                OrePrefixes.plate.get(Materials.Lead) },
            1);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_Microwave.get(1),
            new Object[] { "LWC", "LMR", "LEC", 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.MOTOR, 'R', MTEBasicMachineWithRecipe.X.EMITTER, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'L',
                OrePrefixes.plate.get(Materials.Lead) },
            2);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_Microwave.get(1),
            new Object[] { "LWC", "LMR", "LEC", 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.MOTOR, 'R', MTEBasicMachineWithRecipe.X.EMITTER, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'L',
                OrePrefixes.plate.get(Materials.Lead) },
            3);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_Microwave.get(1),
            new Object[] { "LWC", "LMR", "LEC", 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.MOTOR, 'R', MTEBasicMachineWithRecipe.X.EMITTER, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'L',
                OrePrefixes.plate.get(Materials.Lead) },
            4);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_Microwave.get(1),
            new Object[] { "LWC", "LMR", "LEC", 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.MOTOR, 'R', MTEBasicMachineWithRecipe.X.EMITTER, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'L',
                OrePrefixes.plate.get(Materials.Lead) },
            5);

    }

    private static void registerMixer() {
        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_Mixer.get(1),
            new Object[] { "GRG", "GEG", aTextCableHull, 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.MOTOR, 'R', MTEBasicMachineWithRecipe.X.ROTOR, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'G', MTEBasicMachineWithRecipe.X.GLASS },
            1);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_Mixer.get(1),
            new Object[] { "GRG", "GEG", aTextCableHull, 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.MOTOR, 'R', MTEBasicMachineWithRecipe.X.ROTOR, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'G', MTEBasicMachineWithRecipe.X.GLASS },
            2);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_Mixer.get(1),
            new Object[] { "GRG", "GEG", aTextCableHull, 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.MOTOR, 'R', MTEBasicMachineWithRecipe.X.ROTOR, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'G', MTEBasicMachineWithRecipe.X.GLASS },
            3);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_Mixer.get(1),
            new Object[] { "GRG", "GEG", aTextCableHull, 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.MOTOR, 'R', MTEBasicMachineWithRecipe.X.ROTOR, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'G', MTEBasicMachineWithRecipe.X.GLASS },
            4);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_Mixer.get(1),
            new Object[] { "GRG", "GEG", aTextCableHull, 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.MOTOR, 'R', MTEBasicMachineWithRecipe.X.ROTOR, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'G', MTEBasicMachineWithRecipe.X.GLASS },
            5);
    }

    private static void registerOreWasher() {
        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_OreWasher.get(1),
            new Object[] { "RGR", "CEC", aTextWireHull, 'M', MTEBasicMachineWithRecipe.X.HULL, 'R',
                MTEBasicMachineWithRecipe.X.ROTOR, 'E', MTEBasicMachineWithRecipe.X.MOTOR, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'G',
                MTEBasicMachineWithRecipe.X.PUMP },
            1);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_OreWasher.get(1),
            new Object[] { "RGR", "CEC", aTextWireHull, 'M', MTEBasicMachineWithRecipe.X.HULL, 'R',
                MTEBasicMachineWithRecipe.X.ROTOR, 'E', MTEBasicMachineWithRecipe.X.MOTOR, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'G',
                MTEBasicMachineWithRecipe.X.PUMP },
            2);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_OreWasher.get(1),
            new Object[] { "RGR", "CEC", aTextWireHull, 'M', MTEBasicMachineWithRecipe.X.HULL, 'R',
                MTEBasicMachineWithRecipe.X.ROTOR, 'E', MTEBasicMachineWithRecipe.X.MOTOR, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'G',
                MTEBasicMachineWithRecipe.X.PUMP },
            3);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_OreWasher.get(1),
            new Object[] { "RGR", "CEC", aTextWireHull, 'M', MTEBasicMachineWithRecipe.X.HULL, 'R',
                MTEBasicMachineWithRecipe.X.ROTOR, 'E', MTEBasicMachineWithRecipe.X.MOTOR, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'G',
                MTEBasicMachineWithRecipe.X.PUMP },
            4);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_OreWasher.get(1),
            new Object[] { "RGR", "CEC", aTextWireHull, 'M', MTEBasicMachineWithRecipe.X.HULL, 'R',
                MTEBasicMachineWithRecipe.X.ROTOR, 'E', MTEBasicMachineWithRecipe.X.MOTOR, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'G',
                MTEBasicMachineWithRecipe.X.PUMP },
            5);

    }

    private static void registerOven() {
        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_Oven.get(1),
            new Object[] { "CEC", aTextCableHull, "WEW", 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'C',
                MTEBasicMachineWithRecipe.X.COIL_HEATING },
            1);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_Oven.get(1),
            new Object[] { "CEC", aTextCableHull, "WEW", 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'C',
                MTEBasicMachineWithRecipe.X.COIL_HEATING },
            2);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_Oven.get(1),
            new Object[] { "CEC", aTextCableHull, "WEW", 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'C',
                MTEBasicMachineWithRecipe.X.COIL_HEATING },
            3);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_Oven.get(1),
            new Object[] { "CEC", aTextCableHull, "WEW", 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'C',
                MTEBasicMachineWithRecipe.X.COIL_HEATING },
            4);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_Oven.get(1),
            new Object[] { "CEC", aTextCableHull, "WEW", 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'C',
                MTEBasicMachineWithRecipe.X.COIL_HEATING },
            5);

    }

    private static void registerPlasmaArcFurnace() {
        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_PlasmaArcFurnace.get(1),
            new Object[] { "WGW", aTextCableHull, "TPT", 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PLATE, 'C', MTEBasicMachineWithRecipe.X.BETTER_CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE4, 'T', MTEBasicMachineWithRecipe.X.PUMP, 'G',
                OrePrefixes.cell.get(Materials.Graphite) },
            1);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_PlasmaArcFurnace.get(1),
            new Object[] { "WGW", aTextCableHull, "TPT", 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PLATE, 'C', MTEBasicMachineWithRecipe.X.BETTER_CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE4, 'T', MTEBasicMachineWithRecipe.X.PUMP, 'G',
                OrePrefixes.cell.get(Materials.Graphite) },
            2);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_PlasmaArcFurnace.get(1),
            new Object[] { "WGW", aTextCableHull, "TPT", 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PLATE, 'C', MTEBasicMachineWithRecipe.X.BETTER_CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE4, 'T', MTEBasicMachineWithRecipe.X.PUMP, 'G',
                OrePrefixes.cell.get(Materials.Graphite) },
            3);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_PlasmaArcFurnace.get(1),
            new Object[] { "WGW", aTextCableHull, "TPT", 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PLATE, 'C', MTEBasicMachineWithRecipe.X.BETTER_CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE4, 'T', MTEBasicMachineWithRecipe.X.PUMP, 'G',
                OrePrefixes.cell.get(Materials.Graphite) },
            4);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_PlasmaArcFurnace.get(1),
            new Object[] { "WGW", aTextCableHull, "TPT", 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PLATE, 'C', MTEBasicMachineWithRecipe.X.BETTER_CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE4, 'T', MTEBasicMachineWithRecipe.X.PUMP, 'G',
                OrePrefixes.cell.get(Materials.Graphite) },
            5);

    }

    private static void registerPolarizer() {
        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_Polarizer.get(1),
            new Object[] { "ZSZ", aTextWireHull, "ZSZ", 'M', MTEBasicMachineWithRecipe.X.HULL, 'S',
                MTEBasicMachineWithRecipe.X.STICK_ELECTROMAGNETIC, 'Z', MTEBasicMachineWithRecipe.X.COIL_ELECTRIC, 'W',
                MTEBasicMachineWithRecipe.X.WIRE },
            1);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_Polarizer.get(1),
            new Object[] { "ZSZ", aTextWireHull, "ZSZ", 'M', MTEBasicMachineWithRecipe.X.HULL, 'S',
                MTEBasicMachineWithRecipe.X.STICK_ELECTROMAGNETIC, 'Z', MTEBasicMachineWithRecipe.X.COIL_ELECTRIC, 'W',
                MTEBasicMachineWithRecipe.X.WIRE },
            2);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_Polarizer.get(1),
            new Object[] { "ZSZ", aTextWireHull, "ZSZ", 'M', MTEBasicMachineWithRecipe.X.HULL, 'S',
                MTEBasicMachineWithRecipe.X.STICK_ELECTROMAGNETIC, 'Z', MTEBasicMachineWithRecipe.X.COIL_ELECTRIC, 'W',
                MTEBasicMachineWithRecipe.X.WIRE },
            3);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_Polarizer.get(1),
            new Object[] { "ZSZ", aTextWireHull, "ZSZ", 'M', MTEBasicMachineWithRecipe.X.HULL, 'S',
                MTEBasicMachineWithRecipe.X.STICK_ELECTROMAGNETIC, 'Z', MTEBasicMachineWithRecipe.X.COIL_ELECTRIC, 'W',
                MTEBasicMachineWithRecipe.X.WIRE },
            4);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_Polarizer.get(1),
            new Object[] { "ZSZ", aTextWireHull, "ZSZ", 'M', MTEBasicMachineWithRecipe.X.HULL, 'S',
                MTEBasicMachineWithRecipe.X.STICK_ELECTROMAGNETIC, 'Z', MTEBasicMachineWithRecipe.X.COIL_ELECTRIC, 'W',
                MTEBasicMachineWithRecipe.X.WIRE },
            5);

    }

    private static void registerPrinter() {
        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_Printer.get(1),
            new Object[] { aTextMotorWire, aTextCableHull, "WEW", 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.MOTOR, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE },
            1);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_Printer.get(1),
            new Object[] { aTextMotorWire, aTextCableHull, "WEW", 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.MOTOR, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE },
            2);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_Printer.get(1),
            new Object[] { aTextMotorWire, aTextCableHull, "WEW", 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.MOTOR, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE },
            3);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_Printer.get(1),
            new Object[] { aTextMotorWire, aTextCableHull, "WEW", 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.MOTOR, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE },
            4);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_Printer.get(1),
            new Object[] { aTextMotorWire, aTextCableHull, "WEW", 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.MOTOR, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE },
            5);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LuV_Printer.get(1),
            new Object[] { aTextMotorWire, aTextCableHull, "WEW", 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.MOTOR, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE },
            6);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_ZPM_Printer.get(1),
            new Object[] { aTextMotorWire, aTextCableHull, "WEW", 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.MOTOR, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE },
            7);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_UV_Printer.get(1),
            new Object[] { aTextMotorWire, aTextCableHull, "WEW", 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.MOTOR, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE },
            8);

    }

    private static void registerRecycler() {
        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_Recycler.get(1),
            new Object[] { "GCG", aTextPlateMotor, aTextWireCoil, 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PISTON, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE, 'G', OrePrefixes.dust.get(Materials.Glowstone) },
            1);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_Recycler.get(1),
            new Object[] { "GCG", aTextPlateMotor, aTextWireCoil, 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PISTON, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE, 'G', OrePrefixes.dust.get(Materials.Glowstone) },
            2);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_Recycler.get(1),
            new Object[] { "GCG", aTextPlateMotor, aTextWireCoil, 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PISTON, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE, 'G', OrePrefixes.dust.get(Materials.Glowstone) },
            3);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_Recycler.get(1),
            new Object[] { "GCG", aTextPlateMotor, aTextWireCoil, 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PISTON, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE, 'G', OrePrefixes.dust.get(Materials.Glowstone) },
            4);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_Recycler.get(1),
            new Object[] { "GCG", aTextPlateMotor, aTextWireCoil, 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PISTON, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE, 'G', OrePrefixes.dust.get(Materials.Glowstone) },
            5);

    }

    // This method is for all the structure rework multi recipes
    // Maybe after some point in time, it can be moved to coremod as well.
    private static void registerReworkMTERecipes() {

        // Industrial Centrifuge
        GTModHandler.addCraftingRecipe(
            ItemList.IndustrialCentrifuge.get(1),
            new Object[] { "ABA", "CDC", "EFE", 'A', "circuitData", 'B',
                OrePrefixes.pipeHuge.get(Materials.StainlessSteel), 'C', MaterialsAlloy.MARAGING250.getPlate(1), 'D',
                ItemList.Machine_EV_Centrifuge, 'E', MaterialsAlloy.INCONEL_792.getPlate(1), 'F', ItemList.Casing_EV });

        // Amazon Warehousing Depot
        GTModHandler.addCraftingRecipe(
            ItemList.IndustrialPackager.get(1),
            new Object[] { "DCD", "PMP", "ODO", 'D', GregtechItemList.Casing_AmazonWarehouse, 'C', "circuitElite", 'P',
                ItemList.Electric_Piston_IV, 'M', ItemList.Machine_IV_Boxinator, 'O', ItemList.Conveyor_Module_IV });

        // Industrial Wire Factory
        GTModHandler.addCraftingRecipe(
            ItemList.IndustrialWireFactory.get(1),
            new Object[] { "PHP", "CMC", "PHP", 'P', OrePrefixes.plate.get(Materials.BlueSteel), 'H',
                ItemList.Casing_IV, 'C', "circuitElite", 'M', ItemList.Machine_IV_Wiremill });

    }

    // This method is for all the structure rework shapeless crafing migration recipes
    // for the 2.9 -> next major version cycle
    // Maybe after some point in time, it can be moved to coremod as well.
    // TODO delete after the next major version after 2.9
    private static void registerReworkMigrationRecipes() {

        // Industrial Wire Factory Conversion Recipe
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.IndustrialWireFactory.get(1),
            new Object[] { GregtechItemList.Industrial_WireFactory });

        // Amazon Packager Conversion Recipe
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.IndustrialPackager.get(1),
            new Object[] { GregtechItemList.Amazon_Warehouse_Controller });

        // Industrial Centrifuge Conversion Recipe
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.IndustrialCentrifuge.get(1),
            new Object[] { GregtechItemList.Industrial_Centrifuge });
    }

    private static void registerSifter() {
        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_Sifter.get(1),
            new Object[] { "WFW", aTextPlateMotor, "CFC", 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PISTON, 'F', OreDictNames.craftingFilter, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE },
            1);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_Sifter.get(1),
            new Object[] { "WFW", aTextPlateMotor, "CFC", 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PISTON, 'F', OreDictNames.craftingFilter, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE },
            2);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_Sifter.get(1),
            new Object[] { "WFW", aTextPlateMotor, "CFC", 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PISTON, 'F', OreDictNames.craftingFilter, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE },
            3);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_Sifter.get(1),
            new Object[] { "WFW", aTextPlateMotor, "CFC", 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PISTON, 'F', OreDictNames.craftingFilter, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE },
            4);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_Sifter.get(1),
            new Object[] { "WFW", aTextPlateMotor, "CFC", 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                MTEBasicMachineWithRecipe.X.PISTON, 'F', OreDictNames.craftingFilter, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE },
            5);

    }

    private static void registerThermalCentrifuge() {
        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_ThermalCentrifuge.get(1),
            new Object[] { "CEC", "OMO", "WEW", 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.MOTOR, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE, 'O', MTEBasicMachineWithRecipe.X.COIL_HEATING_DOUBLE },
            1);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_ThermalCentrifuge.get(1),
            new Object[] { "CEC", "OMO", "WEW", 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.MOTOR, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE, 'O', MTEBasicMachineWithRecipe.X.COIL_HEATING_DOUBLE },
            2);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_ThermalCentrifuge.get(1),
            new Object[] { "CEC", "OMO", "WEW", 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.MOTOR, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE, 'O', MTEBasicMachineWithRecipe.X.COIL_HEATING_DOUBLE },
            3);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_ThermalCentrifuge.get(1),
            new Object[] { "CEC", "OMO", "WEW", 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.MOTOR, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE, 'O', MTEBasicMachineWithRecipe.X.COIL_HEATING_DOUBLE },
            4);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_ThermalCentrifuge.get(1),
            new Object[] { "CEC", "OMO", "WEW", 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.MOTOR, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE, 'O', MTEBasicMachineWithRecipe.X.COIL_HEATING_DOUBLE },
            5);

    }

    private static void registerUnpackager() {
        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_Unboxinator.get(1),
            new Object[] { "BCB", "VMR", aTextWireCoil, 'M', MTEBasicMachineWithRecipe.X.HULL, 'R',
                MTEBasicMachineWithRecipe.X.ROBOT_ARM, 'V', MTEBasicMachineWithRecipe.X.CONVEYOR, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'B',
                OreDictNames.craftingChest },
            1);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_Unboxinator.get(1),
            new Object[] { "BCB", "VMR", aTextWireCoil, 'M', MTEBasicMachineWithRecipe.X.HULL, 'R',
                MTEBasicMachineWithRecipe.X.ROBOT_ARM, 'V', MTEBasicMachineWithRecipe.X.CONVEYOR, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'B',
                OreDictNames.craftingChest },
            2);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_Unboxinator.get(1),
            new Object[] { "BCB", "VMR", aTextWireCoil, 'M', MTEBasicMachineWithRecipe.X.HULL, 'R',
                MTEBasicMachineWithRecipe.X.ROBOT_ARM, 'V', MTEBasicMachineWithRecipe.X.CONVEYOR, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'B',
                OreDictNames.craftingChest },
            3);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_Unboxinator.get(1),
            new Object[] { "BCB", "VMR", aTextWireCoil, 'M', MTEBasicMachineWithRecipe.X.HULL, 'R',
                MTEBasicMachineWithRecipe.X.ROBOT_ARM, 'V', MTEBasicMachineWithRecipe.X.CONVEYOR, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'B',
                OreDictNames.craftingChest },
            4);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_Unboxinator.get(1),
            new Object[] { "BCB", "VMR", aTextWireCoil, 'M', MTEBasicMachineWithRecipe.X.HULL, 'R',
                MTEBasicMachineWithRecipe.X.ROBOT_ARM, 'V', MTEBasicMachineWithRecipe.X.CONVEYOR, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'B',
                OreDictNames.craftingChest },
            5);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LuV_Unboxinator.get(1),
            new Object[] { "BCB", "VMR", aTextWireCoil, 'M', MTEBasicMachineWithRecipe.X.HULL, 'R',
                MTEBasicMachineWithRecipe.X.ROBOT_ARM, 'V', MTEBasicMachineWithRecipe.X.CONVEYOR, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'B',
                OreDictNames.craftingChest },
            6);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_ZPM_Unboxinator.get(1),
            new Object[] { "BCB", "VMR", aTextWireCoil, 'M', MTEBasicMachineWithRecipe.X.HULL, 'R',
                MTEBasicMachineWithRecipe.X.ROBOT_ARM, 'V', MTEBasicMachineWithRecipe.X.CONVEYOR, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'B',
                OreDictNames.craftingChest },
            7);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_UV_Unboxinator.get(1),
            new Object[] { "BCB", "VMR", aTextWireCoil, 'M', MTEBasicMachineWithRecipe.X.HULL, 'R',
                MTEBasicMachineWithRecipe.X.ROBOT_ARM, 'V', MTEBasicMachineWithRecipe.X.CONVEYOR, 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'W', MTEBasicMachineWithRecipe.X.WIRE, 'B',
                OreDictNames.craftingChest },
            8);
    }

    private static void registerWiremill() {
        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_Wiremill.get(1),
            new Object[] { aTextMotorWire, aTextCableHull, aTextMotorWire, 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.MOTOR, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE },
            1);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_Wiremill.get(1),
            new Object[] { aTextMotorWire, aTextCableHull, aTextMotorWire, 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.MOTOR, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE },
            2);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_Wiremill.get(1),
            new Object[] { aTextMotorWire, aTextCableHull, aTextMotorWire, 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.MOTOR, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE },
            3);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_EV_Wiremill.get(1),
            new Object[] { aTextMotorWire, aTextCableHull, aTextMotorWire, 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.MOTOR, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE },
            4);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_IV_Wiremill.get(1),
            new Object[] { aTextMotorWire, aTextCableHull, aTextMotorWire, 'M', MTEBasicMachineWithRecipe.X.HULL, 'E',
                MTEBasicMachineWithRecipe.X.MOTOR, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                MTEBasicMachineWithRecipe.X.WIRE },
            5);
    }

    private static void registerDrawerFramer() {

        // only register if storage drawers is loaded
        if (!StorageDrawers.isModLoaded()) return;

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_LV_DrawerFramer.get(1L),
            new Object[] { "PDP", "RHR", "PCP", 'H', MTEBasicMachineWithRecipe.X.HULL, 'R',
                MTEBasicMachineWithRecipe.X.ROBOT_ARM, 'P', OrePrefixes.plate.get(Materials.Steel), 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'D', OreDictNames.craftingChest },
            1);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_MV_DrawerFramer.get(1L),
            new Object[] { "PDP", "RHR", "PCP", 'H', MTEBasicMachineWithRecipe.X.HULL, 'R',
                MTEBasicMachineWithRecipe.X.ROBOT_ARM, 'P', OrePrefixes.plate.get(Materials.Aluminium), 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'D', OreDictNames.craftingChest },
            2);

        GTModHandler.addMachineCraftingRecipe(
            ItemList.Machine_HV_DrawerFramer.get(1L),
            new Object[] { "PDP", "RHR", "PCP", 'H', MTEBasicMachineWithRecipe.X.HULL, 'R',
                MTEBasicMachineWithRecipe.X.ROBOT_ARM, 'P', OrePrefixes.plate.get(Materials.StainlessSteel), 'C',
                MTEBasicMachineWithRecipe.X.CIRCUIT, 'D', OreDictNames.craftingChest },
            3);
    }

    private static void registerShapedCraftingRecipes() {
        GTModHandler.addCraftingRecipe(
            ItemList.Casing_Pipe_Polytetrafluoroethylene.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { "PIP", "IFI", "PIP", 'P', OrePrefixes.plate.get(Materials.Polytetrafluoroethylene), 'F',
                OrePrefixes.frameGt.get(Materials.Polytetrafluoroethylene), 'I',
                OrePrefixes.pipeMedium.get(Materials.Polytetrafluoroethylene) });

        GTModHandler.addCraftingRecipe(
            ItemList.Casing_Pipe_Polybenzimidazole.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { "PIP", "IFI", "PIP", 'P', OrePrefixes.plate.get(Materials.Polybenzimidazole), 'F',
                OrePrefixes.frameGt.get(Materials.Polybenzimidazole), 'I',
                OrePrefixes.pipeMedium.get(Materials.Polybenzimidazole) });
        GTModHandler.addCraftingRecipe(
            ItemList.Casing_ULV.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { aTextPlate, aTextPlateWrench, aTextPlate, 'P',
                OrePrefixes.plate.get(Materials.WroughtIron) });
        GTModHandler.addCraftingRecipe(
            ItemList.Casing_LV.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { aTextPlate, aTextPlateWrench, aTextPlate, 'P', OrePrefixes.plate.get(Materials.Steel) });
        GTModHandler.addCraftingRecipe(
            ItemList.Casing_MV.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { aTextPlate, aTextPlateWrench, aTextPlate, 'P', OrePrefixes.plate.get(Materials.Aluminium) });
        GTModHandler.addCraftingRecipe(
            ItemList.Casing_HV.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { aTextPlate, aTextPlateWrench, aTextPlate, 'P',
                OrePrefixes.plate.get(Materials.StainlessSteel) });
        GTModHandler.addCraftingRecipe(
            ItemList.Casing_EV.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { aTextPlate, aTextPlateWrench, aTextPlate, 'P', OrePrefixes.plate.get(Materials.Titanium) });
        GTModHandler.addCraftingRecipe(
            ItemList.Casing_IV.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { aTextPlate, aTextPlateWrench, aTextPlate, 'P',
                OrePrefixes.plate.get(Materials.TungstenSteel) });
        GTModHandler.addCraftingRecipe(
            ItemList.Casing_LuV.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { aTextPlate, aTextPlateWrench, aTextPlate, 'P',
                OrePrefixes.plate.get(ExternalMaterials.getRhodiumPlatedPalladium()) });
        GTModHandler.addCraftingRecipe(
            ItemList.Casing_ZPM.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { aTextPlate, aTextPlateWrench, aTextPlate, 'P', OrePrefixes.plate.get(Materials.Iridium) });
        GTModHandler.addCraftingRecipe(
            ItemList.Casing_UV.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { aTextPlate, aTextPlateWrench, aTextPlate, 'P', OrePrefixes.plate.get(Materials.Osmium) });
        GTModHandler.addCraftingRecipe(
            ItemList.Casing_MAX.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { aTextPlate, aTextPlateWrench, aTextPlate, 'P',
                OrePrefixes.plate.get(Materials.Neutronium) });
        GTModHandler.addCraftingRecipe(
            ItemList.Casing_BronzePlatedBricks.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { "PhP", "PBP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.Bronze), 'B',
                new ItemStack(Blocks.brick_block, 1) });
        GTModHandler.addCraftingRecipe(
            ItemList.Casing_SolidSteel.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { "PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.Steel), 'F',
                OrePrefixes.frameGt.get(Materials.Steel) });
        GTModHandler.addCraftingRecipe(
            ItemList.Casing_StableTitanium.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { "PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.Titanium), 'F',
                OrePrefixes.frameGt.get(Materials.Titanium) });
        GTModHandler.addCraftingRecipe(
            ItemList.Casing_HeatProof.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { "PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.Invar), 'F',
                OrePrefixes.frameGt.get(Materials.Invar) });
        GTModHandler.addCraftingRecipe(
            ItemList.Casing_FrostProof.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { "PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.Aluminium), 'F',
                OrePrefixes.frameGt.get(Materials.Aluminium) });
        GTModHandler.addCraftingRecipe(
            ItemList.Casing_CleanStainlessSteel.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { "PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.StainlessSteel), 'F',
                OrePrefixes.frameGt.get(Materials.StainlessSteel) });
        GTModHandler.addCraftingRecipe(
            ItemList.Casing_RobustTungstenSteel.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { "PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.TungstenSteel), 'F',
                OrePrefixes.frameGt.get(Materials.TungstenSteel) });
        GTModHandler.addCraftingRecipe(
            ItemList.Casing_MiningOsmiridium.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { "PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.Osmiridium), 'F',
                OrePrefixes.frameGt.get(Materials.Osmiridium) });
        GTModHandler.addCraftingRecipe(
            ItemList.Casing_MiningNeutronium.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { "PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.Neutronium), 'F',
                OrePrefixes.frameGt.get(Materials.Neutronium) });
        GTModHandler.addCraftingRecipe(
            ItemList.Casing_MiningBlackPlutonium.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { "PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.BlackPlutonium), 'F',
                OrePrefixes.frameGt.get(Materials.BlackPlutonium) });
        GTModHandler.addCraftingRecipe(
            ItemList.Casing_Turbine.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { "PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.Magnalium), 'F',
                OrePrefixes.frameGt.get(Materials.BlueSteel) });
        GTModHandler.addCraftingRecipe(
            ItemList.Casing_Turbine1.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { "PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.StainlessSteel), 'F',
                ItemList.Casing_Turbine });
        GTModHandler.addCraftingRecipe(
            ItemList.Casing_Turbine2.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { "PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.Titanium), 'F',
                ItemList.Casing_Turbine });
        GTModHandler.addCraftingRecipe(
            ItemList.Casing_Turbine3.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { "PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.TungstenSteel), 'F',
                ItemList.Casing_Turbine });
        GTModHandler.addCraftingRecipe(
            ItemList.Casing_Pipe_Bronze.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { "PIP", "IFI", "PIP", 'P', OrePrefixes.plate.get(Materials.Bronze), 'F',
                OrePrefixes.frameGt.get(Materials.Bronze), 'I', OrePrefixes.pipeMedium.get(Materials.Bronze) });
        GTModHandler.addCraftingRecipe(
            ItemList.Casing_Pipe_Steel.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { "PIP", "IFI", "PIP", 'P', OrePrefixes.plate.get(Materials.Steel), 'F',
                OrePrefixes.frameGt.get(Materials.Steel), 'I', OrePrefixes.pipeMedium.get(Materials.Steel) });
        GTModHandler.addCraftingRecipe(
            ItemList.Casing_Pipe_Titanium.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { "PIP", "IFI", "PIP", 'P', OrePrefixes.plate.get(Materials.Titanium), 'F',
                OrePrefixes.frameGt.get(Materials.Titanium), 'I', OrePrefixes.pipeMedium.get(Materials.Titanium) });
        GTModHandler.addCraftingRecipe(
            ItemList.Casing_Pipe_TungstenSteel.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { "PIP", "IFI", "PIP", 'P', OrePrefixes.plate.get(Materials.TungstenSteel), 'F',
                OrePrefixes.frameGt.get(Materials.TungstenSteel), 'I',
                OrePrefixes.pipeMedium.get(Materials.TungstenSteel) });
        GTModHandler.addCraftingRecipe(
            ItemList.Casing_Gearbox_Bronze.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { "PhP", "GFG", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.Bronze), 'F',
                OrePrefixes.frameGt.get(Materials.Bronze), 'G', OrePrefixes.gearGt.get(Materials.Bronze) });
        GTModHandler.addCraftingRecipe(
            ItemList.Casing_Gearbox_Steel.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { "PhP", "GFG", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.Steel), 'F',
                OrePrefixes.frameGt.get(Materials.Steel), 'G', OrePrefixes.gearGt.get(Materials.Steel) });
        GTModHandler.addCraftingRecipe(
            ItemList.Casing_Gearbox_Titanium.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { "PhP", "GFG", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.Steel), 'F',
                OrePrefixes.frameGt.get(Materials.Titanium), 'G', OrePrefixes.gearGt.get(Materials.Titanium) });
        GTModHandler.addCraftingRecipe(
            ItemList.Casing_Gearbox_TungstenSteel.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { "PhP", "GFG", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.Steel), 'F',
                OrePrefixes.frameGt.get(Materials.TungstenSteel), 'G', ItemList.Robot_Arm_IV });
        GTModHandler.addCraftingRecipe(
            ItemList.Casing_Grate.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { "PVP", "PFP", aTextPlateMotor, 'P', new ItemStack(Blocks.iron_bars, 1), 'F',
                OrePrefixes.frameGt.get(Materials.Steel), 'M', ItemList.Electric_Motor_MV, 'V',
                OrePrefixes.rotor.get(Materials.Steel) });
        GTModHandler.addCraftingRecipe(
            ItemList.Casing_Assembler.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { "PVP", "PFP", aTextPlateMotor, 'P', OrePrefixes.circuit.get(Materials.ZPM), 'F',
                OrePrefixes.frameGt.get(Materials.TungstenSteel), 'M', ItemList.Electric_Motor_IV, 'V',
                OrePrefixes.circuit.get(Materials.LuV) });
        GTModHandler.addCraftingRecipe(
            ItemList.Casing_Firebox_Bronze.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { "PSP", "SFS", "PSP", 'P', OrePrefixes.plate.get(Materials.Bronze), 'F',
                OrePrefixes.frameGt.get(Materials.Bronze), 'S', OrePrefixes.stick.get(Materials.Bronze) });
        GTModHandler.addCraftingRecipe(
            ItemList.WoodenCasing.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { "PSP", "PFP", "PSP", 'F', OrePrefixes.gear.get(Materials.Wood), 'P',
                OrePrefixes.frameGt.get(Materials.Wood), 'S', OrePrefixes.screw.get(Materials.Wood) });
        GTModHandler.addCraftingRecipe(
            ItemList.Hatch_Output_ULV.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { "ASA", "AFA", "APA", 'S', GTModHandler.getModItem(BuildCraftFactory.ID, "tankBlock", 1L, 0),
                'F', ItemList.Hull_ULV.get(1L), 'A', OrePrefixes.plate.get(Materials.Rubber), 'P',
                OrePrefixes.ring.get(Materials.Rubber) });
        GTModHandler.addCraftingRecipe(
            ItemList.Hatch_Input_ULV.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { "ASA", "AFA", "APA", 'S', GTModHandler.getModItem(BuildCraftFactory.ID, "tankBlock", 1L, 0),
                'F', ItemList.Hull_ULV.get(1L), 'A', OrePrefixes.plate.get(Materials.Rubber), 'P',
                OrePrefixes.gear.get(Materials.Rubber) });
        GTModHandler.addCraftingRecipe(
            ItemList.Casing_Firebox_Steel.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { "PSP", "SFS", "PSP", 'P', OrePrefixes.plate.get(Materials.Steel), 'F',
                OrePrefixes.frameGt.get(Materials.Steel), 'S', OrePrefixes.stick.get(Materials.Steel) });
        GTModHandler.addCraftingRecipe(
            ItemList.Casing_Firebox_Titanium.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { "PSP", "SFS", "PSP", 'P', OrePrefixes.plate.get(Materials.Titanium), 'F',
                OrePrefixes.frameGt.get(Materials.Titanium), 'S', OrePrefixes.stick.get(Materials.Titanium) });
        GTModHandler.addCraftingRecipe(
            ItemList.Casing_Firebox_TungstenSteel.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { "PSP", "SFS", "PSP", 'P', OrePrefixes.plate.get(Materials.TungstenSteel), 'F',
                OrePrefixes.frameGt.get(Materials.TungstenSteel), 'S',
                OrePrefixes.stick.get(Materials.TungstenSteel) });
        GTModHandler.addCraftingRecipe(
            ItemList.Casing_Stripes_A.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { "Y  ", " M ", "  B", 'M', ItemList.Casing_SolidSteel, 'Y', Dyes.dyeYellow, 'B',
                Dyes.dyeBlack });
        GTModHandler.addCraftingRecipe(
            ItemList.Casing_Stripes_B.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { "  Y", " M ", "B  ", 'M', ItemList.Casing_SolidSteel, 'Y', Dyes.dyeYellow, 'B',
                Dyes.dyeBlack });
        GTModHandler.addCraftingRecipe(
            ItemList.Casing_RadioactiveHazard.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { " YB", " M ", "   ", 'M', ItemList.Casing_SolidSteel, 'Y', Dyes.dyeYellow, 'B',
                Dyes.dyeBlack });
        GTModHandler.addCraftingRecipe(
            ItemList.Casing_BioHazard.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { " Y ", " MB", "   ", 'M', ItemList.Casing_SolidSteel, 'Y', Dyes.dyeYellow, 'B',
                Dyes.dyeBlack });
        GTModHandler.addCraftingRecipe(
            ItemList.Casing_ExplosionHazard.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { " Y ", " M ", "  B", 'M', ItemList.Casing_SolidSteel, 'Y', Dyes.dyeYellow, 'B',
                Dyes.dyeBlack });
        GTModHandler.addCraftingRecipe(
            ItemList.Casing_FireHazard.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { " Y ", " M ", " B ", 'M', ItemList.Casing_SolidSteel, 'Y', Dyes.dyeYellow, 'B',
                Dyes.dyeBlack });
        GTModHandler.addCraftingRecipe(
            ItemList.Casing_AcidHazard.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { " Y ", " M ", "B  ", 'M', ItemList.Casing_SolidSteel, 'Y', Dyes.dyeYellow, 'B',
                Dyes.dyeBlack });
        GTModHandler.addCraftingRecipe(
            ItemList.Casing_MagicHazard.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { " Y ", "BM ", "   ", 'M', ItemList.Casing_SolidSteel, 'Y', Dyes.dyeYellow, 'B',
                Dyes.dyeBlack });
        GTModHandler.addCraftingRecipe(
            ItemList.Casing_FrostHazard.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { "BY ", " M ", "   ", 'M', ItemList.Casing_SolidSteel, 'Y', Dyes.dyeYellow, 'B',
                Dyes.dyeBlack });
        GTModHandler.addCraftingRecipe(
            ItemList.Casing_NoiseHazard.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { "   ", " M ", "BY ", 'M', ItemList.Casing_SolidSteel, 'Y', Dyes.dyeYellow, 'B',
                Dyes.dyeBlack });
        GTModHandler.addCraftingRecipe(
            ItemList.Casing_Advanced_Iridium.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { "PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.Iridium), 'F',
                OrePrefixes.frameGt.get(Materials.Iridium) });

        GTModHandler.addCraftingRecipe(
            ItemList.Machine_Bricked_BlastFurnace.get(1L),
            NOT_REMOVABLE | BUFFERED,
            new Object[] { "BFB", "FwF", "BFB", 'B', ItemList.Casing_Firebricks, 'F',
                OreDictNames.craftingIronFurnace });

        GTModHandler.addCraftingRecipe(
            ItemList.Hull_Bronze.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { aTextPlate, "PhP", aTextPlate, 'P', OrePrefixes.plate.get(Materials.Bronze) });
        GTModHandler.addCraftingRecipe(
            ItemList.Hull_Bronze_Bricks.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { aTextPlate, "PhP", "BBB", 'P', OrePrefixes.plate.get(Materials.Bronze), 'B',
                new ItemStack(Blocks.brick_block, 1) });
        GTModHandler.addCraftingRecipe(
            ItemList.Hull_HP.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { aTextPlate, "PhP", aTextPlate, 'P', OrePrefixes.plate.get(Materials.Steel) });
        GTModHandler.addCraftingRecipe(
            ItemList.Hull_HP_Bricks.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { aTextPlate, "PhP", "BBB", 'P', OrePrefixes.plate.get(Materials.WroughtIron), 'B',
                new ItemStack(Blocks.brick_block, 1) });

        // hull crafting recipes. (They can't be used for recycling as that would create an exploit loop with the
        // assembler recipes.)

        GTModHandler.addCraftingRecipe(
            ItemList.Hull_ULV.get(1L),
            NOT_REMOVABLE | BUFFERED,
            new Object[] { "PHP", aTextCableHull, 'M', ItemList.Casing_ULV, 'C',
                OrePrefixes.cableGt01.get(Materials.Lead), 'H', OrePrefixes.plate.get(Materials.WroughtIron), 'P',
                OrePrefixes.plate.get(Materials.Wood) });
        GTModHandler.addCraftingRecipe(
            ItemList.Hull_LV.get(1L),
            NOT_REMOVABLE | BUFFERED,
            new Object[] { "PHP", aTextCableHull, 'M', ItemList.Casing_LV, 'C',
                OrePrefixes.cableGt01.get(Materials.Tin), 'H', OrePrefixes.plate.get(Materials.Steel), 'P',
                OrePrefixes.plate.get(Materials.WroughtIron) });
        GTModHandler.addCraftingRecipe(
            ItemList.Hull_MV.get(1L),
            NOT_REMOVABLE | BUFFERED,
            new Object[] { "PHP", aTextCableHull, 'M', ItemList.Casing_MV, 'C',
                OrePrefixes.cableGt01.get(Materials.Copper), 'H', OrePrefixes.plate.get(Materials.Aluminium), 'P',
                OrePrefixes.plate.get(Materials.WroughtIron) });
        GTModHandler.addCraftingRecipe(
            ItemList.Hull_HV.get(1L),
            NOT_REMOVABLE | BUFFERED,
            new Object[] { "PHP", aTextCableHull, 'M', ItemList.Casing_HV, 'C',
                OrePrefixes.cableGt01.get(Materials.Gold), 'H', OrePrefixes.plate.get(Materials.StainlessSteel), 'P',
                OrePrefixes.plate.get(Materials.Polyethylene) });
        GTModHandler.addCraftingRecipe(
            ItemList.Hull_EV.get(1L),
            NOT_REMOVABLE | BUFFERED,
            new Object[] { "PHP", aTextCableHull, 'M', ItemList.Casing_EV, 'C',
                OrePrefixes.cableGt01.get(Materials.Aluminium), 'H', OrePrefixes.plate.get(Materials.Titanium), 'P',
                OrePrefixes.plate.get(Materials.Polyethylene) });
        GTModHandler.addCraftingRecipe(
            ItemList.Hull_IV.get(1L),
            NOT_REMOVABLE | BUFFERED,
            new Object[] { "PHP", aTextCableHull, 'M', ItemList.Casing_IV, 'C',
                OrePrefixes.cableGt01.get(Materials.Tungsten), 'H', OrePrefixes.plate.get(Materials.TungstenSteel), 'P',
                OrePrefixes.plate.get(Materials.Polytetrafluoroethylene) });
        GTModHandler.addCraftingRecipe(
            ItemList.Hull_LuV.get(1L),
            NOT_REMOVABLE | BUFFERED,
            new Object[] { "PHP", aTextCableHull, 'M', ItemList.Casing_LuV, 'C',
                OrePrefixes.cableGt01.get(Materials.VanadiumGallium), 'H',
                OrePrefixes.plate.get(ExternalMaterials.getRhodiumPlatedPalladium()), 'P',
                OrePrefixes.plate.get(Materials.Polytetrafluoroethylene) });
        GTModHandler.addCraftingRecipe(
            ItemList.Hull_ZPM.get(1L),
            NOT_REMOVABLE | BUFFERED,
            new Object[] { "PHP", aTextCableHull, 'M', ItemList.Casing_ZPM, 'C',
                OrePrefixes.cableGt01.get(Materials.Naquadah), 'H', OrePrefixes.plate.get(Materials.Iridium), 'P',
                OrePrefixes.plate.get(Materials.Polybenzimidazole) });
        GTModHandler.addCraftingRecipe(
            ItemList.Hull_UV.get(1L),
            NOT_REMOVABLE | BUFFERED,
            new Object[] { "PHP", aTextCableHull, 'M', ItemList.Casing_UV, 'C',
                OrePrefixes.wireGt04.get(Materials.NaquadahAlloy), 'H', OrePrefixes.plate.get(Materials.Osmium), 'P',
                OrePrefixes.plate.get(Materials.Polybenzimidazole) });
        GTModHandler.addCraftingRecipe(
            ItemList.Hull_MAX.get(1L),
            NOT_REMOVABLE | BUFFERED,
            new Object[] { "PHP", aTextCableHull, 'M', ItemList.Casing_MAX, 'C',
                OrePrefixes.wireGt04.get(Materials.SuperconductorUV), 'H', OrePrefixes.plate.get(Materials.Neutronium),
                'P', OrePrefixes.plate.get(Materials.Polybenzimidazole) });

        // hull recycling data
        GTOreDictUnificator.addItemDataFromInputs(
            ItemList.Hull_ULV.get(1L),
            ItemList.Casing_ULV.get(1),
            OrePrefixes.cableGt01.get(Materials.Lead),
            OrePrefixes.cableGt01.get(Materials.Lead));
        GTOreDictUnificator.addItemDataFromInputs(
            ItemList.Hull_LV.get(1L),
            ItemList.Casing_LV.get(1),
            OrePrefixes.cableGt01.get(Materials.Tin),
            OrePrefixes.cableGt01.get(Materials.Tin));
        GTOreDictUnificator.addItemDataFromInputs(
            ItemList.Hull_MV.get(1L),
            ItemList.Casing_MV.get(1),
            OrePrefixes.cableGt01.get(Materials.AnyCopper),
            OrePrefixes.cableGt01.get(Materials.AnyCopper));
        GTOreDictUnificator.addItemDataFromInputs(
            ItemList.Hull_HV.get(1L),
            ItemList.Casing_HV.get(1),
            OrePrefixes.cableGt01.get(Materials.Gold),
            OrePrefixes.cableGt01.get(Materials.Gold));
        GTOreDictUnificator.addItemDataFromInputs(
            ItemList.Hull_EV.get(1L),
            ItemList.Casing_EV.get(1),
            OrePrefixes.cableGt01.get(Materials.Aluminium),
            OrePrefixes.cableGt01.get(Materials.Aluminium));
        GTOreDictUnificator.addItemDataFromInputs(
            ItemList.Hull_IV.get(1L),
            ItemList.Casing_IV.get(1),
            OrePrefixes.cableGt01.get(Materials.Tungsten),
            OrePrefixes.cableGt01.get(Materials.Tungsten));
        GTOreDictUnificator.addItemDataFromInputs(
            ItemList.Hull_LuV.get(1L),
            ItemList.Casing_LuV.get(1),
            OrePrefixes.cableGt01.get(Materials.VanadiumGallium),
            OrePrefixes.cableGt01.get(Materials.VanadiumGallium));
        GTOreDictUnificator.addItemDataFromInputs(
            ItemList.Hull_ZPM.get(1L),
            ItemList.Casing_ZPM.get(1),
            OrePrefixes.cableGt01.get(Materials.Naquadah),
            OrePrefixes.cableGt01.get(Materials.Naquadah));
        GTOreDictUnificator.addItemDataFromInputs(
            ItemList.Hull_UV.get(1L),
            ItemList.Casing_UV.get(1),
            OrePrefixes.cableGt04.get(Materials.NaquadahAlloy),
            OrePrefixes.cableGt04.get(Materials.NaquadahAlloy));
        GTOreDictUnificator.addItemDataFromInputs(
            ItemList.Hull_MAX.get(1L),
            ItemList.Casing_MAX.get(1),
            OrePrefixes.cableGt04.get(Materials.SuperconductorUV),
            OrePrefixes.cableGt04.get(Materials.SuperconductorUV));

        GTModHandler.addCraftingRecipe(
            ItemList.Transformer_LV_ULV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { " BB", "CM ", " BB", 'M', ItemList.Hull_ULV, 'C', OrePrefixes.cableGt01.get(Materials.Tin),
                'B', OrePrefixes.cableGt01.get(Materials.Lead) });
        GTModHandler.addCraftingRecipe(
            ItemList.Transformer_MV_LV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { " BB", "CM ", " BB", 'M', ItemList.Hull_LV, 'C',
                OrePrefixes.cableGt01.get(Materials.AnyCopper), 'B', OrePrefixes.cableGt01.get(Materials.Tin) });
        GTModHandler.addCraftingRecipe(
            ItemList.Transformer_HV_MV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "KBB", "CM ", "KBB", 'M', ItemList.Hull_MV, 'C', OrePrefixes.cableGt01.get(Materials.Gold),
                'B', OrePrefixes.cableGt01.get(Materials.AnyCopper), 'K',
                OrePrefixes.componentCircuit.get(Materials.Inductor) });
        GTModHandler.addCraftingRecipe(
            ItemList.Transformer_EV_HV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "KBB", "CM ", "KBB", 'M', ItemList.Hull_HV, 'C',
                OrePrefixes.cableGt01.get(Materials.Aluminium), 'B', OrePrefixes.cableGt01.get(Materials.Gold), 'K',
                ItemList.Circuit_Chip_ULPIC });
        GTModHandler.addCraftingRecipe(
            ItemList.Transformer_IV_EV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "KBB", "CM ", "KBB", 'M', ItemList.Hull_EV, 'C',
                OrePrefixes.cableGt01.get(Materials.Tungsten), 'B', OrePrefixes.cableGt01.get(Materials.Aluminium), 'K',
                ItemList.Circuit_Chip_LPIC });
        GTModHandler.addCraftingRecipe(
            ItemList.Transformer_LuV_IV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "KBB", "CM ", "KBB", 'M', ItemList.Hull_IV, 'C',
                OrePrefixes.cableGt01.get(Materials.VanadiumGallium), 'B',
                OrePrefixes.cableGt01.get(Materials.Tungsten), 'K', ItemList.Circuit_Chip_PIC });
        GTModHandler.addCraftingRecipe(
            ItemList.Transformer_ZPM_LuV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "KBB", "CM ", "KBB", 'M', ItemList.Hull_LuV, 'C',
                OrePrefixes.cableGt01.get(Materials.Naquadah), 'B',
                OrePrefixes.cableGt01.get(Materials.VanadiumGallium), 'K', ItemList.Circuit_Chip_HPIC });
        GTModHandler.addCraftingRecipe(
            ItemList.Transformer_UV_ZPM.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "KBB", "CM ", "KBB", 'M', ItemList.Hull_ZPM, 'C',
                OrePrefixes.cableGt01.get(Materials.NaquadahAlloy), 'B', OrePrefixes.cableGt01.get(Materials.Naquadah),
                'K', ItemList.Circuit_Chip_UHPIC });
        GTModHandler.addCraftingRecipe(
            ItemList.Transformer_MAX_UV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "KBB", "CM ", "KBB", 'M', ItemList.Hull_UV, 'C',
                OrePrefixes.wireGt01.get(Materials.Bedrockium), 'B', OrePrefixes.cableGt01.get(Materials.NaquadahAlloy),
                'K', ItemList.Circuit_Chip_NPIC });

        GTModHandler.addCraftingRecipe(
            ItemList.Hatch_Dynamo_ULV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "XOL", "SMP", "XOL", 'M', ItemList.Hull_ULV, 'S', OrePrefixes.spring.get(Materials.Lead),
                'X', OrePrefixes.circuit.get(Materials.ULV), 'O', ItemList.ULV_Coil, 'L',
                OrePrefixes.cell.get(Materials.Lubricant), 'P', OrePrefixes.rotor.get(Materials.Lead) });
        GTModHandler.addCraftingRecipe(
            ItemList.Hatch_Dynamo_LV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "XOL", "SMP", "XOL", 'M', ItemList.Hull_LV, 'S', OrePrefixes.spring.get(Materials.Tin), 'X',
                OrePrefixes.circuit.get(Materials.LV), 'O', ItemList.LV_Coil, 'L',
                OrePrefixes.cell.get(Materials.Lubricant), 'P', ItemList.Electric_Pump_LV });
        GTModHandler.addCraftingRecipe(
            ItemList.Hatch_Dynamo_MV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "XOL", "SMP", "XOL", 'M', ItemList.Hull_MV, 'S', OrePrefixes.spring.get(Materials.Copper),
                'X', ItemList.Circuit_Chip_ULPIC, 'O', ItemList.MV_Coil, 'L', OrePrefixes.cell.get(Materials.Lubricant),
                'P', ItemList.Electric_Pump_MV });
        GTModHandler.addCraftingRecipe(
            ItemList.Hatch_Energy_ULV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "COL", "XMP", "COL", 'M', ItemList.Hull_ULV, 'C', OrePrefixes.cableGt01.get(Materials.Lead),
                'X', OrePrefixes.circuit.get(Materials.ULV), 'O', ItemList.ULV_Coil, 'L',
                OrePrefixes.cell.get(Materials.Lubricant), 'P', OrePrefixes.rotor.get(Materials.Lead) });
        GTModHandler.addCraftingRecipe(
            ItemList.Hatch_Energy_LV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "COL", "XMP", "COL", 'M', ItemList.Hull_LV, 'C', OrePrefixes.cableGt01.get(Materials.Tin),
                'X', OrePrefixes.circuit.get(Materials.LV), 'O', ItemList.LV_Coil, 'L',
                OrePrefixes.cell.get(Materials.Lubricant), 'P', ItemList.Electric_Pump_LV });
        GTModHandler.addCraftingRecipe(
            ItemList.Hatch_Energy_MV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "XOL", "CMP", "XOL", 'M', ItemList.Hull_MV, 'C', OrePrefixes.cableGt01.get(Materials.Copper),
                'X', ItemList.Circuit_Chip_ULPIC, 'O', ItemList.MV_Coil, 'L', OrePrefixes.cell.get(Materials.Lubricant),
                'P', ItemList.Electric_Pump_MV });
        GTModHandler.addCraftingRecipe(
            ItemList.Hatch_Maintenance.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "dwx", "hMc", "fsr", 'M', ItemList.Hull_LV });

        GTModHandler.addCraftingRecipe(
            ItemList.Hatch_DataAccess_EV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "COC", "OMO", "COC", 'M', ItemList.Hull_EV, 'O', ItemList.Tool_DataStick, 'C',
                OrePrefixes.circuit.get(Materials.IV) });
        GTModHandler.addCraftingRecipe(
            ItemList.Hatch_DataAccess_LuV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "COC", "OMO", "COC", 'M', ItemList.Hull_LuV, 'O', ItemList.Tool_DataOrb, 'C',
                OrePrefixes.circuit.get(Materials.ZPM) });
        GTModHandler.addCraftingRecipe(
            ItemList.Hatch_DataAccess_UV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "CRC", "OMO", "CRC", 'M', ItemList.Hull_UV, 'O', ItemList.Tool_DataOrb, 'C',
                OrePrefixes.circuit.get(Materials.UHV), 'R', ItemList.Robot_Arm_UV });

        GTModHandler.addCraftingRecipe(
            ItemList.Hatch_AutoMaintenance.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "CHC", "AMA", "CHC", 'M', ItemList.Hull_LuV, 'H', ItemList.Hatch_Maintenance, 'A',
                ItemList.Robot_Arm_LuV, 'C', OrePrefixes.circuit.get(Materials.ZPM) });

        GTModHandler.addCraftingRecipe(
            ItemList.Hatch_Muffler_LV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "MX ", "PR ", 'M', ItemList.Hull_LV, 'P', OrePrefixes.pipeMedium.get(Materials.Bronze), 'R',
                OrePrefixes.rotor.get(Materials.Bronze), 'X', ItemList.Electric_Motor_LV });
        GTModHandler.addCraftingRecipe(
            ItemList.Hatch_Muffler_MV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "MX ", "PR ", 'M', ItemList.Hull_MV, 'P', OrePrefixes.pipeMedium.get(Materials.Steel), 'R',
                OrePrefixes.rotor.get(Materials.Steel), 'X', ItemList.Electric_Motor_MV });

        // recycling data for the other mufflers, recipes are in assembler
        GTOreDictUnificator.addItemDataFromInputs(
            ItemList.Hatch_Muffler_HV.get(1L),
            ItemList.Hull_HV.get(1),
            OrePrefixes.pipeLarge.get(Materials.StainlessSteel),
            OrePrefixes.rotor.get(Materials.StainlessSteel),
            ItemList.Electric_Motor_HV.get(1));
        GTOreDictUnificator.addItemDataFromInputs(
            ItemList.Hatch_Muffler_EV.get(1L),
            ItemList.Hull_EV.get(1),
            OrePrefixes.pipeLarge.get(Materials.Titanium),
            OrePrefixes.rotor.get(Materials.Titanium),
            ItemList.Electric_Motor_EV.get(1));
        GTOreDictUnificator.addItemDataFromInputs(
            ItemList.Hatch_Muffler_IV.get(1L),
            ItemList.Hull_IV.get(1),
            OrePrefixes.pipeLarge.get(Materials.TungstenSteel),
            OrePrefixes.rotor.get(Materials.TungstenSteel),
            ItemList.Electric_Motor_IV.get(1));
        GTOreDictUnificator.addItemDataFromInputs(
            ItemList.Hatch_Muffler_LuV.get(1L),
            ItemList.Hull_LuV.get(1),
            OrePrefixes.pipeLarge.get(Materials.Enderium),
            OrePrefixes.rotor.get(Materials.Enderium),
            ItemList.Electric_Motor_LuV.get(1));
        GTOreDictUnificator.addItemDataFromInputs(
            ItemList.Hatch_Muffler_ZPM.get(1L),
            ItemList.Hull_ZPM.get(1),
            OrePrefixes.pipeLarge.get(Materials.Naquadah),
            OrePrefixes.rotor.get(Materials.NaquadahAlloy),
            ItemList.Electric_Motor_ZPM.get(1));
        GTOreDictUnificator.addItemDataFromInputs(
            ItemList.Hatch_Muffler_UV.get(1L),
            ItemList.Hull_UV.get(1),
            OrePrefixes.pipeLarge.get(Materials.NetherStar),
            OrePrefixes.rotor.get(Materials.Neutronium),
            ItemList.Electric_Motor_UV.get(1));
        GTOreDictUnificator.addItemDataFromInputs(
            ItemList.Hatch_Muffler_MAX.get(1L),
            ItemList.Hull_MAX.get(1),
            OrePrefixes.pipeLarge.get(Materials.MysteriousCrystal),
            OrePrefixes.rotor.get(Materials.CosmicNeutronium),
            ItemList.Electric_Motor_UHV.get(1));

        GTModHandler.addCraftingRecipe(
            ItemList.Machine_Bronze_Boiler.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { aTextPlate, "PwP", "BFB", 'F', OreDictNames.craftingIronFurnace, 'P',
                OrePrefixes.plate.get(Materials.Bronze), 'B', new ItemStack(Blocks.brick_block, 1) });
        GTModHandler.addCraftingRecipe(
            ItemList.Machine_Steel_Boiler.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { aTextPlate, "PwP", "BFB", 'F', OreDictNames.craftingIronFurnace, 'P',
                OrePrefixes.plate.get(Materials.Steel), 'B', new ItemStack(Blocks.brick_block, 1) });
        GTModHandler.addCraftingRecipe(
            ItemList.Machine_Steel_Boiler_Lava.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { aTextPlate, "PTP", aTextPlateMotor, 'M', ItemList.Hull_HP, 'P',
                OrePrefixes.plate.get(Materials.Steel), 'T',
                GTModHandler.getModItem(BuildCraftFactory.ID, "tankBlock", 1L, 0) });

        GTModHandler.addCraftingRecipe(
            ItemList.Machine_Bronze_Boiler_Solar.get(1L),
            NOT_REMOVABLE | BUFFERED,
            new Object[] { "GGG", "SSS", aTextPlateMotor, 'M', ItemList.Hull_Bronze_Bricks, 'P',
                OrePrefixes.pipeSmall.get(Materials.Bronze), 'S', OrePrefixes.plateDouble.get(Materials.Silver), 'G',
                new ItemStack(Blocks.glass, 1) });
        GTModHandler.addCraftingRecipe(
            ItemList.Machine_HP_Solar.get(1L),
            NOT_REMOVABLE | BUFFERED,
            new Object[] { "GGG", "WSW", aTextPlateMotor, 'M', ItemList.Machine_Bronze_Boiler_Solar, 'P',
                OrePrefixes.pipeSmall.get(Materials.Steel), 'S', OrePrefixes.plateTriple.get(Materials.Silver), 'W',
                OrePrefixes.plateDouble.get(Materials.WroughtIron), 'G',
                GTModHandler.getModItem(IndustrialCraft2.ID, "blockAlloyGlass", 1L) });

        GTModHandler.addCraftingRecipe(
            ItemList.Machine_Bronze_Furnace.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "XXX", "XMX", "XFX", 'M', ItemList.Hull_Bronze_Bricks, 'X',
                OrePrefixes.pipeSmall.get(Materials.Bronze), 'F', OreDictNames.craftingFurnace });
        GTModHandler.addCraftingRecipe(
            ItemList.Machine_HP_Furnace.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "XSX", "PMP", "XXX", 'M', ItemList.Machine_Bronze_Furnace, 'X',
                OrePrefixes.pipeSmall.get(Materials.WroughtIron), 'P', OrePrefixes.plate.get(Materials.WroughtIron),
                'S', OrePrefixes.plate.get(Materials.Steel) });
        GTModHandler.addCraftingRecipe(
            ItemList.Machine_Bronze_Macerator.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "DXD", "XMX", "PXP", 'M', ItemList.Hull_Bronze, 'X',
                OrePrefixes.pipeSmall.get(Materials.Bronze), 'P', OreDictNames.craftingPiston, 'D',
                OrePrefixes.gem.get(Materials.Diamond) });
        GTModHandler.addCraftingRecipe(
            ItemList.Machine_HP_Macerator.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "PSP", "XMX", "PPP", 'M', ItemList.Machine_Bronze_Macerator, 'X',
                OrePrefixes.pipeSmall.get(Materials.WroughtIron), 'P', OrePrefixes.plate.get(Materials.WroughtIron),
                'S', OrePrefixes.plate.get(Materials.Steel) });
        GTModHandler.addCraftingRecipe(
            ItemList.Machine_Bronze_Extractor.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "XXX", "PMG", "XXX", 'M', ItemList.Hull_Bronze, 'X',
                OrePrefixes.pipeSmall.get(Materials.Bronze), 'P', OreDictNames.craftingPiston, 'G',
                new ItemStack(Blocks.glass, 1) });
        GTModHandler.addCraftingRecipe(
            ItemList.Machine_HP_Extractor.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "XSX", "PMP", "XXX", 'M', ItemList.Machine_Bronze_Extractor, 'X',
                OrePrefixes.pipeSmall.get(Materials.WroughtIron), 'P', OrePrefixes.plate.get(Materials.WroughtIron),
                'S', OrePrefixes.plate.get(Materials.Steel) });
        GTModHandler.addCraftingRecipe(
            ItemList.Machine_Bronze_Hammer.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "XPX", "XMX", "XAX", 'M', ItemList.Hull_Bronze, 'X',
                OrePrefixes.pipeSmall.get(Materials.Bronze), 'P', OreDictNames.craftingPiston, 'A',
                OreDictNames.craftingAnvil });
        GTModHandler.addCraftingRecipe(
            ItemList.Machine_HP_Hammer.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "PSP", "XMX", "PPP", 'M', ItemList.Machine_Bronze_Hammer, 'X',
                OrePrefixes.pipeSmall.get(Materials.WroughtIron), 'P', OrePrefixes.plate.get(Materials.WroughtIron),
                'S', OrePrefixes.plate.get(Materials.Steel) });
        GTModHandler.addCraftingRecipe(
            ItemList.Machine_Bronze_Compressor.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "XXX", aTextPlateMotor, "XXX", 'M', ItemList.Hull_Bronze, 'X',
                OrePrefixes.pipeSmall.get(Materials.Bronze), 'P', OreDictNames.craftingPiston });
        GTModHandler.addCraftingRecipe(
            ItemList.Machine_HP_Compressor.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "XSX", "PMP", "XXX", 'M', ItemList.Machine_Bronze_Compressor, 'X',
                OrePrefixes.pipeSmall.get(Materials.WroughtIron), 'P', OrePrefixes.plate.get(Materials.WroughtIron),
                'S', OrePrefixes.plate.get(Materials.Steel) });
        GTModHandler.addCraftingRecipe(
            ItemList.Machine_Bronze_AlloySmelter.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "XXX", "FMF", "XXX", 'M', ItemList.Hull_Bronze_Bricks, 'X',
                OrePrefixes.pipeSmall.get(Materials.Bronze), 'F', OreDictNames.craftingFurnace });
        GTModHandler.addCraftingRecipe(
            ItemList.Machine_HP_AlloySmelter.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "PSP", "PMP", "PXP", 'M', ItemList.Machine_Bronze_AlloySmelter, 'X',
                OrePrefixes.pipeSmall.get(Materials.WroughtIron), 'P', OrePrefixes.plate.get(Materials.WroughtIron),
                'S', OrePrefixes.plate.get(Materials.Steel) });

        GTModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_1by1_ULV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_ULV, 'W',
                OrePrefixes.wireGt01.get(Materials.Lead), 'T', OreDictNames.craftingChest });
        GTModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_1by1_LV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_LV, 'W',
                OrePrefixes.wireGt01.get(Materials.Tin), 'T', OreDictNames.craftingChest });
        GTModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_1by1_MV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_MV, 'W',
                OrePrefixes.wireGt01.get(Materials.AnyCopper), 'T', OreDictNames.craftingChest });
        GTModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_1by1_HV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_HV, 'W',
                OrePrefixes.wireGt01.get(Materials.Gold), 'T', OreDictNames.craftingChest });
        GTModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_1by1_EV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_EV, 'W',
                OrePrefixes.wireGt01.get(Materials.Aluminium), 'T', OreDictNames.craftingChest });
        GTModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_1by1_IV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_IV, 'W',
                OrePrefixes.wireGt01.get(Materials.Tungsten), 'T', OreDictNames.craftingChest });
        GTModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_1by1_LuV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_LuV, 'W',
                OrePrefixes.wireGt01.get(Materials.VanadiumGallium), 'T', OreDictNames.craftingChest });
        GTModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_1by1_ZPM.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_ZPM, 'W',
                OrePrefixes.wireGt01.get(Materials.Naquadah), 'T', OreDictNames.craftingChest });
        GTModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_1by1_UV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_UV, 'W',
                OrePrefixes.wireGt01.get(Materials.NaquadahAlloy), 'T', OreDictNames.craftingChest });
        GTModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_1by1_UHV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_MAX, 'W',
                OrePrefixes.wireGt01.get(Materials.SuperconductorUHV), 'T', OreDictNames.craftingChest });

        GTModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_2by2_ULV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_ULV, 'W',
                OrePrefixes.wireGt04.get(Materials.Lead), 'T', OreDictNames.craftingChest });
        GTModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_2by2_LV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_LV, 'W',
                OrePrefixes.wireGt04.get(Materials.Tin), 'T', OreDictNames.craftingChest });
        GTModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_2by2_MV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_MV, 'W',
                OrePrefixes.wireGt04.get(Materials.AnyCopper), 'T', OreDictNames.craftingChest });
        GTModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_2by2_HV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_HV, 'W',
                OrePrefixes.wireGt04.get(Materials.Gold), 'T', OreDictNames.craftingChest });
        GTModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_2by2_EV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_EV, 'W',
                OrePrefixes.wireGt04.get(Materials.Aluminium), 'T', OreDictNames.craftingChest });
        GTModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_2by2_IV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_IV, 'W',
                OrePrefixes.wireGt04.get(Materials.Tungsten), 'T', OreDictNames.craftingChest });
        GTModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_2by2_LuV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_LuV, 'W',
                OrePrefixes.wireGt04.get(Materials.VanadiumGallium), 'T', OreDictNames.craftingChest });
        GTModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_2by2_ZPM.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_ZPM, 'W',
                OrePrefixes.wireGt04.get(Materials.Naquadah), 'T', OreDictNames.craftingChest });
        GTModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_2by2_UV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_UV, 'W',
                OrePrefixes.wireGt04.get(Materials.NaquadahAlloy), 'T', OreDictNames.craftingChest });
        GTModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_2by2_UHV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_MAX, 'W',
                OrePrefixes.wireGt04.get(Materials.SuperconductorUHV), 'T', OreDictNames.craftingChest });

        GTModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_3by3_ULV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_ULV, 'W',
                OrePrefixes.wireGt08.get(Materials.Lead), 'T', OreDictNames.craftingChest });
        GTModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_3by3_LV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_LV, 'W',
                OrePrefixes.wireGt08.get(Materials.Tin), 'T', OreDictNames.craftingChest });
        GTModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_3by3_MV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_MV, 'W',
                OrePrefixes.wireGt08.get(Materials.AnyCopper), 'T', OreDictNames.craftingChest });
        GTModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_3by3_HV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_HV, 'W',
                OrePrefixes.wireGt08.get(Materials.Gold), 'T', OreDictNames.craftingChest });
        GTModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_3by3_EV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_EV, 'W',
                OrePrefixes.wireGt08.get(Materials.Aluminium), 'T', OreDictNames.craftingChest });
        GTModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_3by3_IV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_IV, 'W',
                OrePrefixes.wireGt08.get(Materials.Tungsten), 'T', OreDictNames.craftingChest });
        GTModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_3by3_LuV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_LuV, 'W',
                OrePrefixes.wireGt08.get(Materials.VanadiumGallium), 'T', OreDictNames.craftingChest });
        GTModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_3by3_ZPM.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_ZPM, 'W',
                OrePrefixes.wireGt08.get(Materials.Naquadah), 'T', OreDictNames.craftingChest });
        GTModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_3by3_UV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_UV, 'W',
                OrePrefixes.wireGt08.get(Materials.NaquadahAlloy), 'T', OreDictNames.craftingChest });
        GTModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_3by3_UHV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_MAX, 'W',
                OrePrefixes.wireGt08.get(Materials.SuperconductorUHV), 'T', OreDictNames.craftingChest });

        GTModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_4by4_ULV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_ULV, 'W',
                OrePrefixes.wireGt16.get(Materials.Lead), 'T', OreDictNames.craftingChest });
        GTModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_4by4_LV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_LV, 'W',
                OrePrefixes.wireGt16.get(Materials.Tin), 'T', OreDictNames.craftingChest });
        GTModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_4by4_MV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_MV, 'W',
                OrePrefixes.wireGt16.get(Materials.AnyCopper), 'T', OreDictNames.craftingChest });
        GTModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_4by4_HV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_HV, 'W',
                OrePrefixes.wireGt16.get(Materials.Gold), 'T', OreDictNames.craftingChest });
        GTModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_4by4_EV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_EV, 'W',
                OrePrefixes.wireGt16.get(Materials.Aluminium), 'T', OreDictNames.craftingChest });
        GTModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_4by4_IV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_IV, 'W',
                OrePrefixes.wireGt16.get(Materials.Tungsten), 'T', OreDictNames.craftingChest });
        GTModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_4by4_LuV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_LuV, 'W',
                OrePrefixes.wireGt16.get(Materials.VanadiumGallium), 'T', OreDictNames.craftingChest });
        GTModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_4by4_ZPM.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_ZPM, 'W',
                OrePrefixes.wireGt16.get(Materials.Naquadah), 'T', OreDictNames.craftingChest });
        GTModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_4by4_UV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_UV, 'W',
                OrePrefixes.wireGt16.get(Materials.NaquadahAlloy), 'T', OreDictNames.craftingChest });
        GTModHandler.addCraftingRecipe(
            ItemList.Battery_Buffer_4by4_UHV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_MAX, 'W',
                OrePrefixes.wireGt16.get(Materials.SuperconductorUHV), 'T', OreDictNames.craftingChest });

        GTModHandler.addCraftingRecipe(
            ItemList.Battery_Charger_4by4_ULV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { aTextWireChest, aTextWireHull, "BCB", 'M', ItemList.Hull_ULV, 'W',
                OrePrefixes.wireGt16.get(Materials.Lead), 'T', OreDictNames.craftingChest, 'B',
                ItemList.Battery_RE_ULV_Tantalum, 'C', OrePrefixes.circuit.get(Materials.ULV) });
        GTModHandler.addCraftingRecipe(
            ItemList.Battery_Charger_4by4_LV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { aTextWireChest, aTextWireHull, "BCB", 'M', ItemList.Hull_LV, 'W',
                OrePrefixes.wireGt16.get(Materials.Tin), 'T', OreDictNames.craftingChest, 'B',
                ItemList.Battery_RE_LV_Lithium, 'C', OrePrefixes.circuit.get(Materials.LV) });
        GTModHandler.addCraftingRecipe(
            ItemList.Battery_Charger_4by4_MV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { aTextWireChest, aTextWireHull, "BCB", 'M', ItemList.Hull_MV, 'W',
                OrePrefixes.wireGt16.get(Materials.AnyCopper), 'T', OreDictNames.craftingChest, 'B',
                ItemList.Battery_RE_MV_Lithium, 'C', OrePrefixes.circuit.get(Materials.MV) });
        GTModHandler.addCraftingRecipe(
            ItemList.Battery_Charger_4by4_HV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { aTextWireChest, aTextWireHull, "BCB", 'M', ItemList.Hull_HV, 'W',
                OrePrefixes.wireGt16.get(Materials.Gold), 'T', OreDictNames.craftingChest, 'B',
                ItemList.Battery_RE_HV_Lithium, 'C', OrePrefixes.circuit.get(Materials.HV) });
        GTModHandler.addCraftingRecipe(
            ItemList.Battery_Charger_4by4_EV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { aTextWireChest, aTextWireHull, "BCB", 'M', ItemList.Hull_EV, 'W',
                OrePrefixes.wireGt16.get(Materials.Aluminium), 'T', OreDictNames.craftingChest, 'B',
                OrePrefixes.battery.get(Materials.EV), 'C', OrePrefixes.circuit.get(Materials.EV) });
        GTModHandler.addCraftingRecipe(
            ItemList.Battery_Charger_4by4_IV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { aTextWireChest, aTextWireHull, "BCB", 'M', ItemList.Hull_IV, 'W',
                OrePrefixes.wireGt16.get(Materials.Tungsten), 'T', OreDictNames.craftingChest, 'B',
                OrePrefixes.battery.get(Materials.IV), 'C', OrePrefixes.circuit.get(Materials.IV) });
        GTModHandler.addCraftingRecipe(
            ItemList.Battery_Charger_4by4_LuV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { aTextWireChest, aTextWireHull, "BCB", 'M', ItemList.Hull_LuV, 'W',
                OrePrefixes.wireGt16.get(Materials.VanadiumGallium), 'T', OreDictNames.craftingChest, 'B',
                OrePrefixes.battery.get(Materials.LuV), 'C', OrePrefixes.circuit.get(Materials.LuV) });
        GTModHandler.addCraftingRecipe(
            ItemList.Battery_Charger_4by4_ZPM.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { aTextWireChest, aTextWireHull, "BCB", 'M', ItemList.Hull_ZPM, 'W',
                OrePrefixes.wireGt16.get(Materials.Naquadah), 'T', OreDictNames.craftingChest, 'B',
                OrePrefixes.battery.get(Materials.ZPM), 'C', OrePrefixes.circuit.get(Materials.ZPM) });
        GTModHandler.addCraftingRecipe(
            ItemList.Battery_Charger_4by4_UV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { aTextWireChest, aTextWireHull, "BCB", 'M', ItemList.Hull_UV, 'W',
                OrePrefixes.wireGt16.get(Materials.NaquadahAlloy), 'T', OreDictNames.craftingChest, 'B', ItemList.ZPM2,
                'C', OrePrefixes.circuit.get(Materials.UV) });
        GTModHandler.addCraftingRecipe(
            ItemList.Battery_Charger_4by4_UHV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { aTextWireChest, aTextWireHull, "BCB", 'M', ItemList.Hull_MAX, 'W',
                OrePrefixes.wireGt16.get(Materials.SuperconductorUHV), 'T', OreDictNames.craftingChest, 'B',
                ItemList.ZPM2, 'C', OrePrefixes.circuit.get(Materials.UHV) });

        GTModHandler.addCraftingRecipe(
            ItemList.Locker_ULV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "T", "M", 'M', ItemList.Battery_Buffer_2by2_ULV, 'T', OreDictNames.craftingChest });
        GTModHandler.addCraftingRecipe(
            ItemList.Locker_LV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "T", "M", 'M', ItemList.Battery_Buffer_2by2_LV, 'T', OreDictNames.craftingChest });
        GTModHandler.addCraftingRecipe(
            ItemList.Locker_MV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "T", "M", 'M', ItemList.Battery_Buffer_2by2_MV, 'T', OreDictNames.craftingChest });
        GTModHandler.addCraftingRecipe(
            ItemList.Locker_HV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "T", "M", 'M', ItemList.Battery_Buffer_2by2_HV, 'T', OreDictNames.craftingChest });
        GTModHandler.addCraftingRecipe(
            ItemList.Locker_EV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "T", "M", 'M', ItemList.Battery_Buffer_2by2_EV, 'T', OreDictNames.craftingChest });
        GTModHandler.addCraftingRecipe(
            ItemList.Locker_IV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "T", "M", 'M', ItemList.Battery_Buffer_2by2_IV, 'T', OreDictNames.craftingChest });
        GTModHandler.addCraftingRecipe(
            ItemList.Locker_LuV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "T", "M", 'M', ItemList.Battery_Buffer_2by2_LuV, 'T', OreDictNames.craftingChest });
        GTModHandler.addCraftingRecipe(
            ItemList.Locker_ZPM.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "T", "M", 'M', ItemList.Battery_Buffer_2by2_ZPM, 'T', OreDictNames.craftingChest });
        GTModHandler.addCraftingRecipe(
            ItemList.Locker_UV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "T", "M", 'M', ItemList.Battery_Buffer_2by2_UV, 'T', OreDictNames.craftingChest });
        GTModHandler.addCraftingRecipe(
            ItemList.Locker_MAX.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "T", "M", 'M', ItemList.Battery_Buffer_2by2_UHV, 'T', OreDictNames.craftingChest });

        GTModHandler.addCraftingRecipe(
            ItemList.Machine_LV_Scanner.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "CTC", aTextWireHull, "CRC", 'M', ItemList.Hull_LV, 'T', ItemList.Emitter_LV, 'R',
                ItemList.Sensor_LV, 'C', OrePrefixes.circuit.get(Materials.MV), 'W',
                OrePrefixes.cableGt01.get(Materials.Tin) });
        GTModHandler.addCraftingRecipe(
            ItemList.Machine_MV_Scanner.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "CTC", aTextWireHull, "CRC", 'M', ItemList.Hull_MV, 'T', ItemList.Emitter_MV, 'R',
                ItemList.Sensor_MV, 'C', OrePrefixes.circuit.get(Materials.HV), 'W',
                OrePrefixes.cableGt01.get(Materials.AnyCopper) });
        GTModHandler.addCraftingRecipe(
            ItemList.Machine_HV_Scanner.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "CTC", aTextWireHull, "CRC", 'M', ItemList.Hull_HV, 'T', ItemList.Emitter_HV, 'R',
                ItemList.Sensor_HV, 'C', OrePrefixes.circuit.get(Materials.EV), 'W',
                OrePrefixes.cableGt01.get(Materials.Gold) });
        GTModHandler.addCraftingRecipe(
            ItemList.Machine_EV_Scanner.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "CTC", aTextWireHull, "CRC", 'M', ItemList.Hull_EV, 'T', ItemList.Emitter_EV, 'R',
                ItemList.Sensor_EV, 'C', OrePrefixes.circuit.get(Materials.IV), 'W',
                OrePrefixes.cableGt01.get(Materials.Aluminium) });
        GTModHandler.addCraftingRecipe(
            ItemList.Machine_IV_Scanner.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "CTC", aTextWireHull, "CRC", 'M', ItemList.Hull_IV, 'T', ItemList.Emitter_IV, 'R',
                ItemList.Sensor_IV, 'C', OrePrefixes.circuit.get(Materials.LuV), 'W',
                OrePrefixes.cableGt01.get(Materials.Tungsten) });

        GTModHandler.addCraftingRecipe(
            ItemList.Machine_LV_Boxinator.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "BCB", "RMV", aTextWireCoil, 'M', ItemList.Hull_LV, 'R', ItemList.Robot_Arm_LV, 'V',
                ItemList.Conveyor_Module_LV, 'C', OrePrefixes.circuit.get(Materials.LV), 'W',
                OrePrefixes.cableGt01.get(Materials.Tin), 'B', OreDictNames.craftingChest });
        GTModHandler.addCraftingRecipe(
            ItemList.Machine_MV_Boxinator.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "BCB", "RMV", aTextWireCoil, 'M', ItemList.Hull_MV, 'R', ItemList.Robot_Arm_MV, 'V',
                ItemList.Conveyor_Module_MV, 'C', OrePrefixes.circuit.get(Materials.MV), 'W',
                OrePrefixes.cableGt01.get(Materials.AnyCopper), 'B', OreDictNames.craftingChest });
        GTModHandler.addCraftingRecipe(
            ItemList.Machine_HV_Boxinator.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "BCB", "RMV", aTextWireCoil, 'M', ItemList.Hull_HV, 'R', ItemList.Robot_Arm_HV, 'V',
                ItemList.Conveyor_Module_HV, 'C', OrePrefixes.circuit.get(Materials.HV), 'W',
                OrePrefixes.cableGt01.get(Materials.Gold), 'B', OreDictNames.craftingChest });
        GTModHandler.addCraftingRecipe(
            ItemList.Machine_EV_Boxinator.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "BCB", "RMV", aTextWireCoil, 'M', ItemList.Hull_EV, 'R', ItemList.Robot_Arm_EV, 'V',
                ItemList.Conveyor_Module_EV, 'C', OrePrefixes.circuit.get(Materials.EV), 'W',
                OrePrefixes.cableGt01.get(Materials.Aluminium), 'B', OreDictNames.craftingChest });
        GTModHandler.addCraftingRecipe(
            ItemList.Machine_IV_Boxinator.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "BCB", "RMV", aTextWireCoil, 'M', ItemList.Hull_IV, 'R', ItemList.Robot_Arm_IV, 'V',
                ItemList.Conveyor_Module_IV, 'C', OrePrefixes.circuit.get(Materials.IV), 'W',
                OrePrefixes.cableGt01.get(Materials.Tungsten), 'B', OreDictNames.craftingChest });
        GTModHandler.addCraftingRecipe(
            ItemList.Machine_LuV_Boxinator.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "BCB", "RMV", aTextWireCoil, 'M', ItemList.Hull_LuV, 'R', ItemList.Robot_Arm_LuV, 'V',
                ItemList.Conveyor_Module_LuV, 'C', OrePrefixes.circuit.get(Materials.LuV), 'W',
                OrePrefixes.cableGt01.get(Materials.VanadiumGallium), 'B', OreDictNames.craftingChest });
        GTModHandler.addCraftingRecipe(
            ItemList.Machine_ZPM_Boxinator.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "BCB", "RMV", aTextWireCoil, 'M', ItemList.Hull_ZPM, 'R', ItemList.Robot_Arm_ZPM, 'V',
                ItemList.Conveyor_Module_ZPM, 'C', OrePrefixes.circuit.get(Materials.ZPM), 'W',
                OrePrefixes.cableGt01.get(Materials.Naquadah), 'B', OreDictNames.craftingChest });
        GTModHandler.addCraftingRecipe(
            ItemList.Machine_UV_Boxinator.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "BCB", "RMV", aTextWireCoil, 'M', ItemList.Hull_UV, 'R', ItemList.Robot_Arm_UV, 'V',
                ItemList.Conveyor_Module_UV, 'C', OrePrefixes.circuit.get(Materials.UV), 'W',
                OrePrefixes.cableGt01.get(Materials.NaquadahAlloy), 'B', OreDictNames.craftingChest });

        GTModHandler.addCraftingRecipe(
            ItemList.Machine_LV_RockBreaker.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "PED", aTextWireHull, "GGG", 'M', ItemList.Hull_LV, 'D', OreDictNames.craftingGrinder, 'E',
                ItemList.Electric_Motor_LV, 'P', ItemList.Electric_Piston_LV, 'C',
                OrePrefixes.circuit.get(Materials.LV), 'W', OrePrefixes.cableGt01.get(Materials.Tin), 'G',
                new ItemStack(Blocks.glass, 1) });
        GTModHandler.addCraftingRecipe(
            ItemList.Machine_MV_RockBreaker.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "PED", aTextWireHull, "GGG", 'M', ItemList.Hull_MV, 'D', OreDictNames.craftingGrinder, 'E',
                ItemList.Electric_Motor_MV, 'P', ItemList.Electric_Piston_MV, 'C',
                OrePrefixes.circuit.get(Materials.MV), 'W', OrePrefixes.cableGt01.get(Materials.AnyCopper), 'G',
                new ItemStack(Blocks.glass, 1) });
        GTModHandler.addCraftingRecipe(
            ItemList.Machine_HV_RockBreaker.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "PED", aTextWireHull, "GGG", 'M', ItemList.Hull_HV, 'D', OreDictNames.craftingGrinder, 'E',
                ItemList.Electric_Motor_HV, 'P', ItemList.Electric_Piston_HV, 'C',
                OrePrefixes.circuit.get(Materials.HV), 'W', OrePrefixes.cableGt01.get(Materials.Gold), 'G',
                new ItemStack(Blocks.glass, 1) });
        GTModHandler.addCraftingRecipe(
            ItemList.Machine_EV_RockBreaker.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "PED", aTextWireHull, "GGG", 'M', ItemList.Hull_EV, 'D', OreDictNames.craftingGrinder, 'E',
                ItemList.Electric_Motor_EV, 'P', ItemList.Electric_Piston_EV, 'C',
                OrePrefixes.circuit.get(Materials.EV), 'W', OrePrefixes.cableGt01.get(Materials.Aluminium), 'G',
                new ItemStack(Blocks.glass, 1) });
        GTModHandler.addCraftingRecipe(
            ItemList.Machine_IV_RockBreaker.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "PED", aTextWireHull, "GGG", 'M', ItemList.Hull_IV, 'D', OreDictNames.craftingGrinder, 'E',
                ItemList.Electric_Motor_IV, 'P', ItemList.Electric_Piston_IV, 'C',
                OrePrefixes.circuit.get(Materials.IV), 'W', OrePrefixes.cableGt01.get(Materials.Tungsten), 'G',
                new ItemStack(Blocks.glass, 1) });

        GTModHandler.addCraftingRecipe(
            ItemList.Machine_LV_Massfab.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "CFC", aTextWireHull, "CFC", 'M', ItemList.Hull_LV, 'F', ItemList.Field_Generator_LV, 'C',
                OrePrefixes.circuit.get(Materials.MV), 'W', OrePrefixes.cableGt04.get(Materials.Tin) });
        GTModHandler.addCraftingRecipe(
            ItemList.Machine_MV_Massfab.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "CFC", aTextWireHull, "CFC", 'M', ItemList.Hull_MV, 'F', ItemList.Field_Generator_MV, 'C',
                OrePrefixes.circuit.get(Materials.HV), 'W', OrePrefixes.cableGt04.get(Materials.AnyCopper) });
        GTModHandler.addCraftingRecipe(
            ItemList.Machine_HV_Massfab.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "CFC", aTextWireHull, "CFC", 'M', ItemList.Hull_HV, 'F', ItemList.Field_Generator_HV, 'C',
                OrePrefixes.circuit.get(Materials.EV), 'W', OrePrefixes.cableGt04.get(Materials.Gold) });
        GTModHandler.addCraftingRecipe(
            ItemList.Machine_EV_Massfab.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "CFC", aTextWireHull, "CFC", 'M', ItemList.Hull_EV, 'F', ItemList.Field_Generator_EV, 'C',
                OrePrefixes.circuit.get(Materials.IV), 'W', OrePrefixes.cableGt04.get(Materials.Aluminium) });
        GTModHandler.addCraftingRecipe(
            ItemList.Machine_IV_Massfab.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "CFC", aTextWireHull, "CFC", 'M', ItemList.Hull_IV, 'F', ItemList.Field_Generator_IV, 'C',
                OrePrefixes.circuit.get(Materials.LuV), 'W', OrePrefixes.cableGt04.get(Materials.Tungsten) });

        GTModHandler.addCraftingRecipe(
            ItemList.Machine_LV_Replicator.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "EFE", aTextCableHull, aTextMotorWire, 'M', ItemList.Hull_LV, 'F',
                ItemList.Field_Generator_LV, 'E', ItemList.Emitter_LV, 'C', OrePrefixes.circuit.get(Materials.MV), 'W',
                OrePrefixes.cableGt04.get(Materials.Tin) });
        GTModHandler.addCraftingRecipe(
            ItemList.Machine_MV_Replicator.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "EFE", aTextCableHull, aTextMotorWire, 'M', ItemList.Hull_MV, 'F',
                ItemList.Field_Generator_MV, 'E', ItemList.Emitter_MV, 'C', OrePrefixes.circuit.get(Materials.HV), 'W',
                OrePrefixes.cableGt04.get(Materials.AnyCopper) });
        GTModHandler.addCraftingRecipe(
            ItemList.Machine_HV_Replicator.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "EFE", aTextCableHull, aTextMotorWire, 'M', ItemList.Hull_HV, 'F',
                ItemList.Field_Generator_HV, 'E', ItemList.Emitter_HV, 'C', OrePrefixes.circuit.get(Materials.EV), 'W',
                OrePrefixes.cableGt04.get(Materials.Gold) });
        GTModHandler.addCraftingRecipe(
            ItemList.Machine_EV_Replicator.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "EFE", aTextCableHull, aTextMotorWire, 'M', ItemList.Hull_EV, 'F',
                ItemList.Field_Generator_EV, 'E', ItemList.Emitter_EV, 'C', OrePrefixes.circuit.get(Materials.IV), 'W',
                OrePrefixes.cableGt04.get(Materials.Aluminium) });
        GTModHandler.addCraftingRecipe(
            ItemList.Machine_IV_Replicator.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "EFE", aTextCableHull, aTextMotorWire, 'M', ItemList.Hull_IV, 'F',
                ItemList.Field_Generator_IV, 'E', ItemList.Emitter_IV, 'C', OrePrefixes.circuit.get(Materials.LuV), 'W',
                OrePrefixes.cableGt04.get(Materials.Tungsten) });

        GTModHandler.addCraftingRecipe(
            ItemList.Machine_LV_Brewery.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "GPG", aTextWireHull, "CBC", 'M', ItemList.Hull_LV, 'P', ItemList.Electric_Pump_LV, 'B',
                new ItemStack(Items.brewing_stand, 0), 'C', OrePrefixes.circuit.get(Materials.LV), 'W',
                OrePrefixes.cableGt01.get(Materials.Tin), 'G', new ItemStack(Blocks.glass, 1) });
        GTModHandler.addCraftingRecipe(
            ItemList.Machine_MV_Brewery.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "GPG", aTextWireHull, "CBC", 'M', ItemList.Hull_MV, 'P', ItemList.Electric_Pump_MV, 'B',
                new ItemStack(Items.brewing_stand, 0), 'C', OrePrefixes.circuit.get(Materials.MV), 'W',
                OrePrefixes.cableGt01.get(Materials.AnyCopper), 'G', new ItemStack(Blocks.glass, 1) });
        GTModHandler.addCraftingRecipe(
            ItemList.Machine_HV_Brewery.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "GPG", aTextWireHull, "CBC", 'M', ItemList.Hull_HV, 'P', ItemList.Electric_Pump_HV, 'B',
                new ItemStack(Items.brewing_stand, 0), 'C', OrePrefixes.circuit.get(Materials.HV), 'W',
                OrePrefixes.cableGt01.get(Materials.Gold), 'G', new ItemStack(Blocks.glass, 1) });
        GTModHandler.addCraftingRecipe(
            ItemList.Machine_EV_Brewery.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "GPG", aTextWireHull, "CBC", 'M', ItemList.Hull_EV, 'P', ItemList.Electric_Pump_EV, 'B',
                new ItemStack(Items.brewing_stand, 0), 'C', OrePrefixes.circuit.get(Materials.EV), 'W',
                OrePrefixes.cableGt01.get(Materials.Aluminium), 'G', new ItemStack(Blocks.glass, 1) });
        GTModHandler.addCraftingRecipe(
            ItemList.Machine_IV_Brewery.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "GPG", aTextWireHull, "CBC", 'M', ItemList.Hull_IV, 'P', ItemList.Electric_Pump_IV, 'B',
                new ItemStack(Items.brewing_stand, 0), 'C', OrePrefixes.circuit.get(Materials.IV), 'W',
                OrePrefixes.cableGt01.get(Materials.Tungsten), 'G', new ItemStack(Blocks.glass, 1) });

        GTModHandler.addCraftingRecipe(
            ItemList.Machine_LV_Miner.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "EEE", aTextWireHull, "CSC", 'M', ItemList.Hull_LV, 'E', ItemList.Electric_Motor_LV, 'C',
                OrePrefixes.circuit.get(Materials.LV), 'W', OrePrefixes.cableGt01.get(Materials.Tin), 'S',
                ItemList.Sensor_LV });
        GTModHandler.addCraftingRecipe(
            ItemList.Machine_MV_Miner.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "PEP", aTextWireHull, "CSC", 'M', ItemList.Hull_MV, 'E', ItemList.Electric_Motor_MV, 'P',
                ItemList.Electric_Piston_MV, 'C', OrePrefixes.circuit.get(Materials.MV), 'W',
                OrePrefixes.cableGt02.get(Materials.Copper), 'S', ItemList.Sensor_MV });
        GTModHandler.addCraftingRecipe(
            ItemList.Machine_HV_Miner.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "RPR", aTextWireHull, "CSC", 'M', ItemList.Hull_HV, 'E', ItemList.Electric_Motor_HV, 'P',
                ItemList.Electric_Piston_HV, 'R', ItemList.Robot_Arm_HV, 'C', OrePrefixes.circuit.get(Materials.HV),
                'W', OrePrefixes.cableGt04.get(Materials.Gold), 'S', ItemList.Sensor_HV });

        GTModHandler.addCraftingRecipe(
            ItemList.Machine_Multi_BlastFurnace.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "FFF", aTextCableHull, aTextWireCoil, 'M', ItemList.Casing_HeatProof, 'F',
                OreDictNames.craftingIronFurnace, 'C', OrePrefixes.circuit.get(Materials.LV), 'W',
                OrePrefixes.cableGt01.get(Materials.Tin) });
        GTModHandler.addCraftingRecipe(
            ItemList.Machine_Multi_VacuumFreezer.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { aTextPlate, aTextCableHull, aTextWireCoil, 'M', ItemList.Casing_FrostProof, 'P',
                ItemList.Electric_Pump_HV, 'C', OrePrefixes.circuit.get(Materials.EV), 'W',
                OrePrefixes.cableGt01.get(Materials.Gold) });
        GTModHandler.addCraftingRecipe(
            ItemList.Machine_Multi_ImplosionCompressor.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "OOO", aTextCableHull, aTextWireCoil, 'M', ItemList.Casing_SolidSteel, 'O',
                Ic2Items.reinforcedStone, 'C', OrePrefixes.circuit.get(Materials.HV), 'W',
                OrePrefixes.cableGt01.get(Materials.Gold) });
        GTModHandler.addCraftingRecipe(
            ItemList.Machine_Multi_Furnace.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "FFF", aTextCableHull, aTextWireCoil, 'M', ItemList.Casing_HeatProof, 'F',
                OreDictNames.craftingIronFurnace, 'C', OrePrefixes.circuit.get(Materials.HV), 'W',
                OrePrefixes.cableGt01.get(Materials.AnnealedCopper) });

        GTModHandler.addCraftingRecipe(
            ItemList.Machine_Multi_LargeBoiler_Bronze.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { aTextWireCoil, aTextCableHull, aTextWireCoil, 'M', ItemList.Casing_Firebox_Bronze, 'C',
                OrePrefixes.circuit.get(Materials.MV), 'W', OrePrefixes.cableGt01.get(Materials.Tin) });
        GTModHandler.addCraftingRecipe(
            ItemList.Machine_Multi_LargeBoiler_Steel.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { aTextWireCoil, aTextCableHull, aTextWireCoil, 'M', ItemList.Casing_Firebox_Steel, 'C',
                OrePrefixes.circuit.get(Materials.HV), 'W', OrePrefixes.cableGt01.get(Materials.AnyCopper) });
        GTModHandler.addCraftingRecipe(
            ItemList.Machine_Multi_LargeBoiler_Titanium.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { aTextWireCoil, aTextCableHull, aTextWireCoil, 'M', ItemList.Casing_Firebox_Titanium, 'C',
                OrePrefixes.circuit.get(Materials.EV), 'W', OrePrefixes.cableGt01.get(Materials.Gold) });
        GTModHandler.addCraftingRecipe(
            ItemList.Machine_Multi_LargeBoiler_TungstenSteel.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { aTextWireCoil, aTextCableHull, aTextWireCoil, 'M', ItemList.Casing_Firebox_TungstenSteel,
                'C', OrePrefixes.circuit.get(Materials.IV), 'W', OrePrefixes.cableGt01.get(Materials.Aluminium) });

        GTModHandler.addCraftingRecipe(
            ItemList.Generator_Diesel_LV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "PCP", "EME", "GWG", 'M', ItemList.Hull_LV, 'P', ItemList.Electric_Piston_LV, 'E',
                ItemList.Electric_Motor_LV, 'C', OrePrefixes.circuit.get(Materials.LV), 'W',
                OrePrefixes.cableGt01.get(Materials.Tin), 'G', OrePrefixes.gearGt.get(Materials.Steel) });
        GTModHandler.addCraftingRecipe(
            ItemList.Generator_Diesel_MV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "PCP", "EME", "GWG", 'M', ItemList.Hull_MV, 'P', ItemList.Electric_Piston_MV, 'E',
                ItemList.Electric_Motor_MV, 'C', OrePrefixes.circuit.get(Materials.MV), 'W',
                OrePrefixes.cableGt01.get(Materials.AnyCopper), 'G', OrePrefixes.gearGt.get(Materials.Aluminium) });
        GTModHandler.addCraftingRecipe(
            ItemList.Generator_Diesel_HV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "PCP", "EME", "GWG", 'M', ItemList.Hull_HV, 'P', ItemList.Electric_Piston_HV, 'E',
                ItemList.Electric_Motor_HV, 'C', OrePrefixes.circuit.get(Materials.HV), 'W',
                OrePrefixes.cableGt01.get(Materials.Gold), 'G', OrePrefixes.gearGt.get(Materials.StainlessSteel) });

        GTModHandler.addCraftingRecipe(
            ItemList.Generator_Gas_Turbine_LV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "CRC", "RMR", aTextMotorWire, 'M', ItemList.Hull_LV, 'E', ItemList.Electric_Motor_LV, 'R',
                OrePrefixes.rotor.get(Materials.Tin), 'C', OrePrefixes.circuit.get(Materials.LV), 'W',
                OrePrefixes.cableGt01.get(Materials.Tin) });
        GTModHandler.addCraftingRecipe(
            ItemList.Generator_Gas_Turbine_MV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "CRC", "RMR", aTextMotorWire, 'M', ItemList.Hull_MV, 'E', ItemList.Electric_Motor_MV, 'R',
                OrePrefixes.rotor.get(Materials.Bronze), 'C', OrePrefixes.circuit.get(Materials.MV), 'W',
                OrePrefixes.cableGt01.get(Materials.AnyCopper) });
        GTModHandler.addCraftingRecipe(
            ItemList.Generator_Gas_Turbine_HV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "CRC", "RMR", aTextMotorWire, 'M', ItemList.Hull_HV, 'E', ItemList.Electric_Motor_HV, 'R',
                OrePrefixes.rotor.get(Materials.Steel), 'C', OrePrefixes.circuit.get(Materials.HV), 'W',
                OrePrefixes.cableGt01.get(Materials.Gold) });
        GTModHandler.addCraftingRecipe(
            ItemList.Generator_Gas_Turbine_EV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "CRC", "RMR", aTextMotorWire, 'M', ItemList.Hull_EV, 'E', ItemList.Electric_Motor_EV, 'R',
                OrePrefixes.rotor.get(Materials.Titanium), 'C', OrePrefixes.circuit.get(Materials.EV), 'W',
                OrePrefixes.cableGt01.get(Materials.Aluminium) });
        GTModHandler.addCraftingRecipe(
            ItemList.Generator_Gas_Turbine_IV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "CRC", "RMR", aTextMotorWire, 'M', ItemList.Hull_IV, 'E', ItemList.Electric_Motor_IV, 'R',
                OrePrefixes.rotor.get(Materials.TungstenSteel), 'C', OrePrefixes.circuit.get(Materials.IV), 'W',
                OrePrefixes.cableGt01.get(Materials.Tungsten) });

        GTModHandler.addCraftingRecipe(
            ItemList.Generator_Steam_Turbine_LV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "PCP", "RMR", aTextMotorWire, 'M', ItemList.Hull_LV, 'E', ItemList.Electric_Motor_LV, 'R',
                OrePrefixes.rotor.get(Materials.Tin), 'C', OrePrefixes.circuit.get(Materials.LV), 'W',
                OrePrefixes.cableGt01.get(Materials.Tin), 'P', OrePrefixes.pipeMedium.get(Materials.Bronze) });
        GTModHandler.addCraftingRecipe(
            ItemList.Generator_Steam_Turbine_MV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "PCP", "RMR", aTextMotorWire, 'M', ItemList.Hull_MV, 'E', ItemList.Electric_Motor_MV, 'R',
                OrePrefixes.rotor.get(Materials.Bronze), 'C', OrePrefixes.circuit.get(Materials.MV), 'W',
                OrePrefixes.cableGt01.get(Materials.AnyCopper), 'P', OrePrefixes.pipeMedium.get(Materials.Steel) });
        GTModHandler.addCraftingRecipe(
            ItemList.Generator_Steam_Turbine_HV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "PCP", "RMR", aTextMotorWire, 'M', ItemList.Hull_HV, 'E', ItemList.Electric_Motor_HV, 'R',
                OrePrefixes.rotor.get(Materials.Steel), 'C', OrePrefixes.circuit.get(Materials.HV), 'W',
                OrePrefixes.cableGt01.get(Materials.Gold), 'P', OrePrefixes.pipeMedium.get(Materials.StainlessSteel) });

        if (!Thaumcraft.isModLoaded()) {
            GTModHandler.addCraftingRecipe(
                ItemList.MagicEnergyConverter_LV.get(1L),
                GTModHandler.RecipeBits.BITSD,
                new Object[] { "CTC", "FMF", "CBC", 'M', ItemList.Hull_LV, 'B', new ItemStack(Blocks.beacon), 'C',
                    OrePrefixes.circuit.get(Materials.HV), 'T', ItemList.Field_Generator_LV, 'F',
                    OrePrefixes.plate.get(Materials.Platinum) });
            GTModHandler.addCraftingRecipe(
                ItemList.MagicEnergyConverter_MV.get(1L),
                GTModHandler.RecipeBits.BITSD,
                new Object[] { "CTC", "FMF", "CBC", 'M', ItemList.Hull_MV, 'B', new ItemStack(Blocks.beacon), 'C',
                    OrePrefixes.circuit.get(Materials.EV), 'T', ItemList.Field_Generator_MV, 'F',
                    OrePrefixes.plate.get(Materials.Iridium) });
            GTModHandler.addCraftingRecipe(
                ItemList.MagicEnergyConverter_HV.get(1L),
                GTModHandler.RecipeBits.BITSD,
                new Object[] { "CTC", "FMF", "CBC", 'M', ItemList.Hull_HV, 'B', new ItemStack(Blocks.beacon), 'C',
                    OrePrefixes.circuit.get(Materials.IV), 'T', ItemList.Field_Generator_HV, 'F',
                    OrePrefixes.plate.get(Materials.Neutronium) });

            GTModHandler.addCraftingRecipe(
                ItemList.MagicEnergyAbsorber_LV.get(1L),
                GTModHandler.RecipeBits.BITSD,
                new Object[] { "CTC", "FMF", "CBC", 'M', ItemList.Hull_LV, 'B',
                    ItemList.MagicEnergyConverter_LV.get(1L), 'C', OrePrefixes.circuit.get(Materials.HV), 'T',
                    ItemList.Field_Generator_LV, 'F', OrePrefixes.plate.get(Materials.Platinum) });
            GTModHandler.addCraftingRecipe(
                ItemList.MagicEnergyAbsorber_MV.get(1L),
                GTModHandler.RecipeBits.BITSD,
                new Object[] { "CTC", "FMF", "CBC", 'M', ItemList.Hull_MV, 'B',
                    ItemList.MagicEnergyConverter_MV.get(1L), 'C', OrePrefixes.circuit.get(Materials.EV), 'T',
                    ItemList.Field_Generator_MV, 'F', OrePrefixes.plate.get(Materials.Iridium) });
            GTModHandler.addCraftingRecipe(
                ItemList.MagicEnergyAbsorber_HV.get(1L),
                GTModHandler.RecipeBits.BITSD,
                new Object[] { "CTC", "FMF", "CBC", 'M', ItemList.Hull_HV, 'B',
                    ItemList.MagicEnergyConverter_MV.get(1L), 'C', OrePrefixes.circuit.get(Materials.IV), 'T',
                    ItemList.Field_Generator_HV, 'F', OrePrefixes.plate.get(Materials.Europium) });
            GTModHandler.addCraftingRecipe(
                ItemList.MagicEnergyAbsorber_EV.get(1L),
                GTModHandler.RecipeBits.BITSD,
                new Object[] { "CTC", "FMF", "CBC", 'M', ItemList.Hull_HV, 'B',
                    ItemList.MagicEnergyConverter_HV.get(1L), 'C', OrePrefixes.circuit.get(Materials.LuV), 'T',
                    ItemList.Field_Generator_EV, 'F', OrePrefixes.plate.get(Materials.Neutronium) });
        }
        GTModHandler.addCraftingRecipe(
            ItemList.Casing_Fusion_Coil.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "CTC", "FMF", "CTC", 'M', ItemList.Casing_Coil_Superconductor, 'C',
                OrePrefixes.circuit.get(Materials.LuV), 'F', ItemList.Field_Generator_MV, 'T',
                ItemList.Neutron_Reflector });

        GTModHandler.addCraftingRecipe(
            ItemList.Generator_Plasma_EV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "UCU", "FMF", aTextWireCoil, 'M', ItemList.Hull_LuV, 'F', ItemList.Field_Generator_HV, 'C',
                OrePrefixes.circuit.get(Materials.IV), 'W', OrePrefixes.cableGt04.get(Materials.Tungsten), 'U',
                OrePrefixes.stick.get(Materials.Plutonium241) });
        GTModHandler.addCraftingRecipe(
            ItemList.Generator_Plasma_IV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "UCU", "FMF", aTextWireCoil, 'M', ItemList.Hull_ZPM, 'F', ItemList.Field_Generator_EV, 'C',
                OrePrefixes.circuit.get(Materials.LuV), 'W', OrePrefixes.wireGt04.get(Materials.VanadiumGallium), 'U',
                OrePrefixes.stick.get(Materials.Europium) });
        GTModHandler.addCraftingRecipe(
            ItemList.Generator_Plasma_LuV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "UCU", "FMF", aTextWireCoil, 'M', ItemList.Hull_UV, 'F', ItemList.Field_Generator_IV, 'C',
                OrePrefixes.circuit.get(Materials.ZPM), 'W', OrePrefixes.wireGt04.get(Materials.Naquadah), 'U',
                OrePrefixes.stick.get(Materials.Americium) });

        GTModHandler.addCraftingRecipe(
            ItemList.Distillation_Tower.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "CBC", "FMF", "CBC", 'M', ItemList.Hull_HV, 'B',
                OrePrefixes.pipeLarge.get(Materials.StainlessSteel), 'C', OrePrefixes.circuit.get(Materials.EV), 'F',
                ItemList.Electric_Pump_HV });

        GTModHandler.addCraftingRecipe(
            ItemList.LargeSteamTurbine.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "CPC", aTextPlateMotor, "BPB", 'M', ItemList.Hull_HV, 'B',
                OrePrefixes.pipeLarge.get(Materials.Steel), 'C', OrePrefixes.circuit.get(Materials.HV), 'P',
                OrePrefixes.gearGt.get(Materials.Steel) });
        GTModHandler.addCraftingRecipe(
            ItemList.LargeGasTurbine.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "CPC", aTextPlateMotor, "BPB", 'M', ItemList.Hull_EV, 'B',
                OrePrefixes.pipeLarge.get(Materials.StainlessSteel), 'C', OrePrefixes.circuit.get(Materials.EV), 'P',
                OrePrefixes.gearGt.get(Materials.StainlessSteel) });

        GTModHandler.addCraftingRecipe(
            ItemList.Pump_LV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "CPC", aTextPlateMotor, "BPB", 'M', ItemList.Hull_LV, 'B',
                OrePrefixes.pipeLarge.get(Materials.Bronze), 'C', OrePrefixes.circuit.get(Materials.LV), 'P',
                ItemList.Electric_Pump_LV });
        GTModHandler.addCraftingRecipe(
            ItemList.Pump_MV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "CPC", aTextPlateMotor, "BPB", 'M', ItemList.Hull_MV, 'B',
                OrePrefixes.pipeLarge.get(Materials.Steel), 'C', OrePrefixes.circuit.get(Materials.MV), 'P',
                ItemList.Electric_Pump_MV });
        GTModHandler.addCraftingRecipe(
            ItemList.Pump_HV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "CPC", aTextPlateMotor, "BPB", 'M', ItemList.Hull_HV, 'B',
                OrePrefixes.pipeLarge.get(Materials.StainlessSteel), 'C', OrePrefixes.circuit.get(Materials.HV), 'P',
                ItemList.Electric_Pump_HV });

        GTModHandler.addCraftingRecipe(
            ItemList.MobRep_LV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "EEE", " M ", "CCC", 'M', ItemList.Hull_LV, 'E', ItemList.Emitter_LV.get(1L), 'C',
                OrePrefixes.circuit.get(Materials.LV) });
        GTModHandler.addCraftingRecipe(
            ItemList.MobRep_MV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "EEE", " M ", "CCC", 'M', ItemList.Hull_MV, 'E', ItemList.Emitter_MV.get(1L), 'C',
                OrePrefixes.circuit.get(Materials.MV) });
        GTModHandler.addCraftingRecipe(
            ItemList.MobRep_HV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "EEE", " M ", "CCC", 'M', ItemList.Hull_HV, 'E', ItemList.Emitter_HV.get(1L), 'C',
                OrePrefixes.circuit.get(Materials.HV) });
        GTModHandler.addCraftingRecipe(
            ItemList.MobRep_EV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "EEE", " M ", "CCC", 'M', ItemList.Hull_EV, 'E', ItemList.Emitter_EV.get(1L), 'C',
                OrePrefixes.circuit.get(Materials.EV) });
        GTModHandler.addCraftingRecipe(
            ItemList.MobRep_IV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "EEE", " M ", "CCC", 'M', ItemList.Hull_IV, 'E', ItemList.Emitter_IV.get(1L), 'C',
                OrePrefixes.circuit.get(Materials.IV) });
        GTModHandler.addCraftingRecipe(
            ItemList.MobRep_LuV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "EEE", " M ", "CCC", 'M', ItemList.Hull_LuV, 'E', ItemList.Emitter_LuV.get(1L), 'C',
                OrePrefixes.circuit.get(Materials.LuV) });
        GTModHandler.addCraftingRecipe(
            ItemList.MobRep_ZPM.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "EEE", " M ", "CCC", 'M', ItemList.Hull_ZPM, 'E', ItemList.Emitter_ZPM.get(1L), 'C',
                OrePrefixes.circuit.get(Materials.ZPM) });
        GTModHandler.addCraftingRecipe(
            ItemList.MobRep_UV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "EEE", " M ", "CCC", 'M', ItemList.Hull_UV, 'E', ItemList.Emitter_UV.get(1L), 'C',
                OrePrefixes.circuit.get(Materials.UV) });

        GTModHandler.addCraftingRecipe(
            ItemList.Machine_Multi_HeatExchanger.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { aTextWireCoil, aTextCableHull, aTextWireCoil, 'M', ItemList.Casing_Pipe_Titanium, 'C',
                OrePrefixes.pipeMedium.get(Materials.Titanium), 'W', ItemList.Electric_Pump_EV });

        GTModHandler.addCraftingRecipe(
            ItemList.Charcoal_Pile.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "EXE", "EME", "hCw", 'M', ItemList.Hull_HP_Bricks, 'E',
                OrePrefixes.plate.get(Materials.AnyBronze), 'C', new ItemStack(Items.flint_and_steel, 1), 'X',
                OrePrefixes.rotor.get(Materials.Steel), });

        GTModHandler.addCraftingRecipe(
            ItemList.Seismic_Prospector_Adv_LV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "WWW", "EME", "CXC", 'M', ItemList.Hull_LV, 'W',
                OrePrefixes.plateDouble.get(Materials.Steel), 'E', OrePrefixes.circuit.get(Materials.LV), 'C',
                ItemList.Sensor_LV, 'X', OrePrefixes.cableGt02.get(Materials.Tin) });
        GTModHandler.addCraftingRecipe(
            ItemList.Seismic_Prospector_Adv_MV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "WWW", "EME", "CXC", 'M', ItemList.Hull_MV, 'W',
                OrePrefixes.plateDouble.get(Materials.BlackSteel), 'E', OrePrefixes.circuit.get(Materials.MV), 'C',
                ItemList.Sensor_MV, 'X', OrePrefixes.cableGt02.get(Materials.Copper) });
        GTModHandler.addCraftingRecipe(
            ItemList.Seismic_Prospector_Adv_HV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "WWW", "EME", "CXC", 'M', ItemList.Hull_HV, 'W',
                OrePrefixes.plateDouble.get(Materials.StainlessSteel), 'E', OrePrefixes.circuit.get(Materials.HV), 'C',
                ItemList.Sensor_HV, 'X', OrePrefixes.cableGt04.get(Materials.Gold) });
        GTModHandler.addCraftingRecipe(
            ItemList.Seismic_Prospector_Adv_EV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "WWW", "EME", "CXC", 'M', ItemList.Hull_EV, 'W',
                OrePrefixes.plateDouble.get(Materials.VanadiumSteel), 'E', OrePrefixes.circuit.get(Materials.EV), 'C',
                ItemList.Sensor_EV, 'X', OrePrefixes.cableGt04.get(Materials.Aluminium) });

        GTModHandler.addCraftingRecipe(
            ItemList.ConcreteBackfiller1.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "WPW", "EME", "CQC", 'M', ItemList.Hull_MV, 'W', OrePrefixes.frameGt.get(Materials.Steel),
                'E', OrePrefixes.circuit.get(Materials.MV), 'C', ItemList.Electric_Motor_MV, 'P',
                OrePrefixes.pipeLarge.get(Materials.Steel), 'Q', ItemList.Electric_Pump_MV });
        GTModHandler.addCraftingRecipe(
            ItemList.ConcreteBackfiller2.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "WPW", "EME", "CQC", 'M', ItemList.ConcreteBackfiller1, 'W',
                OrePrefixes.frameGt.get(Materials.Titanium), 'E', OrePrefixes.circuit.get(Materials.EV), 'C',
                ItemList.Electric_Motor_EV, 'P', OrePrefixes.pipeLarge.get(Materials.Steel), 'Q',
                ItemList.Electric_Pump_EV });

        GTModHandler.addCraftingRecipe(
            ItemList.PyrolyseOven.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "WEP", "EME", "WCP", 'M', ItemList.Hull_MV, 'W', ItemList.Electric_Piston_MV, 'P',
                OrePrefixes.wireGt04.get(Materials.Cupronickel), 'E', OrePrefixes.circuit.get(Materials.MV), 'C',
                ItemList.Electric_Pump_MV });

        GTModHandler.addCraftingRecipe(
            ItemList.OilCracker.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { aTextWireCoil, "EME", aTextWireCoil, 'M', ItemList.Hull_HV, 'W',
                ItemList.Casing_Coil_Cupronickel, 'E', OrePrefixes.circuit.get(Materials.HV), 'C',
                ItemList.Electric_Pump_HV });

        GTModHandler.addCraftingRecipe(
            ItemList.MicroTransmitter_HV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "CPC", aTextCableHull, "GBG", 'M', ItemList.Hull_HV, 'B', ItemList.Battery_RE_HV_Lithium,
                'C', ItemList.Emitter_HV, 'G', OrePrefixes.circuit.get(Materials.HV), 'P',
                ItemList.Field_Generator_HV });
        GTModHandler.addCraftingRecipe(
            ItemList.MicroTransmitter_EV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "CPC", aTextCableHull, "GBG", 'M', ItemList.Hull_EV, 'B',
                GTModHandler.getIC2Item("lapotronCrystal", 1L, WILDCARD), 'C', ItemList.Emitter_EV, 'G',
                OrePrefixes.circuit.get(Materials.EV), 'P', ItemList.Field_Generator_EV });
        GTModHandler.addCraftingRecipe(
            ItemList.MicroTransmitter_IV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "CPC", aTextCableHull, "GBG", 'M', ItemList.Hull_IV, 'B', ItemList.Energy_LapotronicOrb, 'C',
                ItemList.Emitter_IV, 'G', OrePrefixes.circuit.get(Materials.IV), 'P', ItemList.Field_Generator_IV });
        GTModHandler.addCraftingRecipe(
            ItemList.MicroTransmitter_LUV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "CPC", aTextCableHull, "GBG", 'M', ItemList.Hull_LuV, 'B', ItemList.Energy_LapotronicOrb2,
                'C', ItemList.Emitter_LuV, 'G', OrePrefixes.circuit.get(Materials.LuV), 'P',
                ItemList.Field_Generator_LuV });
        GTModHandler.addCraftingRecipe(
            ItemList.MicroTransmitter_ZPM.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "CPC", aTextCableHull, "GBG", 'M', ItemList.Hull_ZPM, 'B', ItemList.Energy_Module, 'C',
                ItemList.Emitter_ZPM, 'G', OrePrefixes.circuit.get(Materials.ZPM), 'P', ItemList.Field_Generator_ZPM });
        GTModHandler.addCraftingRecipe(
            ItemList.MicroTransmitter_UV.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "CPC", aTextCableHull, "GBG", 'M', ItemList.Hull_UV, 'B', ItemList.Energy_Module, 'C',
                ItemList.Emitter_UV, 'G', OrePrefixes.circuit.get(Materials.UV), 'P', ItemList.Field_Generator_UV });

        GTModHandler.addCraftingRecipe(
            ItemList.Machine_Multi_Assemblyline.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { aTextWireCoil, "EME", aTextWireCoil, 'M', ItemList.Hull_IV, 'W', ItemList.Casing_Assembler,
                'E', OrePrefixes.circuit.get(Materials.IV), 'C', ItemList.Robot_Arm_IV });

        GTModHandler.addCraftingRecipe(
            ItemList.Machine_Multi_DieselEngine.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "PCP", "EME", "GWG", 'M', ItemList.Hull_EV, 'P', ItemList.Electric_Piston_EV, 'E',
                ItemList.Electric_Motor_EV, 'C', OrePrefixes.circuit.get(Materials.IV), 'W',
                OrePrefixes.cableGt01.get(Materials.TungstenSteel), 'G', OrePrefixes.gearGt.get(Materials.Titanium) });
        GTModHandler.addCraftingRecipe(
            ItemList.Casing_EngineIntake.get(4L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "PhP", "RFR", aTextPlateWrench, 'R', OrePrefixes.pipeMedium.get(Materials.Titanium), 'F',
                ItemList.Casing_StableTitanium, 'P', OrePrefixes.rotor.get(Materials.Titanium) });

        GTModHandler.addCraftingRecipe(
            ItemList.Machine_Multi_ExtremeDieselEngine.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "PCP", "EME", "GWG", 'M', ItemList.Hull_IV, 'P', ItemList.Electric_Piston_IV, 'E',
                ItemList.Electric_Motor_IV, 'C', OrePrefixes.circuit.get(Materials.LuV), 'W',
                OrePrefixes.cableGt01.get(Materials.HSSG), 'G', OrePrefixes.gearGt.get(Materials.TungstenSteel) });
        GTModHandler.addCraftingRecipe(
            ItemList.Casing_ExtremeEngineIntake.get(4L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "PhP", "RFR", aTextPlateWrench, 'R', OrePrefixes.pipeMedium.get(Materials.TungstenSteel),
                'F', ItemList.Casing_RobustTungstenSteel, 'P', OrePrefixes.rotor.get(Materials.TungstenSteel) });

        // If Cleanroom is enabled, add a recipe, else hide from NEI.
        if (GTMod.proxy.mEnableCleanroom) {
            GTModHandler.addCraftingRecipe(
                ItemList.Machine_Multi_Cleanroom.get(1L),
                GTModHandler.RecipeBits.BITSD,
                new Object[] { "FFF", "RHR", "MCM", 'H', ItemList.Hull_HV, 'F', ItemList.Component_Filter, 'R',
                    OrePrefixes.rotor.get(Materials.StainlessSteel), 'M', ItemList.Electric_Motor_HV, 'C',
                    OrePrefixes.circuit.get(Materials.HV) });
        } else {
            if (NotEnoughItems.isModLoaded()) {
                API.hideItem(ItemList.Machine_Multi_Cleanroom.get(1L));
            }
        }

        GTModHandler.addCraftingRecipe(
            ItemList.Machine_HV_LightningRod.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "LTL", "TMT", "LTL", 'M', ItemList.Hull_LuV, 'L', ItemList.Energy_LapotronicOrb, 'T',
                ItemList.Transformer_ZPM_LuV });
        GTModHandler.addCraftingRecipe(
            ItemList.Machine_EV_LightningRod.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "LTL", "TMT", "LTL", 'M', ItemList.Hull_ZPM, 'L', ItemList.Energy_LapotronicOrb2, 'T',
                ItemList.Transformer_UV_ZPM });
        GTModHandler.addCraftingRecipe(
            ItemList.Machine_IV_LightningRod.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "LTL", "TMT", "LTL", 'M', ItemList.Hull_UV, 'L', ItemList.ZPM2, 'T',
                ItemList.Transformer_MAX_UV });

        GTModHandler.addCraftingRecipe(
            ItemList.Machine_Multi_LargeChemicalReactor.get(1L),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "CRC", "PMP", "CBC", 'C', OrePrefixes.circuit.get(Materials.HV), 'R',
                OrePrefixes.rotor.get(Materials.StainlessSteel), 'P',
                OrePrefixes.pipeLarge.get(Materials.Polytetrafluoroethylene), 'M', ItemList.Electric_Motor_HV, 'B',
                ItemList.Hull_HV });

        // Add Drone down link hatch
        GTModHandler.addCraftingRecipe(
            ItemList.Hatch_DroneDownLink.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { " S ", "CMC", "RRR", 'M', ItemList.Hatch_Maintenance, 'S', ItemList.Sensor_IV, 'R',
                new ItemStack(GregTechAPI.sBlockReinforced, 1, 9), 'C', ItemList.Conveyor_Module_EV });

        // And Drone Centre
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_Assembler.get(1),
                ItemList.Cover_SolarPanel_HV.get(4),
                ItemList.Conveyor_Module_IV.get(2),
                ItemList.Robot_Arm_IV.get(2),
                ItemList.Sensor_IV.get(2),
                ItemList.Energy_LapotronicOrb.get(4),
                ItemList.Cover_WirelessNeedsMaintainance.get(1),
                GalacticraftCore.isModLoaded() ? GTModHandler.getModItem(GalacticraftCore.ID, "item.basicItem", 1, 19)
                    : ItemList.Sensor_EV.get(4))
            .itemOutputs(ItemList.Machine_Multi_DroneCentre.get(1L))
            .fluidInputs(Materials.GlueAdvanced.getFluid(8_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        GTModHandler.addCraftingRecipe(
            ItemList.MagLevPython_MV.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { "CCC", "CHC", "CMC", 'C', ItemList.MV_Coil, 'H', ItemList.Hull_MV, 'M',
                OrePrefixes.block.get(Materials.SteelMagnetic) });

        GTModHandler.addCraftingRecipe(
            ItemList.MagLevPython_HV.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { "CCC", "CHC", "CMC", 'C', ItemList.HV_Coil, 'H', ItemList.Hull_HV, 'M',
                OrePrefixes.block.get(Materials.SteelMagnetic) });

        GTModHandler.addCraftingRecipe(
            ItemList.MagLevPython_EV.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { "CCC", "CHC", "CMC", 'C', ItemList.EV_Coil, 'H', ItemList.Hull_EV, 'M',
                OrePrefixes.block.get(Materials.NeodymiumMagnetic) });
    }

    private static void registerShapelessCraftingRecipes() {
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Casing_SolidSteel.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { ItemList.Casing_Stripes_A });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Casing_SolidSteel.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { ItemList.Casing_Stripes_B });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Casing_SolidSteel.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { ItemList.Casing_RadioactiveHazard });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Casing_SolidSteel.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { ItemList.Casing_BioHazard });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Casing_SolidSteel.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { ItemList.Casing_ExplosionHazard });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Casing_SolidSteel.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { ItemList.Casing_FireHazard });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Casing_SolidSteel.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { ItemList.Casing_AcidHazard });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Casing_SolidSteel.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { ItemList.Casing_MagicHazard });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Casing_SolidSteel.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { ItemList.Casing_FrostHazard });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Casing_SolidSteel.get(1L),
            GTModHandler.RecipeBits.BITS,
            new Object[] { ItemList.Casing_NoiseHazard });

        if (Forestry.isModLoaded() && Gendustry.isModLoaded()) {
            /* Conversion recipes */
            // TODO: Move those recipes with the other recipes

            GTModHandler.addShapelessCraftingRecipe(
                ItemList.Machine_IndustrialApiary.get(1L),
                new Object[] { GTModHandler.getModItem(Gendustry.ID, "IndustrialApiary", 1, 0) });
            GTModHandler.addShapelessCraftingRecipe(
                ItemList.IndustrialApiary_Upgrade_Frame.get(1),
                new Object[] { GTModHandler.getModItem(Gendustry.ID, "UpgradeFrame", 1) });
            GTModHandler.addShapelessCraftingRecipe(
                ItemList.IndustrialApiary_Upgrade_PRODUCTION.get(1L),
                new Object[] { GTModHandler.getModItem(Gendustry.ID, "ApiaryUpgrade", 1, 0) });
            GTModHandler.addShapelessCraftingRecipe(
                ItemList.IndustrialApiary_Upgrade_PLAINS.get(1L),
                new Object[] { GTModHandler.getModItem(Gendustry.ID, "ApiaryUpgrade", 1, 17) });
            GTModHandler.addShapelessCraftingRecipe(
                ItemList.IndustrialApiary_Upgrade_LIGHT.get(1L),
                new Object[] { GTModHandler.getModItem(Gendustry.ID, "ApiaryUpgrade", 1, 11) });
            GTModHandler.addShapelessCraftingRecipe(
                ItemList.IndustrialApiary_Upgrade_FLOWERING.get(1L),
                new Object[] { GTModHandler.getModItem(Gendustry.ID, "ApiaryUpgrade", 1, 2) });
            GTModHandler.addShapelessCraftingRecipe(
                ItemList.IndustrialApiary_Upgrade_WINTER.get(1L),
                new Object[] { GTModHandler.getModItem(Gendustry.ID, "ApiaryUpgrade", 1, 20) });
            GTModHandler.addShapelessCraftingRecipe(
                ItemList.IndustrialApiary_Upgrade_DRYER.get(1L),
                new Object[] { GTModHandler.getModItem(Gendustry.ID, "ApiaryUpgrade", 1, 5) });
            GTModHandler.addShapelessCraftingRecipe(
                ItemList.IndustrialApiary_Upgrade_AUTOMATION.get(1L),
                new Object[] { GTModHandler.getModItem(Gendustry.ID, "ApiaryUpgrade", 1, 14) });
            GTModHandler.addShapelessCraftingRecipe(
                ItemList.IndustrialApiary_Upgrade_HUMIDIFIER.get(1L),
                new Object[] { GTModHandler.getModItem(Gendustry.ID, "ApiaryUpgrade", 1, 4) });
            GTModHandler.addShapelessCraftingRecipe(
                ItemList.IndustrialApiary_Upgrade_HELL.get(1L),
                new Object[] { GTModHandler.getModItem(Gendustry.ID, "ApiaryUpgrade", 1, 13) });
            GTModHandler.addShapelessCraftingRecipe(
                ItemList.IndustrialApiary_Upgrade_POLLEN.get(1L),
                new Object[] { GTModHandler.getModItem(Gendustry.ID, "ApiaryUpgrade", 1, 22) });
            GTModHandler.addShapelessCraftingRecipe(
                ItemList.IndustrialApiary_Upgrade_DESERT.get(1L),
                new Object[] { GTModHandler.getModItem(Gendustry.ID, "ApiaryUpgrade", 1, 16) });
            GTModHandler.addShapelessCraftingRecipe(
                ItemList.IndustrialApiary_Upgrade_COOLER.get(1L),
                new Object[] { GTModHandler.getModItem(Gendustry.ID, "ApiaryUpgrade", 1, 7) });
            GTModHandler.addShapelessCraftingRecipe(
                ItemList.IndustrialApiary_Upgrade_LIFESPAN.get(1L),
                new Object[] { GTModHandler.getModItem(Gendustry.ID, "ApiaryUpgrade", 1, 1) });
            GTModHandler.addShapelessCraftingRecipe(
                ItemList.IndustrialApiary_Upgrade_SEAL.get(1L),
                new Object[] { GTModHandler.getModItem(Gendustry.ID, "ApiaryUpgrade", 1, 10) });
            GTModHandler.addShapelessCraftingRecipe(
                ItemList.IndustrialApiary_Upgrade_STABILIZER.get(1L),
                new Object[] { GTModHandler.getModItem(Gendustry.ID, "ApiaryUpgrade", 1, 19) });
            GTModHandler.addShapelessCraftingRecipe(
                ItemList.IndustrialApiary_Upgrade_JUNGLE.get(1L),
                new Object[] { GTModHandler.getModItem(Gendustry.ID, "ApiaryUpgrade", 1, 18) });
            GTModHandler.addShapelessCraftingRecipe(
                ItemList.IndustrialApiary_Upgrade_TERRITORY.get(1L),
                new Object[] { GTModHandler.getModItem(Gendustry.ID, "ApiaryUpgrade", 1, 3) });
            GTModHandler.addShapelessCraftingRecipe(
                ItemList.IndustrialApiary_Upgrade_OCEAN.get(1L),
                new Object[] { GTModHandler.getModItem(Gendustry.ID, "ApiaryUpgrade", 1, 21) });
            GTModHandler.addShapelessCraftingRecipe(
                ItemList.IndustrialApiary_Upgrade_SKY.get(1L),
                new Object[] { GTModHandler.getModItem(Gendustry.ID, "ApiaryUpgrade", 1, 12) });
            GTModHandler.addShapelessCraftingRecipe(
                ItemList.IndustrialApiary_Upgrade_HEATER.get(1L),
                new Object[] { GTModHandler.getModItem(Gendustry.ID, "ApiaryUpgrade", 1, 6) });
            GTModHandler.addShapelessCraftingRecipe(
                ItemList.IndustrialApiary_Upgrade_SIEVE.get(1L),
                new Object[] { GTModHandler.getModItem(Gendustry.ID, "ApiaryUpgrade", 1, 15) });

        }
    }

    private static void run4() {
        long bits = DISMANTLEABLE | NOT_REMOVABLE | REVERSIBLE | BUFFERED;

        // high pressure fluid pipes
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.pipeSmall, Materials.TungstenSteel, 1L),
                ItemList.Electric_Pump_EV.get(1L))
            .circuit(5)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.pipeSmall, Materials.ZPM, 1L))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.TungstenSteel, 1L),
                ItemList.Electric_Pump_IV.get(1L))
            .circuit(5)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.ZPM, 1L))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_IV / 2)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.pipeLarge, Materials.TungstenSteel, 1L),
                ItemList.Electric_Pump_IV.get(2L))
            .circuit(5)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.pipeLarge, Materials.ZPM, 1L))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        GTModHandler.addCraftingRecipe(
            ItemList.Automation_ChestBuffer_ULV.get(1L),
            bits,
            new Object[] { "CMV", " X ", 'M', ItemList.Hull_ULV, 'V', ItemList.Conveyor_Module_LV, 'C',
                OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.ULV) });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_ChestBuffer_LV.get(1L),
            bits,
            new Object[] { "CMV", " X ", 'M', ItemList.Hull_LV, 'V', ItemList.Conveyor_Module_LV, 'C',
                OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.LV) });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_ChestBuffer_MV.get(1L),
            bits,
            new Object[] { "CMV", " X ", 'M', ItemList.Hull_MV, 'V', ItemList.Conveyor_Module_MV, 'C',
                OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.MV) });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_ChestBuffer_HV.get(1L),
            bits,
            new Object[] { "CMV", " X ", 'M', ItemList.Hull_HV, 'V', ItemList.Conveyor_Module_HV, 'C',
                OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.HV) });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_ChestBuffer_EV.get(1L),
            bits,
            new Object[] { "CMV", " X ", 'M', ItemList.Hull_EV, 'V', ItemList.Conveyor_Module_EV, 'C',
                OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.EV) });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_ChestBuffer_IV.get(1L),
            bits,
            new Object[] { "CMV", " X ", 'M', ItemList.Hull_IV, 'V', ItemList.Conveyor_Module_IV, 'C',
                OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.IV) });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_ChestBuffer_LuV.get(1L),
            bits,
            new Object[] { "CMV", " X ", 'M', ItemList.Hull_LuV, 'V', ItemList.Conveyor_Module_LuV, 'C',
                OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.LuV) });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_ChestBuffer_ZPM.get(1L),
            bits,
            new Object[] { "CMV", " X ", 'M', ItemList.Hull_ZPM, 'V', ItemList.Conveyor_Module_ZPM, 'C',
                OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.ZPM) });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_ChestBuffer_UV.get(1L),
            bits,
            new Object[] { "CMV", " X ", 'M', ItemList.Hull_UV, 'V', ItemList.Conveyor_Module_UV, 'C',
                OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.UV) });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_ChestBuffer_UHV.get(1L),
            bits,
            new Object[] { "CMV", " X ", 'M', ItemList.Hull_MAX, 'V', ItemList.Conveyor_Module_UHV, 'C',
                OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.UHV) });

        GTModHandler.addCraftingRecipe(
            ItemList.Automation_Filter_ULV.get(1L),
            bits,
            new Object[] { " F ", "CMV", " X ", 'M', ItemList.Hull_ULV, 'V', ItemList.Conveyor_Module_LV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_Filter_LV.get(1L),
            bits,
            new Object[] { " F ", "CMV", " X ", 'M', ItemList.Hull_LV, 'V', ItemList.Conveyor_Module_LV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_Filter_MV.get(1L),
            bits,
            new Object[] { " F ", "CMV", " X ", 'M', ItemList.Hull_MV, 'V', ItemList.Conveyor_Module_MV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_Filter_HV.get(1L),
            bits,
            new Object[] { " F ", "CMV", " X ", 'M', ItemList.Hull_HV, 'V', ItemList.Conveyor_Module_HV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_Filter_EV.get(1L),
            bits,
            new Object[] { " F ", "CMV", " X ", 'M', ItemList.Hull_EV, 'V', ItemList.Conveyor_Module_EV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_Filter_IV.get(1L),
            bits,
            new Object[] { " F ", "CMV", " X ", 'M', ItemList.Hull_IV, 'V', ItemList.Conveyor_Module_IV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_Filter_LuV.get(1L),
            bits,
            new Object[] { " F ", "CMV", " X ", 'M', ItemList.Hull_LuV, 'V', ItemList.Conveyor_Module_LuV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_Filter_ZPM.get(1L),
            bits,
            new Object[] { " F ", "CMV", " X ", 'M', ItemList.Hull_ZPM, 'V', ItemList.Conveyor_Module_ZPM, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_Filter_UV.get(1L),
            bits,
            new Object[] { " F ", "CMV", " X ", 'M', ItemList.Hull_UV, 'V', ItemList.Conveyor_Module_UV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_Filter_MAX.get(1L),
            bits,
            new Object[] { " F ", "CMV", " X ", 'M', ItemList.Hull_MAX, 'V', ItemList.Conveyor_Module_UHV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });

        GTModHandler.addCraftingRecipe(
            ItemList.Automation_TypeFilter_ULV.get(1L),
            bits,
            new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_ULV, 'V', ItemList.Conveyor_Module_LV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_TypeFilter_LV.get(1L),
            bits,
            new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_LV, 'V', ItemList.Conveyor_Module_LV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_TypeFilter_MV.get(1L),
            bits,
            new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_MV, 'V', ItemList.Conveyor_Module_MV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_TypeFilter_HV.get(1L),
            bits,
            new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_HV, 'V', ItemList.Conveyor_Module_HV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_TypeFilter_EV.get(1L),
            bits,
            new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_EV, 'V', ItemList.Conveyor_Module_EV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_TypeFilter_IV.get(1L),
            bits,
            new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_IV, 'V', ItemList.Conveyor_Module_IV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_TypeFilter_LuV.get(1L),
            bits,
            new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_LuV, 'V', ItemList.Conveyor_Module_LuV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_TypeFilter_ZPM.get(1L),
            bits,
            new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_ZPM, 'V', ItemList.Conveyor_Module_ZPM, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_TypeFilter_UV.get(1L),
            bits,
            new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_UV, 'V', ItemList.Conveyor_Module_UV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_TypeFilter_MAX.get(1L),
            bits,
            new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_MAX, 'V', ItemList.Conveyor_Module_UHV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });

        GTModHandler.addCraftingRecipe(
            ItemList.Automation_Regulator_ULV.get(1L),
            bits,
            new Object[] { "XFX", "VMV", "XCX", 'M', ItemList.Hull_ULV, 'V', ItemList.Robot_Arm_LV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_Regulator_LV.get(1L),
            bits,
            new Object[] { "XFX", "VMV", "XCX", 'M', ItemList.Hull_LV, 'V', ItemList.Robot_Arm_LV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_Regulator_MV.get(1L),
            bits,
            new Object[] { "XFX", "VMV", "XCX", 'M', ItemList.Hull_MV, 'V', ItemList.Robot_Arm_MV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_Regulator_HV.get(1L),
            bits,
            new Object[] { "XFX", "VMV", "XCX", 'M', ItemList.Hull_HV, 'V', ItemList.Robot_Arm_HV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_Regulator_EV.get(1L),
            bits,
            new Object[] { "XFX", "VMV", "XCX", 'M', ItemList.Hull_EV, 'V', ItemList.Robot_Arm_EV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_Regulator_IV.get(1L),
            bits,
            new Object[] { "XFX", "VMV", "XCX", 'M', ItemList.Hull_IV, 'V', ItemList.Robot_Arm_IV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_Regulator_LuV.get(1L),
            bits,
            new Object[] { "XFX", "VMV", "XCX", 'M', ItemList.Hull_LuV, 'V', ItemList.Robot_Arm_LuV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_Regulator_ZPM.get(1L),
            bits,
            new Object[] { "XFX", "VMV", "XCX", 'M', ItemList.Hull_ZPM, 'V', ItemList.Robot_Arm_ZPM, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_Regulator_UV.get(1L),
            bits,
            new Object[] { "XFX", "VMV", "XCX", 'M', ItemList.Hull_UV, 'V', ItemList.Robot_Arm_UV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_Regulator_MAX.get(1L),
            bits,
            new Object[] { "XFX", "VMV", "XCX", 'M', ItemList.Hull_MAX, 'V', ItemList.Robot_Arm_UHV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });

        GTModHandler.addCraftingRecipe(
            ItemList.Automation_SuperBuffer_ULV.get(1L),
            bits,
            new Object[] { "DMV", 'M', ItemList.Automation_ChestBuffer_ULV, 'V', ItemList.Conveyor_Module_LV, 'D',
                ItemList.Tool_DataOrb });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_SuperBuffer_LV.get(1L),
            bits,
            new Object[] { "DMV", 'M', ItemList.Automation_ChestBuffer_LV, 'V', ItemList.Conveyor_Module_LV, 'D',
                ItemList.Tool_DataOrb });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_SuperBuffer_MV.get(1L),
            bits,
            new Object[] { "DMV", 'M', ItemList.Automation_ChestBuffer_MV, 'V', ItemList.Conveyor_Module_MV, 'D',
                ItemList.Tool_DataOrb });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_SuperBuffer_HV.get(1L),
            bits,
            new Object[] { "DMV", 'M', ItemList.Automation_ChestBuffer_HV, 'V', ItemList.Conveyor_Module_HV, 'D',
                ItemList.Tool_DataOrb });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_SuperBuffer_EV.get(1L),
            bits,
            new Object[] { "DMV", 'M', ItemList.Automation_ChestBuffer_EV, 'V', ItemList.Conveyor_Module_EV, 'D',
                ItemList.Tool_DataOrb });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_SuperBuffer_IV.get(1L),
            bits,
            new Object[] { "DMV", 'M', ItemList.Automation_ChestBuffer_IV, 'V', ItemList.Conveyor_Module_IV, 'D',
                ItemList.Tool_DataOrb });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_SuperBuffer_LuV.get(1L),
            bits,
            new Object[] { "DMV", 'M', ItemList.Automation_ChestBuffer_LuV, 'V', ItemList.Conveyor_Module_LuV, 'D',
                ItemList.Tool_DataOrb });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_SuperBuffer_ZPM.get(1L),
            bits,
            new Object[] { "DMV", 'M', ItemList.Automation_ChestBuffer_ZPM, 'V', ItemList.Conveyor_Module_ZPM, 'D',
                ItemList.Tool_DataOrb });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_SuperBuffer_UV.get(1L),
            bits,
            new Object[] { "DMV", 'M', ItemList.Automation_ChestBuffer_UV, 'V', ItemList.Conveyor_Module_UV, 'D',
                ItemList.Tool_DataOrb });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_SuperBuffer_MAX.get(1L),
            bits,
            new Object[] { "DMV", 'M', ItemList.Automation_ChestBuffer_UHV, 'V', ItemList.Conveyor_Module_UHV, 'D',
                ItemList.Tool_DataOrb });

        GTModHandler.addCraftingRecipe(
            ItemList.Automation_SuperBuffer_ULV.get(1L),
            bits,
            new Object[] { "DMV", "DDD", 'M', ItemList.Hull_ULV, 'V', ItemList.Conveyor_Module_LV, 'D',
                ItemList.Tool_DataStick });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_SuperBuffer_LV.get(1L),
            bits,
            new Object[] { "DMV", "DDD", 'M', ItemList.Hull_LV, 'V', ItemList.Conveyor_Module_LV, 'D',
                ItemList.Tool_DataStick });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_SuperBuffer_MV.get(1L),
            bits,
            new Object[] { "DMV", "DDD", 'M', ItemList.Hull_MV, 'V', ItemList.Conveyor_Module_MV, 'D',
                ItemList.Tool_DataStick });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_SuperBuffer_HV.get(1L),
            bits,
            new Object[] { "DMV", "DDD", 'M', ItemList.Hull_HV, 'V', ItemList.Conveyor_Module_HV, 'D',
                ItemList.Tool_DataStick });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_SuperBuffer_EV.get(1L),
            bits,
            new Object[] { "DMV", "DDD", 'M', ItemList.Hull_EV, 'V', ItemList.Conveyor_Module_EV, 'D',
                ItemList.Tool_DataStick });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_SuperBuffer_IV.get(1L),
            bits,
            new Object[] { "DMV", "DDD", 'M', ItemList.Hull_IV, 'V', ItemList.Conveyor_Module_IV, 'D',
                ItemList.Tool_DataStick });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_SuperBuffer_LuV.get(1L),
            bits,
            new Object[] { "DMV", "DDD", 'M', ItemList.Hull_LuV, 'V', ItemList.Conveyor_Module_LuV, 'D',
                ItemList.Tool_DataStick });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_SuperBuffer_ZPM.get(1L),
            bits,
            new Object[] { "DMV", "DDD", 'M', ItemList.Hull_ZPM, 'V', ItemList.Conveyor_Module_ZPM, 'D',
                ItemList.Tool_DataStick });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_SuperBuffer_UV.get(1L),
            bits,
            new Object[] { "DMV", "DDD", 'M', ItemList.Hull_UV, 'V', ItemList.Conveyor_Module_UV, 'D',
                ItemList.Tool_DataStick });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_SuperBuffer_MAX.get(1L),
            bits,
            new Object[] { "DMV", "DDD", 'M', ItemList.Hull_MAX, 'V', ItemList.Conveyor_Module_UHV, 'D',
                ItemList.Tool_DataStick });

        GTModHandler.addCraftingRecipe(
            ItemList.Automation_ItemDistributor_ULV.get(1L),
            bits,
            new Object[] { "XCX", "VMV", " V ", 'M', ItemList.Hull_ULV, 'V', ItemList.Conveyor_Module_LV, 'C',
                OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.LV) });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_ItemDistributor_LV.get(1L),
            bits,
            new Object[] { "XCX", "VMV", " V ", 'M', ItemList.Hull_LV, 'V', ItemList.Conveyor_Module_LV, 'C',
                OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.LV) });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_ItemDistributor_MV.get(1L),
            bits,
            new Object[] { "XCX", "VMV", " V ", 'M', ItemList.Hull_MV, 'V', ItemList.Conveyor_Module_MV, 'C',
                OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.LV) });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_ItemDistributor_HV.get(1L),
            bits,
            new Object[] { "XCX", "VMV", " V ", 'M', ItemList.Hull_HV, 'V', ItemList.Conveyor_Module_HV, 'C',
                OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.LV) });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_ItemDistributor_EV.get(1L),
            bits,
            new Object[] { "XCX", "VMV", " V ", 'M', ItemList.Hull_EV, 'V', ItemList.Conveyor_Module_EV, 'C',
                OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.LV) });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_ItemDistributor_IV.get(1L),
            bits,
            new Object[] { "XCX", "VMV", " V ", 'M', ItemList.Hull_IV, 'V', ItemList.Conveyor_Module_IV, 'C',
                OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.LV) });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_ItemDistributor_LuV.get(1L),
            bits,
            new Object[] { "XCX", "VMV", " V ", 'M', ItemList.Hull_LuV, 'V', ItemList.Conveyor_Module_LuV, 'C',
                OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.LV) });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_ItemDistributor_ZPM.get(1L),
            bits,
            new Object[] { "XCX", "VMV", " V ", 'M', ItemList.Hull_ZPM, 'V', ItemList.Conveyor_Module_ZPM, 'C',
                OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.LV) });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_ItemDistributor_UV.get(1L),
            bits,
            new Object[] { "XCX", "VMV", " V ", 'M', ItemList.Hull_UV, 'V', ItemList.Conveyor_Module_UV, 'C',
                OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.LV) });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_ItemDistributor_MAX.get(1L),
            bits,
            new Object[] { "XCX", "VMV", " V ", 'M', ItemList.Hull_MAX, 'V', ItemList.Conveyor_Module_UHV, 'C',
                OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.LV) });

        GTModHandler.addCraftingRecipe(
            ItemList.Automation_RecipeFilter_ULV.get(1L),
            bits,
            new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_ULV, 'V', ItemList.Robot_Arm_LV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_RecipeFilter_LV.get(1L),
            bits,
            new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_LV, 'V', ItemList.Robot_Arm_LV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_RecipeFilter_MV.get(1L),
            bits,
            new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_MV, 'V', ItemList.Robot_Arm_MV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_RecipeFilter_HV.get(1L),
            bits,
            new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_HV, 'V', ItemList.Robot_Arm_HV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_RecipeFilter_EV.get(1L),
            bits,
            new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_EV, 'V', ItemList.Robot_Arm_EV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_RecipeFilter_IV.get(1L),
            bits,
            new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_IV, 'V', ItemList.Robot_Arm_IV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_RecipeFilter_LuV.get(1L),
            bits,
            new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_LuV, 'V', ItemList.Robot_Arm_LuV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_RecipeFilter_ZPM.get(1L),
            bits,
            new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_ZPM, 'V', ItemList.Robot_Arm_ZPM, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GTModHandler.addCraftingRecipe(
            ItemList.Automation_RecipeFilter_UV.get(1L),
            bits,
            new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_UV, 'V', ItemList.Robot_Arm_UV, 'C',
                OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                OrePrefixes.circuit.get(Materials.LV) });
        GTModHandler.addCraftingRecipe(
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
        GTLog.out.println("GTMod: Recipes for MetaTileEntities.");
        registerMachineTypes();
        PCBFactoryMaterialLoader.load();
        run4();
    }
}
