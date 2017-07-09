package gtPlusPlus.core.util.gregtech.recipehandlers;

import java.lang.reflect.Method;

import gregtech.api.util.GT_ModHandler;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import net.minecraft.item.ItemStack;

public final class GregtechRecipe {

	public LibraryProxy ourProxy;
	public GregtechRecipe(){
		Utils.LOG_INFO("Initializing a recipe handler for different versions of Gregtech 5.");
		try {
			if (!CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK){
				this.ourProxy = new LibProxy1();
				Utils.LOG_INFO("Selecting GT 5.7/5.8 Recipe Set");
			}
			if (CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK){
				this.ourProxy = new LibProxy2();
				Utils.LOG_INFO("Selecting GT 5.9 Recipe Set");
			}
		} catch (final NoSuchMethodException e) {
			this.ourProxy = null;
		}
	}

	public boolean addSmeltingAndAlloySmeltingRecipe(final ItemStack aInput, final ItemStack aOutput) {
		Utils.LOG_WARNING("Adding a GT Furnace/Alloy Smelter Recipe"+"| Input:"+aInput.getDisplayName()+" | Output:"+aOutput.getDisplayName()+" |");
		return this.ourProxy.addSmeltingAndAlloySmeltingRecipe(aInput, aOutput);
	}

}

abstract class LibraryProxy { // can also be interface unless you want to have common code here
	abstract public boolean addSmeltingAndAlloySmeltingRecipe(ItemStack aInput, ItemStack aOutput);
}

class LibProxy1 extends LibraryProxy {
	final Method m1;

	public LibProxy1() throws NoSuchMethodException {
		this.m1 = GT_ModHandler.class.getDeclaredMethod("addSmeltingAndAlloySmeltingRecipe", ItemStack.class, ItemStack.class);
	}

	@Override
	public boolean addSmeltingAndAlloySmeltingRecipe(final ItemStack aInput, final ItemStack aOutput) {
		try {
			Utils.LOG_INFO("Trying with Gt 5.7/5.8 Method.");
			return (boolean) this.m1.invoke(null, aInput, aOutput);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}
}

class LibProxy2 extends LibraryProxy {
	final Method m2;

	public LibProxy2() throws NoSuchMethodException {
		this.m2 = GT_ModHandler.class.getDeclaredMethod("addSmeltingAndAlloySmeltingRecipe", ItemStack.class, ItemStack.class, boolean.class);
	}

	@Override
	public boolean addSmeltingAndAlloySmeltingRecipe(final ItemStack aInput, final ItemStack aOutput) {
		try {
			Utils.LOG_INFO("Trying with Gt 5.9 Method.");
			return (boolean) this.m2.invoke(null, aInput, aOutput, true);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}
}