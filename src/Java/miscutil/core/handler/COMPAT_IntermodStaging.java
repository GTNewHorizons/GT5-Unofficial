package miscutil.core.handler;

import miscutil.core.xmod.forestry.HANDLER_Forestry;
import miscutil.core.xmod.gregtech.HANDLER_Gregtech;
import miscutil.core.xmod.growthcraft.HANDLER_Growthcraft;
import miscutil.core.xmod.psychedelicraft.HANDLER_Psychedelicraft;
import miscutil.core.xmod.thermalfoundation.HANDLER_ThermalFoundation;

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
