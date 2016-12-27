package gtPlusPlus.core.material.nuclear;

import gregtech.api.enums.Materials;
import gtPlusPlus.core.material.*;
import gtPlusPlus.core.util.materials.MaterialUtils;


public final class NUCLIDE {

	private static final NUCLIDE thisClass = new NUCLIDE();
	public NUCLIDE(){}
	public static NUCLIDE getInstance(){return thisClass;}

	//Custom Isotopes
	public final Material LITHIUM7 = new Material("Lithium 7", Materials.Lithium.mRGBa, Materials.Lithium.mMeltingPoint, Materials.Lithium.mBlastFurnaceTemp, Materials.Lithium.getProtons(), Materials.Lithium.getNeutrons(), Materials.Lithium.mBlastFurnaceRequired, MaterialUtils.superscript("7Li"), 0);//Not a GT Inherited Material
	public final Material URANIUM232 = new Material("Uranium 232", new short[]{88, 220, 103, 0}, 1132, 4131, 92, 140, false, MaterialUtils.superscript("232U"), 4);//Not a GT Inherited Material
	public final Material URANIUM233 = new Material("Uranium 233", new short[]{73, 220, 83, 0}, 1132, 4131, 92, 141, false, MaterialUtils.superscript("233U"), 2);//Not a GT Inherited Material
	public final Material THORIUM232 = new Material("Thorium 232", new short[]{15, 60, 15, 0}, Materials.Thorium.mMeltingPoint, Materials.Thorium.mBlastFurnaceTemp, 90, 142, false, MaterialUtils.superscript("232Th"), 1);//Not a GT Inherited Material
	
	
	
	public static final Material LiFBeF2ThF4UF4 = new Material(
			"Reactor Salt LiFBeF2ThF4UF4", //Material Name
			new short[]{40, 90, 25, 0}, //Material Colour
			566, //Melting Point in C
			870, //Boiling Point in C
			150, //Protons
			150, //Neutrons
			false, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(FLUORIDES.LITHIUM_FLUORIDE, 65),
					new MaterialStack(FLUORIDES.BERYLLIUM_FLUORIDE, 28),
					new MaterialStack(FLUORIDES.THORIUM_TETRAFLUORIDE, 1),
					new MaterialStack(FLUORIDES.URANIUM_TETRAFLUORIDE, 1)
			});
	
	public static final Material LiFBeF2ZrF4U235 = new Material(
			"Reactor Salt LiFBeF2ZrF4U235", //Material Name
			new short[]{50, 70, 15, 0}, //Material Colour
			590, //Melting Point in C
			890, //Boiling Point in C
			150, //Protons
			150, //Neutrons
			false, //Uses Blast furnace?
			//Material Stacks with Percentage of required elements.
			new MaterialStack[]{
					new MaterialStack(FLUORIDES.LITHIUM_FLUORIDE, 55),
					new MaterialStack(FLUORIDES.BERYLLIUM_FLUORIDE, 25),
					new MaterialStack(FLUORIDES.ZIRCONIUM_TETRAFLUORIDE, 6),
					new MaterialStack(ELEMENT.getInstance().URANIUM235, 14)
			});


}
