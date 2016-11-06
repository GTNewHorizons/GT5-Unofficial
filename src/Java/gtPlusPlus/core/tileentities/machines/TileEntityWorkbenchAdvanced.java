package gtPlusPlus.core.tileentities.machines;

import gtPlusPlus.core.inventories.*;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.network.INetworkDataProvider;
import ic2.api.network.INetworkUpdateListener;
import ic2.api.tile.IWrenchable;
import ic2.core.IC2;
import ic2.core.network.NetworkManager;

import java.util.List;
import java.util.Vector;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityWorkbenchAdvanced extends TileEntity implements IEnergySink, INetworkDataProvider, INetworkUpdateListener, IWrenchable{

	//Credit to NovaViper in http://www.minecraftforge.net/forum/index.php?topic=26439.0 - Helped me restructure my Inventory system and now the crafting matrix works better.

	public InventoryWorkbenchChest inventoryChest;
	public InventoryWorkbenchToolsElectric inventoryTool;
	public InventoryWorkbenchHoloSlots inventoryHolo;	
	public InventoryWorkbenchHoloCrafting inventoryCrafting;	

	public IInventory inventoryCraftResult = new InventoryCraftResult();

	//Wrench Code
	private short facing = 0;
	public short prevFacing = 0;

	//E-Net Code
	public double energy = 0.0D;
	public int maxEnergy;
	private boolean addedToEnergyNet = false;
	private int tier;
	private float guiChargeLevel;


	public TileEntityWorkbenchAdvanced(int maxenergy, int tier1){
		this.inventoryTool = new InventoryWorkbenchToolsElectric();//number of slots - without product slot
		this.inventoryChest = new InventoryWorkbenchChest();//number of slots - without product slot
		this.inventoryHolo = new InventoryWorkbenchHoloSlots();
		this.inventoryCrafting = new InventoryWorkbenchHoloCrafting();
		this.canUpdate();

		//Electric Stats
		this.maxEnergy = maxenergy;
		this.tier = tier1;

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
		nbt.setDouble("energy", this.energy);
		nbt.setShort("facing", this.facing);

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
		this.energy = nbt.getDouble("energy");

		this.prevFacing = (this.facing = nbt.getShort("facing"));

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

	@Override
	public boolean acceptsEnergyFrom(TileEntity emitter, ForgeDirection direction)
	{
		return true;
	}

	@Override
	public double getDemandedEnergy()
	{
		return this.maxEnergy - this.energy;
	}

	@Override
	public int getSinkTier()
	{
		return this.tier;
	}

	@Override
	public double injectEnergy(ForgeDirection directionFrom, double amount, double voltage)
	{
		if (this.energy >= this.maxEnergy) {
			return amount;
		}
		this.energy += amount;
		return 0.0D;
	}

	public final float getChargeLevel()
	{
		return this.guiChargeLevel;
	}

	public void setTier(int tier1)
	{
		if (this.tier == tier1) {
			return;
		}
		boolean addedToENet = this.addedToEnergyNet;
		if (addedToENet)
		{
			MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
			this.addedToEnergyNet = false;
		}
		this.tier = tier1;

		for (int i=0; i<inventoryTool.getSizeInventory(); i++){
			//this.inventoryTool..setTier(tier1); TODO
		}

		if (addedToENet)
		{
			MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
			this.addedToEnergyNet = true;
		}
	}

	@Override
	public List<String> getNetworkedFields(){
		List<String> ret = new Vector(2);	    
		ret.add("facing");	    
		return ret;
	}


	@Override
	public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, int side)
	{
		return false;
	}

	@Override
	public void setFacing(short facing1)
	{
		this.facing = facing1;
		if (this.prevFacing != facing1) {
			((NetworkManager)IC2.network.get()).updateTileEntityField(this, "facing");
		}
		this.prevFacing = facing1;
	}

	@Override
	public short getFacing()
	{
		return this.facing;
	}


	@Override
	public boolean wrenchCanRemove(EntityPlayer entityPlayer)
	{
		return true;
	}

	@Override
	public float getWrenchDropRate()
	{
		return 1.0F;
	}

	@Override
	public ItemStack getWrenchDrop(EntityPlayer entityPlayer)
	{
		return new ItemStack(this.worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord), 1, this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord));
	}

	@Override
	public void onNetworkUpdate(String field) {

		this.prevFacing = this.facing;

	}

}