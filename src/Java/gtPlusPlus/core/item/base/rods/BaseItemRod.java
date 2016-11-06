package gtPlusPlus.core.item.base.rods;

import gregtech.api.enums.GT_Values;
import gtPlusPlus.core.item.base.BaseItemComponent;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.Utils;
import net.minecraft.item.ItemStack;

public class BaseItemRod extends BaseItemComponent{

	public BaseItemRod(Material material) {
		super(material, BaseItemComponent.ComponentTypes.ROD);	
		addExtruderRecipe();		
	}


	private void addExtruderRecipe(){
		Utils.LOG_WARNING("Adding cutter recipe for "+materialName+" Rods");

		ItemStack stackStick = componentMaterial.getRod(1);
		ItemStack stackBolt = componentMaterial.getBolt(4);

		GT_Values.RA.addCutterRecipe(
				stackStick,
				stackBolt,
				null,
				(int) Math.max(componentMaterial.getMass() * 2L, 1L),
				4);	 
	}

}
