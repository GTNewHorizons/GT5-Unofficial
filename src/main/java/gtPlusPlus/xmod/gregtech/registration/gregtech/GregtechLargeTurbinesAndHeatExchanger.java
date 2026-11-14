package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.Hatch_Turbine_Rotor;
import static gregtech.api.enums.MetaTileEntityIDs.XL_HeatExchanger;

import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchTurbine;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.advanced.MTEAdvHeatExchanger;

public class GregtechLargeTurbinesAndHeatExchanger {

    public static void run() {
        run1();
    }

    private static void run1() {
        GregtechItemList.Hatch_Turbine_Rotor
            .set(new MTEHatchTurbine(Hatch_Turbine_Rotor.ID, "hatch.turbine", "Rotor Assembly", 8).getStackForm(1L));
        GregtechItemList.XL_HeatExchanger.set(
            new MTEAdvHeatExchanger(XL_HeatExchanger.ID, "multimachine.reallybigheatexchanger", "Whakawhiti Wera XL")
                .getStackForm(1L));
    }
}
