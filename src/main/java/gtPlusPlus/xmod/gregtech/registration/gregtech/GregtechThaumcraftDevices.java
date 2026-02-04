package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.Thaumcraft_Researcher;
import static gregtech.api.enums.Mods.Thaumcraft;

import gregtech.api.metatileentity.implementations.MTETieredMachineBlock;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic.MTEPollutionCreator;

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
            new MTEPollutionCreator(
                MTETieredMachineBlock.Args.builder()
                    .id(Thaumcraft_Researcher.ID)
                    .translateKey("thaumcraft.gtpp.machine.01")
                    .nameEnglish("Arcane Researcher")
                    .tier(5)
                    .descriptionArray(new String[] { "Thinking for you." })
                    .build()).getStackForm(1L));
    }
}
