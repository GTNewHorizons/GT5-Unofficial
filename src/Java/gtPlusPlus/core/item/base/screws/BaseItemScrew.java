package gtPlusPlus.core.item.base.screws;

import gregtech.api.enums.GT_Values;
import gtPlusPlus.core.item.base.BaseItemComponent;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.ItemUtils;
import net.minecraft.item.ItemStack;

public class BaseItemScrew extends BaseItemComponent{

	public BaseItemScrew(Material material) {
		super(material, BaseItemComponent.ComponentTypes.SCREW);
		addLatheRecipe();
	}

	private void addLatheRecipe(){
		Utils.LOG_WARNING("Adding recipe for "+materialName+" Screws");
		ItemStack boltStack = ItemUtils.getItemStackOfAmountFromOreDict(unlocalName.replace("itemScrew", "bolt"), 1);
		if (null != boltStack){			
	         GT_Values.RA.addLatheRecipe(
	        		 boltStack,
	        		 ItemUtils.getSimpleStack(this),
	        		 null,
	        		 (int) Math.max(componentMaterial.getMass() / 8L, 1L),
	        		 4);			
		}				
	}

}
