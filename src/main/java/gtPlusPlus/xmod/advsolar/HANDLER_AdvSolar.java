package gtPlusPlus.xmod.advsolar;

import advsolar.common.AdvancedSolarPanel;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.RecipeUtils;

public class HANDLER_AdvSolar {

	public static void postInit() {
		if (LoadedMods.AdvancedSolarPanel) {
			RecipeUtils.removeRecipeByOutput(ItemUtils.getSimpleStack(AdvancedSolarPanel.blockMolecularTransformer));			
		}
	}
	
}
