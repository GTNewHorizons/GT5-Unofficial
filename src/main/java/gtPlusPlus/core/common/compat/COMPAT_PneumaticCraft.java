package gtPlusPlus.core.common.compat;

import net.minecraft.item.ItemStack;

import gregtech.api.util.GT_OreDictUnificator;

import gtPlusPlus.core.item.ModItems;

public class COMPAT_PneumaticCraft {

	public static void OreDict(){
		run();
	}

	private static final void run(){
		GT_OreDictUnificator.registerOre("plateCompressedIron", new ItemStack(ModItems.itemPlateCompressedIron));
	}

}
