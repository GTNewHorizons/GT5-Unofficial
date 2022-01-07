package gtPlusPlus.core.common.compat;

import net.minecraft.item.ItemStack;

import gregtech.api.util.GT_OreDictUnificator;

import gtPlusPlus.core.item.ModItems;

public class COMPAT_RFTools {

	public static void OreDict(){
		run();
	}

	private static final void run(){
		GT_OreDictUnificator.registerOre("plateDimensionShard", new ItemStack(ModItems.itemPlateDimensionShard));

	}

}
