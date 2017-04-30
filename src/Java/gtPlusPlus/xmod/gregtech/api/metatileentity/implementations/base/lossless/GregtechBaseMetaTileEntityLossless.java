package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.lossless;

import static gregtech.api.enums.GT_Values.NW;
import static gregtech.api.enums.GT_Values.V;

import java.util.*;

import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IEnergyConnected;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.net.GT_Packet_TileEntity;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.util.*;
import ic2.api.Direction;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFire;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * <p/>
 * This is the main TileEntity for EVERYTHING.
 */
public class GregtechBaseMetaTileEntityLossless extends BaseTileEntity implements IGregTechTileEntity {
	private final GT_CoverBehavior[] mCoverBehaviors = new GT_CoverBehavior[]{GregTech_API.sNoBehavior, GregTech_API.sNoBehavior, GregTech_API.sNoBehavior, GregTech_API.sNoBehavior, GregTech_API.sNoBehavior, GregTech_API.sNoBehavior};
	protected MetaTileEntity mMetaTileEntity;
	protected long mStoredEnergy = 0, mStoredSteam = 0;
	protected int mAverageEUInputIndex = 0, mAverageEUOutputIndex = 0;
	protected boolean mReleaseEnergy = false;
	protected int[] mAverageEUInput = new int[]{0, 0, 0, 0, 0}, mAverageEUOutput = new int[]{0, 0, 0, 0, 0};
	private final boolean[] mActiveEUInputs = new boolean[]{false, false, false, false, false, false}, mActiveEUOutputs = new boolean[]{false, false, false, false, false, false};
	private byte[] mSidedRedstone = new byte[]{15, 15, 15, 15, 15, 15};
	private int[] mCoverSides = new int[]{0, 0, 0, 0, 0, 0}, mCoverData = new int[]{0, 0, 0, 0, 0, 0};
	private final int[] mTimeStatistics = new int[GregTech_API.TICKS_FOR_LAG_AVERAGING];
	private boolean mHasEnoughEnergy = true, mRunningThroughTick = false, mInputDisabled = false, mOutputDisabled = false, mMuffler = false, mLockUpgrade = false, mActive = false, mRedstone = false, mWorkUpdate = false, mSteamConverter = false, mInventoryChanged = false, mWorks = true, mNeedsUpdate = true, mNeedsBlockUpdate = true, mSendClientData = false, oRedstone = false;
	private byte mColor = 0, oColor = 0, mStrongRedstone = 0, oRedstoneData = 63, oTextureData = 0, oUpdateData = 0, oLightValueClient = -1, oLightValue = -1, mLightValue = 0, mOtherUpgrades = 0, mFacing = 0, oFacing = 0, mWorkData = 0;
	private int mDisplayErrorCode = 0, oX = 0, oY = 0, oZ = 0, mTimeStatisticsIndex = 0, mLagWarningCount = 0;
	private short mID = 0;
	private long mTickTimer = 0, oOutput = 0, mAcceptedAmperes = Long.MAX_VALUE;
	private String mOwnerName = "";
	private NBTTagCompound mRecipeStuff = new NBTTagCompound();

	public GregtechBaseMetaTileEntityLossless() {
	}

	@Override
	public void writeToNBT(final NBTTagCompound aNBT) {
		try {
			super.writeToNBT(aNBT);
		} catch (final Throwable e) {
			GT_Log.err.println("Encountered CRITICAL ERROR while saving MetaTileEntity, the Chunk whould've been corrupted by now, but I prevented that. Please report immidietly to GregTech Intergalactical!!!");
			e.printStackTrace(GT_Log.err);
		}
		try {
			aNBT.setInteger("mID", this.mID);
			aNBT.setLong("mStoredSteam", this.mStoredSteam);
			aNBT.setLong("mStoredEnergy", this.mStoredEnergy);
			aNBT.setIntArray("mCoverData", this.mCoverData);
			aNBT.setIntArray("mCoverSides", this.mCoverSides);
			aNBT.setByteArray("mRedstoneSided", this.mSidedRedstone);
			aNBT.setByte("mColor", this.mColor);
			aNBT.setByte("mLightValue", this.mLightValue);
			aNBT.setByte("mOtherUpgrades", this.mOtherUpgrades);
			aNBT.setByte("mWorkData", this.mWorkData);
			aNBT.setByte("mStrongRedstone", this.mStrongRedstone);
			aNBT.setShort("mFacing", this.mFacing);
			aNBT.setString("mOwnerName", this.mOwnerName);
			aNBT.setBoolean("mLockUpgrade", this.mLockUpgrade);
			aNBT.setBoolean("mMuffler", this.mMuffler);
			aNBT.setBoolean("mSteamConverter", this.mSteamConverter);
			aNBT.setBoolean("mActive", this.mActive);
			aNBT.setBoolean("mRedstone", this.mRedstone);
			aNBT.setBoolean("mWorks", !this.mWorks);
			aNBT.setBoolean("mInputDisabled", this.mInputDisabled);
			aNBT.setBoolean("mOutputDisabled", this.mOutputDisabled);
			aNBT.setTag("GT.CraftingComponents", this.mRecipeStuff);
		} catch (final Throwable e) {
			GT_Log.err.println("Encountered CRITICAL ERROR while saving MetaTileEntity, the Chunk whould've been corrupted by now, but I prevented that. Please report immidietly to GregTech Intergalactical!!!");
			e.printStackTrace(GT_Log.err);
		}
		try {
			if (this.hasValidMetaTileEntity()) {
				final NBTTagList tItemList = new NBTTagList();
				for (int i = 0; i < this.mMetaTileEntity.getRealInventory().length; i++) {
					final ItemStack tStack = this.mMetaTileEntity.getRealInventory()[i];
					if (tStack != null) {
						final NBTTagCompound tTag = new NBTTagCompound();
						tTag.setInteger("IntSlot", i);
						tStack.writeToNBT(tTag);
						tItemList.appendTag(tTag);
					}
				}
				aNBT.setTag("Inventory", tItemList);

				try {
					this.mMetaTileEntity.saveNBTData(aNBT);
				} catch (final Throwable e) {
					GT_Log.err.println("Encountered CRITICAL ERROR while saving MetaTileEntity, the Chunk whould've been corrupted by now, but I prevented that. Please report immidietly to GregTech Intergalactical!!!");
					e.printStackTrace(GT_Log.err);
				}
			}
		} catch (final Throwable e) {
			GT_Log.err.println("Encountered CRITICAL ERROR while saving MetaTileEntity, the Chunk whould've been corrupted by now, but I prevented that. Please report immidietly to GregTech Intergalactical!!!");
			e.printStackTrace(GT_Log.err);
		}
	}

	@Override
	public void readFromNBT(final NBTTagCompound aNBT) {
		super.readFromNBT(aNBT);
		this.setInitialValuesAsNBT(aNBT, (short) 0);
	}

	@Override
	public void setInitialValuesAsNBT(final NBTTagCompound aNBT, final short aID) {
		if (aNBT == null) {
			if (aID > 0) {
				this.mID = aID;
			} else {
				this.mID = this.mID > 0 ? this.mID : 0;
			}
			if (this.mID != 0) {
				this.createNewMetatileEntity(this.mID);
			}
			this.mSidedRedstone = (this.hasValidMetaTileEntity() && this.mMetaTileEntity.hasSidedRedstoneOutputBehavior() ? new byte[]{0, 0, 0, 0, 0, 0} : new byte[]{15, 15, 15, 15, 15, 15});
		} else {
			if (aID <= 0) {
				this.mID = (short) aNBT.getInteger("mID");
			} else {
				this.mID = aID;
			}
			this.mStoredSteam = aNBT.getInteger("mStoredSteam");
			this.mStoredEnergy = aNBT.getInteger("mStoredEnergy");
			this.mColor = aNBT.getByte("mColor");
			this.mLightValue = aNBT.getByte("mLightValue");
			this.mWorkData = aNBT.getByte("mWorkData");
			this.mStrongRedstone = aNBT.getByte("mStrongRedstone");
			this.mFacing = this.oFacing = (byte) aNBT.getShort("mFacing");
			this.mOwnerName = aNBT.getString("mOwnerName");
			this.mLockUpgrade = aNBT.getBoolean("mLockUpgrade");
			this.mMuffler = aNBT.getBoolean("mMuffler");
			this.mSteamConverter = aNBT.getBoolean("mSteamConverter");
			this.mActive = aNBT.getBoolean("mActive");
			this.mRedstone = aNBT.getBoolean("mRedstone");
			this.mWorks = !aNBT.getBoolean("mWorks");
			this.mInputDisabled = aNBT.getBoolean("mInputDisabled");
			this.mOutputDisabled = aNBT.getBoolean("mOutputDisabled");
			this.mOtherUpgrades = (byte) (aNBT.getByte("mOtherUpgrades") + aNBT.getByte("mBatteries") + aNBT.getByte("mLiBatteries"));
			this.mCoverSides = aNBT.getIntArray("mCoverSides");
			this.mCoverData = aNBT.getIntArray("mCoverData");
			this.mSidedRedstone = aNBT.getByteArray("mRedstoneSided");
			this.mRecipeStuff = aNBT.getCompoundTag("GT.CraftingComponents");

			if (this.mCoverData.length != 6) {
				this.mCoverData = new int[]{0, 0, 0, 0, 0, 0};
			}
			if (this.mCoverSides.length != 6) {
				this.mCoverSides = new int[]{0, 0, 0, 0, 0, 0};
			}
			if (this.mSidedRedstone.length != 6) {
				if (this.hasValidMetaTileEntity() && this.mMetaTileEntity.hasSidedRedstoneOutputBehavior()) {
					this.mSidedRedstone = new byte[]{0, 0, 0, 0, 0, 0};
				} else {
					this.mSidedRedstone = new byte[]{15, 15, 15, 15, 15, 15};
				}
			}

			for (byte i = 0; i < 6; i++) {
				this.mCoverBehaviors[i] = GregTech_API.getCoverBehavior(this.mCoverSides[i]);
			}

			if ((this.mID != 0) && this.createNewMetatileEntity(this.mID)) {
				final NBTTagList tItemList = aNBT.getTagList("Inventory", 10);
				for (int i = 0; i < tItemList.tagCount(); i++) {
					final NBTTagCompound tTag = tItemList.getCompoundTagAt(i);
					final int tSlot = tTag.getInteger("IntSlot");
					if ((tSlot >= 0) && (tSlot < this.mMetaTileEntity.getRealInventory().length)) {
						this.mMetaTileEntity.getRealInventory()[tSlot] = GT_Utility.loadItem(tTag);
					}
				}

				try {
					this.mMetaTileEntity.loadNBTData(aNBT);
				} catch (final Throwable e) {
					GT_Log.err.println("Encountered Exception while loading MetaTileEntity, the Server should've crashed now, but I prevented that. Please report immidietly to GregTech Intergalactical!!!");
					e.printStackTrace(GT_Log.err);
				}
			}
		}

		if (this.mCoverData.length != 6) {
			this.mCoverData = new int[]{0, 0, 0, 0, 0, 0};
		}
		if (this.mCoverSides.length != 6) {
			this.mCoverSides = new int[]{0, 0, 0, 0, 0, 0};
		}
		if (this.mSidedRedstone.length != 6) {
			if (this.hasValidMetaTileEntity() && this.mMetaTileEntity.hasSidedRedstoneOutputBehavior()) {
				this.mSidedRedstone = new byte[]{0, 0, 0, 0, 0, 0};
			} else {
				this.mSidedRedstone = new byte[]{15, 15, 15, 15, 15, 15};
			}
		}

		for (byte i = 0; i < 6; i++) {
			this.mCoverBehaviors[i] = GregTech_API.getCoverBehavior(this.mCoverSides[i]);
		}
	}

	private boolean createNewMetatileEntity(final short aID) {
		if ((aID <= 0) || (aID >= GregTech_API.METATILEENTITIES.length) || (GregTech_API.METATILEENTITIES[aID] == null)) {
			GT_Log.err.println("MetaID " + aID + " not loadable => locking TileEntity!");
		} else {
			if (aID != 0) {
				if (this.hasValidMetaTileEntity()) {
					this.mMetaTileEntity.setBaseMetaTileEntity(null);
				}
				GregTech_API.METATILEENTITIES[aID].newMetaEntity(this).setBaseMetaTileEntity(this);
				this.mTickTimer = 0;
				this.mID = aID;
				return true;
			}
		}
		return false;
	}

	/**
	 * Used for ticking special BaseMetaTileEntities, which need that for Energy Conversion
	 * It's called right before onPostTick()
	 */
	public void updateStatus() {
		//
	}

	/**
	 * Called when trying to charge Items
	 */
	public void chargeItem(final ItemStack aStack) {
		this.decreaseStoredEU(GT_ModHandler.chargeElectricItem(aStack, (int) Math.min(Integer.MAX_VALUE, this.getStoredEU()), (int) Math.min(Integer.MAX_VALUE, this.mMetaTileEntity.getOutputTier()), false, false), true);
	}

	/**
	 * Called when trying to discharge Items
	 */
	public void dischargeItem(final ItemStack aStack) {
		this.increaseStoredEnergyUnits(GT_ModHandler.dischargeElectricItem(aStack, (int) Math.min(Integer.MAX_VALUE, this.getEUCapacity() - this.getStoredEU()), (int) Math.min(Integer.MAX_VALUE, this.mMetaTileEntity.getInputTier()), false, false, false), true);
	}

	@Override
	public void updateEntity() {
		super.updateEntity();

		if (!this.hasValidMetaTileEntity()) {
			if (this.mMetaTileEntity == null) {
				return;
			}
			this.mMetaTileEntity.setBaseMetaTileEntity(this);
		}

		this.mRunningThroughTick = true;
		long tTime = System.currentTimeMillis();

		for (int tCode = 0; this.hasValidMetaTileEntity() && (tCode >= 0); ) {
			try {
				switch (tCode) {
				case 0:
					tCode++;
					if (this.mTickTimer++ == 0) {
						this.oX = this.xCoord;
						this.oY = this.yCoord;
						this.oZ = this.zCoord;
						if (this.isServerSide()) {
							for (byte i = 0; i < 6; i++) {
								if (this.getCoverIDAtSide(i) != 0) {
									if (!this.mMetaTileEntity.allowCoverOnSide(i, new GT_ItemStack(this.getCoverIDAtSide(i)))) {
										this.dropCover(i, i, true);
									}
								}
							}
						}

						this.worldObj.markTileEntityChunkModified(this.xCoord, this.yCoord, this.zCoord, this);

						this.mMetaTileEntity.onFirstTick(this);
						if (!this.hasValidMetaTileEntity()) {
							this.mRunningThroughTick = false;
							return;
						}
					}
				case 1:
					tCode++;
					if (this.isClientSide()) {
						if (this.mColor != this.oColor) {
							this.mMetaTileEntity.onColorChangeClient(this.oColor = this.mColor);
							this.issueTextureUpdate();
						}

						if (this.mLightValue != this.oLightValueClient) {
							this.worldObj.setLightValue(EnumSkyBlock.Block, this.xCoord, this.yCoord, this.zCoord, this.mLightValue);
							this.worldObj.updateLightByType(EnumSkyBlock.Block, this.xCoord, this.yCoord, this.zCoord);
							this.worldObj.updateLightByType(EnumSkyBlock.Block, this.xCoord + 1, this.yCoord, this.zCoord);
							this.worldObj.updateLightByType(EnumSkyBlock.Block, this.xCoord - 1, this.yCoord, this.zCoord);
							this.worldObj.updateLightByType(EnumSkyBlock.Block, this.xCoord, this.yCoord + 1, this.zCoord);
							this.worldObj.updateLightByType(EnumSkyBlock.Block, this.xCoord, this.yCoord - 1, this.zCoord);
							this.worldObj.updateLightByType(EnumSkyBlock.Block, this.xCoord, this.yCoord, this.zCoord + 1);
							this.worldObj.updateLightByType(EnumSkyBlock.Block, this.xCoord, this.yCoord, this.zCoord - 1);
							this.oLightValueClient = this.mLightValue;
							this.issueTextureUpdate();
						}

						if (this.mNeedsUpdate) {
							this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
							//worldObj.func_147479_m(xCoord, yCoord, zCoord);
							this.mNeedsUpdate = false;
						}
					}
				case 2:
				case 3:
				case 4:
				case 5:
				case 6:
				case 7:
					if (this.isServerSide() && (this.mTickTimer > 10)) {
						for (byte i = (byte) (tCode - 2); i < 6; i++) {
							if (this.getCoverIDAtSide(i) != 0) {
								tCode++;
								final GT_CoverBehavior tCover = this.getCoverBehaviorAtSide(i);
								final int tCoverTickRate = tCover.getTickRate(i, this.getCoverIDAtSide(i), this.mCoverData[i], this);
								if ((tCoverTickRate > 0) && ((this.mTickTimer % tCoverTickRate) == 0)) {
									this.mCoverData[i] = tCover.doCoverThings(i, this.getInputRedstoneSignal(i), this.getCoverIDAtSide(i), this.mCoverData[i], this, this.mTickTimer);
									if (!this.hasValidMetaTileEntity()) {
										this.mRunningThroughTick = false;
										return;
									}
								}
							}
						}

					}
				case 8:
					tCode = 9;
					if (this.isServerSide()) {
						if (++this.mAverageEUInputIndex >= this.mAverageEUInput.length) {
							this.mAverageEUInputIndex = 0;
						}
						if (++this.mAverageEUOutputIndex >= this.mAverageEUOutput.length) {
							this.mAverageEUOutputIndex = 0;
						}

						this.mAverageEUInput[this.mAverageEUInputIndex] = 0;
						this.mAverageEUOutput[this.mAverageEUOutputIndex] = 0;
					}
				case 9:
					tCode++;
					this.mMetaTileEntity.onPreTick(this, this.mTickTimer);
					if (!this.hasValidMetaTileEntity()) {
						this.mRunningThroughTick = false;
						return;
					}
				case 10:
					tCode++;
					if (this.isServerSide()) {
						if ((this.mRedstone != this.oRedstone) || (this.mTickTimer == 10)) {
							for (byte i = 0; i < 6; i++) {
								this.mCoverBehaviors[i] = GregTech_API.getCoverBehavior(this.mCoverSides[i]);
							}
							this.oRedstone = this.mRedstone;
							this.issueBlockUpdate();
						}

						if ((this.xCoord != this.oX) || (this.yCoord != this.oY) || (this.zCoord != this.oZ)) {
							this.oX = this.xCoord;
							this.oY = this.yCoord;
							this.oZ = this.zCoord;
							this.issueClientUpdate();
							this.clearTileEntityBuffer();
						}

						if (this.mFacing != this.oFacing) {
							this.oFacing = this.mFacing;
							for (byte i = 0; i < 6; i++) {
								if (this.getCoverIDAtSide(i) != 0) {
									if (!this.mMetaTileEntity.allowCoverOnSide(i, new GT_ItemStack(this.getCoverIDAtSide(i)))) {
										this.dropCover(i, i, true);
									}
								}
							}
							this.issueBlockUpdate();
						}

						if ((this.mTickTimer > 20) && this.mMetaTileEntity.isElectric()) {
							this.mAcceptedAmperes = 0;

							if (this.getOutputVoltage() != this.oOutput) {
								this.oOutput = this.getOutputVoltage();
							}

							if (this.mMetaTileEntity.isEnetOutput() || this.mMetaTileEntity.isEnetInput()) {
								for (byte i = 0; i < 6; i++) {
									boolean
									temp = this.isEnergyInputSide(i);
									if (temp != this.mActiveEUInputs[i]) {
										this.mActiveEUInputs[i] = temp;
									}
									temp = this.isEnergyOutputSide(i);
									if (temp != this.mActiveEUOutputs[i]) {
										this.mActiveEUOutputs[i] = temp;
									}
								}
							}



							if (this.mMetaTileEntity.isEnetOutput() && (this.oOutput > 0)) {
								final long tOutputVoltage = this.oOutput, tUsableAmperage = Math.min(this.getOutputAmperage(), (this.getStoredEU() - this.mMetaTileEntity.getMinimumStoredEU()) / tOutputVoltage);
								if (tUsableAmperage > 0) {
									final long tEU = tOutputVoltage * IEnergyConnected.Util.emitEnergyToNetwork(this.oOutput, tUsableAmperage, this);
									this.mAverageEUOutput[this.mAverageEUOutputIndex] += tEU;
									this.decreaseStoredEU(tEU, true);
								}
							}


							if (this.getEUCapacity() > 0) {
								if (GregTech_API.sMachineFireExplosions && (this.getRandomNumber(1000) == 0)) {
									final Block tBlock = this.getBlockAtSide((byte) this.getRandomNumber(6));
									if ((tBlock != null) && (tBlock instanceof BlockFire)) {
										this.doEnergyExplosion();
									}
								}

								if (!this.hasValidMetaTileEntity()) {
									this.mRunningThroughTick = false;
									return;
								}

								if (this.getRandomNumber(1000) == 0) {
									if (((this.getCoverIDAtSide((byte) 1) == 0) && ((this.worldObj.getPrecipitationHeight(this.xCoord, this.zCoord) - 2) < this.yCoord))
											|| ((this.getCoverIDAtSide((byte) 2) == 0) && ((this.worldObj.getPrecipitationHeight(this.xCoord, this.zCoord - 1) - 1) < this.yCoord))
											|| ((this.getCoverIDAtSide((byte) 3) == 0) && ((this.worldObj.getPrecipitationHeight(this.xCoord, this.zCoord + 1) - 1) < this.yCoord))
											|| ((this.getCoverIDAtSide((byte) 4) == 0) && ((this.worldObj.getPrecipitationHeight(this.xCoord - 1, this.zCoord) - 1) < this.yCoord))
											|| ((this.getCoverIDAtSide((byte) 5) == 0) && ((this.worldObj.getPrecipitationHeight(this.xCoord + 1, this.zCoord) - 1) < this.yCoord))) {
										if (GregTech_API.sMachineRainExplosions && this.worldObj.isRaining() && (this.getBiome().rainfall > 0)) {
											if (this.getRandomNumber(10) == 0) {
												try {
													GT_Mod.achievements.issueAchievement(this.getWorldObj().getPlayerEntityByName(this.mOwnerName), "badweather");
												} catch (final Exception e) {
												}
												this.doEnergyExplosion();
											} else {
												this.setOnFire();
											}
										}
										if (!this.hasValidMetaTileEntity()) {
											this.mRunningThroughTick = false;
											return;
										}
										if (GregTech_API.sMachineThunderExplosions && this.worldObj.isThundering() && (this.getBiome().rainfall > 0) && (this.getRandomNumber(3) == 0)) {
											try {
												GT_Mod.achievements.issueAchievement(this.getWorldObj().getPlayerEntityByName(this.mOwnerName), "badweather");
											} catch (final Exception e) {
											}
											this.doEnergyExplosion();
										}
									}
								}
							}
						}

						if (!this.hasValidMetaTileEntity()) {
							this.mRunningThroughTick = false;
							return;
						}
					}
				case 11:
					tCode++;
					if (this.isServerSide()) {
						if ((this.mMetaTileEntity.dechargerSlotCount() > 0) && (this.getStoredEU() < this.getEUCapacity())) {
							for (int i = this.mMetaTileEntity.dechargerSlotStartIndex(), k = this.mMetaTileEntity.dechargerSlotCount() + i; i < k; i++) {
								if ((this.mMetaTileEntity.mInventory[i] != null) && (this.getStoredEU() < this.getEUCapacity())) {
									this.dischargeItem(this.mMetaTileEntity.mInventory[i]);
									if (this.mMetaTileEntity.mInventory[i].stackSize <= 0) {
										this.mMetaTileEntity.mInventory[i] = null;
									}
									this.mInventoryChanged = true;
								}
							}
						}
					}
				case 12:
					tCode++;
					if (this.isServerSide()) {
						if ((this.mMetaTileEntity.rechargerSlotCount() > 0) && (this.getStoredEU() > 0)) {
							for (int i = this.mMetaTileEntity.rechargerSlotStartIndex(), k = this.mMetaTileEntity.rechargerSlotCount() + i; i < k; i++) {
								if ((this.getStoredEU() > 0) && (this.mMetaTileEntity.mInventory[i] != null)) {
									this.chargeItem(this.mMetaTileEntity.mInventory[i]);
									if (this.mMetaTileEntity.mInventory[i].stackSize <= 0) {
										this.mMetaTileEntity.mInventory[i] = null;
									}
									this.mInventoryChanged = true;
								}
							}
						}
					}
				case 13:
					tCode++;
					this.updateStatus();
					if (!this.hasValidMetaTileEntity()) {
						this.mRunningThroughTick = false;
						return;
					}
				case 14:
					tCode++;
					this.mMetaTileEntity.onPostTick(this, this.mTickTimer);
					if (!this.hasValidMetaTileEntity()) {
						this.mRunningThroughTick = false;
						return;
					}
				case 15:
					tCode++;
					if (this.isServerSide()) {
						if ((this.mTickTimer % 10) == 0) {
							if (this.mSendClientData) {
								NW.sendPacketToAllPlayersInRange(this.worldObj, new GT_Packet_TileEntity(this.xCoord, (short) this.yCoord, this.zCoord, this.mID, this.mCoverSides[0], this.mCoverSides[1], this.mCoverSides[2], this.mCoverSides[3], this.mCoverSides[4], this.mCoverSides[5], this.oTextureData = (byte) ((this.mFacing & 7) | (this.mActive ? 8 : 0) | (this.mRedstone ? 16 : 0) | (this.mLockUpgrade ? 32 : 0)), this.oUpdateData = this.hasValidMetaTileEntity() ? this.mMetaTileEntity.getUpdateData() : 0, this.oRedstoneData = (byte) (((this.mSidedRedstone[0] > 0) ? 1 : 0) | ((this.mSidedRedstone[1] > 0) ? 2 : 0) | ((this.mSidedRedstone[2] > 0) ? 4 : 0) | ((this.mSidedRedstone[3] > 0) ? 8 : 0) | ((this.mSidedRedstone[4] > 0) ? 16 : 0) | ((this.mSidedRedstone[5] > 0) ? 32 : 0)), this.oColor = this.mColor), this.xCoord, this.zCoord);
								this.mSendClientData = false;
							}
						}

						if (this.mTickTimer > 10) {
							byte tData = (byte) ((this.mFacing & 7) | (this.mActive ? 8 : 0) | (this.mRedstone ? 16 : 0) | (this.mLockUpgrade ? 32 : 0));
							if (tData != this.oTextureData) {
								this.sendBlockEvent((byte) 0, this.oTextureData = tData);
							}
							tData = this.mMetaTileEntity.getUpdateData();
							if (tData != this.oUpdateData) {
								this.sendBlockEvent((byte) 1, this.oUpdateData = tData);
							}
							if (this.mColor != this.oColor) {
								this.sendBlockEvent((byte) 2, this.oColor = this.mColor);
							}
							tData = (byte) (((this.mSidedRedstone[0] > 0) ? 1 : 0) | ((this.mSidedRedstone[1] > 0) ? 2 : 0) | ((this.mSidedRedstone[2] > 0) ? 4 : 0) | ((this.mSidedRedstone[3] > 0) ? 8 : 0) | ((this.mSidedRedstone[4] > 0) ? 16 : 0) | ((this.mSidedRedstone[5] > 0) ? 32 : 0));
							if (tData != this.oRedstoneData) {
								this.sendBlockEvent((byte) 3, this.oRedstoneData = tData);
							}
							if (this.mLightValue != this.oLightValue) {
								this.worldObj.setLightValue(EnumSkyBlock.Block, this.xCoord, this.yCoord, this.zCoord, this.mLightValue);
								this.worldObj.updateLightByType(EnumSkyBlock.Block, this.xCoord, this.yCoord, this.zCoord);
								this.worldObj.updateLightByType(EnumSkyBlock.Block, this.xCoord + 1, this.yCoord, this.zCoord);
								this.worldObj.updateLightByType(EnumSkyBlock.Block, this.xCoord - 1, this.yCoord, this.zCoord);
								this.worldObj.updateLightByType(EnumSkyBlock.Block, this.xCoord, this.yCoord + 1, this.zCoord);
								this.worldObj.updateLightByType(EnumSkyBlock.Block, this.xCoord, this.yCoord - 1, this.zCoord);
								this.worldObj.updateLightByType(EnumSkyBlock.Block, this.xCoord, this.yCoord, this.zCoord + 1);
								this.worldObj.updateLightByType(EnumSkyBlock.Block, this.xCoord, this.yCoord, this.zCoord - 1);
								this.issueTextureUpdate();
								this.sendBlockEvent((byte) 7, this.oLightValue = this.mLightValue);
							}
						}

						if (this.mNeedsBlockUpdate) {
							this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.getBlockOffset(0, 0, 0));
							this.mNeedsBlockUpdate = false;
						}
					}
				default:
					tCode = -1;
					break;
				}
			} catch (final Throwable e) {
				GT_Log.err.println("Encountered Exception while ticking MetaTileEntity in Step " + (tCode - 1) + ". The Game should've crashed now, but I prevented that. Please report immidietly to GregTech Intergalactical!!!");
				e.printStackTrace(GT_Log.err);
			}
		}

		if (this.isServerSide() && this.hasValidMetaTileEntity()) {
			tTime = System.currentTimeMillis() - tTime;
			if (this.mTimeStatistics.length > 0) {
				this.mTimeStatistics[this.mTimeStatisticsIndex = (this.mTimeStatisticsIndex + 1) % this.mTimeStatistics.length] = (int) tTime;
			}
			if ((tTime > 0) && (tTime > GregTech_API.MILLISECOND_THRESHOLD_UNTIL_LAG_WARNING) && (this.mTickTimer > 1000) && this.getMetaTileEntity().doTickProfilingMessageDuringThisTick() && (this.mLagWarningCount++ < 10)) {
				System.out.println("WARNING: Possible Lag Source at [" + this.xCoord + ", " + this.yCoord + ", " + this.zCoord + "] in Dimension " + this.worldObj.provider.dimensionId + " with " + tTime + "ms caused by an instance of " + this.getMetaTileEntity().getClass());
			}
		}

		this.mWorkUpdate = this.mInventoryChanged = this.mRunningThroughTick = false;
	}

	@Override
	public Packet getDescriptionPacket() {
		this.issueClientUpdate();
		return null;
	}

	public final void receiveMetaTileEntityData(final short aID, final int aCover0, final int aCover1, final int aCover2, final int aCover3, final int aCover4, final int aCover5, final byte aTextureData, final byte aUpdateData, final byte aRedstoneData, final byte aColorData) {
		this.issueTextureUpdate();
		if ((this.mID != aID) && (aID > 0)) {
			this.mID = aID;
			this.createNewMetatileEntity(this.mID);
		}

		this.mCoverSides[0] = aCover0;
		this.mCoverSides[1] = aCover1;
		this.mCoverSides[2] = aCover2;
		this.mCoverSides[3] = aCover3;
		this.mCoverSides[4] = aCover4;
		this.mCoverSides[5] = aCover5;

		for (byte i = 0; i < 6; i++) {
			this.mCoverBehaviors[i] = GregTech_API.getCoverBehavior(this.mCoverSides[i]);
		}

		this.receiveClientEvent(0, aTextureData);
		this.receiveClientEvent(1, aUpdateData);
		this.receiveClientEvent(2, aColorData);
		this.receiveClientEvent(3, aRedstoneData);
	}

	@Override
	public boolean receiveClientEvent(final int aEventID, int aValue) {
		super.receiveClientEvent(aEventID, aValue);

		if (this.hasValidMetaTileEntity()) {
			try {
				this.mMetaTileEntity.receiveClientEvent((byte) aEventID, (byte) aValue);
			} catch (final Throwable e) {
				GT_Log.err.println("Encountered Exception while receiving Data from the Server, the Client should've been crashed by now, but I prevented that. Please report immidietly to GregTech Intergalactical!!!");
				e.printStackTrace(GT_Log.err);
			}
		}

		if (this.isClientSide()) {
			this.issueTextureUpdate();
			switch (aEventID) {
			case 0:
				this.mFacing = (byte) (aValue & 7);
				this.mActive = ((aValue & 8) != 0);
				this.mRedstone = ((aValue & 16) != 0);
				//mLockUpgrade	= ((aValue&32) != 0);
				break;
			case 1:
				if (this.hasValidMetaTileEntity()) {
					this.mMetaTileEntity.onValueUpdate((byte) aValue);
				}
				break;
			case 2:
				if ((aValue > 16) || (aValue < 0)) {
					aValue = 0;
				}
				this.mColor = (byte) aValue;
				break;
			case 3:
				this.mSidedRedstone[0] = (byte) ((aValue & 1) > 0 ? 15 : 0);
				this.mSidedRedstone[1] = (byte) ((aValue & 2) > 0 ? 15 : 0);
				this.mSidedRedstone[2] = (byte) ((aValue & 4) > 0 ? 15 : 0);
				this.mSidedRedstone[3] = (byte) ((aValue & 8) > 0 ? 15 : 0);
				this.mSidedRedstone[4] = (byte) ((aValue & 16) > 0 ? 15 : 0);
				this.mSidedRedstone[5] = (byte) ((aValue & 32) > 0 ? 15 : 0);
				break;
			case 4:
				if (this.hasValidMetaTileEntity() && (this.mTickTimer > 20)) {
					this.mMetaTileEntity.doSound((byte) aValue, this.xCoord + 0.5, this.yCoord + 0.5, this.zCoord + 0.5);
				}
				break;
			case 5:
				if (this.hasValidMetaTileEntity() && (this.mTickTimer > 20)) {
					this.mMetaTileEntity.startSoundLoop((byte) aValue, this.xCoord + 0.5, this.yCoord + 0.5, this.zCoord + 0.5);
				}
				break;
			case 6:
				if (this.hasValidMetaTileEntity() && (this.mTickTimer > 20)) {
					this.mMetaTileEntity.stopSoundLoop((byte) aValue, this.xCoord + 0.5, this.yCoord + 0.5, this.zCoord + 0.5);
				}
				break;
			case 7:
				this.mLightValue = (byte) aValue;
				break;
			}
		}
		return true;
	}

	public ArrayList<String> getDebugInfo(final EntityPlayer aPlayer, final int aLogLevel) {
		final ArrayList<String> tList = new ArrayList<>();
		if (aLogLevel > 2) {
			tList.add("Meta-ID: " + this.mID + (this.canAccessData() ? " valid" : " invalid") + (this.mMetaTileEntity == null ? " MetaTileEntity == null!" : " "));
		}
		if (aLogLevel > 1) {
			if (this.mTimeStatistics.length > 0) {
				double tAverageTime = 0;
				for (final int tTime : this.mTimeStatistics) {
					tAverageTime += tTime;
				}
				tList.add("This particular TileEntity has caused an average CPU-load of ~" + (tAverageTime / this.mTimeStatistics.length) + "ms over the last " + this.mTimeStatistics.length + " ticks.");
			}
			if (this.mLagWarningCount > 0) {
				tList.add("This TileEntity has also caused " + (this.mLagWarningCount >= 10 ? "more than 10" : this.mLagWarningCount) + " Lag Spike Warnings (anything taking longer than " + GregTech_API.MILLISECOND_THRESHOLD_UNTIL_LAG_WARNING + "ms) on the Server.");
			}
			tList.add("Is" + (this.mMetaTileEntity.isAccessAllowed(aPlayer) ? " " : " not ") + "accessible for you");
		}
		if (aLogLevel > 0) {
			if ((this.getSteamCapacity() > 0) && this.hasSteamEngineUpgrade()) {
				tList.add(this.getStoredSteam() + " of " + this.getSteamCapacity() + " Steam");
			}
			tList.add("Machine is " + (this.mActive ? "active" : "inactive"));
			if (!this.mHasEnoughEnergy) {
				tList.add("ATTENTION: This Device consumes Energy at a higher Rate than you input. You could insert more to speed up the process.");
			}
		}
		return this.mMetaTileEntity.getSpecialDebugInfo(this, aPlayer, aLogLevel, tList);
	}

	@Override
	public void issueTextureUpdate() {
		this.mNeedsUpdate = true;
	}

	@Override
	public void issueBlockUpdate() {
		this.mNeedsBlockUpdate = true;
	}

	@Override
	public void issueClientUpdate() {
		this.mSendClientData = true;
	}

	@Override
	public void issueCoverUpdate(final byte aSide) {
		this.issueClientUpdate();
	}

	@Override
	public byte getStrongestRedstone() {
		return (byte) Math.max(this.getInternalInputRedstoneSignal((byte) 0), Math.max(this.getInternalInputRedstoneSignal((byte) 1), Math.max(this.getInternalInputRedstoneSignal((byte) 2), Math.max(this.getInternalInputRedstoneSignal((byte) 3), Math.max(this.getInternalInputRedstoneSignal((byte) 4), this.getInternalInputRedstoneSignal((byte) 5))))));
	}

	@Override
	public boolean getRedstone() {
		return this.getRedstone((byte) 0) || this.getRedstone((byte) 1) || this.getRedstone((byte) 2) || this.getRedstone((byte) 3) || this.getRedstone((byte) 4) || this.getRedstone((byte) 5);
	}

	@Override
	public boolean getRedstone(final byte aSide) {
		return this.getInternalInputRedstoneSignal(aSide) > 0;
	}

	public ITexture getCoverTexture(final byte aSide) {
		return GregTech_API.sCovers.get(new GT_ItemStack(this.getCoverIDAtSide(aSide)));
	}

	@Override
	public boolean isGivingInformation() {
		if (this.canAccessData()) {
			return this.mMetaTileEntity.isGivingInformation();
		}
		return false;
	}

	@Override
	public boolean isValidFacing(final byte aSide) {
		if (this.canAccessData()) {
			return this.mMetaTileEntity.isFacingValid(aSide);
		}
		return false;
	}

	@Override
	public byte getBackFacing() {
		return GT_Utility.getOppositeSide(this.mFacing);
	}

	@Override
	public byte getFrontFacing() {
		return this.mFacing;
	}

	@Override
	public void setFrontFacing(final byte aFacing) {
		if (this.isValidFacing(aFacing)) {
			this.mFacing = aFacing;
			this.mMetaTileEntity.onFacingChange();
			this.onMachineBlockUpdate();
		}
	}

	@Override
	public int getSizeInventory() {
		if (this.canAccessData()) {
			return this.mMetaTileEntity.getSizeInventory();
		}
		return 0;
	}

	@Override
	public ItemStack getStackInSlot(final int aIndex) {
		if (this.canAccessData()) {
			return this.mMetaTileEntity.getStackInSlot(aIndex);
		}
		return null;
	}

	@Override
	public void setInventorySlotContents(final int aIndex, final ItemStack aStack) {
		this.mInventoryChanged = true;
		if (this.canAccessData()) {
			this.mMetaTileEntity.setInventorySlotContents(aIndex, this.worldObj.isRemote ? aStack : GT_OreDictUnificator.setStack(true, aStack));
		}
	}

	@Override
	public String getInventoryName() {
		if (this.canAccessData()) {
			return this.mMetaTileEntity.getInventoryName();
		}
		if (GregTech_API.METATILEENTITIES[this.mID] != null) {
			return GregTech_API.METATILEENTITIES[this.mID].getInventoryName();
		}
		return "";
	}

	@Override
	public int getInventoryStackLimit() {
		if (this.canAccessData()) {
			return this.mMetaTileEntity.getInventoryStackLimit();
		}
		return 64;
	}

	@Override
	public void openInventory() {
		if (this.canAccessData()) {
			this.mMetaTileEntity.onOpenGUI();
		}
	}

	@Override
	public void closeInventory() {
		if (this.canAccessData()) {
			this.mMetaTileEntity.onCloseGUI();
		}
	}

	@Override
	public boolean isUseableByPlayer(final EntityPlayer aPlayer) {
		return this.canAccessData() && this.playerOwnsThis(aPlayer, false) && (this.mTickTimer > 40) && (this.getTileEntityOffset(0, 0, 0) == this) && (aPlayer.getDistanceSq(this.xCoord + 0.5, this.yCoord + 0.5, this.zCoord + 0.5) < 64) && this.mMetaTileEntity.isAccessAllowed(aPlayer);
	}

	@Override
	public void validate() {
		super.validate();
		this.mTickTimer = 0;
	}

	@Override
	public void invalidate() {
		this.tileEntityInvalid = false;
		if (this.canAccessData()) {
			this.mMetaTileEntity.onRemoval();
			this.mMetaTileEntity.setBaseMetaTileEntity(null);
		}
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
	public ItemStack getStackInSlotOnClosing(final int slot) {
		final ItemStack stack = this.getStackInSlot(slot);
		if (stack != null) {
			this.setInventorySlotContents(slot, null);
		}
		return stack;
	}

	@Override
	public void onMachineBlockUpdate() {
		if (this.canAccessData()) {
			this.mMetaTileEntity.onMachineBlockUpdate();
		}
	}

	@Override
	public int getProgress() {
		return this.canAccessData() ? this.mMetaTileEntity.getProgresstime() : 0;
	}

	@Override
	public int getMaxProgress() {
		return this.canAccessData() ? this.mMetaTileEntity.maxProgresstime() : 0;
	}

	@Override
	public boolean increaseProgress(final int aProgressAmountInTicks) {
		return this.canAccessData() ? this.mMetaTileEntity.increaseProgress(aProgressAmountInTicks) != aProgressAmountInTicks : false;
	}

	@Override
	public boolean hasThingsToDo() {
		return this.getMaxProgress() > 0;
	}

	@Override
	public void enableWorking() {
		if (!this.mWorks) {
			this.mWorkUpdate = true;
		}
		this.mWorks = true;
	}

	@Override
	public void disableWorking() {
		this.mWorks = false;
	}

	@Override
	public boolean isAllowedToWork() {
		return this.mWorks;
	}

	@Override
	public boolean hasWorkJustBeenEnabled() {
		return this.mWorkUpdate;
	}

	@Override
	public byte getWorkDataValue() {
		return this.mWorkData;
	}

	@Override
	public void setWorkDataValue(final byte aValue) {
		this.mWorkData = aValue;
	}

	@Override
	public int getMetaTileID() {
		return this.mID;
	}

	@Override
	public int setMetaTileID(final short aID) {
		return this.mID = aID;
	}

	@Override
	public boolean isActive() {
		return this.mActive;
	}

	@Override
	public void setActive(final boolean aActive) {
		this.mActive = aActive;
	}

	@Override
	public long getTimer() {
		return this.mTickTimer;
	}

	@Override
	public boolean decreaseStoredEnergyUnits(final long aEnergy, final boolean aIgnoreTooLessEnergy) {
		if (!this.canAccessData()) {
			return false;
		}
		return this.mHasEnoughEnergy = this.decreaseStoredEU(aEnergy, aIgnoreTooLessEnergy) || this.decreaseStoredSteam(aEnergy, false) || (aIgnoreTooLessEnergy && (this.decreaseStoredSteam(aEnergy, true)));
	}

	@Override
	public boolean increaseStoredEnergyUnits(final long aEnergy, final boolean aIgnoreTooMuchEnergy) {
		if (!this.canAccessData()) {
			return false;
		}
		if ((this.getStoredEU() < this.getEUCapacity()) || aIgnoreTooMuchEnergy) {
			this.setStoredEU(this.mMetaTileEntity.getEUVar() + aEnergy);
			return true;
		}
		return false;
	}

	@Override
	public boolean inputEnergyFrom(final byte aSide) {
		if (aSide == 6) {
			return true;
		}
		if (this.isServerSide()) {
			return ((aSide >= 0) && (aSide < 6) ? this.mActiveEUInputs[aSide] : false) && !this.mReleaseEnergy;
		}
		return this.isEnergyInputSide(aSide);
	}

	@Override
	public boolean outputsEnergyTo(final byte aSide) {
		if (aSide == 6) {
			return true;
		}
		if (this.isServerSide()) {
			return ((aSide >= 0) && (aSide < 6) ? this.mActiveEUOutputs[aSide] : false) || this.mReleaseEnergy;
		}
		return this.isEnergyOutputSide(aSide);
	}

	@Override
	public long getOutputAmperage() {
		if (this.canAccessData() && this.mMetaTileEntity.isElectric()) {
			return this.mMetaTileEntity.maxAmperesOut();
		}
		return 0;
	}

	@Override
	public long getOutputVoltage() {
		if (this.canAccessData() && this.mMetaTileEntity.isElectric() && this.mMetaTileEntity.isEnetOutput()) {
			return this.mMetaTileEntity.maxEUOutput();
		}
		return 0;
	}

	@Override
	public long getInputAmperage() {
		if (this.canAccessData() && this.mMetaTileEntity.isElectric()) {
			return this.mMetaTileEntity.maxAmperesIn();
		}
		return 0;
	}

	@Override
	public long getInputVoltage() {
		if (this.canAccessData() && this.mMetaTileEntity.isElectric()) {
			return this.mMetaTileEntity.maxEUInput();
		}
		return Integer.MAX_VALUE;
	}

	@Override
	public boolean increaseStoredSteam(final long aEnergy, final boolean aIgnoreTooMuchEnergy) {
		if (!this.canAccessData()) {
			return false;
		}
		if ((this.mMetaTileEntity.getSteamVar() < this.getSteamCapacity()) || aIgnoreTooMuchEnergy) {
			this.setStoredSteam(this.mMetaTileEntity.getSteamVar() + aEnergy);
			return true;
		}
		return false;
	}

	@Override
	public String[] getDescription() {
		if (this.canAccessData()) {
			return this.mMetaTileEntity.getDescription();
		}
		return new String[0];
	}

	@Override
	public boolean isValidSlot(final int aIndex) {
		if (this.canAccessData()) {
			return this.mMetaTileEntity.isValidSlot(aIndex);
		}
		return false;
	}

	@Override
	public long getUniversalEnergyStored() {
		return Math.max(this.getStoredEU(), this.getStoredSteam());
	}

	@Override
	public long getUniversalEnergyCapacity() {
		return Math.max(this.getEUCapacity(), this.getSteamCapacity());
	}

	@Override
	public long getStoredEU() {
		if (this.canAccessData()) {
			return Math.min(this.mMetaTileEntity.getEUVar(), this.getEUCapacity());
		}
		return 0;
	}

	@Override
	public long getEUCapacity() {
		if (this.canAccessData()) {
			return this.mMetaTileEntity.maxEUStore();
		}
		return 0;
	}

	@Override
	public long getStoredSteam() {
		if (this.canAccessData()) {
			return Math.min(this.mMetaTileEntity.getSteamVar(), this.getSteamCapacity());
		}
		return 0;
	}

	@Override
	public long getSteamCapacity() {
		if (this.canAccessData()) {
			return this.mMetaTileEntity.maxSteamStore();
		}
		return 0;
	}

	private boolean isEnergyInputSide(final byte aSide) {
		if ((aSide >= 0) && (aSide < 6)) {
			if (!this.getCoverBehaviorAtSide(aSide).letsEnergyIn(aSide, this.getCoverIDAtSide(aSide), this.getCoverDataAtSide(aSide), this)) {
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

	private boolean isEnergyOutputSide(final byte aSide) {
		if ((aSide >= 0) && (aSide < 6)) {
			if (!this.getCoverBehaviorAtSide(aSide).letsEnergyOut(aSide, this.getCoverIDAtSide(aSide), this.getCoverDataAtSide(aSide), this)) {
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

	protected boolean hasValidMetaTileEntity() {
		return (this.mMetaTileEntity != null) && (this.mMetaTileEntity.getBaseMetaTileEntity() == this);
	}

	protected boolean canAccessData() {
		return !this.isDead && this.hasValidMetaTileEntity();
	}

	public boolean setStoredEU(long aEnergy) {
		if (!this.canAccessData()) {
			return false;
		}
		if (aEnergy < 0) {
			aEnergy = 0;
		}
		this.mMetaTileEntity.setEUVar(aEnergy);
		return true;
	}

	public boolean setStoredSteam(long aEnergy) {
		if (!this.canAccessData()) {
			return false;
		}
		if (aEnergy < 0) {
			aEnergy = 0;
		}
		this.mMetaTileEntity.setSteamVar(aEnergy);
		return true;
	}

	public boolean decreaseStoredEU(final long aEnergy, final boolean aIgnoreTooLessEnergy) {
		if (!this.canAccessData()) {
			return false;
		}
		if (((this.mMetaTileEntity.getEUVar() - aEnergy) >= 0) || aIgnoreTooLessEnergy) {
			this.setStoredEU(this.mMetaTileEntity.getEUVar() - aEnergy);
			if (this.mMetaTileEntity.getEUVar() < 0) {
				this.setStoredEU(0);
				return false;
			}
			return true;
		}
		return false;
	}

	public boolean decreaseStoredSteam(final long aEnergy, final boolean aIgnoreTooLessEnergy) {
		if (!this.canAccessData()) {
			return false;
		}
		if (((this.mMetaTileEntity.getSteamVar() - aEnergy) >= 0) || aIgnoreTooLessEnergy) {
			this.setStoredSteam(this.mMetaTileEntity.getSteamVar() - aEnergy);
			if (this.mMetaTileEntity.getSteamVar() < 0) {
				this.setStoredSteam(0);
				return false;
			}
			return true;
		}
		return false;
	}

	public boolean playerOwnsThis(final EntityPlayer aPlayer, final boolean aCheckPrecicely) {
		if (!this.canAccessData()) {
			return false;
		}
		if (aCheckPrecicely || this.privateAccess() || this.mOwnerName.equals("")) {
			if (this.mOwnerName.equals("") && this.isServerSide()) {
				this.setOwnerName(aPlayer.getDisplayName());
			} else if (this.privateAccess() && !aPlayer.getDisplayName().equals("Player") && !this.mOwnerName.equals("Player") && !this.mOwnerName.equals(aPlayer.getDisplayName())) {
				return false;
			}
		}
		return true;
	}

	public boolean privateAccess() {
		if (!this.canAccessData()) {
			return this.mLockUpgrade;
		}
		return this.mLockUpgrade || this.mMetaTileEntity.ownerControl();
	}

	public void doEnergyExplosion() {
		if ((this.getUniversalEnergyCapacity() > 0) && (this.getUniversalEnergyStored() >= (this.getUniversalEnergyCapacity() / 5))) {
			this.doExplosion(this.oOutput * (this.getUniversalEnergyStored() >= this.getUniversalEnergyCapacity() ? 4 : this.getUniversalEnergyStored() >= (this.getUniversalEnergyCapacity() / 2) ? 2 : 1));
			GT_Mod.achievements.issueAchievement(this.getWorldObj().getPlayerEntityByName(this.mOwnerName), "electricproblems");
		}
	}

	@Override
	public void doExplosion(final long aAmount) {
		if (this.canAccessData()) {
			// This is only for Electric Machines
			if (GregTech_API.sMachineWireFire && this.mMetaTileEntity.isElectric()) {
				try {
					this.mReleaseEnergy = true;
					IEnergyConnected.Util.emitEnergyToNetwork(V[5], Math.max(1, this.getStoredEU() / V[5]), this);
				} catch (final Exception e) {/* Fun Fact: all these "do nothing" Comments you see in my Code, are just there to let Eclipse shut up about the intended empty Brackets, but I need eclipse to yell at me in some of the regular Cases where I forget to add Code */}
			}
			this.mReleaseEnergy = false;
			// Normal Explosion Code
			this.mMetaTileEntity.onExplosion();
			this.mMetaTileEntity.doExplosion(aAmount);
		}
	}

	@Override
	public ArrayList<ItemStack> getDrops() {
		final ItemStack rStack = new ItemStack(GregTech_API.sBlockMachines, 1, this.mID);
		final NBTTagCompound tNBT = new NBTTagCompound();
		if ((this.mRecipeStuff != null) && !this.mRecipeStuff.hasNoTags()) {
			tNBT.setTag("GT.CraftingComponents", this.mRecipeStuff);
		}
		if (this.mMuffler) {
			tNBT.setBoolean("mMuffler", this.mMuffler);
		}
		if (this.mLockUpgrade) {
			tNBT.setBoolean("mLockUpgrade", this.mLockUpgrade);
		}
		if (this.mSteamConverter) {
			tNBT.setBoolean("mSteamConverter", this.mSteamConverter);
		}
		if (this.mColor > 0) {
			tNBT.setByte("mColor", this.mColor);
		}
		if (this.mOtherUpgrades > 0) {
			tNBT.setByte("mOtherUpgrades", this.mOtherUpgrades);
		}
		if (this.mStrongRedstone > 0) {
			tNBT.setByte("mStrongRedstone", this.mStrongRedstone);
		}
		for (byte i = 0; i < this.mCoverSides.length; i++) {
			if (this.mCoverSides[i] != 0) {
				tNBT.setIntArray("mCoverData", this.mCoverData);
				tNBT.setIntArray("mCoverSides", this.mCoverSides);
				break;
			}
		}
		if (this.hasValidMetaTileEntity()) {
			this.mMetaTileEntity.setItemNBT(tNBT);
		}
		if (!tNBT.hasNoTags()) {
			rStack.setTagCompound(tNBT);
		}
		return new ArrayList<>(Arrays.asList(rStack));
	}

	public int getUpgradeCount() {
		return (this.mMuffler ? 1 : 0) + (this.mLockUpgrade ? 1 : 0) + (this.mSteamConverter ? 1 : 0) + this.mOtherUpgrades;
	}

	@Override
	public boolean onRightclick(final EntityPlayer aPlayer, final byte aSide, final float aX, final float aY, final float aZ) {
		if (this.isClientSide()) {
			if (this.getCoverBehaviorAtSide(aSide).onCoverRightclickClient(aSide, this, aPlayer, aX, aY, aZ)) {
				return true;
			}
			if (!this.getCoverBehaviorAtSide(aSide).isGUIClickable(aSide, this.getCoverIDAtSide(aSide), this.getCoverDataAtSide(aSide), this)) {
				return false;
			}
		}
		if (this.isServerSide()) {
			if (!this.privateAccess() || aPlayer.getDisplayName().equalsIgnoreCase(this.getOwnerName())) {
				final ItemStack tCurrentItem = aPlayer.inventory.getCurrentItem();
				if (tCurrentItem != null) {
					if ((this.getColorization() >= 0) && GT_Utility.areStacksEqual(new ItemStack(Items.water_bucket, 1), tCurrentItem)) {
						tCurrentItem.func_150996_a(Items.bucket);
						this.setColorization((byte) (this.getColorization() >= 16 ? -2 : -1));
						return true;
					}
					if (GT_Utility.isStackInList(tCurrentItem, GregTech_API.sWrenchList)) {
						if (this.mMetaTileEntity.onWrenchRightClick(aSide, GT_Utility.determineWrenchingSide(aSide, aX, aY, aZ), aPlayer, aX, aY, aZ)) {
							GT_ModHandler.damageOrDechargeItem(tCurrentItem, 1, 1000, aPlayer);
							GT_Utility.sendSoundToPlayers(this.worldObj, GregTech_API.sSoundList.get(100), 1.0F, -1, this.xCoord, this.yCoord, this.zCoord);
						}
						return true;
					}

					if (GT_Utility.isStackInList(tCurrentItem, GregTech_API.sScrewdriverList)) {
						if (GT_ModHandler.damageOrDechargeItem(tCurrentItem, 1, 200, aPlayer)) {
							this.setCoverDataAtSide(aSide, this.getCoverBehaviorAtSide(aSide).onCoverScrewdriverclick(aSide, this.getCoverIDAtSide(aSide), this.getCoverDataAtSide(aSide), this, aPlayer, aX, aY, aZ));
							this.mMetaTileEntity.onScrewdriverRightClick(aSide, aPlayer, aX, aY, aZ);
							GT_Utility.sendSoundToPlayers(this.worldObj, GregTech_API.sSoundList.get(100), 1.0F, -1, this.xCoord, this.yCoord, this.zCoord);
						}
						return true;
					}

					if (GT_Utility.isStackInList(tCurrentItem, GregTech_API.sHardHammerList)) {
						if (GT_ModHandler.damageOrDechargeItem(tCurrentItem, 1, 1000, aPlayer)) {
							this.mInputDisabled = !this.mInputDisabled;
							if (this.mInputDisabled) {
								this.mOutputDisabled = !this.mOutputDisabled;
							}
							GT_Utility.sendChatToPlayer(aPlayer, "Auto-Input: " + (this.mInputDisabled ? "Disabled" : "Enabled") + "  Auto-Output: " + (this.mOutputDisabled ? "Disabled" : "Enabled"));
							GT_Utility.sendSoundToPlayers(this.worldObj, GregTech_API.sSoundList.get(1), 1.0F, -1, this.xCoord, this.yCoord, this.zCoord);
						}
						return true;
					}

					if (GT_Utility.isStackInList(tCurrentItem, GregTech_API.sSoftHammerList)) {
						if (GT_ModHandler.damageOrDechargeItem(tCurrentItem, 1, 1000, aPlayer)) {
							if (this.mWorks) {
								this.disableWorking();
							} else {
								this.enableWorking();
							}
							GT_Utility.sendChatToPlayer(aPlayer, "Machine Processing: " + (this.isAllowedToWork() ? "Enabled" : "Disabled"));
							GT_Utility.sendSoundToPlayers(this.worldObj, GregTech_API.sSoundList.get(101), 1.0F, -1, this.xCoord, this.yCoord, this.zCoord);
						}
						return true;
					}

					if (GT_Utility.isStackInList(tCurrentItem, GregTech_API.sSolderingToolList)) {
						final byte tSide = GT_Utility.determineWrenchingSide(aSide, aX, aY, aZ);
						if (GT_ModHandler.useSolderingIron(tCurrentItem, aPlayer)) {
							this.mStrongRedstone ^= (1 << tSide);
							GT_Utility.sendChatToPlayer(aPlayer, "Redstone Output at Side " + tSide + " set to: " + ((this.mStrongRedstone & (1 << tSide)) != 0 ? "Strong" : "Weak"));
							GT_Utility.sendSoundToPlayers(this.worldObj, GregTech_API.sSoundList.get(103), 3.0F, -1, this.xCoord, this.yCoord, this.zCoord);
						}
						return true;
					}

					if (this.getCoverIDAtSide(aSide) == 0) {
						if (GregTech_API.sCovers.containsKey(new GT_ItemStack(tCurrentItem))) {
							if (GregTech_API.getCoverBehavior(tCurrentItem).isCoverPlaceable(aSide, new GT_ItemStack(tCurrentItem), this) && this.mMetaTileEntity.allowCoverOnSide(aSide, new GT_ItemStack(tCurrentItem))) {
								this.setCoverItemAtSide(aSide, tCurrentItem);
								if (!aPlayer.capabilities.isCreativeMode) {
									tCurrentItem.stackSize--;
								}
								GT_Utility.sendSoundToPlayers(this.worldObj, GregTech_API.sSoundList.get(100), 1.0F, -1, this.xCoord, this.yCoord, this.zCoord);
							}
							return true;
						}
					} else {
						if (GT_Utility.isStackInList(tCurrentItem, GregTech_API.sCrowbarList)) {
							if (GT_ModHandler.damageOrDechargeItem(tCurrentItem, 1, 1000, aPlayer)) {
								GT_Utility.sendSoundToPlayers(this.worldObj, GregTech_API.sSoundList.get(0), 1.0F, -1, this.xCoord, this.yCoord, this.zCoord);
								this.dropCover(aSide, aSide, false);
							}
							return true;
						}
					}
				}

				if (this.getCoverBehaviorAtSide(aSide).onCoverRightclick(aSide, this.getCoverIDAtSide(aSide), this.getCoverDataAtSide(aSide), this, aPlayer, aX, aY, aZ)) {
					return true;
				}

				if (!this.getCoverBehaviorAtSide(aSide).isGUIClickable(aSide, this.getCoverIDAtSide(aSide), this.getCoverDataAtSide(aSide), this)) {
					return false;
				}

				if (this.isUpgradable() && (aPlayer.inventory.getCurrentItem() != null)) {/*
                    if (ItemList.Upgrade_SteamEngine.isStackEqual(aPlayer.inventory.getCurrentItem())) {
						if (addSteamEngineUpgrade()) {
							GT_Utility.sendSoundToPlayers(worldObj, GregTech_API.sSoundList.get(3), 1.0F, -1, xCoord, yCoord, zCoord);
							if (!aPlayer.capabilities.isCreativeMode) aPlayer.inventory.getCurrentItem().stackSize--;
						}
						return true;
					}*/
					if (ItemList.Upgrade_Muffler.isStackEqual(aPlayer.inventory.getCurrentItem())) {
						if (this.addMufflerUpgrade()) {
							GT_Utility.sendSoundToPlayers(this.worldObj, GregTech_API.sSoundList.get(3), 1.0F, -1, this.xCoord, this.yCoord, this.zCoord);
							if (!aPlayer.capabilities.isCreativeMode) {
								aPlayer.inventory.getCurrentItem().stackSize--;
							}
						}
						return true;
					}
					if (ItemList.Upgrade_Lock.isStackEqual(aPlayer.inventory.getCurrentItem())) {
						if (this.isUpgradable() && !this.mLockUpgrade) {
							this.mLockUpgrade = true;
							this.setOwnerName(aPlayer.getDisplayName());
							GT_Utility.sendSoundToPlayers(this.worldObj, GregTech_API.sSoundList.get(3), 1.0F, -1, this.xCoord, this.yCoord, this.zCoord);
							if (!aPlayer.capabilities.isCreativeMode) {
								aPlayer.inventory.getCurrentItem().stackSize--;
							}
						}
						return true;
					}
				}
			}
		}

		try {
			if (this.hasValidMetaTileEntity()) {
				return this.mMetaTileEntity.onRightclick(this, aPlayer, aSide, aX, aY, aZ);
			}
		} catch (final Throwable e) {
			GT_Log.err.println("Encountered Exception while rightclicking TileEntity, the Game should've crashed now, but I prevented that. Please report immidietly to GregTech Intergalactical!!!");
			e.printStackTrace(GT_Log.err);
		}

		return true;
	}

	@Override
	public void onLeftclick(final EntityPlayer aPlayer) {
		try {
			if ((aPlayer != null) && this.hasValidMetaTileEntity()) {
				this.mMetaTileEntity.onLeftclick(this, aPlayer);
			}
		} catch (final Throwable e) {
			GT_Log.err.println("Encountered Exception while leftclicking TileEntity, the Game should've crashed now, but I prevented that. Please report immidietly to GregTech Intergalactical!!!");
			e.printStackTrace(GT_Log.err);
		}
	}

	@Override
	public boolean isDigitalChest() {
		if (this.canAccessData()) {
			return this.mMetaTileEntity.isDigitalChest();
		}
		return false;
	}

	@Override
	public ItemStack[] getStoredItemData() {
		if (this.canAccessData()) {
			return this.mMetaTileEntity.getStoredItemData();
		}
		return null;
	}

	@Override
	public void setItemCount(final int aCount) {
		if (this.canAccessData()) {
			this.mMetaTileEntity.setItemCount(aCount);
		}
	}

	@Override
	public int getMaxItemCount() {
		if (this.canAccessData()) {
			return this.mMetaTileEntity.getMaxItemCount();
		}
		return 0;
	}

	/**
	 * Can put aStack into Slot
	 */
	@Override
	public boolean isItemValidForSlot(final int aIndex, final ItemStack aStack) {
		return this.canAccessData() && this.mMetaTileEntity.isItemValidForSlot(aIndex, aStack);
	}

	/**
	 * returns all valid Inventory Slots, no matter which Side (Unless it's covered).
	 * The Side Stuff is done in the following two Functions.
	 */
	@Override
	public int[] getAccessibleSlotsFromSide(final int aSide) {
		if (this.canAccessData() && (this.getCoverBehaviorAtSide((byte) aSide).letsItemsOut((byte) aSide, this.getCoverIDAtSide((byte) aSide), this.getCoverDataAtSide((byte) aSide), -1, this) || this.getCoverBehaviorAtSide((byte) aSide).letsItemsIn((byte) aSide, this.getCoverIDAtSide((byte) aSide), this.getCoverDataAtSide((byte) aSide), -1, this))) {
			return this.mMetaTileEntity.getAccessibleSlotsFromSide(aSide);
		}
		return new int[0];
	}

	/**
	 * Can put aStack into Slot at Side
	 */
	@Override
	public boolean canInsertItem(final int aIndex, final ItemStack aStack, final int aSide) {
		return this.canAccessData() && (this.mRunningThroughTick || !this.mInputDisabled) && this.getCoverBehaviorAtSide((byte) aSide).letsItemsIn((byte) aSide, this.getCoverIDAtSide((byte) aSide), this.getCoverDataAtSide((byte) aSide), aIndex, this) && this.mMetaTileEntity.canInsertItem(aIndex, aStack, aSide);
	}

	/**
	 * Can pull aStack out of Slot from Side
	 */
	@Override
	public boolean canExtractItem(final int aIndex, final ItemStack aStack, final int aSide) {
		return this.canAccessData() && (this.mRunningThroughTick || !this.mOutputDisabled) && this.getCoverBehaviorAtSide((byte) aSide).letsItemsOut((byte) aSide, this.getCoverIDAtSide((byte) aSide), this.getCoverDataAtSide((byte) aSide), aIndex, this) && this.mMetaTileEntity.canExtractItem(aIndex, aStack, aSide);
	}

	@Override
	public boolean isUpgradable() {
		return this.canAccessData() && (this.getUpgradeCount() < 8);
	}

	@Override
	public byte getInternalInputRedstoneSignal(final byte aSide) {
		return (byte) (this.getCoverBehaviorAtSide(aSide).getRedstoneInput(aSide, this.getInputRedstoneSignal(aSide), this.getCoverIDAtSide(aSide), this.getCoverDataAtSide(aSide), this) & 15);
	}

	@Override
	public byte getInputRedstoneSignal(final byte aSide) {
		return (byte) (this.worldObj.getIndirectPowerLevelTo(this.getOffsetX(aSide, 1), this.getOffsetY(aSide, 1), this.getOffsetZ(aSide, 1), aSide) & 15);
	}

	@Override
	public byte getOutputRedstoneSignal(final byte aSide) {
		return this.getCoverBehaviorAtSide(aSide).manipulatesSidedRedstoneOutput(aSide, this.getCoverIDAtSide(aSide), this.getCoverDataAtSide(aSide), this) ? this.mSidedRedstone[aSide] : 0;
		//		return (byte)(getCoverBehaviorAtSide(aSide).manipulatesSidedRedstoneOutput(aSide, getCoverIDAtSide(aSide), getCoverDataAtSide(aSide), this) || (mRedstone && getCoverBehaviorAtSide(aSide).letsRedstoneGoOut(aSide, getCoverIDAtSide(aSide), getCoverDataAtSide(aSide), this))?mSidedRedstone[aSide]&15:0);
	}

	@Override
	public void setInternalOutputRedstoneSignal(final byte aSide, final byte aStrength) {
		if (!this.getCoverBehaviorAtSide(aSide).manipulatesSidedRedstoneOutput(aSide, this.getCoverIDAtSide(aSide), this.getCoverDataAtSide(aSide), this)) {
			this.setOutputRedstoneSignal(aSide, aStrength);
		}
	}

	@Override
	public void setOutputRedstoneSignal(final byte aSide, byte aStrength) {
		aStrength = (byte) Math.min(Math.max(0, aStrength), 15);
		if ((aSide >= 0) && (aSide < 6) && (this.mSidedRedstone[aSide] != aStrength)) {
			this.mSidedRedstone[aSide] = aStrength;
			this.issueBlockUpdate();
		}
	}

	@Override
	public boolean isSteamEngineUpgradable() {
		return this.isUpgradable() && !this.hasSteamEngineUpgrade() && (this.getSteamCapacity() > 0);
	}

	@Override
	public boolean addSteamEngineUpgrade() {
		if (this.isSteamEngineUpgradable()) {
			this.issueBlockUpdate();
			this.mSteamConverter = true;
			return true;
		}
		return false;
	}

	@Override
	public boolean hasSteamEngineUpgrade() {
		if (this.canAccessData() && this.mMetaTileEntity.isSteampowered()) {
			return true;
		}
		return this.mSteamConverter;
	}

	@Override
	public boolean hasMufflerUpgrade() {
		return this.mMuffler;
	}

	@Override
	public boolean isMufflerUpgradable() {
		return this.isUpgradable() && !this.hasMufflerUpgrade();
	}

	@Override
	public boolean addMufflerUpgrade() {
		if (this.isMufflerUpgradable()) {
			return this.mMuffler = true;
		}
		return false;
	}

	@Override
	public boolean hasInventoryBeenModified() {
		return this.mInventoryChanged;
	}

	@Override
	public void setGenericRedstoneOutput(final boolean aOnOff) {
		this.mRedstone = aOnOff;
	}

	@Override
	public int getErrorDisplayID() {
		return this.mDisplayErrorCode;
	}

	@Override
	public void setErrorDisplayID(final int aErrorID) {
		this.mDisplayErrorCode = aErrorID;
	}

	@Override
	public IMetaTileEntity getMetaTileEntity() {
		return this.hasValidMetaTileEntity() ? this.mMetaTileEntity : null;
	}

	@Override
	public void setMetaTileEntity(final IMetaTileEntity aMetaTileEntity) {
		this.mMetaTileEntity = (MetaTileEntity) aMetaTileEntity;
	}

	@Override
	public GT_CoverBehavior getCoverBehaviorAtSide(final byte aSide) {
		return (aSide >= 0) && (aSide < this.mCoverBehaviors.length) ? this.mCoverBehaviors[aSide] : GregTech_API.sNoBehavior;
	}

	@Override
	public void setCoverIDAtSide(final byte aSide, final int aID) {
		if ((aSide >= 0) && (aSide < 6)) {
			this.mCoverSides[aSide] = aID;
			this.mCoverData[aSide] = 0;
			this.mCoverBehaviors[aSide] = GregTech_API.getCoverBehavior(aID);
			this.issueCoverUpdate(aSide);
			this.issueBlockUpdate();
		}
	}

	@Override
	public void setCoverItemAtSide(final byte aSide, final ItemStack aCover) {
		GregTech_API.getCoverBehavior(aCover).placeCover(aSide, aCover, this);
	}

	@Override
	public int getCoverIDAtSide(final byte aSide) {
		if ((aSide >= 0) && (aSide < 6)) {
			return this.mCoverSides[aSide];
		}
		return 0;
	}

	@Override
	public ItemStack getCoverItemAtSide(final byte aSide) {
		return GT_Utility.intToStack(this.getCoverIDAtSide(aSide));
	}

	@Override
	public boolean canPlaceCoverIDAtSide(final byte aSide, final int aID) {
		return this.getCoverIDAtSide(aSide) == 0;
	}

	@Override
	public boolean canPlaceCoverItemAtSide(final byte aSide, final ItemStack aCover) {
		return this.getCoverIDAtSide(aSide) == 0;
	}

	@Override
	public void setCoverDataAtSide(final byte aSide, final int aData) {
		if ((aSide >= 0) && (aSide < 6)) {
			this.mCoverData[aSide] = aData;
		}
	}

	@Override
	public int getCoverDataAtSide(final byte aSide) {
		if ((aSide >= 0) && (aSide < 6)) {
			return this.mCoverData[aSide];
		}
		return 0;
	}

	public byte getLightValue() {
		return this.mLightValue;
	}

	@Override
	public void setLightValue(final byte aLightValue) {
		this.mLightValue = (byte) (aLightValue & 15);
	}

	@Override
	public long getAverageElectricInput() {
		int rEU = 0;
		for (final int tEU : this.mAverageEUInput) {
			rEU += tEU;
		}
		return rEU / this.mAverageEUInput.length;
	}

	@Override
	public long getAverageElectricOutput() {
		int rEU = 0;
		for (final int tEU : this.mAverageEUOutput) {
			rEU += tEU;
		}
		return rEU / this.mAverageEUOutput.length;
	}

	@Override
	public boolean dropCover(final byte aSide, final byte aDroppedSide, final boolean aForced) {
		if (this.getCoverBehaviorAtSide(aSide).onCoverRemoval(aSide, this.getCoverIDAtSide(aSide), this.mCoverData[aSide], this, aForced) || aForced) {
			final ItemStack tStack = this.getCoverBehaviorAtSide(aSide).getDrop(aSide, this.getCoverIDAtSide(aSide), this.getCoverDataAtSide(aSide), this);
			if (tStack != null) {
				tStack.setTagCompound(null);
				final EntityItem tEntity = new EntityItem(this.worldObj, this.getOffsetX(aDroppedSide, 1) + 0.5, this.getOffsetY(aDroppedSide, 1) + 0.5, this.getOffsetZ(aDroppedSide, 1) + 0.5, tStack);
				tEntity.motionX = 0;
				tEntity.motionY = 0;
				tEntity.motionZ = 0;
				this.worldObj.spawnEntityInWorld(tEntity);
			}
			this.setCoverIDAtSide(aSide, 0);
			if (this.mMetaTileEntity.hasSidedRedstoneOutputBehavior()) {
				this.setOutputRedstoneSignal(aSide, (byte) 0);
			} else {
				this.setOutputRedstoneSignal(aSide, (byte) 15);
			}
			return true;
		}
		return false;
	}

	@Override
	public String getOwnerName() {
		if (GT_Utility.isStringInvalid(this.mOwnerName)) {
			return "Player";
		}
		return this.mOwnerName;
	}

	@Override
	public String setOwnerName(final String aName) {
		if (GT_Utility.isStringInvalid(aName)) {
			return this.mOwnerName = "Player";
		}
		return this.mOwnerName = aName;
	}

	@Override
	public byte getComparatorValue(final byte aSide) {
		return this.canAccessData() ? this.mMetaTileEntity.getComparatorValue(aSide) : 0;
	}

	@Override
	public byte getStrongOutputRedstoneSignal(final byte aSide) {
		return (aSide >= 0) && (aSide < 6) && ((this.mStrongRedstone & (1 << aSide)) != 0) ? (byte) (this.mSidedRedstone[aSide] & 15) : 0;
	}

	@Override
	public void setStrongOutputRedstoneSignal(final byte aSide, final byte aStrength) {
		this.mStrongRedstone |= (1 << aSide);
		this.setOutputRedstoneSignal(aSide, aStrength);
	}

	@Override
	public ItemStack decrStackSize(final int aIndex, final int aAmount) {
		if (this.canAccessData()) {
			this.mInventoryChanged = true;
			return this.mMetaTileEntity.decrStackSize(aIndex, aAmount);
		}
		return null;
	}

	@Override
	public long injectEnergyUnits(final byte aSide, final long aVoltage, long aAmperage) {
		if (!this.canAccessData() || !this.mMetaTileEntity.isElectric() || !this.inputEnergyFrom(aSide) || (aAmperage <= 0) || (aVoltage <= 0) || (this.getStoredEU() >= this.getEUCapacity()) || (this.mMetaTileEntity.maxAmperesIn() <= this.mAcceptedAmperes)) {
			return 0;
		}
		if (aVoltage > this.getInputVoltage()) {
			this.doExplosion(aVoltage);
			return 0;
		}
		if (this.increaseStoredEnergyUnits(aVoltage * (aAmperage = Math.min(aAmperage, Math.min(this.mMetaTileEntity.maxAmperesIn() - this.mAcceptedAmperes, 1 + ((this.getEUCapacity() - this.getStoredEU()) / aVoltage)))), true)) {
			this.mAverageEUInput[this.mAverageEUInputIndex] += aVoltage * aAmperage;
			this.mAcceptedAmperes += aAmperage;
			return aAmperage;
		}
		return 0;
	}

	@Override
	public boolean drainEnergyUnits(final byte aSide, final long aVoltage, final long aAmperage) {
		if (!this.canAccessData() || !this.mMetaTileEntity.isElectric() || !this.outputsEnergyTo(aSide) || ((this.getStoredEU() - (aVoltage * aAmperage)) < this.mMetaTileEntity.getMinimumStoredEU())) {
			return false;
		}
		if (this.decreaseStoredEU(aVoltage * aAmperage, false)) {
			this.mAverageEUOutput[this.mAverageEUOutputIndex] += aVoltage * aAmperage;
			return true;
		}
		return false;
	}

	@Override
	public boolean acceptsRotationalEnergy(final byte aSide) {
		if (!this.canAccessData() || (this.getCoverIDAtSide(aSide) != 0)) {
			return false;
		}
		return this.mMetaTileEntity.acceptsRotationalEnergy(aSide);
	}

	@Override
	public boolean injectRotationalEnergy(final byte aSide, final long aSpeed, final long aEnergy) {
		if (!this.canAccessData() || (this.getCoverIDAtSide(aSide) != 0)) {
			return false;
		}
		return this.mMetaTileEntity.injectRotationalEnergy(aSide, aSpeed, aEnergy);
	}

	@Override
	public int fill(final ForgeDirection aSide, final FluidStack aFluid, final boolean doFill) {
		if ((this.mTickTimer > 5) && this.canAccessData() && (this.mRunningThroughTick || !this.mInputDisabled) && ((aSide == ForgeDirection.UNKNOWN) || (this.mMetaTileEntity.isLiquidInput((byte) aSide.ordinal()) && this.getCoverBehaviorAtSide((byte) aSide.ordinal()).letsFluidIn((byte) aSide.ordinal(), this.getCoverIDAtSide((byte) aSide.ordinal()), this.getCoverDataAtSide((byte) aSide.ordinal()), aFluid == null ? null : aFluid.getFluid(), this)))) {
			return this.mMetaTileEntity.fill(aSide, aFluid, doFill);
		}
		return 0;
	}

	@Override
	public FluidStack drain(final ForgeDirection aSide, final int maxDrain, final boolean doDrain) {
		if ((this.mTickTimer > 5) && this.canAccessData() && (this.mRunningThroughTick || !this.mOutputDisabled) && ((aSide == ForgeDirection.UNKNOWN) || (this.mMetaTileEntity.isLiquidOutput((byte) aSide.ordinal()) && this.getCoverBehaviorAtSide((byte) aSide.ordinal()).letsFluidOut((byte) aSide.ordinal(), this.getCoverIDAtSide((byte) aSide.ordinal()), this.getCoverDataAtSide((byte) aSide.ordinal()), this.mMetaTileEntity.getFluid() == null ? null : this.mMetaTileEntity.getFluid().getFluid(), this)))) {
			return this.mMetaTileEntity.drain(aSide, maxDrain, doDrain);
		}
		return null;
	}

	@Override
	public FluidStack drain(final ForgeDirection aSide, final FluidStack aFluid, final boolean doDrain) {
		if ((this.mTickTimer > 5) && this.canAccessData() && (this.mRunningThroughTick || !this.mOutputDisabled) && ((aSide == ForgeDirection.UNKNOWN) || (this.mMetaTileEntity.isLiquidOutput((byte) aSide.ordinal()) && this.getCoverBehaviorAtSide((byte) aSide.ordinal()).letsFluidOut((byte) aSide.ordinal(), this.getCoverIDAtSide((byte) aSide.ordinal()), this.getCoverDataAtSide((byte) aSide.ordinal()), aFluid == null ? null : aFluid.getFluid(), this)))) {
			return this.mMetaTileEntity.drain(aSide, aFluid, doDrain);
		}
		return null;
	}

	@Override
	public boolean canFill(final ForgeDirection aSide, final Fluid aFluid) {
		if ((this.mTickTimer > 5) && this.canAccessData() && (this.mRunningThroughTick || !this.mInputDisabled) && ((aSide == ForgeDirection.UNKNOWN) || (this.mMetaTileEntity.isLiquidInput((byte) aSide.ordinal()) && this.getCoverBehaviorAtSide((byte) aSide.ordinal()).letsFluidIn((byte) aSide.ordinal(), this.getCoverIDAtSide((byte) aSide.ordinal()), this.getCoverDataAtSide((byte) aSide.ordinal()), aFluid, this)))) {
			return this.mMetaTileEntity.canFill(aSide, aFluid);
		}
		return false;
	}

	@Override
	public boolean canDrain(final ForgeDirection aSide, final Fluid aFluid) {
		if ((this.mTickTimer > 5) && this.canAccessData() && (this.mRunningThroughTick || !this.mOutputDisabled) && ((aSide == ForgeDirection.UNKNOWN) || (this.mMetaTileEntity.isLiquidOutput((byte) aSide.ordinal()) && this.getCoverBehaviorAtSide((byte) aSide.ordinal()).letsFluidOut((byte) aSide.ordinal(), this.getCoverIDAtSide((byte) aSide.ordinal()), this.getCoverDataAtSide((byte) aSide.ordinal()), aFluid, this)))) {
			return this.mMetaTileEntity.canDrain(aSide, aFluid);
		}
		return false;
	}

	@Override
	public FluidTankInfo[] getTankInfo(final ForgeDirection aSide) {
		if (this.canAccessData() && ((aSide == ForgeDirection.UNKNOWN) || (this.mMetaTileEntity.isLiquidInput((byte) aSide.ordinal()) && this.getCoverBehaviorAtSide((byte) aSide.ordinal()).letsFluidIn((byte) aSide.ordinal(), this.getCoverIDAtSide((byte) aSide.ordinal()), this.getCoverDataAtSide((byte) aSide.ordinal()), null, this)) || (this.mMetaTileEntity.isLiquidOutput((byte) aSide.ordinal()) && this.getCoverBehaviorAtSide((byte) aSide.ordinal()).letsFluidOut((byte) aSide.ordinal(), this.getCoverIDAtSide((byte) aSide.ordinal()), this.getCoverDataAtSide((byte) aSide.ordinal()), null, this)))) {
			return this.mMetaTileEntity.getTankInfo(aSide);
		}
		return new FluidTankInfo[]{};
	}

	public double getOutputEnergyUnitsPerTick() {
		return this.oOutput;
	}

	public boolean isTeleporterCompatible(final ForgeDirection aSide) {
		return this.canAccessData() && this.mMetaTileEntity.isTeleporterCompatible();
	}

	public double demandedEnergyUnits() {
		if (this.mReleaseEnergy || !this.canAccessData() || !this.mMetaTileEntity.isEnetInput()) {
			return 0;
		}
		return this.getEUCapacity() - this.getStoredEU();
	}

	public double injectEnergyUnits(final ForgeDirection aDirection, final double aAmount) {
		return this.injectEnergyUnits((byte) aDirection.ordinal(), (int) aAmount, 1) > 0 ? 0 : aAmount;
	}

	public boolean acceptsEnergyFrom(final TileEntity aEmitter, final ForgeDirection aDirection) {
		return this.inputEnergyFrom((byte) aDirection.ordinal());
	}

	public boolean emitsEnergyTo(final TileEntity aReceiver, final ForgeDirection aDirection) {
		return this.outputsEnergyTo((byte) aDirection.ordinal());
	}

	public double getOfferedEnergy() {
		return (this.canAccessData() && ((this.getStoredEU() - this.mMetaTileEntity.getMinimumStoredEU()) >= this.oOutput)) ? Math.max(0, this.oOutput) : 0;
	}

	public void drawEnergy(final double amount) {
		this.mAverageEUOutput[this.mAverageEUOutputIndex] += amount;
		this.decreaseStoredEU((int) amount, true);
	}

	public int injectEnergy(final ForgeDirection aForgeDirection, final int aAmount) {
		return this.injectEnergyUnits((byte) aForgeDirection.ordinal(), aAmount, 1) > 0 ? 0 : aAmount;
	}

	public int addEnergy(final int aEnergy) {
		if (!this.canAccessData()) {
			return 0;
		}
		if (aEnergy > 0) {
			this.increaseStoredEnergyUnits(aEnergy, true);
		} else {
			this.decreaseStoredEU(-aEnergy, true);
		}
		return (int) Math.min(Integer.MAX_VALUE, this.mMetaTileEntity.getEUVar());
	}

	public boolean isAddedToEnergyNet() {
		return false;
	}

	public int demandsEnergy() {
		if (this.mReleaseEnergy || !this.canAccessData() || !this.mMetaTileEntity.isEnetInput()) {
			return 0;
		}
		return this.getCapacity() - this.getStored();
	}

	public int getCapacity() {
		return (int) Math.min(Integer.MAX_VALUE, this.getEUCapacity());
	}

	public int getStored() {
		return (int) Math.min(Integer.MAX_VALUE, Math.min(this.getStoredEU(), this.getCapacity()));
	}

	public void setStored(final int aEU) {
		if (this.canAccessData()) {
			this.setStoredEU(aEU);
		}
	}

	public int getMaxSafeInput() {
		return (int) Math.min(Integer.MAX_VALUE, this.getInputVoltage());
	}

	public int getMaxEnergyOutput() {
		if (this.mReleaseEnergy) {
			return Integer.MAX_VALUE;
		}
		return this.getOutput();
	}

	public int getOutput() {
		return (int) Math.min(Integer.MAX_VALUE, this.oOutput);
	}

	public int injectEnergy(final Direction aDirection, final int aAmount) {
		return this.injectEnergyUnits((byte) aDirection.toSideValue(), aAmount, 1) > 0 ? 0 : aAmount;
	}

	public boolean isTeleporterCompatible(final Direction aSide) {
		return this.canAccessData() && this.mMetaTileEntity.isTeleporterCompatible();
	}

	public boolean acceptsEnergyFrom(final TileEntity aReceiver, final Direction aDirection) {
		return this.inputEnergyFrom((byte) aDirection.toSideValue());
	}

	public boolean emitsEnergyTo(final TileEntity aReceiver, final Direction aDirection) {
		return this.outputsEnergyTo((byte) aDirection.toSideValue());
	}

	@Override
	public boolean isInvalidTileEntity() {
		return this.isInvalid();
	}

	@Override
	public boolean addStackToSlot(final int aIndex, ItemStack aStack) {
		if (GT_Utility.isStackInvalid(aStack)) {
			return true;
		}
		if ((aIndex < 0) || (aIndex >= this.getSizeInventory())) {
			return false;
		}
		final ItemStack tStack = this.getStackInSlot(aIndex);
		if (GT_Utility.isStackInvalid(tStack)) {
			this.setInventorySlotContents(aIndex, aStack);
			return true;
		}
		aStack = GT_OreDictUnificator.get(aStack);
		if (GT_Utility.areStacksEqual(tStack, aStack) && ((tStack.stackSize + aStack.stackSize) <= Math.min(aStack.getMaxStackSize(), this.getInventoryStackLimit()))) {
			tStack.stackSize += aStack.stackSize;
			return true;
		}
		return false;
	}

	@Override
	public boolean addStackToSlot(final int aIndex, final ItemStack aStack, final int aAmount) {
		return this.addStackToSlot(aIndex, GT_Utility.copyAmount(aAmount, aStack));
	}

	@Override
	public byte getColorization() {
		return (byte) (this.mColor - 1);
	}

	@Override
	public byte setColorization(byte aColor) {
		if ((aColor > 15) || (aColor < -1)) {
			aColor = -1;
		}
		if (this.canAccessData()) {
			this.mMetaTileEntity.onColorChangeServer(aColor);
		}
		return this.mColor = (byte) (aColor + 1);
	}

	@Override
	public float getBlastResistance(final byte aSide) {
		return this.canAccessData() ? Math.max(0, this.getMetaTileEntity().getExplosionResistance(aSide)) : 10.0F;
	}

	@Override
	public boolean isUniversalEnergyStored(final long aEnergyAmount) {
		if (this.getUniversalEnergyStored() >= aEnergyAmount) {
			return true;
		}
		this.mHasEnoughEnergy = false;
		return false;
	}

	@Override
	public String[] getInfoData() {
		{
			if (this.canAccessData()) {
				return this.getMetaTileEntity().getInfoData();
			}
			return new String[]{};
		}
	}

	@Override
	public void markDirty() {
		super.markDirty();
		this.mInventoryChanged = true;
	}

	@Override
	public int getLightOpacity() {
		return this.mMetaTileEntity == null ? this.getLightValue() > 0 ? 0 : 255 : this.mMetaTileEntity.getLightOpacity();
	}

	@Override
	public void addCollisionBoxesToList(final World aWorld, final int aX, final int aY, final int aZ, final AxisAlignedBB inputAABB, final List<AxisAlignedBB> outputAABB, final Entity collider) {
		this.mMetaTileEntity.addCollisionBoxesToList(aWorld, aX, aY, aZ, inputAABB, outputAABB, collider);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(final World aWorld, final int aX, final int aY, final int aZ) {
		return this.mMetaTileEntity.getCollisionBoundingBoxFromPool(aWorld, aX, aY, aZ);
	}

	@Override
	public void onEntityCollidedWithBlock(final World aWorld, final int aX, final int aY, final int aZ, final Entity collider) {
		this.mMetaTileEntity.onEntityCollidedWithBlock(aWorld, aX, aY, aZ, collider);
	}


	@Override
	public ITexture[] getTexture(final Block arg0, final byte aSide) {
		final ITexture rIcon = this.getCoverTexture(aSide);
		if (rIcon != null) {
			return new ITexture[]{rIcon};
		}
		if (this.hasValidMetaTileEntity()) {
			return this.mMetaTileEntity.getTexture(this, aSide, this.mFacing, (byte) (this.mColor - 1), this.mActive, this.getOutputRedstoneSignal(aSide) > 0);
		}
		return Textures.BlockIcons.ERROR_RENDERING;
	}

	public ITexture[] getTexture(final byte aSide) {
		final ITexture rIcon = this.getCoverTexture(aSide);
		if (rIcon != null) {
			return new ITexture[]{rIcon};
		}
		if (this.hasValidMetaTileEntity()) {
			return this.mMetaTileEntity.getTexture(this, aSide, this.mFacing, (byte) (this.mColor - 1), this.mActive, this.getOutputRedstoneSignal(aSide) > 0);
		}
		return Textures.BlockIcons.ERROR_RENDERING;
	}
}