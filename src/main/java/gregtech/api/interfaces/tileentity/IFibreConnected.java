package gregtech.api.interfaces.tileentity;

/**
 * This File has just internal Information about the Fibre Redstone State of a TileEntity
 */
public interface IFibreConnected extends IColoredTileEntity, IHasWorldObjectAndCoords {

    /**
     * If this Blocks accepts Fibre from this Side
     */
    void inputFibreFrom(ForgeDirection aSide);

    /**
     * If this Blocks emits Fibre to this Side
     */
    void outputsFibreTo(ForgeDirection aSide);

    /**
     * Sets the Signal this Blocks outputs to this Fibre Color
     */
    void setFibreOutput(ForgeDirection aSide, byte aColor, byte aRedstoneStrength);

    /**
     * Gets the Signal this Blocks outputs to this Fibre Color
     */
    byte getFibreOutput(ForgeDirection aSide, byte aColor);

    /**
     * Gets the Signal this Blocks receives from this Fibre Color
     */
    byte getFibreInput(ForgeDirection aSide, byte aColor);
}
