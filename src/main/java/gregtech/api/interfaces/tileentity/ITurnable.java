package gregtech.api.interfaces.tileentity;


import static gregtech.api.enums.GT_Values.ALL_VALID_SIDES;

/**
 * Implemented by all my Machines. However without any security checks, if the Players are even allowed to rotate it.
 */
public interface ITurnable {
    /**
     * Get the block's facing.
     *
     * @return front Block facing
     */
    byte getFrontFacing();

    /**
     * Set the block's facing
     *
     * @param aSide facing to set the block to
     */
    void setFrontFacing(byte aSide);

    /**
     * Get the block's back facing.
     *
     * @return opposite Block facing
     */
    byte getBackFacing();

    /**
     * Determine if the wrench can be used to set the block's facing.
     */
    boolean isValidFacing(byte aSide);

    /**
     * Get the list of valid facings
     */
    default boolean[] getValidFacings() {
        final boolean[] validFacings = new boolean[6];
        for(byte facing : ALL_VALID_SIDES) {
            validFacings[facing] = isValidFacing(facing);
        }
        return validFacings;
    }
}
