package gtPlusPlus.core.handler;

import static gtPlusPlus.core.lib.LoadedMods.Gregtech;

import java.util.LinkedList;
import java.util.Queue;

import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.common.compat.*;
import gtPlusPlus.core.handler.Recipes.LateRegistrationHandler;
import gtPlusPlus.core.handler.Recipes.RegistrationHandler;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.recipe.*;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.core.util.recipe.RecipeUtils;
import gtPlusPlus.xmod.gregtech.registration.gregtech.*;
import net.minecraft.item.ItemStack;

public class COMPAT_HANDLER {

	public static Queue<Object> RemoveRecipeQueue = new LinkedList<>();
	public static Queue<ShapedRecipeObject> AddRecipeQueue = new LinkedList<>();
	public static Boolean areInitItemsLoaded = false;


	public static void registerMyModsOreDictEntries(){

		Logger.INFO("Registering Materials with OreDict.");
		//In-house

		//tools
		GT_OreDictUnificator.registerOre("craftingToolSandHammer", new ItemStack(ModItems.itemSandstoneHammer));

		for(int i=1; i<=10; i++){
			GT_OreDictUnificator.registerOre("bufferCore_"+GT_Values.VN[i-1], new ItemStack(ItemUtils.getItem("miscutils:item.itemBufferCore"+i)));
		}
	}

	public static void registerGregtechMachines() {
		if (Gregtech) {			

			//Free IDs
			/*
			829
			---
			859
			to
			868
			---
			899
			to
			945
			*/

			new RECIPES_LaserEngraver();
			GregtechEnergyBuffer.run();
			GregtechLFTR.run();
			GregtechSteamCondenser.run();
			GregtechSafeBlock.run();
			//GregtechSuperConductionPoint.run();
			GregtechIronBlastFurnace.run();
			GregtechIndustrialCentrifuge.run();
			GregtechIndustrialCokeOven.run();
			GregtechIndustrialPlatePress.run();
			GregtechRocketFuelGenerator.run();
			GregtechIndustrialElectrolyzer.run();
			GregtechIndustrialMacerator.run();
			GregtechIndustrialWiremill.run();
			GregtechIndustrialMassFabricator.run();
			GregtechIndustrialBlastSmelter.run();
			GregtechSolarGenerators.run();
			GregtechPowerSubStation.run();
			GregtechDehydrator.run();
			GregtechAdvancedBoilers.run();
			GregtechPollutionDevices.run();
			GregtechTieredFluidTanks.run();
			GregtechIndustrialMultiTank.run();
			GregtechGeothermalThermalGenerator.run();
			Gregtech4Content.run();
			GregtechIndustrialFuelRefinery.run();
			GregtechTreeFarmerTE.run();
			GregtechIndustrialTreeFarm.run();
			GregtechIndustrialSifter.run();
			GregtechSimpleWasher.run();
			GregtechRTG.run();
			GregtechCyclotron.run();
			GregtechHiAmpTransformer.run();
			GregtechIndustrialThermalCentrifuge.run();
			GregtechIndustrialWashPlant.run();
			GregtechSemiFluidgenerators.run();
			GregtechAdvancedMixer.run();
			GregtechWirelessChargers.run();
			GregtechIndustrialGeneratorArray.run();
			GregtechIndustrialCuttingFactory.run();
			GregtechMiniRaFusion.run();
			GregtechComponentAssembler.run();
			GregtechTeslaTower.run();
			GregtechSuperTanks.run();
			
			//New Horizons Content
			NewHorizonsAccelerator.run();
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
		for(final Object item : RemoveRecipeQueue){
			RecipeUtils.removeCraftingRecipe(item);
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
