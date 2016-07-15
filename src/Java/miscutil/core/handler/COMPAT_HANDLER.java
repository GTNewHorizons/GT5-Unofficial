package miscutil.core.handler;

import static miscutil.core.lib.LoadedMods.Gregtech;
import gregtech.api.util.GT_OreDictUnificator;

import java.util.LinkedList;
import java.util.Queue;

import miscutil.core.block.ModBlocks;
import miscutil.core.common.compat.COMPAT_BigReactors;
import miscutil.core.common.compat.COMPAT_CompactWindmills;
import miscutil.core.common.compat.COMPAT_EnderIO;
import miscutil.core.common.compat.COMPAT_ExtraUtils;
import miscutil.core.common.compat.COMPAT_IC2;
import miscutil.core.common.compat.COMPAT_MorePlanets;
import miscutil.core.common.compat.COMPAT_PneumaticCraft;
import miscutil.core.common.compat.COMPAT_RFTools;
import miscutil.core.common.compat.COMPAT_SimplyJetpacks;
import miscutil.core.common.compat.COMPAT_Thaumcraft;
import miscutil.core.handler.registration.LateRegistrationHandler;
import miscutil.core.handler.registration.RegistrationHandler;
import miscutil.core.handler.registration.gregtech.GregtechConduits;
import miscutil.core.handler.registration.gregtech.GregtechEnergyBuffer;
import miscutil.core.handler.registration.gregtech.GregtechIndustrialCentrifuge;
import miscutil.core.handler.registration.gregtech.GregtechIndustrialCokeOven;
import miscutil.core.handler.registration.gregtech.GregtechIndustrialElectrolyzer;
import miscutil.core.handler.registration.gregtech.GregtechIndustrialMacerator;
import miscutil.core.handler.registration.gregtech.GregtechIndustrialMassFabricator;
import miscutil.core.handler.registration.gregtech.GregtechIndustrialPlatePress;
import miscutil.core.handler.registration.gregtech.GregtechIndustrialWiremill;
import miscutil.core.handler.registration.gregtech.GregtechIronBlastFurnace;
import miscutil.core.handler.registration.gregtech.GregtechRocketFuelGenerator;
import miscutil.core.handler.registration.gregtech.GregtechSafeBlock;
import miscutil.core.handler.registration.gregtech.GregtechSteamCondenser;
import miscutil.core.item.ModItems;
import miscutil.core.lib.CORE;
import miscutil.core.lib.LoadedMods;
import miscutil.core.recipe.RECIPES_GREGTECH;
import miscutil.core.recipe.RECIPES_LaserEngraver;
import miscutil.core.recipe.ShapedRecipeObject;
import miscutil.core.util.Utils;
import miscutil.core.util.item.UtilsItems;
import miscutil.core.util.recipe.UtilsRecipe;
import miscutil.core.xmod.gregtech.common.items.MetaGeneratedGregtechItems;
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
	
	public static void registerGregtechMachines() {
		if (Gregtech) {
		new MetaGeneratedGregtechItems();
		new RECIPES_LaserEngraver();
		//new MetaGeneratedGregtechTools();
		GregtechEnergyBuffer.run();
		GregtechConduits.run();
		GregtechSteamCondenser.run();
		GregtechSafeBlock.run();
		GregtechIronBlastFurnace.run();
		GregtechIndustrialCentrifuge.run();
		GregtechIndustrialCokeOven.run();
		GregtechIndustrialPlatePress.run();
		GregtechRocketFuelGenerator.run();
		GregtechIndustrialElectrolyzer.run();
		GregtechIndustrialMacerator.run();
		GregtechIndustrialWiremill.run();
		GregtechIndustrialMassFabricator.run();
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
			UtilsRecipe.removeCraftingRecipe(item);
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
