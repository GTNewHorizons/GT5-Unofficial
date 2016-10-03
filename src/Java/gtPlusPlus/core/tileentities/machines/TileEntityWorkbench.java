package gtPlusPlus.core.tileentities.machines;

import gtPlusPlus.core.inventories.InventoryWorkbenchChest;
import gtPlusPlus.core.inventories.InventoryWorkbenchTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityWorkbench extends TileEntity {

	public InventoryWorkbenchChest inventoryChest;
	public InventoryWorkbenchTools inventoryTool;
	//public InventoryWorkbenchCrafting inventoryCrafting;	
	
	public TileEntityWorkbench(){
		this.inventoryTool = new InventoryWorkbenchTools();//number of slots - without product slot
		this.inventoryChest = new InventoryWorkbenchChest();//number of slots - without product slot
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
        
    }
    
    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        inventoryChest.readFromNBT(nbt.getCompoundTag("ContentsChest"));
        inventoryTool.readFromNBT(nbt.getCompoundTag("ContentsTools"));
        //inventoryCrafting.readFromNBT(nbt.getCompoundTag("ContentsCrafting"));
    }

}