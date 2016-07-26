package miscutil.core.item.init;

import static miscutil.core.creative.AddToCreativeTab.tabMisc;
import gregtech.api.util.GT_OreDictUnificator;
import miscutil.core.item.ModItems;
import miscutil.core.item.base.dusts.BaseItemDust;
import miscutil.core.lib.CORE;
import miscutil.core.util.Utils;
import miscutil.core.util.item.UtilsItems;

public class ItemsDusts {

	public static void load(){
		run();
	}

	private static void run(){

		//Staballoy Dusts
		ModItems.itemDustStaballoy = new BaseItemDust("itemDustStaballoy", "Staballoy", Utils.rgbtoHexValue(68, 75, 66), "Dust").setCreativeTab(tabMisc);
		GT_OreDictUnificator.registerOre("dustStaballoy", UtilsItems.getItemStack(CORE.MODID+":itemDustStaballoy", 1));
		ModItems.itemDustTinyStaballoy = new BaseItemDust("itemDustTinyStaballoy", "Staballoy", Utils.rgbtoHexValue(68, 75, 66), "Tiny").setCreativeTab(tabMisc);
		GT_OreDictUnificator.registerOre("dustTinyStaballoy", UtilsItems.getItemStack(CORE.MODID+":itemDustTinyStaballoy", 1));
		ModItems.itemDustSmallStaballoy = new BaseItemDust("itemDustSmallStaballoy", "Staballoy", Utils.rgbtoHexValue(68, 75, 66), "Small").setCreativeTab(tabMisc);
		GT_OreDictUnificator.registerOre("dustSmallStaballoy", UtilsItems.getItemStack(CORE.MODID+":itemDustSmallStaballoy", 1));

		//BloodSteel Dusts
		ModItems.itemDustBloodSteel = new BaseItemDust("itemDustBloodSteel", "BloodSteel", Utils.rgbtoHexValue(142, 28, 0), "Dust").setCreativeTab(tabMisc);
		GT_OreDictUnificator.registerOre("dustBloodSteel", UtilsItems.getItemStack(CORE.MODID+":itemDustBloodSteel", 1));
		ModItems.itemDustTinyBloodSteel = new BaseItemDust("itemDustTinyBloodSteel", "BloodSteel", Utils.rgbtoHexValue(142, 28, 0), "Tiny").setCreativeTab(tabMisc);
		GT_OreDictUnificator.registerOre("dustTinyBloodSteel", UtilsItems.getItemStack(CORE.MODID+":itemDustTinyBloodSteel", 1));
		ModItems.itemDustSmallBloodSteel = new BaseItemDust("itemDustSmallBloodSteel", "BloodSteel", Utils.rgbtoHexValue(142, 28, 0), "Small").setCreativeTab(tabMisc);
		GT_OreDictUnificator.registerOre("dustSmallBloodSteel", UtilsItems.getItemStack(CORE.MODID+":itemDustSmallBloodSteel", 1));

	}

}
