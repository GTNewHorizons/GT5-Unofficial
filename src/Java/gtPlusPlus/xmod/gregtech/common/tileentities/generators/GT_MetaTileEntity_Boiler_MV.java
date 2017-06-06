package gtPlusPlus.xmod.gregtech.common.tileentities.generators;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class GT_MetaTileEntity_Boiler_MV
extends GT_MetaTileEntity_Boiler_Base {

	public GT_MetaTileEntity_Boiler_MV(int aID, String aNameRegional, int aBoilerTier) {
		super(aID, aNameRegional, aBoilerTier);               
	}

	public GT_MetaTileEntity_Boiler_MV(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
		super(aName, aTier, aDescription, aTextures);
	}

	@Override
	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GT_MetaTileEntity_Boiler_MV(this.mName, 2, this.mDescription, this.mTextures);
	}    

	@Override
	protected GT_RenderedTexture getCasingTexture(){
		return  new GT_RenderedTexture(Textures.BlockIcons.MACHINE_MV_SIDE);
	}

	@Override
	public ITexture[] getFront(final byte aColor) {
		return new ITexture[]{super.getFront(aColor)[0], this.getCasingTexture(),  new GT_RenderedTexture(Textures.BlockIcons.BOILER_FRONT)};
	}

	@Override
	public ITexture[] getTop(final byte aColor) {
		return new ITexture[]{super.getTop(aColor)[0], this.getCasingTexture(), new GT_RenderedTexture(TexturesGtBlock.Casing_Material_Tumbaga)};
	}

	@Override
	public ITexture[] getFrontActive(final byte aColor) {
		return new ITexture[]{super.getFrontActive(aColor)[0], this.getCasingTexture(),  new GT_RenderedTexture(Textures.BlockIcons.BOILER_FRONT_ACTIVE)};
	}

	@Override
	public ITexture[] getTopActive(final byte aColor) {
		return getTop(aColor);
	}

}