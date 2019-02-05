package gtPlusPlus.core.common.compat;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.item.bauble.BatteryPackBaseBauble;
import gtPlusPlus.core.item.general.ItemCloakingDevice;
import gtPlusPlus.core.item.general.ItemHealingDevice;
import gtPlusPlus.core.item.general.ItemSlowBuildingRing;
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

		try {
			ModItems.itemChargePack1 = new BatteryPackBaseBauble(6);
			ModItems.itemChargePack2 = new BatteryPackBaseBauble(7);
			ModItems.itemChargePack3 = new BatteryPackBaseBauble(8);
			ModItems.itemChargePack4 = new BatteryPackBaseBauble(9);		
		}
		catch (Throwable t) {
			t.printStackTrace();
		}
		
		if (LoadedMods.PlayerAPI){
			ModItems.itemSlowBuildingRing = new ItemSlowBuildingRing();
		}
	}

	public static void baublesNotLoaded(){
		Logger.INFO("Baubles Not Found - Skipping Resources.");
	}

}
