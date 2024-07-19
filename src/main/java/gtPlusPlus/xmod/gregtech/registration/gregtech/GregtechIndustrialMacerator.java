package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.GregtechMetaTileEntity_IndustrialMacerator;

import static gtPlusPlus.xmod.gregtech.registration.gregtech.MetaTileEntityIDs.Industrial_MacerationStack;

public class GregtechIndustrialMacerator {

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Industrial Maceration Stack Multiblock.");
        if (CORE.ConfigSwitches.enableMultiblock_IndustrialMacerationStack) {
            run1();
        }
    }

    private static void run1() {
        // Industrial Maceration Stack Multiblock
        GregtechItemList.Industrial_MacerationStack.set(
            new GregtechMetaTileEntity_IndustrialMacerator(
                Industrial_MacerationStack.ID,
                "industrialmacerator.controller.tier.single",
                "Maceration Stack Controller").getStackForm(1L));
    }
}
