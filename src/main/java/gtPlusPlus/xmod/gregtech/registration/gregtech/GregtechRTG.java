package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.RTG;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.generators.MTERTGenerator;

public class GregtechRTG {

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering RTG.");
        run1();
    }

    private static void run1() {
        GregtechItemList.RTG.set(
            new MTERTGenerator(RTG.ID, "basicgenerator.rtg.tier.01", "Radioisotope Thermoelectric Generator", 3)
                .getStackForm(1L));
    }
}
