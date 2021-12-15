package gtPlusPlus.everglades.gen.gt;

import java.util.Hashtable;

import gtPlusPlus.core.material.ELEMENT;
import gtPlusPlus.core.material.ORES;
import gtPlusPlus.core.material.nuclear.FLUORIDES;

public class WorldGen_Ores {

	public static WorldGen_GT_Ore_Layer BaseVein = new WorldGen_GT_Ore_Layer("veinA", 20, 40, 1, 1, 128,
			ELEMENT.getInstance().IRON, ELEMENT.getInstance().IRON, ELEMENT.getInstance().IRON,
			ELEMENT.getInstance().IRON);

	/**
	 * Custom ore Veins
	 */

	public static WorldGen_GT_Ore_Layer Vein1 = new WorldGen_GT_Ore_Layer("vein1", 0, 60, 30, 2, 16, ORES.AGARDITE_CD,
			ORES.AGARDITE_LA, ORES.DEMICHELEITE_BR, ORES.IRARSITE);

	public static WorldGen_GT_Ore_Layer Vein2 = new WorldGen_GT_Ore_Layer("vein2", 0, 60, 30, 2, 16, ORES.AGARDITE_ND,
			ORES.AGARDITE_Y, ORES.KASHINITE, ORES.CERITE);

	public static WorldGen_GT_Ore_Layer Vein3 = new WorldGen_GT_Ore_Layer("vein3", 0, 60, 30, 3, 32, ORES.CERITE,
			ORES.NICHROMITE, ORES.XENOTIME, ORES.HIBONITE);

	public static WorldGen_GT_Ore_Layer Vein4 = new WorldGen_GT_Ore_Layer("vein4", 0, 60, 40, 3, 32, ORES.GEIKIELITE,
			ORES.CRYOLITE, ORES.GADOLINITE_CE, ORES.AGARDITE_ND);

	public static WorldGen_GT_Ore_Layer Vein5 = new WorldGen_GT_Ore_Layer("vein5", 30, 128, 20, 2, 48, ORES.HIBONITE,
			ORES.YTTRIALITE, ORES.ZIRCONILITE, ORES.CERITE);
	public static WorldGen_GT_Ore_Layer Vein6 = new WorldGen_GT_Ore_Layer("vein6", 0, 40, 20, 2, 48, ORES.XENOTIME,
			ORES.ZIRKELITE, ORES.CROCROITE, ORES.IRARSITE);
	public static WorldGen_GT_Ore_Layer Vein7 = new WorldGen_GT_Ore_Layer("vein7", 40, 128, 20, 2, 48, ORES.HONEAITE,
			ORES.MIESSIITE, ORES.SAMARSKITE_Y, ORES.SAMARSKITE_YB);
	public static WorldGen_GT_Ore_Layer Vein8 = new WorldGen_GT_Ore_Layer("vein8", 0, 40, 20, 2, 48, ORES.TITANITE,
			ORES.ZIMBABWEITE, ORES.ZIRCON, ORES.FLORENCITE);

	public static WorldGen_GT_Ore_Layer Vein9 = new WorldGen_GT_Ore_Layer("vein9", 10, 30, 20, 1, 48,
			ORES.LANTHANITE_CE, FLUORIDES.FLUORITE, ORES.LAFOSSAITE, ORES.FLORENCITE);
	public static WorldGen_GT_Ore_Layer Vein10 = new WorldGen_GT_Ore_Layer("vein10", 20, 50, 20, 2, 32, ORES.GEIKIELITE,
			ORES.YTTROCERITE, ORES.LANTHANITE_LA, ORES.RADIOBARITE);
	public static WorldGen_GT_Ore_Layer Vein11 = new WorldGen_GT_Ore_Layer("vein11", 30, 70, 20, 1, 48,
			FLUORIDES.FLUORITE, ORES.KASHINITE, ORES.ZIRCON, ORES.CRYOLITE);
	public static WorldGen_GT_Ore_Layer Vein12 = new WorldGen_GT_Ore_Layer("vein12", 40, 80, 20, 3, 32, ORES.CERITE,
			ORES.ALBURNITE, ORES.MIESSIITE, ORES.HIBONITE);

	/**
	 * Best Rarest Veins 2017
	 */

	public static WorldGen_GT_Ore_Layer Vein13 = new WorldGen_GT_Ore_Layer("vein13", 5, 15, 5, 1, 16, ORES.CRYOLITE,
			ORES.RADIOBARITE, ORES.HONEAITE, ORES.FLORENCITE);

	public static WorldGen_GT_Ore_Layer Vein14 = new WorldGen_GT_Ore_Layer("vein14", 10, 20, 8, 2, 16,
			ORES.DEMICHELEITE_BR, ORES.PERROUDITE, ORES.IRARSITE, ORES.RADIOBARITE);

	public static WorldGen_GT_Ore_Layer Vein15 = new WorldGen_GT_Ore_Layer("vein15", 5, 25, 5, 3, 24, ORES.FLUORCAPHITE,
			ORES.LAFOSSAITE, ORES.GADOLINITE_CE, ORES.GADOLINITE_Y);

	public static WorldGen_GT_Ore_Layer Vein16 = new WorldGen_GT_Ore_Layer("vein16", 0, 25, 4, 2, 32, ORES.YTTROCERITE,
			ORES.LEPERSONNITE, ORES.LAUTARITE, FLUORIDES.FLUORITE);

	public static WorldGen_GT_Ore_Layer Vein17 = new WorldGen_GT_Ore_Layer("vein17", 10, 35, 4, 1, 32, ORES.FLORENCITE,
			ORES.LAUTARITE, ORES.SAMARSKITE_YB, ORES.POLYCRASE);
	public static WorldGen_GT_Ore_Layer Vein18 = new WorldGen_GT_Ore_Layer("vein18", 15, 40, 4, 1, 48,
			ORES.GADOLINITE_CE, ORES.GADOLINITE_Y, ORES.AGARDITE_LA, ORES.AGARDITE_CD);

	public static Hashtable<Long, WorldGen_GT_Ore_Layer> validOreveins = new Hashtable<Long, WorldGen_GT_Ore_Layer>(
			1024);

	static long ID = 0;

	public static void generateValidOreVeins() {
		validOreveins.put(ID++, BaseVein);
		validOreveins.put(ID++, Vein1);
		validOreveins.put(ID++, Vein2);
		validOreveins.put(ID++, Vein3);
		validOreveins.put(ID++, Vein4);
		validOreveins.put(ID++, Vein5);
		validOreveins.put(ID++, Vein6);
		validOreveins.put(ID++, Vein7);
		validOreveins.put(ID++, Vein8);
		validOreveins.put(ID++, Vein9);
		validOreveins.put(ID++, Vein10);
		validOreveins.put(ID++, Vein11);
		validOreveins.put(ID++, Vein12);
		validOreveins.put(ID++, Vein13);
		validOreveins.put(ID++, Vein14);
		validOreveins.put(ID++, Vein15);
		validOreveins.put(ID++, Vein16);
		validOreveins.put(ID++, Vein17);
		validOreveins.put(ID++, Vein18);
	}

}
