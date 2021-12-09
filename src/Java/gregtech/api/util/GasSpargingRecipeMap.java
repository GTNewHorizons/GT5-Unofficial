package gregtech.api.util;

import static gregtech.api.enums.GT_Values.RES_PATH_GUI;

import gtPlusPlus.api.objects.data.AutoMap;
import net.minecraftforge.fluids.FluidStack;

public class GasSpargingRecipeMap extends AutoMap<GasSpargingRecipe>{

	public static final AutoMap<GasSpargingRecipe> mRecipes = new AutoMap<GasSpargingRecipe>();
	public static final String mUnlocalizedName = "gtpp.recipe.lftr.sparging";
	public static final String mNEIName = mUnlocalizedName;
	public static final String mNEIDisplayName = "LFTR Gas Sparging";
	public static final String mNEIGUIPath = RES_PATH_GUI + "basicmachines/FissionFuel.png";
	
	
	public static boolean addRecipe(FluidStack aSpargeGas, FluidStack aSpentFuel, FluidStack[] aOutputs, int[] aMaxOutputs) {
		if (aSpargeGas == null || aSpargeGas.amount <= 0 ||
				aSpentFuel == null || aSpentFuel.amount <= 0 ||
				aOutputs == null || aOutputs.length < 1 ||
				aMaxOutputs == null || aMaxOutputs.length < 1 ||
				aOutputs.length != aMaxOutputs.length) {
			return false;
		}
		int aMapSize = mRecipes.size();
		GasSpargingRecipe aRecipe = new GasSpargingRecipe(
				aSpargeGas,
				aSpentFuel,
				aOutputs,
				aMaxOutputs
				);
		mRecipes.put(aRecipe);
		return mRecipes.size() > aMapSize;
	}
}


