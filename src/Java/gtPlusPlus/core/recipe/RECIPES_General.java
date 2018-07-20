package gtPlusPlus.core.recipe;

import static gtPlusPlus.core.util.minecraft.ItemUtils.getSimpleStack;

import gregtech.api.enums.*;
import gregtech.api.util.GT_ModHandler;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.material.nuclear.FLUORIDES;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.RecipeUtils;
import gtPlusPlus.xmod.bop.blocks.BOP_Block_Registrator;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class RECIPES_General {

	static final ItemStack NULL = null;
	static ItemStack RECIPE_Paper;
	static ItemStack RECIPE_Dirt;
	static ItemStack RECIPE_Snow;
	static ItemStack RECIPE_Obsidian;
	static String RECIPE_LapisDust = "dustLazurite";
	static ItemStack OUTPUT_Blueprint;
	static ItemStack RECIPE_CraftingTable;
	static String RECIPE_BronzePlate = "plateBronze";
	static ItemStack RECIPE_BasicCasingIC2;
	static ItemStack OUTPUT_Workbench_Bronze;
	static ItemStack RECIPE_HydrogenDust;

	public static void loadRecipes(){
		
		RECIPE_Paper = ItemUtils.getSimpleStack(Items.paper);
		RECIPE_Dirt = ItemUtils.getSimpleStack(Blocks.dirt);
		RECIPE_Snow = ItemUtils.getSimpleStack(Blocks.snow);
		RECIPE_Obsidian = ItemUtils.getSimpleStack(Blocks.obsidian);
		RECIPE_CraftingTable = ItemUtils.getSimpleStack(Blocks.crafting_table);
		RECIPE_HydrogenDust = ItemUtils.getSimpleStack(ModItems.itemHydrogenBlob);	
		
		OUTPUT_Workbench_Bronze = ItemUtils.getSimpleStack(ModBlocks.blockWorkbench);
		OUTPUT_Blueprint = ItemUtils.getSimpleStack(ModItems.itemBlueprintBase);	
		
		if (LoadedMods.Gregtech){
			RECIPE_BasicCasingIC2 = ItemUtils.getItemStack("IC2:blockMachine", 1);
			run();
			addCompressedObsidian();
			addHandPumpRecipes();
		}
	}

	private static void run() {
		//Workbench Blueprint
		RecipeUtils.recipeBuilder(
				RECIPE_Paper, RECIPE_LapisDust, NULL,
				RECIPE_Paper, RECIPE_LapisDust, NULL,
				RECIPE_LapisDust, RECIPE_LapisDust, NULL,
				OUTPUT_Blueprint);

		//Bronze Workbench
		/*RecipeUtils.recipeBuilder(
				RECIPE_BronzePlate, RECIPE_CraftingTable, RECIPE_BronzePlate,
				RECIPE_BronzePlate, RECIPE_BasicCasingIC2, RECIPE_BronzePlate,
				RECIPE_BronzePlate, RECIPE_BronzePlate, RECIPE_BronzePlate,
				OUTPUT_Workbench_Bronze);*/

		//Generates recipes for the Dull shard when TC is not installed.
		if (!LoadedMods.Thaumcraft) {
			//Dull Shard to Aer
			RecipeUtils.recipeBuilder(
					RECIPE_HydrogenDust, RECIPE_HydrogenDust, RECIPE_HydrogenDust,
					RECIPE_HydrogenDust, ItemUtils.getSimpleStack(ModItems.shardDull), RECIPE_HydrogenDust,
					RECIPE_HydrogenDust, RECIPE_HydrogenDust, RECIPE_HydrogenDust,
					ItemUtils.getSimpleStack(ModItems.shardAer));
			//Dull Shard to Ignis
			RecipeUtils.recipeBuilder(
					RECIPE_Obsidian, RECIPE_Obsidian, RECIPE_Obsidian,
					RECIPE_Obsidian, ItemUtils.getSimpleStack(ModItems.shardDull), RECIPE_Obsidian,
					RECIPE_Obsidian, RECIPE_Obsidian, RECIPE_Obsidian,
					ItemUtils.getSimpleStack(ModItems.shardIgnis));
			//Dull Shard to Terra
			RecipeUtils.recipeBuilder(
					RECIPE_Dirt, RECIPE_Dirt, RECIPE_Dirt,
					RECIPE_Dirt, ItemUtils.getSimpleStack(ModItems.shardDull), RECIPE_Dirt,
					RECIPE_Dirt, RECIPE_Dirt, RECIPE_Dirt,
					ItemUtils.getSimpleStack(ModItems.shardTerra));
			//Dull Shard to Aqua
			RecipeUtils.recipeBuilder(
					RECIPE_LapisDust, RECIPE_LapisDust, RECIPE_LapisDust,
					RECIPE_LapisDust, ItemUtils.getSimpleStack(ModItems.shardDull), RECIPE_LapisDust,
					RECIPE_LapisDust, RECIPE_LapisDust, RECIPE_LapisDust,
					ItemUtils.getSimpleStack(ModItems.shardAqua));

			GT_ModHandler.addPulverisationRecipe(ItemUtils.getSimpleStack(ModItems.shardAer), ItemUtils.getSimpleStack(ModItems.dustAer, 2));
			GT_ModHandler.addPulverisationRecipe(ItemUtils.getSimpleStack(ModItems.shardIgnis), ItemUtils.getSimpleStack(ModItems.dustIgnis, 2));
			GT_ModHandler.addPulverisationRecipe(ItemUtils.getSimpleStack(ModItems.shardTerra), ItemUtils.getSimpleStack(ModItems.dustTerra, 2));
			GT_ModHandler.addPulverisationRecipe(ItemUtils.getSimpleStack(ModItems.shardAqua), ItemUtils.getSimpleStack(ModItems.dustAqua, 2));

		}

		//Rainforest oak Sapling
		if (RecipeUtils.recipeBuilder(
				"stickWood", "stickWood", "stickWood",
				"stickWood", "treeSapling", "stickWood",
				"stickWood", "dustBone", "stickWood",
				ItemUtils.getSimpleStack(BOP_Block_Registrator.sapling_Rainforest))){
			Logger.INFO("Added a recipe for Rainforest oak Saplings.");
		}


		// Try fix this ore
		if (ModBlocks.blockOreFluorite != null){
			RecipeUtils.addShapelessGregtechRecipe(new ItemStack[]{ItemUtils.getSimpleStack(ModBlocks.blockOreFluorite)}, FLUORIDES.FLUORITE.getOre(1));
		}

		//Iron bars
		final ItemStack ironBars;
		if (CORE.GTNH) {
			ironBars = ItemUtils.getItemStack("dreamcraft:item.SteelBars", 1);
		} else {
			ironBars = ItemUtils.getItemStack("minecraft:iron_bars", 1);
		}

		//Fish Trap
		if (RecipeUtils.recipeBuilder(
				ironBars, ironBars, ironBars,
				ironBars, "frameGtWroughtIron", ironBars,
				ironBars, ironBars, ironBars,
				ItemUtils.getSimpleStack(ModBlocks.blockFishTrap))){
			Logger.INFO("Added a recipe for the Fish Trap.");
		}

		//Small Gear Extruder Shape
		if (!CORE.GTNH) {
			GT_ModHandler.addCraftingRecipe(GregtechItemList.Shape_Extruder_SmallGear.get(1L, new Object[0]), GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"hXS", "XPX", "fXd", Character.valueOf('P'), ItemList.Shape_Extruder_Gear, Character.valueOf('X'), OrePrefixes.plate.get(Materials.Steel), Character.valueOf('S'), OrePrefixes.screw.get(Materials.Steel)});


			String[] ironTypes = {"", "Wrought", "Pig", "Any"};
			for (int y=0;y<ironTypes.length;y++) {
				//Iron bars
				String ironRecipe = "stick"+ironTypes[y]+"Iron";
				if (RecipeUtils.recipeBuilder(
						null, CI.craftingToolWrench, null,
						ironRecipe, ironRecipe, ironRecipe,
						ironRecipe, ironRecipe, ironRecipe,
						ItemUtils.getItemStack("minecraft:iron_bars", 8))) {
					Logger.INFO("Re-added old GT recipe for Iron Bars.");			
				}
			}
		}

		//Mining Explosive
		Logger.RECIPE("[Inspection] Explosives");
		if (RecipeUtils.recipeBuilder(
				CI.explosiveITNT, CI.explosiveTNT, CI.explosiveITNT,
				CI.explosiveTNT, "frameGtIron", CI.explosiveTNT,
				"dustSulfur", CI.explosiveTNT, "dustSulfur",
				ItemUtils.getSimpleStack(ModBlocks.blockMiningExplosive, 3))){
			Logger.INFO("Added a recipe for Mining Explosives.");
		}

		//Alkalus Coin
		if (RecipeUtils.recipeBuilder(
				"gemExquisiteRuby", "gemFlawlessDiamond", "gemExquisiteDiamond",
				"gemFlawlessRuby", ItemList.Credit_Greg_Osmium.get(1), "gemFlawlessSapphire",
				"gemExquisiteEmerald", "gemFlawlessEmerald", "gemExquisiteSapphire",
				ItemUtils.getSimpleStack(ModItems.itemAlkalusDisk))){
			Logger.INFO("Added a recipe for The Alkalus Disk.");
		}

		/*final String fancyGems[] = new String[]{"gemExquisiteDiamond", "gemExquisiteEmerald", "gemExquisiteRuby", "gemExquisiteSapphire"};
		final ItemStack gemShards[] = new ItemStack[]{ItemUtils.simpleMetaStack(ModItems.itemGemShards, 0, 1),
				ItemUtils.simpleMetaStack(ModItems.itemGemShards, 1, 1),
				ItemUtils.simpleMetaStack(ModItems.itemGemShards, 2, 1),
				ItemUtils.simpleMetaStack(ModItems.itemGemShards, 3, 1)};

		int l=0;
		for (final String gem : fancyGems){
			GameRegistry.addShapelessRecipe(
					gemShards[l],
					ItemUtils.getItemStackOfAmountFromOreDict(gem, 1),
					new ItemStack(ModItems.itemAlkalusDisk, 1, OreDictionary.WILDCARD_VALUE));
			l++;
		}*/

		Logger.RECIPE("[Inspection] Wither Cage");
		if (RecipeUtils.recipeBuilder(
				"stickBlackSteel", "plateTungstenSteel", "stickBlackSteel",
				"plateTungstenSteel", getSimpleStack(Items.nether_star), "plateTungstenSteel",
				"stickBlackSteel", "plateTungstenSteel", "stickBlackSteel",
				ItemUtils.getSimpleStack(ModBlocks.blockWitherGuard, 32))){
			Logger.INFO("Added a recipe for Wither Cages.");
		}

		Logger.RECIPE("[Inspection] Xp Converter");
		if (RecipeUtils.recipeBuilder(
				getSimpleStack(Items.experience_bottle), ItemUtils.simpleMetaStack(ModBlocks.blockCompressedObsidian, 2, 1), getSimpleStack(Items.experience_bottle),
				ItemUtils.simpleMetaStack(ModBlocks.blockCompressedObsidian, 5, 1), getSimpleStack(Items.nether_star), ItemUtils.simpleMetaStack(ModBlocks.blockCompressedObsidian, 5, 1),
				getSimpleStack(Items.experience_bottle), ItemUtils.simpleMetaStack(ModBlocks.blockCompressedObsidian, 2, 1), getSimpleStack(Items.experience_bottle),
				ItemUtils.getSimpleStack(ModBlocks.blockXpConverter, 1))){
			Logger.INFO("Added a recipe for XP Converter.");
		}




		// Rope/Fiber/Net
		if (RecipeUtils.addShapelessGregtechRecipe(
				new ItemStack[]{
						ItemUtils.getItemStackOfAmountFromOreDictNoBroken(CI.craftingToolKnife, 1), ItemUtils.getSimpleStack(Items.reeds)},
				ItemUtils.getSimpleStack(ModItems.itemFiber, 16)
				)){
			Logger.INFO("Added a recipe for Fiber.");
		}

		if (RecipeUtils.addShapelessGregtechRecipe(
				new ItemStack[]{
						ItemUtils.getItemStackOfAmountFromOreDictNoBroken(CI.craftingToolKnife, 1), ItemUtils.getSimpleStack(Blocks.sapling)},
				ItemUtils.getSimpleStack(ModItems.itemFiber, 32)
				)){
			Logger.INFO("Added a recipe for Fiber.");
		}

		if (RecipeUtils.recipeBuilder(
				null, ItemUtils.getSimpleStack(ModItems.itemFiber, 1), null,
				ItemUtils.getSimpleStack(ModItems.itemFiber, 1), CI.craftingToolKnife, ItemUtils.getSimpleStack(ModItems.itemFiber, 1),
				null, ItemUtils.getSimpleStack(ModItems.itemFiber, 1), null,
				ItemUtils.getSimpleStack(ModItems.itemRope, 3))){
			Logger.INFO("Added a recipe for Rope.");
		}

		Logger.RECIPE("[Inspection] Net");
		if (RecipeUtils.recipeBuilder(
				ItemUtils.getSimpleStack(ModItems.itemRope, 1), ItemUtils.getSimpleStack(ModItems.itemRope, 1), ItemUtils.getSimpleStack(ModItems.itemRope, 1),
				ItemUtils.getSimpleStack(ModItems.itemRope, 1), ItemUtils.getSimpleStack(ModItems.itemRope, 1), ItemUtils.getSimpleStack(ModItems.itemRope, 1),
				null, null, null,
				ItemUtils.getSimpleStack(ModBlocks.blockNet, 2))){
			Logger.INFO("Added a recipe for Nets.");
		}


	}

	private static boolean addCompressedObsidian(){
		//Invert Obsidian
		if (RecipeUtils.recipeBuilder(
				getSimpleStack(Items.redstone), getSimpleStack(Items.glowstone_dust), getSimpleStack(Items.redstone),
				getSimpleStack(Items.glowstone_dust), ItemUtils.simpleMetaStack(ModBlocks.blockCompressedObsidian, 1, 1), getSimpleStack(Items.glowstone_dust),
				getSimpleStack(Items.redstone), getSimpleStack(Items.glowstone_dust), getSimpleStack(Items.redstone),
				ItemUtils.simpleMetaStack(ModBlocks.blockCompressedObsidian, 5, 1))){
			Logger.INFO("Added a recipe for Inverted Obsidian.");
		}

		final ItemStack[] mItems = new ItemStack[6];
		mItems[0] = ItemUtils.getSimpleStack(Blocks.obsidian);
		for (int r=0;r<5;r++){
			mItems[r+1] = ItemUtils.simpleMetaStack(ModBlocks.blockCompressedObsidian, r, 1);
		}

		//Compressed Obsidian 1-5
		for (int r=0;r<5;r++){

			final ItemStack input = mItems[r];
			final ItemStack output = mItems[r+1];

			if (RecipeUtils.recipeBuilder(
					input, input, input,
					input, input, input,
					input, input, input,
					output)){
						Logger.INFO("Added a recipe for Compressed Obsidian ["+r+"]");
					}

			if (RecipeUtils.addShapelessGregtechRecipe(new ItemStack[]{output}, ItemUtils.getSimpleStack(input, 9))){
				Logger.INFO("Added a shapeless recipe for Compressed Obsidian ["+r+"]");
			}

		}
		return true;
	}
	
	private static boolean addHandPumpRecipes() {
		boolean a[] = new boolean[4];		
		a[0] = RecipeUtils.recipeBuilder(
				CI.electricPump_LV, "circuitBasic", null,
				"ringBrass", CI.electricMotor_LV, "circuitBasic",
				"plateSteel", "plateSteel", "rodBrass",
				ItemUtils.simpleMetaStack(ModItems.toolGregtechPump, 1000, 1));	
		Logger.INFO("Added recipe for Hand Pump I - "+a[0]);
		a[1] = RecipeUtils.recipeBuilder(
				CI.electricPump_MV, "circuitAdvanced", null,
				"ringMagnalium", CI.electricMotor_MV, "circuitAdvanced",
				"plateAluminium", "plateAluminium", "rodMagnalium",
				ItemUtils.simpleMetaStack(ModItems.toolGregtechPump, 1001, 1));	
		Logger.INFO("Added recipe for Hand Pump II - "+a[1]);	
		a[2] = RecipeUtils.recipeBuilder(
				CI.electricPump_HV, "circuitData", null,
				"ringChrome", CI.electricMotor_HV, "circuitData",
				"plateStainlessSteel", "plateStainlessSteel", "rodChrome",
				ItemUtils.simpleMetaStack(ModItems.toolGregtechPump, 1002, 1));		
		Logger.INFO("Added recipe for Hand Pump III - "+a[2]);
		a[3] = RecipeUtils.recipeBuilder(
				CI.electricPump_EV, "circuitElite", null,
				"ringTitanium", CI.electricMotor_EV, "circuitElite",
				"plateStungstenSteel", "plateTungstenSteel", "rodTitanium",
				ItemUtils.simpleMetaStack(ModItems.toolGregtechPump, 1003, 1));	
		Logger.INFO("Added recipe for Hand Pump IV - "+a[3]);	
		return a[0] && a[1] && a[2] && a[3];		
	}


}


