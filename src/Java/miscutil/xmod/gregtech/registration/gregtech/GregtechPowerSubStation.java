package miscutil.xmod.gregtech.registration.gregtech;

import miscutil.core.util.Utils;
import miscutil.xmod.gregtech.api.enums.GregtechItemList;
import miscutil.xmod.gregtech.common.tileentities.machines.multi.GregtechMetaTileEntityPowerSubStationController;

public class GregtechPowerSubStation
{



	public static void run()
	{
		if (miscutil.core.lib.LoadedMods.Gregtech){
			Utils.LOG_INFO("Gregtech5u Content | Registering Power Substation Node.");
			run1();
		}

	}

	private static void run1()
	{
		//Steam Condensors
		GregtechItemList.PowerSubStation.set(new GregtechMetaTileEntityPowerSubStationController(812, "substation.01.input.single", "Power Substation Node").getStackForm(1L));
		
	}
}
