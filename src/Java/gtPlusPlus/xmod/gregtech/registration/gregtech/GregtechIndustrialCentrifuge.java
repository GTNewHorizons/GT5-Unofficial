package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.GregtechMetaTileEntity_IndustrialCentrifuge;

public class GregtechIndustrialCentrifuge
{



	public static void run()
	{
		if (gtPlusPlus.core.lib.LoadedMods.Gregtech){
			Utils.LOG_INFO("Gregtech5u Content | Registering Industrial Centrifuge Multiblock.");
			if (CORE.configSwitches.enabledMultiblock_IndustrialCentrifuge) run1();
		}

	}

	private static void run1()
	{
		//Industrial Centrifuge Multiblock
		GregtechItemList.Industrial_Centrifuge.set(new GregtechMetaTileEntity_IndustrialCentrifuge(790, "industrialcentrifuge.controller.tier.single", "Industrial Centrifuge").getStackForm(1L));
		
	}
}
