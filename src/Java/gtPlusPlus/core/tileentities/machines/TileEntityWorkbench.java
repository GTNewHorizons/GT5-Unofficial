package gtPlusPlus.core.tileentities.machines;

import java.util.List;
import java.util.Vector;

import gtPlusPlus.core.inventories.*;
import ic2.api.network.INetworkDataProvider;
import ic2.api.network.INetworkUpdateListener;
import ic2.api.tile.IWrenchable;
import ic2.core.IC2;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

public class TileEntityWorkbench extends TileEntity implements INetworkDataProvider, INetworkUpdateListener, IWrenchable{

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
	public NBTTagCompound getTag(final NBTTagCompound nbt, final String tag)
	{
		if(!nbt.hasKey(tag))
		{
			nbt.setTag(tag, new NBTTagCompound());
		}
		return nbt.getCompoundTag(tag);
	}

	@Override
	public void writeToNBT(final NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);

		nbt.setShort("facing", this.facing);

		this.inventoryChest.writeToNBT(this.getTag(nbt, "ContentsChest"));
		this.inventoryTool.writeToNBT(this.getTag(nbt, "ContentsTools"));
		//inventoryCrafting.writeToNBT(getTag(nbt, "ContentsCrafting"));
		this.inventoryHolo.writeToNBT(this.getTag(nbt, "ContentsHolo"));

		// Write Crafting Matrix to NBT
		final NBTTagList craftingTag = new NBTTagList();
		for (int currentIndex = 0; currentIndex < this.inventoryCrafting.getSizeInventory(); ++currentIndex) {
			if (this.inventoryCrafting.getStackInSlot(currentIndex) != null) {
				final NBTTagCompound tagCompound = new NBTTagCompound();
				tagCompound.setByte("Slot", (byte) currentIndex);
				this.inventoryCrafting.getStackInSlot(currentIndex).writeToNBT(tagCompound);
				craftingTag.appendTag(tagCompound);
			}
		}

		nbt.setTag("CraftingMatrix", craftingTag);
		// Write craftingResult to NBT
		if (this.inventoryCraftResult.getStackInSlot(0) != null) {
			nbt.setTag("CraftingResult", this.inventoryCraftResult.getStackInSlot(0).writeToNBT(new NBTTagCompound()));
		}

	}

	@Override
	public void readFromNBT(final NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);

		this.prevFacing = (this.facing = nbt.getShort("facing"));

		this.inventoryChest.readFromNBT(nbt.getCompoundTag("ContentsChest"));
		this.inventoryTool.readFromNBT(nbt.getCompoundTag("ContentsTools"));
		//inventoryCrafting.readFromNBT(nbt.getCompoundTag("ContentsCrafting"));
		this.inventoryHolo.readFromNBT(nbt.getCompoundTag("ContentsHolo"));

		// Read in the Crafting Matrix from NBT
		final NBTTagList craftingTag = nbt.getTagList("CraftingMatrix", 10);
		this.inventoryCrafting = new InventoryWorkbenchHoloCrafting(); //TODO: magic number
		for (int i = 0; i < craftingTag.tagCount(); ++i) {
			final NBTTagCompound tagCompound = craftingTag.getCompoundTagAt(i);
			final byte slot = tagCompound.getByte("Slot");
			if ((slot >= 0) && (slot < this.inventoryCrafting.getSizeInventory())) {
				this.inventoryCrafting.setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(tagCompound));
			}
		}


		// Read craftingResult from NBT
		final NBTTagCompound tagCraftResult = nbt.getCompoundTag("CraftingResult");
		this.inventoryCraftResult.setInventorySlotContents(0, ItemStack.loadItemStackFromNBT(tagCraftResult));

	}

	@Override
	public List<String> getNetworkedFields(){
		final List<String> ret = new Vector(2);
		ret.add("facing");
		return ret;
	}


	@Override
	public boolean wrenchCanSetFacing(final EntityPlayer entityPlayer, final int side)
	{
		return false;
	}

	private short facing = 0;
	public short prevFacing = 0;

	@Override
	public void setFacing(final short facing1)
	{
		this.facing = facing1;
		if (this.prevFacing != facing1) {
			IC2.network.get().updateTileEntityField(this, "facing");
		}
		this.prevFacing = facing1;
	}

	@Override
	public short getFacing()
	{
		return this.facing;
	}


	@Override
	public boolean wrenchCanRemove(final EntityPlayer entityPlayer)
	{
		return true;
	}

	@Override
	public float getWrenchDropRate()
	{
		return 1.0F;
	}

	@Override
	public ItemStack getWrenchDrop(final EntityPlayer entityPlayer)
	{
		return new ItemStack(this.worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord), 1, this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord));
	}

	@Override
	public void onNetworkUpdate(final String field) {

		this.prevFacing = this.facing;

	}



}