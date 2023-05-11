package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gregtech.api.GregTech_API;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.automation.GT_MetaTileEntity_ElectricAutoWorkbench;
import gtPlusPlus.xmod.gregtech.common.tileentities.automation.GT_MetaTileEntity_ElectricInventoryManager;
import gtPlusPlus.xmod.gregtech.common.tileentities.automation.GT_MetaTileEntity_TesseractGenerator;
import gtPlusPlus.xmod.gregtech.common.tileentities.automation.GT_MetaTileEntity_TesseractTerminal;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic.GT_MetaTileEntity_CropHarvestor;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.GT4Entity_AutoCrafter;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.GT4Entity_ThermalBoiler;
import gtPlusPlus.xmod.gregtech.common.tileentities.redstone.GT_MetaTileEntity_RedstoneButtonPanel;
import gtPlusPlus.xmod.gregtech.common.tileentities.redstone.GT_MetaTileEntity_RedstoneCircuitBlock;
import gtPlusPlus.xmod.gregtech.common.tileentities.redstone.GT_MetaTileEntity_RedstoneLamp;
import gtPlusPlus.xmod.gregtech.common.tileentities.redstone.GT_MetaTileEntity_RedstoneStrengthDisplay;
import gtPlusPlus.xmod.gregtech.common.tileentities.redstone.GT_MetaTileEntity_RedstoneStrengthScale;
import gtPlusPlus.xmod.gregtech.common.tileentities.storage.GT_MetaTileEntity_AdvancedCraftingTable;
import gtPlusPlus.xmod.gregtech.common.tileentities.storage.GT_MetaTileEntity_BronzeCraftingTable;
import gtPlusPlus.xmod.gregtech.common.tileentities.storage.shelving.GT4Entity_Shelf;
import gtPlusPlus.xmod.gregtech.common.tileentities.storage.shelving.GT4Entity_Shelf_Compartment;
import gtPlusPlus.xmod.gregtech.common.tileentities.storage.shelving.GT4Entity_Shelf_Desk;
import gtPlusPlus.xmod.gregtech.common.tileentities.storage.shelving.GT4Entity_Shelf_FileCabinet;
import gtPlusPlus.xmod.gregtech.common.tileentities.storage.shelving.GT4Entity_Shelf_Iron;
import gtPlusPlus.xmod.gregtech.common.tileentities.storage.shelving.GT4Entity_Shelf_Large;

public class Gregtech4Content {

    // ID Range 828, 829, 833 - 850

    public static void run() {
        Logger.INFO("Max MTE: " + GregTech_API.METATILEENTITIES.length + " | " + GregTech_API.MAXIMUM_METATILE_IDS);
        workbenches();
        thermalBoiler();
        multiCrafter();
        tesseracts();
        shelves();
        basic();
        automation();
        redstone();
    }

    private static void workbenches() {
        // Gregtech 4 Workbenches
        Logger.INFO("Gregtech 4 Content | Registering Workbenches.");
        GregtechItemList.GT4_Workbench_Bronze.set(
                new GT_MetaTileEntity_BronzeCraftingTable(
                        31081,
                        "workbench.basic",
                        "Bronze Workbench",
                        0,
                        "Stores 16000L of fluid").getStackForm(1L));
        GregtechItemList.GT4_Workbench_Advanced.set(
                new GT_MetaTileEntity_AdvancedCraftingTable(
                        31082,
                        "workbench.advanced",
                        "Advanced Workbench",
                        3,
                        "Stores 64000L of fluid").getStackForm(1L));
    }

    private static void redstone() {
        Logger.INFO("Gregtech 4 Content | Registering Redstone Blocks.");
        GregtechItemList.GT4_Redstone_Lamp.set(new GT_MetaTileEntity_RedstoneLamp(31120).getStackForm(1L));
        GregtechItemList.GT4_Redstone_Button_Panel
                .set(new GT_MetaTileEntity_RedstoneButtonPanel(31121).getStackForm(1L));
        GregtechItemList.GT4_Redstone_Scale.set(new GT_MetaTileEntity_RedstoneStrengthScale(31122).getStackForm(1L));
        GregtechItemList.GT4_Redstone_Display.set(
                new GT_MetaTileEntity_RedstoneStrengthDisplay(
                        31123,
                        "redstone.display.strength",
                        "Redstone Display",
                        "Displays Redstone Strength").getStackForm(1L));
        GregtechItemList.GT4_Redstone_Circuit.set(new GT_MetaTileEntity_RedstoneCircuitBlock(31124).getStackForm(1L));
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

        Logger.INFO("Gregtech 4 Content | Registering Inventory Managers.");
        GregtechItemList.GT4_Electric_Inventory_Manager_LV.set(
                new GT_MetaTileEntity_ElectricInventoryManager(31101, 1, "Manages your inventory and stuff")
                        .getStackForm(1L));
        GregtechItemList.GT4_Electric_Inventory_Manager_MV.set(
                new GT_MetaTileEntity_ElectricInventoryManager(31102, 2, "Manages your inventory and stuff")
                        .getStackForm(1L));
        GregtechItemList.GT4_Electric_Inventory_Manager_HV.set(
                new GT_MetaTileEntity_ElectricInventoryManager(31103, 3, "Manages your inventory and stuff")
                        .getStackForm(1L));
        GregtechItemList.GT4_Electric_Inventory_Manager_EV.set(
                new GT_MetaTileEntity_ElectricInventoryManager(31104, 4, "Manages your inventory and stuff")
                        .getStackForm(1L));
        GregtechItemList.GT4_Electric_Inventory_Manager_IV.set(
                new GT_MetaTileEntity_ElectricInventoryManager(31105, 5, "Manages your inventory and stuff")
                        .getStackForm(1L));
        GregtechItemList.GT4_Electric_Inventory_Manager_LuV.set(
                new GT_MetaTileEntity_ElectricInventoryManager(31106, 6, "Manages your inventory and stuff")
                        .getStackForm(1L));
        GregtechItemList.GT4_Electric_Inventory_Manager_ZPM.set(
                new GT_MetaTileEntity_ElectricInventoryManager(31107, 7, "Manages your inventory and stuff")
                        .getStackForm(1L));
        GregtechItemList.GT4_Electric_Inventory_Manager_UV.set(
                new GT_MetaTileEntity_ElectricInventoryManager(31108, 8, "Manages your inventory and stuff")
                        .getStackForm(1L));
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

    private static void shelves() {
        // Gregtech 4 Shelves
        Logger.INFO("Gregtech 4 Content | Registering Shelves.");
        GregtechItemList.GT4_Shelf.set(
                new GT4Entity_Shelf(870, "gtplusplus.shelf.wooden", "Wooden Shelf", "Usually used for books")
                        .getStackForm(1L));
        GregtechItemList.GT4_Shelf_Iron.set(
                new GT4Entity_Shelf_Iron(871, "gtplusplus.shelf.iron", "Metal Shelf", "A heavy duty shelf")
                        .getStackForm(1L));
        GregtechItemList.GT4_Shelf_FileCabinet.set(
                new GT4Entity_Shelf_FileCabinet(
                        872,
                        "gtplusplus.shelf.filecabinet",
                        "File Cabinet",
                        "Could look nice in your office").getStackForm(1L));
        GregtechItemList.GT4_Shelf_Desk.set(
                new GT4Entity_Shelf_Desk(873, "gtplusplus.shelf.desk", "Metal encased Desk", "A place to study")
                        .getStackForm(1L));
        GregtechItemList.GT4_Shelf_Compartment.set(
                new GT4Entity_Shelf_Compartment(
                        874,
                        "gtplusplus.shelf.compartment",
                        "Compartment",
                        "Stores Books & Things").getStackForm(1L));

        // Custom Storage
        GregtechItemList.GT4_Shelf_Large.set(
                new GT4Entity_Shelf_Large(966, "gtplusplus.shelf.large", "Large Shelf", "A spacious shelf", 2048)
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
