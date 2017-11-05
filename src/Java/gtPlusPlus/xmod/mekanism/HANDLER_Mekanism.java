package gtPlusPlus.xmod.mekanism;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import gregtech.api.util.GT_ModHandler;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.core.util.nbt.NBTUtils;
import gtPlusPlus.core.util.recipe.RecipeUtils;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class HANDLER_Mekanism {

	public static void preInit(){
		if (LoadedMods.Mekanism){

		}
	}

	public static void init(){
		if (LoadedMods.Mekanism){

		}
	}

	public static void postInit(){
		if (LoadedMods.Mekanism){
			
			Utils.LOG_INFO("Performing GT recipe balance for Mek. now that it's Osmium is useless.");
			
			//Steel Casing
			ItemStack tSteelCasing = ItemUtils.simpleMetaStack("Mekanism:BasicBlock:8", 8, 1);
			addNewRecipe(
					"plateSteel", "blockGlass", "plateSteel", 
					"blockGlass", "plateStainlessSteel", "blockGlass", 
					"plateSteel", "blockGlass", "plateSteel",
					tSteelCasing);

			//Energy Storage
			ItemStack tAdvancedEnergyCube = ItemUtils.simpleMetaStack("Mekanism:EnergyCube", 0, 1);
			NBTUtils.setString(tAdvancedEnergyCube, "tier", "Advanced");
			ItemStack tBasicEnergyCube = ItemUtils.simpleMetaStack("Mekanism:EnergyCube", 0, 1);
			NBTUtils.setString(tBasicEnergyCube, "tier", "Basic");

			//Gas tanks
			ItemStack tBasicGasTank = ItemUtils.simpleMetaStack("Mekanism:GasTank", 0, 1);
			NBTUtils.setInteger(tBasicGasTank, "tier", 0);
			ItemStack tAdvancedGasTank = ItemUtils.simpleMetaStack("Mekanism:GasTank", 0, 1);
			NBTUtils.setInteger(tAdvancedGasTank, "tier", 1);
			ItemStack tEliteGasTank = ItemUtils.simpleMetaStack("Mekanism:GasTank", 0, 1);
			NBTUtils.setInteger(tEliteGasTank, "tier", 2);
			ItemStack tMasterGasTank = ItemUtils.simpleMetaStack("Mekanism:GasTank", 0, 1);
			NBTUtils.setInteger(tMasterGasTank, "tier", 3);

			//Machines that use Osmium
			ItemStack tMachineBlock_Basic = ItemUtils.simpleMetaStack("Mekanism:MachineBlock:5", 5, 1);
			ItemStack tMachineBlock = ItemUtils.simpleMetaStack("Mekanism:MachineBlock:6", 6, 1);

			//Smelting
			ItemStack tMachineBlock_0_Basic = tMachineBlock_Basic;
			NBTUtils.setInteger(tMachineBlock_0_Basic, "recipeType", 0);
			ItemStack tMachineBlock_0 = tMachineBlock;
			NBTUtils.setInteger(tMachineBlock_0, "recipeType", 0);
			//Enriching
			ItemStack tMachineBlock_1_Basic = tMachineBlock_Basic;
			NBTUtils.setInteger(tMachineBlock_1_Basic, "recipeType", 1);
			ItemStack tMachineBlock_1 = tMachineBlock;
			NBTUtils.setInteger(tMachineBlock_1, "recipeType", 1);
			//Crushing
			ItemStack tMachineBlock_2_Basic = tMachineBlock_Basic;
			NBTUtils.setInteger(tMachineBlock_2_Basic, "recipeType", 2);
			ItemStack tMachineBlock_2 = tMachineBlock;
			NBTUtils.setInteger(tMachineBlock_2, "recipeType", 2);
			//Compressing
			ItemStack tMachineBlock_3_Basic = tMachineBlock_Basic;
			NBTUtils.setInteger(tMachineBlock_3_Basic, "recipeType", 3);
			ItemStack tMachineBlock_3 = tMachineBlock;
			NBTUtils.setInteger(tMachineBlock_3, "recipeType", 3);
			//Combining
			ItemStack tMachineBlock_4_Basic = tMachineBlock_Basic;
			NBTUtils.setInteger(tMachineBlock_4_Basic, "recipeType", 4);
			ItemStack tMachineBlock_4 = tMachineBlock;
			NBTUtils.setInteger(tMachineBlock_4, "recipeType", 4);
			//Purifying
			ItemStack tMachineBlock_5_Basic = tMachineBlock_Basic;
			NBTUtils.setInteger(tMachineBlock_5_Basic, "recipeType", 5);
			ItemStack tMachineBlock_5 = tMachineBlock;
			NBTUtils.setInteger(tMachineBlock_5, "recipeType", 5);
			//Injecting
			ItemStack tMachineBlock_6_Basic = tMachineBlock_Basic;
			NBTUtils.setInteger(tMachineBlock_6_Basic, "recipeType", 6);
			ItemStack tMachineBlock_6 = tMachineBlock;
			NBTUtils.setInteger(tMachineBlock_6, "recipeType", 6);
			//Infusing
			ItemStack tMachineBlock_7_Basic = tMachineBlock_Basic;
			NBTUtils.setInteger(tMachineBlock_7_Basic, "recipeType", 7);
			ItemStack tMachineBlock_7 = tMachineBlock;
			NBTUtils.setInteger(tMachineBlock_7, "recipeType", 7);

			//Infuser
			ItemStack tMachineBlock_8 = ItemUtils.simpleMetaStack("Mekanism:MachineBlock:8", 8, 1);
			//Purification
			ItemStack tMachineBlock_9 = ItemUtils.simpleMetaStack("Mekanism:MachineBlock:9", 9, 1);
			//Pump
			ItemStack tMachineBlock_12 = ItemUtils.simpleMetaStack("Mekanism:MachineBlock:12", 12, 1);

			//<Mekanism:ElectrolyticCore>
			ItemStack tItem_1 = ItemUtils.simpleMetaStack("Mekanism:ElectrolyticCore", 0, 1);
			//<Mekanism:FactoryInstaller:1>
			ItemStack tItem_2 = ItemUtils.simpleMetaStack("Mekanism:FactoryInstaller:1", 1, 1);
			//<Mekanism:SpeedUpgrade>
			ItemStack tItem_3 = ItemUtils.simpleMetaStack("Mekanism:SpeedUpgrade", 0, 1);

			//MiscItems
			String tAdvancedAlloy = "alloyAdvanced";
			String tCircuitAdvanced = "circuitAdvanced";
			ItemStack tMekBatterySimple = ItemUtils.simpleMetaStack("Mekanism:EnergyTablet", 0, 1);

			//Items
			addNewRecipe(
					tAdvancedAlloy, "plateTitanium", tAdvancedAlloy, 
					"dustIron", tAdvancedAlloy, "dustGold", 
					tAdvancedAlloy, "plateTitanium", tAdvancedAlloy, 
					tItem_1);
			addNewRecipe(
					tAdvancedAlloy, "circuitAdvanced", tAdvancedAlloy, 
					"plateStainlessSteel", "plankWood", "plateStainlessSteel", 
					tAdvancedAlloy, "circuitAdvanced", tAdvancedAlloy, 
					tItem_2);
			addNewRecipe(
					null, "blockGlass", null, 
					tAdvancedAlloy, "plateTungsten", tAdvancedAlloy, 
					null, "blockGlass", null, 
					tItem_3);

			//Power Storage
			addNewRecipe(
					tAdvancedAlloy, tMekBatterySimple, tAdvancedAlloy, 
					"plateAluminium", tBasicEnergyCube, "plateAluminium", 
					tAdvancedAlloy, tMekBatterySimple, tAdvancedAlloy, 
					tAdvancedEnergyCube);

			//Blocks
			addNewRecipe(
					"plateSteel", "craftingFurnace", "plateSteel", 
					"plateRedstone", "platePlatinum", "plateRedstone", 
					"plateSteel", "craftingFurnace", "plateSteel", 
					tMachineBlock_8);
			addNewRecipe(
					tAdvancedAlloy, "circuitAdvanced", tAdvancedAlloy, 
					"plateTitanium", ItemUtils.simpleMetaStack("Mekanism:MachineBlock", 0, 1), "plateTitanium", 
					tAdvancedAlloy, "circuitAdvanced", tAdvancedAlloy, 
					tMachineBlock_9);
			addNewRecipe(
					null, ItemUtils.getSimpleStack(Items.bucket), null, 
					tAdvancedAlloy, tSteelCasing, tAdvancedAlloy, 
					"plateStainlessSteel", "plateStainlessSteel", "plateStainlessSteel", 
					tMachineBlock_12);

			//Machines
			addNewRecipe(
					tAdvancedAlloy, tCircuitAdvanced, tAdvancedAlloy, 
					"plateStainlessSteel", tMachineBlock_0_Basic, "plateStainlessSteel", 
					tAdvancedAlloy, tCircuitAdvanced, tAdvancedAlloy, 
					tMachineBlock_0);
			addNewRecipe(
					tAdvancedAlloy, tCircuitAdvanced, tAdvancedAlloy, 
					"plateStainlessSteel", tMachineBlock_1_Basic, "plateStainlessSteel", 
					tAdvancedAlloy, tCircuitAdvanced, tAdvancedAlloy, 
					tMachineBlock_1);
			addNewRecipe(
					tAdvancedAlloy, tCircuitAdvanced, tAdvancedAlloy, 
					"plateStainlessSteel", tMachineBlock_2_Basic, "plateStainlessSteel", 
					tAdvancedAlloy, tCircuitAdvanced, tAdvancedAlloy,  
					tMachineBlock_2);
			addNewRecipe(
					tAdvancedAlloy, tCircuitAdvanced, tAdvancedAlloy, 
					"plateStainlessSteel", tMachineBlock_3_Basic, "plateStainlessSteel", 
					tAdvancedAlloy, tCircuitAdvanced, tAdvancedAlloy,  
					tMachineBlock_3);
			addNewRecipe(
					tAdvancedAlloy, tCircuitAdvanced, tAdvancedAlloy,  
					"plateStainlessSteel", tMachineBlock_4_Basic, "plateStainlessSteel", 
					tAdvancedAlloy, tCircuitAdvanced, tAdvancedAlloy,  
					tMachineBlock_4);
			addNewRecipe(
					tAdvancedAlloy, tCircuitAdvanced, tAdvancedAlloy, 
					"plateStainlessSteel", tMachineBlock_5_Basic, "plateStainlessSteel", 
					tAdvancedAlloy, tCircuitAdvanced, tAdvancedAlloy, 
					tMachineBlock_5);
			addNewRecipe(
					tAdvancedAlloy, tCircuitAdvanced, tAdvancedAlloy,  
					"plateStainlessSteel", tMachineBlock_6_Basic, "plateStainlessSteel", 
					tAdvancedAlloy, tCircuitAdvanced, tAdvancedAlloy, 
					tMachineBlock_6);
			addNewRecipe(
					tAdvancedAlloy, tCircuitAdvanced, tAdvancedAlloy, 
					"plateStainlessSteel", tMachineBlock_7_Basic, "plateStainlessSteel", 
					tAdvancedAlloy, tCircuitAdvanced, tAdvancedAlloy,  
					tMachineBlock_7);	

			//Gas Tanks
			addNewRecipe(
					"plateRedstone", "plateAluminium", "plateRedstone",
					"plateAluminium", null, "plateAluminium",
					"plateRedstone", "plateAluminium", "plateRedstone",
					tBasicGasTank);
			addNewRecipe(
					tAdvancedAlloy, "plateStainlessSteel", tAdvancedAlloy,
					"plateStainlessSteel", tBasicGasTank, "plateStainlessSteel",
					tAdvancedAlloy, "plateStainlessSteel", tAdvancedAlloy,
					tAdvancedGasTank);
			addNewRecipe(
					"alloyElite", "plateTitanium", "alloyElite",
					"plateTitanium", tAdvancedGasTank, "plateTitanium",
					"alloyElite", "plateTitanium", "alloyElite",
					tEliteGasTank);
			addNewRecipe(
					"alloyUltimate", "plateTungsten", "alloyUltimate",
					"plateTungsten", tEliteGasTank, "plateTungsten",
					"alloyUltimate", "plateTungsten", "alloyUltimate",
					tMasterGasTank);			
		}
	}

	private static boolean addNewRecipe(
			final Object InputItem1, final Object InputItem2, final Object InputItem3,
			final Object InputItem4, final Object InputItem5, final Object InputItem6,
			final Object InputItem7, final Object InputItem8, final Object InputItem9,
			final ItemStack OutputItem){

		/*if (removeRecipe(OutputItem)){
			return RecipeUtils.recipeBuilder(
					InputItem1, InputItem2, InputItem3, 
					InputItem4, InputItem5, InputItem6, 
					InputItem7, InputItem8, InputItem9, 
					OutputItem);
		}*/

		removeRecipe(OutputItem);
		return RecipeUtils.recipeBuilder(
				InputItem1, InputItem2, InputItem3, 
				InputItem4, InputItem5, InputItem6, 
				InputItem7, InputItem8, InputItem9, 
				OutputItem);
	}

	private static boolean removeRecipe(ItemStack item){
		Class<?> mekUtils;
		boolean removed = false;
		try {
			mekUtils = Class.forName("mekanism.common.util.RecipeUtils");
			if (mekUtils != null){
				Method mRemoveRecipe = mekUtils.getDeclaredMethod("removeRecipes", ItemStack.class);
				if (mRemoveRecipe != null){
					removed = (boolean) mRemoveRecipe.invoke(null, item);
					if (!removed) {
						removed = (boolean) mRemoveRecipe.invoke(mekUtils, item);
					}
				}
			}		
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			Utils.LOG_INFO("[Mek] Failed to use the built-in recipe remover from Mekanism.");
		}
		if (!removed){
			removed = GT_ModHandler.removeRecipeByOutput(item);
		}
		Utils.LOG_INFO("[Mek] Successfully removed the recipe for "+item.getDisplayName()+".");
		return removed;
	}

}
