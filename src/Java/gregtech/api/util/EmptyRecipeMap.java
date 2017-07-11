package gregtech.api.util;

import java.util.Collection;

import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class EmptyRecipeMap extends GT_Recipe_Map{

	public EmptyRecipeMap(Collection<GT_Recipe> aRecipeList, String aUnlocalizedName, String aLocalName, String aNEIName, String aNEIGUIPath, int aUsualInputCount, int aUsualOutputCount, int aMinimalInputItems, int aMinimalInputFluids, int aAmperage, String aNEISpecialValuePre, int aNEISpecialValueMultiplier, String aNEISpecialValuePost, boolean aShowVoltageAmperageInNEI, boolean aNEIAllowed) {
		super(aRecipeList, aUnlocalizedName, aLocalName, aNEIName, aNEIGUIPath, aUsualInputCount, aUsualOutputCount,
				aMinimalInputItems, aMinimalInputFluids, aAmperage, aNEISpecialValuePre, aNEISpecialValueMultiplier,
				aNEISpecialValuePost, aShowVoltageAmperageInNEI, aNEIAllowed);
		
	}

	@Override
	public GT_Recipe addRecipe(boolean aOptimize, ItemStack[] aInputs, ItemStack[] aOutputs, Object aSpecial, int[] aOutputChances, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aSpecialValue) {
		return null;
		
	}

	@Override
	public GT_Recipe addRecipe(int[] aOutputChances, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aSpecialValue) {
		return null;
	}

	@Override
	public GT_Recipe addRecipe(boolean aOptimize, ItemStack[] aInputs, ItemStack[] aOutputs, Object aSpecial, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aSpecialValue) {
		return null;
	}

	@Override
	public GT_Recipe addRecipe(GT_Recipe aRecipe) {
		return null;
	}

	@Override
	protected GT_Recipe addRecipe(GT_Recipe aRecipe, boolean aCheckForCollisions, boolean aFakeRecipe, boolean aHidden) {
		return null;
	}

	@Override
	public GT_Recipe add(GT_Recipe aRecipe) {
		return null;
	}

}
