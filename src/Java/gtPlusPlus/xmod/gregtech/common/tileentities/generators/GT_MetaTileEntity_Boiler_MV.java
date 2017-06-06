package gtPlusPlus.xmod.gregtech.common.tileentities.generators;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;

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
        return new GT_MetaTileEntity_Boiler_MV(this.mName, this.mTier, this.mDescription, this.mTextures);
    }

}