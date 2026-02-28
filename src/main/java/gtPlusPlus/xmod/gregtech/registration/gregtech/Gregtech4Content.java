package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.GT4_Crop_Harvester_EV;
import static gregtech.api.enums.MetaTileEntityIDs.GT4_Crop_Harvester_HV;
import static gregtech.api.enums.MetaTileEntityIDs.GT4_Crop_Harvester_IV;
import static gregtech.api.enums.MetaTileEntityIDs.GT4_Crop_Harvester_LV;
import static gregtech.api.enums.MetaTileEntityIDs.GT4_Crop_Harvester_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.GT4_Crop_Harvester_MV;
import static gregtech.api.enums.MetaTileEntityIDs.GT4_Crop_Harvester_UV;
import static gregtech.api.enums.MetaTileEntityIDs.GT4_Crop_Harvester_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.GT4_Electric_Auto_Workbench_EV;
import static gregtech.api.enums.MetaTileEntityIDs.GT4_Electric_Auto_Workbench_HV;
import static gregtech.api.enums.MetaTileEntityIDs.GT4_Electric_Auto_Workbench_IV;
import static gregtech.api.enums.MetaTileEntityIDs.GT4_Electric_Auto_Workbench_LV;
import static gregtech.api.enums.MetaTileEntityIDs.GT4_Electric_Auto_Workbench_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.GT4_Electric_Auto_Workbench_MV;
import static gregtech.api.enums.MetaTileEntityIDs.GT4_Electric_Auto_Workbench_UV;
import static gregtech.api.enums.MetaTileEntityIDs.GT4_Electric_Auto_Workbench_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.GT4_Multi_Crafter;
import static gregtech.api.enums.MetaTileEntityIDs.GT4_Tesseract_Generator;
import static gregtech.api.enums.MetaTileEntityIDs.GT4_Tesseract_Terminal;
import static gregtech.api.enums.MetaTileEntityIDs.GT4_Thermal_Boiler;

import gregtech.api.GregTechAPI;
import gregtech.api.util.GTUtility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.automation.MTEElectricAutoWorkbench;
import gtPlusPlus.xmod.gregtech.common.tileentities.automation.MTETesseractGenerator;
import gtPlusPlus.xmod.gregtech.common.tileentities.automation.MTETesseractTerminal;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic.MTECropHarvestor;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.MTEAutoCrafter;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.MTEThermalBoiler;

public class Gregtech4Content {

    // ID Range 828, 829, 833 - 850

    public static void run() {
        Logger.INFO("Max MTE: " + GregTechAPI.METATILEENTITIES.length + " | " + GregTechAPI.MAXIMUM_METATILE_IDS);
        thermalBoiler();
        multiCrafter();
        tesseracts();
        basic();
        automation();
    }

    private static void automation() {
        Logger.INFO("Gregtech 4 Content | Registering Auto Workbenches.");
        GregtechItemList.GT4_Electric_Auto_Workbench_LV.set(
            new MTEElectricAutoWorkbench(GT4_Electric_Auto_Workbench_LV.ID, 1, "Automatic crafting machine")
                .getStackForm(1L));
        GregtechItemList.GT4_Electric_Auto_Workbench_MV.set(
            new MTEElectricAutoWorkbench(GT4_Electric_Auto_Workbench_MV.ID, 2, "Automatic crafting machine")
                .getStackForm(1L));
        GregtechItemList.GT4_Electric_Auto_Workbench_HV.set(
            new MTEElectricAutoWorkbench(GT4_Electric_Auto_Workbench_HV.ID, 3, "Automatic crafting machine")
                .getStackForm(1L));
        GregtechItemList.GT4_Electric_Auto_Workbench_EV.set(
            new MTEElectricAutoWorkbench(GT4_Electric_Auto_Workbench_EV.ID, 4, "Automatic crafting machine")
                .getStackForm(1L));
        GregtechItemList.GT4_Electric_Auto_Workbench_IV.set(
            new MTEElectricAutoWorkbench(GT4_Electric_Auto_Workbench_IV.ID, 5, "Automatic crafting machine")
                .getStackForm(1L));
        GregtechItemList.GT4_Electric_Auto_Workbench_LuV.set(
            new MTEElectricAutoWorkbench(GT4_Electric_Auto_Workbench_LuV.ID, 6, "Automatic crafting machine")
                .getStackForm(1L));
        GregtechItemList.GT4_Electric_Auto_Workbench_ZPM.set(
            new MTEElectricAutoWorkbench(GT4_Electric_Auto_Workbench_ZPM.ID, 7, "Automatic crafting machine")
                .getStackForm(1L));
        GregtechItemList.GT4_Electric_Auto_Workbench_UV.set(
            new MTEElectricAutoWorkbench(GT4_Electric_Auto_Workbench_UV.ID, 8, "Automatic crafting machine")
                .getStackForm(1L));

    }

    private static void basic() {
        Logger.INFO("Gregtech 4 Content | Registering Crop Managers.");
        GregtechItemList.GT4_Crop_Harvester_LV.set(
            new MTECropHarvestor(
                GT4_Crop_Harvester_LV.ID,
                1,
                GTUtility.translate("gt.machines.cropharvestor.description")).getStackForm(1L));
        GregtechItemList.GT4_Crop_Harvester_MV.set(
            new MTECropHarvestor(
                GT4_Crop_Harvester_MV.ID,
                2,
                GTUtility.translate("gt.machines.cropharvestor.description")).getStackForm(1L));
        GregtechItemList.GT4_Crop_Harvester_HV.set(
            new MTECropHarvestor(
                GT4_Crop_Harvester_HV.ID,
                3,
                GTUtility.translate("gt.machines.cropharvestor.description")).getStackForm(1L));
        GregtechItemList.GT4_Crop_Harvester_EV.set(
            new MTECropHarvestor(
                GT4_Crop_Harvester_EV.ID,
                4,
                GTUtility.translate("gt.machines.cropharvestor.description")).getStackForm(1L));
        GregtechItemList.GT4_Crop_Harvester_IV.set(
            new MTECropHarvestor(
                GT4_Crop_Harvester_IV.ID,
                5,
                GTUtility.translate("gt.machines.cropharvestor.description")).getStackForm(1L));
        GregtechItemList.GT4_Crop_Harvester_LuV.set(
            new MTECropHarvestor(
                GT4_Crop_Harvester_LuV.ID,
                6,
                GTUtility.translate("gt.machines.cropharvestor.description")).getStackForm(1L));
        GregtechItemList.GT4_Crop_Harvester_ZPM.set(
            new MTECropHarvestor(
                GT4_Crop_Harvester_ZPM.ID,
                7,
                GTUtility.translate("gt.machines.cropharvestor.description")).getStackForm(1L));
        GregtechItemList.GT4_Crop_Harvester_UV.set(
            new MTECropHarvestor(
                GT4_Crop_Harvester_UV.ID,
                8,
                GTUtility.translate("gt.machines.cropharvestor.description")).getStackForm(1L));
    }

    private static void tesseracts() {
        // Gregtech 4 Tesseracts
        Logger.INFO("Gregtech 4 Content | Registering Tesseracts.");
        GregtechItemList.GT4_Tesseract_Generator.set(
            new MTETesseractGenerator(GT4_Tesseract_Generator.ID, "tesseract.generator", "Tesseract Generator", 4)
                .getStackForm(1L));
        GregtechItemList.GT4_Tesseract_Terminal.set(
            new MTETesseractTerminal(GT4_Tesseract_Terminal.ID, "tesseract.terminal", "Tesseract Terminal", 4)
                .getStackForm(1L));
    }

    private static void thermalBoiler() {
        // Gregtech 4 Thermal Boiler
        Logger.INFO("Gregtech 4 Content | Registering Thermal Boiler.");
        GregtechItemList.GT4_Thermal_Boiler.set(
            new MTEThermalBoiler(GT4_Thermal_Boiler.ID, "gtplusplus.thermal.boiler", "Thermal Boiler")
                .getStackForm(1L));

    }

    private static void multiCrafter() {
        // Gregtech 4 Multiblock Auto-Crafter
        Logger.INFO("Gregtech 4 Content | Registering Multiblock Crafter.");
        GregtechItemList.GT4_Multi_Crafter.set(
            new MTEAutoCrafter(GT4_Multi_Crafter.ID, "gtplusplus.autocrafter.multi", "Large Scale Auto-Assembler v1.01")
                .getStackForm(1L));
    }
}
