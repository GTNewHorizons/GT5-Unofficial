package gregtech.api.net;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;
import com.gtnewhorizon.gtnhlib.util.CoordinatePacker;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;
import it.unimi.dsi.fastutil.shorts.ShortArrayList;
import it.unimi.dsi.fastutil.shorts.ShortList;

/**
 * Used to transfer Block Events in a much better fashion
 */
public class GTPacketBlockEvent extends GTPacket {

    private int worldId;
    private int eventCount;
    private LongList packedCoordinates;
    private ShortList idsAndValues;

    public GTPacketBlockEvent() {};

    public GTPacketBlockEvent(int worldID, int eventCount, LongList packedCoordinates, ShortList idsAndValues) {
        this.worldId = worldID;
        this.eventCount = eventCount;
        this.packedCoordinates = packedCoordinates;
        this.idsAndValues = idsAndValues;
    }

    @Override
    public void encode(ByteBuf buffer) {
        buffer.writeInt(worldId);
        buffer.writeInt(eventCount);
        for (int i = 0; i < eventCount; ++i) buffer.writeLong(packedCoordinates.getLong(i));
        for (int i = 0; i < eventCount; ++i) buffer.writeShort(idsAndValues.getShort(i));
    }

    @Override
    public GTPacket decode(ByteArrayDataInput buffer) {
        GTPacketBlockEvent packet = new GTPacketBlockEvent();
        packet.worldId = buffer.readInt();
        packet.eventCount = buffer.readInt();
        packet.packedCoordinates = new LongArrayList();
        packet.idsAndValues = new ShortArrayList();
        for (int i = 0; i < packet.eventCount; ++i) packet.packedCoordinates.add(buffer.readLong());
        for (int i = 0; i < packet.eventCount; ++i) packet.idsAndValues.add(buffer.readShort());
        return packet;
    }

    @Override
    public void process(IBlockAccess blockAccess) {
        if (!(blockAccess instanceof World world)) return;
        if (world.provider.dimensionId != worldId) return;
        for (int i = 0; i < eventCount; ++i) {
            final long packedCoordinate = packedCoordinates.getLong(i);
            final int x = CoordinatePacker.unpackX(packedCoordinate);
            final int y = CoordinatePacker.unpackY(packedCoordinate);
            final int z = CoordinatePacker.unpackZ(packedCoordinate);
            final TileEntity tileEntity = world.getTileEntity(x, y, z);
            if (tileEntity == null) continue;
            final short idAndValue = idsAndValues.getShort(i);
            final byte id = (byte) (idAndValue >> 8);
            final byte type = (byte) (idAndValue & 0xFF);
            tileEntity.receiveClientEvent(id, type);
        }
    }

    @Override
    public byte getPacketID() {
        return GTPacketTypes.BLOCK_EVENT.id;
    }
}
