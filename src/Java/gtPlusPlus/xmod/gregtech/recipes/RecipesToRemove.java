package gtPlusPlus.xmod.gregtech.recipes;

import gregtech.api.enums.Materials;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.Recipe_GT;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.material.ELEMENT;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class RecipesToRemove {

	public static void go() {
		
		Logger.INFO("Processing Gregtech recipe maps, removing recipes to suit GT++.");
		
		//Remove Rare Earth Centrifuging 
		//1 Rare Earth Dust - 25% chance for small piles of: neodymium, yttrium, lanthanum, cerium, cadmium, and caesium
		//Replaced by advanced sifting recipe.		
		GT_Recipe aRareEarthCentrifuging = GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes.findRecipe(null, false, 20, new FluidStack[] {}, new ItemStack[] {ItemUtils.getItemStackOfAmountFromOreDict("dustRareEarth", 1)});
		if (aRareEarthCentrifuging != null && aRareEarthCentrifuging.mEnabled) {
			aRareEarthCentrifuging.mEnabled = false;
			aRareEarthCentrifuging.mHidden = true;
			GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes.mRecipeList.remove(aRareEarthCentrifuging);
			Recipe_GT.GT_Recipe_Map_LargeCentrifuge.sMultiblockCentrifugeRecipes.mRecipeList.remove(aRareEarthCentrifuging);
			Recipe_GT.GT_Recipe_Map_LargeCentrifuge.sMultiblockCentrifugeRecipes_GT.mRecipeList.remove(aRareEarthCentrifuging);
			Logger.INFO("Removed vanilla GT Rare Earth processing.");			
			//Set the Chemical Symbol for Rare Earth now that we are giving it custom outputs
			//Best not to set this unless the original recipe is removed.
			Materials.RareEarth.mChemicalFormula = "("
					+ELEMENT.getInstance().YTTRIUM.vChemicalSymbol
					+ELEMENT.getInstance().NEODYMIUM.vChemicalSymbol
					+ELEMENT.getInstance().LANTHANUM.vChemicalSymbol
					+ELEMENT.getInstance().CERIUM.vChemicalSymbol
					+ELEMENT.getInstance().CADMIUM.vChemicalSymbol
					+ELEMENT.getInstance().CAESIUM.vChemicalSymbol
					+ELEMENT.getInstance().YTTERBIUM.vChemicalSymbol
					+ELEMENT.getInstance().SAMARIUM.vChemicalSymbol
					+ELEMENT.getInstance().GADOLINIUM.vChemicalSymbol+
					")";	
		}
		
	}
	
}
