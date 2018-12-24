package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_ControlCore;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Muffler_Adv;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Naquadah;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GT_MetaTileEntity_Hatch_CustomFluidBase;

public class GregtechCustomHatches {

	public static void run() {
		if (gtPlusPlus.core.lib.LoadedMods.Gregtech) {
			Logger.INFO("Gregtech5u Content | Registering Custom Fluid Hatches.");
			run1();
			
			//No pollution in 5.08
			if (CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK)
			run2();
		}
	}

	private static void run1() {

		GregtechItemList.Hatch_Input_Cryotheum
				.set(new GT_MetaTileEntity_Hatch_CustomFluidBase(FluidUtils.getFluidStack("cryotheum", 1).getFluid(), // Fluid
																														// to
																														// resitrct
																														// hatch
																														// to
						128000, // Capacity
						967, // ID
						"hatch.cryotheum.input.tier.00", // unlocal name
						"Cryotheum Cooling Hatch" // Local name
		).getStackForm(1L));

		GregtechItemList.Hatch_Input_Pyrotheum
				.set(new GT_MetaTileEntity_Hatch_CustomFluidBase(FluidUtils.getFluidStack("pyrotheum", 1).getFluid(), // Fluid
																														// to
																														// resitrct
																														// hatch
																														// to
						128000, // Capacity
						968, // ID
						"hatch.pyrotheum.input.tier.00", // unlocal name
						"Pyrotheum Heating Vent" // Local name
		).getStackForm(1L));

		GregtechItemList.Hatch_Input_Naquadah.set(new GT_MetaTileEntity_Hatch_Naquadah(969, // ID
				"hatch.naquadah.input.tier.00", // unlocal name
				"Naquadah Reactor Input hatch" // Local name
		).getStackForm(1L));

	}

	private static void run2() {
		GregtechItemList.Hatch_Muffler_Adv_LV
				.set((new GT_MetaTileEntity_Hatch_Muffler_Adv(30001, "hatch.muffler.adv.tier.01", "Advanced Muffler Hatch (LV)", 1))
						.getStackForm(1L));
		GregtechItemList.Hatch_Muffler_Adv_MV
				.set((new GT_MetaTileEntity_Hatch_Muffler_Adv(30002, "hatch.muffler.adv.tier.02", "Advanced Muffler Hatch (MV)", 2))
						.getStackForm(1L));
		GregtechItemList.Hatch_Muffler_Adv_HV
				.set((new GT_MetaTileEntity_Hatch_Muffler_Adv(30003, "hatch.muffler.adv.tier.03", "Advanced Muffler Hatch (HV)", 3))
						.getStackForm(1L));
		GregtechItemList.Hatch_Muffler_Adv_EV
				.set((new GT_MetaTileEntity_Hatch_Muffler_Adv(30004, "hatch.muffler.adv.tier.04", "Advanced Muffler Hatch (EV)", 4))
						.getStackForm(1L));
		GregtechItemList.Hatch_Muffler_Adv_IV
				.set((new GT_MetaTileEntity_Hatch_Muffler_Adv(30005, "hatch.muffler.adv.tier.05", "Advanced Muffler Hatch (IV)", 5))
						.getStackForm(1L));
		GregtechItemList.Hatch_Muffler_Adv_LuV
				.set((new GT_MetaTileEntity_Hatch_Muffler_Adv(30006, "hatch.muffler.adv.tier.06", "Advanced Muffler Hatch (LuV)", 6))
						.getStackForm(1L));
		GregtechItemList.Hatch_Muffler_Adv_ZPM
				.set((new GT_MetaTileEntity_Hatch_Muffler_Adv(30007, "hatch.muffler.adv.tier.07", "Advanced Muffler Hatch (ZPM)", 7))
						.getStackForm(1L));
		GregtechItemList.Hatch_Muffler_Adv_UV
				.set((new GT_MetaTileEntity_Hatch_Muffler_Adv(30008, "hatch.muffler.adv.tier.08", "Advanced Muffler Hatch (UV)", 8))
						.getStackForm(1L));
		GregtechItemList.Hatch_Muffler_Adv_MAX
				.set((new GT_MetaTileEntity_Hatch_Muffler_Adv(30009, "hatch.muffler.adv.tier.09", "Advanced Muffler Hatch (MAX)", 9))
						.getStackForm(1L));		
		
		//GT++ multiblock Control Core Bus
		GregtechItemList.Hatch_Control_Core
		.set((new GT_MetaTileEntity_Hatch_ControlCore(30020, "hatch.control.adv", "Control Core Module", 1))
				.getStackForm(1L));
	}

}
