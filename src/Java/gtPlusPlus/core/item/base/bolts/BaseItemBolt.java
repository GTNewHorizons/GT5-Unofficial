package gtPlusPlus.core.item.base.bolts;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gtPlusPlus.core.item.base.BaseItemComponent;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.UtilsItems;
import net.minecraft.item.ItemStack;

public class BaseItemBolt extends BaseItemComponent{

	public BaseItemBolt(Material material) {
		super(material, BaseItemComponent.ComponentTypes.BOLT);
		
		addExtruderRecipe();
	}

	private void addExtruderRecipe(){
		Utils.LOG_WARNING("Adding recipe for "+materialName+" Bolts");
		String tempIngot = unlocalName.replace("itemBolt", "ingot");
		ItemStack tempOutputStack = UtilsItems.getItemStackOfAmountFromOreDict(tempIngot, 1);
		if (null != tempOutputStack){
			GT_Values.RA.addExtruderRecipe(tempOutputStack, 
					ItemList.Shape_Extruder_Bolt.get(0), 
					UtilsItems.getSimpleStack(this, 8),
					(int) Math.max(componentMaterial.getMass() * 2L * 1, 1),
					8 * componentMaterial.vVoltageMultiplier);	
		}	
	}

}
