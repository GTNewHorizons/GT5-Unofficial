package gtPlusPlus.xmod.growthcraft.fishtrap;

import growthcraft.api.fishtrap.FishTrapEntry;
import growthcraft.api.fishtrap.FishTrapRegistry;
import net.minecraft.item.ItemStack;

public class Growthcraft_New {

	public static void addTrapJunk(final ItemStack loot, final int lootChance){
		FishTrapRegistry.instance().addTrapJunk(new FishTrapEntry(loot, lootChance));

	}

	public static void addTrapTreasure(final ItemStack loot, final int lootChance){
		FishTrapRegistry.instance().addTrapTreasure(new FishTrapEntry(loot, lootChance));

	}

	public static void addTrapFish(final ItemStack loot, final int lootChance){
		FishTrapRegistry.instance().addTrapFish(new FishTrapEntry(loot, lootChance));

	}

}
