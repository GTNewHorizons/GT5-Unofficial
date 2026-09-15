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
     * internal Output Redstone, so that it doesn't conflict with Cover Redstone. This sets the true Redstone Output
     * Signal. Only Cover Behaviors should use it, not MetaTileEntities.
     */
    void setStrongOutputRedstoneSignal(ForgeDirection side, byte aStrength);

    /**
     * Sets the strength of the redstone signal on the given side to strong or weak. Does not change the actual signal.
     *
     * @param side     Must not be UNKNOWN
     * @param isStrong True = strong, false = weak
     */
    default void setRedstoneOutputStrength(ForgeDirection side, boolean isStrong) {}

    /**
     * Checks if the given side will output a strong redstone signal when emitting a redstone signal.
     *
     * @param side Must not be UNKNOWN
     * @return True = strong signal, false = weak signal
     */
    default boolean getRedstoneOutputStrength(ForgeDirection side) {
        return false;
    }

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
