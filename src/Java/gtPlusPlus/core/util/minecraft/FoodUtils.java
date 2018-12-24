package gtPlusPlus.core.util.minecraft;

import gtPlusPlus.core.util.reflect.ReflectionUtils;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

public class FoodUtils {

	public static final Class IEdibleClass;
	
	static {
		IEdibleClass = ReflectionUtils.getClassByName("squeek.applecore.api.food.IEdible");
	}
	
	public static boolean isFood(ItemStack food) {
		
		if (food == null) {
			return false;
		}
		
		Item item = food.getItem();	
		
		if(item == null) {
			return false;
		}

		EnumAction action = item.getItemUseAction(food);
		
		if(item instanceof ItemBlock || action == EnumAction.eat || action == EnumAction.drink) {
			if(getUnmodifiedFoodValues(food) > 0) {
				return true;
			}
		}
		
		return false;
	}
	
	private static int getUnmodifiedFoodValues(ItemStack stack) {
		
		if (stack == null) {
			return 0;
		}
		
		Item item = stack.getItem();
		
		if(item == null) {
			return 0;
		}		
		
		if(IEdibleClass.isInstance(item) || item instanceof ItemFood || item == Items.cake) {
			return 1;
		}
		
		return 0;
	}
}
