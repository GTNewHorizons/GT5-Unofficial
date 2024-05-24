package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic.GregtechMetaTileEntity_AutoChisel;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.GregtechMetaTileEntity_IndustrialChisel;

public class GregtechIndustrialChisel {

    public static void run() {
        GregtechItemList.GT_Chisel_LV.set(
            new GregtechMetaTileEntity_AutoChisel(31066, "chisel.tier.01", "Basic Auto-Chisel", 1).getStackForm(1L));
        GregtechItemList.GT_Chisel_MV.set(
            new GregtechMetaTileEntity_AutoChisel(31067, "chisel.tier.02", "Advanced Auto-Chisel", 2).getStackForm(1L));
        GregtechItemList.GT_Chisel_HV.set(
            new GregtechMetaTileEntity_AutoChisel(31068, "chisel.tier.03", "Precision Auto-Chisel", 3)
                .getStackForm(1L));

        GregtechItemList.Controller_IndustrialAutoChisel.set(
            new GregtechMetaTileEntity_IndustrialChisel(
                31069,
                "multimachine.adv.chisel",
                "Industrial 3D Copying Machine").getStackForm(1L));
    }
}
