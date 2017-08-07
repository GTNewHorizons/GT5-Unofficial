package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.GregtechMetaTileEntity_Cyclotron;

public class GregtechCyclotron {

	public static void run(){
		if (gtPlusPlus.core.lib.LoadedMods.Gregtech){
			Utils.LOG_INFO("Gregtech5u Content | Registering COMET Cyclotron.");
			run1();
		}
	}

	private static void run1(){
		GregtechItemList.COMET_Cyclotron.set(new GregtechMetaTileEntity_Cyclotron(801, "cyclotron.tier.single", "COMET - Compact Cyclotron", 6).getStackForm(1L));
	}

}
