package gtPlusPlus.core.material;

import gtPlusPlus.core.material.state.MaterialState;

public final class ORES {

	public static final Material GEIKIELITE = new Material(
			"Geikielite", //Material Name
			MaterialState.ORE, //State
			new short[]{187, 193, 204, 0}, //Material Colour
			500,
			1500,
			50,
			75,
			0, //Radiation
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().MAGNESIUM, 1),
					new MaterialStack(ELEMENT.getInstance().TITANIUM, 1),
					new MaterialStack(ELEMENT.getInstance().OXYGEN, 3)
			});	

	public static final Material ZIMBABWEITE = new Material(
			"Zimbabweite", //Material Name
			MaterialState.ORE, //State
			new short[]{193, 187, 131, 0}, //Material Colour
			500,
			1500,
			50,
			75,
			0, //Radiation
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().CALCIUM, 2),
					new MaterialStack(ELEMENT.getInstance().POTASSIUM, 2),
					new MaterialStack(ELEMENT.getInstance().LEAD, 1),
					new MaterialStack(ELEMENT.getInstance().ARSENIC, 4),
					new MaterialStack(ELEMENT.getInstance().NIOBIUM, 4),
					new MaterialStack(ELEMENT.getInstance().TANTALUM, 4),
					new MaterialStack(ELEMENT.getInstance().TITANIUM, 4),
					new MaterialStack(ELEMENT.getInstance().OXYGEN, 18)
			});	

	public static final Material TITANITE = new Material(
			"Titanite", //Material Name
			MaterialState.ORE, //State
			new short[]{184, 198, 105, 0}, //Material Colour
			500,
			1500,
			50,
			75,
			0, //Radiation
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().CALCIUM, 2),
					new MaterialStack(ELEMENT.getInstance().TITANIUM, 2),
					new MaterialStack(ELEMENT.getInstance().SILICON, 2),
					new MaterialStack(ELEMENT.getInstance().THORIUM, 1),
					new MaterialStack(ELEMENT.getInstance().OXYGEN, 10)
			});	

	public static final Material ZIRCONILITE = new Material(
			"Zirconolite", //Material Name
			MaterialState.ORE, //State
			new short[]{45, 26, 0, 0}, //Material Colour
			500,
			1500,
			50,
			75,
			0, //Radiation
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().CALCIUM, 2),
					new MaterialStack(ELEMENT.getInstance().ZIRCONIUM, 2),
					new MaterialStack(ELEMENT.getInstance().TITANIUM, 4),
					new MaterialStack(ELEMENT.getInstance().CERIUM, 1),
					new MaterialStack(ELEMENT.getInstance().OXYGEN, 14)
			});	

	public static final Material CROCROITE = new Material(
			"Crocoite", //Material Name
			MaterialState.ORE, //State
			new short[]{255, 143, 84, 0}, //Material Colour
			500,
			1500,
			50,
			75,
			0, //Radiation
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().LEAD, 1),
					new MaterialStack(ELEMENT.getInstance().CHROMIUM, 1),
					new MaterialStack(ELEMENT.getInstance().OXYGEN, 4)
			});	

	public static final Material NICHROMITE = new Material(
			"Nichromite", //Material Name
			MaterialState.ORE, //State
			new short[]{22, 19, 19, 0}, //Material Colour
			500,
			1500,
			50,
			75,
			0, //Radiation
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().NICKEL, 1),
					new MaterialStack(ELEMENT.getInstance().COBALT, 1),
					new MaterialStack(ELEMENT.getInstance().IRON, 3),
					new MaterialStack(ELEMENT.getInstance().ALUMINIUM, 2),
					new MaterialStack(ELEMENT.getInstance().CHROMIUM, 2),
					new MaterialStack(ELEMENT.getInstance().OXYGEN, 8)
			});	

	public static final Material YTTRIAITE = new Material(
			"Yttriaite", //Material Name
			MaterialState.ORE, //State
			new short[]{255, 143, 84, 0}, //Material Colour
			500,
			1500,
			50,
			75,
			0, //Radiation
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().YTTRIUM, 1), //Y not YT/YB
					new MaterialStack(ELEMENT.getInstance().OXYGEN, 3)
			});	

	//Samarskite_Y
	public static final Material SAMARSKITE_Y = new Material(
			"Samarskite (Y)", //Material Name
			MaterialState.ORE, //State
			new short[]{65, 163, 164, 0}, //Material Colour
			500,
			1500,
			50,
			75,
			1, //Radiation
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().YTTRIUM, 2), //Y not YT/YB
					new MaterialStack(ELEMENT.getInstance().IRON, 10),
					new MaterialStack(ELEMENT.getInstance().URANIUM235, 2),
					new MaterialStack(ELEMENT.getInstance().THORIUM, 3),
					new MaterialStack(ELEMENT.getInstance().NIOBIUM, 2),
					new MaterialStack(ELEMENT.getInstance().TANTALUM, 3)
			});

	//Samarskite_YB
	public static final Material SAMARSKITE_YB = new Material(
			"Samarskite (Yb)", //Material Name
			MaterialState.ORE, //State
			new short[]{95, 193, 194, 0}, //Material Colour
			500,
			1500,
			50,
			75,
			1, //Radiation
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().YTTERBIUM, 2), //Y not YT/YB
					new MaterialStack(ELEMENT.getInstance().IRON, 9),
					new MaterialStack(ELEMENT.getInstance().URANIUM235, 3),
					new MaterialStack(ELEMENT.getInstance().THORIUM, 2),
					new MaterialStack(ELEMENT.getInstance().NIOBIUM, 3),
					new MaterialStack(ELEMENT.getInstance().TANTALUM, 2)
			});

	public static final Material ZIRCON = new Material(
			"Zircon", //Material Name
			MaterialState.ORE, //State
			new short[]{195, 19, 19, 0}, //Material Colour
			500,
			1500,
			50,
			75,
			0, //Radiation
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().ZIRCONIUM, 1),
					new MaterialStack(ELEMENT.getInstance().SILICON, 1),
					new MaterialStack(ELEMENT.getInstance().OXYGEN, 4),
			});

	//Gadolinite_Ce
	public static final Material GADOLINITE_CE = new Material(
			"Gadolinite (Ce)", //Material Name
			MaterialState.ORE, //State
			new short[]{15, 159, 59, 0}, //Material Colour
			500,
			1500,
			50,
			75,
			0, //Radiation
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().CERIUM, 4),
					new MaterialStack(ELEMENT.getInstance().LANTHANUM, 2),
					new MaterialStack(ELEMENT.getInstance().NEODYMIUM, 2),
					new MaterialStack(ELEMENT.getInstance().YTTRIUM, 2),
					new MaterialStack(ELEMENT.getInstance().IRON, 1),
					new MaterialStack(ELEMENT.getInstance().BERYLLIUM, 2),
					new MaterialStack(ELEMENT.getInstance().SILICON, 7),
					new MaterialStack(ELEMENT.getInstance().OXYGEN, 14),
			});

	//Gadolinite_Y
	public static final Material GADOLINITE_Y = new Material(
			"Gadolinite (Y)", //Material Name
			MaterialState.ORE, //State
			new short[]{35, 189, 99, 0}, //Material Colour
			500,
			1500,
			50,
			75,
			0, //Radiation
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().CERIUM, 2),
					new MaterialStack(ELEMENT.getInstance().LANTHANUM, 2),
					new MaterialStack(ELEMENT.getInstance().NEODYMIUM, 2),
					new MaterialStack(ELEMENT.getInstance().YTTRIUM, 4),
					new MaterialStack(ELEMENT.getInstance().IRON, 2),
					new MaterialStack(ELEMENT.getInstance().BERYLLIUM, 3),
					new MaterialStack(ELEMENT.getInstance().SILICON, 4),
					new MaterialStack(ELEMENT.getInstance().OXYGEN, 9),
			});

	public static final Material LEPERSONNITE = new Material(
			"Lepersonnite", //Material Name
			MaterialState.ORE, //State
			new short[]{175, 175, 20, 0}, //Material Colour
			500,
			1500,
			50,
			75,
			0, //Radiation
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().CALCIUM, 1),
					new MaterialStack(ELEMENT.getInstance().GADOLINIUM, 2),
					new MaterialStack(ELEMENT.getInstance().DYSPROSIUM, 2),
					new MaterialStack(ELEMENT.getInstance().URANIUM235, 2),
					new MaterialStack(ELEMENT.getInstance().OXYGEN,32),
					new MaterialStack(ELEMENT.getInstance().HYDROGEN, 24)
			});

	public static final Material XENOTIME = new Material(
			"Xenotime", //Material Name
			MaterialState.ORE, //State
			new short[]{235, 89, 199, 0}, //Material Colour
			500,
			1500,
			50,
			75,
			0, //Radiation
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().YTTRIUM, 1),
					new MaterialStack(ELEMENT.getInstance().PHOSPHORUS, 1),
					new MaterialStack(ELEMENT.getInstance().OXYGEN, 4)
			});

	public static final Material YTTRIALITE = new Material(
			"Yttrialite", //Material Name
			MaterialState.ORE, //State
			new short[]{35, 189, 99, 0}, //Material Colour
			500,
			1500,
			50,
			75,
			0, //Radiation
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().YTTRIUM, 2),
					new MaterialStack(ELEMENT.getInstance().THORIUM, 2),
					new MaterialStack(ELEMENT.getInstance().SILICON, 2),
					new MaterialStack(ELEMENT.getInstance().OXYGEN, 7),
			});

	public static final Material YTTROCERITE = new Material(
			"Yttrocerite", //Material Name
			MaterialState.ORE, //State
			new short[]{35, 19, 199, 0}, //Material Colour
			500,
			1500,
			50,
			75,
			0, //Radiation
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().CERIUM, 1),
					new MaterialStack(ELEMENT.getInstance().CALCIUM, 1),
					new MaterialStack(ELEMENT.getInstance().FLUORINE, 5),
					new MaterialStack(ELEMENT.getInstance().YTTRIUM, 1),
			});






}
