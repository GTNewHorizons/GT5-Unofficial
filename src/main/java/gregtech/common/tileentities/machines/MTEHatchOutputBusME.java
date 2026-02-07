package gregtech.common.tileentities.machines;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;

@Deprecated
public class MTEHatchOutputBusME extends gregtech.common.tileentities.machines.outputme.MTEHatchOutputBusME {

    public MTEHatchOutputBusME(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEHatchOutputBusME(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchOutputBusME(mName, mTier, mDescriptionArray, mTextures);
    }
}
