package gtPlusPlus.core.common.compat;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.item.general.*;
import gtPlusPlus.core.lib.LoadedMods;

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
		Logger.INFO("Baubles Found - Loading Wearables.");
		ModItems.itemPersonalCloakingDevice = new ItemCloakingDevice(0);
		//itemPersonalCloakingDeviceCharged = new ItemCloakingDevice(0).set;
		ModItems.itemPersonalHealingDevice = new ItemHealingDevice();
		if (LoadedMods.PlayerAPI){
			ModItems.itemSlowBuildingRing = new ItemSlowBuildingRing();
		}
	}

	public static void baublesNotLoaded(){
		Logger.INFO("Baubles Not Found - Skipping Resources.");
	}

}
