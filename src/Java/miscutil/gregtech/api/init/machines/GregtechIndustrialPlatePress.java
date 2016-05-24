package miscutil.gregtech.api.init.machines;

import miscutil.core.util.Utils;
import miscutil.gregtech.api.enums.GregtechItemList;
import miscutil.gregtech.common.machines.multi.GregtechMetaTileEntityIndustrialPlatePress;

public class GregtechIndustrialPlatePress
{



	public static void run()
	{
		if (miscutil.core.lib.LoadedMods.Gregtech){
			Utils.LOG_INFO("MiscUtils: Gregtech5u Content | Registering Industrial Press Multiblock.");
			run1();
		}

	}

	private static void run1()
	{
		//Industrial Centrifuge Multiblock
		GregtechItemList.Industrial_PlatePress.set(new GregtechMetaTileEntityIndustrialPlatePress(792, "industrialbender.controller.tier.single", "Industrial Material Press").getStackForm(1L));
		
	}
}