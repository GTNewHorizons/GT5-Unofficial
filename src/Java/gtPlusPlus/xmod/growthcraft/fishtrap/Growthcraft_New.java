package gtPlusPlus.xmod.growthcraft.fishtrap;

import growthcraft.api.fishtrap.FishTrapEntry;
import growthcraft.api.fishtrap.FishTrapRegistry;
import net.minecraft.item.ItemStack;

public class Growthcraft_New {

	public static void addTrapJunk(ItemStack loot, int lootChance){
	    FishTrapRegistry.instance().addTrapJunk(new FishTrapEntry(loot, lootChance));

	}

	public static void addTrapTreasure(ItemStack loot, int lootChance){
	    FishTrapRegistry.instance().addTrapTreasure(new FishTrapEntry(loot, lootChance));

	}

	public static void addTrapFish(ItemStack loot, int lootChance){
		FishTrapRegistry.instance().addTrapFish(new FishTrapEntry(loot, lootChance));

	}

}
