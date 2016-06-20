package miscutil.core.common.compat;

import gregtech.api.util.GT_OreDictUnificator;
import miscutil.core.item.ModItems;
import miscutil.core.recipe.RECIPES_Tools;
import miscutil.core.util.item.UtilsItems;
import miscutil.core.util.recipe.UtilsRecipe;
import net.minecraft.item.ItemStack;

public class COMPAT_ExtraUtils {

	public static void OreDict(){
		RECIPES_Tools.RECIPE_DivisionSigil = new ItemStack(UtilsItems.getItem("ExtraUtilities:divisionSigil"));
		run();
		
	}
	
	private static final void run(){
		UtilsItems.getItemForOreDict("ExtraUtilities:bedrockiumIngot", "ingotBedrockium", "Bedrockium Ingot", 0);
		GT_OreDictUnificator.registerOre("plateBedrockium", new ItemStack(ModItems.itemPlateBedrockium));
		
		//Division Sigil
		UtilsRecipe.recipeBuilder(
						"plateNetherStar", "gemIridium", "plateNetherStar",
						"plateIridium", RECIPES_Tools.craftingToolHardHammer, "plateIridium",
						"plateNetherStar", "gemIridium", "plateNetherStar",
						RECIPES_Tools.RECIPE_DivisionSigil);
	}
	
}
