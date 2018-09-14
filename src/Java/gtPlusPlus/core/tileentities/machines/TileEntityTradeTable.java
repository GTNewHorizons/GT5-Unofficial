package gtPlusPlus.core.tileentities.machines;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import gtPlusPlus.core.container.Container_TradeTable;
import gtPlusPlus.core.inventories.tradetable.InventoryTradeMain;
import gtPlusPlus.core.inventories.tradetable.InventoryTradeOutput;
import gtPlusPlus.core.tileentities.base.TileEntityBase;
import gtPlusPlus.core.util.minecraft.NBTUtils;

public class TileEntityTradeTable extends TileEntityBase {

	public InventoryTradeMain inventoryGrid;
	public InventoryTradeOutput inventoryOutputs;
	
	private Container_TradeTable container;

	public TileEntityTradeTable(){
		super(2);
		this.inventoryGrid = new InventoryTradeMain();//number of slots - without product slot
		this.inventoryOutputs = new InventoryTradeOutput();//number of slots - without product slot
	}

	public void setContainer(Container_TradeTable container_TradeTable){
		this.container = container_TradeTable;
	}

	@Override
	public void writeToNBT(final NBTTagCompound nbt){
		super.writeToNBT(nbt);
		this.inventoryGrid.writeToNBT(this.getTag(nbt, "ContentsGrid"));
		this.inventoryOutputs.writeToNBT(this.getTag(nbt, "ContentsOutput"));

	}

	@Override
	public void readFromNBT(final NBTTagCompound nbt){
		this.inventoryGrid.readFromNBT(nbt);
		this.inventoryOutputs.readFromNBT(nbt);
		super.readFromNBT(nbt);
	}

	@Override
	public void updateEntity() {	
		if (!this.worldObj.isRemote){	
			ItemStack slot0;
			try{

				slot0 = this.inventoryOutputs.getStackInSlot(0);
				if (slot0 != null && slot0.hasTagCompound()){
					NBTUtils.tryIterateNBTData(slot0);
					this.inventoryOutputs.setInventorySlotContents(0, null);
					this.inventoryOutputs.setInventorySlotContents(1, slot0);
				}	

			}
			catch (Throwable t){
				t.printStackTrace();
				this.inventoryOutputs.setInventorySlotContents(0, null);
			}

		}
		super.updateEntity();
	}

	@Override
	public int getSizeInventory() {
		return 0;
	}

	@Override
	public ItemStack getStackInSlot(int p_70301_1_) {
		return null;
	}

	@Override
	public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
		return null;
	}

	@Override
	public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
		
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
		return true;
	}

	@Override
	public void openInventory() {
		
	}

	@Override
	public void closeInventory() {
		
	}

	@Override
	public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
		return false;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int p_94128_1_) {
		return new int[] {};
	}

	@Override
	public boolean canInsertItem(int p_102007_1_, ItemStack p_102007_2_, int p_102007_3_) {
		return false;
	}

	@Override
	public boolean canExtractItem(int p_102008_1_, ItemStack p_102008_2_, int p_102008_3_) {
		return false;
	}

}