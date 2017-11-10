package gtPlusPlus.core.tileentities.machines;

import gtPlusPlus.core.container.Container_ModularityTable;
import gtPlusPlus.core.container.Container_ProjectTable;
import gtPlusPlus.core.inventories.projecttable.InventoryProjectMain;
import gtPlusPlus.core.inventories.projecttable.InventoryProjectOutput;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.nbt.NBTUtils;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityModularityTable extends TileEntity {

	public InventoryProjectMain inventoryGrid;
	public InventoryProjectOutput inventoryOutputs;

	/** The crafting matrix inventory (3x3). */
	public InventoryCrafting craftMatrix;
	public IInventory craftResult;
	private Container_ModularityTable container;

	public TileEntityModularityTable(){ 
		Utils.LOG_INFO("I am created.");
		this.inventoryGrid = new InventoryProjectMain();//number of slots - without product slot
		this.inventoryOutputs = new InventoryProjectOutput();//number of slots - without product slot
		this.canUpdate();
	}

	public void setContainer(Container_ModularityTable container_ModularityTable){
		this.container = container_ModularityTable;
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
		this.inventoryGrid.writeToNBT(this.getTag(nbt, "ContentsGrid"));
		this.inventoryOutputs.writeToNBT(this.getTag(nbt, "ContentsOutput"));

	}

	@Override
	public void readFromNBT(final NBTTagCompound nbt){
		super.readFromNBT(nbt);
		this.inventoryGrid.readFromNBT(nbt.getCompoundTag("ContentsGrid"));
		this.inventoryOutputs.readFromNBT(nbt.getCompoundTag("ContentsOutput"));
	}

	@Override
	public void updateEntity() {
		if (!this.worldObj.isRemote){
			//Data stick
			ItemStack dataStick = this.inventoryOutputs.getStackInSlot(0);
			if (dataStick != null && this.container != null && container.getOutputContent() != null){
				Utils.LOG_WARNING("Found Data Stick and valid container.");


				ItemStack outputComponent = container.getOutputContent();
				ItemStack[] craftInputComponent = container.getInputComponents();


				ItemStack newStick = NBTUtils.writeItemsToNBT(dataStick, new ItemStack[]{outputComponent}, "Output");
				newStick = NBTUtils.writeItemsToNBT(newStick, craftInputComponent);
				NBTUtils.setBookTitle(newStick, "Encrypted Project Data");
				NBTUtils.setBoolean(newStick, "mEncrypted", true);
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
		}
		super.updateEntity();
	}

	@Override
	public boolean canUpdate() {
		return true;
	}





}