package miscutil.core.item.init;

import miscutil.core.item.ModItems;
import miscutil.core.item.base.screws.BaseItemScrew;
import miscutil.core.util.Utils;

public class ItemsScrews {

	public static void load(){
		run();
	}
	
	private static void run(){
		
		ModItems.itemScrewStaballoy = new BaseItemScrew("itemScrewStaballoy", "Staballoy", Utils.rgbtoHexValue(68, 75, 66));
		ModItems.itemScrewBloodSteel = new BaseItemScrew("itemScrewBloodSteel", "Blood Steel", Utils.rgbtoHexValue(142, 28, 0));		
		ModItems.itemScrewTantalloy60 = new BaseItemScrew("itemScrewTantalloy60", "Tantalloy-60", Utils.rgbtoHexValue(68, 75, 166));
		ModItems.itemScrewTantalloy61 = new BaseItemScrew("itemScrewTantalloy61", "Tantalloy-61", Utils.rgbtoHexValue(122, 135, 196));	
		
		
	}
	
}
