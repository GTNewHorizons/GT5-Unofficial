package gregtech.common;

import static gregtech.GT_Mod.GT_FML_LOGGER;

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
import gregtech.api.enums.GT_Values;
import gregtech.api.net.*;
import gregtech.common.blocks.GT_Packet_Ores;
import gregtech.common.net.MessageSetFlaskCapacity;
import gregtech.common.net.MessageUpdateFluidDisplayItem;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.MessageToMessageCodec;

@ChannelHandler.Sharable
@SuppressWarnings("deprecation")
public class GT_Network extends MessageToMessageCodec<FMLProxyPacket, GT_Packet> implements IGT_NetworkHandler {

    private final EnumMap<Side, FMLEmbeddedChannel> mChannel;
    private final GT_Packet[] mSubChannels;

    public GT_Network() {
        this(
            "GregTech",
            new GT_Packet_TileEntity(), // 0
            new GT_Packet_Sound(), // 1
            new GT_Packet_Block_Event(), // 2
            new GT_Packet_Ores(), // 3
            new GT_Packet_Pollution(), // 4
            new MessageSetFlaskCapacity(), // 5
            new GT_Packet_TileEntityCover(), // 6
            new GT_Packet_TileEntityCoverGUI(), // 7
            new MessageUpdateFluidDisplayItem(), // 8
            new GT_Packet_ClientPreference(), // 9
            new GT_Packet_WirelessRedstoneCover(), // 10
            new GT_Packet_TileEntityCoverNew(), // 11
            new GT_Packet_SetConfigurationCircuit(), // 12
            new GT_Packet_UpdateItem(), // 13
            new GT_Packet_SetLockedFluid(), // 14
            new GT_Packet_GtTileEntityGuiRequest(), // 15
            new GT_Packet_SendCoverData(), // 16
            new GT_Packet_RequestCoverData(), // 17
            new GT_Packet_MultiTileEntity() // 18
        );
    }

    public GT_Network(String channelName, GT_Packet... packetTypes) {
        this.mChannel = NetworkRegistry.INSTANCE.newChannel(channelName, this, new HandlerShared());
        this.mSubChannels = new GT_Packet[packetTypes.length];
        for (GT_Packet packetType : packetTypes) {
            final int pId = packetType.getPacketID();
            if (this.mSubChannels[pId] == null) this.mSubChannels[pId] = packetType;
            else throw new IllegalArgumentException("Duplicate Packet ID! " + pId);
        }
    }

    @Override
    protected void encode(ChannelHandlerContext aContext, GT_Packet aPacket, List<Object> aOutput) throws Exception {
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
    protected void decode(ChannelHandlerContext aContext, FMLProxyPacket aPacket, List<Object> aOutput)
        throws Exception {
        final ByteArrayDataInput aData = ByteStreams.newDataInput(
            aPacket.payload()
                .array());
        final GT_Packet tPacket = this.mSubChannels[aData.readByte()].decode(aData);
        tPacket.setINetHandler(aPacket.handler());
        aOutput.add(tPacket);
    }

    @Override
    public void sendToPlayer(GT_Packet aPacket, EntityPlayerMP aPlayer) {
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
    public void sendToAllAround(GT_Packet aPacket, NetworkRegistry.TargetPoint aPosition) {
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
    public void sendToServer(GT_Packet aPacket) {
        this.mChannel.get(Side.CLIENT)
            .attr(FMLOutboundHandler.FML_MESSAGETARGET)
            .set(FMLOutboundHandler.OutboundTarget.TOSERVER);
        this.mChannel.get(Side.CLIENT)
            .writeAndFlush(aPacket);
    }

    @Override
    public void sendPacketToAllPlayersInRange(World aWorld, GT_Packet aPacket, int aX, int aZ) {
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
    static final class HandlerShared extends SimpleChannelInboundHandler<GT_Packet> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, GT_Packet aPacket) {
            final EntityPlayer aPlayer = GT_Values.GT.getThePlayer();
            aPacket.process(aPlayer == null ? null : aPlayer.worldObj);
        }
    }
}
