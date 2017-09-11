package gtPlusPlus.core.container;

import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.inventories.projecttable.InventoryProjectMain;
import gtPlusPlus.core.inventories.projecttable.InventoryProjectOutput;
import gtPlusPlus.core.slots.*;
import gtPlusPlus.core.tileentities.machines.TileEntityProjectTable;
import gtPlusPlus.core.util.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class Container_ProjectTable extends Container {

	protected TileEntityProjectTable tile_entity;
	public final InventoryProjectMain inventoryGrid;
	public final InventoryProjectOutput inventoryOutputs;

	private final World worldObj;
	private final int posX;
	private final int posY;
	private final int posZ;

	public static int StorageSlotNumber = 9; //Number of slots in storage area
	public static int InOutputSlotNumber = StorageSlotNumber; //Same plus Output Slot
	public static int InventorySlotNumber = 36; //Inventory Slots (Inventory and Hotbar)
	public static int InventoryOutSlotNumber = InventorySlotNumber + 1; //Inventory Slot Number + Output
	public static int FullSlotNumber = InventorySlotNumber + InOutputSlotNumber; //All slots

	private final int[] slotOutputs = new int[2];
	private final int[] slotGrid = new int[9];


	public Container_ProjectTable(final InventoryPlayer inventory, final TileEntityProjectTable tile){
		this.tile_entity = tile;
		this.inventoryGrid = tile.inventoryGrid;
		this.inventoryOutputs = tile.inventoryOutputs;

		int var6;
		int var7;
		this.worldObj = tile.getWorldObj();
		this.posX = tile.xCoord;
		this.posY = tile.yCoord;
		this.posZ = tile.zCoord;

		int o=0;

		//Output slots
		this.addSlotToContainer(new SlotDataStick(this.inventoryOutputs, 0, 136, 64));
		this.addSlotToContainer(new SlotNoInput(this.inventoryOutputs, 1, 136, 64));
		
		for (int i=1; i<2; i++){
			this.slotOutputs[o] = i;
			o++;
		}
	

		o=0;

		//Storage Side
		for (var6 = 0; var6 < 3; ++var6)
		{
			for (var7 = 0; var7 < 3; ++var7)
			{
				//Utils.LOG_WARNING("Adding slots at var:"+(var7 + var6 * 4)+" x:"+(8 + var7 * 18)+" y:"+(7 + var6 * 18));
				this.addSlotToContainer(new Slot(this.inventoryGrid, var7 + (var6 * 4), 8 + (var7 * 18), 7 + (var6 * 18)));
				this.slotGrid[o] = o+2;
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

			if (aSlotIndex == 0){
				Utils.LOG_WARNING("Player Clicked on the Data Stick slot");
				//TODO
			}if (aSlotIndex == 1){
				Utils.LOG_WARNING("Player Clicked on the output slot");
				//TODO
			}

			for (final int x : this.slotGrid){
				if (aSlotIndex == x){
					Utils.LOG_WARNING("Player Clicked slot "+aSlotIndex+" in the crafting Grid");
				}
			}			
		}
		//Utils.LOG_WARNING("Player Clicked slot "+aSlotIndex+" in the Grid");
		return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
	}
	
	@Override
	public boolean canInteractWith(final EntityPlayer par1EntityPlayer){
		if (this.worldObj.getBlock(this.posX, this.posY, this.posZ) != ModBlocks.blockWorkbench){
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

		return var3;
	}

	//Can merge Slot
	@Override
	public boolean func_94530_a(final ItemStack p_94530_1_, final Slot p_94530_2_) {
		return super.func_94530_a(p_94530_1_, p_94530_2_);
	}


}