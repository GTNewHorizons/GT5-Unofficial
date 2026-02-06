package gregtech.common.tileentities.machines;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;

@Deprecated
public class MTEHatchOutputME extends gregtech.common.tileentities.machines.outputme.MTEHatchOutputME {

    public MTEHatchOutputME(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEHatchOutputME(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchOutputME(mName, mTier, mDescriptionArray, mTextures);
    }
}
