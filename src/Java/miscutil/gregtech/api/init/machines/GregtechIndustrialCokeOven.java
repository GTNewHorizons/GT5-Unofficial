package miscutil.gregtech.api.init.machines;

import miscutil.core.util.Utils;
import miscutil.gregtech.api.enums.GregtechItemList;
import miscutil.gregtech.common.machines.multi.GregtechMetaTileEntityIndustrialCokeOven;

public class GregtechIndustrialCokeOven
{



	public static void run()
	{
		if (miscutil.core.lib.LoadedMods.Gregtech){
			Utils.LOG_INFO("MiscUtils: Gregtech5u Content | Registering Industrial Coke Oven Multiblock.");
			run1();
		}

	}

	private static void run1()
	{
		//Industrial Centrifuge Multiblock
		GregtechItemList.Industrial_CokeOven.set(new GregtechMetaTileEntityIndustrialCokeOven(791, "industrialcokeoven.controller.tier.single", "Industrial Coke Oven").getStackForm(1L));
		
	}
}