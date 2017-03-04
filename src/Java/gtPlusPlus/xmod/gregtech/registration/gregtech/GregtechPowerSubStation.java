package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.GregtechMetaTileEntity_PowerSubStationController;

public class GregtechPowerSubStation
{



	public static void run()
	{
		if (gtPlusPlus.core.lib.LoadedMods.Gregtech){
			Utils.LOG_INFO("Gregtech5u Content | Registering Power Substation Node.");
			if (CORE.configSwitches.enableMultiblock_PowerSubstation) {
				run1();
			}
		}

	}

	private static void run1()
	{
		//Steam Condensors
		GregtechItemList.PowerSubStation.set(new GregtechMetaTileEntity_PowerSubStationController(812, "substation.01.input.single", "Power Substation Node").getStackForm(1L));

	}
}
