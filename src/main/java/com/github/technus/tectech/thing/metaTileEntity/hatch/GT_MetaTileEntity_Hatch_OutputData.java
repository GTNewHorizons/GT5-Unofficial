package com.github.technus.tectech.thing.metaTileEntity.hatch;

import com.github.technus.tectech.thing.metaTileEntity.pipe.GT_MetaTileEntity_Pipe_Data;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.util.GT_Utility;

/**
 * Created by danie_000 on 27.10.2016.
 */
public class GT_MetaTileEntity_Hatch_OutputData extends GT_MetaTileEntity_Hatch_DataConnector {
    public GT_MetaTileEntity_Hatch_OutputData(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, "Data Output for Multiblocks");
    }

    public GT_MetaTileEntity_Hatch_OutputData(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Hatch_OutputData(mName, mTier, mDescription, mTextures);
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

    @Override
    public void moveAround(IGregTechTileEntity aBaseMetaTileEntity) {//TODO
        byte color = getBaseMetaTileEntity().getColorization();
        if (color < 0) return;
        byte front = aBaseMetaTileEntity.getFrontFacing();
        byte opposite = GT_Utility.getOppositeSide(getBaseMetaTileEntity().getFrontFacing());
        for (byte dist = 1; dist < 16; dist++) {
            IGregTechTileEntity tGTTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityAtSideAndDistance(front, dist);
            if (tGTTileEntity != null && tGTTileEntity.getColorization() == color) {
                IMetaTileEntity aMetaTileEntity = tGTTileEntity.getMetaTileEntity();
                if (aMetaTileEntity != null) {
                    if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputData &&
                            opposite == aMetaTileEntity.getBaseMetaTileEntity().getFrontFacing()) {
                        ((GT_MetaTileEntity_Hatch_InputData) aMetaTileEntity).timeout=2;
                        ((GT_MetaTileEntity_Hatch_InputData) aMetaTileEntity).data=data;
                        return;
                    } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Pipe_Data) {
                        if (((GT_MetaTileEntity_Pipe_Data) aMetaTileEntity).connectionCount > 2) return;
                    } else return;
                } else return;
            } else return;
        }
        //trace optics all the way to the end, no branching splitting etc.
        //Coloring requirement!
        //set data to match this and timout to 3
    }
}
