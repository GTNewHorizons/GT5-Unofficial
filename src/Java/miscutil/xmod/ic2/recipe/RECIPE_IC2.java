package miscutil.xmod.ic2.recipe;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import miscutil.core.lib.LoadedMods;
import miscutil.core.util.Utils;
import miscutil.core.util.item.UtilsItems;
import miscutil.core.util.recipe.UtilsRecipe;
import miscutil.xmod.gregtech.api.enums.GregtechItemList;
import miscutil.xmod.ic2.item.IC2_Items;
import net.minecraft.item.ItemStack;

public class RECIPE_IC2 {
	
	public static String plate_T1 = "plateEnergeticAlloy";
	public static String plate_T2 = "plateTungstenSteel";
	public static String plate_T3 = "plateVibrantAlloy";
	public static String plate_T4 = "plateAlloyIridium";
	
	public static ItemStack block_T1 = GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.EnergeticAlloy, 1L);
	public static ItemStack block_T2 = GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.TungstenSteel, 1L);
	public static ItemStack block_T3 = GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.VibrantAlloy, 1L);
	public static ItemStack block_T4 = GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Iridium, 1L);

	public static String ingot_T1 = "ingotEnergeticAlloy";
	public static String ingot_T2 = "ingotTungstenSteel";
	public static String ingot_T3 = "ingotVibrantAlloy";
	public static String ingot_T4 = "ingotIridium";
	
	private static ItemStack rotor_blade_T1 = UtilsItems.getSimpleStack(IC2_Items.rotor_Blade_Material_1.getItem());
	private static ItemStack rotor_blade_T2 = UtilsItems.getSimpleStack(IC2_Items.rotor_Blade_Material_2.getItem());
	private static ItemStack rotor_blade_T3 = UtilsItems.getSimpleStack(IC2_Items.rotor_Blade_Material_3.getItem());
	private static ItemStack rotor_blade_T4 = UtilsItems.getSimpleStack(IC2_Items.rotor_Blade_Material_4.getItem());
	
	private static ItemStack shaft_T1 = UtilsItems.getSimpleStack(IC2_Items.shaft_Material_1.getItem());
	private static ItemStack shaft_T2 = UtilsItems.getSimpleStack(IC2_Items.shaft_Material_2.getItem());
	private static ItemStack shaft_T3 = UtilsItems.getSimpleStack(IC2_Items.shaft_Material_3.getItem());
	private static ItemStack shaft_T4 = UtilsItems.getSimpleStack(IC2_Items.shaft_Material_4.getItem());

	private static ItemStack rotor_T1 = UtilsItems.getSimpleStack(IC2_Items.rotor_Material_1.getItem());
	private static ItemStack rotor_T2 = UtilsItems.getSimpleStack(IC2_Items.rotor_Material_2.getItem());
	private static ItemStack rotor_T3 = UtilsItems.getSimpleStack(IC2_Items.rotor_Material_3.getItem());
	private static ItemStack rotor_T4 = UtilsItems.getSimpleStack(IC2_Items.rotor_Material_4.getItem());
	
	private static void checkForEnderIO(){
		if(!LoadedMods.EnderIO){
			plate_T1 = "plateMagnalium";
			plate_T2 = "plateTungstenSteel";
			plate_T3 = "plateUltimet";
			plate_T4 = "plateAlloyIridium";
			
			block_T1 = GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Magnalium, 1L);
			block_T2 = GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.TungstenSteel, 1L);
			block_T3 = GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Ultimet, 1L);
			block_T4 = GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Iridium, 1L);

			ingot_T1 = "ingotMagnalium";
			ingot_T2 = "ingotTungstenSteel";
			ingot_T3 = "ingotUltimet";
			ingot_T4 = "ingotIridium";
		}
	}
	
	public static void initRecipes(){
		
		//Rotor Blade Recipes
		UtilsRecipe.recipeBuilder(
				plate_T1, ingot_T1, plate_T1,
				plate_T1, ingot_T1, plate_T1,
				plate_T1, ingot_T1, plate_T1,
				rotor_blade_T1);
		
		UtilsRecipe.recipeBuilder(
				plate_T2, ingot_T2, plate_T2,
				plate_T2, ingot_T2, plate_T2,
				plate_T2, ingot_T2, plate_T2,
				rotor_blade_T2);
		
		UtilsRecipe.recipeBuilder(
				plate_T3, ingot_T3, plate_T3,
				plate_T3, ingot_T3, plate_T3,
				plate_T3, ingot_T3, plate_T3,
				rotor_blade_T3);
		
		UtilsRecipe.recipeBuilder(
				plate_T4, ingot_T4, plate_T4,
				plate_T4, ingot_T4, plate_T4,
				plate_T4, ingot_T4, plate_T4,
				rotor_blade_T4);
		
		//Shaft Extruder Recipe
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Shape_Extruder_WindmillShaft.get(1L, new Object[0]), GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"hXS", "XPX", "fXd", Character.valueOf('P'), ItemList.Shape_Extruder_Rod, Character.valueOf('X'), OrePrefixes.plate.get(Materials.DarkIron), Character.valueOf('S'), OrePrefixes.screw.get(Materials.DarkIron)});
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Shape_Extruder_WindmillShaft.get(1L, new Object[0]), GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"hXS", "XPX", "fXd", Character.valueOf('P'), ItemList.Shape_Extruder_Rod, Character.valueOf('X'), OrePrefixes.plate.get(Materials.TungstenSteel), Character.valueOf('S'), OrePrefixes.screw.get(Materials.TungstenSteel)});
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Shape_Extruder_WindmillShaft.get(1L, new Object[0]), GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"hXS", "XPX", "fXd", Character.valueOf('P'), ItemList.Shape_Extruder_Rod, Character.valueOf('X'), OrePrefixes.plate.get(Materials.Molybdenum), Character.valueOf('S'), OrePrefixes.screw.get(Materials.Molybdenum)});
		Utils.LOG_INFO("Added recipe item for GT5 Extruder: Shaft Shape");
		
		//Shaft Recipes
		GT_Values.RA.addExtruderRecipe(GT_Utility.copyAmount(9L, block_T1), GregtechItemList.Shape_Extruder_WindmillShaft.get(0L, new Object[0]), shaft_T1, 2560, 250);
		if (LoadedMods.EnderIO){
			Utils.LOG_INFO("Added recipe for GT5 Extruder: Windmill Shaft [Energetic]");			
		}
		else {
			Utils.LOG_INFO("Added recipe for GT5 Extruder: Windmill Shaft [Magnalium]");
		}
		GT_Values.RA.addExtruderRecipe(GT_Utility.copyAmount(9L, block_T2), GregtechItemList.Shape_Extruder_WindmillShaft.get(0L, new Object[0]), shaft_T2, 5120, 500);
		Utils.LOG_INFO("Added recipe for GT5 Extruder: Windmill Shaft [TungstenSteel]");
		GT_Values.RA.addExtruderRecipe(GT_Utility.copyAmount(9L, block_T3), GregtechItemList.Shape_Extruder_WindmillShaft.get(0L, new Object[0]), shaft_T3, 10240, 2000);
		if (LoadedMods.EnderIO){
			Utils.LOG_INFO("Added recipe for GT5 Extruder: Windmill Shaft [Vibrant]");			
		}
		else {
			Utils.LOG_INFO("Added recipe for GT5 Extruder: Windmill Shaft [Ultimet]");
		}
		GT_Values.RA.addExtruderRecipe(GT_Utility.copyAmount(9L, block_T4), GregtechItemList.Shape_Extruder_WindmillShaft.get(0L, new Object[0]), shaft_T4, 20480, 4000);
		Utils.LOG_INFO("Added recipe for GT5 Extruder: Windmill Shaft [Iridium]");
		
		//Rotor Recipes
		UtilsRecipe.recipeBuilder(
				null, rotor_blade_T1, null,
				rotor_blade_T1, shaft_T1, rotor_blade_T1,
				null, rotor_blade_T1, null,
				rotor_T1);
		
		UtilsRecipe.recipeBuilder(
				null, rotor_blade_T2, null,
				rotor_blade_T2, shaft_T2, rotor_blade_T2,
				null, rotor_blade_T2, null,
				rotor_T2);
		
		UtilsRecipe.recipeBuilder(
				null, rotor_blade_T3, null,
				rotor_blade_T3, shaft_T3, rotor_blade_T3,
				null, rotor_blade_T3, null,
				rotor_T3);
		
		UtilsRecipe.recipeBuilder(
				null, rotor_blade_T4, null,
				rotor_blade_T4, shaft_T4, rotor_blade_T4,
				null, rotor_blade_T4, null,
				rotor_T4);	
		
	}
	
	
	
}
