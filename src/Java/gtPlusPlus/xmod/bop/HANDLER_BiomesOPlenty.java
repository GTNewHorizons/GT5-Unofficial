package gtPlusPlus.xmod.bop;

import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.xmod.bop.blocks.BOP_Block_Registrator;

public class HANDLER_BiomesOPlenty {

	
	
	public static void preInit(){
		//if (LoadedMods.BiomesOPlenty){
			BOP_Block_Registrator.run();
		//}

	}

	public static void init(){
		if (LoadedMods.BiomesOPlenty){

		}
	}

	public static void postInit(){
		if (LoadedMods.BiomesOPlenty){

		}

	}


}
