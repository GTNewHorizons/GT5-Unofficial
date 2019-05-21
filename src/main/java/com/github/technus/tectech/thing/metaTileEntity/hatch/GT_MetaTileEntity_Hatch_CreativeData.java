package com.github.technus.tectech.thing.metaTileEntity.hatch;

import com.github.technus.tectech.Util;
import com.github.technus.tectech.mechanics.dataTransport.QuantumDataPacket;
import com.github.technus.tectech.thing.metaTileEntity.pipe.GT_MetaTileEntity_Pipe_Data;
import com.github.technus.tectech.thing.metaTileEntity.pipe.IConnectsToDataPipe;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.util.GT_Utility;
import net.minecraft.nbt.NBTTagCompound;

import static com.github.technus.tectech.CommonValues.MOVE_AT;

/**
 * Created by danie_000 on 27.10.2016.
 */
public class GT_MetaTileEntity_Hatch_CreativeData extends GT_MetaTileEntity_Hatch_DataConnector<QuantumDataPacket> {
    public GT_MetaTileEntity_Hatch_CreativeData(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, "Quantum Data Output");
        Util.setTier(aTier,this);
    }

    public GT_MetaTileEntity_Hatch_CreativeData(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Hatch_CreativeData(mName, mTier, mDescription, mTextures);
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
    public boolean isDataInputFacing(byte side) {
        return isInputFacing(side);
    }

    @Override
    protected QuantumDataPacket loadPacketFromNBT(NBTTagCompound nbt) {
        return new QuantumDataPacket(nbt);
    }

    @Override
    public boolean canConnectData(byte side) {
        return isOutputFacing(side);
    }

    @Override
    public void moveAround(IGregTechTileEntity aBaseMetaTileEntity) {
        IConnectsToDataPipe current = this, source = this, next;
        int range = 0;
        while ((next = current.getNext(source)) != null && range++ < 1000) {
            if (next instanceof GT_MetaTileEntity_Hatch_InputData) {
                ((GT_MetaTileEntity_Hatch_InputData) next).setContents(q);
                break;
            }
            source = current;
            current = next;
        }
        q = null;
    }

    @Override
    public IConnectsToDataPipe getNext(IConnectsToDataPipe source/*==this*/) {
        IGregTechTileEntity base = getBaseMetaTileEntity();
        byte color = base.getColorization();
        if (color < 0) {
            return null;
        }
        IGregTechTileEntity next = base.getIGregTechTileEntityAtSide(base.getFrontFacing());
        if (next == null) {
            return null;
        }
        IMetaTileEntity meta = next.getMetaTileEntity();
        if (meta instanceof GT_MetaTileEntity_Pipe_Data){
            return (IConnectsToDataPipe) meta;
        }else if (meta instanceof GT_MetaTileEntity_Hatch_InputData &&
                ((GT_MetaTileEntity_Hatch_InputData) meta).getColorization()==color &&
                ((GT_MetaTileEntity_Hatch_InputData) meta).canConnectData(GT_Utility.getOppositeSide(base.getFrontFacing()))) {
            return (IConnectsToDataPipe) meta;
        }
        return null;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            if (MOVE_AT == aTick % 20) {
                if (aBaseMetaTileEntity.isAllowedToWork()) {
                    getBaseMetaTileEntity().setActive(true);
                    if(q==null) q=new QuantumDataPacket(0xFFFFFFFFL);
                    moveAround(aBaseMetaTileEntity);
                } else {
                    q=null;
                    getBaseMetaTileEntity().setActive(false);
                }
            }
        }
    }
}
