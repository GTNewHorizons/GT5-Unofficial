package gtPlusPlus.core.container;

import gregtech.api.gui.GT_Slot_Holo;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.interfaces.IItemBlueprint;
import gtPlusPlus.core.inventories.*;
import gtPlusPlus.core.item.general.ItemBlueprint;
import gtPlusPlus.core.slots.*;
import gtPlusPlus.core.tileentities.machines.TileEntityWorkbench;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.ItemUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.world.World;

public class Container_Workbench extends Container {

	protected TileEntityWorkbench tile_entity;
	public InventoryCrafting craftMatrix = new InventoryCrafting(this, 3, 3);
	public final InventoryWorkbenchChest inventoryChest;
	public final InventoryWorkbenchTools inventoryTool;
	public final InventoryWorkbenchHoloSlots inventoryHolo;
	public final InventoryWorkbenchHoloCrafting inventoryCrafting;

	private World worldObj;
	private int posX;
	private int posY;
	private int posZ;

	public static int HoloSlotNumber = 6;
	public static int InputSlotNumber = 9; //Number of Slots in the Crafting Grid
	public static int StorageSlotNumber = 16; //Number of slots in storage area
	public static int ToolSlotNumber = 5; // Number of slots in the tool area up top
	public static int InOutputSlotNumber = InputSlotNumber + StorageSlotNumber + ToolSlotNumber + HoloSlotNumber; //Same plus Output Slot
	public static int InventorySlotNumber = 36; //Inventory Slots (Inventory and Hotbar)
	public static int InventoryOutSlotNumber = InventorySlotNumber + 1; //Inventory Slot Number + Output
	public static int FullSlotNumber = InventorySlotNumber + InOutputSlotNumber; //All slots

	private int slotOutput = 0;
	private int[] slotHolo = new int[5];
	private int[] slotCrafting = new int[9];
	private int[] slotStorage = new int[16];
	private int[] slotTools = new int[5];

	public void moveCraftingToChest(){
		//Check Chest Space
		for (int i=0;i<9;i++){
			if (craftMatrix.getStackInSlot(i) != null){
				for (int r=0;r<16;r++){
					if (inventoryChest.getStackInSlot(r) == null || (inventoryChest.getStackInSlot(r).getItem() == craftMatrix.getStackInSlot(i).getItem() && (64-craftMatrix.getStackInSlot(i).stackSize) <= (64-craftMatrix.getStackInSlot(i).stackSize))){
						inventoryChest.setInventorySlotContents(r, craftMatrix.getStackInSlot(i));
						craftMatrix.setInventorySlotContents(i, null);
						break;
					}
				}
			}
		}
		//For Each Space or already existing itemstack, move one itemstack or fill current partial stack
		//Remove old itemstack or partial stack from crafting grid
	}

	public void moveChestToCrafting(){
		//Check Crafting items and slots
		for (int i=0;i<9;i++){
			if (craftMatrix.getStackInSlot(i) == null || craftMatrix.getStackInSlot(i).stackSize > 0){
				for (int r=0;r<16;r++){
					if (inventoryChest.getStackInSlot(r) != null){
						craftMatrix.setInventorySlotContents(i, craftMatrix.getStackInSlot(r));
						inventoryChest.setInventorySlotContents(r, null);
					}
				}
			}
		}
		//For Each already existing itemstack, fill current partial stack
		//Remove partial stack from chest area
	}


	public Container_Workbench(InventoryPlayer inventory, TileEntityWorkbench tile){
		this.tile_entity = tile;
		this.inventoryChest = tile.inventoryChest;
		this.inventoryTool = tile.inventoryTool;
		this.inventoryHolo = tile.inventoryHolo;
		this.inventoryCrafting = tile.inventoryCrafting;

		int var6;
		int var7;
		worldObj = tile.getWorldObj();
		posX = tile.xCoord;
		posY = tile.yCoord;
		posZ = tile.zCoord;

		int o=0;

		//Output slot
		addSlotToContainer(new SlotOutput(inventory.player, this.craftMatrix, tile.inventoryCraftResult, 0, 136, 64));
		//Util Slots
		addSlotToContainer(new SlotBlueprint(inventoryHolo, 1, 136, 28)); //Blueprint
		addSlotToContainer(new SlotNoInput(inventoryHolo, 2, 154, 28)); //Hopper
		addSlotToContainer(new GT_Slot_Holo(inventoryHolo, 3, 154, 64, false, false, 64)); //Parking
		//Holo Slots
		addSlotToContainer(new GT_Slot_Holo(inventoryHolo, 4, 154, 46, false, false, 1));
		addSlotToContainer(new GT_Slot_Holo(inventoryHolo, 5, 136, 46, false, false, 1));

		for (int i=1; i<6; i++){
			slotHolo[o] = o+1;
			o++;
		}

		o=0;

		updateCraftingMatrix();

		//Crafting Grid
		for (var6 = 0; var6 < 3; ++var6)
		{
			for (var7 = 0; var7 < 3; ++var7)
			{
				this.addSlotToContainer(new Slot(this.craftMatrix, var7 + var6 * 3, 82 + var7 * 18, 28 + var6 * 18));

				/*if (this.inventoryCrafting.getStackInSlot(o) != null){
					this.craftMatrix.setInventorySlotContents(o, inventoryCrafting.getStackInSlot(o));		
					this.inventoryCrafting.setInventorySlotContents(o, null);				
				}	*/			
				slotCrafting[o] = o+6;
				o++;
			}
		}

		o=0;

		//Storage Side
		for (var6 = 0; var6 < 4; ++var6)
		{
			for (var7 = 0; var7 < 4; ++var7)
			{
				//Utils.LOG_WARNING("Adding slots at var:"+(var7 + var6 * 4)+" x:"+(8 + var7 * 18)+" y:"+(7 + var6 * 18));
				this.addSlotToContainer(new Slot(inventoryChest, var7 + var6 * 4, 8 + var7 * 18, 7 + var6 * 18));
				slotStorage[o] = o+15;
				o++;
			}
		}

		o=0;

		//Tool Slots
		for (var6 = 0; var6 < 1; ++var6)
		{
			for (var7 = 0; var7 < 5; ++var7)
			{
				this.addSlotToContainer(new SlotGtTool(inventoryTool, var7 + var6 * 3, 82 + var7 * 18, 8 + var6 * 18));
				slotTools[o] = o+31;
				o++;
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
	public ItemStack slotClick(int aSlotIndex, int aMouseclick, int aShifthold, EntityPlayer aPlayer){

		if (!aPlayer.worldObj.isRemote){
			if (aSlotIndex == 999 || aSlotIndex == -999){
				//Utils.LOG_WARNING("??? - "+aSlotIndex);
			}

			if (aSlotIndex == slotOutput){
				Utils.LOG_WARNING("Player Clicked on the output slot");
				//TODO
			}

			for (int x : slotHolo){
				if (aSlotIndex == x){
					Utils.LOG_WARNING("Player Clicked slot "+aSlotIndex+" in the Holo Grid");		
					if (x == 1){
						Utils.LOG_WARNING("Player Clicked Blueprint slot in the Holo Grid");	
					}
					else if (x == 2){
						Utils.LOG_WARNING("Player Clicked Right Arrow slot in the Holo Grid");
						if (inventoryHolo.getStackInSlot(1) != null){
							Utils.LOG_WARNING("Found an ItemStack.");
							if (inventoryHolo.getStackInSlot(1).getItem() instanceof IItemBlueprint){
								Utils.LOG_WARNING("Found a blueprint.");
								ItemStack tempBlueprint = inventoryHolo.getStackInSlot(1);
								ItemBlueprint tempItemBlueprint = (ItemBlueprint) tempBlueprint.getItem();				
								if (inventoryHolo.getStackInSlot(0) != null && !tempItemBlueprint.hasBlueprint(tempBlueprint)){
									Utils.LOG_WARNING("Output slot was not empty.");
									Utils.LOG_WARNING("Trying to manipulate NBT data on the blueprint stack, then replace it with the new one.");
									tempItemBlueprint.setBlueprint(inventoryHolo.getStackInSlot(1), craftMatrix, inventoryHolo.getStackInSlot(0));	
									ItemStack newTempBlueprint = ItemUtils.getSimpleStack(tempItemBlueprint);
									inventoryHolo.setInventorySlotContents(1, newTempBlueprint);
									Utils.LOG_WARNING(ItemUtils.getArrayStackNames(tempItemBlueprint.getBlueprint(newTempBlueprint)));
								}
								else {
									if (tempItemBlueprint.hasBlueprint(tempBlueprint)){
										Utils.LOG_WARNING("Blueprint already holds a recipe.");
									}
									else {
										Utils.LOG_WARNING("Output slot was empty.");									
									}
								}
							}
							else {
								Utils.LOG_WARNING("ItemStack found was not a blueprint.");							
							}
						}
						else {
							Utils.LOG_WARNING("No ItemStack found in Blueprint slot.");
						}
					}
					else if (x == 3){
						Utils.LOG_WARNING("Player Clicked Big [P] slot in the Holo Grid");	
					}
					else if (x == 4){
						Utils.LOG_WARNING("Player Clicked Transfer to Crafting Grid slot in the Holo Grid");	
					}
					else if (x == 5){
						Utils.LOG_WARNING("Player Clicked Transfer to Storage Grid slot in the Holo Grid");	
					}
				}
			}

			for (int x : slotCrafting){
				if (aSlotIndex == x){
					Utils.LOG_WARNING("Player Clicked slot "+aSlotIndex+" in the crafting Grid");				
				}
			}
			for (int x : slotStorage){
				if (aSlotIndex == x){
					Utils.LOG_WARNING("Player Clicked slot "+aSlotIndex+" in the storage Grid");				
				}
			}
			for (int x : slotTools){
				if (aSlotIndex == x){
					Utils.LOG_WARNING("Player Clicked slot "+aSlotIndex+" in the tool Grid");				
				}
			}
		}
		//Utils.LOG_WARNING("Player Clicked slot "+aSlotIndex+" in the Grid");	
		return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
	}

	private void updateCraftingMatrix() {
		for (int i = 0; i < craftMatrix.getSizeInventory(); i++) {
			craftMatrix.setInventorySlotContents(i, tile_entity.inventoryCrafting.getStackInSlot(i));
		}
	}

	@Override
	public void onCraftMatrixChanged(IInventory iiventory) {
		tile_entity.inventoryCraftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(craftMatrix, worldObj));
	}	

	@Override
	public void onContainerClosed(EntityPlayer par1EntityPlayer)
	{
		super.onContainerClosed(par1EntityPlayer);
		saveCraftingMatrix();
	}

	private void saveCraftingMatrix() {
		for (int i = 0; i < craftMatrix.getSizeInventory(); i++) {
			tile_entity.inventoryCrafting.setInventorySlotContents(i, craftMatrix.getStackInSlot(i));
		}
	}	




	/*@Override
	public void onCraftMatrixChanged(IInventory par1IInventory){
		//Custom Recipe Handler
		//craftResult.setInventorySlotContents(0, Workbench_CraftingHandler.getInstance().findMatchingRecipe(craftMatrix, worldObj));

		//Vanilla CraftingManager
		Utils.LOG_WARNING("checking crafting grid for a valid output.");
		ItemStack temp = CraftingManager.getInstance().findMatchingRecipe(craftMatrix, worldObj);
		if (temp != null){
			Utils.LOG_WARNING("Output found. "+temp.getDisplayName()+" x"+temp.stackSize);
			craftResult.setInventorySlotContents(slotOutput, temp);
		}
		else {
		Utils.LOG_WARNING("No Valid output found.");
		craftResult.setInventorySlotContents(slotOutput, null);
		}
	}*/

	/*@Override
	public void onContainerClosed(EntityPlayer par1EntityPlayer)
	{		
		for (int o=0; o<craftMatrix.getSizeInventory(); o++){
			this.inventoryCrafting.setInventorySlotContents(o, craftMatrix.getStackInSlot(o));	
			this.craftMatrix.setInventorySlotContents(o, null);				
		}*/

	//super.onContainerClosed(par1EntityPlayer);

	/*if (worldObj.isRemote)
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
		}*/

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

	//Can merge Slot
	@Override
	public boolean func_94530_a(ItemStack p_94530_1_, Slot p_94530_2_) {
		return p_94530_2_.inventory != tile_entity.inventoryCraftResult && super.func_94530_a(p_94530_1_, p_94530_2_);
	}


}