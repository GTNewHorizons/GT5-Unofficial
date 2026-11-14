package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.GT_Chisel_HV;
import static gregtech.api.enums.MetaTileEntityIDs.GT_Chisel_LV;
import static gregtech.api.enums.MetaTileEntityIDs.GT_Chisel_MV;

import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic.MTEAutoChisel;

public class GregtechIndustrialChisel {

    public static void run() {
        GregtechItemList.GT_Chisel_LV
            .set(new MTEAutoChisel(GT_Chisel_LV.ID, "chisel.tier.01", "Basic Auto-Chisel", 1).getStackForm(1L));
        GregtechItemList.GT_Chisel_MV
            .set(new MTEAutoChisel(GT_Chisel_MV.ID, "chisel.tier.02", "Advanced Auto-Chisel", 2).getStackForm(1L));
        GregtechItemList.GT_Chisel_HV
            .set(new MTEAutoChisel(GT_Chisel_HV.ID, "chisel.tier.03", "Precision Auto-Chisel", 3).getStackForm(1L));
    }
}
