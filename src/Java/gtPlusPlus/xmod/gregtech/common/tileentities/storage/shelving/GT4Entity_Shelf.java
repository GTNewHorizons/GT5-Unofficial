package gtPlusPlus.xmod.gregtech.common.tileentities.storage.shelving;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import gregtech.api.enums.*;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicHull_NonElectric;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Utility;

import gtPlusPlus.core.lib.CORE;

public class GT4Entity_Shelf extends GT_MetaTileEntity_BasicHull_NonElectric {
	public byte mType = 0;

	public GT4Entity_Shelf(final int aID, final String aName, final String aNameRegional, final String aDescription) {
		super(aID, aName, aNameRegional, 0, aDescription);
	}

	public GT4Entity_Shelf(final String aName, final String aDescription, final ITexture[][][] aTextures) {
		super(aName, 0, aDescription, aTextures);
	}

	@Override
	public boolean isSimpleMachine() {
		return true;
	}

	public int getInvSize() {
		return 1;
	}

	@Override
	public boolean isFacingValid(byte aFacing) {
		return aFacing > 1;
	}

	@Override
	public boolean isAccessAllowed(EntityPlayer aPlayer) {
		return true;
	}

	@Override
	public boolean ownerControl() {
		return false;
	}

	@Override
	public boolean isEnetOutput() {
		return false;
	}

	@Override
	public boolean isEnetInput() {
		return false;
	}

	@Override
	public boolean isOutputFacing(byte aSide) {
		return false;
	}

	@Override
	public boolean isInputFacing(byte aSide) {
		return false;
	}

	public void onRightclick(EntityPlayer aPlayer) {
		ItemStack tStack = aPlayer.inventory.getStackInSlot(aPlayer.inventory.currentItem);
		if (tStack == null) {
			if ((this.mInventory[0] != null) && (this.mInventory[0].stackSize > 0)) {
				aPlayer.inventory.setInventorySlotContents(aPlayer.inventory.currentItem, this.mInventory[0]);
				getBaseMetaTileEntity().setInventorySlotContents(0, null);
				this.mType = 0;
			}
		}
		else if (this.mInventory[0] == null) {
			if (OrePrefixes.paper.contains(tStack)) {
				aPlayer.inventory.setInventorySlotContents(aPlayer.inventory.currentItem, null);
				getBaseMetaTileEntity().setInventorySlotContents(0, tStack);
				this.mType = 1;
			}
			else if (OrePrefixes.book.contains(tStack)) {
				aPlayer.inventory.setInventorySlotContents(aPlayer.inventory.currentItem, null);
				getBaseMetaTileEntity().setInventorySlotContents(0, tStack);
				this.mType = 2;
			}
			else if ((ItemList.IC2_Food_Can_Filled.isStackEqual(tStack, true, true))
					|| (ItemList.IC2_Food_Can_Spoiled.isStackEqual(tStack, true, true))
					|| (ItemList.IC2_Food_Can_Empty.isStackEqual(tStack, false, true))) {
				aPlayer.inventory.setInventorySlotContents(aPlayer.inventory.currentItem, null);
				getBaseMetaTileEntity().setInventorySlotContents(0, tStack);
				this.mType = 3;
			}
		}
	}

	public void onLeftclick(EntityPlayer aPlayer) {
		if ((this.mInventory[0] != null) && (this.mInventory[0].stackSize > 0)) {
			ItemStack tOutput = GT_Utility.copy(new Object[] { this.mInventory[0] });
			if (!aPlayer.isSneaking()) {
				tOutput.stackSize = 1;
			}
			getBaseMetaTileEntity().decrStackSize(0, tOutput.stackSize);
			EntityItem tEntity = new EntityItem(getBaseMetaTileEntity().getWorld(),
					getBaseMetaTileEntity().getOffsetX(getBaseMetaTileEntity().getFrontFacing(), 1) + 0.5D,
					getBaseMetaTileEntity().getOffsetY(getBaseMetaTileEntity().getFrontFacing(), 1) + 0.5D,
					getBaseMetaTileEntity().getOffsetZ(getBaseMetaTileEntity().getFrontFacing(), 1) + 0.5D, tOutput);
			tEntity.motionX = 0.0D;
			tEntity.motionY = 0.0D;
			tEntity.motionZ = 0.0D;
			getBaseMetaTileEntity().getWorld().spawnEntityInWorld(tEntity);
			if (this.mInventory[0] == null) {
				this.mType = 0;
			}
		}
	}

	@Override
	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GT4Entity_Shelf(this.mName, this.mDescription, this.mTextures);
	}

	@Override
	public void saveNBTData(NBTTagCompound aNBT) {
		aNBT.setInteger("mType", this.mType);
	}

	@Override
	public void loadNBTData(NBTTagCompound aNBT) {
		this.mType = ((byte) aNBT.getInteger("mType"));
	}

	@Override
	public void onValueUpdate(byte aValue) {
		this.mType = aValue;
	}

	@Override
	public byte getUpdateData() {
		return this.mType;
	}

	public boolean allowCoverOnSide(byte aSide, int aCoverID) {
		return aSide != getBaseMetaTileEntity().getFrontFacing();
	}

	@Override
	public String[] getDescription() {
		return new String[] { "Decorative Item Storage", CORE.GT_Tooltip };
	}

	@Override
	public byte getTileEntityBaseType() {
		return 0;
	}

	@Override
	public boolean allowPullStack(IGregTechTileEntity p0, int p1, byte p2, ItemStack p3) {
		return false;
	}

	@Override
	public boolean allowPutStack(IGregTechTileEntity p0, int p1, byte p2, ItemStack p3) {
		return false;
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aConnections, final byte aColorIndex, final boolean aConnected, final boolean aRedstone) {
		return this.mTextures[Math.min(2, aSide)][aColorIndex + 1];
	}

	@Override
	public ITexture[][][] getTextureSet(final ITexture[] aTextures) {
		final ITexture[][][] rTextures = new ITexture[3][17][];
		for (byte i = -1; i < 16; ++i) {
			final ITexture[] tmp0 = { new GT_RenderedTexture(Textures.BlockIcons.COVER_WOOD_PLATE,
					Dyes.getModulation(i, Dyes._NULL.mRGBa)) };
			rTextures[0][i + 1] = tmp0;
			final ITexture[] tmp2 = { new GT_RenderedTexture(Textures.BlockIcons.COVER_WOOD_PLATE,
					Dyes.getModulation(i, Dyes._NULL.mRGBa)) };
			rTextures[1][i + 1] = tmp2;
			final ITexture[] tmp3 = { new GT_RenderedTexture(Textures.BlockIcons.COVER_WOOD_PLATE,
					Dyes.getModulation(i, Dyes._NULL.mRGBa)) };
			rTextures[2][i + 1] = tmp3;
		}
		return rTextures;
	}
}
