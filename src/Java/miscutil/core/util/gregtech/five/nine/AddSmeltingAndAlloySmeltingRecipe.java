package miscutil.core.util.gregtech.five.nine;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import net.minecraft.item.ItemStack;

public class AddSmeltingAndAlloySmeltingRecipe {

	public static boolean run(ItemStack aInput, ItemStack aOutput, boolean hidden) {
		if (aInput == null || aOutput == null) return false;
		boolean temp = false;
		if (aInput.stackSize == 1 && GT_ModHandler.addSmeltingRecipe(aInput, aOutput)) temp = true;
		if (GT_Values.RA.addAlloySmelterRecipe(aInput, OrePrefixes.ingot.contains(aOutput) ? ItemList.Shape_Mold_Ingot.get(0) : OrePrefixes.block.contains(aOutput) ? ItemList.Shape_Mold_Block.get(0) : OrePrefixes.nugget.contains(aOutput) ? ItemList.Shape_Mold_Nugget.get(0) : null, aOutput, 130, 3))
			temp = true;
		if (GT_ModHandler.addInductionSmelterRecipe(aInput, null, aOutput, null, aOutput.stackSize * 1600, 0)) temp = true;
		return temp;
	}

}
