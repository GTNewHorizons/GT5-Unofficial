package gregtech.api.net.cape;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.IBlockAccess;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.ByteBufUtils;
import gregtech.api.enums.GTValues;
import gregtech.api.net.GTPacket;
import gregtech.api.net.GTPacketTypes;
import gregtech.common.GTCapesLoader;
import io.netty.buffer.ByteBuf;

/**
 * Sent from client to server, informing it about the player's cape choice. The choice is verified.
 */
@ParametersAreNonnullByDefault
public class GTPacketSetCape extends GTPacket {

    private String cape;
    private EntityPlayerMP player;

    public GTPacketSetCape() {}

    public GTPacketSetCape(String cape) {
        this.cape = cape;
    }

    @Override
    public byte getPacketID() {
        return GTPacketTypes.SET_CAPE.id;
    }

    @Override
    public void encode(ByteBuf buffer) {
        ByteBufUtils.writeUTF8String(buffer, this.cape);
    }

    @Override
    public GTPacket decode(ByteArrayDataInput buffer) {
        return new GTPacketSetCape(readUTF8String(buffer));
    }

    // executed on the server
    @Override
    public void process(IBlockAccess world) {
        if (!GTCapesLoader.getAvailableCapes(this.player)
            .contains(this.cape)) {
            this.player.addChatMessage(
                new ChatComponentText(EnumChatFormatting.RED + "You do not own \"" + this.cape + "\"!"));
            return;
        }

        GTCapesLoader.setSelectedCape(this.player, this.cape);
        GTValues.NW.sendToAll(new GTPacketBroadcastCapes(this.player.getUniqueID(), this.cape));
    }

    @Override
    public void setINetHandler(INetHandler handler) {
        if (handler instanceof NetHandlerPlayServer handlerPlayServer) {
            this.player = handlerPlayServer.playerEntity;
        }
    }
}
