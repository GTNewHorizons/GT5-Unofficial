package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.GregtechMetaTileEntityIndustrialSinter;

public class GregtechIndustrialSinter{

	public static void run()
	{
		if (gtPlusPlus.core.lib.LoadedMods.Gregtech){
			Utils.LOG_INFO("Gregtech5u Content | Registering Industrial Sinter Furnace Multiblock.");
			run1();
		}

	}

	private static void run1()
	{
		//Industrial Electrolyzer Multiblock
		GregtechItemList.Industrial_SinterFurnace.set(new GregtechMetaTileEntityIndustrialSinter(810, "industrialsinterfurnace.controller.tier.single", "Sinter Furnace").getStackForm(1L));
		
	}
}