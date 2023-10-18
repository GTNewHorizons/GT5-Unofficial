package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gregtech.api.GregTech_API;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.automation.GT_MetaTileEntity_ElectricAutoWorkbench;
import gtPlusPlus.xmod.gregtech.common.tileentities.automation.GT_MetaTileEntity_TesseractGenerator;
import gtPlusPlus.xmod.gregtech.common.tileentities.automation.GT_MetaTileEntity_TesseractTerminal;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic.GT_MetaTileEntity_CropHarvestor;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.GT4Entity_AutoCrafter;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.GT4Entity_ThermalBoiler;

public class Gregtech4Content {

    // ID Range 828, 829, 833 - 850

    public static void run() {
        Logger.INFO("Max MTE: " + GregTech_API.METATILEENTITIES.length + " | " + GregTech_API.MAXIMUM_METATILE_IDS);
        thermalBoiler();
        multiCrafter();
        tesseracts();
        basic();
        automation();
    }

    private static void automation() {
        Logger.INFO("Gregtech 4 Content | Registering Auto Workbenches.");
        GregtechItemList.GT4_Electric_Auto_Workbench_LV.set(
                new GT_MetaTileEntity_ElectricAutoWorkbench(31091, 1, "Automatic crafting machine").getStackForm(1L));
        GregtechItemList.GT4_Electric_Auto_Workbench_MV.set(
                new GT_MetaTileEntity_ElectricAutoWorkbench(31092, 2, "Automatic crafting machine").getStackForm(1L));
        GregtechItemList.GT4_Electric_Auto_Workbench_HV.set(
                new GT_MetaTileEntity_ElectricAutoWorkbench(31093, 3, "Automatic crafting machine").getStackForm(1L));
        GregtechItemList.GT4_Electric_Auto_Workbench_EV.set(
                new GT_MetaTileEntity_ElectricAutoWorkbench(31094, 4, "Automatic crafting machine").getStackForm(1L));
        GregtechItemList.GT4_Electric_Auto_Workbench_IV.set(
                new GT_MetaTileEntity_ElectricAutoWorkbench(31095, 5, "Automatic crafting machine").getStackForm(1L));
        GregtechItemList.GT4_Electric_Auto_Workbench_LuV.set(
                new GT_MetaTileEntity_ElectricAutoWorkbench(31096, 6, "Automatic crafting machine").getStackForm(1L));
        GregtechItemList.GT4_Electric_Auto_Workbench_ZPM.set(
                new GT_MetaTileEntity_ElectricAutoWorkbench(31097, 7, "Automatic crafting machine").getStackForm(1L));
        GregtechItemList.GT4_Electric_Auto_Workbench_UV.set(
                new GT_MetaTileEntity_ElectricAutoWorkbench(31098, 8, "Automatic crafting machine").getStackForm(1L));

    }

    private static void basic() {
        Logger.INFO("Gregtech 4 Content | Registering Crop Managers.");
        GregtechItemList.GT4_Crop_Harvester_LV.set(
                new GT_MetaTileEntity_CropHarvestor(31111, 1, "Harvests the Cropsticks in front of it")
                        .getStackForm(1L));
        GregtechItemList.GT4_Crop_Harvester_MV.set(
                new GT_MetaTileEntity_CropHarvestor(31112, 2, "Harvests the Cropsticks in front of it")
                        .getStackForm(1L));
        GregtechItemList.GT4_Crop_Harvester_HV.set(
                new GT_MetaTileEntity_CropHarvestor(31113, 3, "Harvests the Cropsticks in front of it")
                        .getStackForm(1L));
        GregtechItemList.GT4_Crop_Harvester_EV.set(
                new GT_MetaTileEntity_CropHarvestor(31114, 4, "Harvests the Cropsticks in front of it")
                        .getStackForm(1L));
        GregtechItemList.GT4_Crop_Harvester_IV.set(
                new GT_MetaTileEntity_CropHarvestor(31115, 5, "Harvests the Cropsticks in front of it")
                        .getStackForm(1L));
        GregtechItemList.GT4_Crop_Harvester_LuV.set(
                new GT_MetaTileEntity_CropHarvestor(31116, 6, "Harvests the Cropsticks in front of it")
                        .getStackForm(1L));
        GregtechItemList.GT4_Crop_Harvester_ZPM.set(
                new GT_MetaTileEntity_CropHarvestor(31117, 7, "Harvests the Cropsticks in front of it")
                        .getStackForm(1L));
        GregtechItemList.GT4_Crop_Harvester_UV.set(
                new GT_MetaTileEntity_CropHarvestor(31118, 8, "Harvests the Cropsticks in front of it")
                        .getStackForm(1L));
    }

    private static void tesseracts() {
        // Gregtech 4 Tesseracts
        Logger.INFO("Gregtech 4 Content | Registering Tesseracts.");
        GregtechItemList.GT4_Tesseract_Generator.set(
                new GT_MetaTileEntity_TesseractGenerator(833, "tesseract.generator", "Tesseract Generator", 4)
                        .getStackForm(1L));
        GregtechItemList.GT4_Tesseract_Terminal.set(
                new GT_MetaTileEntity_TesseractTerminal(834, "tesseract.terminal", "Tesseract Terminal", 4)
                        .getStackForm(1L));
    }

    private static void thermalBoiler() {
        // Gregtech 4 Thermal Boiler
        if (CORE.ConfigSwitches.enableMultiblock_ThermalBoiler) {
            Logger.INFO("Gregtech 4 Content | Registering Thermal Boiler.");
            GregtechItemList.GT4_Thermal_Boiler.set(
                    new GT4Entity_ThermalBoiler(875, "gtplusplus.thermal.boiler", "Thermal Boiler").getStackForm(1L));
        }
    }

    private static void multiCrafter() {
        // Gregtech 4 Multiblock Auto-Crafter
        Logger.INFO("Gregtech 4 Content | Registering Multiblock Crafter.");
        GregtechItemList.GT4_Multi_Crafter.set(
                new GT4Entity_AutoCrafter(876, "gtplusplus.autocrafter.multi", "Large Scale Auto-Assembler v1.01")
                        .getStackForm(1L));
    }
}
