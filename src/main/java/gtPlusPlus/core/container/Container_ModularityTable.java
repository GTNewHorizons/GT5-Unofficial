package gtPlusPlus.core.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.inventories.modulartable.InventoryModularMain;
import gtPlusPlus.core.inventories.modulartable.InventoryModularOutput;
import gtPlusPlus.core.slots.SlotModularBauble;
import gtPlusPlus.core.slots.SlotModularBaubleUpgrades;
import gtPlusPlus.core.slots.SlotNoInput;
import gtPlusPlus.core.tileentities.machines.TileEntityModularityTable;

public class Container_ModularityTable extends Container {

	/** The crafting matrix inventory (3x3). */

	protected TileEntityModularityTable tile_entity;
	public final InventoryModularMain inventoryGrid;
	public final InventoryModularOutput inventoryOutputs;
	public int mRecipeTime;

	private final World worldObj;
	private final int posX;
	private final int posY;
	private final int posZ;

	private final int[] slotOutputs = new int[3];
	private final int[] slotGrid = new int[9];


	public Container_ModularityTable(final InventoryPlayer inventory, final TileEntityModularityTable tile){
		this.tile_entity = tile;
		this.inventoryGrid = tile.inventoryGrid;
		this.inventoryOutputs = tile.inventoryOutputs;
		this.tile_entity.setContainer(this);
		this.mRecipeTime = this.tile_entity.getRecipeTime();
		Logger.INFO("Container: "+this.mRecipeTime);

		int var6;
		int var7;
		this.worldObj = tile.getWorldObj();
		this.posX = tile.xCoord;
		this.posY = tile.yCoord;
		this.posZ = tile.zCoord;

		int nextFreeSlot = 0;


		//Output slots
		this.addSlotToContainer(new SlotModularBauble(this.inventoryOutputs, 0, 26+(18*6), 8));
		this.addSlotToContainer(new SlotModularBaubleUpgrades(this.inventoryOutputs, 1, 26+(18*5), 8));
		this.addSlotToContainer(new SlotNoInput(this.inventoryOutputs, 2, 26+(18*6), 44));   

		int o = 0;
		
		//Storage Side
				for (var6 = 0; var6 < 3; ++var6)
				{
					for (var7 = 0; var7 < 3; ++var7)
					{
						//Utils.LOG_WARNING("Adding slots at var:"+(var7 + var6 * 4)+" x:"+(8 + var7 * 18)+" y:"+(7 + var6 * 18));
						this.addSlotToContainer(new SlotModularBaubleUpgrades(this.inventoryGrid, nextFreeSlot, 8+18 + (var7 * 18), 17 + (var6 * 18)));
						this.slotGrid[o] = nextFreeSlot;
						nextFreeSlot++;
						o++;
					}
				}

		//Player Inventory
		for (var6 = 0; var6 < 3; ++var6)
		{
			for (var7 = 0; var7 < 9; ++var7)
			{
				this.addSlotToContainer(new Slot(inventory, var7 + (var6 * 9) + 9, 8 + (var7 * 18), 84 + (var6 * 18)));
			}
		}

		//Player Hotbar
		for (var6 = 0; var6 < 9; ++var6)
		{
			this.addSlotToContainer(new Slot(inventory, var6, 8 + (var6 * 18), 142));
		}

		//this.onCraftMatrixChanged(this.craftMatrix);

	}

	/**
	 * Callback for when the crafting matrix is changed.
	 */
	/* public void onCraftMatrixChanged(IInventory p_75130_1_)
    {
        this.craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.worldObj));
    }*/

	/**
	 * Called when the container is closed.
	 */
	@Override
	public void onContainerClosed(EntityPlayer p_75134_1_){
		super.onContainerClosed(p_75134_1_);
		if (!this.worldObj.isRemote){
			/*    for (int i = 0; i < 9; ++i){
                ItemStack itemstack = this.craftMatrix.getStackInSlotOnClosing(i);
                if (itemstack != null){
                    p_75134_1_.dropPlayerItemWithRandomChoice(itemstack, false);
                }
            }*/
		}
	}
	
	public TileEntityModularityTable getTileentityViaContainer(){
		if (this.tile_entity != null){
			return this.tile_entity;
		}
		return null;
	}

	@Override
	public ItemStack slotClick(final int aSlotIndex, final int aMouseclick, final int aShifthold, final EntityPlayer aPlayer){

		if (!aPlayer.worldObj.isRemote){
			if ((aSlotIndex == 999) || (aSlotIndex == -999)){
				//Utils.LOG_WARNING("??? - "+aSlotIndex);
			}

			if (aSlotIndex == 0){
				Logger.INFO("Player Clicked on the bauble slot");
				//TODO
			}
			else if (aSlotIndex == 1){
				Logger.INFO("Player Clicked on the upgrade slot");
				//TODO
			}
			else if (aSlotIndex == 2){
				Logger.INFO("Player Clicked on the output slot");
				//TODO
			}
			else {
				for (final int x : this.slotGrid){
					if (aSlotIndex == x){
						Logger.INFO("Player Clicked slot "+aSlotIndex+" in the crafting Grid");
					}
				}
			}
		}
		//Utils.LOG_WARNING("Player Clicked slot "+aSlotIndex+" in the Grid");
		return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
	}

	@Override
	public boolean canInteractWith(final EntityPlayer par1EntityPlayer){
		if (this.worldObj.getBlock(this.posX, this.posY, this.posZ) != ModBlocks.blockModularTable){
			return false;
		}

		return par1EntityPlayer.getDistanceSq(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D) <= 64D;
	}


	@Override
	public ItemStack transferStackInSlot(final EntityPlayer par1EntityPlayer, final int par2)
	{

		return null;

		/*ItemStack var3 = null;
		final Slot var4 = (Slot)this.inventorySlots.get(par2);

		if ((var4 != null) && var4.getHasStack())
		{
			final ItemStack var5 = var4.getStack();
			var3 = var5.copy();

			if (par2 == 0)
			{
				if (!this.mergeItemStack(var5, InOutputSlotNumber, FullSlotNumber, true))
				{
					return null;
				}

				var4.onSlotChange(var5, var3);
			}
			else if ((par2 >= InOutputSlotNumber) && (par2 < InventoryOutSlotNumber))
			{
				if (!this.mergeItemStack(var5, InventoryOutSlotNumber, FullSlotNumber, false))
				{
					return null;
				}
			}
			else if ((par2 >= InventoryOutSlotNumber) && (par2 < FullSlotNumber))
			{
				if (!this.mergeItemStack(var5, InOutputSlotNumber, InventoryOutSlotNumber, false))
				{
					return null;
				}
			}
			else if (!this.mergeItemStack(var5, InOutputSlotNumber, FullSlotNumber, false))
			{
				return null;
			}

			if (var5.stackSize == 0)
			{
				var4.putStack((ItemStack)null);
			}
			else
			{
				var4.onSlotChanged();
			}

			if (var5.stackSize == var3.stackSize)
			{
				return null;
			}

			var4.onPickupFromSlot(par1EntityPlayer, var5);
		}

		return var3;*/
	}

	//Can merge Slot
	/*public boolean func_94530_a(ItemStack p_94530_1_, Slot p_94530_2_){
        return p_94530_2_.inventory != this.craftResult && super.func_94530_a(p_94530_1_, p_94530_2_);
    }*/

	/*public ItemStack getOutputContent(){
		ItemStack output = this.craftResult.getStackInSlot(0);
		if (output != null){
			return output;
		}
		return null;		
	}

	public ItemStack[] getInputComponents(){
		ItemStack inputs[] = new ItemStack[9];
		for (int r=0;r<this.craftMatrix.getSizeInventory();r++){
			ItemStack temp = this.craftMatrix.getStackInSlot(r);
			inputs[r] = temp;
		}
		return inputs;		
	}*/


}