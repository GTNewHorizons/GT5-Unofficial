package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.GregtechMetaTileEntity_IndustrialFishingPond;

import static gtPlusPlus.xmod.gregtech.registration.gregtech.MetaTileEntityIDs.Industrial_FishingPond;

public class GregtechIndustrialFishPond {

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Industrial Fishing Pond Multiblock.");
        run1();
    }

    private static void run1() {
        GregtechItemList.Industrial_FishingPond.set(
            new GregtechMetaTileEntity_IndustrialFishingPond(
                Industrial_FishingPond.ID,
                "industrial.fishpond.controller.tier.single",
                "Zhuhai - Fishing Port").getStackForm(1L));
    }
}
