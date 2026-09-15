package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.Industrial_FuelRefinery;

import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.MTERefinery;

public class GregtechIndustrialFuelRefinery {

    public static void run() {
        run1();
    }

    private static void run1() {
        // Industrial Maceration Stack Multiblock
        GregtechItemList.Industrial_FuelRefinery.set(
            new MTERefinery(
                Industrial_FuelRefinery.ID,
                "industrialrefinery.controller.tier.single",
                "Reactor Fuel Processing Plant").getStackForm(1L));
    }
}
