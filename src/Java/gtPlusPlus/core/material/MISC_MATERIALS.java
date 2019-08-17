package gtPlusPlus.core.material;

import gregtech.api.enums.TextureSet;
import gtPlusPlus.core.material.nuclear.FLUORIDES;
import gtPlusPlus.core.material.state.MaterialState;
import gtPlusPlus.core.util.minecraft.MaterialUtils;

public final class MISC_MATERIALS {	

	/*
	 * Some of these materials purely exist as data objects, items will most likely be assigned seperately.
	 * Most are just compositions which will have dusts assigned to them.
	 */

	public static void run() {
		MaterialUtils.generateSpecialDustAndAssignToAMaterial(STRONTIUM_OXIDE, false);
		MaterialUtils.generateSpecialDustAndAssignToAMaterial(STRONTIUM_HYDROXIDE, false);
	}

	public static final Material STRONTIUM_OXIDE = new Material(
			"Strontium Oxide", 
			MaterialState.SOLID, 
			TextureSet.SET_METALLIC,
			null,
			-1,
			-1,
			-1,
			-1, 
			false, 
			"SrO",
			0, 
			false,
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().STRONTIUM, 1),
					new MaterialStack(ELEMENT.getInstance().OXYGEN, 1)
			});

	public static final Material STRONTIUM_HYDROXIDE = new Material(
			"Strontium Hydroxide", 
			MaterialState.SOLID, 
			TextureSet.SET_METALLIC,
			null,
			-1,
			-1,
			-1,
			-1, 
			false, 
			"Sr(OH)2",
			0, 
			false,
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().STRONTIUM, 1),
					new MaterialStack(FLUORIDES.HYDROXIDE, 2)
			});

	public static final Material SELENIUM_DIOXIDE = new Material(
			"Selenium Dioxide",
			MaterialState.PURE_LIQUID, //State
			null, //Material Colour
			-1, //Melting Point in C
			-1, //Boiling Point in C
			-1, //Protons
			-1,
			false, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().SELENIUM, 1),
					new MaterialStack(ELEMENT.getInstance().OXYGEN, 2)
			});

	public static final Material SELENIOUS_ACID = new Material(
			"Selenious Acid",
			MaterialState.PURE_LIQUID, //State
			null, //Material Colour
			-1, //Melting Point in C
			-1, //Boiling Point in C
			-1, //Protons
			-1,
			false, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(SELENIUM_DIOXIDE, 1),
					new MaterialStack(ELEMENT.getInstance().HYDROGEN, 8),
					new MaterialStack(ELEMENT.getInstance().OXYGEN, 4)
			});

	public static final Material HYDROGEN_CYANIDE = new Material(
			"Hydrogen Cyanide",
			MaterialState.PURE_LIQUID, //State
			null, //Material Colour
			4, //Melting Point in C
			26, //Boiling Point in C
			-1, //Protons
			-1,
			false, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().HYDROGEN, 1),
					new MaterialStack(ELEMENT.getInstance().CARBON, 1),
					new MaterialStack(ELEMENT.getInstance().NITROGEN, 1)
			});

	public static final Material CARBON_DIOXIDE = new Material(
			"Carbon Dioxide",
			MaterialState.PURE_LIQUID, //State
			null, //Material Colour
			-56, //Melting Point in C
			-1, //Boiling Point in C
			-1, //Protons
			-1,
			false, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().CARBON, 1),
					new MaterialStack(ELEMENT.getInstance().OXYGEN, 2)
			});



}
