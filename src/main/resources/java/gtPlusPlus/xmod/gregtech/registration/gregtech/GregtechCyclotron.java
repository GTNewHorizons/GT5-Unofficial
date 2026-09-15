package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.COMET_Cyclotron;

import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.MTECyclotron;

public class GregtechCyclotron {

    public static void run() {
        run1();
    }

    private static void run1() {
        GregtechItemList.COMET_Cyclotron.set(
            new MTECyclotron(COMET_Cyclotron.ID, "cyclotron.tier.single", "COMET - Compact Cyclotron")
                .getStackForm(1L));
    }
}
