package gtPlusPlus.core.container;

import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.inventories.*;
import gtPlusPlus.core.tileentities.general.TileEntityFishTrap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class Container_FishTrap extends Container {

	protected TileEntityFishTrap tile_entity;
	public final InventoryFishTrap inventoryChest;

	private World worldObj;
	private int posX;
	private int posY;
	private int posZ;


	public static int StorageSlotNumber = 16; //Number of slots in storage area
	public static int InventorySlotNumber = 36; //Inventory Slots (Inventory and Hotbar)
	public static int FullSlotNumber = InventorySlotNumber + StorageSlotNumber; //All slots

	private int[] slotStorage = new int[16];

	public Container_FishTrap(InventoryPlayer inventory, TileEntityFishTrap te){
		this.tile_entity = te;
		this.inventoryChest = te.getInventory();

		int var6;
		int var7;
		worldObj = te.getWorldObj();
		posX = te.xCoord;
		posY = te.yCoord;
		posZ = te.zCoord;

		int o=0;

		//Storage Side
		for (var6 = 0; var6 < 4; ++var6)
		{
			for (var7 = 0; var7 < 4; ++var7)
			{
				//Utils.LOG_WARNING("Adding slots at var:"+(var7 + var6 * 4)+" x:"+(8 + var7 * 18)+" y:"+(7 + var6 * 18));
				this.addSlotToContainer(new Slot(inventoryChest, var7 + var6 * 4, 8 + var7 * 18, 7 + var6 * 18));
				slotStorage[o] = o;
				o++;
			}
		}

		o=0;

		//Player Inventory
		for (var6 = 0; var6 < 3; ++var6)
		{
			for (var7 = 0; var7 < 9; ++var7)
			{
				this.addSlotToContainer(new Slot(inventory, var7 + var6 * 9 + 9, 8 + var7 * 18, 84 + var6 * 18));
			}
		}

		//Player Hotbar
		for (var6 = 0; var6 < 9; ++var6)
		{
			this.addSlotToContainer(new Slot(inventory, var6, 8 + var6 * 18, 142));
		}         

	}

	@Override
	public ItemStack slotClick(int aSlotIndex, int aMouseclick, int aShifthold, EntityPlayer aPlayer){

		if (!aPlayer.worldObj.isRemote){
			if (aSlotIndex == 999 || aSlotIndex == -999){
				//Utils.LOG_WARNING("??? - "+aSlotIndex);
			}
		}
		return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
	}

	



	@Override
	public void onContainerClosed(EntityPlayer par1EntityPlayer){
		super.onContainerClosed(par1EntityPlayer);
	}


	@Override
	public boolean canInteractWith(EntityPlayer par1EntityPlayer){
		if (worldObj.getBlock(posX, posY, posZ) != ModBlocks.blockFishTrap){
			return false;
		}

		return par1EntityPlayer.getDistanceSq((double)posX + 0.5D, (double)posY + 0.5D, (double)posZ + 0.5D) <= 64D;
	}


	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
	{
		ItemStack var3 = null;
		Slot var4 = (Slot)this.inventorySlots.get(par2);

		if (var4 != null && var4.getHasStack())
		{
			ItemStack var5 = var4.getStack();
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
	public boolean func_94530_a(ItemStack p_94530_1_, Slot p_94530_2_) {
		return super.func_94530_a(p_94530_1_, p_94530_2_);
	}


}