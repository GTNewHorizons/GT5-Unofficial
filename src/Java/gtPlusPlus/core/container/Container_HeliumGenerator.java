package gtPlusPlus.core.container;

import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.inventories.InventoryHeliumGenerator;
import gtPlusPlus.core.slots.SlotFuelRod;
import gtPlusPlus.core.slots.SlotNoInput;
import gtPlusPlus.core.tileentities.general.TileEntityHeliumGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class Container_HeliumGenerator extends Container {

	protected TileEntityHeliumGenerator tile_entity;
	public final InventoryHeliumGenerator inventoryChest;

	private final World worldObj;
	private final int posX;
	private final int posY;
	private final int posZ;


	public static int StorageSlotNumber = 18; //Number of slots in storage area
	public static int InventorySlotNumber = 36; //Inventory Slots (Inventory and Hotbar)
	public static int FullSlotNumber = InventorySlotNumber + StorageSlotNumber; //All slots

	private final int[] slotStorage = new int[15];

	public Container_HeliumGenerator(final InventoryPlayer inventory, final TileEntityHeliumGenerator te){
		this.tile_entity = te;
		this.inventoryChest = te.getInventory();

		int var6;
		int var7;
		this.worldObj = te.getWorldObj();
		this.posX = te.xCoord;
		this.posY = te.yCoord;
		this.posZ = te.zCoord;

		int o=0;
		
		//Output
		this.addSlotToContainer(new SlotNoInput(this.inventoryChest, 0, 80, 35));
		this.slotStorage[o] = o;
		o++;

		//Side A
		for (var6 = 0; var6 < 3; ++var6)
		{
			for (var7 = 0; var7 < 3; ++var7)
			{
				this.addSlotToContainer(new SlotFuelRod(this.inventoryChest, var7 + (var6 * 4), 36+ 8 + (var7 * 18), 8 + 7 + (var6 * 18)));
				this.slotStorage[o] = o;
				o++;
			}
		}

		//Side B
		for (var6 = 0; var6 < 3; ++var6)
		{
			for (var7 = 0; var7 < 3; ++var7)
			{
				this.addSlotToContainer(new SlotFuelRod(this.inventoryChest, var7 + (var6 * 4), 90 + 8 + (var7 * 18), 8 + 7 + (var6 * 18)));
				this.slotStorage[o] = o;
				o++;
			}
		}

		o=0;

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

	}

	@Override
	public ItemStack slotClick(final int aSlotIndex, final int aMouseclick, final int aShifthold, final EntityPlayer aPlayer){

		if (!aPlayer.worldObj.isRemote){
			if ((aSlotIndex == 999) || (aSlotIndex == -999)){
				//Utils.LOG_WARNING("??? - "+aSlotIndex);
			}
		}
		return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
	}





	@Override
	public void onContainerClosed(final EntityPlayer par1EntityPlayer){
		super.onContainerClosed(par1EntityPlayer);
	}


	@Override
	public boolean canInteractWith(final EntityPlayer par1EntityPlayer){
		if (this.worldObj.getBlock(this.posX, this.posY, this.posZ) != ModBlocks.blockHeliumGenerator){
			return false;
		}

		return par1EntityPlayer.getDistanceSq(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D) <= 64D;
	}


	@Override
	public ItemStack transferStackInSlot(final EntityPlayer par1EntityPlayer, final int par2)
	{
		ItemStack var3 = null;
		final Slot var4 = (Slot)this.inventorySlots.get(par2);

		if ((var4 != null) && var4.getHasStack())
		{
			final ItemStack var5 = var4.getStack();
			var3 = var5.copy();

			/*if (par2 == 0)
			{
				if (!this.mergeItemStack(var5, InOutputSlotNumber, FullSlotNumber, true))
				{
					return null;
				}

				var4.onSlotChange(var5, var3);
			}
			else if (par2 >= InOutputSlotNumber && par2 < InventoryOutSlotNumber)
			{
				if (!this.mergeItemStack(var5, InventoryOutSlotNumber, FullSlotNumber, false))
				{
					return null;
				}
			}
			else if (par2 >= InventoryOutSlotNumber && par2 < FullSlotNumber)
			{
				if (!this.mergeItemStack(var5, InOutputSlotNumber, InventoryOutSlotNumber, false))
				{
					return null;
				}
			}
			else if (!this.mergeItemStack(var5, InOutputSlotNumber, FullSlotNumber, false))
			{
				return null;
			}*/

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

		return var3;
	}

	//Can merge Slot
	@Override
	public boolean func_94530_a(final ItemStack p_94530_1_, final Slot p_94530_2_) {
		return super.func_94530_a(p_94530_1_, p_94530_2_);
	}


}