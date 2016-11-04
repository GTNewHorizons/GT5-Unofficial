package gtPlusPlus.core.container;

import gtPlusPlus.core.tileentities.machines.TileEntityCharger;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class Container_Charger extends Container {
	public static final int			INPUT_1	= 0;

	private final TileEntityCharger	te;

	private int						slotID	= 0;

	public Container_Charger(final TileEntityCharger te, final EntityPlayer player) {
		this.te = te;

		// Fuel Slot A
		this.addSlotToContainer(new Slot(te, this.slotID++, 80, 53));

		// Inventory
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				this.addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}
		// Hotbar
		for (int i = 0; i < 9; i++) {
			this.addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18, 142));
		}
	}

	@Override
	public boolean canInteractWith(final EntityPlayer player) {
		return this.te.isUseableByPlayer(player);
	}

	@Override
	public ItemStack transferStackInSlot(final EntityPlayer player, final int slotRaw) {
		ItemStack stack = null;
		final Slot slot = (Slot) this.inventorySlots.get(slotRaw);

		if (slot != null && slot.getHasStack()) {
			final ItemStack stackInSlot = slot.getStack();
			stack = stackInSlot.copy();

			if (slotRaw < 3 * 9) {
				if (!this.mergeItemStack(stackInSlot, 3 * 9, this.inventorySlots.size(), true)) {
					return null;
				}
			}
			else if (!this.mergeItemStack(stackInSlot, 0, 3 * 9, false)) {
				return null;
			}

			if (stackInSlot.stackSize == 0) {
				slot.putStack((ItemStack) null);
			}
			else {
				slot.onSlotChanged();
			}
		}
		return stack;
	}
}