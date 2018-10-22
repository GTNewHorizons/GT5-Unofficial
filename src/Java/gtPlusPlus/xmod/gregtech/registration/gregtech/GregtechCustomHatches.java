package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gtPlusPlus.core.recipe.common.CI.bitsd;

import gregtech.api.enums.ItemList;
import gregtech.api.util.GT_ModHandler;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.material.ALLOY;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.RecipeUtils;
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

		RecipeUtils.addShapedGregtechRecipe(CI.component_Plate[6], ALLOY.MARAGING250.getGear(1), CI.component_Plate[6],
				CI.getTieredCircuitOreDictName(4), GregtechItemList.Casing_AdvancedVacuum.get(1),
				CI.getTieredCircuitOreDictName(4), CI.component_Plate[5], ItemList.Hatch_Input_IV.get(1),
				CI.component_Plate[5], GregtechItemList.Hatch_Input_Cryotheum.get(1L, new Object[0]));

		RecipeUtils.addShapedGregtechRecipe(CI.component_Plate[5], ALLOY.MARAGING300.getGear(1), CI.component_Plate[5],
				CI.getTieredCircuitOreDictName(4), GregtechItemList.Casing_Adv_BlastFurnace.get(1),
				CI.getTieredCircuitOreDictName(4), CI.component_Plate[6], ItemList.Hatch_Input_IV.get(1),
				CI.component_Plate[6], GregtechItemList.Hatch_Input_Pyrotheum.get(1L, new Object[0]));

		RecipeUtils.addShapedGregtechRecipe(CI.component_Plate[8], ALLOY.HG1223.getGear(1), CI.component_Plate[9],
				CI.getTieredCircuitOreDictName(7), GregtechItemList.Casing_Naq_Reactor_A.get(1),
				CI.getTieredCircuitOreDictName(7), CI.component_Plate[9], ItemList.Hatch_Input_ZPM.get(1),
				CI.component_Plate[8], GregtechItemList.Hatch_Input_Naquadah.get(1L, new Object[0]));

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
		
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Hatch_Muffler_Adv_LV.get(1L, new Object[0]), bitsd,
				new Object[] { "M", "P", Character.valueOf('M'), ItemList.Hatch_Muffler_LV.get(1), Character.valueOf('P'),
						GregtechItemList.Pollution_Cleaner_LV.get(1) });
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Hatch_Muffler_Adv_MV.get(1L, new Object[0]), bitsd,
				new Object[] { "M", "P", Character.valueOf('M'), ItemList.Hatch_Muffler_MV.get(1), Character.valueOf('P'),
						GregtechItemList.Pollution_Cleaner_MV.get(1) });
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Hatch_Muffler_Adv_HV.get(1L, new Object[0]), bitsd,
				new Object[] { "M", "P", Character.valueOf('M'), ItemList.Hatch_Muffler_HV.get(1), Character.valueOf('P'),
						GregtechItemList.Pollution_Cleaner_HV.get(1) });
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Hatch_Muffler_Adv_EV.get(1L, new Object[0]), bitsd,
				new Object[] { "M", "P", Character.valueOf('M'), ItemList.Hatch_Muffler_EV.get(1), Character.valueOf('P'),
						GregtechItemList.Pollution_Cleaner_EV.get(1) });		
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Hatch_Muffler_Adv_IV.get(1L, new Object[0]), bitsd,
				new Object[] { "M", "P", Character.valueOf('M'), ItemList.Hatch_Muffler_IV.get(1), Character.valueOf('P'),
						GregtechItemList.Pollution_Cleaner_IV.get(1) });
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Hatch_Muffler_Adv_LuV.get(1L, new Object[0]), bitsd,
				new Object[] { "M", "P", Character.valueOf('M'), ItemList.Hatch_Muffler_LuV.get(1), Character.valueOf('P'),
						GregtechItemList.Pollution_Cleaner_LuV.get(1) });
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Hatch_Muffler_Adv_ZPM.get(1L, new Object[0]), bitsd,
				new Object[] { "M", "P", Character.valueOf('M'), ItemList.Hatch_Muffler_ZPM.get(1), Character.valueOf('P'),
						GregtechItemList.Pollution_Cleaner_ZPM.get(1) });
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Hatch_Muffler_Adv_UV.get(1L, new Object[0]), bitsd,
				new Object[] { "M", "P", Character.valueOf('M'), ItemList.Hatch_Muffler_UV.get(1), Character.valueOf('P'),
						GregtechItemList.Pollution_Cleaner_UV.get(1) });
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Hatch_Muffler_Adv_MAX.get(1L, new Object[0]), bitsd,
				new Object[] { "M", "P", Character.valueOf('M'), ItemList.Hatch_Muffler_MAX.get(1), Character.valueOf('P'),
						GregtechItemList.Pollution_Cleaner_MAX.get(1) });
		
		
		//GT++ multiblock Control Core Bus
		GregtechItemList.Hatch_Control_Core
		.set((new GT_MetaTileEntity_Hatch_ControlCore(30020, "hatch.control.adv", "Control Core Module", 1))
				.getStackForm(1L));
	}

}
