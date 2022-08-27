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

package com.github.bartimaeusnek.crossmod.tectech;

import com.github.bartimaeusnek.bartworks.common.tileentities.multis.GT_TileEntity_ManualTrafo;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_DynamoMulti;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyMulti;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Dynamo;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import java.util.ArrayList;
import java.util.Iterator;

@SuppressWarnings({"unused", "RedundantSuppression"})
public class TT_TileEntity_ManualTrafo extends GT_TileEntity_ManualTrafo {

    ArrayList<GT_MetaTileEntity_Hatch_EnergyMulti> mTTEnergyHatches = new ArrayList<>();
    ArrayList<GT_MetaTileEntity_Hatch_DynamoMulti> mTTDynamos = new ArrayList<>();

    public TT_TileEntity_ManualTrafo(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public TT_TileEntity_ManualTrafo(String aName) {
        super(aName);
    }

    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new TT_TileEntity_ManualTrafo(this.mName);
    }

    public boolean addEnergyOutput(long aEU) {
        if (aEU <= 0L) {
            return true;
        } else {
            return mTTDynamos.size() > 0
                    || this.mDynamoHatches.size() > 0 && this.addEnergyOutputMultipleDynamos(aEU, true);
        }
    }

    public boolean addEnergyOutputMultipleDynamos(long aEU, boolean aAllowMixedVoltageDynamos) {
        int injected = 0;
        long totalOutput = 0L;
        long aFirstVoltageFound = -1L;
        boolean aFoundMixedDynamos = false;
        Iterator<GT_MetaTileEntity_Hatch_Dynamo> var10 = this.mDynamoHatches.iterator();

        long aVoltage;
        while (var10.hasNext()) {
            GT_MetaTileEntity_Hatch_Dynamo aDynamo = var10.next();
            if (aDynamo == null) {
                return false;
            }

            if (isValidMetaTileEntity(aDynamo)) {
                aVoltage = aDynamo.maxEUOutput();
                long aTotal = aDynamo.maxAmperesOut() * aVoltage;
                if (aFirstVoltageFound == -1L) {
                    aFirstVoltageFound = aVoltage;
                } else if (aFirstVoltageFound != aVoltage) {
                    aFoundMixedDynamos = true;
                }

                totalOutput += aTotal;
            }
        }

        if (totalOutput < aEU || aFoundMixedDynamos && !aAllowMixedVoltageDynamos) {
            this.explodeMultiblock();
            return false;
        } else {
            Iterator<GT_MetaTileEntity_Hatch_Dynamo> var17 = this.mDynamoHatches.iterator();

            while (true) {
                GT_MetaTileEntity_Hatch_Dynamo aDynamo;
                do {
                    if (!var17.hasNext()) {
                        return injected > 0;
                    }

                    aDynamo = var17.next();
                } while (!isValidMetaTileEntity(aDynamo));

                long leftToInject = aEU - (long) injected;
                aVoltage = aDynamo.maxEUOutput();
                int aAmpsToInject = (int) (leftToInject / aVoltage);
                int aRemainder = (int) (leftToInject - (long) aAmpsToInject * aVoltage);
                int ampsOnCurrentHatch = (int) Math.min(aDynamo.maxAmperesOut(), aAmpsToInject);

                for (int i = 0; i < ampsOnCurrentHatch; ++i) {
                    aDynamo.getBaseMetaTileEntity().increaseStoredEnergyUnits(aVoltage, false);
                }

                injected = (int) ((long) injected + aVoltage * (long) ampsOnCurrentHatch);
                if (aRemainder > 0 && (long) ampsOnCurrentHatch < aDynamo.maxAmperesOut()) {
                    aDynamo.getBaseMetaTileEntity().increaseStoredEnergyUnits(aRemainder, false);
                    injected += aRemainder;
                }
            }
        }
    }

    public boolean drainEnergyInput(long aEU) {
        if (aEU > 0L) {
            {
                Iterator<GT_MetaTileEntity_Hatch_EnergyMulti> var3 = this.mTTEnergyHatches.iterator();

                GT_MetaTileEntity_Hatch_EnergyMulti tHatch;
                do {
                    if (!var3.hasNext()) {
                        return false;
                    }

                    tHatch = var3.next();
                } while (!isValidMetaTileEntity(tHatch)
                        || !tHatch.getBaseMetaTileEntity().decreaseStoredEnergyUnits(aEU, false));
            }
            {
                Iterator<GT_MetaTileEntity_Hatch_Energy> var3 = this.mEnergyHatches.iterator();

                GT_MetaTileEntity_Hatch_Energy tHatch;
                do {
                    if (!var3.hasNext()) {
                        return false;
                    }

                    tHatch = var3.next();
                } while (!isValidMetaTileEntity(tHatch)
                        || !tHatch.getBaseMetaTileEntity().decreaseStoredEnergyUnits(aEU, false));
            }
        }
        return true;
    }
}
