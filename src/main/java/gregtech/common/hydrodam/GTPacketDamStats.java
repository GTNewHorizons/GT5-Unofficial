package gregtech.common.hydrodam;

import net.minecraft.world.IBlockAccess;

import com.google.common.io.ByteArrayDataInput;

import gregtech.api.net.GTPacket;
import gregtech.api.net.GTPacketTypes;
import io.netty.buffer.ByteBuf;

/// A volume's litres and net change, sent in reply to {@link GTPacketDamStatsRequest}.
public class GTPacketDamStats extends GTPacket {

    private ClientDamStats.Stats stats;

    public GTPacketDamStats() {}

    public GTPacketDamStats(int dimension, int volumeId, double litres, double netLitresPerTick) {
        this.stats = new ClientDamStats.Stats(dimension, volumeId, litres, netLitresPerTick);
    }

    @Override
    public byte getPacketID() {
        return GTPacketTypes.DAM_STATS.id;
    }

    @Override
    public void encode(ByteBuf buffer) {
        buffer.writeInt(stats.dimension);
        buffer.writeShort(stats.volumeId);
        buffer.writeDouble(stats.litres);
        buffer.writeDouble(stats.netLitresPerTick);
    }

    @Override
    public GTPacket decode(ByteArrayDataInput buffer) {
        return new GTPacketDamStats(buffer.readInt(), buffer.readShort(), buffer.readDouble(), buffer.readDouble());
    }

    @Override
    public void process(IBlockAccess world) {
        ClientDamStats.set(stats);
    }
}
