package miscutil.core.common.compat;

import gregtech.api.util.GT_OreDictUnificator;
import miscutil.core.item.ModItems;
import miscutil.core.util.UtilsItems;
import net.minecraft.item.ItemStack;

public class COMPAT_ExtraUtils {

	public static void OreDict(){
		run();
	}
	
	private static final void run(){
		UtilsItems.getItemForOreDict("ExtraUtilities:bedrockiumIngot", "ingotBedrockium", "Bedrockium Ingot", 0);
		GT_OreDictUnificator.registerOre("plateBedrockium", new ItemStack(ModItems.itemPlateBedrockium));
	}
	
}
