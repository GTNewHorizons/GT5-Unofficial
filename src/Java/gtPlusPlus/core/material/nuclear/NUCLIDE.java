package gtPlusPlus.core.material.nuclear;

import gtPlusPlus.core.material.ELEMENT;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialStack;
import gtPlusPlus.core.material.state.MaterialState;
import gtPlusPlus.core.util.data.StringUtils;


public final class NUCLIDE {

	public static final Material LiFBeF2ThF4UF4 = new Material(
			"LiFBeF2ThF4UF4", //Material Name
			MaterialState.LIQUID, //State
			new short[]{40, 90, 25, 0}, //Material Colour
			566, //Melting Point in C
			870, //Boiling Point in C
			150, //Protons
			150, //Neutrons
			false, //Uses Blast furnace?
			StringUtils.subscript(StringUtils.superscript("7")+"LiFBeF2ThF4UF4"), //Chemical Formula
			5, //Radioactivity Level
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(FLUORIDES.LITHIUM_FLUORIDE, 65),
					new MaterialStack(FLUORIDES.BERYLLIUM_FLUORIDE, 28),
					new MaterialStack(FLUORIDES.THORIUM_TETRAFLUORIDE, 1),
					new MaterialStack(FLUORIDES.URANIUM_TETRAFLUORIDE, 1)
			});

	public static final Material LiFBeF2ZrF4UF4 = new Material(
			"LiFBeF2ZrF4UF4", //Material Name
			MaterialState.LIQUID, //State
			new short[]{20, 70, 45, 0}, //Material Colour
			650, //Melting Point in C
			940, //Boiling Point in C
			150, //Protons
			150, //Neutrons
			false, //Uses Blast furnace?
			StringUtils.subscript(StringUtils.superscript("7")+"LiFBeF2ZrF4UF4"), //Chemical Formula
			5, //Radioactivity Level
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(FLUORIDES.LITHIUM_FLUORIDE, 65),
					new MaterialStack(FLUORIDES.BERYLLIUM_FLUORIDE, 28),
					new MaterialStack(FLUORIDES.ZIRCONIUM_TETRAFLUORIDE, 5),
					new MaterialStack(FLUORIDES.URANIUM_TETRAFLUORIDE, 2)
			});

	public static final Material LiFBeF2ZrF4U235 = new Material(
			"LiFBeF2ZrF4U235", //Material Name
			MaterialState.LIQUID, //State
			new short[]{50, 70, 15, 0}, //Material Colour
			590, //Melting Point in C
			890, //Boiling Point in C
			150, //Protons
			150, //Neutrons
			false, //Uses Blast furnace?
			StringUtils.subscript(StringUtils.superscript("7")+"LiFBeF2ZrF4")+StringUtils.superscript("235U"), //Chemical Formula
			5, //Radioactivity Level
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(FLUORIDES.LITHIUM_FLUORIDE, 55),
					new MaterialStack(FLUORIDES.BERYLLIUM_FLUORIDE, 25),
					new MaterialStack(FLUORIDES.ZIRCONIUM_TETRAFLUORIDE, 6),
					new MaterialStack(ELEMENT.getInstance().URANIUM235, 14)
			});


	private static final NUCLIDE INSTANCE = new NUCLIDE();
	public static NUCLIDE getInstance(){return INSTANCE;}

}
