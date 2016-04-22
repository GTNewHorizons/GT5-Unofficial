package miscutil.gregtech.api.init.machines;

import miscutil.core.util.Utils;
import miscutil.gregtech.api.enums.GregtechItemList;
import miscutil.gregtech.api.metatileentity.implementations.GregtechMetaCondensor;

public class GregtechIndustrialCentrifuge
{



	public static void run()
	{
		if (miscutil.core.lib.LoadedMods.Gregtech){
			Utils.LOG_INFO("MiscUtils: Gregtech5u Content | Registering Industrial Centrifuge Multiblock.");
			run1();
		}

	}

	private static void run1()
	{
		//Industrial Centrifuge Multiblock
		GregtechItemList.Industrial_Centrifuge.set(new GregtechMetaCondensor(790, "industrialcentrifuge.controller.tier.single", "Steam Condensor").getStackForm(1L));
		
	}
}
