/*
 * Copyright (c) 2018-2020 bartimaeusnek
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.bartimaeusnek.crossmod.tectech.helper;

import com.github.bartimaeusnek.crossmod.tectech.TecTechEnabledMulti;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyMulti;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyTunnel;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;

public class TecTechUtils {

    @Deprecated
    public static boolean addEnergyInputToMachineList(
            TecTechEnabledMulti baseTE, IGregTechTileEntity te, int aBaseCasingIndex) {
        return addEnergyInputToMachineList(baseTE, te, aBaseCasingIndex, -1) != -1;
    }

    public static int addEnergyInputToMachineList(
            TecTechEnabledMulti baseTE, IGregTechTileEntity te, int aBaseCasingIndex, int aTier) {
        if (te == null || !(te.getMetaTileEntity() instanceof GT_MetaTileEntity_Hatch)) return -1;
        else {
            GT_MetaTileEntity_Hatch mte = (GT_MetaTileEntity_Hatch) te.getMetaTileEntity();

            if (mte.mTier != aTier && aTier != -1) return -1;

            if (mte instanceof GT_MetaTileEntity_Hatch_EnergyTunnel)
                if (baseTE.getVanillaEnergyHatches().isEmpty()
                        && baseTE.getTecTechEnergyMultis().isEmpty())
                    baseTE.getTecTechEnergyTunnels().add((GT_MetaTileEntity_Hatch_EnergyTunnel) mte);
                else return -1;
            else if (baseTE.getTecTechEnergyTunnels().isEmpty()) {
                if (mte instanceof GT_MetaTileEntity_Hatch_Energy)
                    baseTE.getVanillaEnergyHatches().add((GT_MetaTileEntity_Hatch_Energy) mte);
                else if (mte instanceof GT_MetaTileEntity_Hatch_EnergyMulti)
                    baseTE.getTecTechEnergyMultis().add((GT_MetaTileEntity_Hatch_EnergyMulti) mte);
                else return -1;
            } else return -1;

            mte.updateTexture(aBaseCasingIndex);
            return mte.mTier;
        }
    }

    public static boolean drainEnergyMEBFTecTech(TecTechEnabledMulti multi, long aEU) {
        if (aEU <= 0) return true;

        long allTheEu = 0;
        int hatches = 0;
        for (GT_MetaTileEntity_Hatch_Energy tHatch : multi.getVanillaEnergyHatches())
            if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(tHatch)) {
                allTheEu += tHatch.getEUVar();
                hatches++;
            }

        for (GT_MetaTileEntity_Hatch_EnergyTunnel tHatch : multi.getTecTechEnergyTunnels())
            if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(tHatch)) {
                allTheEu += tHatch.getEUVar();
                hatches++;
            }

        for (GT_MetaTileEntity_Hatch_EnergyMulti tHatch : multi.getTecTechEnergyMultis())
            if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(tHatch)) {
                allTheEu += tHatch.getEUVar();
                hatches++;
            }

        if (allTheEu < aEU) return false;

        if (hatches == 0) return false;

        long euperhatch = aEU / hatches;

        boolean hasDrained = true;

        for (GT_MetaTileEntity_Hatch_Energy tHatch : multi.getVanillaEnergyHatches())
            hasDrained &= tHatch.getBaseMetaTileEntity().decreaseStoredEnergyUnits(euperhatch, false);

        for (GT_MetaTileEntity_Hatch_EnergyTunnel tHatch : multi.getTecTechEnergyTunnels())
            hasDrained &= tHatch.getBaseMetaTileEntity().decreaseStoredEnergyUnits(euperhatch, false);

        for (GT_MetaTileEntity_Hatch_EnergyMulti tHatch : multi.getTecTechEnergyMultis())
            hasDrained &= tHatch.getBaseMetaTileEntity().decreaseStoredEnergyUnits(euperhatch, false);

        return hasDrained
                && (multi.getVanillaEnergyHatches().size() > 0
                        || multi.getTecTechEnergyTunnels().size() > 0
                        || multi.getTecTechEnergyMultis().size() > 0);
    }

    public static long getnominalVoltageTT(TecTechEnabledMulti base) {
        long rVoltage = 0L;
        long rAmperage = 0L;

        for (GT_MetaTileEntity_Hatch_Energy tHatch : base.getVanillaEnergyHatches()) {
            if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(tHatch)) {
                rVoltage = Math.max(tHatch.getBaseMetaTileEntity().getInputVoltage(), rVoltage);
                rAmperage += tHatch.getBaseMetaTileEntity().getInputAmperage();
            }
        }

        for (GT_MetaTileEntity_Hatch_EnergyMulti tHatch : base.getTecTechEnergyMultis()) {
            if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(tHatch)) {
                rVoltage = Math.max(tHatch.getBaseMetaTileEntity().getInputVoltage(), rVoltage);
                rAmperage += tHatch.Amperes;
            }
        }

        for (GT_MetaTileEntity_Hatch_EnergyTunnel tHatch : base.getTecTechEnergyTunnels()) {
            if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(tHatch)) {
                rVoltage = Math.max(getEUPerTickFromLaser(tHatch), rVoltage);
                rAmperage += 1;
            }
        }

        return rVoltage * rAmperage;
    }

    public static long getMaxInputVoltage(TecTechEnabledMulti base) {
        long rVoltage = 0L;
        for (GT_MetaTileEntity_Hatch_Energy tHatch : base.getVanillaEnergyHatches()) {
            if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(tHatch)) {
                rVoltage += tHatch.getBaseMetaTileEntity().getInputVoltage();
            }
        }
        for (GT_MetaTileEntity_Hatch_EnergyMulti tHatch : base.getTecTechEnergyMultis()) {
            if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(tHatch)) {
                rVoltage += tHatch.getBaseMetaTileEntity().getInputVoltage();
            }
        }
        for (GT_MetaTileEntity_Hatch_EnergyTunnel tHatch : base.getTecTechEnergyTunnels()) {
            if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(tHatch)) {
                rVoltage += tHatch.maxEUInput();
            }
        }
        return rVoltage;
    }

    public static long getMaxInputAmperage(TecTechEnabledMulti base) {
        long rAmperage = 0L;
        for (GT_MetaTileEntity_Hatch_Energy tHatch : base.getVanillaEnergyHatches()) {
            if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(tHatch)) {
                rAmperage += tHatch.getBaseMetaTileEntity().getInputAmperage();
            }
        }
        for (GT_MetaTileEntity_Hatch_EnergyMulti tHatch : base.getTecTechEnergyMultis()) {
            if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(tHatch)) {
                rAmperage += tHatch.getBaseMetaTileEntity().getInputAmperage();
            }
        }
        for (GT_MetaTileEntity_Hatch_EnergyTunnel tHatch : base.getTecTechEnergyTunnels()) {
            if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(tHatch)) {
                rAmperage += tHatch.Amperes;
            }
        }
        return rAmperage;
    }

    public static long getEUPerTickFromLaser(GT_MetaTileEntity_Hatch_EnergyTunnel tHatch) {
        return tHatch.Amperes * tHatch.maxEUInput() /* - (tHatch.Amperes / 20)*/;
    }
}
