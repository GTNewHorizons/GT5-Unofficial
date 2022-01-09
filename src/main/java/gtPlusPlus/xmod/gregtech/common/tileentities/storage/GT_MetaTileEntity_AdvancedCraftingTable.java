package gtPlusPlus.xmod.gregtech.common.tileentities.storage;

import java.util.ArrayList;

import gregtech.api.enums.GT_Values;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicTank;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.item.general.ItemBlueprint;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.xmod.gregtech.api.gui.workbench.GT_Container_AdvancedWorkbench;
import gtPlusPlus.xmod.gregtech.api.gui.workbench.GT_GUIContainer_AdvancedWorkbench;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class GT_MetaTileEntity_AdvancedCraftingTable extends GT_MetaTileEntity_BasicTank {

	public boolean mFlushMode = false;
	
	protected String mLocalName;

	public GT_MetaTileEntity_AdvancedCraftingTable(final int aID, final String aName, final String aNameRegional, final int aTier, final String aDescription) {
		super(aID, aName, aNameRegional, aTier, 35, aDescription);
		mLocalName = aNameRegional;
	}

	public GT_MetaTileEntity_AdvancedCraftingTable(final String aName, final int aTier, final String aDescription, final ITexture[][][] aTextures) {
		super(aName, aTier, 35, aDescription, aTextures);
	}

	@Override
	public Object getServerGUI(final int aID, final InventoryPlayer aPlayerInventory, final IGregTechTileEntity aBaseMetaTileEntity) {
		return new GT_Container_AdvancedWorkbench(aPlayerInventory, aBaseMetaTileEntity);
	}

	@Override
	public Object getClientGUI(final int aID, final InventoryPlayer aPlayerInventory, final IGregTechTileEntity aBaseMetaTileEntity) {
		return new GT_GUIContainer_AdvancedWorkbench(aPlayerInventory, aBaseMetaTileEntity, mLocalName);
	}
	
	@Override
	public boolean isTransformerUpgradable() {
		return true;
	}
	@Override
	public boolean isSimpleMachine() {
		return true;
	}
	@Override
	public boolean isValidSlot(int aIndex) {
		return aIndex < 31 || aIndex > 32;
	}
	@Override
	public boolean isFacingValid(byte aFacing) {
		return true;
	}
	@Override
	public boolean isAccessAllowed(EntityPlayer aPlayer) {
		return true;
	}
	@Override
	public boolean isEnetInput() {
		return isElectric();
	}
	@Override
	public boolean isInputFacing(byte aSide) {
		return true;
	}
	@Override
	public long maxEUInput() {
		return isElectric() ? GT_Values.V[3] : 0;
	}
	@Override
	public long maxEUStore() {
		return isElectric() ? GT_Values.V[3] * 1024 : 0;
	}
	
	@Override
	public boolean isElectric() {
		return isAdvanced();
	}
	
	@Override
	public boolean isPneumatic() {
		return false;
	}
	
	@Override
	public boolean isSteampowered() {
		return false;
	}

	@Override
	public long maxAmperesIn() {
		return isElectric() ? 2 : 0;
	}

	@Override
	public long getMinimumStoredEU() {
		return isElectric() ? GT_Values.V[3] * 2 : 0;
	}

	@Override
	public int getSizeInventory() {
		return 35;
	}

	@Override
	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GT_MetaTileEntity_AdvancedCraftingTable(this.mName, this.mTier, this.mDescription, this.mTextures);
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
		return true;
	}
	@Override
	public boolean displaysItemStack() {
		return false;
	}
	@Override
	public boolean displaysStackSize() {
		return false;
	}

	public void sortIntoTheInputSlots() {
		for (byte i = 21; i < 30; i++)
			if (mInventory[i] != null) {
				if (mInventory[i].stackSize == 0) {
					mInventory[i] = null;
				}
				if (mInventory[i] != null)
					for (byte j = 0; j < 16; j++) {
						if (GT_Utility.areStacksEqual(
								mInventory[i], mInventory[j]
								)) {
							GT_Utility.moveStackFromSlotAToSlotB(
									getBaseMetaTileEntity(), getBaseMetaTileEntity(), i, j, (byte) 64, (byte) 1, (byte) 64, (byte) 1
									);
						}
					}
				if (mInventory[i] != null)
					for (byte j = 0; j < 16; j++) {
						if (mInventory[j] == null) {
							GT_Utility.moveStackFromSlotAToSlotB(
									getBaseMetaTileEntity(), getBaseMetaTileEntity(), i, j, (byte) 64, (byte) 1, (byte) 64, (byte) 1
									);
						}
					}
			}
	}

	private void fillLiquidContainers() {
		for (byte i = 16; i < 21 && mFluid != null; i++) {
			ItemStack tOutput = GT_Utility.fillFluidContainer(mFluid, mInventory[i], false, true);
			if (tOutput != null) {
				if (mInventory[i].stackSize == 1) {
					mFluid.amount -= GT_Utility.getFluidForFilledItem(
							tOutput, true
							).amount * tOutput.stackSize;
					mInventory[i] = tOutput;
				}
				else
					for (byte j = 16; j < 21; j++) {
						if (mInventory[j] == null || (GT_Utility.areStacksEqual(
								tOutput, mInventory[j]
								) && mInventory[j].stackSize
								+ tOutput.stackSize <= tOutput.getMaxStackSize())) {
							mFluid.amount -= GT_Utility.getFluidForFilledItem(
									tOutput, true
									).amount * tOutput.stackSize;
							getBaseMetaTileEntity().decrStackSize(i, 1);
							if (mInventory[j] == null) {
								mInventory[j] = tOutput;
							}
							else {
								mInventory[j].stackSize++;
							}
							break;
						}
					}
				if (mFluid != null && mFluid.amount <= 0)
					mFluid = null;
			}
		}
		if (mFluid != null && mFluid.amount <= 0)
			mFluid = null;
	}

	public void setBluePrint(ItemStack aStack) {
		if (aStack == null) {
			aStack = mInventory[30];
			Logger.INFO("Using Slot 30 supply.");
		}
		if (mInventory[31] == null || aStack == null || aStack.getItem() == null
				|| aStack.getItemDamage() != 0 || aStack.stackSize != 1
				|| !(aStack.getItem() instanceof ItemBlueprint)) {
			try {
			Logger.INFO(
					"Could not set Blueprint. Slot 31: "
							+ (mInventory[31] != null ? mInventory[31].getDisplayName() : "Null") 
							+ ", aStack: "+(aStack != null ? aStack.getDisplayName() : "Null") 
							+ ", Damage: "+(aStack != null ? aStack.getItemDamage() : "Null"));
			}
			catch (Throwable t) {
				t.printStackTrace();
			}
			return;
		}
		if (!aStack.getTagCompound().hasKey("Inventory")) {
			NBTTagCompound tNBT = new NBTTagCompound();
			NBTTagList tNBT_ItemList = new NBTTagList();
			for (int i = 0; i < 9; i++) {
				ItemStack tStack = mInventory[i + 21];
				if (tStack != null) {
					NBTTagCompound tag = new NBTTagCompound();
					tag.setByte("Slot", (byte) i);
					tStack.writeToNBT(tag);
					tNBT_ItemList.appendTag(tag);
				}
			}
			tNBT.setTag("Inventory", tNBT_ItemList);
			tNBT.setBoolean("mBlueprint", true);
			tNBT.setInteger("mID", MathUtils.randInt(1, Short.MAX_VALUE));
			tNBT.setString("mName", mInventory[31].getDisplayName());
			aStack.setTagCompound(tNBT);
			Logger.INFO("Set NBT of crafting table to Stack in slot 30.");
		}
		else {
			Logger.INFO("Blueprint already has recipe tags.");
		}
		
	}

	public ItemStack getCraftingOutput() {
		if (mInventory[30] != null && mInventory[30].getItem() != null
				&& mInventory[30].getItemDamage() == 0
				&& mInventory[30].hasTagCompound() && mInventory[30].getItem() instanceof ItemBlueprint) {
			//Logger.INFO("Getting Blueprint Data in slot 30. "+mInventory[30].getDisplayName());
			NBTTagCompound tNBT = mInventory[30].getTagCompound();
			NBTTagList tNBT_ItemList = tNBT.getTagList("Blueprint", 10);
			for (int i = 0; i < tNBT_ItemList.tagCount() && i < 9; i++) {
				NBTTagCompound tag = (NBTTagCompound) tNBT_ItemList.getCompoundTagAt(
						i
						);
				byte slot = tag.getByte("Slot");
				if (slot >= 0 && slot < 9 && mInventory[slot + 21] == null) {
					mInventory[slot + 21] = GT_Utility.loadItem(tag);
					if (mInventory[slot + 21] != null)
						mInventory[slot + 21].stackSize = 0;
				}
			}
		}
		mInventory[31] = GT_ModHandler.getAllRecipeOutput(
				getBaseMetaTileEntity().getWorld(), new ItemStack[]{
						mInventory[21], mInventory[22], mInventory[23],
						mInventory[24], mInventory[25], mInventory[26],
						mInventory[27], mInventory[28], mInventory[29]
				}
				);
		return mInventory[31];
	}

	public boolean canDoCraftingOutput() {
		if (mInventory[31] == null)
			return false;
		for (ItemStack tStack : recipeContent()) {
			if (tStack.stackSize > getAmountOf(tStack)) {
				return false;
			}
		}
		return true;
	}

	private int getAmountOf(ItemStack aStack) {
		int tAmount = 0;
		for (byte i = 0; i < 30 && tAmount < 9; i++) {
			if (GT_Utility.areStacksOrToolsEqual(aStack, mInventory[i])) {
				tAmount += mInventory[i].stackSize;
			}
		}
		return tAmount;
	}

	private ArrayList<ItemStack> recipeContent() {
		ArrayList<ItemStack> tList = new ArrayList<ItemStack>();
		for (byte i = 21; i < 30; i++) {
			if (mInventory[i] != null) {
				boolean temp = false;
				for (byte j = 0; j < tList.size(); j++) {
					if (GT_Utility.areStacksOrToolsEqual(
							mInventory[i], tList.get(j)
							)) {
						tList.get(j).stackSize++;
						temp = true;
						break;
					}
				}
				if (!temp)
					tList.add(GT_Utility.copy(1, mInventory[i]));
			}
		}
		return tList;
	}

	public ItemStack consumeMaterials(EntityPlayer aPlayer, ItemStack aHoldStack) {
		if (mInventory[31] == null)
			return aHoldStack;
		if (aHoldStack != null) {
			if (!GT_Utility.areStacksEqual(aHoldStack, mInventory[31]))
				return aHoldStack;
			if (aHoldStack.stackSize
					+ mInventory[31].stackSize > aHoldStack.getMaxStackSize())
				return aHoldStack;
		}
		for (byte i = 21; i < 30; i++)
			if (mInventory[i] != null) {
				for (byte j = 0; j <= i; j++) {
					if (j < 21 || j == i) {
						if (GT_Utility.areStacksOrToolsEqual(
								mInventory[i], mInventory[j]
								) && mInventory[j].stackSize > 0) {
							ItemStack tStack = GT_Utility.getContainerItem(
									mInventory[j], true
									);
							if (tStack == null
									|| (tStack.isItemStackDamageable()
											&& tStack.getItemDamage() >= tStack.getMaxDamage())) {
								getBaseMetaTileEntity().decrStackSize(j, 1);
							}
							else if (mInventory[j].stackSize == 1) {
								mInventory[j] = tStack;
							}
							else {
								getBaseMetaTileEntity().decrStackSize(j, 1);
								for (byte k = 0; k < 21; k++) {
									if (mInventory[k] == null) {
										mInventory[k] = tStack;
										break;
									}
									else {
										if (GT_Utility.areStacksEqual(
												tStack, mInventory[k]
												)) {
											if (tStack.stackSize
													+ mInventory[k].stackSize <= mInventory[k].getMaxStackSize()) {
												mInventory[k].stackSize += tStack.stackSize;
												break;
											}
										}
									}
								}
							}
							break;
						}
					}
				}
			}
		if (aHoldStack == null) {
			aHoldStack = GT_Utility.copy(mInventory[31]);
			aHoldStack.onCrafting(
					getBaseMetaTileEntity().getWorld(), aPlayer, mInventory[31].stackSize
					);
		}
		else {
			aHoldStack.stackSize += mInventory[31].stackSize;
			aHoldStack.onCrafting(
					getBaseMetaTileEntity().getWorld(), aPlayer, mInventory[31].stackSize
					);
		}

		fillLiquidContainers();

		return aHoldStack;
	}

	@Override
	public int rechargerSlotStartIndex() {
		return 16;
	}

	@Override
	public int rechargerSlotCount() {
		return 5;
	}

	@Override
	public long getOutputTier() {
		return GT_Utility.getTier(getBaseMetaTileEntity().getInputVoltage());
	}

	@Override
	public int getCapacity() {
		return 64000;
	}

	@Override
	public int getTankPressure() {
		return -100;
	}

	@Override
	public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
		return aIndex == 33 || (mFlushMode && aIndex >= 21 && aIndex < 30);
	}

	@Override
	public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
		if (aIndex < 16) {
			for (byte i = 0; i < 16; i++)
				if (GT_Utility.areStacksOrToolsEqual(aStack, mInventory[i]))
					return aIndex == i;
			return true;
		}
		return false;
	}

	@Override
	public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
		super.onPreTick(aBaseMetaTileEntity, aTick);
		getCraftingOutput();
	}

	@Override
	public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
		super.onPostTick(aBaseMetaTileEntity, aTick);
		if (getBaseMetaTileEntity().isServerSide()) {
			if (getBaseMetaTileEntity().hasInventoryBeenModified())
				getCraftingOutput();
			fillLiquidContainers();
			if (mFlushMode) {
				mFlushMode = false;
				for (byte i = 21; i < 30; i++) {
					if (mInventory[i] != null) {
						if (mInventory[i].stackSize == 0) {
							mInventory[i] = null;
						}
						else {
							mFlushMode = true;
							break;
						}
					}
				}
			}
		}		
		/*if (aTick % 100 == 0) {
			for (int i = 0; i < this.mInventory.length; i++) {
				ItemStack aSlot = mInventory[i];
				Logger.INFO("Slot "+i+" "+(aSlot != null ? "contains "+aSlot.getDisplayName() : "is empty"));
			}
		}*/		
	}

	@Override
	public String[] getDescription() {
		return new String[] { 
				isAdvanced() ? "For the very large Projects" : "For the smaller Projects",
				"Hold Shift in GUI to see slot usage",
				this.mDescription, 
				CORE.GT_Tooltip };
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
			rTextures[5][i + 1] = this.getFront(i);
			rTextures[6][i + 1] = this.getBack(i);
			rTextures[7][i + 1] = this.getBottom(i);
			rTextures[8][i + 1] = this.getTop(i);
			rTextures[9][i + 1] = this.getSides(i);
		}
		return rTextures;
	}

	protected boolean isAdvanced() {
		return true;
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing,
			final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		return this.mTextures[(aActive ? 5 : 0) + (aSide == aFacing ? 0
				: aSide == GT_Utility.getOppositeSide(aFacing) ? 1 : aSide == 0 ? 2 : aSide == 1 ? 3 : 4)][aColorIndex
				                                                                                           + 1];
	}

	public ITexture[] getFront(final byte aColor) {
		return new ITexture[] { isAdvanced() ? new GT_RenderedTexture(TexturesGtBlock.Casing_Adv_Workbench_Side) : new GT_RenderedTexture(TexturesGtBlock.Casing_Workbench_Side)};
	}

	public ITexture[] getBack(final byte aColor) {
		return new ITexture[] { isAdvanced() ? new GT_RenderedTexture(TexturesGtBlock.Casing_Adv_Workbench_Side) : new GT_RenderedTexture(TexturesGtBlock.Casing_Workbench_Side)};
	}

	public ITexture[] getBottom(final byte aColor) {
		return new ITexture[] { isAdvanced() ? new GT_RenderedTexture(TexturesGtBlock.Casing_Adv_Workbench_Bottom) : new GT_RenderedTexture(TexturesGtBlock.Casing_Workbench_Bottom)};
	}

	public ITexture[] getTop(final byte aColor) {		
		if (isAdvanced()) {
			return new ITexture[] { new GT_RenderedTexture(TexturesGtBlock.Casing_Adv_Workbench_Top),
					new GT_RenderedTexture(TexturesGtBlock.Casing_Adv_Workbench_Crafting_Overlay) };
		}
		else {
			return new ITexture[] { new GT_RenderedTexture(TexturesGtBlock.Casing_Workbench_Top),
					new GT_RenderedTexture(TexturesGtBlock.Casing_Workbench_Crafting_Overlay) };
		}
	}

	public ITexture[] getSides(final byte aColor) {
		return new ITexture[] { isAdvanced() ? new GT_RenderedTexture(TexturesGtBlock.Casing_Adv_Workbench_Side) : new GT_RenderedTexture(TexturesGtBlock.Casing_Workbench_Side)};
	}

}
