package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gregtech.api.enums.ItemList;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.material.ALLOY;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.RecipeUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GT_MetaTileEntity_Hatch_CustomFluidBase;

public class GregtechCustomHatches {

	public static void run() {
		if (gtPlusPlus.core.lib.LoadedMods.Gregtech) {
			Logger.INFO("Gregtech5u Content | Registering Custom Fluid Hatches.");
			run1();
		}
	}

	private static void run1() {
			

		GregtechItemList.Hatch_Input_Cryotheum.set(
				new GT_MetaTileEntity_Hatch_CustomFluidBase(
						FluidUtils.getFluidStack("cryotheum", 1).getFluid(), // Fluid to resitrct hatch to
						128000, // Capacity
						967, // ID
						"hatch.cryotheum.input.tier.00", // unlocal name
						"Cryotheum Cooling Hatch" //Local name
						).getStackForm(1L));
		
		GregtechItemList.Hatch_Input_Pyrotheum.set(
				new GT_MetaTileEntity_Hatch_CustomFluidBase(
						FluidUtils.getFluidStack("pyrotheum", 1).getFluid(), // Fluid to resitrct hatch to
						128000, // Capacity
						968, // ID
						"hatch.pyrotheum.input.tier.00", // unlocal name
						"Pyrotheum Heating Vent" //Local name
						).getStackForm(1L));		

		RecipeUtils.addShapedGregtechRecipe(
				CI.component_Plate[6], ALLOY.MARAGING250.getGear(1), CI.component_Plate[6],
				CI.getTieredCircuitOreDictName(4), GregtechItemList.Casing_AdvancedVacuum.get(1), CI.getTieredCircuitOreDictName(4),
				CI.component_Plate[5], ItemList.Hatch_Input_IV.get(1), CI.component_Plate[5],
				GregtechItemList.Hatch_Input_Cryotheum.get(1L, new Object[0]));
		
		RecipeUtils.addShapedGregtechRecipe(
				CI.component_Plate[5], ALLOY.MARAGING300.getGear(1), CI.component_Plate[5],
				CI.getTieredCircuitOreDictName(4), GregtechItemList.Casing_Adv_BlastFurnace.get(1), CI.getTieredCircuitOreDictName(4),
				CI.component_Plate[6], ItemList.Hatch_Input_IV.get(1), CI.component_Plate[6],
				GregtechItemList.Hatch_Input_Pyrotheum.get(1L, new Object[0]));
		
	}

}
