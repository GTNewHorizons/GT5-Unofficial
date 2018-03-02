package gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.Recipe_GT;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_DeluxeMachine;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class GregtechMetaTileEntity_PocketFusion extends GT_MetaTileEntity_DeluxeMachine {

	private boolean mCanProcessRecipe = false;
	private boolean mCharging = false;
	private long mChargeConsumed = 0;

	private GT_Recipe mLastRecipe;
	private long mEUStore;
	private boolean mRunningOnLoad = false;
	private boolean mMachine = false;
	private int mEfficiency, mEfficiencyIncrease, mEfficiencyMax = 0;
	private int mStartUpCheck = 100, mUpdate = 0;
	private FluidStack[] mOutputFluids = null;

	public GregtechMetaTileEntity_PocketFusion(int aID, String aName, String aNameRegional, int aTier) {
		super(aID, aName, aNameRegional, aTier, 1, "It's like a midget Ra.", 1, 1, "PotionBrewer.png", "");
	}

	public GregtechMetaTileEntity_PocketFusion(String aName, int aTier, String aDescription,
			ITexture[][][] aTextures, String aGUIName, String aNEIName) {
		super(aName, aTier, 1, aDescription, aTextures, 1, 1, aGUIName, aNEIName);
	}

	@Override
	public String[] getDescription() {
		return new String[] { this.mDescription, "Not Very Fast, but not very big either.",
				"Each side pair in/out puts to different slots.", "Top & Bottom Sides are Outputs.",
				"Front & Back are Input Plasma 1.", "Sides are Input Plasma 2.", CORE.GT_Tooltip };
	}

	@Override
	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntity_PocketFusion(this.mName, this.mTier, this.mDescription,
				this.mTextures, this.mGUIName, this.mNEIName);
	}

	public int tier() {
		return this.mTier;
	}

	public int tierOverclock() {
		return this.mTier == 6 ? 0 : this.mTier == 7 ? 1 : 2;
	}

	private GT_RenderedTexture getCasingTexture() {
		return new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Advanced);
	}

	@Override
	public ITexture[][][] getTextureSet(final ITexture[] aTextures) {
		final ITexture[][][] rTextures = new ITexture[10][17][];
		for (byte i = -1; i < 16; i++) {
			rTextures[0][i + 1] = this.getFront(i);
			rTextures[1][i + 1] = this.getBack(i);
			rTextures[2][i + 1] = this.getBottom(i);
			rTextures[3][i + 1] = this.getTop(i);
			rTextures[4][i + 1] = this.getSides(i);
			rTextures[5][i + 1] = this.getFrontActive(i);
			rTextures[6][i + 1] = this.getBackActive(i);
			rTextures[7][i + 1] = this.getBottomActive(i);
			rTextures[8][i + 1] = this.getTopActive(i);
			rTextures[9][i + 1] = this.getSidesActive(i);
		}
		return rTextures;
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing,
			final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		return this.mTextures[(aActive ? 5 : 0) + (aSide == aFacing ? 0
				: aSide == GT_Utility.getOppositeSide(aFacing) ? 1 : aSide == 0 ? 2 : aSide == 1 ? 3 : 4)][aColorIndex
				                                                                                           + 1];
	}

	@Override
	public GT_Recipe.GT_Recipe_Map getRecipeList() {
		return Recipe_GT.Gregtech_Recipe_Map.sSlowFusionRecipes;
	}

	@Override
	public boolean isOutputFacing(final byte aSide) {
		return aSide == this.getBaseMetaTileEntity().getBackFacing();
	}

	@Override
	public boolean isFluidInputAllowed(FluidStack aFluid) {
		return (aFluid.getFluid().getName().contains("plasma")) || (super.isFluidInputAllowed(aFluid));
	}

	@Override
	public int getCapacity() {
		return 32000;
	}

	@Override
	public void abortProcess() {
		super.abortProcess();
	}

	long mFusionPoint = 20000000L;

	@Override
	public int checkRecipe() {
		Logger.INFO("Recipe Tick 1.");
		if (!this.mCanProcessRecipe) {
			Logger.INFO("Recipe Tick 1.1 - Cannot Process Recipe.");
			if (this.mChargeConsumed < mFusionPoint) {
				Logger.INFO("Recipe Tick 1.2 - Cannot Ignite Fusion, Charge too low.");
				this.mCharging = true;
				this.mCanProcessRecipe = false;
				if (this.getBaseMetaTileEntity().decreaseStoredEnergyUnits((mFusionPoint / 100), false)) {
					Logger.INFO("Recipe Tick 1.3 - Charging Internal storage. " + (mFusionPoint / 100) + "/"
							+ mFusionPoint);
					mChargeConsumed += (mFusionPoint / 100);
				}
			}
			else {
				mChargeConsumed = 0;
				this.mCharging = false;
				this.mCanProcessRecipe = true;

			}
		}
		else {
			Logger.INFO("Recipe Tick 1.1 - Try to Process Recipe.");
			if (checkRecipeMulti()) {
				Logger.INFO("Recipe Tick 1.2 - Process Recipe was Successful.");
				return 2;
			}
		}
		Logger.INFO("Recipe Tick 2. - Process Recipe failed.");
		return 0;
	}

	public ArrayList<FluidStack> getStoredFluids() {
		ArrayList<FluidStack> mList = new ArrayList<FluidStack>();
		mList.add(this.mFluid);
		mList.add(this.mFluid2);
		return mList;
	}

	public boolean checkRecipeMulti() {
		ArrayList<FluidStack> tFluidList = getStoredFluids();
		int tFluidList_sS = tFluidList.size();
		for (int i = 0; i < tFluidList_sS - 1; i++) {
			for (int j = i + 1; j < tFluidList_sS; j++) {
				if (GT_Utility.areFluidsEqual(tFluidList.get(i), tFluidList.get(j))) {
					if (tFluidList.get(i).amount >= tFluidList.get(j).amount) {
						tFluidList.remove(j--);
						tFluidList_sS = tFluidList.size();
					}
					else {
						tFluidList.remove(i--);
						tFluidList_sS = tFluidList.size();
						break;
					}
				}
			}
		}
		if (tFluidList.size() > 1) {
			FluidStack[] tFluids = tFluidList.toArray(new FluidStack[tFluidList.size()]);
			GT_Recipe tRecipe = getRecipeList().findRecipe(this.getBaseMetaTileEntity(), this.mLastRecipe, false,
					GT_Values.V[8], tFluids, new ItemStack[] {});
			
			if (tRecipe == null) {
				return false;
			}
			
			if ((tRecipe == null && !mRunningOnLoad) || (tRecipe != null && maxEUStore() < tRecipe.mSpecialValue)) {
				this.mLastRecipe = null;
				Logger.INFO("Just plain bad.");
				return false;
			}
			if (mRunningOnLoad || tRecipe.isRecipeInputEqual(true, tFluids, new ItemStack[] {})) {
				this.mLastRecipe = tRecipe;
				this.mEUt = (this.mLastRecipe.mEUt * overclock(this.mLastRecipe.mSpecialValue));
				this.mMaxProgresstime = this.mLastRecipe.mDuration / overclock(this.mLastRecipe.mSpecialValue);

				this.mEfficiencyIncrease = 10000;

				this.mOutputFluids = this.mLastRecipe.mFluidOutputs;
				mRunningOnLoad = false;
				return true;
			}
		}
		return false;
	}

	public int overclock(int mStartEnergy) {
		if (tierOverclock() == 1) {
			return 1;
		}
		if (tierOverclock() == 2) {
			return mStartEnergy < 160000000 ? 2 : 1;
		}
		return mStartEnergy < 160000000 ? 4 : mStartEnergy < 320000000 ? 2 : 1;
	}

	@Override
	public boolean displaysItemStack() {
		return false;
	}

	@Override
	public boolean doesAutoOutputFluids() {
		return true;
	}

	@Override
	public boolean doesEmptyContainers() {
		return true;
	}

	@Override
	public boolean doesFillContainers() {
		return true;
	}

	@Override
	public long maxAmperesIn() {
		return 16L;
	}

	@Override
	public long maxEUStore() {
		return Long.MAX_VALUE;
	}

	@Override
	public void doExplosion(long aExplosionPower) {
		super.doExplosion(aExplosionPower * 2);
	}

	@Override
	public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
		super.onPreTick(aBaseMetaTileEntity, aTick);
		onRunningTickMulti();
		if ((aBaseMetaTileEntity.isClientSide()) && (aBaseMetaTileEntity.isActive())
				&& (aBaseMetaTileEntity.getFrontFacing() != 1) && (aBaseMetaTileEntity.getCoverIDAtSide((byte) 1) == 0)
				&& (!aBaseMetaTileEntity.getOpacityAtSide((byte) 1))) {
			if (MathUtils.randInt(0, 4) == 4) {
				final Random tRandom = aBaseMetaTileEntity.getWorld().rand;
				aBaseMetaTileEntity.getWorld().spawnParticle("magicCrit",
						(aBaseMetaTileEntity.getXCoord() + 0.8F) - (tRandom.nextFloat() * 0.6F),
						aBaseMetaTileEntity.getYCoord() + 0.3f + (tRandom.nextFloat() * 0.2F),
						(aBaseMetaTileEntity.getZCoord() + 1.2F) - (tRandom.nextFloat() * 1.6F), 0.0D, 0.0D, 0.0D);
				aBaseMetaTileEntity.getWorld().spawnParticle("magicCrit",
						(aBaseMetaTileEntity.getXCoord() + 0.4F) - (tRandom.nextFloat() * 0.3F),
						aBaseMetaTileEntity.getYCoord() + 0.2f + (tRandom.nextFloat() * 0.1F),
						(aBaseMetaTileEntity.getZCoord() + 0.8F) - (tRandom.nextFloat() * 0.6F), 0.0D, 0.0D, 0.0D);
				aBaseMetaTileEntity.getWorld().spawnParticle("magicCrit",
						(aBaseMetaTileEntity.getXCoord() + 0.6F) - (tRandom.nextFloat() * 0.9F),
						aBaseMetaTileEntity.getYCoord() + 0.4f + (tRandom.nextFloat() * 0.3F),
						(aBaseMetaTileEntity.getZCoord() + 1.8F) - (tRandom.nextFloat() * 2.6F), 0.0D, 0.0D, 0.0D);
			}
		}
	}

	@Override
	public void saveNBTData(NBTTagCompound aNBT) {
		aNBT.setBoolean("mCanProcessRecipe", this.mCanProcessRecipe);
		aNBT.setBoolean("mCharging", this.mCharging);
		aNBT.setLong("mChargeConsumed", this.mChargeConsumed);
		aNBT.setInteger("mEfficiency", this.mEfficiency);
		aNBT.setInteger("mEfficiencyIncrease", this.mEfficiencyIncrease);
		aNBT.setInteger("mEfficiencyMax", this.mEfficiencyMax);
		aNBT.setInteger("mStartUpCheck", this.mStartUpCheck);
		aNBT.setInteger("mUpdate", mUpdate);
		aNBT.setInteger("mEfficiencyIncrease", mEfficiencyIncrease);
		aNBT.setBoolean("mRunningOnLoad", this.mRunningOnLoad);
		aNBT.setBoolean("mMachine", this.mMachine);
		aNBT.setLong("mEUStore", this.mEUStore);
		super.saveNBTData(aNBT);
	}

	@Override
	public void loadNBTData(NBTTagCompound aNBT) {
		if (mMaxProgresstime > 0)
			mRunningOnLoad = true;
		this.mCanProcessRecipe = aNBT.getBoolean("mCanProcessRecipe");
		this.mCharging = aNBT.getBoolean("mCharging");
		this.mChargeConsumed = aNBT.getLong("mChargeConsumed");
		this.mEfficiency = aNBT.getInteger("mEfficiency");
		this.mEfficiencyIncrease = aNBT.getInteger("mEfficiencyIncrease");
		this.mEfficiencyMax = aNBT.getInteger("mEfficiencyMax");
		this.mStartUpCheck = aNBT.getInteger("mStartUpCheck");
		this.mUpdate = aNBT.getInteger("mUpdate");
		this.mEfficiencyIncrease = aNBT.getInteger("mEfficiencyIncrease");
		this.mEfficiencyIncrease = aNBT.getInteger("mEfficiencyIncrease");
		this.mRunningOnLoad = aNBT.getBoolean("mRunningOnLoad");
		this.mMachine = aNBT.getBoolean("mMachine");
		this.mEUStore = aNBT.getLong("mEUStore");
		super.loadNBTData(aNBT);
	}

	@Override
	public String[] getInfoData() {
		String tier = tier() == 6 ? "I" : tier() == 7 ? "II" : "III";
		float plasmaOut = 0;
		String fusionName = "";
		int powerRequired = 0;
		if (this.mLastRecipe != null) {
			fusionName = this.mLastRecipe.mFluidOutputs[0].getLocalizedName() + " Fusion.";
			powerRequired = this.mLastRecipe.mEUt;
			if (this.mLastRecipe.getFluidOutput(0) != null) {
				plasmaOut = (float) this.mLastRecipe.getFluidOutput(0).amount / (float) this.mLastRecipe.mDuration;
			}
		}

		return new String[] { "Fusion Reactor MK " + tier, "EU Required: " + powerRequired + "EU/t",
				"Stored EU: " + this.getEUVar() + " / " + maxEUStore(), "Plasma Output: " + plasmaOut + "L/t",
				"Current Recipe: " + fusionName };
	}

	@Override
	public boolean isGivingInformation() {
		return true;
	}

	public ITexture[] getFront(final byte aColor) {
		return new ITexture[] { this.getCasingTexture(), new GT_RenderedTexture(TexturesGtBlock.Overlay_MatterFab) };
	}

	public ITexture[] getBack(final byte aColor) {
		return new ITexture[] { this.getCasingTexture(), new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_PIPE_OUT) };
	}

	public ITexture[] getBottom(final byte aColor) {
		return new ITexture[] { new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_FUSION_GLASS) };
	}

	public ITexture[] getTop(final byte aColor) {
		return new ITexture[] { new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_FUSION_GLASS) };
	}

	public ITexture[] getSides(final byte aColor) {
		return new ITexture[] { this.getCasingTexture(),
				new GT_RenderedTexture(TexturesGtBlock.Overlay_Machine_Dimensional_Orange) };
	}

	public ITexture[] getFrontActive(final byte aColor) {
		return new ITexture[] { this.getCasingTexture(),
				new GT_RenderedTexture(TexturesGtBlock.Overlay_MatterFab_Active) };
	}

	public ITexture[] getBackActive(final byte aColor) {
		return new ITexture[] { this.getCasingTexture(), new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_PIPE_OUT) };
	}

	public ITexture[] getBottomActive(final byte aColor) {
		return new ITexture[] { new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_FUSION_GLASS_YELLOW) };
	}

	public ITexture[] getTopActive(final byte aColor) {
		return new ITexture[] { new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_FUSION_GLASS_YELLOW) };
	}

	public ITexture[] getSidesActive(final byte aColor) {
		return new ITexture[] { this.getCasingTexture(),
				new GT_RenderedTexture(TexturesGtBlock.Overlay_Machine_Dimensional_Blue) };
	}

	@Override
	public void onMachineBlockUpdate() {
		this.mUpdate = 50;
	}

	@Override
	public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
		// super.onPostTick(aBaseMetaTileEntity, aTick);
		if (aBaseMetaTileEntity.isServerSide()) {
			// Logger.INFO("1");
			if (mEfficiency < 0)
				mEfficiency = 0;
			if (mRunningOnLoad) {
				Logger.INFO("2");
				this.mEUStore = (int) aBaseMetaTileEntity.getStoredEU();
				checkRecipeMulti();
			}
			if (--mUpdate == 0 || --mStartUpCheck == 0) {
				Logger.INFO("3");
				mMachine = true;
			}
			if (mStartUpCheck < 0) {
				//Logger.INFO("4");
				if (mMachine) {
					//Logger.INFO("5");

					if (aBaseMetaTileEntity.getStoredEU() + (2048 * tierOverclock()) < maxEUStore()) {
						if (aBaseMetaTileEntity.increaseStoredEnergyUnits(2048 * tierOverclock(), true)) {
							//Logger.INFO("5.5 A");							
						}
						else {
							//Logger.INFO("5.5 B");							
						}
					}
					if (this.mEUStore <= 0 && mMaxProgresstime > 0) {
						Logger.INFO("6");
						stopMachine();
						this.mLastRecipe = null;
					}
					if (mMaxProgresstime > 0) {
						Logger.INFO("7");
						this.getBaseMetaTileEntity().decreaseStoredEnergyUnits(mEUt, true);
						if (mMaxProgresstime > 0 && ++mProgresstime >= mMaxProgresstime) {
							if (mOutputFluids != null)
								for (FluidStack tStack : mOutputFluids)
									if (tStack != null)
										addOutput(tStack);
							mEfficiency = Math.max(0, (mEfficiency + mEfficiencyIncrease));
							mProgresstime = 0;
							mMaxProgresstime = 0;
							mEfficiencyIncrease = 0;
							if (mOutputFluids != null && mOutputFluids.length > 0) {

							}
							this.mEUStore = (int) aBaseMetaTileEntity.getStoredEU();
							if (aBaseMetaTileEntity.isAllowedToWork())
								checkRecipeMulti();
						}
					}
					else {
						//Logger.INFO("8");
						this.mEUStore = (int) aBaseMetaTileEntity.getStoredEU();
						if (aTick % 100 == 0 || aBaseMetaTileEntity.hasWorkJustBeenEnabled()
								|| aBaseMetaTileEntity.hasInventoryBeenModified()) {
							Logger.INFO("9");
							// turnCasingActive(mMaxProgresstime > 0);
							if (aBaseMetaTileEntity.isAllowedToWork()) {
								Logger.INFO("10");
								if (checkRecipeMulti()) {
									Logger.INFO("11");
									if (this.mEUStore < this.mLastRecipe.mSpecialValue) {
										Logger.INFO("12");
										mMaxProgresstime = 0;
										// turnCasingActive(false);
									}
									aBaseMetaTileEntity.decreaseStoredEnergyUnits(this.mLastRecipe.mSpecialValue, true);
								}
							}
							if (mMaxProgresstime <= 0)
								mEfficiency = Math.max(0, mEfficiency - 1000);
						}
					}
				}
				else {
					// turnCasingActive(false);
					Logger.INFO("Bad");
					this.mLastRecipe = null;
					stopMachine();
				}
			}
			Logger.INFO("Good | "+mMaxProgresstime);
			aBaseMetaTileEntity.setActive(mMaxProgresstime > 0);
		}
	}

	public boolean onRunningTickMulti() {
		if (this.getBaseMetaTileEntity().isServerSide()) {
			if (mEUt < 0) {
				if (!drainEnergyInput(((long) -mEUt * 10000) / Math.max(1000, mEfficiency))) {
					this.mLastRecipe = null;
					stopMachine();
					Logger.INFO("a1");
					return false;
				}
			}
			if (this.mEUStore <= 0) {
				this.mLastRecipe = null;
				stopMachine();
				Logger.INFO("a2");
				return false;
			}
		}
		return true;
	}

	public boolean drainEnergyInput(long aEU) {
		return false;
	}

	public boolean addOutput(FluidStack aLiquid) {
		if (aLiquid == null)
			return false;
		FluidStack copiedFluidStack = aLiquid.copy();
		this.mOutputFluid = copiedFluidStack;
		return false;
	}

	public void stopMachine() {
		mEUt = 0;
		mEfficiency = 0;
		mProgresstime = 0;
		mMaxProgresstime = 0;
		mEfficiencyIncrease = 0;
		getBaseMetaTileEntity().disableWorking();
	}

	@Override
	public boolean isLiquidInput(byte aSide) {
		switch (aSide) {
			case 0:
				return true;
			case 1:
				return true;
			case 2:
				return true;
			case 3:
				return false;
			case 4:
				return false;
			case 5:
				return false;
			default:
				return false;
		}
	}

	@Override
	public boolean isLiquidOutput(byte aSide) {
		switch (aSide) {
			case 0:
				return false;
			case 1:
				return false;
			case 2:
				return false;
			case 3:
				return true;
			case 4:
				return true;
			case 5:
				return true;
			default:
				return true;
		}
	}

	@Override
	public int fill(FluidStack aFluid, boolean doFill) {
		return super.fill(aFluid, doFill);
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {
		return super.drain(maxDrain, doDrain);
	}

	@Override
	public int getTankPressure() {
		return 500;
	}

	@Override
	public boolean canFill(ForgeDirection aSide, Fluid aFluid) {
		if (aSide == ForgeDirection.UP || aSide == ForgeDirection.DOWN) {
			return false;
		}
		else {
			return super.canFill(aSide, aFluid);
		}
	}

	@Override
	public boolean canDrain(ForgeDirection aSide, Fluid aFluid) {
		if (aSide == ForgeDirection.UP || aSide == ForgeDirection.DOWN) {
			return super.canDrain(aSide, aFluid);
		}
		else {
			return false;
		}
	}

	@Override
	public int fill_default(ForgeDirection aSide, FluidStack aFluid, boolean doFill) {
		if (aSide == ForgeDirection.UP || aSide == ForgeDirection.DOWN) {
			return 0;
		}
		else {
			return super.fill_default(aSide, aFluid, doFill);
		}
	}

	@Override
	public int fill(ForgeDirection aSide, FluidStack aFluid, boolean doFill) {
		if (aSide == ForgeDirection.UP || aSide == ForgeDirection.DOWN) {
			return super.fill(aSide, aFluid, doFill);
		}
		else {
			return 0;
		}
	}

	@Override
	public FluidStack drain(ForgeDirection aSide, FluidStack aFluid, boolean doDrain) {
		if (aSide == ForgeDirection.UP || aSide == ForgeDirection.DOWN) {
			return super.drain(aSide, aFluid, doDrain);
		}
		else {
			return null;
		}
	}

	@Override
	public FluidStack drain(ForgeDirection aSide, int maxDrain, boolean doDrain) {
		if (aSide == ForgeDirection.UP || aSide == ForgeDirection.DOWN) {
			return super.drain(aSide, maxDrain, doDrain);
		}
		else {
			return null;
		}
	}

	@Override
	public boolean isOverclockerUpgradable() {
		return true;
	}

	@Override
	public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
		// TODO Auto-generated method stub
		return super.onRightclick(aBaseMetaTileEntity, aPlayer);
	}

	@Override
	public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer, byte aSide, float aX,
			float aY, float aZ) {
		// TODO Auto-generated method stub
		return super.onRightclick(aBaseMetaTileEntity, aPlayer, aSide, aX, aY, aZ);
	}

	@Override
	public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		// TODO Auto-generated method stub
		return super.getServerGUI(aID, aPlayerInventory, aBaseMetaTileEntity);
	}

	@Override
	public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
		// TODO Auto-generated method stub
		super.onScrewdriverRightClick(aSide, aPlayer, aX, aY, aZ);
	}

	@Override
	public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
		return false;
	}

	@Override
	public boolean canInsertItem(int aIndex, ItemStack aStack, int aSide) {
		return false;
	}

	@Override
	public void onExplosion() {
		// TODO Auto-generated method stub
		super.onExplosion();
	}

	@Override
	public void startProcess() {
		this.sendLoopStart((byte) 1);
	}

	@Override
	public void startSoundLoop(byte aIndex, double aX, double aY, double aZ) {
		super.startSoundLoop(aIndex, aX, aY, aZ);
		if (aIndex == 1) {
			GT_Utility.doSoundAtClient((String) GregTech_API.sSoundList.get(Integer.valueOf(212)), 10, 1.0F, aX, aY,
					aZ);
		}
	}

}
