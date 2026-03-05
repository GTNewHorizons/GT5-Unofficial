package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.Energy_Buffer_1by1_EV;
import static gregtech.api.enums.MetaTileEntityIDs.Energy_Buffer_1by1_HV;
import static gregtech.api.enums.MetaTileEntityIDs.Energy_Buffer_1by1_IV;
import static gregtech.api.enums.MetaTileEntityIDs.Energy_Buffer_1by1_LV;
import static gregtech.api.enums.MetaTileEntityIDs.Energy_Buffer_1by1_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.Energy_Buffer_1by1_MAX;
import static gregtech.api.enums.MetaTileEntityIDs.Energy_Buffer_1by1_MV;
import static gregtech.api.enums.MetaTileEntityIDs.Energy_Buffer_1by1_ULV;
import static gregtech.api.enums.MetaTileEntityIDs.Energy_Buffer_1by1_UV;
import static gregtech.api.enums.MetaTileEntityIDs.Energy_Buffer_1by1_ZPM;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.storage.MTEEnergyBuffer;

public class GregtechEnergyBuffer {

    // Misc Items

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Energy Buffer Blocks.");
        run1();
    }

    private static void run1() {

        // Energy Buffers
        GregtechItemList.Energy_Buffer_1by1_ULV.set(
            new MTEEnergyBuffer(
                Energy_Buffer_1by1_ULV.ID,
                "energybuffer.tier.00",
                "Ultra Low Voltage Energy Buffer",
                0,
                "",
                1).getStackForm(1L));
        GregtechItemList.Energy_Buffer_1by1_LV.set(
            new MTEEnergyBuffer(Energy_Buffer_1by1_LV.ID, "energybuffer.tier.01", "Low Voltage Energy Buffer", 1, "", 1)
                .getStackForm(1L));
        GregtechItemList.Energy_Buffer_1by1_MV.set(
            new MTEEnergyBuffer(
                Energy_Buffer_1by1_MV.ID,
                "energybuffer.tier.02",
                "Medium Voltage Energy Buffer",
                2,
                "",
                1).getStackForm(1L));
        GregtechItemList.Energy_Buffer_1by1_HV.set(
            new MTEEnergyBuffer(
                Energy_Buffer_1by1_HV.ID,
                "energybuffer.tier.03",
                "High Voltage Energy Buffer",
                3,
                "",
                1).getStackForm(1L));
        GregtechItemList.Energy_Buffer_1by1_EV.set(
            new MTEEnergyBuffer(
                Energy_Buffer_1by1_EV.ID,
                "energybuffer.tier.04",
                "Extreme Voltage Energy Buffer",
                4,
                "",
                1).getStackForm(1L));
        GregtechItemList.Energy_Buffer_1by1_IV.set(
            new MTEEnergyBuffer(
                Energy_Buffer_1by1_IV.ID,
                "energybuffer.tier.05",
                "Insane Voltage Energy Buffer",
                5,
                "",
                1).getStackForm(1L));
        GregtechItemList.Energy_Buffer_1by1_LuV.set(
            new MTEEnergyBuffer(
                Energy_Buffer_1by1_LuV.ID,
                "energybuffer.tier.06",
                "Ludicrous Voltage Energy Buffer",
                6,
                "",
                1).getStackForm(1L));
        GregtechItemList.Energy_Buffer_1by1_ZPM.set(
            new MTEEnergyBuffer(
                Energy_Buffer_1by1_ZPM.ID,
                "energybuffer.tier.07",
                "ZPM Voltage Energy Buffer",
                7,
                "",
                1).getStackForm(1L));
        GregtechItemList.Energy_Buffer_1by1_UV.set(
            new MTEEnergyBuffer(
                Energy_Buffer_1by1_UV.ID,
                "energybuffer.tier.08",
                "Ultimate Voltage Energy Buffer",
                8,
                "",
                1).getStackForm(1L));
        GregtechItemList.Energy_Buffer_1by1_MAX.set(
            new MTEEnergyBuffer(
                Energy_Buffer_1by1_MAX.ID,
                "energybuffer.tier.09",
                "Highly Ultimate Voltage Energy Buffer",
                9,
                "",
                1).getStackForm(1L));
    }
}
