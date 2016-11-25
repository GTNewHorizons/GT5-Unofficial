package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.GregtechMetaTileEntity_MassFabricator;

public class GregtechIndustrialMassFabricator
{



	public static void run()
	{
		if (gtPlusPlus.core.lib.LoadedMods.Gregtech){
			Utils.LOG_INFO("Gregtech5u Content | Registering Industrial Matter Fabricator Multiblock.");
			if (CORE.configSwitches.enabledMultiblock_MatterFabricator) run1();
		}

	}

	private static void run1()
	{
		//Industrial Matter Fabricator Multiblock
		GregtechItemList.Industrial_MassFab.set(new GregtechMetaTileEntity_MassFabricator(799, "industrialmassfab.controller.tier.single", "Matter Fabrication CPU").getStackForm(1L));
		
	}
}