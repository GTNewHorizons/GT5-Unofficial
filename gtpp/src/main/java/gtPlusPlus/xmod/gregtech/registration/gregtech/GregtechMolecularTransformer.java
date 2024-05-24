package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.GregtechMetaTileEntity_IndustrialMolecularTransformer;

public class GregtechMolecularTransformer {

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Molecular Transformer Multiblock.");
        run1();
    }

    private static void run1() {
        GregtechItemList.Controller_MolecularTransformer.set(
            new GregtechMetaTileEntity_IndustrialMolecularTransformer(
                31072,
                "moleculartransformer.controller.tier.single",
                "Molecular Transformer").getStackForm(1L));
    }
}
