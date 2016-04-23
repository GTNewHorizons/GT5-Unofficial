package miscutil.core.common.compat;

import gregtech.api.util.GT_OreDictUnificator;
import miscutil.core.item.ModItems;
import miscutil.core.util.UtilsItems;
import net.minecraft.item.ItemStack;

public class COMPAT_Thaumcraft {

	public static void OreDict(){
		run();
	}
	
	private static final void run(){
		UtilsItems.getItemForOreDict("Thaumcraft:ItemResource", "ingotVoidMetal", "Void Metal Ingot", 16);
		GT_OreDictUnificator.registerOre("plateVoidMetal", new ItemStack(ModItems.itemPlateVoidMetal));
	}
	
}
