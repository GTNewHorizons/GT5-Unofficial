package gregtech.api.net;

import static gregtech.api.enums.GTValues.NW;
import static gregtech.api.net.GTPacketTypes.LINK_GOGGLES;

import java.math.BigInteger;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.IBlockAccess;

import com.google.common.io.ByteArrayDataInput;

import appeng.api.util.DimensionalCoord;
import gregtech.common.handlers.PowerGogglesEventHandler;
import gregtech.common.misc.WirelessNetworkManager;
import io.netty.buffer.ByteBuf;

public class GTPacketLinkGoggles extends GTPacket {

    private DimensionalCoord coords;
    private long EU;
    private EntityPlayerMP player;

    public GTPacketLinkGoggles() {}

    public GTPacketLinkGoggles(DimensionalCoord coords, long EU) {
        this.coords = coords;
        this.EU = EU;
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

        buffer.writeLong(this.EU);
    }

    @Override
    public GTPacket decode(ByteArrayDataInput buffer) {
        if (buffer.readBoolean()) return new GTPacketLinkGoggles(null, 0);
        DimensionalCoord coords = new DimensionalCoord(
            buffer.readInt(),
            buffer.readInt(),
            buffer.readInt(),
            buffer.readInt());
        long EU = buffer.readLong();
        return new GTPacketLinkGoggles(coords, EU);
    }

    @Override
    public void setINetHandler(INetHandler aHandler) {
        if (aHandler instanceof NetHandlerPlayServer) {
            player = ((NetHandlerPlayServer) aHandler).playerEntity;
        }
    }

    @Override
    public void process(IBlockAccess world) {

        PowerGogglesEventHandler.lscLink = this.coords;
        if (this.coords == null) {
            NW.sendToPlayer(
                new GTPacketUpdatePowerGoggles(WirelessNetworkManager.getUserEU(player.getUniqueID()), true),
                player);
        } else {
            NW.sendToPlayer(new GTPacketUpdatePowerGoggles(BigInteger.valueOf(this.EU), true), player);
        }

    }
}
