package gtPlusPlus.core.tileentities.base;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TILE_ENTITY_BASE extends TileEntity {

	@Override
	public Packet getDescriptionPacket() {
		final NBTTagCompound tag = new NBTTagCompound();
		this.writeCustomNBT(tag);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, -999, tag);
	}

	@Override
	public void onDataPacket(final NetworkManager net, final S35PacketUpdateTileEntity packet) {
		super.onDataPacket(net, packet);
		this.readCustomNBT(packet.func_148857_g());
	}

	public void readCustomNBT(final NBTTagCompound tag) {
	}

	@Override
	public void readFromNBT(final NBTTagCompound tag) {
		super.readFromNBT(tag);
		this.readCustomNBT(tag);
	}

	public void writeCustomNBT(final NBTTagCompound tag) {
	}

	@Override
	public void writeToNBT(final NBTTagCompound tag) {
		super.writeToNBT(tag);
		this.writeCustomNBT(tag);
	}

}
