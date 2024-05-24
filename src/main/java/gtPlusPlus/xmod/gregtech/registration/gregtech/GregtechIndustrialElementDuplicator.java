package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_ElementalDataOrbHolder;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.GregtechMTE_ElementalDuplicator;

public class GregtechIndustrialElementDuplicator {

    public static void run() {

        Logger.INFO("Gregtech5u Content | Registering Elemental Duplicator Multiblock.");

        GregtechItemList.Controller_ElementalDuplicator.set(
            new GregtechMTE_ElementalDuplicator(31050, "gtpp.multimachine.replicator", "Elemental Duplicator")
                .getStackForm(1L));
        GregtechItemList.Hatch_Input_Elemental_Duplicator.set(
            new GT_MetaTileEntity_Hatch_ElementalDataOrbHolder(
                31051,
                "hatch.input_bus.elementalorbholder",
                "Data Orb Repository",
                7).getStackForm(1L));
    }
}
