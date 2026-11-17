package gregtech.common.tileentities.machines.multi.purification;

/**
 * Small wrapper around a MTEPurificationUnitBase, to be stored in the main purification plant controller. May be useful
 * for storing additional data in the controller that the individual units do not need to know about.
 */
public class LinkedPurificationUnit {

    /**
     * Whether this unit is active in the current cycle. We need to keep track of this so units cannot come online in
     * the middle of a cycle and suddenly start processing.
     */
    private boolean mIsActive = false;

    private final MTEPurificationUnitBase<?> mMetaTileEntity;

    public LinkedPurificationUnit(MTEPurificationUnitBase<?> unit) {
        this.mMetaTileEntity = unit;
    }

    public MTEPurificationUnitBase<?> metaTileEntity() {
        return mMetaTileEntity;
    }

    /**
     * Whether this unit is considered as active in the current cycle
     *
     * @return true if this unit is active in the current cycle
     */
    public boolean isActive() {
        return mIsActive;
    }

    public void setActive(boolean active) {
        this.mIsActive = active;
    }
}
