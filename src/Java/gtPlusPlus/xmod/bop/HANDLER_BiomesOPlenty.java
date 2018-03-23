package gtPlusPlus.xmod.bop;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.RecipeUtils;
import gtPlusPlus.xmod.bop.blocks.BOP_Block_Registrator;

public class HANDLER_BiomesOPlenty {

	
	
	public static void preInit(){
		//if (LoadedMods.BiomesOPlenty){
			BOP_Block_Registrator.run();
		//}

	}

	public static void init(){
		if (LoadedMods.BiomesOPlenty){

		}
	}

	public static void postInit(){
		if (LoadedMods.BiomesOPlenty){
			RecipeUtils.addShapelessGregtechRecipe(new ItemStack[] {ItemUtils.getSimpleStack(BOP_Block_Registrator.log_Rainforest)}, ItemUtils.getSimpleStack(Item.getItemFromBlock(Blocks.planks), 2));
			RecipeUtils.recipeBuilder(
					CI.craftingToolSaw, null, null,
					ItemUtils.getSimpleStack(BOP_Block_Registrator.log_Rainforest), null, null,
					null, null, null,
					ItemUtils.getSimpleStack(Item.getItemFromBlock(Blocks.planks), 4));
		}

	}


}
