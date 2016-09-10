package gtPlusPlus.xmod.growthcraft.fishtrap;

import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_ModHandler;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.fluid.FluidUtils;
import gtPlusPlus.core.util.item.UtilsItems;
import net.minecraft.item.ItemStack;

public class FishTrapHandler {
	
	private static final String[] fishTypes = {"fish", "junk", "treasure"};

	protected static void addFish(String lootType, ItemStack lootStack, int lootChance){
		if (LoadedMods.getModVersion("Growthcraft").contains("2.3.1")){
			if (lootType == fishTypes[0]){
				Growthcraft_Old.addTrapFish(lootStack, lootChance);
				Utils.LOG_INFO("Added "+lootStack.getDisplayName()+" as an extra Fish for Growthcraft Fishtraps.");
			}
			else if (lootType == fishTypes[1]){
				Growthcraft_Old.addTrapJunk(lootStack, lootChance);
				Utils.LOG_INFO("Added "+lootStack.getDisplayName()+" as extra Junk for Growthcraft Fishtraps.");
			}
			else if (lootType == fishTypes[2]){
				Growthcraft_Old.addTrapTreasure(lootStack, lootChance);
				Utils.LOG_INFO("Added "+lootStack.getDisplayName()+" as extra Treasure for Growthcraft Fishtraps.");
			}
			else {
				return;
			}
		}
		
		else {
			Utils.LOG_INFO("Extra Fish loot for Growthcraft Fishtraps disabled. Found V."+LoadedMods.getModVersion("Growthcraft"));
		}
		
	}
	
	final static String prefix = "food";
	final static String suffix = "raw";
	final static String seaweed = "cropSeaweed";
	final static String greenheartFish = "Greenheartfish";
	private static final String[] harvestcraftFish = {
		"Anchovy", "Bass", "Carp", "Catfish", "Charr", "Clam", "Crab", "Crayfish", "Eel", "Frog", "Grouper", "Herring",
		"Jellyfish", "Mudfish", "Octopus", "Perch", "Scallop", "Shrimp", "Snail", "Snapper", "Tilapia", "Trout", "Tuna", "Turtle", "Walleye"};
	public static void pamsHarvestCraftCompat(){
		for (int i = 0; i < harvestcraftFish.length; i++){
			String itemName = prefix+harvestcraftFish[i]+suffix;
			int lootChance = getLootChance(harvestcraftFish[i]);
			if (UtilsItems.getItemStackOfAmountFromOreDictNoBroken(itemName, 1) != null){
				addFish("fish", UtilsItems.getItemStackOfAmountFromOreDict(itemName, 1), lootChance);		
				addGregtechMaceratorRecipe(UtilsItems.getItemStackOfAmountFromOreDict(itemName, 1));
				addGregtechCentrifugeRecipe(UtilsItems.getItemStackOfAmountFromOreDict(itemName, 1));
				addGregtechFluidRecipe(UtilsItems.getItemStackOfAmountFromOreDict(itemName, 1));
			}
		}
		if (UtilsItems.getItemStackOfAmountFromOreDictNoBroken(greenheartFish, 1) != null){
			addFish("fish", UtilsItems.getItemStackOfAmountFromOreDict(greenheartFish, 1), getLootChance(greenheartFish));	
			addGregtechMaceratorRecipe(UtilsItems.getItemStackOfAmountFromOreDict(greenheartFish, 1));
			addGregtechCentrifugeRecipe(UtilsItems.getItemStackOfAmountFromOreDict(greenheartFish, 1));
			addGregtechFluidRecipe(UtilsItems.getItemStackOfAmountFromOreDict(greenheartFish, 1));
		}
		if (UtilsItems.getItemStackOfAmountFromOreDictNoBroken(seaweed, 1) != null){
			addFish("junk", UtilsItems.getItemStackOfAmountFromOreDict(seaweed, 1), getLootChance(seaweed));				
		}
	}
	
	private static int getLootChance(String name){
		if (name == harvestcraftFish[0]){
			return 20;
		}
		else if (name == harvestcraftFish[1]){
			return 10;
		}
		else if (name == harvestcraftFish[2]){
			return 15;
		}
		else if (name == harvestcraftFish[3]){
			return 55;
		}
		else if (name == harvestcraftFish[4]){
			return 5;
		}
		else if (name == harvestcraftFish[5]){
			return 8;
		}
		else if (name == harvestcraftFish[6]){
			return 11;
		}
		else if (name == harvestcraftFish[7]){
			return 11;
		}
		else if (name == harvestcraftFish[8]){
			return 15;
		}
		else if (name == harvestcraftFish[9]){
			return 1;
		}
		else if (name == harvestcraftFish[10]){
			return 5;
		}
		else if (name == harvestcraftFish[11]){
			return 10;
		}
		else if (name == harvestcraftFish[12]){
			return 25;
		}
		else if (name == harvestcraftFish[13]){
			return 15;
		}
		else if (name == harvestcraftFish[14]){
			return 20;
		} //"Perch", "Scallop", "Shrimp", "Snail", "Snapper", "Tilapia", "Trout", "Tuna", "Turtle", "Walleye"};
		else if (name == harvestcraftFish[15]){
			return 22;
		}
		else if (name == harvestcraftFish[16]){
			return 10;
		}
		else if (name == harvestcraftFish[17]){
			return 35;
		}
		else if (name == harvestcraftFish[18]){
			return 3;
		}
		else if (name == harvestcraftFish[19]){
			return 20;
		}
		else if (name == harvestcraftFish[20]){
			return 4;
		}
		else if (name == harvestcraftFish[21]){
			return 40;
		}
		else if (name == harvestcraftFish[22]){
			return 30;
		}
		else if (name == harvestcraftFish[23]){
			return 5;
		}
		else if (name == harvestcraftFish[24]){
			return 8;
		}
		else {
			return 25;
		}
	}
	
	private static void addGregtechCentrifugeRecipe(ItemStack input){
		if (LoadedMods.Gregtech){
		GT_Values.RA.addCentrifugeRecipe(input, null, null, FluidUtils.getFluidStack("methane", 96), null, null, null, null, null, null, null, 19*20, 5);
		}		
	}
	
	private static void addGregtechMaceratorRecipe(ItemStack input){
		if (LoadedMods.Gregtech){
			GT_ModHandler.addPulverisationRecipe(input, UtilsItems.getItemStackOfAmountFromOreDict("dustMeatRaw", 1), UtilsItems.getItemStackOfAmountFromOreDict("dustTinyBone", 1), 0);
		}
	}
	
	private static void addGregtechFluidRecipe(ItemStack input){
		if (LoadedMods.Gregtech){
		GT_Values.RA.addFluidExtractionRecipe(input, null, FluidUtils.getFluidStack("fishoil", 4), 0, (64/4), 4); //4eu/t  total eu used = 64 so time = 64/4
		}		
	}
	
}
