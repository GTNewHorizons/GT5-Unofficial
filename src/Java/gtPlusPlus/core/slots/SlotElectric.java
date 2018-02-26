package gtPlusPlus.core.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.GT_MetaGenerated_Tool;

import ic2.api.info.Info;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;

public class SlotElectric extends Slot {
	
	public SlotElectric(final IInventory inventory, final int x, final int y, final int z) {
		super(inventory, x, y, z);
	}

	public SlotElectric(IGregTechTileEntity mTileEntity, int i, int j, int k) {
		this(mTileEntity.getIInventory(mTileEntity.getXCoord(), mTileEntity.getYCoord(), mTileEntity.getZCoord()), i, j, k);
	}

	@Override
	public boolean isItemValid(final ItemStack itemstack) {
		if ((accepts(itemstack)) || (itemstack.getItem() instanceof GT_MetaGenerated_Tool) || (itemstack.getItem() instanceof IElectricItem)) {
			return true;
		}
		return false;
	}
	
	public boolean accepts(final ItemStack stack) {
		if (stack == null) {
			return false;
		}
		return (Info.itemEnergy.getEnergyValue(stack) > 0.0D)
				|| (ElectricItem.manager.discharge(stack, (1.0D / 0.0D), 4, true, true, true) > 0.0D);
	}

	@Override
	public int getSlotStackLimit() {
		return 1;
	}

}
