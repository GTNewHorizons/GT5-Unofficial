package com.github.technus.tectech.thing.metaTileEntity.hatch;

import com.github.technus.tectech.util.CommonValues;
import com.github.technus.tectech.util.Util;
import com.github.technus.tectech.mechanics.dataTransport.InventoryDataPacket;
import com.github.technus.tectech.thing.metaTileEntity.pipe.GT_MetaTileEntity_Pipe_Data;
import com.github.technus.tectech.mechanics.pipe.IConnectsToDataPipe;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.util.GT_Utility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;

import static net.minecraft.util.StatCollector.translateToLocal;

public class GT_MetaTileEntity_Hatch_OutputDataItems extends GT_MetaTileEntity_Hatch_DataConnector<InventoryDataPacket> {
    public GT_MetaTileEntity_Hatch_OutputDataItems(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, "");
        Util.setTier(aTier,this);
    }

    public GT_MetaTileEntity_Hatch_OutputDataItems(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
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
        return new GT_MetaTileEntity_Hatch_OutputDataItems(this.mName, this.mTier, this.mDescription, this.mTextures);
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (aBaseMetaTileEntity.isClientSide()) {
            return true;
        } else {
            super.onRightclick(aBaseMetaTileEntity, aPlayer);
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
    protected InventoryDataPacket loadPacketFromNBT(NBTTagCompound nbt) {
        return new InventoryDataPacket(nbt);
    }

    @Override
    public boolean isDataInputFacing(byte side) {
        return isInputFacing(side);
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
            if (next instanceof GT_MetaTileEntity_Hatch_InputDataItems) {
                ((GT_MetaTileEntity_Hatch_InputDataItems) next).setContents(q);
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
            ((GT_MetaTileEntity_Pipe_Data) meta).markUsed();
            return (IConnectsToDataPipe) meta;
        }else if (meta instanceof GT_MetaTileEntity_Hatch_InputDataItems &&
                ((GT_MetaTileEntity_Hatch_InputDataItems) meta).getColorization()==color &&
                ((GT_MetaTileEntity_Hatch_InputDataItems) meta).canConnectData(GT_Utility.getOppositeSide(base.getFrontFacing()))) {
            return (IConnectsToDataPipe) meta;
        }
        return null;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                CommonValues.TEC_MARK_EM,
                translateToLocal("gt.blockmachines.hatch.dataoutass.desc.0"),//ItemStack Data Output for Multiblocks
                translateToLocal("gt.blockmachines.hatch.dataoutass.desc.1"),//High speed fibre optics connector.
                EnumChatFormatting.AQUA + translateToLocal("gt.blockmachines.hatch.dataoutass.desc.2")//Must be painted to work
        };
    }
}
