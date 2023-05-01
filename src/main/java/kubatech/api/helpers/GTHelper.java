/*
 * spotless:off
 * KubaTech - Gregtech Addon
 * Copyright (C) 2022 - 2023  kuba6000
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see <https://www.gnu.org/licenses/>.
 * spotless:on
 */

package kubatech.api.helpers;

import static gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity;
import static kubatech.api.Variables.ln4;

import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import kubatech.api.implementations.KubaTechGTMultiBlockBase;

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
