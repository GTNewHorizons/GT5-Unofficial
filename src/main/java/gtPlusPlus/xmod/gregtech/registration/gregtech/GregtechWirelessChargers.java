package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.Charger_EV;
import static gregtech.api.enums.MetaTileEntityIDs.Charger_HV;
import static gregtech.api.enums.MetaTileEntityIDs.Charger_IV;
import static gregtech.api.enums.MetaTileEntityIDs.Charger_LV;
import static gregtech.api.enums.MetaTileEntityIDs.Charger_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.Charger_MV;
import static gregtech.api.enums.MetaTileEntityIDs.Charger_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.Charger_UV;
import static gregtech.api.enums.MetaTileEntityIDs.Charger_ZPM;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.helpers.ChargingHelper;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic.MTEWirelessCharger;

public class GregtechWirelessChargers {

    public static void run() {

        Logger.INFO("Gregtech5u Content | Registering Wireless Chargers.");

        Utils.registerEvent(new ChargingHelper());

        GregtechItemList.Charger_LV.set(
            new MTEWirelessCharger(
                Charger_LV.ID,
                "wificharger.01.tier.single",
                "Wireless Charger MK I",
                1,
                "Hopefully won't give you cancer.",
                0).getStackForm(1L));
        GregtechItemList.Charger_MV.set(
            new MTEWirelessCharger(
                Charger_MV.ID,
                "wificharger.02.tier.single",
                "Wireless Charger MK II",
                2,
                "Hopefully won't give you cancer.",
                0).getStackForm(1L));
        GregtechItemList.Charger_HV.set(
            new MTEWirelessCharger(
                Charger_HV.ID,
                "wificharger.03.tier.single",
                "Wireless Charger MK III",
                3,
                "Hopefully won't give you cancer.",
                0).getStackForm(1L));
        GregtechItemList.Charger_EV.set(
            new MTEWirelessCharger(
                Charger_EV.ID,
                "wificharger.04.tier.single",
                "Wireless Charger MK IV",
                4,
                "Hopefully won't give you cancer.",
                0).getStackForm(1L));
        GregtechItemList.Charger_IV.set(
            new MTEWirelessCharger(
                Charger_IV.ID,
                "wificharger.05.tier.single",
                "Wireless Charger MK V",
                5,
                "Hopefully won't give you cancer.",
                0).getStackForm(1L));
        GregtechItemList.Charger_LuV.set(
            new MTEWirelessCharger(
                Charger_LuV.ID,
                "wificharger.06.tier.single",
                "Wireless Charger MK VI",
                6,
                "Hopefully won't give you cancer.",
                0).getStackForm(1L));
        GregtechItemList.Charger_ZPM.set(
            new MTEWirelessCharger(
                Charger_ZPM.ID,
                "wificharger.07.tier.single",
                "Wireless Charger MK VII",
                7,
                "Hopefully won't give you cancer.",
                0).getStackForm(1L));
        GregtechItemList.Charger_UV.set(
            new MTEWirelessCharger(
                Charger_UV.ID,
                "wificharger.08.tier.single",
                "Wireless Charger MK VIII",
                8,
                "Hopefully won't give you cancer.",
                0).getStackForm(1L));
        GregtechItemList.Charger_UHV.set(
            new MTEWirelessCharger(
                Charger_UHV.ID,
                "wificharger.09.tier.single",
                "Wireless Charger MK IX",
                9,
                "Hopefully won't give you cancer.",
                0).getStackForm(1L));
    }
}
