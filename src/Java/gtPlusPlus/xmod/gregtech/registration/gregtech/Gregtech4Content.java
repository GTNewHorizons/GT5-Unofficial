package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.automation.GT_MetaTileEntity_TesseractGenerator;
import gtPlusPlus.xmod.gregtech.common.tileentities.automation.GT_MetaTileEntity_TesseractTerminal;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.GT4Entity_AutoCrafter;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.GT4Entity_ThermalBoiler;
import gtPlusPlus.xmod.gregtech.common.tileentities.storage.shelving.*;

public class Gregtech4Content
{

	//ID Range 828, 829, 833 - 850

	public static void run()
	{
		if (LoadedMods.Gregtech){
			workbenches();
			thermalBoiler();
			multiCrafter();
			tesseracts();
			shelves();
		}
	}

	private static void workbenches(){
		//Gregtech 4 Workbenches
		Utils.LOG_INFO("Gregtech 4 Content | Registering Workbenches.");
		//Free	//GregtechItemList.GT4_Workbench_Bronze.set(new GT_MetaTileEntity_BronzeCraftingTable(828, "workbench.bronze", "Bronze Workbench", 0).getStackForm(1L));
		//Free	//GregtechItemList.GT4_Workbench_Advanced.set(new GT_MetaTileEntity_AdvancedCraftingTable(829, "workbench.advanced", "Advanced Workbench", 1).getStackForm(1L));
	}
	
	private static void thermalBoiler(){
		//Gregtech 4 Workbenches
		Utils.LOG_INFO("Gregtech 4 Content | Registering Thermal Boiler.");
		GregtechItemList.GT4_Thermal_Boiler.set(new GT4Entity_ThermalBoiler(810, "gtplusplus.thermal.boiler", "Thermal Boiler").getStackForm(1L));
	}
	
	private static void multiCrafter(){
		//Gregtech 4 Workbenches
		Utils.LOG_INFO("Gregtech 4 Content | Registering Multiblock Crafter.");
		GregtechItemList.GT4_Multi_Crafter.set(new GT4Entity_AutoCrafter(811, "gtplusplus.autocrafter.multi", "Large Autocrafter").getStackForm(1L));
	}

	private static void tesseracts(){
		//Gregtech 4 Workbenches
		Utils.LOG_INFO("Gregtech 4 Content | Registering Tesseracts.");
		GregtechItemList.GT4_Tesseract_Generator.set(new GT_MetaTileEntity_TesseractGenerator(833, "tesseract.generator", "Tesseract Generator", 4).getStackForm(1L));
		GregtechItemList.GT4_Tesseract_Terminal.set(new GT_MetaTileEntity_TesseractTerminal(834, "tesseract.terminal", "Tesseract Terminal", 4).getStackForm(1L));
	}
	
	private static void shelves(){
		//Gregtech 4 Workbenches
		Utils.LOG_INFO("Gregtech 4 Content | Registering Shelves.");
		GregtechItemList.GT4_Shelf.set(new GT4Entity_Shelf(870, "gtplusplus.shelf.wooden", "Wood encased Shelf").getStackForm(1L));
		GregtechItemList.GT4_Shelf_Iron.set(new GT4Entity_Shelf_Iron(871, "gtplusplus.shelf.iron", "Metal encased Shelf").getStackForm(1L));
		GregtechItemList.GT4_Shelf_FileCabinet.set(new GT4Entity_Shelf_FileCabinet(872, "gtplusplus.shelf.filecabinet", "File Cabinet").getStackForm(1L));
	    GregtechItemList.GT4_Shelf_Desk.set(new GT4Entity_Shelf_Desk(873, "gtplusplus.shelf.desk", "Metal encased Desk").getStackForm(1L));
		GregtechItemList.GT4_Shelf_Compartment.set(new GT4Entity_Shelf_Compartment(874, "gtplusplus.shelf.compartment", "Compartment").getStackForm(1L));
		}
	
}
