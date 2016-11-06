package gtPlusPlus.core.common.compat;

import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.item.general.ItemCloakingDevice;
import gtPlusPlus.core.item.general.ItemHealingDevice;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.util.Utils;

public class COMPAT_Baubles {

	public static void run(){
		if (LoadedMods.Baubles){
			baublesLoaded();
		}
		else {
			baublesNotLoaded();
		}
	}
	
	public static void baublesLoaded(){
		Utils.LOG_INFO("Baubles Found - Loading Wearables.");
		ModItems.itemPersonalCloakingDevice = new ItemCloakingDevice(0);
		//itemPersonalCloakingDeviceCharged = new ItemCloakingDevice(0).set;
		ModItems.itemPersonalHealingDevice = new ItemHealingDevice();
	}
	
	public static void baublesNotLoaded(){
		Utils.LOG_INFO("Baubles Not Found - Skipping Resources.");
	}
	
}
