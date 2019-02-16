package com.github.technus.tectech.thing.cover;

import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.GT_CoverBehavior;
import gregtech.api.util.GT_Utility;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

import static ic2.api.info.Info.DMG_ELECTRIC;

public class GT_Cover_TM_TeslaCoil extends GT_CoverBehavior {
    public GT_Cover_TM_TeslaCoil() {
    }

    public String getDescription(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return "Do not attempt to use screwdriver!";
    }

    public boolean letsEnergyIn(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    public int onCoverScrewdriverclick(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if(aTileEntity.getStoredEU() > 0 && !GT_Utility.isWearingFullElectroHazmat(aPlayer)){
            aPlayer.attackEntityFrom(DMG_ELECTRIC, 20);
        }
        return aCoverVariable;
    }
}