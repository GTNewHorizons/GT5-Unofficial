package gtPlusPlus.core.container;

import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.handler.workbench.Workbench_CraftingHandler;
import gtPlusPlus.core.inventories.InventoryWorkbenchChest;
import gtPlusPlus.core.inventories.InventoryWorkbenchTools;
import gtPlusPlus.core.slots.SlotGtTool;
import gtPlusPlus.core.tileentities.machines.TileEntityWorkbench;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class Container_Workbench extends Container {

	protected TileEntityWorkbench tile_entity;
	public InventoryCrafting craftMatrix = new InventoryCrafting(this, 3, 3);
	public final InventoryWorkbenchChest inventoryChest;
	public final InventoryWorkbenchTools inventoryTool;
	public IInventory craftResult = new InventoryCraftResult();
	private World worldObj;
	private int posX;
	private int posY;
	private int posZ;

	public static int StorageSlotNumber = 12; //Number of slots in storage area
	public static int ToolSlotNumber = 5; // Number of slots in the tool area up top
	public static int InputSlotNumber = 9; //Number of Slots in the Crafting Grid
	public static int InOutputSlotNumber = InputSlotNumber + 1; //Same plus Output Slot
	public static int InventorySlotNumber = 36; //Inventory Slots (Inventory and Hotbar)
	public static int InventoryOutSlotNumber = InventorySlotNumber + 1; //Inventory Slot Number + Output
	public static int FullSlotNumber = InventorySlotNumber + InOutputSlotNumber; //All slots



	public Container_Workbench(InventoryPlayer inventory, TileEntityWorkbench tile){

		this.tile_entity = tile;
		this.inventoryChest = tile.inventoryChest;
		this.inventoryTool = tile.inventoryTool;

		int o=0;

		int var6;
		int var7;
		worldObj = tile.getWorldObj();
		posX = tile.xCoord;
		posY = tile.yCoord;
		posZ = tile.zCoord;

		//Output slot
		addSlotToContainer(new SlotCrafting(inventory.player, this.craftMatrix, craftResult, 0, 136, 64));

		//Crafting Grid
		for (var6 = 0; var6 < 3; ++var6)
		{
			for (var7 = 0; var7 < 3; ++var7)
			{
				this.addSlotToContainer(new Slot(this.craftMatrix, var7 + var6 * 3, 82 + var7 * 18, 28 + var6 * 18));
			}
		}            
		
		//Storage Side
		for (var6 = 0; var6 < 4; ++var6)
		{
			for (var7 = 0; var7 < 4; ++var7)
			{
				this.addSlotToContainer(new Slot(inventoryChest, var7 + var6 * 3, 8 + var7 * 18, 7 + var6 * 18));
			}
		}
		
		//Tool Slots
		for (var6 = 0; var6 < 1; ++var6)
		{
			for (var7 = 0; var7 < 5; ++var7)
			{
				this.addSlotToContainer(new SlotGtTool(inventoryTool, var7 + var6 * 3, 82 + var7 * 18, 8 + var6 * 18));
			}
		} 

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

		this.onCraftMatrixChanged(this.craftMatrix);          

	}


	@Override
	public void onCraftMatrixChanged(IInventory par1IInventory){
		craftResult.setInventorySlotContents(0, Workbench_CraftingHandler.getInstance().findMatchingRecipe(craftMatrix, worldObj));
	}


	@Override
	public void onContainerClosed(EntityPlayer par1EntityPlayer)
	{

		super.onContainerClosed(par1EntityPlayer);

		if (worldObj.isRemote)
		{
			return;
		}

		for (int i = 0; i < InputSlotNumber; i++)
		{
			ItemStack itemstack = craftMatrix.getStackInSlotOnClosing(i);

			if (itemstack != null)
			{
				par1EntityPlayer.dropPlayerItemWithRandomChoice(itemstack, false);
			}
		}

	}


	@Override
	public boolean canInteractWith(EntityPlayer par1EntityPlayer){
		if (worldObj.getBlock(posX, posY, posZ) != ModBlocks.blockWorkbench){
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

			if (par2 == 0)
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
}