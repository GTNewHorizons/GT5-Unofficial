package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.gui.GT_Container_4by4;
import gregtech.api.gui.GT_GUIContainer_4by4;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.GT_MetaBase_Item;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.metatileentity.custom.power.GTPP_MTE_TieredMachineBlock;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class GT_MetaTileEntity_BasicBreaker extends GTPP_MTE_TieredMachineBlock {
	
	public boolean mCharge = false;
	public boolean mDecharge = false;
	public int mBatteryCount = 0;
	public int mChargeableCount = 0;
	private long count = 0L;
	private long mStored = 0L;
	private long mMax = 0L;

	public GT_MetaTileEntity_BasicBreaker(int aID, String aName, String aNameRegional, int aTier,
			String aDescription, int aSlotCount) {
		super(aID, aName, aNameRegional, aTier, aSlotCount, aDescription, new ITexture[0]);
	}

	public GT_MetaTileEntity_BasicBreaker(String aName, int aTier, String aDescription, ITexture[][][] aTextures,
			int aSlotCount) {
		super(aName, aTier, aSlotCount, aDescription, aTextures);
	}

	public GT_MetaTileEntity_BasicBreaker(String aName, int aTier, String[] aDescription,
			ITexture[][][] aTextures, int aSlotCount) {
		super(aName, aTier, aSlotCount, aDescription, aTextures);
	}

	public String[] getDescription() {
		String []s1 = super.getDescription();		
		s1 = new String[0];
		return s1;
	}

	public ITexture[][][] getTextureSet(ITexture[] aTextures) {
		ITexture[][][] rTextures = new ITexture[2][17][];

		for (byte i = -1; i < 16; ++i) {
			rTextures[0][i + 1] = new ITexture[]{BlockIcons.MACHINE_CASINGS[this.mTier][i + 1],
					this.mInventory.length > 4
							? BlockIcons.OVERLAYS_ENERGY_IN_MULTI[Math.min(12, mTier)]
							: BlockIcons.OVERLAYS_ENERGY_IN[Math.min(12, mTier)]};
			
			rTextures[1][i + 1] = new ITexture[]{BlockIcons.MACHINE_CASINGS[this.mTier][i + 1],
					this.mInventory.length > 4
							? BlockIcons.OVERLAYS_ENERGY_OUT_MULTI[this.mTier]
							: BlockIcons.OVERLAYS_ENERGY_OUT[this.mTier]};
		}

		return rTextures;
	}

	public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex,
			boolean aActive, boolean aRedstone) {
		return this.mTextures[aSide == aFacing ? 1 : 0][aColorIndex + 1];
	}

	public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GT_MetaTileEntity_BasicBreaker(this.mName, this.mTier, this.mDescriptionArray, this.mTextures,
				this.mInventory.length);
	}

	public boolean isSimpleMachine() {
		return false;
	}

	public boolean isElectric() {
		return true;
	}

	public boolean isValidSlot(int aIndex) {
		return true;
	}

	public boolean isFacingValid(byte aFacing) {
		return true;
	}

	public boolean isEnetInput() {
		return true;
	}

	public boolean isEnetOutput() {
		return true;
	}

	public boolean isInputFacing(byte aSide) {
		return aSide != this.getBaseMetaTileEntity().getFrontFacing();
	}

	public boolean isOutputFacing(byte aSide) {
		return aSide == this.getBaseMetaTileEntity().getFrontFacing();
	}

	public boolean isTeleporterCompatible() {
		return false;
	}

	public long getMinimumStoredEU() {
		return GT_Values.V[this.mTier] * 16L * 16;
	}

	public long maxEUStore() {
		return GT_Values.V[this.mTier] * 64L * 16;
	}

	public long maxEUInput() {
		return GT_Values.V[this.mTier] * 16;
	}

	public long maxEUOutput() {
		return GT_Values.V[this.mTier];
	}

	public long maxAmperesIn() {
		return (long) (1);
	}

	public long maxAmperesOut() {
		return (long) 16;
	}

	public int rechargerSlotStartIndex() {
		return 0;
	}

	public int dechargerSlotStartIndex() {
		return 0;
	}

	public int rechargerSlotCount() {
		return 0;
	}

	public int dechargerSlotCount() {
		return 0;
	}

	public int getProgresstime() {
		return (int) this.getBaseMetaTileEntity().getUniversalEnergyStored();
	}

	public int maxProgresstime() {
		return (int) this.getBaseMetaTileEntity().getUniversalEnergyCapacity();
	}

	public boolean isAccessAllowed(EntityPlayer aPlayer) {
		return true;
	}

	public void saveNBTData(NBTTagCompound aNBT) {
	}

	public void loadNBTData(NBTTagCompound aNBT) {
	}

	public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
		if (aBaseMetaTileEntity.isClientSide()) {
			return true;
		} else {
			aBaseMetaTileEntity.openGUI(aPlayer);
			return true;
		}
	}

	public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return new GT_Container_4by4(aPlayerInventory, aBaseMetaTileEntity);
	}

	public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return new GT_GUIContainer_4by4(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName());
	}

	public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
		super.onPostTick(aBaseMetaTileEntity, aTick);
		if (aBaseMetaTileEntity.isServerSide()) {
			/*this.mCharge = aBaseMetaTileEntity.getStoredEU() / 2L > aBaseMetaTileEntity.getEUCapacity() / 3L;
			this.mDecharge = aBaseMetaTileEntity.getStoredEU() < aBaseMetaTileEntity.getEUCapacity() / 3L;
			this.mBatteryCount = 0;
			this.mChargeableCount = 0;
			ItemStack[] arg3 = this.mInventory;
			int arg4 = arg3.length;

			for (int arg5 = 0; arg5 < arg4; ++arg5) {
				ItemStack tStack = arg3[arg5];
				if (GT_ModHandler.isElectricItem(tStack, this.mTier)) {
					if (GT_ModHandler.isChargerItem(tStack)) {
						++this.mBatteryCount;
					}

					++this.mChargeableCount;
				}
			}*/
		}

	}

	public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
		return false;
	}

	public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
		return true;
	}

	public int getInventoryStackLimit() {
		return 1;
	}

	public long[] getStoredEnergy() {
		boolean scaleOverflow = false;
		boolean storedOverflow = false;
		long tScale = this.getBaseMetaTileEntity().getEUCapacity();
		long tStored = this.getBaseMetaTileEntity().getStoredEU();
		long tStep = 0L;
		if (this.mInventory != null) {
			ItemStack[] arg8 = this.mInventory;
			int arg9 = arg8.length;

			for (int arg10 = 0; arg10 < arg9; ++arg10) {
				ItemStack aStack = arg8[arg10];
				if (GT_ModHandler.isElectricItem(aStack)) {
					if (aStack.getItem() instanceof GT_MetaBase_Item) {
						Long[] stats = ((GT_MetaBase_Item) aStack.getItem()).getElectricStats(aStack);
						if (stats != null) {
							if (stats[0].longValue() > 4611686018427387903L) {
								scaleOverflow = true;
							}

							tScale += stats[0].longValue();
							tStep = ((GT_MetaBase_Item) aStack.getItem()).getRealCharge(aStack);
							if (tStep > 4611686018427387903L) {
								storedOverflow = true;
							}

							tStored += tStep;
						}
					} else if (aStack.getItem() instanceof IElectricItem) {
						tStored += (long) ElectricItem.manager.getCharge(aStack);
						tScale += (long) ((IElectricItem) aStack.getItem()).getMaxCharge(aStack);
					}
				}
			}
		}

		if (scaleOverflow) {
			tScale = Long.MAX_VALUE;
		}

		if (storedOverflow) {
			tStored = Long.MAX_VALUE;
		}

		return new long[]{tStored, tScale};
	}

	public String[] getInfoData() {		
		return new String[]{};
	}

	public boolean isGivingInformation() {
		return true;
	}
}