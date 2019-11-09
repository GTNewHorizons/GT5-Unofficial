package container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import tileentities.TE_ItemDistributionNode;

public class Container_ItemDistributionNode extends Container {
	
	private final TE_ItemDistributionNode te;
	
	private int slotID = 0;
	
	public Container_ItemDistributionNode(TE_ItemDistributionNode te, EntityPlayer player) {
		this.te = te;
		
		// Networked Storage
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				super.addSlotToContainer(new Slot(te, slotID++, 18 * j + 20, 19 * i + 20));
			}
		}
		
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
		return te.isUseableByPlayer(player);
	}

}
