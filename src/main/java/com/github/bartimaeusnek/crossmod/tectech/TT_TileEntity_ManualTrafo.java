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

public class TT_TileEntity_ManualTrafo extends GT_TileEntity_ManualTrafo {

    ArrayList<GT_MetaTileEntity_Hatch_EnergyMulti> mTTEnerys = new ArrayList<>();
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
            return mTTDynamos.size() > 0 || this.mDynamoHatches.size() > 0 && this.addEnergyOutputMultipleDynamos(aEU, true);
        }
    }


    public boolean addEnergyOutputMultipleDynamos(long aEU, boolean aAllowMixedVoltageDynamos) {
        int injected = 0;
        long totalOutput = 0L;
        long aFirstVoltageFound = -1L;
        boolean aFoundMixedDynamos = false;
        Iterator var10 = this.mDynamoHatches.iterator();

        long aVoltage;
        while(var10.hasNext()) {
            GT_MetaTileEntity_Hatch_Dynamo aDynamo = (GT_MetaTileEntity_Hatch_Dynamo)var10.next();
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
            Iterator var17 = this.mDynamoHatches.iterator();

            while(true) {
                GT_MetaTileEntity_Hatch_Dynamo aDynamo;
                do {
                    if (!var17.hasNext()) {
                        return injected > 0;
                    }

                    aDynamo = (GT_MetaTileEntity_Hatch_Dynamo)var17.next();
                } while(!isValidMetaTileEntity(aDynamo));

                long leftToInject = aEU - (long)injected;
                aVoltage = aDynamo.maxEUOutput();
                int aAmpsToInject = (int)(leftToInject / aVoltage);
                int aRemainder = (int)(leftToInject - (long)aAmpsToInject * aVoltage);
                int ampsOnCurrentHatch = (int)Math.min(aDynamo.maxAmperesOut(), (long)aAmpsToInject);

                for(int i = 0; i < ampsOnCurrentHatch; ++i) {
                    aDynamo.getBaseMetaTileEntity().increaseStoredEnergyUnits(aVoltage, false);
                }

                injected = (int)((long)injected + aVoltage * (long)ampsOnCurrentHatch);
                if (aRemainder > 0 && (long)ampsOnCurrentHatch < aDynamo.maxAmperesOut()) {
                    aDynamo.getBaseMetaTileEntity().increaseStoredEnergyUnits((long)aRemainder, false);
                    injected += aRemainder;
                }
            }
        }
    }



    public boolean drainEnergyInput(long aEU) {
        if (aEU <= 0L) {
            return true;
        } else {
            {
                Iterator var3 = this.mTTEnerys.iterator();

                GT_MetaTileEntity_Hatch_EnergyMulti tHatch;
                do {
                    if (!var3.hasNext()) {
                        return false;
                    }

                    tHatch = (GT_MetaTileEntity_Hatch_EnergyMulti)var3.next();
                } while(!isValidMetaTileEntity(tHatch) || !tHatch.getBaseMetaTileEntity().decreaseStoredEnergyUnits(aEU, false));
            }
            {
                Iterator var3 = this.mEnergyHatches.iterator();

                GT_MetaTileEntity_Hatch_Energy tHatch;
                do {
                    if (!var3.hasNext()) {
                        return false;
                    }

                    tHatch = (GT_MetaTileEntity_Hatch_Energy)var3.next();
                } while(!isValidMetaTileEntity(tHatch) || !tHatch.getBaseMetaTileEntity().decreaseStoredEnergyUnits(aEU, false));
            }

            return true;
        }
    }
}