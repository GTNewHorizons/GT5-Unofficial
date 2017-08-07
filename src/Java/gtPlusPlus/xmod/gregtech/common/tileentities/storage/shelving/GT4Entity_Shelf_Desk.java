package gtPlusPlus.xmod.gregtech.common.tileentities.storage.shelving;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class GT4Entity_Shelf_Desk extends GT4Entity_Shelf {
	public GT4Entity_Shelf_Desk(int aID, String aName, String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GT4Entity_Shelf_Desk(String mName) {
		super(mName);
	}

	@Override
	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GT4Entity_Shelf_Desk(this.mName);
	}

	@Override
	public int getTextureIndex(byte aSide, byte aFacing, boolean aActive, boolean aRedstone) {
		if (aSide == aFacing) {
			return 222;
		}
		if (aSide == 0) {
			return 32;
		}
		if (aSide == 1) {
			return 29;
		}
		return 40;
	}

	@Override
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
			aPlayer.inventory.setInventorySlotContents(aPlayer.inventory.currentItem, null);
			getBaseMetaTileEntity().setInventorySlotContents(0, tStack);
		}
	}
}
