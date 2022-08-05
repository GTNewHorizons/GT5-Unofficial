package gtPlusPlus.nei;

import codechicken.nei.recipe.TemplateRecipeHandler;
import gregtech.api.util.GTPP_Recipe.GTPP_Recipe_Map;

public class GT_NEI_FluidReactor extends GTPP_NEI_DefaultHandler {

	public GT_NEI_FluidReactor() {
		super(GTPP_Recipe_Map.sChemicalPlantRecipes);
	}

	@Override
	public TemplateRecipeHandler newInstance() {
		return new GT_NEI_FluidReactor();
	}

	@Override
	protected String getSpecialInfo(int specialValue) {
		String aTierMaterial = " - ";
		if (specialValue <= 0) {
			aTierMaterial += "Bronze";
		}
		else if (specialValue == 1) {
			aTierMaterial += "Steel";
		}
		else if (specialValue == 2) {
			aTierMaterial += "Aluminium";
		}
		else if (specialValue == 3) {
			aTierMaterial += "Stainless Steel";
		}
		else if (specialValue == 4) {
			aTierMaterial += "Titanium";
		}
		else if (specialValue == 5) {
			aTierMaterial += "Tungsten Steel";
		}
		else if (specialValue == 6) {
			aTierMaterial += "Laurenium";
		}
		else if (specialValue == 7) {
			aTierMaterial += "Botmium";
		}
		return this.mRecipeMap.mNEISpecialValuePre + (specialValue * this.mRecipeMap.mNEISpecialValueMultiplier) + aTierMaterial;
	}
}
