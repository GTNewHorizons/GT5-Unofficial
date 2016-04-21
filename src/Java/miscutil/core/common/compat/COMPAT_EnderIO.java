package miscutil.core.common.compat;

import gregtech.api.util.GT_OreDictUnificator;
import miscutil.core.item.ModItems;
import miscutil.core.util.ItemUtils;
import net.minecraft.item.ItemStack;

public class COMPAT_EnderIO {

	public static void OreDict(){
		run();
	}
	
	private static final void run(){
		ItemUtils.getItemForOreDict("EnderIO:itemAlloy", "ingotVibrantAlloy", "Vibrant Alloy Ingot", 2);
		GT_OreDictUnificator.registerOre("plateConductiveIron", new ItemStack(ModItems.itemPlateConductiveIron));
		GT_OreDictUnificator.registerOre("plateDarkSteel", new ItemStack(ModItems.itemPlateDarkSteel));
		GT_OreDictUnificator.registerOre("plateElectricalSteel", new ItemStack(ModItems.itemPlateElectricalSteel));
		GT_OreDictUnificator.registerOre("plateEnergeticAlloy", new ItemStack(ModItems.itemPlateEnergeticAlloy));
		GT_OreDictUnificator.registerOre("platePulsatingIron", new ItemStack(ModItems.itemPlatePulsatingIron));
		GT_OreDictUnificator.registerOre("plateRedstoneAlloy", new ItemStack(ModItems.itemPlateRedstoneAlloy));
		GT_OreDictUnificator.registerOre("plateSoularium", new ItemStack(ModItems.itemPlateSoularium));
		GT_OreDictUnificator.registerOre("plateVibrantAlloy", new ItemStack(ModItems.itemPlateVibrantAlloy));
	}
	
}
