package gtPlusPlus.core.slots;

import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.xmod.gregtech.common.tileentities.storage.GT_MetaTileEntity_TieredChest;

public class SlotLockedInput extends Slot {

	private ItemStack mLockStack;
	private final IGregTechTileEntity mEntity;
	private boolean mChecked = false;
	
	public SlotLockedInput(final IGregTechTileEntity inventory, final int index, final int x, final int y, ItemStack lockStack) {
		super(inventory, index, x, y);
		mLockStack = lockStack;
		mEntity = inventory;
	}

	@Override
	public boolean isItemValid(final ItemStack itemstack) {	
		if (mEntity == null) {
			return false;
		}
		else {
			if (!mChecked) {
				try {
				mLockStack = (ItemStack) ReflectionUtils.getField(this.mEntity.getMetaTileEntity().getClass(), "mItemStack").get(this.mEntity.getMetaTileEntity());
				}
				catch (Throwable t) {
					t.printStackTrace();
					mLockStack = null;
				}
				mChecked = true;
			}
		}
		
		if (mLockStack == null) {
			return true;
		}
		else {
			if (ItemStack.areItemStacksEqual(itemstack, mLockStack)) {
				return true;
			}
		}		
		return false;
	}

	@Override
	public int getSlotStackLimit() {
		return mLockStack == null ? 64 : mLockStack.getMaxStackSize();
	}

}
