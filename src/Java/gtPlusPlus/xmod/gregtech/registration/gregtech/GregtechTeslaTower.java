package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.misc.GregtechMTE_TeslaTower;

public class GregtechTeslaTower {

	public static void run() {
		Logger.INFO("Gregtech5u Content | Registering Tesla Tower.");
		GregtechItemList.TelsaTower.set(new GregtechMTE_TeslaTower(984, "multimachine.telsatower", "Tesla's Last Testament").getStackForm(1));
	    
	}

}
