package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.storage.GT_MetaTileEntity_AdvancedCraftingTable;
import gtPlusPlus.xmod.gregtech.common.tileentities.storage.GT_MetaTileEntity_BronzeCraftingTable;
import gtPlusPlus.xmod.gregtech.common.tileentities.storage.GT_MetaTileEntity_TieredTank;
import gtPlusPlus.xmod.gregtech.common.tileentities.storage.GT_NBT_Tank;

public class GregtechTieredFluidTanks
{
	public static void run()
	{
		if (LoadedMods.Gregtech){
			Utils.LOG_INFO("Gregtech5u Content | Registering Fluid Tanks.");
			run1();
			//run2();
		}
		
	}

	private static void run1()
	{
		int ID = 817;
		GregtechItemList.GT_FluidTank_ULV.set(new GT_MetaTileEntity_TieredTank(ID++, "fluidtank.tier.00", "Ultra Low Voltage Fluid Tank", 0).getStackForm(1L));
		GregtechItemList.GT_FluidTank_LV.set(new GT_MetaTileEntity_TieredTank(ID++, "fluidtank.tier.01", "Low Voltage Fluid Tank", 1).getStackForm(1L));
        GregtechItemList.GT_FluidTank_MV.set(new GT_MetaTileEntity_TieredTank(ID++, "fluidtank.tier.02", "Medium Voltage Fluid Tank", 2).getStackForm(1L));
        GregtechItemList.GT_FluidTank_HV.set(new GT_MetaTileEntity_TieredTank(ID++, "fluidtank.tier.03", "High Voltage Fluid Tank", 3).getStackForm(1L));
        GregtechItemList.GT_FluidTank_EV.set(new GT_MetaTileEntity_TieredTank(ID++, "fluidtank.tier.04", "Extreme Voltage Fluid Tank", 4).getStackForm(1L));
        GregtechItemList.GT_FluidTank_IV.set(new GT_MetaTileEntity_TieredTank(ID++, "fluidtank.tier.05", "Insane Voltage Fluid Tank", 5).getStackForm(1L));
        GregtechItemList.GT_FluidTank_LuV.set(new GT_MetaTileEntity_TieredTank(ID++, "fluidtank.tier.06", "Ludicrous Voltage Fluid Tank", 6).getStackForm(1L));
        GregtechItemList.GT_FluidTank_ZPM.set(new GT_MetaTileEntity_TieredTank(ID++, "fluidtank.tier.07", "ZPM Voltage Fluid Tank", 7).getStackForm(1L));
        GregtechItemList.GT_FluidTank_UV.set(new GT_MetaTileEntity_TieredTank(ID++, "fluidtank.tier.08", "Ultimate Voltage Fluid Tank", 8).getStackForm(1L));
        GregtechItemList.GT_FluidTank_MAX.set(new GT_MetaTileEntity_TieredTank(ID++, "fluidtank.tier.09", "MAX Voltage Fluid Tank", 9).getStackForm(1L));
        
        //Gregtech 4 Workbenches
		GregtechItemList.GT4_Workbench_Bronze.set(new GT_MetaTileEntity_BronzeCraftingTable(828, "workbench.bronze", "Bronze Workbench", 0).getStackForm(1L));
		GregtechItemList.GT4_Workbench_Advanced.set(new GT_MetaTileEntity_AdvancedCraftingTable(829, "workbench.advanced", "Advanced Workbench", 1).getStackForm(1L));
        
        
	}
	
	private static void run2()
	{
		int ID = 900;
		GregtechItemList.GT_FluidTank_ULV.set(new GT_NBT_Tank(ID++, "fluidtankEx.tier.00", "Ultra Low Voltage Fluid Tank", 0).getStackForm(1L));
		GregtechItemList.GT_FluidTank_LV.set(new GT_NBT_Tank(ID++, "fluidtankEx.tier.01", "Low Voltage Fluid Tank", 1).getStackForm(1L));
        GregtechItemList.GT_FluidTank_MV.set(new GT_NBT_Tank(ID++, "fluidtankEx.tier.02", "Medium Voltage Fluid Tank", 2).getStackForm(1L));
        GregtechItemList.GT_FluidTank_HV.set(new GT_NBT_Tank(ID++, "fluidtankEx.tier.03", "High Voltage Fluid Tank", 3).getStackForm(1L));
        GregtechItemList.GT_FluidTank_EV.set(new GT_NBT_Tank(ID++, "fluidtankEx.tier.04", "Extreme Voltage Fluid Tank", 4).getStackForm(1L));
        GregtechItemList.GT_FluidTank_IV.set(new GT_NBT_Tank(ID++, "fluidtankEx.tier.05", "Insane Voltage Fluid Tank", 5).getStackForm(1L));
        GregtechItemList.GT_FluidTank_LuV.set(new GT_NBT_Tank(ID++, "fluidtankEx.tier.06", "Ludicrous Voltage Fluid Tank", 6).getStackForm(1L));
        GregtechItemList.GT_FluidTank_ZPM.set(new GT_NBT_Tank(ID++, "fluidtankEx.tier.07", "ZPM Voltage Fluid Tank", 7).getStackForm(1L));
        GregtechItemList.GT_FluidTank_UV.set(new GT_NBT_Tank(ID++, "fluidtankEx.tier.08", "Ultimate Voltage Fluid Tank", 8).getStackForm(1L));
        GregtechItemList.GT_FluidTank_MAX.set(new GT_NBT_Tank(ID++, "fluidtankEx.tier.09", "MAX Voltage Fluid Tank", 9).getStackForm(1L));
		

	}
}
