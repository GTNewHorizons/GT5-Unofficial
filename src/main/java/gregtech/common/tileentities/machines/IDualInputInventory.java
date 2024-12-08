package gregtech.common.tileentities.machines;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.util.GTRecipe;

public interface IDualInputInventory {

    ItemStack[] getItemInputs();

    FluidStack[] getFluidInputs();

    MTEHatchCraftingInputME.PatternSlot.recipeInputs getPatternInputs();

    void setPatternRecipe(GTRecipe recipe, int hash);

    GTRecipe getPatternRecipe();

    int getPatternRecipeMapHash();
}
