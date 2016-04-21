package miscutil.core.common.compat;

import gregtech.api.util.GT_OreDictUnificator;
import miscutil.core.item.ModItems;
import net.minecraft.item.ItemStack;

public class COMPAT_RFTools {

	public static void OreDict(){
		run();
	}
	
	private static final void run(){
		GT_OreDictUnificator.registerOre("plateDimensionShard", new ItemStack(ModItems.itemPlateDimensionShard));
		
	}
	
}
