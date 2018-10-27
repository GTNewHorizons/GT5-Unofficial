package gtPlusPlus.everglades.gen.gt;

import java.util.Hashtable;

import net.minecraft.block.Block;

import gregtech.api.enums.Materials;

import gtPlusPlus.api.objects.data.Pair;
import gtPlusPlus.core.material.ELEMENT;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.ORES;
import gtPlusPlus.core.material.nuclear.FLUORIDES;
import gtPlusPlus.core.util.minecraft.MaterialUtils;
import gtPlusPlus.everglades.object.BoxedQuad;

public class WorldGen_Ores {
	
	/**
	 * Set Ore Types on by one.
	 */

	public static volatile Pair<Block, Integer> Geikielite; 	// MgTiO3
	public static volatile Pair<Block, Integer> Zimbabweite; 	// (Na,K)2PbAs4(Nb,Ta,Ti)4O18
	public static volatile Pair<Block, Integer> Titanite;		// CaTiSiO5
	public static volatile Pair<Block, Integer> Zirconolite;	// CaZrTi2O7
	public static volatile Pair<Block, Integer> Crocoite;		// PbCrO4
	public static volatile Pair<Block, Integer> Nichromite;		// (Ni,Co,Fe)(Cr,Fe,Al)2O4
	public static volatile Pair<Block, Integer> Yttriaite;		// Y2O3
	public static volatile Pair<Block, Integer> Samarskite_Y;	// (YFe3+Fe2+U,Th,Ca)2(Nb,Ta)2O8
	public static volatile Pair<Block, Integer> Samarskite_Yb;	// (YbFe3+)2(Nb,Ta)2O8
	public static volatile Pair<Block, Integer> Zircon;			// ZrSiO4
	public static volatile Pair<Block, Integer> Gadolinite_Ce;	// (Ce2,La,Nd,Y)2FeBe2Si1O14
	public static volatile Pair<Block, Integer> Gadolinite_Y;	// (Ce,La,Nd,Y2)2FeBe2Si4O9
	public static volatile Pair<Block, Integer> Lepersonnite;	// Ca(Gd,Dy)2(UO2)24(SiO4)4(CO3)8(OH)24·48H2O
	public static volatile Pair<Block, Integer> Xenotime;		// YPO4
	public static volatile Pair<Block, Integer> Yttrialite;		// Y2Th2Si2O7
	public static volatile Pair<Block, Integer> Yttrocerite;	// CaF5YCe
	public static volatile Pair<Block, Integer> Polycrase;		// YCaCeUThTi2Nb2Ta2O6
	public static volatile Pair<Block, Integer> Zircophyllite;	// (K,Na)3(Mn,Fe)7(Zr,Ti,Nb)2Si8O24(OH,F)7
	public static volatile Pair<Block, Integer> Zirkelite;		// (Ca,Th,Ce)Zr(Ti,Nb)2O7
	public static volatile Pair<Block, Integer> Lanthanite_La;	// (La)2(CO3)3·8(H2O).
	public static volatile Pair<Block, Integer> Lanthanite_Ce;	// (Ce)2(CO3)3·8(H2O).
	public static volatile Pair<Block, Integer> Lanthanite_Nd;	// (Nd)2(CO3)3·8(H2O).
	public static volatile Pair<Block, Integer> Hibonite;		// ((Ca,Ce)(Al,Ti,Mg)12O19)
	public static volatile Pair<Block, Integer> Cerite;			// (Ce,La,Ca)9(Mg,Fe+3)(SiO4)6(SiO3OH)(OH)3
	public static volatile Pair<Block, Integer> Agardite_Y;		// (YCa)Cu5(As2O4)3(OH)6·3H2O
	public static volatile Pair<Block, Integer> Agardite_Cd;	// (CdCa)Cu7(AsO2)4(O2H)5·3H2O
	public static volatile Pair<Block, Integer> Agardite_La;	// (LaCa)Cu5(AsO6)2(OH)4·3H2O
	public static volatile Pair<Block, Integer> Agardite_Nd;	// (NdCa)Cu6(As3O3)2(O2H)6·3H2O
	public static volatile Pair<Block, Integer> Fluorcaphite;	// (Ca,Sr,Ce,Na)5(PO4)3F
	public static volatile Pair<Block, Integer> Florencite;		// SmAl3(PO4)2(OH)6
	public static volatile Pair<Block, Integer> Cryolite;		// Na3AlF6
	//public static volatile Pair<Block, Integer> Pyroxene;		// 


	
	
	public static WorldGen_GT_Ore_Layer BaseVein = new WorldGen_GT_Ore_Layer(
			"veinA",
			20, 40,
			1,
			8,
			128,
			ELEMENT.getInstance().IRON,
			ELEMENT.getInstance().IRON,
			ELEMENT.getInstance().IRON,
			ELEMENT.getInstance().IRON);
	
	
	
	/**
	 * Custom ore Veins
	 */
	
	public static WorldGen_GT_Ore_Layer Vein1 = new WorldGen_GT_Ore_Layer(
			"vein1",
			0, 60,
			30,
			16,
			16,
			ORES.AGARDITE_CD,
			ORES.AGARDITE_LA,
			ORES.DEMICHELEITE_BR,
			ORES.IRARSITE);
	
	public static WorldGen_GT_Ore_Layer Vein2 = new WorldGen_GT_Ore_Layer(
			"vein2",
			0, 60,
			30,
			16,
			16,
			ORES.AGARDITE_ND,
			ORES.AGARDITE_Y,
			ORES.KASHINITE,
			ORES.CERITE);

	public static WorldGen_GT_Ore_Layer Vein3 = new WorldGen_GT_Ore_Layer(
			"vein3",
			0, 60,
			30,
			16,
			32,
			ORES.CERITE,
			ORES.NICHROMITE,
			ORES.XENOTIME,
			ORES.HIBONITE);

	public static WorldGen_GT_Ore_Layer Vein4 = new WorldGen_GT_Ore_Layer(
			"vein4",
			0, 60,
			40,
			16,
			32,
			ORES.GEIKIELITE,
			ORES.CRYOLITE,
			ORES.GADOLINITE_CE,
			ORES.AGARDITE_ND);
	
	
	
	public static WorldGen_GT_Ore_Layer Vein5 = new WorldGen_GT_Ore_Layer(
			"vein5",
			30, 128,
			20,
			8,
			48,
			ORES.HIBONITE,
			ORES.YTTRIALITE,
			ORES.ZIRCONILITE,
			ORES.CERITE);
	public static WorldGen_GT_Ore_Layer Vein6 = new WorldGen_GT_Ore_Layer(
			"vein6",
			0, 40,
			20,
			8,
			48,
			ORES.XENOTIME,
			ORES.ZIRKELITE,
			ORES.CROCROITE,
			ORES.IRARSITE);
	public static WorldGen_GT_Ore_Layer Vein7 = new WorldGen_GT_Ore_Layer(
			"vein7",
			40, 128,
			20,
			8,
			48,
			ORES.HONEAITE,
			ORES.MIESSIITE,
			ORES.SAMARSKITE_Y,
			ORES.SAMARSKITE_YB);
	public static WorldGen_GT_Ore_Layer Vein8 = new WorldGen_GT_Ore_Layer(
			"vein8",
			0, 40,
			20,
			8,
			48,
			ORES.TITANITE,
			ORES.ZIMBABWEITE,
			ORES.ZIRCON,
			ORES.FLORENCITE);
	
	
	

	public static WorldGen_GT_Ore_Layer Vein9 = new WorldGen_GT_Ore_Layer(
			"vein9",
			10, 30,
			20,
			4,
			48,
			ORES.LANTHANITE_CE,
			FLUORIDES.FLUORITE,
			ORES.LAFOSSAITE,
			ORES.FLORENCITE);
	public static WorldGen_GT_Ore_Layer Vein10 = new WorldGen_GT_Ore_Layer(
			"vein10",
			20, 50,
			20,
			8,
			32,
			ORES.GEIKIELITE,
			ORES.YTTROCERITE,
			ORES.LANTHANITE_LA,
			ORES.RADIOBARITE);
	public static WorldGen_GT_Ore_Layer Vein11 = new WorldGen_GT_Ore_Layer(
			"vein11",
			30, 70,
			20,
			5,
			48,
			FLUORIDES.FLUORITE,
			ORES.KASHINITE,
			ORES.ZIRCON,
			ORES.CRYOLITE);
	public static WorldGen_GT_Ore_Layer Vein12 = new WorldGen_GT_Ore_Layer(
			"vein12",
			40, 80,
			20,
			8,
			32,
			ORES.CERITE,
			ORES.ALBURNITE,
			ORES.MIESSIITE,
			ORES.HIBONITE);
	
	/**
	 * Best Rarest Veins 2017
	 */
	
	public static WorldGen_GT_Ore_Layer Vein13 = new WorldGen_GT_Ore_Layer(
			"vein13",
			5, 15,
			5,
			5,
			16,
			ORES.CRYOLITE,
			ORES.RADIOBARITE,
			ORES.HONEAITE,
			ORES.FLORENCITE);

	public static WorldGen_GT_Ore_Layer Vein14 = new WorldGen_GT_Ore_Layer(
			"vein14",
			10, 20,
			8,
			3,
			16,
			ORES.DEMICHELEITE_BR,
			ORES.PERROUDITE,
			ORES.IRARSITE,
			ORES.RADIOBARITE);

	public static WorldGen_GT_Ore_Layer Vein15 = new WorldGen_GT_Ore_Layer(
			"vein15",
			5, 25,
			5,
			6,
			24,
			ORES.FLUORCAPHITE,
			ORES.LAFOSSAITE,
			ORES.GADOLINITE_CE,
			ORES.GADOLINITE_Y);

	public static WorldGen_GT_Ore_Layer Vein16 = new WorldGen_GT_Ore_Layer(
			"vein16",
			0, 25,
			4,
			6,
			32,
			ORES.YTTROCERITE,
			ORES.LEPERSONNITE,
			ORES.LAUTARITE,
			FLUORIDES.FLUORITE);
	
	public static WorldGen_GT_Ore_Layer Vein17 = new WorldGen_GT_Ore_Layer(
			"vein17",
			10, 35,
			4,
			5,
			32,
			ORES.FLORENCITE,
			ORES.LAUTARITE,
			ORES.SAMARSKITE_YB,
			ORES.POLYCRASE);
	public static WorldGen_GT_Ore_Layer Vein18 = new WorldGen_GT_Ore_Layer(
			"vein18",
			15, 40,
			4,
			4,
			48,
			ORES.GADOLINITE_CE,
			ORES.GADOLINITE_Y,
			ORES.AGARDITE_LA,
			ORES.AGARDITE_CD);
	
	
	
	
	public static Hashtable<Long, WorldGen_GT_Ore_Layer> validOreveins = new Hashtable<Long, WorldGen_GT_Ore_Layer>(
			1024);
	

	public static volatile BoxedQuad<Integer, Integer, Integer, Integer> OreVein1 = new BoxedQuad(null, null, null, null);
	
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
