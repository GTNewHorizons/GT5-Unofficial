package com.github.technus.tectech.thing.metaTileEntity.hatch;

import com.github.technus.tectech.dataFramework.QuantumDataPacket;
import com.github.technus.tectech.thing.metaTileEntity.pipe.GT_MetaTileEntity_Pipe_Data;
import com.github.technus.tectech.thing.metaTileEntity.pipe.iConnectsToDataPipe;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.util.GT_Utility;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by danie_000 on 27.10.2016.
 */
public class GT_MetaTileEntity_Hatch_OutputData extends GT_MetaTileEntity_Hatch_DataConnector<QuantumDataPacket> {
    public GT_MetaTileEntity_Hatch_OutputData(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, "Quantum Data Output for Multiblocks");
    }

    public GT_MetaTileEntity_Hatch_OutputData(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Hatch_OutputData(mName, mTier, mDescription, mTextures);
    }

    @Override
    protected QuantumDataPacket loadPacketFromNBT(NBTTagCompound nbt) {
        return new QuantumDataPacket(nbt);
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
    public boolean isDataInputFacing(byte side) {
        return isInputFacing(side);
    }

    @Override
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public boolean canConnect(byte side) {
        return isOutputFacing(side);
    }

    @Override
    public void moveAround(IGregTechTileEntity aBaseMetaTileEntity) {
        iConnectsToDataPipe current = this, source = this, next;
        int range = 0;
        while ((next = current.getNext(source)) != null && range++ < 1000) {
            if (next instanceof GT_MetaTileEntity_Hatch_InputData) {
                ((GT_MetaTileEntity_Hatch_InputData) next).q = q;
                ((GT_MetaTileEntity_Hatch_InputData) next).delDelay = true;
                break;
            }
            source = current;
            current = next;
        }
        q = null;
    }

    @Override
    public iConnectsToDataPipe getNext(iConnectsToDataPipe source/*==this*/) {
        IGregTechTileEntity base = getBaseMetaTileEntity();
        byte color = base.getColorization();
        if (color < 0) {
            return null;
        }
        IGregTechTileEntity next = base.getIGregTechTileEntityAtSide(base.getFrontFacing());
        if (next == null || color != base.getColorization()) {
            return null;
        }
        IMetaTileEntity meta = next.getMetaTileEntity();
        if (meta instanceof iConnectsToDataPipe) {
            if (meta instanceof GT_MetaTileEntity_Hatch_InputData
                    && GT_Utility.getOppositeSide(next.getFrontFacing()) == base.getFrontFacing()) {
                return (iConnectsToDataPipe) meta;
            }
            if (meta instanceof GT_MetaTileEntity_Pipe_Data
                    /*&& ((GT_MetaTileEntity_Pipe_Data) meta).connectionCount==2*/)//Checked later
            {
                return (iConnectsToDataPipe) meta;
            }
        }
        return null;
    }
}
