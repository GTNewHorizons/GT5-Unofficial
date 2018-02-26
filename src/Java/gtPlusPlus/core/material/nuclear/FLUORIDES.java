package gtPlusPlus.core.material.nuclear;

import gregtech.api.enums.Materials;

import gtPlusPlus.core.material.ELEMENT;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialStack;
import gtPlusPlus.core.material.state.MaterialState;
import gtPlusPlus.core.util.minecraft.MaterialUtils;

public class FLUORIDES {

	private static final FLUORIDES thisClass = new FLUORIDES();
	public FLUORIDES(){}
	public static FLUORIDES getInstance(){return thisClass;}

	public static final Material FLUORITE = new Material(
			"Fluorite", //Material Name
			MaterialState.SOLID, //State
			new short[]{75, 70, 25, 0}, //Material Colour
			Materials.Fluorine.mMeltingPoint, //Melting Point in C
			Materials.Fluorine.mBlastFurnaceTemp, //Boiling Point in C
			((ELEMENT.getInstance().CALCIUM.getProtons()+(ELEMENT.getInstance().FLUORINE.getProtons()*2))/3), //Protons
			((ELEMENT.getInstance().CALCIUM.getNeutrons()+(ELEMENT.getInstance().FLUORINE.getNeutrons()*2))/3), //Neutrons
			false, //Uses Blast furnace?
			false, //Generate cells
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().CALCIUM, 24),
					new MaterialStack(ELEMENT.getInstance().FLUORINE, 72),
					new MaterialStack(MaterialUtils.generateMaterialFromGtENUM(Materials.Galena), 4)
			});

	//ThF4
	public static final Material THORIUM_TETRAFLUORIDE = new Material(
			"Thorium Tetrafluoride", //Material Name
			MaterialState.LIQUID, //State
			new short[]{25, 70, 25, 0}, //Material Colour
			Materials.Thorium.mMeltingPoint, //Melting Point in C
			Materials.Thorium.mBlastFurnaceTemp, //Boiling Point in C
			((NUCLIDE.getInstance().THORIUM232.getProtons()+(ELEMENT.getInstance().FLUORINE.getProtons()*4))/5), //Protons
			((NUCLIDE.getInstance().THORIUM232.getNeutrons()+(ELEMENT.getInstance().FLUORINE.getNeutrons()*4))/5), //Neutrons
			false, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(NUCLIDE.getInstance().THORIUM232, 1),
					new MaterialStack(ELEMENT.getInstance().FLUORINE, 4)
			});

	//ThF6
	public static final Material THORIUM_HEXAFLUORIDE = new Material(
			"Thorium Hexafluoride", //Material Name
			MaterialState.LIQUID, //State
			new short[]{10, 50, 10, 0}, //Material Colour
			Materials.Thorium.mMeltingPoint, //Melting Point in C
			Materials.Thorium.mBlastFurnaceTemp, //Boiling Point in C
			((NUCLIDE.getInstance().THORIUM232.getProtons()+NUCLIDE.getInstance().THORIUM232.getProtons()+(ELEMENT.getInstance().FLUORINE.getProtons()*6))/8), //Protons
			((NUCLIDE.getInstance().THORIUM232.getNeutrons()+NUCLIDE.getInstance().THORIUM232.getNeutrons()+(ELEMENT.getInstance().FLUORINE.getNeutrons()*6))/8), //Neutrons
			false, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(NUCLIDE.getInstance().THORIUM232, 1),
					new MaterialStack(ELEMENT.getInstance().THORIUM, 1),
					new MaterialStack(ELEMENT.getInstance().FLUORINE, 12)
			});

	//UF4
	public static final Material URANIUM_TETRAFLUORIDE = new Material(
			"Uranium Tetrafluoride", //Material Name
			MaterialState.LIQUID, //State
			new short[]{50, 240, 50, 0}, //Material Colour
			Materials.Uranium235.mMeltingPoint, //Melting Point in C
			Materials.Uranium235.mBlastFurnaceTemp, //Boiling Point in C
			((NUCLIDE.getInstance().URANIUM233.getProtons()+(ELEMENT.getInstance().FLUORINE.getProtons()*4))/5), //Protons
			((NUCLIDE.getInstance().URANIUM233.getNeutrons()+(ELEMENT.getInstance().FLUORINE.getNeutrons()*4))/5), //Neutrons
			false, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(NUCLIDE.getInstance().URANIUM233, 1),
					new MaterialStack(ELEMENT.getInstance().FLUORINE, 4)
			});

	//UF6
	public static final Material URANIUM_HEXAFLUORIDE = new Material(
			"Uranium Hexafluoride", //Material Name
			MaterialState.LIQUID, //State
			new short[]{70, 250, 70, 0}, //Material Colour
			Materials.Uranium235.mMeltingPoint, //Melting Point in C
			Materials.Uranium235.mBlastFurnaceTemp, //Boiling Point in C
			((FLUORIDES.URANIUM_TETRAFLUORIDE.getProtons()+(ELEMENT.getInstance().FLUORINE.getProtons()*2))/3), //Protons
			((FLUORIDES.URANIUM_TETRAFLUORIDE.getNeutrons()+(ELEMENT.getInstance().FLUORINE.getNeutrons()*2))/3), //Neutrons
			false, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(FLUORIDES.URANIUM_TETRAFLUORIDE, 1),
					new MaterialStack(ELEMENT.getInstance().FLUORINE, 2)
			});

	//ZrF4
	public static final Material ZIRCONIUM_TETRAFLUORIDE = new Material(
			"Zirconium Tetrafluoride", //Material Name
			MaterialState.LIQUID, //State
			ELEMENT.getInstance().ZIRCONIUM.getRGBA(), //Material Colour
			ELEMENT.getInstance().ZIRCONIUM.getMeltingPointC(), //Melting Point in C
			ELEMENT.getInstance().ZIRCONIUM.getBoilingPointC(), //Boiling Point in C
			((ELEMENT.getInstance().ZIRCONIUM.getProtons()+(ELEMENT.getInstance().FLUORINE.getProtons()*4))/5), //Protons
			((ELEMENT.getInstance().ZIRCONIUM.getNeutrons()+(ELEMENT.getInstance().FLUORINE.getNeutrons()*4))/5), //Neutrons
			false, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().ZIRCONIUM, 1),
					new MaterialStack(ELEMENT.getInstance().FLUORINE, 4)
			});

	//BeF2
	public static final Material BERYLLIUM_FLUORIDE = new Material(
			"Beryllium Fluoride", //Material Name
			MaterialState.LIQUID, //State
			new short[]{120, 210, 120, 0}, //Material Colour
			-1,
			-1,
			-1,
			-1,
			false, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().BERYLLIUM, 1),
					new MaterialStack(ELEMENT.getInstance().FLUORINE, 2)
			});

	//LiF
	public static final Material LITHIUM_FLUORIDE = new Material(
			"Lithium Fluoride", //Material Name
			MaterialState.LIQUID, //State
			new short[]{225, 220, 255, 0}, //Material Colour
			Materials.Lithium.mMeltingPoint, //Melting Point in C
			Materials.Lithium.mBlastFurnaceTemp, //Boiling Point in C
			((NUCLIDE.getInstance().LITHIUM7.getProtons()+(ELEMENT.getInstance().FLUORINE.getProtons()))/2), //Protons
			((NUCLIDE.getInstance().LITHIUM7.getNeutrons()+(ELEMENT.getInstance().FLUORINE.getNeutrons()))/2), //Neutrons
			false, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(NUCLIDE.getInstance().LITHIUM7, 1),
					new MaterialStack(ELEMENT.getInstance().FLUORINE, 1)
			});


	//LFTR sub components
	
	//OH
	public static final Material HYDROXIDE = new Material(
			"Hydroxide", //Material Name
			MaterialState.PURE_LIQUID, //State
			new short[]{240, 220, 240, 0}, //Material Colour
			-1, //Melting Point in C
			-1, //Boiling Point in C
			-1, //Protons
			-1,
			false, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().OXYGEN, 1),
					new MaterialStack(ELEMENT.getInstance().HYDROGEN, 1)
			});
	
	//NH3
	public static final Material AMMONIA = new Material(
			"Ammonia", //Material Name
			MaterialState.PURE_LIQUID, //State
			new short[]{240, 220, 240, 0}, //Material Colour
			-77, //Melting Point in C
			-33, //Boiling Point in C
			-1, //Protons
			-1,
			false, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().NITROGEN, 1),
					new MaterialStack(ELEMENT.getInstance().HYDROGEN, 3)
			});
	
	//NH4
	public static final Material AMMONIUM = new Material(
			"Ammonium", //Material Name
			MaterialState.PURE_LIQUID, //State
			new short[]{240, 220, 240, 0}, //Material Colour
			-1, //Melting Point in C
			-1, //Boiling Point in C
			-1, //Protons
			-1,
			false, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().NITROGEN, 1),
					new MaterialStack(ELEMENT.getInstance().HYDROGEN, 4)
			});
	
	//(NH4)HF2
	public static final Material AMMONIUM_BIFLUORIDE = new Material(
			"Ammonium Bifluoride", //Material Name
			MaterialState.PURE_LIQUID, //State
			new short[]{240, 220, 240, 0}, //Material Colour
			126, //Melting Point in C
			240, //Boiling Point in C
			-1, //Protons
			-1,
			false, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(FLUORIDES.AMMONIUM, 1),
					new MaterialStack(ELEMENT.getInstance().HYDROGEN, 1),
					new MaterialStack(ELEMENT.getInstance().FLUORINE, 2)
			});
	
	//Be(OH)2
	public static final Material BERYLLIUM_HYDROXIDE = new Material(
			"Beryllium Hydroxide", //Material Name
			MaterialState.PURE_LIQUID, //State
			new short[]{180, 250, 180, 0}, //Material Colour
			1000, //Melting Point in C
			-1, //Boiling Point in C
			-1,
			-1,
			false, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().BERYLLIUM, 1),
					new MaterialStack(FLUORIDES.HYDROXIDE, 2)
			});
	
	// (NH4)2Be(OH)2 / (NH4)2BeF4
	public static final Material AMMONIUM_TETRAFLUOROBERYLLATE = new Material(
			"Ammonium Tetrafluoroberyllate", //Material Name
			MaterialState.PURE_LIQUID, //State
			new short[]{140, 220, 140, 0}, //Material Colour
			280, //Melting Point in C
			-1, //Boiling Point in C
			-1, //Protons
			-1,
			false, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(FLUORIDES.AMMONIUM, 2),
					new MaterialStack(FLUORIDES.BERYLLIUM_HYDROXIDE, 1)
			});
	
	
	
	//LFTR Output
	public static final Material NEPTUNIUM_HEXAFLUORIDE = new Material(
			"Neptunium Hexafluoride", //Material Name
			MaterialState.GAS, //State
			ELEMENT.getInstance().NEPTUNIUM.getRGBA(), //Material Colour
			ELEMENT.getInstance().NEPTUNIUM.getMeltingPointC(), //Melting Point in C
			ELEMENT.getInstance().NEPTUNIUM.getBoilingPointC(), //Boiling Point in C
			((ELEMENT.getInstance().NEPTUNIUM.getProtons()+(ELEMENT.getInstance().FLUORINE.getProtons()*6))/7), //Protons
			((ELEMENT.getInstance().NEPTUNIUM.getNeutrons()+(ELEMENT.getInstance().FLUORINE.getNeutrons()*6))/7), //Neutrons
			false, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().NEPTUNIUM, 1),
					new MaterialStack(ELEMENT.getInstance().FLUORINE, 6)
			});

	public static final Material TECHNETIUM_HEXAFLUORIDE = new Material(
			"Technetium Hexafluoride", //Material Name
			MaterialState.GAS, //State
			ELEMENT.getInstance().TECHNETIUM.getRGBA(), //Material Colour
			ELEMENT.getInstance().TECHNETIUM.getMeltingPointC(), //Melting Point in C
			ELEMENT.getInstance().TECHNETIUM.getBoilingPointC(), //Boiling Point in C
			((ELEMENT.getInstance().TECHNETIUM.getProtons()+(ELEMENT.getInstance().FLUORINE.getProtons()*6))/7), //Protons
			((ELEMENT.getInstance().TECHNETIUM.getNeutrons()+(ELEMENT.getInstance().FLUORINE.getNeutrons()*6))/7), //Neutrons
			false, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().TECHNETIUM, 1),
					new MaterialStack(ELEMENT.getInstance().FLUORINE, 6)
			});

	public static final Material SELENIUM_HEXAFLUORIDE = new Material(
			"Selenium Hexafluoride", //Material Name
			MaterialState.GAS, //State
			ELEMENT.getInstance().SELENIUM.getRGBA(), //Material Colour
			ELEMENT.getInstance().SELENIUM.getMeltingPointC(), //Melting Point in C
			ELEMENT.getInstance().SELENIUM.getBoilingPointC(), //Boiling Point in C
			((ELEMENT.getInstance().SELENIUM.getProtons()+(ELEMENT.getInstance().FLUORINE.getProtons()*6))/7), //Protons
			((ELEMENT.getInstance().SELENIUM.getNeutrons()+(ELEMENT.getInstance().FLUORINE.getNeutrons()*6))/7), //Neutrons
			false, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().SELENIUM, 1),
					new MaterialStack(ELEMENT.getInstance().FLUORINE, 6)
			});

}
