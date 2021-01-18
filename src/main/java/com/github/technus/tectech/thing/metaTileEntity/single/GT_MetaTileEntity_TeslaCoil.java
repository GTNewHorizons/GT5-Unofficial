package com.github.technus.tectech.thing.metaTileEntity.single;

import com.github.technus.tectech.mechanics.tesla.ITeslaConnectable;
import com.github.technus.tectech.mechanics.tesla.ITeslaConnectableSimple;
import com.github.technus.tectech.util.CommonValues;
import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.loader.NetworkDispatcher;
import com.github.technus.tectech.mechanics.spark.RendererMessage;
import com.github.technus.tectech.mechanics.spark.ThaumSpark;
import com.github.technus.tectech.util.Util;
import com.github.technus.tectech.util.Vec3Impl;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import eu.usrv.yamcore.auxiliary.PlayerChatHelper;
import gregtech.api.interfaces.ITexture;
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
import java.util.HashSet;

import static com.github.technus.tectech.mechanics.tesla.ITeslaConnectable.TeslaUtil.*;
import static com.github.technus.tectech.util.CommonValues.V;
import static com.github.technus.tectech.thing.metaTileEntity.Textures.*;
import static java.lang.Math.round;
import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

public class GT_MetaTileEntity_TeslaCoil extends GT_MetaTileEntity_BasicBatteryBuffer implements ITeslaConnectable {
    //Interface fields
    private final Multimap<Integer, ITeslaConnectableSimple> teslaNodeMap = MultimapBuilder.treeKeys().linkedListValues().build();
    private final HashSet<ThaumSpark> sparkList = new HashSet<>();
    private int sparkCount = 10;

    private final static int transferRadiusMax = TecTech.configTecTech.TESLA_SINGLE_RANGE;//Default is 20
    private final static int perBlockLoss = TecTech.configTecTech.TESLA_SINGLE_LOSS_PER_BLOCK;//Default is 1
    private final static float overDriveLoss = TecTech.configTecTech.TESLA_SINGLE_LOSS_FACTOR_OVERDRIVE;//Default is 0.25F
    private final static int transferRadiusMin = 4;//Minimum user configurable
    private int transferRadius = transferRadiusMax;//Default transferRadius setting

    public boolean powerPassToggle = false;//Power Pass for public viewing
    private final static int histSteps = 20;//Hysteresis Resolution
    private int histSettingLow = 3;//Hysteresis Low Limit
    private int histSettingHigh = 15;//Hysteresis High Limit
    private final static int histLowLimit = 1;//How low can you configure it?
    private final static int histHighLimit = 19;//How high can you configure it?
    private float histLow = (float) histSettingLow / histSteps;//Power pass is disabled if power is under this fraction
    private float histHigh = (float) histSettingHigh / histSteps;//Power pass is enabled if power is over this fraction

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
            if (transferRadius > transferRadiusMin) {
                transferRadius--;
            }
        } else {
            if (transferRadius < transferRadiusMax) {
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

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        if (!aBaseMetaTileEntity.isClientSide()) {
            teslaSimpleNodeSetAdd(this);
            generateTeslaNodeMap(this);
        }
    }

    @Override
    public void onRemoval() {
        super.onRemoval();
        if (!this.getBaseMetaTileEntity().isClientSide()) {
            teslaSimpleNodeSetRemove(this);
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        teslaSimpleNodeSetAdd(this);
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

        //Send Power
        powerTeslaNodeMap(this);

        //TODO Encapsulate the spark sender
        sparkCount--;
        if (sparkCount == 0){
            sparkCount = 10;
            if(!sparkList.isEmpty()){
                NetworkDispatcher.INSTANCE.sendToAllAround(new RendererMessage.RendererData(sparkList),
                        aBaseMetaTileEntity.getWorld().provider.dimensionId,
                        aBaseMetaTileEntity.getXCoord(),
                        aBaseMetaTileEntity.getYCoord(),
                        aBaseMetaTileEntity.getZCoord(),
                        256);
                sparkList.clear();
            }
        }
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (aBaseMetaTileEntity.isServerSide()) {
            try {
                EntityPlayerMP player = (EntityPlayerMP) aPlayer;
                clientLocale = (String) FieldUtils.readField(player, "translator", true);
            } catch (Exception e) {
                clientLocale = "en_US";
            }
            aBaseMetaTileEntity.openGUI(aPlayer);
        }
        return true;
    }

    @Override
    public byte getTeslaReceptionCapability() {
        return 1;
    }

    @Override
    public float getTeslaReceptionCoefficient() {
        return 1;
    }

    @Override
    public Multimap<Integer, ITeslaConnectableSimple> getTeslaNodeMap() {
        return teslaNodeMap;
    }

    @Override
    public HashSet<ThaumSpark> getSparkList() {
        return sparkList;
    }

    @Override
    public byte getTeslaTransmissionCapability() {
        return 2;
    }

    @Override
    public int getTeslaTransmissionRange() {
        return transferRadius;
    }

    @Override
    public boolean isOverdriveEnabled() {
        return overdriveToggle;
    }

    @Override
    public int getTeslaEnergyLossPerBlock() {
        return perBlockLoss;
    }

    @Override
    public float getTeslaOverdriveLossCoefficient() {
        return overDriveLoss;
    }

    @Override
    public long getTeslaOutputVoltage() {
        return outputVoltage;
    }

    @Override
    public long getTeslaOutputCurrent() {
        return mBatteryCount;
    }

    @Override
    public boolean teslaDrainEnergy(long teslaVoltageDrained) {
        if (getEUVar() < teslaVoltageDrained) {
            return false;
        }

        setEUVar(getEUVar() - teslaVoltageDrained);
        return true;
    }

    @Override
    public boolean isTeslaReadyToReceive() {
        return !this.powerPassToggle;
    }

    @Override
    public long getTeslaStoredEnergy() {
        return getEUVar();
    }

    @Override
    public Vec3Impl getTeslaPosition() {
        return new Vec3Impl(this.getBaseMetaTileEntity());
    }

    @Override
    public Integer getTeslaDimension() {
        return this.getBaseMetaTileEntity().getWorld().provider.dimensionId;
    }

    @Override
    public boolean teslaInjectEnergy(long teslaVoltageInjected) {
        return this.getBaseMetaTileEntity().injectEnergyUnits((byte) 1, teslaVoltageInjected, 1L) > 0L;
    }
}
