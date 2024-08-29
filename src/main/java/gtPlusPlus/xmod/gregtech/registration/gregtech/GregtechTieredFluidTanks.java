package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.GT_FluidTank_HV;
import static gregtech.api.enums.MetaTileEntityIDs.GT_FluidTank_LV;
import static gregtech.api.enums.MetaTileEntityIDs.GT_FluidTank_MV;
import static gregtech.api.enums.MetaTileEntityIDs.GT_FluidTank_ULV;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.storage.GT_MetaTileEntity_TieredTank;

public class GregtechTieredFluidTanks {

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Portable Fluid Tanks.");
        if (CORE.ConfigSwitches.enableMachine_FluidTanks) {
            run1();
        }
    }

    private static void run1() {
        Logger.INFO("Only loading ULV-IV tier tanks for New Horizons Modpack.");
        GregtechItemList.GT_FluidTank_ULV.set(
            new GT_MetaTileEntity_TieredTank(
                GT_FluidTank_ULV.ID,
                "fluidtank.tier.00",
                "Ultra Low Voltage Fluid Tank",
                0).getStackForm(1L));
        GregtechItemList.GT_FluidTank_LV.set(
            new GT_MetaTileEntity_TieredTank(GT_FluidTank_LV.ID, "fluidtank.tier.01", "Low Voltage Fluid Tank", 1)
                .getStackForm(1L));
        GregtechItemList.GT_FluidTank_MV.set(
            new GT_MetaTileEntity_TieredTank(GT_FluidTank_MV.ID, "fluidtank.tier.02", "Medium Voltage Fluid Tank", 2)
                .getStackForm(1L));
        GregtechItemList.GT_FluidTank_HV.set(
            new GT_MetaTileEntity_TieredTank(GT_FluidTank_HV.ID, "fluidtank.tier.03", "High Voltage Fluid Tank", 3)
                .getStackForm(1L));
    }
}
