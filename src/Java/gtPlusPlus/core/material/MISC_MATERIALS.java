package gtPlusPlus.core.material;

import gregtech.api.enums.TextureSet;
import gtPlusPlus.core.material.nuclear.FLUORIDES;
import gtPlusPlus.core.material.state.MaterialState;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.MaterialUtils;

public final class MISC_MATERIALS {	
	
	/*
	 * Some of these materials purely exist as data objects, items will most likely be assigned seperately.
	 * Most are just compositions which will have dusts assigned to them.
	 */
	
	public static void run() {
		MaterialUtils.generateSpecialDustAndAssignToAMaterial(STRONTIUM_OXIDE);
		MaterialUtils.generateSpecialDustAndAssignToAMaterial(STRONTIUM_HYDROXIDE);
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
			new MaterialStack[]{
					new MaterialStack(ELEMENT.getInstance().STRONTIUM, 1),
					new MaterialStack(FLUORIDES.HYDROXIDE, 2)
					});

	
	
}
