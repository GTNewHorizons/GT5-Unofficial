package gtPlusPlus.core.util.minecraft;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;

public class ClientUtils {

	static {
		if (FMLCommonHandler.instance().getSide() == Side.SERVER) {
			Logger.ERROR("Something tried to access the ClientUtils class from the Server Side.");
			Logger.ERROR("Soft crashing to prevent data corruption.");	
			CORE.crash();
		}
	}
	
}
