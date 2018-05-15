package gtPlusPlus.xmod.gregtech.common.tileentities.storage.shelving;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;

public class GT4Entity_Shelf_Iron extends GT4Entity_Shelf {

	public GT4Entity_Shelf_Iron(final int aID, final String aName, final String aNameRegional, final String aDescription) {
		super(aID, aName, aNameRegional, aDescription);
	}
		
	public GT4Entity_Shelf_Iron(String mName, String mDescriptionArray, ITexture[][][] mTextures) {
		super(mName, mDescriptionArray, mTextures);
	}

	@Override
	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GT4Entity_Shelf_Iron(this.mName, this.mDescription, this.mTextures);
	}

	public ITexture[] getFront(final byte aColor) {
		return new ITexture[]{texSideCabinet};
	}

	public ITexture[] getBack(final byte aColor) {
		return new ITexture[]{texSide};
	}

	public ITexture[] getBottom(final byte aColor) {
		return new ITexture[]{texBottom};
	}

	public ITexture[] getTop(final byte aColor) {
		return new ITexture[]{texTop};
	}

	public ITexture[] getSides(final byte aColor) {
		return new ITexture[]{texSide};
	}
}
