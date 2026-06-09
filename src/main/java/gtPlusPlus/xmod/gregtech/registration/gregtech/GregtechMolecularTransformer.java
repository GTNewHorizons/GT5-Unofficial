package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.Controller_MolecularTransformer;

import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.MTEIndustrialMolecularTransformerLegacy;

public class GregtechMolecularTransformer {

    public static void run() {
        run1();
    }

    private static void run1() {
        GregtechItemList.Controller_MolecularTransformer.set(
            new MTEIndustrialMolecularTransformerLegacy(
                Controller_MolecularTransformer.ID,
                "moleculartransformer.controller.tier.single",
                "Molecular Transformer").getStackForm(1L));
    }
}
