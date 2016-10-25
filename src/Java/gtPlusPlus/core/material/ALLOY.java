package gtPlusPlus.core.material;


public final class ALLOY {

	public static final Material ENERGYCRYSTAL = new Material(
			"Energy Crystal", //Material Name
			new short[]{228, 255, 0, 0}, //Material Colour
			5660, //Melting Point in C
			0, //Boiling Point in C
			100, //Protons
			100, //Neutrons
			true, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			null);

	public static final Material BLOODSTEEL = new Material(
			"Blood Steel", //Material Name
			new short[]{142, 28, 0, 0}, //Material Colour
			2500, //Melting Point in C
			0, //Boiling Point in C
			100, //Protons
			100, //Neutrons
			false, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			null);

	public static final Material STABALLOY = new Material(
			"Staballoy", //Material Name
			new short[]{68, 75, 66, 0}, //Material Colour
			3450, //Melting Point in C
			((ELEMENT.URANIUM.getBoilingPoint_C()*9)+(ELEMENT.TITANIUM.getBoilingPoint_C()*1))/10, //Boiling Point in C
			((ELEMENT.URANIUM.getProtons()*9)+ELEMENT.TITANIUM.getProtons())/10, //Protons
			((ELEMENT.URANIUM.getNeutrons()*9)+ELEMENT.TITANIUM.getNeutrons())/10, //Neutrons
			true, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.URANIUM, 45),
					new MaterialStack(ELEMENT.URANIUM, 45),
					new MaterialStack(ELEMENT.TITANIUM, 10)
			});

	public static final Material TANTALLOY_60 = new Material(
			"Tantalloy-60", //Material Name
			new short[]{213, 231, 237, 0}, //Material Colour
			3025, //Melting Point in C
			((ELEMENT.TUNGSTEN.getBoilingPoint_C()*1)+(ELEMENT.TANTALUM.getBoilingPoint_C()*8)+(ELEMENT.TITANIUM.getBoilingPoint_C()*1))/10, //Boiling Point in C
			((ELEMENT.TUNGSTEN.getProtons()*1)+(ELEMENT.TANTALUM.getProtons()*8)+(ELEMENT.TITANIUM.getProtons()*1))/10, //Protons
			((ELEMENT.TUNGSTEN.getNeutrons()*1)+(ELEMENT.TANTALUM.getNeutrons()*8)+(ELEMENT.TITANIUM.getNeutrons()*1))/10, //Neutrons
			true, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.TUNGSTEN, 8),
					new MaterialStack(ELEMENT.TANTALUM, 46),
					new MaterialStack(ELEMENT.TANTALUM, 46)
			});

	public static final Material TANTALLOY_61 = new Material(
			"Tantalloy-61", //Material Name
			new short[]{193, 211, 217, 0}, //Material Colour
			3030, //Melting Point in C
			((ELEMENT.TUNGSTEN.getBoilingPoint_C()*1)+(ELEMENT.TANTALUM.getBoilingPoint_C()*7)+(ELEMENT.TITANIUM.getBoilingPoint_C()*1)+(ELEMENT.YTTRIUM.getBoilingPoint_C()*1))/10, //Boiling Point in C
			((ELEMENT.TUNGSTEN.getProtons()*1)+(ELEMENT.TANTALUM.getProtons()*7)+(ELEMENT.TITANIUM.getProtons()*1)+(ELEMENT.YTTRIUM.getProtons()*1))/10, //Protons
			((ELEMENT.TUNGSTEN.getNeutrons()*1)+(ELEMENT.TANTALUM.getNeutrons()*7)+(ELEMENT.TITANIUM.getNeutrons()*1)+(ELEMENT.YTTRIUM.getNeutrons()*1))/10, //Neutrons
			true, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.TUNGSTEN, 20),
					new MaterialStack(ELEMENT.TANTALUM, 60),
					new MaterialStack(ELEMENT.TITANIUM, 12),
					new MaterialStack(ELEMENT.YTTRIUM, 8)
			});

	public static final Material QUANTUM = new Material(
			"Quantum", //Material Name
			new short[]{128, 128, 128, 0}, //Material Colour
			9999, //Melting Point in C
			0, //Boiling Point in C
			100, //Protons
			100, //Neutrons
			true, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			null);

	public static final Material BRONZE = new Material(
			"Bronze", //Material Name
			new short[]{128, 128, 128, 0}, //Material Colour
			((ELEMENT.TIN.getMeltingPoint_C()*1)+(ELEMENT.COPPER.getMeltingPoint_C()*3))/4, //Melting point in C
			((ELEMENT.TIN.getBoilingPoint_C()*1)+(ELEMENT.COPPER.getBoilingPoint_C()*3))/4, //Boiling Point in C
			((ELEMENT.TIN.getProtons()*1)+(ELEMENT.COPPER.getProtons()*3))/4, //Protons
			((ELEMENT.TIN.getNeutrons()*1)+(ELEMENT.COPPER.getNeutrons()*3))/4, //Neutrons
			false, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.COPPER, 35),
					new MaterialStack(ELEMENT.COPPER, 40),
					new MaterialStack(ELEMENT.TIN, 25)
			});

	public static final Material TUMBAGA = new Material(
			"Tumbaga", //Material Name
			new short[]{255,178,15, 0}, //Material Colour
			((ELEMENT.GOLD.getMeltingPoint_C()*7)+(ELEMENT.COPPER.getMeltingPoint_C()*3))/10, //Melting point in C
			((ELEMENT.GOLD.getBoilingPoint_C()*7)+(ELEMENT.COPPER.getBoilingPoint_C()*3))/10, //Boiling Point in C
			((ELEMENT.GOLD.getProtons()*7)+(ELEMENT.COPPER.getProtons()*3))/10, //Protons
			((ELEMENT.GOLD.getNeutrons()*7)+(ELEMENT.COPPER.getNeutrons()*3))/10, //Neutrons
			false, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.GOLD, 30),
					new MaterialStack(ELEMENT.GOLD, 40),
					new MaterialStack(ELEMENT.COPPER, 30)
			});

	public static final Material POTIN = new Material(
			"Potin", //Material Name
			new short[]{201,151,129, 0}, //Material Colour
			((ELEMENT.LEAD.getMeltingPoint_C()*4)+(ALLOY.BRONZE.getMeltingPoint_C()*4)+(ELEMENT.TIN.getMeltingPoint_C()*2))/10, //Melting point in C
			((ELEMENT.LEAD.getBoilingPoint_C()*4)+(ALLOY.BRONZE.getBoilingPoint_C()*4)+(ELEMENT.TIN.getBoilingPoint_C()*2))/10, //Boiling Point in C
			((ELEMENT.LEAD.getProtons()*4)+(ALLOY.BRONZE.getProtons()*4)+(ELEMENT.TIN.getProtons()*2))/10, //Protons
			((ELEMENT.LEAD.getNeutrons()*4)+(ALLOY.BRONZE.getNeutrons()*4)+(ELEMENT.TIN.getNeutrons()*2))/10, //Neutrons
			false, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.LEAD, 40),
					new MaterialStack(ALLOY.BRONZE, 40),
					new MaterialStack(ELEMENT.TIN, 20)
			});

	public static final Material BEDROCKIUM = new Material(
			"Bedrockium", //Material Name
			new short[]{32, 32, 32, 0}, //Material Colour
			7735, //Melting Point in C
			0, //Boiling Point in C
			100, //Protons
			100, //Neutrons
			false, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			null);

	public static final Material INCONEL_625 = new Material(
			"Inconel-625", //Material Name
			new short[]{128, 200, 128, 0}, //Material Colour
			1425, //Melting Point in C
			((ELEMENT.NICKEL.getBoilingPoint_C()*6)+(ELEMENT.CHROMIUM.getBoilingPoint_C()*2)+(ELEMENT.IRON.getBoilingPoint_C()*1)+(ELEMENT.MOLYBDENUM.getBoilingPoint_C()*1))/10, //Boiling Point in C
			((ELEMENT.NICKEL.getProtons()*6)+(ELEMENT.CHROMIUM.getProtons()*2)+(ELEMENT.IRON.getProtons()*1)+(ELEMENT.MOLYBDENUM.getProtons()*1))/10, //Protons
			((ELEMENT.NICKEL.getNeutrons()*6)+(ELEMENT.CHROMIUM.getNeutrons()*2)+(ELEMENT.IRON.getNeutrons()*1)+(ELEMENT.MOLYBDENUM.getNeutrons()*1))/10, //Neutrons
			true, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.NICKEL, 60),
					new MaterialStack(ELEMENT.CHROMIUM, 20),
					new MaterialStack(ELEMENT.IRON, 10),
					new MaterialStack(ELEMENT.MOLYBDENUM, 10)
			});

	public static final Material INCONEL_690 = new Material(
			"Inconel-690", //Material Name
			new short[]{118, 220, 138, 0}, //Material Colour
			1425, //Melting Point in C
			((ELEMENT.NICKEL.getBoilingPoint_C()*6)+(ELEMENT.CHROMIUM.getBoilingPoint_C()*2)+(ELEMENT.NIOBIUM.getBoilingPoint_C()*1)+(ELEMENT.MOLYBDENUM.getBoilingPoint_C()*1))/10, //Boiling Point in C
			((ELEMENT.NICKEL.getProtons()*6)+(ELEMENT.CHROMIUM.getProtons()*2)+(ELEMENT.NIOBIUM.getProtons()*1)+(ELEMENT.MOLYBDENUM.getProtons()*1))/10, //Protons
			((ELEMENT.NICKEL.getNeutrons()*6)+(ELEMENT.CHROMIUM.getNeutrons()*2)+(ELEMENT.NIOBIUM.getNeutrons()*1)+(ELEMENT.MOLYBDENUM.getNeutrons()*1))/10, //Neutrons
			true, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.NICKEL, 60),
					new MaterialStack(ELEMENT.CHROMIUM, 20),
					new MaterialStack(ELEMENT.NIOBIUM, 10),
					new MaterialStack(ELEMENT.MOLYBDENUM, 10)
			});

	public static final Material INCONEL_792 = new Material(
			"Inconel-792", //Material Name
			new short[]{108, 240, 118, 0}, //Material Colour
			1425, //Melting Point in C
			((ELEMENT.NICKEL.getBoilingPoint_C()*6)+(ELEMENT.CHROMIUM.getBoilingPoint_C()*1)+(ELEMENT.IRON.getBoilingPoint_C()*1)+(ELEMENT.ALUMINIUM.getBoilingPoint_C()*2))/10, //Boiling Point in C
			((ELEMENT.NICKEL.getProtons()*6)+(ELEMENT.CHROMIUM.getProtons()*1)+(ELEMENT.IRON.getProtons()*1)+(ELEMENT.ALUMINIUM.getProtons()*2))/10, //Protons
			((ELEMENT.NICKEL.getNeutrons()*6)+(ELEMENT.CHROMIUM.getNeutrons()*1)+(ELEMENT.IRON.getNeutrons()*1)+(ELEMENT.ALUMINIUM.getNeutrons()*2))/10, //Neutrons
			true, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.NICKEL, 60),
					new MaterialStack(ELEMENT.CHROMIUM, 10),
					new MaterialStack(ELEMENT.NIOBIUM, 10),
					new MaterialStack(ELEMENT.ALUMINIUM, 20)
			});
	
	public static final Material STEEL = new Material(
			"Steel", //Material Name
			new short[]{180, 180, 20, 0}, //Material Colour
			((ELEMENT.CARBON.getMeltingPoint_C()*5)+(ELEMENT.IRON.getMeltingPoint_C()*95))/100, //Melting point in C
			((ELEMENT.CARBON.getBoilingPoint_C()*5)+(ELEMENT.IRON.getBoilingPoint_C()*95))/100, //Boiling Point in C
			((ELEMENT.CARBON.getProtons()*5)+(ELEMENT.IRON.getProtons()*95))/100, //Protons
			((ELEMENT.CARBON.getNeutrons()*5)+(ELEMENT.IRON.getNeutrons()*95))/100, //Neutrons
			true, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.CARBON, 10),
					new MaterialStack(ELEMENT.IRON, 30),
					new MaterialStack(ELEMENT.IRON, 30),
					new MaterialStack(ELEMENT.IRON, 30)
			});

	public static final Material ZERON_100 = new Material(
			"Zeron-100", //Material Name
			new short[]{180, 180, 20, 0}, //Material Colour
			((ELEMENT.CHROMIUM.getMeltingPoint_C()*25)+(ELEMENT.NICKEL.getMeltingPoint_C()*6)+(ELEMENT.COBALT.getMeltingPoint_C()*9)+(ALLOY.STEEL.getMeltingPoint_C()*60))/100, //Melting Point in C
			((ELEMENT.CHROMIUM.getBoilingPoint_C()*25)+(ELEMENT.NICKEL.getBoilingPoint_C()*6)+(ELEMENT.COBALT.getBoilingPoint_C()*9)+(ALLOY.STEEL.getBoilingPoint_C()*60))/100, //Boiling Point in C
			((ELEMENT.CHROMIUM.getProtons()*25)+(ELEMENT.NICKEL.getProtons()*6)+(ELEMENT.COBALT.getProtons()*9)+(ALLOY.STEEL.getProtons()*60))/100, //Protons
			((ELEMENT.CHROMIUM.getNeutrons()*25)+(ELEMENT.NICKEL.getNeutrons()*6)+(ELEMENT.COBALT.getNeutrons()*9)+(ALLOY.STEEL.getNeutrons()*60))/100, //Neutrons
			true, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.CHROMIUM, 25),
					new MaterialStack(ELEMENT.NICKEL, 6),
					new MaterialStack(ELEMENT.COBALT, 9),
					new MaterialStack(ALLOY.STEEL, 60)
					});

	public static final Material MARAGING250 = new Material(
			"Maraging Steel 250", //Material Name
			new short[]{140, 140, 140, 0}, //Material Colour
			1413, //Melting Point in C
			((ELEMENT.TITANIUM.getBoilingPoint_C()*5)+(ELEMENT.NICKEL.getBoilingPoint_C()*16)+(ELEMENT.COBALT.getBoilingPoint_C()*9)+(ALLOY.STEEL.getBoilingPoint_C()*70))/100, //Boiling Point in C
			((ELEMENT.TITANIUM.getProtons()*5)+(ELEMENT.NICKEL.getProtons()*16)+(ELEMENT.COBALT.getProtons()*9)+(ALLOY.STEEL.getProtons()*70))/100, //Protons
			((ELEMENT.TITANIUM.getNeutrons()*5)+(ELEMENT.NICKEL.getNeutrons()*16)+(ELEMENT.COBALT.getNeutrons()*9)+(ALLOY.STEEL.getNeutrons()*70))/100, //Neutrons
			true, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.TITANIUM, 5),
					new MaterialStack(ELEMENT.NICKEL, 25),
					new MaterialStack(ELEMENT.COBALT, 10),
					new MaterialStack(ALLOY.STEEL, 60)
			});

	public static final Material MARAGING300 = new Material(
			"Maraging Steel 300", //Material Name
			new short[]{150, 150, 150, 0}, //Material Colour
			1413, //Melting Point in C
			((ELEMENT.TITANIUM.getBoilingPoint_C()*10)+(ELEMENT.NICKEL.getBoilingPoint_C()*21)+(ELEMENT.COBALT.getBoilingPoint_C()*14)+(ALLOY.STEEL.getBoilingPoint_C()*55))/100, //Boiling Point in C
			((ELEMENT.TITANIUM.getProtons()*10)+(ELEMENT.NICKEL.getProtons()*21)+(ELEMENT.COBALT.getProtons()*14)+(ALLOY.STEEL.getProtons()*55))/100, //Protons
			((ELEMENT.TITANIUM.getNeutrons()*10)+(ELEMENT.NICKEL.getNeutrons()*21)+(ELEMENT.COBALT.getNeutrons()*14)+(ALLOY.STEEL.getNeutrons()*55))/100, //Neutrons
			true, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.TITANIUM, 10),
					new MaterialStack(ELEMENT.NICKEL, 20),
					new MaterialStack(ELEMENT.COBALT, 15),
					new MaterialStack(ALLOY.STEEL, 55)
			});

	public static final Material MARAGING350 = new Material(
			"Maraging Steel 350", //Material Name
			new short[]{160, 160, 160, 0}, //Material Colour
			1413, //Melting Point in C
			((ELEMENT.TITANIUM.getBoilingPoint_C()*15)+(ELEMENT.NICKEL.getBoilingPoint_C()*21)+(ELEMENT.COBALT.getBoilingPoint_C()*9)+(ALLOY.STEEL.getBoilingPoint_C()*55))/100, //Boiling Point in C
			((ELEMENT.TITANIUM.getProtons()*15)+(ELEMENT.NICKEL.getProtons()*21)+(ELEMENT.COBALT.getProtons()*9)+(ALLOY.STEEL.getProtons()*55))/100, //Protons
			((ELEMENT.TITANIUM.getNeutrons()*15)+(ELEMENT.NICKEL.getNeutrons()*21)+(ELEMENT.COBALT.getNeutrons()*9)+(ALLOY.STEEL.getNeutrons()*55))/100, //Neutrons
			true, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.TITANIUM, 15),
					new MaterialStack(ELEMENT.NICKEL, 20),
					new MaterialStack(ELEMENT.COBALT, 10),
					new MaterialStack(ALLOY.STEEL, 55)
			});

	public static final Material STELLITE = new Material(
			"Stellite", //Material Name
			new short[]{129, 75, 120, 0}, //Material Colour
			1310, //Melting Point in C
			((ELEMENT.TITANIUM.getBoilingPoint_C()*10)+(ELEMENT.CHROMIUM.getBoilingPoint_C()*35)+(ELEMENT.COBALT.getBoilingPoint_C()*35)+(ELEMENT.MANGANESE.getBoilingPoint_C()*20))/100, //Boiling Point in C
			((ELEMENT.TITANIUM.getProtons()*10)+(ELEMENT.CHROMIUM.getProtons()*35)+(ELEMENT.COBALT.getProtons()*35)+(ELEMENT.MANGANESE.getProtons()*20))/100, //Protons
			((ELEMENT.TITANIUM.getNeutrons()*10)+(ELEMENT.CHROMIUM.getNeutrons()*35)+(ELEMENT.COBALT.getNeutrons()*35)+(ELEMENT.MANGANESE.getNeutrons()*20))/100, //Neutrons
			true, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.COBALT, 35),
					new MaterialStack(ELEMENT.CHROMIUM, 35),
					new MaterialStack(ELEMENT.MANGANESE, 20),
					new MaterialStack(ELEMENT.TITANIUM, 10)
			});

	public static final Material TALONITE = new Material(
			"Talonite", //Material Name
			new short[]{228, 75, 120, 0}, //Material Colour
			1454, //Melting Point in C
			((ELEMENT.MOLYBDENUM.getBoilingPoint_C()*10)+(ELEMENT.CHROMIUM.getBoilingPoint_C()*30)+(ELEMENT.COBALT.getBoilingPoint_C()*40)+(ELEMENT.PHOSPHORUS.getBoilingPoint_C()*20))/100, //Boiling Point in C
			((ELEMENT.MOLYBDENUM.getProtons()*10)+(ELEMENT.CHROMIUM.getProtons()*30)+(ELEMENT.COBALT.getProtons()*40)+(ELEMENT.PHOSPHORUS.getProtons()*20))/100, //Protons
			((ELEMENT.MOLYBDENUM.getNeutrons()*10)+(ELEMENT.CHROMIUM.getNeutrons()*30)+(ELEMENT.COBALT.getNeutrons()*40)+(ELEMENT.PHOSPHORUS.getNeutrons()*20))/100, //Neutrons
			false, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.COBALT, 40),
					new MaterialStack(ELEMENT.CHROMIUM, 30),
					new MaterialStack(ELEMENT.PHOSPHORUS, 20),
					new MaterialStack(ELEMENT.MOLYBDENUM, 10)
			});

	public static final Material HASTELLOY_W = new Material(
			"Hastelloy-W", //Material Name
			new short[]{218, 165, 32, 0}, //Material Colour
			1350, //Melting Point in C
			((ELEMENT.IRON.getBoilingPoint_C()*6)+(ELEMENT.MOLYBDENUM.getBoilingPoint_C()*24)+(ELEMENT.CHROMIUM.getBoilingPoint_C()*5)+(ELEMENT.NICKEL.getBoilingPoint_C()*65))/100, //Boiling Point in C
			((ELEMENT.IRON.getProtons()*6)+(ELEMENT.MOLYBDENUM.getProtons()*24)+(ELEMENT.CHROMIUM.getProtons()*5)+(ELEMENT.NICKEL.getProtons()*65))/100, //Protons
			((ELEMENT.IRON.getNeutrons()*6)+(ELEMENT.MOLYBDENUM.getNeutrons()*24)+(ELEMENT.CHROMIUM.getNeutrons()*5)+(ELEMENT.NICKEL.getNeutrons()*65))/100, //Neutrons
			false, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.IRON, 06),
					new MaterialStack(ELEMENT.MOLYBDENUM, 24),
					new MaterialStack(ELEMENT.CHROMIUM, 8),
					new MaterialStack(ELEMENT.NICKEL, 62)
			});

	/*public static final Material HASTELLOY_X = new Material(
			"Hastelloy-X", //Material Name
			new short[]{255, 193, 37, 0}, //Material Colour
			1350, //Melting Point in C
			((ELEMENT.IRON.getBoilingPoint_C()*18)+(ELEMENT.MOLYBDENUM.getBoilingPoint_C()*9)+(ELEMENT.CHROMIUM.getBoilingPoint_C()*22)+(ELEMENT.NICKEL.getBoilingPoint_C()*51))/100, //Boiling Point in C
			((ELEMENT.IRON.getProtons()*18)+(ELEMENT.MOLYBDENUM.getProtons()*9)+(ELEMENT.CHROMIUM.getProtons()*22)+(ELEMENT.NICKEL.getProtons()*51))/100, //Protons
			((ELEMENT.IRON.getNeutrons()*18)+(ELEMENT.MOLYBDENUM.getNeutrons()*9)+(ELEMENT.CHROMIUM.getNeutrons()*22)+(ELEMENT.NICKEL.getNeutrons()*51))/100, //Neutrons
			false, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.IRON, 18),
					new MaterialStack(ELEMENT.MOLYBDENUM, 9),
					new MaterialStack(ELEMENT.CHROMIUM, 22),
					new MaterialStack(ELEMENT.NICKEL, 51)
			});*/
	
	public static final Material HASTELLOY_X = new Material(
			"Hastelloy-X", //Material Name
			new short[]{255, 193, 37, 0}, //Material Colour
			1350, //Melting Point in C
			((ELEMENT.IRON.getBoilingPoint_C()*18)+(ELEMENT.MOLYBDENUM.getBoilingPoint_C()*9)+(ELEMENT.CHROMIUM.getBoilingPoint_C()*22)+(ELEMENT.NICKEL.getBoilingPoint_C()*51))/100, //Boiling Point in C
			((ELEMENT.IRON.getProtons()*18)+(ELEMENT.MOLYBDENUM.getProtons()*9)+(ELEMENT.CHROMIUM.getProtons()*22)+(ELEMENT.NICKEL.getProtons()*51))/100, //Protons
			((ELEMENT.IRON.getNeutrons()*18)+(ELEMENT.MOLYBDENUM.getNeutrons()*9)+(ELEMENT.CHROMIUM.getNeutrons()*22)+(ELEMENT.NICKEL.getNeutrons()*51))/100, //Neutrons
			false, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.IRON, 18),
					new MaterialStack(ELEMENT.MOLYBDENUM, 9),
					new MaterialStack(ELEMENT.CHROMIUM, 22),
					new MaterialStack(ELEMENT.NICKEL, 51)
			});

	public static final Material HASTELLOY_N = new Material(
			"Hastelloy-N", //Material Name
			new short[]{236, 213, 48, 0}, //Material Colour
			1350, //Melting Point in C
			((ELEMENT.YTTRIUM.getBoilingPoint_C()*5)+(ELEMENT.MOLYBDENUM.getBoilingPoint_C()*16)+(ELEMENT.CHROMIUM.getBoilingPoint_C()*7)+(ELEMENT.NICKEL.getBoilingPoint_C()*72))/100, //Boiling Point in C
			((ELEMENT.YTTRIUM.getProtons()*5)+(ELEMENT.MOLYBDENUM.getProtons()*16)+(ELEMENT.CHROMIUM.getProtons()*7)+(ELEMENT.NICKEL.getProtons()*72))/100, //Protons
			((ELEMENT.YTTRIUM.getNeutrons()*5)+(ELEMENT.MOLYBDENUM.getNeutrons()*16)+(ELEMENT.CHROMIUM.getNeutrons()*7)+(ELEMENT.NICKEL.getNeutrons()*72))/100, //Neutrons
			true, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.YTTRIUM, 10),
					new MaterialStack(ELEMENT.MOLYBDENUM, 16),
					new MaterialStack(ELEMENT.CHROMIUM, 10),
					new MaterialStack(ELEMENT.NICKEL, 64)
			});

	public static final Material HASTELLOY_C276 = new Material(
			"Hastelloy-C276", //Material Name
			new short[]{238, 180, 34, 0}, //Material Colour
			1350, //Melting Point in C
			((ELEMENT.COBALT.getBoilingPoint_C()*2)+(ELEMENT.MOLYBDENUM.getBoilingPoint_C()*16)+(ELEMENT.CHROMIUM.getBoilingPoint_C()*16)+(ELEMENT.NICKEL.getBoilingPoint_C()*66))/100, //Boiling Point in C
			((ELEMENT.COBALT.getProtons()*2)+(ELEMENT.MOLYBDENUM.getProtons()*16)+(ELEMENT.CHROMIUM.getProtons()*16)+(ELEMENT.NICKEL.getProtons()*66))/100, //Protons
			((ELEMENT.COBALT.getNeutrons()*2)+(ELEMENT.MOLYBDENUM.getNeutrons()*16)+(ELEMENT.CHROMIUM.getNeutrons()*16)+(ELEMENT.NICKEL.getNeutrons()*66))/100, //Neutrons
			true, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.COBALT, 02),
					new MaterialStack(ELEMENT.MOLYBDENUM, 16),
					new MaterialStack(ELEMENT.CHROMIUM, 16),
					new MaterialStack(ELEMENT.NICKEL, 66)
			});

	public static final Material INCOLOY_020 = new Material(
			"Incoloy-020", //Material Name
			new short[]{101, 81, 71, 0}, //Material Colour
			1425, //Melting Point in C
			((ELEMENT.IRON.getBoilingPoint_C()*40)+(ELEMENT.COPPER.getBoilingPoint_C()*4)+(ELEMENT.CHROMIUM.getBoilingPoint_C()*20)+(ELEMENT.NICKEL.getBoilingPoint_C()*36))/100, //Boiling Point in C
			((ELEMENT.IRON.getProtons()*40)+(ELEMENT.COPPER.getProtons()*4)+(ELEMENT.CHROMIUM.getProtons()*20)+(ELEMENT.NICKEL.getProtons()*36))/100, //Protons
			((ELEMENT.IRON.getNeutrons()*40)+(ELEMENT.COPPER.getNeutrons()*4)+(ELEMENT.CHROMIUM.getNeutrons()*20)+(ELEMENT.NICKEL.getNeutrons()*36))/100, //Neutrons
			false, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.IRON, 40),
					new MaterialStack(ELEMENT.COPPER, 4),
					new MaterialStack(ELEMENT.CHROMIUM, 20),
					new MaterialStack(ELEMENT.NICKEL, 36)
			});

	public static final Material INCOLOY_DS = new Material(
			"Incoloy-DS", //Material Name
			new short[]{71, 101, 81, 0}, //Material Colour
			1425, //Melting Point in C
			((ELEMENT.IRON.getBoilingPoint_C()*46)+(ELEMENT.COBALT.getBoilingPoint_C()*18)+(ELEMENT.CHROMIUM.getBoilingPoint_C()*18)+(ELEMENT.NICKEL.getBoilingPoint_C()*18))/100, //Boiling Point in C
			((ELEMENT.IRON.getProtons()*46)+(ELEMENT.COBALT.getProtons()*18)+(ELEMENT.CHROMIUM.getProtons()*18)+(ELEMENT.NICKEL.getProtons()*18))/100, //Protons
			((ELEMENT.IRON.getNeutrons()*46)+(ELEMENT.COBALT.getNeutrons()*18)+(ELEMENT.CHROMIUM.getNeutrons()*18)+(ELEMENT.NICKEL.getNeutrons()*18))/100, //Neutrons
			false, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.IRON, 46),
					new MaterialStack(ELEMENT.COBALT, 18),
					new MaterialStack(ELEMENT.CHROMIUM, 18),
					new MaterialStack(ELEMENT.NICKEL, 18)
			});

	public static final Material INCOLOY_MA956 = new Material(
			"Incoloy-MA956", //Material Name
			new short[]{81, 71, 101, 0}, //Material Colour
			1425, //Melting Point in C
			((ELEMENT.IRON.getBoilingPoint_C()*75)+(ELEMENT.ALUMINIUM.getBoilingPoint_C()*4)+(ELEMENT.CHROMIUM.getBoilingPoint_C()*20)+(ELEMENT.YTTRIUM.getBoilingPoint_C()*1))/100, //Boiling Point in C
			((ELEMENT.IRON.getProtons()*75)+(ELEMENT.ALUMINIUM.getProtons()*4)+(ELEMENT.CHROMIUM.getProtons()*20)+(ELEMENT.YTTRIUM.getProtons()*1))/100, //Protons
			((ELEMENT.IRON.getNeutrons()*75)+(ELEMENT.ALUMINIUM.getNeutrons()*4)+(ELEMENT.CHROMIUM.getNeutrons()*20)+(ELEMENT.YTTRIUM.getNeutrons()*1))/100, //Neutrons
			true, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.IRON, 64),
					new MaterialStack(ELEMENT.ALUMINIUM, 12),
					new MaterialStack(ELEMENT.CHROMIUM, 20),
					new MaterialStack(ELEMENT.YTTRIUM, 4)
			});

	public static final Material TUNGSTEN_CARBIDE = new Material(
			"Tungsten Carbide", //Material Name
			new short[]{44, 44, 44, 0}, //Material Colour
			3422, //Melting Point in C
			((ELEMENT.TUNGSTEN.getBoilingPoint_C()*5)+(ELEMENT.CARBON.getBoilingPoint_C()*5))/10, //Boiling Point in C
			((ELEMENT.TUNGSTEN.getProtons()*5)+(ELEMENT.CARBON.getProtons()*5))/10, //Protons
			((ELEMENT.TUNGSTEN.getNeutrons()*5)+(ELEMENT.CARBON.getNeutrons()*5))/10, //Neutrons
			true, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.CARBON, 50),
					new MaterialStack(ELEMENT.TUNGSTEN, 50)
			});

	public static final Material SILICON_CARBIDE = new Material(
			"Silicon Carbide", //Material Name
			new short[]{40, 48, 36, 0}, //Material Colour
			1414, //Melting Point in C
			((ELEMENT.SILICON.getBoilingPoint_C()*5)+(ELEMENT.CARBON.getBoilingPoint_C()*5))/10, //Boiling Point in C
			((ELEMENT.SILICON.getProtons()*5)+(ELEMENT.CARBON.getProtons()*5))/10, //Protons
			((ELEMENT.SILICON.getNeutrons()*5)+(ELEMENT.CARBON.getNeutrons()*5))/10, //Neutrons
			false, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.CARBON, 50),
					new MaterialStack(ELEMENT.SILICON, 50)
			});

	public static final Material TANTALUM_CARBIDE = new Material(
			"Tantalum Carbide", //Material Name
			new short[]{139, 136, 120, 0}, //Material Colour
			2980, //Melting Point in C
			((ELEMENT.TANTALUM.getBoilingPoint_C()*5)+(ELEMENT.CARBON.getBoilingPoint_C()*5))/10, //Boiling Point in C
			((ELEMENT.TANTALUM.getProtons()*5)+(ELEMENT.CARBON.getProtons()*5))/10, //Protons
			((ELEMENT.TANTALUM.getNeutrons()*5)+(ELEMENT.CARBON.getNeutrons()*5))/10, //Neutrons
			true, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.CARBON, 50),
					new MaterialStack(ELEMENT.TANTALUM, 50)
			});

	public static final Material ZIRCONIUM_CARBIDE = new Material(
			"Zirconium Carbide", //Material Name
			new short[]{222, 202, 180, 0}, //Material Colour
			1855, //Melting Point in C
			((ELEMENT.ZIRCONIUM.getBoilingPoint_C()*5)+(ELEMENT.CARBON.getBoilingPoint_C()*5))/10, //Boiling Point in C
			((ELEMENT.ZIRCONIUM.getProtons()*5)+(ELEMENT.CARBON.getProtons()*5))/10, //Protons
			((ELEMENT.ZIRCONIUM.getNeutrons()*5)+(ELEMENT.CARBON.getNeutrons()*5))/10, //Neutrons
			true, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.CARBON, 50),
					new MaterialStack(ELEMENT.ZIRCONIUM, 50)
			});

	public static final Material NIOBIUM_CARBIDE = new Material(
			"Niobium Carbide", //Material Name
			new short[]{205, 197, 191, 0}, //Material Colour
			2477, //Melting Point in C
			((ELEMENT.NIOBIUM.getBoilingPoint_C()*5)+(ELEMENT.CARBON.getBoilingPoint_C()*5))/10, //Boiling Point in C
			((ELEMENT.NIOBIUM.getProtons()*5)+(ELEMENT.CARBON.getProtons()*5))/10, //Protons
			((ELEMENT.NIOBIUM.getNeutrons()*5)+(ELEMENT.CARBON.getNeutrons()*5))/10, //Neutrons
			true, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.CARBON, 50),
					new MaterialStack(ELEMENT.NIOBIUM, 50)
			});
	
	
	public static final Material LEAGRISIUM = new Material(
			"Grisium", //Material Name
			new short[]{53, 93, 106, 0}, //Material Colour
			9001, //Melting Point in C
			25000, //Boiling Point in C
			96, //Protons
			128, //Neutrons
			true, //Uses Blast furnace?
			new MaterialStack[]{
					new MaterialStack(ELEMENT.NICKEL, 30),
					new MaterialStack(ELEMENT.CHROMIUM, 10),
					new MaterialStack(ELEMENT.ZIRCONIUM, 20),
					new MaterialStack(ELEMENT.IRON, 30),
					new MaterialStack(ELEMENT.TUNGSTEN, 10)
			});	//Material Stacks with Percentage of required elements.


}
