package gtPlusPlus.core.common.compat;

import gtPlusPlus.core.lib.CORE.configSwitches;
import gtPlusPlus.core.recipe.RECIPES_Tools;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.core.util.recipe.RecipeUtils;
import net.minecraft.item.ItemStack;

public class COMPAT_ExtraUtils {

	public static void OreDict() {
		RECIPES_Tools.RECIPE_DivisionSigil = new ItemStack(ItemUtils.getItem("ExtraUtilities:divisionSigil"));
		COMPAT_ExtraUtils.run();

	}

	private static final void run() {
		ItemUtils.getItemForOreDict("ExtraUtilities:bedrockiumIngot", "ingotBedrockium", "Bedrockium Ingot", 0);
		// GT_OreDictUnificator.registerOre("plateBedrockium", new
		// ItemStack(ModItems.itemPlateBedrockium));

		if (configSwitches.enableAlternativeDivisionSigilRecipe) {
			// Division Sigil
			RecipeUtils.recipeBuilder("plateNetherStar", "gemIridium", "plateNetherStar", "plateIridium",
					RECIPES_Tools.craftingToolHardHammer, "plateIridium", "plateNetherStar", "gemIridium",
					"plateNetherStar", RECIPES_Tools.RECIPE_DivisionSigil);
		}
	}

}
