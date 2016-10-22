package gtPlusPlus.core.item.base.rings;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gtPlusPlus.core.item.base.BaseItemComponent;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.UtilsItems;
import gtPlusPlus.core.util.recipe.UtilsRecipe;
import net.minecraft.item.ItemStack;

public class BaseItemRing extends BaseItemComponent{

	public BaseItemRing(Material material) {
		super(material, BaseItemComponent.ComponentTypes.RING);
		addExtruderRecipe();
	}

	private void addExtruderRecipe(){
		Utils.LOG_WARNING("Adding recipe for "+materialName+" Rings");
		
		//Extruder Recipe
		String tempIngot = unlocalName.replace("itemRing", "ingot");
		ItemStack tempOutputStack = UtilsItems.getItemStackOfAmountFromOreDict(tempIngot, 1);
		if (null != tempOutputStack){
			GT_Values.RA.addExtruderRecipe(tempOutputStack,
					ItemList.Shape_Extruder_Ring.get(0),
					UtilsItems.getSimpleStack(this, 4),
					(int) Math.max(componentMaterial.getMass() * 2L * 1, 1),
					6 * componentMaterial.vVoltageMultiplier);	
		}		
		
		//Shaped Recipe
		tempIngot = unlocalName.replace("itemRing", "stick");
		tempOutputStack = UtilsItems.getItemStackOfAmountFromOreDict(tempIngot, 1);
		if (null != tempOutputStack){
			UtilsRecipe.addShapedGregtechRecipe(
					"craftingToolWrench", null, null,
					null, tempOutputStack, null,
					null, null, null,
					UtilsItems.getSimpleStack(this, 1));
		}
	}

}
