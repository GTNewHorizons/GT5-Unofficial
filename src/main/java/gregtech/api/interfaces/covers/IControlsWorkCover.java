package gregtech.api.interfaces.covers;

import static gregtech.api.enums.GT_Values.ALL_VALID_SIDES;

import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IMachineProgress;

/**
 * Marker interface for covers that might control whether the work can start on a {@link IMachineProgress}.
 */
public interface IControlsWorkCover {

    /**
     * Make sure there is only one GT_Cover_ControlsWork on the aTileEntity TODO this is a migration thing. Remove this
     * after 2.3.0 is released.
     *
     * @return true if the cover is the first (side) one
     **/
    static boolean makeSureOnlyOne(byte aMySide, ICoverable aTileEntity) {
        for (byte tSide : ALL_VALID_SIDES) {
            if (aTileEntity.getCoverBehaviorAtSideNew(tSide) instanceof IControlsWorkCover && tSide < aMySide) {
                aTileEntity.dropCover(tSide, tSide, true);
                aTileEntity.markDirty();
                return false;
            }
        }
        return true;
    }
}
