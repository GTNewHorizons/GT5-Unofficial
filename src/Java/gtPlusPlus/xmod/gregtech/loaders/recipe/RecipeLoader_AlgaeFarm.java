package gtPlusPlus.xmod.gregtech.loaders.recipe;

import java.util.HashMap;

import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.Recipe_GT;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.objects.data.WeightedCollection;
import gtPlusPlus.core.item.chemistry.AgriculturalChem;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class RecipeLoader_AlgaeFarm {

	private static final HashMap<Integer, AutoMap<GT_Recipe>> mRecipeCache = new HashMap<Integer, AutoMap<GT_Recipe>>();
	private static final HashMap<Integer, AutoMap<GT_Recipe>> mRecipeCompostCache = new HashMap<Integer, AutoMap<GT_Recipe>>();

	public static final void generateRecipes() {
		for (int i=0;i<10;i++) {
			getTieredRecipeFromCache(i, false);
		}
		for (int i=0;i<10;i++) {
			getTieredRecipeFromCache(i, true);
		}
	}	

	public static GT_Recipe getTieredRecipeFromCache(int aTier, boolean aCompost) {
		HashMap<Integer, AutoMap<GT_Recipe>> aMap = aCompost ? mRecipeCompostCache : mRecipeCache;
		String aComp = aCompost ? "(Compost)" : "";

		AutoMap<GT_Recipe> aTemp = aMap.get(aTier);
		if (aTemp == null || aTemp.isEmpty()) {
			aTemp = new AutoMap<GT_Recipe>();
			aMap.put(aTier, aTemp);	
			Logger.INFO("Tier "+aTier+aComp+" had no recipes, initialising new map.");
		}		
		if (aTemp.size() < 500) {
			Logger.INFO("Tier "+aTier+aComp+" has less than 500 recipes, generating "+(500 - aTemp.size())+".");
			for (int i=aTemp.size();i<500;i++) {
				aTemp.put(generateBaseRecipe(aCompost, aTier));
			}
		}		
		int aIndex = MathUtils.randInt(0, aTemp.isEmpty() ? 1 : aTemp.size());
		Logger.INFO("Using recipe with index of "+aIndex+". "+aComp);
		return aTemp.get(aIndex);
	}

	private static GT_Recipe generateBaseRecipe(boolean aUsingCompost, int aTier) {

		// Type Safety
		if (aTier < 0) {
			return null;
		}

		WeightedCollection<Float> aOutputTimeMulti = new WeightedCollection<Float>();
		for (int i=100;i> 0;i--) {
			float aValue = 0;
			if (i < 10) {
				aValue = 3f;
			}
			else if (i < 20) {
				aValue = 2f;
			}
			else {
				aValue = 1f;				
			}
			aOutputTimeMulti.put(i, aValue);			
		}

		final int[] aDurations = new int[] {
				432000,					
				378000,				
				216000, 
				162000,				
				108000,
				81000,				
				54000,
				40500,				
				27000,
				20250,
				13500, 
				6750, 
				3375,
				1686,
				843, 
				421
		};

		ItemStack[] aInputs = new ItemStack[] {};	

		if (aUsingCompost) {
			// Make it use 4 compost per tier if we have some available
			ItemStack aCompost = ItemUtils.getSimpleStack(AgriculturalChem.mCompost, aTier * 4);
			aInputs = new ItemStack[] {aCompost};
			// Boost Tier by one if using compost so it gets a speed boost
			aTier++;
		}

		// We set these elsewhere
		ItemStack[] aOutputs = getOutputsForTier(aTier);

		GT_Recipe tRecipe = new Recipe_GT(
				false, 
				aInputs,
				aOutputs,
				(Object) null, 
				new int[] {}, 
				new FluidStack[] {GT_Values.NF},
				new FluidStack[] {GT_Values.NF},
				(int) (aDurations[aTier] * aOutputTimeMulti.get()), // Time
				0,
				0);
		
		tRecipe.mSpecialValue = tRecipe.hashCode();

		return tRecipe;
	}
	
	private static ItemStack[] getOutputsForTier(int aTier) {
		
		// Create an Automap to dump contents into
		AutoMap<ItemStack> aOutputMap = new AutoMap<ItemStack>();
		
		// Add loot relevant to tier and also add any from lower tiers.
		if (aTier >= 0) {
			aOutputMap.put(ItemUtils.getSimpleStack(AgriculturalChem.mAlgaeBiosmass, MathUtils.randInt(16, 32)));
			aOutputMap.put(ItemUtils.getSimpleStack(AgriculturalChem.mAlgaeBiosmass, MathUtils.randInt(32, 64)));			
			if (MathUtils.randInt(0, 10) > 9) {
				aOutputMap.put(ItemUtils.getSimpleStack(AgriculturalChem.mGreenAlgaeBiosmass, MathUtils.randInt(8, 16)));				
			}
		}
		if (aTier >= 1) {
			aOutputMap.put(ItemUtils.getSimpleStack(AgriculturalChem.mAlgaeBiosmass, MathUtils.randInt(16, 32)));
			aOutputMap.put(ItemUtils.getSimpleStack(AgriculturalChem.mGreenAlgaeBiosmass, MathUtils.randInt(16, 32)));			
			if (MathUtils.randInt(0, 10) > 9) {
				aOutputMap.put(ItemUtils.getSimpleStack(AgriculturalChem.mGreenAlgaeBiosmass, MathUtils.randInt(4, 8)));				
			}
		}
		if (aTier >= 2) {
			aOutputMap.put(ItemUtils.getSimpleStack(AgriculturalChem.mGreenAlgaeBiosmass, MathUtils.randInt(8, 16)));
			aOutputMap.put(ItemUtils.getSimpleStack(AgriculturalChem.mGreenAlgaeBiosmass, MathUtils.randInt(16, 32)));			
			if (MathUtils.randInt(0, 10) > 9) {
				aOutputMap.put(ItemUtils.getSimpleStack(AgriculturalChem.mGreenAlgaeBiosmass, MathUtils.randInt(4, 8)));				
			}
		}
		if (aTier >= 3) {
			aOutputMap.put(ItemUtils.getSimpleStack(AgriculturalChem.mGreenAlgaeBiosmass, MathUtils.randInt(16, 32)));
			aOutputMap.put(ItemUtils.getSimpleStack(AgriculturalChem.mBrownAlgaeBiosmass, MathUtils.randInt(2, 8)));			
			if (MathUtils.randInt(0, 10) > 9) {
				aOutputMap.put(ItemUtils.getSimpleStack(AgriculturalChem.mBrownAlgaeBiosmass, MathUtils.randInt(8, 16)));				
			}
		}
		if (aTier >= 4) {
			aOutputMap.put(ItemUtils.getSimpleStack(AgriculturalChem.mBrownAlgaeBiosmass, MathUtils.randInt(16, 32)));
			aOutputMap.put(ItemUtils.getSimpleStack(AgriculturalChem.mBrownAlgaeBiosmass, MathUtils.randInt(32, 64)));			
			if (MathUtils.randInt(0, 10) > 9) {
				aOutputMap.put(ItemUtils.getSimpleStack(AgriculturalChem.mGoldenBrownAlgaeBiosmass, MathUtils.randInt(4, 8)));				
			}
		}
		if (aTier >= 5) {
			aOutputMap.put(ItemUtils.getSimpleStack(AgriculturalChem.mBrownAlgaeBiosmass, MathUtils.randInt(16, 32)));
			aOutputMap.put(ItemUtils.getSimpleStack(AgriculturalChem.mGoldenBrownAlgaeBiosmass, MathUtils.randInt(16, 32)));			
			if (MathUtils.randInt(0, 10) > 9) {
				aOutputMap.put(ItemUtils.getSimpleStack(AgriculturalChem.mRedAlgaeBiosmass, MathUtils.randInt(1, 2)));				
			}
		}
		// Tier 6 is Highest for outputs
		if (aTier >= 6) {
			aOutputMap.put(ItemUtils.getSimpleStack(AgriculturalChem.mGoldenBrownAlgaeBiosmass, MathUtils.randInt(16, 32)));
			aOutputMap.put(ItemUtils.getSimpleStack(AgriculturalChem.mRedAlgaeBiosmass, MathUtils.randInt(8, 16)));			
			if (MathUtils.randInt(0, 10) > 9) {
				aOutputMap.put(ItemUtils.getSimpleStack(AgriculturalChem.mRedAlgaeBiosmass, MathUtils.randInt(8, 16)));				
			}
		}
		
		// Iterate a special loop at higher tiers to provide more Red/Gold Algae.
		for (int i=0;i<(9-aTier);i++) {
			if (aTier >= (6+i)) {
				int aMulti = i + 1;
				aOutputMap.put(ItemUtils.getSimpleStack(AgriculturalChem.mGoldenBrownAlgaeBiosmass, MathUtils.randInt(4, 8*aMulti)));
				aOutputMap.put(ItemUtils.getSimpleStack(AgriculturalChem.mRedAlgaeBiosmass, MathUtils.randInt(4, 8*aMulti)));			
				if (MathUtils.randInt(0, 10) > 8) {
					aOutputMap.put(ItemUtils.getSimpleStack(AgriculturalChem.mRedAlgaeBiosmass, MathUtils.randInt(8, 16*aMulti)));				
				}
			}
		}
		
		// Map the AutoMap contents to an Itemstack Array.
		ItemStack[] aOutputs = new ItemStack[aOutputMap.size()];
		for (int i=0;i<aOutputMap.size();i++) {
			aOutputs[i] = aOutputMap.get(i);
		}	
		
		// Return filled ItemStack Array.
		return aOutputs;
	}
	
}
