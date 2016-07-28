package miscutil.core.item.init;

import miscutil.core.item.ModItems;
import miscutil.core.item.base.rotors.BaseItemRotor;
import miscutil.core.util.Utils;

public class ItemsRotors {

	public static void load(){
		run();
	}
	
	private static void run(){
		
		ModItems.itemRotorStaballoy = new BaseItemRotor("itemRotorStaballoy", "Staballoy", Utils.rgbtoHexValue(68, 75, 66));
		ModItems.itemRotorBloodSteel = new BaseItemRotor("itemRotorBloodSteel", "Blood Steel", Utils.rgbtoHexValue(142, 28, 0));		
		ModItems.itemRotorTantalloy60 = new BaseItemRotor("itemRotorTantalloy60", "Tantalloy-60", Utils.rgbtoHexValue(68, 75, 166));
		ModItems.itemRotorTantalloy61 = new BaseItemRotor("itemRotorTantalloy61", "Tantalloy-61", Utils.rgbtoHexValue(122, 135, 196));	
		
		
	}
	
}
