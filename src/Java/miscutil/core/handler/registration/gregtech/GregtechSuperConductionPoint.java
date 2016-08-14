package miscutil.core.handler.registration.gregtech;

import miscutil.core.util.Utils;
import miscutil.core.xmod.gregtech.api.enums.GregtechItemList;
import miscutil.core.xmod.gregtech.common.tileentities.generators.GregtechMetaTileEntitySuperCondensor;

public class GregtechSuperConductionPoint
{



	public static void run()
	{
		if (miscutil.core.lib.LoadedMods.Gregtech){
			Utils.LOG_INFO("Gregtech5u Content | Registering Super Conductor Input Node.");
			run1();
		}

	}

	private static void run1()
	{
		//Steam Condensors
		GregtechItemList.SuperConductorInputNode.set(new GregtechMetaTileEntitySuperCondensor(801, "superconductor.01.input.single", "Power Phase Shifting Station", 8).getStackForm(1L));
		
	}
}
