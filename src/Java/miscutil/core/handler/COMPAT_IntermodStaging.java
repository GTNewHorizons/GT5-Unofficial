package miscutil.core.handler;

import miscutil.core.xmod.forestry.HANDLER_FR;
import miscutil.core.xmod.gregtech.HANDLER_GT;
import miscutil.core.xmod.growthcraft.HANDLER_GC;
import miscutil.core.xmod.ic2.HANDLER_IC2;
import miscutil.core.xmod.psychedelicraft.HANDLER_Psych;
import miscutil.core.xmod.thermalfoundation.HANDLER_TF;

public class COMPAT_IntermodStaging {

	public static void preInit(){
		HANDLER_GT.preInit();
		HANDLER_GC.preInit();	
		HANDLER_TF.preInit();
		HANDLER_FR.preInit();
		HANDLER_Psych.preInit();
		HANDLER_IC2.preInit();
		
	}

	public static void init(){
		HANDLER_GT.init();
		HANDLER_GC.init();
		HANDLER_TF.init();
		HANDLER_FR.Init();
		HANDLER_Psych.init();
		HANDLER_IC2.init();
	}

	public static void postInit(){
		HANDLER_GT.postInit();
		HANDLER_GC.postInit();
		HANDLER_TF.postInit();
		HANDLER_FR.postInit();
		HANDLER_Psych.postInit();
		HANDLER_IC2.postInit();
	}


}
