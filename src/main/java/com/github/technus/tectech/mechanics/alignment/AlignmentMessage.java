package com.github.technus.tectech.mechanics.alignment;

import com.github.technus.tectech.mechanics.alignment.enumerable.ExtendedFacing;
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

public class AlignmentMessage implements IMessage {
    int mPosX;
    int mPosY;
    int mPosZ;
    int mPosD;
    int mAlign;

    public AlignmentMessage() {
    }

    private AlignmentMessage(GT_MetaTileEntity_MultiblockBase_EM metaTile) {
        IGregTechTileEntity base=metaTile.getBaseMetaTileEntity();
        mPosX=base.getXCoord();
        mPosY=base.getYCoord();
        mPosZ=base.getZCoord();
        mPosD=base.getWorld().provider.dimensionId;
        mAlign =metaTile.getExtendedFacing().getIndex();
    }

    private AlignmentMessage(World world, int x, int y, int z, IAlignment front) {
        mPosX=x;
        mPosY=y;
        mPosZ=z;
        mPosD=world.provider.dimensionId;
        mAlign =front.getExtendedFacing().getIndex();
    }

    @Override
    public void fromBytes(ByteBuf pBuffer) {
        NBTTagCompound tTag = ByteBufUtils.readTag(pBuffer);
        mPosX = tTag.getInteger("posx");
        mPosY = tTag.getInteger("posy");
        mPosZ = tTag.getInteger("posz");
        mPosD = tTag.getInteger("posd");
        mAlign = tTag.getInteger("rotf");
    }

    @Override
    public void toBytes(ByteBuf pBuffer) {
        NBTTagCompound tFXTag = new NBTTagCompound();
        tFXTag.setInteger("posx", mPosX);
        tFXTag.setInteger("posy", mPosY);
        tFXTag.setInteger("posz", mPosZ);
        tFXTag.setInteger("posd", mPosD);
        tFXTag.setInteger("rotf", mAlign);

        ByteBufUtils.writeTag(pBuffer, tFXTag);
    }

    public static class AlignmentQuery extends AlignmentMessage {
        public AlignmentQuery() {
        }

        public AlignmentQuery(GT_MetaTileEntity_MultiblockBase_EM metaTile) {
            super(metaTile);
        }

        public AlignmentQuery(World world, int x, int y, int z, IAlignment front) {
            super(world,x,y,z,front);
        }
    }

    public static class AlignmentData extends AlignmentMessage {
        public AlignmentData() {
        }

        private AlignmentData(AlignmentQuery query){
            mPosX=query.mPosX;
            mPosY=query.mPosY;
            mPosZ=query.mPosZ;
            mPosD=query.mPosD;
            mAlign =query.mAlign;
        }

        public AlignmentData(GT_MetaTileEntity_MultiblockBase_EM metaTile) {
            super(metaTile);
        }

        public AlignmentData(World world, int x, int y, int z, IAlignment front) {
            super(world,x,y,z,front);
        }
    }

    public static class ClientHandler extends AbstractClientMessageHandler<AlignmentData> {
        @Override
        public IMessage handleClientMessage(EntityPlayer pPlayer, AlignmentData pMessage, MessageContext pCtx) {
            if(pPlayer.worldObj.provider.dimensionId==pMessage.mPosD){
                TileEntity te=pPlayer.worldObj.getTileEntity(pMessage.mPosX,pMessage.mPosY,pMessage.mPosZ);
                if(te instanceof IGregTechTileEntity){
                    IMetaTileEntity meta = ((IGregTechTileEntity) te).getMetaTileEntity();
                    if(meta instanceof IAlignment){
                        ((IAlignment) meta).setExtendedFacing(ExtendedFacing.byIndex(pMessage.mAlign));
                    }
                }else if (te instanceof IAlignment){
                    ((IAlignment) te).setExtendedFacing(ExtendedFacing.byIndex(pMessage.mAlign));
                }
            }
            return null;
        }
    }

    public static class ServerHandler extends AbstractServerMessageHandler<AlignmentQuery> {
        @Override
        public IMessage handleServerMessage(EntityPlayer pPlayer, AlignmentQuery pMessage, MessageContext pCtx) {
            World world= DimensionManager.getWorld(pMessage.mPosD);
            if(world!=null) {
                TileEntity te = world.getTileEntity(pMessage.mPosX, pMessage.mPosY, pMessage.mPosZ);
                if (te instanceof IGregTechTileEntity) {
                    IMetaTileEntity meta = ((IGregTechTileEntity) te).getMetaTileEntity();
                    if (meta instanceof IAlignment) {
                        pMessage.mAlign =((IAlignment) meta).getExtendedFacing().getIndex();
                        return new AlignmentData(pMessage);
                    }
                } else if (te instanceof IAlignment) {
                    pMessage.mAlign =((IAlignment) te).getExtendedFacing().getIndex();
                    return new AlignmentData(pMessage);
                }
            }
            return null;
        }
    }
}