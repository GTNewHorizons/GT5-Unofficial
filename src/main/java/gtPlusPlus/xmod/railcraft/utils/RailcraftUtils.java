package gtPlusPlus.xmod.railcraft.utils;

import mods.railcraft.common.util.crafting.CokeOvenCraftingManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class RailcraftUtils {

	public static void addCokeOvenRecipe(ItemStack input, boolean matchDamage, boolean matchNBT, ItemStack output, FluidStack fluidOutput, int cookTime) {
		CokeOvenCraftingManager.getInstance().addRecipe(input, matchDamage, matchNBT, output, fluidOutput, cookTime);
	}
	
}
