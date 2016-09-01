package miscutil.core.util.gregtech.five;

import miscutil.core.lib.CORE;
import net.minecraft.item.ItemStack;

public class GregtechVersionRecipeHandler {
	
	public static final boolean mainFork = isExperimentalVersion();

	public static boolean isExperimentalVersion(){
		return CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK;
	}
	
	public static void addSmeltingAndAlloySmeltingRecipe(ItemStack aInput, ItemStack aOutput){
		if (mainFork){
			miscutil.core.util.gregtech.five.nine.AddSmeltingAndAlloySmeltingRecipe.run(aInput, aOutput, false);
		}
		else {
			miscutil.core.util.gregtech.five.eight.AddSmeltingAndAlloySmeltingRecipe.run(aInput, aOutput);
		}
	}
	
}
