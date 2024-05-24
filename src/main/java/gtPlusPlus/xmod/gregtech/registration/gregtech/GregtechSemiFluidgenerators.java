package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.generators.GT_MetaTileEntity_SemiFluidGenerator;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.GregtechMetaTileEntity_LargeSemifluidGenerator;

public class GregtechSemiFluidgenerators {

    public static void run() {
        GregtechItemList.Generator_SemiFluid_LV.set(
            new GT_MetaTileEntity_SemiFluidGenerator(
                837,
                "basicgenerator.semifluid.tier.01",
                "Basic Semi-Fluid Generator",
                1).getStackForm(1L));
        GregtechItemList.Generator_SemiFluid_MV.set(
            new GT_MetaTileEntity_SemiFluidGenerator(
                838,
                "basicgenerator.semifluid.tier.02",
                "Advanced Semi-Fluid Generator",
                2).getStackForm(1L));
        GregtechItemList.Generator_SemiFluid_HV.set(
            new GT_MetaTileEntity_SemiFluidGenerator(
                839,
                "basicgenerator.semifluid.tier.03",
                "Turbo Semi-Fluid Generator",
                3).getStackForm(1L));
        GregtechItemList.Generator_SemiFluid_EV.set(
            new GT_MetaTileEntity_SemiFluidGenerator(
                993,
                "basicgenerator.semifluid.tier.04",
                "Turbo Semi-Fluid Generator II",
                4).getStackForm(1L));
        GregtechItemList.Generator_SemiFluid_IV.set(
            new GT_MetaTileEntity_SemiFluidGenerator(
                994,
                "basicgenerator.semifluid.tier.05",
                "Turbo Semi-Fluid Generator III",
                5).getStackForm(1L));
        GregtechItemList.Controller_LargeSemifluidGenerator.set(
            new GregtechMetaTileEntity_LargeSemifluidGenerator(
                31026,
                "gtpp.multimachine.semifluidgenerator",
                "Large Semifluid Burner").getStackForm(1L));
    }
}
