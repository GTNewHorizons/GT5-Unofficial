package miscutil.xmod.ic2.block.RTGGenerator.gui;

import miscutil.core.lib.CORE;
import miscutil.core.slots.SlotRTG;
import miscutil.xmod.ic2.block.RTGGenerator.TileEntityRTG;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import forestry.api.apiculture.IHiveFrame;
import forestry.core.gui.tooltips.ToolTip;

public class CONTAINER_RadioThermalGenerator extends Container
{
	private TileEntityRTG te;

	public static final int INPUT_1 = 0;
	private final ResourceLocation beeFrameIcon = new ResourceLocation(CORE.MODID, "textures/items/machine_Charger.png");
	public ToolTip newTip = new ToolTip();
	public final SlotRTG rtgSlot;

	private int slotID = 0;

	public CONTAINER_RadioThermalGenerator(TileEntityRTG te, EntityPlayer player)
	{
		this.te = te;
		this.rtgSlot = new SlotRTG(te, slotID++, 80, 35);

		//Fuel Slot A
		rtgSlot.setBackgroundIconTexture(beeFrameIcon);
		
		addSlotToContainer(rtgSlot);

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
			
			
			//If your inventory only stores certain instances of Items,
			//you can implement shift-clicking to your inventory like this:
			// Check that the item is the right type
			if (!(stack.getItem() instanceof IHiveFrame)){
			return null;
			}

			if (slotRaw < 1)
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