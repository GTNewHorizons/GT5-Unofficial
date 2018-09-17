package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.GregtechMetaTileEntity_SolarTower;
import gtPlusPlus.xmod.gregtech.common.tileentities.misc.TileEntitySolarHeater;

public class GregtechSolarTower {

	public static void run() {
		if (gtPlusPlus.core.lib.LoadedMods.Gregtech) {
			Logger.INFO("Gregtech5u Content | Registering Solar Tower.");
				run1();			
		}

	}

	private static void run1() {
		// Solar Tower
		GregtechItemList.Industrial_Solar_Tower.set(new GregtechMetaTileEntity_SolarTower(863, "solartower.controller.tier.single", "Solar Tower").getStackForm(1L));
		GregtechItemList.Solar_Tower_Reflector.set(
				new TileEntitySolarHeater(
						864, 
						"solarreflector.simple.single",
						"Solar Reflector",
						8,
						"Part of the Clean Green energy movement",
						0).getStackForm(1L));
		
	}
}