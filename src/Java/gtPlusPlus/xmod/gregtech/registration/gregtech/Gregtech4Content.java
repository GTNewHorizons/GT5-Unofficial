package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.automation.GT_MetaTileEntity_TesseractGenerator;
import gtPlusPlus.xmod.gregtech.common.tileentities.automation.GT_MetaTileEntity_TesseractTerminal;

public class Gregtech4Content
{

	//ID Range 828, 829, 833 - 850

	public static void run()
	{
		if (LoadedMods.Gregtech){
			workbenches();
			tesseracts();
		}
	}

	private static void workbenches(){
		//Gregtech 4 Workbenches
		Utils.LOG_INFO("Gregtech 4 Content | Registering Workbenches.");
		//Free	//GregtechItemList.GT4_Workbench_Bronze.set(new GT_MetaTileEntity_BronzeCraftingTable(828, "workbench.bronze", "Bronze Workbench", 0).getStackForm(1L));
		//Free	//GregtechItemList.GT4_Workbench_Advanced.set(new GT_MetaTileEntity_AdvancedCraftingTable(829, "workbench.advanced", "Advanced Workbench", 1).getStackForm(1L));
	}

	private static void tesseracts(){
		//Gregtech 4 Workbenches
		Utils.LOG_INFO("Gregtech 4 Content | Registering Tesseracts.");
		GregtechItemList.GT4_Tesseract_Generator.set(new GT_MetaTileEntity_TesseractGenerator(833, "tesseract.generator", "Tesseract Generator", 4).getStackForm(1L));
		GregtechItemList.GT4_Tesseract_Terminal.set(new GT_MetaTileEntity_TesseractTerminal(834, "tesseract.terminal", "Tesseract Terminal", 4).getStackForm(1L));
	}
}
