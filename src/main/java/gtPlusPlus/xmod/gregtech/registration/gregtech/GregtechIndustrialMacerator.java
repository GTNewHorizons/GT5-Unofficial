package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.Industrial_MacerationStack;

import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.MTEIndustrialMacerator;

public class GregtechIndustrialMacerator {

    public static void run() {
        GregtechItemList.Industrial_MacerationStack.set(
            new MTEIndustrialMacerator(
                Industrial_MacerationStack.ID,
                "industrialmacerator.controller.tier.single",
                "Industrial Maceration Stack").getStackForm(1L));
    }
}
