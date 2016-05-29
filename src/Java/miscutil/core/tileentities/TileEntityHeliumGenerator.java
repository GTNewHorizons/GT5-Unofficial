package miscutil.core.tileentities;

import miscutil.core.item.ModItems;
import miscutil.core.util.UtilsItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityHeliumGenerator extends TILE_ENTITY_BASE implements IInventory {

    private ItemStack heliumStack;
    private int facing = 2;
    private int progress;

    @Override
    public void updateEntity(){
        if(++progress >= 40){
        //if(++progress >= 300){
            if(heliumStack == null)
                heliumStack = UtilsItems.getSimpleStack(ModItems.itemHeliumBlob);
            else if(heliumStack.getItem() == ModItems.itemHeliumBlob && heliumStack.stackSize < 64)
                heliumStack.stackSize++;
            progress = 0;
            markDirty();
        }
    }

    public int getFacing(){
        return facing;
    }

    public void setFacing(int dir){
        facing = dir;
    }

    @Override
    public void readCustomNBT(NBTTagCompound tag)
    {
        this.heliumStack = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("Helium"));
        this.progress = tag.getInteger("Progress");
        this.facing = tag.getShort("Facing");
    }

    @Override
    public void writeCustomNBT(NBTTagCompound tag)
    {
        tag.setInteger("Progress", this.progress);
        tag.setShort("Facing", (short) this.facing);
        if(heliumStack != null) {
            NBTTagCompound produce = new NBTTagCompound();
            heliumStack.writeToNBT(produce);
            tag.setTag("Helium", produce);
        }
        else
            tag.removeTag("Helium");
    }

    @Override
    public int getSizeInventory()
    {
        return 1;
    }

    @Override
    public ItemStack getStackInSlot(int slot){
        return heliumStack;
    }

    @Override
    public ItemStack decrStackSize(int slot, int decrement){
        if(heliumStack == null)
            return null;
		if(decrement < heliumStack.stackSize){
		    ItemStack take = heliumStack.splitStack(decrement);
		    if(heliumStack.stackSize <= 0)
		        heliumStack = null;
		    return take;
		}
		ItemStack take = heliumStack;
		heliumStack = null;
		return take;
    }

    @Override
    public void openInventory() {}
    @Override
    public void closeInventory() {}

    @Override
    public boolean isUseableByPlayer(EntityPlayer player)
    {
        return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) == this && player.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack){
        return false;
    }

    @Override
    public int getInventoryStackLimit(){
        return 64;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack){
        heliumStack = stack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot){
        return null;
    }

    /**
     * Returns the name of the inventory
     */
    @Override
    public String getInventoryName()
    {
        return  "container.helium_collector";
    }

    /**
     * Returns if the inventory is named
     */
    @Override
    public boolean hasCustomInventoryName()
    {
        return false;
    }

}
