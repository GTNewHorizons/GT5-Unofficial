package com.github.technus.tectech.mechanics.enderStorage;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import eu.usrv.yamcore.network.client.AbstractClientMessageHandler;
import eu.usrv.yamcore.network.server.AbstractServerMessageHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fluids.IFluidHandler;

import java.io.*;
import java.util.Arrays;

import static com.github.technus.tectech.mechanics.enderStorage.EnderWorldSavedData.bindEnderLinkTag;
import static com.github.technus.tectech.mechanics.enderStorage.EnderWorldSavedData.getEnderLinkTag;
import static com.github.technus.tectech.thing.cover.GT_Cover_TM_EnderFluidLink.setEnderLinkTag;

public class EnderLinkCoverMessage implements IMessage {
    EnderLinkTankWithTag messageData;

    public EnderLinkCoverMessage() {
    }

    @Override
    public void fromBytes(ByteBuf pBuffer) {
        try {
            //I'd love to know why I need to offset by one byte for this to work
            byte[] boop = pBuffer.array();
            boop = Arrays.copyOfRange(boop, 1, boop.length);
            InputStream is = new ByteArrayInputStream(boop);
            ObjectInputStream ois = new ObjectInputStream(is);
            Object data = ois.readObject();
            messageData = (EnderLinkTankWithTag) data;
        } catch (Exception ignore) {
        }
    }

    @Override
    public void toBytes(ByteBuf pBuffer) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(messageData);
            oos.flush();
            InputStream is = new ByteArrayInputStream(baos.toByteArray());
            pBuffer.writeBytes(is, baos.toByteArray().length);
        } catch (Exception ignore) {
        }
    }

    public static class EnderLinkCoverQuery extends EnderLinkCoverMessage {
        public EnderLinkCoverQuery() {
        }

        public EnderLinkCoverQuery(EnderLinkTag tag, IFluidHandler fluidHandler) {
            messageData = new EnderLinkTankWithTag(tag, fluidHandler);
        }
    }

    public static class EnderLinkCoverUpdate extends EnderLinkCoverMessage {
        public EnderLinkCoverUpdate() {
        }

        public EnderLinkCoverUpdate(EnderLinkTag tag, IFluidHandler fluidHandler) {
            messageData = new EnderLinkTankWithTag(tag, fluidHandler);
        }
    }

    public static class EnderLinkCoverData extends EnderLinkCoverMessage {
        public EnderLinkCoverData() {
        }

        public EnderLinkCoverData(EnderLinkTag tag, IFluidHandler fluidHandler) {
            messageData = new EnderLinkTankWithTag(tag, fluidHandler);
        }
    }

    public static class ServerHandler extends AbstractServerMessageHandler<EnderLinkCoverQuery> {
        @Override
        public IMessage handleServerMessage(EntityPlayer pPlayer, EnderLinkCoverQuery pMessage, MessageContext pCtx) {
            IMessage reply = null;
            if (pMessage.messageData != null) {
                reply = new EnderLinkCoverData(getEnderLinkTag(pMessage.messageData.getFluidHandler()),
                        pMessage.messageData.getFluidHandler());
            }
            return reply;
        }
    }

    public static class ServerUpdateHandler extends AbstractServerMessageHandler<EnderLinkCoverUpdate> {
        @Override
        public IMessage handleServerMessage(EntityPlayer pPlayer, EnderLinkCoverUpdate pMessage, MessageContext pCtx) {
            if (pMessage.messageData != null) {
                bindEnderLinkTag(pMessage.messageData.getFluidHandler(), pMessage.messageData.getTag());
            }
            return null;
        }
    }

    public static class ClientHandler extends AbstractClientMessageHandler<EnderLinkCoverData> {
        @Override
        public IMessage handleClientMessage(EntityPlayer pPlayer, EnderLinkCoverData pMessage, MessageContext pCtx) {
            if (pMessage.messageData != null) {
                setEnderLinkTag(pMessage.messageData.getTag());
            }
            return null;
        }
    }

    private static class EnderLinkTankWithTag extends EnderLinkTank {
        private final EnderLinkTag tag;

        public EnderLinkTankWithTag(EnderLinkTag tag, IFluidHandler fluidHandler) {
            super(fluidHandler);
            this.tag = tag;
        }

        public EnderLinkTag getTag() {
            return tag;
        }
    }
}