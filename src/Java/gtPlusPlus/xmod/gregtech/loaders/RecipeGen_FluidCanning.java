package gtPlusPlus.xmod.gregtech.loaders;

import java.util.HashSet;
import java.util.Set;

import gregtech.api.GregTech_API;
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

	public boolean valid() {
		return isValid;
	}
	public RecipeGen_FluidCanning(boolean aExtracting, ItemStack aEmpty, ItemStack aFull, FluidStack aFluid) {
		this(aExtracting, aEmpty, aFull, aFluid, GT_Values.NF, null, null);
	}

	public RecipeGen_FluidCanning(boolean aExtracting, ItemStack aEmpty, ItemStack aFull, FluidStack aFluidIn, FluidStack aFluidOut) {
		this(aExtracting, aEmpty, aFull, aFluidIn, aFluidOut, null, null);
	}

	public RecipeGen_FluidCanning(boolean aExtracting, ItemStack aEmpty, ItemStack aFull, FluidStack aFluid, Integer aDuration, Integer aEUt) {
		this(aExtracting, aEmpty, aFull, aFluid, GT_Values.NF, aDuration, aEUt);
	}

	// Alternative Constructor
	public RecipeGen_FluidCanning(boolean aExtracting, ItemStack aEmpty, ItemStack aFull, FluidStack aFluidIn, FluidStack aFluidOut, Integer aDuration, Integer aEUt) {
		ItemStack aInput;
		ItemStack aOutput;
		FluidStack aFluidInput;
		FluidStack aFluidOutput;

		// Safety check on the duration
		if (aDuration == null || aDuration <= 0) {
			aDuration = (aFluidIn != null) ? (aFluidIn.amount / 62) : ((aFluidOut != null) ? (aFluidOut.amount / 62) : 10);
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
			aFluidInput = GT_Values.NF;
			aFluidOutput = aFluidIn;			
		}
		else {
			aInput = aEmpty;
			aOutput = aFull;		
			aFluidInput = aFluidIn;
			aFluidOutput = aFluidOut != null ? aFluidOut : GT_Values.NF;			
		}

		//Check validity

		Recipe_GT aRecipe = new Recipe_GT(
				true,
				new ItemStack[] { aInput },
				new ItemStack[] { aOutput },
				null,
				new int[] {10000},
				new FluidStack[] { aFluidInput },
				new FluidStack[] { aFluidOutput },
				aDuration,
				aEUt,
				0);


		// Check Valid		
		boolean aTempValidityCheck = false;
		if (aExtracting) {
			if (aInput != null && aFluidOutput != null) {
				aTempValidityCheck = true;
			}
		}
		else {
			if (aInput != null && aOutput != null && (aFluidInput != null || aFluidOutput != null)) {
				aTempValidityCheck = true;				
			}
		}		


		if (aTempValidityCheck) {
			// Valid Recipe
			recipe = aRecipe;
			mRecipeGenMap.add(this);
			disableOptional = aExtracting;
			isValid = true;			
		}
		else {
			isValid = false;
			disableOptional = aExtracting;
			aRecipe.mEnabled = false;
			aRecipe.mHidden = true;
			recipe = null;
		}
	}

	@Override
	public void run() {
		generateRecipes();
	}

	private void generateRecipes() {
		if (isValid && recipe != null) {
			if (this.disableOptional) {
				addFluidExtractionRecipe(recipe);	
			}
			else {
				addFluidCannerRecipe(recipe);
			}
		}		
	}

	private final boolean addFluidExtractionRecipe(GT_Recipe aRecipe) {
		if (aRecipe != null) {			
			int aCount1 = GT_Recipe_Map.sFluidExtractionRecipes.mRecipeList.size();
			int aCount2 = aCount1;
			GT_Recipe_Map.sFluidExtractionRecipes.addRecipe(aRecipe);
			aCount1 = GT_Recipe_Map.sFluidExtractionRecipes.mRecipeList.size();
			return aCount1 > aCount2;
		}
		return false;
	}

	private final boolean addFluidCannerRecipe(GT_Recipe recipe2) {
		if (recipe2 != null) {
			int aCount1 = GT_Recipe_Map.sFluidCannerRecipes.mRecipeList.size();
			int aCount2 = aCount1;
			GT_Recipe_Map.sFluidCannerRecipes.addRecipe(recipe2);
			aCount1 = GT_Recipe_Map.sFluidCannerRecipes.mRecipeList.size();
			return aCount1 > aCount2;
		}
		return false;
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
