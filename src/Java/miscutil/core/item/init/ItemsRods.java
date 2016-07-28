package miscutil.core.item.init;

import miscutil.core.item.ModItems;
import miscutil.core.item.base.rods.BaseItemRod;
import miscutil.core.util.Utils;

public class ItemsRods {

	public static void load(){
		run();
	}
	
	private static void run(){
		
		ModItems.itemRodStaballoy = new BaseItemRod("itemRodStaballoy", "Staballoy", Utils.rgbtoHexValue(68, 75, 66));
		ModItems.itemRodBloodSteel = new BaseItemRod("itemRodBloodSteel", "Blood Steel", Utils.rgbtoHexValue(142, 28, 0));		
		ModItems.itemRodTantalloy60 = new BaseItemRod("itemRodTantalloy60", "Tantalloy-60", Utils.rgbtoHexValue(68, 75, 166));
		ModItems.itemRodTantalloy61 = new BaseItemRod("itemRodTantalloy61", "Tantalloy-61", Utils.rgbtoHexValue(122, 135, 196));	
		
		
	}
	
}
