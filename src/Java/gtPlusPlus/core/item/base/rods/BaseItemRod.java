package gtPlusPlus.core.item.base.rods;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gtPlusPlus.core.item.base.BaseItemComponent;
import gtPlusPlus.core.material.ELEMENT;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.UtilsItems;
import gtPlusPlus.core.util.recipe.UtilsRecipe;
import net.minecraft.item.ItemStack;

public class BaseItemRod extends BaseItemComponent{

	public BaseItemRod(Material material) {
		super(material, BaseItemComponent.ComponentTypes.ROD);	
		
		if (!material.equals(ELEMENT.URANIUM233)){
			addExtruderRecipe();			
		}
		
	}


	private void addExtruderRecipe(){
		Utils.LOG_WARNING("Adding recipe for "+materialName+" Rods");

		ItemStack stackStick = componentMaterial.getRod(1);
		ItemStack stackStick2 = componentMaterial.getRod(2);
		ItemStack stackBolt = componentMaterial.getBolt(4);
		ItemStack stackStickLong = componentMaterial.getLongRod(1);
		ItemStack stackIngot = componentMaterial.getIngot(1);


		GT_Values.RA.addExtruderRecipe(
				stackIngot,
				ItemList.Shape_Extruder_Rod.get(0),
				stackStick2,
				(int) Math.max(componentMaterial.getMass() * 2L * 1, 1),
				6 * componentMaterial.vVoltageMultiplier);

		GT_Values.RA.addCutterRecipe(
				stackStick,
				stackBolt,
				null,
				(int) Math.max(componentMaterial.getMass() * 2L, 1L),
				4);	 

		if (componentMaterial.isRadioactive){
			UtilsRecipe.recipeBuilder(
					stackStick, stackStick, stackStick,
					stackStick, "craftingToolWrench", stackStick,
					stackStick, stackStick, stackStick,
					UtilsItems.getItemStackOfAmountFromOreDict(unlocalName.replace("itemRod", "frameGt"), 2));
		}

		//Shaped Recipe - Bolts
		stackBolt = componentMaterial.getBolt(2);
		if (null != stackBolt){
			UtilsRecipe.recipeBuilder(
					"craftingToolSaw", null, null,
					null, stackStick, null,
					null, null, null,
					stackBolt);
		}

		//Shaped Recipe - Ingot to Rod
		if (null != stackIngot){
			UtilsRecipe.recipeBuilder(
					"craftingToolFile", null, null,
					null, stackIngot, null,
					null, null, null,
					stackStick);
		}

	}

}
