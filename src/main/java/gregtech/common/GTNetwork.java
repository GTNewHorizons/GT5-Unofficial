package gregtech.common;

import static gregtech.GTMod.GT_FML_LOGGER;

import java.util.EnumMap;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import gregtech.api.enums.GTValues;
import gregtech.api.net.GTPacket;
import gregtech.api.net.GTPacketNew;
import gregtech.api.net.GTPacketTypes;
import gregtech.api.net.IGT_NetworkHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.MessageToMessageCodec;

@ChannelHandler.Sharable
@SuppressWarnings("deprecation")
public class GTNetwork extends MessageToMessageCodec<FMLProxyPacket, GTPacket> implements IGT_NetworkHandler {

    private final EnumMap<Side, FMLEmbeddedChannel> mChannel;
    private final GTPacket[] mSubChannels;

    public GTNetwork() {
        this("GregTech", GTPacketTypes.referencePackets());
    }

    public GTNetwork(String channelName, GTPacketNew... packetTypes) {
        this.mChannel = NetworkRegistry.INSTANCE.newChannel(channelName, this, new HandlerShared());
        final int lastPId = packetTypes[packetTypes.length - 1].getPacketID();
        this.mSubChannels = new GTPacket[lastPId + 1];
        for (GTPacket packetType : packetTypes) {
            final int pId = packetType.getPacketID();
            if (this.mSubChannels[pId] == null) this.mSubChannels[pId] = packetType;
            else throw new IllegalArgumentException("Duplicate Packet ID! " + pId);
        }
    }

    @Override
    protected void encode(ChannelHandlerContext aContext, GTPacket aPacket, List<Object> aOutput) {
        final ByteBuf tBuf = Unpooled.buffer()
            .writeByte(aPacket.getPacketID());
        aPacket.encode(tBuf);
        aOutput.add(
            new FMLProxyPacket(
                tBuf,
                aContext.channel()
                    .attr(NetworkRegistry.FML_CHANNEL)
                    .get()));
    }

    @Override
    protected void decode(ChannelHandlerContext aContext, FMLProxyPacket aPacket, List<Object> aOutput) {
        final ByteArrayDataInput aData = ByteStreams.newDataInput(
            aPacket.payload()
                .array());
        final GTPacket tPacket = this.mSubChannels[aData.readByte()].decode(aData);
        tPacket.setINetHandler(aPacket.handler());
        aOutput.add(tPacket);
    }

    @Override
    public void sendToPlayer(GTPacket aPacket, EntityPlayerMP aPlayer) {
        if (aPacket == null) {
            GT_FML_LOGGER.info("packet null");
            return;
        }
        if (aPlayer == null) {
            GT_FML_LOGGER.info("player null");
            return;
        }
        this.mChannel.get(Side.SERVER)
            .attr(FMLOutboundHandler.FML_MESSAGETARGET)
            .set(FMLOutboundHandler.OutboundTarget.PLAYER);
        this.mChannel.get(Side.SERVER)
            .attr(FMLOutboundHandler.FML_MESSAGETARGETARGS)
            .set(aPlayer);
        this.mChannel.get(Side.SERVER)
            .writeAndFlush(aPacket);
    }

    @Override
    public void sendToAllAround(GTPacket aPacket, NetworkRegistry.TargetPoint aPosition) {
        this.mChannel.get(Side.SERVER)
            .attr(FMLOutboundHandler.FML_MESSAGETARGET)
            .set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
        this.mChannel.get(Side.SERVER)
            .attr(FMLOutboundHandler.FML_MESSAGETARGETARGS)
            .set(aPosition);
        this.mChannel.get(Side.SERVER)
            .writeAndFlush(aPacket);
    }

    @Override
    public void sendToAll(GTPacket aPacket) {
        this.mChannel.get(Side.SERVER)
            .attr(FMLOutboundHandler.FML_MESSAGETARGET)
            .set(FMLOutboundHandler.OutboundTarget.ALL);
        this.mChannel.get(Side.SERVER)
            .writeAndFlush(aPacket);
    }

    @Override
    public void sendToServer(GTPacket aPacket) {
        this.mChannel.get(Side.CLIENT)
            .attr(FMLOutboundHandler.FML_MESSAGETARGET)
            .set(FMLOutboundHandler.OutboundTarget.TOSERVER);
        this.mChannel.get(Side.CLIENT)
            .writeAndFlush(aPacket);
    }

    @Override
    public void sendPacketToAllPlayersInRange(World aWorld, GTPacket aPacket, int aX, int aZ) {
        if (!aWorld.isRemote) {
            for (Object tObject : aWorld.playerEntities) {
                if (!(tObject instanceof EntityPlayerMP tPlayer)) {
                    break;
                }
                Chunk tChunk = aWorld.getChunkFromBlockCoords(aX, aZ);
                if (tPlayer.getServerForPlayer()
                    .getPlayerManager()
                    .isPlayerWatchingChunk(tPlayer, tChunk.xPosition, tChunk.zPosition)) {
                    sendToPlayer(aPacket, tPlayer);
                }
            }
        }
    }

    @ChannelHandler.Sharable
    static final class HandlerShared extends SimpleChannelInboundHandler<GTPacket> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, GTPacket aPacket) {
            final EntityPlayer aPlayer = GTValues.GT.getThePlayer();
            aPacket.process(aPlayer == null ? null : aPlayer.worldObj);
        }
    }
}
