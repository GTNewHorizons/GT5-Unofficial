package gregtech.api.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class HotFuel {

	public static void addNewHotFuel(FluidStack aInput1, FluidStack aOutput1, ItemStack[] outputItems, int[] chances, int aSpecialValue) {
		GT_Recipe x = new GT_Recipe(
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
		Recipe_GT.Gregtech_Recipe_Map.sThermalFuels.addRecipe(x);
	}
	
	
	
	
}
