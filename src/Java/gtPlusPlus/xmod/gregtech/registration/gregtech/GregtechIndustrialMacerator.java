package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.GregtechMetaTileEntity_IndustrialMacerator;

public class GregtechIndustrialMacerator
{



	public static void run()
	{
		if (gtPlusPlus.core.lib.LoadedMods.Gregtech){
			Utils.LOG_INFO("Gregtech5u Content | Registering Industrial Maceration Stack Multiblock.");
			run1();
		}

	}

	private static void run1()
	{
		//Industrial Electrolyzer Multiblock
		GregtechItemList.Industrial_MacerationStack.set(new GregtechMetaTileEntity_IndustrialMacerator(797, "industrialmacerator.controller.tier.single", "Maceration Stack Controller").getStackForm(1L));
		
	}
}