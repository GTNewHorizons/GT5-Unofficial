package gtPlusPlus.xmod.gregtech.common.helpers.autocrafter;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.world.World;

import gtPlusPlus.api.objects.Logger;

public class AC_Helper_Container extends Container
{
	/** The crafting matrix inventory (3x3). */
	public InventoryCrafting craftMatrix = new InventoryCrafting(this, 3, 3);
	public IInventory craftResult = new InventoryCraftResult();
	private World worldObj;
	
	public InventoryCrafting getMatrix(){
		return this.craftMatrix;
	}
	
	public boolean putItemsIntoGrid(ItemStack[] inputs){
		if (inputs.length < 9){
			return false;
		}
		for (int i=0;i<9;i++){
			this.putStackInSlot(i, inputs[i]);
		}
		this.onCraftMatrixChanged(this.craftMatrix);
		return true;
	}
	
	public AC_Helper_Container(InventoryPlayer playerInventory, World world, int x, int y, int z)
	{
		this.worldObj = world;
		this.addSlotToContainer(new SlotCrafting(playerInventory.player, this.craftMatrix, this.craftResult, 0, 124, 35));
		int l;
		int i1;

		for (l = 0; l < 3; ++l)
		{
			for (i1 = 0; i1 < 3; ++i1)
			{
				this.addSlotToContainer(new Slot(this.craftMatrix, i1 + l * 3, 30 + i1 * 18, 17 + l * 18));
			}
		}

		for (l = 0; l < 3; ++l)
		{
			for (i1 = 0; i1 < 9; ++i1)
			{
				this.addSlotToContainer(new Slot(playerInventory, i1 + l * 9 + 9, 8 + i1 * 18, 84 + l * 18));
			}
		}

		for (l = 0; l < 9; ++l)
		{
			this.addSlotToContainer(new Slot(playerInventory, l, 8 + l * 18, 142));
		}

		this.onCraftMatrixChanged(this.craftMatrix);
	}

	/**
	 * Callback for when the crafting matrix is changed.
	 */
	@Override
	public void onCraftMatrixChanged(IInventory p_75130_1_)
	{
		this.craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.worldObj));
		Logger.INFO("Crafted "+this.craftResult.getStackInSlot(0));
	
	}

	/**
	 * Called when the container is closed.
	 */
	@Override
	public void onContainerClosed(EntityPlayer p_75134_1_)
	{
		super.onContainerClosed(p_75134_1_);

		if (!this.worldObj.isRemote)
		{
			for (int i = 0; i < 9; ++i)
			{
				ItemStack itemstack = this.craftMatrix.getStackInSlotOnClosing(i);

				if (itemstack != null)
				{
					p_75134_1_.dropPlayerItemWithRandomChoice(itemstack, false);
				}
			}
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer p_75145_1_)
	{
		return true;
	}

	/**
	 * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
	 */
	@Override
	public ItemStack transferStackInSlot(EntityPlayer p_82846_1_, int p_82846_2_){
		ItemStack itemstack = null;
		return itemstack;
	}

	@Override
	public boolean func_94530_a(ItemStack p_94530_1_, Slot p_94530_2_)
	{
		return p_94530_2_.inventory != this.craftResult && super.func_94530_a(p_94530_1_, p_94530_2_);
	}
}