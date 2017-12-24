package gtPlusPlus.core.material;

import gtPlusPlus.core.material.state.MaterialState;

public final class ORES {

	public static final Material GEIKIELITE = new Material(
			"Geikielite", //Material Name
			MaterialState.SOLID, //State
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
			MaterialState.SOLID, //State
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
			MaterialState.SOLID, //State
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
			MaterialState.SOLID, //State
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
			MaterialState.SOLID, //State
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
			MaterialState.SOLID, //State
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
}
