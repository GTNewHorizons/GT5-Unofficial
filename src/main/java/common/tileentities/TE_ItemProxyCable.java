package common.tileentities;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TE_ItemProxyCable extends TileEntity {
	
	private static final float THICKNESS = 0.3f;
	private byte connections;
	private String idCache = null;
	
	public TE_ItemProxyCable() {
		connections = 63; // set all connections active until I have something actually control connections
	}
	
	public static float getThickness() {
		return THICKNESS;
	}
	
	/**
	 * Builds a simple unique identifier for this TileEntity by appending 
	 * the x, y, and z coordinates in a string.
	 * 
	 * @return unique identifier for this TileEntity
	 */
	public String getIdentifier() {
		if(idCache == null) {
			idCache = "" + super.xCoord + super.yCoord + super.zCoord;
			return idCache;
		} else {
			return idCache;
		}
	}
	
	/**
	 * 0 0 0 0 0 0 0 0 = 0     -> no connection </br>
	 * 0 0 0 0 0 0 0 1 = 1     -> down  </br>
	 * 0 0 0 0 0 0 1 0 = 2     -> up  </br>
	 * 0 0 0 0 0 1 0 0 = 4     -> north  </br>
	 * 0 0 0 0 1 0 0 0 = 8     -> south  </br>
	 * 0 0 0 1 0 0 0 0 = 16    -> west  </br>
	 * 0 0 1 0 0 0 0 0 = 32    -> east  </br>
	 * 
	 * @param side
	 * 			The side for which to set the connection status.
	 * @param connected
	 * 			Whether this side should be connected or not
	 */
	public void setConnection(ForgeDirection side, boolean connected) {
		switch(side) {
		case DOWN: 	connections = (byte) ((connected) ? connections | 1 : connections ^ 1); break;
		case UP:	connections = (byte) ((connected) ? connections | 2 : connections ^ 2); break;
		case NORTH:	connections = (byte) ((connected) ? connections | 4 : connections ^ 4); break;
		case SOUTH:	connections = (byte) ((connected) ? connections | 8 : connections ^ 8); break;
		case WEST:	connections = (byte) ((connected) ? connections | 16 : connections ^ 16); break;
		case EAST:	connections = (byte) ((connected) ? connections | 32 : connections ^ 32); break;
		default: break;
		}
	}
	
	public boolean isConnected(ForgeDirection side) {
		switch(side) {
		case DOWN: 	return (connections & 1) == connections;
		case UP: 	return (connections & 2) == connections;
		case NORTH: return (connections & 4) == connections;
		case SOUTH: return (connections & 8) == connections;
		case WEST: 	return (connections & 16) == connections;
		case EAST: 	return (connections & 32) == connections;
		default: return false;
		}
	}
	
	public byte getConnections() {
		return connections;
	}
	
}
