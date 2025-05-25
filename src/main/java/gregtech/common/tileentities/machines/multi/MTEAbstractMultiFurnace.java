package gregtech.common.tileentities.machines.multi;

import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;

public abstract class MTEAbstractMultiFurnace<T extends MTEAbstractMultiFurnace<T>>
    extends MTEExtendedPowerMultiBlockBase<T> {

    private HeatingCoilLevel mCoilLevel;

    protected MTEAbstractMultiFurnace(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected MTEAbstractMultiFurnace(String aName) {
        super(aName);
    }

    public HeatingCoilLevel getCoilLevel() {
        return mCoilLevel;
    }

    public void setCoilLevel(HeatingCoilLevel aCoilLevel) {
        mCoilLevel = aCoilLevel;
    }
}
