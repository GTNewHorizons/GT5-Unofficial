package gtPlusPlus.core.tileentities.general;

import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityFirepit extends TileEntity{
	
	private UUID ownerUUID;

	public UUID getOwnerUUID() {
		return ownerUUID;
	}

	public void setOwnerUUID(UUID ownerUUID) {
		this.ownerUUID = ownerUUID;
		markDirty();
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);

		UUID ownerUUID = getOwnerUUID();
		if (ownerUUID != null){
		tagCompound.setLong("OwnerUUIDMost", ownerUUID.getMostSignificantBits());
		tagCompound.setLong("OwnerUUIDLeast", ownerUUID.getLeastSignificantBits());
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);

		setOwnerUUID(new UUID(tagCompound.getLong("OwnerUUIDMost"), tagCompound.getLong("OwnerUUIDLeast")));
	}
	
}
