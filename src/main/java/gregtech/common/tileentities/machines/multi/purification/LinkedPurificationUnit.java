package gregtech.common.tileentities.machines.multi.purification;

// Small wrapper around a GT_MetaTileEntity_PurificationUnitBase, to be stored in the main purification plant
// controller. May be useful for storing additional data in the controller that the individual units do not need
// to know about.
public class LinkedPurificationUnit {

    private final GT_MetaTileEntity_PurificationUnitBase<?> mMetaTileEntity;

    public LinkedPurificationUnit(GT_MetaTileEntity_PurificationUnitBase<?> unit) {
        this.mMetaTileEntity = unit;
    }

    public GT_MetaTileEntity_PurificationUnitBase<?> metaTileEntity() {
        return mMetaTileEntity;
    }
}
