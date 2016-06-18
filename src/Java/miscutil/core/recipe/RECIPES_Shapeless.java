package miscutil.core.recipe;

import gregtech.api.enums.ItemList;
import miscutil.core.lib.LoadedMods;
import miscutil.core.util.Utils;
import miscutil.core.util.item.UtilsItems;
import miscutil.core.util.recipe.UtilsRecipe;
import net.minecraft.item.ItemStack;

public class RECIPES_Shapeless {
	
	static ItemStack dustStaballoy = UtilsItems.getItemStackWithMeta(LoadedMods.Gregtech, "gregtech:gt.metaitem.01", "Staballoy Dust", 2319, 1);
	
	//Circuits
		static String circuitPrimitive = "circuitPrimitive";
		static String circuitBasic = "circuitBasic";
		static String circuitGood = "circuitGood";
		static String circuitAdvanced = "circuitAdvanced";
		static String circuitData = "circuitData";
		static String circuitElite = "circuitElite";
		static String circuitMaster = "circuitMaster";
		static String circuitUltimate = "circuitUltimate";
		static ItemStack gearboxCasing_Tier_1 = ItemList.Casing_Gearbox_Bronze.get(1);
	
	public static final void RECIPES_LOAD(){
		//run();
		Utils.LOG_INFO("Loading Shapeless Recipes.");
	}

	private static void run(){
		UtilsRecipe.shapelessBuilder(dustStaballoy, 
				"dustTitanium", "dustUranium", "dustUranium",
				"dustUranium", "dustUranium", "dustUranium",
				"dustUranium", "dustUranium", "dustUranium");
		
		UtilsRecipe.shapelessBuilder(gearboxCasing_Tier_1, 
				circuitPrimitive, circuitPrimitive, circuitPrimitive,
				circuitPrimitive, circuitPrimitive, circuitPrimitive,
				circuitPrimitive, circuitPrimitive, circuitPrimitive);
	}
	
}
