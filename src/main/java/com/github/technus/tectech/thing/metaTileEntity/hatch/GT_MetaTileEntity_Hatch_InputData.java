package com.github.technus.tectech.thing.metaTileEntity.hatch;

import com.github.technus.tectech.util.CommonValues;
import com.github.technus.tectech.util.Util;
import com.github.technus.tectech.mechanics.dataTransport.QuantumDataPacket;
import com.github.technus.tectech.mechanics.pipe.IConnectsToDataPipe;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;

import static net.minecraft.util.StatCollector.translateToLocal;

/**
 * Created by danie_000 on 27.10.2016.
 */
public class GT_MetaTileEntity_Hatch_InputData extends GT_MetaTileEntity_Hatch_DataConnector<QuantumDataPacket> {
    private boolean delDelay = true;

    public GT_MetaTileEntity_Hatch_InputData(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, "");
        Util.setTier(aTier,this);
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
    public boolean canConnectData(byte side) {
        return isInputFacing(side);
    }

    @Override
    public IConnectsToDataPipe getNext(IConnectsToDataPipe source) {
        return null;
    }

    public void setContents(QuantumDataPacket qIn){
        if(qIn==null){
            this.q=null;
        }else{
            if(qIn.getContent()>0) {
                this.q = qIn;
                delDelay=true;
            }else{
                this.q=null;
            }
        }
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                CommonValues.TEC_MARK_EM,
                translateToLocal("gt.blockmachines.hatch.datain.desc.0"),//Quantum Data Input for Multiblocks
                translateToLocal("gt.blockmachines.hatch.datain.desc.1"),//High speed fibre optics connector.
                EnumChatFormatting.AQUA + translateToLocal("gt.blockmachines.hatch.datain.desc.2")//Must be painted to work
        };
    }

    @Override
    public void moveAround(IGregTechTileEntity aBaseMetaTileEntity) {
        if (delDelay) {
            delDelay = false;
        } else {
            setContents(null);
        }
    }
}
