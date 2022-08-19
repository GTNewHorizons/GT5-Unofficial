package com.github.technus.tectech.thing.cover;

import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.GT_CoverBehavior;
import net.minecraft.item.ItemStack;

public class GT_Cover_TM_PowerPassUpgrade extends GT_CoverBehavior {
    public GT_Cover_TM_PowerPassUpgrade() {}

    @Override
    public boolean isCoverPlaceable(byte aSide, ItemStack aStack, ICoverable aTileEntity) {
        IMetaTileEntity iGregTechTileEntityOffset =
                aTileEntity.getIGregTechTileEntityOffset(0, 0, 0).getMetaTileEntity();
        if (iGregTechTileEntityOffset instanceof GT_MetaTileEntity_MultiblockBase_EM) {
            GT_MetaTileEntity_MultiblockBase_EM multi = (GT_MetaTileEntity_MultiblockBase_EM) iGregTechTileEntityOffset;
            return !multi.ePowerPassCover;
        }
        return false;
    }

    @Override
    public void placeCover(byte aSide, ItemStack aCover, ICoverable aTileEntity) {
        IMetaTileEntity iGregTechTileEntityOffset =
                aTileEntity.getIGregTechTileEntityOffset(0, 0, 0).getMetaTileEntity();
        if (iGregTechTileEntityOffset instanceof GT_MetaTileEntity_MultiblockBase_EM) {
            GT_MetaTileEntity_MultiblockBase_EM multi = (GT_MetaTileEntity_MultiblockBase_EM) iGregTechTileEntityOffset;
            multi.ePowerPassCover = true;
            multi.ePowerPass = true;
        }
        super.placeCover(aSide, aCover, aTileEntity);
    }

    @Override
    public boolean onCoverRemoval(
            byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity, boolean aForced) {
        IMetaTileEntity iGregTechTileEntityOffset =
                aTileEntity.getIGregTechTileEntityOffset(0, 0, 0).getMetaTileEntity();
        if (iGregTechTileEntityOffset instanceof GT_MetaTileEntity_MultiblockBase_EM) {
            GT_MetaTileEntity_MultiblockBase_EM multi = (GT_MetaTileEntity_MultiblockBase_EM) iGregTechTileEntityOffset;
            multi.ePowerPassCover = false;
            multi.ePowerPass = false;
        }
        return true;
    }

    @Deprecated
    public int getTickRate(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return 0;
    }
}
