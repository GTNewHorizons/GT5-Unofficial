package gtPlusPlus.xmod.gregtech.common.tileentities.generators;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;

public class GT_MetaTileEntity_Boiler_HV
        extends GT_MetaTileEntity_Boiler_Base {
			
    public GT_MetaTileEntity_Boiler_HV(int aID, String aNameRegional, int aBoilerTier) {
        super(aID, aNameRegional, aBoilerTier);               
    }

    @Override
	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Boiler_HV(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

}