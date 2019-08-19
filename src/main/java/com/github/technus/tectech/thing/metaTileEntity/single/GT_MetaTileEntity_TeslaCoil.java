package com.github.technus.tectech.thing.metaTileEntity.single;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.Util;
import com.github.technus.tectech.loader.NetworkDispatcher;
import com.github.technus.tectech.mechanics.data.RendererMessage;
import com.github.technus.tectech.thing.cover.GT_Cover_TM_TeslaCoil;
import com.github.technus.tectech.thing.cover.GT_Cover_TM_TeslaCoil_Ultimate;
import com.github.technus.tectech.thing.metaTileEntity.multi.GT_MetaTileEntity_TM_teslaCoil;
import eu.usrv.yamcore.auxiliary.PlayerChatHelper;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicBatteryBuffer;
import net.minecraft.entity.player.EntityPlayer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static com.github.technus.tectech.CommonValues.V;
import static com.github.technus.tectech.Util.entriesSortedByValues;
import static com.github.technus.tectech.Util.map;
import static com.github.technus.tectech.thing.metaTileEntity.Textures.TESLA_TRANSCEIVER_TOP_BA;
import static java.lang.Math.round;


public class GT_MetaTileEntity_TeslaCoil extends GT_MetaTileEntity_BasicBatteryBuffer {
    private final static HashSet<Util.thaumSpark> sparkList = new HashSet<Util.thaumSpark>();
    private byte sparkCount = 0;

    private int maxTier = 4; //Max tier of transceiver
    private int minTier = 0; //Min tier of transceiver

    public Map<IGregTechTileEntity, Integer> eTeslaMap = new HashMap();//Tesla Map to map them tesla bois!

    private int transferRadiusMax = 20;
    private int transferRadiusMin = 4;
    private int transferRadiusLimitTop = (int) map(mTier + 1, minTier + 1, maxTier + 1, transferRadiusMin, transferRadiusMax);
    private int transferRadiusLimitBottom = 1; //Minimum user configurable
    private int transferRadius = transferRadiusLimitTop; //Default transferRadius setting
    private int transferRadiusTower = 0; //Radius for transceiver to tower transfers
    private int transferRadiusCover = 0; //Radius for transceiver to cover transfers

    public boolean powerPassToggle = false; //Power Pass for public viewing
    private int histSteps = 20; //Hysteresis Resolution
    private int histSettingLow = 3; //Hysteresis Low Limit
    private int histSettingHigh = 15; //Hysteresis High Limit
    private int histLowLimit = 1; //How low can you configure it?
    private int histHighLimit = 19; //How high can you configure it?
    private float histLow = (float) histSettingLow / histSteps; //Power pass is disabled if power is under this fraction
    private float histHigh = (float) histSettingHigh / histSteps; //Power pass is enabled if power is over this fraction

    private long outputVoltage = V[mTier];
    private float minEfficiency = TecTech.configTecTech.TESLA_SINGLE_MIN_EFFICIENCY;//Default is 0.91F
    private float maxEfficiency = TecTech.configTecTech.TESLA_SINGLE_MAX_EFFICIENCY;//Default is 0.95F
    private float overdriveEfficiencyExtra = TecTech.configTecTech.TESLA_SINGLE_OVERDRIVE_LOSS;//Default is 0.010F
    private float energyEfficiency = map(mTier + 1, minTier + 1, maxTier + 1, minEfficiency, maxEfficiency);
    private float overdriveEfficiency = energyEfficiency - overdriveEfficiencyExtra;
    private boolean overdriveToggle = false;

    public GT_MetaTileEntity_TeslaCoil(int aID, String aName, String aNameRegional, int aTier, int aSlotCount) {
        super(aID, aName, aNameRegional, aTier, "Tesla Coil Transceiver", aSlotCount);
        Util.setTier(aTier, this);
    }

    public GT_MetaTileEntity_TeslaCoil(String aName, int aTier, String aDescription, ITexture[][][] aTextures, int aSlotCount) {
        super(aName, aTier, aDescription, aTextures, aSlotCount);
    }

    @Override
    public boolean onSolderingToolRightClick(byte aSide, byte aWrenchingSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (overdriveToggle) {
            overdriveToggle = false;
            PlayerChatHelper.SendInfo(aPlayer, "Overdrive disengaged");
        } else {
            overdriveToggle = true;
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
    public boolean isFacingValid(byte aSide) {
        return aSide != 1;
    }//Prevents output at the top side

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        ITexture[][][] rTextures = new ITexture[3][17][];

        for (byte i = -1; i < 16; ++i) {
            rTextures[0][i + 1] = new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier][i + 1]};
            rTextures[1][i + 1] = new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier][i + 1], TESLA_TRANSCEIVER_TOP_BA};
            rTextures[2][i + 1] = new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier][i + 1], this.mInventory.length == 16 ? Textures.BlockIcons.OVERLAYS_ENERGY_OUT_POWER[this.mTier] : (this.mInventory.length > 4 ? Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI[this.mTier] : Textures.BlockIcons.OVERLAYS_ENERGY_OUT[this.mTier])};
        }

        return rTextures;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        return this.mTextures[aSide == aFacing ? 2 : aSide == 1 ? 1 : 0][aColorIndex + 1];
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_TeslaCoil(mName, mTier, mDescription, mTextures, mInventory.length);
    }

    private void thaumLightning(IGregTechTileEntity mte, IGregTechTileEntity node) {
        int x = mte.getXCoord();
        int y = mte.getYCoord();
        int z = mte.getZCoord();

        byte xR;
        byte yR;
        byte zR;

        IMetaTileEntity nodeInside = node.getMetaTileEntity();
        if (nodeInside instanceof GT_MetaTileEntity_TM_teslaCoil) {
            GT_MetaTileEntity_TM_teslaCoil nodeTesla = (GT_MetaTileEntity_TM_teslaCoil) nodeInside;
            xR = (byte) (nodeTesla.xPosTop - x);
            yR = (byte) (nodeTesla.yPosTop - y);
            zR = (byte) (nodeTesla.zPosTop - z);
        } else {
            xR = (byte) (node.getXCoord() - x);
            yR = (byte) (node.getYCoord() - y);
            zR = (byte) (node.getZCoord() - z);
        }

        int wID = mte.getWorld().provider.dimensionId;

        sparkList.add(new Util.thaumSpark(x, y, z, xR, yR, zR, wID));
    }

    private long getEnergyEfficiency(long voltage, int distance, boolean overDriveToggle) {
        if (overDriveToggle) {
            return (long) ((voltage * 2) - (voltage * Math.pow(overdriveEfficiency, distance)));
        } else {
            return (long) (voltage * Math.pow(energyEfficiency, distance));
        }
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isClientSide()) {
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
                    if (getEUVar() >= (overdriveToggle ? outputVoltage * 2 : outputVoltage)) {
                        IGregTechTileEntity node = Rx.getKey();
                        IMetaTileEntity nodeInside = node.getMetaTileEntity();

                        long outputVoltageInjectable;
                        long outputVoltageConsumption;
                        if (overdriveToggle) {
                            outputVoltageInjectable = outputVoltage;
                            outputVoltageConsumption = getEnergyEfficiency(outputVoltage, Rx.getValue(), true);
                        } else {
                            outputVoltageInjectable = getEnergyEfficiency(outputVoltage, Rx.getValue(), false);
                            outputVoltageConsumption = outputVoltage;
                        }
                        if (nodeInside instanceof GT_MetaTileEntity_TM_teslaCoil && Rx.getValue() <= transferRadiusTower) {
                            GT_MetaTileEntity_TM_teslaCoil nodeTesla = (GT_MetaTileEntity_TM_teslaCoil) nodeInside;
                            if (!nodeTesla.ePowerPass) {
                                if (nodeTesla.getEUVar() + outputVoltageInjectable <= (nodeTesla.maxEUStore() / 2)) {
                                    setEUVar(getEUVar() - outputVoltageConsumption);
                                    node.increaseStoredEnergyUnits(outputVoltageInjectable, true);
                                    thaumLightning(aBaseMetaTileEntity, node);
                                    outputCurrent--;
                                    idle = false;
                                }
                            }
                        } else if ((node.getCoverBehaviorAtSide((byte) 1) instanceof GT_Cover_TM_TeslaCoil) && !(node.getCoverBehaviorAtSide((byte) 1) instanceof GT_Cover_TM_TeslaCoil_Ultimate) && Rx.getValue() <= transferRadiusCover) {
                            if (node.injectEnergyUnits((byte) 1, outputVoltageInjectable, 1L) > 0L) {
                                setEUVar(getEUVar() - outputVoltageConsumption);
                                thaumLightning(aBaseMetaTileEntity, node);
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
        sparkCount++;
        if (sparkCount == 60 && !sparkList.isEmpty()) {
            sparkCount = 0;
            NetworkDispatcher.INSTANCE.sendToAllAround(new RendererMessage.RendererData(sparkList),
                    aBaseMetaTileEntity.getWorld().provider.dimensionId,
                    aBaseMetaTileEntity.getXCoord(),
                    aBaseMetaTileEntity.getYCoord(),
                    aBaseMetaTileEntity.getZCoord(),
                    256);
        }
        sparkList.clear();
    }
}