package gregtech.api.metatileentity.implementations;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;

public class MTEHatchQuadrupleHumongous extends MTEHatchMultiInput {

    public MTEHatchQuadrupleHumongous(int aID, int aSlot, String aName, String aNameRegional) {
        super(aID, aSlot, aName, aNameRegional, 13);
    }

    public MTEHatchQuadrupleHumongous(String aName, int aSlot, int aTier, String[] aDescription,
        ITexture[][][] aTextures) {
        super(aName, aSlot, aTier, aDescription, aTextures);
    }

    @Override
    public int getCapacityPerTank(int aTier, int aSlot) {
        return 500_000_000;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchQuadrupleHumongous(mName, getMaxType(), mTier, mDescriptionArray, mTextures);
    }
}
