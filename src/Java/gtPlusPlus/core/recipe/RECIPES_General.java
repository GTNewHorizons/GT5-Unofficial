package gtPlusPlus.core.recipe;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.fluid.FluidUtils;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.core.util.recipe.RecipeUtils;
import gtPlusPlus.core.util.reflect.AddGregtechRecipe;
import gtPlusPlus.core.world.darkworld.Dimension_DarkWorld;
import gtPlusPlus.xmod.bop.blocks.BOP_Block_Registrator;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
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
		}
	}

	private static void run(){
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
		if (!LoadedMods.Thaumcraft){
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

		//Iron bars
		ItemStack ironBars = ItemUtils.getItemStack("minecraft:iron_bars", 1);
		//Fish Trap
		if (RecipeUtils.recipeBuilder(
				ironBars, ironBars, ironBars,
				ironBars, "frameGtWroughtIron", ironBars,
				ironBars, ironBars, ironBars,
				ItemUtils.getSimpleStack(ModBlocks.blockFishTrap))){
			Utils.LOG_INFO("Added a recipe for the Fish Trap.");	
		}

		//Alkalus Coin
		if (RecipeUtils.recipeBuilder(
				"gemExquisiteRuby", "gemFlawlessDiamond", "gemExquisiteDiamond",
				"gemFlawlessRuby", ItemList.Credit_Greg_Naquadah.get(1), "gemFlawlessSapphire",
				"gemExquisiteEmerald", "gemFlawlessEmerald", "gemExquisiteSapphire",
				ItemUtils.getSimpleStack(ModItems.itemAlkalusDisk))){
			Utils.LOG_INFO("Added a recipe for The Alkalus Disk.");	
		}

		String fancyGems[] = new String[]{"gemExquisiteDiamond", "gemExquisiteEmerald", "gemExquisiteRuby", "gemExquisiteSapphire"};
		ItemStack gemShards[] = new ItemStack[]{ItemUtils.simpleMetaStack(ModItems.itemGemShards, 0, 1),
				ItemUtils.simpleMetaStack(ModItems.itemGemShards, 1, 1),
				ItemUtils.simpleMetaStack(ModItems.itemGemShards, 2, 1),
				ItemUtils.simpleMetaStack(ModItems.itemGemShards, 3, 1)};
		
		int l=0;
		for (String gem : fancyGems){
			GameRegistry.addShapelessRecipe(
					gemShards[l],
					ItemUtils.getItemStackOfAmountFromOreDict(gem, 1),
					new ItemStack(ModItems.itemAlkalusDisk, 1, OreDictionary.WILDCARD_VALUE));
			l++;
		}

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
		
		}

	}


