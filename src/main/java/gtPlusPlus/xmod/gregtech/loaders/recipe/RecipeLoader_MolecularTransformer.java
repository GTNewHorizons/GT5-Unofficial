package gtPlusPlus.xmod.gregtech.loaders.recipe;

import static advsolar.utils.MTRecipeManager.transformerRecipes;

import advsolar.utils.MTRecipeRecord;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.MaterialUtils;

public class RecipeLoader_MolecularTransformer {

	public static void run() {
		
	    for (int i = 0; i < transformerRecipes.size(); i++) {
	    	MTRecipeRecord aRecipe = transformerRecipes.get(i);
	    	int aEU = MaterialUtils.getVoltageForTier(5);
	    	Logger.INFO("=======================");
	    	Logger.INFO("Generating GT recipe for Molecular Transformer.");
	    	Logger.INFO("Input: "+aRecipe.inputStack.getDisplayName()+", Output: "+aRecipe.outputStack.getDisplayName()+", EU/t: "+aEU);
	    	float aTicks = (float) aRecipe.energyPerOperation / (float) aEU;	
	    	Logger.INFO("Ticks: "+aTicks);
	    	int aTicksRoundedUp = MathUtils.roundToClosestInt(Math.ceil(aTicks));
	    	Logger.INFO("Ticks: "+aTicksRoundedUp);
	    	Logger.INFO("Total EU equal or greater? "+((aTicksRoundedUp * aEU) >= aRecipe.energyPerOperation));
	    	CORE.RA.addMolecularTransformerRecipe(aRecipe.inputStack, aRecipe.outputStack, aTicksRoundedUp, aEU, 2);
	    	Logger.INFO("=======================");
	    }
		
	}
	
}
