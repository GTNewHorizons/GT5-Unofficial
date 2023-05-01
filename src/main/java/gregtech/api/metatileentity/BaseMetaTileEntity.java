package gregtech.api.metatileentity;

import static gregtech.GT_Mod.GT_FML_LOGGER;
import static gregtech.api.enums.GT_Values.ALL_VALID_SIDES;
import static gregtech.api.enums.GT_Values.NW;
import static gregtech.api.enums.GT_Values.V;
import static gregtech.api.objects.XSTR.XSTR_INSTANCE;

import java.lang.reflect.Field;
import java.util.*;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFire;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

import com.gtnewhorizon.structurelib.alignment.IAlignment;
import com.gtnewhorizon.structurelib.alignment.IAlignmentProvider;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructableProvider;

import appeng.api.networking.IGridNode;
import appeng.api.networking.security.IActionHost;
import appeng.api.util.AECableType;
import appeng.api.util.DimensionalCoord;
import appeng.me.helpers.AENetworkProxy;
import appeng.me.helpers.IGridProxyable;
import appeng.tile.TileEvent;
import appeng.tile.events.TileEventType;
import cpw.mods.fml.relauncher.ReflectionHelper;
import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
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
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.net.GT_Packet_TileEntity;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.util.*;
import gregtech.common.GT_Pollution;
import gregtech.common.covers.CoverInfo;
import ic2.api.Direction;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * <p/>
 * This is the main TileEntity for EVERYTHING.
 */
public class BaseMetaTileEntity extends CommonMetaTileEntity implements IGregTechTileEntity, IActionHost,
    IGridProxyable, IAlignmentProvider, IConstructableProvider, IDebugableTileEntity, IGregtechWailaProvider {

    private static final Field ENTITY_ITEM_HEALTH_FIELD = ReflectionHelper
        .findField(EntityItem.class, "health", "field_70291_e");
    private final boolean[] mActiveEUInputs = new boolean[] { false, false, false, false, false, false };
    private final boolean[] mActiveEUOutputs = new boolean[] { false, false, false, false, false, false };
    private final int[] mTimeStatistics = new int[GregTech_API.TICKS_FOR_LAG_AVERAGING];
    public long mLastSoundTick = 0;
    public boolean mWasShutdown = false;
    protected MetaTileEntity mMetaTileEntity;
    protected long mStoredEnergy = 0, mStoredSteam = 0;
    protected int mAverageEUInputIndex = 0, mAverageEUOutputIndex = 0;
    protected boolean mReleaseEnergy = false;
    protected long[] mAverageEUInput = new long[] { 0, 0, 0, 0, 0 }, mAverageEUOutput = new long[] { 0, 0, 0, 0, 0 };
    private boolean mHasEnoughEnergy = true, mRunningThroughTick = false, mInputDisabled = false,
        mOutputDisabled = false, mMuffler = false, mLockUpgrade = false;
    private boolean mActive = false, mWorkUpdate = false, mSteamConverter = false, mWorks = true;
    private boolean oRedstone = false;
    private byte mColor = 0, oColor = 0, oStrongRedstone = 0, oRedstoneData = 63, oTextureData = 0, oUpdateData = 0,
        oTexturePage = 0;
    private byte oLightValueClient = 0, oLightValue = -1, mLightValue = 0, mOtherUpgrades = 0, mFacing = 0, oFacing = 0,
        mWorkData = 0;
    private int mDisplayErrorCode = 0, oX = 0, oY = 0, oZ = 0, mTimeStatisticsIndex = 0, mLagWarningCount = 0;
    private long oOutput = 0, mAcceptedAmperes = Long.MAX_VALUE;
    private long mLastCheckTick = 0;
    private String mOwnerName = "";
    private UUID mOwnerUuid = GT_Utility.defaultUuid;
    private NBTTagCompound mRecipeStuff = new NBTTagCompound();
    private int cableUpdateDelay = 30;

    public BaseMetaTileEntity() {}

    @Override
    public void writeToNBT(NBTTagCompound aNBT) {
        try {
            super.writeToNBT(aNBT);
        } catch (Throwable e) {
            GT_FML_LOGGER.error("Encountered CRITICAL ERROR while saving MetaTileEntity.", e);
        }
        try {
            aNBT.setInteger("mID", mID);
            aNBT.setLong("mStoredSteam", mStoredSteam);
            aNBT.setLong("mStoredEnergy", mStoredEnergy);
            writeCoverNBT(aNBT, false);
            aNBT.setByte("mColor", mColor);
            aNBT.setByte("mLightValue", mLightValue);
            aNBT.setByte("mOtherUpgrades", mOtherUpgrades);
            aNBT.setByte("mWorkData", mWorkData);
            aNBT.setShort("mFacing", mFacing);
            aNBT.setString("mOwnerName", mOwnerName);
            aNBT.setString("mOwnerUuid", mOwnerUuid == null ? "" : mOwnerUuid.toString());
            aNBT.setBoolean("mLockUpgrade", mLockUpgrade);
            aNBT.setBoolean("mMuffler", mMuffler);
            aNBT.setBoolean("mSteamConverter", mSteamConverter);
            aNBT.setBoolean("mActive", mActive);
            aNBT.setBoolean("mWorks", !mWorks);
            aNBT.setBoolean("mInputDisabled", mInputDisabled);
            aNBT.setBoolean("mOutputDisabled", mOutputDisabled);
            aNBT.setTag("GT.CraftingComponents", mRecipeStuff);
        } catch (Throwable e) {
            GT_FML_LOGGER.error("Encountered CRITICAL ERROR while saving MetaTileEntity.", e);
        }
        saveMetaTileNBT(aNBT);
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
            mSidedRedstone = (hasValidMetaTileEntity() && mMetaTileEntity.hasSidedRedstoneOutputBehavior()
                ? new byte[] { 0, 0, 0, 0, 0, 0 }
                : new byte[] { 15, 15, 15, 15, 15, 15 });
        } else {
            if (aID <= 0) mID = (short) aNBT.getInteger("mID");
            else mID = aID;
            mStoredSteam = aNBT.getLong("mStoredSteam");
            mStoredEnergy = aNBT.getLong("mStoredEnergy");
            mColor = aNBT.getByte("mColor");
            mLightValue = aNBT.getByte("mLightValue");
            mWorkData = aNBT.getByte("mWorkData");
            mFacing = oFacing = (byte) aNBT.getShort("mFacing");
            mOwnerName = aNBT.getString("mOwnerName");
            try {
                mOwnerUuid = UUID.fromString(aNBT.getString("mOwnerUuid"));
            } catch (IllegalArgumentException e) {
                mOwnerUuid = null;
            }
            mLockUpgrade = aNBT.getBoolean("mLockUpgrade");
            mMuffler = aNBT.getBoolean("mMuffler");
            mSteamConverter = aNBT.getBoolean("mSteamConverter");
            mActive = aNBT.getBoolean("mActive");
            mWorks = !aNBT.getBoolean("mWorks");
            mInputDisabled = aNBT.getBoolean("mInputDisabled");
            mOutputDisabled = aNBT.getBoolean("mOutputDisabled");
            mOtherUpgrades = (byte) (aNBT.getByte("mOtherUpgrades") + aNBT.getByte("mBatteries")
                + aNBT.getByte("mLiBatteries"));

            mRecipeStuff = aNBT.getCompoundTag("GT.CraftingComponents");
            final int nbtVersion = aNBT.getInteger("nbtVersion");
            readCoverNBT(aNBT);
            loadMetaTileNBT(aNBT);
        }

        if (mSidedRedstone.length != 6)
            if (hasValidMetaTileEntity() && mMetaTileEntity.hasSidedRedstoneOutputBehavior())
                mSidedRedstone = new byte[] { 0, 0, 0, 0, 0, 0 };
            else mSidedRedstone = new byte[] { 15, 15, 15, 15, 15, 15 };

        updateCoverBehavior();
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
            GT_ModHandler.chargeElectricItem(
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
            GT_ModHandler.dischargeElectricItem(
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
        return (getCoverIDAtSide((byte) 1) == 0 && worldObj.getPrecipitationHeight(xCoord, zCoord) - 2 < yCoord)
            || (getCoverIDAtSide((byte) 2) == 0 && precipitationHeightAtSide2 - 1 < yCoord
                && precipitationHeightAtSide2 > -1)
            || (getCoverIDAtSide((byte) 3) == 0 && precipitationHeightAtSide3 - 1 < yCoord
                && precipitationHeightAtSide3 > -1)
            || (getCoverIDAtSide((byte) 4) == 0 && precipitationHeightAtSide4 - 1 < yCoord
                && precipitationHeightAtSide4 > -1)
            || (getCoverIDAtSide((byte) 5) == 0 && precipitationHeightAtSide5 - 1 < yCoord
                && precipitationHeightAtSide5 > -1);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();

        if (!hasValidMetaTileEntity()) {
            if (mMetaTileEntity == null) return;
            mMetaTileEntity.setBaseMetaTileEntity(this);
        }

        mRunningThroughTick = true;
        long tTime = System.nanoTime();
        final boolean aSideServer = isServerSide();
        final boolean aSideClient = isClientSide();

        try {
            if (hasValidMetaTileEntity()) {
                if (mTickTimer++ == 0) {
                    oX = xCoord;
                    oY = yCoord;
                    oZ = zCoord;
                    if (aSideServer) {
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
                if (aSideClient) {
                    if (mColor != oColor) {
                        mMetaTileEntity.onColorChangeClient(oColor = mColor);
                        issueTextureUpdate();
                    }

                    if (mLightValue != oLightValueClient) {
                        worldObj.setLightValue(EnumSkyBlock.Block, xCoord, yCoord, zCoord, mLightValue);
                        worldObj.updateLightByType(EnumSkyBlock.Block, xCoord, yCoord, zCoord);
                        worldObj.updateLightByType(EnumSkyBlock.Block, xCoord + 1, yCoord, zCoord);
                        worldObj.updateLightByType(EnumSkyBlock.Block, xCoord - 1, yCoord, zCoord);
                        worldObj.updateLightByType(EnumSkyBlock.Block, xCoord, yCoord + 1, zCoord);
                        worldObj.updateLightByType(EnumSkyBlock.Block, xCoord, yCoord - 1, zCoord);
                        worldObj.updateLightByType(EnumSkyBlock.Block, xCoord, yCoord, zCoord + 1);
                        worldObj.updateLightByType(EnumSkyBlock.Block, xCoord, yCoord, zCoord - 1);
                        oLightValueClient = mLightValue;
                        issueTextureUpdate();
                    }

                    if (mNeedsUpdate) {
                        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                        // worldObj.func_147479_m(xCoord, yCoord, zCoord);
                        mNeedsUpdate = false;
                    }
                }
                if (aSideServer && mTickTimer > 10) {
                    if (!doCoverThings()) {
                        mRunningThroughTick = false;
                        return;
                    }
                }
                if (aSideServer) {
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
                if (aSideServer) {
                    if (mRedstone != oRedstone || mTickTimer == 10) {
                        updateCoverBehavior();
                        oRedstone = mRedstone;
                        issueBlockUpdate();
                    }
                    if (mTickTimer == 10) joinEnet();

                    if (xCoord != oX || yCoord != oY || zCoord != oZ) {
                        oX = xCoord;
                        oY = yCoord;
                        oZ = zCoord;
                        issueClientUpdate();
                        clearTileEntityBuffer();
                    }

                    if (mFacing != oFacing) {
                        oFacing = mFacing;
                        checkDropCover();
                        issueBlockUpdate();
                    }

                    if (mTickTimer > 20 && mMetaTileEntity.isElectric()) {
                        mAcceptedAmperes = 0;

                        if (getOutputVoltage() != oOutput) {
                            oOutput = getOutputVoltage();
                        }

                        if (mMetaTileEntity.isEnetOutput() || mMetaTileEntity.isEnetInput()) {
                            for (byte i : ALL_VALID_SIDES) {
                                boolean temp = isEnergyInputSide(i);
                                if (temp != mActiveEUInputs[i]) {
                                    mActiveEUInputs[i] = temp;
                                }
                                temp = isEnergyOutputSide(i);
                                if (temp != mActiveEUOutputs[i]) {
                                    mActiveEUOutputs[i] = temp;
                                }
                            }
                        }

                        if (mMetaTileEntity.isEnetOutput() && oOutput > 0) {
                            final long tOutputVoltage = Math
                                .max(oOutput, oOutput + (1L << Math.max(0, GT_Utility.getTier(oOutput) - 1)));
                            final long tUsableAmperage = Math.min(
                                getOutputAmperage(),
                                (getStoredEU() - mMetaTileEntity.getMinimumStoredEU()) / tOutputVoltage);
                            if (tUsableAmperage > 0) {
                                final long tEU = tOutputVoltage
                                    * Util.emitEnergyToNetwork(oOutput, tUsableAmperage, this);
                                mAverageEUOutput[mAverageEUOutputIndex] += tEU;
                                decreaseStoredEU(tEU, true);
                            }
                        }
                        if (getEUCapacity() > 0) {
                            if (GregTech_API.sMachineFireExplosions && getRandomNumber(1000) == 0) {
                                final Block tBlock = getBlockAtSide((byte) getRandomNumber(6));
                                if (tBlock instanceof BlockFire) doEnergyExplosion();
                            }

                            if (!hasValidMetaTileEntity()) {
                                mRunningThroughTick = false;
                                return;
                            }

                            if (GregTech_API.sMachineRainExplosions) {
                                if (mMetaTileEntity.willExplodeInRain()) {
                                    if (getRandomNumber(1000) == 0 && isRainPossible()) {
                                        if (isRainExposed()) {
                                            if (worldObj.isRaining()) {
                                                if (getRandomNumber(10) == 0) {
                                                    try {
                                                        GT_Mod.achievements.issueAchievement(
                                                            this.getWorldObj()
                                                                .getPlayerEntityByName(mOwnerName),
                                                            "badweather");
                                                    } catch (Exception ignored) {}
                                                    GT_Log.exp.println(
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
                                                    GT_Log.exp.println(
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
                                            if (GregTech_API.sMachineThunderExplosions && worldObj.isThundering()
                                                && getRandomNumber(3) == 0) {
                                                try {
                                                    GT_Mod.achievements.issueAchievement(
                                                        this.getWorldObj()
                                                            .getPlayerEntityByName(mOwnerName),
                                                        "badweather");
                                                } catch (Exception ignored) {}
                                                GT_Log.exp.println(
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
                    }

                    if (!hasValidMetaTileEntity()) {
                        mRunningThroughTick = false;
                        return;
                    }
                }
                if (aSideServer) {
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
                if (aSideServer) {
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
                if (aSideServer) {
                    if (mTickTimer > 20 && cableUpdateDelay == 0) {
                        generatePowerNodes();
                    }
                    cableUpdateDelay--;
                    if (mTickTimer % 10 == 0) {
                        sendClientData();
                    }

                    if (mTickTimer > 10) {
                        byte tData = (byte) ((mFacing & 7) | (mActive ? 8 : 0)
                            | (mRedstone ? 16 : 0)
                            | (mLockUpgrade ? 32 : 0)
                            | (mWorks ? 64 : 0)
                            | (mMuffler ? 128 : 0));
                        if (tData != oTextureData)
                            sendBlockEvent(GregTechTileClientEvents.CHANGE_COMMON_DATA, oTextureData = tData);

                        tData = mMetaTileEntity.getUpdateData();
                        if (tData != oUpdateData)
                            sendBlockEvent(GregTechTileClientEvents.CHANGE_CUSTOM_DATA, oUpdateData = tData);
                        if (mMetaTileEntity instanceof GT_MetaTileEntity_Hatch) {
                            tData = ((GT_MetaTileEntity_Hatch) mMetaTileEntity).getTexturePage();
                            if (tData != oTexturePage) sendBlockEvent(
                                GregTechTileClientEvents.CHANGE_CUSTOM_DATA,
                                (byte) ((oTexturePage = tData) | 0x80)); // set last bit as a flag for page
                        }
                        if (mColor != oColor) sendBlockEvent(GregTechTileClientEvents.CHANGE_COLOR, oColor = mColor);
                        tData = (byte) (((mSidedRedstone[0] > 0) ? 1 : 0) | ((mSidedRedstone[1] > 0) ? 2 : 0)
                            | ((mSidedRedstone[2] > 0) ? 4 : 0)
                            | ((mSidedRedstone[3] > 0) ? 8 : 0)
                            | ((mSidedRedstone[4] > 0) ? 16 : 0)
                            | ((mSidedRedstone[5] > 0) ? 32 : 0));
                        if (tData != oRedstoneData)
                            sendBlockEvent(GregTechTileClientEvents.CHANGE_REDSTONE_OUTPUT, oRedstoneData = tData);
                        if (mLightValue != oLightValue) {
                            worldObj.setLightValue(EnumSkyBlock.Block, xCoord, yCoord, zCoord, mLightValue);
                            worldObj.updateLightByType(EnumSkyBlock.Block, xCoord, yCoord, zCoord);
                            worldObj.updateLightByType(EnumSkyBlock.Block, xCoord + 1, yCoord, zCoord);
                            worldObj.updateLightByType(EnumSkyBlock.Block, xCoord - 1, yCoord, zCoord);
                            worldObj.updateLightByType(EnumSkyBlock.Block, xCoord, yCoord + 1, zCoord);
                            worldObj.updateLightByType(EnumSkyBlock.Block, xCoord, yCoord - 1, zCoord);
                            worldObj.updateLightByType(EnumSkyBlock.Block, xCoord, yCoord, zCoord + 1);
                            worldObj.updateLightByType(EnumSkyBlock.Block, xCoord, yCoord, zCoord - 1);
                            issueTextureUpdate();
                            sendBlockEvent(GregTechTileClientEvents.CHANGE_LIGHT, oLightValue = mLightValue);
                        }
                    }

                    if (mNeedsBlockUpdate) {
                        updateNeighbours(mStrongRedstone, oStrongRedstone);
                        oStrongRedstone = mStrongRedstone;
                        mNeedsBlockUpdate = false;
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
            e.printStackTrace(GT_Log.err);
        }

        if (aSideServer && hasValidMetaTileEntity()) {
            tTime = System.nanoTime() - tTime;
            if (mTimeStatistics.length > 0) mTimeStatistics[mTimeStatisticsIndex = (mTimeStatisticsIndex + 1)
                % mTimeStatistics.length] = (int) tTime;
            if (tTime > 0 && tTime > (GregTech_API.MILLISECOND_THRESHOLD_UNTIL_LAG_WARNING * 1_000_000L)
                && mTickTimer > 1000
                && getMetaTileEntity().doTickProfilingMessageDuringThisTick()
                && mLagWarningCount++ < 10)
                GT_FML_LOGGER.warn(
                    "WARNING: Possible Lag Source at [" + xCoord
                        + ", "
                        + yCoord
                        + ", "
                        + zCoord
                        + "] in Dimension "
                        + worldObj.provider.dimensionId
                        + " with "
                        + tTime
                        + "ns caused by an instance of "
                        + getMetaTileEntity().getClass());
        }

        mWorkUpdate = mInventoryChanged = mRunningThroughTick = false;
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        if (hasValidMetaTileEntity()) {
            getMetaTileEntity().getWailaBody(itemStack, currenttip, accessor, config);
        }
        super.getWailaBody(itemStack, currenttip, accessor, config);
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
            NW.sendPacketToAllPlayersInRange(
                worldObj,
                new GT_Packet_TileEntity(
                    xCoord,
                    (short) yCoord,
                    zCoord,
                    mID,
                    getCoverInfoAtSide((byte) 0).getCoverID(),
                    getCoverInfoAtSide((byte) 1).getCoverID(),
                    getCoverInfoAtSide((byte) 2).getCoverID(),
                    getCoverInfoAtSide((byte) 3).getCoverID(),
                    getCoverInfoAtSide((byte) 4).getCoverID(),
                    getCoverInfoAtSide((byte) 5).getCoverID(),
                    oTextureData = (byte) ((mFacing & 7) | (mActive ? 8 : 0)
                        | (mRedstone ? 16 : 0)
                        | (mLockUpgrade ? 32 : 0)
                        | (mWorks ? 64 : 0)),
                    oTexturePage = (hasValidMetaTileEntity() && mMetaTileEntity instanceof GT_MetaTileEntity_Hatch)
                        ? ((GT_MetaTileEntity_Hatch) mMetaTileEntity).getTexturePage()
                        : 0,
                    oUpdateData = hasValidMetaTileEntity() ? mMetaTileEntity.getUpdateData() : 0,
                    oRedstoneData = (byte) (((mSidedRedstone[0] > 0) ? 1 : 0) | ((mSidedRedstone[1] > 0) ? 2 : 0)
                        | ((mSidedRedstone[2] > 0) ? 4 : 0)
                        | ((mSidedRedstone[3] > 0) ? 8 : 0)
                        | ((mSidedRedstone[4] > 0) ? 16 : 0)
                        | ((mSidedRedstone[5] > 0) ? 32 : 0)),
                    oColor = mColor),
                xCoord,
                zCoord);
            mSendClientData = false;
        }
        sendCoverDataIfNeeded();
    }

    public final void receiveMetaTileEntityData(short aID, int aCover0, int aCover1, int aCover2, int aCover3,
        int aCover4, int aCover5, byte aTextureData, byte aTexturePage, byte aUpdateData, byte aRedstoneData,
        byte aColorData) {
        issueTextureUpdate();
        if (mID != aID && aID > 0) {
            mID = aID;
            createNewMetatileEntity(mID);
        }

        setCoverIDAtSide((byte) 0, aCover0);
        setCoverIDAtSide((byte) 1, aCover1);
        setCoverIDAtSide((byte) 2, aCover2);
        setCoverIDAtSide((byte) 3, aCover3);
        setCoverIDAtSide((byte) 4, aCover4);
        setCoverIDAtSide((byte) 5, aCover5);

        receiveClientEvent(GregTechTileClientEvents.CHANGE_COMMON_DATA, aTextureData);
        receiveClientEvent(GregTechTileClientEvents.CHANGE_CUSTOM_DATA, aUpdateData & 0x7F);
        receiveClientEvent(GregTechTileClientEvents.CHANGE_CUSTOM_DATA, aTexturePage | 0x80);
        receiveClientEvent(GregTechTileClientEvents.CHANGE_COLOR, aColorData);
        receiveClientEvent(GregTechTileClientEvents.CHANGE_REDSTONE_OUTPUT, aRedstoneData);
    }

    @Deprecated
    public final void receiveMetaTileEntityData(short aID, int aCover0, int aCover1, int aCover2, int aCover3,
        int aCover4, int aCover5, byte aTextureData, byte aUpdateData, byte aRedstoneData, byte aColorData) {
        receiveMetaTileEntityData(
            aID,
            aCover0,
            aCover1,
            aCover2,
            aCover3,
            aCover4,
            aCover5,
            aTextureData,
            (byte) 0,
            aUpdateData,
            aRedstoneData,
            aColorData);
    }

    @Override
    public boolean receiveClientEvent(int aEventID, int aValue) {
        super.receiveClientEvent(aEventID, aValue);

        if (hasValidMetaTileEntity()) {
            try {
                mMetaTileEntity.receiveClientEvent((byte) aEventID, (byte) aValue);
            } catch (Throwable e) {
                GT_Log.err.println(
                    "Encountered Exception while receiving Data from the Server, the Client should've been crashed by now, but I prevented that. Please report immediately to GregTech Intergalactical!!!");
                e.printStackTrace(GT_Log.err);
            }
        }

        if (isClientSide()) {
            issueTextureUpdate();
            switch (aEventID) {
                case GregTechTileClientEvents.CHANGE_COMMON_DATA -> {
                    mFacing = (byte) (aValue & 7);
                    mActive = ((aValue & 8) != 0);
                    mRedstone = ((aValue & 16) != 0);
                    // mLockUpgrade = ((aValue&32) != 0);
                    mWorks = ((aValue & 64) != 0);
                    mMuffler = ((aValue & 128) != 0);
                }
                case GregTechTileClientEvents.CHANGE_CUSTOM_DATA -> {
                    if (hasValidMetaTileEntity()) {
                        if ((aValue & 0x80) == 0) // Is texture index
                            mMetaTileEntity.onValueUpdate((byte) (aValue & 0x7F));
                        else if (mMetaTileEntity instanceof GT_MetaTileEntity_Hatch) // is texture page and hatch
                            ((GT_MetaTileEntity_Hatch) mMetaTileEntity).onTexturePageUpdate((byte) (aValue & 0x7F));
                    }
                }
                case GregTechTileClientEvents.CHANGE_COLOR -> {
                    if (aValue > 16 || aValue < 0) aValue = 0;
                    mColor = (byte) aValue;
                }
                case GregTechTileClientEvents.CHANGE_REDSTONE_OUTPUT -> {
                    mSidedRedstone[0] = (byte) ((aValue & 1) == 1 ? 15 : 0);
                    mSidedRedstone[1] = (byte) ((aValue & 2) == 2 ? 15 : 0);
                    mSidedRedstone[2] = (byte) ((aValue & 4) == 4 ? 15 : 0);
                    mSidedRedstone[3] = (byte) ((aValue & 8) == 8 ? 15 : 0);
                    mSidedRedstone[4] = (byte) ((aValue & 16) == 16 ? 15 : 0);
                    mSidedRedstone[5] = (byte) ((aValue & 32) == 32 ? 15 : 0);
                }
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
        if (aLogLevel > 1) {
            if (mTimeStatistics.length > 0) {
                double tAverageTime = 0;
                double tWorstTime = 0;
                for (int tTime : mTimeStatistics) {
                    tAverageTime += tTime;
                    if (tTime > tWorstTime) {
                        tWorstTime = tTime;
                    }
                    // Uncomment this line to print out tick-by-tick times.
                    // tList.add("tTime " + tTime);
                }
                tList.add(
                    "Average CPU load of ~" + GT_Utility.formatNumbers(tAverageTime / mTimeStatistics.length)
                        + "ns over "
                        + GT_Utility.formatNumbers(mTimeStatistics.length)
                        + " ticks with worst time of "
                        + GT_Utility.formatNumbers(tWorstTime)
                        + "ns.");
                tList.add(
                    "Recorded " + GT_Utility.formatNumbers(mMetaTileEntity.mSoundRequests)
                        + " sound requests in "
                        + GT_Utility.formatNumbers(mTickTimer - mLastCheckTick)
                        + " ticks.");
                mLastCheckTick = mTickTimer;
                mMetaTileEntity.mSoundRequests = 0;
            }
            if (mLagWarningCount > 0) {
                tList.add(
                    "Caused " + (mLagWarningCount >= 10 ? "more than 10" : mLagWarningCount)
                        + " Lag Spike Warnings (anything taking longer than "
                        + GregTech_API.MILLISECOND_THRESHOLD_UNTIL_LAG_WARNING
                        + "ms) on the Server.");
            }
            tList.add(
                "Is" + (mMetaTileEntity.isAccessAllowed(aPlayer) ? " "
                    : EnumChatFormatting.RED + " not " + EnumChatFormatting.RESET) + "accessible for you");
        }
        if (aLogLevel > 0) {
            if (getSteamCapacity() > 0 && hasSteamEngineUpgrade()) tList.add(
                GT_Utility.formatNumbers(getStoredSteam()) + " of "
                    + GT_Utility.formatNumbers(getSteamCapacity())
                    + " Steam");
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
        if (canAccessData()) return mMetaTileEntity.isGivingInformation();
        return false;
    }

    @Override
    public byte getBackFacing() {
        return GT_Utility.getOppositeSide(mFacing);
    }

    @Override
    public byte getFrontFacing() {
        return mFacing;
    }

    @Override
    public void setFrontFacing(byte aFacing) {
        if (isValidFacing(aFacing)) {
            mFacing = aFacing;
            mMetaTileEntity.onFacingChange();

            doEnetUpdate();
            cableUpdateDelay = 10;

            if (mMetaTileEntity.shouldTriggerBlockUpdate()) {
                // If we're triggering a block update this will call onMachineBlockUpdate()
                GregTech_API.causeMachineUpdate(worldObj, xCoord, yCoord, zCoord);
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
                worldObj.isRemote ? aStack : GT_OreDictUnificator.setStack(true, aStack));
        }
    }

    @Override
    public String getInventoryName() {
        if (canAccessData()) return mMetaTileEntity.getInventoryName();
        if (GregTech_API.METATILEENTITIES[mID] != null) return GregTech_API.METATILEENTITIES[mID].getInventoryName();
        return "";
    }

    @Override
    public int getInventoryStackLimit() {
        if (canAccessData()) return mMetaTileEntity.getInventoryStackLimit();
        return 64;
    }

    @Override
    public void openInventory() {
        if (canAccessData()) mMetaTileEntity.onOpenGUI();
    }

    @Override
    public void closeInventory() {
        if (canAccessData()) mMetaTileEntity.onCloseGUI();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer aPlayer) {
        return canAccessData() && playerOwnsThis(aPlayer, false)
            && mTickTimer > 40
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
            if (GregTech_API.mAE2) invalidateAE();
            mMetaTileEntity.onRemoval();
            mMetaTileEntity.setBaseMetaTileEntity(null);
        }
        super.invalidate();
    }

    @Override
    public void onChunkUnload() {
        super.onChunkUnload();
        if (GregTech_API.mAE2) onChunkUnloadAE();
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
        mWasShutdown = false;
    }

    @Override
    public void disableWorking() {
        mWorks = false;
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
    public byte getWorkDataValue() {
        return mWorkData;
    }

    @Override
    public void setWorkDataValue(byte aValue) {
        mWorkData = aValue;
    }

    @Override
    public int getMetaTileID() {
        return mID;
    }

    @Override
    public int setMetaTileID(short aID) {
        return mID = aID;
    }

    @Override
    public boolean isActive() {
        return mActive;
    }

    @Override
    public void setActive(boolean aActive) {
        mActive = aActive;
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
    public boolean inputEnergyFrom(byte aSide) {
        return inputEnergyFrom(aSide, true);
    }

    @Override
    public boolean inputEnergyFrom(byte aSide, boolean waitForActive) {
        if (aSide == 6) return true;
        if (isServerSide() && waitForActive)
            return ((aSide >= 0 && aSide < 6) && mActiveEUInputs[aSide]) && !mReleaseEnergy;
        return isEnergyInputSide(aSide);
    }

    @Override
    public boolean outputsEnergyTo(byte aSide) {
        return outputsEnergyTo(aSide, true);
    }

    @Override
    public boolean outputsEnergyTo(byte aSide, boolean waitForActive) {
        if (aSide == 6) return true;
        if (isServerSide() && waitForActive)
            return ((aSide >= 0 && aSide < 6) && mActiveEUOutputs[aSide]) || mReleaseEnergy;
        return isEnergyOutputSide(aSide);
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
            for (byte i : ALL_VALID_SIDES) {
                if (outputsEnergyTo(i, false) || inputEnergyFrom(i, false)) {
                    final IGregTechTileEntity TE = getIGregTechTileEntityAtSide(i);
                    if (TE instanceof BaseMetaPipeEntity) {
                        final Node node = ((BaseMetaPipeEntity) TE).getNode();
                        if (node == null) {
                            new GenerateNodeMapPower((BaseMetaPipeEntity) TE);
                        } else if (node.mCreationTime != time) {
                            GenerateNodeMap.clearNodeMap(node, -1);
                            new GenerateNodeMapPower((BaseMetaPipeEntity) TE);
                        }
                    }
                }
            }
        }
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
    public ITexture[] getTexture(Block aBlock, byte aSide) {
        final ITexture coverTexture = getCoverTexture(aSide);
        final ITexture[] textureUncovered = hasValidMetaTileEntity()
            ? mMetaTileEntity
                .getTexture(this, aSide, mFacing, (byte) (mColor - 1), mActive, getOutputRedstoneSignal(aSide) > 0)
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

    private boolean isEnergyInputSide(byte aSide) {
        if (aSide >= 0 && aSide < 6) {
            if (!getCoverInfoAtSide(aSide).letsEnergyIn()) return false;
            if (isInvalid() || mReleaseEnergy) return false;
            if (canAccessData() && mMetaTileEntity.isElectric() && mMetaTileEntity.isEnetInput())
                return mMetaTileEntity.isInputFacing(aSide);
        }
        return false;
    }

    private boolean isEnergyOutputSide(byte aSide) {
        if (aSide >= 0 && aSide < 6) {
            if (!getCoverInfoAtSide(aSide).letsEnergyOut()) return false;
            if (isInvalid() || mReleaseEnergy) return mReleaseEnergy;
            if (canAccessData() && mMetaTileEntity.isElectric() && mMetaTileEntity.isEnetOutput())
                return mMetaTileEntity.isOutputFacing(aSide);
        }
        return false;
    }

    @Override
    protected boolean hasValidMetaTileEntity() {
        return mMetaTileEntity != null && mMetaTileEntity.getBaseMetaTileEntity() == this;
    }

    @Override
    protected boolean canAccessData() {
        return !isDead && hasValidMetaTileEntity();
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
        if (mMetaTileEntity.getEUVar() - aEnergy >= 0 || aIgnoreTooLessEnergy) {
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
        if (mMetaTileEntity.getSteamVar() - aEnergy >= 0 || aIgnoreTooLessEnergy) {
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
        if (aCheckPrecicely || privateAccess() || (mOwnerName.length() == 0))
            if ((mOwnerName.length() == 0) && isServerSide()) {
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
            GT_Log.exp.println(
                "Energy Explosion, injected " + getUniversalEnergyStored()
                    + "EU >= "
                    + getUniversalEnergyCapacity() / 5D
                    + "Capacity of the Machine!");

            doExplosion(
                oOutput * (getUniversalEnergyStored() >= getUniversalEnergyCapacity() ? 4
                    : getUniversalEnergyStored() >= getUniversalEnergyCapacity() / 2 ? 2 : 1));
            GT_Mod.achievements.issueAchievement(
                this.getWorldObj()
                    .getPlayerEntityByName(mOwnerName),
                "electricproblems");
        }
    }

    @Override
    public void doExplosion(long aAmount) {
        if (canAccessData()) {
            // This is only for Electric Machines
            if (GregTech_API.sMachineWireFire && mMetaTileEntity.isElectric()) {
                try {
                    mReleaseEnergy = true;
                    IEnergyConnected.Util.emitEnergyToNetwork(V[5], Math.max(1, getStoredEU() / V[5]), this);
                } catch (Exception ignored) {}
            }
            mReleaseEnergy = false;
            // Normal Explosion Code
            mMetaTileEntity.onExplosion();
            if (GT_Mod.gregtechproxy.mExplosionItemDrop) {
                for (int i = 0; i < this.getSizeInventory(); i++) {
                    final ItemStack tItem = this.getStackInSlot(i);
                    if ((tItem != null) && (tItem.stackSize > 0) && (this.isValidSlot(i))) {
                        dropItems(tItem);
                        this.setInventorySlotContents(i, null);
                    }
                }
            }
            if (mRecipeStuff != null) {
                for (int i = 0; i < 9; i++) {
                    if (this.getRandomNumber(100) < 50) {
                        dropItems(GT_Utility.loadItem(mRecipeStuff, "Ingredient." + i));
                    }
                }
            }

            GT_Pollution.addPollution(this, GT_Mod.gregtechproxy.mPollutionOnExplosion);
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
        try {
            if (ENTITY_ITEM_HEALTH_FIELD != null) ENTITY_ITEM_HEALTH_FIELD.setInt(tItemEntity, 99999999);
        } catch (Exception ignored) {}
        this.worldObj.spawnEntityInWorld(tItemEntity);
        tItem.stackSize = 0;
    }

    @Override
    public ArrayList<ItemStack> getDrops() {
        final ItemStack rStack = new ItemStack(GregTech_API.sBlockMachines, 1, mID);
        final NBTTagCompound tNBT = new NBTTagCompound();
        if (mRecipeStuff != null && !mRecipeStuff.hasNoTags()) tNBT.setTag("GT.CraftingComponents", mRecipeStuff);
        if (mMuffler) tNBT.setBoolean("mMuffler", mMuffler);
        if (mLockUpgrade) tNBT.setBoolean("mLockUpgrade", mLockUpgrade);
        if (mSteamConverter) tNBT.setBoolean("mSteamConverter", mSteamConverter);
        if (mColor > 0) tNBT.setByte("mColor", mColor);
        if (mOtherUpgrades > 0) tNBT.setByte("mOtherUpgrades", mOtherUpgrades);

        writeCoverNBT(tNBT, true);

        if (hasValidMetaTileEntity()) mMetaTileEntity.setItemNBT(tNBT);
        if (!tNBT.hasNoTags()) rStack.setTagCompound(tNBT);

        onBaseTEDestroyed();
        return new ArrayList<>(Collections.singletonList(rStack));
    }

    @Override
    public boolean shouldDropItemAt(int index) {
        return this.mMetaTileEntity != null ? this.mMetaTileEntity.shouldDropItemAt(index) : true;
    }

    public int getUpgradeCount() {
        return (mMuffler ? 1 : 0) + (mLockUpgrade ? 1 : 0) + (mSteamConverter ? 1 : 0) + mOtherUpgrades;
    }

    @Override
    public boolean onRightclick(EntityPlayer aPlayer, byte aSide, float aX, float aY, float aZ) {
        if (isClientSide()) {
            // Configure Cover, sneak can also be: screwdriver, wrench, side cutter, soldering iron
            if (aPlayer.isSneaking()) {
                final byte tSide = (getCoverIDAtSide(aSide) == 0) ? GT_Utility.determineWrenchingSide(aSide, aX, aY, aZ)
                    : aSide;
                return (getCoverBehaviorAtSideNew(tSide).hasCoverGUI());
            } else if (getCoverBehaviorAtSideNew(aSide).onCoverRightclickClient(aSide, this, aPlayer, aX, aY, aZ)) {
                return true;
            }

            if (!getCoverInfoAtSide(aSide).isGUIClickable()) return false;
        }

        if (isServerSide()) {
            if (!privateAccess() || aPlayer.getDisplayName()
                .equalsIgnoreCase(getOwnerName())) {
                final ItemStack tCurrentItem = aPlayer.inventory.getCurrentItem();
                if (tCurrentItem != null) {
                    if (getColorization() >= 0
                        && GT_Utility.areStacksEqual(new ItemStack(Items.water_bucket, 1), tCurrentItem)) {
                        tCurrentItem.func_150996_a(Items.bucket);
                        setColorization((byte) (getColorization() >= 16 ? -2 : -1));
                        return true;
                    }
                    if (GT_Utility.isStackInList(tCurrentItem, GregTech_API.sWrenchList)) {
                        if (aPlayer.isSneaking() && mMetaTileEntity instanceof GT_MetaTileEntity_BasicMachine
                            && ((GT_MetaTileEntity_BasicMachine) mMetaTileEntity)
                                .setMainFacing(GT_Utility.determineWrenchingSide(aSide, aX, aY, aZ))) {
                            GT_ModHandler.damageOrDechargeItem(tCurrentItem, 1, 1000, aPlayer);
                            GT_Utility.sendSoundToPlayers(
                                worldObj,
                                SoundResource.IC2_TOOLS_WRENCH,
                                1.0F,
                                -1,
                                xCoord,
                                yCoord,
                                zCoord);
                            cableUpdateDelay = 10;
                        } else if (mMetaTileEntity.onWrenchRightClick(
                            aSide,
                            GT_Utility.determineWrenchingSide(aSide, aX, aY, aZ),
                            aPlayer,
                            aX,
                            aY,
                            aZ)) {
                                GT_ModHandler.damageOrDechargeItem(tCurrentItem, 1, 1000, aPlayer);
                                GT_Utility.sendSoundToPlayers(
                                    worldObj,
                                    SoundResource.IC2_TOOLS_WRENCH,
                                    1.0F,
                                    -1,
                                    xCoord,
                                    yCoord,
                                    zCoord);
                                cableUpdateDelay = 10;
                            }
                        return true;
                    }

                    if (GT_Utility.isStackInList(tCurrentItem, GregTech_API.sScrewdriverList)) {
                        if (GT_ModHandler.damageOrDechargeItem(tCurrentItem, 1, 200, aPlayer)) {
                            setCoverDataAtSide(
                                aSide,
                                getCoverBehaviorAtSideNew(aSide).onCoverScrewdriverClick(
                                    aSide,
                                    getCoverIDAtSide(aSide),
                                    getComplexCoverDataAtSide(aSide),
                                    this,
                                    aPlayer,
                                    aX,
                                    aY,
                                    aZ));
                            mMetaTileEntity.onScrewdriverRightClick(aSide, aPlayer, aX, aY, aZ);
                            GT_Utility.sendSoundToPlayers(
                                worldObj,
                                SoundResource.IC2_TOOLS_WRENCH,
                                1.0F,
                                -1,
                                xCoord,
                                yCoord,
                                zCoord);
                        }
                        return true;
                    }

                    if (GT_Utility.isStackInList(tCurrentItem, GregTech_API.sHardHammerList)) {
                        if (GT_ModHandler.damageOrDechargeItem(tCurrentItem, 1, 1000, aPlayer)) {
                            mInputDisabled = !mInputDisabled;
                            if (mInputDisabled) mOutputDisabled = !mOutputDisabled;
                            GT_Utility.sendChatToPlayer(
                                aPlayer,
                                GT_Utility.trans("086", "Auto-Input: ") + (mInputDisabled
                                    ? GT_Utility.trans("087", "Disabled")
                                    : GT_Utility.trans("088", "Enabled") + GT_Utility.trans("089", "  Auto-Output: ")
                                        + (mOutputDisabled ? GT_Utility.trans("087", "Disabled")
                                            : GT_Utility.trans("088", "Enabled"))));
                            GT_Utility.sendSoundToPlayers(
                                worldObj,
                                SoundResource.RANDOM_ANVIL_USE,
                                1.0F,
                                -1,
                                xCoord,
                                yCoord,
                                zCoord);
                        }
                        return true;
                    }

                    if (GT_Utility.isStackInList(tCurrentItem, GregTech_API.sSoftHammerList)) {
                        if (GT_ModHandler.damageOrDechargeItem(tCurrentItem, 1, 1000, aPlayer)) {
                            if (mWorks) disableWorking();
                            else enableWorking();
                            {
                                String tChat = GT_Utility.trans("090", "Machine Processing: ")
                                    + (isAllowedToWork() ? GT_Utility.trans("088", "Enabled")
                                        : GT_Utility.trans("087", "Disabled"));
                                if (getMetaTileEntity() != null && getMetaTileEntity().hasAlternativeModeText())
                                    tChat = getMetaTileEntity().getAlternativeModeText();
                                GT_Utility.sendChatToPlayer(aPlayer, tChat);
                            }
                            GT_Utility.sendSoundToPlayers(
                                worldObj,
                                SoundResource.IC2_TOOLS_RUBBER_TRAMPOLINE,
                                1.0F,
                                -1,
                                xCoord,
                                yCoord,
                                zCoord);
                        }
                        return true;
                    }

                    if (GT_Utility.isStackInList(tCurrentItem, GregTech_API.sSolderingToolList)) {
                        final byte tSide = GT_Utility.determineWrenchingSide(aSide, aX, aY, aZ);
                        if (mMetaTileEntity.onSolderingToolRightClick(aSide, tSide, aPlayer, aX, aY, aZ)) {
                            // logic handled internally
                            GT_Utility.sendSoundToPlayers(
                                worldObj,
                                SoundResource.IC2_TOOLS_BATTERY_USE,
                                1.0F,
                                -1,
                                xCoord,
                                yCoord,
                                zCoord);
                        } else if (GT_ModHandler.useSolderingIron(tCurrentItem, aPlayer)) {
                            mStrongRedstone ^= (1 << tSide);
                            GT_Utility.sendChatToPlayer(
                                aPlayer,
                                GT_Utility.trans("091", "Redstone Output at Side ") + tSide
                                    + GT_Utility.trans("092", " set to: ")
                                    + ((mStrongRedstone & (1 << tSide)) != 0 ? GT_Utility.trans("093", "Strong")
                                        : GT_Utility.trans("094", "Weak")));
                            GT_Utility.sendSoundToPlayers(
                                worldObj,
                                SoundResource.IC2_TOOLS_BATTERY_USE,
                                3.0F,
                                -1,
                                xCoord,
                                yCoord,
                                zCoord);
                            issueBlockUpdate();
                        }
                        doEnetUpdate();
                        cableUpdateDelay = 10;
                        return true;
                    }

                    if (GT_Utility.isStackInList(tCurrentItem, GregTech_API.sWireCutterList)) {
                        final byte tSide = GT_Utility.determineWrenchingSide(aSide, aX, aY, aZ);
                        if (mMetaTileEntity.onWireCutterRightClick(aSide, tSide, aPlayer, aX, aY, aZ)) {
                            // logic handled internally
                            GT_Utility.sendSoundToPlayers(
                                worldObj,
                                SoundResource.IC2_TOOLS_WRENCH,
                                1.0F,
                                -1,
                                xCoord,
                                yCoord,
                                zCoord);
                        }
                        doEnetUpdate();
                        cableUpdateDelay = 10;
                        return true;
                    }

                    byte coverSide = aSide;
                    if (getCoverIDAtSide(aSide) == 0) coverSide = GT_Utility.determineWrenchingSide(aSide, aX, aY, aZ);

                    if (getCoverIDAtSide(coverSide) == 0) {
                        if (GT_Utility.isStackInList(tCurrentItem, GregTech_API.sCovers.keySet())) {
                            if (GregTech_API.getCoverBehaviorNew(tCurrentItem)
                                .isCoverPlaceable(coverSide, tCurrentItem, this)
                                && mMetaTileEntity.allowCoverOnSide(coverSide, new GT_ItemStack(tCurrentItem))) {
                                setCoverItemAtSide(coverSide, tCurrentItem);
                                if (!aPlayer.capabilities.isCreativeMode) tCurrentItem.stackSize--;
                                GT_Utility.sendSoundToPlayers(
                                    worldObj,
                                    SoundResource.IC2_TOOLS_WRENCH,
                                    1.0F,
                                    -1,
                                    xCoord,
                                    yCoord,
                                    zCoord);
                                sendClientData();
                            }
                            return true;
                        }
                    } else {
                        if (GT_Utility.isStackInList(tCurrentItem, GregTech_API.sCrowbarList)) {
                            if (GT_ModHandler.damageOrDechargeItem(tCurrentItem, 1, 1000, aPlayer)) {
                                GT_Utility.sendSoundToPlayers(
                                    worldObj,
                                    SoundResource.RANDOM_BREAK,
                                    1.0F,
                                    -1,
                                    xCoord,
                                    yCoord,
                                    zCoord);
                                dropCover(coverSide, aSide, false);
                            }
                            return true;
                        }
                    }
                    // End item != null
                } else if (aPlayer.isSneaking()) { // Sneak click, no tool -> open cover config if possible.
                    aSide = (getCoverIDAtSide(aSide) == 0) ? GT_Utility.determineWrenchingSide(aSide, aX, aY, aZ)
                        : aSide;
                    return getCoverIDAtSide(aSide) > 0 && getCoverBehaviorAtSideNew(aSide).onCoverShiftRightClick(
                        aSide,
                        getCoverIDAtSide(aSide),
                        getComplexCoverDataAtSide(aSide),
                        this,
                        aPlayer);
                }

                if (getCoverBehaviorAtSideNew(aSide).onCoverRightClick(
                    aSide,
                    getCoverIDAtSide(aSide),
                    getComplexCoverDataAtSide(aSide),
                    this,
                    aPlayer,
                    aX,
                    aY,
                    aZ)) return true;

                if (!getCoverInfoAtSide(aSide).isGUIClickable()) return false;

                if (isUpgradable() && tCurrentItem != null) {
                    if (ItemList.Upgrade_Muffler.isStackEqual(aPlayer.inventory.getCurrentItem())) {
                        if (addMufflerUpgrade()) {
                            GT_Utility.sendSoundToPlayers(
                                worldObj,
                                SoundResource.RANDOM_CLICK,
                                1.0F,
                                -1,
                                xCoord,
                                yCoord,
                                zCoord);
                            if (!aPlayer.capabilities.isCreativeMode) aPlayer.inventory.getCurrentItem().stackSize--;
                        }
                        return true;
                    }
                    if (ItemList.Upgrade_Lock.isStackEqual(aPlayer.inventory.getCurrentItem())) {
                        if (isUpgradable() && !mLockUpgrade) {
                            mLockUpgrade = true;
                            setOwnerName(aPlayer.getDisplayName());
                            setOwnerUuid(aPlayer.getUniqueID());
                            GT_Utility.sendSoundToPlayers(
                                worldObj,
                                SoundResource.RANDOM_CLICK,
                                1.0F,
                                -1,
                                xCoord,
                                yCoord,
                                zCoord);
                            if (!aPlayer.capabilities.isCreativeMode) aPlayer.inventory.getCurrentItem().stackSize--;
                        }
                        return true;
                    }
                }
            }
        }

        try {
            if (!aPlayer.isSneaking() && hasValidMetaTileEntity())
                return mMetaTileEntity.onRightclick(this, aPlayer, aSide, aX, aY, aZ);
        } catch (Throwable e) {
            GT_Log.err.println(
                "Encountered Exception while rightclicking TileEntity, the Game should've crashed now, but I prevented that. Please report immediately to GregTech Intergalactical!!!");
            e.printStackTrace(GT_Log.err);
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public void onLeftclick(EntityPlayer aPlayer) {
        try {
            if (aPlayer != null && hasValidMetaTileEntity()) mMetaTileEntity.onLeftclick(this, aPlayer);
        } catch (Throwable e) {
            GT_Log.err.println(
                "Encountered Exception while leftclicking TileEntity, the Game should've crashed now, but I prevented that. Please report immediately to GregTech Intergalactical!!!");
            e.printStackTrace(GT_Log.err);
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
    public int[] getAccessibleSlotsFromSide(int aSide) {
        final CoverInfo coverInfo = getCoverInfoAtSide((byte) aSide);
        if (canAccessData() && (coverInfo.letsItemsOut(-1) || coverInfo.letsItemsIn(-1)))
            return mMetaTileEntity.getAccessibleSlotsFromSide(aSide);
        return GT_Values.emptyIntArray;
    }

    /**
     * Can put aStack into Slot at Side
     */
    @Override
    public boolean canInsertItem(int aIndex, ItemStack aStack, int aSide) {
        return canAccessData() && (mRunningThroughTick || !mInputDisabled)
            && getCoverInfoAtSide((byte) aSide).letsItemsIn(aIndex)
            && mMetaTileEntity.canInsertItem(aIndex, aStack, aSide);
    }

    /**
     * Can pull aStack out of Slot from Side
     */
    @Override
    public boolean canExtractItem(int aIndex, ItemStack aStack, int aSide) {
        return canAccessData() && (mRunningThroughTick || !mOutputDisabled)
            && getCoverBehaviorAtSideNew((byte) aSide).letsItemsOut(
                (byte) aSide,
                getCoverIDAtSide((byte) aSide),
                getComplexCoverDataAtSide((byte) aSide),
                aIndex,
                this)
            && mMetaTileEntity.canExtractItem(aIndex, aStack, aSide);
    }

    @Override
    public boolean isUpgradable() {
        return canAccessData() && getUpgradeCount() < 8;
    }

    @Override
    public byte getGeneralRS(byte aSide) {
        if (mMetaTileEntity == null) return 0;
        return mMetaTileEntity.allowGeneralRedstoneOutput() ? mSidedRedstone[aSide] : 0;
    }

    @Override
    public boolean isSteamEngineUpgradable() {
        return isUpgradable() && !hasSteamEngineUpgrade() && getSteamCapacity() > 0;
    }

    @Override
    public boolean addSteamEngineUpgrade() {
        if (isSteamEngineUpgradable()) {
            issueBlockUpdate();
            mSteamConverter = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean hasSteamEngineUpgrade() {
        if (canAccessData() && mMetaTileEntity.isSteampowered()) return true;
        return mSteamConverter;
    }

    @Override
    public boolean hasMufflerUpgrade() {
        return mMuffler;
    }

    @Override
    public boolean isMufflerUpgradable() {
        return isUpgradable() && !hasMufflerUpgrade();
    }

    @Override
    public boolean addMufflerUpgrade() {
        if (isMufflerUpgradable()) return mMuffler = true;
        return false;
    }

    @Override
    public void markInventoryBeenModified() {
        mInventoryChanged = true;
    }

    @Override
    public int getErrorDisplayID() {
        return mDisplayErrorCode;
    }

    @Override
    public void setErrorDisplayID(int aErrorID) {
        mDisplayErrorCode = aErrorID;
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
    protected void updateOutputRedstoneSignal(byte aSide) {
        if (mMetaTileEntity.hasSidedRedstoneOutputBehavior()) {
            setOutputRedstoneSignal(aSide, (byte) 0);
        } else {
            setOutputRedstoneSignal(aSide, (byte) 15);
        }
    }

    @Override
    public String getOwnerName() {
        if (GT_Utility.isStringInvalid(mOwnerName)) return "Player";
        return mOwnerName;
    }

    @Override
    public String setOwnerName(String aName) {
        if (GT_Utility.isStringInvalid(aName)) return mOwnerName = "Player";
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
    public byte getComparatorValue(byte aSide) {
        return canAccessData() ? mMetaTileEntity.getComparatorValue(aSide) : 0;
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
    public long injectEnergyUnits(byte aSide, long aVoltage, long aAmperage) {
        if (!canAccessData() || !mMetaTileEntity.isElectric()
            || !inputEnergyFrom(aSide)
            || aAmperage <= 0
            || aVoltage <= 0
            || getStoredEU() >= getEUCapacity()
            || mMetaTileEntity.maxAmperesIn() <= mAcceptedAmperes) return 0;
        if (aVoltage > getInputVoltage()) {
            GT_Log.exp
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
    public boolean drainEnergyUnits(byte aSide, long aVoltage, long aAmperage) {
        if (!canAccessData() || !mMetaTileEntity.isElectric()
            || !outputsEnergyTo(aSide)
            || getStoredEU() - (aVoltage * aAmperage) < mMetaTileEntity.getMinimumStoredEU()) return false;
        if (decreaseStoredEU(aVoltage * aAmperage, false)) {
            mAverageEUOutput[mAverageEUOutputIndex] += aVoltage * aAmperage;
            return true;
        }
        return false;
    }

    @Override
    public boolean acceptsRotationalEnergy(byte aSide) {
        if (!canAccessData() || getCoverIDAtSide(aSide) != 0) return false;
        return mMetaTileEntity.acceptsRotationalEnergy(aSide);
    }

    @Override
    public boolean injectRotationalEnergy(byte aSide, long aSpeed, long aEnergy) {
        if (!canAccessData() || getCoverIDAtSide(aSide) != 0) return false;
        return mMetaTileEntity.injectRotationalEnergy(aSide, aSpeed, aEnergy);
    }

    @Override
    public int fill(ForgeDirection aSide, FluidStack aFluid, boolean doFill) {
        if (mTickTimer > 5 && canAccessData()
            && (mRunningThroughTick || !mInputDisabled)
            && (aSide == ForgeDirection.UNKNOWN || (mMetaTileEntity.isLiquidInput((byte) aSide.ordinal())
                && getCoverInfoAtSide((byte) aSide.ordinal()).letsFluidIn(aFluid == null ? null : aFluid.getFluid()))))
            return mMetaTileEntity.fill(aSide, aFluid, doFill);
        return 0;
    }

    @Override
    public FluidStack drain(ForgeDirection aSide, int maxDrain, boolean doDrain) {
        if (mTickTimer > 5 && canAccessData()
            && (mRunningThroughTick || !mOutputDisabled)
            && (aSide == ForgeDirection.UNKNOWN || (mMetaTileEntity.isLiquidOutput((byte) aSide.ordinal())
                && getCoverInfoAtSide((byte) aSide.ordinal()).letsFluidOut(
                    mMetaTileEntity.getFluid() == null ? null
                        : mMetaTileEntity.getFluid()
                            .getFluid()))))
            return mMetaTileEntity.drain(aSide, maxDrain, doDrain);
        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection aSide, FluidStack aFluid, boolean doDrain) {
        if (mTickTimer > 5 && canAccessData()
            && (mRunningThroughTick || !mOutputDisabled)
            && (aSide == ForgeDirection.UNKNOWN || (mMetaTileEntity.isLiquidOutput((byte) aSide.ordinal())
                && getCoverInfoAtSide((byte) aSide.ordinal()).letsFluidOut(aFluid == null ? null : aFluid.getFluid()))))
            return mMetaTileEntity.drain(aSide, aFluid, doDrain);
        return null;
    }

    @Override
    public boolean canFill(ForgeDirection aSide, Fluid aFluid) {
        if (mTickTimer > 5 && canAccessData()
            && (mRunningThroughTick || !mInputDisabled)
            && (aSide == ForgeDirection.UNKNOWN || (mMetaTileEntity.isLiquidInput((byte) aSide.ordinal())
                && getCoverInfoAtSide((byte) aSide.ordinal()).letsFluidIn(aFluid))))
            return mMetaTileEntity.canFill(aSide, aFluid);
        return false;
    }

    @Override
    public boolean canDrain(ForgeDirection aSide, Fluid aFluid) {
        if (mTickTimer > 5 && canAccessData()
            && (mRunningThroughTick || !mOutputDisabled)
            && (aSide == ForgeDirection.UNKNOWN || (mMetaTileEntity.isLiquidOutput((byte) aSide.ordinal())
                && getCoverInfoAtSide((byte) aSide.ordinal()).letsFluidOut(aFluid))))
            return mMetaTileEntity.canDrain(aSide, aFluid);
        return false;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection aSide) {
        final byte tSide = (byte) aSide.ordinal();

        if (canAccessData() && (aSide == ForgeDirection.UNKNOWN
            || (mMetaTileEntity.isLiquidInput(tSide) && getCoverInfoAtSide(tSide).letsFluidIn(null))
            || (mMetaTileEntity.isLiquidOutput(tSide) && getCoverInfoAtSide(tSide).letsFluidOut(null))))
            return mMetaTileEntity.getTankInfo(aSide);
        return new FluidTankInfo[] {};
    }

    public double getOutputEnergyUnitsPerTick() {
        return oOutput;
    }

    public boolean isTeleporterCompatible(ForgeDirection aSide) {
        return canAccessData() && mMetaTileEntity.isTeleporterCompatible();
    }

    public double demandedEnergyUnits() {
        if (mReleaseEnergy || !canAccessData() || !mMetaTileEntity.isEnetInput()) return 0;
        return getEUCapacity() - getStoredEU();
    }

    public double injectEnergyUnits(ForgeDirection aDirection, double aAmount) {
        return injectEnergyUnits((byte) aDirection.ordinal(), (int) aAmount, 1) > 0 ? 0 : aAmount;
    }

    public boolean acceptsEnergyFrom(TileEntity aEmitter, ForgeDirection aDirection) {
        return inputEnergyFrom((byte) aDirection.ordinal());
    }

    public boolean emitsEnergyTo(TileEntity aReceiver, ForgeDirection aDirection) {
        return outputsEnergyTo((byte) aDirection.ordinal());
    }

    public double getOfferedEnergy() {
        return (canAccessData() && getStoredEU() - mMetaTileEntity.getMinimumStoredEU() >= oOutput)
            ? Math.max(0, oOutput)
            : 0;
    }

    public void drawEnergy(double amount) {
        mAverageEUOutput[mAverageEUOutputIndex] += amount;
        decreaseStoredEU((int) amount, true);
    }

    public int injectEnergy(ForgeDirection aForgeDirection, int aAmount) {
        return injectEnergyUnits((byte) aForgeDirection.ordinal(), aAmount, 1) > 0 ? 0 : aAmount;
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

    public int getOutput() {
        return (int) Math.min(Integer.MAX_VALUE, oOutput);
    }

    public int injectEnergy(Direction aDirection, int aAmount) {
        return injectEnergyUnits((byte) aDirection.toSideValue(), aAmount, 1) > 0 ? 0 : aAmount;
    }

    public boolean isTeleporterCompatible(Direction aSide) {
        return canAccessData() && mMetaTileEntity.isTeleporterCompatible();
    }

    public boolean acceptsEnergyFrom(TileEntity aReceiver, Direction aDirection) {
        return inputEnergyFrom((byte) aDirection.toSideValue());
    }

    public boolean emitsEnergyTo(TileEntity aReceiver, Direction aDirection) {
        return outputsEnergyTo((byte) aDirection.toSideValue());
    }

    @Override
    public boolean addStackToSlot(int aIndex, ItemStack aStack) {
        if (GT_Utility.isStackInvalid(aStack)) return true;
        if (aIndex < 0 || aIndex >= getSizeInventory()) return false;
        final ItemStack tStack = getStackInSlot(aIndex);
        if (GT_Utility.isStackInvalid(tStack)) {
            setInventorySlotContents(aIndex, aStack);
            return true;
        }
        aStack = GT_OreDictUnificator.get(aStack);
        if (GT_Utility.areStacksEqual(tStack, aStack)
            && tStack.stackSize + aStack.stackSize <= Math.min(aStack.getMaxStackSize(), getInventoryStackLimit())) {
            tStack.stackSize += aStack.stackSize;
            markDirty();
            return true;
        }
        return false;
    }

    @Override
    public boolean addStackToSlot(int aIndex, ItemStack aStack, int aAmount) {
        return addStackToSlot(aIndex, GT_Utility.copyAmount(aAmount, aStack));
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
    public float getBlastResistance(byte aSide) {
        return canAccessData() ? Math.max(0, getMetaTileEntity().getExplosionResistance(aSide)) : 10.0F;
    }

    @Override
    public void onBlockDestroyed() {
        if (canAccessData()) getMetaTileEntity().onBlockDestroyed();
    }

    @Override
    public boolean isUniversalEnergyStored(long aEnergyAmount) {
        if (getUniversalEnergyStored() >= aEnergyAmount) return true;
        mHasEnoughEnergy = false;
        return false;
    }

    @Override
    public String[] getInfoData() {
        {
            if (canAccessData()) return getMetaTileEntity().getInfoData();
            return new String[] {};
        }
    }

    @Override
    public int getLightOpacity() {
        return mMetaTileEntity == null ? getLightValue() > 0 ? 0 : 255 : mMetaTileEntity.getLightOpacity();
    }

    @Override
    public void addCollisionBoxesToList(World aWorld, int aX, int aY, int aZ, AxisAlignedBB inputAABB,
        List<AxisAlignedBB> outputAABB, Entity collider) {
        mMetaTileEntity.addCollisionBoxesToList(aWorld, aX, aY, aZ, inputAABB, outputAABB, collider);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World aWorld, int aX, int aY, int aZ) {
        return mMetaTileEntity.getCollisionBoundingBoxFromPool(aWorld, aX, aY, aZ);
    }

    @Override
    public void onEntityCollidedWithBlock(World aWorld, int aX, int aY, int aZ, Entity collider) {
        mMetaTileEntity.onEntityCollidedWithBlock(aWorld, aX, aY, aZ, collider);
    }

    /**
     * Shifts the machine Inventory index according to the change in Input/Output Slots. This is NOT done automatically.
     * If you want to change slot count for a machine this method needs to be adapted. Currently this method only works
     * for GT_MetaTileEntity_BasicMachine
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
        final int chemistryUpdateVersion = GT_Mod.calculateTotalGTVersion(509, 31);
        final int configCircuitAdditionVersion = GT_Mod.calculateTotalGTVersion(509, 40);
        final int wireAdditionVersion = GT_Mod.calculateTotalGTVersion(509, 41);
        final int disassemblerRemoveVersion = GT_Mod.calculateTotalGTVersion(509, 42, 44);
        if (nbtVersion < 1000000) nbtVersion *= 1000;
        // 4 is old GT_MetaTileEntity_BasicMachine.OTHER_SLOT_COUNT
        if (nbtVersion < configCircuitAdditionVersion && getMetaTileEntity() instanceof GT_MetaTileEntity_BasicMachine
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
        if (slotIndex >= GT_MetaTileEntity_BasicMachine.OTHER_SLOT_COUNT + oldInputSize) {
            indexShift += newInputSize - oldInputSize;
        }
        if (slotIndex >= GT_MetaTileEntity_BasicMachine.OTHER_SLOT_COUNT + oldInputSize + oldOutputSize) {
            indexShift += newOutputSize - oldOutputSize;
        }
        return slotIndex + indexShift;
    }

    @Override
    public IGridNode getGridNode(ForgeDirection forgeDirection) {
        if (mFacing != forgeDirection.ordinal()) return null;
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

    @TileEvent(TileEventType.WORLD_NBT_READ)
    public void readFromNBT_AENetwork(final NBTTagCompound data) {
        final AENetworkProxy gp = getProxy();
        if (gp != null) getProxy().readFromNBT(data);
    }

    @TileEvent(TileEventType.WORLD_NBT_WRITE)
    public void writeToNBT_AENetwork(final NBTTagCompound data) {
        final AENetworkProxy gp = getProxy();
        if (gp != null) gp.writeToNBT(data);
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
}
