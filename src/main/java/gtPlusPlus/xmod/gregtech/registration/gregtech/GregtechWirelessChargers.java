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

import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic.MTEWirelessCharger;

public class GregtechWirelessChargers {

    public static void run() {

        GregtechItemList.Charger_LV
            .set(new MTEWirelessCharger(Charger_LV.ID, "wificharger.01.tier.single", "", 1, 0).getStackForm(1L));
        GregtechItemList.Charger_MV
            .set(new MTEWirelessCharger(Charger_MV.ID, "wificharger.02.tier.single", "", 2, 0).getStackForm(1L));
        GregtechItemList.Charger_HV
            .set(new MTEWirelessCharger(Charger_HV.ID, "wificharger.03.tier.single", "", 3, 0).getStackForm(1L));
        GregtechItemList.Charger_EV
            .set(new MTEWirelessCharger(Charger_EV.ID, "wificharger.04.tier.single", "", 4, 0).getStackForm(1L));
        GregtechItemList.Charger_IV
            .set(new MTEWirelessCharger(Charger_IV.ID, "wificharger.05.tier.single", "", 5, 0).getStackForm(1L));
        GregtechItemList.Charger_LuV
            .set(new MTEWirelessCharger(Charger_LuV.ID, "wificharger.06.tier.single", "", 6, 0).getStackForm(1L));
        GregtechItemList.Charger_ZPM
            .set(new MTEWirelessCharger(Charger_ZPM.ID, "wificharger.07.tier.single", "", 7, 0).getStackForm(1L));
        GregtechItemList.Charger_UV
            .set(new MTEWirelessCharger(Charger_UV.ID, "wificharger.08.tier.single", "", 8, 0).getStackForm(1L));
        GregtechItemList.Charger_UHV
            .set(new MTEWirelessCharger(Charger_UHV.ID, "wificharger.09.tier.single", "", 9, 0).getStackForm(1L));
    }
}
