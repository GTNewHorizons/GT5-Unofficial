package gtPlusPlus.xmod.gregtech.common.tileentities.storage.shelving;

import net.minecraft.entity.player.EntityPlayer;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class GT4Entity_Shelf_Compartment extends GT4Entity_Shelf {
	

	public GT4Entity_Shelf_Compartment(final int aID, final String aName, final String aNameRegional, final String aDescription) {
		super(aID, aName, aNameRegional, aDescription);
	}
	
	public GT4Entity_Shelf_Compartment(String mName, String mDescriptionArray, ITexture[][][] mTextures) {
		super(mName, mDescriptionArray, mTextures);
	}

	@Override
	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GT4Entity_Shelf_Compartment(this.mName, this.mDescription, this.mTextures);
	}

	@Override
	public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
		if (aSide == getBaseMetaTileEntity().getFrontFacing()) {
			this.mType = ((byte) ((this.mType + 1) % 16));
			PlayerUtils.messagePlayer(aPlayer, "Set type to "+this.mType+".");
		}
	}

	@Override
	public ITexture[] getFront(final byte aColor) {
		return new ITexture[]{TexturesGtBlock.OVERLAYS_COMPARTMENT_FRONT[this.mType < 16 ? this.mType : 0]};
	}
}
