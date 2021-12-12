package gtPlusPlus.core.common.compat;

import gtPlusPlus.core.util.minecraft.ItemUtils;

public class COMPAT_MorePlanets {


	public static void OreDict(){
		run();
	}

	private final static void run(){
		//Metals
		ItemUtils.getItemForOreDict("MorePlanet:kapteyn-b_item", "ingotFrozenIron", "Frozen Iron Ingot", 0);
		ItemUtils.getItemForOreDict("MorePlanet:kapteyn-b_item", "ingotAnyIron", "Frozen Iron Ingot", 0);
		ItemUtils.getItemForOreDict("MorePlanet:polongnius_item", "ingotPalladium", "Palladium Ingot", 5);
		ItemUtils.getItemForOreDict("MorePlanet:fronos_item", "ingotIridium", "Iridium Ingot", 3);
		ItemUtils.getItemForOreDict("MorePlanet:nibiru_item", "ingotNorium", "Norium Ingot", 1);
		ItemUtils.getItemForOreDict("MorePlanet:venus_item", "ingotLead", "Lead Ingot", 0);
		ItemUtils.getItemForOreDict("MorePlanet:diona_item", "ingotQuontonium", "Quontonium Ingot", 0);
		ItemUtils.getItemForOreDict("MorePlanet:diona_item", "ingotFronisium", "Fronisium Ingot", 1);
		ItemUtils.getItemForOreDict("MorePlanet:sirius-b_item", "ingotSulfur", "Sulfur Ingot", 3);
		ItemUtils.getItemForOreDict("MorePlanet:koentus_item", "ingotKoentusMeteoricIron", "Koentus Meteoric Iron Ingot", 4);
		ItemUtils.getItemForOreDict("MorePlanet:mercury_item", "ingotMetallic", "Metallic Ingot", 2);
		ItemUtils.getItemForOreDict("MorePlanet:polongnius_item", "ingotPolongiusMeteoricIron", "Polongius Meteoric Iron Ingot", 4);
		ItemUtils.getItemForOreDict("MorePlanet:mercury_item", "ingotMeteoricSteel", "Meteoric Steel Ingot", 3);
		ItemUtils.getItemForOreDict("MorePlanet:sirius-b_item", "dustSulfur", "Sulfur Dust", 2);

		//Gems
		ItemUtils.getItemForOreDict("MorePlanet:fronos_item", "gemBlackDiamond", "Black Diamond Gem", 2);
		ItemUtils.getItemForOreDict("MorePlanet:koentus_item", "gemWhiteCrystal", "White Crystal", 0);
		ItemUtils.getItemForOreDict("MorePlanet:nibiru_item", "gemRedCrystal", "Red Crystal", 0);
		ItemUtils.getItemForOreDict("MorePlanet:pluto_item", "gemXeonius", "Xeonius Gem", 0);
		ItemUtils.getItemForOreDict("MorePlanet:kapteyn-b_item", "gemUranium", "Uranium Gem", 1);
	}

}
