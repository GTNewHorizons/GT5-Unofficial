package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.Thaumcraft_Researcher;
import static gregtech.api.enums.Mods.Thaumcraft;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic.GregtechMetaPollutionCreator;

public class GregtechThaumcraftDevices {

    public static void run() {
        if (Thaumcraft.isModLoaded()) {
            Logger.INFO("Gregtech5u Content | Registering Thaumcraft content.");
            run1();
        }
    }

    private static void run1() {
        // 956-960
        GregtechItemList.Thaumcraft_Researcher.set(
            new GregtechMetaPollutionCreator(
                Thaumcraft_Researcher.ID,
                "thaumcraft.gtpp.machine.01",
                "Arcane Researcher",
                5,
                "Thinking for you.",
                0).getStackForm(1L));
    }
}
