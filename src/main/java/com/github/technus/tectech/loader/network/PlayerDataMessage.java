package com.github.technus.tectech.loader.network;

import com.github.technus.tectech.TecTech;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import eu.usrv.yamcore.network.client.AbstractClientMessageHandler;
import eu.usrv.yamcore.network.server.AbstractServerMessageHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

import java.util.UUID;

import static java.nio.charset.Charset.forName;

public class PlayerDataMessage implements IMessage {
    NBTTagCompound data;
    UUID uuid1,uuid2;

    public PlayerDataMessage(){}

    @Override
    public void fromBytes(ByteBuf pBuffer) {
        NBTTagCompound tag = ByteBufUtils.readTag(pBuffer);
        uuid1=UUID.fromString(tag.getString("id1"));
        uuid2=UUID.fromString(tag.getString("id2"));
        if(tag.hasKey("data")){
            data=tag.getCompoundTag("data");
        }
    }

    @Override
    public void toBytes(ByteBuf pBuffer) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("id1",uuid1.toString());
        tag.setString("id2",uuid2.toString());
        if(data!=null){
            tag.setTag("data",data);
        }
        ByteBufUtils.writeTag(pBuffer, tag);
    }

    public static class PlayerDataQuery extends PlayerDataMessage {
        public PlayerDataQuery() {
        }

        public PlayerDataQuery(EntityPlayer player) {
            uuid1=player.getUniqueID();
            uuid2=UUID.nameUUIDFromBytes(player.getCommandSenderName().getBytes(forName("UTF-8")));
        }
    }

    public static class PlayerDataData extends PlayerDataMessage {
        public PlayerDataData() {
        }

        public PlayerDataData(EntityPlayer player){
            uuid1=player.getUniqueID();
            uuid2=UUID.nameUUIDFromBytes(player.getCommandSenderName().getBytes(forName("UTF-8")));
            data=TecTech.playerPersistence.getDataOrSetToNewTag(player);
        }

        public PlayerDataData(PlayerDataQuery query){
            uuid1=query.uuid1;
            uuid2=query.uuid2;
            data= TecTech.playerPersistence.getDataOrSetToNewTag(uuid1,uuid2);
        }
    }

    public static class ClientHandler extends AbstractClientMessageHandler<PlayerDataData> {
        @Override
        public IMessage handleClientMessage(EntityPlayer pPlayer, PlayerDataData pMessage, MessageContext pCtx) {
            TecTech.playerPersistence.putDataOrSetToNewTag(pMessage.uuid1,pMessage.uuid2,pMessage.data);
            return null;
        }
    }

    public static class ServerHandler extends AbstractServerMessageHandler<PlayerDataQuery> {
        @Override
        public IMessage handleServerMessage(EntityPlayer pPlayer, PlayerDataQuery pMessage, MessageContext pCtx) {
            if(pPlayer instanceof EntityPlayerMP){
                NetworkDispatcher.INSTANCE.sendTo(new PlayerDataData(pMessage),(EntityPlayerMP) pPlayer);
            }
            return null;
        }
    }
}
