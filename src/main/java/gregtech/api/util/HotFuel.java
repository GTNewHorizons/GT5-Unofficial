package gregtech.api.util;

import net.minecraft.item.ItemStack;

import net.minecraftforge.fluids.FluidStack;

public class HotFuel {

	public static void addNewHotFuel(FluidStack aInput1, FluidStack aOutput1, ItemStack[] outputItems, int[] chances, int aSpecialValue) {
		GTPP_Recipe x = new GTPP_Recipe(
				true,
				null,
				outputItems,
				null,
				chances,
				new FluidStack[]{aInput1},
				new FluidStack[]{aOutput1},
				1, //1 Tick
				0, //No Eu produced
				aSpecialValue //Magic Number
		);
		GTPP_Recipe.GTPP_Recipe_Map.sThermalFuels.addRecipe(x);
	}
	
	
	
	
}
