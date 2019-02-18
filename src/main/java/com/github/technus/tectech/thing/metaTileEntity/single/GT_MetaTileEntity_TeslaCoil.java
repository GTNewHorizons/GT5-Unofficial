package com.github.technus.tectech.thing.metaTileEntity.single;

import com.github.technus.tectech.Util;
import com.github.technus.tectech.thing.cover.GT_Cover_TM_TeslaCoil;
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
    private int maxTier = 4; //Max tier of transceiver
    private int minTier = 1; //Min tier of transceiver

    private Map<IGregTechTileEntity, Integer> eTeslaMap = new HashMap<IGregTechTileEntity, Integer>();//Tesla Map to map them tesla bois!
    private int scanTime = 0; //Sets scan time to Z E R O :epic:
    private int scanTimeMin = 100; //Min scan time in ticks
    private int scanTimeTill = scanTimeMin; //Set default scan time
    private int scanRadiusMax = 20; //Tesla scan radius
    private int scanRadiusMin = 4; //Tesla scan radius
    private int scanRadiusLimitTop = scanRadiusMin + (scanRadiusMax - scanRadiusMin) / (maxTier - minTier + 1) * (mTier - 1); //Tesla scan radius Formula
    private int scanRadiusLimitBottom = 1; //Minimum user configurable scanRadius
    private int scanRadius = scanRadiusLimitTop; //Default scanRadius setting
    private int transferRadiusTower = 0; //Radius for transceiver to tower transfers
    private int transferRadiusCover = 0; //Radius for transceiver to cover transfers

    public boolean powerPassToggle = false; //Power Pass for public viewing
    private int histSteps = 20; //Hysteresis Resolution
    private int histSettingLow = 3; //Hysteresis Low Limit
    private int histSettingHigh = 15; //Hysteresis High Limit
    private int histLowLimit = 1; //How low can you configure it?
    private int histHighLimit = histSteps - 1; //How high can you configure it?
    private float histLow = (float) histSettingLow / histSteps; //Power pass is disabled if power is under this fraction
    private float histHigh = (float) histSettingHigh / histSteps; //Power pass is enabled if power is over this fraction

    private long lossPerBlock = 2; //EU lost per block traveled
    private float energyEfficiencyMax = 0.95F; //Max efficiency
    private float energyEfficiencyMin = 0.75F; //Min efficiency
    private float overdriveEfficiency = 0.95F; //Overdrive efficiency
    private float energyEfficiency = energyEfficiencyMin + (energyEfficiencyMax - energyEfficiencyMin) / (maxTier - minTier + 1) * (mTier - 1); //Efficiency Formula
    private float sumOverdriveEfficiency = (2-energyEfficiency)*(2- overdriveEfficiency); //Sum overdrive efficiency formula

    public boolean overDriveToggle = false; //Overdrive toggle
    private long outputVoltage = V[mTier]; //Tesla Voltage Output
    private long outputVoltagePostEfficiency = (long) (outputVoltage * energyEfficiency); //Max power a machine can actually receive
    private long outputVoltagePostOverdrive = (long) (outputVoltage * sumOverdriveEfficiency); //Max power the sender can consume
    private long outputVoltageInjectable = 0; //How much EU will be received post distance losses
    private long outputVoltageConsumption = 0; //How much EU will be drained
    private long outputCurrent = 0; //Tesla Current Output

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
            PlayerChatHelper.SendInfo(aPlayer, "Overdrive Disengaged");
        } else {
            overDriveToggle = true;
            PlayerChatHelper.SendInfo(aPlayer, "Overdrive Engaged");
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
            PlayerChatHelper.SendInfo(aPlayer, "Hysteresis High Changed to " + round(histHigh * 100F) + "%");
        } else {
            if (histSettingLow > histLowLimit) {
                histSettingLow--;
            } else {
                histSettingLow = histSettingHigh - 1;
            }
            histLow = (float) histSettingLow / histSteps;
            PlayerChatHelper.SendInfo(aPlayer, "Hysteresis Low Changed to " + round(histLow * 100F) + "%");
        }
    }

    @Override
    public boolean onWireCutterRightClick(byte aSide, byte aWrenchingSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (aPlayer.isSneaking()) {
            if (scanRadius > scanRadiusLimitBottom) {
                scanRadius--;
            } else {
                scanRadius = scanRadiusLimitTop;
            }
        } else {
            if (scanRadius < scanRadiusLimitTop) {
                scanRadius++;
            } else {
                scanRadius = scanRadiusLimitBottom;
            }
        }
        PlayerChatHelper.SendInfo(aPlayer, "Tesla Radius Changed to " + scanRadius + " Blocks");
        return false;
    }

    // Cheeky skrub stuff to get machine to switch powerPass on soft mallet
    @Override
    public boolean hasAlternativeModeText() {
        return true;
    }

    @Override
    public String getAlternativeModeText() {
        ////Hysteresis based ePowerPass Config
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
        return powerPassToggle ? "Sending Power!" : "Receiving Power!";
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_TeslaCoil(mName, mTier, mDescription, mTextures, mInventory.length);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide()) {

            ////Hysteresis based ePowerPass Config
            long energyMax = getStoredEnergy()[1];
            long energyStored = getStoredEnergy()[0];
            float energyFraction = (float) energyStored / energyMax;

            //ePowerPass hist toggle
            if (!powerPassToggle && energyFraction > histHigh) {
                powerPassToggle = true;
            } else if (powerPassToggle && energyFraction < histLow) {
                powerPassToggle = false;
            }

            ////Scanning for active teslas
            scanTime++;
            if (scanTime >= scanTimeTill) {
                scanTime = 0;
                eTeslaMap.clear();

                for (int xPosOffset = -scanRadius; xPosOffset <= scanRadius; xPosOffset++) {
                    for (int yPosOffset = -scanRadius; yPosOffset <= scanRadius; yPosOffset++) {
                        for (int zPosOffset = -scanRadius; zPosOffset <= scanRadius; zPosOffset++) {
                            if (xPosOffset == 0 && yPosOffset == 0 && zPosOffset == 0) {
                                continue;
                            }
                            IGregTechTileEntity node = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xPosOffset, yPosOffset, zPosOffset);
                            if (node == null) {
                                continue;
                            }
                            IMetaTileEntity nodeInside = node.getMetaTileEntity();
                            if (nodeInside instanceof GT_MetaTileEntity_TM_teslaCoil && node.isActive() || (node.getCoverBehaviorAtSide((byte) 1) instanceof GT_Cover_TM_TeslaCoil)) {
                                eTeslaMap.put(node, (int) Math.ceil(Math.sqrt(xPosOffset * xPosOffset + yPosOffset * yPosOffset + zPosOffset * zPosOffset)));
                            }
                        }
                    }
                }
            }

            //Stuff to do if ePowerPass
            if (powerPassToggle) {
                outputCurrent = mBatteryCount;
                transferRadiusTower = (int) (scanRadius * energyFraction);
                transferRadiusCover = (int) (transferRadiusTower / 1.5);

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
                        if (getEUVar() >= (overDriveToggle ? outputVoltage*2 : outputVoltage)) {
                            IGregTechTileEntity node = Rx.getKey();
                            IMetaTileEntity nodeInside = node.getMetaTileEntity();
                            if (overDriveToggle){
                                outputVoltageInjectable = outputVoltage;
                                outputVoltageConsumption = outputVoltagePostOverdrive + (lossPerBlock * Rx.getValue());
                            } else {
                                outputVoltageInjectable = outputVoltagePostEfficiency - (lossPerBlock * Rx.getValue());
                                outputVoltageConsumption = outputVoltage;
                            }
                            if (nodeInside instanceof GT_MetaTileEntity_TM_teslaCoil && Rx.getValue() <= transferRadiusTower) {
                                GT_MetaTileEntity_TM_teslaCoil nodeTesla = (GT_MetaTileEntity_TM_teslaCoil) nodeInside;
                                if (!nodeTesla.powerPassToggle) {
                                   if (nodeTesla.getEUVar() + outputVoltageInjectable <= (nodeTesla.maxEUStore() / 2)) {
                                       setEUVar(getEUVar() - outputVoltageConsumption);
                                       node.increaseStoredEnergyUnits(outputVoltageInjectable, true);
                                       outputCurrent--;
                                       idle = false;
                                   }
                                }
                            } else if ((node.getCoverBehaviorAtSide((byte) 1) instanceof GT_Cover_TM_TeslaCoil) && Rx.getValue() <= transferRadiusCover) {
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
}