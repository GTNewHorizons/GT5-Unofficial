package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.storage.GT_MetaTileEntity_AdvancedCraftingTable;
import gtPlusPlus.xmod.gregtech.common.tileentities.storage.GT_MetaTileEntity_BronzeCraftingTable;

public class GregtechWorkbenches
{
	public static void run()
	{
		if (LoadedMods.Gregtech){
			Utils.LOG_INFO("Gregtech5u Content | Registering Workbenches.");
			run1();
			//run2();
		}
		
	}

	private static void run1()
	{
        //Gregtech 4 Workbenches
		GregtechItemList.GT4_Workbench_Bronze.set(new GT_MetaTileEntity_BronzeCraftingTable(828, "workbench.bronze", "Bronze Workbench", 0).getStackForm(1L));
		GregtechItemList.GT4_Workbench_Advanced.set(new GT_MetaTileEntity_AdvancedCraftingTable(829, "workbench.advanced", "Advanced Workbench", 1).getStackForm(1L));
        
        
	}
}
