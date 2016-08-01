package miscutil.core.handler.registration.gregtech;

import miscutil.core.util.Utils;
import miscutil.core.xmod.gregtech.api.enums.GregtechItemList;
import miscutil.core.xmod.gregtech.common.tileentities.machines.multi.GregtechMetaTileEntityIronBlastFurnace;

public class GregtechIronBlastFurnace
{



	public static void run()
	{
		if (miscutil.core.lib.LoadedMods.Gregtech){
			Utils.LOG_INFO("Gregtech5u Content | Registering Iron Blast Furnace.");
			run1();
		}

	}

	private static void run1()
	{
		GregtechItemList.Machine_Iron_BlastFurnace.set(new GregtechMetaTileEntityIronBlastFurnace(768, "ironmachine.blastfurnace", "Iron Plated Blast Furnace").getStackForm(1L));
	}
}
