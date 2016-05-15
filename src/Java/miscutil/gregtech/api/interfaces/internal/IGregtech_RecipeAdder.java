package miscutil.gregtech.api.interfaces.internal;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public interface IGregtech_RecipeAdder {
	/**
	 * Adds a FusionreactorRecipe
	 *
	 * @param aOptimize		= EU needed for heating up (must be >= 0)
	 * @param aInput1       = first Input (can be null, and respects StackSize)
	 * @param aOutput1      = Output of the Creosote (not null, and respects StackSize)
	 * @param bInput1       = first solid Input (not null, and respects StackSize)
	 * @param bOutput1      = Output of the Coal/coke (can be null, and respects StackSize)
	 * @param aDuration 	= Duration (must be >= 0)
	 * @param aEUt			= EU needed for heating up (must be >= 0)
	 * @param aSpecialValue	= EU needed for heating up (must be >= 0)
	 * @return true if the Recipe got added, otherwise false.
	 */
	public boolean addCokeOvenRecipe(boolean aOptimize, ItemStack[] aInputs, ItemStack[] aOutputs, Object aSpecial, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aSpecialValue);

}
