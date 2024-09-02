package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;

public class MTEHatchAirIntakeExtreme extends METHatchAirIntake {

    public MTEHatchAirIntakeExtreme(final int aID, final String aName, final String aNameRegional, final int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    public MTEHatchAirIntakeExtreme(final String aName, final int aTier, final String[] aDescription,
        final ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTEHatchAirIntakeExtreme(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public int getAmountOfFluidToGenerate() {
        return 8000;
    }

    @Override
    public int getCapacity() {
        return 256000;
    }
}
