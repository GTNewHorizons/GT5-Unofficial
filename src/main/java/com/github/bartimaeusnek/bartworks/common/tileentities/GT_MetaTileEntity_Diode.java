package com.github.bartimaeusnek.bartworks.common.tileentities;

import com.github.bartimaeusnek.bartworks.util.ChatColorHelper;
import gregtech.api.enums.GT_Values;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicHull;

public class GT_MetaTileEntity_Diode extends GT_MetaTileEntity_BasicHull {

    private long amps;

    public GT_MetaTileEntity_Diode(int aID, String aName, String aNameRegional, int aTier, int amps) {
        super(aID, aName, aNameRegional, aTier, "A Simple diode that will allow Energy Flow in only one direction.", null);
        this.amps=amps;
    }

    public GT_MetaTileEntity_Diode(String aName, int aTier, int aInvSlotCount, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    @Override
    public long maxAmperesOut() {
        return amps;
    }

    @Override
    public long maxAmperesIn() {
        return amps;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Diode(this.mName, this.mTier, this.mInventory.length, this.mDescriptionArray, this.mTextures);
    }
    public String[] getDescription() {
        return new String[] {mDescription,"Voltage: "+ ChatColorHelper.YELLOW + GT_Values.V[this.mTier],"Amperage IN: " + ChatColorHelper.YELLOW + maxAmperesIn(),"Amperage OUT: " + ChatColorHelper.YELLOW + maxAmperesOut(), "Added by bartimaeusnek via "+ChatColorHelper.DARKGREEN+"BartWorks"};
    }
}
