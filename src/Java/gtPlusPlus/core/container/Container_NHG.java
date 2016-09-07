package gtPlusPlus.core.container;

import gtPlusPlus.core.tileentities.machines.TileEntityNHG;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;

public class Container_NHG extends Container
{
	private TileEntityNHG te;

	public static final int INPUT_1 = 0, INPUT_2 = 1, INPUT_3 = 2,
			INPUT_4 = 3, INPUT_5 = 4, INPUT_6 = 5,
			INPUT_7 = 6, INPUT_8 = 7, INPUT_9 = 8, 
			INPUT_10 = 9, INPUT_11 = 10, INPUT_12 = 11,
			INPUT_13 = 12, INPUT_14 = 13, INPUT_15 = 14,
			INPUT_16 = 15, INPUT_17 = 16, INPUT_18 = 17,
			OUTPUT = 18;
	
	private int slotID = 0;

	public Container_NHG(TileEntityNHG te, EntityPlayer player)
	{
		this.te = te;


		//Fuel Rods A
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				addSlotToContainer(new Slot(te, slotID++, 8 + j * 18, 17 + i * 18));
			}
		}
		//Fuel Rods B
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				addSlotToContainer(new Slot(te, slotID++, 116 + j * 18, 17 + i * 18));
			}
		}

		//Output
		addSlotToContainer(new SlotFurnace(player, te, OUTPUT, 80, 53));

		//Inventory
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 9; j++)
			{
				addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}
		// Hotbar
		for (int i = 0; i < 9; i++)
		{
			addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18, 142));
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotRaw)
	{
		ItemStack stack = null;
		Slot slot = (Slot)inventorySlots.get(slotRaw);

		if (slot != null && slot.getHasStack())
		{
			ItemStack stackInSlot = slot.getStack();
			stack = stackInSlot.copy();

			if (slotRaw < 3 * 9)
			{
				if (!mergeItemStack(stackInSlot, 3 * 9, inventorySlots.size(), true))
				{
					return null;
				}
			}
			else if (!mergeItemStack(stackInSlot, 0, 3 * 9, false))
			{
				return null;
			}

			if (stackInSlot.stackSize == 0)
			{
				slot.putStack((ItemStack)null);
			}
			else
			{
				slot.onSlotChanged();
			}
		}
		return stack;
	}

	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return te.isUseableByPlayer(player);
	}
}