package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.GregtechMetaTileEntity_AlloyBlastSmelter;

public class GregtechIndustrialBlastSmelter{

	public static void run()
	{
		if (gtPlusPlus.core.lib.LoadedMods.Gregtech){
			Utils.LOG_INFO("Gregtech5u Content | Registering Industrial Alloy Blast Smelter Multiblock.");
			if (CORE.configSwitches.enableMultiblock_AlloyBlastSmelter) run1();
		}

	}

	private static void run1()
	{
		//Industrial Alloy Blast Smelter Multiblock
		GregtechItemList.Industrial_AlloyBlastSmelter.set(new GregtechMetaTileEntity_AlloyBlastSmelter(810, "industrialsalloyamelter.controller.tier.single", "Alloy Blast Smelter").getStackForm(1L));
		
	}
}