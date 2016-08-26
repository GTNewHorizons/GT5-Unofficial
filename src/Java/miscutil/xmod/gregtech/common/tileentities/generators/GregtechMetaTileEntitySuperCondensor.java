package miscutil.xmod.gregtech.common.tileentities.generators;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import miscutil.xmod.gregtech.api.metatileentity.implementations.GregtechMetaSuperConductorNodeBase;

public class GregtechMetaTileEntitySuperCondensor
        extends GregtechMetaSuperConductorNodeBase {

    public int mEfficiency;

    public GregtechMetaTileEntitySuperCondensor(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, "Requires liquid Nitrogen/Helium", new ITexture[0]);
        onConfigLoad();
    }

    public GregtechMetaTileEntitySuperCondensor(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
        onConfigLoad();
    }

    @Override
	public boolean isOutputFacing(byte aSide) {
        return aSide == getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
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
	public ITexture[] getFront(byte aColor) {
        return new ITexture[]{super.getFront(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.MACHINE_COIL_SUPERCONDUCTOR), Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI[this.mTier]};
    }

    @Override
	public ITexture[] getBack(byte aColor) {
        return new ITexture[]{super.getBack(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.MACHINE_COIL_SUPERCONDUCTOR), Textures.BlockIcons.OVERLAYS_ENERGY_IN[this.mTier]};
    }

    @Override
	public ITexture[] getBottom(byte aColor) {
        return new ITexture[]{super.getBottom(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.MACHINE_COIL_SUPERCONDUCTOR), Textures.BlockIcons.OVERLAYS_ENERGY_IN[this.mTier]};
    }

    @Override
	public ITexture[] getTop(byte aColor) {
        return new ITexture[]{super.getTop(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.DIESEL_GENERATOR_TOP)};
    }

    @Override
	public ITexture[] getSides(byte aColor) {
        return new ITexture[]{super.getSides(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.MACHINE_COIL_SUPERCONDUCTOR), Textures.BlockIcons.OVERLAYS_ENERGY_IN[this.mTier]};
    }

    @Override
	public ITexture[] getFrontActive(byte aColor) {
        return new ITexture[]{super.getFrontActive(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.MACHINE_COIL_SUPERCONDUCTOR), Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI[this.mTier]};
    }

    @Override
	public ITexture[] getBackActive(byte aColor) {
        return new ITexture[]{super.getBackActive(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.MACHINE_COIL_SUPERCONDUCTOR), Textures.BlockIcons.OVERLAYS_ENERGY_IN[this.mTier]};
    }

    @Override
	public ITexture[] getBottomActive(byte aColor) {
        return new ITexture[]{super.getBottomActive(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.MACHINE_COIL_SUPERCONDUCTOR), Textures.BlockIcons.OVERLAYS_ENERGY_IN[this.mTier]};
    }

    @Override
	public ITexture[] getTopActive(byte aColor) {
        return new ITexture[]{super.getTopActive(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.DIESEL_GENERATOR_TOP_ACTIVE)};
    }

    @Override
	public ITexture[] getSidesActive(byte aColor) {
        return new ITexture[]{super.getSidesActive(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.MACHINE_COIL_SUPERCONDUCTOR), Textures.BlockIcons.OVERLAYS_ENERGY_IN[this.mTier]};
    }
}
