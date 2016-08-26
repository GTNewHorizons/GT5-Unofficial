package miscutil.xmod.psychedelicraft;

import miscutil.core.lib.LoadedMods;
import miscutil.xmod.psychedelicraft.fluids.PS_Fluids;


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
