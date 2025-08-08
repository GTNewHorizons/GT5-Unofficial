package gtPlusPlus.xmod.railcraft.utils;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import mods.railcraft.common.util.crafting.BlastFurnaceCraftingManager;
import mods.railcraft.common.util.crafting.CokeOvenCraftingManager;

public class RailcraftUtils {

    public static void addCokeOvenRecipe(ItemStack input, boolean matchDamage, boolean matchNBT, ItemStack output,
            FluidStack fluidOutput, int cookTime) {
        CokeOvenCraftingManager.getInstance().addRecipe(input, matchDamage, matchNBT, output, fluidOutput, cookTime);
    }

    public static void addAdvancedCokeOvenRecipe(ItemStack input, boolean matchDamage, boolean matchNBT,
            ItemStack output, int cookTime) {
        BlastFurnaceCraftingManager.getInstance().addRecipe(input, matchDamage, matchNBT, cookTime, output);
    }
}
