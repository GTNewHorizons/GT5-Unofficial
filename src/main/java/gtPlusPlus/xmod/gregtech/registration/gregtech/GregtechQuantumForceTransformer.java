package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.QuantumForceTransformer;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.GregtechMetaTileEntity_QuantumForceTransformer;

public class GregtechQuantumForceTransformer {

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Quantum Force Transformer Multiblock.");
        if (CORE.ConfigSwitches.enableMultiblock_QuantumForceTransformer) {
            GregtechItemList.QuantumForceTransformer.set(
                new GregtechMetaTileEntity_QuantumForceTransformer(
                    QuantumForceTransformer.ID,
                    "quantumforcetransformer.controller.tier.single",
                    "Quantum Force Transformer").getStackForm(1L));
        }
    }
}
