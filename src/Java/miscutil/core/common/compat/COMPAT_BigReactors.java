package miscutil.core.common.compat;

import gregtech.api.util.GT_OreDictUnificator;
import miscutil.core.item.ModItems;
import net.minecraft.item.ItemStack;

public class COMPAT_BigReactors {

	public static void OreDict(){
		run();
	}
	
	private static final void run(){
		GT_OreDictUnificator.registerOre("plateBlutonium", new ItemStack(ModItems.itemPlateBlutonium));
		GT_OreDictUnificator.registerOre("plateCyanite", new ItemStack(ModItems.itemPlateCyanite));
		GT_OreDictUnificator.registerOre("plateLudicrite", new ItemStack(ModItems.itemPlateLudicrite));
	}
	
}
