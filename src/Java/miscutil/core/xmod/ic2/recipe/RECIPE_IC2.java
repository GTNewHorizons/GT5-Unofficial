package miscutil.core.xmod.ic2.recipe;

import miscutil.core.util.item.UtilsItems;
import miscutil.core.util.recipe.UtilsRecipe;
import miscutil.core.xmod.ic2.item.IC2_Items;
import net.minecraft.item.ItemStack;

public class RECIPE_IC2 {
	
	public static String plate_T1 = "ore:plateEnergeticAlloy";
	public static String plate_T2 = "ore:plateTungstenSteel";
	public static String plate_T3 = "ore:plateVibrantAlloy";
	public static String plate_T4 = "ore:plateIridium";

	public static String ingot_T1 = "ore:ingotEnergeticAlloy";
	public static String ingot_T2 = "ore:ingotTungstenSteel";
	public static String ingot_T3 = "ore:ingotVibrantAlloy";
	public static String ingot_T4 = "ore:ingotIridium";
	
	private static ItemStack rotor_T1 = UtilsItems.getSimpleStack(IC2_Items.rotor_Blade_Material_1.getItem());
	private static ItemStack rotor_T2 = UtilsItems.getSimpleStack(IC2_Items.rotor_Blade_Material_2.getItem());
	private static ItemStack rotor_T3 = UtilsItems.getSimpleStack(IC2_Items.rotor_Blade_Material_3.getItem());
	private static ItemStack rotor_T4 = UtilsItems.getSimpleStack(IC2_Items.rotor_Blade_Material_4.getItem());

	public static void initRecipes(){
		UtilsRecipe.recipeBuilder(
				plate_T1, ingot_T1, plate_T1,
				plate_T1, ingot_T1, plate_T1,
				plate_T1, ingot_T1, plate_T1,
				rotor_T1);
		
		UtilsRecipe.recipeBuilder(
				plate_T2, ingot_T2, plate_T2,
				plate_T2, ingot_T2, plate_T2,
				plate_T2, ingot_T2, plate_T2,
				rotor_T2);
		
		UtilsRecipe.recipeBuilder(
				plate_T3, ingot_T3, plate_T3,
				plate_T3, ingot_T3, plate_T3,
				plate_T3, ingot_T3, plate_T3,
				rotor_T3);
		
		UtilsRecipe.recipeBuilder(
				plate_T4, ingot_T4, plate_T4,
				plate_T4, ingot_T4, plate_T4,
				plate_T4, ingot_T4, plate_T4,
				rotor_T4);
	}
	
}
