package gregtech.common.tileentities.machines.multi.purification;

public class LinkedPurificationUnit {

    private GT_MetaTileEntity_PurificationUnitBase<?> mMetaTileEntity;
    private PurificationUnitStatus mStatus;

    public LinkedPurificationUnit(GT_MetaTileEntity_PurificationUnitBase<?> unit, PurificationUnitStatus status) {
        this.mMetaTileEntity = unit;
        this.mStatus = status;
    }

    public GT_MetaTileEntity_PurificationUnitBase<?> metaTileEntity() {
        return mMetaTileEntity;
    }

    public PurificationUnitStatus status() {
        return mStatus;
    }

    public void setStatus(PurificationUnitStatus status) {
        this.mStatus = status;
    }
}
