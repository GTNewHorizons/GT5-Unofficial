package gregtech.api.net.cape;

import java.util.Collection;
import java.util.HashSet;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.IBlockAccess;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.ByteBufUtils;
import gregtech.api.enums.GTValues;
import gregtech.api.net.GTPacket;
import gregtech.api.net.GTPacketTypes;
import gregtech.client.GTCapesClientHandler;
import gregtech.common.GTCapesLoader;
import io.netty.buffer.ByteBuf;

/**
 * Sent from the client to the server (capes == null) and back (capes != null)
 */
@ParametersAreNonnullByDefault
public class GTPacketListCapes extends GTPacket {

    private Collection<String> capes;
    private EntityPlayerMP player;

    public GTPacketListCapes() {}

    public GTPacketListCapes(Collection<String> capes) {
        this.capes = capes;
    }

    @Override
    public byte getPacketID() {
        return GTPacketTypes.LIST_CAPES.id;
    }

    @Override
    public void encode(ByteBuf buffer) {
        if (this.capes == null) {
            buffer.writeInt(-1);
            return;
        }
        buffer.writeInt(this.capes.size());
        for (String cape : this.capes) {
            ByteBufUtils.writeUTF8String(buffer, cape);
        }
    }

    @Override
    public GTPacket decode(ByteArrayDataInput buffer) {
        int len = buffer.readInt();
        if (len == -1) {
            return new GTPacketListCapes();
        }
        Collection<String> capes = new HashSet<>(len);
        for (int i = 0; i < len; i++) {
            capes.add(readUTF8String(buffer));
        }
        return new GTPacketListCapes(capes);
    }

    @Override
    public void process(IBlockAccess world) {
        if (this.capes == null) {
            this.capes = GTCapesLoader.getAvailableCapes(this.player);
            GTValues.NW.sendToPlayer(this, this.player);
            return;
        }
        GTCapesClientHandler.printCapes(this.capes);
    }

    @Override
    public void setINetHandler(INetHandler handler) {
        if (handler instanceof NetHandlerPlayServer handlerPlayServer) {
            this.player = handlerPlayServer.playerEntity;
        }
    }
}
