package common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class Container_ItemProxySource extends Container {
	
	private final IInventory teInventory;
	
	private int slotID = 0;
	
	public Container_ItemProxySource(TileEntity te, EntityPlayer player) {
		this.teInventory = (IInventory) te;
		
		// Source Slots
		addSlotToContainer(new Slot(teInventory, slotID++, 53, 8));
		addSlotToContainer(new Slot(teInventory, slotID++, 71, 8));
		addSlotToContainer(new Slot(teInventory, slotID++, 89, 8));
		addSlotToContainer(new Slot(teInventory, slotID++, 107, 8));
		addSlotToContainer(new Slot(teInventory, slotID++, 53, 26));
		addSlotToContainer(new Slot(teInventory, slotID++, 71, 26));
		addSlotToContainer(new Slot(teInventory, slotID++, 89, 26));
		addSlotToContainer(new Slot(teInventory, slotID++, 107, 26));
		addSlotToContainer(new Slot(teInventory, slotID++, 53, 44));
		addSlotToContainer(new Slot(teInventory, slotID++, 71, 44));
		addSlotToContainer(new Slot(teInventory, slotID++, 89, 44));
		addSlotToContainer(new Slot(teInventory, slotID++, 107, 44));
		addSlotToContainer(new Slot(teInventory, slotID++, 53, 62));
		addSlotToContainer(new Slot(teInventory, slotID++, 71, 62));
		addSlotToContainer(new Slot(teInventory, slotID++, 89, 62));
		addSlotToContainer(new Slot(teInventory, slotID++, 107, 62));
		
		//Inventory
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        // Hotbar
        for (int i = 0; i < 9; i++) {
            addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18, 142));
        }
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotRaw) {
		ItemStack stack = null;
		final Slot slot = (Slot) inventorySlots.get(slotRaw);

		if (slot != null && slot.getHasStack()) {
			final ItemStack stackInSlot = slot.getStack();
			stack = stackInSlot.copy();

			if (slotRaw < 3 * 9) {
				if (!mergeItemStack(stackInSlot, 3 * 9, inventorySlots.size(), true)) {
					return null;
				}
			} else if (!mergeItemStack(stackInSlot, 0, 3 * 9, false)) {
				return null;
			}

			if (stackInSlot.stackSize == 0) {
				slot.putStack((ItemStack) null);
			} else {
				slot.onSlotChanged();
			}
		}
		return stack;
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return teInventory.isUseableByPlayer(player);
	}

}
