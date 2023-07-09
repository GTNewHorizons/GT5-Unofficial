package gregtech.api.metatileentity.implementations;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;

public class GT_MetaTileEntity_Hatch_QuadrupleHumongous extends GT_MetaTileEntity_Hatch_MultiInput {

    public GT_MetaTileEntity_Hatch_QuadrupleHumongous(int aID, int aSlot, String aName, String aNameRegional) {
        super(aID, aSlot, aName, aNameRegional, 0);
    }

    public GT_MetaTileEntity_Hatch_QuadrupleHumongous(String aName, int aSlot, int aTier, String[] aDescription,
        ITexture[][][] aTextures) {
        super(aName, aSlot, aTier, aDescription, aTextures);
    }

    @Override
    public int getCapacityPerTank(int aTier, int aSlot) {
        return 500_000_000;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Hatch_QuadrupleHumongous(mName, getMaxType(), mTier, mDescriptionArray, mTextures);

    }
}
