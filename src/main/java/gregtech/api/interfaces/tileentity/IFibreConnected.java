package gregtech.api.interfaces.tileentity;

/**
 * This File has just internal Information about the Fibre Redstone State of a TileEntity
 */
public interface IFibreConnected extends IColoredTileEntity, IHasWorldObjectAndCoords {

    /**
     * If this Blocks accepts Fibre from this Side
     */
    void inputFibreFrom(byte aSide);

    /**
     * If this Blocks emits Fibre to this Side
     */
    void outputsFibreTo(byte aSide);

    /**
     * Sets the Signal this Blocks outputs to this Fibre Color
     */
    void setFibreOutput(byte aSide, byte aColor, byte aRedstoneStrength);

    /**
     * Gets the Signal this Blocks outputs to this Fibre Color
     */
    byte getFibreOutput(byte aSide, byte aColor);

    /**
     * Gets the Signal this Blocks receives from this Fibre Color
     */
    byte getFibreInput(byte aSide, byte aColor);
}
