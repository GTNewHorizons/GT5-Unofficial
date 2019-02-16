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
import gregtech.api.util.GT_ModHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.*;

import static com.github.technus.tectech.CommonValues.V;
import static com.github.technus.tectech.Util.entriesSortedByValues;
import static java.lang.Math.round;


public class GT_MetaTileEntity_TeslaCoil extends GT_MetaTileEntity_BasicBatteryBuffer {
    private int maixTier = 4; //Max tier of transceiver
    private int minTier = 1; //Min tier of transceiver

    private Map<IGregTechTileEntity, Integer> eTeslaMap = new HashMap<IGregTechTileEntity, Integer>();//Tesla Map to map them tesla bois!
    private int scanTime = 0; //Sets scan time to Z E R O :epic:
    private int scanTimeMin = 100; //Min scan time in ticks
    private int scanTimeTill = scanTimeMin; //Set default scan time
    private int scanRadiusMax = 20; //Tesla scan radius
    private int scanRadiusMin = 4; //Tesla scan radius
    private int scanRadiusLimitTop = scanRadiusMin + (scanRadiusMax - scanRadiusMin) / (maixTier - minTier + 1) * (mTier - 1); //Tesla scan actual maximum radius Formula
    private int scanRadiusLimitBottom = 1; //Configurable Scan range minimum
    private int scanRadius = scanRadiusLimitTop; // Sets default scan radius first run
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
    private float energyEfficiency = energyEfficiencyMin + (energyEfficiencyMax - energyEfficiencyMin) / (maixTier - minTier + 1) * (mTier - 1); //Energy efficiency Formula

    private long outputVoltage = V[mTier]; //Tesla Voltage Output
    private long outputVoltagePostEfficency = (long) (outputVoltage * energyEfficiency); //Max power a machine can actually receive
    private long outputVoltageInjectable = 0; //How much EU will be received post distance losses
    private long outputCurrent = 0; //Tesla Current Output

    public GT_MetaTileEntity_TeslaCoil(int aID, String aName, String aNameRegional, int aTier, int aSlotCount) {
        super(aID, aName, aNameRegional, aTier, "Tesla Coil Transceiver", aSlotCount);
        Util.setTier(aTier, this);
    }

    public GT_MetaTileEntity_TeslaCoil(String aName, int aTier, String aDescription, ITexture[][][] aTextures, int aSlotCount) {
        super(aName, aTier, aDescription, aTextures, aSlotCount);
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
        PlayerChatHelper.SendInfo(aPlayer, "Scan Radius Limit Changed to " + scanRadius + " Blocks");
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
        if (aBaseMetaTileEntity.isServerSide()) {
            IGregTechTileEntity mte = getBaseMetaTileEntity();

            //region BasicBatteryBuffer
            this.mCharge = aBaseMetaTileEntity.getStoredEU() / 2L > aBaseMetaTileEntity.getEUCapacity() / 3L;
            this.mDecharge = aBaseMetaTileEntity.getStoredEU() < aBaseMetaTileEntity.getEUCapacity() / 3L;
            this.mBatteryCount = 0;
            this.mChargeableCount = 0;
            ItemStack[] var4 = this.mInventory;

            int var5 = var4.length;
            for (int var6 = 0; var6 < var5; ++var6) {
                ItemStack tStack = var4[var6];
                if (GT_ModHandler.isElectricItem(tStack, this.mTier)) {
                    if (GT_ModHandler.isChargerItem(tStack)) {
                        ++this.mBatteryCount;
                    }
                    ++this.mChargeableCount;
                }
            }
            //endregion

            ////Hysteresis based ePowerPass Config
            long energyMax = getStoredEnergy()[1];
            long energyStored = getStoredEnergy()[0];
            float energyFrac = (float) energyStored / energyMax;

            //ePowerPass hist toggle
            if (!powerPassToggle && energyFrac > histHigh) {
                powerPassToggle = true;
            } else if (powerPassToggle && energyFrac < histLow) {
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
                            IGregTechTileEntity node = mte.getIGregTechTileEntityOffset(xPosOffset, yPosOffset, zPosOffset);
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
                transferRadiusTower = (int) (scanRadius * energyFrac);
                transferRadiusCover = (int) (transferRadiusTower/1.5);

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
                        if (getEUVar() >= outputVoltage) {
                            IGregTechTileEntity node = Rx.getKey();
                            IMetaTileEntity nodeInside = node.getMetaTileEntity();
                            outputVoltageInjectable = outputVoltagePostEfficency - (lossPerBlock * Rx.getValue());
                            if (nodeInside instanceof GT_MetaTileEntity_TM_teslaCoil && Rx.getValue() <= transferRadiusTower) {
                                GT_MetaTileEntity_TM_teslaCoil nodeTesla = (GT_MetaTileEntity_TM_teslaCoil) nodeInside;
                                if (!nodeTesla.powerPassToggle) {
                                    if (nodeTesla.getEUVar() + outputVoltage <= (nodeTesla.maxEUStore() / 2)) {
                                        setEUVar(getEUVar() - outputVoltage);
                                        node.increaseStoredEnergyUnits(outputVoltageInjectable, true);
                                        outputCurrent--;
                                        idle = false;
                                    }
                                }
                            } else if ((node.getCoverBehaviorAtSide((byte) 1) instanceof GT_Cover_TM_TeslaCoil) && Rx.getValue() <= transferRadiusCover) {
                                if (node.injectEnergyUnits((byte) 1, outputVoltageInjectable, 1L) > 0L) {
                                    setEUVar(getEUVar() - outputVoltage);
                                    outputCurrent--;
                                    idle = false;
                                }
                            }
                            if (outputCurrent == 0) {
                                break;
                            }
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
