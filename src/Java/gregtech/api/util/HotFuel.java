package gregtech.api.util;

import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import net.minecraftforge.fluids.FluidStack;

public class HotFuel {

	public static void addNewHotFuel(FluidStack aInput1, FluidStack aInput2, FluidStack aOutput1, int aSpecialValue) {
		GT_Recipe x = new GT_Recipe(
				true,
				null,
				null,
				null,
				null,
				new FluidStack[]{aInput1, aInput2},
				new FluidStack[]{aOutput1},
				1, //1 Tick
				0, //No Eu produced
				aSpecialValue //Magic Number
		);
			GT_Recipe_Map.sHotFuels.addRecipe(x);
	}
	
	
	
	
}
