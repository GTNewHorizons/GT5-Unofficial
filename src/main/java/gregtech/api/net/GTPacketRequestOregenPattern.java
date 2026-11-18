package gregtech.api.net;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.IBlockAccess;

import com.google.common.io.ByteArrayDataInput;

import gregtech.api.enums.GTValues;
import gregtech.common.GTWorldgenerator;
import io.netty.buffer.ByteBuf;

/**
 * Asks the server for the world's oregen pattern. The server pushes it on login, this is the client asking again once
 * it actually has a world, so a missed push does not leave consumers reading the default forever.
 */
public class GTPacketRequestOregenPattern extends GTPacket {

    private EntityPlayerMP player;

    public GTPacketRequestOregenPattern() {
        super();
    }

    @Override
    public byte getPacketID() {
        return GTPacketTypes.REQUEST_OREGEN_PATTERN.id;
    }

    @Override
    public void encode(ByteBuf buffer) {}

    @Override
    public GTPacket decode(ByteArrayDataInput buffer) {
        return new GTPacketRequestOregenPattern();
    }

    @Override
    public void setINetHandler(INetHandler handler) {
        if (handler instanceof NetHandlerPlayServer serverHandler) {
            player = serverHandler.playerEntity;
        }
    }

    @Override
    public void process(IBlockAccess world) {
        if (player != null) {
            GTValues.NW.sendToPlayer(
                new GTPacketSendOregenPattern(
                    GTWorldgenerator.getOregenPattern(),
                    GTWorldgenerator.getOregenPatternSource()),
                player);
        }
    }
}
