package gtPlusPlus.core.tileentities.machines;

import gtPlusPlus.core.container.Container_TradeTable;
import gtPlusPlus.core.inventories.tradetable.InventoryTradeMain;
import gtPlusPlus.core.inventories.tradetable.InventoryTradeOutput;
import gtPlusPlus.core.tileentities.base.TileEntityBase;
import gtPlusPlus.core.util.nbt.NBTUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityTradeTable extends TileEntityBase {

	public InventoryTradeMain inventoryGrid;
	public InventoryTradeOutput inventoryOutputs;
	
	private Container_TradeTable container;

	public TileEntityTradeTable(){
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

}