package gregtech.api.interfaces.tileentity;

import net.minecraftforge.common.util.ForgeDirection;

/**
 * This File has just internal Information about the Redstone State of a TileEntity
 */
public interface IRedstoneEmitter extends IHasWorldObjectAndCoords {

    /**
     * gets the Redstone Level the TileEntity should emit to the given Output Side
     *
     * @param side the {@link ForgeDirection} side
     * @return the Redstone Level the TileEntity
     */
    byte getOutputRedstoneSignal(ForgeDirection side);

    /**
     * sets the Redstone Level the TileEntity should emit to the given Output Side
     * <p/>
     * Do not use this if ICoverable is implemented. ICoverable has @getInternalOutputRedstoneSignal for Machine
     * internal Output Redstone, so that it doesnt conflict with Cover Redstone. This sets the true Redstone Output
     * Signal. Only Cover Behaviors should use it, not MetaTileEntities.
     */
    void setOutputRedstoneSignal(ForgeDirection side, byte aStrength);

    /**
     * gets the Redstone Level the TileEntity should emit to the given Output Side
     */
    byte getStrongOutputRedstoneSignal(ForgeDirection side);

    /**
     * sets the Redstone Level the TileEntity should emit to the given Output Side
     * <p/>
     * Do not use this if ICoverable is implemented. ICoverable has @getInternalOutputRedstoneSignal for Machine
     * internal Output Redstone, so that it doesnt conflict with Cover Redstone. This sets the true Redstone Output
     * Signal. Only Cover Behaviors should use it, not MetaTileEntities.
     */
    void setStrongOutputRedstoneSignal(ForgeDirection side, byte aStrength);

    /**
     * Gets the Output for the comparator on the given Side
     */
    byte getComparatorValue(ForgeDirection side);

    /**
     * Get the redstone output signal strength for a given side
     */
    default byte getGeneralRS(ForgeDirection side) {
        return 0;
    }
}
