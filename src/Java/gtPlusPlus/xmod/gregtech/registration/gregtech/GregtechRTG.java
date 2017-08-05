package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.generators.GregtechMetaTileEntity_RTG;

public class GregtechRTG {

	public static void run(){
		if (gtPlusPlus.core.lib.LoadedMods.Gregtech){
			Utils.LOG_INFO("Gregtech5u Content | Registering RTG.");
				run1();
		}
	}

	private static void run1(){
		GregtechItemList.RTG.set(new GregtechMetaTileEntity_RTG(800, "rtg.01.tier.single", "Radioisotope Thermoelectric Generator", 0).getStackForm(1L));
	}
	
}
