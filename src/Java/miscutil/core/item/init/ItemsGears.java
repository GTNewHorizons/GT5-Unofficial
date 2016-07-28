package miscutil.core.item.init;

import miscutil.core.item.ModItems;
import miscutil.core.item.base.gears.BaseItemGear;
import miscutil.core.util.Utils;

public class ItemsGears {

	public static void load(){
		run();
	}
	
	private static void run(){
		
		ModItems.itemGearStaballoy = new BaseItemGear("itemGearStaballoy", "Staballoy", Utils.rgbtoHexValue(68, 75, 66));
		ModItems.itemGearBloodSteel = new BaseItemGear("itemGearBloodSteel", "Blood Steel", Utils.rgbtoHexValue(142, 28, 0));		
		ModItems.itemGearTantalloy60 = new BaseItemGear("itemGearTantalloy60", "Tantalloy-60", Utils.rgbtoHexValue(68, 75, 166));
		ModItems.itemGearTantalloy61 = new BaseItemGear("itemGearTantalloy61", "Tantalloy-61", Utils.rgbtoHexValue(122, 135, 196));	
		
		
	}
	
}
