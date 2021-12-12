package gtPlusPlus.core.util.minecraft.network;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gtPlusPlus.api.interfaces.IGregtechPacketEntity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import mods.railcraft.common.util.misc.Game;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class PacketTileEntity extends CustomPacket {
	private IGregTechTileEntity tile;
	private IGregtechPacketEntity ptile;

	public PacketTileEntity() {
	}

	public PacketTileEntity(IGregTechTileEntity tile) {
		this.tile = tile;		
		if (tile instanceof IGregtechPacketEntity) {
			ptile = (IGregtechPacketEntity) tile;
		}		
	}

	public void writeData(DataOutputStream data) throws IOException {
		if (ptile != null) {
			data.writeInt(this.tile.getXCoord());
			data.writeInt(this.tile.getYCoord());
			data.writeInt(this.tile.getZCoord());
			data.writeShort(this.tile.getMetaTileID());
			this.ptile.writePacketData(data);
		}
	}

	@SideOnly(Side.CLIENT)
	public void readData(DataInputStream data) throws IOException {
		Minecraft mc = FMLClientHandler.instance().getClient();		
		World world = mc != null ? mc.theWorld : null;
		if (world != null) {
			int x = data.readInt();
			int y = data.readInt();
			int z = data.readInt();
			short id = data.readShort();
			if (id >= 0 && y >= 0 && world.blockExists(x, y, z)) {
				TileEntity te = world.getTileEntity(x, y, z);
				if (te instanceof IGregTechTileEntity) {
					this.tile = (IGregTechTileEntity) te;
					if (this.tile.getMetaTileID() != id) {
						this.tile = null;
					}
				} else {
					this.tile = null;
				}
				if (this.tile != null) {					
					if (tile instanceof IGregtechPacketEntity) {
						ptile = (IGregtechPacketEntity) tile;
						try {
							this.ptile.readPacketData(data);
						} catch (IOException var10) {
							throw var10;
						} catch (RuntimeException var11) {							
							Game.logThrowable("Exception in PacketTileEntity.readData:", var11, new Object[0]);
						}
					}
				}
			}
		}
	}

	public int getID() {
		return 0;
	}
}