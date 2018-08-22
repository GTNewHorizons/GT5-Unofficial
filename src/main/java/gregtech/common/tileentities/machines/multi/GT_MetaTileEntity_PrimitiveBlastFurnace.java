package gregtech.common.tileentities.machines.multi;

import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.objects.XSTR;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_Pollution;
import gregtech.common.gui.GT_Container_PrimitiveBlastFurnace;
import gregtech.common.gui.GT_GUIContainer_PrimitiveBlastFurnace;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.ChunkPosition;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class GT_MetaTileEntity_PrimitiveBlastFurnace extends MetaTileEntity {
	public static final int INPUT_SLOTS = 3, OUTPUT_SLOTS = 3;

	public int mMaxProgresstime = 0;
	public int mUpdate = 5;
	public int mProgresstime = 0;
	public boolean mMachine = false;

	public ItemStack[] mOutputItems = new ItemStack[OUTPUT_SLOTS];

	@Deprecated
	public ItemStack mOutputItem1;
	@Deprecated
	public ItemStack mOutputItem2;

	public GT_MetaTileEntity_PrimitiveBlastFurnace(int aID, String aName, String aNameRegional) {
		super(aID, aName, aNameRegional, INPUT_SLOTS + OUTPUT_SLOTS);
	}

	public GT_MetaTileEntity_PrimitiveBlastFurnace(String aName) {
		super(aName, INPUT_SLOTS + OUTPUT_SLOTS);
	}

	public boolean isSteampowered() {
		return false;
	}

	public boolean isElectric() {
		return false;
	}

	public boolean isPneumatic() {
		return false;
	}

	public boolean isEnetInput() {
		return false;
	}

	public boolean isEnetOutput() {
		return false;
	}

	public boolean isInputFacing(byte aSide) {
		return false;
	}

	public boolean isOutputFacing(byte aSide) {
		return false;
	}

	public boolean isTeleporterCompatible() {
		return false;
	}

	public boolean isFacingValid(byte aFacing) {
		return aFacing > 1;
	}

	public boolean isAccessAllowed(EntityPlayer aPlayer) {
		return true;
	}

	public int getProgresstime() {
		return this.mProgresstime;
	}

	public int maxProgresstime() {
		return this.mMaxProgresstime;
	}

	public int increaseProgress(int aProgress) {
		this.mProgresstime += aProgress;
		return this.mMaxProgresstime - this.mProgresstime;
	}

	public boolean allowCoverOnSide(byte aSide, GT_ItemStack aCoverID) {
		return (GregTech_API.getCoverBehavior(aCoverID.toStack()).isSimpleCover()) && (super.allowCoverOnSide(aSide, aCoverID));
	}

	public abstract MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity);

	public void saveNBTData(NBTTagCompound aNBT) {
		aNBT.setInteger("mProgresstime", this.mProgresstime);
		aNBT.setInteger("mMaxProgresstime", this.mMaxProgresstime);
		if (this.mOutputItems != null) {
			for (int i = 0; i < mOutputItems.length; i++) {
				if (this.mOutputItems[i] != null) {
					NBTTagCompound tNBT = new NBTTagCompound();
					this.mOutputItems[i].writeToNBT(tNBT);
					aNBT.setTag("mOutputItem" + i, tNBT);
				}
			}
		}
	}

	public void loadNBTData(NBTTagCompound aNBT) {
		this.mUpdate = 5;
		this.mProgresstime = aNBT.getInteger("mProgresstime");
		this.mMaxProgresstime = aNBT.getInteger("mMaxProgresstime");
		this.mOutputItems = new ItemStack[OUTPUT_SLOTS];
		for (int i = 0; i < OUTPUT_SLOTS; i++) {
			this.mOutputItems[i] = GT_Utility.loadItem(aNBT, "mOutputItem" + i);
		}
	}

	public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
		if (aBaseMetaTileEntity.isClientSide()) {
			return true;
		}
		aBaseMetaTileEntity.openGUI(aPlayer);
		return true;
	}

	public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return new GT_Container_PrimitiveBlastFurnace(aPlayerInventory, aBaseMetaTileEntity);
	}

	public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return new GT_GUIContainer_PrimitiveBlastFurnace(aPlayerInventory, aBaseMetaTileEntity, getName(), GT_Recipe.GT_Recipe_Map.sPrimitiveBlastRecipes.mNEIName);
	}

	private boolean checkMachine() {
		int xDir = ForgeDirection.getOrientation(getBaseMetaTileEntity().getBackFacing()).offsetX;
		int zDir = ForgeDirection.getOrientation(getBaseMetaTileEntity().getBackFacing()).offsetZ;
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 3; j++) {
				for (int k = -1; k < 2; k++) {
					if ((xDir + i != 0) || (j != 0) || (zDir + k != 0)) {
						if ((i != 0) || (j == -1) || (k != 0)) {
							if (!isCorrectCasingBlock(getBaseMetaTileEntity().getBlockOffset(xDir + i, j, zDir + k))
									|| !isCorrectCasingMetaID(getBaseMetaTileEntity().getMetaIDOffset(xDir + i, j, zDir + k))) {
								return false;
							}
						} else if ((!GT_Utility.arrayContains(getBaseMetaTileEntity().getBlockOffset(xDir + i, j, zDir + k),
								new Object[] { Blocks.lava, Blocks.flowing_lava, null }))
								&& (!getBaseMetaTileEntity().getAirOffset(xDir + i, j, zDir + k))) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	protected abstract boolean isCorrectCasingBlock(Block block);

	protected abstract boolean isCorrectCasingMetaID(int metaID);

	public void onMachineBlockUpdate() {
		this.mUpdate = 5;
	}

	public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {
		if ((aBaseMetaTileEntity.isClientSide()) && (aBaseMetaTileEntity.isActive())) {
			aBaseMetaTileEntity.getWorld().spawnParticle("largesmoke",
					aBaseMetaTileEntity.getOffsetX(aBaseMetaTileEntity.getBackFacing(), 1) + (new XSTR()).nextFloat(),
					aBaseMetaTileEntity.getOffsetY(aBaseMetaTileEntity.getBackFacing(), 1),
					aBaseMetaTileEntity.getOffsetZ(aBaseMetaTileEntity.getBackFacing(), 1) + (new XSTR()).nextFloat(), 0.0D, 0.3D, 0.0D);
		}
		if (aBaseMetaTileEntity.isServerSide()) {
			if (this.mUpdate-- == 0) {
				this.mMachine = checkMachine();
			}
			if (this.mMachine) {
				if (this.mMaxProgresstime > 0) {
					if (++this.mProgresstime >= this.mMaxProgresstime) {
						addOutputProducts();
						this.mOutputItems = null;
						this.mProgresstime = 0;
						this.mMaxProgresstime = 0;
						GT_Mod.achievements.issueAchievement(
								aBaseMetaTileEntity.getWorld().getPlayerEntityByName(aBaseMetaTileEntity.getOwnerName()), "steel");
					}
				} else if (aBaseMetaTileEntity.isAllowedToWork()) {
					checkRecipe();
				}
			}
			if (this.mMaxProgresstime > 0 && (aTimer % 20L == 0L)) {
				GT_Pollution.addPollution(this.getBaseMetaTileEntity().getWorld(),
						new ChunkPosition(this.getBaseMetaTileEntity().getXCoord(), this.getBaseMetaTileEntity().getYCoord(),
								this.getBaseMetaTileEntity().getZCoord()),
						50);
			}

			aBaseMetaTileEntity.setActive((this.mMaxProgresstime > 0) && (this.mMachine));
			if (aBaseMetaTileEntity.isActive()) {
				if (aBaseMetaTileEntity.getAir(aBaseMetaTileEntity.getOffsetX(aBaseMetaTileEntity.getBackFacing(), 1),
						aBaseMetaTileEntity.getYCoord(), aBaseMetaTileEntity.getOffsetZ(aBaseMetaTileEntity.getBackFacing(), 1))) {
					aBaseMetaTileEntity.getWorld().setBlock(aBaseMetaTileEntity.getOffsetX(aBaseMetaTileEntity.getBackFacing(), 1),
							aBaseMetaTileEntity.getYCoord(), aBaseMetaTileEntity.getOffsetZ(aBaseMetaTileEntity.getBackFacing(), 1),
							Blocks.lava, 1, 2);
					this.mUpdate = 1;
				}
				if (aBaseMetaTileEntity.getAir(aBaseMetaTileEntity.getOffsetX(aBaseMetaTileEntity.getBackFacing(), 1),
						aBaseMetaTileEntity.getYCoord() + 1, aBaseMetaTileEntity.getOffsetZ(aBaseMetaTileEntity.getBackFacing(), 1))) {
					aBaseMetaTileEntity.getWorld().setBlock(aBaseMetaTileEntity.getOffsetX(aBaseMetaTileEntity.getBackFacing(), 1),
							aBaseMetaTileEntity.getYCoord() + 1, aBaseMetaTileEntity.getOffsetZ(aBaseMetaTileEntity.getBackFacing(), 1),
							Blocks.lava, 1, 2);
					this.mUpdate = 1;
				}
			} else {
				if (aBaseMetaTileEntity.getBlock(aBaseMetaTileEntity.getOffsetX(aBaseMetaTileEntity.getBackFacing(), 1),
						aBaseMetaTileEntity.getYCoord(),
						aBaseMetaTileEntity.getOffsetZ(aBaseMetaTileEntity.getBackFacing(), 1)) == Blocks.lava) {
					aBaseMetaTileEntity.getWorld().setBlock(aBaseMetaTileEntity.getOffsetX(aBaseMetaTileEntity.getBackFacing(), 1),
							aBaseMetaTileEntity.getYCoord(), aBaseMetaTileEntity.getOffsetZ(aBaseMetaTileEntity.getBackFacing(), 1),
							Blocks.air, 0, 2);
					this.mUpdate = 1;
				}
				if (aBaseMetaTileEntity.getBlock(aBaseMetaTileEntity.getOffsetX(aBaseMetaTileEntity.getBackFacing(), 1),
						aBaseMetaTileEntity.getYCoord() + 1,
						aBaseMetaTileEntity.getOffsetZ(aBaseMetaTileEntity.getBackFacing(), 1)) == Blocks.lava) {
					aBaseMetaTileEntity.getWorld().setBlock(aBaseMetaTileEntity.getOffsetX(aBaseMetaTileEntity.getBackFacing(), 1),
							aBaseMetaTileEntity.getYCoord() + 1, aBaseMetaTileEntity.getOffsetZ(aBaseMetaTileEntity.getBackFacing(), 1),
							Blocks.air, 0, 2);
					this.mUpdate = 1;
				}
			}
		}
	}

	private void addOutputProducts() {
		if (this.mOutputItems == null) {
			return;
		}
		int limit = Math.min(mOutputItems.length, OUTPUT_SLOTS);
		for (int i = 0; i < limit; i++) {
			int absi = INPUT_SLOTS + i;
			if (this.mInventory[absi] == null) {
				this.mInventory[absi] = GT_Utility.copy(this.mOutputItems[i]);
			} else if (GT_Utility.areStacksEqual(this.mInventory[absi], this.mOutputItems[i])) {
				this.mInventory[absi].stackSize = Math.min(this.mInventory[absi].getMaxStackSize(),
						this.mInventory[absi].stackSize + this.mOutputItems[i].stackSize);
			}
		}
	}

	private boolean spaceForOutput(ItemStack outputStack, int relativeOutputSlot) {
		int absoluteSlot = relativeOutputSlot + INPUT_SLOTS;
		if (this.mInventory[absoluteSlot] == null || outputStack == null) {
			return true;
		}
		if (((this.mInventory[absoluteSlot].stackSize + outputStack.stackSize <= this.mInventory[absoluteSlot].getMaxStackSize())
				&& (GT_Utility.areStacksEqual(this.mInventory[absoluteSlot], outputStack)))) {
			return true;
		}
		return false;
	}

	private boolean checkRecipe() {
		if (!this.mMachine) {
			return false;
		}
		ItemStack[] inputs = new ItemStack[INPUT_SLOTS];
		System.arraycopy(mInventory, 0, inputs, 0, INPUT_SLOTS);
		GT_Recipe recipe = GT_Recipe.GT_Recipe_Map.sPrimitiveBlastRecipes.findRecipe(getBaseMetaTileEntity(), false, 0, null, inputs);
		if (recipe == null) {
			this.mOutputItems = null;
			return false;
		}
		for (int i = 0; i < OUTPUT_SLOTS; i++) {
			if (!spaceForOutput(recipe.getOutput(i), i)) {
				this.mOutputItems = null;
				return false;
			}
		}

		if (!recipe.isRecipeInputEqual(true, null, inputs)) {
			this.mOutputItems = null;
			return false;
		}
		for (int i = 0; i < INPUT_SLOTS; i++) {
			if (mInventory[i] != null && mInventory[i].stackSize == 0) {
				mInventory[i] = null;
			}
		}
		
		this.mMaxProgresstime = recipe.mDuration;
		this.mOutputItems = recipe.mOutputs;
		return true;
	}

	public boolean isGivingInformation() {
		return false;
	}

	public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
		return aIndex > INPUT_SLOTS;
	}

	public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
		return !GT_Utility.areStacksEqual(aStack, this.mInventory[0]);
	}

	public byte getTileEntityBaseType() {
		return 0;
	}
	
	public abstract String getName();
}