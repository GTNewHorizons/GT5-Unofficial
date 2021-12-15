package gtPlusPlus.core.handler;

import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.xmod.gregtech.HANDLER_GT;
import gtPlusPlus.xmod.ic2.CustomInternalName;

public class EnumHelperHandler {

	public static void init() {		

		if (LoadedMods.IndustrialCraft2) {
			CustomInternalName.init();
		}
		
		if (LoadedMods.Gregtech) {
			HANDLER_GT.addNewOrePrefixes();
		}
		
	}

}
