package gregtech.common.tileentities.machines.multi.purification;

public class LinkedPurificationUnit {

    private GT_MetaTileEntity_PurificationUnitBase<?> mMetaTileEntity;

    public LinkedPurificationUnit(GT_MetaTileEntity_PurificationUnitBase<?> unit) {
        this.mMetaTileEntity = unit;
    }

    public GT_MetaTileEntity_PurificationUnitBase<?> metaTileEntity() {
        return mMetaTileEntity;
    }
}
