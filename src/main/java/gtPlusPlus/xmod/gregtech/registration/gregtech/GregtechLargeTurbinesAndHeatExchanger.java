package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.Mods.GoodGenerator;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Turbine;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.advanced.GregtechMetaTileEntity_Adv_HeatExchanger;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.turbines.GT_MTE_LargeTurbine_Gas;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.turbines.GT_MTE_LargeTurbine_Plasma;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.turbines.GT_MTE_LargeTurbine_SCSteam;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.turbines.GT_MTE_LargeTurbine_SHSteam;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.turbines.GT_MTE_LargeTurbine_Steam;

public class GregtechLargeTurbinesAndHeatExchanger {

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Larger Turbines & Extra Large Heat Exchanger.");
        run1();
    }

    private static void run1() {
        GregtechItemList.Large_Steam_Turbine.set(
                new GT_MTE_LargeTurbine_Steam(865, "multimachine.largerturbine", "XL Turbo Steam Turbine")
                        .getStackForm(1L));
        GregtechItemList.Large_HPSteam_Turbine.set(
                new GT_MTE_LargeTurbine_SHSteam(866, "multimachine.largerhpturbine", "XL Turbo HP Steam Turbine")
                        .getStackForm(1L));
        GregtechItemList.Large_Gas_Turbine.set(
                new GT_MTE_LargeTurbine_Gas(31073, "multimachine.largergasturbine", "XL Turbo Gas Turbine")
                        .getStackForm(1L));
        GregtechItemList.Large_Plasma_Turbine.set(
                new GT_MTE_LargeTurbine_Plasma(31074, "multimachine.largerplasmaturbine", "XL Turbo Plasma Turbine")
                        .getStackForm(1L));
        if (GoodGenerator.isModLoaded()) {
            GregtechItemList.Large_SCSteam_Turbine.set(
                    new GT_MTE_LargeTurbine_SCSteam(31076, "multimachine.largerscturbine", "XL Turbo SC Steam Turbine")
                            .getStackForm(1L));
        }
        GregtechItemList.Hatch_Turbine_Rotor
                .set(new GT_MetaTileEntity_Hatch_Turbine(30010, "hatch.turbine", "Rotor Assembly", 8).getStackForm(1L));
        GregtechItemList.XL_HeatExchanger.set(
                new GregtechMetaTileEntity_Adv_HeatExchanger(
                        31079,
                        "multimachine.reallybigheatexchanger",
                        "Whakawhiti Wera XL").getStackForm(1L));
    }
}
