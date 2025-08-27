package gregtech.api.net;

import static gregtech.api.net.GTPacketTypes.LINK_GOGGLES;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.IBlockAccess;

import com.google.common.io.ByteArrayDataInput;

import appeng.api.util.DimensionalCoord;
import gregtech.common.powergoggles.handlers.PowerGogglesEventHandler;
import io.netty.buffer.ByteBuf;

public class GTPacketLinkPowerGoggles extends GTPacket {

    private DimensionalCoord coords = null;
    private EntityPlayerMP player;
    private final PowerGogglesEventHandler EVENT_HANDLER = PowerGogglesEventHandler.getInstance();

    public GTPacketLinkPowerGoggles() {}

    public GTPacketLinkPowerGoggles(DimensionalCoord coords) {
        this.coords = coords;
    }

    @Override
    public byte getPacketID() {
        return LINK_GOGGLES.id;
    }

    @Override
    public void encode(ByteBuf buffer) {
        buffer.writeBoolean(this.coords == null);
        if (this.coords == null) return;
        buffer.writeInt(this.coords.x);
        buffer.writeInt(this.coords.y);
        buffer.writeInt(this.coords.z);
        buffer.writeInt(this.coords.getDimension());
    }

    @Override
    public GTPacket decode(ByteArrayDataInput buffer) {
        if (buffer.readBoolean()) return new GTPacketLinkPowerGoggles(null);
        DimensionalCoord coords = new DimensionalCoord(
            buffer.readInt(),
            buffer.readInt(),
            buffer.readInt(),
            buffer.readInt());
        return new GTPacketLinkPowerGoggles(coords);
    }

    @Override
    public void setINetHandler(INetHandler aHandler) {
        if (aHandler instanceof NetHandlerPlayServer) {
            player = ((NetHandlerPlayServer) aHandler).playerEntity;
        }
    }

    @Override
    public void process(IBlockAccess world) {
        // EVENT_HANDLER.setLscLink(player, this.coords);
        // EVENT_HANDLER.forceUpdate = true;
        // EVENT_HANDLER.forceRefresh = true;
    }
}
