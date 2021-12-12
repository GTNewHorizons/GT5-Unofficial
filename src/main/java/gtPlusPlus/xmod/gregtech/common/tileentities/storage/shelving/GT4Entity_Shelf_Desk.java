package gtPlusPlus.xmod.gregtech.common.tileentities.storage.shelving;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.objects.GT_RenderedTexture;

import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

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
	public ITexture[] getFront(final byte aColor) {
		return new ITexture[]{TexturesGtBlock.OVERLAYS_CABINET_FRONT[this.mType < 16 ? this.mType : 0]};
	}

	public ITexture[] getBack(final byte aColor) {
		return new ITexture[]{new GT_RenderedTexture(TexturesGtBlock.VanillaIcon_OakPlanks)};
	}

	public ITexture[] getBottom(final byte aColor) {
		return new ITexture[]{new GT_RenderedTexture(TexturesGtBlock.VanillaIcon_OakPlanks)};
	}

	public ITexture[] getTop(final byte aColor) {
		return new ITexture[]{new GT_RenderedTexture(TexturesGtBlock.VanillaIcon_OakPlanks)};
	}

	public ITexture[] getSides(final byte aColor) {
		return new ITexture[]{new GT_RenderedTexture(TexturesGtBlock.VanillaIcon_OakPlanks)};
	}
}
