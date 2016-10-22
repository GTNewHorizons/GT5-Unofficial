package gtPlusPlus.core.item.base.gears;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gtPlusPlus.core.item.base.BaseItemComponent;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.UtilsItems;
import net.minecraft.item.ItemStack;

public class BaseItemGear extends BaseItemComponent{

	public BaseItemGear(Material material) {
		super(material, BaseItemComponent.ComponentTypes.GEAR);		
		addExtruderRecipe();
	}

	private void addExtruderRecipe(){
		Utils.LOG_WARNING("Adding recipe for "+materialName+" Gears");
		ItemStack tempOutputStack = componentMaterial.getIngot(8);
		if (null != tempOutputStack){
			GT_Values.RA.addExtruderRecipe(
					tempOutputStack,
					ItemList.Shape_Extruder_Gear.get(0),
					UtilsItems.getSimpleStack(this),
					(int) Math.max(componentMaterial.getMass() * 5L, 1),
					8 * componentMaterial.vVoltageMultiplier);
		}				
	}

}
