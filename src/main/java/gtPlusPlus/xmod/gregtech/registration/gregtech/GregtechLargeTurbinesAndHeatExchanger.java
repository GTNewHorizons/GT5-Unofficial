package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.Hatch_Turbine_Rotor;
import static gregtech.api.enums.MetaTileEntityIDs.Large_Gas_Turbine;
import static gregtech.api.enums.MetaTileEntityIDs.Large_HPSteam_Turbine;
import static gregtech.api.enums.MetaTileEntityIDs.Large_Plasma_Turbine;
import static gregtech.api.enums.MetaTileEntityIDs.Large_SCSteam_Turbine;
import static gregtech.api.enums.MetaTileEntityIDs.Large_Steam_Turbine;
import static gregtech.api.enums.MetaTileEntityIDs.XL_HeatExchanger;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchTurbine;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.advanced.MTEAdvHeatExchanger;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.turbines.MTELargeTurbineGas;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.turbines.MTELargeTurbinePlasma;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.turbines.MTELargeTurbineSCSteam;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.turbines.MTELargeTurbineSHSteam;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.turbines.MTELargeTurbineSteam;

public class GregtechLargeTurbinesAndHeatExchanger {

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Larger Turbines & Extra Large Heat Exchanger.");
        run1();
    }

    private static void run1() {
        GregtechItemList.Large_Steam_Turbine.set(
            new MTELargeTurbineSteam(Large_Steam_Turbine.ID, "multimachine.largerturbine", "XL Turbo Steam Turbine")
                .getStackForm(1L));
        GregtechItemList.Large_HPSteam_Turbine.set(
            new MTELargeTurbineSHSteam(
                Large_HPSteam_Turbine.ID,
                "multimachine.largerhpturbine",
                "XL Turbo HP Steam Turbine").getStackForm(1L));
        GregtechItemList.Large_Gas_Turbine.set(
            new MTELargeTurbineGas(Large_Gas_Turbine.ID, "multimachine.largergasturbine", "XL Turbo Gas Turbine")
                .getStackForm(1L));
        GregtechItemList.Large_Plasma_Turbine.set(
            new MTELargeTurbinePlasma(
                Large_Plasma_Turbine.ID,
                "multimachine.largerplasmaturbine",
                "XL Turbo Plasma Turbine").getStackForm(1L));
        GregtechItemList.Large_SCSteam_Turbine.set(
            new MTELargeTurbineSCSteam(
                Large_SCSteam_Turbine.ID,
                "multimachine.largerscturbine",
                "XL Turbo SC Steam Turbine").getStackForm(1L));
        GregtechItemList.Hatch_Turbine_Rotor
            .set(new MTEHatchTurbine(Hatch_Turbine_Rotor.ID, "hatch.turbine", "Rotor Assembly", 8).getStackForm(1L));
        GregtechItemList.XL_HeatExchanger.set(
            new MTEAdvHeatExchanger(XL_HeatExchanger.ID, "multimachine.reallybigheatexchanger", "Whakawhiti Wera XL")
                .getStackForm(1L));
    }
}
