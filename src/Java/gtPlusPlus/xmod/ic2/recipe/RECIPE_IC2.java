package gtPlusPlus.xmod.ic2.recipe;

import gregtech.api.enums.*;
import gregtech.api.util.*;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.RecipeUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.ic2.item.IC2_Items;
import net.minecraft.item.ItemStack;

import static gtPlusPlus.core.recipe.RECIPES_Tools.craftingToolHardHammer;
import static gtPlusPlus.core.recipe.RECIPES_Tools.craftingToolWrench;

public class RECIPE_IC2 {

	public static String plate_T1 = "plateEnergeticAlloy";
	public static String plate_T2 = "plateTungstenSteel";
	public static String plate_T3 = "plateVibrantAlloy";
	public static String plate_T4 = "plateAlloyIridium";

	public static ItemStack block_T1 = GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.EnergeticAlloy, 1L);
	public static ItemStack block_T2 = GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.TungstenSteel, 1L);
	public static ItemStack block_T3 = GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.VibrantAlloy, 1L);
	public static ItemStack block_T4 = GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Iridium, 1L);

	public static ItemStack shaft_block_T1 = GT_OreDictUnificator.get(OrePrefixes.block, Materials.EnergeticAlloy, 1L);
	public static ItemStack shaft_block_T2 = GT_OreDictUnificator.get(OrePrefixes.block, Materials.TungstenSteel, 1L);
	public static ItemStack shaft_block_T3 = GT_OreDictUnificator.get(OrePrefixes.block, Materials.VibrantAlloy, 1L);
	public static ItemStack shaft_block_T4 = GT_OreDictUnificator.get(OrePrefixes.block, Materials.Iridium, 1L);

	public static String ingot_T1 = "ingotEnergeticAlloy";
	public static String ingot_T2 = "ingotTungstenSteel";
	public static String ingot_T3 = "ingotVibrantAlloy";
	public static String ingot_T4 = "ingotIridium";

	public static String ring_T1 = "ringStainlessSteel";
	public static String ring_T2 = "ringTungstenSteel";
	public static String ring_T3 = "ringChrome";
	public static String ring_T4 = "ringOsmiridium";


	private static ItemStack rotor_blade_T1 = ItemUtils.getSimpleStack(IC2_Items.rotor_Blade_Material_1.getItem());
	private static ItemStack rotor_blade_T2 = ItemUtils.getSimpleStack(IC2_Items.rotor_Blade_Material_2.getItem());
	private static ItemStack rotor_blade_T3 = ItemUtils.getSimpleStack(IC2_Items.rotor_Blade_Material_3.getItem());
	private static ItemStack rotor_blade_T4 = ItemUtils.getSimpleStack(IC2_Items.rotor_Blade_Material_4.getItem());

	private static ItemStack shaft_T1 = ItemUtils.getSimpleStack(IC2_Items.shaft_Material_1.getItem());
	private static ItemStack shaft_T2 = ItemUtils.getSimpleStack(IC2_Items.shaft_Material_2.getItem());
	private static ItemStack shaft_T3 = ItemUtils.getSimpleStack(IC2_Items.shaft_Material_3.getItem());
	private static ItemStack shaft_T4 = ItemUtils.getSimpleStack(IC2_Items.shaft_Material_4.getItem());

	private static ItemStack rotor_T1 = ItemUtils.getSimpleStack(IC2_Items.rotor_Material_1.getItem());
	private static ItemStack rotor_T2 = ItemUtils.getSimpleStack(IC2_Items.rotor_Material_2.getItem());
	private static ItemStack rotor_T3 = ItemUtils.getSimpleStack(IC2_Items.rotor_Material_3.getItem());
	private static ItemStack rotor_T4 = ItemUtils.getSimpleStack(IC2_Items.rotor_Material_4.getItem());

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

	public static void initRecipes() {

		if (!CORE.GTNH) {
			//Rotor Blade Recipes

			RecipeUtils.recipeBuilder(
					plate_T1, ingot_T1, plate_T1,
					plate_T1, ingot_T1, plate_T1,
					plate_T1, ingot_T1, plate_T1,
					rotor_blade_T1);

			RecipeUtils.recipeBuilder(
					plate_T2, ingot_T2, plate_T2,
					plate_T2, ingot_T2, plate_T2,
					plate_T2, ingot_T2, plate_T2,
					rotor_blade_T2);

			RecipeUtils.recipeBuilder(
					plate_T3, ingot_T3, plate_T3,
					plate_T3, ingot_T3, plate_T3,
					plate_T3, ingot_T3, plate_T3,
					rotor_blade_T3);

			RecipeUtils.recipeBuilder(
					plate_T4, ingot_T4, plate_T4,
					plate_T4, ingot_T4, plate_T4,
					plate_T4, ingot_T4, plate_T4,
					rotor_blade_T4);
		}
		if (CORE.GTNH) {

			RecipeUtils.recipeBuilder(
					plate_T1, plate_T1, plate_T1,
					plate_T1, ring_T1, plate_T1,
					plate_T1, plate_T1, plate_T1,
					rotor_blade_T1);

			RecipeUtils.recipeBuilder(
					plate_T2, plate_T2, plate_T2,
					plate_T2, ring_T2, plate_T2,
					plate_T2, plate_T2, plate_T2,
					rotor_blade_T2);

			RecipeUtils.recipeBuilder(
					plate_T3, plate_T3, plate_T3,
					plate_T3, ring_T3, plate_T3,
					plate_T3, plate_T3, plate_T3,
					rotor_blade_T3);

			RecipeUtils.recipeBuilder(
					plate_T4, plate_T4, plate_T4,
					plate_T4, ring_T4, plate_T4,
					plate_T4, plate_T4, plate_T4,
					rotor_blade_T4);

		}

		//Shaft Extruder Recipe
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Shape_Extruder_WindmillShaft.get(1L, new Object[0]), GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"hXS", "XPX", "fXd", Character.valueOf('P'), ItemList.Shape_Extruder_Rod, Character.valueOf('X'), OrePrefixes.plate.get(Materials.DarkIron), Character.valueOf('S'), OrePrefixes.screw.get(Materials.DarkIron)});
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Shape_Extruder_WindmillShaft.get(1L, new Object[0]), GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"hXS", "XPX", "fXd", Character.valueOf('P'), ItemList.Shape_Extruder_Rod, Character.valueOf('X'), OrePrefixes.plate.get(Materials.TungstenSteel), Character.valueOf('S'), OrePrefixes.screw.get(Materials.TungstenSteel)});
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Shape_Extruder_WindmillShaft.get(1L, new Object[0]), GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"hXS", "XPX", "fXd", Character.valueOf('P'), ItemList.Shape_Extruder_Rod, Character.valueOf('X'), OrePrefixes.plate.get(Materials.Molybdenum), Character.valueOf('S'), OrePrefixes.screw.get(Materials.Molybdenum)});
		Logger.INFO("Added recipe item for GT5 Extruder: Shaft Shape");

		//Shaft Recipes
		GT_Values.RA.addExtruderRecipe(GT_Utility.copyAmount(9L, block_T1), GregtechItemList.Shape_Extruder_WindmillShaft.get(0L, new Object[0]), shaft_T1, 2560, 250);
		GT_Values.RA.addExtruderRecipe(GT_Utility.copyAmount(1L, shaft_block_T1), GregtechItemList.Shape_Extruder_WindmillShaft.get(0L, new Object[0]), shaft_T1, 2560, 250);
		if (LoadedMods.EnderIO || CORE.GTNH) {
			Logger.INFO("Added recipe for GT5 Extruder: Windmill Shaft [Energetic]");
		} else {
			Logger.INFO("Added recipe for GT5 Extruder: Windmill Shaft [Magnalium]");
		}
		GT_Values.RA.addExtruderRecipe(GT_Utility.copyAmount(9L, block_T2), GregtechItemList.Shape_Extruder_WindmillShaft.get(0L, new Object[0]), shaft_T2, 5120, 500);
		GT_Values.RA.addExtruderRecipe(GT_Utility.copyAmount(1L, shaft_block_T2), GregtechItemList.Shape_Extruder_WindmillShaft.get(0L, new Object[0]), shaft_T2, 5120, 500);

		Logger.INFO("Added recipe for GT5 Extruder: Windmill Shaft [TungstenSteel]");
		GT_Values.RA.addExtruderRecipe(GT_Utility.copyAmount(9L, block_T3), GregtechItemList.Shape_Extruder_WindmillShaft.get(0L, new Object[0]), shaft_T3, 10240, 2000);
		GT_Values.RA.addExtruderRecipe(GT_Utility.copyAmount(1L, shaft_block_T3), GregtechItemList.Shape_Extruder_WindmillShaft.get(0L, new Object[0]), shaft_T3, 10240, 2000);
		if (LoadedMods.EnderIO || CORE.GTNH) {
			Logger.INFO("Added recipe for GT5 Extruder: Windmill Shaft [Vibrant]");
		} else {
			Logger.INFO("Added recipe for GT5 Extruder: Windmill Shaft [Ultimet]");
		}
		GT_Values.RA.addExtruderRecipe(GT_Utility.copyAmount(9L, block_T4), GregtechItemList.Shape_Extruder_WindmillShaft.get(0L, new Object[0]), shaft_T4, 20480, 4000);
		GT_Values.RA.addExtruderRecipe(GT_Utility.copyAmount(1L, shaft_block_T4), GregtechItemList.Shape_Extruder_WindmillShaft.get(0L, new Object[0]), shaft_T4, 20480, 4000);
		Logger.INFO("Added recipe for GT5 Extruder: Windmill Shaft [Iridium]");

		if (!CORE.GTNH) {
			//Rotor Recipes
			RecipeUtils.recipeBuilder(
					null, rotor_blade_T1, null,
					rotor_blade_T1, shaft_T1, rotor_blade_T1,
					null, rotor_blade_T1, null,
					rotor_T1);

			RecipeUtils.recipeBuilder(
					null, rotor_blade_T2, null,
					rotor_blade_T2, shaft_T2, rotor_blade_T2,
					null, rotor_blade_T2, null,
					rotor_T2);

			RecipeUtils.recipeBuilder(
					null, rotor_blade_T3, null,
					rotor_blade_T3, shaft_T3, rotor_blade_T3,
					null, rotor_blade_T3, null,
					rotor_T3);

			RecipeUtils.recipeBuilder(
					null, rotor_blade_T4, null,
					rotor_blade_T4, shaft_T4, rotor_blade_T4,
					null, rotor_blade_T4, null,
					rotor_T4);

		}
		if (CORE.GTNH) {
			RecipeUtils.recipeBuilder(
					shaft_T1, rotor_blade_T1, craftingToolHardHammer,
					rotor_blade_T1, ring_T1, rotor_blade_T1,
					craftingToolWrench, rotor_blade_T1, shaft_T1,
					rotor_T1);

			RecipeUtils.recipeBuilder(
					shaft_T2, rotor_blade_T2, craftingToolHardHammer,
					rotor_blade_T2, ring_T2, rotor_blade_T2,
					craftingToolWrench, rotor_blade_T2, shaft_T2,
					rotor_T2);

			RecipeUtils.recipeBuilder(
					shaft_T3, rotor_blade_T3, craftingToolHardHammer,
					rotor_blade_T3, ring_T3, rotor_blade_T3,
					craftingToolWrench, rotor_blade_T3, shaft_T3,
					rotor_T3);

			RecipeUtils.recipeBuilder(
					shaft_T4, rotor_blade_T4, craftingToolHardHammer,
					rotor_blade_T4, ring_T4, rotor_blade_T4,
					craftingToolWrench, rotor_blade_T4, shaft_T4,
					rotor_T4);
		}


	}
}
