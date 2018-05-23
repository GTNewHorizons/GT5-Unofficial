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
					recipe.mInputs.length == 1 ? recipe.mInputs[0] : null, //Input
					recipe.mInputs.length == 2 ? recipe.mInputs[1] : null, //Input 2
					recipe.mFluidOutputs.length == 1 ? recipe.mFluidOutputs[0] : null, //Fluid Output
					recipe.mSpecialValue, //Chance
					recipe.mDuration, //Duration
					recipe.mEUt //Eu Tick
					);	
			}
			else {
				GT_Values.RA.addFluidCannerRecipe(
					recipe.mInputs.length == 1 ? recipe.mInputs[0] : null, //Input
					recipe.mOutputs.length == 1 ? recipe.mOutputs[0] : null, //Input 2
					recipe.mFluidInputs.length == 1 ? recipe.mFluidInputs[0] : null, //Fluid Input
					recipe.mFluidOutputs.length == 1 ? recipe.mFluidOutputs[0] : null //Fluid Output
					);

			}

		}		
	}

}
