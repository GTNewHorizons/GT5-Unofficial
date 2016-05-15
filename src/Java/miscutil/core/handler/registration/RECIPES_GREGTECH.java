package miscutil.core.handler.registration;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;
import miscutil.core.lib.CORE;
import miscutil.core.util.Utils;
import miscutil.core.util.UtilsItems;

public class RECIPES_GREGTECH {

	public static void run(){
		Utils.LOG_INFO("Loading Recipes through GregAPI for Industrial Multiblocks.");
		execute();
	}

	private static void execute(){
		Utils.LOG_INFO("Loading Recipes for Industrial Coking Oven.");

		try {

			//GT Logs to Charcoal Recipe	
			CORE.RA.addCokeOvenRecipe(
					GT_OreDictUnificator.get(OrePrefixes.log, Materials.Wood, 2L), //Input 1
					GT_OreDictUnificator.get(OrePrefixes.log, Materials.Wood, 2L), //Input 2
					Materials.SulfuricAcid.getFluid(20L), //Fluid Input
					Materials.Creosote.getFluid(175L), //Fluid Output
					GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Charcoal, 2L), //Item Output 
					800,  //Time in ticks
					30); //EU

		}catch (NullPointerException e){Utils.LOG_INFO("FAILED TO LOAD RECIPES - NULL POINTER SOMEWHERE");}

		try {

			//Coal -> Coke Recipe		
			CORE.RA.addCokeOvenRecipe(
					GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Coal, 2L), //Input 1
					GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Coal, 2L), //Input 2
					Materials.SulfuricAcid.getFluid(80L), //Fluid Input
					Materials.Creosote.getFluid(250L), //Fluid Output
					UtilsItems.getItemStack("Railcraft:fuel.coke", 2), //Item Output 
					600,  //Time in ticks
					120); //EU

		}catch (NullPointerException e){Utils.LOG_INFO("FAILED TO LOAD RECIPES - NULL POINTER SOMEWHERE");}
	}
}