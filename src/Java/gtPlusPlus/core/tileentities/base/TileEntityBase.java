package gtPlusPlus.core.tileentities.base;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import gtPlusPlus.api.objects.Logger;

public abstract class TileEntityBase extends TileEntity implements ISidedInventory {

	private String customName;
	public String mOwnerName = "null";
	public String mOwnerUUID = "null";	
	private boolean mIsOwnerOP = false;

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

		if (this.hasCustomInventoryName()) {
			nbt.setString("CustomName", this.getCustomName());
		}

		nbt.setBoolean("mIsOwnerOP", this.mIsOwnerOP);
		nbt.setString("mOwnerName", this.mOwnerName);
		nbt.setString("mOwnerUUID", this.mOwnerUUID);
	}

	@Override
	public void readFromNBT(final NBTTagCompound nbt){

		super.readFromNBT(nbt);

		if (nbt.hasKey("CustomName", 8)) {
			this.setCustomName(nbt.getString("CustomName"));
		}

		this.mIsOwnerOP = nbt.getBoolean("mIsOwnerOP");
		this.mOwnerName = nbt.getString("mOwnerName");
		this.mOwnerUUID = nbt.getString("mOwnerUUID");
	}

	@Override
	public void updateEntity() {
		try{
			if (this.isServerSide()){
				onPreTick();
			}
		} catch (Throwable t){
			Logger.ERROR("Tile Entity Encountered an error in it's pre-tick stage.");
			t.printStackTrace();
		}
		try{
			if (this.isServerSide()){
				onTick();
			}
		} catch (Throwable t){
			Logger.ERROR("Tile Entity Encountered an error in it's tick stage.");
			t.printStackTrace();
		}
		try{
			if (this.isServerSide()){
				onPostTick();
			}
		} catch (Throwable t){
			Logger.ERROR("Tile Entity Encountered an error in it's post-tick stage.");
			t.printStackTrace();
		}
	}

	public boolean onPreTick(){
		return true;
	}

	public boolean onTick(){
		try{
			if (this.isServerSide()){
				processRecipe();
			}
		} catch (Throwable t){
			Logger.ERROR("Tile Entity Encountered an error in it's processing of a recipe stage.");
			t.printStackTrace();
		}
		return true;
	}

	public boolean onPostTick(){

		return true;
	}
	
	public boolean processRecipe(){
		return true;
	}
	

	@Override
	public boolean canUpdate() {
		return true;
	}

	public String getOwner(){
		if (this.mOwnerName == null){
			return "null";
		}
		return this.mOwnerName;
	}

	public UUID getOwnerUUID(){
		return UUID.fromString(this.mOwnerUUID);
	}

	public boolean isOwnerOP() {
		return mIsOwnerOP;
	}

	public void setOwnerInformation(String mName, String mUUID, boolean mOP){
		if (isServerSide()){
			if (this.mOwnerName.equals("null") || this.mOwnerUUID.equals("null")
					|| this.mOwnerName == null || this.mOwnerUUID == null){
				this.mOwnerName = mName;
				this.mOwnerUUID = mUUID;
				this.mIsOwnerOP = mOP;
			}
		}
	}

	public boolean isServerSide(){
		if (this.hasWorldObj()){
			if (!this.getWorldObj().isRemote){
				return true;
			}
		}
		return false;
	}

	public String getCustomName() {
		return this.customName;
	}

	public void setCustomName(String customName) {
		this.customName = customName;
	}

	@Override
	public String getInventoryName() {
		return this.hasCustomInventoryName() ? this.customName : "container.fishrap";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return this.customName != null && !this.customName.equals("");
	}

	@Override
	public abstract int getSizeInventory();

	@Override
	public abstract ItemStack getStackInSlot(int p_70301_1_);

	@Override
	public abstract ItemStack decrStackSize(int p_70298_1_, int p_70298_2_);

	@Override
	public abstract ItemStack getStackInSlotOnClosing(int p_70304_1_);

	@Override
	public abstract void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_);

	@Override
	public abstract int getInventoryStackLimit();

	@Override
	public abstract boolean isUseableByPlayer(EntityPlayer p_70300_1_);

	@Override
	public abstract void openInventory();

	@Override
	public abstract void closeInventory();

	@Override
	public abstract boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_);

	@Override
	public abstract int[] getAccessibleSlotsFromSide(int p_94128_1_);

	@Override
	public abstract boolean canInsertItem(int p_102007_1_, ItemStack p_102007_2_, int p_102007_3_);

	@Override
	public abstract boolean canExtractItem(int p_102008_1_, ItemStack p_102008_2_, int p_102008_3_);

}