package gtPlusPlus.xmod.psychedelicraft;

import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.xmod.psychedelicraft.fluids.PS_Fluids;


public class HANDLER_Psych {

	public static void preInit(){
		if (LoadedMods.Psychedelicraft){
			PS_Fluids.registerFluids();
		}	
	}

	public static void init(){
		if (LoadedMods.Psychedelicraft){
			PS_Fluids.registerAlcohols();
		}
	}

	public static void postInit(){
		if (LoadedMods.Psychedelicraft){

		}		
	}

}
