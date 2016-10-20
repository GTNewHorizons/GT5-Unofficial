package gtPlusPlus.core.tileentities.machines;

import gtPlusPlus.core.inventories.InventoryWorkbenchChest;
import gtPlusPlus.core.inventories.InventoryWorkbenchHoloCrafting;
import gtPlusPlus.core.inventories.InventoryWorkbenchHoloSlots;
import gtPlusPlus.core.inventories.InventoryWorkbenchTools;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

public class TileEntityWorkbench extends TileEntity {
	
	//Credit to NovaViper in http://www.minecraftforge.net/forum/index.php?topic=26439.0 - Helped me restructure my Inventory system and now the crafting matrix works better.

	public InventoryWorkbenchChest inventoryChest;
	public InventoryWorkbenchTools inventoryTool;
	public InventoryWorkbenchHoloSlots inventoryHolo;	
	public InventoryWorkbenchHoloCrafting inventoryCrafting;	

	public IInventory inventoryCraftResult = new InventoryCraftResult();

	public TileEntityWorkbench(){
		this.inventoryTool = new InventoryWorkbenchTools();//number of slots - without product slot
		this.inventoryChest = new InventoryWorkbenchChest();//number of slots - without product slot
		this.inventoryHolo = new InventoryWorkbenchHoloSlots();
		this.inventoryCrafting = new InventoryWorkbenchHoloCrafting();
		this.canUpdate();
	}

	@SuppressWarnings("static-method")
	public NBTTagCompound getTag(NBTTagCompound nbt, String tag)
	{
		if(!nbt.hasKey(tag))
		{
			nbt.setTag(tag, new NBTTagCompound());
		}
		return nbt.getCompoundTag(tag);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		inventoryChest.writeToNBT(getTag(nbt, "ContentsChest"));
		inventoryTool.writeToNBT(getTag(nbt, "ContentsTools"));
		//inventoryCrafting.writeToNBT(getTag(nbt, "ContentsCrafting"));
		inventoryHolo.writeToNBT(getTag(nbt, "ContentsHolo"));

		 // Write Crafting Matrix to NBT
        NBTTagList craftingTag = new NBTTagList();
        for (int currentIndex = 0; currentIndex < inventoryCrafting.getSizeInventory(); ++currentIndex) {
            if (inventoryCrafting.getStackInSlot(currentIndex) != null) {
                NBTTagCompound tagCompound = new NBTTagCompound();
                tagCompound.setByte("Slot", (byte) currentIndex);
                inventoryCrafting.getStackInSlot(currentIndex).writeToNBT(tagCompound);
                craftingTag.appendTag(tagCompound);
            }
        }
		
		nbt.setTag("CraftingMatrix", craftingTag);
		// Write craftingResult to NBT
		if (inventoryCraftResult.getStackInSlot(0) != null)
			nbt.setTag("CraftingResult", inventoryCraftResult.getStackInSlot(0).writeToNBT(new NBTTagCompound()));

	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		inventoryChest.readFromNBT(nbt.getCompoundTag("ContentsChest"));
		inventoryTool.readFromNBT(nbt.getCompoundTag("ContentsTools"));
		//inventoryCrafting.readFromNBT(nbt.getCompoundTag("ContentsCrafting"));
		inventoryHolo.readFromNBT(nbt.getCompoundTag("ContentsHolo"));

		// Read in the Crafting Matrix from NBT
        NBTTagList craftingTag = nbt.getTagList("CraftingMatrix", 10);
        inventoryCrafting = new InventoryWorkbenchHoloCrafting(); //TODO: magic number
        for (int i = 0; i < craftingTag.tagCount(); ++i) {
            NBTTagCompound tagCompound = (NBTTagCompound) craftingTag.getCompoundTagAt(i);
            byte slot = tagCompound.getByte("Slot");
            if (slot >= 0 && slot < inventoryCrafting.getSizeInventory()) {
            	inventoryCrafting.setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(tagCompound));
            }
        }

		
		// Read craftingResult from NBT
		NBTTagCompound tagCraftResult = nbt.getCompoundTag("CraftingResult");
		inventoryCraftResult.setInventorySlotContents(0, ItemStack.loadItemStackFromNBT(tagCraftResult));

	}



}