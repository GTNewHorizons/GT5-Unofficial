package miscutil.xmod.gregtech.registration.gregtech;

import miscutil.core.util.Utils;
import miscutil.xmod.gregtech.api.enums.GregtechItemList;
import miscutil.xmod.gregtech.common.tileentities.machines.multi.GregtechMetaTileEntityIndustrialWireMill;

public class GregtechIndustrialWiremill
{



	public static void run()
	{
		if (miscutil.core.lib.LoadedMods.Gregtech){
			Utils.LOG_INFO("Gregtech5u Content | Registering Industrial Wire Factory Multiblock.");
			run1();
		}

	}

	private static void run1()
	{
		//Industrial Electrolyzer Multiblock
		GregtechItemList.Industrial_WireFactory.set(new GregtechMetaTileEntityIndustrialWireMill(798, "industrialwiremill.controller.tier.single", "Wire Factory Controller").getStackForm(1L));
		
	}
}