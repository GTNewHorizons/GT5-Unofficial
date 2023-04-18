package gregtech.api.interfaces.tileentity;

import net.minecraftforge.common.util.ForgeDirection;

/**
 * Implemented by all my Machines. However without any security checks, if the Players are even allowed to rotate it.
 */
public interface ITurnable {

    /**
     * Get the block's facing.
     *
     * @return front Block facing
     */
    ForgeDirection getFrontFacing();

    /**
     * Set the block's facing
     *
     * @param side facing to set the block to
     */
    void setFrontFacing(ForgeDirection side);

    /**
     * Get the block's back facing.
     *
     * @return opposite Block facing
     */
    ForgeDirection getBackFacing();

    /**
     * Determine if the wrench can be used to set the block's facing.
     */
    boolean isValidFacing(ForgeDirection side);

    /**
     * Get the list of valid facings
     */
    default boolean[] getValidFacings() {
        final boolean[] validFacings = new boolean[6];
        for (final ForgeDirection facing : ForgeDirection.VALID_DIRECTIONS) {
            validFacings[facing.ordinal()] = isValidFacing(facing);
        }
        return validFacings;
    }
}
