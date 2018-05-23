package gtPlusPlus.xmod.gregtech.loaders;

import java.util.HashSet;
import java.util.Set;

import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_Recipe;

import gtPlusPlus.api.interfaces.RunnableWithInfo;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialGenerator;

public class RecipeGen_FluidCanning extends RecipeGen_Base {

	public final static Set<RunnableWithInfo<Material>> mRecipeGenMap = new HashSet<RunnableWithInfo<Material>>();
	static {
		MaterialGenerator.mRecipeMapsToGenerate.put(mRecipeGenMap);
	}
	
	private final GT_Recipe recipe;
	public RecipeGen_FluidCanning(GT_Recipe g) {
		this(g, true);
	}
	public RecipeGen_FluidCanning(GT_Recipe g, boolean extracting) {
		recipe = g;
		mRecipeGenMap.add(this);
		disableOptional = extracting;
	}

	@Override
	public void run() {
		generateRecipes();
	}

	private void generateRecipes() {
		if (recipe != null) {			
			//Used to store Fluid extraction state
			if (this.disableOptional) {
				GT_Values.RA.addFluidExtractionRecipe(
						recipe.mInputs[0], //Input
						recipe.mInputs[1], //Input 2
						recipe.mFluidOutputs[0], //Fluid Output
						recipe.mSpecialValue, //Chance
						recipe.mDuration, //Duration
						recipe.mEUt //Eu Tick
						);	
			}
			else {
				GT_Values.RA.addFluidCannerRecipe(
						recipe.mInputs[0], //Input
						recipe.mOutputs[0], //Input 2
						recipe.mFluidInputs[0], //Fluid Input
						recipe.mFluidOutputs[0] //Fluid Output
						);
				
			}
			
		}		
	}

}
