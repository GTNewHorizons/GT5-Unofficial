package gtPlusPlus.xmod.gregtech.common.tileentities.storage;

import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_TieredMachineBlock;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Utility;
import gregtech.common.gui.GT_Container_QuantumChest;
import gregtech.common.gui.GT_GUIContainer_QuantumChest;
import gtPlusPlus.xmod.gregtech.api.gui.CONTAINER_SuperChest;
import gtPlusPlus.xmod.gregtech.api.gui.GUI_SuperChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class GT_MetaTileEntity_TieredChest extends GT_MetaTileEntity_TieredMachineBlock {
	public int mItemCount = 0;
	public ItemStack mItemStack = null;
	private final static double mStorageFactor = (270000.0D/16);

	public GT_MetaTileEntity_TieredChest(int aID, String aName, String aNameRegional, int aTier) {
		super(aID, aName, aNameRegional, aTier, 3,
				"This Chest stores " + (int) (Math.pow(6.0D, (double) aTier) * mStorageFactor) + " Items", new ITexture[0]);
	}

	public GT_MetaTileEntity_TieredChest(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
		super(aName, aTier, 3, aDescription, aTextures);
	}

	public GT_MetaTileEntity_TieredChest(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
		super(aName, aTier, 3, aDescription, aTextures);
	}

	public boolean isSimpleMachine() {
		return true;
	}

	public boolean isFacingValid(byte aFacing) {
		return true;
	}

	public boolean isAccessAllowed(EntityPlayer aPlayer) {
		return true;
	}

	public boolean isValidSlot(int aIndex) {
		return true;
	}

	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GT_MetaTileEntity_TieredChest(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
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
		return new CONTAINER_SuperChest(aPlayerInventory, aBaseMetaTileEntity);
	}

	public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return new GUI_SuperChest(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName());
	}

	public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {
		if (this.getBaseMetaTileEntity().isServerSide() && this.getBaseMetaTileEntity().isAllowedToWork()) {
			if (this.getItemCount() <= 0) {
				this.mItemStack = null;
				this.mItemCount = 0;
			}

			if (this.mItemStack == null && this.mInventory[0] != null) {
				this.mItemStack = this.mInventory[0].copy();
			}

			if (this.mInventory[0] != null && this.mItemCount < this.getMaxItemCount()
					&& GT_Utility.areStacksEqual(this.mInventory[0], this.mItemStack)) {
				this.mItemCount += this.mInventory[0].stackSize;
				if (this.mItemCount > this.getMaxItemCount()) {
					this.mInventory[0].stackSize = this.mItemCount - this.getMaxItemCount();
					this.mItemCount = this.getMaxItemCount();
				} else {
					this.mInventory[0] = null;
				}
			}

			if (this.mInventory[1] == null && this.mItemStack != null) {
				this.mInventory[1] = this.mItemStack.copy();
				this.mInventory[1].stackSize = Math.min(this.mItemStack.getMaxStackSize(), this.mItemCount);
				this.mItemCount -= this.mInventory[1].stackSize;
			} else if (this.mItemCount > 0 && GT_Utility.areStacksEqual(this.mInventory[1], this.mItemStack)
					&& this.mInventory[1].getMaxStackSize() > this.mInventory[1].stackSize) {
				int tmp = Math.min(this.mItemCount,
						this.mInventory[1].getMaxStackSize() - this.mInventory[1].stackSize);
				this.mInventory[1].stackSize += tmp;
				this.mItemCount -= tmp;
			}

			if (this.mItemStack != null) {
				this.mInventory[2] = this.mItemStack.copy();
				this.mInventory[2].stackSize = Math.min(this.mItemStack.getMaxStackSize(), this.mItemCount);
			} else {
				this.mInventory[2] = null;
			}
		}

	}

	private int getItemCount() {
		return this.mItemCount;
	}

	public void setItemCount(int aCount) {
		this.mItemCount = aCount;
	}

	public int getProgresstime() {
		return this.mItemCount + (this.mInventory[0] == null ? 0 : this.mInventory[0].stackSize)
				+ (this.mInventory[1] == null ? 0 : this.mInventory[1].stackSize);
	}

	public int maxProgresstime() {
		return this.getMaxItemCount();
	}

	public int getMaxItemCount() {
		return (int) (Math.pow(6.0D, (double) this.mTier) * mStorageFactor - 128.0D);
	}

	public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
		return aIndex == 1;
	}

	public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
		return aIndex == 0 && (this.mInventory[0] == null || GT_Utility.areStacksEqual(this.mInventory[0], aStack));
	}

	public String[] getInfoData() {
		return this.mItemStack == null
				? new String[]{"Super Storage Chest", "Stored Items:", "No Items", Integer.toString(0),
						Integer.toString(this.getMaxItemCount())}
				: new String[]{"Super Storage Chest", "Stored Items:", this.mItemStack.getDisplayName(),
						Integer.toString(this.mItemCount), Integer.toString(this.getMaxItemCount())};
	}

	public boolean isGivingInformation() {
		return true;
	}

	public void saveNBTData(NBTTagCompound aNBT) {
		aNBT.setInteger("mItemCount", this.mItemCount);
		if (this.mItemStack != null) {
			aNBT.setTag("mItemStack", this.mItemStack.writeToNBT(new NBTTagCompound()));
		}

	}

	public void loadNBTData(NBTTagCompound aNBT) {
		if (aNBT.hasKey("mItemCount")) {
			this.mItemCount = aNBT.getInteger("mItemCount");
		}

		if (aNBT.hasKey("mItemStack")) {
			this.mItemStack = ItemStack.loadItemStackFromNBT((NBTTagCompound) aNBT.getTag("mItemStack"));
		}

	}

	public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex,
			boolean aActive, boolean aRedstone) {
		return aBaseMetaTileEntity.getFrontFacing() == 0 && aSide == 4
				? new ITexture[]{BlockIcons.MACHINE_CASINGS[this.mTier][aColorIndex + 1],
						new GT_RenderedTexture(BlockIcons.OVERLAY_QCHEST)}
				: (aSide == aBaseMetaTileEntity.getFrontFacing()
						? new ITexture[]{BlockIcons.MACHINE_CASINGS[this.mTier][aColorIndex + 1],
								new GT_RenderedTexture(BlockIcons.OVERLAY_QCHEST)}
						: new ITexture[]{BlockIcons.MACHINE_CASINGS[this.mTier][aColorIndex + 1]});
	}

	public ITexture[][][] getTextureSet(ITexture[] aTextures) {
		return new ITexture[0][0][0];
	}
}