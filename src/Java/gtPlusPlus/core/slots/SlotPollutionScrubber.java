package gtPlusPlus.core.slots;

import java.util.HashMap;

import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.item.general.ItemAirFilter;
import gtPlusPlus.core.item.general.ItemBasicScrubberTurbine;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.recipe.common.CI;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotPollutionScrubber extends Slot {

	private final int mType;
	private final int mTier;
	
	private static HashMap<Integer, ItemStack> mConveyorMap = new HashMap<Integer, ItemStack>();
	
	static {
		for (int i=0; i<(CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK ? 9 : 5); i++) {
			mConveyorMap.put(i, CI.getConveyor(i, 1));			
		}
	}

	public SlotPollutionScrubber(final int aType, final int aTier, final IInventory inventory, final int slot, final int x, final int y) {
		super(inventory, slot, x, y);
		mType = aType;
		mTier = aTier;
	}

	@Override
	public synchronized boolean isItemValid(final ItemStack itemstack) {
		return isItemValidForSlot(this, itemstack);
	}

	public static synchronized boolean isItemValidForSlot(final SlotPollutionScrubber aSlot, final ItemStack itemstack) {
		if (aSlot.mType == 0) {
			if (itemstack.getItem() instanceof ItemBasicScrubberTurbine) {
				return true;
			}			
			if (itemstack.getItem() instanceof GT_MetaGenerated_Tool  && itemstack.getItemDamage() >= 170 && itemstack.getItemDamage() <= 179){
				return true;
			}
		}
		else if (aSlot.mType == 1) {
			if (itemstack.getItem() instanceof ItemAirFilter) {
				return true;
			}		
		}
		else if (aSlot.mType == 2) {
			ItemStack aConveyorStack = mConveyorMap.get(aSlot.mTier);
			if (GT_Utility.areStacksEqual(itemstack, aConveyorStack, true)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int getSlotStackLimit() {
		return 1;
	}
}
