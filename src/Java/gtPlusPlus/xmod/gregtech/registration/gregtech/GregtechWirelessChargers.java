package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gregtech.api.util.GT_ModHandler;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.helpers.ChargingHelper;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic.GregtechMetaWirelessCharger;

public class GregtechWirelessChargers {


	public static void run(){

		if (gtPlusPlus.core.lib.LoadedMods.Gregtech) {
			Logger.INFO("Gregtech5u Content | Registering Wireless Chargers.");


			long bitsd = GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
					| GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED;
			int mID = 890;		

			Utils.registerEvent(new ChargingHelper());

			GregtechItemList.Charger_LV.set(new GregtechMetaWirelessCharger(mID++, "wificharger.01.tier.single",
					"Wireless Charger MK I", 1, "Hopefully won't give you cancer.", 0).getStackForm(1L));
			GregtechItemList.Charger_MV.set(new GregtechMetaWirelessCharger(mID++, "wificharger.02.tier.single",
					"Wireless Charger MK II", 2, "Hopefully won't give you cancer.", 0).getStackForm(1L));
			GregtechItemList.Charger_HV.set(new GregtechMetaWirelessCharger(mID++, "wificharger.03.tier.single",
					"Wireless Charger MK III", 3, "Hopefully won't give you cancer.", 0).getStackForm(1L));
			GregtechItemList.Charger_EV.set(new GregtechMetaWirelessCharger(mID++, "wificharger.04.tier.single",
					"Wireless Charger MK IV", 4, "Hopefully won't give you cancer.", 0).getStackForm(1L));
			GregtechItemList.Charger_IV.set(new GregtechMetaWirelessCharger(mID++, "wificharger.05.tier.single",
					"Wireless Charger MK V", 5, "Hopefully won't give you cancer.", 0).getStackForm(1L));
			GregtechItemList.Charger_LuV.set(new GregtechMetaWirelessCharger(mID++, "wificharger.06.tier.single",
					"Wireless Charger MK VI", 6, "Hopefully won't give you cancer.", 0).getStackForm(1L));
			GregtechItemList.Charger_ZPM.set(new GregtechMetaWirelessCharger(mID++, "wificharger.07.tier.single",
					"Wireless Charger MK VII", 7, "Hopefully won't give you cancer.", 0).getStackForm(1L));
			GregtechItemList.Charger_UV.set(new GregtechMetaWirelessCharger(mID++, "wificharger.08.tier.single",
					"Wireless Charger MK VIII", 8, "Hopefully won't give you cancer.", 0).getStackForm(1L));
			GregtechItemList.Charger_MAX.set(new GregtechMetaWirelessCharger(mID++, "wificharger.09.tier.single",
					"Wireless Charger MK IX", 9, "Hopefully won't give you cancer.", 0).getStackForm(1L));

		}
	}

}
