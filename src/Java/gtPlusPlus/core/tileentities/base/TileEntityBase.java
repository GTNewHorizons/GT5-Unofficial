package gtPlusPlus.core.tileentities.base;

import java.util.UUID;

import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.interfaces.IDescribable;
import gregtech.api.interfaces.tileentity.IGregTechDeviceInformation;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.net.GT_Packet_Block_Event;
import gregtech.api.util.GT_CoverBehavior;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
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
import gtPlusPlus.api.interfaces.ILazyCoverable;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.minecraft.BTF_Inventory;
import gtPlusPlus.core.util.data.ArrayUtils;
import gtPlusPlus.core.util.minecraft.gregtech.PollutionUtils;
import ic2.api.Direction;

public class TileEntityBase extends TileEntity implements ILazyCoverable, IGregTechDeviceInformation, IDescribable {

	private String customName;
	public String mOwnerName = "null";
	public String mOwnerUUID = "null";	
	private boolean mIsOwnerOP = false;

	public final BTF_Inventory mInventory;

	public TileEntityBase(int aCapacity) {
		mInventory = new BTF_Inventory(aCapacity, this);
	}

	public NBTTagCompound getTag(final NBTTagCompound nbt, final String tag){
		if(!nbt.hasKey(tag))
		{
			nbt.setTag(tag, new NBTTagCompound());
		}
		return nbt.getCompoundTag(tag);
	}

	@Override
	public void writeToNBT(final NBTTagCompound nbt){
		super.writeToNBT(nbt);
		if (this.hasCustomInventoryName()) {
			nbt.setString("CustomName", this.getCustomName());
		}
		nbt.setBoolean("mIsOwnerOP", this.mIsOwnerOP);
		nbt.setString("mOwnerName", this.mOwnerName);
		nbt.setString("mOwnerUUID", this.mOwnerUUID);
	}

	@Override
	public void readFromNBT(final NBTTagCompound nbt){

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
		try{
			if (this.isServerSide()){
				onPreTick(aTick);
			}
		} catch (Throwable t){
			Logger.ERROR("Tile Entity Encountered an error in it's pre-tick stage.");
			t.printStackTrace();
		}
		try{
			if (this.isServerSide()){
				onTick(aTick);
			}
		} catch (Throwable t){
			Logger.ERROR("Tile Entity Encountered an error in it's tick stage.");
			t.printStackTrace();
		}
		try{
			if (this.isServerSide()){
				onPostTick(aTick);
			}
		} catch (Throwable t){
			Logger.ERROR("Tile Entity Encountered an error in it's post-tick stage.");
			t.printStackTrace();
		}
	}

	public boolean onPreTick(long aTick) {
		return true;
	}

	public boolean onTick(long aTick){
		try{
			if (this.isServerSide()){
				processRecipe();
			}
		} catch (Throwable t){
			Logger.ERROR("Tile Entity Encountered an error in it's processing of a recipe stage.");
			t.printStackTrace();
		}
		return true;
	}

	public boolean onPostTick(long aTick){
		return true;
	}

	public boolean processRecipe(){
		return true;
	}	

	@Override
	public boolean canUpdate() {
		return true;
	}

	public String getOwner(){
		if (this.mOwnerName == null){
			return "null";
		}
		return this.mOwnerName;
	}

	public UUID getOwnerUUID(){
		return UUID.fromString(this.mOwnerUUID);
	}

	public boolean isOwnerOP() {
		return mIsOwnerOP;
	}

	public void setOwnerInformation(String mName, String mUUID, boolean mOP){
		if (isServerSide()){
			if (this.mOwnerName == null || this.mOwnerUUID == null || this.mOwnerName.equals("null") || this.mOwnerUUID.equals("null")){
				this.mOwnerName = mName;
				this.mOwnerUUID = mUUID;
				this.mIsOwnerOP = mOP;
			}
		}
	}

	public boolean isServerSide(){
		if (this.hasWorldObj()){
			if (!this.getWorldObj().isRemote){
				return true;
			}
		}
		return false;
	}

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
		return this.customName != null && !this.customName.equals("");
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
     * returns all valid Inventory Slots, no matter which Side (Unless it's covered).
     * The Side Stuff is done in the following two Functions.
     */
    @Override
    public int[] getAccessibleSlotsFromSide(int aSide) {
        if (canAccessData() && (getCoverBehaviorAtSide((byte) aSide).letsItemsOut((byte) aSide, getCoverIDAtSide((byte) aSide), getCoverDataAtSide((byte) aSide), -1, this) || getCoverBehaviorAtSide((byte) aSide).letsItemsIn((byte) aSide, getCoverIDAtSide((byte) aSide), getCoverDataAtSide((byte) aSide), -1, this)))
            return mInventory.getAccessibleSlotsFromSide(aSide);
        return new int[0];
    }

    /**
     * Can put aStack into Slot at Side
     */
    @Override
    public boolean canInsertItem(int aIndex, ItemStack aStack, int aSide) {
        return canAccessData() && (mRunningThroughTick || !mInputDisabled) && getCoverBehaviorAtSide((byte) aSide).letsItemsIn((byte) aSide, getCoverIDAtSide((byte) aSide), getCoverDataAtSide((byte) aSide), aIndex, this) && mInventory.canInsertItem(aIndex, aStack, aSide);
    }

    /**
     * Can pull aStack out of Slot from Side
     */
    @Override
    public boolean canExtractItem(int aIndex, ItemStack aStack, int aSide) {
        return canAccessData() && (mRunningThroughTick || !mOutputDisabled) && getCoverBehaviorAtSide((byte) aSide).letsItemsOut((byte) aSide, getCoverIDAtSide((byte) aSide), getCoverDataAtSide((byte) aSide), aIndex, this) && mInventory.canExtractItem(aIndex, aStack, aSide);
    }
	

	@Override
	public boolean isValidSlot(int aIndex) {
		return this.canAccessData() ? this.mInventory.isValidSlot(aIndex) : false;
	}




	private final GT_CoverBehavior[] mCoverBehaviors = new GT_CoverBehavior[] { GregTech_API.sNoBehavior,
			GregTech_API.sNoBehavior, GregTech_API.sNoBehavior, GregTech_API.sNoBehavior, GregTech_API.sNoBehavior,
			GregTech_API.sNoBehavior };
	protected TileEntityBase mMetaTileEntity;
	protected long mStoredEnergy = 0;
	protected int mAverageEUInputIndex = 0, mAverageEUOutputIndex = 0;
	protected boolean mReleaseEnergy = false;
	protected int[] mAverageEUInput = new int[11], mAverageEUOutput = new int[11];
	private boolean[] mActiveEUInputs = new boolean[] { false, false, false, false, false, false },
			mActiveEUOutputs = new boolean[] { false, false, false, false, false, false };
	private byte[] mSidedRedstone = new byte[] { 15, 15, 15, 15, 15, 15 };
	private int[] mCoverSides = new int[] { 0, 0, 0, 0, 0, 0 }, mCoverData = new int[] { 0, 0, 0, 0, 0, 0 },
			mTimeStatistics = new int[GregTech_API.TICKS_FOR_LAG_AVERAGING];
	private boolean mHasEnoughEnergy = true;
	protected boolean mRunningThroughTick = false;
	protected boolean mInputDisabled = false;
	protected boolean mOutputDisabled = false;
	private boolean mMuffler = false;
	private boolean mLockUpgrade = false;
	private boolean mActive = false;
	private boolean mRedstone = false;
	private boolean mWorkUpdate = false;
	private boolean mSteamConverter = false;
	private boolean mInventoryChanged = false;
	private boolean mWorks = true;
	private boolean mNeedsUpdate = true;
	private boolean mNeedsBlockUpdate = true;
	private boolean mSendClientData = false;
	private boolean oRedstone = false;
	private boolean mEnergyStateReady = false;
	private byte mColor = 0, oColor = 0, mStrongRedstone = 0, oRedstoneData = 63, oTextureData = 0, oUpdateData = 0,
			oTexturePage = 0, oLightValueClient = -1, oLightValue = -1, mLightValue = 0, mOtherUpgrades = 0,
			mFacing = 0, oFacing = 0, mWorkData = 0;
	private int mDisplayErrorCode = 0, oX = 0, oY = 0, oZ = 0, mTimeStatisticsIndex = 0, mLagWarningCount = 0;
	private short mID = 0;
	protected long mTickTimer = 0;
	private long oOutput = 0;
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
	public void issueCoverUpdate(byte aSide) {
		this.issueClientUpdate();
	}
	
	@Override
	public long getTimer() {
		return this.mTickTimer;
	}

	
	
	
	
	

	public long getOutputAmperage() {
		return this.canAccessData() && this.mMetaTileEntity.isElectric() ? this.mMetaTileEntity.maxAmperesOut() : 0L;
	}

	public long getOutputVoltage() {
		return this.canAccessData() && this.mMetaTileEntity.isElectric() && this.mMetaTileEntity.isEnetOutput()
				? this.mMetaTileEntity.maxEUOutput()
				: 0L;
	}

	public long getInputAmperage() {
		return this.canAccessData() && this.mMetaTileEntity.isElectric() ? this.mMetaTileEntity.maxAmperesIn() : 0L;
	}

	public long getInputVoltage() {
		return this.canAccessData() && this.mMetaTileEntity.isElectric()
				? this.mMetaTileEntity.maxEUInput()
				: 2147483647L;
	}

	@Override
	public boolean decreaseStoredEnergyUnits(long aEnergy, boolean aIgnoreTooLessEnergy) {
		return !this.canAccessData() ? false : (this.mHasEnoughEnergy = this.decreaseStoredEU(aEnergy, aIgnoreTooLessEnergy));
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
	public boolean inputEnergyFrom(byte aSide) {
		return aSide == 6
				? true
				: (!this.isServerSide()
						? this.isEnergyInputSide(aSide)
						: aSide >= 0 && aSide < 6 && this.mActiveEUInputs[aSide] && !this.mReleaseEnergy);
	}
	
	@Override
	public boolean outputsEnergyTo(byte aSide) {
		return aSide == 6
				? true
				: (!this.isServerSide()
						? this.isEnergyOutputSide(aSide)
						: aSide >= 0 && aSide < 6 && this.mActiveEUOutputs[aSide] || this.mReleaseEnergy);
	}
	
	private boolean isEnergyInputSide(byte aSide) {
		if (aSide >= 0 && aSide < 6) {
			if (!this.getCoverBehaviorAtSide(aSide).letsEnergyIn(aSide, this.getCoverIDAtSide(aSide),
					this.getCoverDataAtSide(aSide), this)) {
				return false;
			}

			if (this.isInvalid() || this.mReleaseEnergy) {
				return false;
			}

			if (this.canAccessData() && this.mMetaTileEntity.isElectric() && this.mMetaTileEntity.isEnetInput()) {
				return this.mMetaTileEntity.isInputFacing(aSide);
			}
		}

		return false;
	}

	private boolean isEnergyOutputSide(byte aSide) {
		if (aSide >= 0 && aSide < 6) {
			if (!this.getCoverBehaviorAtSide(aSide).letsEnergyOut(aSide, this.getCoverIDAtSide(aSide),
					this.getCoverDataAtSide(aSide), this)) {
				return false;
			}

			if (this.isInvalid() || this.mReleaseEnergy) {
				return this.mReleaseEnergy;
			}

			if (this.canAccessData() && this.mMetaTileEntity.isElectric() && this.mMetaTileEntity.isEnetOutput()) {
				return this.mMetaTileEntity.isOutputFacing(aSide);
			}
		}

		return false;
	}
	
	public boolean isOutputFacing(byte aSide) {
		return false;
	}

	public boolean isInputFacing(byte aSide) {
		return false;
	}	
	
	private final TileEntity[] mBufferedTileEntities = new TileEntity[6];
	public boolean ignoreUnloadedChunks = true;
	public boolean isDead = false;

	private final void clearNullMarkersFromTileEntityBuffer() {
		for (int i = 0; i < this.mBufferedTileEntities.length; ++i) {
			if (this.mBufferedTileEntities[i] == this) {
				this.mBufferedTileEntities[i] = null;
			}
		}

	}

	protected final void clearTileEntityBuffer() {
		for (int i = 0; i < this.mBufferedTileEntities.length; ++i) {
			this.mBufferedTileEntities[i] = null;
		}

	}

	public final World getWorld() {
		return this.worldObj;
	}

	public final int getXCoord() {
		return this.xCoord;
	}

	public final short getYCoord() {
		return (short) this.yCoord;
	}

	public final int getZCoord() {
		return this.zCoord;
	}

	public final int getOffsetX(byte aSide, int aMultiplier) {
		return this.xCoord + ForgeDirection.getOrientation(aSide).offsetX * aMultiplier;
	}

	public final short getOffsetY(byte aSide, int aMultiplier) {
		return (short) (this.yCoord + ForgeDirection.getOrientation(aSide).offsetY * aMultiplier);
	}

	public final int getOffsetZ(byte aSide, int aMultiplier) {
		return this.zCoord + ForgeDirection.getOrientation(aSide).offsetZ * aMultiplier;
	}

	public final boolean openGUI(EntityPlayer aPlayer) {
		return this.openGUI(aPlayer, 0);
	}

	public final boolean openGUI(EntityPlayer aPlayer, int aID) {
		if (aPlayer == null) {
			return false;
		} else {
			aPlayer.openGui(GT_Values.GT, aID, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
			return true;
		}
	}

	public final int getRandomNumber(int aRange) {
		return this.worldObj.rand.nextInt(aRange);
	}

	public final BiomeGenBase getBiome(int aX, int aZ) {
		return this.worldObj.getBiomeGenForCoords(aX, aZ);
	}

	public final BiomeGenBase getBiome() {
		return this.getBiome(this.xCoord, this.zCoord);
	}

	public final Block getBlockOffset(int aX, int aY, int aZ) {
		return this.getBlock(this.xCoord + aX, this.yCoord + aY, this.zCoord + aZ);
	}

	public final Block getBlockAtSide(byte aSide) {
		return this.getBlockAtSideAndDistance(aSide, 1);
	}

	public final Block getBlockAtSideAndDistance(byte aSide, int aDistance) {
		return this.getBlock(this.getOffsetX(aSide, aDistance), this.getOffsetY(aSide, aDistance),
				this.getOffsetZ(aSide, aDistance));
	}

	public final byte getMetaIDOffset(int aX, int aY, int aZ) {
		return this.getMetaID(this.xCoord + aX, this.yCoord + aY, this.zCoord + aZ);
	}

	public final byte getMetaIDAtSide(byte aSide) {
		return this.getMetaIDAtSideAndDistance(aSide, 1);
	}

	public final byte getMetaIDAtSideAndDistance(byte aSide, int aDistance) {
		return this.getMetaID(this.getOffsetX(aSide, aDistance), this.getOffsetY(aSide, aDistance),
				this.getOffsetZ(aSide, aDistance));
	}

	public final byte getLightLevelOffset(int aX, int aY, int aZ) {
		return this.getLightLevel(this.xCoord + aX, this.yCoord + aY, this.zCoord + aZ);
	}

	public final byte getLightLevelAtSide(byte aSide) {
		return this.getLightLevelAtSideAndDistance(aSide, 1);
	}

	public final byte getLightLevelAtSideAndDistance(byte aSide, int aDistance) {
		return this.getLightLevel(this.getOffsetX(aSide, aDistance), this.getOffsetY(aSide, aDistance),
				this.getOffsetZ(aSide, aDistance));
	}

	public final boolean getOpacityOffset(int aX, int aY, int aZ) {
		return this.getOpacity(this.xCoord + aX, this.yCoord + aY, this.zCoord + aZ);
	}

	public final boolean getOpacityAtSide(byte aSide) {
		return this.getOpacityAtSideAndDistance(aSide, 1);
	}

	public final boolean getOpacityAtSideAndDistance(byte aSide, int aDistance) {
		return this.getOpacity(this.getOffsetX(aSide, aDistance), this.getOffsetY(aSide, aDistance),
				this.getOffsetZ(aSide, aDistance));
	}

	public final boolean getSkyOffset(int aX, int aY, int aZ) {
		return this.getSky(this.xCoord + aX, this.yCoord + aY, this.zCoord + aZ);
	}

	public final boolean getSkyAtSide(byte aSide) {
		return this.getSkyAtSideAndDistance(aSide, 1);
	}

	public final boolean getSkyAtSideAndDistance(byte aSide, int aDistance) {
		return this.getSky(this.getOffsetX(aSide, aDistance), this.getOffsetY(aSide, aDistance),
				this.getOffsetZ(aSide, aDistance));
	}

	public final boolean getAirOffset(int aX, int aY, int aZ) {
		return this.getAir(this.xCoord + aX, this.yCoord + aY, this.zCoord + aZ);
	}

	public final boolean getAirAtSide(byte aSide) {
		return this.getAirAtSideAndDistance(aSide, 1);
	}

	public final boolean getAirAtSideAndDistance(byte aSide, int aDistance) {
		return this.getAir(this.getOffsetX(aSide, aDistance), this.getOffsetY(aSide, aDistance),
				this.getOffsetZ(aSide, aDistance));
	}

	public final TileEntity getTileEntityOffset(int aX, int aY, int aZ) {
		return this.getTileEntity(this.xCoord + aX, this.yCoord + aY, this.zCoord + aZ);
	}

	public final TileEntity getTileEntityAtSideAndDistance(byte aSide, int aDistance) {
		return aDistance == 1
				? this.getTileEntityAtSide(aSide)
				: this.getTileEntity(this.getOffsetX(aSide, aDistance), this.getOffsetY(aSide, aDistance),
						this.getOffsetZ(aSide, aDistance));
	}

	public final IInventory getIInventory(int aX, int aY, int aZ) {
		TileEntity tTileEntity = this.getTileEntity(aX, aY, aZ);
		return tTileEntity instanceof IInventory ? (IInventory) tTileEntity : null;
	}

	public final IInventory getIInventoryOffset(int aX, int aY, int aZ) {
		TileEntity tTileEntity = this.getTileEntityOffset(aX, aY, aZ);
		return tTileEntity instanceof IInventory ? (IInventory) tTileEntity : null;
	}

	public final IInventory getIInventoryAtSide(byte aSide) {
		TileEntity tTileEntity = this.getTileEntityAtSide(aSide);
		return tTileEntity instanceof IInventory ? (IInventory) tTileEntity : null;
	}

	public final IInventory getIInventoryAtSideAndDistance(byte aSide, int aDistance) {
		TileEntity tTileEntity = this.getTileEntityAtSideAndDistance(aSide, aDistance);
		return tTileEntity instanceof IInventory ? (IInventory) tTileEntity : null;
	}

	public final IFluidHandler getITankContainer(int aX, int aY, int aZ) {
		TileEntity tTileEntity = this.getTileEntity(aX, aY, aZ);
		return tTileEntity instanceof IFluidHandler ? (IFluidHandler) tTileEntity : null;
	}

	public final IFluidHandler getITankContainerOffset(int aX, int aY, int aZ) {
		TileEntity tTileEntity = this.getTileEntityOffset(aX, aY, aZ);
		return tTileEntity instanceof IFluidHandler ? (IFluidHandler) tTileEntity : null;
	}

	public final IFluidHandler getITankContainerAtSide(byte aSide) {
		TileEntity tTileEntity = this.getTileEntityAtSide(aSide);
		return tTileEntity instanceof IFluidHandler ? (IFluidHandler) tTileEntity : null;
	}

	public final IFluidHandler getITankContainerAtSideAndDistance(byte aSide, int aDistance) {
		TileEntity tTileEntity = this.getTileEntityAtSideAndDistance(aSide, aDistance);
		return tTileEntity instanceof IFluidHandler ? (IFluidHandler) tTileEntity : null;
	}

	public final IGregTechTileEntity getIGregTechTileEntity(int aX, int aY, int aZ) {
		TileEntity tTileEntity = this.getTileEntity(aX, aY, aZ);
		return tTileEntity instanceof IGregTechTileEntity ? (IGregTechTileEntity) tTileEntity : null;
	}

	public final IGregTechTileEntity getIGregTechTileEntityOffset(int aX, int aY, int aZ) {
		TileEntity tTileEntity = this.getTileEntityOffset(aX, aY, aZ);
		return tTileEntity instanceof IGregTechTileEntity ? (IGregTechTileEntity) tTileEntity : null;
	}

	public final IGregTechTileEntity getIGregTechTileEntityAtSide(byte aSide) {
		TileEntity tTileEntity = this.getTileEntityAtSide(aSide);
		return tTileEntity instanceof IGregTechTileEntity ? (IGregTechTileEntity) tTileEntity : null;
	}

	public final IGregTechTileEntity getIGregTechTileEntityAtSideAndDistance(byte aSide, int aDistance) {
		TileEntity tTileEntity = this.getTileEntityAtSideAndDistance(aSide, aDistance);
		return tTileEntity instanceof IGregTechTileEntity ? (IGregTechTileEntity) tTileEntity : null;
	}

	public final Block getBlock(int aX, int aY, int aZ) {
		return this.ignoreUnloadedChunks && this.crossedChunkBorder(aX, aZ) && !this.worldObj.blockExists(aX, aY, aZ)
				? Blocks.air
				: this.worldObj.getBlock(aX, aY, aZ);
	}

	public final byte getMetaID(int aX, int aY, int aZ) {
		return this.ignoreUnloadedChunks && this.crossedChunkBorder(aX, aZ) && !this.worldObj.blockExists(aX, aY, aZ)
				? 0
				: (byte) this.worldObj.getBlockMetadata(aX, aY, aZ);
	}

	public final byte getLightLevel(int aX, int aY, int aZ) {
		return this.ignoreUnloadedChunks && this.crossedChunkBorder(aX, aZ) && !this.worldObj.blockExists(aX, aY, aZ)
				? 0
				: (byte) ((int) (this.worldObj.getLightBrightness(aX, aY, aZ) * 15.0F));
	}

	public final boolean getSky(int aX, int aY, int aZ) {
		return this.ignoreUnloadedChunks && this.crossedChunkBorder(aX, aZ) && !this.worldObj.blockExists(aX, aY, aZ)
				? true
				: this.worldObj.canBlockSeeTheSky(aX, aY, aZ);
	}

	public final boolean getOpacity(int aX, int aY, int aZ) {
		return this.ignoreUnloadedChunks && this.crossedChunkBorder(aX, aZ) && !this.worldObj.blockExists(aX, aY, aZ)
				? false
				: GT_Utility.isOpaqueBlock(this.worldObj, aX, aY, aZ);
	}

	public final boolean getAir(int aX, int aY, int aZ) {
		return this.ignoreUnloadedChunks && this.crossedChunkBorder(aX, aZ) && !this.worldObj.blockExists(aX, aY, aZ)
				? true
				: GT_Utility.isBlockAir(this.worldObj, aX, aY, aZ);
	}

	public final TileEntity getTileEntity(int aX, int aY, int aZ) {
		return this.ignoreUnloadedChunks && this.crossedChunkBorder(aX, aZ) && !this.worldObj.blockExists(aX, aY, aZ)
				? null
				: this.worldObj.getTileEntity(aX, aY, aZ);
	}

	public final TileEntity getTileEntityAtSide(byte aSide) {
		if (aSide >= 0 && aSide < 6 && this.mBufferedTileEntities[aSide] != this) {
			int tX = this.getOffsetX(aSide, 1);
			short tY = this.getOffsetY(aSide, 1);
			int tZ = this.getOffsetZ(aSide, 1);
			if (this.crossedChunkBorder(tX, tZ)) {
				this.mBufferedTileEntities[aSide] = null;
				if (this.ignoreUnloadedChunks && !this.worldObj.blockExists(tX, tY, tZ)) {
					return null;
				}
			}

			if (this.mBufferedTileEntities[aSide] == null) {
				this.mBufferedTileEntities[aSide] = this.worldObj.getTileEntity(tX, tY, tZ);
				if (this.mBufferedTileEntities[aSide] == null) {
					this.mBufferedTileEntities[aSide] = this;
					return null;
				} else {
					return this.mBufferedTileEntities[aSide];
				}
			} else if (this.mBufferedTileEntities[aSide].isInvalid()) {
				this.mBufferedTileEntities[aSide] = null;
				return this.getTileEntityAtSide(aSide);
			} else {
				return this.mBufferedTileEntities[aSide].xCoord == tX && this.mBufferedTileEntities[aSide].yCoord == tY
						&& this.mBufferedTileEntities[aSide].zCoord == tZ ? this.mBufferedTileEntities[aSide] : null;
			}
		} else {
			return null;
		}
	}

	public boolean isDead() {
		return this.isDead || this.isInvalidTileEntity();
	}

	public void validate() {
		this.clearNullMarkersFromTileEntityBuffer();
		super.validate();
	}

	public void invalidate() {
		this.clearNullMarkersFromTileEntityBuffer();
		super.invalidate();
	}

	public void onChunkUnload() {
		this.clearNullMarkersFromTileEntityBuffer();
		super.onChunkUnload();
		this.isDead = true;
	}

	public final void onAdjacentBlockChange(int aX, int aY, int aZ) {
		this.clearNullMarkersFromTileEntityBuffer();
	}

	public final void sendBlockEvent(byte aID, byte aValue) {
		GT_Values.NW.sendPacketToAllPlayersInRange(this.worldObj,
				new GT_Packet_Block_Event(this.xCoord, (short) this.yCoord, this.zCoord, aID, aValue), this.xCoord,
				this.zCoord);
	}

	private boolean crossedChunkBorder(int aX, int aZ) {
		return aX >> 4 != this.xCoord >> 4 || aZ >> 4 != this.zCoord >> 4;
	}

	public final void setOnFire() {
		GT_Utility.setCoordsOnFire(this.worldObj, this.xCoord, this.yCoord, this.zCoord, false);
	}

	public final void setToFire() {
		this.worldObj.setBlock(this.xCoord, this.yCoord, this.zCoord, Blocks.fire);
	}

	public String trans(String aKey, String aEnglish) {
		return GT_LanguageManager.addStringLocalization("Interaction_DESCRIPTION_Index_" + aKey, aEnglish, false);
	}

    @Override
    public byte getInternalInputRedstoneSignal(byte aSide) {
        return (byte) (getCoverBehaviorAtSide(aSide).getRedstoneInput(aSide, getInputRedstoneSignal(aSide), getCoverIDAtSide(aSide), getCoverDataAtSide(aSide), this) & 15);
    }

    @Override
    public byte getInputRedstoneSignal(byte aSide) {
        return (byte) (worldObj.getIndirectPowerLevelTo(getOffsetX(aSide, 1), getOffsetY(aSide, 1), getOffsetZ(aSide, 1), aSide) & 15);
    }

    @Override
    public byte getOutputRedstoneSignal(byte aSide) {
        return getCoverBehaviorAtSide(aSide).manipulatesSidedRedstoneOutput(aSide, getCoverIDAtSide(aSide), getCoverDataAtSide(aSide), this) ? mSidedRedstone[aSide] : getGeneralRS(aSide);
    }
    
	public boolean allowGeneralRedstoneOutput() {
		return false;
	}
    
    public byte getGeneralRS(byte aSide){
    	return allowGeneralRedstoneOutput() ? mSidedRedstone[aSide] : 0;
    }

    @Override
    public void setInternalOutputRedstoneSignal(byte aSide, byte aStrength) {
        if (!getCoverBehaviorAtSide(aSide).manipulatesSidedRedstoneOutput(aSide, getCoverIDAtSide(aSide), getCoverDataAtSide(aSide), this))
            setOutputRedstoneSignal(aSide, aStrength);
    }

    @Override
    public void setOutputRedstoneSignal(byte aSide, byte aStrength) {
        aStrength = (byte) Math.min(Math.max(0, aStrength), 15);
        if (aSide >= 0 && aSide < 6 && mSidedRedstone[aSide] != aStrength) {
            mSidedRedstone[aSide] = aStrength;
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
    public GT_CoverBehavior getCoverBehaviorAtSide(byte aSide) {
        return aSide >= 0 && aSide < mCoverBehaviors.length ? mCoverBehaviors[aSide] : GregTech_API.sNoBehavior;
    }

    @Override
    public void setCoverIDAtSide(byte aSide, int aID) {
        if (aSide >= 0 && aSide < 6) {
            mCoverSides[aSide] = aID;
            mCoverData[aSide] = 0;
            mCoverBehaviors[aSide] = GregTech_API.getCoverBehavior(aID);
            issueCoverUpdate(aSide);
            issueBlockUpdate();
        }
    }

    @Override
    public void setCoverItemAtSide(byte aSide, ItemStack aCover) {
        GregTech_API.getCoverBehavior(aCover).placeCover(aSide, aCover, this);
    }

    @Override
    public int getCoverIDAtSide(byte aSide) {
        if (aSide >= 0 && aSide < 6) return mCoverSides[aSide];
        return 0;
    }

    @Override
    public ItemStack getCoverItemAtSide(byte aSide) {
        return GT_Utility.intToStack(getCoverIDAtSide(aSide));
    }

    @Override
    public boolean canPlaceCoverIDAtSide(byte aSide, int aID) {
        return getCoverIDAtSide(aSide) == 0;
    }

    @Override
    public boolean canPlaceCoverItemAtSide(byte aSide, ItemStack aCover) {
        return getCoverIDAtSide(aSide) == 0;
    }

    @Override
    public void setCoverDataAtSide(byte aSide, int aData) {
        if (aSide >= 0 && aSide < 6) mCoverData[aSide] = aData;
    }

    @Override
    public int getCoverDataAtSide(byte aSide) {
        if (aSide >= 0 && aSide < 6) return mCoverData[aSide];
        return 0;
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
        int rEU = 0;
        for (int i = 0; i < mAverageEUInput.length; i++) {
            if (i != mAverageEUInputIndex)
                rEU += mAverageEUInput[i];
        }
        return rEU / (mAverageEUInput.length - 1);
    }

    @Override
    public long getAverageElectricOutput() {
        int rEU = 0;
        for (int i = 0; i < mAverageEUOutput.length; i++) {
            if (i != mAverageEUOutputIndex)
                rEU += mAverageEUOutput[i];
        }
        return rEU / (mAverageEUOutput.length - 1);
    }

	public boolean hasSidedRedstoneOutputBehavior() {
		return false;
	}

    @Override
    public boolean dropCover(byte aSide, byte aDroppedSide, boolean aForced) {
        if (getCoverBehaviorAtSide(aSide).onCoverRemoval(aSide, getCoverIDAtSide(aSide), mCoverData[aSide], this, aForced) || aForced) {
            ItemStack tStack = getCoverBehaviorAtSide(aSide).getDrop(aSide, getCoverIDAtSide(aSide), getCoverDataAtSide(aSide), this);
            if (tStack != null) {
                tStack.setTagCompound(null);
                EntityItem tEntity = new EntityItem(worldObj, getOffsetX(aDroppedSide, 1) + 0.5, getOffsetY(aDroppedSide, 1) + 0.5, getOffsetZ(aDroppedSide, 1) + 0.5, tStack);
                tEntity.motionX = 0;
                tEntity.motionY = 0;
                tEntity.motionZ = 0;
                worldObj.spawnEntityInWorld(tEntity);
            }
            setCoverIDAtSide(aSide, 0);
            if (mMetaTileEntity.hasSidedRedstoneOutputBehavior()) {
                setOutputRedstoneSignal(aSide, (byte) 0);
            } else {
                setOutputRedstoneSignal(aSide, (byte) 15);
            }
            return true;
        }
        return false;
    }

    public String getOwnerName() {
        if (GT_Utility.isStringInvalid(mOwnerName)) return "Player";
        return mOwnerName;
    }

    public String setOwnerName(String aName) {
        if (GT_Utility.isStringInvalid(aName)) return mOwnerName = "Player";
        return mOwnerName = aName;
    }

    @Override
    public byte getComparatorValue(byte aSide) {
        return canAccessData() ? mMetaTileEntity.getComparatorValue(aSide) : 0;
    }

    @Override
    public byte getStrongOutputRedstoneSignal(byte aSide) {
        return aSide >= 0 && aSide < 6 && (mStrongRedstone & (1 << aSide)) != 0 ? (byte) (mSidedRedstone[aSide] & 15) : 0;
    }

    @Override
    public void setStrongOutputRedstoneSignal(byte aSide, byte aStrength) {
        mStrongRedstone |= (1 << aSide);
        setOutputRedstoneSignal(aSide, aStrength);
    }

    @Override
    public long injectEnergyUnits(byte aSide, long aVoltage, long aAmperage) {
        if (!canAccessData() || !mMetaTileEntity.isElectric() || !inputEnergyFrom(aSide) || aAmperage <= 0 || aVoltage <= 0 || getStoredEU() >= getEUCapacity() || mMetaTileEntity.maxAmperesIn() <= mAcceptedAmperes)
            return 0;
        if (aVoltage > getInputVoltage()) {
            doExplosion(aVoltage);
            return 0;
        }
        if (increaseStoredEnergyUnits(aVoltage * (aAmperage = Math.min(aAmperage, Math.min(mMetaTileEntity.maxAmperesIn() - mAcceptedAmperes, 1 + ((getEUCapacity() - getStoredEU()) / aVoltage)))), true)) {
            mAverageEUInput[mAverageEUInputIndex] += aVoltage * aAmperage;
            mAcceptedAmperes += aAmperage;
            return aAmperage;
        }
        return 0;
    }

    @Override
    public boolean drainEnergyUnits(byte aSide, long aVoltage, long aAmperage) {
        if (!canAccessData() || !mMetaTileEntity.isElectric() || !outputsEnergyTo(aSide) || getStoredEU() - (aVoltage * aAmperage) < mMetaTileEntity.getMinimumStoredEU())
            return false;
        if (decreaseStoredEU(aVoltage * aAmperage, false)) {
            mAverageEUOutput[mAverageEUOutputIndex] += aVoltage * aAmperage;
            return true;
        }
        return false;
    }

    
    public double getOutputEnergyUnitsPerTick() {
        return oOutput;
    }

    
    public boolean isTeleporterCompatible(ForgeDirection aSide) {
        return false;
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
        return (canAccessData() && getStoredEU() - mMetaTileEntity.getMinimumStoredEU() >= oOutput) ? Math.max(0, oOutput) : 0;
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
        if (aEnergy > 0)
            increaseStoredEnergyUnits(aEnergy, true);
        else
            decreaseStoredEU(-aEnergy, true);
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

    public boolean acceptsEnergyFrom(TileEntity aReceiver, Direction aDirection) {
        return inputEnergyFrom((byte) aDirection.toSideValue());
    }

    
    public boolean emitsEnergyTo(TileEntity aReceiver, Direction aDirection) {
        return outputsEnergyTo((byte) aDirection.toSideValue());
    }

    @Override
    public boolean isInvalidTileEntity() {
        return isInvalid();
    }

    @Override
    public boolean addStackToSlot(int aIndex, ItemStack aStack) {
        if (GT_Utility.isStackInvalid(aStack)) return true;
        if (aIndex < 0 || aIndex >= getSizeInventory()) return false;
        ItemStack tStack = getStackInSlot(aIndex);
        if (GT_Utility.isStackInvalid(tStack)) {
            setInventorySlotContents(aIndex, aStack);
            return true;
        }
        aStack = GT_OreDictUnificator.get(aStack);
        if (GT_Utility.areStacksEqual(tStack, aStack) && tStack.stackSize + aStack.stackSize <= Math.min(aStack.getMaxStackSize(), getInventoryStackLimit())) {
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
			this.doExplosion(this.oOutput * (long) (this.getUniversalEnergyStored() >= this.getUniversalEnergyCapacity()
					? 4
					: (this.getUniversalEnergyStored() >= this.getUniversalEnergyCapacity() / 2L ? 2 : 1)));
			GT_Mod arg9999 = GT_Mod.instance;
			GT_Mod.achievements.issueAchievement(this.getWorldObj().getPlayerEntityByName(this.mOwnerName),
					"electricproblems");
		}

	}

	public void doExplosion(long aAmount) {
		if (this.canAccessData()) {
			if (GregTech_API.sMachineWireFire && this.mMetaTileEntity.isElectric()) {
				try {
					this.mReleaseEnergy = true;
					Util.emitEnergyToNetwork(GT_Values.V[5], Math.max(1L, this.getStoredEU() / GT_Values.V[5]), this);
				} catch (Exception arg4) {
				}
			}
			this.mReleaseEnergy = false;
			this.onExplosion();
			PollutionUtils.addPollution(this, 100000);
			this.mMetaTileEntity.doExplosion(aAmount);
		}

	}

	public void onExplosion() {
		
	}

	@Override
	public String[] getDescription() {
		return this.canAccessData() ? this.mMetaTileEntity.getDescription() : new String[0];
	}

	@Override
	public boolean isGivingInformation() {
		return true;
	}

	@Override
	public String[] getInfoData() {
		return null;
	}
	
	public long getEUVar() {
		return this.mStoredEnergy;
	}

	public void setEUVar(long aEnergy) {
		this.mStoredEnergy = aEnergy;
	}
	
	public long getStoredEU() {
		return this.canAccessData() ? Math.min(this.mMetaTileEntity.getEUVar(), this.getEUCapacity()) : 0L;
	}

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

	//Required as of 5.09.32-pre5
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
	 * Adds support for the newer function added by https://github.com/Blood-Asp/GT5-Unofficial/commit/73ee102b63efd92c0f164a7ed7a79ebcd2619617#diff-3051838621d8ae87aa5ccd1345e1f07d
	 */
	public boolean inputEnergyFrom(byte arg0, boolean arg1) {
		// TODO Auto-generated method stub
		return false;
	}


	/**
	 * Adds support for the newer function added by https://github.com/Blood-Asp/GT5-Unofficial/commit/73ee102b63efd92c0f164a7ed7a79ebcd2619617#diff-3051838621d8ae87aa5ccd1345e1f07d
	 */
	public boolean outputsEnergyTo(byte arg0, boolean arg1) {
		// TODO Auto-generated method stub
		return false;
	}
	
	



}