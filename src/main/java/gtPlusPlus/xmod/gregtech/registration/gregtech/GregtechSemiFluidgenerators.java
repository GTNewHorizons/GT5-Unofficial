package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.Controller_LargeSemifluidGenerator;
import static gregtech.api.enums.MetaTileEntityIDs.Generator_SemiFluid_EV;
import static gregtech.api.enums.MetaTileEntityIDs.Generator_SemiFluid_HV;
import static gregtech.api.enums.MetaTileEntityIDs.Generator_SemiFluid_IV;
import static gregtech.api.enums.MetaTileEntityIDs.Generator_SemiFluid_LV;
import static gregtech.api.enums.MetaTileEntityIDs.Generator_SemiFluid_MV;

import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.generators.MTESemiFluidGenerator;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.MTELargeSemifluidGenerator;

public class GregtechSemiFluidgenerators {

    public static void run() {
        GregtechItemList.Generator_SemiFluid_LV.set(
            new MTESemiFluidGenerator(
                Generator_SemiFluid_LV.ID,
                "basicgenerator.semifluid.tier.01",
                "Basic Semi-Fluid Generator",
                1).getStackForm(1L));
        GregtechItemList.Generator_SemiFluid_MV.set(
            new MTESemiFluidGenerator(
                Generator_SemiFluid_MV.ID,
                "basicgenerator.semifluid.tier.02",
                "Advanced Semi-Fluid Generator",
                2).getStackForm(1L));
        GregtechItemList.Generator_SemiFluid_HV.set(
            new MTESemiFluidGenerator(
                Generator_SemiFluid_HV.ID,
                "basicgenerator.semifluid.tier.03",
                "Turbo Semi-Fluid Generator",
                3).getStackForm(1L));
        GregtechItemList.Generator_SemiFluid_EV.set(
            new MTESemiFluidGenerator(
                Generator_SemiFluid_EV.ID,
                "basicgenerator.semifluid.tier.04",
                "Turbo Semi-Fluid Generator II",
                4).getStackForm(1L));
        GregtechItemList.Generator_SemiFluid_IV.set(
            new MTESemiFluidGenerator(
                Generator_SemiFluid_IV.ID,
                "basicgenerator.semifluid.tier.05",
                "Turbo Semi-Fluid Generator III",
                5).getStackForm(1L));
        GregtechItemList.Controller_LargeSemifluidGenerator.set(
            new MTELargeSemifluidGenerator(
                Controller_LargeSemifluidGenerator.ID,
                "gtpp.multimachine.semifluidgenerator",
                "Large Semifluid Burner").getStackForm(1L));
    }
}
