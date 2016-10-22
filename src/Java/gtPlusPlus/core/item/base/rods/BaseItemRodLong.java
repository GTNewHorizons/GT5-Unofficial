package gtPlusPlus.core.item.base.rods;

import gregtech.api.enums.GT_Values;
import gtPlusPlus.core.item.base.BaseItemComponent;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.UtilsItems;
import gtPlusPlus.core.util.recipe.UtilsRecipe;
import net.minecraft.item.ItemStack;

public class BaseItemRodLong extends BaseItemComponent{

	public BaseItemRodLong(Material material) {
		super(material, BaseItemComponent.ComponentTypes.RODLONG);
		addExtruderRecipe();
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack p_77653_1_) {
		return ("Long "+materialName+ " Rod");
	}

	private void addExtruderRecipe(){
		Utils.LOG_WARNING("Adding recipe for Long "+materialName+" Rods");

		String tempStick = unlocalName.replace("itemRodLong", "stick");
		String tempStickLong = unlocalName.replace("itemRodLong", "stickLong");
		ItemStack stackStick = UtilsItems.getItemStackOfAmountFromOreDict(tempStick, 1);
		ItemStack stackLong = UtilsItems.getItemStackOfAmountFromOreDict(tempStickLong, 1);

		UtilsRecipe.addShapedGregtechRecipe(
				stackStick, "craftingToolHardHammer", stackStick,
				null, null, null,
				null, null, null,
				stackLong);

		ItemStack temp = stackStick;
		temp.stackSize = 2;

		GT_Values.RA.addForgeHammerRecipe(
				temp,
				stackLong,
				(int) Math.max(componentMaterial.getMass(), 1L),
				16);

		GT_Values.RA.addCutterRecipe(
				stackLong,
				temp,
				null,
				(int) Math.max(componentMaterial.getMass(), 1L),
				4);

		//Shaped Recipe - Long Rod to two smalls
		if (null != stackLong){
			stackStick.stackSize = 2;
			UtilsRecipe.recipeBuilder(
					"craftingToolSaw", null, null,
					stackLong, null, null,
					null, null, null,
					stackStick);
		}
	}

}
