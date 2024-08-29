package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.Controller_IndustrialAutoChisel;
import static gregtech.api.enums.MetaTileEntityIDs.GT_Chisel_HV;
import static gregtech.api.enums.MetaTileEntityIDs.GT_Chisel_LV;
import static gregtech.api.enums.MetaTileEntityIDs.GT_Chisel_MV;

import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic.GregtechMetaTileEntity_AutoChisel;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.GregtechMetaTileEntity_IndustrialChisel;

public class GregtechIndustrialChisel {

    public static void run() {
        GregtechItemList.GT_Chisel_LV.set(
            new GregtechMetaTileEntity_AutoChisel(GT_Chisel_LV.ID, "chisel.tier.01", "Basic Auto-Chisel", 1)
                .getStackForm(1L));
        GregtechItemList.GT_Chisel_MV.set(
            new GregtechMetaTileEntity_AutoChisel(GT_Chisel_MV.ID, "chisel.tier.02", "Advanced Auto-Chisel", 2)
                .getStackForm(1L));
        GregtechItemList.GT_Chisel_HV.set(
            new GregtechMetaTileEntity_AutoChisel(GT_Chisel_HV.ID, "chisel.tier.03", "Precision Auto-Chisel", 3)
                .getStackForm(1L));

        GregtechItemList.Controller_IndustrialAutoChisel.set(
            new GregtechMetaTileEntity_IndustrialChisel(
                Controller_IndustrialAutoChisel.ID,
                "multimachine.adv.chisel",
                "Industrial 3D Copying Machine").getStackForm(1L));
    }
}
