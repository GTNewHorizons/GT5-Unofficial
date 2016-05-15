package miscutil.core.common.compat;

import static miscutil.core.util.UtilsItems.removeCraftingRecipe;

import java.util.LinkedList;
import java.util.Queue;

import miscutil.core.handler.registration.RECIPES_GREGTECH;
import miscutil.core.lib.LoadedMods;

public class COMPAT_HANDLER {
	
	public static Queue<Object> RemoveRecipeQueue = new LinkedList<Object>();
	public static Queue<Object> AddRecipeQueue = new LinkedList<Object>();
	
	//InterMod
	public static void run(){
		
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
	
	public static void ServerStartedEvent(){
		//Removal of Recipes
		for(Object item : RemoveRecipeQueue){
			removeCraftingRecipe(item);
		}
	}
	
	public static void ServerStartedEvent_RECIPES(){
		//Remoal Recipes
		for(Object item : AddRecipeQueue){
			removeCraftingRecipe(item);
		}
	}
	
	public static void loadGregAPIRecipes(){
		RECIPES_GREGTECH.run();
	}
}
