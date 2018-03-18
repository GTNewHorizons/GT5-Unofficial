package com.github.technus.tectech.thing.metaTileEntity.hatch;

import com.github.technus.tectech.dataFramework.QuantumDataPacket;
import com.github.technus.tectech.thing.metaTileEntity.pipe.iConnectsToDataPipe;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by danie_000 on 27.10.2016.
 */
public class GT_MetaTileEntity_Hatch_InputData extends GT_MetaTileEntity_Hatch_DataConnector<QuantumDataPacket> {
    public boolean delDelay = true;

    public GT_MetaTileEntity_Hatch_InputData(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, "Quantum Data Input for Multiblocks");
    }

    public GT_MetaTileEntity_Hatch_InputData(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Hatch_InputData(mName, mTier, mDescription, mTextures);
    }

    @Override
    protected QuantumDataPacket loadPacketFromNBT(NBTTagCompound nbt) {
        return new QuantumDataPacket(nbt);
    }

    @Override
    public boolean isInputFacing(byte aSide) {
        return aSide == getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public boolean isDataInputFacing(byte side) {
        return isInputFacing(side);
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
    public iConnectsToDataPipe getNext(iConnectsToDataPipe source) {
        return null;
    }

    @Override
    public void moveAround(IGregTechTileEntity aBaseMetaTileEntity) {
        if (delDelay) {
            delDelay = false;
        } else {
            q = null;
        }
    }
}
