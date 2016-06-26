package miscutil.core.handler;

import miscutil.core.intermod.growthcraft.HANDLER_Growthcraft;
import miscutil.core.intermod.thermalfoundation.HANDLER_ThermalFoundation;
import miscutil.gregtech.HANDLER_Gregtech;

public class COMPAT_IntermodStaging {

	public static void preInit(){
		HANDLER_Growthcraft.preInit();	
		HANDLER_ThermalFoundation.preInit();
		HANDLER_Gregtech.preInit();
		
	}

	public static void init(){
		HANDLER_ThermalFoundation.Init();
		HANDLER_Gregtech.init();
	}

	public static void postInit(){
		HANDLER_ThermalFoundation.postInit();
		HANDLER_Gregtech.postInit();
	}


}
