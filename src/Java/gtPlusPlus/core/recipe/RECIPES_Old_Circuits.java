package gtPlusPlus.core.recipe;

import cpw.mods.fml.common.Loader;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.*;
import gregtech.api.interfaces.IOreRecipeRegistrator;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;

import codechicken.nei.api.API;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.RecipeUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public class RECIPES_Old_Circuits  implements IOreRecipeRegistrator {
	public RECIPES_Old_Circuits() {
		OrePrefixes.crafting.add(this);
	}

	@Override
	public void registerOre(final OrePrefixes aPrefix, final Materials aMaterial, final String aOreDictName, final String aModName, final ItemStack aStack) {
		if (aOreDictName.equals(OreDictNames.craftingLensRed.toString())) {
			Logger.INFO("[Old Feature - Circuits] Adding recipes for old circuits. (Part 2)");
			GT_Values.RA.addLaserEngraverRecipe(GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Copper, 1L), GT_Utility.copyAmount(0L, new Object[]{aStack}), GregtechItemList.Old_Circuit_Parts_Wiring_Basic.get(1L, new Object[0]), 64, 30);
			GT_Values.RA.addLaserEngraverRecipe(GT_OreDictUnificator.get(OrePrefixes.foil, Materials.AnnealedCopper, 1L), GT_Utility.copyAmount(0L, new Object[]{aStack}), GregtechItemList.Old_Circuit_Parts_Wiring_Basic.get(1L, new Object[0]), 64, 30);
			GT_Values.RA.addLaserEngraverRecipe(GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Gold, 1L), GT_Utility.copyAmount(0L, new Object[]{aStack}), GregtechItemList.Old_Circuit_Parts_Wiring_Advanced.get(1L, new Object[0]), 64, 120);
			GT_Values.RA.addLaserEngraverRecipe(GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Electrum, 1L), GT_Utility.copyAmount(0L, new Object[]{aStack}), GregtechItemList.Old_Circuit_Parts_Wiring_Advanced.get(1L, new Object[0]), 64, 120);
			GT_Values.RA.addLaserEngraverRecipe(GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Platinum, 1L), GT_Utility.copyAmount(0L, new Object[]{aStack}), GregtechItemList.Old_Circuit_Parts_Wiring_Elite.get(1L, new Object[0]), 64, 480);
		} 

		else if (aOreDictName.equals(OreDictNames.craftingLensGreen.toString())) {
			Logger.INFO("[Old Feature - Circuits] Adding recipes for old circuits. (Part 3)");
			GT_Values.RA.addLaserEngraverRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Olivine, 1L), GT_Utility.copyAmount(0L, new Object[]{aStack}), GregtechItemList.Old_Circuit_Parts_Crystal_Chip_Elite.get(1L, new Object[0]), 256, 480);
			GT_Values.RA.addLaserEngraverRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Emerald, 1L), GT_Utility.copyAmount(0L, new Object[]{aStack}), GregtechItemList.Old_Circuit_Parts_Crystal_Chip_Elite.get(1L, new Object[0]), 256, 480);
		} 

		else if (aOreDictName.equals(OreDictNames.craftingLensBlue.toString()) || aOreDictName.equals(OreDictNames.craftingLensCyan.toString()) || aOreDictName.equals(OreDictNames.craftingLensLightBlue.toString())) {
			Logger.INFO("[Old Feature - Circuits] Adding recipes for old circuits. (Part 4)");
			GT_Values.RA.addLaserEngraverRecipe(ItemList.IC2_LapotronCrystal.getWildcard(1L, new Object[0]), GT_Utility.copyAmount(0L, new Object[]{aStack}), GregtechItemList.Old_Circuit_Parts_Crystal_Chip_Master.get(3L, new Object[0]), 256, 480);
		}		
	}


	private static boolean addCircuitRecipes(){
		Logger.INFO("[Old Feature - Circuits] Adding recipes for old circuits. (Part 1)");
		GT_ModHandler.addShapelessCraftingRecipe(GregtechItemList.Old_Circuit_Primitive.get(1L, new Object[0]), new Object[]{GT_ModHandler.getIC2Item("casingadviron", 1L), OrePrefixes.wireGt01.get(Materials.RedAlloy), OrePrefixes.wireGt01.get(Materials.RedAlloy), OrePrefixes.wireGt01.get(Materials.Tin)});
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Old_Circuit_Basic.get(1L, new Object[0]), new Object[]{"WWW", "CPC", "WWW", 'C', OrePrefixes.circuit.get(Materials.Primitive), 'W', OreDictNames.craftingWireCopper, 'P', OrePrefixes.plate.get(Materials.Steel)});
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Old_Circuit_Basic.get(1L, new Object[0]), new Object[]{"WCW", "WPW", "WCW", 'C', OrePrefixes.circuit.get(Materials.Primitive), 'W', OreDictNames.craftingWireCopper, 'P', OrePrefixes.plate.get(Materials.Steel)});
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Old_Circuit_Basic.get(1L, new Object[0]), new Object[]{"WWW", "CPC", "WWW", 'C', OrePrefixes.circuit.get(Materials.Primitive), 'W', OrePrefixes.cableGt01.get(Materials.RedAlloy), 'P', OrePrefixes.plate.get(Materials.Steel)});
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Old_Circuit_Basic.get(1L, new Object[0]), new Object[]{"WCW", "WPW", "WCW", 'C', OrePrefixes.circuit.get(Materials.Primitive), 'W', OrePrefixes.cableGt01.get(Materials.RedAlloy), 'P', OrePrefixes.plate.get(Materials.Steel)});

		GT_Values.RA.addFormingPressRecipe(GregtechItemList.Old_Empty_Board_Basic.get(1L, new Object[0]), GregtechItemList.Old_Circuit_Parts_Wiring_Basic.get(4L, new Object[0]), GregtechItemList.Old_Circuit_Board_Basic.get(1L, new Object[0]), 32, 16);
		GT_Values.RA.addFormingPressRecipe(GregtechItemList.Old_Empty_Board_Basic.get(1L, new Object[0]), GregtechItemList.Old_Circuit_Parts_Wiring_Advanced.get(4L, new Object[0]), GregtechItemList.Old_Circuit_Board_Advanced.get(1L, new Object[0]), 32, 64);
		GT_Values.RA.addFormingPressRecipe(GregtechItemList.Old_Empty_Board_Elite.get(1L, new Object[0]), GregtechItemList.Old_Circuit_Parts_Wiring_Elite.get(4L, new Object[0]), GregtechItemList.Old_Circuit_Board_Elite.get(1L, new Object[0]), 32, 256);

		GT_Values.RA.addFormingPressRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Lapis, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Glowstone, 1L), GregtechItemList.Old_Circuit_Parts_Advanced.get(2L, new Object[0]), 32, 64);
		GT_Values.RA.addFormingPressRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Lazurite, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Glowstone, 1L), GregtechItemList.Old_Circuit_Parts_Advanced.get(2L, new Object[0]), 32, 64);

		int tMultiplier;
		for (Materials tMat : Materials.values()) {
			if ((tMat.mStandardMoltenFluid != null) && (tMat.contains(SubTag.SOLDERING_MATERIAL)))
			{
				tMultiplier = tMat.contains(SubTag.SOLDERING_MATERIAL_BAD) ? 4 : tMat.contains(SubTag.SOLDERING_MATERIAL_GOOD) ? 1 : 2;

				GT_Values.RA.addAssemblerRecipe(ItemList.IC2_Item_Casing_Steel.get(1L, new Object[0]), GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.RedAlloy, 2L), tMat.getMolten(144L * tMultiplier / 8L), GregtechItemList.Old_Circuit_Primitive.get(1L, new Object[0]), 16, 8);
				GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Plastic, 1L), GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.RedAlloy, 1L), tMat.getMolten(144L * tMultiplier / 8L), GregtechItemList.Old_Circuit_Primitive.get(1L, new Object[0]), 16, 8);
				GT_Values.RA.addAssemblerRecipe(GregtechItemList.Old_Circuit_Board_Basic.get(1L, new Object[0]), GregtechItemList.Old_Circuit_Primitive.get(2L, new Object[0]), tMat.getMolten(144L * tMultiplier / 4L), GregtechItemList.Old_Circuit_Basic.get(1L, new Object[0]), 32, 16);
				GT_Values.RA.addAssemblerRecipe(GregtechItemList.Old_Circuit_Basic.get(1L, new Object[0]), GregtechItemList.Old_Circuit_Primitive.get(2L, new Object[0]), tMat.getMolten(144L * tMultiplier / 4L), GregtechItemList.Old_Circuit_Good.get(1L, new Object[0]), 32, 16);
				GT_Values.RA.addAssemblerRecipe(GregtechItemList.Old_Circuit_Board_Advanced.get(1L, new Object[0]), GregtechItemList.Old_Circuit_Parts_Advanced.get(2L, new Object[0]), tMat.getMolten(144L * tMultiplier / 2L), GregtechItemList.Old_Circuit_Advanced.get(1L, new Object[0]), 32, 64);
				GT_Values.RA.addAssemblerRecipe(GregtechItemList.Old_Circuit_Board_Advanced.get(1L, new Object[0]), GregtechItemList.Old_Circuit_Parts_Crystal_Chip_Elite.get(1L, new Object[0]), tMat.getMolten(144L * tMultiplier / 2L), GregtechItemList.Old_Circuit_Data.get(1L, new Object[0]), 32, 64);
				GT_Values.RA.addAssemblerRecipe(GregtechItemList.Old_Circuit_Board_Elite.get(1L, new Object[0]), GregtechItemList.Old_Circuit_Data.get(3L, new Object[0]), tMat.getMolten(144L * tMultiplier / 1L), GregtechItemList.Old_Circuit_Elite.get(1L, new Object[0]), 32, 256);
				GT_Values.RA.addAssemblerRecipe(GregtechItemList.Old_Circuit_Board_Elite.get(1L, new Object[0]), ItemList.Circuit_Parts_Crystal_Chip_Master.get(3L, new Object[0]), tMat.getMolten(144L * tMultiplier / 1L), GregtechItemList.Old_Circuit_Master.get(1L, new Object[0]), 32, 256);
				GT_Values.RA.addAssemblerRecipe(GregtechItemList.Old_Circuit_Board_Elite.get(1L, new Object[0]), GregtechItemList.Old_Circuit_Parts_Crystal_Chip_Master.get(3L, new Object[0]), tMat.getMolten(144L * tMultiplier / 1L), GregtechItemList.Old_Circuit_Master.get(1L, new Object[0]), 32, 256);
				GT_Values.RA.addAssemblerRecipe(GregtechItemList.Old_Circuit_Data.get(1L, new Object[0]), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Plastic, 2L), tMat.getMolten(144L * tMultiplier / 2L), GregtechItemList.Old_Tool_DataStick.get(1L, new Object[0]), 128, 64);
			}
		}

		GT_Values.RA.addAssemblerRecipe(GregtechItemList.Old_Circuit_Elite.get(2L, new Object[0]), GregtechItemList.Old_Circuit_Parts_Crystal_Chip_Elite.get(18L, new Object[0]), GT_Values.NF, GregtechItemList.Old_Tool_DataOrb.get(1L, new Object[0]), 512, 256);
		GT_Values.RA.addAssemblerRecipe(GregtechItemList.Old_Circuit_Master.get(2L, new Object[0]), ItemList.Circuit_Parts_Crystal_Chip_Master.get(18L, new Object[0]), GT_Values.NF, ItemList.Energy_LapotronicOrb.get(1L, new Object[0]), 512, 1024);
		GT_Values.RA.addAssemblerRecipe(GregtechItemList.Old_Circuit_Master.get(2L, new Object[0]), GregtechItemList.Old_Circuit_Parts_Crystal_Chip_Master.get(18L, new Object[0]), GT_Values.NF, ItemList.Energy_LapotronicOrb.get(1L, new Object[0]), 512, 1024);
		GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Silicon, 1L), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Plastic, 1L), GregtechItemList.Old_Empty_Board_Basic.get(1L, new Object[0]), 32, 16);

		Materials plasticType = Materials.get("Polytetrafluoroethylene") != null ? Materials.get("Polytetrafluoroethylene") : Materials.Plastic;
		GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Silicon, 2L), GT_OreDictUnificator.get(OrePrefixes.plate, plasticType, 1L), GregtechItemList.Old_Empty_Board_Elite.get(1L, new Object[0]), 32, 256);



		return true;
	}

	public static boolean handleCircuits(){

		hideCircuitsNEI();
		addCircuitRecipes();
		removeNewCircuits();
		generateTradeRecipes();
		return true;
	}
	
	private static boolean setItemList(ItemList Set, GregtechItemList Get) {
		try{
			Set.set(Get.get(1));
			return true;
		}
		catch (Throwable t) {}
		return false;
	}

	private static boolean removeNewCircuits(){
		Logger.INFO("[Old Feature - Circuits] Overriding .28+ circuit values in the GT5u Itemlist with values from GT++.");

		boolean newVersion = CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK && Utils.getGregtechSubVersion() >= 30;
		
		setItemList(ItemList.Circuit_Primitive, GregtechItemList.Old_Circuit_Primitive);
		setItemList(ItemList.Circuit_Basic, GregtechItemList.Old_Circuit_Basic);
		if (newVersion) {
			setItemList(ItemList.valueOf("Circuit_Integrated_Good"), GregtechItemList.Old_Circuit_Good); //New
		}		
		setItemList(ItemList.Circuit_Good, GregtechItemList.Old_Circuit_Good);
		setItemList(ItemList.Circuit_Advanced, GregtechItemList.Old_Circuit_Advanced);
		//ItemList.Circuit_Data, GregtechItemList.Old_Circuit_Data);
		setItemList(ItemList.Circuit_Elite, GregtechItemList.Old_Circuit_Elite);
		setItemList(ItemList.Circuit_Master, GregtechItemList.Old_Circuit_Master);
		setItemList(ItemList.Circuit_Ultimate, GregtechItemList.Old_Circuit_Ultimate);

		/**
		 * Try Set New circuits to have old replacements
		 */

		//Basic
		if (newVersion) {
			setItemList(ItemList.valueOf("Circuit_Microprocessor"), GregtechItemList.Old_Circuit_Basic);	//NEW	
		}
		//Good
		if (newVersion) {
			setItemList(ItemList.valueOf("Circuit_Integrated"), GregtechItemList.Old_Circuit_Good);		
		}
		//Advanced
		if (newVersion) {
			setItemList(ItemList.valueOf("Circuit_Nanoprocessor"), GregtechItemList.Old_Circuit_Advanced);		
		}
		//Data
		if (newVersion) {
			setItemList(ItemList.valueOf("Circuit_Quantumprocessor"), GregtechItemList.Old_Circuit_Data);
		}
		if (newVersion) {
			setItemList(ItemList.valueOf("Circuit_Nanocomputer"), GregtechItemList.Old_Circuit_Data);	
		}
		//Elite
		if (newVersion) {
			setItemList(ItemList.valueOf("Circuit_Crystalprocessor"), GregtechItemList.Old_Circuit_Elite);
		}
		if (newVersion) {
			setItemList(ItemList.valueOf("Circuit_Quantumcomputer"), GregtechItemList.Old_Circuit_Elite);
		}
		if (newVersion) {
			setItemList(ItemList.valueOf("Circuit_Elitenanocomputer"), GregtechItemList.Old_Circuit_Elite);		
		}
		//Master
		if (newVersion) {
			setItemList(ItemList.valueOf("Circuit_Neuroprocessor"), GregtechItemList.Old_Circuit_Master);
		}
		if (newVersion) {
			setItemList(ItemList.valueOf("Circuit_Masterquantumcomputer"), GregtechItemList.Old_Circuit_Master);	
		}
		//Ultimate
		if (newVersion) {
			setItemList(ItemList.valueOf("Circuit_Wetwarecomputer"), GregtechItemList.Old_Circuit_Ultimate);
		}
		if (newVersion) {
			setItemList(ItemList.valueOf("Circuit_Ultimatecrystalcomputer"), GregtechItemList.Old_Circuit_Ultimate);
		}
		if (newVersion) {
			setItemList(ItemList.valueOf("Circuit_Quantummainframe"), GregtechItemList.Old_Circuit_Ultimate);	
		}
		//Superconductor
		if (newVersion) {
			setItemList(ItemList.valueOf("Circuit_Wetwaresupercomputer"), GregtechItemList.Circuit_IV);
		}
		if (newVersion) {
			setItemList(ItemList.valueOf("Circuit_Crystalmainframe"), GregtechItemList.Circuit_IV);
		}
		//Infinite
		if (newVersion) {
			setItemList(ItemList.valueOf("Circuit_Wetwaremainframe"), GregtechItemList.Circuit_LuV);		
		}

		//set data orbs and sticks to their new replacements
		setItemList(ItemList.Tool_DataStick, GregtechItemList.Old_Tool_DataStick);
		setItemList(ItemList.Tool_DataOrb, GregtechItemList.Old_Tool_DataOrb);

		setItemList(ItemList.Circuit_Board_Basic, GregtechItemList.Old_Circuit_Board_Basic);
		setItemList(ItemList.Circuit_Board_Advanced, GregtechItemList.Old_Circuit_Board_Advanced);
		setItemList(ItemList.Circuit_Board_Elite, GregtechItemList.Old_Circuit_Board_Elite);
		setItemList(ItemList.Circuit_Parts_Advanced, GregtechItemList.Old_Circuit_Parts_Advanced);
		setItemList(ItemList.Circuit_Parts_Wiring_Basic, GregtechItemList.Old_Circuit_Parts_Wiring_Basic);
		setItemList(ItemList.Circuit_Parts_Wiring_Advanced, GregtechItemList.Old_Circuit_Parts_Wiring_Advanced);
		setItemList(ItemList.Circuit_Parts_Wiring_Elite, GregtechItemList.Old_Circuit_Parts_Wiring_Elite);
		setItemList(ItemList.Circuit_Parts_Crystal_Chip_Elite, GregtechItemList.Old_Circuit_Parts_Crystal_Chip_Elite);
		setItemList(ItemList.Circuit_Parts_Crystal_Chip_Master, GregtechItemList.Old_Circuit_Parts_Crystal_Chip_Master);

		return true;
	}

	private static boolean generateTradeRecipes(){

		//Data stick and Data orbs.		
		//GT Type to GT++ Type
		RecipeUtils.recipeBuilder(
				CI.craftingToolScrewdriver, null, null,
				ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01:32708", 32708, 1), null, null,
				null, null, null, 
				GregtechItemList.Old_Tool_DataStick.get(1));
		RecipeUtils.recipeBuilder(
				CI.craftingToolScrewdriver, null, null,
				ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01:32707", 32707, 1), null, null,
				null, null, null, 
				GregtechItemList.Old_Tool_DataOrb.get(1));

		//GT++ Type to GT Type
		RecipeUtils.recipeBuilder(
				CI.craftingToolScrewdriver, null, null,
				GregtechItemList.Old_Tool_DataStick.get(1), null, null,
				null, null, null, 
				ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01:32708", 32708, 1));
		RecipeUtils.recipeBuilder(
				CI.craftingToolScrewdriver, null, null,
				GregtechItemList.Old_Tool_DataOrb.get(1), null, null,
				null, null, null, 
				ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01:32707", 32707, 1));



		//Primitive
		RecipeUtils.addShapelessGregtechRecipe(
				new ItemStack[]{ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01:32700", 32700, 1)}, 
				ItemUtils.getItemStackOfAmountFromOreDict("circuitPrimitive", 1));

		//Basic
		RecipeUtils.addShapelessGregtechRecipe(
				new ItemStack[]{ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01:32701", 32701, 1)}, 
				ItemUtils.getItemStackOfAmountFromOreDict("circuitBasic", 1));
		RecipeUtils.addShapelessGregtechRecipe(
				new ItemStack[]{ItemUtils.simpleMetaStack("gregtech:gt.metaitem.03:32078", 32078, 1)}, 
				ItemUtils.getItemStackOfAmountFromOreDict("circuitBasic", 1));

		//Good
		RecipeUtils.addShapelessGregtechRecipe(
				new ItemStack[]{ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01:32702", 32702, 1)}, 
				ItemUtils.getItemStackOfAmountFromOreDict("circuitGood", 1));
		RecipeUtils.addShapelessGregtechRecipe(
				new ItemStack[]{ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01:32702", 32702, 1)}, 
				ItemUtils.getItemStackOfAmountFromOreDict("circuitGood", 1));

		//Advanced
		RecipeUtils.addShapelessGregtechRecipe(
				new ItemStack[]{ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01:32703", 32703, 1)}, 
				ItemUtils.getItemStackOfAmountFromOreDict("circuitAdvanced", 1));
		RecipeUtils.addShapelessGregtechRecipe(
				new ItemStack[]{ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01:32703", 32703, 1)}, 
				ItemUtils.getItemStackOfAmountFromOreDict("circuitAdvanced", 1));

		//Data
		RecipeUtils.addShapelessGregtechRecipe(
				new ItemStack[]{ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01:32704", 32704, 1)}, 
				ItemUtils.getItemStackOfAmountFromOreDict("circuitData", 1));

		//Elite
		RecipeUtils.addShapelessGregtechRecipe(
				new ItemStack[]{ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01:32705", 32705, 1)}, 
				ItemUtils.getItemStackOfAmountFromOreDict("circuitElite", 1));

		//Master
		RecipeUtils.addShapelessGregtechRecipe(
				new ItemStack[]{ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01:32706", 32706, 1)}, 
				ItemUtils.getItemStackOfAmountFromOreDict("circuitMaster", 1));



		//Components
		//Green Chip
		RecipeUtils.addShapelessGregtechRecipe(
				new ItemStack[]{ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01:32713", 32713, 1)}, 
				GregtechItemList.Old_Circuit_Parts_Crystal_Chip_Elite.get(1));
		//Blue Chip
		RecipeUtils.addShapelessGregtechRecipe(
				new ItemStack[]{ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01:32714", 32714, 1)}, 
				GregtechItemList.Old_Circuit_Parts_Crystal_Chip_Master.get(1));

		//Basic Board
		RecipeUtils.addShapelessGregtechRecipe(
				new ItemStack[]{ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01:32710", 32710, 1)}, 
				GregtechItemList.Old_Circuit_Board_Basic.get(1));
		//Advanced Board
		RecipeUtils.addShapelessGregtechRecipe(
				new ItemStack[]{ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01:32711", 32711, 1)}, 
				GregtechItemList.Old_Circuit_Board_Advanced.get(1));
		//Elite Board
		RecipeUtils.addShapelessGregtechRecipe(
				new ItemStack[]{ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01:32712", 32712, 1)}, 
				GregtechItemList.Old_Circuit_Board_Elite.get(1));


		//remove a few recipes
		GT_ModHandler.removeRecipeByOutput(ItemUtils.simpleMetaStack("gregtech:gt.metaitem.03:32070", 32070, 1));
		GT_ModHandler.removeRecipeByOutput(ItemUtils.simpleMetaStack("gregtech:gt.metaitem.03:32069", 32069, 1));
		if (LoadedMods.Extra_Utils){
			ItemStack EQU = ItemUtils.simpleMetaStack("ExtraUtilities:enderQuarryUpgrade", 0, 1);
			if (EQU != null){
				GT_ModHandler.removeRecipeByOutput(EQU);
				GT_Values.RA.addAssemblerRecipe(
						ItemUtils.simpleMetaStack("ExtraUtilities:decorativeBlock1:12", 12, 1), 
						GregtechItemList.Old_Circuit_Master.get(1), 
						EQU, 
						80*20, 
						2);
			}			
		}
		if (LoadedMods.GalacticraftCore){
			ItemStack ACW = ItemUtils.simpleMetaStack("GalacticraftCore:item.basicItem:14", 14, 1);
			if (ACW != null){
				GT_ModHandler.removeRecipeByOutput(ACW);
				GT_Values.RA.addAssemblerRecipe(
						ItemUtils.getItemStackOfAmountFromOreDict("gemDiamond", 1),
						GregtechItemList.Old_Circuit_Board_Advanced.get(1), 
						ACW, 
						160*20, 
						4);
			}
			ItemStack ACW2 = ItemUtils.simpleMetaStack("GalacticraftCore:item.basicItem:13", 13, 1);
			if (ACW2 != null){
				GT_ModHandler.removeRecipeByOutput(ACW2);
				GT_Values.RA.addAssemblerRecipe(
						ItemUtils.getItemStackOfAmountFromOreDict("gemDiamond", 1),
						GregtechItemList.Old_Circuit_Board_Basic.get(1), 
						ACW2, 
						80*20, 
						2);
			}
		}

		return true;
	}

	private static boolean hideCircuitsNEI(){
		Boolean isNEILoaded = Loader.isModLoaded("NotEnoughItems");
		if (isNEILoaded && !CORE.ConfigSwitches.showHiddenNEIItems){
			Logger.INFO("[Old Feature - Circuits] Hiding .28+ circuits in NEI.");
			String[] CircuitToHide = {
					"Circuit_Board_Basic",
					"Circuit_Board_Advanced",
					"Circuit_Board_Elite",
					"Circuit_Parts_Advanced",
					"Circuit_Parts_Wiring_Basic",
					"Circuit_Parts_Wiring_Advanced",
					"Circuit_Parts_Wiring_Elite",
					"Circuit_Parts_Crystal_Chip_Elite",
					"Circuit_Parts_Crystal_Chip_Master",
					"Circuit_Primitive",
					"Circuit_Basic",
					"Circuit_Integrated_Good",
					"Circuit_Good",
					"Circuit_Advanced",
					"Circuit_Data",
					"Circuit_Elite",
					"Circuit_Master",
					"Circuit_Ultimate",
					"Circuit_Board_Coated", 
					"Circuit_Board_Phenolic", 
					"Circuit_Board_Epoxy", 
					"Circuit_Board_Fiberglass", 
					"Circuit_Board_Multifiberglass", 
					"Circuit_Board_Wetware", 
					"Circuit_Parts_Resistor", 
					"Circuit_Parts_ResistorSMD", 
					"Circuit_Parts_Glass_Tube", 
					"Circuit_Parts_Vacuum_Tube", 
					"Circuit_Parts_Coil", 
					"Circuit_Parts_Diode", 
					"Circuit_Parts_DiodeSMD", 
					"Circuit_Parts_Transistor", 
					"Circuit_Parts_TransistorSMD", 
					"Circuit_Parts_Capacitor", 
					"Circuit_Parts_CapacitorSMD", 
					"Circuit_Silicon_Ingot", 
					"Circuit_Silicon_Ingot2", 
					"Circuit_Silicon_Ingot3", 
					"Circuit_Silicon_Wafer", 
					"Circuit_Silicon_Wafer2", 
					"Circuit_Silicon_Wafer3",
					"Circuit_Wafer_ILC", 
					"Circuit_Chip_ILC", 
					"Circuit_Wafer_Ram", 
					"Circuit_Chip_Ram", 
					"Circuit_Wafer_NAND", 
					"Circuit_Chip_NAND", 
					"Circuit_Wafer_NOR", 
					"Circuit_Chip_NOR", 
					"Circuit_Wafer_CPU", 
					"Circuit_Chip_CPU", 
					"Circuit_Wafer_SoC", 
					"Circuit_Chip_SoC", 
					"Circuit_Wafer_SoC2", 
					"Circuit_Chip_SoC2", 
					"Circuit_Wafer_PIC", 
					"Circuit_Chip_PIC", 
					"Circuit_Wafer_HPIC", 
					"Circuit_Chip_HPIC", 
					"Circuit_Wafer_NanoCPU", 
					"Circuit_Chip_NanoCPU", 
					"Circuit_Wafer_QuantumCPU", 
					"Circuit_Chip_QuantumCPU", 
					"Circuit_Chip_CrystalCPU", 
					"Circuit_Chip_CrystalSoC", 
					"Circuit_Chip_NeuroCPU", 
					"Circuit_Chip_Stemcell",
					"Circuit_Processor", 
					"Circuit_Computer", 
					"Circuit_Nanoprocessor", 
					"Circuit_Nanocomputer", 
					"Circuit_Elitenanocomputer", 
					"Circuit_Quantumprocessor", 
					"Circuit_Quantumcomputer", 
					"Circuit_Masterquantumcomputer", 
					"Circuit_Quantummainframe", 
					"Circuit_Crystalprocessor", 
					"Circuit_Crystalcomputer", 
					"Circuit_Ultimatecrystalcomputer", 
					"Circuit_Crystalmainframe", 
					"Circuit_Neuroprocessor", 
					"Circuit_Wetwarecomputer", 
					"Circuit_Wetwaresupercomputer", 
					"Circuit_Wetwaremainframe", 
					"Circuit_Parts_RawCrystalChip",
					//Circuits Additions in .30/.31
					"Circuit_Board_Plastic",
					"Circuit_Parts_GlassFiber",
					"Circuit_Parts_PetriDish",
					"Circuit_Microprocessor"					
			};

			for (String component : CircuitToHide){
				try {
					API.hideItem(ItemList.valueOf(component).get(1L, new Object[0]));
				} catch (IllegalArgumentException I){
					Logger.INFO("Could not find "+component+" in the Gregtech item list.");
					Logger.INFO("This is NOT an error, simply a notification.");
				}
			}			
		}
		return true;
	}

}
