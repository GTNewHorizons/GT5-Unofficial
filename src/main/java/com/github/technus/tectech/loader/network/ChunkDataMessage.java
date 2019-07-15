package com.github.technus.tectech.loader.network;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.Util;
import com.github.technus.tectech.chunkData.IChunkMetaDataHandler;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import eu.usrv.yamcore.network.client.AbstractClientMessageHandler;
import eu.usrv.yamcore.network.server.AbstractServerMessageHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraftforge.event.world.ChunkEvent;

public class ChunkDataMessage implements IMessage {
    int worldId;
    ChunkCoordIntPair chunk;
    NBTTagCompound data;
    IChunkMetaDataHandler handler;

    public ChunkDataMessage(){}

    @Override
    public void fromBytes(ByteBuf pBuffer) {
        NBTTagCompound tag = ByteBufUtils.readTag(pBuffer);
        worldId = tag.getInteger("wId");
        chunk=new ChunkCoordIntPair(
                tag.getInteger("posx"),
                tag.getInteger("posz"));
        handler = TecTech.chunkDataHandler.getChunkMetaDataHandler(
                tag.getString("handle"));
        if(tag.hasKey("data")){
            data=tag.getCompoundTag("data");
        }
    }

    @Override
    public void toBytes(ByteBuf pBuffer) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("wId",worldId);
        tag.setInteger("posx",chunk.chunkXPos);
        tag.setInteger("posz",chunk.chunkZPos);
        tag.setString("handle",handler.getTagName());
        if(data!=null){
            tag.setTag("data",data);
        }
        ByteBufUtils.writeTag(pBuffer, tag);
    }

    public static class ChunkDataQuery extends ChunkDataMessage {
        public ChunkDataQuery() {
        }
        public ChunkDataQuery(ChunkEvent.Load aEvent, IChunkMetaDataHandler handler) {
            worldId=aEvent.world.provider.dimensionId;
            chunk=aEvent.getChunk().getChunkCoordIntPair();
            this.handler=handler;
        }
    }

    public static class ChunkDataData extends ChunkDataMessage {
        public ChunkDataData() {
        }

        public ChunkDataData(int worldId, ChunkCoordIntPair chunk, IChunkMetaDataHandler handler){
            this.worldId=worldId;
            this.chunk=chunk;
            this.handler=handler;
            this.data=TecTech.chunkDataHandler.getChunkData(handler,worldId,chunk);
        }

        public ChunkDataData(ChunkDataQuery query){
            worldId=query.worldId;
            chunk=query.chunk;
            handler=query.handler;
            data=TecTech.chunkDataHandler.getChunkData(handler,worldId,chunk);
        }
    }

    public static class ClientHandler extends AbstractClientMessageHandler<ChunkDataData> {
        @Override
        public IMessage handleClientMessage(EntityPlayer pPlayer, ChunkDataData pMessage, MessageContext pCtx) {
            if(Util.checkChunkExist(pPlayer.worldObj,pMessage.chunk)){
                TecTech.chunkDataHandler.putChunkData(pMessage.handler, pMessage.worldId,pMessage.chunk, pMessage.data);
            }
            return null;
        }
    }

    public static class ServerHandler extends AbstractServerMessageHandler<ChunkDataQuery> {
        @Override
        public IMessage handleServerMessage(EntityPlayer pPlayer, ChunkDataQuery pMessage, MessageContext pCtx) {
            if(pPlayer instanceof EntityPlayerMP){
                NetworkDispatcher.INSTANCE.sendTo(new ChunkDataData(pMessage),(EntityPlayerMP) pPlayer);
            }
            return null;
        }
    }
}
