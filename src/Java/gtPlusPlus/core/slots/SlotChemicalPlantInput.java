package gtPlusPlus.core.slots;

import gregtech.api.util.Recipe_GT;
import gregtech.api.util.Recipe_GT.Gregtech_Recipe_Map;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

public class SlotChemicalPlantInput extends Slot {

	public SlotChemicalPlantInput(final IInventory inventory, final int index, final int x, final int y) {
		super(inventory, index, x, y);
	}

	@Override
	public boolean isItemValid(final ItemStack itemstack) {
		return isItemValidForChemicalPlantSlot(itemstack);
	}
	
	public static boolean isItemValidForChemicalPlantSlot(ItemStack aStack) {
		boolean validItem = Gregtech_Recipe_Map.sChemicalPlantRecipes.containsInput(aStack);		
		if (!validItem) {
			for (Recipe_GT f : Gregtech_Recipe_Map.sChemicalPlantRecipes.mRecipeList) {
				if (f.mFluidInputs.length > 0) {
					for (FluidStack g : f.mFluidInputs) {
						if (g != null) {
							if (FluidContainerRegistry.containsFluid(aStack, g)) {
								return true;
							}
						}
					}
				}
			}
		}		
		return validItem;
	}

	@Override
	public int getSlotStackLimit() {
		return 64;
	}

}
