package gtPlusPlus.xmod.gregtech.common.tileentities.storage.shelving;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.lib.CORE;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class GT4Entity_Shelf extends MetaTileEntity {
	public byte mType = 0;

	public GT4Entity_Shelf(int aID, String aName, String aNameRegional) {
		super(aID, aName, aNameRegional, 1);
	}

	public GT4Entity_Shelf(String aName) {
		super(aName, 1);
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
		return new GT4Entity_Shelf(this.mName);
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

	public int getTextureIndex(byte aSide, byte aFacing, boolean aActive, boolean aRedstone) {
		if (aSide == aFacing) {
			return 208 + this.mType;
		}
		return 10;
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
	public ITexture[] getTexture(IGregTechTileEntity p0, byte p1, byte p2, byte p3, boolean p4, boolean p5) {
		// TODO Auto-generated method stub
		return null;
	}
}
