package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.GregtechMetaTileEntity_Refinery;

import static gtPlusPlus.xmod.gregtech.registration.gregtech.MetaTileEntityIDs.Industrial_FuelRefinery;

public class GregtechIndustrialFuelRefinery {

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Industrial Fuel Processing and Refinery Multiblock.");
        if (CORE.ConfigSwitches.enableMultiblock_NuclearFuelRefinery) {
            run1();
        }
    }

    private static void run1() {
        // Industrial Maceration Stack Multiblock
        GregtechItemList.Industrial_FuelRefinery.set(
            new GregtechMetaTileEntity_Refinery(
                Industrial_FuelRefinery.ID,
                "industrialrefinery.controller.tier.single",
                "Reactor Fuel Processing Plant").getStackForm(1L));
    }
}
