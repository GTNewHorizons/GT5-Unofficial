package tectech.thing.metaTileEntity.single;

import static gregtech.api.enums.GTValues.V;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static java.lang.Math.round;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.Arrays;
import java.util.HashSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.ArrayUtils;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.gtnewhorizon.structurelib.util.Vec3Impl;

import cpw.mods.fml.common.network.NetworkRegistry;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEBasicBatteryBuffer;
import gregtech.api.render.TextureFactory;
import tectech.loader.ConfigHandler;
import tectech.loader.NetworkDispatcher;
import tectech.mechanics.spark.RendererMessage;
import tectech.mechanics.spark.ThaumSpark;
import tectech.mechanics.tesla.ITeslaConnectable;
import tectech.mechanics.tesla.ITeslaConnectableSimple;
import tectech.util.CommonValues;

public class MTETeslaCoil extends MTEBasicBatteryBuffer implements ITeslaConnectable {

    // Interface fields
    private final Multimap<Integer, ITeslaConnectableSimple> teslaNodeMap = MultimapBuilder.treeKeys()
        .linkedListValues()
        .build();
    private final HashSet<ThaumSpark> sparkList = new HashSet<>();
    private int sparkCount = 20;

    private static final int transferRadiusMax = ConfigHandler.TeslaTweaks.TESLA_SINGLE_RANGE;
    private static final int transferRadiusMin = 4; // Minimum user configurable
    private int transferRadius = transferRadiusMax; // Default transferRadius setting

    public boolean powerPassToggle = false; // Power Pass for public viewing
    private static final int histSteps = 20; // Hysteresis Resolution
    private int histSettingLow = 3; // Hysteresis Low Limit
    private int histSettingHigh = 15; // Hysteresis High Limit
    private static final int histLowLimit = 1; // How low can you configure it?
    private static final int histHighLimit = 19; // How high can you configure it?
    private float histLow = (float) histSettingLow / histSteps; // Power pass is disabled if power is under this
                                                                // fraction
    private float histHigh = (float) histSettingHigh / histSteps; // Power pass is enabled if power is over this
                                                                  // fraction

    private final long outputVoltage = V[mTier];
    private boolean overdriveToggle = false;

    public MTETeslaCoil(int aID, String aName, String aNameRegional, int aTier, int aSlotCount) {
        super(aID, aName, aNameRegional, aTier, "", aSlotCount);
    }

    public MTETeslaCoil(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures, int aSlotCount) {
        super(aName, aTier, aDescription, aTextures, aSlotCount);
    }

    @Override
    public String[] getDescription() {
        String[] jargon = new String[] { CommonValues.THETA_MOVEMENT,
            translateToLocal("gt.blockmachines.machine.tt.tesla.desc.0"), // Your Tesla I/O machine of choice
            EnumChatFormatting.AQUA + translateToLocal("gt.blockmachines.machine.tt.tesla.desc.1") // Lightning
                                                                                                   // stoves for the
                                                                                                   // rich
        };
        String[] sDesc = super.getDescription();
        sDesc = Arrays.copyOfRange(sDesc, 1, sDesc.length); // Removes first element from array
        return ArrayUtils.addAll(jargon, sDesc);
    }

    @Override
    public boolean onSolderingToolRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ, ItemStack aTool) {
        if (overdriveToggle) {
            overdriveToggle = false;

            if (aPlayer instanceof EntityPlayerMP playerMP) {
                playerMP.addChatMessage(new ChatComponentTranslation("tt.keyphrase.Overdrive_disengaged"));
            }
        } else {
            overdriveToggle = true;

            if (aPlayer instanceof EntityPlayerMP playerMP) {
                playerMP.addChatMessage(new ChatComponentTranslation("tt.keyphrase.Overdrive_engaged"));
            }
        }
        return true;
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        if (aPlayer.isSneaking()) {
            if (histSettingHigh < histHighLimit) {
                histSettingHigh++;
            } else {
                histSettingHigh = histSettingLow + 1;
            }
            histHigh = (float) histSettingHigh / histSteps;

            if (aPlayer instanceof EntityPlayerMP playerMP) {
                playerMP.addChatMessage(
                    new ChatComponentTranslation("tt.keyphrase.Hysteresis_high_set_to", round(histHigh * 100F)));
            }
        } else {
            if (histSettingLow > histLowLimit) {
                histSettingLow--;
            } else {
                histSettingLow = histSettingHigh - 1;
            }
            histLow = (float) histSettingLow / histSteps;

            if (aPlayer instanceof EntityPlayerMP playerMP) {
                playerMP.addChatMessage(
                    new ChatComponentTranslation("tt.keyphrase.Hysteresis_low_set_to", round(histLow * 100F)));
            }
        }
    }

    @Override
    public boolean onWireCutterRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ, ItemStack aTool) {
        if (aPlayer.isSneaking()) {
            if (transferRadius > transferRadiusMin) {
                transferRadius--;
            }
        } else {
            if (transferRadius < transferRadiusMax) {
                transferRadius++;
            }
        }

        if (aPlayer instanceof EntityPlayerMP playerMP) {
            playerMP.addChatMessage(new ChatComponentTranslation("tt.keyphrase.Tesla_radius_set_to", transferRadius));
        }
        return false;
    }

    // Cheeky skrub stuff to get machine to switch powerPass on soft mallet
    @Override
    public boolean hasAlternativeModeText() {
        return true;
    }

    @Override
    public String getAlternativeModeText() {
        // Hysteresis based ePowerPass Config
        long energyMax = getStoredEnergy()[1];
        long energyStored = getStoredEnergy()[0];
        float energyFrac = (float) energyStored / energyMax;

        // ePowerPass hist toggle
        if (energyFrac > histHigh) {
            powerPassToggle = true;
        } else if (energyFrac < histLow) {
            powerPassToggle = false;
        } else {
            powerPassToggle = !powerPassToggle;
        }

        // And after this cheeky-ness, toss the string XD
        return powerPassToggle ? translateToLocal("tt.keyphrase.Sending_power")
            : translateToLocal("tt.keyphrase.Receiving_power");
    }

    @Override
    public boolean isFacingValid(ForgeDirection side) {
        return side != ForgeDirection.UP;
    } // Prevents output at the top side

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        ITexture[][][] rTextures = new ITexture[3][17][];
        for (byte i = -1; i < 16; ++i) {
            rTextures[0][i + 1] = new ITexture[] { MACHINE_CASINGS[this.mTier][i + 1] };
            rTextures[1][i + 1] = new ITexture[] { MACHINE_CASINGS[this.mTier][i + 1],
                TextureFactory.of(Textures.BlockIcons.TESLA_TRANSCEIVER_TOP) };
            rTextures[2][i + 1] = new ITexture[] { MACHINE_CASINGS[this.mTier][i + 1],
                this.mInventory.length == 16 ? OVERLAYS_ENERGY_OUT_MULTI_16A[this.mTier + 1]
                    : (this.mInventory.length > 4 ? OVERLAYS_ENERGY_OUT_MULTI_2A[this.mTier + 1]
                        : OVERLAYS_ENERGY_OUT[this.mTier + 1]) };
        }
        return rTextures;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        return this.mTextures[side == facing ? 2 : side == ForgeDirection.UP ? 1 : 0][colorIndex + 1];
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTETeslaCoil(mName, mTier, mDescriptionArray, mTextures, mInventory.length);
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        if (!aBaseMetaTileEntity.isClientSide()) {
            TeslaUtil.teslaSimpleNodeSetAdd(this);
            TeslaUtil.generateTeslaNodeMap(this);
        }
    }

    @Override
    public void onRemoval() {
        super.onRemoval();
        if (!this.getBaseMetaTileEntity()
            .isClientSide()) {
            TeslaUtil.teslaSimpleNodeSetRemove(this);
        }
    }

    @Override
    public void onUnload() {
        if (!this.getBaseMetaTileEntity()
            .isClientSide()) {
            TeslaUtil.teslaSimpleNodeSetRemove(this);
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        TeslaUtil.teslaSimpleNodeSetAdd(this);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isClientSide()) {
            return;
        }

        // Hysteresis based ePowerPass Config
        long energyMax = getStoredEnergy()[1];
        long energyStored = getStoredEnergy()[0];
        float energyFrac = (float) energyStored / energyMax;

        // ePowerPass hist toggle
        if (!powerPassToggle && energyFrac > histHigh) {
            powerPassToggle = true;
        } else if (powerPassToggle && energyFrac < histLow) {
            powerPassToggle = false;
        }

        // Send Power
        TeslaUtil.powerTeslaNodeMap(this);

        // TODO Encapsulate the spark sender
        sparkCount--;
        if (sparkCount == 0) {
            sparkCount = 20;
            if (!sparkList.isEmpty()) {
                NetworkDispatcher.INSTANCE.sendToAllAround(
                    new RendererMessage.RendererData(sparkList),
                    new NetworkRegistry.TargetPoint(
                        aBaseMetaTileEntity.getWorld().provider.dimensionId,
                        aBaseMetaTileEntity.getXCoord(),
                        aBaseMetaTileEntity.getYCoord(),
                        aBaseMetaTileEntity.getZCoord(),
                        256));
                sparkList.clear();
            }
        }
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
        return ConfigHandler.TeslaTweaks.TESLA_SINGLE_LOSS_PER_BLOCK;
    }

    @Override
    public float getTeslaOverdriveLossCoefficient() {
        return ConfigHandler.TeslaTweaks.TESLA_SINGLE_LOSS_FACTOR_OVERDRIVE;
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
        return new Vec3Impl(
            this.getBaseMetaTileEntity()
                .getXCoord(),
            this.getBaseMetaTileEntity()
                .getYCoord(),
            this.getBaseMetaTileEntity()
                .getZCoord());
    }

    @Override
    public Integer getTeslaDimension() {
        return this.getBaseMetaTileEntity()
            .getWorld().provider.dimensionId;
    }

    @Override
    public boolean teslaInjectEnergy(long teslaVoltageInjected) {
        return this.getBaseMetaTileEntity()
            .injectEnergyUnits(ForgeDirection.UP, teslaVoltageInjected, 1L) > 0L;
    }
}
