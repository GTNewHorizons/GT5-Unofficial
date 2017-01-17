package gtPlusPlus.core.item.init;

import gregtech.api.enums.Materials;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.material.ALLOY;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.ItemUtils;

public class ItemsMultiTools {

	public static void load(){
		run();
	}
	
	private static void run(){
		
		//Load Multitools
				boolean gtStyleTools = LoadedMods.Gregtech;
				if (CORE.configSwitches.enableMultiSizeTools){
					
					//GT Materials
					Materials[] rm = Materials.values();
					for (Materials m : rm){
						toolFactoryGT(m, gtStyleTools);
					}
					
					//GT++ Materials
					toolFactory(ALLOY.HASTELLOY_C276);
					toolFactory(ALLOY.HASTELLOY_N);
					toolFactory(ALLOY.HASTELLOY_W);
					toolFactory(ALLOY.HASTELLOY_X);
					toolFactory(ALLOY.INCOLOY_020);
					toolFactory(ALLOY.INCOLOY_DS);
					toolFactory(ALLOY.INCOLOY_MA956);
					toolFactory(ALLOY.INCONEL_625);
					toolFactory(ALLOY.INCONEL_690);
					toolFactory(ALLOY.INCONEL_792);
					toolFactory(ALLOY.LEAGRISIUM);
					toolFactory(ALLOY.TANTALLOY_60);
					toolFactory(ALLOY.TANTALLOY_61);
					toolFactory(ALLOY.STABALLOY);
					toolFactory(ALLOY.QUANTUM);
					toolFactory(ALLOY.BEDROCKIUM);
					toolFactory(ALLOY.POTIN);
					toolFactory(ALLOY.TUMBAGA);
					toolFactory(ALLOY.TALONITE);
					toolFactory(ALLOY.STELLITE);
					toolFactory(ALLOY.TUNGSTEN_CARBIDE);
					toolFactory(ALLOY.TANTALUM_CARBIDE);
					
					
				}
		
	}
	
	
	private static boolean toolFactoryGT(Materials m, boolean b){
		ModItems.MP_GTMATERIAL = ItemUtils.generateMultiPick(b, m);
		ModItems.MS_GTMATERIAL = ItemUtils.generateMultiShovel(b, m);
		return true;
	}
	
	private static boolean toolFactory(Material m){
		Utils.LOG_WARNING("Generating Multi-Tools for "+m.getLocalizedName());
		ModItems.MP_GTMATERIAL = ItemUtils.generateMultiPick(m);
		ModItems.MS_GTMATERIAL = ItemUtils.generateMultiShovel(m);
		return true;
	}
	
}
