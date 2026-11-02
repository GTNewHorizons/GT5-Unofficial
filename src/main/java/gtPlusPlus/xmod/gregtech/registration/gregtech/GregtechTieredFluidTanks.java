package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.GTFluidTank_HV;
import static gregtech.api.enums.MetaTileEntityIDs.GTFluidTank_LV;
import static gregtech.api.enums.MetaTileEntityIDs.GTFluidTank_MV;
import static gregtech.api.enums.MetaTileEntityIDs.GTFluidTank_ULV;

import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.storage.MTETieredTank;

public class GregtechTieredFluidTanks {

    public static void run() {
        run1();
    }

    private static void run1() {
        GregtechItemList.GTFluidTank_ULV
            .set(new MTETieredTank(GTFluidTank_ULV.ID, "fluidtank.tier.00", "", 0).getStackForm(1L));
        GregtechItemList.GTFluidTank_LV
            .set(new MTETieredTank(GTFluidTank_LV.ID, "fluidtank.tier.01", "", 1).getStackForm(1L));
        GregtechItemList.GTFluidTank_MV
            .set(new MTETieredTank(GTFluidTank_MV.ID, "fluidtank.tier.02", "", 2).getStackForm(1L));
        GregtechItemList.GTFluidTank_HV
            .set(new MTETieredTank(GTFluidTank_HV.ID, "fluidtank.tier.03", "", 3).getStackForm(1L));
    }
}
