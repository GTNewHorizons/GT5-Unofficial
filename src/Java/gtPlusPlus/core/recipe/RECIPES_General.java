package gtPlusPlus.core.recipe;

import static gtPlusPlus.core.util.item.ItemUtils.getSimpleStack;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.ItemList;
import gregtech.api.util.GT_ModHandler;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.core.util.recipe.RecipeUtils;
import gtPlusPlus.xmod.bop.blocks.BOP_Block_Registrator;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
	static String RECIPE_BronzePlate = "plateAnyBronze";
	static ItemStack RECIPE_BasicCasingIC2;
	static ItemStack OUTPUT_Workbench_Bronze = ItemUtils.getSimpleStack(Item.getItemFromBlock(ModBlocks.blockWorkbench));
	static ItemStack RECIPE_HydrogenDust = ItemUtils.getSimpleStack(ModItems.itemHydrogenBlob);

	public static void RECIPES_LOAD(){
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
			Utils.LOG_INFO("Added a recipe for Rainforest oak Saplings.");
		}

		if (!CORE.GTNH) {
			//Iron bars
			final ItemStack ironBars = ItemUtils.getItemStack("minecraft:iron_bars", 1);
			//Fish Trap
			if (RecipeUtils.recipeBuilder(
					ironBars, ironBars, ironBars,
					ironBars, "frameGtWroughtIron", ironBars,
					ironBars, ironBars, ironBars,
					ItemUtils.getSimpleStack(ModBlocks.blockFishTrap))){
				Utils.LOG_INFO("Added a recipe for the Fish Trap.");
			}
		} else {
			//Steel Bars
			final ItemStack steelBars = ItemUtils.getItemStack("dreamcraft:item.SteelBars", 1);
			//Fish Trap
			if (RecipeUtils.recipeBuilder(
					steelBars, steelBars, steelBars,
					steelBars, "frameGtWroughtIron", steelBars,
					steelBars, steelBars, steelBars,
					ItemUtils.getSimpleStack(ModBlocks.blockFishTrap))) {
				Utils.LOG_INFO("Added a recipe for the Fish Trap.");
			}
		}

		//Alkalus Coin
		if (RecipeUtils.recipeBuilder(
				"gemExquisiteRuby", "gemFlawlessDiamond", "gemExquisiteDiamond",
				"gemFlawlessRuby", ItemList.Credit_Greg_Naquadah.get(1), "gemFlawlessSapphire",
				"gemExquisiteEmerald", "gemFlawlessEmerald", "gemExquisiteSapphire",
				ItemUtils.getSimpleStack(ModItems.itemAlkalusDisk))){
			Utils.LOG_INFO("Added a recipe for The Alkalus Disk.");
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

		//Alkalus Coin
		/*AddGregtechRecipe.addAssemblylineRecipe(
				ItemUtils.getSimpleStack(ModItems.itemAlkalusDisk),
				288000,
				new ItemStack[]{
				ItemUtils.getSimpleStack(gemShards[0], 10),
				ItemUtils.getSimpleStack(gemShards[1], 10),
				ItemUtils.getSimpleStack(gemShards[2], 10),
				ItemUtils.getSimpleStack(gemShards[3], 10),
        		GT_OreDictUnificator.get(OrePrefixes.block, Materials.NeodymiumMagnetic, 1L),
        		GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Superconductor, 16L),
        		GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Superconductor, 16L),
        		GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Superconductor, 16L),
        		GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Superconductor, 16L),
        		GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.NiobiumTitanium, 2L)},
				new FluidStack[]{
        		Materials.Osmium.getMolten(144*32),
        		Materials.Europium.getFluid(144*8)},
				ItemUtils.getSimpleStack(Dimension_DarkWorld.portalItem),
				30*20*60,
				100000);*/

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


