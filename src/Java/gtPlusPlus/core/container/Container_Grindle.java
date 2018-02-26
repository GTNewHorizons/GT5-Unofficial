package gtPlusPlus.core.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import gtPlusPlus.core.inventories.BaseInventoryGrindle;
import gtPlusPlus.core.slots.SlotDataStick;

public class Container_Grindle extends Container
{
	/** The Item Inventory for this Container, only needed if you want to reference isUseableByPlayer */
	public final BaseInventoryGrindle inventory;

	/** Using these will make transferStackInSlot easier to understand and implement
	 * INV_START is the index of the first slot in the Player's Inventory, so our
	 * BaseInventoryBackpack's number of slots (e.g. 5 slots is array indices 0-4, so start at 5)
	 * Notice how we don't have to remember how many slots we made? We can just use
	 * BaseInventoryBackpack.INV_SIZE and if we ever change it, the Container updates automatically. */
	private static final int INV_START = BaseInventoryGrindle.INV_SIZE, INV_END = INV_START+0,
			HOTBAR_START = INV_END, HOTBAR_END = HOTBAR_START+8;

	// If you're planning to add armor slots, put those first like this:
	// ARMOR_START = BaseInventoryBackpack.INV_SIZE, ARMOR_END = ARMOR_START+3,
	// INV_START = ARMOR_END+1, and then carry on like above.

	public Container_Grindle(final EntityPlayer par1Player, final InventoryPlayer inventoryPlayer, final BaseInventoryGrindle inventoryItem)
	{
		this.inventory = inventoryItem;

		int i;
		
		//Actual Scan Slot
		this.addSlotToContainer(new SlotDataStick(this.inventory, 0, 152, 5));
		
		for (i = 1; i < BaseInventoryGrindle.INV_SIZE; ++i){
			this.addSlotToContainer(new SlotDataStick(this.inventory, i, 153, 30+(18*i)));
		}

		// PLAYER ACTION BAR - uses default locations for standard action bar texture file
		for (i = 0; i < 9; ++i)
		{
			this.addSlotToContainer(new Slot(inventoryPlayer, i, 8 + (i * 18), 142));
		}
	}

	@Override
	public boolean canInteractWith(final EntityPlayer entityplayer)
	{
		// be sure to return the inventory's isUseableByPlayer method
		// if you defined special behavior there:
		return this.inventory.isUseableByPlayer(entityplayer);
	}

	/**
	 * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
	 */
	@Override
	public ItemStack transferStackInSlot(final EntityPlayer par1EntityPlayer, final int index)
	{
		ItemStack itemstack = null;
		final Slot slot = (Slot) this.inventorySlots.get(index);

		if ((slot != null) && slot.getHasStack())
		{
			final ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			// If item is in our custom Inventory or armor slot
			if (index < INV_START)
			{
				// try to place in player inventory / action bar
				if (!this.mergeItemStack(itemstack1, INV_START, HOTBAR_END+1, true))
				{
					return null;
				}

				slot.onSlotChange(itemstack1, itemstack);
			}
			// Item is in inventory / hotbar, try to place in custom inventory or armor slots
			else
			{
				/*
				If your inventory only stores certain instances of Items,
				you can implement shift-clicking to your inventory like this:

				// Check that the item is the right type
				if (itemstack1.getItem() instanceof ItemCustom)
				{
					// Try to merge into your custom inventory slots
					// We use 'BaseInventoryBackpack.INV_SIZE' instead of INV_START just in case
					// you also add armor or other custom slots
					if (!this.mergeItemStack(itemstack1, 0, BaseInventoryBackpack.INV_SIZE, false))
					{
						return null;
					}
				}
				// If you added armor slots, check them here as well:
				// Item being shift-clicked is armor - try to put in armor slot
				if (itemstack1.getItem() instanceof ItemArmor)
				{
					int type = ((ItemArmor) itemstack1.getItem()).armorType;
					if (!this.mergeItemStack(itemstack1, ARMOR_START + type, ARMOR_START + type + 1, false))
					{
						return null;
					}
				}
				Otherwise, you have basically 2 choices:
				1. shift-clicking between player inventory and custom inventory
				2. shift-clicking between action bar and inventory

				Be sure to choose only ONE of the following implementations!!!
				 */
				/**
				 * Implementation number 1: Shift-click into your custom inventory
				 */
				if (index >= INV_START)
				{
					// place in custom inventory
					if (!this.mergeItemStack(itemstack1, 0, INV_START, false))
					{
						return null;
					}
				}

				/**
				 * Implementation number 2: Shift-click items between action bar and inventory
				 */
				// item is in player's inventory, but not in action bar
				if ((index >= INV_START) && (index < HOTBAR_START))
				{
					// place in action bar
					if (!this.mergeItemStack(itemstack1, HOTBAR_START, HOTBAR_END+1, false))
					{
						return null;
					}
				}
				// item in action bar - place in player inventory
				else if ((index >= HOTBAR_START) && (index < (HOTBAR_END+1)))
				{
					if (!this.mergeItemStack(itemstack1, INV_START, INV_END+1, false))
					{
						return null;
					}
				}
			}

			if (itemstack1.stackSize == 0)
			{
				slot.putStack((ItemStack) null);
			}
			else
			{
				slot.onSlotChanged();
			}

			if (itemstack1.stackSize == itemstack.stackSize)
			{
				return null;
			}

			slot.onPickupFromSlot(par1EntityPlayer, itemstack1);
		}

		return itemstack;
	}

	/**
	 * You should override this method to prevent the player from moving the stack that
	 * opened the inventory, otherwise if the player moves it, the inventory will not
	 * be able to save properly
	 */
	@Override
	public ItemStack slotClick(final int slot, final int button, final int flag, final EntityPlayer player) {
		// this will prevent the player from interacting with the item that opened the inventory:
		if ((slot >= 0) && (this.getSlot(slot) != null) && (this.getSlot(slot).getStack() == player.getHeldItem())) {
			return null;
		}
		return super.slotClick(slot, button, flag, player);
	}
}
