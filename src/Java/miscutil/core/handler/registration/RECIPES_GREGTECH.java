package miscutil.core.handler.registration;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;
import miscutil.core.lib.CORE;
import miscutil.core.util.Utils;

public class RECIPES_GREGTECH {

	public static void run(){
		Utils.LOG_INFO("Loading Recipes through GregAPI for Industrial Multiblocks.");
		execute();
	}
	
	private static void execute(){
		Utils.LOG_INFO("Loading Recipes for Industrial Coking Oven.");
		
		CORE.RA.addCokeOvenRecipe(
				GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Emerald, 1L), //Input 1
				GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Emerald, 1L), //Input 2
				Materials.Water.getFluid(100L), //Fluid Input 1
				Materials.Lava.getFluid(500L), //Fluid Output
				GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Diamond, 1L), //Item Output 
				20,  //Time in seconds?
				120); //EU
	}
	
}
