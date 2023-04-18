package com.github.technus.tectech.thing.metaTileEntity.hatch;

import static net.minecraft.util.StatCollector.translateToLocal;

import com.github.technus.tectech.thing.metaTileEntity.pipe.GT_MetaTileEntity_Pipe_EM;
import com.github.technus.tectech.util.TT_Utility;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.util.GT_Utility;

/**
 * Created by danie_000 on 27.10.2016.
 */
public class GT_MetaTileEntity_Hatch_OutputElemental extends GT_MetaTileEntity_Hatch_ElementalContainer {

    public GT_MetaTileEntity_Hatch_OutputElemental(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, translateToLocal("gt.blockmachines.emout.desc")); // Elemental Output
                                                                                                  // for Multiblocks
        TT_Utility.setTier(aTier, this);
    }

    public GT_MetaTileEntity_Hatch_OutputElemental(String aName, int aTier, String[] aDescription,
            ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Hatch_OutputElemental(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public boolean isOutputFacing(byte aSide) {
        return aSide == getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public boolean isInputFacing(byte aSide) {
        return false;
    }

    @Override
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public void moveAround(IGregTechTileEntity aBaseMetaTileEntity) {
        byte color = getBaseMetaTileEntity().getColorization();
        if (color < 0) {
            return;
        }
        byte front = aBaseMetaTileEntity.getFrontFacing();
        byte opposite = GT_Utility.getOppositeSide(front);
        for (byte dist = 1; dist < 16; dist++) {
            IGregTechTileEntity tGTTileEntity = aBaseMetaTileEntity
                    .getIGregTechTileEntityAtSideAndDistance(front, dist);
            if (tGTTileEntity != null && tGTTileEntity.getColorization() == color) {
                IMetaTileEntity aMetaTileEntity = tGTTileEntity.getMetaTileEntity();
                if (aMetaTileEntity != null) {
                    if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputElemental
                            && opposite == tGTTileEntity.getFrontFacing()) {
                        ((GT_MetaTileEntity_Hatch_InputElemental) aMetaTileEntity).getContentHandler()
                                .putUnifyAll(content);
                        ((GT_MetaTileEntity_Hatch_InputElemental) aMetaTileEntity).updateSlots();
                        content.clear();
                        return;
                    } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Pipe_EM) {
                        if (((GT_MetaTileEntity_Pipe_EM) aMetaTileEntity).connectionCount != 2) {
                            return;
                        } else {
                            ((GT_MetaTileEntity_Pipe_EM) aMetaTileEntity).markUsed();
                        }
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    @Override
    public boolean canConnect(byte side) {
        return isOutputFacing(side);
    }
}
