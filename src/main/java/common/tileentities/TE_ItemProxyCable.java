package common.tileentities;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TE_ItemProxyCable extends TileEntity {
	
	private static final float THICKNESS = 0.5F;
	private byte connections = 0;
	private byte connectionAllowed = 63;
	private String idCache = null;
	
	public TE_ItemProxyCable() {

	}

	@Override
	public void updateEntity() {
		// Check all 6 sides and connect the conduit if it is allowed to
		for(ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
			final TileEntity te = super.getWorldObj().getTileEntity(
					super.xCoord + side.offsetX,
					super.yCoord + side.offsetY,
					super.zCoord + side.offsetZ);
			if(te instanceof TE_ItemProxyCable) {
				final TE_ItemProxyCable cable = (TE_ItemProxyCable) te;
				setConnection(side, cable.isConnectionAllowed(side.getOpposite()));
			} else {
				setConnection(side, false);
			}
		}
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
	 * @return
	 * 			True if the connection was allowed
	 */
	public boolean setConnection(ForgeDirection side, boolean connected) {
		if(isConnectionAllowed(side)){
			switch(side) {
			case DOWN: 	connections = (byte) ((connected) ? connections | 1 : connections ^ 1); break;
			case UP:	connections = (byte) ((connected) ? connections | 2 : connections ^ 2); break;
			case NORTH:	connections = (byte) ((connected) ? connections | 4 : connections ^ 4); break;
			case SOUTH:	connections = (byte) ((connected) ? connections | 8 : connections ^ 8); break;
			case WEST:	connections = (byte) ((connected) ? connections | 16 : connections ^ 16); break;
			case EAST:	connections = (byte) ((connected) ? connections | 32 : connections ^ 32); break;
			default: return false;
			}
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isConnected(ForgeDirection side) {
		switch(side) {
		case DOWN: 	return (connections & 1) == 1;
		case UP: 	return (connections & 2) == 2;
		case NORTH: return (connections & 4) == 4;
		case SOUTH: return (connections & 8) == 8;
		case WEST: 	return (connections & 16) == 16;
		case EAST: 	return (connections & 32) == 32;
		default: return false;
		}
	}

	public void setConnectionAllowed(ForgeDirection side, boolean allowed) {
		switch(side) {
			case DOWN: 	connectionAllowed = (byte) ((allowed) ? connectionAllowed | 1 : connectionAllowed ^ 1); break;
			case UP:	connectionAllowed = (byte) ((allowed) ? connectionAllowed | 2 : connectionAllowed ^ 2); break;
			case NORTH:	connectionAllowed = (byte) ((allowed) ? connectionAllowed | 4 : connectionAllowed ^ 4); break;
			case SOUTH:	connectionAllowed = (byte) ((allowed) ? connectionAllowed | 8 : connectionAllowed ^ 8); break;
			case WEST:	connectionAllowed = (byte) ((allowed) ? connectionAllowed | 16 : connectionAllowed ^ 16); break;
			case EAST:	connectionAllowed = (byte) ((allowed) ? connectionAllowed | 32 : connectionAllowed ^ 32); break;
			default: break;
		}
	}

	public boolean isConnectionAllowed(ForgeDirection side) {
		switch(side) {
			case DOWN: 	return (connectionAllowed & 1) == 1;
			case UP: 	return (connectionAllowed & 2) == 2;
			case NORTH: return (connectionAllowed & 4) == 4;
			case SOUTH: return (connectionAllowed & 8) == 8;
			case WEST: 	return (connectionAllowed & 16) == 16;
			case EAST: 	return (connectionAllowed & 32) == 32;
			default: return false;
		}
	}
}
