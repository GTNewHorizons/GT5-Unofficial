package com.github.technus.tectech.thing.metaTileEntity.single;

import com.github.technus.tectech.util.CommonValues;
import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.util.Util;
import com.github.technus.tectech.loader.NetworkDispatcher;
import com.github.technus.tectech.mechanics.spark.RendererMessage;
import com.github.technus.tectech.mechanics.spark.ThaumSpark;
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
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static com.github.technus.tectech.thing.metaTileEntity.multi.GT_MetaTileEntity_TM_teslaCoil.teslaNodeSet;
import static com.github.technus.tectech.util.CommonValues.V;
import static com.github.technus.tectech.util.Util.entriesSortedByValues;
import static com.github.technus.tectech.util.Util.map;
import static com.github.technus.tectech.thing.metaTileEntity.Textures.*;
import static java.lang.Math.round;
import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;


public class GT_MetaTileEntity_TeslaCoil extends GT_MetaTileEntity_BasicBatteryBuffer {
    private final static int perBlockLoss = TecTech.configTecTech.TESLA_SINGLE_LOSS_PER_BLOCK;//Default is 1
    private final static float overDriveLoss = TecTech.configTecTech.TESLA_SINGLE_OVERDRIVE_LOSS_FACTOR;//Default is 0.25F

    public Map<IGregTechTileEntity, Integer> eTeslaMap = new HashMap<>();//Tesla Map to map them tesla bois!
    private final static HashSet<ThaumSpark> sparkList = new HashSet<>();
    private byte sparkCount = 0;

    private final static int maxTier = 4; //Max tier of transceiver
    private final static int minTier = 0; //Min tier of transceiver

    private final static int transferRadiusMax = 20;
    private final static int transferRadiusMin = 4;
    private final int transferRadiusLimitTop = (int) map(mTier + 1, minTier + 1, maxTier + 1, transferRadiusMin, transferRadiusMax);
    private final static int transferRadiusLimitBottom = 1; //Minimum user configurable
    private int transferRadius = transferRadiusLimitTop; //Default transferRadius setting

    public boolean powerPassToggle = false; //Power Pass for public viewing
    private final static int histSteps = 20; //Hysteresis Resolution
    private int histSettingLow = 3; //Hysteresis Low Limit
    private int histSettingHigh = 15; //Hysteresis High Limit
    private final static int histLowLimit = 1; //How low can you configure it?
    private final static int histHighLimit = 19; //How high can you configure it?
    private float histLow = (float) histSettingLow / histSteps; //Power pass is disabled if power is under this fraction
    private float histHigh = (float) histSettingHigh / histSteps; //Power pass is enabled if power is over this fraction

    private final long outputVoltage = V[mTier];
    private boolean overdriveToggle = false;

    private String clientLocale = "en_US";

    public GT_MetaTileEntity_TeslaCoil(int aID, String aName, String aNameRegional, int aTier, int aSlotCount) {
        super(aID, aName, aNameRegional, aTier, "", aSlotCount);
        Util.setTier(aTier, this);
    }

    public GT_MetaTileEntity_TeslaCoil(String aName, int aTier, String aDescription, ITexture[][][] aTextures, int aSlotCount) {
        super(aName, aTier, aDescription, aTextures, aSlotCount);
    }

    @Override
    public String[] getDescription() {
        String[] jargon = new String[]{
                CommonValues.BASS_MARK,
                translateToLocal("gt.blockmachines.machine.tt.tesla.desc.0"),//Your Tesla I/O machine of choice
                EnumChatFormatting.AQUA + translateToLocal("gt.blockmachines.machine.tt.tesla.desc.1")//Lightning stoves for the rich
        };
        String[] sDesc = super.getDescription();
        sDesc = Arrays.copyOfRange(sDesc, 1, sDesc.length);//Removes first element from array
        return ArrayUtils.addAll(jargon, sDesc);
    }

    @Override
    public boolean onSolderingToolRightClick(byte aSide, byte aWrenchingSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (overdriveToggle) {
            overdriveToggle = false;
            PlayerChatHelper.SendInfo(aPlayer, translateToLocalFormatted("tt.keyphrase.Overdrive_disengaged", clientLocale));
        } else {
            overdriveToggle = true;
            PlayerChatHelper.SendInfo(aPlayer, translateToLocalFormatted("tt.keyphrase.Overdrive_engaged", clientLocale));
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
            PlayerChatHelper.SendInfo(aPlayer, translateToLocalFormatted("tt.keyphrase.Hysteresis_high_set_to", clientLocale) + " " + round(histHigh * 100F) + "%");
        } else {
            if (histSettingLow > histLowLimit) {
                histSettingLow--;
            } else {
                histSettingLow = histSettingHigh - 1;
            }
            histLow = (float) histSettingLow / histSteps;
            PlayerChatHelper.SendInfo(aPlayer, translateToLocalFormatted("tt.keyphrase.Hysteresis_low_set_to", clientLocale) + " " + round(histLow * 100F) + "%");
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
        PlayerChatHelper.SendInfo(aPlayer, translateToLocalFormatted("tt.keyphrase.Tesla_radius_set_to", clientLocale) + " " + transferRadius + "m");
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
        return powerPassToggle ? translateToLocalFormatted("tt.keyphrase.Sending_power", clientLocale) + "!" : translateToLocalFormatted("tt.keyphrase.Receiving_power", clientLocale) + "!";
    }

    @Override
    public boolean isFacingValid(byte aSide) {
        return aSide != 1;
    }//Prevents output at the top side

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        ITexture[][][] rTextures = new ITexture[3][17][];

        for (byte i = -1; i < 16; ++i) {
            rTextures[0][i + 1] = new ITexture[]{MACHINE_CASINGS_TT[this.mTier][i + 1]};
            rTextures[1][i + 1] = new ITexture[]{MACHINE_CASINGS_TT[this.mTier][i + 1], TESLA_TRANSCEIVER_TOP_BA};
            rTextures[2][i + 1] = new ITexture[]{MACHINE_CASINGS_TT[this.mTier][i + 1], this.mInventory.length == 16 ? OVERLAYS_ENERGY_OUT_POWER_TT[this.mTier] : (this.mInventory.length > 4 ? OVERLAYS_ENERGY_OUT_MULTI_TT[this.mTier] : OVERLAYS_ENERGY_OUT_TT[this.mTier])};
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
            xR = (byte) (nodeTesla.posTop.get0() - x);
            yR = (byte) (nodeTesla.posTop.get1() - y);
            zR = (byte) (nodeTesla.posTop.get2() - z);
        } else {
            xR = (byte) (node.getXCoord() - x);
            yR = (byte) (node.getYCoord() - y);
            zR = (byte) (node.getZCoord() - z);
        }

        int wID = mte.getWorld().provider.dimensionId;

        sparkList.add(new ThaumSpark(x, y, z, xR, yR, zR, wID));
    }

    private long[] getOutputVoltage(long outputVoltage, int distance, boolean overDriveToggle) {
        long outputVoltageInjectable;
        long outputVoltageConsumption;

        if (overDriveToggle) {
            outputVoltageInjectable = outputVoltage;
            outputVoltageConsumption = outputVoltage + (distance * perBlockLoss) + (long) Math.round(overDriveLoss * outputVoltage);
        } else {
            outputVoltageInjectable = outputVoltage - (distance * perBlockLoss);
            outputVoltageConsumption = outputVoltage;
        }
        return new long[]{outputVoltageInjectable, outputVoltageConsumption};
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        if (!aBaseMetaTileEntity.isClientSide()) {
            teslaNodeSet.add(aBaseMetaTileEntity);
        }
    }

    @Override
    public void onRemoval() {
        super.onRemoval();
        IGregTechTileEntity aBaseMetaTileEntity = this.getBaseMetaTileEntity();
        if (!aBaseMetaTileEntity.isClientSide()) {
            teslaNodeSet.remove(aBaseMetaTileEntity);
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        teslaNodeSet.add(this.getBaseMetaTileEntity());
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
            //Radius for transceiver to tower transfers
            int transferRadiusTower = (int) (transferRadius * rangeFrac);
            //Radius for transceiver to cover transfers
            int transferRadiusCover = (int) (transferRadiusTower / 1.25);

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

                        long[] outputVoltageNow = getOutputVoltage(outputVoltage, Rx.getValue(), overdriveToggle);
                        long outputVoltageInjectable = outputVoltageNow[0];
                        long outputVoltageConsumption = outputVoltageNow[1];

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
                            if (nodeInside instanceof GT_MetaTileEntity_TeslaCoil){
                                GT_MetaTileEntity_TeslaCoil nodeTesla = (GT_MetaTileEntity_TeslaCoil) nodeInside;
                                if (nodeTesla.powerPassToggle){
                                    continue;
                                }
                            }
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

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (aBaseMetaTileEntity.isClientSide()) {
            return true;
        }
        try {
            EntityPlayerMP player = (EntityPlayerMP) aPlayer;
            clientLocale = (String) FieldUtils.readField(player, "translator", true);
        } catch (Exception e) {
            clientLocale = "en_US";
        }
        aBaseMetaTileEntity.openGUI(aPlayer);
        return true;
    }

}
