package com.github.technus.tectech.thing.metaTileEntity.hatch;

import static net.minecraft.util.StatCollector.translateToLocal;

import com.github.technus.tectech.util.TT_Utility;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;

/**
 * Created by danie_000 on 27.10.2016.
 */
public class GT_MetaTileEntity_Hatch_InputElemental extends GT_MetaTileEntity_Hatch_ElementalContainer {

    public GT_MetaTileEntity_Hatch_InputElemental(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, translateToLocal("gt.blockmachines.emin.desc")); // Elemental Input for
                                                                                                 // Multiblocks
        TT_Utility.setTier(aTier, this);
    }

    public GT_MetaTileEntity_Hatch_InputElemental(String aName, int aTier, String aDescription,
            ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Hatch_InputElemental(mName, mTier, mDescription, mTextures);
    }

    @Override
    public boolean isInputFacing(byte aSide) {
        return aSide == getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public boolean isOutputFacing(byte aSide) {
        return false;
    }

    @Override
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public boolean canConnect(byte side) {
        return isInputFacing(side);
    }
}
