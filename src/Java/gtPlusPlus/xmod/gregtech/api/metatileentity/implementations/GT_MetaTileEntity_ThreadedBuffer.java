package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.entity.player.EntityPlayer;
import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_Utility;

import gtPlusPlus.xmod.gregtech.api.objects.GregtechBufferThread;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Buffer;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;

public abstract class GT_MetaTileEntity_ThreadedBuffer extends GT_MetaTileEntity_Buffer {

	protected GregtechBufferThread mLogicThread;
	public final ItemStack[] mInventorySynchro;

	public GT_MetaTileEntity_ThreadedBuffer(final int aID, final String aName, final String aNameRegional, final int aTier,
			final int aInvSlotCount, final String aDescription) {
		super(aID, aName, aNameRegional, aTier, aInvSlotCount, aDescription);
		this.bOutput = false;
		this.bRedstoneIfFull = false;
		this.bInvert = false;
		this.mSuccess = 0;
		this.mTargetStackSize = 0;
		this.mInventorySynchro = new ItemStack[aInvSlotCount];
	}

	public GT_MetaTileEntity_ThreadedBuffer(final int aID, final String aName, final String aNameRegional, final int aTier,
			final int aInvSlotCount, final String[] aDescription) {
		super(aID, aName, aNameRegional, aTier, aInvSlotCount, aDescription);
		this.bOutput = false;
		this.bRedstoneIfFull = false;
		this.bInvert = false;
		this.mSuccess = 0;
		this.mTargetStackSize = 0;
		this.mInventorySynchro = new ItemStack[aInvSlotCount];
	}

	public GT_MetaTileEntity_ThreadedBuffer(final String aName, final int aTier, final int aInvSlotCount,
			final String aDescription, final ITexture[][][] aTextures) {
		super(aName, aTier, aInvSlotCount, aDescription, aTextures);
		this.bOutput = false;
		this.bRedstoneIfFull = false;
		this.bInvert = false;
		this.mSuccess = 0;
		this.mTargetStackSize = 0;
		this.mInventorySynchro = new ItemStack[aInvSlotCount];
	}

	public GT_MetaTileEntity_ThreadedBuffer(final String aName, final int aTier, final int aInvSlotCount,
			final String[] aDescription, final ITexture[][][] aTextures) {
		super(aName, aTier, aInvSlotCount, aDescription, aTextures);
		this.bOutput = false;
		this.bRedstoneIfFull = false;
		this.bInvert = false;
		this.mSuccess = 0;
		this.mTargetStackSize = 0;
		this.mInventorySynchro = new ItemStack[aInvSlotCount];
	}

	public synchronized final GregtechBufferThread getLogicThread() {
		if (mLogicThread != null) {
			return mLogicThread;
		}
		else {
			return this.mLogicThread = GregtechBufferThread.getBufferThread(this.getBaseMetaTileEntity().getWorld());
		}
	}

	public ITexture[][][] getTextureSet(final ITexture[] aTextures) {
		final ITexture[][][] rTextures = new ITexture[6][17][];
		final ITexture tIcon = this.getOverlayIcon();
		final ITexture tOut = (ITexture) new GT_RenderedTexture((IIconContainer) Textures.BlockIcons.OVERLAY_PIPE_OUT);
		final ITexture tUp = (ITexture) new GT_RenderedTexture((IIconContainer) Textures.BlockIcons.ARROW_UP);
		final ITexture tDown = (ITexture) new GT_RenderedTexture((IIconContainer) Textures.BlockIcons.ARROW_DOWN);
		final ITexture tLeft = (ITexture) new GT_RenderedTexture((IIconContainer) Textures.BlockIcons.ARROW_LEFT);
		final ITexture tRight = (ITexture) new GT_RenderedTexture((IIconContainer) Textures.BlockIcons.ARROW_RIGHT);
		for (byte i = -1; i < 16; ++i) {
			rTextures[0][i + 1] = new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier][i + 1], tOut};
			rTextures[1][i + 1] = new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier][i + 1], tRight, tIcon};
			rTextures[2][i + 1] = new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier][i + 1], tDown, tIcon};
			rTextures[3][i + 1] = new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier][i + 1], tLeft, tIcon};
			rTextures[4][i + 1] = new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier][i + 1], tUp, tIcon};
			rTextures[5][i + 1] = new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier][i + 1], tIcon};
		}
		return rTextures;
	}

	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing,
			final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == aFacing) {
			return this.mTextures[5][aColorIndex + 1];
		}
		if (GT_Utility.getOppositeSide((int) aSide) == aFacing) {
			return this.mTextures[0][aColorIndex + 1];
		}
		Label_0356 : {
			switch (aFacing) {
				case 0 : {
					return this.mTextures[4][aColorIndex + 1];
				}
				case 1 : {
					return this.mTextures[2][aColorIndex + 1];
				}
				case 2 : {
					switch (aSide) {
						case 0 : {
							return this.mTextures[2][aColorIndex + 1];
						}
						case 1 : {
							return this.mTextures[2][aColorIndex + 1];
						}
						case 4 : {
							return this.mTextures[1][aColorIndex + 1];
						}
						case 5 : {
							return this.mTextures[3][aColorIndex + 1];
						}
						default : {
							break Label_0356;
						}
					}
				}
				case 3 : {
					switch (aSide) {
						case 0 : {
							return this.mTextures[4][aColorIndex + 1];
						}
						case 1 : {
							return this.mTextures[4][aColorIndex + 1];
						}
						case 4 : {
							return this.mTextures[3][aColorIndex + 1];
						}
						case 5 : {
							return this.mTextures[1][aColorIndex + 1];
						}
						default : {
							break Label_0356;
						}
					}
				}
				case 4 : {
					switch (aSide) {
						case 0 : {
							return this.mTextures[3][aColorIndex + 1];
						}
						case 1 : {
							return this.mTextures[1][aColorIndex + 1];
						}
						case 2 : {
							return this.mTextures[3][aColorIndex + 1];
						}
						case 3 : {
							return this.mTextures[1][aColorIndex + 1];
						}
						default : {
							break Label_0356;
						}
					}					
				}
				case 5 : {
					switch (aSide) {
						case 0 : {
							return this.mTextures[1][aColorIndex + 1];
						}
						case 1 : {
							return this.mTextures[3][aColorIndex + 1];
						}
						case 2 : {
							return this.mTextures[1][aColorIndex + 1];
						}
						case 3 : {
							return this.mTextures[3][aColorIndex + 1];
						}
						default : {
							break Label_0356;
						}
					}
				}
			}
		}
		return this.mTextures[5][aColorIndex + 1];
	}

	public boolean isSimpleMachine() {
		return false;
	}

	public boolean isValidSlot(final int aIndex) {
		return aIndex < this.mInventorySynchro.length - 1;
	}

	public boolean isFacingValid(final byte aFacing) {
		return true;
	}

	public boolean isEnetInput() {
		return true;
	}

	public boolean isEnetOutput() {
		return true;
	}

	public boolean isInputFacing(final byte aSide) {
		return !this.isOutputFacing(aSide);
	}

	public boolean isOutputFacing(final byte aSide) {
		return this.getBaseMetaTileEntity().getBackFacing() == aSide;
	}

	public boolean isTeleporterCompatible() {
		return false;
	}

	public long getMinimumStoredEU() {
		return 512L;
	}

	public long maxEUStore() {
		return 512L + GT_Values.V[this.mTier] * 50L;
	}

	public long maxEUInput() {
		return GT_Values.V[this.mTier];
	}

	public long maxEUOutput() {
		return this.bOutput ? GT_Values.V[this.mTier] : 0L;
	}

	public long maxAmperesIn() {
		return 2L;
	}

	public long maxAmperesOut() {
		return 2L;
	}

	public boolean isAccessAllowed(final EntityPlayer aPlayer) {
		return true;
	}

	public abstract ITexture getOverlayIcon();

	public boolean onRightclick(final IGregTechTileEntity aBaseMetaTileEntity, final EntityPlayer aPlayer) {
		if (aBaseMetaTileEntity.isClientSide()) {
			return true;
		}
		aBaseMetaTileEntity.openGUI(aPlayer);
		return true;
	}

	public void saveNBTData(final NBTTagCompound aNBT) {
		aNBT.setBoolean("bInvert", this.bInvert);
		aNBT.setBoolean("bOutput", this.bOutput);
		aNBT.setBoolean("bRedstoneIfFull", this.bRedstoneIfFull);
		aNBT.setInteger("mTargetStackSize", this.mTargetStackSize);
	}

	public void loadNBTData(final NBTTagCompound aNBT) {
		this.bInvert = aNBT.getBoolean("bInvert");
		this.bOutput = aNBT.getBoolean("bOutput");
		this.bRedstoneIfFull = aNBT.getBoolean("bRedstoneIfFull");
		this.mTargetStackSize = aNBT.getInteger("mTargetStackSize");
	}

	public void setItemNBT(final NBTTagCompound aNBT) {
		super.setItemNBT(aNBT);
		if (this.mTargetStackSize > 0) {
			aNBT.setInteger("mTargetStackSize", this.mTargetStackSize);
		}
	}

	public void onScrewdriverRightClick(final byte aSide, final EntityPlayer aPlayer, final float aX, final float aY,
			final float aZ) {
		if (aSide == this.getBaseMetaTileEntity().getBackFacing()) {
			this.mTargetStackSize = (byte) ((this.mTargetStackSize + (aPlayer.isSneaking() ? -1 : 1)) % 65);
			if (this.mTargetStackSize < 0) {
				this.mTargetStackSize = 64;
			}
			if (this.mTargetStackSize == 0) {
				GT_Utility.sendChatToPlayer(aPlayer, this.trans("098", "Do not regulate Item Stack Size"));
			} else {
				GT_Utility.sendChatToPlayer(aPlayer,
						this.trans("099", "Regulate Item Stack Size to: ") + this.mTargetStackSize);
			}
		}
	}

	public synchronized void onPostTick(final IGregTechTileEntity aBaseMetaTileEntity, final long aTimer) {
		if (aBaseMetaTileEntity.isServerSide())
			getLogicThread().onPostTick(aBaseMetaTileEntity, aTimer, this);
	}

	public void onFirstTick(final IGregTechTileEntity aBaseMetaTileEntity) {
		for (byte b = 0; b < 6; ++b) {
			aBaseMetaTileEntity.setInternalOutputRedstoneSignal(b, (byte) 0);
		}
	}

	protected synchronized void moveItems(final IGregTechTileEntity aBaseMetaTileEntity, final long aTimer) {
		if (aBaseMetaTileEntity.isServerSide())
			getLogicThread().moveItems(aBaseMetaTileEntity, aTimer, this);
	}

	public boolean allowPullStack(final IGregTechTileEntity aBaseMetaTileEntity, final int aIndex, final byte aSide,
			final ItemStack aStack) {
		return true;
	}

	public boolean allowPutStack(final IGregTechTileEntity aBaseMetaTileEntity, final int aIndex, final byte aSide,
			final ItemStack aStack) {
		return aSide != aBaseMetaTileEntity.getBackFacing();
	}

	public boolean allowGeneralRedstoneOutput() {
		return true;
	}

	//Custom inventory handler

	@Override
	public synchronized ItemStack[] getRealInventory() {
		return this.mInventorySynchro;
	}

	@Override
	public synchronized boolean canInsertItem(final int aIndex, final ItemStack aStack, final int aSide) {
		return this.isValidSlot(aIndex) && aStack != null && aIndex < getSizeInventory()
				&& (this.mInventorySynchro[aIndex] == null || GT_Utility.areStacksEqual(aStack, this.mInventorySynchro[aIndex]))
				&& this.allowPutStack(this.getBaseMetaTileEntity(), aIndex, (byte) aSide, aStack);
	}

	@Override
	public synchronized boolean canExtractItem(final int aIndex, final ItemStack aStack, final int aSide) {
		return this.isValidSlot(aIndex) && aStack != null && aIndex < getSizeInventory()
				&& this.allowPullStack(this.getBaseMetaTileEntity(), aIndex, (byte) aSide, aStack);
	}

	@Override
	public synchronized int getSizeInventory() {
		return this.mInventorySynchro.length;
	}

	@Override
	public synchronized ItemStack getStackInSlot(final int aIndex) {
		if (aIndex >= 0 && aIndex < getSizeInventory()) {
			return this.mInventorySynchro[aIndex];
		}
		return null;
	}

	@Override
	public synchronized void setInventorySlotContents(final int aIndex, final ItemStack aStack) {
		if (aIndex >= 0 && aIndex < getSizeInventory()) {
			this.mInventorySynchro[aIndex] = aStack;
		}
	}
}