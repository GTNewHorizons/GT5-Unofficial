package gtPlusPlus.core.recipe;

import static gtPlusPlus.core.util.minecraft.ItemUtils.getSimpleStack;

import cpw.mods.fml.common.registry.GameRegistry;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
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
import gtPlusPlus.core.world.darkworld.Dimension_DarkWorld;
import gtPlusPlus.xmod.bop.blocks.BOP_Block_Registrator;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import net.minecraftforge.oredict.OreDictionary;

public class RECIPES_General {

	static ItemStack NULL = null;
	static ItemStack RECIPE_Paper = ItemUtils.getSimpleStack(Items.paper);
	static ItemStack RECIPE_Dirt = ItemUtils.getSimpleStack(Item.getItemFromBlock(Blocks.dirt));
	static ItemStack RECIPE_Snow = ItemUtils.getSimpleStack(Item.getItemFromBlock(Blocks.snow));
	static ItemStack RECIPE_Obsidian = ItemUtils.getSimpleStack(Item.getItemFromBlock(Blocks.obsidian));
	static String RECIPE_LapisDust = "dustLazurite";
	static ItemStack OUTPUT_Blueprint = ItemUtils.getSimpleStack(ModItems.itemBlueprintBase);
	static ItemStack RECIPE_CraftingTable = ItemUtils.getSimpleStack(Item.getItemFromBlock(Blocks.crafting_table));
	static String RECIPE_BronzePlate = "plateBronze";
	static ItemStack RECIPE_BasicCasingIC2;
	static ItemStack OUTPUT_Workbench_Bronze = ItemUtils.getSimpleStack(Item.getItemFromBlock(ModBlocks.blockWorkbench));
	static ItemStack RECIPE_HydrogenDust = ItemUtils.getSimpleStack(ModItems.itemHydrogenBlob);

	public static void loadRecipes(){
		if (LoadedMods.Gregtech){
			RECIPE_BasicCasingIC2 = ItemUtils.getItemStack("IC2:blockMachine", 1);
			run();
			addCompressedObsidian();
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
		if (RecipeUtils.recipeBuilder(
				CI.explosiveITNT, CI.explosiveTNT, CI.explosiveITNT,
				CI.explosiveTNT, "frameGtWroughtIron", CI.explosiveTNT,
				"dustSulfur", CI.explosiveTNT, "dustSulfur",
				ItemUtils.getSimpleStack(ModBlocks.blockMiningExplosive, 3))){
			Logger.INFO("Added a recipe for Mining Explosives.");
		}

		//Mystic Frame
		if (RecipeUtils.recipeBuilder(
				CI.sensor_HV, CI.fieldGenerator_MV, CI.sensor_HV,
				CI.craftingToolHammer_Hard, ItemList.Casing_SolidSteel.get(1), CI.craftingToolSolderingIron,
				CI.emitter_HV, CI.fieldGenerator_MV, CI.emitter_HV,
				ItemUtils.getSimpleStack(Dimension_DarkWorld.blockPortalFrame, 2))){
			Logger.INFO("Added a recipe for the Toxic Everglades Portal frame");
		}

		//Alkalus Coin
		if (RecipeUtils.recipeBuilder(
				"gemExquisiteRuby", "gemFlawlessDiamond", "gemExquisiteDiamond",
				"gemFlawlessRuby", ItemList.Credit_Greg_Osmium.get(1), "gemFlawlessSapphire",
				"gemExquisiteEmerald", "gemFlawlessEmerald", "gemExquisiteSapphire",
				ItemUtils.getSimpleStack(ModItems.itemAlkalusDisk))){
			Logger.INFO("Added a recipe for The Alkalus Disk.");
		}

		final String fancyGems[] = new String[]{"gemExquisiteDiamond", "gemExquisiteEmerald", "gemExquisiteRuby", "gemExquisiteSapphire"};
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
		}

		RecipeUtils.addShapedGregtechRecipe(
				"stickBlackSteel", "plateTungstenSteel", "stickBlackSteel",
				"plateTungstenSteel", getSimpleStack(Items.nether_star), "plateTungstenSteel",
				"stickBlackSteel", "plateTungstenSteel", "stickBlackSteel",
				ItemUtils.getSimpleStack(ModBlocks.blockWitherGuard, 32));

		RecipeUtils.addShapedGregtechRecipe(
				getSimpleStack(Items.experience_bottle), ItemUtils.simpleMetaStack(ModBlocks.blockCompressedObsidian, 2, 1), getSimpleStack(Items.experience_bottle),
				ItemUtils.simpleMetaStack(ModBlocks.blockCompressedObsidian, 5, 1), getSimpleStack(Items.nether_star), ItemUtils.simpleMetaStack(ModBlocks.blockCompressedObsidian, 5, 1),
				getSimpleStack(Items.experience_bottle), ItemUtils.simpleMetaStack(ModBlocks.blockCompressedObsidian, 2, 1), getSimpleStack(Items.experience_bottle),
				ItemUtils.getSimpleStack(ModBlocks.blockXpConverter, 1));




		// Rope/Fiber/Net
		RecipeUtils.addShapelessGregtechRecipe(
				new ItemStack[]{
						ItemUtils.getItemStackOfAmountFromOreDictNoBroken(CI.craftingToolKnife, 1), ItemUtils.getSimpleStack(Items.reeds)},
				ItemUtils.getSimpleStack(ModItems.itemFiber, 16)
				);
		RecipeUtils.addShapelessGregtechRecipe(
				new ItemStack[]{
						ItemUtils.getItemStackOfAmountFromOreDictNoBroken(CI.craftingToolKnife, 1), ItemUtils.getSimpleStack(Blocks.sapling)},
				ItemUtils.getSimpleStack(ModItems.itemFiber, 32)
				);
		RecipeUtils.recipeBuilder(
				null, ItemUtils.getSimpleStack(ModItems.itemFiber, 1), null,
				ItemUtils.getSimpleStack(ModItems.itemFiber, 1), CI.craftingToolKnife, ItemUtils.getSimpleStack(ModItems.itemFiber, 1),
				null, ItemUtils.getSimpleStack(ModItems.itemFiber, 1), null,
				ItemUtils.getSimpleStack(ModItems.itemRope, 3));
		RecipeUtils.recipeBuilder(
				ItemUtils.getSimpleStack(ModItems.itemRope, 1), ItemUtils.getSimpleStack(ModItems.itemRope, 1), ItemUtils.getSimpleStack(ModItems.itemRope, 1),
				ItemUtils.getSimpleStack(ModItems.itemRope, 1), ItemUtils.getSimpleStack(ModItems.itemRope, 1), ItemUtils.getSimpleStack(ModItems.itemRope, 1),
				null, null, null,
				ItemUtils.getSimpleStack(ModBlocks.blockNet, 2));


	}

	private static boolean addCompressedObsidian(){
		//Invert Obsidian
		RecipeUtils.addShapedGregtechRecipe(
				getSimpleStack(Items.redstone), getSimpleStack(Items.glowstone_dust), getSimpleStack(Items.redstone),
				getSimpleStack(Items.glowstone_dust), ItemUtils.simpleMetaStack(ModBlocks.blockCompressedObsidian, 1, 1), getSimpleStack(Items.glowstone_dust),
				getSimpleStack(Items.redstone), getSimpleStack(Items.glowstone_dust), getSimpleStack(Items.redstone),
				ItemUtils.simpleMetaStack(ModBlocks.blockCompressedObsidian, 5, 1));

		final ItemStack[] mItems = new ItemStack[6];
		mItems[0] = ItemUtils.getSimpleStack(Blocks.obsidian);
		for (int r=0;r<5;r++){
			mItems[r+1] = ItemUtils.simpleMetaStack(ModBlocks.blockCompressedObsidian, r, 1);
		}

		//Compressed Obsidian 1-5
		for (int r=0;r<5;r++){

			final ItemStack input = mItems[r];
			final ItemStack output = mItems[r+1];

			RecipeUtils.addShapedGregtechRecipe(
					input, input, input,
					input, input, input,
					input, input, input,
					output);

			RecipeUtils.addShapelessGregtechRecipe(new ItemStack[]{output}, ItemUtils.getSimpleStack(input, 9));

		}
		return true;
	}


}


