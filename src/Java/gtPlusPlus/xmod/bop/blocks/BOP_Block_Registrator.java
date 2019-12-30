package gtPlusPlus.xmod.bop.blocks;

import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.ConfigCategories;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.helpers.MaterialHelper;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.MaterialUtils;
import gtPlusPlus.core.util.minecraft.RecipeUtils;
import gtPlusPlus.xmod.bop.blocks.rainforest.LeavesRainforestTree;
import gtPlusPlus.xmod.bop.blocks.rainforest.LogRainforestTree;
import gtPlusPlus.xmod.bop.blocks.rainforest.SaplingRainforestTree;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BOP_Block_Registrator {

	public static Block log_Rainforest;
	public static Block leaves_Rainforest;
	public static Block sapling_Rainforest;

	//Runs Each tree Type separately
	public static final void run(){
		registerTree_Rainforest();
	}


	private static final boolean registerTree_Rainforest(){
		log_Rainforest = new LogRainforestTree();
		leaves_Rainforest = new LeavesRainforestTree();
		sapling_Rainforest = new SaplingRainforestTree();
		ItemUtils.addItemToOreDictionary(ItemUtils.getSimpleStack(log_Rainforest), "logWood");
		ItemUtils.addItemToOreDictionary(ItemUtils.getSimpleStack(leaves_Rainforest), "treeLeaves");
		ItemUtils.addItemToOreDictionary(ItemUtils.getSimpleStack(sapling_Rainforest), "treeSapling");		
		return true;
	}

	public static final void recipes() {		
		//Rainforest Oak
		addLogRecipes(ItemUtils.getSimpleStack(log_Rainforest));
		addSaplingRecipes(ItemUtils.getSimpleStack(sapling_Rainforest));

	}

	public static final void addLogRecipes(final ItemStack aStack) {		
		RecipeUtils.addShapelessGregtechRecipe(new ItemStack[] {aStack}, ItemUtils.getSimpleStack(Item.getItemFromBlock(Blocks.planks), GT_Mod.gregtechproxy.mNerfedWoodPlank ? 2 : 4));
		RecipeUtils.recipeBuilder(
				CI.craftingToolSaw, null, null,
				aStack, null, null,
				null, null, null,
				ItemUtils.getSimpleStack(Item.getItemFromBlock(Blocks.planks), 4));		
		GT_ModHandler.addPulverisationRecipe(GT_Utility.copyAmount(1L, aStack), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 6L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 1L), 80, false);
		GT_ModHandler.addCraftingRecipe(GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Wood, 2L), GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED, new Object[] { "sLf", 'L', GT_Utility.copyAmount(1L, aStack) });
		GT_Values.RA.addLatheRecipe(GT_Utility.copyAmount(1L, aStack), GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Wood, 4L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 2L), 160, 8);

		if (!CORE.GTNH) {
			GT_Values.RA.addAssemblerRecipe(GT_Utility.copyAmount(1L, aStack), ItemList.Circuit_Integrated.getWithDamage(0L, 2L, new Object[0]), Materials.SeedOil.getFluid(50L), ItemList.FR_Stick.get(1L, new Object[0]), 16, 8);
			GT_Values.RA.addAssemblerRecipe(GT_Utility.copyAmount(8L, aStack), ItemList.Circuit_Integrated.getWithDamage(0L, 8L, new Object[0]), Materials.SeedOil.getFluid(250L), ItemList.FR_Casing_Impregnated.get(1L, new Object[0]), 64, 16);		
			GT_Values.RA.addChemicalBathRecipe(GT_Utility.copyAmount(1L, aStack), Materials.Creosote.getFluid(1000L), GT_ModHandler.getModItem("Railcraft", "tile.railcraft.cube", 1L, 8), null, null, null, 16, 16);
		}

		final short aMeta = (short)aStack.getItemDamage();
		if (aMeta == 32767) {
			if (GT_Utility.areStacksEqual(GT_ModHandler.getSmeltingOutput(GT_Utility.copyAmount(1L, aStack), false, null), new ItemStack(Items.coal, 1, 1))) {
				addPyrolyeOvenRecipes(aStack);
				if (GregTech_API.sRecipeFile.get(ConfigCategories.Recipes.disabledrecipes, "wood2charcoalsmelting", true)) {
					GT_ModHandler.removeFurnaceSmelting(GT_Utility.copyAmount(1L, aStack));
				}
			}
			for (int i = 0; i < 32767; ++i) {
				if (GT_Utility.areStacksEqual(GT_ModHandler.getSmeltingOutput(new ItemStack(aStack.getItem(), 1, i), false, null), new ItemStack(Items.coal, 1, 1))) {
					addPyrolyeOvenRecipes(aStack);
					if (GregTech_API.sRecipeFile.get(ConfigCategories.Recipes.disabledrecipes, "wood2charcoalsmelting", true)) {
						GT_ModHandler.removeFurnaceSmelting(new ItemStack(aStack.getItem(), 1, i));
					}
				}
				final ItemStack tStack = GT_ModHandler.getRecipeOutput(new ItemStack(aStack.getItem(), 1, i));
				if (tStack == null) {
					if (i >= 16) {
						break;
					}
				}
				else {
					final ItemStack tPlanks = GT_Utility.copy(tStack);
					tPlanks.stackSize = tPlanks.stackSize * 3 / 2;
					GT_Values.RA.addCutterRecipe(new ItemStack(aStack.getItem(), 1, i), Materials.Lubricant.getFluid(1L), GT_Utility.copy(tPlanks), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 1L), 200, 8);
					GT_Values.RA.addCutterRecipe(new ItemStack(aStack.getItem(), 1, i), GT_Utility.copyAmount(GT_Mod.gregtechproxy.mNerfedWoodPlank ? ((long)tStack.stackSize) : ((long)(tStack.stackSize * 5 / 4)), tStack), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 2L), 200, 8);
					GT_ModHandler.addSawmillRecipe(new ItemStack(aStack.getItem(), 1, i), tPlanks, GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 1L));
					GT_ModHandler.removeRecipe(new ItemStack(aStack.getItem(), 1, i));
					GT_ModHandler.addCraftingRecipe(GT_Utility.copyAmount(GT_Mod.gregtechproxy.mNerfedWoodPlank ? ((long)tStack.stackSize) : ((long)(tStack.stackSize * 5 / 4)), tStack), new Object[] { "s", "L", 'L', new ItemStack(aStack.getItem(), 1, i) });
					GT_ModHandler.addShapelessCraftingRecipe(GT_Utility.copyAmount(tStack.stackSize / (GT_Mod.gregtechproxy.mNerfedWoodPlank ? 2 : 1), tStack), new Object[] { new ItemStack(aStack.getItem(), 1, i) });
				}
			}
		}
		else {
			if (GT_Utility.areStacksEqual(GT_ModHandler.getSmeltingOutput(GT_Utility.copyAmount(1L, aStack), false, null), new ItemStack(Items.coal, 1, 1))) {
				addPyrolyeOvenRecipes(aStack);
				if (GregTech_API.sRecipeFile.get(ConfigCategories.Recipes.disabledrecipes, "wood2charcoalsmelting", true)) {
					GT_ModHandler.removeFurnaceSmelting(GT_Utility.copyAmount(1L, aStack));
				}
			}
			final ItemStack tStack2 = GT_ModHandler.getRecipeOutput(GT_Utility.copyAmount(1L, aStack));
			if (tStack2 != null) {
				final ItemStack tPlanks2 = GT_Utility.copy(tStack2);
				tPlanks2.stackSize = tPlanks2.stackSize * 3 / 2;
				GT_Values.RA.addCutterRecipe(GT_Utility.copyAmount(1L, aStack), Materials.Lubricant.getFluid(1L), GT_Utility.copy(tPlanks2), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 1L), 200, 8);
				GT_Values.RA.addCutterRecipe(GT_Utility.copyAmount(1L, aStack), GT_Utility.copyAmount(GT_Mod.gregtechproxy.mNerfedWoodPlank ? ((long)tStack2.stackSize) : ((long)(tStack2.stackSize * 5 / 4)), tStack2), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 2L), 200, 8);
				GT_ModHandler.addSawmillRecipe(GT_Utility.copyAmount(1L, aStack), tPlanks2, GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 1L));
				GT_ModHandler.removeRecipe(GT_Utility.copyAmount(1L, aStack));
				GT_ModHandler.addCraftingRecipe(GT_Utility.copyAmount(GT_Mod.gregtechproxy.mNerfedWoodPlank ? ((long)tStack2.stackSize) : ((long)(tStack2.stackSize * 5 / 4)), tStack2), new Object[] { "s", "L", 'L', GT_Utility.copyAmount(1L, aStack) });
				GT_ModHandler.addShapelessCraftingRecipe(GT_Utility.copyAmount(tStack2.stackSize / (GT_Mod.gregtechproxy.mNerfedWoodPlank ? 2 : 1), tStack2), new Object[] { GT_Utility.copyAmount(1L, aStack) });
			}
		}
		if (GT_Utility.areStacksEqual(GT_ModHandler.getSmeltingOutput(GT_Utility.copyAmount(1L, aStack), false, null), new ItemStack(Items.coal, 1, 1))) {
			addPyrolyeOvenRecipes(aStack);
			if (GregTech_API.sRecipeFile.get(ConfigCategories.Recipes.disabledrecipes, "wood2charcoalsmelting", true)) {
				GT_ModHandler.removeFurnaceSmelting(GT_Utility.copyAmount(1L, aStack));
			}
		}
	}

	public static void addSaplingRecipes(final ItemStack aStack) {
		GT_ModHandler.addPulverisationRecipe(GT_Utility.copyAmount(1L, aStack), GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Wood, 2L), null, 0, false);
		GT_ModHandler.addCompressionRecipe(GT_Utility.copyAmount(8L, aStack), ItemList.IC2_Plantball.get(1L, new Object[0]));
		GT_Values.RA.addLatheRecipe(GT_Utility.copyAmount(1L, aStack), GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 1L), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Wood, 1L), 16, 8);
	}

	public static void addPyrolyeOvenRecipes(final ItemStack logStack) {

		Materials aOilHeavy = MaterialUtils.getMaterial("OilHeavy");
		Materials aCharcoalByproducts = MaterialUtils.getMaterial("CharcoalByproducts");
		Materials aWoodGas = MaterialUtils.getMaterial("WoodGas");
		Materials aWoodVinegar = MaterialUtils.getMaterial("WoodVinegar");
		Materials aWoodTar = MaterialUtils.getMaterial("WoodTar");


		CORE.RA.addPyrolyseRecipe(GT_Utility.copyAmount(16L, logStack), GT_Values.NF, 1, MaterialHelper.getGems(Materials.Charcoal, 20), Materials.Creosote.getFluid(4000L), 640, 64);
		CORE.RA.addPyrolyseRecipe(GT_Utility.copyAmount(16L, logStack), Materials.Nitrogen.getGas(1000L), 2, MaterialHelper.getGems(Materials.Charcoal, 20), Materials.Creosote.getFluid(4000L), 320, 96);
		
		if (aOilHeavy != null) {
			CORE.RA.addPyrolyseRecipe(GT_Utility.copyAmount(16L, logStack), GT_Values.NF, 3, MaterialHelper.getDust(Materials.Ash, 4), aOilHeavy.getFluid(200L), 320, 192);
			
		}
		if (aCharcoalByproducts != null) {
			CORE.RA.addPyrolyseRecipe(GT_Utility.copyAmount(16L, logStack), GT_Values.NF, 3, MaterialHelper.getGems(Materials.Charcoal, 20), aCharcoalByproducts.getGas(4000L), 640, 64);
			CORE.RA.addPyrolyseRecipe(GT_Utility.copyAmount(16L, logStack), Materials.Nitrogen.getGas(1000L), 4, MaterialHelper.getGems(Materials.Charcoal, 20), aCharcoalByproducts.getGas(4000L), 320, 96);
			
		}
		if (aWoodGas != null) {
			CORE.RA.addPyrolyseRecipe(GT_Utility.copyAmount(16L, logStack), GT_Values.NF, 5, MaterialHelper.getGems(Materials.Charcoal, 20), aWoodGas.getGas(1500L), 640, 64);
			CORE.RA.addPyrolyseRecipe(GT_Utility.copyAmount(16L, logStack), Materials.Nitrogen.getGas(1000L), 6, MaterialHelper.getGems(Materials.Charcoal, 20), aWoodGas.getGas(1500L), 320, 96);
			
		}
		if (aWoodVinegar != null) {
			CORE.RA.addPyrolyseRecipe(GT_Utility.copyAmount(16L, logStack), GT_Values.NF, 7, MaterialHelper.getGems(Materials.Charcoal, 20), aWoodVinegar.getFluid(3000L), 640, 64);
			CORE.RA.addPyrolyseRecipe(GT_Utility.copyAmount(16L, logStack), Materials.Nitrogen.getGas(1000L), 8, MaterialHelper.getGems(Materials.Charcoal, 20), aWoodVinegar.getFluid(3000L), 320, 96);
			
		}
		if (aWoodTar != null) {
			CORE.RA.addPyrolyseRecipe(GT_Utility.copyAmount(16L, logStack), GT_Values.NF, 9, MaterialHelper.getGems(Materials.Charcoal, 20), aWoodTar.getFluid(1500L), 640, 64);
			CORE.RA.addPyrolyseRecipe(GT_Utility.copyAmount(16L, logStack), Materials.Nitrogen.getGas(1000L), 10, MaterialHelper.getGems(Materials.Charcoal, 20), aWoodTar.getFluid(1500L), 320, 96);
		
		}
	}

}
