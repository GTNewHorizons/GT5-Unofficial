package gtPlusPlus.core.tileentities.base;

import java.util.UUID;

import gtPlusPlus.core.util.Utils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityBase extends TileEntity {

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
		nbt.setBoolean("mIsOwnerOP", this.mIsOwnerOP);
		nbt.setString("mOwnerName", this.mOwnerName);
		nbt.setString("mOwnerUUID", this.mOwnerUUID);
	}

	@Override
	public void readFromNBT(final NBTTagCompound nbt){
		this.mIsOwnerOP = nbt.getBoolean("mIsOwnerOP");
		this.mOwnerName = nbt.getString("mOwnerName");
		this.mOwnerUUID = nbt.getString("mOwnerUUID");
		super.readFromNBT(nbt);
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
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
				Utils.LOG_INFO("Finished setting TE information. owner: "+this.mOwnerName+" | UUID: "+this.mOwnerUUID+" | OP: "+this.mIsOwnerOP);
			}
		}
	}

	public boolean isServerSide(){
		if (this.hasWorldObj()){
			if (this.getWorldObj().isRemote){
				return false;
			}
			else {
				return true;
			}
		}
		return false;
	}

}