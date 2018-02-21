package gtPlusPlus.core.tileentities.base;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.interfaces.tileentity.IEnergyConnected;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.net.GT_Packet;
import gregtech.api.net.GT_Packet_Block_Event;
import gregtech.api.net.GT_Packet_TileEntity;
import gregtech.api.net.IGT_NetworkHandler;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.interfaces.IGregtechPower;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import ic2.api.info.Info;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFire;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class TilePoweredGT extends TileEntityBase implements IGregtechPower {
	
	public static AutoMap<TilePoweredGT> mPoweredEntities = new AutoMap<TilePoweredGT>();
	
	//Base Tile Fields
	public boolean ignoreUnloadedChunks;
	public boolean isDead;
	//Meta Tile Fields
	private long mAcceptedAmperes;
	
	private boolean[] mActiveEUInputs;
	private boolean[] mActiveEUOutputs;
	protected int[] mAverageEUInput;
	protected int mAverageEUInputIndex;
	protected int[] mAverageEUOutput;
	protected int mAverageEUOutputIndex;
	private final TileEntity[] mBufferedTileEntities;
	private byte mFacing = 0;
	private boolean mHasEnoughEnergy;
	private boolean mNeedsUpdate;
	private boolean mNeedsBlockUpdate;
	private boolean mRunningThroughTick;
	private boolean mSendClientData;
	protected boolean mReleaseEnergy;
	private long mTickTimer;
	protected long mStoredEnergy;
	protected long mStoredSteam;

	public TilePoweredGT() {
		super();
		this.mBufferedTileEntities = new TileEntity[6];
		this.ignoreUnloadedChunks = true;
		this.isDead = false;
		mPoweredEntities.put(this);
	}
	
	@Override
	public boolean acceptsRotationalEnergy(byte p0) {
		return false;
	}

	private boolean canAccessData() {		
		return this.isInvalid() ? false : true;
	}

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

	private boolean crossedChunkBorder(final int aX, final int aZ) {
		return aX >> 4 != this.xCoord >> 4 || aZ >> 4 != this.zCoord >> 4;
	}

	@Override
	public boolean decreaseStoredEnergyUnits(final long aEnergy, final boolean aIgnoreTooLessEnergy) {
		return this.canAccessData() && (this.mHasEnoughEnergy = (this.decreaseStoredEU(aEnergy, aIgnoreTooLessEnergy)
				|| this.decreaseStoredSteam(aEnergy, false)
				|| (aIgnoreTooLessEnergy && this.decreaseStoredSteam(aEnergy, true))));
	}

	public boolean decreaseStoredEU(final long aEnergy, final boolean aIgnoreTooLessEnergy) {
		if (!this.canAccessData()) {
			return false;
		}
		if (this.getEUVar() - aEnergy < 0L && !aIgnoreTooLessEnergy) {
			return false;
		}
		this.setStoredEU(this.getEUVar() - aEnergy);
		if (this.getEUVar() < 0L) {
			this.setStoredEU(0L);
			return false;
		}
		return true;
	}

	public boolean decreaseStoredSteam(final long aEnergy, final boolean aIgnoreTooLessEnergy) {
		if (!this.canAccessData()) {
			return false;
		}
		if (this.getSteamVar() - aEnergy < 0L && !aIgnoreTooLessEnergy) {
			return false;
		}
		this.setStoredSteam(this.getSteamVar() - aEnergy);
		if (this.getSteamVar() < 0L) {
			this.setStoredSteam(0L);
			return false;
		}
		return true;
	}
	
	public void doExplosion(final long aExplosionPower) {
		final float tStrength = (aExplosionPower < GT_Values.V[0])
				? 1.0f
				: ((aExplosionPower < GT_Values.V[1])
						? 2.0f
						: ((aExplosionPower < GT_Values.V[2])
								? 3.0f
								: ((aExplosionPower < GT_Values.V[3])
										? 4.0f
										: ((aExplosionPower < GT_Values.V[4])
												? 5.0f
												: ((aExplosionPower < GT_Values.V[4] * 2L)
														? 6.0f
														: ((aExplosionPower < GT_Values.V[5])
																? 7.0f
																: ((aExplosionPower < GT_Values.V[6])
																		? 8.0f
																		: ((aExplosionPower < GT_Values.V[7])
																				? 9.0f
																				: 10.0f))))))));
		final int tX = this.getXCoord();
		final int tY = this.getYCoord();
		final int tZ = this.getZCoord();
		final World tWorld = this.getWorld();
		GT_Utility.sendSoundToPlayers(tWorld, (String) GregTech_API.sSoundList.get(209), 1.0f, -1.0f, tX, tY, tZ);
		tWorld.setBlock(tX, tY, tZ, Blocks.air);
		if (GregTech_API.sMachineExplosions) {
			tWorld.createExplosion((Entity) null, tX + 0.5, tY + 0.5, tZ + 0.5, tStrength, true);
		}
	}

	public boolean drainEnergyUnits(final byte aSide, final long aVoltage, final long aAmperage) {
		if (!this.canAccessData() || !this.isElectric() || !this.outputsEnergyTo(aSide)
				|| this.getStoredEU() - aVoltage * aAmperage < this.getMinimumStoredEU()) {
			return false;
		}
		if (this.decreaseStoredEU(aVoltage * aAmperage, false)) {
			final int[] mAverageEUOutput = this.mAverageEUOutput;
			final int mAverageEUOutputIndex = this.mAverageEUOutputIndex;
			mAverageEUOutput[mAverageEUOutputIndex] += (int) (aVoltage * aAmperage);
			return true;
		}
		return false;
	}

	@Override
	public final boolean getAir(final int aX, final int aY, final int aZ) {
		return (this.ignoreUnloadedChunks && this.crossedChunkBorder(aX, aZ) && !this.worldObj.blockExists(aX, aY, aZ))
				|| GT_Utility.isBlockAir(this.worldObj, aX, aY, aZ);
	}

	@Override
	public final boolean getAirAtSide(final byte aSide) {
		return this.getAirAtSideAndDistance(aSide, 1);
	}

	@Override
	public final boolean getAirAtSideAndDistance(final byte aSide, final int aDistance) {
		return this.getAir(this.getOffsetX(aSide, aDistance), this.getOffsetY(aSide, aDistance),
				this.getOffsetZ(aSide, aDistance));
	}

	@Override
	public final boolean getAirOffset(final int aX, final int aY, final int aZ) {
		return this.getAir(this.xCoord + aX, this.yCoord + aY, this.zCoord + aZ);
	}

	@Override
	public long getAverageElectricInput() {
		int rEU = 0;
		for (int i = 0; i < this.mAverageEUInput.length; ++i) {
			if (i != this.mAverageEUInputIndex) {
				rEU += this.mAverageEUInput[i];
			}
		}
		return rEU / (this.mAverageEUInput.length - 1);
	}

	public long getAverageElectricOutput() {
		int rEU = 0;
		for (int i = 0; i < this.mAverageEUOutput.length; ++i) {
			if (i != this.mAverageEUOutputIndex) {
				rEU += this.mAverageEUOutput[i];
			}
		}
		return rEU / (this.mAverageEUOutput.length - 1);
	}

	@Override
	public byte getBackFacing() {
		return GT_Utility.getOppositeSide((int) this.mFacing);
	}

	@Override
	public final Block getBlock(final int aX, final int aY, final int aZ) {
		if (this.ignoreUnloadedChunks && this.crossedChunkBorder(aX, aZ) && !this.worldObj.blockExists(aX, aY, aZ)) {
			return Blocks.air;
		}
		return this.worldObj.getBlock(aX, aY, aZ);
	}


	@Override
	public final Block getBlockAtSide(final byte aSide) {
		return this.getBlockAtSideAndDistance(aSide, 1);
	}

	@Override
	public final Block getBlockAtSideAndDistance(final byte aSide, final int aDistance) {
		return this.getBlock(this.getOffsetX(aSide, aDistance), this.getOffsetY(aSide, aDistance),
				this.getOffsetZ(aSide, aDistance));
	}

	@Override
	public final Block getBlockOffset(final int aX, final int aY, final int aZ) {
		return this.getBlock(this.xCoord + aX, this.yCoord + aY, this.zCoord + aZ);
	}

	@Override
	public String[] getDescription() {
		// TODO Auto-generated method stub
		return null;
	}








	@Override
	public long getEUCapacity() {
		if (this.canAccessData()) {
			return this.maxEUStore();
		}
		return 0L;
	}

	public long getEUVar() {
		return mStoredEnergy;
	}

	@Override
	public byte getFrontFacing() {
		return this.mFacing;
	}

	@Override
	public final IGregTechTileEntity getIGregTechTileEntity(final int aX, final int aY, final int aZ) {
		final TileEntity tTileEntity = this.getTileEntity(aX, aY, aZ);
		if (tTileEntity instanceof IGregTechTileEntity) {
			return (IGregTechTileEntity) tTileEntity;
		}
		return null;
	}

	@Override
	public final IGregTechTileEntity getIGregTechTileEntityAtSide(final byte aSide) {
		final TileEntity tTileEntity = this.getTileEntityAtSide(aSide);
		if (tTileEntity instanceof IGregTechTileEntity) {
			return (IGregTechTileEntity) tTileEntity;
		}
		return null;
	}
	
	@Override
	public final IGregTechTileEntity getIGregTechTileEntityAtSideAndDistance(final byte aSide, final int aDistance) {
		final TileEntity tTileEntity = this.getTileEntityAtSideAndDistance(aSide, aDistance);
		if (tTileEntity instanceof IGregTechTileEntity) {
			return (IGregTechTileEntity) tTileEntity;
		}
		return null;
	}

	@Override
	public final IGregTechTileEntity getIGregTechTileEntityOffset(final int aX, final int aY, final int aZ) {
		final TileEntity tTileEntity = this.getTileEntityOffset(aX, aY, aZ);
		if (tTileEntity instanceof IGregTechTileEntity) {
			return (IGregTechTileEntity) tTileEntity;
		}
		return null;
	}

	@Override
	public final IInventory getIInventory(final int aX, final int aY, final int aZ) {
		final TileEntity tTileEntity = this.getTileEntity(aX, aY, aZ);
		if (tTileEntity instanceof IInventory) {
			return (IInventory) tTileEntity;
		}
		return null;
	}

	@Override
	public final IInventory getIInventoryAtSide(final byte aSide) {
		final TileEntity tTileEntity = this.getTileEntityAtSide(aSide);
		if (tTileEntity instanceof IInventory) {
			return (IInventory) tTileEntity;
		}
		return null;
	}

	@Override
	public final IInventory getIInventoryAtSideAndDistance(final byte aSide, final int aDistance) {
		final TileEntity tTileEntity = this.getTileEntityAtSideAndDistance(aSide, aDistance);
		if (tTileEntity instanceof IInventory) {
			return (IInventory) tTileEntity;
		}
		return null;
	}

	@Override
	public final IInventory getIInventoryOffset(final int aX, final int aY, final int aZ) {
		final TileEntity tTileEntity = this.getTileEntityOffset(aX, aY, aZ);
		if (tTileEntity instanceof IInventory) {
			return (IInventory) tTileEntity;
		}
		return null;
	}

	@Override
	public String[] getInfoData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getInputAmperage() {
		if (this.canAccessData() && this.isElectric()) {
			return this.maxAmperesIn();
		}
		return 0L;
	}

	public long getInputTier() {
		return GT_Utility.getTier(this.getInputVoltage());
	}

	@Override
	public long getInputVoltage() {
		if (this.canAccessData() && this.isElectric()) {
			return this.maxEUInput();
		}
		return 2147483647L;
	}

	private long getMinimumStoredEU() {
		return 0;
	}

	@Override
	public final int getOffsetX(final byte aSide, final int aMultiplier) {
		return this.xCoord + ForgeDirection.getOrientation((int) aSide).offsetX * aMultiplier;
	}

	public final short getOffsetY(final byte aSide, final int aMultiplier) {
		return (short) (this.yCoord + ForgeDirection.getOrientation((int) aSide).offsetY * aMultiplier);
	}

	public final int getOffsetZ(final byte aSide, final int aMultiplier) {
		return this.zCoord + ForgeDirection.getOrientation((int) aSide).offsetZ * aMultiplier;
	}

	@Override
	public long getOutputAmperage() {
		if (this.canAccessData() && this.isElectric()) {
			return this.maxAmperesOut();
		}
		return 0L;
	}

	public long getOutputTier() {
		return GT_Utility.getTier(this.getOutputVoltage());
	}

	@Override
	public long getOutputVoltage() {
		if (this.canAccessData() && this.isElectric() && this.isEnetOutput()) {
			return this.maxEUOutput();
		}
		return 0L;
	}

	@Override
	public int getRandomNumber(int p0) {
		return CORE.RANDOM.nextInt();
	}

	@Override
	public long getSteamCapacity() {
		if (this.canAccessData()) {
			return this.maxSteamStore();
		}
		return 0L;
	}

	public long getSteamVar() {
		return mStoredSteam;
	}

	@Override
	public long getStoredEU() {
		if (this.canAccessData()) {
			return Math.min(this.getEUVar(), this.getEUCapacity());
		}
		return 0L;
	}









	@Override
	public long getStoredSteam() {
		if (this.canAccessData()) {
			return Math.min(this.getSteamVar(), this.getSteamCapacity());
		}
		return 0L;
	}

	@Override
	public final TileEntity getTileEntity(final int aX, final int aY, final int aZ) {
		if (this.ignoreUnloadedChunks && this.crossedChunkBorder(aX, aZ) && !this.worldObj.blockExists(aX, aY, aZ)) {
			return null;
		}
		return this.worldObj.getTileEntity(aX, aY, aZ);
	}

	public final TileEntity getTileEntityAtSide(final byte aSide) {
		if (aSide < 0 || aSide >= 6 || this.mBufferedTileEntities[aSide] == this) {
			return null;
		}
		final int tX = this.getOffsetX(aSide, 1);
		final int tY = this.getOffsetY(aSide, 1);
		final int tZ = this.getOffsetZ(aSide, 1);
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
			}
			return this.mBufferedTileEntities[aSide];
		} else {
			if (this.mBufferedTileEntities[aSide].isInvalid()) {
				this.mBufferedTileEntities[aSide] = null;
				return this.getTileEntityAtSide(aSide);
			}
			if (this.mBufferedTileEntities[aSide].xCoord == tX && this.mBufferedTileEntities[aSide].yCoord == tY
					&& this.mBufferedTileEntities[aSide].zCoord == tZ) {
				return this.mBufferedTileEntities[aSide];
			}
			return null;
		}
	}

	@Override
	public final TileEntity getTileEntityAtSideAndDistance(final byte aSide, final int aDistance) {
		if (aDistance == 1) {
			return this.getTileEntityAtSide(aSide);
		}
		return this.getTileEntity(this.getOffsetX(aSide, aDistance), this.getOffsetY(aSide, aDistance),
				this.getOffsetZ(aSide, aDistance));
	}










	@Override
	public final TileEntity getTileEntityOffset(final int aX, final int aY, final int aZ) {
		return this.getTileEntity(this.xCoord + aX, this.yCoord + aY, this.zCoord + aZ);
	}

	@Override
	public long getUniversalEnergyCapacity() {
		return 0;
	}

	@Override
	public long getUniversalEnergyStored() {
		return 0;
	}

	@Override
	public World getWorld() {
		return this.getWorldObj();
	}






	@Override
	public int getXCoord() {
		return this.xCoord;
	}





	@Override
	public short getYCoord() {
		return (short) this.yCoord;
	}

	@Override
	public int getZCoord() {
		return this.zCoord;
	}

	public boolean hasEnoughEnergy() {
		return (this.getStoredEU() > 0 ? (mHasEnoughEnergy = true) : (mHasEnoughEnergy = false));
	}




	@Override
	public boolean increaseStoredEnergyUnits(final long aEnergy, final boolean aIgnoreTooMuchEnergy) {
		if (!this.canAccessData()) {
			return false;
		}
		if (this.getStoredEU() < this.getEUCapacity() || aIgnoreTooMuchEnergy) {
			this.setStoredEU(this.getEUVar() + aEnergy);
			return true;
		}
		return false;
	}

	@Override
	public boolean increaseStoredSteam(final long aEnergy, final boolean aIgnoreTooMuchEnergy) {
		if (!this.canAccessData()) {
			return false;
		}
		if (this.getSteamVar() < this.getSteamCapacity() || aIgnoreTooMuchEnergy) {
			this.setStoredSteam(this.getSteamVar() + aEnergy);
			return true;
		}
		return false;
	}

	public long injectEnergyUnits(final byte aSide, final long aVoltage, long aAmperage) {
		if (!this.canAccessData() || !this.isElectric() || !this.inputEnergyFrom(aSide)
				|| aAmperage <= 0L || aVoltage <= 0L || this.getStoredEU() >= this.getEUCapacity()
				|| this.maxAmperesIn() <= this.mAcceptedAmperes) {
			return 0L;
		}
		if (aVoltage > this.getInputVoltage()) {
			this.doExplosion(aVoltage);
			return 0L;
		}
		if (this.increaseStoredEnergyUnits(aVoltage
				* (aAmperage = Math.min(aAmperage, Math.min(this.maxAmperesIn() - this.mAcceptedAmperes,
						1L + (this.getEUCapacity() - this.getStoredEU()) / aVoltage))),
				true)) {
			final int[] mAverageEUInput = this.mAverageEUInput;
			final int mAverageEUInputIndex = this.mAverageEUInputIndex;
			mAverageEUInput[mAverageEUInputIndex] += (int) (aVoltage * aAmperage);
			this.mAcceptedAmperes += aAmperage;
			return aAmperage;
		}
		return 0L;
	}

	@Override
	public boolean injectRotationalEnergy(byte p0, long p1, long p2) {
		return false;
	}

	@Override
	public boolean inputEnergyFrom(final byte aSide) {
		if (aSide == 6) {
			return true;
		}
		if (this.isServerSide()) {
			return aSide >= 0 && aSide < 6 && this.mActiveEUInputs[aSide] && !this.mReleaseEnergy;
		}
		return this.isEnergyInputSide(aSide);
	}	
	
	
	public final boolean isClientSide() {
		return this.worldObj.isRemote;
	}

	@Override
	public boolean isDead() {
		return this.isDead;
	}
	
	private boolean isElectric() {
		return true;
	}

	private boolean isEnergyInputSide(final byte aSide) {
		if (aSide >= 0 && aSide < 6) {			
			if (this.isInvalid() || this.mReleaseEnergy) {
				return false;
			}
			if (this.canAccessData() && this.isElectric() && this.isEnetInput()) {
				return this.isInputFacing(aSide);
			}
		}
		return false;
	}
	
	private boolean isEnergyOutputSide(final byte aSide) {
		if (aSide >= 0 && aSide < 6) {			
			if (this.isInvalid() || this.mReleaseEnergy) {
				return this.mReleaseEnergy;
			}
			if (this.canAccessData() && this.isElectric() && this.isEnetOutput()) {
				return this.isOutputFacing(aSide);
			}
		}
		return false;
	}

	public boolean isEnetInput() {
		return false;
	}

	public boolean isEnetOutput() {
		return false;
	}

	@Override
	public boolean isGivingInformation() {
		return this.canAccessData() && this.isGivingInformation();
	}

	public boolean isInputFacing(final byte aSide) {
		return false;
	}
	
	


	@Override
	public boolean isInvalidTileEntity() {
		return isDead() ? true : false;
	}

	public boolean isOutputFacing(final byte aSide) {
		return false;
	}

	public final boolean isServerSide() {
		return !this.worldObj.isRemote;
	}

	public boolean isUniversalEnergyStored(final long aEnergyAmount) {
		return this.getUniversalEnergyStored() >= aEnergyAmount || (this.mHasEnoughEnergy = false);
	}

	@Override
	public boolean isValidFacing(final byte aSide) {
		return this.canAccessData();
	}

	public long maxAmperesIn() {
		return 1L;
	}

	public long maxAmperesOut() {
		return 1L;
	}
	
	public long maxEUInput() {
		return 0L;
	}

	public long maxEUOutput() {
		return 0L;
	}
	
	public long maxEUStore() {
		return 0L;
	}

	public long maxSteamStore() {
		return 256000L;
	}
	
	public final void onAdjacentBlockChange(final int aX, final int aY, final int aZ) {
		this.clearNullMarkersFromTileEntityBuffer();
	}

	@Override
	public boolean outputsEnergyTo(final byte aSide) {
		if (aSide == 6) {
			return true;
		}
		if (this.isServerSide()) {
			if (aSide < 0 || aSide >= 6 || !this.mActiveEUOutputs[aSide]) {
				if (!this.mReleaseEnergy) {
					return false;
				}
			}
			return true;
		}
		return this.isEnergyOutputSide(aSide);
	}

	public final void sendBlockEvent(final byte aID, final byte aValue) {
		GT_Values.NW.sendPacketToAllPlayersInRange(this.worldObj,
				(GT_Packet) new GT_Packet_Block_Event(this.xCoord, (short) this.yCoord, this.zCoord, aID, aValue),
				this.xCoord, this.zCoord);
	}

	public void setEUVar(final long aEnergy) {
		mStoredEnergy = aEnergy;
	}

	@Override
	public void setFrontFacing(byte aFacing) {
		if (this.isValidFacing(aFacing)) {
			this.mFacing = aFacing;
			//this.onFacingChange();
			//this.onMachineBlockUpdate();
		}
	}
	
	public void setSteamVar(final long aSteam) {
		mStoredSteam = aSteam;
	}

	public boolean setStoredEU(long aEnergy) {
		if (!this.canAccessData()) {
			return false;
		}
		if (aEnergy < 0L) {
			aEnergy = 0L;
		}
		this.setEUVar(aEnergy);
		return true;
	}
	
	public boolean setStoredSteam(long aEnergy) {
		if (!this.canAccessData()) {
			return false;
		}
		if (aEnergy < 0L) {
			aEnergy = 0L;
		}
		this.setSteamVar(aEnergy);
		return true;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		// TODO Auto-generated method stub
		super.writeToNBT(nbt);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		// TODO Auto-generated method stub
		super.readFromNBT(nbt);
	}

	
	
	
	@Override
	public boolean onPreTick() {
		return onPreTick(this, this.mTickTimer);
	}

	@Override
	public boolean onTick() {
		return onTick(this, this.mTickTimer);
	}

	@Override
	public boolean onPostTick() {
		return onPostTick(this, this.mTickTimer);
	}

	@Override
	public boolean onPreTick(TilePoweredGT tilePoweredGT, long mTickTimer2) {
		return super.onPreTick();
	}

	@Override
	public boolean onTick(TilePoweredGT iGregTechTileEntity, long mTickTimer2) {
		return super.onTick();
	}

	@Override
	public boolean onPostTick(TilePoweredGT iGregTechTileEntity, long mTickTimer2) {
		return super.onPostTick();
	}

	@Override
	public void markDirty() {
		super.markDirty();
	}

	@Override
	public boolean receiveClientEvent(int p_145842_1_, int p_145842_2_) {
		// TODO Auto-generated method stub
		return super.receiveClientEvent(p_145842_1_, p_145842_2_);
	}

	@Override
	public void onChunkUnload() {
		this.clearNullMarkersFromTileEntityBuffer();
		super.onChunkUnload();
		this.isDead = true;
	}
	
	
	public void updateEntity() {
		super.updateEntity();
		this.isDead = false;
		
		this.mRunningThroughTick = true;
		long tTime = System.currentTimeMillis();
		int tCode = 0;
		final boolean aSideServer = this.isServerSide();
		final boolean aSideClient = this.isClientSide();
		/*try {
			for (tCode = 0; this.hasValidMetaTileEntity() && tCode >= 0; tCode = -1) {
				Label_1743 : {
					switch (tCode) {
						case 0 : {
							++tCode;
							if (this.mTickTimer++ != 0L) {
								break Label_1743;
							}
							this.oX = this.xCoord;
							this.oY = this.yCoord;
							this.oZ = this.zCoord;
							
							this.worldObj.markTileEntityChunkModified(this.xCoord, this.yCoord, this.zCoord,
									(TileEntity) this);
							this.onFirstTick(this);
							if (!this.hasValidMetaTileEntity()) {
								this.mRunningThroughTick = false;
								return;
							}
							break Label_1743;
						}
						case 1 : {
							++tCode;
							if (!aSideClient) {
								break Label_1743;
							}							
							if (this.mNeedsUpdate) {
								this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
								this.mNeedsUpdate = false;
							}
							break Label_1743;
						}
						case 2 :
						case 3 :
						case 4 :
						case 5 :
						case 6 :
						case 7 : {
							if (aSideServer && this.mTickTimer > 10L) {
								for (byte i = (byte) (tCode - 2); i < 6; ++i) {
									
								}
							}
						}
						case 8 : {
							tCode = 9;
							if (aSideServer) {
								if (++this.mAverageEUInputIndex >= this.mAverageEUInput.length) {
									this.mAverageEUInputIndex = 0;
								}
								if (++this.mAverageEUOutputIndex >= this.mAverageEUOutput.length) {
									this.mAverageEUOutputIndex = 0;
								}
								this.mAverageEUInput[this.mAverageEUInputIndex] = 0;
								this.mAverageEUOutput[this.mAverageEUOutputIndex] = 0;
							}
						}
						case 9 : {
							++tCode;
							this.onPreTick(this, this.mTickTimer);
							if (!this.hasValidMetaTileEntity()) {
								this.mRunningThroughTick = false;
								return;
							}
						}
						case 10 : {
							++tCode;
							if (!aSideServer) {
								break Label_1743;
							}
							
							if (this.xCoord != this.oX || this.yCoord != this.oY || this.zCoord != this.oZ) {
								this.oX = this.xCoord;
								this.oY = this.yCoord;
								this.oZ = this.zCoord;
								this.issueClientUpdate();
								this.clearTileEntityBuffer();
							}
							if (this.mFacing != this.oFacing) {
								this.oFacing = this.mFacing;								
								this.issueBlockUpdate();
							}
							if (this.mTickTimer > 20L && this.isElectric()) {
								this.mAcceptedAmperes = 0L;
								if (this.getOutputVoltage() != this.oOutput) {
									this.oOutput = this.getOutputVoltage();
								}
								if (this.isEnetOutput() || this.isEnetInput()) {
									for (byte i = 0; i < 6; ++i) {
										boolean temp = this.isEnergyInputSide(i);
										if (temp != this.mActiveEUInputs[i]) {
											this.mActiveEUInputs[i] = temp;
										}
										temp = this.isEnergyOutputSide(i);
										if (temp != this.mActiveEUOutputs[i]) {
											this.mActiveEUOutputs[i] = temp;
										}
									}
								}
								if (this.isEnetOutput() && this.oOutput > 0L) {
									final long tOutputVoltage = Math.max(this.oOutput,
											this.oOutput + (1 << GT_Utility.getTier(this.oOutput)));
									final long tUsableAmperage = Math.min(this.getOutputAmperage(),
											(this.getStoredEU() - this.getMinimumStoredEU())
													/ tOutputVoltage);
									if (tUsableAmperage > 0L) {
										final long tEU = tOutputVoltage * IEnergyConnected.Util.emitEnergyToNetwork(
												this.oOutput, tUsableAmperage, (IEnergyConnected) this);
										final int[] mAverageEUOutput = this.mAverageEUOutput;
										final int mAverageEUOutputIndex = this.mAverageEUOutputIndex;
										mAverageEUOutput[mAverageEUOutputIndex] += (int) tEU;
										this.decreaseStoredEU(tEU, true);
									}
								}
								if (this.getEUCapacity() > 0L) {
									if (GregTech_API.sMachineFireExplosions && this.getRandomNumber(1000) == 0) {
										final Block tBlock = this.getBlockAtSide((byte) this.getRandomNumber(6));
										if (tBlock instanceof BlockFire) {
											this.doEnergyExplosion();
										}
									}
									if (!this.hasValidMetaTileEntity()) {
										this.mRunningThroughTick = false;
										return;
									}									
								}
							}
							if (!this.hasValidMetaTileEntity()) {
								this.mRunningThroughTick = false;
								return;
							}
							break Label_1743;
						}
						case 13 : {
							++tCode;
							this.updateStatus();
							if (!this.hasValidMetaTileEntity()) {
								this.mRunningThroughTick = false;
								return;
							}
						}
						case 14 : {
							++tCode;
							this.onPostTick((IGregTechTileEntity) this, this.mTickTimer);
							if (!this.hasValidMetaTileEntity()) {
								this.mRunningThroughTick = false;
								return;
							}
						}
						case 15 : {
							++tCode;
							if (!aSideServer) {
								break;
							}
							if (this.mTickTimer % 10L == 0L && this.mSendClientData) {
								final IGT_NetworkHandler nw = GT_Values.NW;
								final World worldObj = this.worldObj;
								final int xCoord = this.xCoord;
								final short n = (short) this.yCoord;
								final int zCoord = this.zCoord;
								final short mid = this.mID;
								final int n2 = this.mCoverSides[0];
								final int n3 = this.mCoverSides[1];
								final int n4 = this.mCoverSides[2];
								final int n5 = this.mCoverSides[3];
								final int n6 = this.mCoverSides[4];
								final int n7 = this.mCoverSides[5];
								final byte oTextureData = (byte) ((this.mFacing & 0x7) | (this.mActive ? 8 : 0)
										| (this.mRedstone ? 16 : 0) | (this.mLockUpgrade ? 32 : 0));
								this.oTextureData = oTextureData;
								final byte oTexturePage = (byte) ((this.hasValidMetaTileEntity()
										&& this.mMetaTileEntity instanceof GT_MetaTileEntity_Hatch)
												? ((GT_MetaTileEntity_Hatch) this.mMetaTileEntity).getTexturePage()
												: 0);
								this.oTexturePage = oTexturePage;
								final byte oUpdateData = (byte) (this.hasValidMetaTileEntity()
										? this.mMetaTileEntity.getUpdateData()
										: 0);
								this.oUpdateData = oUpdateData;
								final byte oRedstoneData = (byte) (((this.mSidedRedstone[0] > 0) ? 1 : 0)
										| ((this.mSidedRedstone[1] > 0) ? 2 : 0)
										| ((this.mSidedRedstone[2] > 0) ? 4 : 0)
										| ((this.mSidedRedstone[3] > 0) ? 8 : 0)
										| ((this.mSidedRedstone[4] > 0) ? 16 : 0)
										| ((this.mSidedRedstone[5] > 0) ? 32 : 0));
								this.oRedstoneData = oRedstoneData;
								final byte mColor = this.mColor;
								this.oColor = mColor;
								nw.sendPacketToAllPlayersInRange(worldObj,
										(GT_Packet) new GT_Packet_TileEntity(xCoord, n, zCoord, mid, n2, n3, n4, n5, n6,
												n7, oTextureData, oTexturePage, oUpdateData, oRedstoneData, mColor),
										this.xCoord, this.zCoord);
								this.mSendClientData = false;
							}
							if (this.mTickTimer > 10L) {
								byte tData = (byte) ((this.mFacing & 0x7) | (this.mActive ? 8 : 0)
										| (this.mRedstone ? 16 : 0) | (this.mLockUpgrade ? 32 : 0));
								if (tData != this.oTextureData) {
									this.sendBlockEvent((byte) 0, this.oTextureData = tData);
								}
								tData = this.mMetaTileEntity.getUpdateData();
								if (tData != this.oUpdateData) {
									this.sendBlockEvent((byte) 1, this.oUpdateData = tData);
								}
								if (this.mMetaTileEntity instanceof GT_MetaTileEntity_Hatch) {
									tData = ((GT_MetaTileEntity_Hatch) this.mMetaTileEntity).getTexturePage();
									if (tData != this.oTexturePage) {
										final byte b = 1;
										final byte oTexturePage2 = tData;
										this.oTexturePage = oTexturePage2;
										this.sendBlockEvent(b, (byte) (oTexturePage2 | 0x80));
									}
								}
								if (this.mColor != this.oColor) {
									this.sendBlockEvent((byte) 2, this.oColor = this.mColor);
								}
								tData = (byte) (((this.mSidedRedstone[0] > 0) ? 1 : 0)
										| ((this.mSidedRedstone[1] > 0) ? 2 : 0)
										| ((this.mSidedRedstone[2] > 0) ? 4 : 0)
										| ((this.mSidedRedstone[3] > 0) ? 8 : 0)
										| ((this.mSidedRedstone[4] > 0) ? 16 : 0)
										| ((this.mSidedRedstone[5] > 0) ? 32 : 0));
								if (tData != this.oRedstoneData) {
									this.sendBlockEvent((byte) 3, this.oRedstoneData = tData);
								}
								if (this.mLightValue != this.oLightValue) {
									this.worldObj.setLightValue(EnumSkyBlock.Block, this.xCoord, this.yCoord,
											this.zCoord, (int) this.mLightValue);
									this.worldObj.updateLightByType(EnumSkyBlock.Block, this.xCoord, this.yCoord,
											this.zCoord);
									this.worldObj.updateLightByType(EnumSkyBlock.Block, this.xCoord + 1, this.yCoord,
											this.zCoord);
									this.worldObj.updateLightByType(EnumSkyBlock.Block, this.xCoord - 1, this.yCoord,
											this.zCoord);
									this.worldObj.updateLightByType(EnumSkyBlock.Block, this.xCoord, this.yCoord + 1,
											this.zCoord);
									this.worldObj.updateLightByType(EnumSkyBlock.Block, this.xCoord, this.yCoord - 1,
											this.zCoord);
									this.worldObj.updateLightByType(EnumSkyBlock.Block, this.xCoord, this.yCoord,
											this.zCoord + 1);
									this.worldObj.updateLightByType(EnumSkyBlock.Block, this.xCoord, this.yCoord,
											this.zCoord - 1);
									this.issueTextureUpdate();
									this.sendBlockEvent((byte) 7, this.oLightValue = this.mLightValue);
								}
							}
							if (this.mNeedsBlockUpdate) {
								this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord,
										this.getBlockOffset(0, 0, 0));
								this.mNeedsBlockUpdate = false;
								break;
							}
							break;
						}
					}
				}
			}
		} catch (Throwable e) {
			e.printStackTrace(GT_Log.err);
		}
		if (aSideServer && this.hasValidMetaTileEntity()) {
			tTime = System.currentTimeMillis() - tTime;
			if (this.mTimeStatistics.length > 0) {
				this.mTimeStatistics[this.mTimeStatisticsIndex = (this.mTimeStatisticsIndex + 1)
						% this.mTimeStatistics.length] = (int) tTime;
			}
			if (tTime > 0L && tTime > GregTech_API.MILLISECOND_THRESHOLD_UNTIL_LAG_WARNING && this.mTickTimer > 1000L
					&& this.getMetaTileEntity().doTickProfilingMessageDuringThisTick()
					&& this.mLagWarningCount++ < 10) {
				System.out.println("WARNING: Possible Lag Source at [" + this.xCoord + ", " + this.yCoord + ", "
						+ this.zCoord + "] in Dimension " + this.worldObj.provider.dimensionId + " with " + tTime
						+ "ms caused by an instance of " + this.getMetaTileEntity().getClass());
			}
		}
		final boolean mWorkUpdate = false;
		this.mRunningThroughTick = mWorkUpdate;
		this.mInventoryChanged = mWorkUpdate;
		this.mWorkUpdate = mWorkUpdate;*/
	}

	private void onFirstTick(TilePoweredGT tilePoweredGT) {
		// TODO Auto-generated method stub
		
	}

	private boolean hasValidMetaTileEntity() {
		return Utils.invertBoolean(isDead());
	}

	public void issueBlockUpdate() {
		this.mNeedsBlockUpdate = true;
	}

	public void issueClientUpdate() {
		this.mSendClientData = true;
	}
	
	public Packet getDescriptionPacket() {
		this.issueClientUpdate();
		return null;
	}
	
}
