package gtPlusPlus.core.material;

import gregtech.api.enums.Materials;

import gtPlusPlus.core.material.state.MaterialState;
import gtPlusPlus.core.util.minecraft.MaterialUtils;

public final class ALLOY {

	//Just some GT Alloys that I need within mine.
	public static final Material BRONZE = MaterialUtils.generateMaterialFromGtENUM(Materials.Bronze);
	public static final Material STEEL = MaterialUtils.generateMaterialFromGtENUM(Materials.Steel);
	public static final Material STEEL_BLACK = MaterialUtils.generateMaterialFromGtENUM(Materials.BlackSteel);
	public static final Material INVAR = MaterialUtils.generateMaterialFromGtENUM(Materials.Invar);
	public static final Material KANTHAL = MaterialUtils.generateMaterialFromGtENUM(Materials.Kanthal);
	public static final Material NICHROME = MaterialUtils.generateMaterialFromGtENUM(Materials.Nichrome);
	public static final Material TUNGSTENSTEEL = MaterialUtils.generateMaterialFromGtENUM(Materials.TungstenSteel);
	public static final Material STAINLESSSTEEL = MaterialUtils.generateMaterialFromGtENUM(Materials.StainlessSteel);
	public static final Material OSMIRIDIUM = MaterialUtils.generateMaterialFromGtENUM(Materials.Osmiridium);

	public static final Material ENERGYCRYSTAL = new Material(
			"Energy Crystal", //Material Name
			MaterialState.SOLID, //State
			new short[]{228, 255, 0, 0}, //Material Colour
			5660, //Melting Point in C
			7735, //Boiling Point in C
			150, //Protons
			80, //Neutrons
			true, //Uses Blast furnace?
			"⬟ ⯂ ⬢ ⬣ ⯃ ⯄",
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().AER, 5),
					new MaterialStack(ELEMENT.getInstance().IGNIS, 5),
					new MaterialStack(ELEMENT.getInstance().TERRA, 5),
					new MaterialStack(ELEMENT.getInstance().AQUA, 5)
			});

	public static final Material BLOODSTEEL = new Material(
			"Blood Steel", //Material Name
			MaterialState.SOLID, //State
			new short[]{142, 28, 0, 0}, //Material Colour
			2500, //Melting Point in C
			0, //Boiling Point in C
			100, //Protons
			100, //Neutrons
			false, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ALLOY.STEEL, 5),
					new MaterialStack(ELEMENT.getInstance().IGNIS, 5)
			});

	public static final Material STABALLOY = new Material(
			"Staballoy", //Material Name
			MaterialState.SOLID, //State
			new short[]{68, 75, 66, 0}, //Material Colour
			3450, //Melting Point in C
			-1,
			-1,
			-1,
			true, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().URANIUM238, 9),
					new MaterialStack(ELEMENT.getInstance().TITANIUM, 1)
			});

	public static final Material TANTALLOY_60 = new Material(
			"Tantalloy-60", //Material Name
			MaterialState.SOLID, //State
			new short[]{213, 231, 237, 0}, //Material Colour
			3025, //Melting Point in C
			-1,
			-1,
			-1,
			true, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().TUNGSTEN, 4),
					new MaterialStack(ELEMENT.getInstance().TANTALUM, 46)
			});

	public static final Material TANTALLOY_61 = new Material(
			"Tantalloy-61", //Material Name
			MaterialState.SOLID, //State
			new short[]{193, 211, 217, 0}, //Material Colour
			3030, //Melting Point in C
			-1,
			-1,
			-1,
			true, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ALLOY.TANTALLOY_60, 2),
					new MaterialStack(ELEMENT.getInstance().TITANIUM, 12),
					new MaterialStack(ELEMENT.getInstance().YTTRIUM, 8)
			});

	public static final Material TUMBAGA = new Material(
			"Tumbaga", //Material Name
			MaterialState.SOLID, //State
			new short[]{255,178,15, 0}, //Material Colour
			-1,
			-1,
			-1,
			-1,
			false, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().GOLD, 70),
					new MaterialStack(ELEMENT.getInstance().COPPER, 30)
			});

	public static final Material POTIN = new Material(
			"Potin", //Material Name
			MaterialState.SOLID, //State
			new short[]{201,151,129, 0}, //Material Colour
			-1,
			-1,
			-1,
			-1,
			false, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().LEAD, 40),
					new MaterialStack(ALLOY.BRONZE, 40),
					new MaterialStack(ELEMENT.getInstance().TIN, 20)
			});

	/*public static final Material BEDROCKIUM = new Material(
			"Bedrockium", //Material Name
			new short[]{32, 32, 32, 0}, //Material Colour
			7735, //Melting Point in C
			0, //Boiling Point in C
			100, //Protons
			100, //Neutrons
			false, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			null);*/

	public static final Material INCONEL_625 = new Material(
			"Inconel-625", //Material Name
			MaterialState.SOLID, //State
			new short[]{128, 200, 128, 0}, //Material Colour
			2425, //Melting Point in C
			3758,
			-1,
			-1,
			true, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().NICKEL, 3),
					new MaterialStack(ELEMENT.getInstance().CHROMIUM, 7),
					new MaterialStack(ELEMENT.getInstance().MOLYBDENUM, 10),
					new MaterialStack(INVAR, 10),
					new MaterialStack(NICHROME, 13)
			});

	public static final Material INCONEL_690 = new Material(
			"Inconel-690", //Material Name
			MaterialState.SOLID, //State
			new short[]{118, 220, 138, 0}, //Material Colour
			3425, //Melting Point in C
			4895,
			-1,
			-1,
			true, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().CHROMIUM, 5),
					new MaterialStack(ELEMENT.getInstance().NIOBIUM, 10),
					new MaterialStack(ELEMENT.getInstance().MOLYBDENUM, 10),
					new MaterialStack(NICHROME, 15)
			});

	public static final Material INCONEL_792 = new Material(
			"Inconel-792", //Material Name
			MaterialState.SOLID, //State
			new short[]{108, 240, 118, 0}, //Material Colour
			3425, //Melting Point in C
			6200,
			-1,
			-1,
			true, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().NICKEL, 20),
					new MaterialStack(ELEMENT.getInstance().NIOBIUM, 10),
					new MaterialStack(ELEMENT.getInstance().ALUMINIUM, 20),
					new MaterialStack(NICHROME, 10)
			});
	
	public static final Material NITINOL_60 = new Material(
			"Nitinol 60", //Material Name
			MaterialState.SOLID, //State
			null, //Material Colour
			5651, //Melting Point in C
			8975,
			-1,
			-1,
			true, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().NICKEL, 40),
					new MaterialStack(ELEMENT.getInstance().TITANIUM, 60)
			});


	public static final Material ZERON_100 = new Material(
			"Zeron-100", //Material Name
			MaterialState.SOLID, //State
			new short[]{180, 180, 20, 0}, //Material Colour
			6100,
			9785,
			-1,
			-1,
			true, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().CHROMIUM, 26),
					new MaterialStack(ELEMENT.getInstance().NICKEL, 6),
					new MaterialStack(ELEMENT.getInstance().MOLYBDENUM, 4),
					new MaterialStack(ELEMENT.getInstance().COPPER, 20),
					new MaterialStack(ELEMENT.getInstance().TUNGSTEN, 4),
					new MaterialStack(ALLOY.STEEL, 40)
			});

	public static final Material MARAGING250 = new Material(
			"Maraging Steel 250", //Material Name
			MaterialState.SOLID, //State
			null, //Material Colour
			2413, //Melting Point in C
			4555,
			-1,
			-1,
			true, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ALLOY.STEEL, 64),
					new MaterialStack(ELEMENT.getInstance().MOLYBDENUM, 4),
					new MaterialStack(ELEMENT.getInstance().TITANIUM, 4),
					new MaterialStack(ELEMENT.getInstance().NICKEL, 16),
					new MaterialStack(ELEMENT.getInstance().COBALT, 8),
			});

	public static final Material MARAGING300 = new Material(
			"Maraging Steel 300", //Material Name
			MaterialState.SOLID, //State
			null, //Material Colour
			2413, //Melting Point in C
			4555,
			-1,
			-1,
			true, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ALLOY.STEEL, 64),
					new MaterialStack(ELEMENT.getInstance().TITANIUM, 4),
					new MaterialStack(ELEMENT.getInstance().ALUMINIUM, 4),
					new MaterialStack(ELEMENT.getInstance().NICKEL, 16),
					new MaterialStack(ELEMENT.getInstance().COBALT, 8),
			});

	public static final Material MARAGING350 = new Material(
			"Maraging Steel 350", //Material Name
			MaterialState.SOLID, //State
			null, //Material Colour
			2413, //Melting Point in C
			4555,
			-1,
			-1,
			true, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ALLOY.STEEL, 64),
					new MaterialStack(ELEMENT.getInstance().ALUMINIUM, 4),
					new MaterialStack(ELEMENT.getInstance().MOLYBDENUM, 4),
					new MaterialStack(ELEMENT.getInstance().NICKEL, 16),
					new MaterialStack(ELEMENT.getInstance().COBALT, 8),
			});

	public static final Material AQUATIC_STEEL = new Material(
			"Watertight Steel", //Material Name
			MaterialState.SOLID, //State
			new short[] {120, 120, 180}, //Material Colour
			2673, //Melting Point in C
			4835,
			-1,
			-1,
			true, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ALLOY.STEEL, 60),
					new MaterialStack(ELEMENT.getInstance().CARBON, 10),
					new MaterialStack(ELEMENT.getInstance().MANGANESE, 5),
					new MaterialStack(ELEMENT.getInstance().SILICON, 10),
					new MaterialStack(ELEMENT.getInstance().PHOSPHORUS, 5),
					new MaterialStack(ELEMENT.getInstance().SULFUR, 5),
					new MaterialStack(ELEMENT.getInstance().ALUMINIUM, 5)
			});

	public static final Material STELLITE = new Material(
			"Stellite", //Material Name
			MaterialState.SOLID, //State
			null, //Material Colour
			4310, //Melting Point in C
			6250,
			-1,
			-1,
			true, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().COBALT, 35),
					new MaterialStack(ELEMENT.getInstance().CHROMIUM, 35),
					new MaterialStack(ELEMENT.getInstance().MANGANESE, 20),
					new MaterialStack(ELEMENT.getInstance().TITANIUM, 10)
			});

	public static final Material TALONITE = new Material(
			"Talonite", //Material Name
			MaterialState.SOLID, //State
			null, //Material Colour
			3454, //Melting Point in C
			5500,
			-1,
			-1,
			true, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().COBALT, 40),
					new MaterialStack(ELEMENT.getInstance().CHROMIUM, 30),
					new MaterialStack(ELEMENT.getInstance().PHOSPHORUS, 20),
					new MaterialStack(ELEMENT.getInstance().MOLYBDENUM, 10)
			});

	public static final Material HASTELLOY_W = new Material(
			"Hastelloy-W", //Material Name
			MaterialState.SOLID, //State
			null, //Material Colour
			3350, //Melting Point in C
			5755,
			-1,
			-1,
			true, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().IRON, 06),
					new MaterialStack(ELEMENT.getInstance().COBALT, 2),
					new MaterialStack(ELEMENT.getInstance().MOLYBDENUM, 24),
					new MaterialStack(ELEMENT.getInstance().CHROMIUM, 6),
					new MaterialStack(ELEMENT.getInstance().NICKEL, 62)
			});

	public static final Material HASTELLOY_X = new Material(
			"Hastelloy-X", //Material Name
			MaterialState.SOLID, //State
			null, //Material Colour
			3350, //Melting Point in C
			5755,
			-1,
			-1,
			true, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().IRON, 18),
					new MaterialStack(ELEMENT.getInstance().MANGANESE, 2),
					new MaterialStack(ELEMENT.getInstance().SILICON, 2),
					new MaterialStack(ELEMENT.getInstance().MOLYBDENUM, 8),
					new MaterialStack(ELEMENT.getInstance().CHROMIUM, 22),
					new MaterialStack(ELEMENT.getInstance().NICKEL, 48)
			});

	public static final Material HASTELLOY_N = new Material(
			"Hastelloy-N", //Material Name
			MaterialState.SOLID, //State
			null, //Material Colour
			4350, //Melting Point in C
			6875,
			-1,
			-1,
			true, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().YTTRIUM, 8),
					new MaterialStack(ELEMENT.getInstance().MOLYBDENUM, 16),
					new MaterialStack(ELEMENT.getInstance().CHROMIUM, 8),
					new MaterialStack(ELEMENT.getInstance().TITANIUM, 8),
					new MaterialStack(ELEMENT.getInstance().NICKEL, 60)
			});

	public static final Material HASTELLOY_C276 = new Material(
			"Hastelloy-C276", //Material Name
			MaterialState.SOLID, //State
			null, //Material Colour
			4350, //Melting Point in C
			6520,
			-1,
			-1,
			true, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().COBALT, 2),
					new MaterialStack(ELEMENT.getInstance().MOLYBDENUM, 16),
					new MaterialStack(ELEMENT.getInstance().TUNGSTEN, 2),
					new MaterialStack(ELEMENT.getInstance().COPPER, 2),
					new MaterialStack(ELEMENT.getInstance().CHROMIUM, 14),
					new MaterialStack(ELEMENT.getInstance().NICKEL, 64)
			});

	public static final Material INCOLOY_020 = new Material(
			"Incoloy-020", //Material Name
			MaterialState.SOLID, //State
			null, //Material Colour
			3425, //Melting Point in C
			5420,
			-1,
			-1,
			true, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().IRON, 40),
					new MaterialStack(ELEMENT.getInstance().COPPER, 4),
					new MaterialStack(ELEMENT.getInstance().CHROMIUM, 20),
					new MaterialStack(ELEMENT.getInstance().NICKEL, 36)
			});

	public static final Material INCOLOY_DS = new Material(
			"Incoloy-DS", //Material Name
			MaterialState.SOLID, //State
			null, //Material Colour
			3425, //Melting Point in C
			5420,
			-1,
			-1,
			true, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().IRON, 46),
					new MaterialStack(ELEMENT.getInstance().COBALT, 18),
					new MaterialStack(ELEMENT.getInstance().CHROMIUM, 18),
					new MaterialStack(ELEMENT.getInstance().NICKEL, 18)
			});

	public static final Material INCOLOY_MA956 = new Material(
			"Incoloy-MA956", //Material Name
			MaterialState.SOLID, //State
			null, //Material Colour
			4425, //Melting Point in C
			6875,
			-1,
			-1,
			true, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().IRON, 64),
					new MaterialStack(ELEMENT.getInstance().ALUMINIUM, 12),
					new MaterialStack(ELEMENT.getInstance().CHROMIUM, 20),
					new MaterialStack(ELEMENT.getInstance().YTTRIUM, 4)
			});

	public static final Material TUNGSTEN_CARBIDE = new Material(
			"Tungsten Carbide", //Material Name
			MaterialState.SOLID, //State
			new short[]{44, 44, 44, 0}, //Material Colour
			3422, //Melting Point in C
			-1,
			-1,
			-1,
			true, //Uses Blast furnace?
			false, //Generate cells
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().CARBON, 50),
					new MaterialStack(ELEMENT.getInstance().TUNGSTEN, 50)
			});
	
	public static final Material TUNGSTEN_TITANIUM_CARBIDE = new Material(
			"Tungsten Titanium Carbide", //Material Name
			MaterialState.SOLID, //State
			null,
			4422, //Melting Point in C
			-1,
			-1,
			-1,
			true, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(TUNGSTEN_CARBIDE, 70),
					new MaterialStack(ELEMENT.getInstance().TITANIUM, 30)
			});

	public static final Material SILICON_CARBIDE = new Material(
			"Silicon Carbide", //Material Name
			MaterialState.SOLID, //State
			new short[]{40, 48, 36, 0}, //Material Colour
			1414, //Melting Point in C
			-1,
			-1,
			-1,
			false, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().SILICON, 50),
					new MaterialStack(ELEMENT.getInstance().CARBON, 50)
			});

	public static final Material TANTALUM_CARBIDE = new Material(
			"Tantalum Carbide", //Material Name
			MaterialState.SOLID, //State
			new short[]{139, 136, 120, 0}, //Material Colour
			2980, //Melting Point in C
			-1,
			-1,
			-1,
			true, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().TANTALUM, 50),
					new MaterialStack(ELEMENT.getInstance().CARBON, 50)
			});

	public static final Material ZIRCONIUM_CARBIDE = new Material(
			"Zirconium Carbide", //Material Name
			MaterialState.SOLID, //State
			new short[]{222, 202, 180, 0}, //Material Colour
			1855, //Melting Point in C
			-1,
			-1,
			-1,
			true, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().ZIRCONIUM, 50),
					new MaterialStack(ELEMENT.getInstance().CARBON, 50)
			});

	public static final Material NIOBIUM_CARBIDE = new Material(
			"Niobium Carbide", //Material Name
			MaterialState.SOLID, //State
			new short[]{205, 197, 191, 0}, //Material Colour
			2477, //Melting Point in C
			-1,
			-1,
			-1,
			true, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().NIOBIUM, 50),
					new MaterialStack(ELEMENT.getInstance().CARBON, 50)
			});
	
	public static final Material ARCANITE = new Material(
			"Arcanite", //Material Name
			MaterialState.SOLID, //State
			null,
			5666, //Melting Point in C
			9875,
			-1,
			-1,
			true, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().THORIUM232, 40),
					new MaterialStack(ENERGYCRYSTAL, 40),
					new MaterialStack(ELEMENT.getInstance().ORDO, 10),
					new MaterialStack(ELEMENT.getInstance().PERDITIO, 10)
			});

	public static final Material LEAGRISIUM = new Material(
			"Grisium", //Material Name
			MaterialState.SOLID, //State
			new short[]{53, 93, 106, 0}, //Material Colour
			3850, //Melting Point in C
			5550, //Boiling Point in C
			96, //Protons
			128, //Neutrons
			true, //Uses Blast furnace?
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().TITANIUM, 18),
					new MaterialStack(ELEMENT.getInstance().CARBON, 18),
					new MaterialStack(ELEMENT.getInstance().POTASSIUM, 18),
					new MaterialStack(ELEMENT.getInstance().LITHIUM, 18),
					new MaterialStack(ELEMENT.getInstance().SULFUR, 18),
					new MaterialStack(ELEMENT.getInstance().HYDROGEN, 10)
			});	//Material Stacks with Percentage of required elements.

	public static final Material EGLIN_STEEL_BASE = new Material(
			"Eglin Steel Base Compound", //Material Name
			MaterialState.SOLID, //State
			null, //Material Colour
			-1, //Melting Point in C
			-1, //Boiling Point in C
			-1,
			-1,
			false, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{

					new MaterialStack(ELEMENT.getInstance().IRON, 12),
					new MaterialStack(KANTHAL, 3),
					new MaterialStack(INVAR, 15)
			});

	public static final Material EGLIN_STEEL = new Material(
			"Eglin Steel", //Material Name
			MaterialState.SOLID, //State
			new short[]{139,69,19, 0}, //Material Colour
			1048, //Melting Point in C
			1973, //Boiling Point in C
			-1,
			-1,
			false, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ALLOY.EGLIN_STEEL_BASE, 10),
					new MaterialStack(ELEMENT.getInstance().SULFUR, 1),
					new MaterialStack(ELEMENT.getInstance().SILICON, 4),
					new MaterialStack(ELEMENT.getInstance().CARBON, 1)
			});

	public static final Material HG1223 = new Material(
			"HG-1223", //Material Name
			MaterialState.LIQUID, //State
			new short[]{39,85,159, 0}, //Material Colour
			-1, //Melting Point in C
			-1, //Boiling Point in C
			-1,
			-1,
			false, //Uses Blast furnace?
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().MERCURY, 1),
					new MaterialStack(ELEMENT.getInstance().BARIUM, 2),
					new MaterialStack(ELEMENT.getInstance().CALCIUM, 2),
					new MaterialStack(ELEMENT.getInstance().COPPER, 3),
					new MaterialStack(ELEMENT.getInstance().OXYGEN, 8)
			});


	/**
	 * Stargate Materials - #D2FFA9 210, 255, 170
	 */

	public static final Material TRINIUM_TITANIUM = new Material(
			"Trinium Titanium Alloy", //Material Name
			MaterialState.SOLID, //State
			null, //Material Colour
			3750, //Melting Point in C
			7210, //Boiling Point in C
			-1,
			-1,
			true, //Uses Blast furnace?
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().TRINIUM_REFINED, 3),
					new MaterialStack(ELEMENT.getInstance().TITANIUM, 7)
			});
	public static final Material TRINIUM_NAQUADAH = new Material(
			"Trinium Naquadah Alloy", //Material Name
			MaterialState.SOLID, //State
			null, //Material Colour
			4200, //Melting Point in C
			7400, //Boiling Point in C
			-1,
			-1,
			false, //Uses Blast furnace?
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().TRINIUM_REFINED, 5),
					new MaterialStack(ELEMENT.getInstance().NAQUADAH, 9)
			});
	public static final Material TRINIUM_NAQUADAH_CARBON = new Material(
			"Trinium Naquadah Carbonite", //Material Name
			MaterialState.SOLID, //State
			null, //Material Colour
			6500, //Melting Point in C
			9000, //Boiling Point in C
			-1,
			-1,
			true, //Uses Blast furnace?
			new MaterialStack[]{
					new MaterialStack(TRINIUM_NAQUADAH, 9),
					new MaterialStack(ELEMENT.getInstance().CARBON, 1)
			});
	
	public static final Material TRINIUM_REINFORCED_STEEL = new Material(
			"Arceus Alloy 2B", //Material Name
			MaterialState.SOLID, //State
			new short[]{205, 197, 23, 0}, //Material Colour
			7555, //Melting Point in C
			12350,
			-1,
			-1,
			true, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().TRINIUM_REFINED, 30),
					new MaterialStack(ALLOY.MARAGING350, 40),
					new MaterialStack(ALLOY.TUNGSTENSTEEL, 20),
					new MaterialStack(ALLOY.OSMIRIDIUM, 10)
			});
	
	

	/*
	 * Witchery Material
	 */

	public static final Material KOBOLDITE = new Material(
			"Koboldite", //Material Name
			MaterialState.SOLID, //State
			new short[]{80, 210, 255, 0}, //Material Colour
			-1, //Melting Point in C
			-1,
			-1,
			-1,
			true, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().NICKEL, 35),
					new MaterialStack(ELEMENT.getInstance().THAUMIUM, 30),
					new MaterialStack(ELEMENT.getInstance().IRON, 35)
			});
	
	
	/*
	 * Top Tier Alloys
	 */
	
	//0lafe Compound
	public static final Material LAFIUM = new Material(
			"Lafium Compound", //Material Name
			MaterialState.SOLID, //State
			null, //Material Colour
			6750, //Melting Point in C
			9865, //Boiling Point in C
			-1,
			-1,
			true, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ALLOY.HASTELLOY_N, 8),
					new MaterialStack(ELEMENT.getInstance().NAQUADAH, 4),
					new MaterialStack(ELEMENT.getInstance().SAMARIUM, 2),
					new MaterialStack(ELEMENT.getInstance().TUNGSTEN, 4),
					new MaterialStack(ELEMENT.getInstance().ARGON, 2),
					new MaterialStack(ELEMENT.getInstance().ALUMINIUM, 6),
					new MaterialStack(ELEMENT.getInstance().NICKEL, 8),
					new MaterialStack(ELEMENT.getInstance().CARBON, 2)
			});

	//Cinobi Alloy
	public static final Material CINOBITE = new Material(
			"Cinobite A243", //Material Name
			MaterialState.SOLID, //State
			null, //Material Colour
			7350, //Melting Point in C
			12565, //Boiling Point in C
			-1,
			-1,
			true, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ALLOY.ZERON_100, 16),
					new MaterialStack(ELEMENT.getInstance().NAQUADRIA, 7),
					new MaterialStack(ELEMENT.getInstance().GADOLINIUM, 5),
					new MaterialStack(ELEMENT.getInstance().ALUMINIUM, 3),
					new MaterialStack(ELEMENT.getInstance().MERCURY, 2),
					new MaterialStack(ELEMENT.getInstance().TIN, 2),
					new MaterialStack(ELEMENT.getInstance().TITANIUM, 12),
					new MaterialStack(ALLOY.OSMIRIDIUM, 6)
			});
	
	//Piky Alloy
	public static final Material PIKYONIUM = new Material(
			"Pikyonium 64B", //Material Name
			MaterialState.SOLID, //State
            new short[]{52, 103, 186, 0}, //Material Colour
			7850, //Melting Point in C
			11765, //Boiling Point in C
			-1,
			-1,
			true, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ALLOY.INCONEL_792, 16),
					new MaterialStack(ALLOY.EGLIN_STEEL, 10),
					new MaterialStack(ELEMENT.getInstance().NAQUADAH_ENRICHED, 8),
					new MaterialStack(ELEMENT.getInstance().CERIUM, 6),
					new MaterialStack(ELEMENT.getInstance().ANTIMONY, 4),
					new MaterialStack(ELEMENT.getInstance().PLATINUM, 4),					
					new MaterialStack(ELEMENT.getInstance().YTTERBIUM, 2),
					new MaterialStack(ALLOY.TUNGSTENSTEEL, 8)
			});
	
	//Piky Alloy
	public static final Material ABYSSAL = new Material(
			"Abyssal Alloy", //Material Name
			MaterialState.SOLID, //State
			null, //Material Colour
			9650, //Melting Point in C
			13765, //Boiling Point in C
			-1,
			-1,
			true, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ALLOY.STAINLESSSTEEL, 10),
					new MaterialStack(ALLOY.TUNGSTEN_CARBIDE, 10),
					new MaterialStack(ALLOY.NICHROME, 10),
					new MaterialStack(ALLOY.BRONZE, 10),
					new MaterialStack(ALLOY.INCOLOY_MA956, 10),
					new MaterialStack(ELEMENT.getInstance().IODINE, 2),
					new MaterialStack(ELEMENT.getInstance().RADON, 2),
					new MaterialStack(ELEMENT.getInstance().GERMANIUM, 2),
			});
	
	//Titansteel
	public static final Material TITANSTEEL = new Material(
			"Titansteel", //Material Name
			MaterialState.SOLID, //State
			null, //Material Colour
			8250, //Melting Point in C
			11765, //Boiling Point in C
			-1,
			-1,
			true, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ALLOY.TUNGSTEN_TITANIUM_CARBIDE, 3),
					new MaterialStack(ELEMENT.getInstance().IGNIS, 1),
					new MaterialStack(ELEMENT.getInstance().TERRA, 1),
					new MaterialStack(ELEMENT.getInstance().PERDITIO, 1),
			});
	
	
	public static final Material OCTIRON = new Material(
			"Octiron", //Material Name
			MaterialState.SOLID, //State
			null,
			9120, //Melting Point in C
			14200,
			-1,
			-1,
			true, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ARCANITE, 30),
					new MaterialStack(TITANSTEEL, 30),
					new MaterialStack(ENERGYCRYSTAL, 5),
					new MaterialStack(STEEL_BLACK, 10),
					new MaterialStack(ELEMENT.getInstance().THAUMIUM, 25)
			});
	
	//Quantum
	public static final Material QUANTUM = new Material(
			"Quantum", //Material Name
			MaterialState.SOLID, //State
			null, //Material Colour
			10500, //Melting Point in C
			25000, //Boiling Point in C
			150, //Protons
			200, //Neutrons
			true, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ALLOY.STELLITE, 15),
					new MaterialStack(ALLOY.ENERGYCRYSTAL, 5),
					new MaterialStack(ALLOY.SILICON_CARBIDE, 5),
					new MaterialStack(ELEMENT.getInstance().GALLIUM, 5),
					new MaterialStack(ELEMENT.getInstance().AMERICIUM, 5),
					new MaterialStack(ELEMENT.getInstance().PALLADIUM, 5),
					new MaterialStack(ELEMENT.getInstance().BISMUTH, 5),
					new MaterialStack(ELEMENT.getInstance().GERMANIUM, 5)
			});



}
