package gtPlusPlus.core.material;

import gregtech.api.enums.Materials;
import gtPlusPlus.core.material.state.MaterialState;
import gtPlusPlus.core.util.materials.MaterialUtils;

public final class ALLOY {
	
	//Just some GT Alloys that I need within mine.
	public static final Material BRONZE = MaterialUtils.generateMaterialFromGtENUM(Materials.Bronze);
	public static final Material STEEL = MaterialUtils.generateMaterialFromGtENUM(Materials.Steel);

	public static final Material ENERGYCRYSTAL = new Material(
			"Energy Crystal", //Material Name
			MaterialState.SOLID, //State
			new short[]{228, 255, 0, 0}, //Material Colour
			5660, //Melting Point in C
			7735, //Boiling Point in C
			150, //Protons
			80, //Neutrons
			true, //Uses Blast furnace?
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
					new MaterialStack(ELEMENT.getInstance().GOLD, 30),
					new MaterialStack(ELEMENT.getInstance().GOLD, 40),
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
			1425, //Melting Point in C
			-1,
			-1,
			-1,
			true, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().NICKEL, 60),
					new MaterialStack(ELEMENT.getInstance().CHROMIUM, 20),
					new MaterialStack(ELEMENT.getInstance().IRON, 10),
					new MaterialStack(ELEMENT.getInstance().MOLYBDENUM, 10)
			});

	public static final Material INCONEL_690 = new Material(
			"Inconel-690", //Material Name
			MaterialState.SOLID, //State
			new short[]{118, 220, 138, 0}, //Material Colour
			1425, //Melting Point in C
			-1,
			-1,
			-1,
			true, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().NICKEL, 60),
					new MaterialStack(ELEMENT.getInstance().CHROMIUM, 20),
					new MaterialStack(ELEMENT.getInstance().NIOBIUM, 10),
					new MaterialStack(ELEMENT.getInstance().MOLYBDENUM, 10)
			});

	public static final Material INCONEL_792 = new Material(
			"Inconel-792", //Material Name
			MaterialState.SOLID, //State
			new short[]{108, 240, 118, 0}, //Material Colour
			1425, //Melting Point in C
			-1,
			-1,
			-1,
			true, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().NICKEL, 60),
					new MaterialStack(ELEMENT.getInstance().CHROMIUM, 10),
					new MaterialStack(ELEMENT.getInstance().NIOBIUM, 10),
					new MaterialStack(ELEMENT.getInstance().ALUMINIUM, 20)
			});


	public static final Material ZERON_100 = new Material(
			"Zeron-100", //Material Name
			MaterialState.SOLID, //State
			new short[]{180, 180, 20, 0}, //Material Colour
			-1,
			-1,
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
			new short[]{140, 140, 140, 0}, //Material Colour
			1413, //Melting Point in C
			-1,
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
			new short[]{150, 150, 150, 0}, //Material Colour
			1413, //Melting Point in C
			-1,
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
			new short[]{160, 160, 160, 0}, //Material Colour
			1413, //Melting Point in C
			-1,
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

	public static final Material STELLITE = new Material(
			"Stellite", //Material Name
			MaterialState.SOLID, //State
			new short[]{129, 75, 120, 0}, //Material Colour
			1310, //Melting Point in C
			-1,
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
			new short[]{228, 75, 120, 0}, //Material Colour
			1454, //Melting Point in C
			-1,
			-1,
			-1,
			false, //Uses Blast furnace?
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
			new short[]{218, 165, 32, 0}, //Material Colour
			1350, //Melting Point in C
			-1,
			-1,
			-1,
			false, //Uses Blast furnace?
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
			new short[]{255, 193, 37, 0}, //Material Colour
			1350, //Melting Point in C
			-1,
			-1,
			-1,
			false, //Uses Blast furnace?
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
			new short[]{236, 213, 48, 0}, //Material Colour
			1350, //Melting Point in C
			-1,
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
			new short[]{238, 180, 34, 0}, //Material Colour
			1350, //Melting Point in C
			-1,
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
			new short[]{101, 81, 71, 0}, //Material Colour
			1425, //Melting Point in C
			-1,
			-1,
			-1,
			false, //Uses Blast furnace?
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
			new short[]{71, 101, 81, 0}, //Material Colour
			1425, //Melting Point in C
			-1,
			-1,
			-1,
			false, //Uses Blast furnace?
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
			new short[]{81, 71, 101, 0}, //Material Colour
			1425, //Melting Point in C
			-1,
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
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().CARBON, 50),
					new MaterialStack(ELEMENT.getInstance().TUNGSTEN, 50)
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
					new MaterialStack(ELEMENT.getInstance().SILICON, 40),
					new MaterialStack(ELEMENT.getInstance().CARBON, 50),
					new MaterialStack(ELEMENT.getInstance().OXYGEN, 10)
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
					new MaterialStack(ELEMENT.getInstance().TANTALUM, 40),
					new MaterialStack(ELEMENT.getInstance().CARBON, 50),
					new MaterialStack(ELEMENT.getInstance().OXYGEN, 10)
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
					new MaterialStack(ELEMENT.getInstance().ZIRCONIUM, 40),
					new MaterialStack(ELEMENT.getInstance().CARBON, 50),
					new MaterialStack(ELEMENT.getInstance().OXYGEN, 10)
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
					new MaterialStack(ELEMENT.getInstance().NIOBIUM, 40),
					new MaterialStack(ELEMENT.getInstance().CARBON, 50),
					new MaterialStack(ELEMENT.getInstance().OXYGEN, 10)
			});

	public static final Material LEAGRISIUM = new Material(
			"Grisium", //Material Name
			MaterialState.SOLID, //State
			new short[]{53, 93, 106, 0}, //Material Colour
			-1, //Melting Point in C
			-1, //Boiling Point in C
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
			new short[]{139,69,19, 0}, //Material Colour
			-1, //Melting Point in C
			-1, //Boiling Point in C
			-1,
			-1,
			false, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().NICKEL, 5),
					new MaterialStack(ELEMENT.getInstance().ALUMINIUM, 1),
					new MaterialStack(ELEMENT.getInstance().CHROMIUM, 1),
					new MaterialStack(ELEMENT.getInstance().IRON, 23)
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










	//Quantum
	public static final Material QUANTUM = new Material(
			"Quantum", //Material Name
			MaterialState.SOLID, //State
			new short[]{128, 128, 255, 50}, //Material Colour
			9999, //Melting Point in C
			25000, //Boiling Point in C
			150, //Protons
			200, //Neutrons
			true, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ALLOY.STELLITE, 25),
					new MaterialStack(ELEMENT.getInstance().GALLIUM, 5),
					new MaterialStack(ELEMENT.getInstance().AMERICIUM, 5),
					new MaterialStack(ELEMENT.getInstance().PALLADIUM, 5),
					new MaterialStack(ELEMENT.getInstance().BISMUTH, 5)
			});



}
