package gregtech.api.metatileentity;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.GTMod.GT_FML_LOGGER;
import static gregtech.api.enums.GTValues.NW;
import static gregtech.api.enums.GTValues.V;
import static gregtech.api.objects.XSTR.XSTR_INSTANCE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFire;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.IAlignment;
import com.gtnewhorizon.structurelib.alignment.IAlignmentProvider;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructableProvider;

import appeng.api.networking.IGridNode;
import appeng.api.networking.security.IActionHost;
import appeng.api.util.AECableType;
import appeng.api.util.DimensionalCoord;
import appeng.helpers.ICustomNameObject;
import appeng.me.helpers.AENetworkProxy;
import appeng.me.helpers.IGridProxyable;
import gregtech.GTMod;
import gregtech.api.GregTechAPI;
import gregtech.api.covers.CoverRegistry;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.graphs.GenerateNodeMap;
import gregtech.api.graphs.GenerateNodeMapPower;
import gregtech.api.graphs.Node;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IDebugableTileEntity;
import gregtech.api.interfaces.tileentity.IEnergyConnected;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IGregtechWailaProvider;
import gregtech.api.metatileentity.implementations.MTEBasicMachine;
import gregtech.api.net.GTPacketTileEntity;
import gregtech.api.objects.blockupdate.BlockUpdateHandler;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.covers.Cover;
import gregtech.common.pollution.Pollution;
import gregtech.common.render.IMTERenderer;
import gregtech.mixin.interfaces.accessors.EntityItemAccessor;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.MTESteamMultiBase;
import ic2.api.Direction;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * <p/>
 * This is the main TileEntity for EVERYTHING.
 */
public class BaseMetaTileEntity extends CommonBaseMetaTileEntity
    implements IGregTechTileEntity, IActionHost, IGridProxyable, IAlignmentProvider, IConstructableProvider,
    IDebugableTileEntity, IGregtechWailaProvider, ICustomNameObject {

    private final boolean[] mActiveEUInputs = new boolean[] { false, false, false, false, false, false };
    private final boolean[] mActiveEUOutputs = new boolean[] { false, false, false, false, false, false };
    public long mLastSoundTick = 0;
    public boolean mWasShutdown = false;
    public @Nonnull ShutDownReason lastShutDownReason = ShutDownReasonRegistry.NONE;
    protected MetaTileEntity mMetaTileEntity;
    protected long mStoredEnergy = 0, mStoredSteam = 0;
    protected int mAverageEUInputIndex = 0, mAverageEUOutputIndex = 0;
    protected boolean mReleaseEnergy = false;
    protected final long[] mAverageEUInput = new long[] { 0, 0, 0, 0, 0 };
    protected final long[] mAverageEUOutput = new long[] { 0, 0, 0, 0, 0 };
    private boolean mHasEnoughEnergy = true, mRunningThroughTick = false, mInputDisabled = false,
        mOutputDisabled = false, mMuffler = false, mLockUpgrade = false;
    private boolean mActive = false;
    private boolean mWorkUpdate = false;
    private boolean mWorks = true;
    private boolean oRedstone = false;
    private byte mColor = 0, oldColor = 0, oldStrongRedstone = 0, oldRedstoneData = 63, oldTextureData = 0,
        oldUpdateData = 0;
    private byte oldLightValueClient = 0, oldLightValue = -1, mLightValue = 0, mOtherUpgrades = 0;
    private ForgeDirection mFacing = ForgeDirection.DOWN, oldFacing = ForgeDirection.DOWN;
    private int oldX = 0, oldY = 0, oldZ = 0;
    private long oldOutput = 0, mAcceptedAmperes = Long.MAX_VALUE;
    private long mLastCheckTick = 0;
    private String mOwnerName = "";
    private UUID mOwnerUuid = GTUtility.defaultUuid;
    private int cableUpdateDelay = 30;

    public BaseMetaTileEntity() {}

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        try {
            super.writeToNBT(nbt);
        } catch (Exception e) {
            GT_FML_LOGGER.error("Encountered CRITICAL ERROR while saving MetaTileEntity.", e);
        }
        try {
            nbt.setBoolean("mWasShutdown", mWasShutdown);
            nbt.setInteger("mID", mID);
            nbt.setLong("mStoredSteam", mStoredSteam);
            nbt.setLong("mStoredEnergy", mStoredEnergy);
            writeCoverNBT(nbt, false);
            nbt.setByte("mColor", mColor);
            nbt.setByte("mLightValue", mLightValue);
            nbt.setByte("mOtherUpgrades", mOtherUpgrades);
            nbt.setShort("mFacing", (short) mFacing.ordinal());
            nbt.setString("mOwnerName", mOwnerName);
            nbt.setString("mOwnerUuid", mOwnerUuid == null ? "" : mOwnerUuid.toString());
            nbt.setBoolean("mLockUpgrade", mLockUpgrade);
            nbt.setBoolean("mMuffler", mMuffler);
            nbt.setBoolean("mActive", mActive);
            nbt.setBoolean("mWorks", !mWorks);
            nbt.setBoolean("mInputDisabled", mInputDisabled);
            nbt.setBoolean("mOutputDisabled", mOutputDisabled);
            nbt.setString("shutDownReasonID", getLastShutDownReason().getID());
            nbt.setTag("shutDownReason", getLastShutDownReason().writeToNBT(new NBTTagCompound()));
        } catch (Exception e) {
            GT_FML_LOGGER.error("Encountered CRITICAL ERROR while saving MetaTileEntity.", e);
        }
        saveMetaTileNBT(nbt);
    }

    @Override
    public void readFromNBT(NBTTagCompound aNBT) {
        super.readFromNBT(aNBT);
        setInitialValuesAsNBT(aNBT, (short) 0);
    }

    @Override
    public void setInitialValuesAsNBT(NBTTagCompound aNBT, short aID) {
        if (aNBT == null) {
            if (aID > 0) mID = aID;
            else mID = mID > 0 ? mID : 0;
            if (mID != 0) createNewMetatileEntity(mID);
        } else {
            if (aID <= 0) mID = (short) aNBT.getInteger("mID");
            else mID = aID;
            mStoredSteam = aNBT.getLong("mStoredSteam");
            mStoredEnergy = aNBT.getLong("mStoredEnergy");
            mColor = aNBT.getByte("mColor");
            mLightValue = aNBT.getByte("mLightValue");
            mFacing = oldFacing = ForgeDirection.getOrientation(aNBT.getShort("mFacing"));
            mOwnerName = aNBT.getString("mOwnerName");
            setShutdownStatus(aNBT.getBoolean("mWasShutdown"));
            String shutDownReasonID = aNBT.getString("shutDownReasonID");
            if (ShutDownReasonRegistry.isRegistered(shutDownReasonID)) {
                ShutDownReason reason = ShutDownReasonRegistry.getSampleFromRegistry(shutDownReasonID)
                    .newInstance();
                reason.readFromNBT(aNBT.getCompoundTag("shutDownReason"));
                setShutDownReason(reason);
            }
            try {
                mOwnerUuid = UUID.fromString(aNBT.getString("mOwnerUuid"));
            } catch (IllegalArgumentException e) {
                mOwnerUuid = null;
            }
            mLockUpgrade = aNBT.getBoolean("mLockUpgrade");
            mMuffler = aNBT.getBoolean("mMuffler");
            mActive = aNBT.getBoolean("mActive");
            mWorks = !aNBT.getBoolean("mWorks");
            mInputDisabled = aNBT.getBoolean("mInputDisabled");
            mOutputDisabled = aNBT.getBoolean("mOutputDisabled");
            mOtherUpgrades = (byte) (aNBT.getByte("mOtherUpgrades") + aNBT.getByte("mBatteries")
                + aNBT.getByte("mLiBatteries"));

            final int nbtVersion = aNBT.getInteger("nbtVersion");
            readCoverNBT(aNBT);
            loadMetaTileNBT(aNBT);
        }
    }

    /**
     * Used for ticking special BaseMetaTileEntities, which need that for Energy Conversion It's called right before
     * onPostTick()
     */
    public void updateStatus() {
        //
    }

    /**
     * Called when trying to charge Items
     */
    public void chargeItem(ItemStack aStack) {
        decreaseStoredEU(
            GTModHandler.chargeElectricItem(
                aStack,
                (int) Math.min(Integer.MAX_VALUE, getStoredEU()),
                (int) Math.min(Integer.MAX_VALUE, mMetaTileEntity.getOutputTier()),
                false,
                false),
            true);
    }

    /**
     * Called when trying to discharge Items
     */
    public void dischargeItem(ItemStack aStack) {
        increaseStoredEnergyUnits(
            GTModHandler.dischargeElectricItem(
                aStack,
                (int) Math.min(Integer.MAX_VALUE, getEUCapacity() - getStoredEU()),
                (int) Math.min(Integer.MAX_VALUE, mMetaTileEntity.getInputTier()),
                false,
                false,
                false),
            true);
    }

    protected boolean isRainPossible() {
        BiomeGenBase biome = getBiome();
        // see net.minecraft.client.renderer.EntityRenderer.renderRainSnow
        return biome.rainfall > 0 && (biome.canSpawnLightningBolt() || biome.getEnableSnow());
    }

    /**
     * Check if this is exposed to rain
     *
     * @return True if exposed to rain, else false
     */
    public boolean isRainExposed() {
        final int precipitationHeightAtSide2 = worldObj.getPrecipitationHeight(xCoord, zCoord - 1);
        final int precipitationHeightAtSide3 = worldObj.getPrecipitationHeight(xCoord, zCoord + 1);
        final int precipitationHeightAtSide4 = worldObj.getPrecipitationHeight(xCoord - 1, zCoord);
        final int precipitationHeightAtSide5 = worldObj.getPrecipitationHeight(xCoord + 1, zCoord);
        return (!hasCoverAtSide(ForgeDirection.UP) && worldObj.getPrecipitationHeight(xCoord, zCoord) - 2 < yCoord)
            || (!hasCoverAtSide(ForgeDirection.NORTH) && precipitationHeightAtSide2 - 1 < yCoord
                && precipitationHeightAtSide2 > -1)
            || (!hasCoverAtSide(ForgeDirection.SOUTH) && precipitationHeightAtSide3 - 1 < yCoord
                && precipitationHeightAtSide3 > -1)
            || (!hasCoverAtSide(ForgeDirection.WEST) && precipitationHeightAtSide4 - 1 < yCoord
                && precipitationHeightAtSide4 > -1)
            || (!hasCoverAtSide(ForgeDirection.EAST) && precipitationHeightAtSide5 - 1 < yCoord
                && precipitationHeightAtSide5 > -1);
    }

    @Override
    public void updateEntityProfiled() {
        if (!hasValidMetaTileEntity()) {
            if (mMetaTileEntity == null) return;
            mMetaTileEntity.setBaseMetaTileEntity(this);
        }

        mRunningThroughTick = true;
        final boolean isServerSide = isServerSide();
        if (hasValidMetaTileEntity()) {
            if (mTickTimer++ == 0) {
                oldX = xCoord;
                oldY = yCoord;
                oldZ = zCoord;
                if (isServerSide) {
                    checkDropCover();
                } else {
                    requestCoverDataIfNeeded();
                }
                worldObj.markTileEntityChunkModified(xCoord, yCoord, zCoord, this);
                mMetaTileEntity.onFirstTick(this);
                if (!hasValidMetaTileEntity()) {
                    mRunningThroughTick = false;
                    return;
                }
            }
            if (isClientSide()) {
                if (mColor != oldColor) {
                    mMetaTileEntity.onColorChangeClient(oldColor = mColor);
                    issueTextureUpdate();
                }

                if (mLightValue != oldLightValueClient) {
                    worldObj.setLightValue(EnumSkyBlock.Block, xCoord, yCoord, zCoord, mLightValue);
                    worldObj.updateLightByType(EnumSkyBlock.Block, xCoord, yCoord, zCoord);
                    worldObj.updateLightByType(EnumSkyBlock.Block, xCoord + 1, yCoord, zCoord);
                    worldObj.updateLightByType(EnumSkyBlock.Block, xCoord - 1, yCoord, zCoord);
                    worldObj.updateLightByType(EnumSkyBlock.Block, xCoord, yCoord + 1, zCoord);
                    worldObj.updateLightByType(EnumSkyBlock.Block, xCoord, yCoord - 1, zCoord);
                    worldObj.updateLightByType(EnumSkyBlock.Block, xCoord, yCoord, zCoord + 1);
                    worldObj.updateLightByType(EnumSkyBlock.Block, xCoord, yCoord, zCoord - 1);
                    oldLightValueClient = mLightValue;
                    issueTextureUpdate();
                }

                if (mNeedsUpdate) {
                    if (GTMod.proxy.mUseBlockUpdateHandler) {
                        BlockUpdateHandler.Instance.enqueueBlockUpdate(worldObj, getLocation());
                    } else {
                        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                    }
                    mMetaTileEntity.onTextureUpdate();
                    mNeedsUpdate = false;
                }
            }
            if (isServerSide && mTickTimer > 10) {
                if (!doCoverThings()) {
                    mRunningThroughTick = false;
                    return;
                }
            }
            if (isServerSide) {
                if (++mAverageEUInputIndex >= mAverageEUInput.length) mAverageEUInputIndex = 0;
                if (++mAverageEUOutputIndex >= mAverageEUOutput.length) mAverageEUOutputIndex = 0;

                mAverageEUInput[mAverageEUInputIndex] = 0;
                mAverageEUOutput[mAverageEUOutputIndex] = 0;
            }

            mMetaTileEntity.onPreTick(this, mTickTimer);

            if (!hasValidMetaTileEntity()) {
                mRunningThroughTick = false;
                return;
            }
            if (isServerSide) {
                if (mRedstone != oRedstone || mTickTimer == 10) {
                    oRedstone = mRedstone;
                    issueBlockUpdate();
                }
                if (mTickTimer == 10) joinEnet();

                if (xCoord != oldX || yCoord != oldY || zCoord != oldZ) {
                    oldX = xCoord;
                    oldY = yCoord;
                    oldZ = zCoord;
                    issueClientUpdate();
                    clearTileEntityBuffer();
                }

                if (mFacing != oldFacing) {
                    oldFacing = mFacing;
                    checkDropCover();
                    issueBlockUpdate();
                }

                if (mNeedsTileUpdate) {
                    worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                    mNeedsTileUpdate = false;
                }

                if (mTickTimer > 20 && mMetaTileEntity.isElectric()) {
                    mAcceptedAmperes = 0;

                    if (getOutputVoltage() != oldOutput) {
                        oldOutput = getOutputVoltage();
                    }

                    if (mMetaTileEntity.isEnetOutput() || mMetaTileEntity.isEnetInput()) {
                        for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
                            final int ordinalSide = side.ordinal();
                            boolean temp = isEnergyInputSide(side);
                            if (temp != mActiveEUInputs[ordinalSide]) {
                                mActiveEUInputs[ordinalSide] = temp;
                            }
                            temp = isEnergyOutputSide(side);
                            if (temp != mActiveEUOutputs[ordinalSide]) {
                                mActiveEUOutputs[ordinalSide] = temp;
                            }
                        }
                    }

                    if (mMetaTileEntity.isEnetOutput() && oldOutput > 0) {
                        final long tOutputVoltage = Math
                            .max(oldOutput, oldOutput + (1L << Math.max(0, GTUtility.getTier(oldOutput) - 1)));
                        final long tUsableAmperage = Math.min(
                            getOutputAmperage(),
                            (getStoredEU() - mMetaTileEntity.getMinimumStoredEU()) / tOutputVoltage);
                        if (tUsableAmperage > 0) {
                            final long tEU = tOutputVoltage
                                * Util.emitEnergyToNetwork(oldOutput, tUsableAmperage, this);
                            mAverageEUOutput[mAverageEUOutputIndex] += tEU;
                            decreaseStoredEU(tEU, true);
                        }
                    }
                    if (getEUCapacity() > 0) {
                        if (GregTechAPI.sMachineFireExplosions && getRandomNumber(1000) == 0) {
                            final Block tBlock = getBlockAtSide(ForgeDirection.getOrientation(getRandomNumber(6)));
                            if (tBlock instanceof BlockFire) doEnergyExplosion();
                        }

                        if (!hasValidMetaTileEntity()) {
                            mRunningThroughTick = false;
                            return;
                        }

                        if (GregTechAPI.sMachineRainExplosions) {
                            if (mMetaTileEntity.willExplodeInRain()) {
                                if (getRandomNumber(1000) == 0 && isRainPossible()) {
                                    // Short-circuit so raincheck happens before isRainExposed,
                                    // saves sme TPS since rain exposed check can be slow
                                    // This logic can be compressed further by only checking for
                                    // isRainExposed once IF we can guarantee it never thunders without
                                    // raining, but I don't know if this is true or not.
                                    if (worldObj.isRaining() && isRainExposed()) {
                                        if (getRandomNumber(10) == 0) {
                                            try {
                                                GTMod.achievements.issueAchievement(
                                                    this.getWorldObj()
                                                        .getPlayerEntityByName(mOwnerName),
                                                    "badweather");
                                            } catch (Exception ignored) {}
                                            GTLog.exp.println(
                                                "Machine at: " + this.getXCoord()
                                                    + " | "
                                                    + this.getYCoord()
                                                    + " | "
                                                    + this.getZCoord()
                                                    + " DIMID: "
                                                    + this.worldObj.provider.dimensionId
                                                    + " explosion due to rain!");
                                            doEnergyExplosion();
                                        } else {
                                            GTLog.exp.println(
                                                "Machine at: " + this.getXCoord()
                                                    + " | "
                                                    + this.getYCoord()
                                                    + " | "
                                                    + this.getZCoord()
                                                    + " DIMID: "
                                                    + this.worldObj.provider.dimensionId
                                                    + "  set to Fire due to rain!");
                                            setOnFire();
                                        }
                                    }
                                    if (!hasValidMetaTileEntity()) {
                                        mRunningThroughTick = false;
                                        return;
                                    }
                                    if (GregTechAPI.sMachineThunderExplosions && worldObj.isThundering()
                                        && getRandomNumber(3) == 0
                                        && isRainExposed()) {
                                        try {
                                            GTMod.achievements.issueAchievement(
                                                this.getWorldObj()
                                                    .getPlayerEntityByName(mOwnerName),
                                                "badweather");
                                        } catch (Exception ignored) {}
                                        GTLog.exp.println(
                                            "Machine at: " + this.getXCoord()
                                                + " | "
                                                + this.getYCoord()
                                                + " | "
                                                + this.getZCoord()
                                                + " DIMID: "
                                                + this.worldObj.provider.dimensionId
                                                + " explosion due to Thunderstorm!");
                                        doEnergyExplosion();
                                    }
                                }
                            }
                        }
                    }
                }

                if (!hasValidMetaTileEntity()) {
                    mRunningThroughTick = false;
                    return;
                }
            }
            if (isServerSide) {
                if (mMetaTileEntity.dechargerSlotCount() > 0 && getStoredEU() < getEUCapacity()) {
                    for (int i = mMetaTileEntity.dechargerSlotStartIndex(),
                        k = mMetaTileEntity.dechargerSlotCount() + i; i < k; i++) {
                        if (mMetaTileEntity.mInventory[i] != null && getStoredEU() < getEUCapacity()) {
                            dischargeItem(mMetaTileEntity.mInventory[i]);
                            if (ic2.api.info.Info.itemEnergy.getEnergyValue(mMetaTileEntity.mInventory[i]) > 0) {
                                if ((getStoredEU()
                                    + ic2.api.info.Info.itemEnergy.getEnergyValue(mMetaTileEntity.mInventory[i]))
                                    < getEUCapacity()) {
                                    increaseStoredEnergyUnits(
                                        (long) ic2.api.info.Info.itemEnergy
                                            .getEnergyValue(mMetaTileEntity.mInventory[i]),
                                        false);
                                    mMetaTileEntity.mInventory[i].stackSize--;
                                    mInventoryChanged = true;
                                }
                            }
                            if (mMetaTileEntity.mInventory[i].stackSize <= 0) {
                                mMetaTileEntity.mInventory[i] = null;
                                mInventoryChanged = true;
                            }
                        }
                    }
                }
            }
            if (isServerSide) {
                if (mMetaTileEntity.rechargerSlotCount() > 0 && getStoredEU() > 0) {
                    for (int i = mMetaTileEntity.rechargerSlotStartIndex(),
                        k = mMetaTileEntity.rechargerSlotCount() + i; i < k; i++) {
                        if (getStoredEU() > 0 && mMetaTileEntity.mInventory[i] != null) {
                            chargeItem(mMetaTileEntity.mInventory[i]);
                            if (mMetaTileEntity.mInventory[i].stackSize <= 0) {
                                mMetaTileEntity.mInventory[i] = null;
                                mInventoryChanged = true;
                            }
                        }
                    }
                }
            }
            updateStatus();
            if (!hasValidMetaTileEntity()) {
                mRunningThroughTick = false;
                return;
            }
            mMetaTileEntity.onPostTick(this, mTickTimer);
            if (!hasValidMetaTileEntity()) {
                mRunningThroughTick = false;
                return;
            }
            if (isServerSide) {
                if (mTickTimer > 20 && cableUpdateDelay == 0) {
                    generatePowerNodes();
                }
                cableUpdateDelay--;
                if (mTickTimer % 10 == 0) {
                    sendClientData();
                }

                if (mTickTimer > 10) {
                    byte textureData = (byte) ((mFacing.ordinal() & 7) | (mActive ? 8 : 0)
                        | (mRedstone ? 16 : 0)
                        | (mLockUpgrade ? 32 : 0)
                        | (mWorks ? 64 : 0)
                        | (mMuffler ? 128 : 0));

                    if (textureData != oldTextureData) {
                        oldTextureData = textureData;
                        sendBlockEvent(GregTechTileClientEvents.CHANGE_COMMON_DATA, oldTextureData);
                    }

                    byte updateData = mMetaTileEntity.getUpdateData();

                    if (updateData != oldUpdateData) {
                        oldUpdateData = updateData;
                        sendBlockEvent(GregTechTileClientEvents.CHANGE_CUSTOM_DATA, oldUpdateData);
                    }

                    if (mColor != oldColor) {
                        oldColor = mColor;
                        sendBlockEvent(GregTechTileClientEvents.CHANGE_COLOR, oldColor);
                    }

                    byte redstone = getSidedRedstoneMask();

                    if (redstone != oldRedstoneData) {
                        oldRedstoneData = redstone;
                        sendBlockEvent(GregTechTileClientEvents.CHANGE_REDSTONE_OUTPUT, oldRedstoneData);
                    }

                    if (mLightValue != oldLightValue) {
                        worldObj.setLightValue(EnumSkyBlock.Block, xCoord, yCoord, zCoord, mLightValue);
                        worldObj.updateLightByType(EnumSkyBlock.Block, xCoord, yCoord, zCoord);
                        worldObj.updateLightByType(EnumSkyBlock.Block, xCoord + 1, yCoord, zCoord);
                        worldObj.updateLightByType(EnumSkyBlock.Block, xCoord - 1, yCoord, zCoord);
                        worldObj.updateLightByType(EnumSkyBlock.Block, xCoord, yCoord + 1, zCoord);
                        worldObj.updateLightByType(EnumSkyBlock.Block, xCoord, yCoord - 1, zCoord);
                        worldObj.updateLightByType(EnumSkyBlock.Block, xCoord, yCoord, zCoord + 1);
                        worldObj.updateLightByType(EnumSkyBlock.Block, xCoord, yCoord, zCoord - 1);
                        issueTextureUpdate();
                        sendBlockEvent(GregTechTileClientEvents.CHANGE_LIGHT, oldLightValue = mLightValue);
                    }
                }

                if (mNeedsBlockUpdate) {
                    updateNeighbours(mStrongRedstone, oldStrongRedstone);
                    oldStrongRedstone = mStrongRedstone;
                    mNeedsBlockUpdate = false;
                }
            }
        }

        mWorkUpdate = mInventoryChanged = mRunningThroughTick = false;
    }

    @Override
    protected void onTickFail() {
        mMetaTileEntity.onTickFail(this, mTickTimer);
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        if (hasValidMetaTileEntity()) {
            getMetaTileEntity().getWailaBody(itemStack, currentTip, accessor, config);
        }
        super.getWailaBody(itemStack, currentTip, accessor, config);
    }

    @Override
    public boolean hasWailaAdvancedBody(ItemStack itemStack, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if (hasValidMetaTileEntity()) {
            return getMetaTileEntity().hasWailaAdvancedBody(itemStack, accessor, config);
        }
        return super.hasWailaAdvancedBody(itemStack, accessor, config);
    }

    @Override
    public void getWailaAdvancedBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        if (hasValidMetaTileEntity()) {
            getMetaTileEntity().getWailaAdvancedBody(itemStack, currentTip, accessor, config);
        }
        super.getWailaAdvancedBody(itemStack, currentTip, accessor, config);
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        if (hasValidMetaTileEntity()) {
            getMetaTileEntity().getWailaNBTData(player, tile, tag, world, x, y, z);
        }
    }

    private void sendClientData() {
        if (mSendClientData) {
            oldTextureData = (byte) ((mFacing.ordinal() & 7) | (mActive ? 8 : 0)
                | (mRedstone ? 16 : 0)
                | (mLockUpgrade ? 32 : 0)
                | (mWorks ? 64 : 0)
                | (mMuffler ? 128 : 0));

            oldUpdateData = hasValidMetaTileEntity() ? mMetaTileEntity.getUpdateData() : 0;

            oldRedstoneData = getSidedRedstoneMask();

            oldColor = mColor;

            NW.sendPacketToAllPlayersInRange(
                worldObj,
                new GTPacketTileEntity(
                    xCoord,
                    (short) yCoord,
                    zCoord,
                    mID,
                    getCoverAtSide(ForgeDirection.DOWN).getCoverID(),
                    getCoverAtSide(ForgeDirection.UP).getCoverID(),
                    getCoverAtSide(ForgeDirection.NORTH).getCoverID(),
                    getCoverAtSide(ForgeDirection.SOUTH).getCoverID(),
                    getCoverAtSide(ForgeDirection.WEST).getCoverID(),
                    getCoverAtSide(ForgeDirection.EAST).getCoverID(),
                    oldTextureData,
                    oldUpdateData,
                    oldRedstoneData,
                    oldColor),
                xCoord,
                zCoord);
            mSendClientData = false;
        }
        sendCoverDataIfNeeded();
    }

    public final void receiveMetaTileEntityData(short aID, int aCover0, int aCover1, int aCover2, int aCover3,
        int aCover4, int aCover5, byte aTextureData, byte aUpdateData, byte aRedstoneData, byte aColorData) {
        issueTextureUpdate();
        if (mID != aID && aID > 0) {
            mID = aID;
            createNewMetatileEntity(mID);
        }

        CoverRegistry.cover(this, aCover0, aCover1, aCover2, aCover3, aCover4, aCover5);

        receiveClientEvent(GregTechTileClientEvents.CHANGE_COMMON_DATA, aTextureData);
        receiveClientEvent(GregTechTileClientEvents.CHANGE_CUSTOM_DATA, aUpdateData & 0x7F);
        receiveClientEvent(GregTechTileClientEvents.CHANGE_COLOR, aColorData);
        receiveClientEvent(GregTechTileClientEvents.CHANGE_REDSTONE_OUTPUT, aRedstoneData);
    }

    @Override
    public boolean receiveClientEvent(int aEventID, int aValue) {
        super.receiveClientEvent(aEventID, aValue);

        if (hasValidMetaTileEntity()) {
            try {
                mMetaTileEntity.receiveClientEvent((byte) aEventID, (byte) aValue);
            } catch (Exception e) {
                GTLog.err.println(
                    "Encountered Exception while receiving Data from the Server, the Client should've been crashed by now, but I prevented that. Please report immediately to GregTech Intergalactical!!!");
                e.printStackTrace(GTLog.err);
            }
        }

        if (isClientSide()) {
            issueTextureUpdate();
            switch (aEventID) {
                case GregTechTileClientEvents.CHANGE_COMMON_DATA -> {
                    mFacing = ForgeDirection.getOrientation((byte) (aValue & 7));
                    mActive = ((aValue & 8) != 0);
                    mRedstone = ((aValue & 16) != 0);
                    // mLockUpgrade = ((aValue&32) != 0);
                    mWorks = ((aValue & 64) != 0);
                    mMuffler = ((aValue & 128) != 0);
                }
                case GregTechTileClientEvents.CHANGE_CUSTOM_DATA -> {
                    if (hasValidMetaTileEntity()) {
                        mMetaTileEntity.onValueUpdate((byte) (aValue & 0x7F));
                    }
                }
                case GregTechTileClientEvents.CHANGE_COLOR -> {
                    if (aValue > 16 || aValue < 0) aValue = 0;
                    mColor = (byte) aValue;
                }
                case GregTechTileClientEvents.CHANGE_REDSTONE_OUTPUT -> setRedstoneOutput(aValue);
                case GregTechTileClientEvents.DO_SOUND -> {
                    if (hasValidMetaTileEntity() && mTickTimer > 20)
                        mMetaTileEntity.doSound((byte) aValue, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5);
                }
                case GregTechTileClientEvents.START_SOUND_LOOP -> {
                    if (hasValidMetaTileEntity() && mTickTimer > 20)
                        mMetaTileEntity.startSoundLoop((byte) aValue, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5);
                }
                case GregTechTileClientEvents.STOP_SOUND_LOOP -> {
                    if (hasValidMetaTileEntity() && mTickTimer > 20)
                        mMetaTileEntity.stopSoundLoop((byte) aValue, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5);
                }
                case GregTechTileClientEvents.CHANGE_LIGHT -> mLightValue = (byte) aValue;
            }
        }
        return true;
    }

    @Override
    public ArrayList<String> getDebugInfo(EntityPlayer aPlayer, int aLogLevel) {
        final ArrayList<String> tList = new ArrayList<>();
        if (aLogLevel > 2) {
            tList.add(
                "Meta-ID: " + EnumChatFormatting.BLUE
                    + mID
                    + EnumChatFormatting.RESET
                    + (canAccessData() ? EnumChatFormatting.GREEN + " valid" + EnumChatFormatting.RESET
                        : EnumChatFormatting.RED + " invalid" + EnumChatFormatting.RESET)
                    + (mMetaTileEntity == null
                        ? EnumChatFormatting.RED + " MetaTileEntity == null!" + EnumChatFormatting.RESET
                        : " "));
        }
        if (aLogLevel > 1 && mMetaTileEntity != null) {
            addProfilingInformation(tList);
            tList.add(
                "Is" + (mMetaTileEntity.isAccessAllowed(aPlayer) ? " "
                    : EnumChatFormatting.RED + " not " + EnumChatFormatting.RESET) + "accessible for you");
            tList.add(
                "Recorded " + formatNumber(mMetaTileEntity.mSoundRequests)
                    + " sound requests in "
                    + formatNumber(mTickTimer - mLastCheckTick)
                    + " ticks.");
            mLastCheckTick = mTickTimer;
            mMetaTileEntity.mSoundRequests = 0;
        }
        if (aLogLevel > 0) {
            tList.add(
                "Machine is " + (mActive ? EnumChatFormatting.GREEN + "active" + EnumChatFormatting.RESET
                    : EnumChatFormatting.RED + "inactive" + EnumChatFormatting.RESET));
            if (!mHasEnoughEnergy) tList
                .add(EnumChatFormatting.RED + "ATTENTION: This Device needs more power." + EnumChatFormatting.RESET);
        }
        if (joinedIc2Enet) tList.add("Joined IC2 ENet");
        return mMetaTileEntity.getSpecialDebugInfo(this, aPlayer, aLogLevel, tList);
    }

    @Override
    public boolean isGivingInformation() {
        return canAccessData() && mMetaTileEntity.isGivingInformation();
    }

    @Override
    public String[] getInfoData() {
        return canAccessData() ? getMetaTileEntity().getInfoData() : GTValues.emptyStringArray;
    }

    @Override
    public Map<String, String> getInfoMap() {
        return canAccessData() ? getMetaTileEntity().getInfoMap() : Collections.emptyMap();
    }

    @Override
    public ForgeDirection getBackFacing() {
        return mFacing.getOpposite();
    }

    @Override
    public ForgeDirection getFrontFacing() {
        return mFacing;
    }

    @Override
    public void setFrontFacing(ForgeDirection aFacing) {
        if (isValidFacing(aFacing)) {
            mFacing = aFacing;
            mMetaTileEntity.onFacingChange();

            doEnetUpdate();
            cableUpdateDelay = 10;

            if (mMetaTileEntity.shouldTriggerBlockUpdate()) {
                // If we're triggering a block update this will call onMachineBlockUpdate()
                GregTechAPI.causeMachineUpdate(worldObj, xCoord, yCoord, zCoord);
            } else {
                // If we're not trigger a cascading one, call the update here.
                onMachineBlockUpdate();
            }
        }
    }

    @Override
    public int getSizeInventory() {
        if (canAccessData()) return mMetaTileEntity.getSizeInventory();
        return 0;
    }

    @Override
    public ItemStack getStackInSlot(int aIndex) {
        if (canAccessData()) return mMetaTileEntity.getStackInSlot(aIndex);
        return null;
    }

    @Override
    public void setInventorySlotContents(int aIndex, ItemStack aStack) {
        mInventoryChanged = true;
        if (canAccessData()) {
            markDirty();
            mMetaTileEntity.setInventorySlotContents(
                aIndex,
                worldObj.isRemote ? aStack : GTOreDictUnificator.setStack(true, aStack));
        }
    }

    @Override
    public String getInventoryName() {
        if (canAccessData()) return mMetaTileEntity.getInventoryName();
        if (GregTechAPI.METATILEENTITIES[mID] != null) return GregTechAPI.METATILEENTITIES[mID].getInventoryName();
        return "";
    }

    @Override
    public int getInventoryStackLimit() {
        if (canAccessData()) return mMetaTileEntity.getInventoryStackLimit();
        return 64;
    }

    @Override
    public void openInventory() {}

    @Override
    public void closeInventory() {}

    @Override
    public boolean isUseableByPlayer(EntityPlayer aPlayer) {
        return canAccessData() && playerOwnsThis(aPlayer, false)
            && mTickTimer > 1
            && getTileEntityOffset(0, 0, 0) == this
            && aPlayer.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) < 64
            && mMetaTileEntity.isAccessAllowed(aPlayer);
    }

    @Override
    public void validate() {
        super.validate();
        mTickTimer = 0;
    }

    @Override
    public void invalidate() {
        tileEntityInvalid = false;
        leaveEnet();
        if (canAccessData()) {
            invalidateAE();
            mMetaTileEntity.onRemoval();
            mMetaTileEntity.setBaseMetaTileEntity(null);
        }
        super.invalidate();
    }

    @Override
    public void onChunkUnload() {
        if (canAccessData()) {
            onCoverUnload();
            mMetaTileEntity.onUnload();
        }
        super.onChunkUnload();
        onChunkUnloadAE();
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        final ItemStack stack = getStackInSlot(slot);
        if (stack != null) setInventorySlotContents(slot, null);
        return stack;
    }

    /**
     * Checks validity of meta tile and delegates to it
     */
    @Override
    public void onMachineBlockUpdate() {
        if (canAccessData()) mMetaTileEntity.onMachineBlockUpdate();
        cableUpdateDelay = 10;
    }

    /**
     * Checks validity of meta tile and delegates to it
     */
    @Override
    public boolean isMachineBlockUpdateRecursive() {
        return canAccessData() && mMetaTileEntity.isMachineBlockUpdateRecursive();
    }

    @Override
    public int getProgress() {
        return canAccessData() ? mMetaTileEntity.getProgresstime() : 0;
    }

    @Override
    public int getMaxProgress() {
        return canAccessData() ? mMetaTileEntity.maxProgresstime() : 0;
    }

    @Override
    public boolean increaseProgress(int aProgressAmountInTicks) {
        return canAccessData() && mMetaTileEntity.increaseProgress(aProgressAmountInTicks) != aProgressAmountInTicks;
    }

    @Override
    public boolean hasThingsToDo() {
        return getMaxProgress() > 0;
    }

    @Override
    public void enableWorking() {
        if (!mWorks) mWorkUpdate = true;
        mWorks = true;
        setShutdownStatus(false);
        if (hasValidMetaTileEntity()) {
            mMetaTileEntity.onEnableWorking();
        }
    }

    @Override
    public void disableWorking() {
        mWorks = false;
        if (hasValidMetaTileEntity()) {
            mMetaTileEntity.onDisableWorking();
        }
    }

    @Override
    public boolean isAllowedToWork() {
        return mWorks;
    }

    @Override
    public boolean hasWorkJustBeenEnabled() {
        return mWorkUpdate;
    }

    @Override
    public int getMetaTileID() {
        return mID;
    }

    @Override
    public int setMetaTileID(short aID) {
        return mID = aID;
    }

    @Nullable
    @Override
    public <T> T getCapability(@NotNull Class<T> capability, @NotNull ForgeDirection side) {
        if (canAccessData()) {
            return mMetaTileEntity.getCapability(capability, side);
        }
        return null;
    }

    @Override
    public boolean isActive() {
        return mActive;
    }

    @Override
    public void setActive(boolean aActive) {
        mActive = aActive;
        if (hasValidMetaTileEntity()) {
            mMetaTileEntity.onSetActive(aActive);
        }
    }

    @Override
    public long getTimer() {
        return mTickTimer;
    }

    @Override
    public boolean decreaseStoredEnergyUnits(long aEnergy, boolean aIgnoreTooLessEnergy) {
        if (!canAccessData()) return false;
        return mHasEnoughEnergy = decreaseStoredEU(aEnergy, aIgnoreTooLessEnergy) || decreaseStoredSteam(aEnergy, false)
            || (aIgnoreTooLessEnergy && (decreaseStoredSteam(aEnergy, true)));
    }

    @Override
    public boolean increaseStoredEnergyUnits(long aEnergy, boolean aIgnoreTooMuchEnergy) {
        if (!canAccessData()) return false;
        if (getStoredEU() < getEUCapacity() || aIgnoreTooMuchEnergy) {
            setStoredEU(mMetaTileEntity.getEUVar() + aEnergy);
            return true;
        }
        return false;
    }

    @Override
    public boolean inputEnergyFrom(ForgeDirection side) {
        return inputEnergyFrom(side, true);
    }

    @Override
    public boolean inputEnergyFrom(ForgeDirection side, boolean waitForActive) {
        if (side == ForgeDirection.UNKNOWN) return true;
        if (isServerSide() && waitForActive) return mActiveEUInputs[side.ordinal()] && !mReleaseEnergy;
        return isEnergyInputSide(side);
    }

    @Override
    public boolean outputsEnergyTo(ForgeDirection side) {
        return outputsEnergyTo(side, true);
    }

    @Override
    public boolean outputsEnergyTo(ForgeDirection side, boolean waitForActive) {
        if (side == ForgeDirection.UNKNOWN) return true;
        if (isServerSide() && waitForActive) return (mActiveEUOutputs[side.ordinal()]) || mReleaseEnergy;
        return isEnergyOutputSide(side);
    }

    @Override
    public boolean isEnetOutput() {
        return mMetaTileEntity != null && mMetaTileEntity.isEnetOutput();
    }

    @Override
    public boolean isEnetInput() {
        return mMetaTileEntity != null && mMetaTileEntity.isEnetInput();
    }

    public void generatePowerNodes() {
        if (isServerSide() && (isEnetInput() || isEnetOutput())) {
            final int time = MinecraftServer.getServer()
                .getTickCounter();
            for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
                if (outputsEnergyTo(side, false) || inputEnergyFrom(side, false)) {
                    final IGregTechTileEntity TE = getIGregTechTileEntityAtSide(side);
                    if (TE instanceof BaseMetaPipeEntity pipe
                        && (pipe.getConnections() & side.getOpposite().flag) != 0) {
                        final Node node = pipe.getNode();
                        if (node == null) {
                            new GenerateNodeMapPower(pipe);
                        } else if (node.mCreationTime != time) {
                            GenerateNodeMap.clearNodeMap(node, -1);
                            new GenerateNodeMapPower(pipe);
                        }
                    }
                }
            }
        }
    }

    public void setCableUpdateDelay(int delay) {
        cableUpdateDelay = delay;
    }

    @Override
    public long getOutputAmperage() {
        if (canAccessData() && mMetaTileEntity.isElectric()) return mMetaTileEntity.maxAmperesOut();
        return 0;
    }

    @Override
    public long getOutputVoltage() {
        if (canAccessData() && mMetaTileEntity.isElectric() && mMetaTileEntity.isEnetOutput())
            return mMetaTileEntity.maxEUOutput();
        return 0;
    }

    @Override
    public long getInputAmperage() {
        if (canAccessData() && mMetaTileEntity.isElectric()) return mMetaTileEntity.maxAmperesIn();
        return 0;
    }

    @Override
    public long getInputVoltage() {
        if (canAccessData() && mMetaTileEntity.isElectric()) return mMetaTileEntity.maxEUInput();
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean increaseStoredSteam(long aEnergy, boolean aIgnoreTooMuchEnergy) {
        if (!canAccessData()) return false;
        if (mMetaTileEntity.getSteamVar() < getSteamCapacity() || aIgnoreTooMuchEnergy) {
            setStoredSteam(mMetaTileEntity.getSteamVar() + aEnergy);
            return true;
        }
        return false;
    }

    @Override
    public long getUniversalEnergyStored() {
        return Math.max(getStoredEU(), getStoredSteam());
    }

    @Override
    public long getUniversalEnergyCapacity() {
        return Math.max(getEUCapacity(), getSteamCapacity());
    }

    @Override
    public long getStoredEU() {
        if (canAccessData()) return Math.min(mMetaTileEntity.getEUVar(), getEUCapacity());
        return 0;
    }

    @Override
    public long getEUCapacity() {
        if (canAccessData()) return mMetaTileEntity.maxEUStore();
        return 0;
    }

    @Override
    public long getStoredSteam() {
        if (canAccessData()) return Math.min(mMetaTileEntity.getSteamVar(), getSteamCapacity());
        return 0;
    }

    @Override
    public long getSteamCapacity() {
        if (canAccessData()) return mMetaTileEntity.maxSteamStore();
        return 0;
    }

    @Override
    public ITexture[] getTexture(Block aBlock, ForgeDirection side) {
        final ITexture coverTexture = getCoverTexture(side);
        final ITexture[] textureUncovered = hasValidMetaTileEntity()
            ? mMetaTileEntity
                .getTexture(this, side, mFacing, (byte) (mColor - 1), mActive, getOutputRedstoneSignal(side) > 0)
            : Textures.BlockIcons.ERROR_RENDERING;
        final ITexture[] textureCovered;
        if (coverTexture != null) {
            textureCovered = Arrays.copyOf(textureUncovered, textureUncovered.length + 1);
            textureCovered[textureUncovered.length] = coverTexture;
            return textureCovered;
        } else {
            return textureUncovered;
        }
    }

    private boolean isEnergyInputSide(ForgeDirection side) {
        if (side != ForgeDirection.UNKNOWN) {
            if (!getCoverAtSide(side).letsEnergyIn()) return false;
            if (isInvalid() || mReleaseEnergy) return false;
            if (canAccessData() && mMetaTileEntity.isElectric() && mMetaTileEntity.isEnetInput())
                return mMetaTileEntity.isInputFacing(side);
        }
        return false;
    }

    private boolean isEnergyOutputSide(ForgeDirection side) {
        if (side != ForgeDirection.UNKNOWN) {
            if (!getCoverAtSide(side).letsEnergyOut()) return false;
            if (isInvalid() || mReleaseEnergy) return mReleaseEnergy;
            if (canAccessData() && mMetaTileEntity.isElectric() && mMetaTileEntity.isEnetOutput())
                return mMetaTileEntity.isOutputFacing(side);
        }
        return false;
    }

    @Override
    protected final boolean hasValidMetaTileEntity() {
        return mMetaTileEntity != null && mMetaTileEntity.getBaseMetaTileEntity() == this;
    }

    public boolean setStoredEU(long aEnergy) {
        if (!canAccessData()) return false;
        if (aEnergy < 0) aEnergy = 0;
        mMetaTileEntity.setEUVar(aEnergy);
        return true;
    }

    public boolean setStoredSteam(long aEnergy) {
        if (!canAccessData()) return false;
        if (aEnergy < 0) aEnergy = 0;
        mMetaTileEntity.setSteamVar(aEnergy);
        return true;
    }

    public boolean decreaseStoredEU(long aEnergy, boolean aIgnoreTooLessEnergy) {
        if (!canAccessData()) {
            return false;
        }
        if (aIgnoreTooLessEnergy || mMetaTileEntity.getEUVar() - aEnergy >= 0) {
            setStoredEU(mMetaTileEntity.getEUVar() - aEnergy);
            if (mMetaTileEntity.getEUVar() < 0) {
                setStoredEU(0);
                return false;
            }
            return true;
        }
        return false;
    }

    public boolean decreaseStoredSteam(long aEnergy, boolean aIgnoreTooLessEnergy) {
        if (!canAccessData()) return false;
        if (aIgnoreTooLessEnergy || mMetaTileEntity.getSteamVar() - aEnergy >= 0) {
            setStoredSteam(mMetaTileEntity.getSteamVar() - aEnergy);
            if (mMetaTileEntity.getSteamVar() < 0) {
                setStoredSteam(0);
                return false;
            }
            return true;
        }
        return false;
    }

    public boolean playerOwnsThis(EntityPlayer aPlayer, boolean aCheckPrecicely) {
        if (!canAccessData()) return false;
        if (aCheckPrecicely || privateAccess() || (mOwnerName.isEmpty()))
            if ((mOwnerName.isEmpty()) && isServerSide()) {
                setOwnerName(aPlayer.getDisplayName());
                setOwnerUuid(aPlayer.getUniqueID());
            } else return !privateAccess() || aPlayer.getDisplayName()
                .equals("Player") || mOwnerName.equals("Player") || mOwnerName.equals(aPlayer.getDisplayName());
        return true;
    }

    public boolean privateAccess() {
        if (!canAccessData()) return mLockUpgrade;
        return mLockUpgrade || mMetaTileEntity.ownerControl();
    }

    public void doEnergyExplosion() {
        if (getUniversalEnergyCapacity() > 0 && getUniversalEnergyStored() >= getUniversalEnergyCapacity() / 5) {
            GTLog.exp.println(
                "Energy Explosion, injected " + getUniversalEnergyStored()
                    + "EU >= "
                    + getUniversalEnergyCapacity() / 5D
                    + "Capacity of the Machine!");

            doExplosion(
                oldOutput * (getUniversalEnergyStored() >= getUniversalEnergyCapacity() ? 4
                    : getUniversalEnergyStored() >= getUniversalEnergyCapacity() / 2 ? 2 : 1));
            GTMod.achievements.issueAchievement(
                this.getWorldObj()
                    .getPlayerEntityByName(mOwnerName),
                "electricproblems");
        }
    }

    @Override
    public void doExplosion(long aAmount) {
        if (canAccessData()) {
            // This is only for Electric Machines
            if (GregTechAPI.sMachineWireFire && mMetaTileEntity.isElectric()) {
                try {
                    mReleaseEnergy = true;
                    IEnergyConnected.Util.emitEnergyToNetwork(V[5], Math.max(1, getStoredEU() / V[5]), this);
                } catch (Exception ignored) {}
            }
            mReleaseEnergy = false;
            // Normal Explosion Code
            mMetaTileEntity.onExplosion();
            if (GTMod.proxy.mExplosionItemDrop) {
                for (int i = 0; i < this.getSizeInventory(); i++) {
                    final ItemStack tItem = this.getStackInSlot(i);
                    if ((tItem != null) && (tItem.stackSize > 0) && (this.isValidSlot(i))) {
                        dropItems(tItem);
                        this.setInventorySlotContents(i, null);
                    }
                }
            }
            Pollution.addPollution((TileEntity) this, GTMod.proxy.mPollutionOnExplosion);
            mMetaTileEntity.doExplosion(aAmount);
        }
    }

    public void dropItems(ItemStack tItem) {
        if (tItem == null) return;
        final EntityItem tItemEntity = new EntityItem(
            this.worldObj,
            this.xCoord + XSTR_INSTANCE.nextFloat() * 0.8F + 0.1F,
            this.yCoord + XSTR_INSTANCE.nextFloat() * 0.8F + 0.1F,
            this.zCoord + XSTR_INSTANCE.nextFloat() * 0.8F + 0.1F,
            new ItemStack(tItem.getItem(), tItem.stackSize, tItem.getItemDamage()));
        if (tItem.hasTagCompound()) {
            tItemEntity.getEntityItem()
                .setTagCompound(
                    (NBTTagCompound) tItem.getTagCompound()
                        .copy());
        }
        tItemEntity.motionX = (XSTR_INSTANCE.nextGaussian() * 0.0500000007450581D);
        tItemEntity.motionY = (XSTR_INSTANCE.nextGaussian() * 0.0500000007450581D + 0.2000000029802322D);
        tItemEntity.motionZ = (XSTR_INSTANCE.nextGaussian() * 0.0500000007450581D);
        tItemEntity.hurtResistantTime = 999999;
        tItemEntity.lifespan = 60000;
        ((EntityItemAccessor) tItemEntity).gt5$setHealth(99999999);
        this.worldObj.spawnEntityInWorld(tItemEntity);
        tItem.stackSize = 0;
    }

    @Override
    public ArrayList<ItemStack> getDrops() {
        final ItemStack rStack = new ItemStack(GregTechAPI.sBlockMachines, 1, mID);
        final NBTTagCompound tNBT = new NBTTagCompound();
        if (mLockUpgrade) tNBT.setBoolean("mLockUpgrade", true);
        if (mColor > 0) tNBT.setByte("mColor", mColor);
        if (mOtherUpgrades > 0) tNBT.setByte("mOtherUpgrades", mOtherUpgrades);

        writeCoverNBT(tNBT, true);

        if (hasValidMetaTileEntity()) mMetaTileEntity.setItemNBT(tNBT);
        if (!tNBT.hasNoTags()) rStack.setTagCompound(tNBT);

        onBaseTEDestroyed();
        return new ArrayList<>(Collections.singletonList(rStack));
    }

    public int getUpgradeCount() {
        return (mLockUpgrade ? 1 : 0) + mOtherUpgrades;
    }

    @Override
    public boolean onRightclick(final EntityPlayer aPlayer, final ForgeDirection side, final float aX, final float aY,
        final float aZ) {
        final ForgeDirection wrenchingSide = GTUtility.determineWrenchingSide(side, aX, aY, aZ);
        final ForgeDirection effectiveSide = !hasCoverAtSide(side) ? wrenchingSide : side;
        Cover effectiveSideCover = getCoverAtSide(effectiveSide);
        if (isClientSide()) {
            // Place/configure Cover, sneak can also be: screwdriver, wrench, side cutter, soldering iron
            if (aPlayer.isSneaking()) {
                return (effectiveSideCover.hasCoverGUI());
            }

            if (!getCoverAtSide(side).isGUIClickable()) return false;
        }

        if (isServerSide()) {
            if (!privateAccess() || aPlayer.getDisplayName()
                .equalsIgnoreCase(getOwnerName())) {
                final ItemStack tCurrentItem = aPlayer.inventory.getCurrentItem();
                if (tCurrentItem != null) {
                    if (getColorization() >= 0
                        && GTUtility.areStacksEqual(new ItemStack(Items.water_bucket, 1), tCurrentItem)) {
                        tCurrentItem.func_150996_a(Items.bucket);
                        setColorization((byte) (getColorization() >= 16 ? -2 : -1));
                        return true;
                    }
                    if (GTUtility.isStackInList(tCurrentItem, GregTechAPI.sWrenchList)) {
                        if (aPlayer.isSneaking() && mMetaTileEntity instanceof MTEBasicMachine
                            && ((MTEBasicMachine) mMetaTileEntity).setMainFacing(wrenchingSide)) {
                            GTModHandler.damageOrDechargeItem(tCurrentItem, 1, 1000, aPlayer);
                            sendSoundToPlayers(SoundResource.GTCEU_OP_WRENCH, 1.0F, 1);
                            cableUpdateDelay = 10;
                        } else if (mMetaTileEntity
                            .onWrenchRightClick(side, wrenchingSide, aPlayer, aX, aY, aZ, tCurrentItem)) {
                                GTModHandler.damageOrDechargeItem(tCurrentItem, 1, 1000, aPlayer);
                                sendSoundToPlayers(SoundResource.GTCEU_OP_WRENCH, 1.0F, 1);
                                cableUpdateDelay = 10;
                            }

                        if (tCurrentItem.stackSize == 0) ForgeEventFactory.onPlayerDestroyItem(aPlayer, tCurrentItem);
                        return true;
                    }

                    if (GTUtility.isStackInList(tCurrentItem, GregTechAPI.sScrewdriverList)) {
                        if (GTModHandler.damageOrDechargeItem(tCurrentItem, 1, 200, aPlayer)) {
                            getCoverAtSide(side).onCoverScrewdriverClick(aPlayer, aX, aY, aZ);
                            mMetaTileEntity.onScrewdriverRightClick(side, aPlayer, aX, aY, aZ, tCurrentItem);
                            sendSoundToPlayers(SoundResource.GTCEU_OP_SCREWDRIVER, 1.0F, 1);
                            if (tCurrentItem.stackSize == 0)
                                ForgeEventFactory.onPlayerDestroyItem(aPlayer, tCurrentItem);
                        }
                        return true;
                    }

                    if (GTUtility.isStackInList(tCurrentItem, GregTechAPI.sHardHammerList)) {
                        if (GTModHandler.damageOrDechargeItem(tCurrentItem, 1, 1000, aPlayer)) {
                            if (aPlayer.isSneaking()) {
                                mInputDisabled = !mInputDisabled;
                                if (mInputDisabled) mOutputDisabled = !mOutputDisabled;
                                GTUtility.sendChatComp(
                                    aPlayer,
                                    new ChatComponentTranslation(
                                        mInputDisabled ? "GT5U.chat.machine.auto_input.disable"
                                            : "GT5U.chat.machine.auto_input.enable").appendText("  ")
                                                .appendSibling(
                                                    new ChatComponentTranslation(
                                                        mOutputDisabled ? "GT5U.chat.machine.auto_output.disable"
                                                            : "GT5U.chat.machine.auto_output.enable")));
                                sendSoundToPlayers(SoundResource.GTCEU_LOOP_FORGE_HAMMER, 1.0F, 1);
                            } else {
                                mMuffler = !mMuffler;
                                GTUtility.sendChatTrans(
                                    aPlayer,
                                    mMuffler ? "GT5U.machines.muffled.on" : "GT5U.machines.muffled.off");
                            }
                            if (tCurrentItem.stackSize == 0)
                                ForgeEventFactory.onPlayerDestroyItem(aPlayer, tCurrentItem);
                        }
                        return true;
                    }

                    if (GTUtility.isStackInList(tCurrentItem, GregTechAPI.sSoftMalletList)) {
                        if (GTModHandler.damageOrDechargeItem(tCurrentItem, 1, 1000, aPlayer)) {
                            if (mWorks) disableWorking();
                            else {
                                if (this.getLastShutDownReason() == ShutDownReasonRegistry.POWER_LOSS) {
                                    GTMod.proxy.powerfailTracker.removePowerfailEvents(this);
                                }
                                enableWorking();
                            }
                            {
                                if (getMetaTileEntity() != null && getMetaTileEntity().hasAlternativeModeText()) {
                                    // FIXME: localize it
                                    GTUtility.sendChatToPlayer(aPlayer, getMetaTileEntity().getAlternativeModeText());
                                } else {
                                    GTUtility.sendChatTrans(
                                        aPlayer,
                                        isAllowedToWork() ? "GT5U.chat.machine.processing.enable"
                                            : "GT5U.chat.machine.processing.disable");
                                }
                            }
                            sendSoundToPlayers(SoundResource.GTCEU_OP_SOFT_HAMMER, 1.0F, 1);
                            if (tCurrentItem.stackSize == 0)
                                ForgeEventFactory.onPlayerDestroyItem(aPlayer, tCurrentItem);
                        }
                        return true;
                    }

                    if (GTUtility.isStackInList(tCurrentItem, GregTechAPI.sSolderingToolList)) {
                        if (mMetaTileEntity
                            .onSolderingToolRightClick(side, wrenchingSide, aPlayer, aX, aY, aZ, tCurrentItem)) {
                            // logic handled internally
                            sendSoundToPlayers(SoundResource.IC2_TOOLS_BATTERY_USE, 1.0F, -1);
                        } else if (GTModHandler.useSolderingIron(tCurrentItem, aPlayer)) {
                            mStrongRedstone ^= wrenchingSide.flag;
                            // FIXME: localize wrenchingSide
                            GTUtility.sendChatTrans(
                                aPlayer,
                                (mStrongRedstone & wrenchingSide.flag) != 0
                                    ? "GT5U.chat.machine.redstone_output_set.strong"
                                    : "GT5U.chat.machine.redstone_output_set.weak",
                                wrenchingSide);
                            sendSoundToPlayers(SoundResource.IC2_TOOLS_BATTERY_USE, 3.0F, -1);
                            issueBlockUpdate();
                        }
                        if (tCurrentItem.stackSize == 0) ForgeEventFactory.onPlayerDestroyItem(aPlayer, tCurrentItem);
                        doEnetUpdate();
                        cableUpdateDelay = 10;
                        return true;
                    }

                    if (GTUtility.isStackInList(tCurrentItem, GregTechAPI.sWireCutterList)) {
                        if (mMetaTileEntity
                            .onWireCutterRightClick(side, wrenchingSide, aPlayer, aX, aY, aZ, tCurrentItem)) {
                            // logic handled internally
                            sendSoundToPlayers(SoundResource.GTCEU_OP_WIRECUTTER, 1.0F, 1);
                            if (tCurrentItem.stackSize == 0)
                                ForgeEventFactory.onPlayerDestroyItem(aPlayer, tCurrentItem);
                        }
                        doEnetUpdate();
                        cableUpdateDelay = 10;
                        return true;
                    }

                    if (!hasCoverAtSide(effectiveSide)) {
                        if (aPlayer.isSneaking() && CoverRegistry.isCover(tCurrentItem)) {
                            if (CoverRegistry.getCoverPlacer(tCurrentItem)
                                .isCoverPlaceable(effectiveSide, tCurrentItem, this)
                                && mMetaTileEntity.allowCoverOnSide(effectiveSide, tCurrentItem)) {

                                CoverRegistry.getCoverPlacer(tCurrentItem)
                                    .placeCover(aPlayer, tCurrentItem, this, effectiveSide);

                                if (!aPlayer.capabilities.isCreativeMode) tCurrentItem.stackSize--;
                                sendSoundToPlayers(SoundResource.GTCEU_OP_WRENCH, 1.0F, 1);
                                sendClientData();
                            }
                            return true;
                        }
                    } else {
                        if (aPlayer.isSneaking() && GTUtility.isStackInList(tCurrentItem, GregTechAPI.sCrowbarList)) {
                            if (GTModHandler.damageOrDechargeItem(tCurrentItem, 1, 1000, aPlayer)) {
                                sendSoundToPlayers(SoundResource.RANDOM_BREAK, 1.0F, -1);
                                dropCover(effectiveSide, side);
                                if (tCurrentItem.stackSize == 0)
                                    ForgeEventFactory.onPlayerDestroyItem(aPlayer, tCurrentItem);
                            }
                            return true;
                        } else if (GTUtility.isStackInList(tCurrentItem, GregTechAPI.sJackhammerList)) {
                            // Configuration of delicate electronics calls for a tool with precision and subtlety.
                            if (GTModHandler.damageOrDechargeItem(tCurrentItem, 1, 1000, aPlayer)) {
                                if (effectiveSideCover.isValid()) {
                                    if (effectiveSideCover.allowsTickRateAddition()) {
                                        effectiveSideCover.onCoverJackhammer(aPlayer);
                                        sendSoundToPlayers(SoundResource.IC2_TOOLS_DRILL_DRILL_SOFT, 1.0F, 1);

                                    } else {
                                        GTUtility.sendChatTrans(aPlayer, "gt.cover.info.chat.tick_rate_not_allowed");
                                    }
                                    if (tCurrentItem.stackSize == 0)
                                        ForgeEventFactory.onPlayerDestroyItem(aPlayer, tCurrentItem);
                                    return true;
                                }
                            }
                        }
                    }
                    // End item != null
                } else if (aPlayer.isSneaking()) { // Sneak click, no tool -> open cover config if possible.
                    return effectiveSideCover.isValid() && effectiveSideCover.onCoverShiftRightClick(aPlayer);
                }

                if (getCoverAtSide(side).onCoverRightClick(aPlayer, aX, aY, aZ)) return true;

                if (!getCoverAtSide(side).isGUIClickable()) return false;

                if (isUpgradable() && tCurrentItem != null) {
                    if (ItemList.Upgrade_Lock.isStackEqual(aPlayer.inventory.getCurrentItem())) {
                        if (isUpgradable() && !mLockUpgrade) {
                            mLockUpgrade = true;
                            setOwnerName(aPlayer.getDisplayName());
                            setOwnerUuid(aPlayer.getUniqueID());
                            sendSoundToPlayers(SoundResource.GTCEU_OP_CLICK, 1.0F, 1);
                            if (!aPlayer.capabilities.isCreativeMode) aPlayer.inventory.getCurrentItem().stackSize--;
                        }
                        return true;
                    }
                }
            }
        }

        try {
            if (!aPlayer.isSneaking() && hasValidMetaTileEntity())
                return mMetaTileEntity.onRightclick(this, aPlayer, side, aX, aY, aZ);
        } catch (Exception e) {
            GTLog.err.println(
                "Encountered Exception while rightclicking TileEntity, the Game should've crashed now, but I prevented that. Please report immediately to GregTech Intergalactical!!!");
            e.printStackTrace(GTLog.err);
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public void onLeftclick(EntityPlayer aPlayer) {
        try {
            if (aPlayer != null && hasValidMetaTileEntity()) mMetaTileEntity.onLeftclick(this, aPlayer);
        } catch (Exception e) {
            GTLog.err.println(
                "Encountered Exception while leftclicking TileEntity, the Game should've crashed now, but I prevented that. Please report immediately to GregTech Intergalactical!!!");
            e.printStackTrace(GTLog.err);
        }
    }

    @Override
    public boolean isDigitalChest() {
        if (canAccessData()) return mMetaTileEntity.isDigitalChest();
        return false;
    }

    @Override
    public ItemStack[] getStoredItemData() {
        if (canAccessData()) return mMetaTileEntity.getStoredItemData();
        return null;
    }

    @Override
    public void setItemCount(int aCount) {
        if (canAccessData()) mMetaTileEntity.setItemCount(aCount);
    }

    @Override
    public int getMaxItemCount() {
        if (canAccessData()) return mMetaTileEntity.getMaxItemCount();
        return 0;
    }

    /**
     * Can put aStack into Slot
     */
    @Override
    public boolean isItemValidForSlot(int aIndex, ItemStack aStack) {
        return canAccessData() && mMetaTileEntity.isItemValidForSlot(aIndex, aStack);
    }

    /**
     * returns all valid Inventory Slots, no matter which Side (Unless it's covered). The Side Stuff is done in the
     * following two Functions.
     */
    @Override
    public int[] getAccessibleSlotsFromSide(int ordinalSide) {
        final Cover cover = getCoverAtSide(ForgeDirection.getOrientation(ordinalSide));
        if (canAccessData() && (cover.letsItemsOut(-1) || cover.letsItemsIn(-1)))
            return mMetaTileEntity.getAccessibleSlotsFromSide(ordinalSide);
        return GTValues.emptyIntArray;
    }

    /**
     * Can put aStack into Slot at Side
     */
    @Override
    public boolean canInsertItem(int slotIndex, ItemStack stack, int ordinalSide) {
        return canAccessData() && (mRunningThroughTick || !mInputDisabled)
            && getCoverAtSide(ForgeDirection.getOrientation(ordinalSide)).letsItemsIn(slotIndex)
            && mMetaTileEntity.canInsertItem(slotIndex, stack, ordinalSide);
    }

    /**
     * Can pull stack out of Slot from Side
     */
    @Override
    public boolean canExtractItem(int slotIndex, ItemStack stack, int ordinalSide) {
        final ForgeDirection side = ForgeDirection.getOrientation(ordinalSide);
        return canAccessData() && (mRunningThroughTick || !mOutputDisabled)
            && getCoverAtSide(side).letsItemsOut(slotIndex)
            && mMetaTileEntity.canExtractItem(slotIndex, stack, ordinalSide);
    }

    @Override
    public boolean isUpgradable() {
        return canAccessData() && getUpgradeCount() < 8;
    }

    @Override
    public byte getGeneralRS(ForgeDirection side) {
        if (mMetaTileEntity == null) return 0;
        return mMetaTileEntity.allowGeneralRedstoneOutput() ? mSidedRedstone[side.ordinal()] : 0;
    }

    @Override
    public boolean isMuffled() {
        return mMuffler;
    }

    @Override
    public boolean isMufflerUpgradable() {
        return isUpgradable() && !isMuffled();
    }

    @Override
    public boolean addMufflerUpgrade() {
        if (isMufflerUpgradable()) return mMuffler = true;
        return false;
    }

    @Override
    public void setMuffler(boolean value) {
        mMuffler = value;
    }

    @Override
    public void markInventoryBeenModified() {
        mInventoryChanged = true;
    }

    @Override
    public IMetaTileEntity getMetaTileEntity() {
        return hasValidMetaTileEntity() ? mMetaTileEntity : null;
    }

    @Override
    public void setMetaTileEntity(IMetaTileEntity aMetaTileEntity) {
        if (aMetaTileEntity instanceof MetaTileEntity || aMetaTileEntity == null)
            mMetaTileEntity = (MetaTileEntity) aMetaTileEntity;
        else {
            GT_FML_LOGGER.error(
                "Unknown meta tile entity set! Class {}, inventory name {}.",
                aMetaTileEntity.getClass(),
                aMetaTileEntity.getInventoryName());
        }
    }

    public byte getLightValue() {
        return mLightValue;
    }

    @Override
    public void setLightValue(byte aLightValue) {
        mLightValue = (byte) (aLightValue & 15);
    }

    @Override
    public long getAverageElectricInput() {
        long rEU = 0;
        for (int i = 0; i < mAverageEUInput.length; ++i) if (i != mAverageEUInputIndex) rEU += mAverageEUInput[i];
        return rEU / (mAverageEUInput.length - 1);
    }

    @Override
    public long getAverageElectricOutput() {
        long rEU = 0;
        for (int i = 0; i < mAverageEUOutput.length; ++i) if (i != mAverageEUOutputIndex) rEU += mAverageEUOutput[i];
        return rEU / (mAverageEUOutput.length - 1);
    }

    @Override
    public String getOwnerName() {
        if (GTUtility.isStringInvalid(mOwnerName)) return "Player";
        return mOwnerName;
    }

    @Override
    public String setOwnerName(String aName) {
        if (GTUtility.isStringInvalid(aName)) return mOwnerName = "Player";
        return mOwnerName = aName;
    }

    @Override
    public UUID getOwnerUuid() {
        return mOwnerUuid;
    }

    @Override
    public void setOwnerUuid(UUID uuid) {
        mOwnerUuid = uuid;
    }

    @Override
    public byte getComparatorValue(ForgeDirection side) {
        return canAccessData() ? mMetaTileEntity.getComparatorValue(side) : 0;
    }

    @Override
    public ItemStack decrStackSize(int aIndex, int aAmount) {
        if (canAccessData()) {
            mInventoryChanged = true;
            return mMetaTileEntity.decrStackSize(aIndex, aAmount);
        }
        return null;
    }

    @Override
    public long injectEnergyUnits(ForgeDirection side, long aVoltage, long aAmperage) {
        if (!canAccessData() || !mMetaTileEntity.isElectric()
            || !inputEnergyFrom(side)
            || aAmperage <= 0
            || aVoltage <= 0
            || getStoredEU() >= getEUCapacity()
            || mMetaTileEntity.maxAmperesIn() <= mAcceptedAmperes) return 0;
        if (aVoltage > getInputVoltage()) {
            GTLog.exp
                .println("Energy Explosion, injected " + aVoltage + "EU/t in a " + getInputVoltage() + "EU/t Machine!");
            doExplosion(aVoltage);
            return 0;
        }
        if (increaseStoredEnergyUnits(
            aVoltage * (aAmperage = Math.min(
                aAmperage,
                Math.min(
                    mMetaTileEntity.maxAmperesIn() - mAcceptedAmperes,
                    1 + ((getEUCapacity() - getStoredEU()) / aVoltage)))),
            true)) {
            mAverageEUInput[mAverageEUInputIndex] += aVoltage * aAmperage;
            mAcceptedAmperes += aAmperage;
            return aAmperage;
        }
        return 0;
    }

    @Override
    public boolean drainEnergyUnits(ForgeDirection side, long aVoltage, long aAmperage) {
        if (!canAccessData() || !mMetaTileEntity.isElectric()
            || !outputsEnergyTo(side)
            || getStoredEU() - (aVoltage * aAmperage) < mMetaTileEntity.getMinimumStoredEU()) return false;
        if (decreaseStoredEU(aVoltage * aAmperage, false)) {
            mAverageEUOutput[mAverageEUOutputIndex] += aVoltage * aAmperage;
            return true;
        }
        return false;
    }

    @Override
    public int fill(ForgeDirection side, FluidStack aFluid, boolean doFill) {
        if (mTickTimer > 5 && canAccessData()
            && (mRunningThroughTick || !mInputDisabled)
            && (side == ForgeDirection.UNKNOWN || (mMetaTileEntity.isLiquidInput(side)
                && getCoverAtSide(side).letsFluidIn(aFluid == null ? null : aFluid.getFluid()))))
            return mMetaTileEntity.fill(side, aFluid, doFill);
        return 0;
    }

    @Override
    public FluidStack drain(ForgeDirection side, int maxDrain, boolean doDrain) {
        if (mTickTimer > 5 && canAccessData()
            && (mRunningThroughTick || !mOutputDisabled)
            && (side == ForgeDirection.UNKNOWN
                || (mMetaTileEntity.isLiquidOutput(side) && getCoverAtSide(side).letsFluidOut(
                    mMetaTileEntity.getFluid() == null ? null
                        : mMetaTileEntity.getFluid()
                            .getFluid()))))
            return mMetaTileEntity.drain(side, maxDrain, doDrain);
        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection side, FluidStack aFluid, boolean doDrain) {
        if (mTickTimer > 5 && canAccessData()
            && (mRunningThroughTick || !mOutputDisabled)
            && (side == ForgeDirection.UNKNOWN || (mMetaTileEntity.isLiquidOutput(side)
                && getCoverAtSide(side).letsFluidOut(aFluid == null ? null : aFluid.getFluid()))))
            return mMetaTileEntity.drain(side, aFluid, doDrain);
        return null;
    }

    @Override
    public boolean canFill(ForgeDirection side, Fluid aFluid) {
        if (mTickTimer > 5 && canAccessData()
            && (mRunningThroughTick || !mInputDisabled)
            && (side == ForgeDirection.UNKNOWN
                || (mMetaTileEntity.isLiquidInput(side) && getCoverAtSide(side).letsFluidIn(aFluid))))
            return mMetaTileEntity.canFill(side, aFluid);
        return false;
    }

    @Override
    public boolean canDrain(ForgeDirection side, Fluid aFluid) {
        if (mTickTimer > 5 && canAccessData()
            && (mRunningThroughTick || !mOutputDisabled)
            && (side == ForgeDirection.UNKNOWN
                || (mMetaTileEntity.isLiquidOutput(side) && getCoverAtSide(side).letsFluidOut(aFluid))))
            return mMetaTileEntity.canDrain(side, aFluid);
        return false;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection side) {
        if (canAccessData() && (side == ForgeDirection.UNKNOWN
            || (mMetaTileEntity.isLiquidInput(side) && getCoverAtSide(side).letsFluidIn(null))
            || (mMetaTileEntity.isLiquidOutput(side) && getCoverAtSide(side).letsFluidOut(null))))
            return mMetaTileEntity.getTankInfo(side);
        return GTValues.emptyFluidTankInfo;
    }

    public double getOutputEnergyUnitsPerTick() {
        return oldOutput;
    }

    public boolean isTeleporterCompatible(ForgeDirection side) {
        return canAccessData() && mMetaTileEntity.isTeleporterCompatible();
    }

    public double demandedEnergyUnits() {
        if (mReleaseEnergy || !canAccessData() || !mMetaTileEntity.isEnetInput()) return 0;
        return getEUCapacity() - getStoredEU();
    }

    public double injectEnergyUnits(ForgeDirection aDirection, double aAmount) {
        return injectEnergyUnits(aDirection, (int) aAmount, 1) > 0 ? 0 : aAmount;
    }

    public boolean acceptsEnergyFrom(TileEntity aEmitter, ForgeDirection aDirection) {
        return inputEnergyFrom(aDirection);
    }

    public boolean emitsEnergyTo(TileEntity aReceiver, ForgeDirection aDirection) {
        return outputsEnergyTo(aDirection);
    }

    public double getOfferedEnergy() {
        return (canAccessData() && getStoredEU() - mMetaTileEntity.getMinimumStoredEU() >= oldOutput)
            ? Math.max(0, oldOutput)
            : 0;
    }

    public void drawEnergy(double amount) {
        mAverageEUOutput[mAverageEUOutputIndex] += amount;
        decreaseStoredEU((int) amount, true);
    }

    public int injectEnergy(ForgeDirection aForgeDirection, int aAmount) {
        return injectEnergyUnits(aForgeDirection, aAmount, 1) > 0 ? 0 : aAmount;
    }

    public int addEnergy(int aEnergy) {
        if (!canAccessData()) return 0;
        if (aEnergy > 0) increaseStoredEnergyUnits(aEnergy, true);
        else decreaseStoredEU(-aEnergy, true);
        return (int) Math.min(Integer.MAX_VALUE, mMetaTileEntity.getEUVar());
    }

    public boolean isAddedToEnergyNet() {
        return false;
    }

    @Override
    public boolean isSteampowered() {
        return getSteamCapacity() > 0;
    }

    public int demandsEnergy() {
        if (mReleaseEnergy || !canAccessData() || !mMetaTileEntity.isEnetInput()) return 0;
        return getCapacity() - getStored();
    }

    public int getCapacity() {
        return (int) Math.min(Integer.MAX_VALUE, getEUCapacity());
    }

    public int getStored() {
        return (int) Math.min(Integer.MAX_VALUE, Math.min(getStoredEU(), getCapacity()));
    }

    public void setStored(int aEU) {
        if (canAccessData()) setStoredEU(aEU);
    }

    public int getMaxSafeInput() {
        return (int) Math.min(Integer.MAX_VALUE, getInputVoltage());
    }

    public int getMaxEnergyOutput() {
        if (mReleaseEnergy) return Integer.MAX_VALUE;
        return getOutput();
    }

    public long getStoredEUuncapped() {
        return mMetaTileEntity.getEUVar();
    }

    public int getOutput() {
        return (int) Math.min(Integer.MAX_VALUE, oldOutput);
    }

    public int injectEnergy(Direction aDirection, int aAmount) {
        return injectEnergyUnits(aDirection.toForgeDirection(), aAmount, 1) > 0 ? 0 : aAmount;
    }

    public boolean isTeleporterCompatible(Direction ignoredDirection) {
        return canAccessData() && mMetaTileEntity.isTeleporterCompatible();
    }

    public boolean acceptsEnergyFrom(TileEntity ignoredTileEntity, Direction aDirection) {
        return inputEnergyFrom(aDirection.toForgeDirection());
    }

    public boolean emitsEnergyTo(TileEntity ignoredTileEntity, Direction aDirection) {
        return outputsEnergyTo(aDirection.toForgeDirection());
    }

    @Override
    public boolean addStackToSlot(int slotIndex, ItemStack stack) {
        if (GTUtility.isStackInvalid(stack)) return true;
        if (slotIndex < 0 || slotIndex >= getSizeInventory()) return false;
        final ItemStack toStack = getStackInSlot(slotIndex);
        if (GTUtility.isStackInvalid(toStack)) {
            setInventorySlotContents(slotIndex, stack);
            return true;
        }
        final ItemStack fromStack = GTOreDictUnificator.get(stack);
        if (GTUtility.areStacksEqual(toStack, fromStack) && toStack.stackSize + fromStack.stackSize
            <= Math.min(fromStack.getMaxStackSize(), getInventoryStackLimit())) {
            toStack.stackSize += fromStack.stackSize;
            markDirty();
            return true;
        }
        return false;
    }

    @Override
    public boolean addStackToSlot(int aIndex, ItemStack aStack, int aAmount) {
        return addStackToSlot(aIndex, GTUtility.copyAmount(aAmount, aStack));
    }

    @Override
    public byte getColorization() {
        return (byte) (mColor - 1);
    }

    @Override
    public byte setColorization(byte aColor) {
        if (aColor > 15 || aColor < -1) aColor = -1;
        mColor = (byte) (aColor + 1);
        if (canAccessData()) mMetaTileEntity.onColorChangeServer(aColor);
        return mColor;
    }

    @Override
    public boolean isUniversalEnergyStored(long aEnergyAmount) {
        if (getUniversalEnergyStored() >= aEnergyAmount) return true;
        mHasEnoughEnergy = false;
        return false;
    }

    @Override
    public int getLightOpacity() {
        return mMetaTileEntity == null ? getLightValue() > 0 ? 0 : 255 : mMetaTileEntity.getLightOpacity();
    }

    /**
     * Shifts the machine Inventory index according to the change in Input/Output Slots. This is NOT done automatically.
     * If you want to change slot count for a machine this method needs to be adapted. Currently this method only works
     * for MTEBasicMachine
     *
     * @param slotIndex  The original Inventory index
     * @param nbtVersion The GregTech version in which the original Inventory Index was saved.
     * @return The corrected Inventory index
     */
    @Override
    protected int migrateInventoryIndex(int slotIndex, int nbtVersion) {
        final int oldInputSize;
        final int newInputSize;
        final int oldOutputSize;
        final int newOutputSize;
        final int chemistryUpdateVersion = GTMod.calculateTotalGTVersion(509, 31);
        final int configCircuitAdditionVersion = GTMod.calculateTotalGTVersion(509, 40);
        final int wireAdditionVersion = GTMod.calculateTotalGTVersion(509, 41);
        final int disassemblerRemoveVersion = GTMod.calculateTotalGTVersion(509, 42, 44);
        if (nbtVersion < 1000000) nbtVersion *= 1000;
        // 4 is old MTEBasicMachine.OTHER_SLOT_COUNT
        if (nbtVersion < configCircuitAdditionVersion && getMetaTileEntity() instanceof MTEBasicMachine
            && slotIndex >= 4) slotIndex += 1;
        if (mID >= 211 && mID <= 218) { // Assembler
            if (nbtVersion < chemistryUpdateVersion) {
                oldInputSize = 2;
                oldOutputSize = 1;
            } else {
                return slotIndex;
            }
            newInputSize = 6;
            newOutputSize = 1;

        } else if (mID >= 421 && mID <= 428) { // Chemical Reactor
            if (nbtVersion < chemistryUpdateVersion) {
                oldInputSize = 2;
                oldOutputSize = 1;
            } else {
                return slotIndex;
            }
            newInputSize = 2;
            newOutputSize = 2;

        } else if (mID >= 531 && mID <= 538) { // Distillery
            if (nbtVersion < chemistryUpdateVersion) {
                oldInputSize = 1;
                oldOutputSize = 0;
            } else {
                return slotIndex;
            }
            newInputSize = 1;
            newOutputSize = 1;
        } else if (mID >= 581 && mID <= 588) { // Mixer
            if (nbtVersion < chemistryUpdateVersion) {
                oldInputSize = 4;
                oldOutputSize = 1;
            } else {
                return slotIndex;
            }
            newInputSize = 6;
            newOutputSize = 1;

        } else if (mID >= 351 && mID <= 355 || mID >= 11050 && mID <= 11056) { // wire mill
            if (nbtVersion < wireAdditionVersion) {
                oldInputSize = 1;
                oldOutputSize = 1;
            } else {
                return slotIndex;
            }
            newInputSize = 2;
            newOutputSize = 1;

        } else if (mID >= 654 && mID <= 655 || mID >= 11070 && mID <= 11076) { // arc furnace
            if (nbtVersion < disassemblerRemoveVersion) {
                oldInputSize = 1;
                oldOutputSize = 4;
            } else {
                return slotIndex;
            }
            newInputSize = 1;
            newOutputSize = 9;

        } else {
            return slotIndex;
        }

        int indexShift = 0;
        if (slotIndex >= MTEBasicMachine.OTHER_SLOT_COUNT + oldInputSize) {
            indexShift += newInputSize - oldInputSize;
        }
        if (slotIndex >= MTEBasicMachine.OTHER_SLOT_COUNT + oldInputSize + oldOutputSize) {
            indexShift += newOutputSize - oldOutputSize;
        }
        return slotIndex + indexShift;
    }

    @Override
    public IGridNode getGridNode(ForgeDirection forgeDirection) {
        final AENetworkProxy gp = getProxy();
        return gp != null ? gp.getNode() : null;
    }

    @Override
    public AECableType getCableConnectionType(ForgeDirection forgeDirection) {
        return mMetaTileEntity == null ? AECableType.NONE : mMetaTileEntity.getCableConnectionType(forgeDirection);
    }

    @Override
    public void securityBreak() {}

    @Override
    public IGridNode getActionableNode() {
        final AENetworkProxy gp = getProxy();
        return gp != null ? gp.getNode() : null;
    }

    @Override
    public AENetworkProxy getProxy() {
        return mMetaTileEntity == null ? null : mMetaTileEntity.getProxy();
    }

    @Override
    public DimensionalCoord getLocation() {
        return new DimensionalCoord(this);
    }

    @Override
    public void gridChanged() {
        if (mMetaTileEntity != null) mMetaTileEntity.gridChanged();
    }

    void onChunkUnloadAE() {
        final AENetworkProxy gp = getProxy();
        if (gp != null) gp.onChunkUnload();
    }

    void invalidateAE() {
        final AENetworkProxy gp = getProxy();
        if (gp != null) gp.invalidate();
    }

    @Override
    public boolean wasShutdown() {
        return mWasShutdown;
    }

    @Override
    public void setShutdownStatus(boolean newStatus) {
        mWasShutdown = newStatus;
    }

    @Override
    public void setShutDownReason(@NotNull ShutDownReason reason) {
        lastShutDownReason = reason;
    }

    @Override
    public @NotNull ShutDownReason getLastShutDownReason() {
        return lastShutDownReason;
    }

    @Override
    public IAlignment getAlignment() {
        return getMetaTileEntity() instanceof IAlignmentProvider
            ? ((IAlignmentProvider) getMetaTileEntity()).getAlignment()
            : getMetaTileEntity() instanceof IAlignment ? (IAlignment) getMetaTileEntity() : null;
    }

    @Nullable
    @Override
    public IConstructable getConstructable() {
        return getMetaTileEntity() instanceof IConstructable ? (IConstructable) getMetaTileEntity() : null;
    }

    @Nullable
    @Override
    public List<ItemStack> getItemsForHoloGlasses() {
        if (canAccessData()) {
            return mMetaTileEntity.getItemsForHoloGlasses();
        }
        return null;
    }

    @Override
    public String getCustomName() {
        return getMetaTileEntity() instanceof ICustomNameObject customNameObject ? customNameObject.getCustomName()
            : null;
    }

    @Override
    public boolean hasCustomName() {
        return getMetaTileEntity() instanceof ICustomNameObject customNameObject && customNameObject.hasCustomName();
    }

    @Override
    public void setCustomName(String name) {
        if (getMetaTileEntity() instanceof ICustomNameObject customNameObject) customNameObject.setCustomName(name);
    }

    @Override
    protected int getCoverTabHeightOffset() {
        return isSteampowered() || getMetaTileEntity() instanceof MTESteamMultiBase<?> ? 32 : 0;
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return getMetaTileEntity() instanceof IMTERenderer mteRenderer
            ? mteRenderer.getRenderBoundingBox(xCoord, yCoord, zCoord)
            : super.getRenderBoundingBox();
    }

    @Override
    public double getMaxRenderDistanceSquared() {
        return getMetaTileEntity() instanceof IMTERenderer mteRenderer ? mteRenderer.getMaxRenderDistanceSquared()
            : super.getMaxRenderDistanceSquared();
    }
}
