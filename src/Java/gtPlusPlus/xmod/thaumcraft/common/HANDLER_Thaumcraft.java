package gtPlusPlus.xmod.thaumcraft.common;

import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.xmod.thaumcraft.common.block.TC_BlockHandler;

public class HANDLER_Thaumcraft {

	public static void preInit(){
		if (LoadedMods.Thaumcraft){
			TC_BlockHandler.run();
		}
	}

	public static void init(){
		if (LoadedMods.Thaumcraft){

		}
	}

	public static void postInit(){
		if (LoadedMods.Thaumcraft){
			
		}
	}
	

}
