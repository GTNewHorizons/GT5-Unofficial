package miscutil.core.handler.registration.gregtech;

import miscutil.core.util.Utils;
import miscutil.core.xmod.gregtech.api.enums.GregtechItemList;
import miscutil.core.xmod.gregtech.common.tileentities.machines.multi.GregtechMetaTileEntityIndustrialSinter;

public class GregtechIndustrialSinter{

	public static void run()
	{
		if (miscutil.core.lib.LoadedMods.Gregtech){
			Utils.LOG_INFO("Gregtech5u Content | Registering Industrial Sinter Furnace Multiblock.");
			run1();
		}

	}

	private static void run1()
	{
		//Industrial Electrolyzer Multiblock
		GregtechItemList.Industrial_SinterFurnace.set(new GregtechMetaTileEntityIndustrialSinter(800, "industrialsinterfurnace.controller.tier.single", "Sinter Furnace").getStackForm(1L));
		
	}
}