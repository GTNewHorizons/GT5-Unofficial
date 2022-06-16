package gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic;

import java.util.*;

import gregtech.api.enums.*;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicTank;
import gregtech.api.objects.*;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.gui.basic.*;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import ic2.api.crops.*;
import ic2.core.item.DamageHandler;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class GT_MetaTileEntity_CropHarvestor extends GT_MetaTileEntity_BasicTank {

	protected String mLocalName;

	private static final int SLOT_WEEDEX_1 = 1;
	private static final int SLOT_WEEDEX_2 = 2;
	private static final int SLOT_FERT_1 = 3;
	private static final int SLOT_FERT_4 = 6;
	private static final int SLOT_OUTPUT_START = 7;

	public boolean mModeAlternative = false;

	public GT_MetaTileEntity_CropHarvestor(final int aID, final int aTier, final String aDescription) {
		super(aID, "basicmachine.cropharvester.0"+aTier, "Crop Manager ("+GT_Values.VN[aTier]+")", aTier, 21, aDescription);
		this.mLocalName = "Crop Manager ("+GT_Values.VN[aTier]+")";
	}

	public GT_MetaTileEntity_CropHarvestor(final String aName, final int aTier, final String aDescription, final ITexture[][][] aTextures) {
		super(aName, aTier, 21, aDescription, aTextures);
		this.mLocalName = "Crop Manager ("+GT_Values.VN[aTier]+")";
	}

	@Override
	public Object getServerGUI(final int aID, final InventoryPlayer aPlayerInventory, final IGregTechTileEntity aBaseMetaTileEntity) {
		return new GT_Container_CropHarvestor(aPlayerInventory, aBaseMetaTileEntity);
	}

	@Override
	public Object getClientGUI(final int aID, final InventoryPlayer aPlayerInventory, final IGregTechTileEntity aBaseMetaTileEntity) {
		return new GT_GUIContainer_CropHarvestor(aPlayerInventory, aBaseMetaTileEntity, this.mLocalName);
	}

	@Override
	public boolean isTransformerUpgradable() {
		return true;
	}

	@Override
	public boolean isOverclockerUpgradable() {
		return true;
	}

	@Override
	public boolean isSimpleMachine() {
		return true;
	}

	@Override
	public boolean isAccessAllowed(EntityPlayer aPlayer) {
		return true;
	}

	@Override
	public boolean isInputFacing(byte aSide) {
		return true;
	}

	@Override
	public boolean isEnetInput() {
		return true;
	}

	@Override
	public boolean isElectric() {
		return true;
	}

	@Override
	public long maxAmperesIn() {
		return 8;
	}

	@Override
	public long getMinimumStoredEU() {
		return GT_Values.V[this.mTier];
	}

	@Override
	public long maxEUStore() {
		return GT_Values.V[this.mTier] * (this.mTier * GT_Values.V[this.mTier]);
	}

	@Override
	public long maxEUInput() {
		return GT_Values.V[this.mTier];
	}

	@Override
	public int getCapacity() {
		return 32000 * this.mTier;
	}

	@Override
	public int getTankPressure() {
		return -100;
	}

	@Override
	public boolean onRightclick(final IGregTechTileEntity aBaseMetaTileEntity, final EntityPlayer aPlayer) {
		if (aBaseMetaTileEntity.isClientSide()) {
			return true;
		}
		aBaseMetaTileEntity.openGUI(aPlayer);
		return true;
	}

	@Override
	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GT_MetaTileEntity_CropHarvestor(this.mName, this.mTier, this.mDescription, this.mTextures);
	}

	@Override
	public int getSizeInventory() {
		return 21;
	}

	private static final int getRange(int aTier) {
		switch(aTier) {
		case 1:
			return 1;
		case 2:
			return 5;
		case 3:
			return 9;
		case 4:
			return 13;
		case 5:
			return 17;
		case 6:
			return 21;
		case 7:
			return 25;
		case 8:
			return 29;
		case 9:
			return 33;
		default:
			return 0;
		}
	}

	private HashSet<ICropTile> mCropCache = new HashSet<ICropTile>();
	private boolean mInvalidCache = false;

	public boolean doesInventoryHaveSpace() {
		for (int i = SLOT_OUTPUT_START; i < this.getSizeInventory(); i++) {
			if (this.mInventory[i] == null || this.mInventory[i].stackSize < 64) {
				return true;
			}
		}
		return false;
	}

	public long powerUsage() {
		return this.maxEUInput() / 8;
	}

	public long powerUsageSecondary() {
		return this.maxEUInput() / 32;
	}


	public static AutoMap<ItemStack> splitOutputStacks(ItemStack aOutput) {
		return splitOutputStacks(new ItemStack[] {aOutput});
	}

	public static AutoMap<ItemStack> splitOutputStacks(ItemStack[] aOutputs) {
		AutoMap<ItemStack> aOutputMap = new AutoMap<ItemStack>();
		for (ItemStack aStack : aOutputs) {
			if (aStack != null) {
				if (aStack.stackSize <= 64) {
					aOutputMap.add(aStack);
				} else {
					int aStacks = 0;
					int aExcess = 0;
					int aOriginalSize = aStack.stackSize;
					while (aOriginalSize >= 64) {
						aStacks += 1;
						aOriginalSize -= 64;
					}
					aExcess = aOriginalSize;
					for (int i = 0; i < aStacks; i++) {
						aOutputMap.add(ItemUtils.getSimpleStack(aStack, 64));
					}
					aOutputMap.add(ItemUtils.getSimpleStack(aStack, aExcess));
				}
			}
		}
		return aOutputMap;
	}

	@Override
	public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
		super.onPostTick(aBaseMetaTileEntity, aTick);
		if (getBaseMetaTileEntity().isServerSide() && getBaseMetaTileEntity().isAllowedToWork() && (getBaseMetaTileEntity().hasWorkJustBeenEnabled() || aTick % 100 == 0)) {
			if (this.getBaseMetaTileEntity().getUniversalEnergyStored() >= getMinimumStoredEU()) {

				int aTileX = this.getBaseMetaTileEntity().getXCoord();
				int aTileY = this.getBaseMetaTileEntity().getXCoord();
				int aTileZ = this.getBaseMetaTileEntity().getXCoord();

				int aRadius = 10 + getRange(this.mTier);
				int aSide = (aRadius-1)/2;
				ArrayList<ItemStack> aAllDrops = new ArrayList<ItemStack>();

				if (this.mCropCache.isEmpty() || aTick % 1200 == 0 || this.mInvalidCache) {
					if (!this.mCropCache.isEmpty()) {
						this.mCropCache.clear();
					}
					//Logger.INFO("Looking for crops.");
					for (int y = 0; y <= 2; y++) {
						for (int x = (-aSide); x <= aSide; x++) {
							for (int z = (-aSide); z <= aSide; z++) {
								TileEntity tTileEntity = getBaseMetaTileEntity().getTileEntityOffset(x, y, z);
								if (tTileEntity != null && tTileEntity instanceof ICropTile) {
									ICropTile tCrop = (ICropTile) tTileEntity;
									this.mCropCache.add(tCrop);
								}
							}
						}
					}
				}

				//Process Cache
				if (doesInventoryHaveSpace()) {
					for (ICropTile tCrop : this.mCropCache) {
						if (tCrop == null) {
							this.mInvalidCache = true;
							break;
						}
						CropCard aCrop = tCrop.getCrop();
						if (aCrop != null) {
							//Logger.INFO("Found "+aCrop.displayName()+" at offset "+x+", "+y+", "+z);
							if (!aCrop.canGrow(tCrop) && aCrop.canBeHarvested(tCrop)) {
								if (getBaseMetaTileEntity().decreaseStoredEnergyUnits(powerUsage(), true)) {
									ItemStack[] aHarvest = tCrop.harvest_automated(true);
									if (aHarvest != null && aHarvest.length > 0) {
										for (ItemStack aStack : aHarvest) {
											if (aStack.stackSize > 0) {
												if (this.mTier * 5 > MathUtils.randInt(1, 100)) {
													aStack.stackSize += Math.floor(tCrop.getGain()/10);
													Logger.INFO("Bonus output given for "+aCrop.displayName());
												}
												Logger.INFO("Harvested "+aCrop.displayName());
												aAllDrops.addAll(splitOutputStacks(aStack));
											}
										}
									}
								}
							}
							if (this.mModeAlternative) {
								processSecondaryFunctions(tCrop);
							}
						}
					}

					if (!aAllDrops.isEmpty()) {
						Logger.INFO("Handling "+aAllDrops.size()+" Harvests");
						Iterator<ItemStack> iter = aAllDrops.iterator();
						while (iter.hasNext()) {
							ItemStack aDrop = iter.next();
							if (ItemUtils.checkForInvalidItems(aDrop)) {

								for (int i = SLOT_OUTPUT_START; i < this.getSizeInventory(); i++) {
									if (this.mInventory[i] != null) {
										//Logger.INFO("Slot "+i+" contains "+this.mInventory[i].getDisplayName());
										if (GT_Utility.areStacksEqual(aDrop, this.mInventory[i], false)) {
											//Same
											if (this.mInventory[i].stackSize < 64 && (this.mInventory[i].stackSize + aDrop.stackSize <= 64)) {
												//can merge
												//Logger.INFO("Slot "+i+" size: "+mInventory[i].stackSize+" + Drop Size: "+aDrop.stackSize+" = "+(mInventory[i].stackSize + aDrop.stackSize));
												this.mInventory[i].stackSize += aDrop.stackSize;
												break;
											}
											else if (this.mInventory[i].stackSize < 64 && (this.mInventory[i].stackSize + aDrop.stackSize > 64)) {
												//can merge
												//Logger.INFO("Slot "+i+" size: "+mInventory[i].stackSize+" + Drop Size: "+aDrop.stackSize+" = "+(mInventory[i].stackSize + aDrop.stackSize));
												int aRemainder = this.mInventory[i].stackSize + aDrop.stackSize - 64;
												Logger.INFO("Remainder: "+aRemainder+", Continuing.");
												this.mInventory[i].stackSize = 64;
												aDrop.stackSize = aRemainder;
												continue;
											}
											else {
												//Logger.INFO("Slot "+i+" size: 64, Continuing.");
												continue;
											}
										}
									}
									else {
										//Logger.INFO("Slot "+i+" is empty, setting to "+aDrop.getDisplayName()+" x"+aDrop.stackSize);
										this.mInventory[i] = aDrop;
										break;
									}
								}
							}

						}
					}
				}
			}
		}
	}

	public boolean hasFertilizer() {
		for (int i = SLOT_FERT_1; i <= SLOT_FERT_4; i++) {
			if (this.mInventory[i] != null) {
				return true;
			}
		}
		return false;
	}

	public boolean consumeFertilizer(boolean aSimulate) {
		if (hasFertilizer()) {
			for (int i = SLOT_FERT_1; i <= SLOT_FERT_4; i++) {
				if (this.mInventory[i] != null) {
					consume(i, 1, aSimulate);
					return true;
				}
			}
		}
		return false;
	}

	public boolean hasWeedEX() {
		for (int i = SLOT_WEEDEX_1; i <= SLOT_WEEDEX_2; i++) {
			if (this.mInventory[i] != null) {
				return true;
			}
		}
		return false;
	}

	public boolean consumeWeedEX(boolean aSimulate) {
		if (hasWeedEX()) {
			for (int i = SLOT_WEEDEX_1; i <= SLOT_WEEDEX_2; i++) {
				if (this.mInventory[i] != null) {
					damage(i, 1, aSimulate);
					return true;
				}
			}
		}
		return false;
	}

	public void processSecondaryFunctions(ICropTile aCrop) {
		if (!this.mModeAlternative) {
			return;
		}
		if (hasFertilizer() && consumeFertilizer(true) && this.getBaseMetaTileEntity().getUniversalEnergyStored() >= getMinimumStoredEU() && getBaseMetaTileEntity().decreaseStoredEnergyUnits(powerUsageSecondary(), true) && applyFertilizer(aCrop)) {
			if (consumeFertilizer(false)) {
				//Logger.INFO("Consumed Fert.");
			}
		}
		if (this.getFluidAmount() > 0 && this.getBaseMetaTileEntity().getUniversalEnergyStored() >= getMinimumStoredEU() && getBaseMetaTileEntity().decreaseStoredEnergyUnits(powerUsageSecondary(), true) && applyHydration(aCrop)) {
			//Logger.INFO("Consumed Water.");
		}
		if (hasWeedEX() && consumeWeedEX(true) && this.getBaseMetaTileEntity().getUniversalEnergyStored() >= getMinimumStoredEU() && getBaseMetaTileEntity().decreaseStoredEnergyUnits(powerUsageSecondary(), true) && applyWeedEx(aCrop)) {
			if (consumeWeedEX(false)) {
				//Logger.INFO("Consumed Weed-EX.");
			}
		}
	}


	public boolean applyWeedEx(ICropTile aCrop) {
		if (aCrop.getWeedExStorage() < 150) {
			aCrop.setWeedExStorage(aCrop.getWeedExStorage() + 50);
			boolean triggerDecline;
			triggerDecline = aCrop.getWorld().rand.nextInt(3) == 0;
			if (aCrop.getCrop() != null && aCrop.getCrop().isWeed(aCrop) && aCrop.getWeedExStorage() >= 75 && triggerDecline) {
				switch (aCrop.getWorld().rand.nextInt(5)) {
				case 0 :
					if (aCrop.getGrowth() > 0) {
						aCrop.setGrowth((byte) (aCrop.getGrowth() - 1));
					}
				case 1 :
					if (aCrop.getGain() > 0) {
						aCrop.setGain((byte) (aCrop.getGain() - 1));
					}
				default :
					if (aCrop.getResistance() > 0) {
						aCrop.setResistance((byte) (aCrop.getResistance() - 1));
					}
				}
			}
			return true;
		} else {
			return false;
		}
	}

	public boolean applyFertilizer(ICropTile aCrop) {
		if (aCrop.getNutrientStorage() >= 100) {
			return false;
		} else {
			//Logger.INFO("Current Nutrient: "+aCrop.getNutrientStorage()+" for "+aCrop.getCrop().displayName());
			aCrop.setNutrientStorage(aCrop.getNutrientStorage() + 100);
			return true;
		}
	}

	public boolean applyHydration(ICropTile aCrop) {
		if (aCrop.getHydrationStorage() >= 200 || this.getFluidAmount() == 0) {
			//Logger.INFO("Hydration Max");
			return false;
		} else {
			int apply = 200 - aCrop.getHydrationStorage();
			if (this.getFluidAmount() >= 0) {
				int drain = 0;
				if (this.getFluidAmount() >= apply) {
					drain = apply;
				}
				else {
					drain = this.getFluidAmount();
				}
				this.mFluid.amount -= drain;
				if (this.mFluid.amount <= 0) {
					this.mFluid = null;
				}
				//Logger.INFO("Did Hydrate");
				aCrop.setHydrationStorage(aCrop.getHydrationStorage() + drain);
				return true;
			}
			else {
				//Logger.INFO("No water?");
				return false;
			}
		}
	}

	public boolean consume(int aSlot, int amount, boolean simulate) {
		ItemStack stack = this.mInventory[aSlot];
		if (stack != null && stack.stackSize >= amount) {
			int currentAmount = Math.min(amount, stack.stackSize);
			amount -= currentAmount;
			if (!simulate) {
				if (stack.stackSize == currentAmount) {
					this.mInventory[aSlot] = null;
				} else {
					stack.stackSize -= currentAmount;
				}
			}
			else {
				return amount >= 0;
			}
			return true;
		}
		return false;
	}

	public ItemStack damage(int aSlot, int amount, boolean simulate) {
		ItemStack ret = null;
		int damageApplied = 0;
		ItemStack stack = this.mInventory[aSlot];
		Item item = stack.getItem();
		if (stack != null && item.isDamageable() && (ret == null
				|| stack.getItem() == ret.getItem() && ItemStack.areItemStackTagsEqual(stack, ret))) {
			if (simulate) {
				stack = stack.copy();
			}
			int maxDamage = DamageHandler.getMaxDamage(stack);
			while (amount > 0 && stack.stackSize > 0) {
				int currentAmount = Math.min(amount, maxDamage - DamageHandler.getDamage(stack));
				DamageHandler.damage(stack, currentAmount, null);
				damageApplied += currentAmount;
				amount -= currentAmount;
				if (DamageHandler.getDamage(stack) >= maxDamage) {
					--stack.stackSize;
					DamageHandler.setDamage(stack, 0);
				}

				if (ret == null) {
					ret = stack.copy();
				}
			}
			if (stack.stackSize == 0 && !simulate) {
				this.mInventory[aSlot] = null;
			}
		}

		if (ret != null) {
			int i = DamageHandler.getMaxDamage(ret);
			ret.stackSize = damageApplied / i;
			DamageHandler.setDamage(ret, damageApplied % i);
		}
		return ret;
	}

	@Override
	public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
		return aStack != null && aIndex >= SLOT_OUTPUT_START && aIndex < this.getSizeInventory();
	}

	@Override
	public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
		if (aStack != null) {
			if (aStack.getItem().getUnlocalizedName().equals("ic2.itemFertilizer")) {
				return aIndex >= SLOT_FERT_1 && aIndex <= SLOT_FERT_4;
			}
			else if (aStack.getItem().getUnlocalizedName().equals("ic2.itemWeedEx")) {
				return aIndex >= SLOT_WEEDEX_1 && aIndex <= SLOT_WEEDEX_2;
			}
		}
		return false;
	}

	@Override
	public String[] getDescription() {
		int aRadius = 10 + getRange(this.mTier);
		int aSide = (aRadius-1)/2;
		return new String[] {
				this.mDescription,
				"Secondary mode can Hydrate/Fertilize/Weed-EX",
				"Consumes "+powerUsage()+"eu per harvest",
				"Consumes "+powerUsageSecondary()+"eu per secondary operation",
				"Can harvest 2 blocks above",
				"Radius: "+aSide+" each side ("+aRadius+"x3x"+aRadius+")",
				"Has "+(this.mTier * 5)+"% chance for extra drops",
				"Holds "+this.getCapacity()+"L of Water",
				CORE.GT_Tooltip
		};
	}

	@Override
	public boolean allowCoverOnSide(byte aSide, GT_ItemStack aStack) {
		return true;
	}

	/*@Override
	public int getTextureIndex(byte aSide, byte aFacing, boolean aActive, boolean aRedstone) {
		if (aSide == aFacing)
			return 118+(aRedstone?8:0);
		if (GT_Utility.getOppositeSide(aSide) == aFacing)
			return 113+(aRedstone?8:0);

		int tIndex = 128+(aRedstone?8:0);

		switch (aFacing) {
		case 0:
			return tIndex+64;
		case 1:
			return tIndex+32;
		case 2: switch (aSide) {
			case 0: return tIndex+32;
			case 1: return tIndex+32;
			case 4: return tIndex+16;
			case 5: return tIndex+48;
			}
		case 3: switch (aSide) {
			case 0: return tIndex+64;
			case 1: return tIndex+64;
			case 4: return tIndex+48;
			case 5: return tIndex+16;
			}
		case 4: switch (aSide) {
			case 0: return tIndex+16;
			case 1: return tIndex+16;
			case 2: return tIndex+48;
			case 3: return tIndex+16;
			}
		case 5: switch (aSide) {
			case 0: return tIndex+48;
			case 1: return tIndex+48;
			case 2: return tIndex+16;
			case 3: return tIndex+48;
			}
		}
		return tIndex;
	}	*/

	@Override
	public ITexture[][][] getTextureSet(final ITexture[] aTextures) {
		final ITexture[][][] rTextures = new ITexture[10][17][];
		for (byte i = -1; i < 16; i++) {
			rTextures[0][i + 1] = this.getFront(i);
			rTextures[1][i + 1] = this.getBack(i);
			rTextures[2][i + 1] = this.getBottom(i);
			rTextures[3][i + 1] = this.getTop(i);
			rTextures[4][i + 1] = this.getSides(i);
			rTextures[5][i + 1] = this.getFront(i);
			rTextures[6][i + 1] = this.getBack(i);
			rTextures[7][i + 1] = this.getBottom(i);
			rTextures[8][i + 1] = this.getTop(i);
			rTextures[9][i + 1] = this.getSides(i);
		}
		return rTextures;
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing,
			final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == 0 || aSide == 1) {
			return this.mTextures[3][aColorIndex + 1];
		}
		else {
			return this.mTextures[4][aColorIndex + 1];
		}
		/*return this.mTextures[(aActive ? 5 : 0) + (aSide == aFacing ? 0
				: aSide == GT_Utility.getOppositeSide(aFacing) ? 1 : aSide == 0 ? 2 : aSide == 1 ? 3 : 4)][aColorIndex
				                                                                                           + 1];*/
	}

	public ITexture[] getFront(final byte aColor) {
		return new ITexture[] {Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1], new GT_RenderedTexture(TexturesGtBlock.Casing_CropHarvester_Cutter)};
	}

	public ITexture[] getBack(final byte aColor) {
		return new ITexture[] {Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1], new GT_RenderedTexture(TexturesGtBlock.Casing_CropHarvester_Cutter)};
	}

	public ITexture[] getBottom(final byte aColor) {
		return new ITexture[] {Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1], new GT_RenderedTexture(TexturesGtBlock.Casing_CropHarvester_Boxes)};
	}

	public ITexture[] getTop(final byte aColor) {
		return new ITexture[] {Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1], new GT_RenderedTexture(TexturesGtBlock.Casing_CropHarvester_Boxes)};
	}

	public ITexture[] getSides(final byte aColor) {
		return new ITexture[] {Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1], new GT_RenderedTexture(TexturesGtBlock.Casing_CropHarvester_Cutter)};
	}

	@Override
	public boolean doesFillContainers() {
		return false;
	}

	@Override
	public boolean doesEmptyContainers() {
		return false;
	}

	@Override
	public boolean canTankBeFilled() {
		return true;
	}

	@Override
	public boolean canTankBeEmptied() {
		return false;
	}

	@Override
	public boolean displaysItemStack() {
		return false;
	}

	@Override
	public boolean displaysStackSize() {
		return false;
	}

	@Override
	public void saveNBTData(NBTTagCompound aNBT) {
		super.saveNBTData(aNBT);
		aNBT.setBoolean("mModeAlternative", this.mModeAlternative);
	}

	@Override
	public void loadNBTData(NBTTagCompound aNBT) {
		super.loadNBTData(aNBT);
		this.mModeAlternative = aNBT.getBoolean("mModeAlternative");
	}

}
