package miscutil.core.tileentities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TILE_ENTITY_BASE extends TileEntity {

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		writeCustomNBT(tag);
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		readCustomNBT(tag);
	}

	public void writeCustomNBT(NBTTagCompound tag) {}
	public void readCustomNBT(NBTTagCompound tag) {}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound tag = new NBTTagCompound();
		writeCustomNBT(tag);
		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, -999, tag);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
		super.onDataPacket(net, packet);
		readCustomNBT(packet.func_148857_g());
	}

}
