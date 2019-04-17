package gtPlusPlus.xmod.gregtech.recipes;

import gregtech.api.util.GT_Recipe;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class RecipesToRemove {

	public static void go() {
		
		//Remove Rare Earth Centrifuging 
		//1 Rare Earth Dust - 25% chance for small piles of: neodymium, yttrium, lanthanum, cerium, cadmium, and caesium
		//Replaced by advanced sifting recipe.		
		GT_Recipe aRareEarthCentrifuging = GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes.findRecipe(null, false, 20, new FluidStack[] {}, new ItemStack[] {ItemUtils.getItemStackOfAmountFromOreDict("dustRareEarth", 1)});
		if (aRareEarthCentrifuging != null && aRareEarthCentrifuging.mEnabled) {
			GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes.mRecipeList.remove(null);
		}
		
	}
	
}
