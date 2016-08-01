package miscutil.core.handler.registration.gregtech;

import miscutil.core.util.Utils;
import miscutil.core.xmod.gregtech.api.enums.GregtechItemList;
import miscutil.core.xmod.gregtech.common.tileentities.machines.multi.GregtechMetaTileEntityMassFabricator;

public class GregtechIndustrialMassFabricator
{



	public static void run()
	{
		if (miscutil.core.lib.LoadedMods.Gregtech){
			Utils.LOG_INFO("Gregtech5u Content | Registering Industrial Mass Fabricator Multiblock.");
			run1();
		}

	}

	private static void run1()
	{
		//Industrial Electrolyzer Multiblock
		GregtechItemList.Industrial_MassFab.set(new GregtechMetaTileEntityMassFabricator(799, "industrialmassfab.controller.tier.single", "Matter Fabrication CPU").getStackForm(1L));
		
	}
}