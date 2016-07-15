package miscutil.core.xmod.gregtech.api.interfaces.internal;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public interface IGregtech_RecipeAdder {
	/**
	 * Adds a Coke Oven Recipe
	 *
	 * @param aInput1       = first Input (not null, and respects StackSize)
	 * @param aInputb       = second Input (can be null, and respects StackSize)
	 * @param aFluidOutput      = Output of the Creosote (not null, and respects StackSize)
	 * @param aFluidInput   = fluid Input (can be null, and respects StackSize)
	 * @param aOutput       = Output of the Coal/coke (can be null, and respects StackSize)
	 * @param aDuration 	= Duration (must be >= 0)
	 * @param aEUt			= EU needed for heating up (must be >= 0)
	 * @return true if the Recipe got added, otherwise false.
	 */
	//public boolean addCokeOvenRecipe(boolean aOptimize, ItemStack[] aInputs, ItemStack[] aOutputs, Object aSpecial, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aSpecialValue);
	public boolean addCokeOvenRecipe(ItemStack aInput1, ItemStack aInput2,	FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput,	int aDuration, int aEUt);

	public boolean addFuel(ItemStack aInput1, ItemStack aOutput1, int aEU, int aType);

	
	/**
	 * Adds a Matter Fabricator Recipe
	 *
	 * @param aFluidOutput   = Output of the UU-Matter (not null, and respects StackSize)
	 * @param aFluidInput   = fluid Input (can be UU_Amp or null, and respects StackSize)
	 * @param aDuration 	= Duration (must be >= 0)
	 * @param aEUt			= EU needed for heating up (must be >= 0)
	 * @return true if the Recipe got added, otherwise false.
	 */
	public boolean addMattrFabricatorRecipe(FluidStack aFluidInput, FluidStack aFluidOutput, int aDuration, int aEUt);

}
