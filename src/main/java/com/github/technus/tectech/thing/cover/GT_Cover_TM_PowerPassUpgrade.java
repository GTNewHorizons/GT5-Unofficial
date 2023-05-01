package com.github.technus.tectech.thing.cover;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.GT_CoverBehavior;

public class GT_Cover_TM_PowerPassUpgrade extends GT_CoverBehavior {

    public GT_Cover_TM_PowerPassUpgrade() {}

    @Override
    public boolean isCoverPlaceable(ForgeDirection side, ItemStack aStack, ICoverable aTileEntity) {
        IMetaTileEntity iGregTechTileEntityOffset = aTileEntity.getIGregTechTileEntityOffset(0, 0, 0)
                .getMetaTileEntity();
        if (iGregTechTileEntityOffset instanceof GT_MetaTileEntity_MultiblockBase_EM) {
            GT_MetaTileEntity_MultiblockBase_EM multi = (GT_MetaTileEntity_MultiblockBase_EM) iGregTechTileEntityOffset;
            return !multi.ePowerPassCover;
        }
        return false;
    }

    @Override
    public void placeCover(ForgeDirection side, ItemStack aCover, ICoverable aTileEntity) {
        IMetaTileEntity iGregTechTileEntityOffset = aTileEntity.getIGregTechTileEntityOffset(0, 0, 0)
                .getMetaTileEntity();
        if (iGregTechTileEntityOffset instanceof GT_MetaTileEntity_MultiblockBase_EM) {
            GT_MetaTileEntity_MultiblockBase_EM multi = (GT_MetaTileEntity_MultiblockBase_EM) iGregTechTileEntityOffset;
            multi.ePowerPassCover = true;
            multi.ePowerPass = true;
        }
        super.placeCover(side, aCover, aTileEntity);
    }

    @Override
    public boolean onCoverRemoval(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
            boolean aForced) {
        IMetaTileEntity iGregTechTileEntityOffset = aTileEntity.getIGregTechTileEntityOffset(0, 0, 0)
                .getMetaTileEntity();
        if (iGregTechTileEntityOffset instanceof GT_MetaTileEntity_MultiblockBase_EM) {
            GT_MetaTileEntity_MultiblockBase_EM multi = (GT_MetaTileEntity_MultiblockBase_EM) iGregTechTileEntityOffset;
            multi.ePowerPassCover = false;
            multi.ePowerPass = false;
        }
        return true;
    }

    @Deprecated
    public int getTickRate(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return 0;
    }
}
