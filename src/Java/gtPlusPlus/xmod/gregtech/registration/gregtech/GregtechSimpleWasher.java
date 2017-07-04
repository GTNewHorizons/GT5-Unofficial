package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.Recipe_GT;
import gtPlusPlus.core.util.fluid.FluidUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic.GregtechMetaTileEntity_BasicWasher;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class GregtechSimpleWasher {

	public static void run(){

		
		//Generate Recipe Map for the Dust Washer.
		ItemStack dustClean;
		ItemStack dustDirty;
		for (Materials v : Materials.values()){
			dustClean = GT_OreDictUnificator.get(OrePrefixes.dust, v, 1L);
			dustDirty = GT_OreDictUnificator.get(OrePrefixes.dustImpure, v, 1L);
			if (dustClean != null && dustDirty != null){
				Recipe_GT.Gregtech_Recipe_Map.sSimpleWasherRecipes.addRecipe(
						false,
						new ItemStack[]{dustDirty},
						new ItemStack[]{dustClean},
						null,
						new FluidStack[]{FluidUtils.getFluidStack("water", 100)},
						null,
						20,
						8,
						0);
			}			
		}
		
		
		//Register the Simple Washer Entity.
		GregtechItemList.SimpleDustWasher.set(new GregtechMetaTileEntity_BasicWasher(767, "simplewasher.01.tier.00", "Simple Washer", 0).getStackForm(1L));
	}
}
