package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.GTFluidTank_HV;
import static gregtech.api.enums.MetaTileEntityIDs.GTFluidTank_LV;
import static gregtech.api.enums.MetaTileEntityIDs.GTFluidTank_MV;
import static gregtech.api.enums.MetaTileEntityIDs.GTFluidTank_ULV;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.storage.MTETieredTank;

public class GregtechTieredFluidTanks {

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Portable Fluid Tanks.");
        if (GTPPCore.ConfigSwitches.enableMachine_FluidTanks) {
            run1();
        }
    }

    private static void run1() {
        Logger.INFO("Only loading ULV-IV tier tanks for New Horizons Modpack.");
        GregtechItemList.GTFluidTank_ULV.set(
            new MTETieredTank(GTFluidTank_ULV.ID, "fluidtank.tier.00", "Ultra Low Voltage Fluid Tank", 0)
                .getStackForm(1L));
        GregtechItemList.GTFluidTank_LV.set(
            new MTETieredTank(GTFluidTank_LV.ID, "fluidtank.tier.01", "Low Voltage Fluid Tank", 1).getStackForm(1L));
        GregtechItemList.GTFluidTank_MV.set(
            new MTETieredTank(GTFluidTank_MV.ID, "fluidtank.tier.02", "Medium Voltage Fluid Tank", 2).getStackForm(1L));
        GregtechItemList.GTFluidTank_HV.set(
            new MTETieredTank(GTFluidTank_HV.ID, "fluidtank.tier.03", "High Voltage Fluid Tank", 3).getStackForm(1L));
    }
}
