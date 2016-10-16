package gtPlusPlus.core.handler;

import static gtPlusPlus.core.lib.LoadedMods.Gregtech;
import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.core.common.compat.COMPAT_BigReactors;
import gtPlusPlus.core.common.compat.COMPAT_CompactWindmills;
import gtPlusPlus.core.common.compat.COMPAT_EnderIO;
import gtPlusPlus.core.common.compat.COMPAT_ExtraUtils;
import gtPlusPlus.core.common.compat.COMPAT_IC2;
import gtPlusPlus.core.common.compat.COMPAT_MorePlanets;
import gtPlusPlus.core.common.compat.COMPAT_PneumaticCraft;
import gtPlusPlus.core.common.compat.COMPAT_RFTools;
import gtPlusPlus.core.common.compat.COMPAT_SimplyJetpacks;
import gtPlusPlus.core.common.compat.COMPAT_Thaumcraft;
import gtPlusPlus.core.handler.Recipes.LateRegistrationHandler;
import gtPlusPlus.core.handler.Recipes.RegistrationHandler;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.recipe.RECIPES_GREGTECH;
import gtPlusPlus.core.recipe.RECIPES_LaserEngraver;
import gtPlusPlus.core.recipe.ShapedRecipeObject;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.UtilsItems;
import gtPlusPlus.core.util.recipe.UtilsRecipe;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechDehydrator;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechEnergyBuffer;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechGeothermalThermalGenerator;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechIndustrialCentrifuge;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechIndustrialCokeOven;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechIndustrialElectrolyzer;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechIndustrialMacerator;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechIndustrialMassFabricator;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechIndustrialMultiTank;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechIndustrialPlatePress;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechIndustrialWiremill;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechIronBlastFurnace;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechPowerSubStation;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechRocketFuelGenerator;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechSafeBlock;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechSolarGenerators;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechSteamCondenser;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechSuperConductionPoint;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechTieredFluidTanks;
import gtPlusPlus.xmod.gregtech.registration.gregtech.Gregtech4Content;

import java.util.LinkedList;
import java.util.Queue;

import net.minecraft.item.ItemStack;

public class COMPAT_HANDLER {
	
	public static Queue<Object> RemoveRecipeQueue = new LinkedList<Object>();
	public static Queue<ShapedRecipeObject> AddRecipeQueue = new LinkedList<ShapedRecipeObject>();
	public static Boolean areInitItemsLoaded = false;
	
	
	public static void registerMyModsOreDictEntries(){

		Utils.LOG_INFO("Registering Materials with OreDict.");
		//In-house

		//tools
		GT_OreDictUnificator.registerOre("craftingToolSandHammer", new ItemStack(ModItems.itemSandstoneHammer));

		for(int i=1; i<=10; i++){
			GT_OreDictUnificator.registerOre("bufferCore_"+CORE.VOLTAGES[i-1], new ItemStack(UtilsItems.getItem("miscutils:item.itemBufferCore"+i)));
		}
	}
	
	public static void registerGregtechMachines() {
		if (Gregtech) {
		new RECIPES_LaserEngraver();
		GregtechEnergyBuffer.run();
		GregtechSteamCondenser.run();
		GregtechSafeBlock.run();
		GregtechSuperConductionPoint.run();
		GregtechIronBlastFurnace.run();
		GregtechIndustrialCentrifuge.run();
		GregtechIndustrialCokeOven.run();
		GregtechIndustrialPlatePress.run();
		GregtechRocketFuelGenerator.run();
		GregtechIndustrialElectrolyzer.run();
		GregtechIndustrialMacerator.run();
		GregtechIndustrialWiremill.run();
		GregtechIndustrialMassFabricator.run();
		//GregtechIndustrialSinter.run();
		GregtechSolarGenerators.run();
		GregtechPowerSubStation.run();
		GregtechDehydrator.run();
		GregtechTieredFluidTanks.run();
		GregtechIndustrialMultiTank.run();
		Gregtech4Content.run();
		GregtechGeothermalThermalGenerator.run();
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
