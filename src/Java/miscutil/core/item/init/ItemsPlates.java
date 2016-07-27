package miscutil.core.item.init;

import miscutil.core.item.ModItems;
import miscutil.core.item.base.plates.BaseItemPlate;
import miscutil.core.util.Utils;

public class ItemsPlates {

	public static void load(){
		run();
	}
	
	private static void run(){
		
		ModItems.itemPlateStaballoy = new BaseItemPlate("itemPlateStaballoy", "Staballoy", Utils.rgbtoHexValue(68, 75, 66));
		ModItems.itemPlateBloodSteel = new BaseItemPlate("itemPlateBloodSteel", "Blood Steel", Utils.rgbtoHexValue(142, 28, 0));		
		ModItems.itemPlateTantalloy60 = new BaseItemPlate("itemPlateTantalloy60", "Tantalloy-60", Utils.rgbtoHexValue(68, 75, 166));
		ModItems.itemPlateTantalloy61 = new BaseItemPlate("itemPlateTantalloy61", "Tantalloy-61", Utils.rgbtoHexValue(122, 135, 196));	
		
		
	}
	
}
