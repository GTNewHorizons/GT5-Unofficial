package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.storage.GT_MetaTileEntity_TieredTank;

public class GregtechTieredFluidTanks
{
	public static void run()
	{
		if (LoadedMods.Gregtech){
			Utils.LOG_INFO("Gregtech5u Content | Registering Portable Fluid Tanks.");
			if (CORE.configSwitches.enableMachine_FluidTanks) run1();
		}

	}

	private static void run1()	{
		int ID = 817;
		GregtechItemList.GT_FluidTank_ULV.set(new GT_MetaTileEntity_TieredTank(ID++, "fluidtank.tier.00", "Ultra Low Voltage Fluid Tank", 0).getStackForm(1L));
		GregtechItemList.GT_FluidTank_LV.set(new GT_MetaTileEntity_TieredTank(ID++, "fluidtank.tier.01", "Low Voltage Fluid Tank", 1).getStackForm(1L));
		GregtechItemList.GT_FluidTank_MV.set(new GT_MetaTileEntity_TieredTank(ID++, "fluidtank.tier.02", "Medium Voltage Fluid Tank", 2).getStackForm(1L));
		GregtechItemList.GT_FluidTank_HV.set(new GT_MetaTileEntity_TieredTank(ID++, "fluidtank.tier.03", "High Voltage Fluid Tank", 3).getStackForm(1L));
		GregtechItemList.GT_FluidTank_EV.set(new GT_MetaTileEntity_TieredTank(ID++, "fluidtank.tier.04", "Extreme Voltage Fluid Tank", 4).getStackForm(1L));
		GregtechItemList.GT_FluidTank_IV.set(new GT_MetaTileEntity_TieredTank(ID++, "fluidtank.tier.05", "Insane Voltage Fluid Tank", 5).getStackForm(1L));
		if (!CORE.GTNH){
			GregtechItemList.GT_FluidTank_LuV.set(new GT_MetaTileEntity_TieredTank(ID++, "fluidtank.tier.06", "Ludicrous Voltage Fluid Tank", 6).getStackForm(1L));
			GregtechItemList.GT_FluidTank_ZPM.set(new GT_MetaTileEntity_TieredTank(ID++, "fluidtank.tier.07", "ZPM Voltage Fluid Tank", 7).getStackForm(1L));
			GregtechItemList.GT_FluidTank_UV.set(new GT_MetaTileEntity_TieredTank(ID++, "fluidtank.tier.08", "Ultimate Voltage Fluid Tank", 8).getStackForm(1L));
			GregtechItemList.GT_FluidTank_MAX.set(new GT_MetaTileEntity_TieredTank(ID++, "fluidtank.tier.09", "MAX Voltage Fluid Tank", 9).getStackForm(1L));        
		}
	}
}
