package gtPlusPlus.australia.gen.gt;

import java.util.Hashtable;

import gregtech.api.enums.Materials;

import gtPlusPlus.core.material.ELEMENT;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.minecraft.MaterialUtils;
import gtPlusPlus.everglades.object.BoxedQuad;

public class WorldGen_Australia_Ores {
	
	/**
	 * Set Ore Types on by one.
	 */

	private static final Material PYRITE = MaterialUtils.generateMaterialFromGtENUM(Materials.Pyrite);
	private static final Material PYROPE = MaterialUtils.generateMaterialFromGtENUM(Materials.Pyrope);
	private static final Material ALMANDINE = MaterialUtils.generateMaterialFromGtENUM(Materials.Almandine);
	private static final Material RUBY = MaterialUtils.generateMaterialFromGtENUM(Materials.Ruby);
	private static final Material CHALCOPYRITE = MaterialUtils.generateMaterialFromGtENUM(Materials.Chalcopyrite);
	private static final Material TOPAZ = MaterialUtils.generateMaterialFromGtENUM(Materials.Topaz);
	private static final Material SAPPHIRE_GREEN = MaterialUtils.generateMaterialFromGtENUM(Materials.GreenSapphire);
	private static final Material SAPPHIRE_BLUE = MaterialUtils.generateMaterialFromGtENUM(Materials.Sapphire);
	
	
	private static final Material EMERALD = MaterialUtils.generateMaterialFromGtENUM(Materials.Emerald);
	private static final Material DIAMOND = MaterialUtils.generateMaterialFromGtENUM(Materials.Diamond);
	private static final Material BANDED_IRON = MaterialUtils.generateMaterialFromGtENUM(Materials.BandedIron);
	private static final Material LIM_YELLOW = MaterialUtils.generateMaterialFromGtENUM(Materials.YellowLimonite);
	private static final Material LIM_BROWN = MaterialUtils.generateMaterialFromGtENUM(Materials.BrownLimonite);
	private static final Material TETRAHEDRITE = MaterialUtils.generateMaterialFromGtENUM(Materials.Tetrahedrite);
	private static final Material COAL = MaterialUtils.generateMaterialFromGtENUM(Materials.Coal);
	private static final Material SHIT_COAL = MaterialUtils.generateMaterialFromGtENUM(Materials.Lignite);
	private static final Material GRAPHITE = MaterialUtils.generateMaterialFromGtENUM(Materials.Graphite);
	
	
	
	public static WorldGen_GT_Australia_Ore_Layer BaseVein = new WorldGen_GT_Australia_Ore_Layer(
			"veinA",
			0, 128,
			5,
			8,
			32,
			ELEMENT.getInstance().IRON,
			BANDED_IRON,
			LIM_YELLOW,
			TETRAHEDRITE);
	
	
	
	/**
	 * Custom ore Veins
	 */
	
	public static WorldGen_GT_Australia_Ore_Layer Vein1 = new WorldGen_GT_Australia_Ore_Layer(
			"vein1",
			0, 10,
			1,
			8,
			16,
			EMERALD,
			SHIT_COAL,
			TOPAZ,
			DIAMOND);
	
	public static WorldGen_GT_Australia_Ore_Layer Vein2 = new WorldGen_GT_Australia_Ore_Layer(
			"vein2",
			0, 128,
			20,
			16,
			64,
			COAL,
			COAL,
			SHIT_COAL,
			SHIT_COAL);

	public static WorldGen_GT_Australia_Ore_Layer Vein3 = new WorldGen_GT_Australia_Ore_Layer(
			"vein3",
			0, 128,
			5,
			8,
			32,
			ELEMENT.getInstance().IRON,
			ELEMENT.getInstance().TIN,
			ELEMENT.getInstance().GOLD,
			ELEMENT.getInstance().LEAD);

	public static WorldGen_GT_Australia_Ore_Layer Vein4 = new WorldGen_GT_Australia_Ore_Layer(
			"vein4",
			0, 128,
			5,
			8,
			32,
			ELEMENT.getInstance().GOLD,
			ELEMENT.getInstance().COPPER,
			ELEMENT.getInstance().COBALT,
			ALMANDINE);
	
	
	
	public static WorldGen_GT_Australia_Ore_Layer Vein5 = new WorldGen_GT_Australia_Ore_Layer(
			"vein5",
			0, 128,
			15,
			8,
			16,
			PYRITE,
			PYROPE,
			LIM_YELLOW,
			CHALCOPYRITE);
	
	public static WorldGen_GT_Australia_Ore_Layer Vein6 = new WorldGen_GT_Australia_Ore_Layer(
			"vein6",
			0, 128,
			5,
			8,
			32,
			LIM_BROWN,
			SAPPHIRE_GREEN,
			ELEMENT.getInstance().IRON,
			ELEMENT.getInstance().COPPER);
	
	public static WorldGen_GT_Australia_Ore_Layer Vein7 = new WorldGen_GT_Australia_Ore_Layer(
			"vein7",
			0, 128,
			5,
			8,
			32,
			GRAPHITE,
			RUBY,
			LIM_YELLOW,
			ELEMENT.getInstance().NICKEL);
	
	public static WorldGen_GT_Australia_Ore_Layer Vein8 = new WorldGen_GT_Australia_Ore_Layer(
			"vein8",
			0, 128,
			5,
			8,
			32,
			ELEMENT.getInstance().IRON,
			SAPPHIRE_BLUE,
			LIM_YELLOW,
			GRAPHITE);
	
	
	

	/*public static WorldGen_GT_Australia_Ore_Layer Vein9 = new WorldGen_GT_Australia_Ore_Layer(
			"vein9",
			10, 30,
			20,
			4,
			64,
			ORES.LANTHANITE_CE,
			FLUORIDES.FLUORITE,
			PLATINUM,
			ORES.FLORENCITE);
	public static WorldGen_GT_Australia_Ore_Layer Vein10 = new WorldGen_GT_Australia_Ore_Layer(
			"vein10",
			20, 50,
			20,
			8,
			32,
			ORES.GEIKIELITE,
			ORES.YTTROCERITE,
			ORES.LANTHANITE_LA,
			BAUXITE);
	public static WorldGen_GT_Australia_Ore_Layer Vein11 = new WorldGen_GT_Australia_Ore_Layer(
			"vein11",
			30, 70,
			20,
			5,
			64,
			FLUORIDES.FLUORITE,
			SAPPHIRE_BLUE,
			ORES.ZIRCON,
			ORES.CRYOLITE);
	public static WorldGen_GT_Australia_Ore_Layer Vein12 = new WorldGen_GT_Australia_Ore_Layer(
			"vein12",
			40, 80,
			20,
			8,
			32,
			ORES.CERITE,
			SAPPHIRE_GREEN,
			CHALCOPYRITE,
			ORES.HIBONITE);
	
	*//**
	 * Best Rarest Veins 2017
	 *//*
	
	public static WorldGen_GT_Australia_Ore_Layer Vein13 = new WorldGen_GT_Australia_Ore_Layer(
			"vein13",
			5, 15,
			5,
			5,
			16,
			ORES.CRYOLITE,
			NAQPLUS,
			NAQUADRIA,
			ORES.FLORENCITE);

	public static WorldGen_GT_Australia_Ore_Layer Vein14 = new WorldGen_GT_Australia_Ore_Layer(
			"vein14",
			10, 20,
			8,
			3,
			16,
			URNAIUM235,
			PLUTONIUM,
			OSMIUM,
			AMETHYST);

	public static WorldGen_GT_Australia_Ore_Layer Vein15 = new WorldGen_GT_Australia_Ore_Layer(
			"vein15",
			5, 25,
			5,
			6,
			24,
			ORES.FLUORCAPHITE,
			BISMUTH,
			ORES.GADOLINITE_CE,
			ORES.GADOLINITE_Y);

	public static WorldGen_GT_Australia_Ore_Layer Vein16 = new WorldGen_GT_Australia_Ore_Layer(
			"vein16",
			0, 25,
			4,
			6,
			32,
			ORES.YTTROCERITE,
			ORES.LEPERSONNITE,
			INFUSEDGOLD,
			FLUORIDES.FLUORITE);
	
	public static WorldGen_GT_Australia_Ore_Layer Vein17 = new WorldGen_GT_Australia_Ore_Layer(
			"vein17",
			10, 35,
			4,
			5,
			32,
			ORES.FLORENCITE,
			URNAIUM235,
			ORES.SAMARSKITE_YB,
			ORES.POLYCRASE);
	public static WorldGen_GT_Australia_Ore_Layer Vein18 = new WorldGen_GT_Australia_Ore_Layer(
			"vein18",
			15, 40,
			4,
			4,
			64,
			ORES.GADOLINITE_CE,
			ORES.GADOLINITE_Y,
			ORES.AGARDITE_LA,
			ORES.AGARDITE_CD);*/
	
	
	
	
	public static Hashtable<Long, WorldGen_GT_Australia_Ore_Layer> validOreveins = new Hashtable<Long, WorldGen_GT_Australia_Ore_Layer>(
			1024);
	

	public static volatile BoxedQuad<Integer, Integer, Integer, Integer> OreVein1 = new BoxedQuad<Integer, Integer, Integer, Integer>(null, null, null, null);
	
	static long ID = 0;
	public static void generateValidOreVeins(){
		validOreveins.put(ID++, BaseVein);
		
		validOreveins.put(ID++, Vein1);
		validOreveins.put(ID++, Vein2);
		validOreveins.put(ID++, Vein3);
		validOreveins.put(ID++, Vein4);
		validOreveins.put(ID++, Vein5);
		validOreveins.put(ID++, Vein6);
		validOreveins.put(ID++, Vein7);
		validOreveins.put(ID++, Vein8);
		
		/*validOreveins.put(ID++, Vein9);
		validOreveins.put(ID++, Vein10);
		validOreveins.put(ID++, Vein11);
		validOreveins.put(ID++, Vein12);
		validOreveins.put(ID++, Vein13);
		validOreveins.put(ID++, Vein14);
		validOreveins.put(ID++, Vein15);
		validOreveins.put(ID++, Vein16);
		validOreveins.put(ID++, Vein17);
		validOreveins.put(ID++, Vein18);*/
	}
	
}
