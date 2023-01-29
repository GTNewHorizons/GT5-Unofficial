package kubatech.api.helpers;

import static gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity;
import static kubatech.api.Variables.ln4;

import kubatech.api.implementations.KubaTechGTMultiBlockBase;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;

public class GTHelper {

    public static long getMaxInputEU(GT_MetaTileEntity_MultiBlockBase mte) {
        if (mte instanceof KubaTechGTMultiBlockBase) return ((KubaTechGTMultiBlockBase<?>) mte).getMaxInputEu();
        long rEU = 0;
        for (GT_MetaTileEntity_Hatch_Energy tHatch : mte.mEnergyHatches)
            if (isValidMetaTileEntity(tHatch)) rEU += tHatch.maxEUInput() * tHatch.maxAmperesIn();
        return rEU;
    }

    public static double getVoltageTierD(long voltage) {
        return Math.log((double) voltage / 8L) / ln4;
    }

    public static double getVoltageTierD(GT_MetaTileEntity_MultiBlockBase mte) {
        return Math.log((double) getMaxInputEU(mte) / 8L) / ln4;
    }

    public static int getVoltageTier(long voltage) {
        return (int) getVoltageTierD(voltage);
    }

    public static int getVoltageTier(GT_MetaTileEntity_MultiBlockBase mte) {
        return (int) getVoltageTierD(mte);
    }
}
