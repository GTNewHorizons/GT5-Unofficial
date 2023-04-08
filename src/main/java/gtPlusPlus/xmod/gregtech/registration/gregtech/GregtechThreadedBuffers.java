package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gregtech.common.tileentities.automation.GT_MetaTileEntity_SuperBuffer;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.storage.creative.GT_MetaTileEntity_InfiniteItemHolder;

public class GregtechThreadedBuffers {

    public static void run() {
        run2();
    }

    private static void run2() {

        GregtechItemList.Infinite_Item_Chest.set(
                (new GT_MetaTileEntity_InfiniteItemHolder(31010, "infinite.chest.tier.01", "Infinite Item Chest", 1))
                        .getStackForm(1L));

        /*
         * This file used to contain registration and recipes for GT++ threaded super buffers. Those have now been
         * deprecated and replaced by the original GT super buffers. To avoid breaking existing bases, we've assigned
         * the threaded super buffer metadata IDs to a copy of the GT super buffers, with a deprecation notice attached.
         * We've also provided disassembler recipes that are the reverse of the original crafting recipes, allowing
         * players to reclaim their original materials as well as convert the deprecated threaded super buffer metadata
         * ID item back to the original GT super buffer metadata ID item.
         */

        final String deprecationNotice = "**DEPRECATED - Drop in disassembler!** ";
        GregtechItemList.Automation_Threaded_SuperBuffer_ULV.set(
                (new GT_MetaTileEntity_SuperBuffer(
                        31000,
                        "automation.superbuffer.tier.00.deprecated",
                        deprecationNotice + "Ultra Low Voltage Super Buffer",
                        0)).getStackForm(1L));
        GregtechItemList.Automation_Threaded_SuperBuffer_LV.set(
                (new GT_MetaTileEntity_SuperBuffer(
                        31001,
                        "automation.superbuffer.tier.01.deprecated",
                        deprecationNotice + "Low Voltage Super Buffer",
                        1)).getStackForm(1L));
        GregtechItemList.Automation_Threaded_SuperBuffer_MV.set(
                (new GT_MetaTileEntity_SuperBuffer(
                        31002,
                        "automation.superbuffer.tier.02.deprecated",
                        deprecationNotice + "Medium Voltage Super Buffer",
                        2)).getStackForm(1L));
        GregtechItemList.Automation_Threaded_SuperBuffer_HV.set(
                (new GT_MetaTileEntity_SuperBuffer(
                        31003,
                        "automation.superbuffer.tier.03.deprecated",
                        deprecationNotice + "High Voltage Super Buffer",
                        3)).getStackForm(1L));
        GregtechItemList.Automation_Threaded_SuperBuffer_EV.set(
                (new GT_MetaTileEntity_SuperBuffer(
                        31004,
                        "automation.superbuffer.tier.04.deprecated",
                        deprecationNotice + "Extreme Voltage Super Buffer",
                        4)).getStackForm(1L));
        GregtechItemList.Automation_Threaded_SuperBuffer_IV.set(
                (new GT_MetaTileEntity_SuperBuffer(
                        31005,
                        "automation.superbuffer.tier.05.deprecated",
                        deprecationNotice + "Insane Voltage Super Buffer",
                        5)).getStackForm(1L));
        GregtechItemList.Automation_Threaded_SuperBuffer_LuV.set(
                (new GT_MetaTileEntity_SuperBuffer(
                        31006,
                        "automation.superbuffer.tier.06.deprecated",
                        deprecationNotice + "Ludicrous Voltage Super Buffer",
                        6)).getStackForm(1L));
        GregtechItemList.Automation_Threaded_SuperBuffer_ZPM.set(
                (new GT_MetaTileEntity_SuperBuffer(
                        31007,
                        "automation.superbuffer.tier.07.deprecated",
                        deprecationNotice + "ZPM Voltage Super Buffer",
                        7)).getStackForm(1L));
        GregtechItemList.Automation_Threaded_SuperBuffer_UV.set(
                (new GT_MetaTileEntity_SuperBuffer(
                        31008,
                        "automation.superbuffer.tier.08.deprecated",
                        deprecationNotice + "Ultimate Voltage Super Buffer",
                        8)).getStackForm(1L));
        GregtechItemList.Automation_Threaded_SuperBuffer_MAX.set(
                (new GT_MetaTileEntity_SuperBuffer(
                        31009,
                        "automation.superbuffer.tier.09.deprecated",
                        deprecationNotice + "Highly Ultimate Voltage Super Buffer",
                        9)).getStackForm(1L));

    }
}
