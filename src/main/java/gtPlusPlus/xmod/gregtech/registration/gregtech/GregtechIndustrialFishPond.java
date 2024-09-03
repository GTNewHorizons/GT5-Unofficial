package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.Industrial_FishingPond;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.MTEIndustrialFishingPond;

public class GregtechIndustrialFishPond {

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Industrial Fishing Pond Multiblock.");
        run1();
    }

    private static void run1() {
        GregtechItemList.Industrial_FishingPond.set(
            new MTEIndustrialFishingPond(
                Industrial_FishingPond.ID,
                "industrial.fishpond.controller.tier.single",
                "Zhuhai - Fishing Port").getStackForm(1L));
    }
}
