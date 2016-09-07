package gtPlusPlus.core.util.gregtech.recipehandlers;

import gregtech.api.util.GT_ModHandler;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;

import java.lang.reflect.Method;

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
		} catch (NoSuchMethodException e) {
			this.ourProxy = null;
		}
	}

	public boolean addSmeltingAndAlloySmeltingRecipe(ItemStack aInput, ItemStack aOutput) {
		Utils.LOG_INFO("Adding a GT Furnace/Alloy Smelter Recipe");
		return ourProxy.addSmeltingAndAlloySmeltingRecipe(aInput, aOutput);
	}

}

abstract class LibraryProxy { // can also be interface unless you want to have common code here
	abstract public boolean addSmeltingAndAlloySmeltingRecipe(ItemStack aInput, ItemStack aOutput);
}

class LibProxy1 extends LibraryProxy {
	final Method m1;

	public LibProxy1() throws NoSuchMethodException {
		m1 = GT_ModHandler.class.getDeclaredMethod("addSmeltingAndAlloySmeltingRecipe", ItemStack.class, ItemStack.class);
	}

	@Override
	public boolean addSmeltingAndAlloySmeltingRecipe(ItemStack aInput, ItemStack aOutput) {
		try {
			Utils.LOG_INFO("Trying with Gt 5.7/5.8 Method.");
			return (boolean) m1.invoke(null, aInput, aOutput);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}

class LibProxy2 extends LibraryProxy {
	final Method m2;

	public LibProxy2() throws NoSuchMethodException {
		m2 = GT_ModHandler.class.getDeclaredMethod("addSmeltingAndAlloySmeltingRecipe", ItemStack.class, ItemStack.class, boolean.class);
	}

	@Override
	public boolean addSmeltingAndAlloySmeltingRecipe(ItemStack aInput, ItemStack aOutput) {
		try {
			Utils.LOG_INFO("Trying with Gt 5.9 Method.");
			return (boolean) m2.invoke(null, aInput, aOutput, true);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}

/*class Lib { // v1
	public static void addRecipe(ItemStack aInput, ItemStack aOutput) {
		System.out.println("shit totally happened v1");
	}
}

class Lib2 { // v2
    public static void addRecipe(ItemStack aInput, ItemStack aOutput, boolean hidden) {
        System.out.println("shit totally happened v2");
    }
}*/