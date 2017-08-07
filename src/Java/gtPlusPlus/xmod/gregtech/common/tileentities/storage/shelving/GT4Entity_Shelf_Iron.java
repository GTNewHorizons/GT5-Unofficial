package gtPlusPlus.xmod.gregtech.common.tileentities.storage.shelving;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.objects.GT_RenderedTexture;

public class GT4Entity_Shelf_Iron extends GT4Entity_Shelf {

	public GT4Entity_Shelf_Iron(final int aID, final String aName, final String aNameRegional, final String aDescription) {
		super(aID, aName, aNameRegional, aDescription);
	}
		
	public GT4Entity_Shelf_Iron(String mName, String[] mDescriptionArray, ITexture[][][] mTextures) {
		super(mName, mDescriptionArray, mTextures);
	}

	@Override
	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GT4Entity_Shelf_Iron(this.mName, this.mDescriptionArray, this.mTextures);
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
