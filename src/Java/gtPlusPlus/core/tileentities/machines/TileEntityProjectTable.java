package gtPlusPlus.core.tileentities.machines;

import java.util.List;
import java.util.Vector;

import gtPlusPlus.core.inventories.*;
import gtPlusPlus.core.inventories.projecttable.InventoryProjectMain;
import gtPlusPlus.core.inventories.projecttable.InventoryProjectOutput;
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

public class TileEntityProjectTable extends TileEntity implements INetworkDataProvider, INetworkUpdateListener, IWrenchable{
	
	public InventoryProjectMain inventoryGrid;
	public InventoryProjectOutput inventoryOutputs;

	public TileEntityProjectTable(){
		this.inventoryGrid = new InventoryProjectMain();//number of slots - without product slot
		this.inventoryOutputs = new InventoryProjectOutput();//number of slots - without product slot
		this.canUpdate();
	}

	@SuppressWarnings("static-method")
	public NBTTagCompound getTag(final NBTTagCompound nbt, final String tag){
		if(!nbt.hasKey(tag))
		{
			nbt.setTag(tag, new NBTTagCompound());
		}
		return nbt.getCompoundTag(tag);
	}

	@Override
	public void writeToNBT(final NBTTagCompound nbt){
		super.writeToNBT(nbt);
		nbt.setShort("facing", this.facing);
		this.inventoryGrid.writeToNBT(this.getTag(nbt, "ContentsGrid"));
		this.inventoryOutputs.writeToNBT(this.getTag(nbt, "ContentsOutput"));

	}

	@Override
	public void readFromNBT(final NBTTagCompound nbt){
		super.readFromNBT(nbt);
		this.prevFacing = (this.facing = nbt.getShort("facing"));
		this.inventoryGrid.readFromNBT(nbt.getCompoundTag("ContentsGrid"));
		this.inventoryOutputs.readFromNBT(nbt.getCompoundTag("ContentsOutput"));
	}

	@Override
	public List<String> getNetworkedFields(){
		final List<String> ret = new Vector(2);
		ret.add("facing");
		return ret;
	}


	@Override
	public boolean wrenchCanSetFacing(final EntityPlayer entityPlayer, final int side){
		return false;
	}

	private short facing = 0;
	public short prevFacing = 0;

	@Override
	public void setFacing(final short facing1){
		this.facing = facing1;
		if (this.prevFacing != facing1) {
			IC2.network.get().updateTileEntityField(this, "facing");
		}
		this.prevFacing = facing1;
	}

	@Override
	public short getFacing(){
		return this.facing;
	}


	@Override
	public boolean wrenchCanRemove(final EntityPlayer entityPlayer){
		return true;
	}

	@Override
	public float getWrenchDropRate(){
		return 1.0F;
	}

	@Override
	public ItemStack getWrenchDrop(final EntityPlayer entityPlayer){
		return new ItemStack(this.worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord), 1, this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord));
	}

	@Override
	public void onNetworkUpdate(final String field) {
		this.prevFacing = this.facing;

	}



}