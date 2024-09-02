package gregtech.api.metatileentity;

import static gregtech.GTMod.GT_FML_LOGGER;
import static gregtech.api.enums.GTValues.NW;

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

import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
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
import gregtech.api.net.GTPacketTileEntity;
import gregtech.api.objects.GTItemStack;
import gregtech.api.util.CoverBehaviorBase;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.common.covers.CoverInfo;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * <p/>
 * This is the main TileEntity for EVERYTHING.
 */
public class BaseMetaPipeEntity extends CommonMetaTileEntity
    implements IGregTechTileEntity, IPipeRenderedTileEntity, IDebugableTileEntity {

    public byte mConnections = IConnectable.NO_CONNECTION;
    protected MetaPipeEntity mMetaTileEntity;
    private final int[] mTimeStatistics = new int[GregTechAPI.TICKS_FOR_LAG_AVERAGING];
    private boolean hasTimeStatisticsStarted;
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

    public void addToLock(TileEntity tileEntity, ForgeDirection side) {
        if (node != null) {
            final Lock lock = node.locks[side.ordinal()];
            if (lock != null) {
                lock.addTileEntity(tileEntity);
            }
        } else if (nodePath != null) {
            nodePath.lock.addTileEntity(tileEntity);
        }
    }

    public void removeFromLock(TileEntity tileEntity, ForgeDirection side) {
        if (node != null) {
            final Lock lock = node.locks[side.ordinal()];
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
    public void writeToNBT(NBTTagCompound nbt) {
        try {
            super.writeToNBT(nbt);
        } catch (Throwable e) {
            GT_FML_LOGGER.error("Encountered CRITICAL ERROR while saving MetaTileEntity", e);
        }
        try {
            nbt.setInteger("mID", mID);
            writeCoverNBT(nbt, false);
            nbt.setByte("mConnections", mConnections);
            nbt.setByte("mColor", mColor);
            nbt.setBoolean("mWorks", !mWorks);
        } catch (Throwable e) {
            GT_FML_LOGGER.error("Encountered CRITICAL ERROR while saving MetaTileEntity", e);
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

        long tTime;
        if (hasTimeStatisticsStarted) {
            tTime = System.nanoTime();
        } else {
            tTime = 0;
        }
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
                        GregTechAPI.causeCableUpdate(worldObj, xCoord, yCoord, zCoord);
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
            e.printStackTrace(GTLog.err);
        }

        if (isServerSide() && hasTimeStatisticsStarted && hasValidMetaTileEntity()) {
            tTime = System.nanoTime() - tTime;
            mTimeStatisticsIndex = (mTimeStatisticsIndex + 1) % mTimeStatistics.length;
            mTimeStatistics[mTimeStatisticsIndex] = (int) tTime;
            if (tTime > 0 && tTime > (GregTechAPI.MILLISECOND_THRESHOLD_UNTIL_LAG_WARNING * 1000000L)
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
                new GTPacketTileEntity(
                    xCoord,
                    (short) yCoord,
                    zCoord,
                    mID,
                    getCoverInfoAtSide(ForgeDirection.DOWN).getCoverID(),
                    getCoverInfoAtSide(ForgeDirection.UP).getCoverID(),
                    getCoverInfoAtSide(ForgeDirection.NORTH).getCoverID(),
                    getCoverInfoAtSide(ForgeDirection.SOUTH).getCoverID(),
                    getCoverInfoAtSide(ForgeDirection.WEST).getCoverID(),
                    getCoverInfoAtSide(ForgeDirection.EAST).getCoverID(),
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

        setCoverIDAtSide(ForgeDirection.DOWN, aCover0);
        setCoverIDAtSide(ForgeDirection.UP, aCover1);
        setCoverIDAtSide(ForgeDirection.NORTH, aCover2);
        setCoverIDAtSide(ForgeDirection.SOUTH, aCover3);
        setCoverIDAtSide(ForgeDirection.WEST, aCover4);
        setCoverIDAtSide(ForgeDirection.EAST, aCover5);

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
            if (hasTimeStatisticsStarted) {
                double tAverageTime = 0;
                double tWorstTime = 0;
                int amountOfZero = 0;
                for (int tTime : mTimeStatistics) {
                    tAverageTime += tTime;
                    if (tTime > tWorstTime) {
                        tWorstTime = tTime;
                    }
                    if (tTime == 0) {
                        amountOfZero += 1;
                    }
                }
                // tick time zero means it has not been updated yet
                int samples = mTimeStatistics.length - amountOfZero;
                if (samples > 0) {
                    tList.add(
                        "Average CPU-load of ~" + (tAverageTime / samples)
                            + "ns since "
                            + samples
                            + " ticks with worst time of "
                            + tWorstTime
                            + "ns.");
                }
            } else {
                startTimeStatistics();
                tList.add("Just started tick time statistics.");
            }
            if (mLagWarningCount > 0) {
                tList.add(
                    "Caused " + (mLagWarningCount >= 10 ? "more than 10" : mLagWarningCount)
                        + " Lag Spike Warnings (anything taking longer than "
                        + GregTechAPI.MILLISECOND_THRESHOLD_UNTIL_LAG_WARNING
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
    public ForgeDirection getBackFacing() {
        return getFrontFacing().getOpposite();
    }

    @Override
    public ForgeDirection getFrontFacing() {
        return ForgeDirection.UNKNOWN;
    }

    @Override
    public void setFrontFacing(ForgeDirection aFacing) {
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
            .setInventorySlotContents(aIndex, worldObj.isRemote ? aStack : GTOreDictUnificator.setStack(true, aStack));
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
    public void openInventory() {
        /* Do nothing */
    }

    @Override
    public void closeInventory() {
        /* Do nothing */
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer aPlayer) {
        return hasValidMetaTileEntity() && mTickTimer > 1
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
    public boolean inputEnergyFrom(ForgeDirection side) {
        return false;
    }

    @Override
    public boolean inputEnergyFrom(ForgeDirection side, boolean waitForActive) {
        return false;
    }

    @Override
    public boolean outputsEnergyTo(ForgeDirection side) {
        return false;
    }

    @Override
    public boolean outputsEnergyTo(ForgeDirection side, boolean waitForActive) {
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
    public ITexture[] getTexture(Block aBlock, ForgeDirection side) {
        final ITexture rIcon = getCoverTexture(side);
        if (rIcon != null) return new ITexture[] { rIcon };
        return getTextureUncovered(side);
    }

    @Override
    public ITexture[] getTextureCovered(ForgeDirection side) {
        final ITexture coverTexture = getCoverTexture(side);
        final ITexture[] textureUncovered = getTextureUncovered(side);
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
    public ITexture[] getTextureUncovered(ForgeDirection sideDirection) {
        if ((mConnections & IConnectable.HAS_FRESHFOAM) != 0) return Textures.BlockIcons.FRESHFOAM;
        if ((mConnections & IConnectable.HAS_HARDENEDFOAM) != 0) return Textures.BlockIcons.HARDENEDFOAMS[mColor];
        if ((mConnections & IConnectable.HAS_FOAM) != 0) return Textures.BlockIcons.ERROR_RENDERING;
        int tConnections = mConnections;
        if (tConnections == IConnectable.CONNECTED_WEST || tConnections == IConnectable.CONNECTED_EAST)
            tConnections = IConnectable.CONNECTED_WEST | IConnectable.CONNECTED_EAST;
        else if (tConnections == IConnectable.CONNECTED_DOWN || tConnections == IConnectable.CONNECTED_UP)
            tConnections = IConnectable.CONNECTED_DOWN | IConnectable.CONNECTED_UP;
        else if (tConnections == IConnectable.CONNECTED_NORTH || tConnections == IConnectable.CONNECTED_SOUTH)
            tConnections = IConnectable.CONNECTED_NORTH | IConnectable.CONNECTED_SOUTH;
        if (hasValidMetaTileEntity()) return mMetaTileEntity.getTexture(
            this,
            sideDirection,
            tConnections,
            mColor - 1,
            tConnections == 0 || (tConnections & sideDirection.flag) != 0,
            getOutputRedstoneSignal(sideDirection) > 0);
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
        final ItemStack rStack = new ItemStack(GregTechAPI.sBlockMachines, 1, mID);
        final NBTTagCompound tNBT = new NBTTagCompound();

        writeCoverNBT(tNBT, true);

        if (hasValidMetaTileEntity()) mMetaTileEntity.setItemNBT(tNBT);
        if (!tNBT.hasNoTags()) rStack.setTagCompound(tNBT);

        onBaseTEDestroyed();
        return new ArrayList<>(Collections.singletonList(rStack));
    }

    @Override
    public boolean shouldDropItemAt(int index) {
        return this.mMetaTileEntity == null || this.mMetaTileEntity.shouldDropItemAt(index);
    }

    @Override
    public boolean onRightclick(EntityPlayer aPlayer, ForgeDirection side, float aX, float aY, float aZ) {
        if (isClientSide()) {
            // Configure Cover, sneak can also be: screwdriver, wrench, side cutter, soldering iron
            if (aPlayer.isSneaking()) {
                final ForgeDirection tSide = (getCoverIDAtSide(side) == 0)
                    ? GTUtility.determineWrenchingSide(side, aX, aY, aZ)
                    : side;
                return (getCoverInfoAtSide(tSide).hasCoverGUI());
            } else if (getCoverBehaviorAtSideNew(side).onCoverRightclickClient(side, this, aPlayer, aX, aY, aZ)) {
                return true;
            }
        }
        if (isServerSide()) {
            final ItemStack tCurrentItem = aPlayer.inventory.getCurrentItem();
            if (tCurrentItem != null) {
                if (getColorization() >= 0
                    && GTUtility.areStacksEqual(new ItemStack(Items.water_bucket, 1), tCurrentItem)) {
                    mMetaTileEntity.markDirty();
                    tCurrentItem.func_150996_a(Items.bucket);
                    setColorization((byte) -1);
                    return true;
                }
                final ForgeDirection tSide = GTUtility.determineWrenchingSide(side, aX, aY, aZ);
                if (GTUtility.isStackInList(tCurrentItem, GregTechAPI.sWrenchList)) {

                    if (mMetaTileEntity.onWrenchRightClick(side, tSide, aPlayer, aX, aY, aZ, tCurrentItem)) {
                        mMetaTileEntity.markDirty();
                        GTModHandler.damageOrDechargeItem(tCurrentItem, 1, 1000, aPlayer);
                        GTUtility.sendSoundToPlayers(
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
                if (GTUtility.isStackInList(tCurrentItem, GregTechAPI.sScrewdriverList)) {
                    if (getCoverIDAtSide(side) == 0 && getCoverIDAtSide(tSide) != 0) {
                        if (GTModHandler.damageOrDechargeItem(tCurrentItem, 1, 200, aPlayer)) {
                            setCoverDataAtSide(
                                tSide,
                                getCoverInfoAtSide(tSide).onCoverScrewdriverClick(aPlayer, 0.5F, 0.5F, 0.5F));
                            mMetaTileEntity.onScrewdriverRightClick(tSide, aPlayer, aX, aY, aZ, tCurrentItem);
                            mMetaTileEntity.markDirty();
                            GTUtility.sendSoundToPlayers(
                                worldObj,
                                SoundResource.IC2_TOOLS_WRENCH,
                                1.0F,
                                -1,
                                xCoord,
                                yCoord,
                                zCoord);
                        }
                    } else {
                        if (GTModHandler.damageOrDechargeItem(tCurrentItem, 1, 1000, aPlayer)) {
                            setCoverDataAtSide(
                                side,
                                getCoverInfoAtSide(side).onCoverScrewdriverClick(aPlayer, aX, aY, aZ));
                            mMetaTileEntity.onScrewdriverRightClick(side, aPlayer, aX, aY, aZ, tCurrentItem);
                            mMetaTileEntity.markDirty();
                            GTUtility.sendSoundToPlayers(
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

                if (GTUtility.isStackInList(tCurrentItem, GregTechAPI.sHardHammerList)) {
                    return true;
                }

                if (GTUtility.isStackInList(tCurrentItem, GregTechAPI.sSoftHammerList)) {
                    if (GTModHandler.damageOrDechargeItem(tCurrentItem, 1, 1000, aPlayer)) {
                        if (mWorks) disableWorking();
                        else enableWorking();
                        mMetaTileEntity.markDirty();
                        GTUtility.sendChatToPlayer(
                            aPlayer,
                            GTUtility.trans("090", "Machine Processing: ")
                                + (isAllowedToWork() ? GTUtility.trans("088", "Enabled")
                                    : GTUtility.trans("087", "Disabled")));
                        GTUtility.sendSoundToPlayers(
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

                if (GTUtility.isStackInList(tCurrentItem, GregTechAPI.sWireCutterList)) {
                    if (mMetaTileEntity.onWireCutterRightClick(side, tSide, aPlayer, aX, aY, aZ, tCurrentItem)) {
                        mMetaTileEntity.markDirty();
                        // logic handled internally
                        GTUtility.sendSoundToPlayers(
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

                if (GTUtility.isStackInList(tCurrentItem, GregTechAPI.sSolderingToolList)) {
                    if (mMetaTileEntity.onSolderingToolRightClick(side, tSide, aPlayer, aX, aY, aZ, tCurrentItem)) {
                        mMetaTileEntity.markDirty();
                        // logic handled internally
                        GTUtility.sendSoundToPlayers(
                            worldObj,
                            SoundResource.IC2_TOOLS_BATTERY_USE,
                            1.0F,
                            -1,
                            xCoord,
                            yCoord,
                            zCoord);
                    } else if (GTModHandler.useSolderingIron(tCurrentItem, aPlayer)) {
                        mMetaTileEntity.markDirty();
                        mStrongRedstone ^= tSide.flag;
                        GTUtility.sendChatToPlayer(
                            aPlayer,
                            GTUtility.trans("091", "Redstone Output at Side ") + tSide
                                + GTUtility.trans("092", " set to: ")
                                + ((mStrongRedstone & tSide.flag) != 0 ? GTUtility.trans("093", "Strong")
                                    : GTUtility.trans("094", "Weak")));
                        GTUtility.sendSoundToPlayers(
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

                ForgeDirection coverSide = side;
                if (getCoverIDAtSide(side) == 0) coverSide = tSide;

                final CoverInfo coverInfo = getCoverInfoAtSide(coverSide);

                if (coverInfo.getCoverID() == 0) {
                    if (GTUtility.isStackInList(tCurrentItem, GregTechAPI.sCovers.keySet())) {
                        final CoverBehaviorBase<?> coverBehavior = GregTechAPI.getCoverBehaviorNew(tCurrentItem);
                        if (coverBehavior.isCoverPlaceable(coverSide, tCurrentItem, this)
                            && mMetaTileEntity.allowCoverOnSide(coverSide, new GTItemStack(tCurrentItem))) {

                            setCoverItemAtSide(coverSide, tCurrentItem);
                            coverBehavior.onPlayerAttach(aPlayer, tCurrentItem, this, side);

                            mMetaTileEntity.markDirty();
                            if (!aPlayer.capabilities.isCreativeMode) tCurrentItem.stackSize--;
                            GTUtility.sendSoundToPlayers(
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
                    if (GTUtility.isStackInList(tCurrentItem, GregTechAPI.sCrowbarList)) {
                        if (GTModHandler.damageOrDechargeItem(tCurrentItem, 1, 1000, aPlayer)) {
                            GTUtility.sendSoundToPlayers(
                                worldObj,
                                SoundResource.RANDOM_BREAK,
                                1.0F,
                                -1,
                                xCoord,
                                yCoord,
                                zCoord);
                            dropCover(coverSide, side, false);
                            mMetaTileEntity.markDirty();
                        }
                        return true;
                    }
                }
            } else if (aPlayer.isSneaking()) { // Sneak click, no tool -> open cover config or turn back.
                side = (getCoverIDAtSide(side) == 0) ? GTUtility.determineWrenchingSide(side, aX, aY, aZ) : side;
                final CoverInfo coverInfo = getCoverInfoAtSide(side);
                return coverInfo.isValid() && coverInfo.onCoverShiftRightClick(aPlayer);
            }

            if (getCoverInfoAtSide(side).onCoverRightClick(aPlayer, aX, aY, aZ)) return true;
        }

        if (!getCoverInfoAtSide(side).isGUIClickable()) return false;

        try {
            if (!aPlayer.isSneaking() && hasValidMetaTileEntity()) {
                final boolean handled = mMetaTileEntity.onRightclick(this, aPlayer, side, aX, aY, aZ);
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
    public int[] getAccessibleSlotsFromSide(int ordinalSide) {
        final CoverInfo coverInfo = getCoverInfoAtSide(ForgeDirection.getOrientation(ordinalSide));
        if (canAccessData() && (coverInfo.letsItemsOut(-1) || coverInfo.letsItemsIn(-1)))
            return mMetaTileEntity.getAccessibleSlotsFromSide(ordinalSide);
        return GTValues.emptyIntArray;
    }

    /**
     * Can put aStack into Slot at Side
     */
    @Override
    public boolean canInsertItem(int aIndex, ItemStack aStack, int ordinalSide) {
        return canAccessData() && getCoverInfoAtSide(ForgeDirection.getOrientation(ordinalSide)).letsItemsIn(aIndex)
            && mMetaTileEntity.canInsertItem(aIndex, aStack, ordinalSide);
    }

    /**
     * Can pull aStack out of Slot from Side
     */
    @Override
    public boolean canExtractItem(int aIndex, ItemStack aStack, int ordinalSide) {
        final ForgeDirection side = ForgeDirection.getOrientation(ordinalSide);
        return canAccessData()
            && getCoverBehaviorAtSideNew(side)
                .letsItemsOut(side, getCoverIDAtSide(side), getComplexCoverDataAtSide(side), aIndex, this)
            && mMetaTileEntity.canExtractItem(aIndex, aStack, ordinalSide);
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
        return GTUtility.defaultUuid;
    }

    @Override
    public void setOwnerUuid(UUID uuid) {}

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
        if (canAccessData()) return mMetaTileEntity.injectEnergyUnits(side, aVoltage, aAmperage);
        return 0;
    }

    @Override
    public boolean drainEnergyUnits(ForgeDirection side, long aVoltage, long aAmperage) {
        return false;
    }

    @Override
    public boolean acceptsRotationalEnergy(ForgeDirection side) {
        if (!canAccessData() || getCoverIDAtSide(side) != 0) return false;
        return mMetaTileEntity.acceptsRotationalEnergy(side);
    }

    @Override
    public boolean injectRotationalEnergy(ForgeDirection side, long aSpeed, long aEnergy) {
        if (!canAccessData() || getCoverIDAtSide(side) != 0) return false;
        return mMetaTileEntity.injectRotationalEnergy(side, aSpeed, aEnergy);
    }

    private boolean canMoveFluidOnSide(ForgeDirection side, Fluid fluid, boolean isFill) {
        if (side == ForgeDirection.UNKNOWN) return true;

        final IFluidHandler tTileEntity = getITankContainerAtSide(side);
        // Only require a connection if there's something to connect to - Allows fluid cells & buckets to interact with
        // the pipe
        if (tTileEntity != null && !mMetaTileEntity.isConnectedAtSide(side)) return false;

        if (isFill && mMetaTileEntity.isLiquidInput(side) && getCoverInfoAtSide(side).letsFluidIn(fluid)) return true;

        return !isFill && mMetaTileEntity.isLiquidOutput(side) && getCoverInfoAtSide(side).letsFluidOut(fluid);
    }

    @Override
    public int fill(ForgeDirection side, FluidStack aFluidStack, boolean doFill) {
        if (mTickTimer > 5 && canAccessData()
            && canMoveFluidOnSide(side, aFluidStack == null ? null : aFluidStack.getFluid(), true))
            return mMetaTileEntity.fill(side, aFluidStack, doFill);
        return 0;
    }

    @Override
    public FluidStack drain(ForgeDirection side, int maxDrain, boolean doDrain) {
        if (mTickTimer > 5 && canAccessData()
            && canMoveFluidOnSide(
                side,
                mMetaTileEntity.getFluid() == null ? null
                    : mMetaTileEntity.getFluid()
                        .getFluid(),
                false))
            return mMetaTileEntity.drain(side, maxDrain, doDrain);
        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection side, FluidStack aFluidStack, boolean doDrain) {
        if (mTickTimer > 5 && canAccessData()
            && canMoveFluidOnSide(side, aFluidStack == null ? null : aFluidStack.getFluid(), false))
            return mMetaTileEntity.drain(side, aFluidStack, doDrain);
        return null;
    }

    @Override
    public boolean canFill(ForgeDirection side, Fluid aFluid) {
        if (mTickTimer > 5 && canAccessData() && canMoveFluidOnSide(side, aFluid, true))
            return mMetaTileEntity.canFill(side, aFluid);
        return false;
    }

    @Override
    public boolean canDrain(ForgeDirection side, Fluid aFluid) {
        if (mTickTimer > 5 && canAccessData() && canMoveFluidOnSide(side, aFluid, false))
            return mMetaTileEntity.canDrain(side, aFluid);
        return false;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection side) {
        final CoverInfo coverInfo = getCoverInfoAtSide(side);
        if (canAccessData()
            && (side == ForgeDirection.UNKNOWN || (mMetaTileEntity.isLiquidInput(side) && coverInfo.letsFluidIn(null))
                || (mMetaTileEntity.isLiquidOutput(side) && coverInfo.letsFluidOut(null))
            // Doesn't need to be connected to get Tank Info -- otherwise things can't connect
            )) return mMetaTileEntity.getTankInfo(side);
        return new FluidTankInfo[] {};
    }

    @Override
    public boolean addStackToSlot(int aIndex, ItemStack aStack) {
        if (GTUtility.isStackInvalid(aStack)) return true;
        if (aIndex < 0 || aIndex >= getSizeInventory()) return false;
        final ItemStack tStack = getStackInSlot(aIndex);
        if (GTUtility.isStackInvalid(tStack)) {
            setInventorySlotContents(aIndex, aStack);
            return true;
        }
        aStack = GTOreDictUnificator.get(aStack);
        if (GTUtility.areStacksEqual(tStack, aStack)
            && tStack.stackSize + aStack.stackSize <= Math.min(aStack.getMaxStackSize(), getInventoryStackLimit())) {
            markDirty();
            tStack.stackSize += aStack.stackSize;
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
    public float getThickNess() {
        if (canAccessData()) return mMetaTileEntity.getThickNess();
        return 1.0F;
    }

    public boolean renderInside(ForgeDirection side) {
        if (canAccessData()) return mMetaTileEntity.renderInside(side);
        return false;
    }

    @Override
    public float getBlastResistance(ForgeDirection side) {
        return canAccessData() ? Math.max(0, getMetaTileEntity().getExplosionResistance(side)) : 5.0F;
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
        if (canAccessData()) return getMetaTileEntity().getInfoData();
        return new String[] {};
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

    @Override
    public int[] getTimeStatistics() {
        return mTimeStatistics;
    }

    @Override
    public void startTimeStatistics() {
        hasTimeStatisticsStarted = true;
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        mMetaTileEntity.getWailaBody(itemStack, currentTip, accessor, config);
    }
}
