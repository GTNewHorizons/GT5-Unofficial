package gtPlusPlus.core.recipe;

import gregtech.api.enums.ItemList;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.util.item.UtilsItems;
import gtPlusPlus.core.util.recipe.UtilsRecipe;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class RECIPES_General {

	static ItemStack RECIPE_Paper = UtilsItems.getSimpleStack(Items.paper);
	static ItemStack RECIPE_LapisDust = UtilsItems.getItemStackOfAmountFromOreDictNoBroken("dustLazurite", 2);
	static ItemStack OUTPUT_Blueprint = UtilsItems.getSimpleStack(ModItems.itemBlueprintBase);
	static ItemStack RECIPE_CraftingTable = UtilsItems.getSimpleStack(Item.getItemFromBlock(Blocks.crafting_table));
	static ItemStack RECIPE_BronzePlate = UtilsItems.getItemStackOfAmountFromOreDictNoBroken("plateBronze", 1);
	static ItemStack RECIPE_BasicCasingIC2;
	static ItemStack OUTPUT_Workbench_Bronze = UtilsItems.getSimpleStack(Item.getItemFromBlock(ModBlocks.blockWorkbench));
	static ItemStack NULL = null;

	public static void RECIPES_LOAD(){
		
		if (LoadedMods.Gregtech){
			RECIPE_BasicCasingIC2 = ItemList.Casing_Gearbox_Bronze.get(1);
		run();
		}
	}
	
	private static void run(){
		
		
		UtilsRecipe.recipeBuilder(
				RECIPE_Paper, RECIPE_LapisDust, NULL,
				RECIPE_Paper, RECIPE_LapisDust, NULL,
				RECIPE_LapisDust, RECIPE_LapisDust, NULL,
				OUTPUT_Blueprint);	
		
		UtilsRecipe.recipeBuilder(
				RECIPE_BronzePlate, RECIPE_CraftingTable, RECIPE_BronzePlate,
				RECIPE_BronzePlate, RECIPE_BasicCasingIC2, RECIPE_BronzePlate,
				RECIPE_BronzePlate, RECIPE_BronzePlate, RECIPE_BronzePlate,
				OUTPUT_Workbench_Bronze);
	}
	
}
