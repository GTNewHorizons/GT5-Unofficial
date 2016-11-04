package gtPlusPlus.core.slots;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;

public class SlotOutput extends SlotCrafting{

	private final IInventory craftMatrix;
	private final EntityPlayer thePlayer;
	private int amountCrafted;


	public SlotOutput(EntityPlayer player, InventoryCrafting craftingInventory, IInventory p_i45790_3_, int slotIndex, int xPosition, int yPosition)
	{
		super(player, craftingInventory, p_i45790_3_, slotIndex, xPosition, yPosition);
		this.thePlayer = player;
		this.craftMatrix = craftingInventory;
	}
	/**
	 * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
	 */
	@Override
	public boolean isItemValid(ItemStack par1ItemStack)
	{
		return false;
	}
	/**
	 * Decrease the size of the stack in slot (first int arg) by the amount of the second int arg. Returns the new
	 * stack.
	 */
	@Override
	public ItemStack decrStackSize(int par1)
	{
		if (this.getHasStack())
		{
			this.amountCrafted += Math.min(par1, this.getStack().stackSize);
		}
		return super.decrStackSize(par1);
	}
	/**
	 * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood. Typically increases an
	 * internal count then calls onCrafting(item).
	 */
	@Override
	protected void onCrafting(ItemStack par1ItemStack, int par2)
	{
		this.amountCrafted += par2;
		this.onCrafting(par1ItemStack);
	}
	/**
	 * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood.
	 */
	@Override
	protected void onCrafting(ItemStack stack)
	{
		stack.onCrafting(this.thePlayer.worldObj, this.thePlayer, this.amountCrafted);
		this.amountCrafted = 0;
	}

	@Override
	public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack)
	{
		{
			FMLCommonHandler.instance().firePlayerCraftingEvent(playerIn, stack, craftMatrix);
			this.onCrafting(stack);
			for (int i = 0; i < this.craftMatrix.getSizeInventory(); ++i)
			{
				ItemStack itemstack1 = this.craftMatrix.getStackInSlot(i);
				if (itemstack1 != null)
				{
					this.craftMatrix.decrStackSize(i, 1);
					if (itemstack1.getItem().hasContainerItem(itemstack1))
					{
						ItemStack itemstack2 = itemstack1.getItem().getContainerItem(itemstack1);
						if (itemstack2.isItemStackDamageable() && itemstack2.getItemDamage() > itemstack2.getMaxDamage())
						{
							MinecraftForge.EVENT_BUS.post(new PlayerDestroyItemEvent(thePlayer, itemstack2));
							itemstack2 = null;
						}
						if (!this.thePlayer.inventory.addItemStackToInventory(itemstack2))
						{
							if (this.craftMatrix.getStackInSlot(i) == null)
							{
								this.craftMatrix.setInventorySlotContents(i, itemstack2);
							}
							else
							{
								this.thePlayer.dropPlayerItemWithRandomChoice(itemstack2, false);
							}
						}
					}
				}
			}
		}
	}
}