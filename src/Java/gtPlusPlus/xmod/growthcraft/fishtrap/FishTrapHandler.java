package gtPlusPlus.xmod.growthcraft.fishtrap;

import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_ModHandler;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.fluid.FluidUtils;
import gtPlusPlus.core.util.item.ItemUtils;
import net.minecraft.item.ItemStack;

public class FishTrapHandler {

	private static final String[] fishTypes = {
			"fish", "junk", "treasure"
	};

	final static String				prefix				= "food";

	final static String				suffix				= "raw";
	final static String				seaweed				= "cropSeaweed";
	final static String				greenheartFish		= "Greenheartfish";
	private static final String[]	harvestcraftFish	= {
			"Anchovy", "Bass", "Carp", "Catfish", "Charr", "Clam", "Crab", "Crayfish", "Eel", "Frog", "Grouper",
			"Herring", "Jellyfish", "Mudfish", "Octopus", "Perch", "Scallop", "Shrimp", "Snail", "Snapper", "Tilapia",
			"Trout", "Tuna", "Turtle", "Walleye"
	};
	protected static void addFish(final String lootType, final ItemStack lootStack, final int lootChance) {
		if (LoadedMods.getModVersion("Growthcraft").contains("2.3.1")) {
			if (lootType == FishTrapHandler.fishTypes[0]) {
				Growthcraft_Old.addTrapFish(lootStack, lootChance);
				Utils.LOG_INFO("Added " + lootStack.getDisplayName() + " as an extra Fish for Growthcraft Fishtraps.");
			}
			else if (lootType == FishTrapHandler.fishTypes[1]) {
				Growthcraft_Old.addTrapJunk(lootStack, lootChance);
				Utils.LOG_INFO("Added " + lootStack.getDisplayName() + " as extra Junk for Growthcraft Fishtraps.");
			}
			else if (lootType == FishTrapHandler.fishTypes[2]) {
				Growthcraft_Old.addTrapTreasure(lootStack, lootChance);
				Utils.LOG_INFO("Added " + lootStack.getDisplayName() + " as extra Treasure for Growthcraft Fishtraps.");
			}
			else {
				return;
			}
		}

		else {
			Utils.LOG_INFO("Extra Fish loot for Growthcraft Fishtraps disabled. Found V."
					+ LoadedMods.getModVersion("Growthcraft"));
		}

	}

	private static void addGregtechCentrifugeRecipe(final ItemStack input) {
		if (LoadedMods.Gregtech) {
			GT_Values.RA.addCentrifugeRecipe(input, null, null, FluidUtils.getFluidStack("methane", 96), null, null,
					null, null, null, null, null, 19 * 20, 5);
		}
	}

	private static void addGregtechFluidRecipe(final ItemStack input) {
		if (LoadedMods.Gregtech) {
			GT_Values.RA.addFluidExtractionRecipe(input, null, FluidUtils.getFluidStack("fishoil", 4), 0, 64 / 4, 4); // 4eu/t
																														// total
																														// eu
																														// used
																														// =
																														// 64
																														// so
																														// time
																														// =
																														// 64/4
		}
	}

	private static void addGregtechMaceratorRecipe(final ItemStack input) {
		if (LoadedMods.Gregtech) {
			GT_ModHandler.addPulverisationRecipe(input, ItemUtils.getItemStackOfAmountFromOreDict("dustMeatRaw", 1),
					ItemUtils.getItemStackOfAmountFromOreDict("dustTinyBone", 1), 0);
		}
	}

	private static int getLootChance(final String name) {
		if (name == FishTrapHandler.harvestcraftFish[0]) {
			return 20;
		}
		else if (name == FishTrapHandler.harvestcraftFish[1]) {
			return 10;
		}
		else if (name == FishTrapHandler.harvestcraftFish[2]) {
			return 15;
		}
		else if (name == FishTrapHandler.harvestcraftFish[3]) {
			return 55;
		}
		else if (name == FishTrapHandler.harvestcraftFish[4]) {
			return 5;
		}
		else if (name == FishTrapHandler.harvestcraftFish[5]) {
			return 8;
		}
		else if (name == FishTrapHandler.harvestcraftFish[6]) {
			return 11;
		}
		else if (name == FishTrapHandler.harvestcraftFish[7]) {
			return 11;
		}
		else if (name == FishTrapHandler.harvestcraftFish[8]) {
			return 15;
		}
		else if (name == FishTrapHandler.harvestcraftFish[9]) {
			return 1;
		}
		else if (name == FishTrapHandler.harvestcraftFish[10]) {
			return 5;
		}
		else if (name == FishTrapHandler.harvestcraftFish[11]) {
			return 10;
		}
		else if (name == FishTrapHandler.harvestcraftFish[12]) {
			return 25;
		}
		else if (name == FishTrapHandler.harvestcraftFish[13]) {
			return 15;
		}
		else if (name == FishTrapHandler.harvestcraftFish[14]) {
			return 20;
		} // "Perch", "Scallop", "Shrimp", "Snail", "Snapper", "Tilapia",
			// "Trout", "Tuna", "Turtle", "Walleye"};
		else if (name == FishTrapHandler.harvestcraftFish[15]) {
			return 22;
		}
		else if (name == FishTrapHandler.harvestcraftFish[16]) {
			return 10;
		}
		else if (name == FishTrapHandler.harvestcraftFish[17]) {
			return 35;
		}
		else if (name == FishTrapHandler.harvestcraftFish[18]) {
			return 3;
		}
		else if (name == FishTrapHandler.harvestcraftFish[19]) {
			return 20;
		}
		else if (name == FishTrapHandler.harvestcraftFish[20]) {
			return 4;
		}
		else if (name == FishTrapHandler.harvestcraftFish[21]) {
			return 40;
		}
		else if (name == FishTrapHandler.harvestcraftFish[22]) {
			return 30;
		}
		else if (name == FishTrapHandler.harvestcraftFish[23]) {
			return 5;
		}
		else if (name == FishTrapHandler.harvestcraftFish[24]) {
			return 8;
		}
		else {
			return 25;
		}
	}

	public static void pamsHarvestCraftCompat() {
		for (int i = 0; i < FishTrapHandler.harvestcraftFish.length; i++) {
			final String itemName = FishTrapHandler.prefix + FishTrapHandler.harvestcraftFish[i]
					+ FishTrapHandler.suffix;
			final int lootChance = FishTrapHandler.getLootChance(FishTrapHandler.harvestcraftFish[i]);
			if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken(itemName, 1) != null) {
				FishTrapHandler.addFish("fish", ItemUtils.getItemStackOfAmountFromOreDict(itemName, 1), lootChance);
				FishTrapHandler.addGregtechMaceratorRecipe(ItemUtils.getItemStackOfAmountFromOreDict(itemName, 1));
				FishTrapHandler.addGregtechCentrifugeRecipe(ItemUtils.getItemStackOfAmountFromOreDict(itemName, 1));
				FishTrapHandler.addGregtechFluidRecipe(ItemUtils.getItemStackOfAmountFromOreDict(itemName, 1));
			}
		}
		if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken(FishTrapHandler.greenheartFish, 1) != null) {
			FishTrapHandler.addFish("fish",
					ItemUtils.getItemStackOfAmountFromOreDict(FishTrapHandler.greenheartFish, 1),
					FishTrapHandler.getLootChance(FishTrapHandler.greenheartFish));
			FishTrapHandler.addGregtechMaceratorRecipe(
					ItemUtils.getItemStackOfAmountFromOreDict(FishTrapHandler.greenheartFish, 1));
			FishTrapHandler.addGregtechCentrifugeRecipe(
					ItemUtils.getItemStackOfAmountFromOreDict(FishTrapHandler.greenheartFish, 1));
			FishTrapHandler.addGregtechFluidRecipe(
					ItemUtils.getItemStackOfAmountFromOreDict(FishTrapHandler.greenheartFish, 1));
		}
		if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken(FishTrapHandler.seaweed, 1) != null) {
			FishTrapHandler.addFish("junk", ItemUtils.getItemStackOfAmountFromOreDict(FishTrapHandler.seaweed, 1),
					FishTrapHandler.getLootChance(FishTrapHandler.seaweed));
		}
	}

}
