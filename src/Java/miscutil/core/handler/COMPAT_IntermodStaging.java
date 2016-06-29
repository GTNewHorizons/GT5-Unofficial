package miscutil.core.handler;

import miscutil.core.intermod.forestry.HANDLER_Forestry;
import miscutil.core.intermod.growthcraft.HANDLER_Growthcraft;
import miscutil.core.intermod.psychedelicraft.HANDLER_Psychedelicraft;
import miscutil.core.intermod.thermalfoundation.HANDLER_ThermalFoundation;
import miscutil.gregtech.HANDLER_Gregtech;

public class COMPAT_IntermodStaging {

	public static void preInit(){
		HANDLER_Gregtech.preInit();
		HANDLER_Growthcraft.preInit();	
		HANDLER_ThermalFoundation.preInit();
		HANDLER_Forestry.preInit();
		HANDLER_Psychedelicraft.preInit();
		
	}

	public static void init(){
		HANDLER_Gregtech.init();
		HANDLER_ThermalFoundation.init();
		HANDLER_Forestry.Init();
		HANDLER_Psychedelicraft.init();
	}

	public static void postInit(){
		HANDLER_Gregtech.postInit();
		HANDLER_ThermalFoundation.postInit();
		HANDLER_Forestry.postInit();
		HANDLER_Psychedelicraft.postInit();
	}


}
