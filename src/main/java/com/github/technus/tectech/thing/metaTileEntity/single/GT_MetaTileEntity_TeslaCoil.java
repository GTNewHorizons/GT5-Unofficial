package com.github.technus.tectech.thing.metaTileEntity.single;

import com.github.technus.tectech.Util;
import com.github.technus.tectech.thing.cover.GT_Cover_TM_TeslaCoil;
import com.github.technus.tectech.thing.cover.GT_Cover_TM_TeslaCoil_Ultimate;
import com.github.technus.tectech.thing.metaTileEntity.multi.GT_MetaTileEntity_TM_teslaCoil;
import eu.usrv.yamcore.auxiliary.PlayerChatHelper;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicBatteryBuffer;
import net.minecraft.entity.player.EntityPlayer;


import java.util.*;

import static com.github.technus.tectech.CommonValues.V;
import static com.github.technus.tectech.Util.entriesSortedByValues;
import static java.lang.Math.round;


public class GT_MetaTileEntity_TeslaCoil extends GT_MetaTileEntity_BasicBatteryBuffer {
    private int maxTier = 3; //Max tier of transceiver
    private int minTier = 0; //Min tier of transceiver

    public Map<IGregTechTileEntity, Integer> eTeslaMap = new HashMap();//Tesla Map to map them tesla bois!

    //TODO Rewrite range limits, use new formula
    private int transferRadiusMax = 20; //Tesla scan radius
    private int transferRadiusMin = 4; //Tesla scan radius
    private int transferRadiusLimitTop = transferRadiusMin + (transferRadiusMax - transferRadiusMin) / (maxTier - minTier + 1) * mTier; //Tesla scan radius Formula
    private int transferRadiusLimitBottom = 1; //Minimum user configurable
    private int transferRadius = transferRadiusLimitTop; //Default transferRadius setting
    private int transferRadiusTower = 0; //Radius for transceiver to tower transfers
    private int transferRadiusCover = 0; //Radius for transceiver to cover transfers

    //TODO Rewrite histSetting to improve readability
    public boolean powerPassToggle = false; //Power Pass for public viewing
    private int histSteps = 20; //Hysteresis Resolution
    private int histSettingLow = 3; //Hysteresis Low Limit
    private int histSettingHigh = 15; //Hysteresis High Limit
    private int histLowLimit = 1; //How low can you configure it?
    private int histHighLimit = histSteps - 1; //How high can you configure it?
    private float histLow = (float) histSettingLow / histSteps; //Power pass is disabled if power is under this fraction
    private float histHigh = (float) histSettingHigh / histSteps; //Power pass is enabled if power is over this fraction

    //TODO Rewrite efficiency formulas
    private long lossPerBlock = 2; //EU lost per block traveled
    private float energyEfficiencyMax = 0.95F; //Max efficiency
    private float energyEfficiencyMin = 0.75F; //Min efficiency
    private float overdriveEfficiency = 0.95F; //Overdrive efficiency
    private float energyEfficiency = energyEfficiencyMin + (energyEfficiencyMax - energyEfficiencyMin) / (maxTier - minTier + 1) * mTier; //Efficiency Formula
    private float sumOverdriveEfficiency = (2 - energyEfficiency) * (2 - overdriveEfficiency); //Sum overdrive efficiency formula

    //TODO Rewrite over drive efficiency formulas
    private boolean overDriveToggle = false; //Overdrive toggle
    private long outputVoltage = V[mTier]; //Tesla Voltage Output
    private long outputVoltagePostEfficiency = (long) (outputVoltage * energyEfficiency); //Max power a machine can actually receive
    private long outputVoltagePostOverdrive = (long) (outputVoltage * sumOverdriveEfficiency); //Max power the sender can consume

    public GT_MetaTileEntity_TeslaCoil(int aID, String aName, String aNameRegional, int aTier, int aSlotCount) {
        super(aID, aName, aNameRegional, aTier, "Tesla Coil Transceiver", aSlotCount);
        Util.setTier(aTier, this);
    }

    public GT_MetaTileEntity_TeslaCoil(String aName, int aTier, String aDescription, ITexture[][][] aTextures, int aSlotCount) {
        super(aName, aTier, aDescription, aTextures, aSlotCount);
    }

    @Override
    public boolean onSolderingToolRightClick(byte aSide, byte aWrenchingSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (overDriveToggle) {
            overDriveToggle = false;
            PlayerChatHelper.SendInfo(aPlayer, "Overdrive disengaged");
        } else {
            overDriveToggle = true;
            PlayerChatHelper.SendInfo(aPlayer, "Overdrive engaged");
        }
        return true;
    }

    @Override
    public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (aPlayer.isSneaking()) {
            if (histSettingHigh < histHighLimit) {
                histSettingHigh++;
            } else {
                histSettingHigh = histSettingLow + 1;
            }
            histHigh = (float) histSettingHigh / histSteps;
            PlayerChatHelper.SendInfo(aPlayer, "Hysteresis high set to " + round(histHigh * 100F) + "%");
        } else {
            if (histSettingLow > histLowLimit) {
                histSettingLow--;
            } else {
                histSettingLow = histSettingHigh - 1;
            }
            histLow = (float) histSettingLow / histSteps;
            PlayerChatHelper.SendInfo(aPlayer, "Hysteresis low set to " + round(histLow * 100F) + "%");
        }
    }

    @Override
    public boolean onWireCutterRightClick(byte aSide, byte aWrenchingSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (aPlayer.isSneaking()) {
            if (transferRadius > transferRadiusLimitBottom) {
                transferRadius--;
            }
        } else {
            if (transferRadius < 0) {
                transferRadius++;
            }
        }
        PlayerChatHelper.SendInfo(aPlayer, "Tesla radius set to " + transferRadius + "m");
        return false;
    }

    // Cheeky skrub stuff to get machine to switch powerPass on soft mallet
    @Override
    public boolean hasAlternativeModeText() {
        return true;
    }

    @Override
    public String getAlternativeModeText() {
        //Hysteresis based ePowerPass Config
        long energyMax = getStoredEnergy()[1];
        long energyStored = getStoredEnergy()[0];
        float energyFrac = (float) energyStored / energyMax;

        //ePowerPass hist toggle
        if (energyFrac > histHigh) {
            powerPassToggle = true;
        } else if (energyFrac < histLow) {
            powerPassToggle = false;
        } else {
            powerPassToggle = !powerPassToggle;
        }

        //And after this cheeky-ness, toss the string XD
        return powerPassToggle ? "Sending power!" : "Receiving power!";
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_TeslaCoil(mName, mTier, mDescription, mTextures, mInventory.length);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isClientSide() || eTeslaMap.isEmpty()) {
            return;
        }

        //Hysteresis based ePowerPass Config
        long energyMax = getStoredEnergy()[1];
        long energyStored = getStoredEnergy()[0];
        float energyFrac = (float) energyStored / energyMax;

        //ePowerPass hist toggle
        if (!powerPassToggle && energyFrac > histHigh) {
            powerPassToggle = true;
        } else if (powerPassToggle && energyFrac < histLow) {
            powerPassToggle = false;
        }

        //Stuff to do if ePowerPass
        if (powerPassToggle) {
            float rangeFrac = (float) ((-0.5 * Math.pow(energyFrac, 2)) + (1.5 * energyFrac));
            long outputCurrent = mBatteryCount;
            transferRadiusTower = (int) (transferRadius * rangeFrac);
            transferRadiusCover = (int) (transferRadiusTower / 1.25);

            //Clean the eTeslaMap
            for (Map.Entry<IGregTechTileEntity, Integer> Rx : eTeslaMap.entrySet()) {
                IGregTechTileEntity node = Rx.getKey();
                if (node != null) {
                    IMetaTileEntity nodeInside = node.getMetaTileEntity();
                    try {
                        if (nodeInside instanceof GT_MetaTileEntity_TM_teslaCoil && node.isActive()) {
                            GT_MetaTileEntity_TM_teslaCoil teslaTower = (GT_MetaTileEntity_TM_teslaCoil) nodeInside;
                            if (teslaTower.maxEUStore() > 0) {
                                continue;
                            }
                        } else if ((node.getCoverBehaviorAtSide((byte) 1) instanceof GT_Cover_TM_TeslaCoil) && node.getEUCapacity() > 0) {
                            continue;
                        }
                    } catch (Exception e) {
                    }
                }
                eTeslaMap.remove(Rx.getKey());
            }

            //Power transfer
            while (outputCurrent > 0) {
                boolean idle = true;
                for (Map.Entry<IGregTechTileEntity, Integer> Rx : entriesSortedByValues(eTeslaMap)) {
                    if (getEUVar() >= (overDriveToggle ? outputVoltage * 2 : outputVoltage)) {
                        IGregTechTileEntity node = Rx.getKey();
                        IMetaTileEntity nodeInside = node.getMetaTileEntity();

                        long outputVoltageInjectable;
                        long outputVoltageConsumption;
                        if (overDriveToggle) {
                            outputVoltageInjectable = outputVoltage;
                            outputVoltageConsumption = outputVoltagePostOverdrive + (lossPerBlock * Rx.getValue());
                        } else {
                            outputVoltageInjectable = outputVoltagePostEfficiency - (lossPerBlock * Rx.getValue());
                            outputVoltageConsumption = outputVoltage;
                        }
                        if (nodeInside instanceof GT_MetaTileEntity_TM_teslaCoil && Rx.getValue() <= transferRadiusTower) {
                            GT_MetaTileEntity_TM_teslaCoil nodeTesla = (GT_MetaTileEntity_TM_teslaCoil) nodeInside;
                            if (!nodeTesla.ePowerPass) {
                                if (nodeTesla.getEUVar() + outputVoltageInjectable <= (nodeTesla.maxEUStore() / 2)) {
                                    setEUVar(getEUVar() - outputVoltageConsumption);
                                    node.increaseStoredEnergyUnits(outputVoltageInjectable, true);
                                    outputCurrent--;
                                    idle = false;
                                }
                            }
                        } else if ((node.getCoverBehaviorAtSide((byte) 1) instanceof GT_Cover_TM_TeslaCoil) && !(node.getCoverBehaviorAtSide((byte) 1) instanceof GT_Cover_TM_TeslaCoil_Ultimate) && Rx.getValue() <= transferRadiusCover) {
                            if (node.injectEnergyUnits((byte) 1, outputVoltageInjectable, 1L) > 0L) {
                                setEUVar(getEUVar() - outputVoltageConsumption);
                                outputCurrent--;
                                idle = false;
                            }
                        }
                        if (outputCurrent == 0) {
                            break;
                        }
                    } else {
                        idle = true;
                        break;
                    }
                }
                if (idle) {
                    break;
                }
            }
        }
    }
}