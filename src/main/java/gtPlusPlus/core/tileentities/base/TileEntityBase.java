package gtPlusPlus.core.tileentities.base;

import java.util.Arrays;
import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.IFluidHandler;

import gregtech.GTMod;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.interfaces.IDescribable;
import gregtech.api.interfaces.tileentity.IBasicEnergyContainer;
import gregtech.api.interfaces.tileentity.IGregTechDeviceInformation;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IHasInventory;
import gregtech.api.interfaces.tileentity.IRedstoneTileEntity;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.common.data.GTBlockEventTracker;
import gregtech.common.pollution.Pollution;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.minecraft.BTF_Inventory;
import ic2.api.Direction;

public class TileEntityBase extends TileEntity
    implements IRedstoneTileEntity, IHasInventory, IBasicEnergyContainer, IGregTechDeviceInformation, IDescribable {

    private String customName;
    public String mOwnerName = "null";
    public String mOwnerUUID = "null";
    private boolean mIsOwnerOP = false;

    public final BTF_Inventory mInventory;

    public TileEntityBase(int aCapacity) {
        mInventory = new BTF_Inventory(aCapacity, this);
    }

    public NBTTagCompound getTag(final NBTTagCompound nbt, final String tag) {
        if (!nbt.hasKey(tag)) {
            nbt.setTag(tag, new NBTTagCompound());
        }
        return nbt.getCompoundTag(tag);
    }

    @Override
    public void writeToNBT(final NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        if (this.hasCustomInventoryName()) {
            nbt.setString("CustomName", this.getCustomName());
        }
        nbt.setBoolean("mIsOwnerOP", this.mIsOwnerOP);
        nbt.setString("mOwnerName", this.mOwnerName);
        nbt.setString("mOwnerUUID", this.mOwnerUUID);
    }

    @Override
    public void readFromNBT(final NBTTagCompound nbt) {

        super.readFromNBT(nbt);

        if (nbt.hasKey("CustomName", 8)) {
            this.setCustomName(nbt.getString("CustomName"));
        }

        this.mIsOwnerOP = nbt.getBoolean("mIsOwnerOP");
        this.mOwnerName = nbt.getString("mOwnerName");
        this.mOwnerUUID = nbt.getString("mOwnerUUID");
    }

    @Override
    public void updateEntity() {
        long aTick = System.currentTimeMillis();
        this.isDead = false;
        if (!firstTicked) {
            onFirstTick();
        }
        try {
            if (this.isServerSide()) {
                onPreTick(aTick);
            }
        } catch (Exception t) {
            Logger.ERROR("Tile Entity Encountered an error in it's pre-tick stage.");
            t.printStackTrace();
        }
        try {
            if (this.isServerSide()) {
                onTick(aTick);
            }
        } catch (Exception t) {
            Logger.ERROR("Tile Entity Encountered an error in it's tick stage.");
            t.printStackTrace();
        }
        try {
            if (this.isServerSide()) {
                onPostTick(aTick);
            }
        } catch (Exception t) {
            Logger.ERROR("Tile Entity Encountered an error in it's post-tick stage.");
            t.printStackTrace();
        }
    }

    public boolean onPreTick(long aTick) {
        return true;
    }

    public boolean onTick(long aTick) {
        try {
            if (this.isServerSide()) {
                processRecipe();
            }
        } catch (Exception t) {
            Logger.ERROR("Tile Entity Encountered an error in it's processing of a recipe stage.");
            t.printStackTrace();
        }
        return true;
    }

    public boolean onPostTick(long aTick) {
        return true;
    }

    public boolean processRecipe() {
        return true;
    }

    public String getOwner() {
        if (this.mOwnerName == null) {
            return "null";
        }
        return this.mOwnerName;
    }

    public UUID getOwnerUUID() {
        return UUID.fromString(this.mOwnerUUID);
    }

    public boolean isOwnerOP() {
        return mIsOwnerOP;
    }

    public void setOwnerInformation(String mName, String mUUID, boolean mOP) {
        if (isServerSide()) {
            if (this.mOwnerName == null || this.mOwnerUUID == null
                || this.mOwnerName.equals("null")
                || this.mOwnerUUID.equals("null")) {
                this.mOwnerName = mName;
                this.mOwnerUUID = mUUID;
                this.mIsOwnerOP = mOP;
            }
        }
    }

    @Override
    public boolean isServerSide() {
        if (this.hasWorldObj()) {
            return !this.getWorldObj().isRemote;
        }
        return false;
    }

    @Override
    public final boolean isClientSide() {
        return this.worldObj.isRemote;
    }

    public String getCustomName() {
        return this.customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    @Override
    public String getInventoryName() {
        return this.hasCustomInventoryName() ? this.customName : "container.tileentity.name";
    }

    @Override
    public boolean hasCustomInventoryName() {
        return this.customName != null && !this.customName.isEmpty();
    }

    @Override
    public int getSizeInventory() {
        return this.mInventory.getSizeInventory();
    }

    @Override
    public ItemStack getStackInSlot(int aIndex) {
        return this.mInventory.getStackInSlot(aIndex);
    }

    @Override
    public ItemStack decrStackSize(int aIndex, int aAmount) {
        if (canAccessData()) {
            mInventoryChanged = true;
            return mInventory.decrStackSize(aIndex, aAmount);
        }
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
        return this.mInventory.getStackInSlotOnClosing(p_70304_1_);
    }

    @Override
    public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
        this.mInventory.setInventorySlotContents(p_70299_1_, p_70299_2_);
    }

    @Override
    public int getInventoryStackLimit() {
        return this.mInventory.getInventoryStackLimit();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
        return this.mInventory.isUseableByPlayer(p_70300_1_);
    }

    @Override
    public void openInventory() {
        this.mInventory.openInventory();
    }

    @Override
    public void closeInventory() {
        this.mInventory.closeInventory();
    }

    /**
     * Can put aStack into Slot
     */
    @Override
    public boolean isItemValidForSlot(int aIndex, ItemStack aStack) {
        return canAccessData() && mInventory.isItemValidForSlot(aIndex, aStack);
    }

    /**
     * returns all valid Inventory Slots, no matter which Side (Unless it's covered). The Side Stuff is done in the
     * following two Functions.
     */
    @Override
    public int[] getAccessibleSlotsFromSide(int ordinalSide) {
        if (canAccessData()) return mInventory.getAccessibleSlotsFromSide(ordinalSide);
        return GTValues.emptyIntArray;
    }

    /**
     * Can put aStack into Slot at Side
     */
    @Override
    public boolean canInsertItem(int aIndex, ItemStack aStack, int ordinalSide) {
        return canAccessData() && (mRunningThroughTick || !mInputDisabled)
            && mInventory.canInsertItem(aIndex, aStack, ordinalSide);
    }

    /**
     * Can pull aStack out of Slot from Side
     */
    @Override
    public boolean canExtractItem(int aIndex, ItemStack aStack, int ordinalSide) {
        return canAccessData() && (mRunningThroughTick || !mOutputDisabled)
            && mInventory.canExtractItem(aIndex, aStack, ordinalSide);
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return this.canAccessData() && this.mInventory.isValidSlot(aIndex);
    }

    protected TileEntityBase mMetaTileEntity;
    protected long mStoredEnergy = 0;
    protected int mAverageEUInputIndex = 0, mAverageEUOutputIndex = 0;
    protected boolean mReleaseEnergy = false;
    protected int[] mAverageEUInput = new int[11], mAverageEUOutput = new int[11];
    private final boolean[] mActiveEUInputs = new boolean[] { false, false, false, false, false, false };
    private final boolean[] mActiveEUOutputs = new boolean[] { false, false, false, false, false, false };
    private final byte[] mSidedRedstone = new byte[] { 0, 0, 0, 0, 0, 0 };
    private final int[] mTimeStatistics = new int[GregTechAPI.TICKS_FOR_LAG_AVERAGING];
    private boolean mHasEnoughEnergy = true;
    protected boolean mRunningThroughTick = false;
    protected boolean mInputDisabled = false;
    protected boolean mOutputDisabled = false;
    private final boolean mMuffler = false;
    private final boolean mLockUpgrade = false;
    private final boolean mActive = false;
    private boolean mRedstone = false;
    private final boolean mWorkUpdate = false;
    private boolean mInventoryChanged = false;
    private final boolean mWorks = true;
    private final boolean mNeedsUpdate = true;
    private final boolean mNeedsBlockUpdate = true;
    private boolean mSendClientData = false;
    private final boolean oRedstone = false;
    private final boolean mEnergyStateReady = false;
    private final byte mColor = 0;
    private final byte oColor = 0;
    private byte mStrongRedstone = 0;
    private final byte oRedstoneData = 63;
    private final byte oTextureData = 0;
    private final byte oUpdateData = 0;
    private final byte oTexturePage = 0;
    private final byte oLightValueClient = -1;
    private final byte oLightValue = -1;
    private byte mLightValue = 0;
    private final byte mOtherUpgrades = 0;
    private final byte mFacing = 0;
    private final byte oFacing = 0;
    private final byte mWorkData = 0;
    private final int mDisplayErrorCode = 0;
    private final int oX = 0;
    private final int oY = 0;
    private final int oZ = 0;
    private final int mTimeStatisticsIndex = 0;
    private final int mLagWarningCount = 0;
    private final short mID = 0;
    protected long mTickTimer = 0;
    private final long oOutput = 0;
    private long mAcceptedAmperes = Long.MAX_VALUE;

    /**
     * Cover Support
     */
    public void issueClientUpdate() {
        this.mSendClientData = true;
    }

    protected final boolean canAccessData() {
        return !isDead() && !this.isInvalid();
    }

    @Override
    public void issueBlockUpdate() {
        super.markDirty();
    }

    @Override
    public long getTimer() {
        return this.mTickTimer;
    }

    @Override
    public long getOutputAmperage() {
        return this.canAccessData() && this.mMetaTileEntity.isElectric() ? this.mMetaTileEntity.maxAmperesOut() : 0L;
    }

    @Override
    public long getOutputVoltage() {
        return this.canAccessData() && this.mMetaTileEntity.isElectric() && this.mMetaTileEntity.isEnetOutput()
            ? this.mMetaTileEntity.maxEUOutput()
            : 0L;
    }

    @Override
    public long getInputAmperage() {
        return this.canAccessData() && this.mMetaTileEntity.isElectric() ? this.mMetaTileEntity.maxAmperesIn() : 0L;
    }

    @Override
    public long getInputVoltage() {
        return this.canAccessData() && this.mMetaTileEntity.isElectric() ? this.mMetaTileEntity.maxEUInput()
            : 2147483647L;
    }

    @Override
    public boolean decreaseStoredEnergyUnits(long aEnergy, boolean aIgnoreTooLessEnergy) {
        return this.canAccessData() && (this.mHasEnoughEnergy = this.decreaseStoredEU(aEnergy, aIgnoreTooLessEnergy));
    }

    @Override
    public boolean increaseStoredEnergyUnits(long aEnergy, boolean aIgnoreTooMuchEnergy) {
        if (!this.canAccessData()) {
            return false;
        } else if (this.getStoredEU() >= this.getEUCapacity() && !aIgnoreTooMuchEnergy) {
            return false;
        } else {
            this.setStoredEU(this.mMetaTileEntity.getEUVar() + aEnergy);
            return true;
        }
    }

    @Override
    public boolean inputEnergyFrom(ForgeDirection side) {
        return side == ForgeDirection.UNKNOWN || (!this.isServerSide() ? this.isEnergyInputSide(side)
            : side != ForgeDirection.UNKNOWN && this.mActiveEUInputs[side.ordinal()] && !this.mReleaseEnergy);
    }

    @Override
    public boolean outputsEnergyTo(ForgeDirection side) {
        return side == ForgeDirection.UNKNOWN || (!this.isServerSide() ? this.isEnergyOutputSide(side)
            : side != ForgeDirection.UNKNOWN && this.mActiveEUOutputs[side.ordinal()] || this.mReleaseEnergy);
    }

    private boolean isEnergyInputSide(ForgeDirection side) {
        if (side != ForgeDirection.UNKNOWN) {
            if (this.isInvalid() || this.mReleaseEnergy) {
                return false;
            }

            if (this.canAccessData() && this.mMetaTileEntity.isElectric() && this.mMetaTileEntity.isEnetInput()) {
                return this.mMetaTileEntity.isInputFacing(side);
            }
        }

        return false;
    }

    private boolean isEnergyOutputSide(ForgeDirection side) {
        if (side != ForgeDirection.UNKNOWN) {
            if (this.isInvalid() || this.mReleaseEnergy) {
                return this.mReleaseEnergy;
            }

            if (this.canAccessData() && this.mMetaTileEntity.isElectric() && this.mMetaTileEntity.isEnetOutput()) {
                return this.mMetaTileEntity.isOutputFacing(side);
            }
        }

        return false;
    }

    public boolean isOutputFacing(ForgeDirection side) {
        return false;
    }

    public boolean isInputFacing(ForgeDirection side) {
        return false;
    }

    private final TileEntity[] mBufferedTileEntities = new TileEntity[6];
    public boolean ignoreUnloadedChunks = true;
    public boolean isDead = false;

    private void clearNullMarkersFromTileEntityBuffer() {
        for (int i = 0; i < this.mBufferedTileEntities.length; ++i) {
            if (this.mBufferedTileEntities[i] == this) {
                this.mBufferedTileEntities[i] = null;
            }
        }
    }

    protected final void clearTileEntityBuffer() {
        Arrays.fill(this.mBufferedTileEntities, null);
    }

    @Override
    public final World getWorld() {
        return this.worldObj;
    }

    @Override
    public final int getXCoord() {
        return this.xCoord;
    }

    @Override
    public final short getYCoord() {
        return (short) this.yCoord;
    }

    @Override
    public final int getZCoord() {
        return this.zCoord;
    }

    @Override
    public final int getOffsetX(ForgeDirection side, int aMultiplier) {
        return this.xCoord + side.offsetX * aMultiplier;
    }

    @Override
    public final short getOffsetY(ForgeDirection side, int aMultiplier) {
        return (short) (this.yCoord + side.offsetY * aMultiplier);
    }

    @Override
    public final int getOffsetZ(ForgeDirection side, int aMultiplier) {
        return this.zCoord + side.offsetZ * aMultiplier;
    }

    @Override
    public final int getRandomNumber(int aRange) {
        return this.worldObj.rand.nextInt(aRange);
    }

    @Override
    public final BiomeGenBase getBiome(int aX, int aZ) {
        return this.worldObj.getBiomeGenForCoords(aX, aZ);
    }

    @Override
    public final BiomeGenBase getBiome() {
        return this.getBiome(this.xCoord, this.zCoord);
    }

    @Override
    public final Block getBlockOffset(int aX, int aY, int aZ) {
        return this.getBlock(this.xCoord + aX, this.yCoord + aY, this.zCoord + aZ);
    }

    @Override
    public final Block getBlockAtSide(ForgeDirection side) {
        return this.getBlockAtSideAndDistance(side, 1);
    }

    @Override
    public final Block getBlockAtSideAndDistance(ForgeDirection side, int aDistance) {
        return this.getBlock(
            this.getOffsetX(side, aDistance),
            this.getOffsetY(side, aDistance),
            this.getOffsetZ(side, aDistance));
    }

    @Override
    public final int getMetaIDOffset(int aX, int aY, int aZ) {
        return this.getMetaID(this.xCoord + aX, this.yCoord + aY, this.zCoord + aZ);
    }

    @Override
    public final int getMetaIDAtSide(ForgeDirection side) {
        return this.getMetaIDAtSideAndDistance(side, 1);
    }

    @Override
    public final int getMetaIDAtSideAndDistance(ForgeDirection side, int aDistance) {
        return this.getMetaID(
            this.getOffsetX(side, aDistance),
            this.getOffsetY(side, aDistance),
            this.getOffsetZ(side, aDistance));
    }

    @Override
    public final byte getLightLevelOffset(int aX, int aY, int aZ) {
        return this.getLightLevel(this.xCoord + aX, this.yCoord + aY, this.zCoord + aZ);
    }

    @Override
    public final byte getLightLevelAtSide(ForgeDirection side) {
        return this.getLightLevelAtSideAndDistance(side, 1);
    }

    @Override
    public final byte getLightLevelAtSideAndDistance(ForgeDirection side, int aDistance) {
        return this.getLightLevel(
            this.getOffsetX(side, aDistance),
            this.getOffsetY(side, aDistance),
            this.getOffsetZ(side, aDistance));
    }

    @Override
    public final boolean getOpacityOffset(int aX, int aY, int aZ) {
        return this.getOpacity(this.xCoord + aX, this.yCoord + aY, this.zCoord + aZ);
    }

    @Override
    public final boolean getOpacityAtSide(ForgeDirection side) {
        return this.getOpacityAtSideAndDistance(side, 1);
    }

    @Override
    public final boolean getOpacityAtSideAndDistance(ForgeDirection side, int aDistance) {
        return this.getOpacity(
            this.getOffsetX(side, aDistance),
            this.getOffsetY(side, aDistance),
            this.getOffsetZ(side, aDistance));
    }

    @Override
    public final boolean getSkyOffset(int aX, int aY, int aZ) {
        return this.getSky(this.xCoord + aX, this.yCoord + aY, this.zCoord + aZ);
    }

    @Override
    public final boolean getSkyAtSide(ForgeDirection side) {
        return this.getSkyAtSideAndDistance(side, 1);
    }

    @Override
    public final boolean getSkyAtSideAndDistance(ForgeDirection side, int aDistance) {
        return this.getSky(
            this.getOffsetX(side, aDistance),
            this.getOffsetY(side, aDistance),
            this.getOffsetZ(side, aDistance));
    }

    @Override
    public final boolean getAirOffset(int aX, int aY, int aZ) {
        return this.getAir(this.xCoord + aX, this.yCoord + aY, this.zCoord + aZ);
    }

    @Override
    public final boolean getAirAtSide(ForgeDirection side) {
        return this.getAirAtSideAndDistance(side, 1);
    }

    @Override
    public final boolean getAirAtSideAndDistance(ForgeDirection side, int aDistance) {
        return this.getAir(
            this.getOffsetX(side, aDistance),
            this.getOffsetY(side, aDistance),
            this.getOffsetZ(side, aDistance));
    }

    @Override
    public final TileEntity getTileEntityOffset(int aX, int aY, int aZ) {
        return this.getTileEntity(this.xCoord + aX, this.yCoord + aY, this.zCoord + aZ);
    }

    @Override
    public final TileEntity getTileEntityAtSideAndDistance(ForgeDirection side, int aDistance) {
        return aDistance == 1 ? this.getTileEntityAtSide(side)
            : this.getTileEntity(
                this.getOffsetX(side, aDistance),
                this.getOffsetY(side, aDistance),
                this.getOffsetZ(side, aDistance));
    }

    @Override
    public final IInventory getIInventory(int aX, int aY, int aZ) {
        TileEntity tTileEntity = this.getTileEntity(aX, aY, aZ);
        return tTileEntity instanceof IInventory ? (IInventory) tTileEntity : null;
    }

    @Override
    public final IInventory getIInventoryOffset(int aX, int aY, int aZ) {
        TileEntity tTileEntity = this.getTileEntityOffset(aX, aY, aZ);
        return tTileEntity instanceof IInventory ? (IInventory) tTileEntity : null;
    }

    @Override
    public final IInventory getIInventoryAtSide(ForgeDirection side) {
        TileEntity tTileEntity = this.getTileEntityAtSide(side);
        return tTileEntity instanceof IInventory ? (IInventory) tTileEntity : null;
    }

    @Override
    public final IInventory getIInventoryAtSideAndDistance(ForgeDirection side, int aDistance) {
        TileEntity tTileEntity = this.getTileEntityAtSideAndDistance(side, aDistance);
        return tTileEntity instanceof IInventory ? (IInventory) tTileEntity : null;
    }

    @Override
    public final IFluidHandler getITankContainer(int aX, int aY, int aZ) {
        TileEntity tTileEntity = this.getTileEntity(aX, aY, aZ);
        return tTileEntity instanceof IFluidHandler ? (IFluidHandler) tTileEntity : null;
    }

    @Override
    public final IFluidHandler getITankContainerOffset(int aX, int aY, int aZ) {
        TileEntity tTileEntity = this.getTileEntityOffset(aX, aY, aZ);
        return tTileEntity instanceof IFluidHandler ? (IFluidHandler) tTileEntity : null;
    }

    @Override
    public final IFluidHandler getITankContainerAtSide(ForgeDirection side) {
        TileEntity tTileEntity = this.getTileEntityAtSide(side);
        return tTileEntity instanceof IFluidHandler ? (IFluidHandler) tTileEntity : null;
    }

    @Override
    public final IFluidHandler getITankContainerAtSideAndDistance(ForgeDirection side, int aDistance) {
        TileEntity tTileEntity = this.getTileEntityAtSideAndDistance(side, aDistance);
        return tTileEntity instanceof IFluidHandler ? (IFluidHandler) tTileEntity : null;
    }

    @Override
    public final IGregTechTileEntity getIGregTechTileEntity(int aX, int aY, int aZ) {
        TileEntity tTileEntity = this.getTileEntity(aX, aY, aZ);
        return tTileEntity instanceof IGregTechTileEntity ? (IGregTechTileEntity) tTileEntity : null;
    }

    @Override
    public final IGregTechTileEntity getIGregTechTileEntityOffset(int aX, int aY, int aZ) {
        TileEntity tTileEntity = this.getTileEntityOffset(aX, aY, aZ);
        return tTileEntity instanceof IGregTechTileEntity ? (IGregTechTileEntity) tTileEntity : null;
    }

    @Override
    public final IGregTechTileEntity getIGregTechTileEntityAtSide(ForgeDirection side) {
        TileEntity tTileEntity = this.getTileEntityAtSide(side);
        return tTileEntity instanceof IGregTechTileEntity ? (IGregTechTileEntity) tTileEntity : null;
    }

    @Override
    public final IGregTechTileEntity getIGregTechTileEntityAtSideAndDistance(ForgeDirection side, int aDistance) {
        TileEntity tTileEntity = this.getTileEntityAtSideAndDistance(side, aDistance);
        return tTileEntity instanceof IGregTechTileEntity ? (IGregTechTileEntity) tTileEntity : null;
    }

    @Override
    public final Block getBlock(int aX, int aY, int aZ) {
        return this.ignoreUnloadedChunks && this.crossedChunkBorder(aX, aZ) && !this.worldObj.blockExists(aX, aY, aZ)
            ? Blocks.air
            : this.worldObj.getBlock(aX, aY, aZ);
    }

    @Override
    public final int getMetaID(int aX, int aY, int aZ) {
        return this.ignoreUnloadedChunks && this.crossedChunkBorder(aX, aZ) && !this.worldObj.blockExists(aX, aY, aZ)
            ? 0
            : this.worldObj.getBlockMetadata(aX, aY, aZ);
    }

    @Override
    public final byte getLightLevel(int aX, int aY, int aZ) {
        return this.ignoreUnloadedChunks && this.crossedChunkBorder(aX, aZ) && !this.worldObj.blockExists(aX, aY, aZ)
            ? 0
            : (byte) ((int) (this.worldObj.getLightBrightness(aX, aY, aZ) * 15.0F));
    }

    @Override
    public final boolean getSky(int aX, int aY, int aZ) {
        return this.ignoreUnloadedChunks && this.crossedChunkBorder(aX, aZ) && !this.worldObj.blockExists(aX, aY, aZ)
            || this.worldObj.canBlockSeeTheSky(aX, aY, aZ);
    }

    @Override
    public final boolean getOpacity(int aX, int aY, int aZ) {
        return (!this.ignoreUnloadedChunks || !this.crossedChunkBorder(aX, aZ) || this.worldObj.blockExists(aX, aY, aZ))
            && GTUtility.isOpaqueBlock(this.worldObj, aX, aY, aZ);
    }

    @Override
    public final boolean getAir(int aX, int aY, int aZ) {
        return this.ignoreUnloadedChunks && this.crossedChunkBorder(aX, aZ) && !this.worldObj.blockExists(aX, aY, aZ)
            || GTUtility.isBlockAir(this.worldObj, aX, aY, aZ);
    }

    @Override
    public final TileEntity getTileEntity(int aX, int aY, int aZ) {
        return this.ignoreUnloadedChunks && this.crossedChunkBorder(aX, aZ) && !this.worldObj.blockExists(aX, aY, aZ)
            ? null
            : this.worldObj.getTileEntity(aX, aY, aZ);
    }

    @Override
    public final TileEntity getTileEntityAtSide(ForgeDirection side) {
        final int ordinalSide = side.ordinal();
        if (side != ForgeDirection.UNKNOWN && this.mBufferedTileEntities[ordinalSide] != this) {
            int tX = this.getOffsetX(side, 1);
            short tY = this.getOffsetY(side, 1);
            int tZ = this.getOffsetZ(side, 1);
            if (this.crossedChunkBorder(tX, tZ)) {
                this.mBufferedTileEntities[ordinalSide] = null;
                if (this.ignoreUnloadedChunks && !this.worldObj.blockExists(tX, tY, tZ)) {
                    return null;
                }
            }

            if (this.mBufferedTileEntities[ordinalSide] == null) {
                this.mBufferedTileEntities[ordinalSide] = this.worldObj.getTileEntity(tX, tY, tZ);
                if (this.mBufferedTileEntities[ordinalSide] == null) {
                    this.mBufferedTileEntities[ordinalSide] = this;
                    return null;
                } else {
                    return this.mBufferedTileEntities[ordinalSide];
                }
            } else if (this.mBufferedTileEntities[ordinalSide].isInvalid()) {
                this.mBufferedTileEntities[ordinalSide] = null;
                return this.getTileEntityAtSide(side);
            } else {
                return this.mBufferedTileEntities[ordinalSide].xCoord == tX
                    && this.mBufferedTileEntities[ordinalSide].yCoord == tY
                    && this.mBufferedTileEntities[ordinalSide].zCoord == tZ ? this.mBufferedTileEntities[ordinalSide]
                        : null;
            }
        } else {
            return null;
        }
    }

    @Override
    public boolean isDead() {
        return this.isDead || this.isInvalidTileEntity();
    }

    @Override
    public void validate() {
        this.clearNullMarkersFromTileEntityBuffer();
        super.validate();
    }

    @Override
    public void invalidate() {
        this.clearNullMarkersFromTileEntityBuffer();
        super.invalidate();
    }

    @Override
    public void onChunkUnload() {
        this.clearNullMarkersFromTileEntityBuffer();
        super.onChunkUnload();
        this.isDead = true;
    }

    public final void onAdjacentBlockChange(int aX, int aY, int aZ) {
        this.clearNullMarkersFromTileEntityBuffer();
    }

    @Override
    public final void sendBlockEvent(byte aID, byte aValue) {
        GTBlockEventTracker.enqueue(worldObj, xCoord, yCoord, zCoord, aID, aValue);
    }

    private boolean crossedChunkBorder(int aX, int aZ) {
        return aX >> 4 != this.xCoord >> 4 || aZ >> 4 != this.zCoord >> 4;
    }

    public final void setOnFire() {
        GTUtility.setCoordsOnFire(this.worldObj, this.xCoord, this.yCoord, this.zCoord, false);
    }

    public final void setToFire() {
        this.worldObj.setBlock(this.xCoord, this.yCoord, this.zCoord, Blocks.fire);
    }

    @Override
    public byte getInputRedstoneSignal(ForgeDirection side) {
        return (byte) (worldObj
            .getIndirectPowerLevelTo(getOffsetX(side, 1), getOffsetY(side, 1), getOffsetZ(side, 1), side.ordinal())
            & 15);
    }

    @Override
    public byte getOutputRedstoneSignal(ForgeDirection side) {
        return getGeneralRS(side);
    }

    public boolean allowGeneralRedstoneOutput() {
        return false;
    }

    @Override
    public byte getGeneralRS(ForgeDirection side) {
        return allowGeneralRedstoneOutput() ? mSidedRedstone[side.ordinal()] : 0;
    }

    @Override
    public void setOutputRedstoneSignal(ForgeDirection side, byte aStrength) {
        aStrength = (byte) Math.min(Math.max(0, aStrength), 15);
        if (side != ForgeDirection.UNKNOWN && mSidedRedstone[side.ordinal()] != aStrength) {
            mSidedRedstone[side.ordinal()] = aStrength;
            issueBlockUpdate();
        }
    }

    @Override
    public boolean hasInventoryBeenModified() {
        return mInventoryChanged;
    }

    @Override
    public void setGenericRedstoneOutput(boolean aOnOff) {
        mRedstone = aOnOff;
    }

    @Override
    public void setLightValue(byte aLightValue) {
        mLightValue = (byte) (aLightValue & 15);
    }

    @Override
    public long getAverageElectricInput() {
        int rEU = 0;
        for (int i = 0; i < mAverageEUInput.length; i++) {
            if (i != mAverageEUInputIndex) rEU += mAverageEUInput[i];
        }
        return rEU / (mAverageEUInput.length - 1);
    }

    @Override
    public long getAverageElectricOutput() {
        int rEU = 0;
        for (int i = 0; i < mAverageEUOutput.length; i++) {
            if (i != mAverageEUOutputIndex) rEU += mAverageEUOutput[i];
        }
        return rEU / (mAverageEUOutput.length - 1);
    }

    public String getOwnerName() {
        if (GTUtility.isStringInvalid(mOwnerName)) return "Player";
        return mOwnerName;
    }

    public String setOwnerName(String aName) {
        if (GTUtility.isStringInvalid(aName)) return mOwnerName = "Player";
        return mOwnerName = aName;
    }

    @Override
    public byte getComparatorValue(ForgeDirection side) {
        return canAccessData() ? mMetaTileEntity.getComparatorValue(side) : 0;
    }

    @Override
    public byte getStrongOutputRedstoneSignal(ForgeDirection side) {
        final int ordinalSide = side.ordinal();
        return side != ForgeDirection.UNKNOWN && (mStrongRedstone & (1 << ordinalSide)) != 0
            ? (byte) (mSidedRedstone[ordinalSide] & 15)
            : 0;
    }

    @Override
    public void setStrongOutputRedstoneSignal(ForgeDirection side, byte aStrength) {
        mStrongRedstone |= (1 << side.ordinal());
        setOutputRedstoneSignal(side, aStrength);
    }

    @Override
    public void setRedstoneOutputStrength(ForgeDirection side, boolean isStrong) {
        if (isStrong) {
            mStrongRedstone |= (byte) side.flag;
        } else {
            mStrongRedstone &= ~(byte) side.flag;
        }
        setOutputRedstoneSignal(side, mSidedRedstone[side.ordinal()]);
    }

    @Override
    public boolean getRedstoneOutputStrength(ForgeDirection side) {
        return (mStrongRedstone & side.flag) != 0;
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

    public double getOutputEnergyUnitsPerTick() {
        return oOutput;
    }

    public boolean isTeleporterCompatible(ForgeDirection side) {
        return false;
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
        return (canAccessData() && getStoredEU() - mMetaTileEntity.getMinimumStoredEU() >= oOutput)
            ? Math.max(0, oOutput)
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
        return injectEnergyUnits(aDirection.toForgeDirection(), aAmount, 1) > 0 ? 0 : aAmount;
    }

    public boolean acceptsEnergyFrom(TileEntity aReceiver, Direction aDirection) {
        return inputEnergyFrom(aDirection.toForgeDirection());
    }

    public boolean emitsEnergyTo(TileEntity aReceiver, Direction aDirection) {
        return outputsEnergyTo(aDirection.toForgeDirection());
    }

    @Override
    public boolean isInvalidTileEntity() {
        return isInvalid();
    }

    @Override
    public boolean addStackToSlot(int aIndex, ItemStack aStack) {
        if (GTUtility.isStackInvalid(aStack)) return true;
        if (aIndex < 0 || aIndex >= getSizeInventory()) return false;
        ItemStack tStack = getStackInSlot(aIndex);
        if (GTUtility.isStackInvalid(tStack)) {
            setInventorySlotContents(aIndex, aStack);
            return true;
        }
        aStack = GTOreDictUnificator.get(aStack);
        if (GTUtility.areStacksEqual(tStack, aStack)
            && tStack.stackSize + aStack.stackSize <= Math.min(aStack.getMaxStackSize(), getInventoryStackLimit())) {
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
    public void markDirty() {
        super.markDirty();
        mInventoryChanged = true;
    }

    /**
     * To Do
     */
    public boolean isElectric() {
        return true;
    }

    public boolean isEnetOutput() {
        return false;
    }

    public boolean isEnetInput() {
        return false;
    }

    public long maxEUStore() {
        return 0L;
    }

    public long maxEUInput() {
        return 0L;
    }

    public long maxEUOutput() {
        return 0L;
    }

    public long maxAmperesOut() {
        return 1L;
    }

    public long maxAmperesIn() {
        return 1L;
    }

    public void doEnergyExplosion() {
        if (this.getUniversalEnergyCapacity() > 0L
            && this.getUniversalEnergyStored() >= this.getUniversalEnergyCapacity() / 5L) {
            this.doExplosion(
                this.oOutput * (long) (this.getUniversalEnergyStored() >= this.getUniversalEnergyCapacity() ? 4
                    : (this.getUniversalEnergyStored() >= this.getUniversalEnergyCapacity() / 2L ? 2 : 1)));
            GTMod arg9999 = GTMod.GT;
            GTMod.achievements.issueAchievement(
                this.getWorldObj()
                    .getPlayerEntityByName(this.mOwnerName),
                "electricproblems");
        }
    }

    public void doExplosion(long aAmount) {
        if (this.canAccessData()) {
            if (GregTechAPI.sMachineWireFire && this.mMetaTileEntity.isElectric()) {
                try {
                    this.mReleaseEnergy = true;
                    Util.emitEnergyToNetwork(GTValues.V[5], Math.max(1L, this.getStoredEU() / GTValues.V[5]), this);
                } catch (Exception arg4) {}
            }
            this.mReleaseEnergy = false;
            this.onExplosion();
            Pollution.addPollution(this, 100000);
            this.mMetaTileEntity.doExplosion(aAmount);
        }
    }

    public void onExplosion() {}

    @Override
    public String[] getDescription() {
        return this.canAccessData() ? this.mMetaTileEntity.getDescription() : GTValues.emptyStringArray;
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    public long getEUVar() {
        return this.mStoredEnergy;
    }

    public void setEUVar(long aEnergy) {
        this.mStoredEnergy = aEnergy;
    }

    @Override
    public long getStoredEU() {
        return this.canAccessData() ? Math.min(this.mMetaTileEntity.getEUVar(), this.getEUCapacity()) : 0L;
    }

    @Override
    public long getEUCapacity() {
        return this.canAccessData() ? this.mMetaTileEntity.maxEUStore() : 0L;
    }

    public long getMinimumStoredEU() {
        return 512L;
    }

    public boolean setStoredEU(long aEnergy) {
        if (!this.canAccessData()) {
            return false;
        } else {
            if (aEnergy < 0L) {
                aEnergy = 0L;
            }

            this.mMetaTileEntity.setEUVar(aEnergy);
            return true;
        }
    }

    public boolean decreaseStoredEU(long aEnergy, boolean aIgnoreTooLessEnergy) {
        if (!this.canAccessData()) {
            return false;
        } else if (this.mMetaTileEntity.getEUVar() - aEnergy < 0L && !aIgnoreTooLessEnergy) {
            return false;
        } else {
            this.setStoredEU(this.mMetaTileEntity.getEUVar() - aEnergy);
            if (this.mMetaTileEntity.getEUVar() < 0L) {
                this.setStoredEU(0L);
                return false;
            } else {
                return true;
            }
        }
    }

    // Required as of 5.09.32-pre5
    public boolean energyStateReady() {
        return false;
    }

    private boolean firstTicked = false;

    public boolean onFirstTick() {
        if (!firstTicked) {
            firstTicked = true;
            if (this.mInventory != null) {
                this.mInventory.purgeNulls();
                return true;
            }
        }
        return false;
    }

    /**
     * Adds support for the newer function added by
     * https://github.com/Blood-Asp/GT5-Unofficial/commit/73ee102b63efd92c0f164a7ed7a79ebcd2619617#diff-3051838621d8ae87aa5ccd1345e1f07d
     */
    public boolean inputEnergyFrom(byte arg0, boolean arg1) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * Adds support for the newer function added by
     * https://github.com/Blood-Asp/GT5-Unofficial/commit/73ee102b63efd92c0f164a7ed7a79ebcd2619617#diff-3051838621d8ae87aa5ccd1345e1f07d
     */
    public boolean outputsEnergyTo(byte arg0, boolean arg1) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public byte getColorization() {
        return 0;
    }

    @Override
    public byte setColorization(byte arg0) {
        return 0;
    }

    @Override
    public byte getStrongestRedstone() {
        return 0;
    }

    @Override
    public boolean getRedstone() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean getRedstone(ForgeDirection side) {
        return false;
    }

    @Override
    public boolean isUniversalEnergyStored(long arg0) {
        return false;
    }

    @Override
    public long getUniversalEnergyStored() {
        return 0;
    }

    @Override
    public long getUniversalEnergyCapacity() {
        return 0;
    }
}
