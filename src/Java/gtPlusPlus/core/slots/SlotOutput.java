package gtPlusPlus.core.slots;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;

public class SlotOutput extends SlotCrafting {

	private final IInventory	craftMatrix;
	private final EntityPlayer	thePlayer;
	private int					amountCrafted;

	public SlotOutput(final EntityPlayer player, final InventoryCrafting craftingInventory,
			final IInventory p_i45790_3_, final int slotIndex, final int xPosition, final int yPosition) {
		super(player, craftingInventory, p_i45790_3_, slotIndex, xPosition, yPosition);
		this.thePlayer = player;
		this.craftMatrix = craftingInventory;
	}

	/**
	 * Decrease the size of the stack in slot (first int arg) by the amount of
	 * the second int arg. Returns the new stack.
	 */
	@Override
	public ItemStack decrStackSize(final int par1) {
		if (this.getHasStack()) {
			this.amountCrafted += Math.min(par1, this.getStack().stackSize);
		}
		return super.decrStackSize(par1);
	}

	/**
	 * Check if the stack is a valid item for this slot. Always true beside for
	 * the armor slots.
	 */
	@Override
	public boolean isItemValid(final ItemStack par1ItemStack) {
		return false;
	}

	/**
	 * the itemStack passed in is the output - ie, iron ingots, and pickaxes,
	 * not ore and wood.
	 */
	@Override
	protected void onCrafting(final ItemStack stack) {
		stack.onCrafting(this.thePlayer.worldObj, this.thePlayer, this.amountCrafted);
		this.amountCrafted = 0;
	}

	/**
	 * the itemStack passed in is the output - ie, iron ingots, and pickaxes,
	 * not ore and wood. Typically increases an internal count then calls
	 * onCrafting(item).
	 */
	@Override
	protected void onCrafting(final ItemStack par1ItemStack, final int par2) {
		this.amountCrafted += par2;
		this.onCrafting(par1ItemStack);
	}

	@Override
	public void onPickupFromSlot(final EntityPlayer playerIn, final ItemStack stack) {
		{
			FMLCommonHandler.instance().firePlayerCraftingEvent(playerIn, stack, this.craftMatrix);
			this.onCrafting(stack);
			for (int i = 0; i < this.craftMatrix.getSizeInventory(); ++i) {
				final ItemStack itemstack1 = this.craftMatrix.getStackInSlot(i);
				if (itemstack1 != null) {
					this.craftMatrix.decrStackSize(i, 1);
					if (itemstack1.getItem().hasContainerItem(itemstack1)) {
						ItemStack itemstack2 = itemstack1.getItem().getContainerItem(itemstack1);
						if (itemstack2.isItemStackDamageable()
								&& itemstack2.getItemDamage() > itemstack2.getMaxDamage()) {
							MinecraftForge.EVENT_BUS.post(new PlayerDestroyItemEvent(this.thePlayer, itemstack2));
							itemstack2 = null;
						}
						if (!this.thePlayer.inventory.addItemStackToInventory(itemstack2)) {
							if (this.craftMatrix.getStackInSlot(i) == null) {
								this.craftMatrix.setInventorySlotContents(i, itemstack2);
							}
							else {
								this.thePlayer.dropPlayerItemWithRandomChoice(itemstack2, false);
							}
						}
					}
				}
			}
		}
	}
}