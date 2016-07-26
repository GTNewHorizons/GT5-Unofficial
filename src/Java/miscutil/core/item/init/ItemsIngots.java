package miscutil.core.item.init;

import miscutil.core.item.ModItems;
import miscutil.core.item.base.ingots.BaseItemIngot;
import miscutil.core.item.base.ingots.BaseItemIngotHot;
import miscutil.core.lib.CORE;
import miscutil.core.util.Utils;
import miscutil.core.util.item.UtilsItems;

public class ItemsIngots {

	public static void load(){
		run();
	}

	private static void run(){

		//Standard
		ModItems.itemIngotStaballoy = new BaseItemIngot("itemIngotStaballoy", "Staballoy", Utils.rgbtoHexValue(68, 75, 66));
		ModItems.itemIngotTantalloy60 = new BaseItemIngot("itemIngotTantalloy60", "Tantalloy-60", Utils.rgbtoHexValue(68, 75, 166));
		ModItems.itemIngotTantalloy61 = new BaseItemIngot("itemIngotTantalloy61", "Tantalloy-61", Utils.rgbtoHexValue(122, 135, 196));
		ModItems.itemIngotBloodSteel = new BaseItemIngot("itemIngotBloodSteel", "Blood Steel", Utils.rgbtoHexValue(142, 28, 0));
		ModItems.itemIngotBatteryAlloy = new BaseItemIngot("itemIngotBatteryAlloy", "Battery Alloy", Utils.rgbtoHexValue(35, 228, 141));


		//Hot Ingots
		ModItems.itemHotIngotStaballoy = new BaseItemIngotHot("itemHotIngotStaballoy", "Staballoy", UtilsItems.getItemStack(CORE.MODID+":itemIngotStaballoy", 1));
		ModItems.itemHotIngotTantalloy60 = new BaseItemIngotHot("itemHotIngotTantalloy60", "Tantalloy-60", UtilsItems.getItemStack(CORE.MODID+":itemIngotTantalloy60", 1));
		ModItems.itemHotIngotTantalloy61 = new BaseItemIngotHot("itemHotIngotTantalloy61", "Tantalloy-61", UtilsItems.getItemStack(CORE.MODID+":itemIngotTantalloy61", 1));
		
	}

}
