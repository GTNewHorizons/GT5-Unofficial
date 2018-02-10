package com.github.technus.tectech.thing.metaTileEntity.multi.base.network;

import com.github.technus.tectech.thing.metaTileEntity.IFrontRotation;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import eu.usrv.yamcore.network.client.AbstractClientMessageHandler;
import eu.usrv.yamcore.network.server.AbstractServerMessageHandler;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class RotationMessage implements IMessage {
    protected int mPosX;
    protected int mPosY;
    protected int mPosZ;
    protected int mPosD;
    protected int mRotF;

    public RotationMessage() {
    }

    private RotationMessage(GT_MetaTileEntity_MultiblockBase_EM metaTile) {
        IGregTechTileEntity base=metaTile.getBaseMetaTileEntity();
        mPosX=base.getXCoord();
        mPosY=base.getYCoord();
        mPosZ=base.getZCoord();
        mPosD=base.getWorld().provider.dimensionId;
        mRotF=metaTile.getFrontRotation();
    }

    private RotationMessage(World world, int x,int y,int z, IFrontRotation front) {
        mPosX=x;
        mPosY=y;
        mPosZ=z;
        mPosD=world.provider.dimensionId;
        mRotF=front.getFrontRotation();
    }

    @Override
    public void fromBytes(ByteBuf pBuffer) {
        NBTTagCompound tTag = ByteBufUtils.readTag(pBuffer);
        mPosX = tTag.getInteger("posx");
        mPosY = tTag.getInteger("posy");
        mPosZ = tTag.getInteger("posz");
        mPosD = tTag.getInteger("posd");
        mRotF = tTag.getInteger("rotf");
    }

    @Override
    public void toBytes(ByteBuf pBuffer) {
        NBTTagCompound tFXTag = new NBTTagCompound();
        tFXTag.setInteger("posx", mPosX);
        tFXTag.setInteger("posy", mPosY);
        tFXTag.setInteger("posz", mPosZ);
        tFXTag.setInteger("posd", mPosD);
        tFXTag.setInteger("rotf", mRotF);

        ByteBufUtils.writeTag(pBuffer, tFXTag);
    }

    public static class RotationQuery extends RotationMessage{
        public RotationQuery() {
        }

        public RotationQuery(GT_MetaTileEntity_MultiblockBase_EM metaTile) {
            super(metaTile);
        }

        public RotationQuery(World world, int x,int y,int z, IFrontRotation front) {
            super(world,x,y,z,front);
        }
    }

    public static class RotationData extends RotationMessage{
        public RotationData() {
        }

        private RotationData(RotationQuery query){
            mPosX=query.mPosX;
            mPosY=query.mPosY;
            mPosZ=query.mPosZ;
            mPosD=query.mPosD;
            mRotF=query.mRotF;
        }

        public RotationData(GT_MetaTileEntity_MultiblockBase_EM metaTile) {
            super(metaTile);
        }

        public RotationData(World world, int x,int y,int z, IFrontRotation front) {
            super(world,x,y,z,front);
        }
    }

    public static class ClientHandler extends AbstractClientMessageHandler<RotationData> {
        @Override
        public IMessage handleClientMessage(EntityPlayer pPlayer, RotationData pMessage, MessageContext pCtx) {
            if(pPlayer.worldObj.provider.dimensionId==pMessage.mPosD){
                TileEntity te=pPlayer.worldObj.getTileEntity(pMessage.mPosX,pMessage.mPosY,pMessage.mPosZ);
                if(te instanceof IGregTechTileEntity){
                    IMetaTileEntity meta = ((IGregTechTileEntity) te).getMetaTileEntity();
                    if(meta instanceof IFrontRotation){
                        ((IFrontRotation) meta).forceSetRotationDoRender((byte)pMessage.mRotF);
                    }
                }else if (te instanceof IFrontRotation){
                    ((IFrontRotation) te).forceSetRotationDoRender((byte)pMessage.mRotF);
                }
            }
            return null;
        }
    }

    public static class ServerHandler extends AbstractServerMessageHandler<RotationQuery> {
        @Override
        public IMessage handleServerMessage(EntityPlayer pPlayer, RotationQuery pMessage, MessageContext pCtx) {
            World world= DimensionManager.getWorld(pMessage.mPosD);
            if(world!=null) {
                TileEntity te = world.getTileEntity(pMessage.mPosX, pMessage.mPosY, pMessage.mPosZ);
                if (te instanceof IGregTechTileEntity) {
                    IMetaTileEntity meta = ((IGregTechTileEntity) te).getMetaTileEntity();
                    if (meta instanceof IFrontRotation) {
                        pMessage.mRotF=((IFrontRotation) meta).getFrontRotation();
                        return new RotationData(pMessage);
                    }
                } else if (te instanceof IFrontRotation) {
                    pMessage.mRotF=((IFrontRotation) te).getFrontRotation();
                    return new RotationData(pMessage);
                }
            }
            return null;
        }
    }
}