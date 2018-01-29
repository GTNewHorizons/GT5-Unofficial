package gtPlusPlus.xmod.eio.material;

import gtPlusPlus.core.material.*;
import gtPlusPlus.core.material.state.MaterialState;

public class MaterialEIO {

	public static final Material SOULARIUM = new Material(
			"Soularium", //Material Name
			MaterialState.SOLID, //State
			new short[]{95,90,54, 0}, //Material Colour
			10, //Melting Point in C
			10,
			10,
			10,
			false, //Uses Blast furnace?
			false, //Generates a cell
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().GOLD, 1),
					new MaterialStack(NONMATERIAL.SOULSAND, 1)
			});
	
	public static final Material CONDUCTIVE_IRON = new Material(
			"Conductive Iron", //Material Name
			MaterialState.SOLID, //State
			new short[]{164,109,100, 0}, //Material Colour
			10, //Melting Point in C
			10,
			10,
			10,
			false, //Uses Blast furnace?
			false, //Generates a cell
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().IRON, 1),
					new MaterialStack(NONMATERIAL.REDSTONE, 1)
			});
	
	public static final Material PULSATING_IRON = new Material(
			"Pulsating Iron", //Material Name
			MaterialState.SOLID, //State
			new short[]{50,91,21, 0}, //Material Colour
			10, //Melting Point in C
			10,
			10,
			10,
			false, //Uses Blast furnace?
			false, //Generates a cell
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().IRON, 1),
					new MaterialStack(NONMATERIAL.ENDERPEARL, 1)
			});
	
	public static final Material ELECTRICAL_STEEL = new Material(
			"Electrical Steel", //Material Name
			MaterialState.SOLID, //State
			new short[]{194,194,194, 0}, //Material Colour
			10, //Melting Point in C
			10,
			10,
			10,
			true, //Uses Blast furnace?
			false, //Generates a cell
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ALLOY.STEEL, 3),
					new MaterialStack(ELEMENT.getInstance().SILICON, 1)
			});
	
	public static final Material ENERGETIC_ALLOY = new Material(
			"Energetic Alloy", //Material Name
			MaterialState.SOLID, //State
			new short[]{252,151,45, 0}, //Material Colour
			10, //Melting Point in C
			10,
			10,
			10,
			true, //Uses Blast furnace?
			false, //Generates a cell
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().GOLD, 1),
					new MaterialStack(NONMATERIAL.REDSTONE, 1),
					new MaterialStack(NONMATERIAL.GLOWSTONE, 1)
			});
	
	public static final Material VIBRANT_ALLOY = new Material(
			"Vibrant Alloy", //Material Name
			MaterialState.SOLID, //State
			new short[]{204,242,142, 0}, //Material Colour
			10, //Melting Point in C
			10,
			10,
			10,
			true, //Uses Blast furnace?
			false, //Generates a cell
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ENERGETIC_ALLOY, 1),
					new MaterialStack(NONMATERIAL.ENDERPEARL, 1)
			});
	
	public static final Material REDSTONE_ALLOY = new Material(
			"Redstone Alloy", //Material Name
			MaterialState.SOLID, //State
			new short[]{178,34,34, 0}, //Material Colour
			10, //Melting Point in C
			10,
			10,
			10,
			false, //Uses Blast furnace?
			false, //Generates a cell
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().SILICON, 1),
					new MaterialStack(NONMATERIAL.REDSTONE, 1)
			});
	
}
