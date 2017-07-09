package gtPlusPlus.core.util.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public final class AddGregtechRecipe {

	public static boolean PyrolyseOven(final ItemStack p0, final FluidStack p1, final int p2, final ItemStack p3,
			final FluidStack p4, final int p5, final int p6){

		try {
			Class<?> GT_RecipeAdder = Class.forName("gregtech.common.GT_RecipeAdder");
			if (GT_RecipeAdder != null){
				Method addPollution = GT_RecipeAdder.getMethod("addPyrolyseRecipe", ItemStack.class, FluidStack.class, int.class, ItemStack.class, FluidStack.class, int.class, int.class);
				if (addPollution != null){
					return (boolean) addPollution.invoke(p0, p1, p2, p3, p4, p5, p6);
				}
			}
		}
		catch (ClassNotFoundException | SecurityException | NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			return false;
		}
		return false;
	}

}