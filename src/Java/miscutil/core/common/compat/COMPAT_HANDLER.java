package miscutil.core.common.compat;

import static miscutil.core.util.UtilsItems.removeCraftingRecipe;
import gregtech.api.util.GT_OreDictUnificator;

import java.util.LinkedList;
import java.util.Queue;

import miscutil.core.block.ModBlocks;
import miscutil.core.handler.registration.LateRegistrationHandler;
import miscutil.core.handler.registration.RegistrationHandler;
import miscutil.core.item.ModItems;
import miscutil.core.lib.CORE;
import miscutil.core.lib.LoadedMods;
import miscutil.core.util.Utils;
import miscutil.core.util.UtilsItems;
import miscutil.core.util.recipe.RECIPES_GREGTECH;
import miscutil.core.util.recipe.ShapedRecipeObject;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class COMPAT_HANDLER {
	
	public static Queue<Object> RemoveRecipeQueue = new LinkedList<Object>();
	public static Queue<ShapedRecipeObject> AddRecipeQueue = new LinkedList<ShapedRecipeObject>();
	public static Boolean areInitItemsLoaded = false;
	
	
	public static void registerMyModsOreDictEntries(){

		Utils.LOG_INFO("Registering Materials with OreDict.");
		//In-house

		//tools
		GT_OreDictUnificator.registerOre("craftingToolSandHammer", new ItemStack(ModItems.itemSandstoneHammer));
		GT_OreDictUnificator.registerOre("ingotBloodSteel", new ItemStack(ModItems.itemIngotBloodSteel));
		GT_OreDictUnificator.registerOre("ingotStaballoy", new ItemStack(ModItems.itemIngotStaballoy));

		//Plates
		GT_OreDictUnificator.registerOre("plateBloodSteel", new ItemStack(ModItems.itemPlateBloodSteel));
		GT_OreDictUnificator.registerOre("plateStaballoy", new ItemStack(ModItems.itemPlateStaballoy));

		//Blocks
		GT_OreDictUnificator.registerOre("blockStaballoy", new ItemStack(Item.getItemFromBlock(ModBlocks.blockStaballoy)));
		OreDictionary.registerOre("blockBloodSteel", new ItemStack(ModBlocks.blockBloodSteel));


		for(int i=1; i<=10; i++){
			GT_OreDictUnificator.registerOre("bufferCore_"+CORE.VOLTAGES[i-1], new ItemStack(UtilsItems.getItem("miscutils:item.itemBufferCore"+i)));
		}
	}
	
	
	//InterMod
	public static void intermodOreDictionarySupport(){
		
		if (LoadedMods.Big_Reactors){
			COMPAT_BigReactors.OreDict();
		}
		if (LoadedMods.EnderIO){
			COMPAT_EnderIO.OreDict();
		}
		if (LoadedMods.MorePlanets){
			COMPAT_MorePlanets.OreDict();
		}
		if (LoadedMods.Simply_Jetpacks){
			COMPAT_SimplyJetpacks.OreDict();
		}
		if (LoadedMods.RFTools){
			COMPAT_RFTools.OreDict();
		}
		if (LoadedMods.Thaumcraft){
			COMPAT_Thaumcraft.OreDict();
		}
		if (LoadedMods.Extra_Utils){
			COMPAT_ExtraUtils.OreDict();
		}
		if (LoadedMods.PneumaticCraft){
			COMPAT_PneumaticCraft.OreDict();
		}
		if (LoadedMods.CompactWindmills){
			COMPAT_CompactWindmills.OreDict();
		}
		if (LoadedMods.IndustrialCraft2){
			COMPAT_IC2.OreDict();
		}
	}
	
	public static void RemoveRecipesFromOtherMods(){
		//Removal of Recipes
		for(Object item : RemoveRecipeQueue){
			removeCraftingRecipe(item);
		}		
	}
	
	public static void InitialiseHandlerThenAddRecipes(){
		RegistrationHandler.run();
	}
	public static void InitialiseLateHandlerThenAddRecipes(){
		LateRegistrationHandler.run();
	}
	
	public static void startLoadingGregAPIBasedRecipes(){
		RECIPES_GREGTECH.run();
	}
}
