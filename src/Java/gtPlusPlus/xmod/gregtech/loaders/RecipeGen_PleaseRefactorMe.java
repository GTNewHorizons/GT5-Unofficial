package gtPlusPlus.xmod.gregtech.loaders;

import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.recipe.UtilsRecipe;

public class RecipeGen_PleaseRefactorMe {

	public static void generateRecipes(Material material){
		int tVoltageMultiplier = material.getMeltingPoint_K() >= 2800 ? 64 : 16;
		Utils.LOG_INFO("Generating Shaped Crafting recipes for "+material.getLocalizedName()); //TODO
		//Ring Recipe
		
		if (UtilsRecipe.addShapedGregtechRecipe(
				"craftingToolWrench", null, null,
				null, material.getRod(1), null,
				null, null, null,
				material.getRing(1))){
			Utils.LOG_INFO("Ring Recipe: "+material.getLocalizedName()+" - Success");
		}
		else {
			Utils.LOG_INFO("Ring Recipe: "+material.getLocalizedName()+" - Failed");			
		}



	}
}
