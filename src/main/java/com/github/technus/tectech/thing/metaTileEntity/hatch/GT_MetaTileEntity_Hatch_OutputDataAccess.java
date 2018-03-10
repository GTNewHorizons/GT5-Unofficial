package com.github.technus.tectech.thing.metaTileEntity.hatch;

import com.github.technus.tectech.dataFramework.InventoryDataPacket;
import com.github.technus.tectech.thing.metaTileEntity.pipe.GT_MetaTileEntity_Pipe_Data;
import com.github.technus.tectech.thing.metaTileEntity.pipe.iConnectsToDataPipe;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.util.GT_Utility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class GT_MetaTileEntity_Hatch_OutputDataAccess extends GT_MetaTileEntity_Hatch_DataConnector<InventoryDataPacket> {
    public GT_MetaTileEntity_Hatch_OutputDataAccess(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, "ItemStack Data Output for Multiblocks");
    }

    public GT_MetaTileEntity_Hatch_OutputDataAccess(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public boolean isFacingValid(byte aFacing) {
        return true;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Hatch_OutputDataAccess(this.mName, this.mTier, this.mDescription, this.mTextures);
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (aBaseMetaTileEntity.isClientSide()) {
            return true;
        } else {
            aBaseMetaTileEntity.openGUI(aPlayer);
            return true;
        }
    }

    @Override
    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return null;
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return null;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
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
    public boolean canConnect(byte side) {
        return isOutputFacing(side);
    }

    @Override
    public void moveAround(IGregTechTileEntity aBaseMetaTileEntity) {
        iConnectsToDataPipe current = this, source = this, next;
        int range = 0;
        while ((next = current.getNext(source)) != null && range++ < 1000) {
            if (next instanceof GT_MetaTileEntity_Hatch_InputDataAccess) {
                ((GT_MetaTileEntity_Hatch_InputDataAccess) next).setContents(q);
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
            if (meta instanceof GT_MetaTileEntity_Hatch_InputDataAccess
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

    @Override
    protected InventoryDataPacket loadPacketFromNBT(NBTTagCompound nbt) {
        return new InventoryDataPacket(nbt);
    }
}
