package miscutil.core.handler.registration.gregtech;

import miscutil.core.util.Utils;
import miscutil.core.xmod.gregtech.api.enums.GregtechItemList;
import miscutil.core.xmod.gregtech.common.tileentities.machines.multi.GregtechMetaTileEntityIndustrialPlatePress;

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