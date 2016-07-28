package miscutil.core.item.init;

import miscutil.core.item.ModItems;
import miscutil.core.item.base.rings.BaseItemRing;
import miscutil.core.util.Utils;

public class ItemsRings {

	public static void load(){
		run();
	}
	
	private static void run(){
		
		ModItems.itemRingStaballoy = new BaseItemRing("itemRingStaballoy", "Staballoy", Utils.rgbtoHexValue(68, 75, 66));
		ModItems.itemRingBloodSteel = new BaseItemRing("itemRingBloodSteel", "Blood Steel", Utils.rgbtoHexValue(142, 28, 0));		
		ModItems.itemRingTantalloy60 = new BaseItemRing("itemRingTantalloy60", "Tantalloy-60", Utils.rgbtoHexValue(68, 75, 166));
		ModItems.itemRingTantalloy61 = new BaseItemRing("itemRingTantalloy61", "Tantalloy-61", Utils.rgbtoHexValue(122, 135, 196));	
		
		
	}
	
}
