package gregtech.api.interfaces.metatileentity;

/**
 * For pipes, wires, and other MetaTiles which need to be decided whether they should connect to the block at each side.
 */
public interface IConnectable {

    // spotless:off
    int NO_CONNECTION = 0b00000000;
    int CONNECTED_DOWN = 0b00000001;
    int CONNECTED_UP = 0b00000010;
    int CONNECTED_NORTH = 0b00000100;
    int CONNECTED_SOUTH = 0b00001000;
    int CONNECTED_WEST = 0b00010000;
    int CONNECTED_EAST = 0b00100000;
    int CONNECTED_ALL = CONNECTED_DOWN|CONNECTED_UP|CONNECTED_NORTH|CONNECTED_SOUTH|CONNECTED_WEST|CONNECTED_EAST;
    int HAS_FRESHFOAM = 0b01000000;
    int HAS_HARDENEDFOAM = 0b10000000;
    int HAS_FOAM = HAS_FRESHFOAM|HAS_HARDENEDFOAM;
    // spotless:on

    /**
     * Try to connect to the Block at the specified side returns the connection state. Non-positive values for failed,
     * others for succeeded.
     */
    int connect(byte aSide);

    /**
     * Try to disconnect to the Block at the specified side
     */
    void disconnect(byte aSide);

    boolean isConnectedAtSide(int aSide);
}
