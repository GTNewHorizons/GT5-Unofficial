package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.GregtechMetaTileEntity_AlloyBlastSmelter;

public class GregtechIndustrialBlastSmelter{

	public static void run()
	{
		if (gtPlusPlus.core.lib.LoadedMods.Gregtech){
			Utils.LOG_INFO("Gregtech5u Content | Registering Industrial Blast Smelter Multiblock.");
			run1();
		}

	}

	private static void run1()
	{
		//Industrial Electrolyzer Multiblock
		GregtechItemList.Industrial_AlloyBlastSmelter.set(new GregtechMetaTileEntity_AlloyBlastSmelter(810, "industrialsalloyamelter.controller.tier.single", "Blast Smelter").getStackForm(1L));
		
	}
}