package miscutil.core.item.init;

import miscutil.core.item.ModItems;
import miscutil.core.item.base.bolts.BaseItemBolt;
import miscutil.core.util.Utils;

public class ItemsBolts {

	public static void load(){
		run();
	}
	
	private static void run(){
		
		ModItems.itemBoltStaballoy = new BaseItemBolt("itemBoltStaballoy", "Staballoy", Utils.rgbtoHexValue(68, 75, 66));
		ModItems.itemBoltBloodSteel = new BaseItemBolt("itemBoltBloodSteel", "Blood Steel", Utils.rgbtoHexValue(142, 28, 0));		
		ModItems.itemBoltTantalloy60 = new BaseItemBolt("itemBoltTantalloy60", "Tantalloy-60", Utils.rgbtoHexValue(68, 75, 166));
		ModItems.itemBoltTantalloy61 = new BaseItemBolt("itemBoltTantalloy61", "Tantalloy-61", Utils.rgbtoHexValue(122, 135, 196));	
		
		
	}
	
}
