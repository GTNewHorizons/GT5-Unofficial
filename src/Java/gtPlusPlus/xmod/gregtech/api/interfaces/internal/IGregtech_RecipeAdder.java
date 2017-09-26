package gtPlusPlus.xmod.gregtech.api.interfaces.internal;

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
	public boolean addMatterFabricatorRecipe(FluidStack aFluidInput, FluidStack aFluidOutput, int aDuration, int aEUt);

	/**
	 * Adds a Matter Fabricator Recipe
	 *
	 * @param aItemInput = ItemStack Input, can be null I assume.
	 * @param aFluidInput   = fluid Input (can be UU_Amp or null, and respects StackSize)
	 * @param aFluidOutput   = Output of the UU-Matter (not null, and respects StackSize)
	 * @param aDuration 	= Duration (must be >= 0)
	 * @param aEUt			= EU needed for heating up (must be >= 0)
	 * @return true if the Recipe got added, otherwise false.
	 */
	public boolean addMatterFabricatorRecipe(ItemStack aItemInput, FluidStack aFluidInput, FluidStack aFluidOutput, int aDuration, int aEUt);




	/**
	 * Adds a Recipe for the Dehydrator. (up to 9 Outputs)
	 *
	 * @param aInput   = Input itemstack (not null, and respects StackSize)
	 * @param aFluidInput   = fluid Input (can be UU_Amp or null, and respects StackSize)
	 * @param aOutputItems   = Itemstack[] (not null, and respects StackSize)
	 * @param aDuration 	= Duration (must be >= 0)
	 * @param aEUt			= EU needed for heating up (must be >= 0)
	 * @return true if the Recipe got added, otherwise false.
	 */

	public boolean addDehydratorRecipe(ItemStack aInput, FluidStack aFluid, ItemStack[] aOutputItems, int aDuration, int aEUt);
	/*public boolean addDehydratorRecipe(FluidStack aFluid, FluidStack aOutputFluid, ItemStack[] aOutputItems, int aDuration, int aEUt);*/
	/*public boolean addDehydratorRecipe(ItemStack aItemA, ItemStack aItemB, ItemStack[] aOutputItems, int aDuration, int aEUt);
    public boolean addDehydratorRecipe(ItemStack aItemA, ItemStack aItemB, FluidStack aFluid, ItemStack[] aOutputItems, FluidStack aOutputFluid, int aDuration, int aEUt);*/

	/**
	 * Adds a Recipe for the Dehydrator. (up to 9 Outputs)
	 *
	 * @param aInput   = ItemStack[] (not null, and respects StackSize)
	 * @param aFluidInput   = fluid Input (can be UU_Amp or null, and respects StackSize)
	 * @param aFluidOutput   = Output of the UU-Matter (not null, and respects StackSize)
	 * @param aOutputItems   = ItemStack[] (not null, and respects StackSize)
	 * @param aChances 	= Output Change (can be == 0)
	 * @param aDuration 	= Duration (must be >= 0)
	 * @param aEUt			= EU needed for heating up (must be >= 0)
	 * @return true if the Recipe got added, otherwise false.
	 */
	public boolean addDehydratorRecipe(ItemStack[] aInput, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack[] aOutputItems, int[] aChances, int aDuration, int aEUt);


	/**
	 * Adds a Recipe for the Alloy Blast Smelter. (up to 9 Inputs)
	 *
	 * @param aInput   = ItemStack[] (not null, and respects StackSize)
	 * @param aFluidOutput   = Output of the Molten Metal (not null, and respects StackSize)
	 * @param aChances 	= Output Chance (can be == 0)
	 * @param aDuration 	= Duration (must be >= 0)
	 * @param aEUt			= EU per tick needed for heating up (must be >= 0)
	 * @return true if the Recipe got added, otherwise false.
	 */
	public boolean addBlastSmelterRecipe(ItemStack[] aInput, FluidStack aOutput, int aChance, int aDuration, int aEUt);

	/**
	 * Adds a Recipe for the Alloy Blast Smelter. (up to 9 Inputs)
	 *
	 * @param aInput   = ItemStack[] (not null, and respects StackSize)
	 * @param aFluidInput   = Input of a fluid (can be null, and respects StackSize)
	 * @param aFluidOutput   = Output of the Molten Metal (not null, and respects StackSize)
	 * @param aChances 	= Output Chance (can be == 0)
	 * @param aDuration 	= Duration (must be >= 0)
	 * @param aEUt			= EU per tick needed for heating up (must be >= 0)
	 * @return true if the Recipe got added, otherwise false.
	 */
	public boolean addBlastSmelterRecipe(ItemStack[] aInput, FluidStack aInputFluid, FluidStack aOutput, int aChance, int aDuration, int aEUt);

	
	
	public boolean addLFTRRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, int aDuration, int aEUt);
	public boolean addLFTRRecipe(ItemStack aInput1, FluidStack aInput2, ItemStack aOutput1, FluidStack aOutput2, int aDuration, int aEUt);
	public boolean addLFTRRecipe(FluidStack aInput1, FluidStack aInput2, FluidStack aOutput1, int aDuration, int aEUt);


	public boolean addFissionFuel(
			FluidStack aInput1, FluidStack aInput2, FluidStack aInput3,
			FluidStack aInput4, FluidStack aInput5, FluidStack aInput6,
			FluidStack aInput7, FluidStack aInput8, FluidStack aInput9,
			FluidStack aOutput1, FluidStack aOutput2,
			int aDuration, int aEUt);
	
	public boolean addCyclotronRecipe(ItemStack aInput, FluidStack aFluidInput, ItemStack[] aOutputs,
			FluidStack aFluidOutput, int[] aChances, int aDuration, int aEUt, int aSpecialValue);

}
