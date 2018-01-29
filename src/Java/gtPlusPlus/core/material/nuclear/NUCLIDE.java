package gtPlusPlus.core.material.nuclear;

import gregtech.api.enums.Materials;
import gtPlusPlus.core.material.*;
import gtPlusPlus.core.material.state.MaterialState;
import gtPlusPlus.core.util.StringUtils;


public final class NUCLIDE {

	private static final NUCLIDE thisClass = new NUCLIDE();
	public NUCLIDE(){}
	public static NUCLIDE getInstance(){return thisClass;}

	//Custom Isotopes
	public final Material LITHIUM7 = new Material("Lithium 7", MaterialState.LIQUID, Materials.Lithium.mRGBa, Materials.Lithium.mMeltingPoint, Materials.Lithium.mBlastFurnaceTemp, Materials.Lithium.getProtons(), Materials.Lithium.getNeutrons(), Materials.Lithium.mBlastFurnaceRequired, StringUtils.superscript("7Li"), 0, false);//Not a GT Inherited Material
	public final Material URANIUM232 = new Material("Uranium 232", MaterialState.SOLID, new short[]{88, 220, 103, 0}, 1132, 4131, 92, 140, false, StringUtils.superscript("232U"), 4);//Not a GT Inherited Material
	public final Material URANIUM233 = new Material("Uranium 233", MaterialState.SOLID, new short[]{73, 220, 83, 0}, 1132, 4131, 92, 141, false, StringUtils.superscript("233U"), 2);//Not a GT Inherited Material
	public final Material THORIUM232 = new Material("Thorium 232", MaterialState.SOLID, new short[]{15, 60, 15, 0}, Materials.Thorium.mMeltingPoint, Materials.Thorium.mBlastFurnaceTemp, 90, 142, false, StringUtils.superscript("232Th"), 1, false);//Not a GT Inherited Material
	
	//RTG Fuels
	public final Material PLUTONIUM238 = new Material("Plutonium-238", MaterialState.SOLID, Materials.Plutonium241.mIconSet, Materials.Plutonium241.mDurability, Materials.Plutonium241.mRGBa, Materials.Plutonium241.mMeltingPoint, Materials.Plutonium241.mBlastFurnaceTemp, 94, 144, false, StringUtils.superscript("238Pu"), 2, false);//Not a GT Inherited Material
	public final Material STRONTIUM90 = new Material("Strontium-90", MaterialState.SOLID, Materials.Strontium.mIconSet, Materials.Strontium.mDurability, Materials.Strontium.mRGBa, Materials.Strontium.mMeltingPoint, Materials.Strontium.mBlastFurnaceTemp, 38, 52, false, StringUtils.superscript("90Sr"), 2, false);//Not a GT Inherited Material
	public final Material POLONIUM210 = new Material("Polonium-210", MaterialState.SOLID, Materials.Plutonium241.mIconSet, ELEMENT.getInstance().POLONIUM.vDurability, ELEMENT.getInstance().POLONIUM.getRGBA(), ELEMENT.getInstance().POLONIUM.getMeltingPointK(), ELEMENT.getInstance().POLONIUM.getBoilingPointK(), 84, 126, false, StringUtils.superscript("210Po"), 2, false);//Not a GT Inherited Material
	public final Material AMERICIUM241 = new Material("Americium-241", MaterialState.SOLID, Materials.Americium.mIconSet, Materials.Americium.mDurability, Materials.Americium.mRGBa, Materials.Americium.mMeltingPoint, Materials.Americium.mBlastFurnaceTemp, 95, 146, false, StringUtils.superscript("241Am"), 2, false);//Not a GT Inherited Material

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


}
