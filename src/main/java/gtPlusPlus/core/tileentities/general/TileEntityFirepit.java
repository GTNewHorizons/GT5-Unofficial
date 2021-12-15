package gtPlusPlus.core.tileentities.general;

import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityFirepit extends TileEntity{

	private UUID ownerUUID;

	public UUID getOwnerUUID() {
		return this.ownerUUID;
	}

	public void setOwnerUUID(final UUID ownerUUID) {
		this.ownerUUID = ownerUUID;
		this.markDirty();
	}

	@Override
	public void writeToNBT(final NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);

		final UUID ownerUUID = this.getOwnerUUID();
		if (ownerUUID != null){
			tagCompound.setLong("OwnerUUIDMost", ownerUUID.getMostSignificantBits());
			tagCompound.setLong("OwnerUUIDLeast", ownerUUID.getLeastSignificantBits());
		}
	}

	@Override
	public void readFromNBT(final NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);

		this.setOwnerUUID(new UUID(tagCompound.getLong("OwnerUUIDMost"), tagCompound.getLong("OwnerUUIDLeast")));
	}

}
