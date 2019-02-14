package com.github.technus.tectech.thing.cover;

import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.GT_CoverBehavior;

public class GT_Cover_TM_TeslaCoil extends GT_CoverBehavior {
    public GT_Cover_TM_TeslaCoil() {
    }

    public boolean teslaCompatibiliy(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        if (aSide != 1) {
            return false;
        } else {
            return true;
        }
    }
}