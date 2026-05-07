package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.Industrial_Mixer;

import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.MTEIndustrialMixerLegacy;

public class GregtechIndustrialMixer {

    public static void run() {
        run1();
    }

    private static void run1() {
        // Industrial Mixer Multiblock
        GregtechItemList.Industrial_Mixer.set(
            new MTEIndustrialMixerLegacy(
                Industrial_Mixer.ID,
                "industrialmixer.controller.tier.single",
                "Industrial Mixing Machine").getStackForm(1L));
    }
}
