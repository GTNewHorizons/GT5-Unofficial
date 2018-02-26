package gtPlusPlus.core.recipe;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.ItemList;

import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.RecipeUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public class RECIPE_Batteries {

	static ItemStack RECIPE_Battery_Sodium = GregtechItemList.Battery_RE_EV_Sodium.get(1);
	static ItemStack RECIPE_Battery_Cadmium = GregtechItemList.Battery_RE_EV_Cadmium.get(1);
	static ItemStack RECIPE_Battery_Lithium = GregtechItemList.Battery_RE_EV_Lithium.get(1);
	static ItemStack GT_Battery_Sodium = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32539, 1);
	static ItemStack GT_Battery_Cadmium = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32537, 1);
	static ItemStack GT_Battery_Lithium = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32538, 1);
	static ItemStack machineTransformer_EV;

	public static void loadRecipes(){

		if (LoadedMods.Gregtech){
			machineTransformer_EV = ItemList.Transformer_EV_HV.get(1);
			run();
		}
	}

	private static void run(){


		RecipeUtils.addShapedGregtechRecipe(
				GT_Battery_Sodium, RECIPES_Machines.cableTier4, GT_Battery_Sodium,
				CI.circuitTier3, machineTransformer_EV, CI.circuitTier3,
				GT_Battery_Sodium, RECIPES_Machines.cableTier4, GT_Battery_Sodium,
				RECIPE_Battery_Sodium);
		RecipeUtils.addShapedGregtechRecipe(
				GT_Battery_Cadmium, RECIPES_Machines.cableTier4, GT_Battery_Cadmium,
				CI.circuitTier3, machineTransformer_EV, CI.circuitTier3,
				GT_Battery_Cadmium, RECIPES_Machines.cableTier4, GT_Battery_Cadmium,
				RECIPE_Battery_Cadmium);
		RecipeUtils.addShapedGregtechRecipe(
				GT_Battery_Lithium, RECIPES_Machines.cableTier4, GT_Battery_Lithium,
				CI.circuitTier3, machineTransformer_EV, CI.circuitTier3,
				GT_Battery_Lithium, RECIPES_Machines.cableTier4, GT_Battery_Lithium,
				RECIPE_Battery_Lithium);

	}

}
