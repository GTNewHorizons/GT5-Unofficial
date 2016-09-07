package gtPlusPlus.core.recipe;

import gregtech.api.enums.ItemList;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.util.item.UtilsItems;
import gtPlusPlus.core.util.recipe.UtilsRecipe;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import net.minecraft.item.ItemStack;

public class RECIPE_Batteries {
	
	static ItemStack RECIPE_Battery_Sodium = GregtechItemList.Battery_RE_EV_Sodium.get(1);
	static ItemStack RECIPE_Battery_Cadmium = GregtechItemList.Battery_RE_EV_Cadmium.get(1);
	static ItemStack RECIPE_Battery_Lithium = GregtechItemList.Battery_RE_EV_Lithium.get(1);
	static ItemStack GT_Battery_Sodium = UtilsItems.simpleMetaStack("gregtech:gt.metaitem.01", 32539, 1);
	static ItemStack GT_Battery_Cadmium = UtilsItems.simpleMetaStack("gregtech:gt.metaitem.01", 32537, 1);
	static ItemStack GT_Battery_Lithium = UtilsItems.simpleMetaStack("gregtech:gt.metaitem.01", 32538, 1);
	static ItemStack machineTransformer_EV;

	public static void RECIPES_LOAD(){
		
		if (LoadedMods.Gregtech){
			machineTransformer_EV = ItemList.Transformer_EV_HV.get(1);
		run();
		}
	}
	
	private static void run(){
		
		
		UtilsRecipe.addShapedGregtechRecipe(
				GT_Battery_Sodium, RECIPES_Machines.cableTier4, GT_Battery_Sodium,
				RECIPES_Machines.circuitTier3, machineTransformer_EV, RECIPES_Machines.circuitTier3,
				GT_Battery_Sodium, RECIPES_Machines.cableTier4, GT_Battery_Sodium,
				RECIPE_Battery_Sodium);
		UtilsRecipe.addShapedGregtechRecipe(
				GT_Battery_Cadmium, RECIPES_Machines.cableTier4, GT_Battery_Cadmium,
				RECIPES_Machines.circuitTier3, machineTransformer_EV, RECIPES_Machines.circuitTier3,
				GT_Battery_Cadmium, RECIPES_Machines.cableTier4, GT_Battery_Cadmium,
				RECIPE_Battery_Cadmium);
		UtilsRecipe.addShapedGregtechRecipe(
				GT_Battery_Lithium, RECIPES_Machines.cableTier4, GT_Battery_Lithium,
				RECIPES_Machines.circuitTier3, machineTransformer_EV, RECIPES_Machines.circuitTier3,
				GT_Battery_Lithium, RECIPES_Machines.cableTier4, GT_Battery_Lithium,
				RECIPE_Battery_Lithium);
		
	}
	
}
