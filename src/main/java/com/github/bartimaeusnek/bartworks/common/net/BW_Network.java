/*
 * Copyright (c) 2018-2020 bartimaeusnek
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.bartimaeusnek.bartworks.common.net;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.server.FMLServerHandler;
import gregtech.api.enums.GT_Values;
import gregtech.api.net.GT_Packet;
import gregtech.api.net.IGT_NetworkHandler;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.MessageToMessageCodec;
import java.util.EnumMap;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

/*
 * Original GT File slightly Modified
 */
@SuppressWarnings("ALL")
@ChannelHandler.Sharable
public class BW_Network extends MessageToMessageCodec<FMLProxyPacket, GT_Packet> implements IGT_NetworkHandler {

    private final EnumMap<Side, FMLEmbeddedChannel> mChannel;
    private final GT_Packet[] mSubChannels;

    public BW_Network() {
        this.mChannel = NetworkRegistry.INSTANCE.newChannel("BartWorks", this, new BW_Network.HandlerShared());
        this.mSubChannels = new GT_Packet[] {
            new RendererPacket(),
            new CircuitProgrammerPacket(),
            new MetaBlockPacket(),
            new OreDictCachePacket(),
            new ServerJoinedPackage(),
            new EICPacket()
        };
    }

    protected void encode(ChannelHandlerContext aContext, GT_Packet aPacket, List<Object> aOutput) throws Exception {
        aOutput.add(new FMLProxyPacket(
                Unpooled.buffer()
                        .writeByte(aPacket.getPacketID())
                        .writeBytes(aPacket.encode())
                        .copy(),
                (String) aContext.channel().attr(NetworkRegistry.FML_CHANNEL).get()));
    }

    protected void decode(ChannelHandlerContext aContext, FMLProxyPacket aPacket, List<Object> aOutput)
            throws Exception {
        ByteArrayDataInput aData = ByteStreams.newDataInput(aPacket.payload().array());
        aOutput.add(this.mSubChannels[aData.readByte()].decode(aData));
    }

    public void sendToPlayer(@Nonnull GT_Packet aPacket, @Nonnull EntityPlayerMP aPlayer) {
        ((FMLEmbeddedChannel) this.mChannel.get(Side.SERVER))
                .attr(FMLOutboundHandler.FML_MESSAGETARGET)
                .set(FMLOutboundHandler.OutboundTarget.PLAYER);
        ((FMLEmbeddedChannel) this.mChannel.get(Side.SERVER))
                .attr(FMLOutboundHandler.FML_MESSAGETARGETARGS)
                .set(aPlayer);
        ((FMLEmbeddedChannel) this.mChannel.get(Side.SERVER)).writeAndFlush(aPacket);
    }

    public void sendToAllPlayersinWorld(@Nonnull GT_Packet aPacket, World world) {
        for (String name : FMLServerHandler.instance().getServer().getAllUsernames()) {
            ((FMLEmbeddedChannel) this.mChannel.get(Side.SERVER))
                    .attr(FMLOutboundHandler.FML_MESSAGETARGET)
                    .set(FMLOutboundHandler.OutboundTarget.PLAYER);
            ((FMLEmbeddedChannel) this.mChannel.get(Side.SERVER))
                    .attr(FMLOutboundHandler.FML_MESSAGETARGETARGS)
                    .set(world.getPlayerEntityByName(name));
            ((FMLEmbeddedChannel) this.mChannel.get(Side.SERVER)).writeAndFlush(aPacket);
        }
    }

    public void sendToAllAround(@Nonnull GT_Packet aPacket, NetworkRegistry.TargetPoint aPosition) {
        ((FMLEmbeddedChannel) this.mChannel.get(Side.SERVER))
                .attr(FMLOutboundHandler.FML_MESSAGETARGET)
                .set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
        ((FMLEmbeddedChannel) this.mChannel.get(Side.SERVER))
                .attr(FMLOutboundHandler.FML_MESSAGETARGETARGS)
                .set(aPosition);
        ((FMLEmbeddedChannel) this.mChannel.get(Side.SERVER)).writeAndFlush(aPacket);
    }

    public void sendToServer(@Nonnull GT_Packet aPacket) {
        ((FMLEmbeddedChannel) this.mChannel.get(Side.CLIENT))
                .attr(FMLOutboundHandler.FML_MESSAGETARGET)
                .set(FMLOutboundHandler.OutboundTarget.TOSERVER);
        ((FMLEmbeddedChannel) this.mChannel.get(Side.CLIENT)).writeAndFlush(aPacket);
    }

    public void sendPacketToAllPlayersInRange(World aWorld, @Nonnull GT_Packet aPacket, int aX, int aZ) {
        if (!aWorld.isRemote) {

            for (Object tObject : aWorld.playerEntities) {
                if (!(tObject instanceof EntityPlayerMP)) {
                    break;
                }

                EntityPlayerMP tPlayer = (EntityPlayerMP) tObject;
                Chunk tChunk = aWorld.getChunkFromBlockCoords(aX, aZ);
                if (tPlayer.getServerForPlayer()
                        .getPlayerManager()
                        .isPlayerWatchingChunk(tPlayer, tChunk.xPosition, tChunk.zPosition)) {
                    this.sendToPlayer(aPacket, tPlayer);
                }
            }
        }
    }

    @Sharable
    static final class HandlerShared extends SimpleChannelInboundHandler<GT_Packet> {
        HandlerShared() {}

        protected void channelRead0(ChannelHandlerContext ctx, GT_Packet aPacket) throws Exception {
            EntityPlayer aPlayer = GT_Values.GT.getThePlayer();
            aPacket.process(aPlayer == null ? null : GT_Values.GT.getThePlayer().worldObj);
        }
    }
}
