package gtPlusPlus.core.container;

import gtPlusPlus.core.tileentities.machines.TileEntityNHG;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;

public class Container_NHG extends Container {
	public static final int		INPUT_1	= 0, INPUT_2 = 1, INPUT_3 = 2, INPUT_4 = 3, INPUT_5 = 4, INPUT_6 = 5,
			INPUT_7 = 6, INPUT_8 = 7, INPUT_9 = 8, INPUT_10 = 9, INPUT_11 = 10, INPUT_12 = 11, INPUT_13 = 12,
			INPUT_14 = 13, INPUT_15 = 14, INPUT_16 = 15, INPUT_17 = 16, INPUT_18 = 17, OUTPUT = 18;

	private final TileEntityNHG	te;

	private int					slotID	= 0;

	public Container_NHG(final TileEntityNHG te, final EntityPlayer player) {
		this.te = te;

		// Fuel Rods A
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				this.addSlotToContainer(new Slot(te, this.slotID++, 8 + j * 18, 17 + i * 18));
			}
		}
		// Fuel Rods B
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				this.addSlotToContainer(new Slot(te, this.slotID++, 116 + j * 18, 17 + i * 18));
			}
		}

		// Output
		this.addSlotToContainer(new SlotFurnace(player, te, Container_NHG.OUTPUT, 80, 53));

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