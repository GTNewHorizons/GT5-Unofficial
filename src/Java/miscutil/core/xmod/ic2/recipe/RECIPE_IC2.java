package miscutil.core.xmod.ic2.recipe;

import miscutil.core.util.recipe.UtilsRecipe;
import miscutil.core.xmod.ic2.item.IC2_Items;

public class RECIPE_IC2 {
	
	public static String plate_T1 = "ore:plateEnergeticAlloy";
	public static String plate_T2 = "ore:plateTungstenSteel";
	public static String plate_T3 = "ore:plateVibrantAlloy";
	public static String plate_T4 = "ore:plateIridium";

	public static String ingot_T1 = "ore:ingotEnergeticAlloy";
	public static String ingot_T2 = "ore:ingotTungstenSteel";
	public static String ingot_T3 = "ore:ingotVibrantAlloy";
	public static String ingot_T4 = "ore:ingotIridium";

	public static void initRecipes(){
		UtilsRecipe.recipeBuilder(
				plate_T1, ingot_T1, plate_T1,
				plate_T1, ingot_T1, plate_T1,
				plate_T1, ingot_T1, plate_T1,
				IC2_Items.rotor_Blade_Material_1);
		
		UtilsRecipe.recipeBuilder(
				plate_T2, ingot_T2, plate_T2,
				plate_T2, ingot_T2, plate_T2,
				plate_T2, ingot_T2, plate_T2,
				IC2_Items.rotor_Blade_Material_2);
		
		UtilsRecipe.recipeBuilder(
				plate_T3, ingot_T3, plate_T3,
				plate_T3, ingot_T3, plate_T3,
				plate_T3, ingot_T3, plate_T3,
				IC2_Items.rotor_Blade_Material_3);
		
		UtilsRecipe.recipeBuilder(
				plate_T4, ingot_T4, plate_T4,
				plate_T4, ingot_T4, plate_T4,
				plate_T4, ingot_T4, plate_T4,
				IC2_Items.rotor_Blade_Material_4);
	}
	
}
