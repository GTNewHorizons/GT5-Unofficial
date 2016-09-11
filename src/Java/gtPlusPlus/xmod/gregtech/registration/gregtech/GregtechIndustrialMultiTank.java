package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.GregtechMetaTileEntityMultiTank;

public class GregtechIndustrialMultiTank
{



	public static void run()
	{
		if (gtPlusPlus.core.lib.LoadedMods.Gregtech){
			Utils.LOG_INFO("Gregtech5u Content | Registering Industrial Multitank Multiblock.");
			run1();
		}

	}

	private static void run1()
	{
		GregtechItemList.Industrial_MultiTank.set(new GregtechMetaTileEntityMultiTank(827, "multitank.controller.tier.single", "Gregtech Multitank").getStackForm(1L));
		
	}
}
