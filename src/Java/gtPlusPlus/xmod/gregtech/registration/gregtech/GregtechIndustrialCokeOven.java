package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.GregtechMetaTileEntityIndustrialCokeOven;

public class GregtechIndustrialCokeOven
{



	public static void run()
	{
		if (gtPlusPlus.core.lib.LoadedMods.Gregtech){
			Utils.LOG_INFO("Gregtech5u Content | Registering Industrial Coke Oven Multiblock.");
			run1();
		}

	}

	private static void run1()
	{
		//Industrial Centrifuge Multiblock
		GregtechItemList.Industrial_CokeOven.set(new GregtechMetaTileEntityIndustrialCokeOven(791, "industrialcokeoven.controller.tier.single", "Industrial Coke Oven").getStackForm(1L));
		
	}
}