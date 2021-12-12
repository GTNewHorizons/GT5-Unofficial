package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.GregtechMetaTileEntity_IndustrialArcFurnace;

public class GregtechIndustrialArcFurnace {

	public static void run() {
		if (gtPlusPlus.core.lib.LoadedMods.Gregtech) {
			Logger.INFO("Gregtech5u Content | Registering Industrial Arc Furnace Multiblock.");
				run1();			
		}

	}

	private static void run1() {
		// Industrial Arc Furnace Multiblock
		GregtechItemList.Industrial_Arc_Furnace.set(new GregtechMetaTileEntity_IndustrialArcFurnace(862, "industrialarcfurnace.controller.tier.single", "High Current Industrial Arc Furnace").getStackForm(1L));

	}
}