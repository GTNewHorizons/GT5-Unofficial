package com.github.technus.tectech.thing.cover;

import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_CoverBehavior;

public class GT_Cover_TM_PowerPassUpgrade extends GT_CoverBehavior {
    public GT_Cover_TM_PowerPassUpgrade() {
    }

    @Override
    public int doCoverThings(byte aSide, byte aInputRedstone, int aCoverID, int aCoverVariable, ICoverable aTileEntity, long aTimer) {
        IGregTechTileEntity TE = aTileEntity.getIGregTechTileEntityOffset(0, 0, 0);
        if (TE instanceof GT_MetaTileEntity_MultiblockBase_EM) {
            GT_MetaTileEntity_MultiblockBase_EM multi = (GT_MetaTileEntity_MultiblockBase_EM) TE;
            multi.ePowerPassUpgraded = true;
        }
        return super.doCoverThings(aSide, aInputRedstone, aCoverID, aCoverVariable, aTileEntity, aTimer);
    }

    @Override
    public boolean onCoverRemoval(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity, boolean aForced) {
        IGregTechTileEntity TE = aTileEntity.getIGregTechTileEntityOffset(0, 0, 0);
        if (TE instanceof GT_MetaTileEntity_MultiblockBase_EM) {
            GT_MetaTileEntity_MultiblockBase_EM multi = (GT_MetaTileEntity_MultiblockBase_EM) TE;
            multi.ePowerPassUpgraded = false;
        }
        return true;
    }

    @Override
    public int getTickRate(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        //It updates once every 200 ticks, so once every 10 seconds
        return 200;
    }
}
