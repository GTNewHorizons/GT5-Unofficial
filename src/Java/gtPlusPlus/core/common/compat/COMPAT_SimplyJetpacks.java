package gtPlusPlus.core.common.compat;

import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.core.item.ModItems;
import net.minecraft.item.ItemStack;

public class COMPAT_SimplyJetpacks {

	public static void OreDict() {
		COMPAT_SimplyJetpacks.run();
	}

	private static final void run() {
		GT_OreDictUnificator.registerOre("plateEnrichedSoularium", new ItemStack(ModItems.itemPlateEnrichedSoularium));

	}

}
