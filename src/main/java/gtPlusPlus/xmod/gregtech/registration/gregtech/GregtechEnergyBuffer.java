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

import gregtech.api.metatileentity.implementations.MTETieredMachineBlock;
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
                MTETieredMachineBlock.Args.builder()
                    .registerToApi(true)
                    .id(Energy_Buffer_1by1_ULV.ID)
                    .translateKey("energybuffer.tier.00")
                    .nameEnglish("Ultra Low Voltage Energy Buffer")
                    .tier(0)
                    .inventorySlotCount(1)
                    .build()).getStackForm(1L));
        GregtechItemList.Energy_Buffer_1by1_LV.set(
            new MTEEnergyBuffer(
                MTETieredMachineBlock.Args.builder()
                    .registerToApi(true)
                    .id(Energy_Buffer_1by1_LV.ID)
                    .translateKey("energybuffer.tier.01")
                    .nameEnglish("Low Voltage Energy Buffer")
                    .tier(1)
                    .inventorySlotCount(1)
                    .build()).getStackForm(1L));
        GregtechItemList.Energy_Buffer_1by1_MV.set(
            new MTEEnergyBuffer(
                MTETieredMachineBlock.Args.builder()
                    .registerToApi(true)
                    .id(Energy_Buffer_1by1_MV.ID)
                    .translateKey("energybuffer.tier.02")
                    .nameEnglish("Medium Voltage Energy Buffer")
                    .tier(2)
                    .inventorySlotCount(1)
                    .build()).getStackForm(1L));
        GregtechItemList.Energy_Buffer_1by1_HV.set(
            new MTEEnergyBuffer(
                MTETieredMachineBlock.Args.builder()
                    .registerToApi(true)
                    .id(Energy_Buffer_1by1_HV.ID)
                    .translateKey("energybuffer.tier.03")
                    .nameEnglish("High Voltage Energy Buffer")
                    .tier(3)
                    .inventorySlotCount(1)
                    .build()).getStackForm(1L));
        GregtechItemList.Energy_Buffer_1by1_EV.set(
            new MTEEnergyBuffer(
                MTETieredMachineBlock.Args.builder()
                    .registerToApi(true)
                    .id(Energy_Buffer_1by1_EV.ID)
                    .translateKey("energybuffer.tier.04")
                    .nameEnglish("Extreme Voltage Energy Buffer")
                    .tier(4)
                    .inventorySlotCount(1)
                    .build()).getStackForm(1L));
        GregtechItemList.Energy_Buffer_1by1_IV.set(
            new MTEEnergyBuffer(
                MTETieredMachineBlock.Args.builder()
                    .registerToApi(true)
                    .id(Energy_Buffer_1by1_IV.ID)
                    .translateKey("energybuffer.tier.05")
                    .nameEnglish("Insane Voltage Energy Buffer")
                    .tier(5)
                    .inventorySlotCount(1)
                    .build()).getStackForm(1L));
        GregtechItemList.Energy_Buffer_1by1_LuV.set(
            new MTEEnergyBuffer(
                MTETieredMachineBlock.Args.builder()
                    .registerToApi(true)
                    .id(Energy_Buffer_1by1_LuV.ID)
                    .translateKey("energybuffer.tier.06")
                    .nameEnglish("Ludicrous Voltage Energy Buffer")
                    .tier(6)
                    .inventorySlotCount(1)
                    .build()).getStackForm(1L));
        GregtechItemList.Energy_Buffer_1by1_ZPM.set(
            new MTEEnergyBuffer(
                MTETieredMachineBlock.Args.builder()
                    .registerToApi(true)
                    .id(Energy_Buffer_1by1_ZPM.ID)
                    .translateKey("energybuffer.tier.07")
                    .nameEnglish("ZPM Voltage Energy Buffer")
                    .tier(7)
                    .inventorySlotCount(1)
                    .build()).getStackForm(1L));
        GregtechItemList.Energy_Buffer_1by1_UV.set(
            new MTEEnergyBuffer(
                MTETieredMachineBlock.Args.builder()
                    .registerToApi(true)
                    .id(Energy_Buffer_1by1_UV.ID)
                    .translateKey("energybuffer.tier.08")
                    .nameEnglish("Ultimate Voltage Energy Buffer")
                    .tier(8)
                    .inventorySlotCount(1)
                    .build()).getStackForm(1L));
        GregtechItemList.Energy_Buffer_1by1_MAX.set(
            new MTEEnergyBuffer(
                MTETieredMachineBlock.Args.builder()
                    .registerToApi(true)
                    .id(Energy_Buffer_1by1_MAX.ID)
                    .translateKey("energybuffer.tier.09")
                    .nameEnglish("MAX Voltage Energy Buffer")
                    .tier(9)
                    .inventorySlotCount(1)
                    .build()).getStackForm(1L));
    }
}
