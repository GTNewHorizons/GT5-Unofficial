package miscutil.core.intermod.psychedelicraft;

import miscutil.core.intermod.psychedelicraft.fluids.PS_Fluids;
import miscutil.core.lib.LoadedMods;


public class HANDLER_Psychedelicraft {

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
