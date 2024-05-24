package gtPlusPlus.core.common.compat;

import gtPlusPlus.xmod.pamsharvest.fishtrap.FishTrapHandler;

public class COMPAT_HarvestCraft {

    public static void OreDict() {
        run();
    }

    private static void run() {
        FishTrapHandler.pamsHarvestCraftCompat();
    }
}
