package gtPlusPlus.xmod.forestry.bees.alveary.gui;

import forestry.api.apiculture.IHiveFrame;
import forestry.core.gui.tooltips.ToolTip;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.slots.SlotFrame;
import gtPlusPlus.xmod.forestry.bees.alveary.TileAlvearyFrameHousing;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class CONTAINER_FrameHousing extends Container
{
	private final TileAlvearyFrameHousing te;

	public static final int INPUT_1 = 0;
	private final ResourceLocation beeFrameIcon = new ResourceLocation(CORE.MODID, "textures/items/machine_Charger.png");
	public ToolTip newTip = new ToolTip();
	private final SlotFrame beeFrameSlot;

	private int slotID = 0;

	public CONTAINER_FrameHousing(final TileAlvearyFrameHousing te, final EntityPlayer player)
	{
		this.te = te;
		this.beeFrameSlot = new SlotFrame(te, this.slotID++, 80, 35);

		//Fuel Slot A
		this.beeFrameSlot.setBackgroundIconTexture(this.beeFrameIcon);

		this.addSlotToContainer(this.beeFrameSlot);

		//Inventory
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 9; j++)
			{
				this.addSlotToContainer(new Slot(player.inventory, j + (i * 9) + 9, 8 + (j * 18), 84 + (i * 18)));
			}
		}
		// Hotbar
		for (int i = 0; i < 9; i++)
		{
			this.addSlotToContainer(new Slot(player.inventory, i, 8 + (i * 18), 142));
		}
	}

	@Override
	public ItemStack transferStackInSlot(final EntityPlayer player, final int slotRaw)
	{
		ItemStack stack = null;
		final Slot slot = (Slot)this.inventorySlots.get(slotRaw);

		if ((slot != null) && slot.getHasStack())
		{
			final ItemStack stackInSlot = slot.getStack();
			stack = stackInSlot.copy();


			//If your inventory only stores certain instances of Items,
			//you can implement shift-clicking to your inventory like this:
			// Check that the item is the right type
			if (!(stack.getItem() instanceof IHiveFrame)){
				return null;
			}

			if (slotRaw < 1)
			{
				if (!this.mergeItemStack(stackInSlot, 3 * 9, this.inventorySlots.size(), true))
				{
					return null;
				}
			}
			else if (!this.mergeItemStack(stackInSlot, 0, 3 * 9, false))
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
	public boolean canInteractWith(final EntityPlayer player)
	{
		return this.te.isUseableByPlayer(player);
	}
}