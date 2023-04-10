package gregtech.api.metatileentity;

import static gregtech.GT_Mod.GT_FML_LOGGER;
import static gregtech.api.enums.GT_Values.NW;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.graphs.Lock;
import gregtech.api.graphs.Node;
import gregtech.api.graphs.paths.NodePath;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IConnectable;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IDebugableTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IPipeRenderedTileEntity;
import gregtech.api.net.GT_Packet_TileEntity;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gregtech.common.covers.CoverInfo;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * <p/>
 * This is the main TileEntity for EVERYTHING.
 */
public class BaseMetaPipeEntity extends CommonMetaTileEntity
    implements IGregTechTileEntity, IPipeRenderedTileEntity, IDebugableTileEntity {

    public byte mConnections = IConnectable.NO_CONNECTION;
    protected MetaPipeEntity mMetaTileEntity;
    private final int[] mTimeStatistics = new int[GregTech_API.TICKS_FOR_LAG_AVERAGING];
    private boolean mWorkUpdate = false, mWorks = true;
    private byte mColor = 0, oColor = 0, oStrongRedstone = 0, oRedstoneData = 63, oTextureData = 0, oUpdateData = 0,
        mLagWarningCount = 0;
    private int oX = 0, oY = 0, oZ = 0, mTimeStatisticsIndex = 0;
    protected Node node;
    protected NodePath nodePath;

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public NodePath getNodePath() {
        return nodePath;
    }

    public void setNodePath(NodePath nodePath) {
        this.nodePath = nodePath;
    }

    public void addToLock(TileEntity tileEntity, int side) {
        if (node != null) {
            final Lock lock = node.locks[side];
            if (lock != null) {
                lock.addTileEntity(tileEntity);
            }
        } else if (nodePath != null) {
            nodePath.lock.addTileEntity(tileEntity);
        }
    }

    public void removeFromLock(TileEntity tileEntity, int side) {
        if (node != null) {
            final Lock lock = node.locks[side];
            if (lock != null) {
                lock.removeTileEntity(tileEntity);
            }
        } else if (nodePath != null) {
            nodePath.lock.removeTileEntity(tileEntity);
        }
    }

    public void reloadLocks() {
        final IMetaTileEntity meta = getMetaTileEntity();
        if (meta instanceof MetaPipeEntity) {
            ((MetaPipeEntity) meta).reloadLocks();
        }
    }

    public BaseMetaPipeEntity() {}

    @Override
    public void writeToNBT(NBTTagCompound aNBT) {
        try {
            super.writeToNBT(aNBT);
        } catch (Throwable e) {
            GT_FML_LOGGER.error("Encountered CRITICAL ERROR while saving MetaTileEntity", e);
        }
        try {
            aNBT.setInteger("mID", mID);
            writeCoverNBT(aNBT, false);
            aNBT.setByte("mConnections", mConnections);
            aNBT.setByte("mColor", mColor);
            aNBT.setBoolean("mWorks", !mWorks);
        } catch (Throwable e) {
            GT_FML_LOGGER.error("Encountered CRITICAL ERROR while saving MetaTileEntity", e);
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
        } else {
            if (aID <= 0) mID = (short) aNBT.getInteger("mID");
            else mID = aID;
            mConnections = aNBT.getByte("mConnections");
            mColor = aNBT.getByte("mColor");
            mWorks = !aNBT.getBoolean("mWorks");

            if (mSidedRedstone.length != 6) mSidedRedstone = new byte[] { 0, 0, 0, 0, 0, 0 };

            readCoverNBT(aNBT);
            loadMetaTileNBT(aNBT);
        }
    }

    @Override
    public void updateEntity() {
        super.updateEntity();

        if (!hasValidMetaTileEntity()) {
            if (mMetaTileEntity == null) return;
            mMetaTileEntity.setBaseMetaTileEntity(this);
        }

        long tTime = System.nanoTime();
        try {
            if (hasValidMetaTileEntity()) {
                if (mTickTimer++ == 0) {
                    oX = xCoord;
                    oY = yCoord;
                    oZ = zCoord;
                    if (isServerSide()) checkDropCover();
                    else {
                        requestCoverDataIfNeeded();
                    }
                    worldObj.markTileEntityChunkModified(xCoord, yCoord, zCoord, this);
                    mMetaTileEntity.onFirstTick(this);
                    if (!hasValidMetaTileEntity()) return;
                }

                if (isClientSide()) {
                    if (mColor != oColor) {
                        mMetaTileEntity.onColorChangeClient(oColor = mColor);
                        issueTextureUpdate();
                    }

                    if (mNeedsUpdate) {
                        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                        mNeedsUpdate = false;
                    }
                }
                if (isServerSide() && mTickTimer > 10) {
                    if (!doCoverThings()) return;

                    final byte oldConnections = mConnections;
                    // Mask-out connection direction bits to keep only Foam related connections
                    mConnections = (byte) (mMetaTileEntity.mConnections | (mConnections & ~IConnectable.CONNECTED_ALL));
                    // If foam not hardened, tries roll chance to harden
                    if ((mConnections & IConnectable.HAS_FOAM) == IConnectable.HAS_FRESHFOAM
                        && getRandomNumber(1000) == 0) {
                        mConnections = (byte) ((mConnections & ~IConnectable.HAS_FRESHFOAM)
                            | IConnectable.HAS_HARDENEDFOAM);
                    }
                    if (mTickTimer > 12 && oldConnections != mConnections)
                        GregTech_API.causeCableUpdate(worldObj, xCoord, yCoord, zCoord);
                }
                mMetaTileEntity.onPreTick(this, mTickTimer);
                if (!hasValidMetaTileEntity()) return;
                if (isServerSide()) {
                    if (mTickTimer == 10) {
                        updateCoverBehavior();
                        issueBlockUpdate();
                        joinEnet();
                    }

                    if (xCoord != oX || yCoord != oY || zCoord != oZ) {
                        oX = xCoord;
                        oY = yCoord;
                        oZ = zCoord;
                        issueClientUpdate();
                        clearTileEntityBuffer();
                    }
                }

                mMetaTileEntity.onPostTick(this, mTickTimer);
                if (!hasValidMetaTileEntity()) return;

                if (isServerSide()) {
                    if (mTickTimer % 10 == 0) {
                        sendClientData();
                    }

                    if (mTickTimer > 10) {
                        if (mConnections != oTextureData) sendBlockEvent((byte) 0, oTextureData = mConnections);
                        byte tData = mMetaTileEntity.getUpdateData();
                        if (tData != oUpdateData) sendBlockEvent((byte) 1, oUpdateData = tData);
                        if (mColor != oColor) sendBlockEvent((byte) 2, oColor = mColor);
                        tData = (byte) (((mSidedRedstone[0] > 0) ? 1 : 0) | ((mSidedRedstone[1] > 0) ? 2 : 0)
                            | ((mSidedRedstone[2] > 0) ? 4 : 0)
                            | ((mSidedRedstone[3] > 0) ? 8 : 0)
                            | ((mSidedRedstone[4] > 0) ? 16 : 0)
                            | ((mSidedRedstone[5] > 0) ? 32 : 0));
                        if (tData != oRedstoneData) sendBlockEvent((byte) 3, oRedstoneData = tData);
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

        if (isServerSide() && hasValidMetaTileEntity()) {
            tTime = System.nanoTime() - tTime;
            if (mTimeStatistics.length > 0) mTimeStatistics[mTimeStatisticsIndex = (mTimeStatisticsIndex + 1)
                % mTimeStatistics.length] = (int) tTime;
            if (tTime > 0 && tTime > (GregTech_API.MILLISECOND_THRESHOLD_UNTIL_LAG_WARNING * 1000000L)
                && mTickTimer > 1000
                && getMetaTileEntity().doTickProfilingMessageDuringThisTick()
                && mLagWarningCount++ < 10)
                GT_FML_LOGGER.warn(
                    "WARNING: Possible Lag Source at [" + xCoord
                        + ","
                        + yCoord
                        + ","
                        + zCoord
                        + "] in Dimension "
                        + worldObj.provider.dimensionId
                        + " with "
                        + tTime
                        + " ns caused by an instance of "
                        + getMetaTileEntity().getClass());
        }

        mWorkUpdate = mInventoryChanged = false;
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
                    oTextureData = mConnections,
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
        int aCover4, int aCover5, byte aTextureData, byte aUpdateData, byte aRedstoneData, byte aColorData) {
        issueTextureUpdate();
        if (aID > 0 && mID != aID) {
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
        receiveClientEvent(GregTechTileClientEvents.CHANGE_CUSTOM_DATA, aUpdateData);
        receiveClientEvent(GregTechTileClientEvents.CHANGE_COLOR, aColorData);
        receiveClientEvent(GregTechTileClientEvents.CHANGE_REDSTONE_OUTPUT, aRedstoneData);
    }

    @Override
    public boolean receiveClientEvent(int aEventID, int aValue) {
        super.receiveClientEvent(aEventID, aValue);

        if (hasValidMetaTileEntity()) {
            try {
                mMetaTileEntity.receiveClientEvent((byte) aEventID, (byte) aValue);
            } catch (Throwable e) {
                GT_FML_LOGGER.error("Encountered Exception while receiving Data from the Server", e);
            }
        }

        if (isClientSide()) {
            issueTextureUpdate();
            switch (aEventID) {
                case GregTechTileClientEvents.CHANGE_COMMON_DATA -> mConnections = (byte) aValue;
                case GregTechTileClientEvents.CHANGE_CUSTOM_DATA -> {
                    if (hasValidMetaTileEntity()) mMetaTileEntity.onValueUpdate((byte) aValue);
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
            }
        }
        return true;
    }

    @Override
    public ArrayList<String> getDebugInfo(EntityPlayer aPlayer, int aLogLevel) {
        final ArrayList<String> tList = new ArrayList<>();
        if (aLogLevel > 3) {
            tList.add(
                "Meta-ID: " + EnumChatFormatting.BLUE
                    + mID
                    + EnumChatFormatting.RESET
                    + (hasValidMetaTileEntity() ? EnumChatFormatting.GREEN + " valid" + EnumChatFormatting.RESET
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
                }
                tList.add(
                    "Average CPU-load of ~" + (tAverageTime / mTimeStatistics.length)
                        + "ns since "
                        + mTimeStatistics.length
                        + " ticks with worst time of "
                        + tWorstTime
                        + "ns.");
            }
            if (mLagWarningCount > 0) {
                tList.add(
                    "Caused " + (mLagWarningCount >= 10 ? "more than 10" : mLagWarningCount)
                        + " Lag Spike Warnings (anything taking longer than "
                        + GregTech_API.MILLISECOND_THRESHOLD_UNTIL_LAG_WARNING
                        + "ms) on the Server.");
            }
            if (mMetaTileEntity != null) {
                tList.add(
                    "Is" + (mMetaTileEntity.isAccessAllowed(aPlayer) ? " "
                        : EnumChatFormatting.RED + " not " + EnumChatFormatting.RESET) + "accessible for you");
            }
        }
        if (joinedIc2Enet) tList.add("Joined IC2 ENet");

        return mMetaTileEntity != null ? mMetaTileEntity.getSpecialDebugInfo(this, aPlayer, aLogLevel, tList)
            : new ArrayList<>();
    }

    @Override
    public boolean isGivingInformation() {
        if (canAccessData()) return mMetaTileEntity.isGivingInformation();
        return false;
    }

    @Override
    public byte getBackFacing() {
        return GT_Utility.getOppositeSide(getFrontFacing());
    }

    @Override
    public byte getFrontFacing() {
        return 6;
    }

    @Override
    public void setFrontFacing(byte aFacing) {
        doEnetUpdate();
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
        markDirty();
        mInventoryChanged = true;
        if (canAccessData()) mMetaTileEntity
            .setInventorySlotContents(aIndex, worldObj.isRemote ? aStack : GT_OreDictUnificator.setStack(true, aStack));
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
        /* Do nothing */
    }

    @Override
    public void closeInventory() {
        /* Do nothing */
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer aPlayer) {
        return hasValidMetaTileEntity() && mTickTimer > 40
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
        if (hasValidMetaTileEntity()) {
            mMetaTileEntity.onRemoval();
            mMetaTileEntity.setBaseMetaTileEntity(null);
        }
        leaveEnet();
        super.invalidate();
    }

    @Override
    public void onChunkUnload() {
        super.onChunkUnload();
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        ItemStack stack = getStackInSlot(slot);
        if (stack != null) setInventorySlotContents(slot, null);
        return stack;
    }

    /**
     * Checks validity of meta tile and delegates to it
     */
    @Override
    public void onMachineBlockUpdate() {
        if (canAccessData()) mMetaTileEntity.onMachineBlockUpdate();
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
        reloadLocks();
    }

    @Override
    public void disableWorking() {
        mWorks = false;
        reloadLocks();
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
        return 0;
    }

    @Override
    public void setWorkDataValue(byte aValue) {
        /* Do nothing */
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
        return false;
    }

    @Override
    public void setActive(boolean aActive) {
        /* Do nothing */
    }

    @Override
    public long getTimer() {
        return mTickTimer;
    }

    @Override
    public boolean decreaseStoredEnergyUnits(long aEnergy, boolean aIgnoreTooLessEnergy) {
        return false;
    }

    @Override
    public boolean increaseStoredEnergyUnits(long aEnergy, boolean aIgnoreTooMuchEnergy) {
        return false;
    }

    @Override
    public boolean inputEnergyFrom(byte aSide) {
        return false;
    }

    @Override
    public boolean inputEnergyFrom(byte aSide, boolean waitForActive) {
        return false;
    }

    @Override
    public boolean outputsEnergyTo(byte aSide) {
        return false;
    }

    @Override
    public boolean outputsEnergyTo(byte aSide, boolean waitForActive) {
        return false;
    }

    @Override
    public long getOutputAmperage() {
        return 0;
    }

    @Override
    public long getOutputVoltage() {
        return 0;
    }

    @Override
    public long getInputAmperage() {
        return 0;
    }

    @Override
    public long getInputVoltage() {
        return 0;
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
        return 0;
    }

    @Override
    public long getEUCapacity() {
        return 0;
    }

    @Override
    public ITexture[] getTexture(Block aBlock, byte aSide) {
        final ITexture rIcon = getCoverTexture(aSide);
        if (rIcon != null) return new ITexture[] { rIcon };
        return getTextureUncovered(aSide);
    }

    @Override
    @Deprecated
    public ITexture[] getTextureCovered(byte aSide) {
        final ITexture coverTexture = getCoverTexture(aSide);
        final ITexture[] textureUncovered = getTextureUncovered(aSide);
        final ITexture[] textureCovered;
        if (coverTexture != null) {
            textureCovered = Arrays.copyOf(textureUncovered, textureUncovered.length + 1);
            textureCovered[textureUncovered.length] = coverTexture;
            return textureCovered;
        } else {
            return textureUncovered;
        }
    }

    @Override
    public ITexture getTextureCovered(ForgeDirection dir) {
        final ITexture textureUncovered = getTextureUncovered(dir);
        final ITexture coverTexture = getCoverTexture(dir);
        if (coverTexture == null) {
            return textureUncovered;
        } else {
            return TextureFactory.of(textureUncovered, coverTexture);
        }
    }

    @Override
    public ITexture[] getTextureUncovered(byte aSide) {
        if ((mConnections & IConnectable.HAS_FRESHFOAM) != 0) return Textures.BlockIcons.FRESHFOAM;
        if ((mConnections & IConnectable.HAS_HARDENEDFOAM) != 0) return Textures.BlockIcons.HARDENEDFOAMS[mColor];
        if ((mConnections & IConnectable.HAS_FOAM) == 0) {
            byte tConnections = (byte) switch (mConnections) {
                case IConnectable.CONNECTED_WEST, IConnectable.CONNECTED_EAST -> (IConnectable.CONNECTED_WEST
                    | IConnectable.CONNECTED_EAST);
                case IConnectable.CONNECTED_DOWN, IConnectable.CONNECTED_UP -> (IConnectable.CONNECTED_DOWN
                    | IConnectable.CONNECTED_UP);
                case IConnectable.CONNECTED_NORTH, IConnectable.CONNECTED_SOUTH -> (IConnectable.CONNECTED_NORTH
                    | IConnectable.CONNECTED_SOUTH);
                default -> mConnections;
            };
            if (hasValidMetaTileEntity()) {
                return mMetaTileEntity.getTexture(
                    this,
                    aSide,
                    tConnections,
                    (byte) (mColor - 1),
                    tConnections == 0 || (tConnections & (1 << aSide)) != 0,
                    getOutputRedstoneSignal(aSide) > 0);
            }
            return Textures.BlockIcons.ERROR_RENDERING;
        }
        return Textures.BlockIcons.ERROR_RENDERING;
    }

    @Override
    protected boolean hasValidMetaTileEntity() {
        return mMetaTileEntity != null && mMetaTileEntity.getBaseMetaTileEntity() == this;
    }

    @Override
    public void doExplosion(long aAmount) {
        if (canAccessData()) {
            mMetaTileEntity.onExplosion();
            mMetaTileEntity.doExplosion(aAmount);
        }
    }

    @Override
    public ArrayList<ItemStack> getDrops() {
        final ItemStack rStack = new ItemStack(GregTech_API.sBlockMachines, 1, mID);
        final NBTTagCompound tNBT = new NBTTagCompound();

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

    @Override
    public boolean onRightclick(EntityPlayer aPlayer, byte aSide, float aX, float aY, float aZ) {
        if (isClientSide()) {
            // Configure Cover, sneak can also be: screwdriver, wrench, side cutter, soldering iron
            if (aPlayer.isSneaking()) {
                final byte tSide = (getCoverIDAtSide(aSide) == 0) ? GT_Utility.determineWrenchingSide(aSide, aX, aY, aZ)
                    : aSide;
                return (getCoverInfoAtSide(tSide).hasCoverGUI());
            } else if (getCoverBehaviorAtSideNew(aSide).onCoverRightclickClient(aSide, this, aPlayer, aX, aY, aZ)) {
                return true;
            }
        }
        if (isServerSide()) {
            final ItemStack tCurrentItem = aPlayer.inventory.getCurrentItem();
            if (tCurrentItem != null) {
                if (getColorization() >= 0
                    && GT_Utility.areStacksEqual(new ItemStack(Items.water_bucket, 1), tCurrentItem)) {
                    mMetaTileEntity.markDirty();
                    tCurrentItem.func_150996_a(Items.bucket);
                    setColorization((byte) -1);
                    return true;
                }
                final byte tSide = GT_Utility.determineWrenchingSide(aSide, aX, aY, aZ);
                if (GT_Utility.isStackInList(tCurrentItem, GregTech_API.sWrenchList)) {
                    if (mMetaTileEntity.onWrenchRightClick(aSide, tSide, aPlayer, aX, aY, aZ)) {
                        mMetaTileEntity.markDirty();
                        GT_ModHandler.damageOrDechargeItem(tCurrentItem, 1, 1000, aPlayer);
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
                if (GT_Utility.isStackInList(tCurrentItem, GregTech_API.sScrewdriverList)) {
                    if (getCoverIDAtSide(aSide) == 0 && getCoverIDAtSide(tSide) != 0) {
                        if (GT_ModHandler.damageOrDechargeItem(tCurrentItem, 1, 200, aPlayer)) {
                            setCoverDataAtSide(
                                tSide,
                                getCoverInfoAtSide(tSide).onCoverScrewdriverClick(aPlayer, 0.5F, 0.5F, 0.5F));
                            mMetaTileEntity.onScrewdriverRightClick(tSide, aPlayer, aX, aY, aZ);
                            mMetaTileEntity.markDirty();
                            GT_Utility.sendSoundToPlayers(
                                worldObj,
                                SoundResource.IC2_TOOLS_WRENCH,
                                1.0F,
                                -1,
                                xCoord,
                                yCoord,
                                zCoord);
                        }
                    } else {
                        if (GT_ModHandler.damageOrDechargeItem(tCurrentItem, 1, 1000, aPlayer)) {
                            setCoverDataAtSide(
                                aSide,
                                getCoverInfoAtSide(aSide).onCoverScrewdriverClick(aPlayer, aX, aY, aZ));
                            mMetaTileEntity.onScrewdriverRightClick(aSide, aPlayer, aX, aY, aZ);
                            mMetaTileEntity.markDirty();
                            GT_Utility.sendSoundToPlayers(
                                worldObj,
                                SoundResource.IC2_TOOLS_WRENCH,
                                1.0F,
                                -1,
                                xCoord,
                                yCoord,
                                zCoord);
                        }
                    }
                    return true;
                }

                if (GT_Utility.isStackInList(tCurrentItem, GregTech_API.sHardHammerList)) {
                    // if (GT_ModHandler.damageOrDechargeItem(tCurrentItem, 1, 1000, aPlayer)) {
                    // GT_Utility.sendSoundToPlayers(worldObj, GregTech_API.sSoundList.get(1), 1.0F, -1, xCoord, yCoord,
                    // zCoord);
                    // }
                    return true;
                }

                if (GT_Utility.isStackInList(tCurrentItem, GregTech_API.sSoftHammerList)) {
                    if (GT_ModHandler.damageOrDechargeItem(tCurrentItem, 1, 1000, aPlayer)) {
                        if (mWorks) disableWorking();
                        else enableWorking();
                        mMetaTileEntity.markDirty();
                        GT_Utility.sendChatToPlayer(
                            aPlayer,
                            GT_Utility.trans("090", "Machine Processing: ")
                                + (isAllowedToWork() ? GT_Utility.trans("088", "Enabled")
                                    : GT_Utility.trans("087", "Disabled")));
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

                if (GT_Utility.isStackInList(tCurrentItem, GregTech_API.sWireCutterList)) {
                    if (mMetaTileEntity.onWireCutterRightClick(aSide, tSide, aPlayer, aX, aY, aZ)) {
                        mMetaTileEntity.markDirty();
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
                    return true;
                }

                if (GT_Utility.isStackInList(tCurrentItem, GregTech_API.sSolderingToolList)) {
                    if (mMetaTileEntity.onSolderingToolRightClick(aSide, tSide, aPlayer, aX, aY, aZ)) {
                        mMetaTileEntity.markDirty();
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
                        mMetaTileEntity.markDirty();
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
                    return true;
                }

                byte coverSide = aSide;
                if (getCoverIDAtSide(aSide) == 0) coverSide = tSide;

                final CoverInfo coverInfo = getCoverInfoAtSide(coverSide);

                if (coverInfo.getCoverID() == 0) {
                    if (GT_Utility.isStackInList(tCurrentItem, GregTech_API.sCovers.keySet())) {
                        if (GregTech_API.getCoverBehaviorNew(tCurrentItem)
                            .isCoverPlaceable(coverSide, tCurrentItem, this)
                            && mMetaTileEntity.allowCoverOnSide(coverSide, new GT_ItemStack(tCurrentItem))) {
                            setCoverItemAtSide(coverSide, tCurrentItem);
                            mMetaTileEntity.markDirty();
                            if (!aPlayer.capabilities.isCreativeMode) tCurrentItem.stackSize--;
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
                            mMetaTileEntity.markDirty();
                        }
                        return true;
                    }
                }
            } else if (aPlayer.isSneaking()) { // Sneak click, no tool -> open cover config or turn back.
                aSide = (getCoverIDAtSide(aSide) == 0) ? GT_Utility.determineWrenchingSide(aSide, aX, aY, aZ) : aSide;
                final CoverInfo coverInfo = getCoverInfoAtSide(aSide);
                return coverInfo.isValid() && coverInfo.onCoverShiftRightClick(aPlayer);
            }

            if (getCoverInfoAtSide(aSide).onCoverRightClick(aPlayer, aX, aY, aZ)) return true;
        }

        if (!getCoverInfoAtSide(aSide).isGUIClickable()) return false;

        try {
            if (!aPlayer.isSneaking() && hasValidMetaTileEntity()) {
                final boolean handled = mMetaTileEntity.onRightclick(this, aPlayer, aSide, aX, aY, aZ);
                if (handled) {
                    mMetaTileEntity.markDirty();
                }
                return handled;
            }
        } catch (Throwable e) {
            GT_FML_LOGGER.error("Encountered Exception while right clicking TileEntity", e);
        }

        return false;
    }

    @Override
    public void onLeftclick(EntityPlayer aPlayer) {
        try {
            if (aPlayer != null && hasValidMetaTileEntity()) mMetaTileEntity.onLeftclick(this, aPlayer);
        } catch (Throwable e) {
            GT_FML_LOGGER.error("Encountered Exception while left clicking TileEntity", e);
        }
    }

    @Override
    public boolean isDigitalChest() {
        return false;
    }

    @Override
    public ItemStack[] getStoredItemData() {
        return null;
    }

    @Override
    public void setItemCount(int aCount) {
        //
    }

    @Override
    public int getMaxItemCount() {
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
        return canAccessData() && getCoverInfoAtSide((byte) aSide).letsItemsIn(aIndex)
            && mMetaTileEntity.canInsertItem(aIndex, aStack, aSide);
    }

    /**
     * Can pull aStack out of Slot from Side
     */
    @Override
    public boolean canExtractItem(int aIndex, ItemStack aStack, int aSide) {
        return canAccessData() && getCoverBehaviorAtSideNew((byte) aSide).letsItemsOut(
            (byte) aSide,
            getCoverIDAtSide((byte) aSide),
            getComplexCoverDataAtSide((byte) aSide),
            aIndex,
            this) && mMetaTileEntity.canExtractItem(aIndex, aStack, aSide);
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isSteamEngineUpgradable() {
        return isUpgradable() && !hasSteamEngineUpgrade() && getSteamCapacity() > 0;
    }

    @Override
    public boolean addSteamEngineUpgrade() {
        if (isSteamEngineUpgradable()) {
            issueBlockUpdate();
            return true;
        }
        return false;
    }

    @Override
    public boolean hasSteamEngineUpgrade() {
        return false;
    }

    @Override
    public void setGenericRedstoneOutput(boolean aOnOff) {
        // Do nothing
    }

    @Override
    public int getErrorDisplayID() {
        return 0;
    }

    @Override
    public void setErrorDisplayID(int aErrorID) {
        //
    }

    @Override
    public IMetaTileEntity getMetaTileEntity() {
        return hasValidMetaTileEntity() ? mMetaTileEntity : null;
    }

    @Override
    public void setMetaTileEntity(IMetaTileEntity aMetaTileEntity) {
        mMetaTileEntity = (MetaPipeEntity) aMetaTileEntity;
    }

    @Override
    public void setLightValue(byte aLightValue) {
        //
    }

    @Override
    public long getAverageElectricInput() {
        return 0;
    }

    @Override
    public long getAverageElectricOutput() {
        return 0;
    }

    @Override
    public String getOwnerName() {
        return "Player";
    }

    @Override
    public String setOwnerName(String aName) {
        return "Player";
    }

    @Override
    public UUID getOwnerUuid() {
        return GT_Utility.defaultUuid;
    }

    @Override
    public void setOwnerUuid(UUID uuid) {}

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
        if (canAccessData()) return mMetaTileEntity.injectEnergyUnits(aSide, aVoltage, aAmperage);
        return 0;
    }

    @Override
    public boolean drainEnergyUnits(byte aSide, long aVoltage, long aAmperage) {
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

    private boolean canMoveFluidOnSide(ForgeDirection aSide, Fluid aFluid, boolean isFill) {
        if (aSide == ForgeDirection.UNKNOWN) return true;

        final IFluidHandler tTileEntity = getITankContainerAtSide((byte) aSide.ordinal());
        // Only require a connection if there's something to connect to - Allows fluid cells & buckets to interact with
        // the pipe
        if (tTileEntity != null && !mMetaTileEntity.isConnectedAtSide((byte) aSide.ordinal())) return false;

        if (isFill && mMetaTileEntity.isLiquidInput((byte) aSide.ordinal())
            && getCoverInfoAtSide((byte) aSide.ordinal()).letsFluidIn(aFluid)) return true;

        if (!isFill && mMetaTileEntity.isLiquidOutput((byte) aSide.ordinal())
            && getCoverInfoAtSide((byte) aSide.ordinal()).letsFluidOut(aFluid)) return true;

        return false;
    }

    @Override
    public int fill(ForgeDirection aSide, FluidStack aFluidStack, boolean doFill) {
        if (mTickTimer > 5 && canAccessData()
            && canMoveFluidOnSide(aSide, aFluidStack == null ? null : aFluidStack.getFluid(), true))
            return mMetaTileEntity.fill(aSide, aFluidStack, doFill);
        return 0;
    }

    @Override
    public FluidStack drain(ForgeDirection aSide, int maxDrain, boolean doDrain) {
        if (mTickTimer > 5 && canAccessData()
            && canMoveFluidOnSide(
                aSide,
                mMetaTileEntity.getFluid() == null ? null
                    : mMetaTileEntity.getFluid()
                        .getFluid(),
                false))
            return mMetaTileEntity.drain(aSide, maxDrain, doDrain);
        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection aSide, FluidStack aFluidStack, boolean doDrain) {
        if (mTickTimer > 5 && canAccessData()
            && canMoveFluidOnSide(aSide, aFluidStack == null ? null : aFluidStack.getFluid(), false))
            return mMetaTileEntity.drain(aSide, aFluidStack, doDrain);
        return null;
    }

    @Override
    public boolean canFill(ForgeDirection aSide, Fluid aFluid) {
        if (mTickTimer > 5 && canAccessData() && canMoveFluidOnSide(aSide, aFluid, true))
            return mMetaTileEntity.canFill(aSide, aFluid);
        return false;
    }

    @Override
    public boolean canDrain(ForgeDirection aSide, Fluid aFluid) {
        if (mTickTimer > 5 && canAccessData() && canMoveFluidOnSide(aSide, aFluid, false))
            return mMetaTileEntity.canDrain(aSide, aFluid);
        return false;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection aSide) {
        final CoverInfo coverInfo = getCoverInfoAtSide((byte) aSide.ordinal());
        if (canAccessData() && (aSide == ForgeDirection.UNKNOWN
            || (mMetaTileEntity.isLiquidInput((byte) aSide.ordinal()) && coverInfo.letsFluidIn(null))
            || (mMetaTileEntity.isLiquidOutput((byte) aSide.ordinal()) && coverInfo.letsFluidOut(null))
        // Doesn't need to be connected to get Tank Info -- otherwise things can't connect
        )) return mMetaTileEntity.getTankInfo(aSide);
        return new FluidTankInfo[] {};
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
            markDirty();
            tStack.stackSize += aStack.stackSize;
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
    public float getThickNess() {
        if (canAccessData()) return mMetaTileEntity.getThickNess();
        return 1.0F;
    }

    public boolean renderInside(byte aSide) {
        if (canAccessData()) return mMetaTileEntity.renderInside(aSide);
        return false;
    }

    @Override
    public float getBlastResistance(byte aSide) {
        return (mConnections & IConnectable.HAS_FOAM) != 0 ? 50.0F : 5.0F;
    }

    @Override
    public void onBlockDestroyed() {
        if (canAccessData()) getMetaTileEntity().onBlockDestroyed();
    }

    @Override
    public boolean isMufflerUpgradable() {
        return false;
    }

    @Override
    public boolean addMufflerUpgrade() {
        return false;
    }

    @Override
    public boolean hasMufflerUpgrade() {
        return false;
    }

    @Override
    public boolean isUniversalEnergyStored(long aEnergyAmount) {
        return getUniversalEnergyStored() >= aEnergyAmount;
    }

    @Override
    public String[] getInfoData() {
        {
            if (canAccessData()) return getMetaTileEntity().getInfoData();
            return new String[] {};
        }
    }

    @Override
    public byte getConnections() {
        return mConnections;
    }

    public void onNeighborBlockChange(int aX, int aY, int aZ) {
        if (canAccessData()) {
            final IMetaTileEntity meta = getMetaTileEntity();
            if (meta instanceof MetaPipeEntity) {
                // Trigger a checking of connections in case someone placed down a block that the pipe/wire shouldn't be
                // connected to.
                // However; don't do it immediately in case the world isn't finished loading
                // (This caused issues with AE2 GTEU p2p connections.
                ((MetaPipeEntity) meta).setCheckConnections();
            }
        }
    }

    @Override
    public int getLightOpacity() {
        return mMetaTileEntity == null ? 0 : mMetaTileEntity.getLightOpacity();
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
}
