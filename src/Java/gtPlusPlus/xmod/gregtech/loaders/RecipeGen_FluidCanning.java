package gtPlusPlus.xmod.gregtech.loaders;

import java.util.HashSet;
import java.util.Set;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gregtech.api.util.Recipe_GT;
import gtPlusPlus.api.interfaces.RunnableWithInfo;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialGenerator;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class RecipeGen_FluidCanning extends RecipeGen_Base {

	public final static Set<RunnableWithInfo<Material>> mRecipeGenMap = new HashSet<RunnableWithInfo<Material>>();
	static {
		MaterialGenerator.mRecipeMapsToGenerate.put(mRecipeGenMap);
	}

	private final GT_Recipe recipe;
	private final boolean isValid;

	// Alternative Constructor
	public RecipeGen_FluidCanning(boolean aExtracting, ItemStack aEmpty, ItemStack aFull, FluidStack aFluid, Integer aDuration, Integer aEUt) {
		ItemStack aInput;
		ItemStack aOutput;
		FluidStack aFluidInput;
		FluidStack aFluidOutput;

		// Safety check on the duration
		if (aDuration == null || aDuration <= 0) {
			aDuration = (aFluid != null) ? (aFluid.amount / 62) : (1000 / 62);
		}

		// Safety check on the EU
		if (aEUt == null || aEUt <= 0) {
			if (aExtracting) {
				aEUt = 2;
			}
			else {
				aEUt = 1;
			}
		}

		// Set Item stacks correctly, invert if extraction recipe.
		if (aExtracting) {
			aInput = aFull;
			aOutput = aEmpty;
			aFluidInput = null;
			aFluidOutput = aFluid;			
		}
		else {
			aInput = aEmpty;
			aOutput = aFull;
			aFluidInput = aFluid;
			aFluidOutput = null;			
		}

		//Check validity

		Recipe_GT aRecipe = new Recipe_GT(
				true,
				new ItemStack[] { aInput },
				new ItemStack[] { aOutput },
				null,
				new int[] {},
				new FluidStack[] { aFluidInput },
				new FluidStack[] { aFluidOutput },
				aDuration,
				1,
				0);


		// Not Valid
		if ((aExtracting && (aInput == null || aOutput == null ||aFluidOutput == null)) || (!aExtracting && (aInput == null || aOutput == null || aFluidInput == null))) {
			isValid = false;
			disableOptional = aExtracting;
			recipe = null;
		}
		else {
			// Valid Recipe
			recipe = aRecipe;
			mRecipeGenMap.add(this);
			disableOptional = aExtracting;
			isValid = true;
		}




	}

	@Override
	public void run() {
		generateRecipes();
	}

	private void generateRecipes() {
		if (isValid && recipe != null) {			
			//Used to store Fluid extraction state
			if (this.disableOptional) {
				addFluidExtractionRecipe(
						recipe.mInputs.length >= 1 ? recipe.mInputs[0] : null, //Input
								recipe.mInputs.length == 2 ? recipe.mInputs[1] : null, //Input 2
										recipe.mFluidOutputs.length == 1 ? recipe.mFluidOutputs[0] : null, //Fluid Output
												recipe.mDuration, //Duration
												recipe.mEUt //Eu Tick
						);	
			}
			else {
				addFluidCannerRecipe(
						recipe.mInputs.length == 1 ? recipe.mInputs[0] : null, //Input
								recipe.mOutputs.length == 1 ? recipe.mOutputs[0] : null, //Input 2
										recipe.mFluidInputs.length == 1 ? recipe.mFluidInputs[0] : null //Fluid Input
						);

			}

		}		
	}

	private final boolean addFluidExtractionRecipe(final ItemStack aInput, final ItemStack aRemains, FluidStack aOutput, int aDuration, final int aEUt) {
		if (aInput == null || aOutput == null) {
			return false;
		}
		if (aOutput.isFluidEqual(Materials.PhasedGold.getMolten(1L))) {
			aOutput = Materials.VibrantAlloy.getMolten(aOutput.amount);
		}
		if (aOutput.isFluidEqual(Materials.PhasedIron.getMolten(1L))) {
			aOutput = Materials.PulsatingIron.getMolten(aOutput.amount);
		}
		//Logger.INFO(buildLogString());
		boolean result = GT_Values.RA.addFluidExtractionRecipe(aInput, aRemains, aOutput, 10000, aDuration, aEUt);
		//Logger.INFO(buildLogString());
		//dumpStack();
		return result;
	}

	public final boolean addFluidCannerRecipe(final ItemStack aInput, final ItemStack aOutput, FluidStack aFluidInput) {
		if (aInput == null || aOutput == null || aFluidInput == null) {
			return false;
		}
		if (aFluidInput.isFluidEqual(Materials.PhasedGold.getMolten(1L))) {
			aFluidInput = Materials.VibrantAlloy.getMolten(aFluidInput.amount);
		}
		if (aFluidInput.isFluidEqual(Materials.PhasedIron.getMolten(1L))) {
			aFluidInput = Materials.PulsatingIron.getMolten(aFluidInput.amount);
		}
		//Logger.INFO(buildLogString());
		boolean result = GT_Values.RA.addFluidCannerRecipe(aInput, aOutput, aFluidInput, GT_Values.NF);
		//Logger.INFO(buildLogString());
		//dumpStack();
		return result;
	}	
	
	private void dumpStack() {
		int parents = 2;
		for (int i=0;i<6;i++) {
			Logger.INFO((disableOptional ? "EXTRACTING" : "CANNING")+" DEBUG | "+(i == 0 ? "Called from: " : "Parent: ")+ReflectionUtils.getMethodName(i+parents));			
		}
		
	}
	
	private String buildLogString() {
		int solidSize = getMapSize(GT_Recipe_Map.sCannerRecipes);
		int fluidSize = getMapSize(GT_Recipe_Map.sFluidCannerRecipes);		
		return (disableOptional ? "EXTRACTING" : "CANNING")+" DEBUG | Solids: "+solidSize+" | Liquids: "+fluidSize+" | ";
	}
	
	private final int getMapSize(GT_Recipe_Map aMap) {
		return aMap.mRecipeList.size();
	}

}
