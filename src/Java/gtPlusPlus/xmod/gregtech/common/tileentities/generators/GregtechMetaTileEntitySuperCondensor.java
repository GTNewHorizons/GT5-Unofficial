package gtPlusPlus.xmod.gregtech.common.tileentities.generators;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GregtechMetaSuperConductorNodeBase;

public class GregtechMetaTileEntitySuperCondensor
extends GregtechMetaSuperConductorNodeBase {

	public int mEfficiency;

	public GregtechMetaTileEntitySuperCondensor(final int aID, final String aName, final String aNameRegional, final int aTier) {
		super(aID, aName, aNameRegional, aTier, "Requires liquid Nitrogen/Helium", new ITexture[0]);
		this.onConfigLoad();
	}

	public GregtechMetaTileEntitySuperCondensor(final String aName, final int aTier, final String aDescription, final ITexture[][][] aTextures) {
		super(aName, aTier, aDescription, aTextures);
		this.onConfigLoad();
	}

	@Override
	public boolean isOutputFacing(final byte aSide) {
		return aSide == this.getBaseMetaTileEntity().getFrontFacing();
	}

	@Override
	public MetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntitySuperCondensor(this.mName, this.mTier, this.mDescription, this.mTextures);
	}

	@Override
	public GT_Recipe.GT_Recipe_Map getRecipes() {
		return GT_Recipe.GT_Recipe_Map.sDieselFuels;
	}

	@Override
	public int getCapacity() {
		return 64000;
	}

	public void onConfigLoad() {
		this.mEfficiency = 100;
	}

	@Override
	public int getEfficiency() {
		return this.mEfficiency;
	}

	@Override
	public ITexture[] getFront(final byte aColor) {
		return new ITexture[]{super.getFront(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.MACHINE_COIL_SUPERCONDUCTOR), Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI[this.mTier]};
	}

	@Override
	public ITexture[] getBack(final byte aColor) {
		return new ITexture[]{super.getBack(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.MACHINE_COIL_SUPERCONDUCTOR), Textures.BlockIcons.OVERLAYS_ENERGY_IN[this.mTier]};
	}

	@Override
	public ITexture[] getBottom(final byte aColor) {
		return new ITexture[]{super.getBottom(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.MACHINE_COIL_SUPERCONDUCTOR), Textures.BlockIcons.OVERLAYS_ENERGY_IN[this.mTier]};
	}

	@Override
	public ITexture[] getTop(final byte aColor) {
		return new ITexture[]{super.getTop(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.DIESEL_GENERATOR_TOP)};
	}

	@Override
	public ITexture[] getSides(final byte aColor) {
		return new ITexture[]{super.getSides(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.MACHINE_COIL_SUPERCONDUCTOR), Textures.BlockIcons.OVERLAYS_ENERGY_IN[this.mTier]};
	}

	@Override
	public ITexture[] getFrontActive(final byte aColor) {
		return new ITexture[]{super.getFrontActive(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.MACHINE_COIL_SUPERCONDUCTOR), Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI[this.mTier]};
	}

	@Override
	public ITexture[] getBackActive(final byte aColor) {
		return new ITexture[]{super.getBackActive(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.MACHINE_COIL_SUPERCONDUCTOR), Textures.BlockIcons.OVERLAYS_ENERGY_IN[this.mTier]};
	}

	@Override
	public ITexture[] getBottomActive(final byte aColor) {
		return new ITexture[]{super.getBottomActive(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.MACHINE_COIL_SUPERCONDUCTOR), Textures.BlockIcons.OVERLAYS_ENERGY_IN[this.mTier]};
	}

	@Override
	public ITexture[] getTopActive(final byte aColor) {
		return new ITexture[]{super.getTopActive(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.DIESEL_GENERATOR_TOP_ACTIVE)};
	}

	@Override
	public ITexture[] getSidesActive(final byte aColor) {
		return new ITexture[]{super.getSidesActive(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.MACHINE_COIL_SUPERCONDUCTOR), Textures.BlockIcons.OVERLAYS_ENERGY_IN[this.mTier]};
	}
}
