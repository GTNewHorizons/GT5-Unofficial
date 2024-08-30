package com.detrav.net;

import java.util.EnumMap;
import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;

import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.MessageToMessageCodec;

/**
 * Created by wital_000 on 20.03.2016.
 */
@ChannelHandler.Sharable
public class DetravNetwork extends MessageToMessageCodec<FMLProxyPacket, DetravPacket> {

    static public DetravNetwork INSTANCE;
    private final EnumMap<Side, FMLEmbeddedChannel> mChannel;

    public DetravNetwork() {
        INSTANCE = this;
        this.mChannel = NetworkRegistry.INSTANCE.newChannel("DetravScanner", this, new HandlerShared());
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, DetravPacket msg, List<Object> out) throws Exception {
        ByteBuf buf = Unpooled.buffer();
        buf.writeByte(msg.getPacketID());
        msg.encode(new ByteBufOutputStream(buf));
        out.add(
            new FMLProxyPacket(
                buf,
                ctx.channel()
                    .attr(NetworkRegistry.FML_CHANNEL)
                    .get()));
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, FMLProxyPacket msg, List<Object> out) throws Exception {
        ByteBuf payload = msg.payload();
        payload.readByte(); // Sub Channel - Ignore
        out.add(ProspectingPacket.decode(new ByteBufInputStream(payload)));
    }

    public void sendToPlayer(DetravPacket aPacket, EntityPlayerMP aPlayer) {
        this.mChannel.get(Side.SERVER)
            .attr(FMLOutboundHandler.FML_MESSAGETARGET)
            .set(FMLOutboundHandler.OutboundTarget.PLAYER);
        this.mChannel.get(Side.SERVER)
            .attr(FMLOutboundHandler.FML_MESSAGETARGETARGS)
            .set(aPlayer);
        this.mChannel.get(Side.SERVER)
            .writeAndFlush(aPacket);
    }

    public void sendToServer(DetravPacket aPacket) {
        this.mChannel.get(Side.CLIENT)
            .attr(FMLOutboundHandler.FML_MESSAGETARGET)
            .set(FMLOutboundHandler.OutboundTarget.TOSERVER);
        this.mChannel.get(Side.CLIENT)
            .writeAndFlush(aPacket);
    }

    @ChannelHandler.Sharable
    static final class HandlerShared extends SimpleChannelInboundHandler<DetravPacket> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, DetravPacket aPacket) {
            aPacket.process();
        }
    }
}
