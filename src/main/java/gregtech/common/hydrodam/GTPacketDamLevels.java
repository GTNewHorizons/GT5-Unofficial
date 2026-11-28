package gregtech.common.hydrodam;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import org.jetbrains.annotations.NotNull;

import com.google.common.io.ByteArrayDataInput;

import gregtech.api.net.GTPacket;
import gregtech.api.net.GTPacketTypes;
import io.netty.buffer.ByteBuf;

/// Sends volume levels in one dimension to clients. A full update replaces every level the client knows for that
/// dimension.
public class GTPacketDamLevels extends GTPacket {

    /// One volume's level, plus the horizontal bounds of its top layer so the client knows what to re-render.
    public static final class Entry {

        public final int volumeId;
        public final DamLevel level;
        public final int minX, minZ, maxX, maxZ;

        public Entry(int volumeId, DamLevel level, int minX, int minZ, int maxX, int maxZ) {
            this.volumeId = volumeId;
            this.level = level;
            this.minX = minX;
            this.minZ = minZ;
            this.maxX = maxX;
            this.maxZ = maxZ;
        }
    }

    private int dimension;
    private boolean full;
    private List<Entry> entries;

    public GTPacketDamLevels() {}

    public GTPacketDamLevels(int dimension, boolean full, @NotNull List<Entry> entries) {
        this.dimension = dimension;
        this.full = full;
        this.entries = entries;
    }

    @Override
    public byte getPacketID() {
        return GTPacketTypes.DAM_LEVELS.id;
    }

    @Override
    public void encode(ByteBuf buffer) {
        buffer.writeInt(dimension);
        buffer.writeBoolean(full);
        buffer.writeInt(entries.size());

        for (Entry entry : entries) {
            buffer.writeShort(entry.volumeId);
            buffer.writeInt(entry.level.topY);
            buffer.writeFloat(entry.level.topLayerFill);
            buffer.writeInt(entry.minX);
            buffer.writeInt(entry.minZ);
            buffer.writeInt(entry.maxX);
            buffer.writeInt(entry.maxZ);
        }
    }

    @Override
    public GTPacket decode(ByteArrayDataInput buffer) {
        int dimension = buffer.readInt();
        boolean full = buffer.readBoolean();
        int count = buffer.readInt();

        List<Entry> entries = new ArrayList<>(count);

        for (int i = 0; i < count; i++) {
            int volumeId = buffer.readShort();
            DamLevel level = new DamLevel(buffer.readInt(), buffer.readFloat());

            entries.add(
                new Entry(volumeId, level, buffer.readInt(), buffer.readInt(), buffer.readInt(), buffer.readInt()));
        }

        return new GTPacketDamLevels(dimension, full, entries);
    }

    @Override
    public void process(IBlockAccess blockAccess) {
        if (full) ClientDamLevels.clear(dimension);

        World world = blockAccess instanceof World w && w.provider.dimensionId == dimension ? w : null;

        for (Entry entry : entries) {
            DamLevel previous = ClientDamLevels.set(dimension, entry.volumeId, entry.level);

            if (world == null || entry.level.equals(previous)) continue;

            DamLighting.relightChangedLayers(world, entry, previous);
            markForRerender(world, entry, previous);
        }
    }

    /// Re-renders the blocks around the top layer, and around the previous top layer if it moved.
    private static void markForRerender(World world, Entry entry, DamLevel previous) {
        int minY = entry.level.topY - 1;
        int maxY = entry.level.topY + 1;

        if (previous != null) {
            minY = Math.min(minY, previous.topY - 1);
            maxY = Math.max(maxY, previous.topY + 1);
        }

        world.markBlockRangeForRenderUpdate(entry.minX - 1, minY, entry.minZ - 1, entry.maxX + 1, maxY, entry.maxZ + 1);
    }
}
