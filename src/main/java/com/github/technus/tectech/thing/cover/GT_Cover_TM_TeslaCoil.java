package com.github.technus.tectech.thing.cover;

import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.GT_CoverBehavior;
import gregtech.api.util.GT_Utility;
import net.minecraft.entity.player.EntityPlayer;

import static com.github.technus.tectech.thing.metaTileEntity.multi.GT_MetaTileEntity_TM_teslaCoil.teslaNodeSet;
import static ic2.api.info.Info.DMG_ELECTRIC;

public class GT_Cover_TM_TeslaCoil extends GT_CoverBehavior {
    public GT_Cover_TM_TeslaCoil() {
    }

    public int doCoverThings(byte aSide, byte aInputRedstone, int aCoverID, int aCoverVariable, ICoverable aTileEntity, long aTimer) {
        if (aTileEntity.getEUCapacity() > 0) {
            teslaNodeSet.add(aTileEntity.getIGregTechTileEntityOffset(0, 0, 0));
        }
        return super.doCoverThings(aSide, aInputRedstone, aCoverID, aCoverVariable, aTileEntity, aTimer);
    }

    public String getDescription(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return "Do not attempt to use screwdriver!";
    }

    public boolean letsEnergyIn(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    public int onCoverScrewdriverclick(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (aTileEntity.getStoredEU() > 0 && !GT_Utility.isWearingFullElectroHazmat(aPlayer)) {
            aPlayer.attackEntityFrom(DMG_ELECTRIC, 20);
        }
        return aCoverVariable;
    }

    public int getTickRate(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return 200;
    }
}
