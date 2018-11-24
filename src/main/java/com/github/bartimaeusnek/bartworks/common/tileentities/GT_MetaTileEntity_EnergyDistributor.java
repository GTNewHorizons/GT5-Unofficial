package com.github.bartimaeusnek.bartworks.common.tileentities;

import com.github.bartimaeusnek.bartworks.util.ChatColorHelper;
import gregtech.api.enums.GT_Values;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Transformer;

public class GT_MetaTileEntity_EnergyDistributor extends GT_MetaTileEntity_Transformer {

    public GT_MetaTileEntity_EnergyDistributor(int aID, String aName, String aNameRegional, int aTier, String aDescription) {
        super(aID, aName, aNameRegional, aTier, aDescription);
    }

    public GT_MetaTileEntity_EnergyDistributor(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    public GT_MetaTileEntity_EnergyDistributor(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_EnergyDistributor(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    public long maxEUInput() {
        return GT_Values.V[this.mTier];
    }

    public long maxEUOutput() {
        return GT_Values.V[this.mTier];
    }

    public long maxAmperesOut() {
        return 320;
    }

    public long maxAmperesIn() {
        return 320;
    }

    @Override
    public long maxEUStore() {
        return 512L + (GT_Values.V[this.mTier ] * 320L);
    }

    public String[] getDescription() {
        return new String[] {mDescription,"Voltage: "+ ChatColorHelper.YELLOW +GT_Values.V[this.mTier],"Amperage IN: " + ChatColorHelper.YELLOW + maxAmperesIn(),"Amperage OUT: " + ChatColorHelper.YELLOW + maxAmperesOut(), "Added by bartimaeusnek via BartWorks"};
    }

}
