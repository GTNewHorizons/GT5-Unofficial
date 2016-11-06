package gtPlusPlus.core.recipe;

import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.core.util.recipe.RecipeUtils;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class RECIPES_General {

	static ItemStack RECIPE_Paper = ItemUtils.getSimpleStack(Items.paper);
	static String RECIPE_LapisDust = "dustLazurite";
	static ItemStack OUTPUT_Blueprint = ItemUtils.getSimpleStack(ModItems.itemBlueprintBase);
	static ItemStack RECIPE_CraftingTable = ItemUtils.getSimpleStack(Item.getItemFromBlock(Blocks.crafting_table));
	static String RECIPE_BronzePlate = "plateAnyBronze";
	static ItemStack RECIPE_BasicCasingIC2;
	static ItemStack OUTPUT_Workbench_Bronze = ItemUtils.getSimpleStack(Item.getItemFromBlock(ModBlocks.blockWorkbench));
	static ItemStack NULL = null;

	public static void RECIPES_LOAD(){
		
		if (LoadedMods.Gregtech){
			RECIPE_BasicCasingIC2 = ItemUtils.getItemStack("IC2:blockMachine", 1);
		run();
		}
	}
	
	private static void run(){
		
		
		RecipeUtils.recipeBuilder(
				RECIPE_Paper, RECIPE_LapisDust, NULL,
				RECIPE_Paper, RECIPE_LapisDust, NULL,
				RECIPE_LapisDust, RECIPE_LapisDust, NULL,
				OUTPUT_Blueprint);	
		
		RecipeUtils.recipeBuilder(
				RECIPE_BronzePlate, RECIPE_CraftingTable, RECIPE_BronzePlate,
				RECIPE_BronzePlate, RECIPE_BasicCasingIC2, RECIPE_BronzePlate,
				RECIPE_BronzePlate, RECIPE_BronzePlate, RECIPE_BronzePlate,
				OUTPUT_Workbench_Bronze);
	}
	
}
