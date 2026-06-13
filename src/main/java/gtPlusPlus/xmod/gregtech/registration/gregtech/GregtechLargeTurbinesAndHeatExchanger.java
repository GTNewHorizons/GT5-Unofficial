package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.Hatch_Turbine_Rotor;
import static gregtech.api.enums.MetaTileEntityIDs.Large_Gas_Turbine;
import static gregtech.api.enums.MetaTileEntityIDs.Large_HPSteam_Turbine;
import static gregtech.api.enums.MetaTileEntityIDs.Large_Plasma_Turbine;
import static gregtech.api.enums.MetaTileEntityIDs.Large_SCSteam_Turbine;
import static gregtech.api.enums.MetaTileEntityIDs.Large_Steam_Turbine;
import static gregtech.api.enums.MetaTileEntityIDs.XL_HeatExchanger;

import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchTurbine;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.advanced.MTEAdvHeatExchanger;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.turbines.MTELargerTurbineGasLegacy;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.turbines.MTELargerTurbineHPSteamLegacy;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.turbines.MTELargerTurbinePlasmaLegacy;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.turbines.MTELargerTurbineSCSteamLegacy;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.turbines.MTELargerTurbineSteamLegacy;

public class GregtechLargeTurbinesAndHeatExchanger {

    public static void run() {
        run1();
    }

    private static void run1() {
        GregtechItemList.Large_Steam_Turbine.set(
            new MTELargerTurbineSteamLegacy(
                Large_Steam_Turbine.ID,
                "multimachine.largerturbine",
                "XL Turbo Steam Turbine").getStackForm(1L));
        GregtechItemList.Large_HPSteam_Turbine.set(
            new MTELargerTurbineHPSteamLegacy(
                Large_HPSteam_Turbine.ID,
                "multimachine.largerhpturbine",
                "XL Turbo HP Steam Turbine").getStackForm(1L));
        GregtechItemList.Large_Gas_Turbine.set(
            new MTELargerTurbineGasLegacy(Large_Gas_Turbine.ID, "multimachine.largergasturbine", "XL Turbo Gas Turbine")
                .getStackForm(1L));
        GregtechItemList.Large_Plasma_Turbine.set(
            new MTELargerTurbinePlasmaLegacy(
                Large_Plasma_Turbine.ID,
                "multimachine.largerplasmaturbine",
                "XL Turbo Plasma Turbine").getStackForm(1L));
        GregtechItemList.Large_SCSteam_Turbine.set(
            new MTELargerTurbineSCSteamLegacy(
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
