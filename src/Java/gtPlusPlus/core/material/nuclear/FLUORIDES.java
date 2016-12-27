package gtPlusPlus.core.material.nuclear;

import gregtech.api.enums.Materials;
import gtPlusPlus.core.material.*;

public class FLUORIDES {

	private static final FLUORIDES thisClass = new FLUORIDES();
	public FLUORIDES(){}
	public static FLUORIDES getInstance(){return thisClass;}

	public static final Material THORIUM_TETRAFLUORIDE = new Material(
	"Thorium Tetrafluoride", //Material Name
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

	public static final Material THORIUM_HEXAFLUORIDE = new Material(
	"Thorium Hexafluoride", //Material Name
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
	
	public static final Material URANIUM_TETRAFLUORIDE = new Material(
			"Uranium Tetrafluoride", //Material Name
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
	
	public static final Material URANIUM_HEXAFLUORIDE = new Material(
			"Uranium Hexafluoride", //Material Name
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
	
	//
	
	public static final Material ZIRCONIUM_TETRAFLUORIDE = new Material(
			"Zirconium Tetrafluoride", //Material Name
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
	
	public static final Material BERYLLIUM_FLUORIDE = new Material(
			"Beryllium Tetrafluoride", //Material Name
			new short[]{120, 180, 120, 0}, //Material Colour
			Materials.Beryllium.mMeltingPoint, //Melting Point in C
			Materials.Beryllium.mBlastFurnaceTemp, //Boiling Point in C
			((ELEMENT.getInstance().BERYLLIUM.getProtons()+(ELEMENT.getInstance().FLUORINE.getProtons()*2))/3), //Protons
			((ELEMENT.getInstance().BERYLLIUM.getNeutrons()+(ELEMENT.getInstance().FLUORINE.getNeutrons()*2))/3), //Neutrons
			false, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().BERYLLIUM, 1),
					new MaterialStack(ELEMENT.getInstance().FLUORINE, 2)
			});
	
	public static final Material LITHIUM_FLUORIDE = new Material(
			"Lithium Tetrafluoride", //Material Name
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
	
}
