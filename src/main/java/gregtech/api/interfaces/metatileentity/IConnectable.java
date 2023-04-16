package gregtech.api.interfaces.metatileentity;

import net.minecraftforge.common.util.ForgeDirection;

/**
 * For pipes, wires, and other MetaTiles which need to be decided whether they should connect to the block at each side.
 */
public interface IConnectable {

    int NO_CONNECTION = 0b00000000;
    int CONNECTED_DOWN = 0b00000001;
    int CONNECTED_UP = 0b00000010;
    int CONNECTED_NORTH = 0b00000100;
    int CONNECTED_SOUTH = 0b00001000;
    int CONNECTED_WEST = 0b00010000;
    int CONNECTED_EAST = 0b00100000;
    int CONNECTED_ALL = 0b00111111;
    int HAS_FRESHFOAM = 0b01000000;
    int HAS_HARDENEDFOAM = 0b10000000;
    int HAS_FOAM = 0b11000000;

    /**
     * Try to connect to the Block at the specified side returns the connection state. Non-positive values for failed,
     * others for succeeded.
     */
    int connect(ForgeDirection side);

    /**
     * Try to disconnect to the Block at the specified side
     */
    void disconnect(ForgeDirection side);

    boolean isConnectedAtSide(ForgeDirection side);
}
