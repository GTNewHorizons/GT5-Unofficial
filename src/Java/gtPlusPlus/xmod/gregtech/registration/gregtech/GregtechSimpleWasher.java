package gtPlusPlus.xmod.gregtech.registration.gregtech;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.Recipe_GT;

import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic.GregtechMetaTileEntity_BasicWasher;
import net.minecraftforge.fluids.FluidStack;

public class GregtechSimpleWasher {

	public static void run() {
		if (CORE.ConfigSwitches.enableMachine_SimpleWasher){
			generateDirtyDustRecipes();
			generateDirtyCrushedRecipes();
			// Register the Simple Washer Entity.
			GregtechItemList.SimpleDustWasher_ULV
			.set(new GregtechMetaTileEntity_BasicWasher(767, "simplewasher.01.tier.01", "Simple Washer I", 0)
					.getStackForm(1L));
			
			//People want them in higher tiers apparently
			GregtechItemList.SimpleDustWasher_MV
			.set(new GregtechMetaTileEntity_BasicWasher(31017, "simplewasher.01.tier.02", "Simple Washer II", 2)
					.getStackForm(1L));
			GregtechItemList.SimpleDustWasher_EV
			.set(new GregtechMetaTileEntity_BasicWasher(31018, "simplewasher.01.tier.03", "Simple Washer III", 4)
					.getStackForm(1L));
			GregtechItemList.SimpleDustWasher_LuV
			.set(new GregtechMetaTileEntity_BasicWasher(31019, "simplewasher.01.tier.04", "Simple Washer IV", 6)
					.getStackForm(1L));
			GregtechItemList.SimpleDustWasher_UV
			.set(new GregtechMetaTileEntity_BasicWasher(31020, "simplewasher.01.tier.05", "Simple Washer V", 8)
					.getStackForm(1L));
		}
	}

	private static boolean generateDirtyDustRecipes(){
		int mRecipeCount = 0;
		// Generate Recipe Map for the Dust Washer.
		ItemStack dustClean;
		ItemStack dustDirty;
		for (Materials v : Materials.values()) {
			dustClean = GT_OreDictUnificator.get(OrePrefixes.dust, v, 1L);
			dustDirty = GT_OreDictUnificator.get(OrePrefixes.dustImpure, v, 1L);
			if (dustClean != null && dustDirty != null) {
				Recipe_GT.Gregtech_Recipe_Map.sSimpleWasherRecipes.addRecipe(false, new ItemStack[] { dustDirty },
						new ItemStack[] { dustClean }, null,
						new FluidStack[] { FluidUtils.getFluidStack("water", 100) }, null, 5, 8, 0);
			}
		}	
		
		if (Recipe_GT.Gregtech_Recipe_Map.sSimpleWasherRecipes.mRecipeList.size() > mRecipeCount){
			return true;
		}
		return false;
	}

	private static boolean generateDirtyCrushedRecipes(){
		int mRecipeCount = Recipe_GT.Gregtech_Recipe_Map.sSimpleWasherRecipes.mRecipeList.size();
		// Generate Recipe Map for the Dust Washer.
		ItemStack crushedClean;
		ItemStack crushedDirty;
		for (Materials v : Materials.values()) {
			crushedClean = GT_OreDictUnificator.get(OrePrefixes.crushedPurified, v, 1L);
			crushedDirty = GT_OreDictUnificator.get(OrePrefixes.crushed, v, 1L);
			if (crushedClean != null && crushedDirty != null) {
				Recipe_GT.Gregtech_Recipe_Map.sSimpleWasherRecipes.addRecipe(false, new ItemStack[] { crushedDirty },
						new ItemStack[] { crushedClean }, null,
						new FluidStack[] { FluidUtils.getFluidStack("water", 100) }, null, 5, 8, 0);
			}
		}

		if (Recipe_GT.Gregtech_Recipe_Map.sSimpleWasherRecipes.mRecipeList.size() > mRecipeCount){
			return true;
		}
		return false;
	}
}
