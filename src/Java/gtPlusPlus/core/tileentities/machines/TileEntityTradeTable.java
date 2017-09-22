package gtPlusPlus.core.tileentities.machines;

import java.util.List;
import java.util.Vector;

import gtPlusPlus.core.container.Container_ProjectTable;
import gtPlusPlus.core.container.Container_TradeTable;
import gtPlusPlus.core.inventories.*;
import gtPlusPlus.core.inventories.projecttable.InventoryProjectMain;
import gtPlusPlus.core.inventories.projecttable.InventoryProjectOutput;
import gtPlusPlus.core.inventories.tradetable.InventoryTradeMain;
import gtPlusPlus.core.inventories.tradetable.InventoryTradeOutput;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.nbt.NBTUtils;
import ic2.api.network.INetworkDataProvider;
import ic2.api.network.INetworkUpdateListener;
import ic2.api.tile.IWrenchable;
import ic2.core.IC2;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

public class TileEntityTradeTable extends TileEntity implements INetworkDataProvider, INetworkUpdateListener, IWrenchable{
	
	public InventoryTradeMain inventoryGrid;
	public InventoryTradeOutput inventoryOutputs;
	
	/** The crafting matrix inventory (3x3). */
    public InventoryCrafting craftMatrix;
    public IInventory craftResult;
    private Container_TradeTable container;

	public TileEntityTradeTable(){
		this.inventoryGrid = new InventoryTradeMain();//number of slots - without product slot
		this.inventoryOutputs = new InventoryTradeOutput();//number of slots - without product slot
		this.canUpdate();
	}
	
	public void setContainer(Container_TradeTable container_TradeTable){
		this.container = container_TradeTable;
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

	@Override
	public void updateEntity() {
		
		//Data stick
		ItemStack dataStick = this.inventoryOutputs.getStackInSlot(0);
		if (dataStick != null && this.container != null){
			Utils.LOG_WARNING("Found Data Stick and valid container.");
			
			
			ItemStack outputComponent = container.getOutputContent();
			ItemStack[] craftInputComponent = container.getInputComponents();
			
			
			ItemStack newStick = NBTUtils.writeItemsToNBT(dataStick, new ItemStack[]{outputComponent}, "Output");
			newStick = NBTUtils.writeItemsToNBT(newStick, craftInputComponent);
			NBTUtils.setBookTitle(newStick, "Encrypted Project Data");
			int slotm=0;
			Utils.LOG_WARNING("Uploading to Data Stick.");
			for (ItemStack is : NBTUtils.readItemsFromNBT(newStick)){
				if (is != null){
					Utils.LOG_WARNING("Uploaded "+is.getDisplayName()+" into memory slot "+slotm+".");
				}
				else {					
					Utils.LOG_WARNING("Left memory slot "+slotm+" blank.");
				}
				slotm++;
			}
			Utils.LOG_WARNING("Encrypting Data Stick.");
			this.inventoryOutputs.setInventorySlotContents(1, newStick);
			this.inventoryOutputs.setInventorySlotContents(0, null);
		}		
		super.updateEntity();
	}

	@Override
	public boolean canUpdate() {
		return true;
	}
	




}