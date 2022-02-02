package com.github.technus.tectech.thing.metaTileEntity.hatch;


import com.github.technus.tectech.util.Util;
import com.github.technus.tectech.thing.metaTileEntity.hatch.gui.GT_GUIContainer_ParamText;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import eu.usrv.yamcore.network.client.AbstractClientMessageHandler;
import eu.usrv.yamcore.network.server.AbstractServerMessageHandler;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class TextParametersMessage implements IMessage {
    int mPosX;
    int mPosY;
    int mPosZ;
    int mPosD;
    String mVal0;
    String mVal1;

    public TextParametersMessage() {}

    @Override
    public void fromBytes(ByteBuf pBuffer) {
        NBTTagCompound tTag = ByteBufUtils.readTag(pBuffer);
        mPosX = tTag.getInteger("posx");
        mPosY = tTag.getInteger("posy");
        mPosZ = tTag.getInteger("posz");
        mPosD = tTag.getInteger("posd");
        mVal0 = tTag.getString("value0s");
        if(mVal0==null) {
            mVal0="";
        }
        mVal1 = tTag.getString("value1s");
        if(mVal1==null) {
            mVal1="";
        }
    }

    @Override
    public void toBytes(ByteBuf pBuffer) {
        NBTTagCompound tFXTag = new NBTTagCompound();
        tFXTag.setInteger("posx", mPosX);
        tFXTag.setInteger("posy", mPosY);
        tFXTag.setInteger("posz", mPosZ);
        tFXTag.setInteger("posd", mPosD);
        tFXTag.setString("value0s", mVal0);
        tFXTag.setString("value1s", mVal1);
        ByteBufUtils.writeTag(pBuffer, tFXTag);
    }

    public static class ParametersTextQuery extends TextParametersMessage {
        public ParametersTextQuery() {}

        public ParametersTextQuery(GT_MetaTileEntity_Hatch_ParamText metaTile) {
            IGregTechTileEntity base=metaTile.getBaseMetaTileEntity();
            mPosX=base.getXCoord();
            mPosY=base.getYCoord();
            mPosZ=base.getZCoord();
            mPosD=base.getWorld().provider.dimensionId;
        }
    }

    public static class ParametersTextData extends TextParametersMessage{
        public ParametersTextData() {}

        public ParametersTextData(GT_MetaTileEntity_Hatch_ParamText metaTile) {
            IGregTechTileEntity base=metaTile.getBaseMetaTileEntity();
            mPosX=base.getXCoord();
            mPosY=base.getYCoord();
            mPosZ=base.getZCoord();
            mPosD=base.getWorld().provider.dimensionId;
            mVal0 =metaTile.value0s;
            mVal1 =metaTile.value1s;
        }
    }

    public static class ParametersTextUpdate extends TextParametersMessage{
        public ParametersTextUpdate() {}

        public ParametersTextUpdate(GT_MetaTileEntity_Hatch_ParamText metaTile) {
            IGregTechTileEntity base=metaTile.getBaseMetaTileEntity();
            mPosX=base.getXCoord();
            mPosY=base.getYCoord();
            mPosZ=base.getZCoord();
            mPosD=base.getWorld().provider.dimensionId;
            mVal0 =metaTile.value0s;
            mVal1 =metaTile.value1s;
        }
    }

    public static class ClientHandler extends AbstractClientMessageHandler<ParametersTextData> {
        @Override
        public IMessage handleClientMessage(EntityPlayer pPlayer, ParametersTextData pMessage, MessageContext pCtx) {
            if(pPlayer.worldObj.provider.dimensionId==pMessage.mPosD){
                TileEntity te=pPlayer.worldObj.getTileEntity(pMessage.mPosX,pMessage.mPosY,pMessage.mPosZ);
                if(te instanceof IGregTechTileEntity){
                    IMetaTileEntity meta = ((IGregTechTileEntity) te).getMetaTileEntity();
                    if(meta instanceof GT_MetaTileEntity_Hatch_ParamText){
                        ((GT_MetaTileEntity_Hatch_ParamText) meta).value0s =pMessage.mVal0;
                        ((GT_MetaTileEntity_Hatch_ParamText) meta).value1s =pMessage.mVal1;
                        if(Minecraft.getMinecraft().currentScreen instanceof GT_GUIContainer_ParamText){
                            GT_GUIContainer_ParamText gui=((GT_GUIContainer_ParamText) Minecraft.getMinecraft().currentScreen);
                            if(gui.mContainer==meta){
                                gui.setTextIn0(pMessage.mVal0);
                                gui.setTextIn1(pMessage.mVal1);
                            }
                        }
                    }
                }
            }
            return null;
        }
    }

    public static class ServerHandler extends AbstractServerMessageHandler<ParametersTextQuery> {
        @Override
        public IMessage handleServerMessage(EntityPlayer pPlayer, ParametersTextQuery pMessage, MessageContext pCtx) {
            World world = DimensionManager.getWorld(pMessage.mPosD);
            if (world != null) {
                TileEntity te = world.getTileEntity(pMessage.mPosX, pMessage.mPosY, pMessage.mPosZ);
                if (te instanceof IGregTechTileEntity) {
                    IMetaTileEntity meta = ((IGregTechTileEntity) te).getMetaTileEntity();
                    if (meta instanceof GT_MetaTileEntity_Hatch_ParamText) {
                        return new ParametersTextData((GT_MetaTileEntity_Hatch_ParamText) meta);
                    }
                }
            }
            return null;
        }
    }

    public static class ServerUpdateHandler extends AbstractServerMessageHandler<ParametersTextUpdate> {
        @Override
        public IMessage handleServerMessage(EntityPlayer pPlayer, ParametersTextUpdate pMessage, MessageContext pCtx) {
            World world = DimensionManager.getWorld(pMessage.mPosD);
            if(world!=null){
                TileEntity te=world.getTileEntity(pMessage.mPosX,pMessage.mPosY,pMessage.mPosZ);
                if(te instanceof IGregTechTileEntity){
                    IMetaTileEntity meta = ((IGregTechTileEntity) te).getMetaTileEntity();
                    if(meta instanceof GT_MetaTileEntity_Hatch_ParamText){
                        ((GT_MetaTileEntity_Hatch_ParamText) meta).value0s =pMessage.mVal0;
                        ((GT_MetaTileEntity_Hatch_ParamText) meta).value1s =pMessage.mVal1;
                        ((GT_MetaTileEntity_Hatch_ParamText) meta).value0D=Util.getValue(pMessage.mVal0);
                        ((GT_MetaTileEntity_Hatch_ParamText) meta).value1D=Util.getValue(pMessage.mVal1);
                    }
                }
            }
            return null;
        }
    }
}
