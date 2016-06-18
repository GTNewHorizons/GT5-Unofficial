package miscutil.core.common.compat;

import miscutil.core.util.item.UtilsItems;

public class COMPAT_MorePlanets {

	
	public static void OreDict(){
		run();
	}
	
	private final static void run(){
		//Metals
		UtilsItems.getItemForOreDict("MorePlanet:kapteyn-b_item", "ingotFrozenIron", "Frozen Iron Ingot", 0);
		UtilsItems.getItemForOreDict("MorePlanet:kapteyn-b_item", "ingotAnyIron", "Frozen Iron Ingot", 0);
		UtilsItems.getItemForOreDict("MorePlanet:polongnius_item", "ingotPalladium", "Palladium Ingot", 5);
		UtilsItems.getItemForOreDict("MorePlanet:fronos_item", "ingotIridium", "Iridium Ingot", 3);
		UtilsItems.getItemForOreDict("MorePlanet:nibiru_item", "ingotNorium", "Norium Ingot", 1);
		UtilsItems.getItemForOreDict("MorePlanet:venus_item", "ingotLead", "Lead Ingot", 0);
		UtilsItems.getItemForOreDict("MorePlanet:diona_item", "ingotQuontonium", "Quontonium Ingot", 0);
		UtilsItems.getItemForOreDict("MorePlanet:diona_item", "ingotFronisium", "Fronisium Ingot", 1);
		UtilsItems.getItemForOreDict("MorePlanet:sirius-b_item", "ingotSulfur", "Sulfur Ingot", 3);
		UtilsItems.getItemForOreDict("MorePlanet:koentus_item", "ingotKoentusMeteoricIron", "Koentus Meteoric Iron Ingot", 4);
		UtilsItems.getItemForOreDict("MorePlanet:mercury_item", "ingotMetallic", "Metallic Ingot", 2);
		UtilsItems.getItemForOreDict("MorePlanet:polongnius_item", "ingotPolongiusMeteoricIron", "Polongius Meteoric Iron Ingot", 4);
		UtilsItems.getItemForOreDict("MorePlanet:mercury_item", "ingotMeteoricSteel", "Meteoric Steel Ingot", 3);
		UtilsItems.getItemForOreDict("MorePlanet:sirius-b_item", "dustSulfur", "Sulfur Dust", 2);
		
		//Gems
		UtilsItems.getItemForOreDict("MorePlanet:fronos_item", "gemBlackDiamond", "Black Diamond Gem", 2);
		UtilsItems.getItemForOreDict("MorePlanet:koentus_item", "gemWhiteCrystal", "White Crystal", 0);
		UtilsItems.getItemForOreDict("MorePlanet:nibiru_item", "gemRedCrystal", "Red Crystal", 0);
		UtilsItems.getItemForOreDict("MorePlanet:pluto_item", "gemXeonius", "Xeonius Gem", 0);
		UtilsItems.getItemForOreDict("MorePlanet:kapteyn-b_item", "gemUranium", "Uranium Gem", 1);
	}
	
}
