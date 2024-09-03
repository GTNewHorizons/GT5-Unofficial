package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.Industrial_Mixer;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.MTEIndustrialMixer;

public class GregtechIndustrialMixer {

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Industrial Mixer Multiblock.");
        if (GTPPCore.ConfigSwitches.enableMultiblock_IndustrialPlatePress) {
            run1();
        }
    }

    private static void run1() {
        // Industrial Mixer Multiblock
        GregtechItemList.Industrial_Mixer.set(
            new MTEIndustrialMixer(
                Industrial_Mixer.ID,
                "industrialmixer.controller.tier.single",
                "Industrial Mixing Machine").getStackForm(1L));
    }
}
