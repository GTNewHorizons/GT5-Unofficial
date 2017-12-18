package gtPlusPlus.xmod.growthcraft.fishtrap;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_ModHandler;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.fluid.FluidUtils;
import gtPlusPlus.core.util.item.ItemUtils;
import net.minecraft.item.ItemStack;

public class FishTrapHandler {

	private static final String[] fishTypes = {"fish", "junk", "treasure"};
	private static Object mFishingRegistry;
	private static Growthcraft_Old mHandler;
	
	public static Object getFishingRegistry(){
		if (mFishingRegistry != null){
			return mFishingRegistry;
		}
		else {
			return setFishTrapRegistry();
		}
	}

	private final static Object setFishTrapRegistry(){
		Class mFishingRegistryClass;
		try {
			mFishingRegistryClass = Class.forName("growthcraft.api.fishtrap.FishTrapRegistry");
			final Method mFishingRegistryMethod = mFishingRegistryClass.getDeclaredMethod("getInstance", null);
			mFishingRegistry = mFishingRegistryMethod.invoke(null);
			if (mFishingRegistry != null){
				return mFishingRegistry;
			}
		}
		catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
		}
		return null;
	}

	protected static void addFish(final String lootType, final ItemStack lootStack, final int lootChance){

		if (mFishingRegistry == null){
			setFishTrapRegistry();
		}
		if (mHandler == null){
			mHandler = new Growthcraft_Old();
		}
		
		final String GCVersion = LoadedMods.getModVersion("Growthcraft");
		final String[] versionString = GCVersion.split("//.");

		if (LoadedMods.getModVersion("Growthcraft").contains("2.3.1") || versionString[1].equals("3")){
			if (lootType.equals(fishTypes[0])){
				mHandler.addTrapFish(lootStack, lootChance);
				Utils.LOG_INFO("Added "+lootStack.getDisplayName()+" as an extra Fish for Growthcraft Fishtraps.");
			}
			else if (lootType.equals(fishTypes[1])){
				mHandler.addTrapJunk(lootStack, lootChance);
				Utils.LOG_INFO("Added "+lootStack.getDisplayName()+" as extra Junk for Growthcraft Fishtraps.");
			}
			else if (lootType.equals(fishTypes[2])){
				mHandler.addTrapTreasure(lootStack, lootChance);
				Utils.LOG_INFO("Added "+lootStack.getDisplayName()+" as extra Treasure for Growthcraft Fishtraps.");
			}
			else {
				return;
			}
		}/*
		else if (LoadedMods.getModVersion("Growthcraft").contains("2.7.2")){
			if (lootType.equals(fishTypes[0])){
				Growthcraft_New.addTrapFish(lootStack, lootChance);
				Utils.LOG_INFO("Added "+lootStack.getDisplayName()+" as an extra Fish for Growthcraft Fishtraps.");
			}
			else if (lootType.equals(fishTypes[1])){
				Growthcraft_New.addTrapJunk(lootStack, lootChance);
				Utils.LOG_INFO("Added "+lootStack.getDisplayName()+" as extra Junk for Growthcraft Fishtraps.");
			}
			else if (lootType.equals(fishTypes[2])){
				Growthcraft_New.addTrapTreasure(lootStack, lootChance);
				Utils.LOG_INFO("Added "+lootStack.getDisplayName()+" as extra Treasure for Growthcraft Fishtraps.");
			}
			else {
				return;
			}
		}*/

		else {
			Utils.LOG_INFO("Extra Fish loot for Growthcraft Fishtraps disabled. Found V."+LoadedMods.getModVersion("Growthcraft"));
		}

	}

	final static String prefix = "food";
	final static String suffix = "raw";
	final static String seaweed = "cropSeaweed";
	final static String greenheartFish = "foodGreenheartfish";
	private static final String[] harvestcraftFish = {
			"Anchovy", "Bass", "Carp", "Catfish", "Charr", "Clam", "Crab", "Crayfish", "Eel", "Frog", "Grouper", "Herring",
			"Jellyfish", "Mudfish", "Octopus", "Perch", "Scallop", "Shrimp", "Snail", "Snapper", "Tilapia", "Trout", "Tuna", "Turtle", "Walleye"};
	public static void pamsHarvestCraftCompat(){
		for (int i = 0; i < harvestcraftFish.length; i++){
			final String itemName = prefix+harvestcraftFish[i]+suffix;
			final int lootChance = getLootChance(harvestcraftFish[i]);
			if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken(itemName, 1) != null){
				addFish("fish", ItemUtils.getItemStackOfAmountFromOreDict(itemName, 1), lootChance);
				addGregtechMaceratorRecipe(ItemUtils.getItemStackOfAmountFromOreDict(itemName, 1));
				addGregtechCentrifugeRecipe(ItemUtils.getItemStackOfAmountFromOreDict(itemName, 1));
				addGregtechFluidRecipe(ItemUtils.getItemStackOfAmountFromOreDict(itemName, 1));
			}
		}
		if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken(greenheartFish, 1) != null){
			addFish("fish", ItemUtils.getItemStackOfAmountFromOreDict(greenheartFish, 1), getLootChance(greenheartFish));
			addGregtechMaceratorRecipe(ItemUtils.getItemStackOfAmountFromOreDict(greenheartFish, 1));
			addGregtechCentrifugeRecipe(ItemUtils.getItemStackOfAmountFromOreDict(greenheartFish, 1));
			addGregtechFluidRecipe(ItemUtils.getItemStackOfAmountFromOreDict(greenheartFish, 1));
		}
		if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken(seaweed, 1) != null){
			addFish("junk", ItemUtils.getItemStackOfAmountFromOreDict(seaweed, 1), getLootChance(seaweed));
		}
	}

	private static int getLootChance(final String name){
		if (name.equals(harvestcraftFish[0])){
			return 20;
		}
		else if (name.equals(harvestcraftFish[1])){
			return 10;
		}
		else if (name.equals(harvestcraftFish[2])){
			return 15;
		}
		else if (name.equals(harvestcraftFish[3])){
			return 55;
		}
		else if (name.equals(harvestcraftFish[4])){
			return 5;
		}
		else if (name.equals(harvestcraftFish[5])){
			return 8;
		}
		else if (name.equals(harvestcraftFish[6])){
			return 11;
		}
		else if (name.equals(harvestcraftFish[7])){
			return 11;
		}
		else if (name.equals(harvestcraftFish[8])){
			return 15;
		}
		else if (name.equals(harvestcraftFish[9])){
			return 1;
		}
		else if (name.equals(harvestcraftFish[10])){
			return 5;
		}
		else if (name.equals(harvestcraftFish[11])){
			return 10;
		}
		else if (name.equals(harvestcraftFish[12])){
			return 25;
		}
		else if (name.equals(harvestcraftFish[13])){
			return 15;
		}
		else if (name.equals(harvestcraftFish[14])){
			return 20;
		} //"Perch", "Scallop", "Shrimp", "Snail", "Snapper", "Tilapia", "Trout", "Tuna", "Turtle", "Walleye"};
		else if (name.equals(harvestcraftFish[15])){
			return 22;
		}
		else if (name.equals(harvestcraftFish[16])){
			return 10;
		}
		else if (name.equals(harvestcraftFish[17])){
			return 35;
		}
		else if (name.equals(harvestcraftFish[18])){
			return 3;
		}
		else if (name.equals(harvestcraftFish[19])){
			return 20;
		}
		else if (name.equals(harvestcraftFish[20])){
			return 4;
		}
		else if (name.equals(harvestcraftFish[21])){
			return 40;
		}
		else if (name.equals(harvestcraftFish[22])){
			return 30;
		}
		else if (name.equals(harvestcraftFish[23])){
			return 5;
		}
		else if (name.equals(harvestcraftFish[24])){
			return 8;
		}
		else {
			return 25;
		}
	}

	private static void addGregtechCentrifugeRecipe(final ItemStack input){
		if (LoadedMods.Gregtech){
			GT_Values.RA.addCentrifugeRecipe(input, null, null, FluidUtils.getFluidStack("methane", 96), null, null, null, null, null, null, null, 19*20, 5);
		}
	}

	private static void addGregtechMaceratorRecipe(final ItemStack input){
		if (LoadedMods.Gregtech){
			GT_ModHandler.addPulverisationRecipe(input, ItemUtils.getItemStackOfAmountFromOreDict("dustMeatRaw", 1), ItemUtils.getItemStackOfAmountFromOreDict("dustTinyBone", 1), 0);
		}
	}

	private static void addGregtechFluidRecipe(final ItemStack input){
		if (LoadedMods.Gregtech){
			GT_Values.RA.addFluidExtractionRecipe(input, null, FluidUtils.getFluidStack("fishoil", 4), 0, (64/4), 4); //4eu/t  total eu used = 64 so time = 64/4
		}
	}

}
