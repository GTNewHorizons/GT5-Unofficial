package gtPlusPlus.core.item.tool.misc.box;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import gregtech.api.enums.Materials;
import gregtech.api.metatileentity.implementations.GT_MetaPipeEntity_Fluid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerBoxBase extends Container {

	/*
	 * Finally, in your Container class, you will need to check if the currently
	 * opened inventory's uniqueID is equal to the itemstack's uniqueID in the
	 * method 'transferStackInSlot' as well as check if the itemstack is the
	 * currently equipped item in the method 'slotClick'. In both cases, you'll need
	 * to prevent the itemstack from being moved or it will cause bad things to
	 * happen.
	 */

	/**
	 * Step 3: Create a custom Container for your Inventory
	 */

	/*
	 * There's a LOT of code in this one, but read through all of the comments
	 * carefully and it should become clear what everything does. As a bonus, one of
	 * my previous tutorials is included within!
	 * "How to Properly Override Shift-Clicking" is here and better than ever! At
	 * least in my opinion. If you're like me, and you find no end of frustration
	 * trying to figure out which f-ing index you should use for which slots in your
	 * container when overriding transferStackInSlot, or if your following the
	 * original tutorial, then read on.
	 */

	/**
	 * The Item Inventory for this Container, only needed if you want to reference
	 * isUseableByPlayer
	 */
	private final CustomBoxInventory inventory;
	/**
	 * Using these will make transferStackInSlot easier to understand and implement
	 * INV_START is the index of the first slot in the Player's Inventory, so our
	 * CustomBoxInventory's number of slots (e.g. 5 slots is array indices 0-4, so
	 * start at 5) Notice how we don't have to remember how many slots we made? We
	 * can just use CustomBoxInventory.INV_SIZE and if we ever change it, the
	 * Container updates automatically.
	 */
	private final int INV_START, INV_END, HOTBAR_START, HOTBAR_END;

	// If you're planning to add armor slots, put those first like this:
	// ARMOR_START = CustomBoxInventory.INV_SIZE, ARMOR_END = ARMOR_START+3,
	// INV_START = ARMOR_END+1, and then carry on like above.

	private Slot generateSlot(final Constructor<?> aClazz, final IInventory base, final int id, final int x,
			final int y) {
		Slot aSlot;
		try {
			aSlot = (Slot) aClazz.newInstance(base, id, x, y);
			if (aSlot != null) {
				return aSlot;
			}
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	public ContainerBoxBase(EntityPlayer par1Player, InventoryPlayer inventoryPlayer,
			CustomBoxInventory CustomBoxInventory, Class<?> aClazz, int aSlotCount) {

		INV_START = aSlotCount;
		INV_END = INV_START + 26;
		HOTBAR_START = INV_END + 1;
		HOTBAR_END = HOTBAR_START + 8;

		this.inventory = CustomBoxInventory;
		try {

			Constructor<?> constructor;
			constructor = aClazz.getConstructor(IInventory.class, int.class, int.class, int.class);

			int i;

			// ITEM INVENTORY - you'll need to adjust the slot locations to match your
			// texture file
			// I have them set vertically in columns of 4 to the right of the player model
			for (i = 0; i < CustomBoxInventory.INV_SIZE; ++i) {
				// You can make a custom Slot if you need different behavior,
				// such as only certain item types can be put into this slot
				// We made a custom slot to prevent our inventory-storing item
				// from being stored within itself, but if you want to allow that and
				// you followed my advice at the end of the above step, then you
				// could get away with using the vanilla Slot class
				this.addSlotToContainer(generateSlot(constructor, this.getInventoryObject(), i,
						80 + (18 * (int) (i / 4)), 8 + (18 * (i % 4))));
			}

			// If you want, you can add ARMOR SLOTS here as well, but you need to
			// make a public version of SlotArmor. I won't be doing that in this tutorial.
			/*
			 * for (i = 0; i < 4; ++i) { // These are the standard positions for survival
			 * inventory layout this.addSlotToContainer(new SlotArmor(this.player,
			 * inventoryPlayer, inventoryPlayer.getSizeInventory() - 1 - i, 8, 8 + i * 18,
			 * i)); }
			 */

			// PLAYER INVENTORY - uses default locations for standard inventory texture file
			for (i = 0; i < 3; ++i) {
				for (int j = 0; j < 9; ++j) {
					this.addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
				}
			}

			// PLAYER ACTION BAR - uses default locations for standard action bar texture
			// file
			for (i = 0; i < 9; ++i) {
				this.addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
			}

		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		// be sure to return the inventory's isUseableByPlayer method
		// if you defined special behavior there:
		return getInventoryObject().isUseableByPlayer(entityplayer);
	}

	/**
	 * Called when a player shift-clicks on a slot. You must override this or you
	 * will crash when someone does that.
	 */
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int index) {
		ItemStack itemstack = null;
		Slot slot = (Slot) this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			// If item is in our custom Inventory or armor slot
			if (index < INV_START) {
				// try to place in player inventory / action bar
				if (!this.mergeItemStack(itemstack1, INV_START, HOTBAR_END + 1, true)) {
					return null;
				}

				slot.onSlotChange(itemstack1, itemstack);
			}
			// Item is in inventory / hotbar, try to place in custom inventory or armor
			// slots
			else {
				/*
				 * If your inventory only stores certain instances of Items, you can implement
				 * shift-clicking to your inventory like this:
				 * 
				 * // Check that the item is the right type if (itemstack1.getItem() instanceof
				 * ItemCustom) { // Try to merge into your custom inventory slots // We use
				 * 'CustomBoxInventory.INV_SIZE' instead of INV_START just in case // you also
				 * add armor or other custom slots if (!this.mergeItemStack(itemstack1, 0,
				 * CustomBoxInventory.INV_SIZE, false)) { return null; } } // If you added armor
				 * slots, check them here as well: // Item being shift-clicked is armor - try to
				 * put in armor slot if (itemstack1.getItem() instanceof ItemArmor) { int type =
				 * ((ItemArmor) itemstack1.getItem()).armorType; if
				 * (!this.mergeItemStack(itemstack1, ARMOR_START + type, ARMOR_START + type + 1,
				 * false)) { return null; } } Otherwise, you have basically 2 choices: 1.
				 * shift-clicking between player inventory and custom inventory 2.
				 * shift-clicking between action bar and inventory
				 * 
				 * Be sure to choose only ONE of the following implementations!!!
				 */
				/**
				 * Implementation number 1: Shift-click into your custom inventory
				 */
				if (index >= INV_START) {
					// place in custom inventory
					if (!this.mergeItemStack(itemstack1, 0, INV_START, false)) {
						return null;
					}
				}

				/**
				 * Implementation number 2: Shift-click items between action bar and inventory
				 */
				// item is in player's inventory, but not in action bar
				if (index >= INV_START && index < HOTBAR_START) {
					// place in action bar
					if (!this.mergeItemStack(itemstack1, HOTBAR_START, HOTBAR_END + 1, false)) {
						return null;
					}
				}
				// item in action bar - place in player inventory
				else if (index >= HOTBAR_START && index < HOTBAR_END + 1) {
					if (!this.mergeItemStack(itemstack1, INV_START, INV_END + 1, false)) {
						return null;
					}
				}
			}

			if (itemstack1.stackSize == 0) {
				slot.putStack((ItemStack) null);
			} else {
				slot.onSlotChanged();
			}

			if (itemstack1.stackSize == itemstack.stackSize) {
				return null;
			}

			slot.onPickupFromSlot(par1EntityPlayer, itemstack1);
		}

		return itemstack;
	}

	/**
	 * You should override this method to prevent the player from moving the stack
	 * that opened the inventory, otherwise if the player moves it, the inventory
	 * will not be able to save properly
	 */
	@Override
	public ItemStack slotClick(int slot, int button, int flag, EntityPlayer player) {
		// this will prevent the player from interacting with the item that opened the
		// inventory:
		if (slot >= 0 && getSlot(slot) != null && getSlot(slot).getStack() == player.getHeldItem()) {
			return null;
		}
		return super.slotClick(slot, button, flag, player);
	}

	/*
	 * Special note: If your custom inventory's stack limit is 1 and you allow
	 * shift-clicking itemstacks into it, you will need to override mergeStackInSlot
	 * to avoid losing all the items but one in a stack when you shift-click.
	 */
	/**
	 * Vanilla mergeItemStack method doesn't correctly handle inventories whose max
	 * stack size is 1 when you shift-click into the inventory. This is a modified
	 * method I wrote to handle such cases. Note you only need it if your slot /
	 * inventory's max stack size is 1
	 */
	@Override
	protected boolean mergeItemStack(ItemStack stack, int start, int end, boolean backwards) {
		boolean flag1 = false;
		int k = (backwards ? end - 1 : start);
		Slot slot;
		ItemStack itemstack1;

		if (stack.isStackable()) {
			while (stack.stackSize > 0 && (!backwards && k < end || backwards && k >= start)) {
				slot = (Slot) inventorySlots.get(k);
				itemstack1 = slot.getStack();

				if (!slot.isItemValid(stack)) {
					k += (backwards ? -1 : 1);
					continue;
				}

				if (itemstack1 != null && itemstack1.getItem() == stack.getItem()
						&& (!stack.getHasSubtypes() || stack.getItemDamage() == itemstack1.getItemDamage())
						&& ItemStack.areItemStackTagsEqual(stack, itemstack1)) {
					int l = itemstack1.stackSize + stack.stackSize;

					if (l <= stack.getMaxStackSize() && l <= slot.getSlotStackLimit()) {
						stack.stackSize = 0;
						itemstack1.stackSize = l;
						getInventoryObject().markDirty();
						flag1 = true;
					} else if (itemstack1.stackSize < stack.getMaxStackSize() && l < slot.getSlotStackLimit()) {
						stack.stackSize -= stack.getMaxStackSize() - itemstack1.stackSize;
						itemstack1.stackSize = stack.getMaxStackSize();
						getInventoryObject().markDirty();
						flag1 = true;
					}
				}

				k += (backwards ? -1 : 1);
			}
		}
		if (stack.stackSize > 0) {
			k = (backwards ? end - 1 : start);
			while (!backwards && k < end || backwards && k >= start) {
				slot = (Slot) inventorySlots.get(k);
				itemstack1 = slot.getStack();

				if (!slot.isItemValid(stack)) {
					k += (backwards ? -1 : 1);
					continue;
				}

				if (itemstack1 == null) {
					int l = stack.stackSize;
					if (l <= slot.getSlotStackLimit()) {
						slot.putStack(stack.copy());
						stack.stackSize = 0;
						getInventoryObject().markDirty();
						flag1 = true;
						break;
					} else {
						putStackInSlot(k,
								new ItemStack(stack.getItem(), slot.getSlotStackLimit(), stack.getItemDamage()));
						stack.stackSize -= slot.getSlotStackLimit();
						getInventoryObject().markDirty();
						flag1 = true;
					}
				}

				k += (backwards ? -1 : 1);
			}
		}

		return flag1;
	}

	public CustomBoxInventory getInventoryObject() {
		return inventory;
	}
}