package gtPlusPlus.core.handler;

import static gtPlusPlus.core.lib.LoadedMods.Gregtech;

import java.util.*;

import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.api.interfaces.RunnableWithInfo;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.objects.minecraft.ItemPackage;
import gtPlusPlus.core.common.compat.*;
import gtPlusPlus.core.handler.Recipes.LateRegistrationHandler;
import gtPlusPlus.core.handler.Recipes.RegistrationHandler;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialGenerator;
import gtPlusPlus.core.recipe.*;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.RecipeUtils;
import gtPlusPlus.xmod.gregtech.HANDLER_GT;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGen_Recycling;
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
			GT_OreDictUnificator.registerOre("bufferCore_"+GT_Values.VN[i-1], new ItemStack(ItemUtils.getItemFromFQRN("miscutils:item.itemBufferCore"+i)));
		}
	}

	public static void registerGregtechMachines() {
		if (Gregtech) {			

			//Free IDs
			/*
			---
			859
			to
			868
			---
			911
			to
			940
			 */

			new RECIPES_LaserEngraver();
			new RECIPES_Extruder();
			GregtechGeneratorsULV.run();
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
			GregtechSuperChests.run();
			GregtechIndustrialFishPond.run();
			GregtechTieredChunkloaders.run();
			GregtechIndustrialExtruder.run();
			GregtechIndustrialMultiMachine.run();
			GregtechBedrockPlatforms.run();
			GregtechBufferDynamos.run();
			GregtechAmazonWarehouse.run();
			GregtechIndustrialCryogenicFreezer.run();
			GregtechThaumcraftDevices.run();
			GregtechThreadedBuffers.run();
			GregtechIndustrialMixer.run();
			GregtechCustomHatches.run();
			GregtechNaqReactor.run();
			GregtechIndustrialArcFurnace.run();
			GregtechSolarTower.run();
			GregtechLargeTurbinesAndHeatExchanger.run();
			GregtechPowerBreakers.run();

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
		if (LoadedMods.OpenBlocks){
			COMPAT_OpenBlocks.OreDict();
		}
		if (LoadedMods.Railcraft){
			COMPAT_Railcraft.OreDict();
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
		if (LoadedMods.PamsHarvestcraft){
			COMPAT_HarvestCraft.OreDict();
		}
		if (LoadedMods.Witchery) {
			COMPAT_Witchery.OreDict();
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
		//Add hand-made recipes
		RECIPES_GREGTECH.run();		
		//Add autogenerated Recipes from Item Components
		for (Set<RunnableWithInfo<Material>> m : MaterialGenerator.mRecipeMapsToGenerate) {
			for (RunnableWithInfo<Material> r : m) {
				try {
					r.run();
				}
				catch (Throwable t) {
					t.printStackTrace();
					Logger.INFO("[ERROR] "+r.getInfoData().getLocalizedName()+" recipes failed to generated.");
				}
			}
		}
		RecipeGen_Recycling.executeGenerators();	
		runQueuedMisc();
	}
	


	public static void onLoadComplete(FMLLoadCompleteEvent event) {
		runQueuedOnLoadComplete(event);
	}

	public static final AutoMap<RunnableWithInfo<String>> mRecipesToGenerate = new AutoMap<RunnableWithInfo<String>>();
	public static final AutoMap<RunnableWithInfo<String>> mGtRecipesToGenerate = new AutoMap<RunnableWithInfo<String>>();	

	public static final AutoMap<RunnableWithInfo<String>> mObjectsToRunInPostInit = new AutoMap<RunnableWithInfo<String>>();
	public static final AutoMap<ItemPackage> mObjectsToRunInOnLoadComplete = new AutoMap<ItemPackage>();

	public static void runQueuedRecipes() {
		//Add autogenerated Recipes from Item Components
		for (RunnableWithInfo<String> m : mRecipesToGenerate) {
			try {
				m.run();
			}
			catch (Throwable t) {
				t.printStackTrace();
				Logger.INFO("[ERROR] "+m.getInfoData()+" recipe failed to generated.");
			}

		}
		for (RunnableWithInfo<String> m : mGtRecipesToGenerate) {
			try {
				m.run();
			}
			catch (Throwable t) {
				t.printStackTrace();
				Logger.INFO("[ERROR] "+m.getInfoData()+" recipe failed to generated.");
			}

		}
	}
	
	public static void runQueuedMisc() {
		for (RunnableWithInfo<String> m : mObjectsToRunInPostInit) {
			try {
				m.run();
			}
			catch (Throwable t) {
				t.printStackTrace();
				Logger.INFO("[ERROR] "+m.getInfoData());
			}

		}
	}
	
	/**
	 * Generally used to register GT recipe map changes after they've been populated.
	 */
	public static void runQueuedOnLoadComplete(FMLLoadCompleteEvent event) {
		for (ItemPackage m : mObjectsToRunInOnLoadComplete) {
			try {
				m.onLoadComplete(event);
			}
			catch (Throwable t) {
				t.printStackTrace();
				Logger.INFO("[ERROR] "+m.getInfoData());
			}

		}
	}
}
