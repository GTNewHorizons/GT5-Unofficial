package gtPlusPlus.xmod.rftools;

import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.xmod.rftools.blocks.RFT_Block_Registrator;

public class HANDLER_RfTools {

	public static void preInit(){
		if (LoadedMods.RFTools){
			//RFT_Block_Registrator.run();
		}
	}

	public static void init(){
		if (LoadedMods.RFTools){

		}
	}

	public static void postInit(){
		if (LoadedMods.RFTools){

		}
	}


}
