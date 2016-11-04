package gtPlusPlus.core.recipe;

import gregtech.api.enums.ItemList;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.core.util.recipe.RecipeUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import net.minecraft.item.ItemStack;

public class RECIPE_Batteries {

	static ItemStack	RECIPE_Battery_Sodium	= GregtechItemList.Battery_RE_EV_Sodium.get(1);
	static ItemStack	RECIPE_Battery_Cadmium	= GregtechItemList.Battery_RE_EV_Cadmium.get(1);
	static ItemStack	RECIPE_Battery_Lithium	= GregtechItemList.Battery_RE_EV_Lithium.get(1);
	static ItemStack	GT_Battery_Sodium		= ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32539, 1);
	static ItemStack	GT_Battery_Cadmium		= ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32537, 1);
	static ItemStack	GT_Battery_Lithium		= ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32538, 1);
	static ItemStack	machineTransformer_EV;

	public static void RECIPES_LOAD() {

		if (LoadedMods.Gregtech) {
			RECIPE_Batteries.machineTransformer_EV = ItemList.Transformer_EV_HV.get(1);
			RECIPE_Batteries.run();
		}
	}

	private static void run() {

		RecipeUtils.addShapedGregtechRecipe(RECIPE_Batteries.GT_Battery_Sodium, RECIPES_Machines.cableTier4,
				RECIPE_Batteries.GT_Battery_Sodium, RECIPES_Machines.circuitTier3,
				RECIPE_Batteries.machineTransformer_EV, RECIPES_Machines.circuitTier3,
				RECIPE_Batteries.GT_Battery_Sodium, RECIPES_Machines.cableTier4, RECIPE_Batteries.GT_Battery_Sodium,
				RECIPE_Batteries.RECIPE_Battery_Sodium);
		RecipeUtils.addShapedGregtechRecipe(RECIPE_Batteries.GT_Battery_Cadmium, RECIPES_Machines.cableTier4,
				RECIPE_Batteries.GT_Battery_Cadmium, RECIPES_Machines.circuitTier3,
				RECIPE_Batteries.machineTransformer_EV, RECIPES_Machines.circuitTier3,
				RECIPE_Batteries.GT_Battery_Cadmium, RECIPES_Machines.cableTier4, RECIPE_Batteries.GT_Battery_Cadmium,
				RECIPE_Batteries.RECIPE_Battery_Cadmium);
		RecipeUtils.addShapedGregtechRecipe(RECIPE_Batteries.GT_Battery_Lithium, RECIPES_Machines.cableTier4,
				RECIPE_Batteries.GT_Battery_Lithium, RECIPES_Machines.circuitTier3,
				RECIPE_Batteries.machineTransformer_EV, RECIPES_Machines.circuitTier3,
				RECIPE_Batteries.GT_Battery_Lithium, RECIPES_Machines.cableTier4, RECIPE_Batteries.GT_Battery_Lithium,
				RECIPE_Batteries.RECIPE_Battery_Lithium);

	}

}
