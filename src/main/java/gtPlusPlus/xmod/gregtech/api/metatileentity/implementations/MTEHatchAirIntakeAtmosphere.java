package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;

public class MTEHatchAirIntakeAtmosphere extends MTEHatchAirIntake {

    public MTEHatchAirIntakeAtmosphere(final int aID, final String aName, final String aNameRegional, final int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    public MTEHatchAirIntakeAtmosphere(final String aName, final int aTier, final String[] aDescription,
        final ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public String[] getCustomTooltip() {
        String[] aTooltip = new String[3];
        aTooltip[0] = "§cDO NOT OBSTRUCT THE INPUT!";
        aTooltip[1] = "Draws in Air from the surrounding environment";
        aTooltip[2] = "Completelly fills up every " + getMaxTickTime() + " ticks";
        return aTooltip;
    }

    @Override
    public MetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTEHatchAirIntakeAtmosphere(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public int getAmountOfFluidToGenerate() {
        return 2000000000;
    }

    @Override
    public int getCapacity() {
        return 2000000000;
    }
}
