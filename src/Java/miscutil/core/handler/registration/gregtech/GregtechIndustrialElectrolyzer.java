package miscutil.core.handler.registration.gregtech;

import miscutil.core.util.Utils;
import miscutil.core.xmod.gregtech.api.enums.GregtechItemList;
import miscutil.core.xmod.gregtech.common.tileentities.machines.multi.GregtechMetaTileEntityIndustrialElectrolyzer;

public class GregtechIndustrialElectrolyzer
{



	public static void run()
	{
		if (miscutil.core.lib.LoadedMods.Gregtech){
			Utils.LOG_INFO("Gregtech5u Content | Registering Industrial Electrolyzer Multiblock.");
			run1();
		}

	}

	private static void run1()
	{
		//Industrial Electrolyzer Multiblock
		GregtechItemList.Industrial_Electrolyzer.set(new GregtechMetaTileEntityIndustrialElectrolyzer(796, "industrialelectrolyzer.controller.tier.single", "Industrial Electrolyzer").getStackForm(1L));
		
	}
}