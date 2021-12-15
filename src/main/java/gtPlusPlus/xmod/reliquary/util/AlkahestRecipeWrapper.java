package gtPlusPlus.xmod.reliquary.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import gtPlusPlus.core.util.reflect.ReflectionUtils;
import net.minecraft.item.ItemStack;

public class AlkahestRecipeWrapper {
	public ItemStack item = null;
	public int yield = 0;
	public int cost = 0;
	public String dictionaryName = null;

	public AlkahestRecipeWrapper(ItemStack par1, int par2, int par3) {
		this.item = par1;
		this.yield = par2;
		this.cost = par3;
	}

	public AlkahestRecipeWrapper(String par1, int par2, int par3) {
		this.dictionaryName = par1;
		this.yield = par2;
		this.cost = par3;
	}

	public Object getOriginalRecipe() {
		try {
			Constructor<?> o;
			if (dictionaryName == null) {
				 o = ReflectionUtils.getClass("xreliquary.util.alkahestry.AlkahestRecipe").getConstructor(ItemStack.class, int.class, int.class);
			}
			else {
				 o = ReflectionUtils.getClass("xreliquary.util.alkahestry.AlkahestRecipe").getConstructor(String.class, int.class, int.class);
			}
			
			Object r = o.newInstance(dictionaryName == null ? item : dictionaryName, yield, cost);
			if (r != null) {
				return r;
			}

		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// oops
		}
		return null;
	}
}