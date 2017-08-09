package gtPlusPlus.xmod.gregtech.common.tileentities.storage.shelving;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class GT4Entity_Shelf_Desk extends GT4Entity_Shelf {

	public GT4Entity_Shelf_Desk(final int aID, final String aName, final String aNameRegional, final String aDescription) {
		super(aID, aName, aNameRegional, aDescription);
	}
	
	public GT4Entity_Shelf_Desk(String mName, String mDescriptionArray, ITexture[][][] mTextures) {
		super(mName, mDescriptionArray, mTextures);
	}

	@Override
	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GT4Entity_Shelf_Desk(this.mName, this.mDescription, this.mTextures);
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

	@Override
	public ITexture[][][] getTextureSet(final ITexture[] aTextures) {
		final ITexture[][][] rTextures = new ITexture[3][17][];
		for (byte i = -1; i < 16; ++i) {
			final ITexture[] tmp0 = { new GT_RenderedTexture(Textures.BlockIcons.MACHINE_STEEL_BOTTOM,
					Dyes.getModulation(i, Dyes._NULL.mRGBa)) };
			rTextures[0][i + 1] = tmp0;
			final ITexture[] tmp2 = { new GT_RenderedTexture(Textures.BlockIcons.MACHINE_STEEL_TOP,
					Dyes.getModulation(i, Dyes._NULL.mRGBa)) };
			rTextures[1][i + 1] = tmp2;
			final ITexture[] tmp3 = { new GT_RenderedTexture(Textures.BlockIcons.MACHINE_STEEL_SIDE,
					Dyes.getModulation(i, Dyes._NULL.mRGBa)) };
			rTextures[2][i + 1] = tmp3;
		}
		return rTextures;
	}
}
